package com.lu.gademo.task;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lu.gademo.entity.ClassificationResult;
import com.lu.gademo.entity.ExcelParam;
import com.lu.gademo.entity.FileStorageDetails;
import com.lu.gademo.entity.dataplatform.SadaGdpiClickDtl;
import com.lu.gademo.service.*;
import com.lu.gademo.utils.Course2Communication;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

@Slf4j
@Component

@ConditionalOnProperty(name = "fetch.database.dataPlatformTask.enabled", havingValue = "true", matchIfMissing = false)
public class FetchDatabase {

    private final DataPlatformDesenService dataPlatformDesenService;
    private final FileService fileService;
    private final FileStorageService fileStorageService;
    private final ExcelParamService excelParamService;

    // 远程服务器信息
    private final String remoteFilePath;        // 远程文件路径
    private final Path localFilePath;           // 本地存储路径
    private final ObjectMapper objectMapper;

    private final TypeAlgoMappingDaoService algoMappingDaoService;
    private final Course2Communication course2Communication;

    @Autowired
    public FetchDatabase(DataPlatformDesenService dataPlatformDesenService,
                         FileService fileService, FileStorageService fileStorageService,
                         ExcelParamService excelParamService,
                         TypeAlgoMappingDaoService typeAlgoMappingDao,
                         Course2Communication course2Communication,
                         @Value("${fetch.database.dataPlatformTask.remoteConfigPath}") String remoteFilePath

                         ) {
        this.dataPlatformDesenService = dataPlatformDesenService;
        this.fileService = fileService;
        this.fileStorageService = fileStorageService;
        this.excelParamService = excelParamService;
        this.localFilePath = Paths.get("dataplatform_config.json");           // 本地存储路径
        this.objectMapper = new ObjectMapper();
        this.algoMappingDaoService = typeAlgoMappingDao;
        this.course2Communication = course2Communication;
        this.remoteFilePath = remoteFilePath;

    }

    @Scheduled(initialDelayString = "${fetch.database.task.initialDelay}", fixedRateString = "${fetch.database.task.fixedRate}")
    public boolean fetchDatabaseAndDesen() throws IOException, IllegalAccessException {
        String sheetName = "sada_gdpi_click_dtl";
        String mediumStrategy = sheetName + "_medium";
        Scanner scanner = new Scanner(System.in);
        Path tempFilePath = Paths.get(sheetName + "_temp.xlsx");

        log.info("定时任务：拉取电信数据平台数据库数据并脱敏");
        // 检查本地是否已经存在文件
        if (Files.exists(localFilePath)) {
            log.info("分类分级文件 {} 已存在", localFilePath);
        } else {
            // 使用 SCP 从远程服务器下载文件
            log.info("分类分级文件不存在，开始从课题二远程服务器下载分类分级文件...");
            course2Communication.downloadFileFromRemote(remoteFilePath, localFilePath);
        }

        System.out.println("Press Enter to continue...");
        scanner.nextLine();

        // 1. 从目标数据库获取数据
        List<SadaGdpiClickDtl> allRecordsByTableName = dataPlatformDesenService.getAllRecordsByTableName(sheetName);
        log.info("已从电信数据平台数据库获取数据");
        // 2. 对数据进行脱敏处理并写入文件
        dataPlatformDesenService.writeToExcel(allRecordsByTableName, dataPlatformDesenService.getColumnMapping(), tempFilePath);

        System.out.println("Press Enter to continue...");
        scanner.nextLine();

        FileStorageDetails fileStorageDetails2 = null;

        log.info("正在读取课程二脱敏策略");
        Map<String, Integer> courseTwoMap = readFromCourseTwo();

        List<ExcelParam> mediumStrategyConfig = excelParamService.getParamsByTableName(mediumStrategy + "_param");

        log.info("正在更新脱敏策略");
        updateExcelParam(courseTwoMap, mediumStrategyConfig);
        System.out.println("Press Enter to continue...");
        scanner.nextLine();

        String mediumStrategyConfigString = objectMapper.writeValueAsString(mediumStrategyConfig);

        try {
            fileStorageDetails2 = fileStorageService.saveRawFileWithDesenInfo(tempFilePath);
        } catch (IOException e) {
            if (Files.exists(tempFilePath)) {
                Files.delete(tempFilePath);
            }
            log.error("Failed to save raw file: {}", e.getMessage());
            return false;
        }
        try {
            log.info("正在使用脱敏策略进行脱敏");
            ResponseEntity<byte[]> mediumResponseEntity = fileService.dealExcel(fileStorageDetails2, mediumStrategyConfigString,
                    mediumStrategy, false);
            log.info("ResponseEntity Status Code: {}", mediumResponseEntity.getStatusCode());
            if (
                    mediumResponseEntity.getStatusCode() != HttpStatus.OK
            ) {
                return false;
            }
        } catch (Exception e) {
            log.error("Failed to desen excel: {}", e.getMessage());
            if (Files.exists(tempFilePath)) {
                Files.delete(tempFilePath);
            }
            return false;
        }
        if (Files.exists(tempFilePath)) {
            Files.delete(tempFilePath);
        }
        return true;
    }

    private Map<String, Integer> readFromCourseTwo() throws IOException {
        Path path = Paths.get("./dataplatform_config.json");
        JsonNode columnList = objectMapper.readTree(path.toFile()).get("columnList");
        List<ClassificationResult> courseTwoList = objectMapper.readValue(columnList.toString(),
                new TypeReference<List<ClassificationResult>>() {
                });
        for (ClassificationResult item : courseTwoList) {
            log.info("{} 对应的分类：{}, 可选算法：{}", "f_" + item.getColumnName(), item.getColumnType(),
                    algoMappingDaoService.getAlgNamesByTypeName(item.getColumnType()));
        }
        Map<String, Integer> courseTwoMap = new HashMap<>();

        for (ClassificationResult item : courseTwoList) {
            courseTwoMap.put("f_" + item.getColumnName(), item.getColumnLevel());
        }
        return courseTwoMap;
    }

    private void updateExcelParam(Map<String, Integer> courseTwoMap, List<ExcelParam> strategyConfig) {
        for (ExcelParam item : strategyConfig) {
            if (item.getColumnName().equals("sid")) {
                continue;
            }
            Integer courseTwoMapTmParam = courseTwoMap.get(item.getColumnName());
            log.info("分类分级结果 {} 对应脱敏等级：{}", item.getColumnName(), courseTwoMapTmParam);
            if (courseTwoMapTmParam == 4) {
                courseTwoMapTmParam = 3;
            }
            item.setTmParam(courseTwoMapTmParam > item.getTmParam() ? courseTwoMapTmParam : item.getTmParam());
        }
    }
}

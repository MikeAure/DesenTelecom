package com.lu.gademo.task;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lu.gademo.entity.ClassificationResult;
import com.lu.gademo.entity.ExcelParam;
import com.lu.gademo.entity.FileStorageDetails;
import com.lu.gademo.entity.dataplatform.SadaGdpiClickDtl;
import com.lu.gademo.service.ExcelParamService;
import com.lu.gademo.service.FileService;
import com.lu.gademo.service.impl.DataPlatformDesenServiceImpl;
import com.lu.gademo.service.impl.FileStorageService;
import com.lu.gademo.utils.LogCollectUtil;
import com.lu.gademo.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@ConditionalOnProperty(name = "fetch.database.task.enabled", havingValue = "true", matchIfMissing = false)
public class FetchDatabase {
    @Autowired
    private DataPlatformDesenServiceImpl dataPlatformDesenService;
    @Autowired
    private FileService fileService;
    @Autowired
    private FileStorageService fileStorageService;
    @Autowired
    private ExcelParamService excelParamService;
    @Autowired
    private LogCollectUtil logCollectUtil;

    private ObjectMapper objectMapper = new ObjectMapper();

//    @Scheduled(initialDelay = 5000, fixedRate = 180000)
    @Scheduled(initialDelayString = "${fetch.database.task.initialDelay}", fixedRateString = "${fetch.database.task.fixedRate}")
    public boolean fetchDatabaseAndDesen() throws IOException, IllegalAccessException {
        log.info("定时任务：拉取电信大数据平台数据并脱敏");
        String sheetName = "sada_gdpi_click_dtl";
        // 1. 从目标数据库获取数据
        List<SadaGdpiClickDtl> allRecordsByTableName = dataPlatformDesenService.getAllRecordsByTableName(sheetName);
        // 2. 对数据进行脱敏处理并写入文件
        Path tempFilePath = Paths.get(sheetName + "_temp.xlsx");
        dataPlatformDesenService.writeToExcel(allRecordsByTableName, dataPlatformDesenService.getColumnMapping(), tempFilePath);

        FileStorageDetails fileStorageDetails1 = null;
        FileStorageDetails fileStorageDetails2 = null;
        FileStorageDetails fileStorageDetails3 = null;
//        int strategyInt = Integer.parseInt(algName);
//        String sheetNameWithStrategy = "";

        String lowStrategy = sheetName + "_low";
        String mediumStrategy = sheetName + "_medium";
        String highStrategy = sheetName + "_high";

        log.info("正在读取课程二脱敏策略");
        Map<String, Integer> courseTwoMap = readFromCourseTwo();

        List<ExcelParam> lowStrategyConfig = excelParamService.getParamsByTableName(lowStrategy + "_param");
        List<ExcelParam> mediumStrategyConfig = excelParamService.getParamsByTableName(mediumStrategy + "_param");
        List<ExcelParam> highStrategyConfig = excelParamService.getParamsByTableName(highStrategy + "_param");
        log.info("正在更新低脱敏策略");
        updateExcelParam(courseTwoMap, lowStrategyConfig);
        log.info("正在更新中脱敏策略");
        updateExcelParam(courseTwoMap, mediumStrategyConfig);
        log.info("正在更新高脱敏策略");
        updateExcelParam(courseTwoMap, highStrategyConfig);

        String lowStrategyConfigString = objectMapper.writeValueAsString(lowStrategyConfig);
        String mediumStrategyConfigString = objectMapper.writeValueAsString(mediumStrategyConfig);
        String highStrategyConfigString = objectMapper.writeValueAsString(highStrategyConfig);
//        switch (strategyInt) {
//            case 2: {
//                sheetNameWithStrategy = sheetName + "_medium";
//                break;
//            }
//            case 3: {
//                sheetNameWithStrategy = sheetName + "_high";
//                break;
//            }
//            default: {
//                sheetNameWithStrategy = sheetName + "_low";
//                break;
//            }
//        }
        try {
            fileStorageDetails1 = fileStorageService.saveRawFileWithDesenInfo(tempFilePath);
            fileStorageDetails2 = fileStorageService.saveRawFileWithDesenInfo(tempFilePath);
            fileStorageDetails3 = fileStorageService.saveRawFileWithDesenInfo(tempFilePath);
        } catch (IOException e) {
            if (Files.exists(tempFilePath)) {
                Files.delete(tempFilePath);
            }
            log.error("Failed to save raw file: {}", e.getMessage());
//            return new Result<>(500, "Failed to save file", "");
            return false;
        }
        try {
            log.info("正在使用低脱敏策略进行脱敏");
            ResponseEntity<byte[]> lowResponseEntity = fileService.dealExcel(fileStorageDetails1, lowStrategyConfigString,
                    lowStrategy, false);
            log.info("正在使用中脱敏策略进行脱敏");
            ResponseEntity<byte[]> mediumResponseEntity = fileService.dealExcel(fileStorageDetails2, mediumStrategyConfigString,
                    mediumStrategy, false);
            log.info("正在使用高脱敏策略进行脱敏");
            ResponseEntity<byte[]> highResponseEntity = fileService.dealExcel(fileStorageDetails3, highStrategyConfigString,
                    highStrategy, false);
            log.info("LowResponseEntity Status Code: {}", lowResponseEntity.getStatusCode());
            log.info("MediumResponseEntity Status Code: {}", mediumResponseEntity.getStatusCode());
            log.info("HighResponseEntity Status Code: {}", highResponseEntity.getStatusCode());

            if (lowResponseEntity.getStatusCode() != HttpStatus.OK ||
                    mediumResponseEntity.getStatusCode() != HttpStatus.OK ||
                    highResponseEntity.getStatusCode() != HttpStatus.OK) {
                // 如果有任意一个需求未完成，则返回异常结果
//                return new Result<>(500, "Failed to complete all desen tasks", "");
                return false;
            }
        } catch (Exception e) {
            log.error("Failed to desen excel: {}", e.getMessage());
//            return new Result<>(500, "Failed to desen excel", "");
            if (Files.exists(tempFilePath)) {
                Files.delete(tempFilePath);
            }
            return false;
        }
        if (Files.exists(tempFilePath)) {
            Files.delete(tempFilePath);
        }
//        return new Result<>(200, "Success", "");
        return true;

    }

    private Map<String, Integer> readFromCourseTwo() throws IOException {
        Path path = Paths.get("./dataplatform_config.json");
        JsonNode columnList = objectMapper.readTree(path.toFile()).get("columnList");
        List<ClassificationResult> courseTwoList = objectMapper.readValue(columnList.toString(), new TypeReference<List<ClassificationResult>>() {
        });
        Map<String, Integer> courseTwoMap = new HashMap<>();

        for (ClassificationResult item : courseTwoList) {
            courseTwoMap.put("f_" + item.getColumnName(), item.getColumnLevel());
        }
        return courseTwoMap;


    }

    private void updateExcelParam(Map<String,Integer> courseTwoMap, List<ExcelParam> strategyConfig ) {
        for (ExcelParam item : strategyConfig) {
            if (item.getColumnName().equals("sid") || item.getColumnName().equals("f_dataid") || item.getColumnName().equals("f_ts")) {
                continue;
            }
            Integer courseTwoMapTmParam = courseTwoMap.get(item.getColumnName());
            System.out.println(courseTwoMapTmParam);
            if (courseTwoMapTmParam == 4) {
                courseTwoMapTmParam = 3;
            }
            item.setTmParam(courseTwoMapTmParam > item.getTmParam() ? courseTwoMapTmParam : item.getTmParam());
        }

//        strategyConfig.forEach(System.out::println);
    }
}

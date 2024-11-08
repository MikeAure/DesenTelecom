package com.lu.gademo.task;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageInfo;
import com.lu.gademo.entity.ClassificationResult;
import com.lu.gademo.entity.ExcelParam;
import com.lu.gademo.entity.FileStorageDetails;
import com.lu.gademo.entity.ga.effectEva.SendEvaReq;
import com.lu.gademo.entity.jmtLogStock.TUserInfo;
import com.lu.gademo.service.*;
import com.lu.gademo.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.ListUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.lu.gademo.service.impl.FileServiceImpl.getDsList;

@Slf4j
@Component
@ConditionalOnProperty(name = "fetch.database.jdPlatformTask.enabled", havingValue = "true", matchIfMissing = false)
public class JdFetchDatabase {
    private final AlgorithmsFactory algorithmsFactory;
    // 分页访问本地数据库
    private final TUserInfoDaoService tUserInfoDaoService;
    private final FileService fileService;
    private final FileStorageService fileStorageService;
    private final ExcelParamService excelParamService;

    private final int jdDatabasePageSize;
    private final String jdTableName;
    private final String jdTargetTable;

    private final ThreadLocal<SimpleDateFormat> sdfWithTime;

    private final ObjectMapper objectMapper;

    private final SendEvaReqService sendEvaReqService;
    private final boolean ifSendToEvaluationSystem;
    private final TypeAlgoMappingDaoService typeAlgoMappingDaoService;
    private final Course2Communication course2Communication;
    private final String configFileName;

    @Autowired
    public JdFetchDatabase(AlgorithmsFactory algorithmsFactory,
                           TUserInfoDaoService tUserInfoDaoService,
                           FileService fileService, FileStorageService fileStorageService, ExcelParamService excelParamService,
                           @Value("${fetch.database.pageSize.jdDatabase}") int jdDatabasePageSize,
                           @Value("${fetch.database.jdPlatformTask.jdTableName}") String jdTableName,
                           @Value("${fetch.database.jdPlatformTask.jdTargetTableName}") String jdTargetTableName,
                           @Value("${fetch.database.jdPlatformTask.ifSendToEvaluationSystem}") boolean ifSendToEvaluationSystem,
                           SendEvaReqService sendEvaReqDao, TypeAlgoMappingDaoService typeAlgoMappingDaoService,
                           Course2Communication course2Communication,
                           @Value("${fetch.database.jdPlatformTask.configFileName}") String configFileName
    ) {
        this.algorithmsFactory = algorithmsFactory;
        this.tUserInfoDaoService = tUserInfoDaoService;
        this.fileService = fileService;
        this.fileStorageService = fileStorageService;
        this.excelParamService = excelParamService;
        this.jdDatabasePageSize = jdDatabasePageSize;
        this.jdTableName = jdTableName;
        this.sendEvaReqService = sendEvaReqDao;
        this.typeAlgoMappingDaoService = typeAlgoMappingDaoService;

        this.sdfWithTime = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        this.jdTargetTable = jdTargetTableName;
        this.objectMapper = new ObjectMapper();
        this.ifSendToEvaluationSystem = ifSendToEvaluationSystem;
        this.course2Communication = course2Communication;
        this.configFileName = configFileName;
    }

    /**
     * 将数据发送到评测系统进行脱敏
     *
     * @param page           待保存的数据
     * @param tempFilePath   临时文件路径
     * @param excelParamList 脱敏参数表
     */
    private void sendToEvaluationSystem(List<TUserInfo> page, Path tempFilePath, List<ExcelParam> excelParamList) throws IOException, ExecutionException, InterruptedException, IllegalAccessException, TimeoutException {
        try {
            writeToExcel(page, tempFilePath);
            String strategy = objectMapper.writeValueAsString(excelParamList);
            FileStorageDetails fileStorageDetails = fileStorageService.saveRawFileWithDesenInfo(tempFilePath);
            ResponseEntity<byte[]> responseEntity = fileService.dealExcel(fileStorageDetails, strategy,
                    jdTableName + "_medium", false);
            log.info("ResponseEntity Status Code: {}", responseEntity.getStatusCode());
            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                List<SendEvaReq> resultList = sendEvaReqService.findByFileTypeContains(jdTableName, Sort.by(Sort.Direction.DESC,
                        "desenPerformEndTime"));
                Map<String, String> paramMap = getNewestDesenLevelMap(resultList);
                log.info("Param Map: {}", paramMap);
                // 更新脱敏等级
                for (ExcelParam item : excelParamList) {
                    item.setTmParam(Integer.parseInt(paramMap.get(item.getColumnName())));
                }
            } else {
                log.error("文件脱敏期间发生错误");
                throw new IOException("文件脱敏期间发生错误");
            }
        } catch (Exception e) {
            log.error("Failed to desen excel: {}", e.getMessage());
            throw e;
        }

    }

    @Scheduled(initialDelayString = "${fetch.database.jdPlatformTask.initialDelay}",
            fixedRateString = "${fetch.database.jdPlatformTask.fixedRate}")
    public void fetchJdDatabaseaAndDesen() throws IOException {
        String idColumnName = "id";
        log.info("JD Database Fetch Task Started");
        Path tempFilePath = Paths.get(jdTableName + "_temp.xlsx");
        Path configPath = Paths.get(configFileName + ".json");
        int currentPageNum = 1;
        if (!Files.exists(configPath)) {
            log.info("配置文件不存在，从课题二下载配置文件");
            try {
                course2Communication.writeJsonToFile(configFileName);
            } catch (IOException e) {
                log.error(e.getMessage());
                return;
            }
        }

        Map<String, Integer> courseTwoMap = readFromCourseTwo(configPath);
        log.info("课题二分类分级结果：{}", courseTwoMap);
        List<ExcelParam> excelParamList = excelParamService.getParamsByTableName(jdTableName + "_medium_param");

        if (ListUtils.emptyIfNull(excelParamList).isEmpty()) {
            log.error("未找到数据表配置");
            return;
        }
        log.info("正在更新脱敏策略");
        updateExcelParam(courseTwoMap, excelParamList, idColumnName);
        log.info("当前脱敏策略：{}", excelParamList);

        // TODO: 结合课题二

        PageInfo<TUserInfo> tUserPageInfo = null;
        boolean isHasNextPage = true;
        tUserInfoDaoService.deleteAll(jdTargetTable);

        while (isHasNextPage) {
            tUserPageInfo = tUserInfoDaoService.getRecordsByTableNameAndPageInfo(jdTableName, currentPageNum, jdDatabasePageSize);
            if (tUserPageInfo == null) {
                log.error("从数据库获取当前页的数据失败");
                return;
            }
            List<TUserInfo> page = tUserPageInfo.getList();
            // 第一页请求交给评测系统评测
            if (ifSendToEvaluationSystem) {
                if (currentPageNum == 1) {
                    try {
                        sendToEvaluationSystem(page, tempFilePath, excelParamList);
                    } catch (Exception e) {
                        log.error("Failed to send to evaluation system: {}", e.getMessage());
                        return;
                    }
                }
            }

            try {
                log.info("Current page: {}", currentPageNum);
                log.info("Total pages: {}", tUserPageInfo.getPages());
                log.info("Next page: {}", tUserPageInfo.getNextPage());
                log.info("If has next page: {}", tUserPageInfo.isHasNextPage());
                Map<String, ExcelParam> config = excelParamList.parallelStream()
                        .collect(Collectors.toMap(param -> param.getColumnName().trim(), Function.identity()));
                log.info("Config: {}", config);

                Map<String, List<Object>> desenData = desen(preprocessData(page), config);
                DesenResultConverter<TUserInfo> desenResultConverter = new DesenResultConverter<>(TUserInfo::new);
                List<TUserInfo> desenResult = desenResultConverter.getDesenResult(desenData, "id");
                log.info("Desen Result Size: {}", desenResult.size());
                tUserInfoDaoService.insertListInBatch(jdTargetTable, desenResult, 1000);
                isHasNextPage = tUserPageInfo.isHasNextPage();

                currentPageNum++;
            } catch (IllegalAccessException e) {
                log.error("数据脱敏失败", e);
            }
        }

    }

    /**
     * 从日志中获取最后一次成功脱敏的记录并获取最新的脱敏等级
     *
     * @param resultList 获取到的日志记录列表
     * @return 返回更新后的脱敏等级
     */
    private Map<String, String> getNewestDesenLevelMap(List<SendEvaReq> resultList) {
        SendEvaReq newestResult = resultList.get(0);
        List<String> desenInfoPreIden = Arrays.asList(newestResult.getDesenInfoPreIden().split(","));
        List<String> desenLevelList = Arrays.asList(newestResult.getDesenLevel().split(","));
        Map<String, String> paramMap = new HashMap<>();
        for (int i = 0; i < desenInfoPreIden.size(); i++) {
            paramMap.put(desenInfoPreIden.get(i), desenLevelList.get(i));
        }
        return paramMap;
    }

    public <T> Map<String, List<Object>> preprocessData(List<T> batch) throws IllegalAccessException {
        Map<String, List<Object>> map = new HashMap<>();
        Field[] fields = TUserInfo.class.getDeclaredFields();

        for (Field item : fields) {
//            System.out.println(item.getName());
            map.put(item.getName(), new ArrayList<>());
        }
        // 清洗数据
        for (T obj : batch) {
            for (Field field : fields) {
                field.setAccessible(true);
                if (field.getType() == Date.class) {
                    Object temp = field.get(obj);
                    if (temp == null) {
                        map.get(field.getName()).add(null);
                    } else {
                        Date value = (Date) temp;
                        map.get(field.getName()).add(sdfWithTime.get().format(value));
                    }
                } else {
                    Object value = field.get(obj);
                    map.get(field.getName()).add(value);
                }
            }
        }
        return map;
    }

    private Map<String, List<Object>> desen(Map<String, List<Object>> map, Map<String, ExcelParam> config) {

        Map<String, List<Object>> desenResult = new HashMap<>();
        for (String key : map.keySet()) {
            System.out.println("key: " + key);
            ExcelParam param = config.get(key);
            Integer algoNum = param.getK();

            List<Object> rawList = map.get(key);
            AlgorithmInfo algorithmInfo = algorithmsFactory.getAlgorithmInfoFromId(algoNum);
            DSObject rawData = new DSObject(rawList);
            List<Object> dsList = getDsList(algorithmInfo, rawData, param);
            desenResult.put(key, dsList);
        }

        return desenResult;
    }

    public <T> void writeToExcel(List<T> dataList, Path filePath) throws IOException, IllegalAccessException {
        DateTimeFormatter ldtFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        ThreadLocal<SimpleDateFormat> sdfWithTime = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        // 创建一个新的工作簿
        Workbook workbook = new XSSFWorkbook();
        // 创建一个新的工作表
        Sheet sheet = workbook.createSheet("Data");
        // 创建表头行
        Row headerRow = sheet.createRow(0);
        Field[] fields = TUserInfo.class.getDeclaredFields();

        // 写入数据库列名作为表头
        int colIndex = 0;
        for (Field field : fields) {
            Cell cell = headerRow.createCell(colIndex++);
            cell.setCellValue(field.getName());  // 使用数据库列名
        }

        // 写入数据行
        int rowIndex = 1;
        for (T entity : dataList) {
            Row row = sheet.createRow(rowIndex++);
            colIndex = 0;
            for (Field field : fields) {
                Cell cell = row.createCell(colIndex++);
                if (field != null) {
                    field.setAccessible(true);
                    Object value = field.get(entity);
                    if (value != null) {
                        // 格式化LocalDateTime
                        if (value instanceof LocalDateTime) {
                            cell.setCellValue(((LocalDateTime) value).format(ldtFormatter));
                        } else if (value instanceof Date) {
                            cell.setCellValue(sdfWithTime.get().format((Date) value));
                        } else {
                            cell.setCellValue(value.toString());
                        }
                    }
                }
            }
        }

        // 写入 Excel 文件
        try (FileOutputStream fileOut = new FileOutputStream(filePath.toAbsolutePath().toString())) {
            log.info(filePath.toAbsolutePath().toString());
            workbook.write(fileOut);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 关闭工作簿
        workbook.close();
    }

    private Map<String, Integer> readFromCourseTwo(Path path) throws IOException {

        JsonNode columnList = objectMapper.readTree(path.toFile()).get("columnList");
        List<ClassificationResult> courseTwoList = objectMapper.readValue(columnList.toString(),
                new TypeReference<List<ClassificationResult>>() {
                });
        for (ClassificationResult item : courseTwoList) {
            log.info("{} 对应的分类：{}, 可选算法：{}", item.getColumnName(), item.getColumnType(),
                    typeAlgoMappingDaoService.getAlgNamesByTypeName(item.getColumnType()));
        }
        Map<String, Integer> courseTwoMap = new HashMap<>();

        for (ClassificationResult item : courseTwoList) {
            courseTwoMap.put(item.getColumnName(), item.getColumnLevel());
        }
        return courseTwoMap;
    }

    private void updateExcelParam(Map<String, Integer> courseTwoMap, List<ExcelParam> strategyConfig, String idColumnName) {
        for (ExcelParam item : strategyConfig) {
            if (item.getColumnName().equals(idColumnName)) {
                continue;
            }
            Integer courseTwoMapTmParam = courseTwoMap.get(item.getFieldName());
            log.info("分类分级结果 {} 对应脱敏等级：{}", item.getColumnName(), courseTwoMapTmParam);
            if (courseTwoMapTmParam == 4) {
                courseTwoMapTmParam = 3;
            }
            item.setTmParam(courseTwoMapTmParam > item.getTmParam() ? courseTwoMapTmParam : item.getTmParam());
        }
    }


}

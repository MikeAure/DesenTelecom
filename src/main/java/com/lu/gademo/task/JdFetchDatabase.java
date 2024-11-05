package com.lu.gademo.task;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageInfo;
import com.lu.gademo.entity.ExcelParam;
import com.lu.gademo.entity.FileStorageDetails;
import com.lu.gademo.entity.ga.MeetingDataTable;
import com.lu.gademo.entity.ga.effectEva.SendEvaReq;
import com.lu.gademo.entity.jmtLogStock.TUserInfo;
import com.lu.gademo.mapper.ga.TypeAlgoMappingDao;
import com.lu.gademo.service.*;
import com.lu.gademo.service.impl.FileStorageService;
import com.lu.gademo.utils.AlgorithmInfo;
import com.lu.gademo.utils.AlgorithmsFactory;
import com.lu.gademo.utils.DSObject;
import com.lu.gademo.utils.DesenResultConverter;
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
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.lu.gademo.service.impl.FileServiceImpl.getDsList;

@Slf4j
@Component
@ConditionalOnProperty(name = "fetch.database.jdPlatformTask.enabled", havingValue = "true", matchIfMissing = false)
public class JdFetchDatabase {
    private final AlgorithmsFactory algorithmsFactory;
    // 分页访问本地数据库
    private final MeetingDataTableDaoService meetingDataTableDaoService;
    private final TUserInfoDaoService tUserInfoDaoService;
    private final FileService fileService;
    private final FileStorageService fileStorageService;
    private final ExcelParamService excelParamService;

    private final TypeAlgoMappingDao typeAlgoMappingDao;
    private final int localDatabasePageSize;
    private final int jdDatabasePageSize;
    private final String localTableName;
    private final String jdTableName;

    private final String localTargetTable;
    private final String jdTargetTable;

    private final ThreadLocal<SimpleDateFormat> sdfWithTime;
    private final ThreadLocal<SimpleDateFormat> sdfWithoutTime;


    private final ObjectMapper objectMapper;

    private final SendEvaReqService sendEvaReqDao;
    private final boolean ifSendToEvaluationSystem;

    @Autowired
    public JdFetchDatabase(AlgorithmsFactory algorithmsFactory, MeetingDataTableDaoService meetingDataTableDaoService,
                           TUserInfoDaoService tUserInfoDaoService,
                           FileService fileService, FileStorageService fileStorageService, ExcelParamService excelParamService,
                           TypeAlgoMappingDao typeAlgoMappingDao,
                           @Value("${fetch.database.pageSize.localDatabase}") int localDatabasePageSize,
                           @Value("${fetch.database.pageSize.jdDatabase}") int jdDatabasePageSize,
                           @Value("${fetch.database.jdPlatformTask.localTableName}") String localTableName,
                           @Value("${fetch.database.jdPlatformTask.jdTableName}") String jdTableName,
                           @Value("${fetch.database.jdPlatformTask.localTargetTableName}") String localTargetTableName,
                           @Value("${fetch.database.jdPlatformTask.jdTargetTableName}") String jdTargetTableName,
                           @Value("${fetch.database.jdPlatformTask.ifSendToEvaluationSystem}") boolean ifSendToEvaluationSystem,
                           SendEvaReqService sendEvaReqDao
    ) {
        this.algorithmsFactory = algorithmsFactory;
        this.meetingDataTableDaoService = meetingDataTableDaoService;
        this.tUserInfoDaoService = tUserInfoDaoService;
        this.fileService = fileService;
        this.fileStorageService = fileStorageService;
        this.excelParamService = excelParamService;
        this.typeAlgoMappingDao = typeAlgoMappingDao;
        this.localDatabasePageSize = localDatabasePageSize;
        this.jdDatabasePageSize = jdDatabasePageSize;
        this.localTableName = localTableName;
        this.jdTableName = jdTableName;
        this.sendEvaReqDao = sendEvaReqDao;

        this.sdfWithTime = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        this.sdfWithoutTime = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd"));
        this.localTargetTable = localTargetTableName;
        this.jdTargetTable = jdTargetTableName;
        this.objectMapper = new ObjectMapper();
        this.ifSendToEvaluationSystem = ifSendToEvaluationSystem;

    }

//    public void fetchLocalDatabaseaAndDesen() throws IOException, IllegalAccessException {
//        Path tempFilePath = Paths.get(localTableName + "_temp.xlsx");
//        Field[] fields = MeetingDataTable.class.getDeclaredFields();
//        int currentPageNum = 1;
//        log.info("获取当前数据表配置");
//        List<ExcelParam> excelParamList = excelParamService.getParamsByTableName(localTableName + "_param");
//        if (ListUtils.emptyIfNull(excelParamList).isEmpty()) {
//            log.error("未找到数据表配置");
//            return;
//        }
//        Map<String, ExcelParam> config = excelParamList.parallelStream()
//                .collect(Collectors.toMap(param -> param.getFieldName().trim(), Function.identity()));        // TODO: 结合课题二
//
//        PageInfo<MeetingDataTable> meetingDataTableDaoPageInfo = null;
//        boolean isHasNextPage = true;
//        meetingDataTableDaoService.deleteAll(localTargetTable);
//
//
//        while (isHasNextPage) {
//            meetingDataTableDaoPageInfo = meetingDataTableDaoService.getRecordsByTableNameAndPageInfo(localTableName, currentPageNum, localDatabasePageSize);
//            if (meetingDataTableDaoPageInfo == null) {
//                log.error("从数据库获取当前页的数据失败");
//                return;
//            }
//            List<MeetingDataTable> page = meetingDataTableDaoPageInfo.getList();
//            // 第一页请求交给评测系统评测
//            if (currentPageNum == 1) {
//                writeToExcel(page, this.columnMapping, tempFilePath);
//                String strategy = objectMapper.writeValueAsString(excelParamList);
//                FileStorageDetails fileStorageDetails = null;
//                try {
//                    fileStorageDetails = fileStorageService.saveRawFileWithDesenInfo(tempFilePath);
//                } catch (IOException e) {
//                    if (Files.exists(tempFilePath)) {
//                        Files.delete(tempFilePath);
//                    }
//                    log.error("Failed to save raw file: {}", e.getMessage());
//                    return;
//                }
//                try {
//                    log.info("正在对文件进行脱敏");
//                    ResponseEntity<byte[]> responseEntity = fileService.dealExcel(fileStorageDetails, strategy,
//                            localTableName, false);
//                    log.info("ResponseEntity Status Code: {}", responseEntity.getStatusCode());
//                    if (responseEntity.getStatusCode() == HttpStatus.OK) {
//                        List<SendEvaReq> resultList = sendEvaReqDao.findByFileTypeContains(localTableName, Sort.by(Sort.Direction.DESC,
//                                "desenPerformEndTime"));
//                        Map<String, String> paramMap = getNewestDesenLevelMap(resultList);
//                        log.info("Param Map: {}", paramMap);
//                        for (ExcelParam item : excelParamList) {
//                            item.setTmParam(Integer.parseInt(paramMap.get(item.getColumnName())));
//                        }
//
//                    } else {
//                        log.error("文件脱敏期间发生错误");
//                        return;
//                    }
//                } catch (Exception e) {
//                    log.error("Failed to desen excel: {}", e.getMessage());
//                    if (Files.exists(tempFilePath)) {
//                        Files.delete(tempFilePath);
//                    }
//                    return;
//                }
//
//            }
//            try {
//                Map<String, List<Object>> desenData = desen(preprocessData(page), config);
//
//                List<MeetingDataTable> desenResult = getDesenResult(desenData, fields);
//                meetingDataTableDaoService.insertListInBatch(localTargetTable, desenResult, 1000);
//                isHasNextPage = meetingDataTableDaoPageInfo.isHasNextPage();
//                log.info("Current page: {}", currentPageNum);
//                log.info("Total pages: {}", meetingDataTableDaoPageInfo.getPages());
//                log.info("Next page: {}", meetingDataTableDaoPageInfo.getNextPage());
//                log.info("If has next page: {}", meetingDataTableDaoPageInfo.isHasNextPage());
//                currentPageNum++;
//            } catch (IllegalAccessException e) {
//                log.error("数据脱敏失败", e);
//            }
//        }
//
//    }

    /**
     * 将第一页脱敏后生成的excel文件发送给评测系统
     */
    private void sendToEvaluationSystem(List<TUserInfo> page, Path tempFilePath, List<ExcelParam> excelParamList) {
        try {
            writeToExcel(page, tempFilePath);
            String strategy = objectMapper.writeValueAsString(excelParamList);
            FileStorageDetails fileStorageDetails = fileStorageService.saveRawFileWithDesenInfo(tempFilePath);
            ResponseEntity<byte[]> responseEntity = fileService.dealExcel(fileStorageDetails, strategy,
                    jdTableName + "_medium", false);
            log.info("ResponseEntity Status Code: {}", responseEntity.getStatusCode());
            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                List<SendEvaReq> resultList = sendEvaReqDao.findByFileTypeContains(jdTableName, Sort.by(Sort.Direction.DESC,
                        "desenPerformEndTime"));
                Map<String, String> paramMap = getNewestDesenLevelMap(resultList);
                log.info("Param Map: {}", paramMap);
                // 更新脱敏等级
                for (ExcelParam item : excelParamList) {
                    item.setTmParam(Integer.parseInt(paramMap.get(item.getColumnName())));
                }

            } else {
                log.error("文件脱敏期间发生错误");
            }
        } catch (Exception e) {
            log.error("Failed to desen excel: {}", e.getMessage());
        }

    }

    @Scheduled(initialDelayString = "${fetch.database.jdPlatformTask.initialDelay}",
            fixedRateString = "${fetch.database.jdPlatformTask.fixedRate}")
    public void fetchJdDatabaseaAndDesen() throws IOException, IllegalAccessException {
        log.info("JD Database Fetch Task Started");
        Path tempFilePath = Paths.get(jdTableName + "_temp.xlsx");

        int currentPageNum = 1;
        log.info("获取当前数据表配置");
        List<ExcelParam> excelParamList = excelParamService.getParamsByTableName(jdTableName + "_medium_param");
        if (ListUtils.emptyIfNull(excelParamList).isEmpty()) {
            log.error("未找到数据表配置");
            return;
        }

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
                    sendToEvaluationSystem(page, tempFilePath, excelParamList);
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

//    public Map<String, List<Object>> preprocessData(List<MeetingDataTable> batch) throws IllegalAccessException {
//        Map<String, List<Object>> map = new HashMap<>();
//        Field[] fields = MeetingDataTable.class.getDeclaredFields();
//
//        for (Field item : fields) {
////            System.out.println(item.getName());
//            map.put(item.getName(), new ArrayList<>());
//        }
//        // 清洗数据
//        for (MeetingDataTable obj : batch) {
//            for (Field field : fields) {
//                field.setAccessible(true);
//                if (field.getType() == Date.class) {
//                    Date value = (Date) field.get(obj);
//                    map.get(field.getName()).add(sdfWithTime.get().format(value));
//                } else {
//                    Object value = field.get(obj);
//                    map.get(field.getName()).add(value);
//                }
//            }
//        }
//        return map;
//    }

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

    private List<MeetingDataTable> getDesenResult(Map<String, List<Object>> desenData, Field[] fields) throws IllegalAccessException {
        List<MeetingDataTable> result = new ArrayList<>();
        System.out.println("sjhm size: " + desenData.get("sjhm").size());
        for (int i = 0; i < desenData.get("sjhm").size(); i++) {
            MeetingDataTable meeting = new MeetingDataTable();
            for (Field field : fields) {
                field.setAccessible(true);
                if (field.getType() == Date.class) {
                    Date value = (Date) desenData.get(field.getName()).get(i);
                    field.set(meeting, value);
                } else if (field.getType() == Long.class) {
                    Long value = Long.parseLong(desenData.get(field.getName()).get(i).toString());
                    field.set(meeting, value);
                } else {
                    String value = (String) desenData.get(field.getName()).get(i);
                    field.set(meeting, value);
                }
            }
            result.add(meeting);
        }
        return result;
    }


    public void writeToExcel(List<MeetingDataTable> dataList, Map<String, String> columnMapping, Path filePath) throws IOException, IllegalAccessException {
        DateTimeFormatter ldtFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        ThreadLocal<SimpleDateFormat> sdfWithTime = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        // 创建一个新的工作簿
        Workbook workbook = new XSSFWorkbook();
        // 创建一个新的工作表
        Sheet sheet = workbook.createSheet("Data");
        // 创建表头行
        Row headerRow = sheet.createRow(0);
        Field[] fields = MeetingDataTable.class.getDeclaredFields();

        // 写入数据库列名作为表头
        int colIndex = 0;
        for (String dbColumn : columnMapping.keySet()) {
            Cell cell = headerRow.createCell(colIndex++);
            cell.setCellValue(dbColumn);  // 使用数据库列名
        }

        // 写入数据行
        int rowIndex = 1;
        for (MeetingDataTable entity : dataList) {
            Row row = sheet.createRow(rowIndex++);
            colIndex = 0;
            for (String fieldName : columnMapping.values()) {
                Cell cell = row.createCell(colIndex++);
                Field field = getFieldByName(entity, fieldName);
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

    // 动态获取字段
    private Field getFieldByName(Object obj, String fieldName) {
        try {
            return obj.getClass().getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return null;
    }


}

package com.lu.gademo.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lu.gademo.entity.ExcelParam;
import com.lu.gademo.entity.FileStorageDetails;
import com.lu.gademo.entity.LogCollectResult;
import com.lu.gademo.service.SendEvaReqService;
import com.lu.gademo.utils.AlgorithmInfo;
import com.lu.gademo.entity.ga.effectEva.RecEvaResultInv;
import com.lu.gademo.entity.ga.effectEva.SendEvaReq;
import com.lu.gademo.event.LogManagerEvent;
import com.lu.gademo.event.ReDesensitizeEvent;
import com.lu.gademo.model.LogSenderManager;
import com.lu.gademo.service.ExcelParamService;
import com.lu.gademo.service.FileService;
import com.lu.gademo.utils.*;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.ArrayUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Date;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Data
@Slf4j
@Service
public class FileServiceImpl implements FileService {
    private final SendEvaReqService sendEvaReqService;

    private AlgorithmsFactory algorithmsFactory;

    private Dp dp;
    private Replace replacement;
    private Generalization generalization;
    private Anonymity anonymity;
    // 发送类
    private LogSenderManager logSenderManager;
    private ExcelParamService excelParamService;
    private final DpUtil dpUtil;
    // 工具类
    private Util util;
    // 构造各类请求
    private LogCollectUtil logCollectUtil;
    private ApplicationEventPublisher eventPublisher;

    private Random randomNum;
    private Path currentDirectory;
    private Path rawFileDirectory;
    private Path desenFileDirectory;

    private final ObjectMapper objectMapper;
    private final Boolean ifSendToEvaFirst;
    private final Boolean ifPerformanceTest;
    private final FileStorageService fileStorageService;

    private final DateParseUtil dateParseUtil;


    @EventListener
    public void handleReDesensitizeEvent(ReDesensitizeEvent event) throws Exception {
        SendEvaReq sendEvaReq = event.getLogManagerEvent().getSendEvaReq();
        String fileType = sendEvaReq.getFileType();

        switch (fileType) {
            case "image":
                redesenImage(event);
                break;
            case "audio":
                redesenAudio(event);
                break;
            case "video":
                redesenVideo(event);
                break;
            case "text":
                redesenSingleText(event);
                break;
            case "graph":
                redesenGraph(event);
                break;
            default:
                redesenExcel(event);
                break;
        }
    }

    @Autowired
    public FileServiceImpl(AlgorithmsFactory algorithmsFactory, Dp dp,
                           Replace replacement, Generalization generalization, Anonymity anonymity,
                           LogSenderManager logSenderManager, Util util, ExcelParamService excelParamService,
                           DpUtil dpUtil, LogCollectUtil logCollectUtil, ApplicationEventPublisher eventPublisher,
                           SendEvaReqService sendEvaReqDao,
                           @Value("${logSenderManager.ifSendToEvaFirst}")
                           Boolean ifSendEvaFirst, FileStorageService fileStorageService,
                           @Value("${logSenderManager.ifPerformenceTest}")
                           Boolean ifPerformanceTest, DateParseUtil dateParseUtil) throws IOException {
        this.algorithmsFactory = algorithmsFactory;
        this.dp = dp;
        this.replacement = replacement;
        this.generalization = generalization;
        this.anonymity = anonymity;
        this.logSenderManager = logSenderManager;
        this.util = util;
        this.excelParamService = excelParamService;
        this.dpUtil = dpUtil;
        this.randomNum = new Random();
        this.logCollectUtil = logCollectUtil;
        this.eventPublisher = eventPublisher;
        this.currentDirectory = Paths.get("");
        this.rawFileDirectory = Paths.get("raw_files");
        this.desenFileDirectory = Paths.get("desen_files");
        if (!Files.exists(rawFileDirectory)) {
            Files.createDirectory(rawFileDirectory);
        }
        if (!Files.exists(desenFileDirectory)) {
            Files.createDirectory(desenFileDirectory);
        }
        log.info("rawFileDirectory: " + rawFileDirectory.toAbsolutePath());
        log.info("desenFileDirectory: " + desenFileDirectory.toAbsolutePath());
        this.sendEvaReqService = sendEvaReqDao;
        this.objectMapper = new ObjectMapper();
        this.ifSendToEvaFirst = ifSendEvaFirst;
        this.fileStorageService = fileStorageService;
        this.ifPerformanceTest = ifPerformanceTest;
        this.dateParseUtil = dateParseUtil;
    }

    private LogInfo processExcel(FileStorageDetails fileStorageDetails, String params, String sheetName,
                                 Boolean ifSaveExcelParams) throws IOException {
        String objectMode = "text";
        // 包含不同的流转场景信息
        Boolean desenCom = false;
        DesenInfoStringBuilders infoBuilders = new DesenInfoStringBuilders();

        // 设置原文件保存路径
        String rawFileName = fileStorageDetails.getRawFileName();
        String rawFileSuffix = fileStorageDetails.getRawFileSuffix();
        Path rawFilePath = fileStorageDetails.getRawFilePath();
        byte[] rawFileBytes = fileStorageDetails.getRawFileBytes();
        Long rawFileSize = fileStorageDetails.getRawFileSize();

        // 设置脱敏后文件路径信息
        String desenFileName = fileStorageDetails.getDesenFileName();
        Path desenFilePath = fileStorageDetails.getDesenFilePath();
        String desenFilePathString = fileStorageDetails.getDesenFilePathString();

        // 读取excel文件
        Workbook workbook = new XSSFWorkbook(Files.newInputStream(rawFilePath));
        Sheet originalSheet = workbook.getSheetAt(0);
        //脱敏参数处理,转为json
        List<ExcelParam> excelParamList = logCollectUtil.jsonStringToParams(params);
        // 数据行数
        int totalRowNum = originalSheet.getLastRowNum();
        // 字段名行
        Row firstRow = originalSheet.getRow(0);
        // 列数
        int columnCount = firstRow.getPhysicalNumberOfCells(); // 获取列数
        List<String> fieldNameRow = Stream.iterate(0, index -> index + 1)
                .limit(columnCount)
                .map(firstRow::getCell)
                .map(Cell::toString)
                .collect(Collectors.toList());

        log.info("总列数：{}", columnCount);
        // 调用脱敏程序处理
        log.info("开始进行脱敏");
        String startTime = util.getTime();
        Map<Integer, List<?>> desenResult = realDealExcel(
                preprocessSheet(originalSheet, excelParamList, totalRowNum, fieldNameRow, columnCount)
                ,infoBuilders, excelParamList, totalRowNum, fieldNameRow, columnCount);
        String endTime = util.getTime();
        log.info("脱敏结束");
//        log.info("创建新Excel Workbook");
        Workbook targetWorkbook = new XSSFWorkbook();
        Sheet targetSheet = targetWorkbook.createSheet("MaskedSheet");

        log.info("正在准备文件");
        for (Map.Entry<Integer, List<?>> entry : desenResult.entrySet()) {
            util.write2Excel(originalSheet, targetSheet, totalRowNum, entry.getKey(), entry.getValue());
        }
        log.info("文件准备完成");
        // 保存处理后的Excel数据到ByteArrayOutputStream中
        // 保存脱敏后文件
        // 脱敏文件路径
        try (FileOutputStream fileOutputStream = new FileOutputStream(desenFilePathString)) {
            targetWorkbook.write(fileOutputStream);
        }
        // 保存参数文件到本地txt文件
        Path paramDirectory = Paths.get("desen_params");
        if (!Files.exists(paramDirectory)) {
            Files.createDirectory(paramDirectory);
        }
        String paramsFileName = "params" + rawFileName.substring(0, rawFileName.lastIndexOf('.')) + ".txt";
        String paramsFilePath = paramDirectory.resolve(paramsFileName).toAbsolutePath().toString();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(paramsFilePath))) {
            for (ExcelParam p : excelParamList) {
                String line = p.toString();
                writer.write(line);
                writer.newLine(); // 换行
            }
        } catch (IOException e) {
            log.error(e.getMessage());
            log.error("Failed to save params to file");
        }

        // 关闭工作簿和流
        workbook.close();
        desenCom = true;
        // 脱敏前信息
        // 将文件字节流转换为字符串
        String globalID = System.currentTimeMillis() + randomNum.nextInt() + "脱敏工具集";
        byte[] desenFileBytes = Files.readAllBytes(desenFilePath);
        long desenFileSize = Files.size(desenFilePath);

        fileStorageDetails.setDesenFileBytes(desenFileBytes);
        fileStorageDetails.setDesenFileSize(desenFileSize);
        // 存证系统
        String evidenceID = util.getSM3Hash(ArrayUtils.addAll(desenFileBytes, util.getTime().getBytes()));
        return LogInfo.builder().fileType(sheetName).desenFileBytes(desenFileBytes).desenFileSize(desenFileSize)
                .desenFileName(desenFileName).desenCom(desenCom).evidenceID(evidenceID).globalID(globalID).objectMode(objectMode)
                .rawFileName(rawFileName).rawFileBytes(rawFileBytes).rawFileSize(rawFileSize).startTime(startTime).endTime(endTime)
                .infoBuilders(infoBuilders).rawFileSuffix(rawFileSuffix).build();
    }

    private LogInfo processSingleExcel(FileStorageDetails fileStorageDetails, String params, String algName
    ) throws IOException {
        Boolean desenCom = false;
        DesenInfoStringBuilders infoBuilders = new DesenInfoStringBuilders();
        String objectMode = "text";

        // 设置原文件保存路径
        String rawFileName = fileStorageDetails.getRawFileName();
        String rawFileSuffix = fileStorageDetails.getRawFileSuffix();
        Path rawFilePath = fileStorageDetails.getRawFilePath();
        String rawFilePathString = fileStorageDetails.getRawFilePathString();
        byte[] rawFileBytes = fileStorageDetails.getRawFileBytes();
        Long rawFileSize = fileStorageDetails.getRawFileSize();

        // 设置脱敏后文件路径信息
        String desenFileName = fileStorageDetails.getDesenFileName();
        Path desenFilePath = fileStorageDetails.getDesenFilePath();
        log.info(desenFilePath.toAbsolutePath().toString());
        String desenFilePathString = fileStorageDetails.getDesenFilePathString();

        // 读取excel文件
        InputStream inputStream = Files.newInputStream(rawFilePath);
        Workbook workbook = new XSSFWorkbook(inputStream);
        Sheet sheet = workbook.getSheetAt(0);

        // 保存脱敏后文件
        // 脱敏文件路径
        FileOutputStream fileOutputStream = new FileOutputStream(desenFilePathString);
        // 保存参数文件
        int desenParam = Integer.parseInt(String.valueOf(params.charAt(params.length() - 1)));
        // 数据类型
        List<Integer> dataType = new ArrayList<>();
        // 数据行数
        int totalRowNum = sheet.getLastRowNum();
        // 字段名行
        Row fieldRow = sheet.getRow(0);
        // 列数
        int columnCount = fieldRow.getPhysicalNumberOfCells(); // 获取列数
        Map<Integer, List<?>> desenResult = new HashMap<>();
        log.info("Total column number is " + columnCount);
        // 调用脱敏程序处理
        log.info("Start desen");
        // 脱敏开始时间
        String startTime = util.getTime();
        long startTimePoint = System.nanoTime();

        //  逐列处理
        DataFormatter dataFormatter = new DataFormatter();
        for (int columnIndex = 0; columnIndex < columnCount; columnIndex++) {
            // 取列名
            String colName = fieldRow.getCell(columnIndex).toString();
            log.info("Column Index: {}, Column Name: {}", columnIndex, colName);

            // 脱敏前信息类型标识
            infoBuilders.desenInfoPreIden.append(colName);
            infoBuilders.desenInfoAfterIden.append(colName);
            // 读取脱敏级别
            infoBuilders.desenLevel.append(desenParam);
            // 脱敏意图
            infoBuilders.desenIntention.append(colName).append("脱敏").append(",");
            // 脱敏数据类型
            infoBuilders.fileDataType.append(rawFileSuffix);
            // 取列数据
            List<Object> objs = new ArrayList<>();

            for (int j = 1; j <= totalRowNum; j++) {
                Row row = sheet.getRow(j);
                if (row != null) {
                    Cell cell = row.getCell(columnIndex);
                    objs.add(cell);
                }
            }

            AlgorithmInfo algorithmInfo = algorithmsFactory.getAlgorithmInfoFromName(algName.trim());
            DSObject dsObject = new DSObject(objs);
            infoBuilders.desenAlgParam.append(algorithmInfo.getParams() == null ? "无参" : algorithmInfo.getParams().get(desenParam - 1));

            switch (algName.trim()) {
                case "dpDate": {
                    // 脱敏要求
                    infoBuilders.desenRequirements.append(colName).append("添加Laplace噪声,");
                    infoBuilders.desenAlg.append(algorithmInfo.getId());

                    List<Date> datas = getDsList(algorithmInfo, dsObject, desenParam);
                    desenResult.put(columnIndex, datas);
//                    util.write2Excel(sheet, totalRowNum, columnIndex, dates);
                    break;
                }
                case "dpCode": {
                    // 脱敏要求
                    infoBuilders.desenRequirements.append(colName).append("随机扰动,");
                    infoBuilders.desenAlg.append(algorithmInfo.getId());

                    List<String> datas = getDsList(algorithmInfo, dsObject, desenParam);
                    desenResult.put(columnIndex, datas);
//                    util.write2Excel(sheet, totalRowNum, columnIndex, datas);
                    break;
                }
                case "laplaceToValue": {
                    // 脱敏要求
                    infoBuilders.desenRequirements.append(colName).append("添加差分隐私Laplace噪声,");
                    infoBuilders.desenAlg.append(algorithmInfo.getId());
                    // 脱敏
                    List<Double> datas = getDsList(algorithmInfo, dsObject, desenParam);
                    desenResult.put(columnIndex, datas);
//                    util.write2Excel(sheet, totalRowNum, columnIndex, datas);
                    break;
                }

                case "randomUniformToValue": {
                    // 脱敏要求
                    infoBuilders.desenRequirements.append(colName).append("添加随机均匀噪声,");
                    infoBuilders.desenAlg.append(algorithmInfo.getId());
                    // 脱敏
                    List<Double> datas = getDsList(algorithmInfo, dsObject, desenParam);
                    desenResult.put(columnIndex, datas);
                    // 写列数据
//                    util.write2Excel(sheet, totalRowNum, columnIndex, datas);
                    break;
                }
                case "randomLaplaceToValue": {
                    // 脱敏要求
                    infoBuilders.desenRequirements.append(colName).append("添加随机laplace噪声,");
                    infoBuilders.desenAlg.append(algorithmInfo.getId());
                    List<Double> datas = getDsList(algorithmInfo, dsObject, desenParam);
                    desenResult.put(columnIndex, datas);
//                    util.write2Excel(sheet, totalRowNum, columnIndex, datas);
                    break;
                }
                case "randomGaussianToValue": {
                    // 脱敏要求
                    infoBuilders.desenRequirements.append(colName).append("添加随机高斯噪声,");
                    infoBuilders.desenAlg.append(algorithmInfo.getId());
                    List<Double> datas = getDsList(algorithmInfo, dsObject, desenParam);
                    desenResult.put(columnIndex, datas);
//                    util.write2Excel(sheet, totalRowNum, columnIndex, datas);
                    break;
                }
                case "valueShift": {
                    // 脱敏要求
                    infoBuilders.desenRequirements.append(colName).append("数值偏移,");
                    infoBuilders.desenAlg.append(algorithmInfo.getId());
                    List<Double> datas = getDsList(algorithmInfo, dsObject, desenParam);
                    desenResult.put(columnIndex, datas);
//                    util.write2Excel(sheet, totalRowNum, columnIndex, datas);
                    break;
                }
                case "floor": {
                    // 脱敏要求
                    infoBuilders.desenRequirements.append(colName).append("数值取整,");
                    infoBuilders.desenAlg.append(algorithmInfo.getId());
                    if (desenParam == 0) {
                        desenResult.put(columnIndex, objs);
//                        util.write2Excel(sheet, totalRowNum, columnIndex, objs);
                    } else {
                        List<Integer> datas = getDsList(algorithmInfo, dsObject, desenParam);
                        desenResult.put(columnIndex, datas);
//                        util.write2Excel(sheet, totalRowNum, columnIndex, datas);
                    }
                    break;
                }
                case "valueMapping": {
                    // 脱敏要求
                    infoBuilders.desenRequirements.append(colName).append("数值映射,");
                    infoBuilders.desenAlg.append(algorithmInfo.getId());
                    List<Double> datas = getDsList(algorithmInfo, dsObject, desenParam);
                    desenResult.put(columnIndex, datas);
//                    util.write2Excel(sheet, totalRowNum, columnIndex, datas);
                    break;
                }
                case "truncation": {
                    // 脱敏要求
                    infoBuilders.desenRequirements.append(colName).append("截断,");
                    infoBuilders.desenAlg.append(algorithmInfo.getId());
                    List<String> datas = getDsList(algorithmInfo, dsObject, desenParam);
                    desenResult.put(columnIndex, datas);
//                    util.write2Excel(sheet, totalRowNum, columnIndex, datas);
                    break;
                }

                case "floorTime":
                case "suppressEmail":
                case "addressHide":
                case "nameHide":
                case "numberHide":
                case "suppressIpRandomParts":
                case "suppressAllIp": {
                    // 脱敏要求
                    infoBuilders.desenRequirements.append(colName).append("抑制,");
                    infoBuilders.desenAlg.append(algorithmInfo.getId());
                    List<String> datas = getDsList(algorithmInfo, dsObject, desenParam);
                    desenResult.put(columnIndex, datas);
//                    util.write2Excel(sheet, totalRowNum, columnIndex, datas);
                    break;
                }

                case "SHA512":
                case "passReplace":
                case "value_hide": {
                    // 脱敏要求
                    infoBuilders.desenRequirements.append(colName).append("置换,");
                    infoBuilders.desenAlg.append(algorithmInfo.getId());
                    List<String> datas = getDsList(algorithmInfo, dsObject, desenParam);
                    desenResult.put(columnIndex, datas);
                    break;
                }
            }
        }

        // 结束时间
        long endTimePoint = System.nanoTime();
        // 脱敏结束时间
        String endTime = util.getTime();

        log.info("脱敏共耗时：" + (endTimePoint - startTimePoint) / 1e6 + "ms");
        long oneTime = (endTimePoint - startTimePoint) / columnCount / (totalRowNum - 1);
        // 打印单条运行时间
        log.info("脱敏单条数据时长：" + oneTime / 1e6 + " ms");
        // 一秒数据量
        log.info("每秒脱敏数据条数：" + 1e9 / oneTime);
        log.info("文件脱敏完成");

        log.info("开始写入文件");
        for (int columnIndex = 0; columnIndex < columnCount; columnIndex++) {
            List<?> datas = desenResult.get(columnIndex);
            util.write2Excel(sheet, totalRowNum, columnIndex, datas);
        }
        log.info("文件写入完成");

        // 保存处理后的Excel数据到outputStream中
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        workbook.write(byteArrayOutputStream);
        byte[] newExcelData = byteArrayOutputStream.toByteArray();
        fileOutputStream.write(newExcelData);

        // 标志脱敏完成
        desenCom = true;
        String globalID = System.currentTimeMillis() + randomNum.nextInt() + "脱敏工具集";
        byte[] desenFileBytes = Files.readAllBytes(desenFilePath.toAbsolutePath());
        // 脱敏前信息
        Long desenFileSize = Files.size(desenFilePath.toAbsolutePath());

        // 存证系统
        String evidenceID = util.getSM3Hash((new String(newExcelData, StandardCharsets.UTF_8) + util.getTime()).getBytes());
        fileStorageDetails.setDesenFileBytes(desenFileBytes);
        fileStorageDetails.setDesenFileSize(desenFileSize);

        // 关闭工作簿和流
        fileOutputStream.close();
        workbook.close();
        inputStream.close();

        return LogInfo.builder().fileType(objectMode).desenFileBytes(desenFileBytes).desenFileSize(desenFileSize)
                .desenFileName(desenFileName).desenCom(desenCom).evidenceID(evidenceID).globalID(globalID).objectMode(objectMode)
                .rawFileName(rawFileName).rawFileBytes(rawFileBytes).rawFileSize(rawFileSize).startTime(startTime).endTime(endTime)
                .infoBuilders(infoBuilders).rawFileSuffix(rawFileSuffix).build();
    }

    /**
     * 处理单列文本文件
     * @param fileStorageDetails 封装的文件信息
     * @param level 脱敏等级
     * @param algName 脱敏算法名称
     * @param ifSkipFirstRow 是否跳过第一行，在非性能测试场景下，需要跳过第一行
     * @return
     * @throws IOException
     */
    private LogInfo processSingleColumnTextFile(FileStorageDetails fileStorageDetails, String level, String algName,
                                                boolean ifSkipFirstRow) throws IOException {
        Boolean desenCom = false;
        DesenInfoStringBuilders infoBuilders = new DesenInfoStringBuilders();
        String objectMode = "text";

        // 设置原文件保存路径
        String rawFileName = fileStorageDetails.getRawFileName();
        String rawFileSuffix = fileStorageDetails.getRawFileSuffix();
        Path rawFilePath = fileStorageDetails.getRawFilePath();
        String rawFilePathString = fileStorageDetails.getRawFilePathString();
        byte[] rawFileBytes = fileStorageDetails.getRawFileBytes();
        Long rawFileSize = fileStorageDetails.getRawFileSize();

        // 设置脱敏后文件路径信息
        String desenFileName = fileStorageDetails.getDesenFileName();
        Path desenFilePath = fileStorageDetails.getDesenFilePath();
        log.info(desenFilePath.toAbsolutePath().toString());
        String desenFilePathString = fileStorageDetails.getDesenFilePathString();

        Stream<String> lines = Files.lines(rawFilePath);
        List<String> dataList = lines.collect(Collectors.toList());
        // 保存脱敏后文件
        // 脱敏文件路径
        BufferedWriter writer = new BufferedWriter(new FileWriter(desenFilePathString));      // 保存参数文件
        int desenParam = Integer.parseInt(String.valueOf(level.charAt(level.length() - 1)));
        // 数据行数
        int totalRowNum = dataList.size();

        // 列数
        int columnCount = 1; // 获取列数
        Map<Integer, List<?>> desenResult = new HashMap<>();
        log.info("Total column number is " + columnCount);
        // 调用脱敏程序处理
        log.info("Start single column text file desen");
        // 脱敏开始时间
        String startTime = util.getTime();
        long startTimePoint = System.nanoTime();

        for (int columnIndex = 0; columnIndex < columnCount; columnIndex++) {
            // 取列名
//            String colName = fieldRow.getCell(columnIndex).toString();
            log.info("Column Index: {}, Column Name: {}", columnIndex, rawFileName);

            // 脱敏前信息类型标识
            infoBuilders.desenInfoPreIden.append(rawFileName);
            infoBuilders.desenInfoAfterIden.append(rawFileName);
            // 读取脱敏级别
            infoBuilders.desenLevel.append(desenParam);
            // 脱敏意图
            infoBuilders.desenIntention.append(rawFileName).append("脱敏,");
            // 脱敏数据类型
            infoBuilders.fileDataType.append(rawFileSuffix);

            AlgorithmInfo algorithmInfo = algorithmsFactory.getAlgorithmInfoFromName(algName.trim());
            DSObject dsObject = ifSkipFirstRow ? new DSObject(dataList.subList(1, dataList.size())) : new DSObject(dataList);
            infoBuilders.desenAlgParam.append(algorithmInfo.getParams() == null ? "无参" : algorithmInfo.getParams().get(desenParam - 1));
            infoBuilders.desenAlg.append(algorithmInfo.getId());
            infoBuilders.desenRequirements.append(rawFileName).append(algorithmInfo.getRequirement()).append(",");

            // 脱敏
            desenResult.put(columnIndex, getDsList(algorithmInfo, dsObject, desenParam));
        }

        // 结束时间
        long endTimePoint = System.nanoTime();
        // 脱敏结束时间
        String endTime = util.getTime();
        log.info("脱敏共耗时：" + (endTimePoint - startTimePoint) / 1e6 + "ms");
        long oneTime = (endTimePoint - startTimePoint) / columnCount / (totalRowNum - 1);
        // 打印单条运行时间
        log.info("脱敏单条数据时长：" + oneTime / 1e6 + " ms");
        // 一秒数据量
        log.info("每秒脱敏数据条数：" + 1e9 / oneTime);
        log.info("文件脱敏完成");

        log.info("开始写入文件");
        for (int columnIndex = 0; columnIndex < columnCount; columnIndex++) {
            List<?> datas = desenResult.get(columnIndex);
            if (ifSkipFirstRow) {
                writer.write(dataList.get(0));
                writer.newLine();
            }
            for (Object data : datas) {
                writer.write(data.toString());
                writer.newLine();
            }
        }
        log.info("文件写入完成");
        // 关闭工作簿和流
        writer.close();

        // 保存处理后的Excel数据到outputStream中
//        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//        workbook.write(byteArrayOutputStream);
//        byte[] newExcelData = byteArrayOutputStream.toByteArray();


        // 标志脱敏完成
        desenCom = true;
        String globalID = System.currentTimeMillis() + randomNum.nextInt() + "脱敏工具集";
        byte[] desenFileBytes = Files.readAllBytes(desenFilePath.toAbsolutePath());
        // 脱敏前信息
        Long desenFileSize = Files.size(desenFilePath.toAbsolutePath());

        // 存证系统
        String evidenceID = util.getSM3Hash(ArrayUtils.addAll(desenFileBytes, util.getTime().getBytes()));
        fileStorageDetails.setDesenFileBytes(desenFileBytes);
        fileStorageDetails.setDesenFileSize(desenFileSize);

        return LogInfo.builder().fileType(objectMode).desenFileBytes(desenFileBytes).desenFileSize(desenFileSize)
                .desenFileName(desenFileName).desenCom(desenCom).evidenceID(evidenceID).globalID(globalID).objectMode(objectMode)
                .rawFileName(rawFileName).rawFileBytes(rawFileBytes).rawFileSize(rawFileSize).startTime(startTime).endTime(endTime)
                .infoBuilders(infoBuilders).rawFileSuffix(rawFileSuffix).build();
    }

    private LogInfo processImage(FileStorageDetails fileStorageDetails, String params, String algName)
            throws IOException {
        Boolean desenCom = false;
        DesenInfoStringBuilders infoBuilders = new DesenInfoStringBuilders();
        String objectMode = "image";

        // 设置原文件保存路径
        String rawFileName = fileStorageDetails.getRawFileName();
        String rawFileSuffix = fileStorageDetails.getRawFileSuffix();
        Path rawFilePath = fileStorageDetails.getRawFilePath();
        String rawFilePathString = fileStorageDetails.getRawFilePathString();
        byte[] rawFileBytes = fileStorageDetails.getRawFileBytes();
        Long rawFileSize = fileStorageDetails.getRawFileSize();

        // 设置脱敏后文件路径信息
        String desenFileName = fileStorageDetails.getDesenFileName();
        Path desenFilePath = fileStorageDetails.getDesenFilePath();
        log.info(desenFilePath.toAbsolutePath().toString());
        String desenFilePathString = fileStorageDetails.getDesenFilePathString();

        // 脱敏参数处理
        Integer reqDesenParam = Integer.valueOf(params);
        // 调用脱敏程序处理
        DSObject dsObject = new DSObject(Arrays.asList(rawFilePathString, desenFilePathString));
        AlgorithmInfo algorithmInfo = algorithmsFactory.getAlgorithmInfoFromName(algName);
        log.info("Start image desen");
        // 脱敏开始时间
        String startTime = util.getTime();
        long startTimePoint = System.currentTimeMillis();
        algorithmInfo.execute(dsObject, reqDesenParam);
        // 脱敏后收集信息
        // 结束时间
        long endTimePoint = System.currentTimeMillis();
        // 脱敏结束时间
        String endTime = util.getTime();
        // 脱敏耗时
        long executionTime = endTimePoint - startTimePoint;
        logCollectUtil.logExecutionTime(String.valueOf(executionTime), objectMode);
        // 标志脱敏完成
        desenCom = true;
        // 设置globalID
        String globalID = System.currentTimeMillis() + randomNum.nextInt() + "脱敏工具集";
        // 脱敏后文件字节流
        byte[] desenFileBytes = Files.readAllBytes(desenFilePath.toAbsolutePath());
        Long desenFileSize = Files.size(desenFilePath.toAbsolutePath());

        infoBuilders.desenAlg.append(algorithmInfo.getId());
        infoBuilders.desenLevel.append(reqDesenParam + 1);
        if (algorithmInfo.getParams() == null) {
            infoBuilders.desenAlgParam.append("无参");
        } else {
            infoBuilders.desenAlgParam.append(algorithmInfo.getParams().get(reqDesenParam).toString());
        }
        // 脱敏前类型
        infoBuilders.desenInfoPreIden.append("image");
        // 脱敏后类型
        infoBuilders.desenInfoAfterIden.append("image");
        // 脱敏意图
        infoBuilders.desenIntention.append("对图像脱敏");
        // 脱敏要求
        infoBuilders.desenRequirements.append(algorithmInfo.getRequirement());
        // 脱敏数据类型
        infoBuilders.fileDataType.append(rawFileSuffix);

        fileStorageDetails.setDesenFileBytes(desenFileBytes);
        fileStorageDetails.setDesenFileSize(desenFileSize);

        // 本地存证
        // 存证系统
        String evidenceID = util.getSM3Hash(ArrayUtils.addAll(desenFileBytes, util.getTime().getBytes()));
        return LogInfo.builder().fileType(objectMode).desenFileBytes(desenFileBytes).desenFileSize(desenFileSize)
                .desenFileName(desenFileName).desenCom(desenCom).evidenceID(evidenceID).globalID(globalID).objectMode(objectMode)
                .rawFileName(rawFileName).rawFileBytes(rawFileBytes).rawFileSize(rawFileSize).startTime(startTime).endTime(endTime)
                .infoBuilders(infoBuilders).rawFileSuffix(rawFileSuffix).build();
    }


    private LogInfo processVideo(FileStorageDetails fileStorageDetails, String params, String algName) throws IOException {
        Boolean desenCom = false;
        DesenInfoStringBuilders infoBuilders = new DesenInfoStringBuilders();
        String objectMode = "video";

        // 设置原文件保存路径
        String rawFileName = fileStorageDetails.getRawFileName();
        String rawFileSuffix = fileStorageDetails.getRawFileSuffix();
        Path rawFilePath = fileStorageDetails.getRawFilePath();
        String rawFilePathString = fileStorageDetails.getRawFilePathString();
        byte[] rawFileBytes = fileStorageDetails.getRawFileBytes();
        Long rawFileSize = fileStorageDetails.getRawFileSize();

        // 设置脱敏后文件路径信息
        String desenFileName = fileStorageDetails.getDesenFileName();
        Path desenFilePath = fileStorageDetails.getDesenFilePath();
        log.info("Video desen file path: {}", desenFilePath.toAbsolutePath().toString());
        String desenFilePathString = fileStorageDetails.getDesenFilePathString();

        Integer desenParam = Integer.valueOf(params);
        DSObject dsObject = new DSObject(Arrays.asList(rawFilePathString, desenFilePathString));

        AlgorithmInfo algorithmInfo = algorithmsFactory.getAlgorithmInfoFromName(algName);

        // 调用脱敏程序处理
        log.info("Start video desen");
        // 脱敏开始时间
        String startTime = util.getTime();
        long startTimePoint = System.currentTimeMillis();
        algorithmInfo.execute(dsObject, desenParam);

        // 脱敏后收集信息
        // 结束时间
        long endTimePoint = System.currentTimeMillis();
        // 脱敏耗时
        long executionTime = endTimePoint - startTimePoint;
        log.info("Desensitization finished in " + executionTime + "ms");
        log.info("Video desen finished");
        // 脱敏结束时间
        String endTime = util.getTime();
        // 标志脱敏完成
        desenCom = true;
        String globalID = System.currentTimeMillis() + randomNum.nextInt() + "脱敏工具集";
        // 脱敏后文件字节流
        byte[] desenFileBytes = Files.readAllBytes(desenFilePath.toAbsolutePath());
        Long desenFileSize = Files.size(desenFilePath.toAbsolutePath());
        // 脱敏算法
        infoBuilders.desenAlg.append(algorithmInfo.getId());
        if (algorithmInfo.getParams() == null) {
            infoBuilders.desenAlgParam.append("无参");
        } else {
            infoBuilders.desenAlgParam.append(algorithmInfo.getParams().get(desenParam).toString());
        }
        infoBuilders.desenLevel.append(desenParam + 1);
        // 脱敏前类型
        infoBuilders.desenInfoPreIden.append("video");
        // 脱敏后类型
        infoBuilders.desenInfoAfterIden.append("video");
        // 脱敏意图
        infoBuilders.desenIntention.append("对视频脱敏");
        // 脱敏要求
        infoBuilders.desenRequirements.append("对视频脱敏");
        // 脱敏数据类型
        infoBuilders.fileDataType.append(rawFileSuffix);

        fileStorageDetails.setDesenFileBytes(desenFileBytes);
        fileStorageDetails.setDesenFileSize(desenFileSize);
        // 存证系统evidenceID
        String evidenceID = util.getSM3Hash((new String(desenFileBytes, StandardCharsets.UTF_8) + util.getTime()).getBytes());
        return LogInfo.builder().fileType(objectMode).desenFileBytes(desenFileBytes).desenFileSize(desenFileSize)
                .desenFileName(desenFileName).desenCom(desenCom).evidenceID(evidenceID).globalID(globalID).objectMode(objectMode)
                .rawFileName(rawFileName).rawFileBytes(rawFileBytes).rawFileSize(rawFileSize).startTime(startTime).endTime(endTime)
                .infoBuilders(infoBuilders).rawFileSuffix(rawFileSuffix).build();
    }

    private LogInfo processAudio(FileStorageDetails fileStorageDetails, String params, String algName)
            throws IOException {
        Boolean desenCom = false;
        DesenInfoStringBuilders infoBuilders = new DesenInfoStringBuilders();
        String objectMode = "audio";

        // 设置原文件保存路径
        String rawFileName = fileStorageDetails.getRawFileName();
        String rawFileSuffix = fileStorageDetails.getRawFileSuffix();
        Path rawFilePath = fileStorageDetails.getRawFilePath();
        String rawFilePathString = fileStorageDetails.getRawFilePathString();
        byte[] rawFileBytes = fileStorageDetails.getRawFileBytes();
        Long rawFileSize = fileStorageDetails.getRawFileSize();

        // 设置脱敏后文件路径信息
        String desenFileName = fileStorageDetails.getDesenFileName();
        Path desenFilePath = fileStorageDetails.getDesenFilePath();
        log.info(desenFilePath.toAbsolutePath().toString());
        String desenFilePathString = fileStorageDetails.getDesenFilePathString();

        // 脱敏参数处理
        String[] paramsList = params.split(",");
        int desenParam = Integer.parseInt(paramsList[paramsList.length - 1]);
        // 获取脱敏算法
        AlgorithmInfo algorithmInfo = algorithmsFactory.getAlgorithmInfoFromName(algName);
        List<String> paths = Arrays.asList(rawFilePathString, desenFilePathString);

        DSObject desenObj = new DSObject(paths);

        // 调用脱敏程序处理
        log.info("Start Audio desen");
        // 开始时间
        String startTime = util.getTime();
        // 脱敏开始时间
        long startTimePoint = System.currentTimeMillis();
        algorithmInfo.execute(desenObj, desenParam);
        // 结束时间
        long endTimePoint = System.currentTimeMillis();
        // 脱敏耗时
        long executionTime = endTimePoint - startTimePoint;
        // 脱敏结束时间
        String endTime = util.getTime();
        log.info("Desensitization finished in " + executionTime + "ms");
        log.info("Audio desen finished");
        // 标志脱敏完成
        desenCom = true;
        String globalID = System.currentTimeMillis() + randomNum.nextInt() + "脱敏工具集";
        // 脱敏后文件字节流
        byte[] desenFileBytes = Files.readAllBytes(desenFilePath.toAbsolutePath());
        Long desenFileSize = Files.size(desenFilePath.toAbsolutePath());
        infoBuilders.desenAlg.append(algorithmInfo.getId());
        if (algorithmInfo.getParams() == null) {
            infoBuilders.desenAlgParam.append("无参");
        } else {
            infoBuilders.desenAlgParam.append(algorithmInfo.getParams().get(desenParam).toString());
        }
        // 脱敏级别
        infoBuilders.desenLevel.append(desenParam + 1);
        // 脱敏前类型
        infoBuilders.desenInfoPreIden.append("audio");
        // 脱敏后类型
        infoBuilders.desenInfoAfterIden.append("audio");
        // 脱敏意图
        infoBuilders.desenIntention.append("对音频脱敏");
        // 脱敏要求
        infoBuilders.desenRequirements.append("对声纹脱敏");
        // 脱敏数据类型
        infoBuilders.fileDataType.append(rawFileSuffix);

        fileStorageDetails.setDesenFileBytes(desenFileBytes);
        fileStorageDetails.setDesenFileSize(desenFileSize);
        // 存证系统
        //存证请求  消息版本：中心0x1000，0x1010; 本地0x1100，0x1110
        String evidenceID = util.getSM3Hash((new String(desenFileBytes, StandardCharsets.UTF_8) + util.getTime()).getBytes());
        return LogInfo.builder().fileType(objectMode).desenFileBytes(desenFileBytes).desenFileSize(desenFileSize)
                .desenFileName(desenFileName).desenCom(desenCom).evidenceID(evidenceID).globalID(globalID).objectMode(objectMode)
                .rawFileName(rawFileName).rawFileBytes(rawFileBytes).rawFileSize(rawFileSize).startTime(startTime).endTime(endTime)
                .infoBuilders(infoBuilders).rawFileSuffix(rawFileSuffix).build();
    }

    private LogInfo processGraph(FileStorageDetails fileStorageDetails, String params) throws IOException {
        Boolean desenCom = false;
        DesenInfoStringBuilders infoBuilders = new DesenInfoStringBuilders();
        String objectMode = "graph";

        // 设置原文件保存路径
        String rawFileName = fileStorageDetails.getRawFileName();
        String rawFileSuffix = fileStorageDetails.getRawFileSuffix();
        Path rawFilePath = fileStorageDetails.getRawFilePath();
        String rawFilePathString = fileStorageDetails.getRawFilePathString();
        byte[] rawFileBytes = fileStorageDetails.getRawFileBytes();
        Long rawFileSize = fileStorageDetails.getRawFileSize();

        // 设置脱敏后文件路径信息
        String desenFileName = fileStorageDetails.getDesenFileName();
        Path desenFilePath = fileStorageDetails.getDesenFilePath();
        log.info(desenFilePath.toAbsolutePath().toString());
        String desenFilePathString = fileStorageDetails.getDesenFilePathString();

        // 调用脱敏程序处理
        String desenParam = String.valueOf(params.charAt(params.length() - 1));
        DSObject dsObject = new DSObject(Arrays.asList(rawFilePathString, desenFilePathString));

        // 调用脱敏程序处理
        log.info("Start Graph desen");
        // 开始时间
        String startTime = util.getTime();
        // 脱敏开始时间
        long startTimePoint = System.currentTimeMillis();
        dp.service(dsObject, 7, Integer.parseInt(desenParam));

        // 结束时间
        long endTimePoint = System.currentTimeMillis();
        // 脱敏耗时
        long executionTime = endTimePoint - startTimePoint;
        // 脱敏结束时间
        String endTime = util.getTime();
        logCollectUtil.logExecutionTime(String.valueOf(executionTime), "Graph");
        // 标志脱敏完成
        desenCom = true;
        String globalID = System.currentTimeMillis() + randomNum.nextInt() + "脱敏工具集";
        // 脱敏后文件字节流
        byte[] desenFileBytes = Files.readAllBytes(desenFilePath.toAbsolutePath());
        Long desenFileSize = Files.size(desenFilePath.toAbsolutePath());
        // 脱敏参数
        double[] param = new double[]{5.0, 1.0, 0.2};
        // 脱敏算法
        infoBuilders.desenAlg.append(60);
        infoBuilders.desenAlgParam.append(param[Integer.parseInt(desenParam)]);
        // 脱敏级别
        infoBuilders.desenLevel.append(Integer.parseInt(params) + 1);
        // 脱敏前类型
        infoBuilders.desenInfoPreIden.append(objectMode);
        // 脱敏后类型
        infoBuilders.desenInfoAfterIden.append(objectMode);
        // 脱敏意图
        infoBuilders.desenIntention.append("对图形脱敏");
        // 脱敏要求
        infoBuilders.desenRequirements.append("对图形脱敏");
        // 脱敏数据类型
        infoBuilders.fileDataType.append(rawFileSuffix);

        fileStorageDetails.setDesenFileBytes(desenFileBytes);
        fileStorageDetails.setDesenFileSize(desenFileSize);
        // 存证系统
        //存证请求  消息版本：中心0x1000，0x1010; 本地0x1100，0x1110
        String evidenceID = util.getSM3Hash((new String(desenFileBytes, StandardCharsets.UTF_8) + util.getTime()).getBytes());
        return LogInfo.builder().fileType(objectMode).desenFileBytes(desenFileBytes).desenFileSize(desenFileSize)
                .desenFileName(desenFileName).desenCom(desenCom).evidenceID(evidenceID).globalID(globalID).objectMode(objectMode)
                .rawFileName(rawFileName).rawFileBytes(rawFileBytes).rawFileSize(rawFileSize).startTime(startTime).endTime(endTime)
                .infoBuilders(infoBuilders).rawFileSuffix(rawFileSuffix).build();
    }


    private LogInfo processReplaceVideoBackground(FileStorageDetails fileStorageDetails, String params,
                                                  String algName, FileStorageDetails sheetStorageDetails) throws IOException {
        Boolean desenCom = false;
        DesenInfoStringBuilders infoBuilders = new DesenInfoStringBuilders();
        String objectMode = "video";
        AlgorithmInfo algorithmInfo = algorithmsFactory.getAlgorithmInfoFromName(algName);

        // 设置原文件保存路径
        String rawFileName = fileStorageDetails.getRawFileName();
        String rawFileSuffix = fileStorageDetails.getRawFileSuffix();
        Path rawFilePath = fileStorageDetails.getRawFilePath();
        String rawFilePathString = fileStorageDetails.getRawFilePathString();
        byte[] rawFileBytes = fileStorageDetails.getRawFileBytes();
        Long rawFileSize = fileStorageDetails.getRawFileSize();

        // 设置脱敏后文件路径信息
        String desenFileName = fileStorageDetails.getDesenFileName();
        Path desenFilePath = fileStorageDetails.getDesenFilePath();
        log.info("Video desen file path: {}", desenFilePath.toAbsolutePath().toString());
        String desenFilePathString = fileStorageDetails.getDesenFilePathString();
        // 设置原文件保存路径
        String imageFileName = sheetStorageDetails.getRawFileName();
        String rawBgPathString = sheetStorageDetails.getRawFilePathString();

        String desenParam = String.valueOf(params.charAt(params.length() - 1));

        long startTimePoint;
        long endTimePoint;
        long executionTime = 0;
        String startTime = util.getTime();
        startTimePoint = System.currentTimeMillis();
        // 执行脱敏算法
        List<String> rawData = Arrays.asList(rawFilePathString, rawBgPathString, desenFilePathString);
        DSObject dsObject = new DSObject(rawData);
        // 调用脱敏程序处理
        String resultString = algorithmInfo.execute(dsObject, Integer.valueOf(params)).getList().toString();
        log.info(resultString);
        // 结束时间
        endTimePoint = System.currentTimeMillis();
        executionTime = endTimePoint - startTimePoint;
        String endTime = util.getTime();
        // 脱敏耗时
        logCollectUtil.logExecutionTime(String.valueOf(executionTime), "Video");
        byte[] desenFileBytes = Files.readAllBytes(desenFilePath.toAbsolutePath());
        Long desenFileSize = Files.size(desenFilePath.toAbsolutePath());
        // 标志脱敏完成
        desenCom = true;
        String globalID = System.currentTimeMillis() + randomNum.nextInt() + "脱敏工具集";

        // 脱敏算法
        infoBuilders.desenAlg.append(algorithmInfo.getId());
        // 脱敏参数
        infoBuilders.desenAlgParam.append(rawBgPathString);
        // 脱敏级别
        infoBuilders.desenLevel.append(Integer.parseInt(desenParam) + 1);
        // 脱敏前类型
        infoBuilders.desenInfoPreIden.append("video");
        // 脱敏后类型
        infoBuilders.desenInfoAfterIden.append("video");
        // 脱敏意图
        infoBuilders.desenIntention.append("对视频脱敏");
        // 脱敏要求
        infoBuilders.desenRequirements.append("对视频脱敏");
        // 脱敏数据类型
        infoBuilders.fileDataType.append(rawFileSuffix);

        fileStorageDetails.setDesenFileBytes(desenFileBytes);
        fileStorageDetails.setDesenFileSize(desenFileSize);

        // 存证系统
        //存证请求  消息版本：中心0x1000，0x1010; 本地0x1100，0x1110
        String evidenceID = util.getSM3Hash((new String(desenFileBytes, StandardCharsets.UTF_8) + util.getTime()).getBytes());

        return LogInfo.builder().fileType(objectMode).desenFileBytes(desenFileBytes).desenFileSize(desenFileSize)
                .desenFileName(desenFileName).desenCom(desenCom).evidenceID(evidenceID).globalID(globalID).objectMode(objectMode)
                .rawFileName(rawFileName).rawFileBytes(rawFileBytes).rawFileSize(rawFileSize).startTime(startTime).endTime(endTime)
                .infoBuilders(infoBuilders).rawFileSuffix(rawFileSuffix).build();
    }

    private LogInfo processReplaceFace(FileStorageDetails fileStorageDetails, String params, String algName, FileStorageDetails sheetStorageDetails) throws IOException {
        Boolean desenCom = false;
        DesenInfoStringBuilders infoBuilders = new DesenInfoStringBuilders();
        String objectMode = "image";
        AlgorithmInfo algorithmInfo = algorithmsFactory.getAlgorithmInfoFromName(algName);

        // 设置原文件保存路径
        String rawFileName = fileStorageDetails.getRawFileName();
        String rawFileSuffix = fileStorageDetails.getRawFileSuffix();
        String rawFilePathString = fileStorageDetails.getRawFilePathString();
        byte[] rawFileBytes = fileStorageDetails.getRawFileBytes();
        Long rawFileSize = fileStorageDetails.getRawFileSize();

        // 设置脱敏后文件路径信息
        String desenFileName = fileStorageDetails.getDesenFileName();
        Path desenFilePath = fileStorageDetails.getDesenFilePath();
        log.info("Image desen file path: {}", desenFilePath.toAbsolutePath().toString());
        String desenFilePathString = fileStorageDetails.getDesenFilePathString();

        // 设置替换的人脸图片文件保存路径
        String rawFacePathString = sheetStorageDetails.getRawFilePathString();

        // 设置脱敏后文件路径信息
        // 调用脱敏程序处理
        String desenParam = String.valueOf(params.charAt(params.length() - 1));
        long startTimePoint;
        long endTimePoint;
        long executionTime;
        String startTime;
        // 开始时间
        // 脱敏开始时间
        startTime = util.getTime();
        startTimePoint = System.currentTimeMillis();
        List<String> rawData = Arrays.asList(rawFilePathString, rawFacePathString, desenFilePathString);
        DSObject dsObject = new DSObject(rawData);
        String resultString = algorithmInfo.execute(dsObject, Integer.valueOf(params)).getList().toString();
        if (resultString != null) {
            log.info(resultString);
        }
        // 结束时间
        endTimePoint = System.currentTimeMillis();
        executionTime = endTimePoint - startTimePoint;
        String endTime = util.getTime();
        // 脱敏耗时
        logCollectUtil.logExecutionTime(String.valueOf(executionTime), "Image");

        // 标志脱敏完成
        desenCom = true;
        String globalID = System.currentTimeMillis() + randomNum.nextInt() + "脱敏工具集";
        // 脱敏后文件字节流
        Long desenFileSize = Files.size(desenFilePath.toAbsolutePath());
        byte[] desenFileBytes = Files.readAllBytes(desenFilePath.toAbsolutePath());
        // 脱敏算法
        infoBuilders.desenAlg.append(algorithmInfo.getId());
        // 脱敏参数
        infoBuilders.desenAlgParam.append(rawFacePathString);
        // 脱敏前类型
        infoBuilders.desenInfoPreIden.append("image");
        // 脱敏后类型
        infoBuilders.desenInfoAfterIden.append("image");
        // 脱敏意图
        infoBuilders.desenIntention.append("对图像脱敏");
        // 脱敏要求
        infoBuilders.desenRequirements.append("对图像脱敏");
        // 脱敏数据类型
        infoBuilders.fileDataType.append(rawFileSuffix);
        // 设置脱敏等级
        infoBuilders.desenLevel.append(Integer.parseInt(desenParam) + 1);

        fileStorageDetails.setDesenFileBytes(desenFileBytes);
        fileStorageDetails.setDesenFileSize(desenFileSize);
        // 存证系统
        //存证请求  消息版本：中心0x1000，0x1010; 本地0x1100，0x1110
        String evidenceID = util.getSM3Hash((new String(desenFileBytes, StandardCharsets.UTF_8) + util.getTime()).getBytes());

        return LogInfo.builder().fileType(objectMode).desenFileBytes(desenFileBytes).desenFileSize(desenFileSize)
                .desenFileName(desenFileName).desenCom(desenCom).evidenceID(evidenceID).globalID(globalID).objectMode(objectMode)
                .rawFileName(rawFileName).rawFileBytes(rawFileBytes).rawFileSize(rawFileSize).startTime(startTime).endTime(endTime)
                .infoBuilders(infoBuilders).rawFileSuffix(rawFileSuffix).build();
    }

    private LogInfo processReplaceFaceVideo(FileStorageDetails fileStorageDetails, String params, String algName,
                                            FileStorageDetails sheet) throws IOException {
        Boolean desenCom = false;
        DesenInfoStringBuilders infoBuilders = new DesenInfoStringBuilders();
        String objectMode = "video";
        AlgorithmInfo algorithmInfo = algorithmsFactory.getAlgorithmInfoFromName(algName);

        // 设置原文件保存路径
        String rawFileName = fileStorageDetails.getRawFileName();
        String rawFileSuffix = fileStorageDetails.getRawFileSuffix();
        Path rawFilePath = fileStorageDetails.getRawFilePath();
        String rawFilePathString = fileStorageDetails.getRawFilePathString();
        byte[] rawFileBytes = fileStorageDetails.getRawFileBytes();
        Long rawFileSize = fileStorageDetails.getRawFileSize();

        // 设置原文件保存路径
        // 设置脱敏后文件路径信息
        String desenFileName = fileStorageDetails.getDesenFileName();
        Path desenFilePath = fileStorageDetails.getDesenFilePath();
        log.info("Image desen file path: {}", desenFilePath.toAbsolutePath().toString());
        String desenFilePathString = fileStorageDetails.getDesenFilePathString();

        String rawFacePathString = sheet.getRawFilePathString();

        // 调用脱敏程序处理
        String desenParam = String.valueOf(params.charAt(params.length() - 1));
        long startTimePoint;
        long endTimePoint;
        long executionTime;
        String startTime;
        // 开始时间
        // 脱敏开始时间
        startTime = util.getTime();
        startTimePoint = System.currentTimeMillis();

        // 执行脱敏
        DSObject dsObject = new DSObject(Arrays.asList(rawFilePathString, rawFacePathString, desenFilePathString));
        algorithmInfo.execute(dsObject);
        // 结束时间
        endTimePoint = System.currentTimeMillis();
        // 脱敏耗时
        executionTime = endTimePoint - startTimePoint;
        logCollectUtil.logExecutionTime(String.valueOf(executionTime), objectMode);

        // 脱敏结束时间
        String endTime = util.getTime();
        // 标志脱敏完成
        desenCom = true;
        String globalID = System.currentTimeMillis() + randomNum.nextInt() + "脱敏工具集";
        // rawFileBytes
        byte[] desenFileBytes = Files.readAllBytes(desenFilePath.toAbsolutePath());
        Long desenFileSize = Files.size(desenFilePath.toAbsolutePath());

        // 脱敏算法
        infoBuilders.desenAlg.append(algorithmInfo.getId());
        // 脱敏参数
        infoBuilders.desenAlgParam.append(rawFacePathString);
        // 脱敏级别
        infoBuilders.desenLevel.append(Integer.parseInt(desenParam) + 1);
        // 脱敏前类型
        infoBuilders.desenInfoPreIden.append("video");
        // 脱敏后类型
        infoBuilders.desenInfoAfterIden.append("video");
        // 脱敏意图
        infoBuilders.desenIntention.append("对视频脱敏");
        // 脱敏要求
        infoBuilders.desenRequirements.append("对视频脱敏");
        // 脱敏数据类型
        infoBuilders.fileDataType.append(rawFileSuffix);

        fileStorageDetails.setDesenFileBytes(desenFileBytes);
        fileStorageDetails.setDesenFileSize(desenFileSize);

        // 存证系统
        //存证请求  消息版本：中心0x1000，0x1010; 本地0x1100，0x1110
        String evidenceID = util.getSM3Hash((new String(desenFileBytes, StandardCharsets.UTF_8) + util.getTime()).getBytes());
        return LogInfo.builder().fileType(objectMode).desenFileBytes(desenFileBytes).desenFileSize(desenFileSize)
                .desenFileName(desenFileName).desenCom(desenCom).evidenceID(evidenceID).globalID(globalID).objectMode(objectMode)
                .rawFileName(rawFileName).rawFileBytes(rawFileBytes).rawFileSize(rawFileSize).startTime(startTime).endTime(endTime)
                .infoBuilders(infoBuilders).rawFileSuffix(rawFileSuffix).build();
    }

    /**
     * 执行重脱敏的逻辑
     *
     * @param event
     * @throws Exception
     */
    @Override
    public void redesenExcel(ReDesensitizeEvent event) throws Exception {
        RecEvaResultInv recEvaResultInv = event.getRecEvaResultInv();
        LogManagerEvent logManagerEvent = event.getLogManagerEvent();

        String desenInfoAfterID = recEvaResultInv.getDesenInfoAfterID();
        String[] updateFieldList = recEvaResultInv.getDesenFailedColName().split(",");
        log.info("DesenFailedColName {}", recEvaResultInv.getDesenFailedColName());
        log.info("updateFieldList: {}", Arrays.toString(updateFieldList));
        SendEvaReq evaReq = sendEvaReqService.findByDesenInfoAfterId(desenInfoAfterID);
        // 字段名列表
        String[] attributeNameList = evaReq.getDesenInfoPreIden().split(",");
        String rawFileName = evaReq.getDesenInfoPre();
        String rawFileSuffix = rawFileName.substring(rawFileName.lastIndexOf(".") + 1);
        String[] desenFileNameList = evaReq.getDesenInfoAfter().split("_");
        String[] desenAlgList = evaReq.getDesenAlg().split(",");
        String[] desenLevelList = evaReq.getDesenLevel().split(",");

        Map<String, Integer> attributeDesenMap = new HashMap<>();
        for (int i = 0; i < attributeNameList.length; i++) {
            attributeDesenMap.put(attributeNameList[i], Integer.parseInt(desenLevelList[i]));
        }
        // 取模板名，根据模板名在数据库中找到相应的算法
        String templateName = evaReq.getFileType() + "_param";

        List<ExcelParam> originExcelParam = excelParamService.getParamsByTableName(templateName);
        List<ExcelParam> resultExcelParam = new ArrayList<>();
        for (int i = 0; i < originExcelParam.size(); i++) {
            ExcelParam excelParamTemp = originExcelParam.get(i);
            String colName = excelParamTemp.getColumnName();
            // 根据评测结果针对特定字段提升脱敏等级
            if (Arrays.asList(updateFieldList).contains(colName)) {
                resultExcelParam.add(addDesenLevel(excelParamTemp, attributeDesenMap.get(colName), true));
            } else {
                resultExcelParam.add(addDesenLevel(excelParamTemp, attributeDesenMap.get(colName), false));
            }
        }
        // 将脱敏参数转为字符串，方便接口复用
        String jsonParamString = objectMapper.writeValueAsString(resultExcelParam);
        // 读取原始文件内容
        Path rawFilePath = fileStorageService.getRawFileDirectory().resolve(rawFileName);
        String rawFilePathString = rawFilePath.toAbsolutePath().toString();
        byte[] rawFileBytes = Files.readAllBytes(rawFilePath);
        Long rawFileSize = Files.size(rawFilePath);
        String timeStamp = String.valueOf(System.currentTimeMillis());
        // 构造新的脱敏后文件名
        desenFileNameList[desenFileNameList.length - 1] = timeStamp + "." + rawFileSuffix;
        String recFileName = String.join("_", desenFileNameList);
        log.info("recFileName {}", recFileName);
        Path desenFilePath = fileStorageService.getDesenFileDirectory().resolve(recFileName);
        String desenFilePathString = desenFilePath.toAbsolutePath().toString();

        FileStorageDetails fileStorageDetails = FileStorageDetails.builder()
                .rawFileName(rawFileName)
                .rawFileSuffix(rawFileSuffix)
                .rawFilePath(rawFilePath)
                .rawFilePathString(rawFilePathString)
                .rawFileBytes(rawFileBytes)
                .rawFileSize(rawFileSize)
                .desenFileName(recFileName)
                .desenFileSuffix(rawFileSuffix)
                .desenFilePath(desenFilePath)
                .desenFilePathString(desenFilePathString)
                .build();

        LogInfo logInfo = processExcel(fileStorageDetails, jsonParamString, evaReq.getFileType(), false);
        LogCollectResult logCollectResult = logSenderManager.buildLogCollectResults(logInfo);

        CompletableFuture<ResponseEntity<byte[]>> responseEntityCompletableFuture = logManagerEvent.getResponseEntityCompletableFuture();
        eventPublisher.publishEvent(new LogManagerEvent(this, fileStorageDetails, logCollectResult, responseEntityCompletableFuture));
    }

    @Override
    public void redesenSingleText(ReDesensitizeEvent event) throws Exception {
        RecEvaResultInv recEvaResultInv = event.getRecEvaResultInv();
        LogManagerEvent logManagerEvent = event.getLogManagerEvent();
        String desenInfoAfterID = recEvaResultInv.getDesenInfoAfterID();
        SendEvaReq evaReq = sendEvaReqService.findByDesenInfoAfterId(desenInfoAfterID);
        // 字段名列表
        String rawFileName = evaReq.getDesenInfoPre();
        String rawFileSuffix = rawFileName.substring(rawFileName.lastIndexOf(".") + 1);
        String desenFileName = evaReq.getDesenInfoAfter();
        String[] desenFileNameList = desenFileName.split("_");
        int desenAlg = Integer.parseInt(evaReq.getDesenAlg());
        String algName = algorithmsFactory.getAlgorithmInfoFromId(desenAlg).getName();
        int desenLevel = Integer.parseInt(evaReq.getDesenLevel());
        // 读取原始文件内容
        Path rawFilePath = fileStorageService.getRawFileDirectory().resolve(rawFileName);
        String rawFilePathString = rawFilePath.toAbsolutePath().toString();
        byte[] rawFileBytes = Files.readAllBytes(rawFilePath);
        Long rawFileSize = Files.size(rawFilePath);
        String timeStamp = String.valueOf(System.currentTimeMillis());
        // 构造新的脱敏后文件名
        desenFileNameList[desenFileNameList.length - 1] = timeStamp + "." + rawFileSuffix;
        String recFileName = String.join("_", desenFileNameList);
        log.info("recFileName {}", recFileName);
        Path desenFilePath = fileStorageService.getDesenFileDirectory().resolve(recFileName);
        String desenFilePathString = desenFilePath.toAbsolutePath().toString();

        // 提升脱敏等级，除表格外其他模态的数据的脱敏等级为0，1，2
        if (desenLevel < 3) {
            desenLevel++;
        } else {
            desenLevel = 3;
        }

        FileStorageDetails fileStorageDetails = FileStorageDetails.builder()
                .rawFileName(rawFileName)
                .rawFileSuffix(rawFileSuffix)
                .rawFilePath(rawFilePath)
                .rawFilePathString(rawFilePathString)
                .rawFileBytes(rawFileBytes)
                .rawFileSize(rawFileSize)
                .desenFileName(recFileName)
                .desenFileSuffix(rawFileSuffix)
                .desenFilePath(desenFilePath)
                .desenFilePathString(desenFilePathString)
                .build();

        LogInfo logInfo = processSingleExcel(fileStorageDetails, String.valueOf(desenLevel), algName);
        LogCollectResult logCollectResult = logSenderManager.buildLogCollectResults(logInfo);
        eventPublisher.publishEvent(new LogManagerEvent(this, fileStorageDetails, logCollectResult,
                logManagerEvent.getResponseEntityCompletableFuture()));
    }

    @Override
    public void redesenImage(ReDesensitizeEvent event) throws Exception {
        List<Integer> algIdList = Arrays.asList(40, 41, 42, 43, 44, 45, 46, 48);

        RecEvaResultInv recEvaResultInv = event.getRecEvaResultInv();
        LogManagerEvent logManagerEvent = event.getLogManagerEvent();
        String desenInfoAfterID = recEvaResultInv.getDesenInfoAfterID();
        SendEvaReq evaReq = sendEvaReqService.findByDesenInfoAfterId(desenInfoAfterID);
        // 字段名列表
        String rawFileName = evaReq.getDesenInfoPre();
        String rawFileSuffix = rawFileName.substring(rawFileName.lastIndexOf(".") + 1);
        String desenFileName = evaReq.getDesenInfoAfter();
        String[] desenFileNameList = desenFileName.split("_");
        int desenAlg = Integer.parseInt(evaReq.getDesenAlg());
        String algName = algorithmsFactory.getAlgorithmInfoFromId(desenAlg).getName();
        int desenLevel = Integer.parseInt(evaReq.getDesenLevel());
        // 取模板名，根据模板名在数据库中找到相应的算法
        // 读取原始文件内容
        Path rawFilePath = fileStorageService.getRawFileDirectory().resolve(rawFileName);
        String rawFilePathString = rawFilePath.toAbsolutePath().toString();
        byte[] rawFileBytes = Files.readAllBytes(rawFilePath);
        Long rawFileSize = Files.size(rawFilePath);
        String timeStamp = String.valueOf(System.currentTimeMillis());
        // 构造新的脱敏后文件名
        desenFileNameList[desenFileNameList.length - 1] = timeStamp + "." + rawFileSuffix;
        String recFileName = String.join("_", desenFileNameList);
        log.info("recFileName {}", recFileName);
        Path desenFilePath = fileStorageService.getDesenFileDirectory().resolve(recFileName);
        String desenFilePathString = desenFilePath.toAbsolutePath().toString();

        // 提升脱敏等级，除表格外其他模态的数据的脱敏等级为0，1，2
        desenLevel -= 1;
        if (desenLevel < 2) {
            desenLevel++;
        } else {
            desenLevel = 2;
        }

        if (!algIdList.contains(desenAlg)) {
            desenAlg = 41;
            algName = algorithmsFactory.getAlgorithmInfoFromId(desenAlg).getName();
            desenLevel = 0;
        }

        FileStorageDetails fileStorageDetails = FileStorageDetails.builder()
                .rawFileName(rawFileName)
                .rawFileSuffix(rawFileSuffix)
                .rawFilePath(rawFilePath)
                .rawFilePathString(rawFilePathString)
                .rawFileBytes(rawFileBytes)
                .rawFileSize(rawFileSize)
                .desenFileName(recFileName)
                .desenFileSuffix(rawFileSuffix)
                .desenFilePath(desenFilePath)
                .desenFilePathString(desenFilePathString)
                .build();

        LogInfo logInfo = processImage(fileStorageDetails, String.valueOf(desenLevel), algName);
        LogCollectResult logCollectResult = logSenderManager.buildLogCollectResults(logInfo);
        eventPublisher.publishEvent(new LogManagerEvent(this, fileStorageDetails, logCollectResult,
                logManagerEvent.getResponseEntityCompletableFuture()));
    }

    @Override
    public void redesenVideo(ReDesensitizeEvent event) throws Exception {
        List<Integer> algIdList = Arrays.asList(50, 51, 52, 53, 54, 55);

        RecEvaResultInv recEvaResultInv = event.getRecEvaResultInv();
        LogManagerEvent logManagerEvent = event.getLogManagerEvent();
        String desenInfoAfterID = recEvaResultInv.getDesenInfoAfterID();
        SendEvaReq evaReq = sendEvaReqService.findByDesenInfoAfterId(desenInfoAfterID);
        // 字段名列表
        String rawFileName = evaReq.getDesenInfoPre();
        String rawFileSuffix = rawFileName.substring(rawFileName.lastIndexOf(".") + 1);
        String desenFileName = evaReq.getDesenInfoAfter();
        String[] desenFileNameList = desenFileName.split("_");
        int desenAlg = Integer.parseInt(evaReq.getDesenAlg());
        String algName = algorithmsFactory.getAlgorithmInfoFromId(desenAlg).getName();
        int desenLevel = Integer.parseInt(evaReq.getDesenLevel());

        // 读取原始文件内容
        Path rawFilePath = fileStorageService.getRawFileDirectory().resolve(rawFileName);
        String rawFilePathString = rawFilePath.toAbsolutePath().toString();
        byte[] rawFileBytes = Files.readAllBytes(rawFilePath);
        Long rawFileSize = Files.size(rawFilePath);
        String timeStamp = String.valueOf(System.currentTimeMillis());
        // 构造新的脱敏后文件名
        desenFileNameList[desenFileNameList.length - 1] = timeStamp + "." + rawFileSuffix;

        String recFileName = String.join("_", desenFileNameList);
        log.info("recFileName {}", recFileName);
        Path desenFilePath = fileStorageService.getDesenFileDirectory().resolve(recFileName);
        String desenFilePathString = desenFilePath.toAbsolutePath().toString();

        // 提升脱敏等级，除表格外其他模态的数据的脱敏等级为0，1，2
        desenLevel -= 1;
        if (desenLevel < 2) {
            desenLevel++;
        } else {
            desenLevel = 2;
        }

        if (!algIdList.contains(desenAlg)) {
            desenAlg = 51;
            algName = algorithmsFactory.getAlgorithmInfoFromId(desenAlg).getName();
            desenLevel = 0;
        }

        FileStorageDetails fileStorageDetails = FileStorageDetails.builder()
                .rawFileName(rawFileName)
                .rawFileSuffix(rawFileSuffix)
                .rawFilePath(rawFilePath)
                .rawFilePathString(rawFilePathString)
                .rawFileBytes(rawFileBytes)
                .rawFileSize(rawFileSize)
                .desenFileName(recFileName)
                .desenFileSuffix(rawFileSuffix)
                .desenFilePath(desenFilePath)
                .desenFilePathString(desenFilePathString)
                .build();

        LogInfo logInfo = processVideo(fileStorageDetails, String.valueOf(desenLevel), algName);
        LogCollectResult logCollectResult = logSenderManager.buildLogCollectResults(logInfo);
        eventPublisher.publishEvent(new LogManagerEvent(this, fileStorageDetails, logCollectResult,
                logManagerEvent.getResponseEntityCompletableFuture()));
    }

    @Override
    public void redesenAudio(ReDesensitizeEvent event) throws Exception {
        List<Integer> algIdList = Arrays.asList(70, 72, 73, 74, 75, 76, 77);

        RecEvaResultInv recEvaResultInv = event.getRecEvaResultInv();
        LogManagerEvent logManagerEvent = event.getLogManagerEvent();
        String desenInfoAfterID = recEvaResultInv.getDesenInfoAfterID();
        SendEvaReq evaReq = sendEvaReqService.findByDesenInfoAfterId(desenInfoAfterID);
        // 字段名列表
        String rawFileName = evaReq.getDesenInfoPre();
        String rawFileSuffix = rawFileName.substring(rawFileName.lastIndexOf(".") + 1);
        String desenFileName = evaReq.getDesenInfoAfter();
        String[] desenFileNameList = desenFileName.split("_");
        int desenAlg = Integer.parseInt(evaReq.getDesenAlg());
        String algName = algorithmsFactory.getAlgorithmInfoFromId(desenAlg).getName();
        int desenLevel = Integer.parseInt(evaReq.getDesenLevel());

        // 读取原始文件内容
        Path rawFilePath = fileStorageService.getRawFileDirectory().resolve(rawFileName);
        String rawFilePathString = rawFilePath.toAbsolutePath().toString();
        byte[] rawFileBytes = Files.readAllBytes(rawFilePath);
        Long rawFileSize = Files.size(rawFilePath);
        String timeStamp = String.valueOf(System.currentTimeMillis());
//        System.out.println(rawFileName.split("_")[1]);
        // 构造新的脱敏后文件名
        desenFileNameList[desenFileNameList.length - 1] = timeStamp + "." + rawFileSuffix;

        String recFileName = String.join("_", desenFileNameList);
        log.info("recFileName {}", recFileName);
        Path desenFilePath = fileStorageService.getDesenFileDirectory().resolve(recFileName);
        String desenFilePathString = desenFilePath.toAbsolutePath().toString();

        // 提升脱敏等级，除表格外其他模态的数据的脱敏等级为0，1，2
        desenLevel -= 1;
        if (desenLevel < 2) {
            desenLevel++;
        } else {
            desenLevel = 2;
        }

        if (!algIdList.contains(desenAlg)) {
            desenAlg = 74;
            algName = algorithmsFactory.getAlgorithmInfoFromId(desenAlg).getName();
            desenLevel = 0;
        }

        FileStorageDetails fileStorageDetails = FileStorageDetails.builder()
                .rawFileName(rawFileName)
                .rawFileSuffix(rawFileSuffix)
                .rawFilePath(rawFilePath)
                .rawFilePathString(rawFilePathString)
                .rawFileBytes(rawFileBytes)
                .rawFileSize(rawFileSize)
                .desenFileName(recFileName)
                .desenFileSuffix(rawFileSuffix)
                .desenFilePath(desenFilePath)
                .desenFilePathString(desenFilePathString)
                .build();

        LogInfo logInfo = processAudio(fileStorageDetails, String.valueOf(desenLevel), algName);
        LogCollectResult logCollectResult = logSenderManager.buildLogCollectResults(logInfo);
        eventPublisher.publishEvent(new LogManagerEvent(this, fileStorageDetails, logCollectResult,
                logManagerEvent.getResponseEntityCompletableFuture()));
    }


    @Override
    public void redesenGraph(ReDesensitizeEvent event) throws Exception {
        RecEvaResultInv recEvaResultInv = event.getRecEvaResultInv();
        LogManagerEvent logManagerEvent = event.getLogManagerEvent();
        String desenInfoAfterID = recEvaResultInv.getDesenInfoAfterID();
        SendEvaReq evaReq = sendEvaReqService.findByDesenInfoAfterId(desenInfoAfterID);
        // 字段名列表
        String rawFileName = evaReq.getDesenInfoPre();
        String rawFileSuffix = rawFileName.substring(rawFileName.lastIndexOf(".") + 1);
        String desenFileName = evaReq.getDesenInfoAfter();
        String[] desenFileNameList = desenFileName.split("_");
        int desenAlg = Integer.parseInt(evaReq.getDesenAlg());
        String algName = algorithmsFactory.getAlgorithmInfoFromId(desenAlg).getName();
        int desenLevel = Integer.parseInt(evaReq.getDesenLevel());

        // 读取原始文件内容
        Path rawFilePath = fileStorageService.getRawFileDirectory().resolve(rawFileName);
        String rawFilePathString = rawFilePath.toAbsolutePath().toString();
        byte[] rawFileBytes = Files.readAllBytes(rawFilePath);
        Long rawFileSize = Files.size(rawFilePath);
        String timeStamp = String.valueOf(System.currentTimeMillis());
        // System.out.println(rawFileName.split("_")[1]);
        // 构造新的脱敏后文件名
        desenFileNameList[desenFileNameList.length - 1] = timeStamp + "." + rawFileSuffix;

        String recFileName = String.join("_", desenFileNameList);
        log.info("recFileName {}", recFileName);
        Path desenFilePath = fileStorageService.getDesenFileDirectory().resolve(recFileName);
        String desenFilePathString = desenFilePath.toAbsolutePath().toString();

        // 提升脱敏等级，除表格外其他模态的数据的脱敏等级为0，1，2
        desenLevel -= 1;
        if (desenLevel < 2) {
            desenLevel++;
        } else {
            desenLevel = 2;
        }

        FileStorageDetails fileStorageDetails = FileStorageDetails.builder()
                .rawFileName(rawFileName)
                .rawFileSuffix(rawFileSuffix)
                .rawFilePath(rawFilePath)
                .rawFilePathString(rawFilePathString)
                .rawFileBytes(rawFileBytes)
                .rawFileSize(rawFileSize)
                .desenFileName(recFileName)
                .desenFileSuffix(rawFileSuffix)
                .desenFilePath(desenFilePath)
                .desenFilePathString(desenFilePathString)
                .build();

        LogInfo logInfo = processGraph(fileStorageDetails, String.valueOf(desenLevel));
        LogCollectResult logCollectResult = logSenderManager.buildLogCollectResults(logInfo);
        eventPublisher.publishEvent(new LogManagerEvent(this, fileStorageDetails, logCollectResult,
                logManagerEvent.getResponseEntityCompletableFuture()));
    }


    @Override
    public ResponseEntity<byte[]> dealImage(FileStorageDetails fileStorageDetails, String params, String algName) throws IOException {
        HttpHeaders errorHttpHeaders = new HttpHeaders();
        errorHttpHeaders.add(HttpHeaders.CONTENT_TYPE, "text/plain");
        // 处理图片
        LogInfo logInfo = processImage(fileStorageDetails, params, algName);
        LogCollectResult logCollectResult = logSenderManager.buildLogCollectResults(logInfo);
        // 构造响应结果
        CompletableFuture<ResponseEntity<byte[]>> responseEntityCompletableFuture = new CompletableFuture<>();
        // 获取文件信息
        byte[] rawFileBytes = logInfo.getRawFileBytes();
        byte[] desenFileBytes = logInfo.getDesenFileBytes();
        String desenFileName = logInfo.getDesenFileName();
        // 选择不同的日志发送方式
        // 选择首先发给评测系统评测
        if (ifSendToEvaFirst) {
            eventPublisher.publishEvent(new LogManagerEvent(this, fileStorageDetails,
                    logCollectResult, responseEntityCompletableFuture));
        }
        // 直接发给四个系统
        else {
            logSenderManager.submitToFourSystems(logCollectResult, rawFileBytes, desenFileBytes);
            HttpHeaders headers = new HttpHeaders();
            if (logInfo.getRawFileSuffix().equals("png")) {
                headers.setContentType(MediaType.IMAGE_PNG);
            } else {
                headers.setContentType(MediaType.IMAGE_JPEG);
            }
            headers.setContentDispositionFormData("attachment", desenFileName); // 设置文件名
            responseEntityCompletableFuture.complete(new ResponseEntity<>(desenFileBytes, headers, HttpStatus.OK));
        }
        // 在此处等待响应返回
        // TODO: Controller Advice
        try {
            return responseEntityCompletableFuture.get(10, TimeUnit.MINUTES);
        } catch (TimeoutException e) {
            log.error("等待处理结果超时：{}", e.getMessage());
            String errorMsg = "等待处理结果超时";
            return ResponseEntity.status(500).headers(errorHttpHeaders).body(errorMsg.getBytes());
        } catch (InterruptedException e) {
            log.error("等待处理结果时异常中断：{}", e.getMessage());
            String errorMsg = "等待处理结果时异常中断";
            return ResponseEntity.status(500).headers(errorHttpHeaders).body(errorMsg.getBytes());
        } catch (ExecutionException e) {
            log.error("等待处理结果时执行异常：{}", e.getMessage());
            String errorMsg = "等待处理结果时执行异常";
            return ResponseEntity.status(500).headers(errorHttpHeaders).body(errorMsg.getBytes());
        }

    }

    @Override
    public ResponseEntity<byte[]> dealImage(MultipartFile file, String params, String algName) throws IOException {
        Boolean desenCom = false;
        DesenInfoStringBuilders infoBuilders = new DesenInfoStringBuilders();
        String objectMode = "image";
        // 设置文件时间戳
        String fileTimeStamp = String.valueOf(System.currentTimeMillis());
        // 设置原文件信息
        if (file.getOriginalFilename() == null) {
            throw new IOException("Input file name is null");
        }
        // 设置原文件保存路径
        String rawFileName = fileTimeStamp + file.getOriginalFilename();
        String rawFileSuffix = rawFileName.substring(rawFileName.lastIndexOf(".") + 1);
        Path rawFilePath = rawFileDirectory.resolve(rawFileName);
        String rawFilePathString = rawFilePath.toAbsolutePath().toString();
        byte[] rawFileBytes = file.getBytes();
        Long rawFileSize = file.getSize();
        // 保存源文件
        file.transferTo(rawFilePath.toAbsolutePath());
        // 设置脱敏后文件路径信息
        String desenFileName = "desen_" + rawFileName;
        Path desenFilePath = desenFileDirectory.resolve(desenFileName);
        String desenFilePathString = desenFilePath.toAbsolutePath().toString();
        // 脱敏参数处理
        Integer desenParam = Integer.valueOf(params);
        // 调用脱敏程序处理
        DSObject dsObject = new DSObject(Arrays.asList(rawFilePathString, desenFilePathString));
        // 脱敏开始时间
        AlgorithmInfo algorithmInfo = algorithmsFactory.getAlgorithmInfoFromName(algName);
        log.info("Start image desen");
        String startTime = util.getTime();
        long startTimePoint = System.currentTimeMillis();
        algorithmInfo.execute(dsObject, desenParam);
        // 脱敏后收集信息
        // 结束时间
        long endTimePoint = System.currentTimeMillis();
        // 脱敏结束时间
        String endTime = util.getTime();
        // 脱敏耗时
        long executionTime = endTimePoint - startTimePoint;
        logCollectUtil.logExecutionTime(String.valueOf(executionTime), objectMode);

        // 标志脱敏完成
        desenCom = true;
        // 设置globalID
        String globalID = System.currentTimeMillis() + randomNum.nextInt() + "脱敏工具集";
        // 脱敏后文件字节流
        byte[] desenFileBytes = Files.readAllBytes(desenFilePath.toAbsolutePath());
        Long desenFileSize = Files.size(desenFilePath.toAbsolutePath());

        infoBuilders.desenAlg.append(algorithmInfo.getId());
        if (algorithmInfo.getParams() == null) {
            infoBuilders.desenAlgParam.append("无参");
        } else {
            infoBuilders.desenAlgParam.append(algorithmInfo.getParams().get(desenParam).toString());
        }
        // TODO: 非失真算法的脱敏级别
        infoBuilders.desenLevel.append(desenParam + 1);
        // 脱敏前类型
        infoBuilders.desenInfoPreIden.append("image");
        // 脱敏后类型
        infoBuilders.desenInfoAfterIden.append("image");
        // 脱敏意图
        infoBuilders.desenIntention.append("对图像脱敏");
        // 脱敏要求
        infoBuilders.desenRequirements.append("对图像脱敏");
        // 脱敏数据类型
        infoBuilders.fileDataType.append(rawFileSuffix);

        String evidenceID = util.getSM3Hash((new String(desenFileBytes, StandardCharsets.UTF_8) +
                util.getTime()).getBytes());

        logSenderManager.submitToFourSystems(globalID, evidenceID, desenCom, objectMode, infoBuilders, rawFileName,
                rawFileBytes, rawFileSize, desenFileName, desenFileBytes, desenFileSize, objectMode, rawFileSuffix,
                startTime, endTime);
        // 读取文件返回
        HttpHeaders headers = new HttpHeaders();
        if ((rawFileSuffix.equals("png"))) {
            headers.setContentType(MediaType.IMAGE_PNG);
        } else {
            headers.setContentType(MediaType.IMAGE_JPEG);
        }
        headers.setContentDispositionFormData("attachment", desenFileName); // 设置文件名

        return ResponseEntity.ok().headers(headers).body(desenFileBytes);

    }
    private Map<Integer, List<Object>> preprocessSheet(Sheet sheet, List<ExcelParam> excelParamList, int totalRowNum,
                                                       List<String> fieldNameRow, int columnCount) {
        Map<Integer, List<Object>> preprocessedData = new HashMap<>();
        Map<String, ExcelParam> excelParamMap = excelParamList.parallelStream().collect(Collectors.toMap(
                param -> param.getColumnName().trim(), Function.identity()));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DataFormatter dataFormatter = new DataFormatter();

        // 对表格数据进行预处理
        for (int columnIndex = 0; columnIndex < columnCount; columnIndex++) {
            String columnName = fieldNameRow.get(columnIndex);
            log.info("Current preprocessed column name: " + columnName);
            // 当前列操作脱敏的参数
            ExcelParam excelParam = null;
            // 遍历模板中的列名，找到匹配的脱敏参数
            excelParam = excelParamMap.get(columnName.trim());
            List<Object> objs = new ArrayList<>();
            // 从第一行开始取
            for (int rowIndex = 1; rowIndex <= totalRowNum; rowIndex++) {
                Row row = sheet.getRow(rowIndex);
                if (row != null) {
                    Cell cell = row.getCell(columnIndex);
                    // 如果单元格为空
                    if (cell == null || cell.getCellType() == CellType.BLANK || cell.getCellType() == CellType._NONE) {
                        objs.add(null);
                        continue;
                    }
                    // 如果表格中出现单元格中有null字符串的情况
                    if (cell.getCellType() == CellType.STRING && cell.getStringCellValue().equalsIgnoreCase("null")) {
                        objs.add(null);
                        continue;
                    }
                    if (cell.getCellType() == CellType.STRING && cell.getStringCellValue().equalsIgnoreCase("")) {
                        objs.add(null);
                        continue;
                    }
                    // 日期类型
                    if (excelParam.getDataType() == 4) {
                        switch (cell.getCellType()) {
                            case STRING:
                                // 如果单元格是字符串类型，尝试解析为日期
                                String dateString = cell.getStringCellValue();
                                java.util.Date date = dateParseUtil.parseDate(dateString);
                                if (date != null) {
                                    String formattedDate = sdf.format(date);
                                    objs.add(formattedDate);
                                } else {
                                    log.error("Invalid Date String: " + dateString);
                                }
                                break;
                            case NUMERIC:
                                if (DateUtil.isCellDateFormatted(cell)) {
                                    // 如果单元格是日期类型
                                    java.util.Date numericDate = cell.getDateCellValue();
                                    String formattedDate = sdf.format(numericDate);
//                                    System.out.println("Formatted Date from Numeric: " + formattedDate);
                                    objs.add(formattedDate);
                                } else {
                                    String formatCellValue = dataFormatter.formatCellValue(cell);
                                    java.util.Date date2 = dateParseUtil.parseDate(formatCellValue);
                                    if (date2 != null) {
                                        String formattedDate = sdf.format(date2);
                                        objs.add(formattedDate);
                                    } else {
                                        log.error("Invalid Date String: " + formatCellValue);
                                    }
                                }
                                break;
                            default:
                                log.error("Unsupported Cell Type: " + cell.getCellType());
                                break;
                        }
                    }
                    // 文本型数据
                    else if (excelParam.getDataType() == 3) {
                        if (cell.getCellType() == CellType.NUMERIC) {
                            double cellTemp = cell.getNumericCellValue();
                            if (cellTemp != (long) cellTemp) {
                                objs.add(cellTemp);
                            } else {
                                objs.add((long) cellTemp);
                            }
                        } else {
                            objs.add(cell.getStringCellValue());
                        }
                    }
                    // 编码型数据
                    else if (excelParam.getDataType() == 1) {
                        if (cell.getCellType() == CellType.NUMERIC) {
                            objs.add(String.valueOf(cell.getNumericCellValue()));
                        } else {
                            objs.add(cell.getStringCellValue());
                        }
                    }
                    // 数值型数据
                    else if (excelParam.getDataType() == 0) {
                        if (cell.getCellType() == CellType.NUMERIC) {
                            objs.add(String.valueOf(cell.getNumericCellValue()));
                        } else {
                            objs.add(cell.getStringCellValue());
                        }
                    } else {
                        objs.add(cell);
                    }
                }
            }
            preprocessedData.put(columnIndex, objs);
        }
        return preprocessedData;
    }

    private Map<Integer, List<?>> realDealExcel(Map<Integer, List<Object>> preprocessedData, DesenInfoStringBuilders infoBuilders,
                                                List<ExcelParam> excelParamList, int totalRowNum,
                                                List<String> fieldNameRow, int columnCount) throws IOException {
        //  脱敏，逐列处理
        Map<String, ExcelParam> excelParamMap = excelParamList.parallelStream().collect(Collectors.toMap(
                param -> param.getColumnName().trim(), Function.identity()));
        Map<Integer, List<?>> desenResult = new HashMap<>();
        Long startTimePoint = System.currentTimeMillis();
        // 脱敏
        log.info("开始脱敏");
        for (int columnIndex = 0; columnIndex < columnCount; columnIndex++) {
            // 取列名
            String columnName = fieldNameRow.get(columnIndex).trim();
            log.info("Current desen column name: " + columnName);
            // 当前列操作脱敏的参数
            // 遍历模板中的列名，找到匹配的脱敏参数
            ExcelParam excelParam = excelParamMap.get(columnName);
            if (excelParam == null) {
                log.error("Current column name does't match any column in the template");
                throw new IOException("Current column name does't match any column in the template");
            }

            int algoNum = excelParam.getK();
            AlgorithmInfo algorithmInfo = algorithmsFactory.getAlgorithmInfoFromId(algoNum);
//            log.info("Excel Param: {}", excelParam);
            addDataTypeAndInfoIden(infoBuilders, excelParam, columnName, algoNum);
            // 取列数据
            List<Object> objs = preprocessedData.get(columnIndex);
            // 添加脱敏参数
            infoBuilders.desenAlgParam.append(
                    excelParam.getTmParam() == 0 ? "没有脱敏," :
                            algorithmInfo.getParams().get(excelParam.getTmParam() - 1) + ","
            );
            infoBuilders.desenRequirements.append(columnName).append(algorithmInfo.getRequirement()).append(",");
            desenResult.put(columnIndex, getDsList(algorithmInfo, new DSObject(objs), excelParam));
        }

        long endTimePoint = System.currentTimeMillis();
        long totalMilliSeconds = endTimePoint - startTimePoint;
        double everyColumnTime = totalMilliSeconds / (double) columnCount;
        double singleCellTime = everyColumnTime / totalRowNum;
        double cellsInOneSecond = 1000 / singleCellTime;

        log.info("脱敏精确总用时：{} ms", endTimePoint - startTimePoint);
        log.info("脱敏每列用时：{} ms", everyColumnTime);
        log.info("每秒钟可脱敏单元格数量：{}", cellsInOneSecond);
        return desenResult;

    }

    @Override
    public ResponseEntity<byte[]> dealExcel(FileStorageDetails fileStorageDetails, String params, String sheetName,
                                            Boolean ifSaveExcelParams) throws IOException, ExecutionException, InterruptedException, TimeoutException {

        LogInfo logInfo = processExcel(fileStorageDetails, params, sheetName, ifSaveExcelParams);
        // 构造日志收集结果
        LogCollectResult logCollectResult = logSenderManager.buildLogCollectResults(logInfo);
        // 构造响应结果
        CompletableFuture<ResponseEntity<byte[]>> responseEntityCompletableFuture = new CompletableFuture<>();
        // 获取文件信息
        byte[] rawFileBytes = logInfo.getRawFileBytes();
        byte[] desenFileBytes = logInfo.getDesenFileBytes();
        String desenFileName = logInfo.getDesenFileName();
        // 如果没进行性能测试，则选择不同的日志发送方式
        // 选择首先发给评测系统评测
        if (!ifPerformanceTest) {
            if (ifSendToEvaFirst) {
                eventPublisher.publishEvent(new LogManagerEvent(this, fileStorageDetails,
                        logCollectResult, responseEntityCompletableFuture));
            }
            // 直接发给四个系统
            else {
                logSenderManager.submitToFourSystems(logCollectResult, rawFileBytes, desenFileBytes);
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
                headers.setContentDispositionFormData("attachment", desenFileName); // 设置文件名
                responseEntityCompletableFuture.complete(new ResponseEntity<>(desenFileBytes, headers, HttpStatus.OK));
            }
        } else {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", desenFileName); // 设置文件名
            responseEntityCompletableFuture.complete(new ResponseEntity<>(desenFileBytes, headers, HttpStatus.OK));
        }
        // 在此处等待响应返回
        return responseEntityCompletableFuture.get(10, TimeUnit.MINUTES);

    }

    @Override
    public ResponseEntity<byte[]> dealExcel(MultipartFile file, String params, String sheetName, Boolean ifSaveExcelParam) throws IOException {
        String sheetTemplate = sheetName.split("_")[0];
        Boolean desenCom = false;
        DesenInfoStringBuilders infoBuilders = new DesenInfoStringBuilders();
        String objectMode = "text";

        // 设置文件时间戳
        String fileTimeStamp = String.valueOf(System.currentTimeMillis());
        // 设置原文件信息
        if (file.getOriginalFilename() == null) {
            throw new IOException("Input file name is null");
        }
        // 设置原文件保存路径
        String rawFileName = fileTimeStamp + "_" + file.getOriginalFilename();
        String rawFileSuffix = rawFileName.substring(rawFileName.lastIndexOf(".") + 1);
        Path rawFilePath = rawFileDirectory.resolve(rawFileName);
        String rawFilePathString = rawFilePath.toAbsolutePath().toString();
        byte[] rawFileBytes = file.getBytes();
        Long rawFileSize = file.getSize();
        // 保存源文件
        file.transferTo(rawFilePath.toAbsolutePath());

        // 设置脱敏后文件路径信息
        String desenFileName = "desen_" + rawFileName;
        Path desenFilePath = desenFileDirectory.resolve(desenFileName);
        log.info(desenFilePath.toAbsolutePath().toString());
        String desenFilePathString = desenFilePath.toAbsolutePath().toString();

        // 读取excel文件
        InputStream inputStream = Files.newInputStream(rawFilePath);
        Workbook workbook = new XSSFWorkbook(inputStream);
        Sheet sheet = workbook.getSheetAt(0);

        //脱敏参数处理,转为json
        List<ExcelParam> excelParamList = logCollectUtil.jsonStringToParams(params);
        // 保存参数到数据库中
        saveExcelParamsToDatabase(sheetName, ifSaveExcelParam, excelParamList);

        // 数据类型
        List<Integer> dataType = new ArrayList<>();
        // 数据行数
        int totalRowNum = sheet.getLastRowNum();
        // 字段名行
        Row fieldNameRow = sheet.getRow(0);
        // 列数
        int columnCount = fieldNameRow.getPhysicalNumberOfCells(); // 获取列数
        log.info("Total column number is " + columnCount);
        // 调用脱敏程序处理
        log.info("Start Excel file desen");
        // 脱敏开始时间
        String startTime = util.getTime();
        long startTimePoint = System.nanoTime();
        //  脱敏，逐列处理
        for (int columnIndex = 0; columnIndex < columnCount; columnIndex++) {
            DataFormatter dataFormatter = new DataFormatter();
            // 取列名
            String columnName = fieldNameRow.getCell(columnIndex).toString();
            log.info("Current column name: " + columnName);

            // 当前列操作脱敏的参数
            ExcelParam excelParam = null;
            // 遍历模板中的列名，找到匹配的脱敏参数
            for (ExcelParam param : excelParamList) {
                if (columnName.trim().equals(param.getColumnName().trim())) {
                    excelParam = param;
                }
            }
            if (excelParam == null) {
                log.error("Current column name does't match any column in the template");
                throw new IOException("Current column name does't match any column in the template");
            }

            int algoNum = excelParam.getK();
            AlgorithmInfo algorithmInfo = algorithmsFactory.getAlgorithmInfoFromId(algoNum);
            log.info("Excel Param: {}", excelParam);
            dataType.add(excelParam.getDataType());

            addDataTypeAndInfoIden(infoBuilders, excelParam, columnName, algoNum);

            // 取列数据
            List<Object> objs = new ArrayList<>();
            // 从第一列开始取
            for (int rowIndex = 1; rowIndex <= totalRowNum; rowIndex++) {
                Row row = sheet.getRow(rowIndex);
                if (row != null) {
                    Cell cell = row.getCell(columnIndex);
                    // 如果单元格为空
                    if (cell == null || cell.getCellType() == CellType.BLANK || cell.getCellType() == CellType._NONE) {
                        objs.add(null);
                        continue;
                    }
                    // 如果表格中出现单元格中有null字符串的情况
                    if (cell.getCellType() == CellType.STRING && cell.getStringCellValue().equalsIgnoreCase("null")) {
                        objs.add(null);
                        continue;
                    }
                    if (cell.getCellType() == CellType.STRING && cell.getStringCellValue().equalsIgnoreCase("")) {
                        objs.add(null);
                        continue;
                    }
                    // 日期类型
                    if (excelParam.getDataType() == 4) {
//                        objs.add(dataFormatter.formatCellValue(cell));
                        switch (cell.getCellType()) {
                            case STRING:
                                // 如果单元格是字符串类型，尝试解析为日期
                                String dateString = cell.getStringCellValue();
                                java.util.Date date = dateParseUtil.parseDate(dateString);
                                if (date != null) {
                                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                                    String formattedDate = sdf.format(date);
                                    objs.add(formattedDate);
                                } else {
                                    log.error("Invalid Date String: " + dateString);
                                }
                                break;
                            case NUMERIC:
                                if (DateUtil.isCellDateFormatted(cell)) {
                                    // 如果单元格是日期类型
                                    java.util.Date numericDate = cell.getDateCellValue();
                                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                                    String formattedDate = sdf.format(numericDate);
//                                    System.out.println("Formatted Date from Numeric: " + formattedDate);
                                    objs.add(formattedDate);
                                } else {
                                    objs.add(dataFormatter.formatCellValue(cell));
                                }
                                break;
                            default:
                                log.error("Unsupported Cell Type: " + cell.getCellType());
                                break;
                        }
                    }
                    // 文本型数据
                    else if (excelParam.getDataType() == 3) {
                        if (cell.getCellType() == CellType.NUMERIC) {
                            double cellTemp = cell.getNumericCellValue();
                            if (cellTemp != (long) cellTemp) {
                                objs.add(cellTemp);
                            } else {
                                objs.add((long) cellTemp);
                            }
//                            if (sheetName.contains("telecomclient") && (columnName.equals("联系电话"))) {
////                                log.info(df.format(cell.getNumericCellValue()));
//                                objs.add(df.format(cell.getNumericCellValue()));
//                            } else {
//                                objs.add(cell);
//                            }
                        } else {
                            objs.add(cell.getStringCellValue());
                        }
                    }
                    // 编码型数据
                    else if (excelParam.getDataType() == 1) {
                        if (cell.getCellType() == CellType.NUMERIC) {
                            objs.add(String.valueOf(cell.getNumericCellValue()));
                        } else {
                            objs.add(cell.getStringCellValue());
                        }
                    }
                    // 数值型数据
                    else if (excelParam.getDataType() == 0) {
                        if (cell.getCellType() == CellType.NUMERIC) {
                            objs.add(String.valueOf(cell.getNumericCellValue()));
                        } else {
                            objs.add(cell.getStringCellValue());
                        }
                    } else {
                        objs.add(cell);
                    }
                }
            }
            int columnDataType = excelParam.getDataType();

            // 添加脱敏参数
            infoBuilders.desenAlgParam.append(
                    excelParam.getTmParam() == 0 ? "没有脱敏," :
                            algorithmInfo.getParams().get(excelParam.getTmParam() - 1) + ","
            );

            switch (columnDataType) {
                // 数值型数据
                case 0: {
                    System.out.println(objs.size());
                    List<Double> tempResult;
                    switch (algoNum) {
                        // 差分隐私laplace噪声
                        case 3: {
                            // 脱敏要求
                            infoBuilders.desenRequirements.append(excelParam.getColumnName()).append("添加差分隐私Laplace噪声,");
                            // 脱敏
                            DSObject rawData = new DSObject(objs);
                            tempResult = getDsList(algorithmInfo, rawData, excelParam);
                            // 写列数据
                            util.write2Excel(sheet, totalRowNum, columnIndex, tempResult);
                            break;
                        }

                        // 基于随机均匀噪声的数值加噪算法
                        case 5: {
                            // 脱敏要求
                            infoBuilders.desenRequirements.append(excelParam.getColumnName()).append("添加随机均匀噪声,");
                            // 脱敏
                            DSObject rawData = new DSObject(objs);
                            tempResult = getDsList(algorithmInfo, rawData, excelParam);
                            // 写列数据
                            util.write2Excel(sheet, totalRowNum, columnIndex, tempResult);
                            break;
                        }

                        // 基于随机拉普拉斯噪声的数值加噪算法
                        case 6: {
                            // 脱敏要求
                            infoBuilders.desenRequirements.append(excelParam.getColumnName()).append("添加随机laplace噪声,");
                            // 脱敏
                            DSObject rawData = new DSObject(objs);
                            tempResult = getDsList(algorithmInfo, rawData, excelParam);
                            // 写列数据
                            util.write2Excel(sheet, totalRowNum, columnIndex, tempResult);
                            break;
                        }
                        // 基于随机高斯噪声的数值加噪算法
                        case 7: {
                            // 脱敏要求
                            infoBuilders.desenRequirements.append(excelParam.getColumnName()).append("添加随机高斯噪声,");
                            // 脱敏
                            DSObject rawData = new DSObject(objs);
                            tempResult = getDsList(algorithmInfo, rawData, excelParam);
                            // 写列数据
                            util.write2Excel(sheet, totalRowNum, columnIndex, tempResult);
                            break;
                        }
                        // 数值偏移
                        case 8: {
                            // 脱敏要求
                            infoBuilders.desenRequirements.append(excelParam.getColumnName()).append("数值偏移,");
                            // 脱敏
                            DSObject rawData = new DSObject(objs);
                            tempResult = getDsList(algorithmInfo, rawData, excelParam);
                            // 写列数据
                            util.write2Excel(sheet, totalRowNum, columnIndex, tempResult);
                            break;

                        }
                        // 数值取整
                        case 9: {
                            // 脱敏要求
                            infoBuilders.desenRequirements.append(excelParam.getColumnName()).append("数值取整,");
                            // 脱敏
                            DSObject rawData = new DSObject(objs);
                            List<String> list = getDsList(algorithmInfo, rawData, excelParam);
                            // 写列数据
                            if (excelParam.getTmParam() == 0) {
                                util.write2Excel(sheet, totalRowNum, columnIndex, objs);
                            } else {
                                util.write2Excel(sheet, totalRowNum, columnIndex, list);
                            }
                            break;
                        }
                        // 数值映射
                        case 10: {
                            // 脱敏要求
                            infoBuilders.desenRequirements.append(excelParam.getColumnName()).append("数值映射,");
                            // 脱敏
                            DSObject rawData = new DSObject(objs);
                            tempResult = getDsList(algorithmInfo, rawData, excelParam);
                            // 写列数据
                            util.write2Excel(sheet, totalRowNum, columnIndex, tempResult);
                            break;
                        }
//                        default: {
//                            // 脱敏算法参数
//                            Map<Integer, String> map = new HashMap<>();
//                            map.put(0, "没有脱敏,");
//                            map.put(1, "10,");
//                            map.put(2, "30,");
//                            map.put(3, "50,");
//                            infoBuilders.desenAlgParam.append(map.get(excelParam.getTmParam()));
//                            // 脱敏要求
//                            infoBuilders.desenRequirements.append(excelParam.getColumnName()).append("进行分组置换,");
//                            // 脱敏
//                            tempResult = dpUtil.k_NumberCode(objs, excelParam.getTmParam());
//                            // 写列数据
//                            util.write2Excel(sheet, totalRowNum, columnIndex, tempResult);
//                            break;
//                        }
                    }
                    break;
                }

                case 1: {
                    // 脱敏要求
                    infoBuilders.desenRequirements.append(excelParam.getColumnName()).append("随机扰动,");
                    //
                    DSObject rawData = new DSObject(objs);
                    List<String> dpCode = getDsList(algorithmInfo, rawData, excelParam);
                    // 写列数据
                    util.write2Excel(sheet, totalRowNum, columnIndex, dpCode);
                    break;
                }

                case 3: {
                    List<String> tempResult;

//                    if (excelParam.getTmParam() == 0) {
//                        infoBuilders.desenAlgParam.append("没有脱敏,");
//                    } else {
//                        infoBuilders.desenAlgParam.append(algorithmInfo.getParams() == null ? "无参，"
//                                : algorithmInfo.getParams().get(excelParam.getTmParam()).toString() + ",");
//                    }
                    switch (algoNum) {
                        case 11: {
                            // 脱敏要求
                            infoBuilders.desenRequirements.append(excelParam.getColumnName()).append("尾部截断,");
                            // 脱敏
                            DSObject rawData = new DSObject(objs);
                            tempResult = getDsList(algorithmInfo, rawData, excelParam);
                            // 写列数据
                            util.write2Excel(sheet, totalRowNum, columnIndex, tempResult);
                            break;
                        }
                        case 13:
                        case 16:
                        case 14:
                        case 15:
                        case 21:
                        case 22: {
                            // 脱敏要求
                            infoBuilders.desenRequirements.append(excelParam.getColumnName()).append("抑制,");
                            // 脱敏
                            DSObject rawData = new DSObject(objs);
                            tempResult = getDsList(algorithmInfo, rawData, excelParam);
                            // 写列数据
                            util.write2Excel(sheet, totalRowNum, columnIndex, tempResult);
                            break;
                        }
                        case 17: {
                            // 脱敏要求
                            infoBuilders.desenRequirements.append(excelParam.getColumnName()).append("哈希,");
                            // 脱敏
                            DSObject rawData = new DSObject(objs);
                            tempResult = getDsList(algorithmInfo, rawData, excelParam);                            // 写列数据
                            util.write2Excel(sheet, totalRowNum, columnIndex, tempResult);
                            break;
                        }
                        case 19:
                        case 20: {
                            // 脱敏要求
                            infoBuilders.desenRequirements.append(excelParam.getColumnName()).append("置换,");
                            // 脱敏
                            DSObject rawData = new DSObject(objs);
                            tempResult = getDsList(algorithmInfo, rawData, excelParam);
                            // 写列数据
                            util.write2Excel(sheet, totalRowNum, columnIndex, tempResult);
                            break;
                        }
                    }
                    break;
                }

                case 4: {
                    List<Date> dates;
                    // 加噪处理
                    // 基于差分隐私的日期加噪算法
                    switch (algoNum) {
                        case 1: {
                            // 脱敏要求: 列名+当前算法作用
                            infoBuilders.desenRequirements.append(excelParam.getColumnName()).append("日期添加Laplace噪声,");
                            // 脱敏
                            DSObject rawData = new DSObject(objs);
                            dates = getDsList(algorithmInfo, rawData, excelParam);
                            // 写列数据
                            util.write2Excel(sheet, totalRowNum, columnIndex, dates);
                            break;
                        }
                        case 18: {
                            // 脱敏要求
                            infoBuilders.desenRequirements.append(excelParam.getColumnName()).append("日期进行分组置换,");
                            // 脱敏
                            DSObject rawData = new DSObject(objs);
                            dates = getDsList(algorithmInfo, rawData, excelParam);                            // 写列数据
                            util.write2Excel(sheet, totalRowNum, columnIndex, dates);
                            break;
                        }
                    }
                    break;
                }

            }

        }

        // 结束时间
        long endTimePoint = System.nanoTime();
        // 脱敏结束时间
        String endTime = util.getTime();
        log.info("Desensitization finished in " + (endTimePoint - startTimePoint) / 10e6 + "ms");

        // 单条运行时间
        long oneTime = (endTimePoint - startTimePoint) / (totalRowNum - 1) / columnCount;
        log.info("Single data running time：" + oneTime + " ns");

        // 一秒数据量
        log.info("Number of dealt data per second:" + 10e9 / oneTime);
        log.info("Desen finished");

        // 保存处理后的Excel数据到ByteArrayOutputStream中
        // 保存脱敏后文件
        // 脱敏文件路径
        FileOutputStream fileOutputStream = new FileOutputStream(desenFilePathString);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        workbook.write(byteArrayOutputStream);
        byte[] newExcelData = byteArrayOutputStream.toByteArray();
        fileOutputStream.write(newExcelData);

        // 保存参数文件到本地txt文件
        Path paramDirectory = Paths.get("desen_params");
        if (!Files.exists(paramDirectory)) {
            Files.createDirectory(paramDirectory);
        }
        String paramsFileName = "params" + rawFileName.substring(0, rawFileName.lastIndexOf('.')) + ".txt";
        String paramsFilePath = paramDirectory.resolve(paramsFileName).toAbsolutePath().toString();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(paramsFilePath))) {
            for (ExcelParam p : excelParamList) {
                // 将每个Person对象转换为字符串并写入文件
                String line = p.toString();
                writer.write(line);
                writer.newLine(); // 换行
            }
        } catch (IOException e) {
            log.error(e.getMessage());
            log.error("Failed to save params to file");
        }

        // 关闭工作簿和流
        fileOutputStream.close();
        workbook.close();
        inputStream.close();

        // 标志脱敏完成
        desenCom = true;
        // 脱敏前信息
        // 将文件字节流转换为字符串
        String globalID = System.currentTimeMillis() + randomNum.nextInt() + "脱敏工具集";
        byte[] desenFileBytes = Files.readAllBytes(desenFilePath.toAbsolutePath());
        Long desenFileSize = Files.size(desenFilePath.toAbsolutePath());
        // 存证系统
        String evidenceID = util.getSM3Hash((new String(newExcelData, StandardCharsets.UTF_8) + util.getTime()).getBytes());

        // 使用单独方法构建线程池发送日志
        logSenderManager.submitToFourSystems(globalID, evidenceID, desenCom, objectMode, infoBuilders, rawFileName,
                rawFileBytes, rawFileSize, desenFileName, desenFileBytes, desenFileSize, sheetTemplate, rawFileSuffix,
                startTime, endTime);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", desenFileName); // 设置文件名

        return new ResponseEntity<>(desenFileBytes, headers, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<byte[]> dealVideo(FileStorageDetails fileStorageDetails, String params, String algName) throws IOException, ExecutionException,
            InterruptedException, TimeoutException {

        LogInfo logInfo = processVideo(fileStorageDetails, params, algName);
        // 构造日志收集结果
        LogCollectResult logCollectResult = logSenderManager.buildLogCollectResults(logInfo);
        // 构造响应结果
        CompletableFuture<ResponseEntity<byte[]>> responseEntityCompletableFuture = new CompletableFuture<>();
        // 获取文件信息
        byte[] rawFileBytes = logInfo.getRawFileBytes();
        byte[] desenFileBytes = logInfo.getDesenFileBytes();
        String desenFileName = logInfo.getDesenFileName();
        // 选择不同的日志发送方式
        // 选择首先发给评测系统评测
        if (ifSendToEvaFirst) {
            eventPublisher.publishEvent(new LogManagerEvent(this, fileStorageDetails,
                    logCollectResult, responseEntityCompletableFuture));
        }
        // 直接发给四个系统
        else {
            logSenderManager.submitToFourSystems(logCollectResult, rawFileBytes, desenFileBytes);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentDispositionFormData("attachment", desenFileName); // 设置文件名
            responseEntityCompletableFuture.complete(ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.parseMediaType("video/mp4")).body(desenFileBytes));
        }

        return responseEntityCompletableFuture.get(10, TimeUnit.MINUTES);
    }


    @Override
    public ResponseEntity<byte[]> dealVideo(MultipartFile file, String params, String algName) throws IOException {
        Boolean desenCom = false;
        DesenInfoStringBuilders infoBuilders = new DesenInfoStringBuilders();
        String objectMode = "video";
        // 设置文件时间戳
        String fileTimeStamp = String.valueOf(System.currentTimeMillis());
        // 设置原文件信息
        if (file.getOriginalFilename() == null) {
            throw new IOException("Input file name is null");
        }
        // 设置原文件保存路径
        String rawFileName = fileTimeStamp + file.getOriginalFilename();
        String rawFileSuffix = rawFileName.substring(rawFileName.lastIndexOf(".") + 1);
        Path rawFilePath = rawFileDirectory.resolve(rawFileName);
        String rawFilePathString = rawFilePath.toAbsolutePath().toString();
        byte[] rawFileBytes = file.getBytes();
        Long rawFileSize = file.getSize();

        // 保存源文件
        file.transferTo(rawFilePath.toAbsolutePath());
        // 设置脱敏后文件路径信息
        String desenFileName = "desen_" + rawFileName;
        Path desenFilePath = desenFileDirectory.resolve(desenFileName);
        String desenFilePathString = desenFilePath.toAbsolutePath().toString();

        Integer desenParam = Integer.valueOf(params);
        DSObject dsObject = new DSObject(Arrays.asList(rawFilePathString, desenFilePathString));

        AlgorithmInfo algorithmInfo = algorithmsFactory.getAlgorithmInfoFromName(algName);

        // 调用脱敏程序处理
        log.info("Start video desen");
        // 脱敏开始时间
        String startTime = util.getTime();
        long startTimePoint = System.currentTimeMillis();
        algorithmInfo.execute(dsObject, desenParam);

        // 脱敏后收集信息
        // 结束时间
        long endTimePoint = System.currentTimeMillis();
        // 脱敏耗时
        long executionTime = endTimePoint - startTimePoint;
        log.info("Desensitization finished in " + executionTime + "ms");
        log.info("Video desen finished");
        // 脱敏结束时间
        String endTime = util.getTime();
        // 标志脱敏完成
        desenCom = true;
        String globalID = System.currentTimeMillis() + randomNum.nextInt() + "脱敏工具集";
        // 脱敏后文件字节流
        byte[] desenFileBytes = Files.readAllBytes(desenFilePath.toAbsolutePath());
        Long desenFileSize = Files.size(desenFilePath.toAbsolutePath());
        // 脱敏算法
        infoBuilders.desenAlg.append(algorithmInfo.getId());
        if (algorithmInfo.getParams() == null) {
            infoBuilders.desenAlgParam.append("无参");
        } else {
            infoBuilders.desenAlgParam.append(algorithmInfo.getParams().get(desenParam).toString());
        }
        infoBuilders.desenLevel.append(desenParam + 1);
        // 脱敏前类型
        infoBuilders.desenInfoPreIden.append("video");
        // 脱敏后类型
        infoBuilders.desenInfoAfterIden.append("video");
        // 脱敏意图
        infoBuilders.desenIntention.append("对视频脱敏");
        // 脱敏要求
        infoBuilders.desenRequirements.append("对视频脱敏");
        // 脱敏数据类型
        infoBuilders.fileDataType.append(rawFileSuffix);
        // 存证系统evidenceID
        String evidenceID = util.getSM3Hash((new String(desenFileBytes, StandardCharsets.UTF_8) + util.getTime()).getBytes());

        logSenderManager.submitToFourSystems(globalID, evidenceID, desenCom, objectMode, infoBuilders, rawFileName,
                rawFileBytes, rawFileSize, desenFileName, desenFileBytes, desenFileSize, objectMode, rawFileSuffix, startTime, endTime);

        // 读取文件返回
        HttpHeaders headers = new HttpHeaders();
        headers.setContentDispositionFormData("attachment", desenFileName); // 设置文件名

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.parseMediaType("video/mp4")).body(desenFileBytes);
    }

    @Override
    public ResponseEntity<byte[]> dealAudio(FileStorageDetails fileStorageDetails, String params, String algName) throws IOException, ExecutionException, InterruptedException, TimeoutException {

        LogInfo logInfo = processAudio(fileStorageDetails, params, algName);
        // 构造日志收集结果
        LogCollectResult logCollectResult = logSenderManager.buildLogCollectResults(logInfo);
        // 构造响应结果
        CompletableFuture<ResponseEntity<byte[]>> responseEntityCompletableFuture = new CompletableFuture<>();
        // 获取文件信息
        byte[] rawFileBytes = logInfo.getRawFileBytes();
        byte[] desenFileBytes = logInfo.getDesenFileBytes();
        String desenFileName = logInfo.getDesenFileName();
        // 选择不同的日志发送方式
        // 选择首先发给评测系统评测
        if (ifSendToEvaFirst) {
            eventPublisher.publishEvent(new LogManagerEvent(this, fileStorageDetails,
                    logCollectResult, responseEntityCompletableFuture));
        }
        // 直接发给四个系统
        else {
            logSenderManager.submitToFourSystems(logCollectResult, rawFileBytes, desenFileBytes);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", desenFileName); // 设置文件名
            responseEntityCompletableFuture.complete(new ResponseEntity<>(desenFileBytes, headers, HttpStatus.OK));
        }
        return responseEntityCompletableFuture.get(10, TimeUnit.MINUTES);

    }

    @Override
    public ResponseEntity<byte[]> dealAudio(MultipartFile file, String params, String algName) throws IOException, SQLException, InterruptedException {
        Boolean desenCom = false;
        DesenInfoStringBuilders infoBuilders = new DesenInfoStringBuilders();
        String objectMode = "audio";
        // 设置文件时间戳
        String fileTimeStamp = String.valueOf(System.currentTimeMillis());
        // 设置原文件信息
        if (file.getOriginalFilename() == null) {
            throw new IOException("Input file name is null");
        }
        // 设置原文件保存路径
        String rawFileName = fileTimeStamp + file.getOriginalFilename();
        String rawFileSuffix = rawFileName.substring(rawFileName.lastIndexOf(".") + 1);
        Path rawFilePath = rawFileDirectory.resolve(rawFileName);
        String rawFilePathString = rawFilePath.toAbsolutePath().toString();
        byte[] rawFileBytes = file.getBytes();
        Long rawFileSize = file.getSize();

        // 保存源文件
        file.transferTo(rawFilePath.toAbsolutePath());
        // 设置脱敏后文件路径信息
        String desenFileName = "desen_" + rawFileName;
        Path desenFilePath = desenFileDirectory.resolve(desenFileName);
        String desenFilePathString = desenFilePath.toAbsolutePath().toString();

        // 脱敏参数处理
        String[] paramsList = params.split(",");
        int desenParam = Integer.parseInt(paramsList[paramsList.length - 1]);
        // 获取脱敏算法
        AlgorithmInfo algorithmInfo = algorithmsFactory.getAlgorithmInfoFromName(algName);
        List<String> paths = Arrays.asList(rawFilePathString, desenFilePathString);

        DSObject desenObj = new DSObject(paths);

        // 调用脱敏程序处理
        log.info("Start Audio desen");
        // 开始时间
        String startTime = util.getTime();
        // 脱敏开始时间
        long startTimePoint = System.currentTimeMillis();
        algorithmInfo.execute(desenObj, desenParam);
        // 结束时间
        long endTimePoint = System.currentTimeMillis();
        // 脱敏耗时
        long executionTime = endTimePoint - startTimePoint;
        // 脱敏结束时间
        String endTime = util.getTime();
        log.info("Desensitization finished in " + executionTime + "ms");
        log.info("Audio desen finished");
        // 标志脱敏完成
        desenCom = true;
        String globalID = System.currentTimeMillis() + randomNum.nextInt() + "脱敏工具集";
        // 脱敏后文件字节流
        byte[] desenFileBytes = Files.readAllBytes(desenFilePath.toAbsolutePath());
        Long desenFileSize = Files.size(desenFilePath.toAbsolutePath());
        infoBuilders.desenAlg.append(algorithmInfo.getId());
        if (algorithmInfo.getParams() == null) {
            infoBuilders.desenAlgParam.append("无参");
        } else {
            infoBuilders.desenAlgParam.append(algorithmInfo.getParams().get(desenParam).toString());
        }
        // 脱敏级别
        infoBuilders.desenLevel.append(desenParam + 1);
        // 脱敏前类型
        infoBuilders.desenInfoPreIden.append("audio");
        // 脱敏后类型
        infoBuilders.desenInfoAfterIden.append("audio");
        // 脱敏意图
        infoBuilders.desenIntention.append("对音频脱敏");
        // 脱敏要求
        infoBuilders.desenRequirements.append("对声纹脱敏");
        // 脱敏数据类型
        infoBuilders.fileDataType.append(rawFileSuffix);
        // 存证系统
        //存证请求  消息版本：中心0x1000，0x1010; 本地0x1100，0x1110
        String evidenceID = util.getSM3Hash((new String(desenFileBytes, StandardCharsets.UTF_8) + util.getTime()).getBytes());

        logSenderManager.submitToFourSystems(globalID, evidenceID, desenCom, objectMode, infoBuilders, rawFileName,
                rawFileBytes, rawFileSize, desenFileName, desenFileBytes, desenFileSize, objectMode, rawFileSuffix, startTime, endTime);

        // 设置HTTP响应头部信息
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", desenFileName);

        // 返回处理后的音频文件数据给前端
        return new ResponseEntity<>(desenFileBytes, headers, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<byte[]> dealGraph(FileStorageDetails fileStorageDetails, String params) throws IOException, ExecutionException, InterruptedException, TimeoutException {
        HttpHeaders errorHttpHeaders = new HttpHeaders();
        errorHttpHeaders.add(HttpHeaders.CONTENT_TYPE, "text/plain");
        LogInfo logInfo = processGraph(fileStorageDetails, params);
        // 构造日志收集结果
        LogCollectResult logCollectResult = logSenderManager.buildLogCollectResults(logInfo);
        // 构造响应结果
        CompletableFuture<ResponseEntity<byte[]>> responseEntityCompletableFuture = new CompletableFuture<>();
        // 获取文件信息
        byte[] rawFileBytes = logInfo.getRawFileBytes();
        byte[] desenFileBytes = logInfo.getDesenFileBytes();
        String desenFileName = logInfo.getDesenFileName();
        // 选择不同的日志发送方式
        // 选择首先发给评测系统评测
        if (ifSendToEvaFirst) {
            eventPublisher.publishEvent(new LogManagerEvent(this, fileStorageDetails,
                    logCollectResult, responseEntityCompletableFuture));
        }
        // 直接发给四个系统
        else {
            logSenderManager.submitToFourSystems(logCollectResult, rawFileBytes, desenFileBytes);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", desenFileName); // 设置文件名
            responseEntityCompletableFuture.complete(new ResponseEntity<>(desenFileBytes, headers, HttpStatus.OK));
        }
        return responseEntityCompletableFuture.get(60, TimeUnit.MINUTES);
    }

    @Override
    public ResponseEntity<byte[]> dealGraph(MultipartFile file, String params) throws IOException, SQLException, InterruptedException {
        Boolean desenCom = false;
        DesenInfoStringBuilders infoBuilders = new DesenInfoStringBuilders();
        String objectMode = "graph";

        // 设置文件时间戳
        String fileTimeStamp = String.valueOf(System.currentTimeMillis());

        // 设置原文件信息
        if (file.getOriginalFilename() == null) {
            throw new IOException("Input file name is null");
        }
        // 设置原文件保存路径
        String rawFileName = fileTimeStamp + file.getOriginalFilename();
        String rawFileSuffix = rawFileName.substring(rawFileName.lastIndexOf(".") + 1);
        Path rawFilePath = rawFileDirectory.resolve(rawFileName);
        String rawFilePathString = rawFilePath.toAbsolutePath().toString();
        byte[] rawFileBytes = file.getBytes();
        Long rawFileSize = file.getSize();
        // 保存源文件
        file.transferTo(rawFilePath.toAbsolutePath());
        // 设置脱敏后文件路径信息
        String desenFileName = "desen_" + rawFileName;
        Path desenFilePath = desenFileDirectory.resolve(desenFileName);
        String desenFilePathString = desenFilePath.toAbsolutePath().toString();

        // 调用脱敏程序处理
        String desenParam = String.valueOf(params.charAt(params.length() - 1));

        DSObject dsObject = new DSObject(Arrays.asList(rawFilePathString, desenFilePathString));

        // 调用脱敏程序处理
        log.info("Start Graph desen");
        // 开始时间
        String startTime = util.getTime();
        // 脱敏开始时间
        long startTimePoint = System.currentTimeMillis();
        dp.service(dsObject, 7, Integer.parseInt(desenParam));

        // 结束时间
        long endTimePoint = System.currentTimeMillis();
        // 脱敏耗时
        long executionTime = endTimePoint - startTimePoint;
        // 脱敏结束时间
        String endTime = util.getTime();
        logCollectUtil.logExecutionTime(String.valueOf(executionTime), "Graph");
        // 标志脱敏完成
        desenCom = true;
        String globalID = System.currentTimeMillis() + randomNum.nextInt() + "脱敏工具集";
        // 脱敏后文件字节流
        byte[] desenFileBytes = Files.readAllBytes(desenFilePath.toAbsolutePath());
        Long desenFileSize = Files.size(desenFilePath.toAbsolutePath());
        // 脱敏算法
        infoBuilders.desenAlg.append(60);
        // 脱敏参数
        double[] param = new double[]{5.0, 1.0, 0.2};
        infoBuilders.desenAlgParam.append(param[Integer.parseInt(desenParam)]);
        // 脱敏级别
        infoBuilders.desenLevel.append(Integer.parseInt(params) + 1);
        // 脱敏前类型
        infoBuilders.desenInfoPreIden.append(objectMode);
        // 脱敏后类型
        infoBuilders.desenInfoAfterIden.append(objectMode);
        // 脱敏意图
        infoBuilders.desenIntention.append("对图形脱敏");
        // 脱敏要求
        infoBuilders.desenRequirements.append("对图形脱敏");
        // 脱敏数据类型
        infoBuilders.fileDataType.append(rawFileSuffix);

        // 存证系统
        //存证请求  消息版本：中心0x1000，0x1010; 本地0x1100，0x1110
        String evidenceID = util.getSM3Hash((new String(desenFileBytes, StandardCharsets.UTF_8) + util.getTime()).getBytes());
        logSenderManager.submitToFourSystems(globalID, evidenceID, desenCom, objectMode, infoBuilders, rawFileName,
                rawFileBytes, rawFileSize, desenFileName, desenFileBytes, desenFileSize, objectMode, rawFileSuffix, startTime, endTime);

        // 返回数据给前端
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", desenFileName); // 设置文件名

        return new ResponseEntity<>(desenFileBytes, headers, HttpStatus.OK);
    }

    //    @Override
//    public ResponseEntity<byte[]> dealCsv(FileStorageDetails fileStorageDetails, String params, String algName) throws InterruptedException, IOException {
//        Boolean desenCom = false;
//        DesenInfoStringBuilders infoBuilders = new DesenInfoStringBuilders();
//        String objectMode = "text";
//
//        // 设置原文件保存路径
//        String rawFileName = fileStorageDetails.getRawFileName();
//        String rawFileSuffix = fileStorageDetails.getRawFileSuffix();
//        Path rawFilePath = fileStorageDetails.getRawFilePath();
//        String rawFilePathString = fileStorageDetails.getRawFilePathString();
//        byte[] rawFileBytes = fileStorageDetails.getRawFileBytes();
//        Long rawFileSize = fileStorageDetails.getRawFileSize();
//
//        // 设置脱敏后文件路径信息
//        String desenFileName = fileStorageDetails.getDesenFileName();
//        Path desenFilePath = fileStorageDetails.getDesenFilePath();
//        log.info(desenFilePath.toAbsolutePath().toString());
//        String desenFilePathString = fileStorageDetails.getDesenFilePathString();
//
//        // 脱敏参数
//        String privacyLevel = String.valueOf(params.charAt(params.length() - 1));
//        int[] k_anonymity_param = new int[]{5, 10, 20};
//        int[] l_diversity_param = new int[]{2, 4, 6};
//        double[] t_closeness_param = new double[]{0.6, 0.4, 0.2};
//        String desenParam;
//        DSObject dsObject = new DSObject(Arrays.asList(rawFilePathString, desenFilePathString));
//        // 脱敏开始时间
//        String startTime = util.getTime();
//        // 开始时间
//        // TODO: 整合该部分进AlgorithmsFactory
//        long desenStartTime = System.currentTimeMillis();
//        switch (algName) {
//            case "k_anonymity":
//                infoBuilders.desenAlg.append(25);
//                anonymity.service(dsObject, 1, Integer.parseInt(privacyLevel));
//                desenParam = String.valueOf(k_anonymity_param[Integer.parseInt(privacyLevel)]);
//                break;
//            case "l_diversity":
//                infoBuilders.desenAlg.append(26);
//                anonymity.service(dsObject, 7, Integer.parseInt(privacyLevel));
//                desenParam = String.valueOf(l_diversity_param[Integer.parseInt(privacyLevel)]);
//                break;
//            case "t_closeness":
//                infoBuilders.desenAlg.append(27);
//                anonymity.service(dsObject, 10, Integer.parseInt(privacyLevel));
//                desenParam = String.valueOf(t_closeness_param[Integer.parseInt(privacyLevel)]);
//                break;
//
//            default:
//                desenParam = String.valueOf(k_anonymity_param[Integer.parseInt(privacyLevel)]);
//        }
//        infoBuilders.desenAlgParam.append(desenParam);
//        // 脱敏级别
//        infoBuilders.desenLevel.append(privacyLevel + 1);
//        // 脱敏结束时间
//        String endTime = util.getTime();
//        // 结束时间
//        long desenEndTime = System.currentTimeMillis();
//        long executionTime = desenEndTime - desenStartTime;
//        logCollectUtil.logExecutionTime(String.valueOf(executionTime), "CSV");
//
//        // 标志脱敏完成
//        desenCom = true;
//        String globalID = System.currentTimeMillis() + randomNum.nextInt() + "脱敏工具集";
//
//        // 脱敏后信息
//        // 脱敏后文件大小
//        byte[] desenFileBytes = Files.readAllBytes(desenFilePath.toAbsolutePath());
//        Long desenFileSize = Files.size(desenFilePath.toAbsolutePath());
//        // 脱敏前类型
//        infoBuilders.desenInfoPreIden.append("csv文件");
//        // 脱敏后类型
//        infoBuilders.desenInfoAfterIden.append("csv文件");
//        // 脱敏意图
//        infoBuilders.desenIntention.append("对csv文件脱敏");
//        // 脱敏要求
//        infoBuilders.desenRequirements.append("对csv文件脱敏");
//        // 脱敏数据类型
//        infoBuilders.fileDataType.append(rawFileSuffix);
//
//        // 存证系统
//        //存证请求  消息版本：中心0x1000，0x1010; 本地0x1100，0x1110
//        String evidenceID = util.getSM3Hash((new String(desenFileBytes, StandardCharsets.UTF_8) + util.getTime()).getBytes());
//        // 返回excel给前端
//        logSenderManager.submitToFourSystems(globalID, evidenceID, desenCom, objectMode, infoBuilders, rawFileName,
//                rawFileBytes, rawFileSize, desenFileName, desenFileBytes, desenFileSize, objectMode, rawFileSuffix, startTime, endTime);
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
//        headers.setContentDispositionFormData("attachment", desenFileName); // 设置文件名
//
//        return new ResponseEntity<>(desenFileBytes, headers, HttpStatus.OK);
//    }
//    @Override
//    public ResponseEntity<byte[]> dealCsv(FileStorageDetails fileStorageDetails, String params, String algName)
//            throws IOException, InterruptedException {
//        Boolean desenCom = false;
//        DesenInfoStringBuilders infoBuilders = new DesenInfoStringBuilders();
//        String objectMode = "text";
//
//        /// 设置原文件保存路径
//        String rawFileName = fileStorageDetails.getRawFileName();
//        String rawFileSuffix = fileStorageDetails.getRawFileSuffix();
//        Path rawFilePath = fileStorageDetails.getRawFilePath();
//        String rawFilePathString = fileStorageDetails.getRawFilePathString();
//        byte[] rawFileBytes = fileStorageDetails.getRawFileBytes();
//        Long rawFileSize = fileStorageDetails.getRawFileSize();
//
//        // 设置脱敏后文件路径信息
//        String desenFileName = fileStorageDetails.getDesenFileName();
//        Path desenFilePath = fileStorageDetails.getDesenFilePath();
//        log.info(desenFilePath.toAbsolutePath().toString());
//        String desenFilePathString = fileStorageDetails.getDesenFilePathString();
//
//        // 脱敏参数
//        String privacyLevel = String.valueOf(params.charAt(params.length() - 1));
//        int[] k_anonymity_param = new int[]{5, 10, 20};
//        int[] l_diversity_param = new int[]{2, 4, 6};
//        double[] t_closeness_param = new double[]{0.6, 0.4, 0.2};
//        String desenParam;
//        DSObject dsObject = new DSObject(Arrays.asList(rawFilePathString, desenFilePathString));
//        // 脱敏开始时间
//        String startTime = util.getTime();
//        // 开始时间
//        // TODO: 整合该部分进AlgorithmsFactory
//        long desenStartTime = System.currentTimeMillis();
//        switch (algName) {
//            case "k_anonymity":
//                infoBuilders.desenAlg.append(25);
//                anonymity.service(dsObject, 1, Integer.parseInt(privacyLevel));
//                desenParam = String.valueOf(k_anonymity_param[Integer.parseInt(privacyLevel)]);
//                break;
//            case "l_diversity":
//                infoBuilders.desenAlg.append(26);
//                anonymity.service(dsObject, 7, Integer.parseInt(privacyLevel));
//                desenParam = String.valueOf(l_diversity_param[Integer.parseInt(privacyLevel)]);
//                break;
//            case "t_closeness":
//                infoBuilders.desenAlg.append(27);
//                anonymity.service(dsObject, 10, Integer.parseInt(privacyLevel));
//                desenParam = String.valueOf(t_closeness_param[Integer.parseInt(privacyLevel)]);
//                break;
//
//            default:
//                desenParam = String.valueOf(k_anonymity_param[Integer.parseInt(privacyLevel)]);
//        }
//        infoBuilders.desenAlgParam.append(desenParam);
//        // 脱敏级别
//        infoBuilders.desenLevel.append(privacyLevel + 1);
//        // 脱敏结束时间
//        String endTime = util.getTime();
//
//        // 结束时间
//        long desenEndTime = System.currentTimeMillis();
//        long executionTime = desenEndTime - desenStartTime;
//        logCollectUtil.logExecutionTime(String.valueOf(executionTime), "CSV");
//
//        // 标志脱敏完成
//        desenCom = true;
//        String globalID = System.currentTimeMillis() + randomNum.nextInt() + "脱敏工具集";
//
//        // 脱敏后信息
//        // 脱敏后文件大小
//        byte[] desenFileBytes = Files.readAllBytes(desenFilePath.toAbsolutePath());
//        Long desenFileSize = Files.size(desenFilePath.toAbsolutePath());
//        // 脱敏前类型
//        infoBuilders.desenInfoPreIden.append("csv文件");
//        // 脱敏后类型
//        infoBuilders.desenInfoAfterIden.append("csv文件");
//        // 脱敏意图
//        infoBuilders.desenIntention.append("对csv文件脱敏");
//        // 脱敏要求
//        infoBuilders.desenRequirements.append("对csv文件脱敏");
//        // 脱敏数据类型
//        infoBuilders.fileDataType.append(rawFileSuffix);
//        // 存证系统
//        //存证请求  消息版本：中心0x1000，0x1010; 本地0x1100，0x1110
//        String evidenceID = util.getSM3Hash((new String(desenFileBytes, StandardCharsets.UTF_8) + util.getTime()).getBytes());
//
//        // 返回excel给前端
//        logSenderManager.submitToFourSystems(globalID, evidenceID, desenCom, objectMode, infoBuilders, rawFileName,
//                rawFileBytes, rawFileSize, desenFileName, desenFileBytes, desenFileSize, objectMode, rawFileSuffix, startTime, endTime);
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
//        headers.setContentDispositionFormData("attachment", desenFileName); // 设置文件名
//
//        return new ResponseEntity<>(desenFileBytes, headers, HttpStatus.OK);
//    }
    @Override
    public ResponseEntity<byte[]> dealSingleExcel(FileStorageDetails fileStorageDetails, String params, String algName)
            throws IOException {
        HttpHeaders errorHttpHeaders = new HttpHeaders();
        errorHttpHeaders.add(HttpHeaders.CONTENT_TYPE, "text/plain");
        LogInfo logInfo = processSingleExcel(fileStorageDetails, params, algName);
        // 构造日志收集结果
        LogCollectResult logCollectResult = logSenderManager.buildLogCollectResults(logInfo);
        // 构造响应结果
        CompletableFuture<ResponseEntity<byte[]>> responseEntityCompletableFuture = new CompletableFuture<>();
        // 获取文件信息
        byte[] rawFileBytes = logInfo.getRawFileBytes();
        byte[] desenFileBytes = logInfo.getDesenFileBytes();
        String desenFileName = logInfo.getDesenFileName();
        // 选择不同的日志发送方式
        // 选择首先发给评测系统评测
        if (ifSendToEvaFirst) {
            eventPublisher.publishEvent(new LogManagerEvent(this, fileStorageDetails,
                    logCollectResult, responseEntityCompletableFuture));
        }
        // 直接发给四个系统
        else {
            logSenderManager.submitToFourSystems(logCollectResult, rawFileBytes, desenFileBytes);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", desenFileName); // 设置文件名
            responseEntityCompletableFuture.complete(new ResponseEntity<>(desenFileBytes, headers, HttpStatus.OK));
        }
        // 在此处等待响应返回
        try {
            return responseEntityCompletableFuture.get(10, TimeUnit.MINUTES);
        } catch (TimeoutException e) {
            log.error("等待处理结果超时：{}", e.getMessage());
            String errorMsg = "等待处理结果超时";
            return ResponseEntity.status(500).headers(errorHttpHeaders).body(errorMsg.getBytes());
        } catch (InterruptedException e) {
            log.error("等待处理结果时异常中断：{}", e.getMessage());
            String errorMsg = "等待处理结果时异常中断";
            return ResponseEntity.status(500).headers(errorHttpHeaders).body(errorMsg.getBytes());
        } catch (ExecutionException e) {
            log.error("等待处理结果时执行异常：{}", e.getMessage());
            String errorMsg = "等待处理结果时执行异常";
            return ResponseEntity.status(500).headers(errorHttpHeaders).body(errorMsg.getBytes());
        }
    }

    @Override
    public ResponseEntity<byte[]> dealSingleExcel(MultipartFile file, String params, String algName) throws IOException {
        Boolean desenCom = false;
        DesenInfoStringBuilders infoBuilders = new DesenInfoStringBuilders();
        String objectMode = "text";

        // 读取excel文件
        InputStream inputStream = file.getInputStream();
        Workbook workbook = new XSSFWorkbook(inputStream);
        Sheet sheet = workbook.getSheetAt(0);

        // 设置文件时间戳
        String fileTimeStamp = String.valueOf(System.currentTimeMillis());

        // 设置原文件信息
        if (file.getOriginalFilename() == null) {
            throw new IOException("Input file name is null");
        }
        // 设置原文件保存路径
        String rawFileName = fileTimeStamp + file.getOriginalFilename();
        String rawFileSuffix = rawFileName.substring(rawFileName.lastIndexOf(".") + 1);
        Path rawFilePath = rawFileDirectory.resolve(rawFileName);
        String rawFilePathString = rawFilePath.toAbsolutePath().toString();
        byte[] rawFileBytes = file.getBytes();
        Long rawFileSize = file.getSize();

        // 保存源文件
        file.transferTo(rawFilePath.toAbsolutePath());
        // 设置脱敏后文件路径信息
        String desenFileName = "desen_" + rawFileName;
        Path desenFilePath = desenFileDirectory.resolve(desenFileName);
        String desenFilePathString = desenFilePath.toAbsolutePath().toString();

        // 保存脱敏后文件
        // 脱敏文件路径
        FileOutputStream fileOutputStream = new FileOutputStream(desenFilePathString);
        // 保存参数文件
        int desenParam = Integer.parseInt(String.valueOf(params.charAt(params.length() - 1)));
        // 数据类型
        List<Integer> dataType = new ArrayList<>();
        // 数据行数
        int totalRowNum = sheet.getLastRowNum();
        // 字段名行
        Row fieldRow = sheet.getRow(0);
        // 列数
        int columnCount = fieldRow.getPhysicalNumberOfCells(); // 获取列数
        log.info("Total column number is " + columnCount);
        // 调用脱敏程序处理
        log.info("Start file desen");
        // 脱敏开始时间
        String startTime = util.getTime();
        long startTimePoint = System.nanoTime();

        //  逐列处理
        DataFormatter dataFormatter = new DataFormatter();
        for (int columnIndex = 0; columnIndex < columnCount; columnIndex++) {
            // 取列名
            String colName = fieldRow.getCell(columnIndex).toString();
            log.info("Column Index: {}, Column Name: {}", columnIndex, colName);

            // 脱敏前信息类型标识
            infoBuilders.desenInfoPreIden.append(colName);
            infoBuilders.desenInfoAfterIden.append(colName);
            // 读取脱敏级别
            infoBuilders.desenLevel.append(desenParam);
            // 脱敏意图
            infoBuilders.desenIntention.append(colName).append("脱敏,");
            // 脱敏数据类型
            infoBuilders.fileDataType.append(rawFileSuffix);
            // 取列数据
            List<Object> objs = new ArrayList<>();

            for (int j = 1; j <= totalRowNum; j++) {
                Row row = sheet.getRow(j);
                if (row != null) {
                    Cell cell = row.getCell(columnIndex);
                    objs.add(cell);
                }
            }
            AlgorithmInfo algorithmInfo = algorithmsFactory.getAlgorithmInfoFromName(algName.trim());
            DSObject dsObject = new DSObject(objs);

            infoBuilders.desenAlgParam.append(algorithmInfo.getParams() == null ? "无参" : algorithmInfo.getParams().get(desenParam - 1));

            switch (algName.trim()) {
                case "dpDate": {
                    // 脱敏要求
                    infoBuilders.desenRequirements.append(colName).append("添加Laplace噪声,");
                    infoBuilders.desenAlg.append(algorithmInfo.getId());

                    List<Date> dates = algorithmInfo.execute(dsObject, desenParam).getList()
                            .stream()
                            .map(obj -> obj == null ? null : (Date) obj)
                            .collect(Collectors.toList());

                    util.write2Excel(sheet, totalRowNum, columnIndex, dates);
                    break;
                }
                case "dpCode": {
                    // 脱敏要求
                    infoBuilders.desenRequirements.append(colName).append("随机扰动,");
                    infoBuilders.desenAlg.append(algorithmInfo.getId());

                    List<String> datas = algorithmInfo.execute(dsObject, desenParam).getList()
                            .stream()
                            .map(obj -> obj == null ? null : (String) obj)
                            .collect(Collectors.toList());

                    util.write2Excel(sheet, totalRowNum, columnIndex, datas);
                    break;
                }
                case "laplaceToValue": {
                    // 脱敏要求
                    infoBuilders.desenRequirements.append(colName).append("添加差分隐私Laplace噪声,");
                    infoBuilders.desenAlg.append(algorithmInfo.getId());
                    // 脱敏
                    List<Double> datas = algorithmInfo.execute(dsObject, desenParam).getList()
                            .stream()
                            .map(obj -> obj == null ? null : (Double) obj)
                            .collect(Collectors.toList());

                    // 写列数据
                    util.write2Excel(sheet, totalRowNum, columnIndex, datas);
                    break;
                }
                case "gaussianToValue": {
                    // 脱敏要求
                    infoBuilders.desenRequirements.append(colName).append("添加差分隐私高斯噪声,");
                    infoBuilders.desenAlg.append(4);

                    List<Double> datas = dp.service(dsObject, 27, desenParam).getList()
                            .stream()
                            .map(obj -> obj == null ? null : (Double) obj)
                            .collect(Collectors.toList());

                    util.write2Excel(sheet, totalRowNum, columnIndex, datas);
                    break;
                }
                case "randomUniformToValue": {
                    // 脱敏要求
                    infoBuilders.desenRequirements.append(colName).append("添加随机均匀噪声,");
                    infoBuilders.desenAlg.append(algorithmInfo.getId());

                    List<Double> datas = algorithmInfo.execute(dsObject, desenParam).getList()
                            .stream()
                            .map(obj -> obj == null ? null : (Double) obj)
                            .collect(Collectors.toList());

                    util.write2Excel(sheet, totalRowNum, columnIndex, datas);
                    break;
                }
                case "randomLaplaceToValue": {
                    // 脱敏要求
                    infoBuilders.desenRequirements.append(colName).append("添加随机laplace噪声,");
                    infoBuilders.desenAlg.append(algorithmInfo.getId());
                    List<Double> datas = algorithmInfo.execute(dsObject, desenParam).getList()
                            .stream()
//                            .filter(obj -> obj instanceof Double)
                            .map(obj -> obj == null ? null : (Double) obj)
                            .collect(Collectors.toList());

                    util.write2Excel(sheet, totalRowNum, columnIndex, datas);
                    break;
                }
                case "randomGaussianToValue": {
                    // 脱敏要求
                    infoBuilders.desenRequirements.append(colName).append("添加随机高斯噪声,");
                    infoBuilders.desenAlg.append(algorithmInfo.getId());

                    List<Double> datas = algorithmInfo.execute(dsObject, desenParam).getList()
                            .stream()
                            .map(obj -> obj == null ? null : (Double) obj)
                            .collect(Collectors.toList());

                    util.write2Excel(sheet, totalRowNum, columnIndex, datas);
                    break;
                }
                case "valueShift": {
                    // 脱敏要求
                    infoBuilders.desenRequirements.append(colName).append("数值偏移,");
                    infoBuilders.desenAlg.append(algorithmInfo.getId());

                    List<Double> datas = algorithmInfo.execute(dsObject, desenParam).getList()
                            .stream()
                            .map(obj -> obj == null ? null : (Double) obj)
                            .collect(Collectors.toList());

                    util.write2Excel(sheet, totalRowNum, columnIndex, datas);
                    break;
                }
                case "floor": {
                    // 脱敏要求
                    infoBuilders.desenRequirements.append(colName).append("数值取整,");
                    infoBuilders.desenAlg.append(algorithmInfo.getId());
                    if (desenParam == 0) {
                        util.write2Excel(sheet, totalRowNum, columnIndex, objs);
                    } else {
                        List<Integer> datas = algorithmInfo.execute(dsObject, desenParam).getList()
                                .stream()
                                .map(obj -> obj == null ? null : (Integer) obj)
                                .collect(Collectors.toList());
                        util.write2Excel(sheet, totalRowNum, columnIndex, datas);
                    }
                    break;
                }
                case "valueMapping": {
                    // 脱敏要求
                    infoBuilders.desenRequirements.append(colName).append("数值映射,");
                    infoBuilders.desenAlg.append(algorithmInfo.getId());
                    List<Double> datas = algorithmInfo.execute(dsObject, desenParam).getList()
                            .stream()
                            .map(obj -> obj == null ? null : (Double) obj)
                            .collect(Collectors.toList());

                    util.write2Excel(sheet, totalRowNum, columnIndex, datas);
                    break;
                }
                case "truncation": {
                    // 脱敏要求
                    infoBuilders.desenRequirements.append(colName).append("截断,");
                    infoBuilders.desenAlg.append(algorithmInfo.getId());
                    List<String> datas = algorithmInfo.execute(dsObject, desenParam).getList()
                            .stream()
                            .map(obj -> obj == null ? null : (String) obj)
                            .collect(Collectors.toList());

                    util.write2Excel(sheet, totalRowNum, columnIndex, datas);
                    break;
                }

                case "floorTime":
                case "suppressEmail":
                case "addressHide":
                case "nameHide":
                case "numberHide":
                case "suppressIpRandomParts":
                case "suppressAllIp": {
                    // 脱敏要求
                    infoBuilders.desenRequirements.append(colName).append("抑制,");
                    infoBuilders.desenAlg.append(algorithmInfo.getId());
                    List<String> datas = algorithmInfo.execute(dsObject, desenParam).getList()
                            .stream()
//                            .filter(obj -> obj instanceof String)
                            .map(obj -> obj == null ? null : (String) obj)
                            .collect(Collectors.toList());

                    util.write2Excel(sheet, totalRowNum, columnIndex, datas);
                    break;
                }

                case "SHA512":
                case "passReplace":
                case "value_hide": {
                    // 脱敏要求
                    infoBuilders.desenRequirements.append(colName).append("置换,");
                    infoBuilders.desenAlg.append(algorithmInfo.getId());
                    List<String> datas = algorithmInfo.execute(dsObject, desenParam).getList()
                            .stream()
//                            .filter(obj -> obj instanceof String)
                            .map(obj -> obj == null ? null : (String) obj)
                            .collect(Collectors.toList());
                    util.write2Excel(sheet, totalRowNum, columnIndex, datas);
                    break;
                }
            }
        }

        // 结束时间
        long endTimePoint = System.nanoTime();
        // 脱敏结束时间
        String endTime = util.getTime();

        log.info("Desensitization finished in" + (endTimePoint - startTimePoint) / 1e6 + "ms");
        long oneTime = (endTimePoint - startTimePoint) / columnCount / (totalRowNum - 1);
        // 打印单条运行时间
        log.info("Single data running time：" + oneTime + " ns");
        // 一秒数据量
        log.info("Number of dealt data per second:" + 1e9 / oneTime);
        log.info("Desen finished");

        // 保存处理后的Excel数据到outputStream中
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        workbook.write(byteArrayOutputStream);
        byte[] newExcelData = byteArrayOutputStream.toByteArray();
        fileOutputStream.write(newExcelData);

        // 标志脱敏完成
        desenCom = true;
        String globalID = System.currentTimeMillis() + randomNum.nextInt() + "脱敏工具集";

        byte[] desenFileBytes = Files.readAllBytes(desenFilePath.toAbsolutePath());
        // 脱敏前信息
        Long desenFileSize = Files.size(desenFilePath.toAbsolutePath());

        // 存证系统
        String evidenceID = util.getSM3Hash((new String(newExcelData, StandardCharsets.UTF_8) + util.getTime()).getBytes());

        logSenderManager.submitToFourSystems(globalID, evidenceID, desenCom, objectMode, infoBuilders, rawFileName,
                rawFileBytes, rawFileSize, desenFileName, desenFileBytes, desenFileSize, objectMode, rawFileSuffix, startTime, endTime);

        // 关闭工作簿和流
        fileOutputStream.close();
        workbook.close();
        inputStream.close();

        String encodedFileName = URLEncoder.encode(desenFileName, StandardCharsets.UTF_8.name());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
//        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename*=UTF-8''" + encodedFileName);
        headers.setContentDispositionFormData("attachment", encodedFileName); // 设置文件名

        return new ResponseEntity<>(desenFileBytes, headers, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<byte[]> dealSingleColumnTextFile(FileStorageDetails fileStorageDetails, String params, String algName, boolean ifSkipFirstRow) throws IOException, ParseException {
        HttpHeaders errorHttpHeaders = new HttpHeaders();
        errorHttpHeaders.add(HttpHeaders.CONTENT_TYPE, "text/plain");
        LogInfo logInfo = processSingleColumnTextFile(fileStorageDetails, params, algName, ifSkipFirstRow);
        // 构造日志收集结果
        LogCollectResult logCollectResult = logSenderManager.buildLogCollectResults(logInfo);
        // 构造响应结果
        CompletableFuture<ResponseEntity<byte[]>> responseEntityCompletableFuture = new CompletableFuture<>();
        // 获取文件信息
        byte[] rawFileBytes = logInfo.getRawFileBytes();
        byte[] desenFileBytes = logInfo.getDesenFileBytes();
        String desenFileName = logInfo.getDesenFileName();
        // 选择不同的日志发送方式
        // 选择首先发给评测系统评测
        if (ifSendToEvaFirst) {
            eventPublisher.publishEvent(new LogManagerEvent(this, fileStorageDetails,
                    logCollectResult, responseEntityCompletableFuture));
        }
        // 直接发给四个系统
        else {
            logSenderManager.submitToFourSystems(logCollectResult, rawFileBytes, desenFileBytes);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.TEXT_PLAIN);
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + desenFileName);
            responseEntityCompletableFuture.complete(new ResponseEntity<>(desenFileBytes, headers, HttpStatus.OK));
        }
        // 在此处等待响应返回
        try {
            return responseEntityCompletableFuture.get(10, TimeUnit.MINUTES);
        } catch (TimeoutException e) {
            log.error("等待处理结果超时：{}", e.getMessage());
            String errorMsg = "等待处理结果超时";
            return ResponseEntity.status(500).headers(errorHttpHeaders).body(errorMsg.getBytes());
        } catch (InterruptedException e) {
            log.error("等待处理结果时异常中断：{}", e.getMessage());
            String errorMsg = "等待处理结果时异常中断";
            return ResponseEntity.status(500).headers(errorHttpHeaders).body(errorMsg.getBytes());
        } catch (ExecutionException e) {
            log.error("等待处理结果时执行异常：{}", e.getMessage());
            String errorMsg = "等待处理结果时执行异常";
            return ResponseEntity.status(500).headers(errorHttpHeaders).body(errorMsg.getBytes());
        }
    }

    @Override
    public ResponseEntity<byte[]> replaceVideoBackground(FileStorageDetails fileStorageDetails, String params,
                                                         String algName, FileStorageDetails sheetStorageDetails) throws IOException {
        HttpHeaders errorHttpHeaders = new HttpHeaders();
        errorHttpHeaders.add(HttpHeaders.CONTENT_TYPE, "text/plain");

        LogInfo logInfo = processReplaceVideoBackground(fileStorageDetails, params, algName, sheetStorageDetails);

        // 构造日志收集结果
        LogCollectResult logCollectResult = logSenderManager.buildLogCollectResults(logInfo);
        // 构造响应结果
        CompletableFuture<ResponseEntity<byte[]>> responseEntityCompletableFuture = new CompletableFuture<>();
        // 获取文件信息
        byte[] rawFileBytes = logInfo.getRawFileBytes();
        byte[] desenFileBytes = logInfo.getDesenFileBytes();
        String desenFileName = logInfo.getDesenFileName();
        // 选择不同的日志发送方式
        // 选择首先发给评测系统评测
        if (ifSendToEvaFirst) {
            eventPublisher.publishEvent(new LogManagerEvent(this, fileStorageDetails,
                    logCollectResult, responseEntityCompletableFuture));
        }
        // 直接发给四个系统
        else {
            logSenderManager.submitToFourSystems(logCollectResult, rawFileBytes, desenFileBytes);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentDispositionFormData("attachment", desenFileName); // 设置文件名
            responseEntityCompletableFuture.complete(ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.parseMediaType("video/mp4")).body(desenFileBytes));
        }
        // 在此处等待响应返回
        try {
            return responseEntityCompletableFuture.get(10, TimeUnit.MINUTES);
        } catch (TimeoutException e) {
            log.error("等待处理结果超时：{}", e.getMessage());
            String errorMsg = "等待处理结果超时";
            return ResponseEntity.status(500).headers(errorHttpHeaders).body(errorMsg.getBytes());
        } catch (InterruptedException e) {
            log.error("等待处理结果时异常中断：{}", e.getMessage());
            String errorMsg = "等待处理结果时异常中断";
            return ResponseEntity.status(500).headers(errorHttpHeaders).body(errorMsg.getBytes());
        } catch (ExecutionException e) {
            log.error("等待处理结果时执行异常：{}", e.getMessage());
            String errorMsg = "等待处理结果时执行异常";
            return ResponseEntity.status(500).headers(errorHttpHeaders).body(errorMsg.getBytes());
        }

    }

    @Override
    public ResponseEntity<byte[]> replaceVideoBackground(MultipartFile file, String params, String algName, MultipartFile sheet)
            throws IOException {
        Boolean desenCom = false;
        DesenInfoStringBuilders infoBuilders = new DesenInfoStringBuilders();
        String objectMode = "video";
        AlgorithmInfo algorithmInfo = algorithmsFactory.getAlgorithmInfoFromName(algName);

        // 设置文件时间戳
        String fileTimeStamp = String.valueOf(System.currentTimeMillis());

        // 设置原文件信息
        if (file.getOriginalFilename() == null || sheet.getOriginalFilename() == null) {
            throw new IOException("Input file name is null");
        }
        // 设置原文件保存路径
        String rawFileName = fileTimeStamp + file.getOriginalFilename();
        String imageFileName = fileTimeStamp + sheet.getOriginalFilename();
        String rawFileSuffix = rawFileName.substring(rawFileName.lastIndexOf(".") + 1);
        Path rawFilePath = rawFileDirectory.resolve(rawFileName);
        Path rawBgPath = rawFileDirectory.resolve(imageFileName);
        String rawFilePathString = rawFilePath.toAbsolutePath().toString();
        String rawBgPathString = rawBgPath.toAbsolutePath().toString();
        byte[] rawFileBytes = file.getBytes();
        Long rawFileSize = file.getSize();

        // 保存源文件
        file.transferTo(rawFilePath.toAbsolutePath());
        sheet.transferTo(rawBgPath.toAbsolutePath());

        // 设置脱敏后文件路径信息
        String desenFileName = "desen_" + rawFileName;
        Path desenFilePath = desenFileDirectory.resolve(desenFileName);
        String desenFilePathString = desenFilePath.toAbsolutePath().toString();

        String desenParam = String.valueOf(params.charAt(params.length() - 1));

        long startTimePoint;
        long endTimePoint;
        long executionTime = 0;
        String startTime;
        String endTime = util.getTime();

        // 开始时间
        // 脱敏开始时间
        startTime = util.getTime();
        startTimePoint = System.currentTimeMillis();
        // 执行脱敏算法
        List<String> rawData = Arrays.asList(rawFilePathString, rawBgPathString, desenFilePathString);
        DSObject dsObject = new DSObject(rawData);
        // 调用脱敏程序处理
        String resultString = algorithmInfo.execute(dsObject, Integer.valueOf(params)).getList().toString();
        log.info(resultString);
        // 结束时间
        endTimePoint = System.currentTimeMillis();
        executionTime = endTimePoint - startTimePoint;
        // 脱敏耗时
        logCollectUtil.logExecutionTime(String.valueOf(executionTime), "Video");
        byte[] desenFileBytes = Files.readAllBytes(desenFilePath.toAbsolutePath());
        Long desenFileSize = Files.size(desenFilePath.toAbsolutePath());
        // 标志脱敏完成
        desenCom = true;
        String globalID = System.currentTimeMillis() + randomNum.nextInt() + "脱敏工具集";

        // 脱敏参数
//        int[] param = algorithmInfo.getParams().toArray();
//        List<Object> algParams = algorithmInfo.getParams();
//        if (algParams == null) {
//            infoBuilders.desenAlgParam.append("无参");
//        } else {
//            infoBuilders.desenAlgParam.append(algParams);
//        }
        // 脱敏算法
        infoBuilders.desenAlg.append(55);
        // 脱敏参数
        infoBuilders.desenAlgParam.append(rawBgPathString);
        // 脱敏级别
        infoBuilders.desenLevel.append(Integer.parseInt(desenParam) + 1);
        // 脱敏前类型
        infoBuilders.desenInfoPreIden.append("video");
        // 脱敏后类型
        infoBuilders.desenInfoAfterIden.append("video");
        // 脱敏意图
        infoBuilders.desenIntention.append("对视频脱敏");
        // 脱敏要求
        infoBuilders.desenRequirements.append("对视频脱敏");
        // 脱敏数据类型
        infoBuilders.fileDataType.append(rawFileSuffix);
        ObjectMapper objectMapper = new ObjectMapper();
        // 存证系统
        //存证请求  消息版本：中心0x1000，0x1010; 本地0x1100，0x1110
        String evidenceID = util.getSM3Hash((new String(desenFileBytes, StandardCharsets.UTF_8) + util.getTime()).getBytes());

        logSenderManager.submitToFourSystems(globalID, evidenceID, desenCom, objectMode, infoBuilders, rawFileName,
                rawFileBytes, rawFileSize, desenFileName, desenFileBytes, desenFileSize, objectMode, rawFileSuffix, startTime, endTime);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "video/mp4");
        headers.setContentDispositionFormData("attachment", desenFileName); // 设置文件名

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.parseMediaType("video/mp4")).body(desenFileBytes);

    }

    @Override
    public ResponseEntity<byte[]> replaceFace(FileStorageDetails fileStorageDetails, String params, String algName,
                                              FileStorageDetails sheetStorageDetails) throws IOException {
        HttpHeaders errorHttpHeaders = new HttpHeaders();
        errorHttpHeaders.add(HttpHeaders.CONTENT_TYPE, "text/plain");
        // 处理图片
        LogInfo logInfo = processReplaceFace(fileStorageDetails, params, algName, sheetStorageDetails);
        LogCollectResult logCollectResult = logSenderManager.buildLogCollectResults(logInfo);
        // 构造响应结果
        CompletableFuture<ResponseEntity<byte[]>> responseEntityCompletableFuture = new CompletableFuture<>();
        // 获取文件信息
        byte[] rawFileBytes = logInfo.getRawFileBytes();
        byte[] desenFileBytes = logInfo.getDesenFileBytes();
        String desenFileName = logInfo.getDesenFileName();
        // 选择不同的日志发送方式
        // 选择首先发给评测系统评测
        if (ifSendToEvaFirst) {
            eventPublisher.publishEvent(new LogManagerEvent(this, fileStorageDetails,
                    logCollectResult, responseEntityCompletableFuture));
        }
        // 直接发给四个系统
        else {
            logSenderManager.submitToFourSystems(logCollectResult, rawFileBytes, desenFileBytes);
            HttpHeaders headers = new HttpHeaders();
            if (logInfo.getRawFileSuffix().equals("png")) {
                headers.setContentType(MediaType.IMAGE_PNG);
            } else {
                headers.setContentType(MediaType.IMAGE_JPEG);
            }
            headers.setContentDispositionFormData("attachment", desenFileName); // 设置文件名
            responseEntityCompletableFuture.complete(new ResponseEntity<>(desenFileBytes, headers, HttpStatus.OK));
        }
        // 在此处等待响应返回
        // TODO: Controller Advice
        try {
            return responseEntityCompletableFuture.get(10, TimeUnit.MINUTES);
        } catch (TimeoutException e) {
            log.error("等待处理结果超时：{}", e.getMessage());
            String errorMsg = "等待处理结果超时";
            return ResponseEntity.status(500).headers(errorHttpHeaders).body(errorMsg.getBytes());
        } catch (InterruptedException e) {
            log.error("等待处理结果时异常中断：{}", e.getMessage());
            String errorMsg = "等待处理结果时异常中断";
            return ResponseEntity.status(500).headers(errorHttpHeaders).body(errorMsg.getBytes());
        } catch (ExecutionException e) {
            log.error("等待处理结果时执行异常：{}", e.getMessage());
            String errorMsg = "等待处理结果时执行异常";
            return ResponseEntity.status(500).headers(errorHttpHeaders).body(errorMsg.getBytes());
        }
    }

    @Override
    public ResponseEntity<byte[]> replaceFace(MultipartFile file, String params, String algName, MultipartFile sheet) throws IOException {
        Boolean desenCom = false;
        DesenInfoStringBuilders infoBuilders = new DesenInfoStringBuilders();
        String objectMode = "image";
        AlgorithmInfo algorithmInfo = algorithmsFactory.getAlgorithmInfoFromName(algName);
        // 设置文件时间戳
        String fileTimeStamp = String.valueOf(System.currentTimeMillis());

        // 设置原文件信息
        if (file.getOriginalFilename() == null || sheet.getOriginalFilename() == null) {
            throw new IOException("Input file name is null");
        }
        // 设置原文件保存路径
        String rawFileName = fileTimeStamp + file.getOriginalFilename();
        String imageFileName = fileTimeStamp + sheet.getOriginalFilename();
        String rawFileSuffix = rawFileName.substring(rawFileName.lastIndexOf(".") + 1);
        Path rawFilePath = rawFileDirectory.resolve(rawFileName);
        Path rawFacePath = rawFileDirectory.resolve(imageFileName);
        String rawFilePathString = rawFilePath.toAbsolutePath().toString();
        String rawFacePathString = rawFacePath.toAbsolutePath().toString();
        byte[] rawFileBytes = file.getBytes();
        Long rawFileSize = file.getSize();

        // 保存源文件
        file.transferTo(rawFilePath.toAbsolutePath());
        sheet.transferTo(rawFacePath.toAbsolutePath());
        // 设置脱敏后文件路径信息
        String desenFileName = "desen_" + rawFileName;
        Path desenFilePath = desenFileDirectory.resolve(desenFileName);
        String desenFilePathString = desenFilePath.toAbsolutePath().toString();

        // 调用脱敏程序处理

        String desenParam = String.valueOf(params.charAt(params.length() - 1));
        long startTimePoint;
        long endTimePoint;
        long executionTime;
        String startTime;
        // 开始时间
        // 脱敏开始时间
        startTime = util.getTime();
        startTimePoint = System.currentTimeMillis();
        List<String> rawData = Arrays.asList(rawFilePathString, rawFacePathString, desenFilePathString);
        DSObject dsObject = new DSObject(rawData);
        String resultString = algorithmInfo.execute(dsObject, Integer.valueOf(params)).getList().toString();
        if (resultString != null) {
            log.info(resultString);
        }
        // 结束时间
        endTimePoint = System.currentTimeMillis();
        executionTime = endTimePoint - startTimePoint;
        // 脱敏耗时
        logCollectUtil.logExecutionTime(String.valueOf(executionTime), "Image");

        // 脱敏结束时间
        String endTime = util.getTime();
        // 标志脱敏完成
        desenCom = true;
        String globalID = System.currentTimeMillis() + randomNum.nextInt() + "脱敏工具集";
        // 脱敏后文件字节流
        Long desenFileSize = Files.size(desenFilePath.toAbsolutePath());
        byte[] desenFileBytes = Files.readAllBytes(desenFilePath.toAbsolutePath());
        // 脱敏算法
        infoBuilders.desenAlg.append(algorithmInfo.getId());
        // 脱敏参数
        infoBuilders.desenAlgParam.append(rawFacePathString);
        // 脱敏前类型
        infoBuilders.desenInfoPreIden.append("image");
        // 脱敏后类型
        infoBuilders.desenInfoAfterIden.append("image");
        // 脱敏意图
        infoBuilders.desenIntention.append("对图像脱敏");
        // 脱敏要求
        infoBuilders.desenRequirements.append("对图像脱敏");
        // 脱敏数据类型
        infoBuilders.fileDataType.append(rawFileSuffix);
        // 设置脱敏等级
        infoBuilders.desenLevel.append(Integer.parseInt(desenParam) + 1);
        // 存证系统
        //存证请求  消息版本：中心0x1000，0x1010; 本地0x1100，0x1110
        String evidenceID = util.getSM3Hash((new String(desenFileBytes, StandardCharsets.UTF_8) + util.getTime()).getBytes());

        logSenderManager.submitToFourSystems(globalID, evidenceID, desenCom, objectMode, infoBuilders, rawFileName,
                rawFileBytes, rawFileSize, desenFileName, desenFileBytes, desenFileSize, objectMode, rawFileSuffix, startTime, endTime);
        HttpHeaders headers = new HttpHeaders();
        if (rawFileName.contains("jpg")) {
            headers.setContentType(MediaType.IMAGE_JPEG);
        } else if (rawFileName.contains("png")) {
            headers.setContentType(MediaType.IMAGE_PNG);
        }
        headers.setContentDispositionFormData("attachment", desenFileName); // 设置文件名

        return ResponseEntity.ok()
                .headers(headers).body(desenFileBytes);
    }

    @Override
    public ResponseEntity<byte[]> replaceFaceVideo(FileStorageDetails fileStorageDetails, String params, String algName,
                                                   FileStorageDetails sheetStorageDetails) throws IOException {
        HttpHeaders errorHttpHeaders = new HttpHeaders();
        errorHttpHeaders.add(HttpHeaders.CONTENT_TYPE, "text/plain");

        LogInfo logInfo = processReplaceFaceVideo(fileStorageDetails, params, algName, sheetStorageDetails);

        // 构造日志收集结果
        LogCollectResult logCollectResult = logSenderManager.buildLogCollectResults(logInfo);
        // 构造响应结果
        CompletableFuture<ResponseEntity<byte[]>> responseEntityCompletableFuture = new CompletableFuture<>();
        // 获取文件信息
        byte[] rawFileBytes = logInfo.getRawFileBytes();
        byte[] desenFileBytes = logInfo.getDesenFileBytes();
        String desenFileName = logInfo.getDesenFileName();
        // 选择不同的日志发送方式
        // 选择首先发给评测系统评测
        if (ifSendToEvaFirst) {
            eventPublisher.publishEvent(new LogManagerEvent(this, fileStorageDetails,
                    logCollectResult, responseEntityCompletableFuture));
        }
        // 直接发给四个系统
        else {
            logSenderManager.submitToFourSystems(logCollectResult, rawFileBytes, desenFileBytes);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentDispositionFormData("attachment", desenFileName); // 设置文件名
            responseEntityCompletableFuture.complete(ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.parseMediaType("video/mp4")).body(desenFileBytes));
        }
        // 在此处等待响应返回
        try {
            return responseEntityCompletableFuture.get(10, TimeUnit.MINUTES);
        } catch (TimeoutException e) {
            log.error("等待处理结果超时：{}", e.getMessage());
            String errorMsg = "等待处理结果超时";
            return ResponseEntity.status(500).headers(errorHttpHeaders).body(errorMsg.getBytes());
        } catch (InterruptedException e) {
            log.error("等待处理结果时异常中断：{}", e.getMessage());
            String errorMsg = "等待处理结果时异常中断";
            return ResponseEntity.status(500).headers(errorHttpHeaders).body(errorMsg.getBytes());
        } catch (ExecutionException e) {
            log.error("等待处理结果时执行异常：{}", e.getMessage());
            String errorMsg = "等待处理结果时执行异常";
            return ResponseEntity.status(500).headers(errorHttpHeaders).body(errorMsg.getBytes());
        }
    }

    @Override
    public ResponseEntity<byte[]> replaceFaceVideo(MultipartFile file, String params, String algName, MultipartFile sheet) throws IOException {
        Boolean desenCom = false;
        DesenInfoStringBuilders infoBuilders = new DesenInfoStringBuilders();
        String objectMode = "video";
        AlgorithmInfo algorithmInfo = algorithmsFactory.getAlgorithmInfoFromName(algName);

        // 设置文件时间戳
        String fileTimeStamp = String.valueOf(System.currentTimeMillis());

        // 设置原文件信息
        if (file.getOriginalFilename() == null || sheet.getOriginalFilename() == null) {
            throw new IOException("Input file name is null");
        }
        // 设置原文件保存路径
        String rawFileName = fileTimeStamp + file.getOriginalFilename();
        String imageFileName = fileTimeStamp + sheet.getOriginalFilename();
        String rawFileSuffix = rawFileName.substring(rawFileName.lastIndexOf(".") + 1);
        Path rawFilePath = rawFileDirectory.resolve(rawFileName);
        Path rawFacePath = rawFileDirectory.resolve(imageFileName);
        String rawFilePathString = rawFilePath.toAbsolutePath().toString();
        String rawFacePathString = rawFacePath.toAbsolutePath().toString();
        byte[] rawFileBytes = file.getBytes();
        Long rawFileSize = file.getSize();

        // 保存源文件
        file.transferTo(rawFilePath.toAbsolutePath());
        sheet.transferTo(rawFacePath.toAbsolutePath());
        // 设置脱敏后文件路径信息
        String desenFileName = "desen_" + rawFileName;
        Path desenFilePath = desenFileDirectory.resolve(desenFileName);
        String desenFilePathString = desenFilePath.toAbsolutePath().toString();

        // 调用脱敏程序处理
        String desenParam = String.valueOf(params.charAt(params.length() - 1));
        long startTimePoint;
        long endTimePoint;
        long executionTime;
        String startTime;
        // 开始时间
        // 脱敏开始时间
        startTime = util.getTime();
        startTimePoint = System.currentTimeMillis();

        // 执行脱敏
        DSObject dsObject = new DSObject(Arrays.asList(rawFilePathString, rawFacePathString, desenFilePathString));
        algorithmInfo.execute(dsObject);
        // 结束时间
        endTimePoint = System.currentTimeMillis();
        // 脱敏耗时
        executionTime = endTimePoint - startTimePoint;
        logCollectUtil.logExecutionTime(String.valueOf(executionTime), objectMode);

        // 脱敏结束时间
        String endTime = util.getTime();
        // 标志脱敏完成
        desenCom = true;
        String globalID = System.currentTimeMillis() + randomNum.nextInt() + "脱敏工具集";
        // rawFileBytes
        byte[] desenFileBytes = Files.readAllBytes(desenFilePath.toAbsolutePath());
        Long desenFileSize = Files.size(desenFilePath.toAbsolutePath());

        // 脱敏算法
        infoBuilders.desenAlg.append(algorithmInfo.getId());
        // 脱敏参数
        infoBuilders.desenAlgParam.append(rawFacePathString);
        // 脱敏级别
        infoBuilders.desenLevel.append(Integer.parseInt(desenParam) + 1);
        // 脱敏前类型
        infoBuilders.desenInfoPreIden.append("video");
        // 脱敏后类型
        infoBuilders.desenInfoAfterIden.append("video");
        // 脱敏意图
        infoBuilders.desenIntention.append("对视频脱敏");
        // 脱敏要求
        infoBuilders.desenRequirements.append("对视频脱敏");
        // 脱敏数据类型
        infoBuilders.fileDataType.append(rawFileSuffix);

        // 存证系统
        //存证请求  消息版本：中心0x1000，0x1010; 本地0x1100，0x1110
        String evidenceID = util.getSM3Hash((new String(desenFileBytes, StandardCharsets.UTF_8) + util.getTime()).getBytes());
        logSenderManager.submitToFourSystems(globalID, evidenceID, desenCom, objectMode, infoBuilders, rawFileName,
                rawFileBytes, rawFileSize, desenFileName, desenFileBytes, desenFileSize, objectMode, rawFileSuffix, startTime, endTime);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "video/mp4");
        headers.setContentDispositionFormData("attachment", desenFileName); // 设置文件名
        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.parseMediaType("video/mp4")).body(desenFileBytes);
    }

    @Override
    public String desenText(String textInput, String textType, String privacyLevel, String algName) throws ParseException {
        int param = 1;
        log.info("File Type: " + textType);
        log.info("AlgName: " + algName);

        String result = null;
        if (!privacyLevel.isEmpty()) {
            param = Integer.parseInt(privacyLevel);
        }

        textInput = textInput.trim();
        AlgorithmInfo algorithmInfo = algorithmsFactory.getAlgorithmInfoFromName(algName);
        switch (algName.trim()) {
            case "addressHide":
            case "numberHide":
            case "nameHide":
            case "passReplace":
            case "truncation":
            case "floorTime":
            case "suppressEmail":
            case "value_hide":
            case "SHA512":
            case "suppressAllIp":
            case "suppressIpRandomParts": {
                List<String> input = Collections.singletonList(textInput);
                DSObject rawData = new DSObject(input);
                result = algorithmInfo.execute(rawData, param).getList().get(0).toString();
                break;
            }

            case "randomLaplaceToValue":
            case "randomUniformToValue":
            case "randomGaussianToValue":
            case "valueShift":
            case "floor":
            case "valueMapping":
            case "laplaceToValue":
            case "gaussianToValue": {
                List<Double> val = Arrays.stream(textInput.split(","))
                        .map(x -> x == null ? null : Double.parseDouble(x))
                        .collect(Collectors.toList());
                DSObject rawVal = new DSObject(val);
                StringBuilder builder = new StringBuilder();
                List<?> temp = algorithmInfo.execute(rawVal, param).getList();
                for (Object elem : temp) {
                    builder.append(elem.toString()).append(",");
                }
                result = builder.substring(0, builder.length() - 1);
                break;
            }
            case "dpDate":
            case "dpCode":
            case "date_group_replace":
                String[] inputList = textInput.trim().split(",");
                DSObject codes = new DSObject(Arrays.asList(inputList));
                StringBuilder sb = new StringBuilder();
                List<?> result_b = algorithmInfo.execute(codes, param).getList();
                for (Object a : result_b) {
                    sb.append(a.toString()).append(",");
                }
                result = sb.substring(0, sb.length() - 1);
                break;

        }
        log.info(result);
        return result;
    }

    @Override
    public FileStorageDetails generateTextTestFile(int totalNumber) throws IOException {
        String fileName = "textTestFile.txt"; // 定义输出文件的路径

        // 使用随机数生成器生成数值数据
        Random random = new Random();
        FileStorageDetails rawFileStorageDetails = fileStorageService.saveRawFile(fileName);
//        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        OutputStreamWriter writer = new OutputStreamWriter(Files.newOutputStream(rawFileStorageDetails.getRawFilePath()),
                StandardCharsets.UTF_8);

        for (int i = 0; i < totalNumber; i++) {
            double randomNumber = 10000 * random.nextDouble(); // 生成随机浮点数
            writer.write(String.valueOf(randomNumber));
            writer.write("\n"); // 每个数字后写入一个新行
        }

        writer.flush();  // 确保所有数据都写入到ByteArrayOutputStream中
        writer.close();

        return rawFileStorageDetails; // 保存文件到文件系统

    }

    private void saveExcelParamsToDatabase(String sheetName, Boolean ifSaveExcelParam, List<ExcelParam> excelParamList) {
        if (ifSaveExcelParam) {
            excelParamService.deleteAll(sheetName + "_param");
            excelParamService.insertAll(sheetName + "_param", excelParamList);
        }
    }

    /**
     * 向InfoBuilder中添加每列的数据类型、脱敏前后的信息类型标识、脱敏级别、脱敏意图、脱敏算法
     *
     * @param infoBuilders 脱敏日志信息构建器
     * @param excelParam   传递进函数的Excel脱敏参数
     * @param columnName   当前处理的Excel列名
     * @param algoNum      当前使用的算法编号
     */
    private void addDataTypeAndInfoIden(DesenInfoStringBuilders infoBuilders, ExcelParam excelParam, String columnName, int algoNum) {
        // 添加每列的数据类型
        infoBuilders.fileDataType.append(excelParam.getDataType()).append(",");
        // 脱敏前信息类型标识
        infoBuilders.desenInfoPreIden.append(columnName).append(",");
        // 脱敏后信息类型标识
        infoBuilders.desenInfoAfterIden.append(columnName).append(",");
        // 读取脱敏级别
        infoBuilders.desenLevel.append(excelParam.getTmParam()).append(",");
        // 脱敏意图：列名+脱敏，
        infoBuilders.desenIntention.append(excelParam.getColumnName()).append("脱敏,");
        // 脱敏算法
        infoBuilders.desenAlg.append(algoNum).append(",");
    }

    /**
     * 从Excel脱敏参数中获取脱敏等级
     * @param algorithmInfo 封装脱敏结果的算法信息类
     * @param rawData 原始数据
     * @param excelParam Excel脱敏参数
     * @return 包含脱敏结果的List
     * @param <T>
     */
    public static <T> List<T> getDsList(AlgorithmInfo algorithmInfo, DSObject rawData, ExcelParam excelParam) {
        log.info("当前列脱敏算法名称: " + algorithmInfo.getName());
        log.info("当前列脱敏算法编号: " + algorithmInfo.getId());
        return algorithmInfo.execute(rawData, excelParam.getTmParam()).getList()
                .stream()
                .map(item -> item != null ? (T) item : null)
                .collect(Collectors.toList());
    }

    /**
     * 使用对应的脱敏算法执行脱敏并获取结果
     * @param algorithmInfo
     * @param rawData
     * @param param
     * @return 包含脱敏结果的List
     * @param <T>
     */
    public static <T> List<T> getDsList(AlgorithmInfo algorithmInfo, DSObject rawData, int param) {
        return algorithmInfo.execute(rawData, param).getList()
                .stream()
                .map(item -> item != null ? (T) item : null)
                .collect(Collectors.toList());
    }

    /**
     * 为特定脱敏字段增加脱敏等级
     *
     * @param originalExcelParam 原始参数
     * @param desenLevel         新脱敏等级
     * @param ifUpdateDesenLevel
     * @return
     */
    private ExcelParam addDesenLevel(ExcelParam originalExcelParam, int desenLevel, Boolean ifUpdateDesenLevel) {
        int desenLevelNum = desenLevel;
        if (ifUpdateDesenLevel) {
            if (desenLevelNum != 3) {
                desenLevelNum += 1;
                originalExcelParam.setTmParam(desenLevelNum);
            } else {
                originalExcelParam.setTmParam(desenLevelNum);
            }
        } else {
            originalExcelParam.setTmParam(desenLevelNum);
        }
        return originalExcelParam;
    }
}
package com.lu.gademo.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.lu.gademo.entity.ExcelParam;
import com.lu.gademo.entity.effectEva.SendEvaReq;
import com.lu.gademo.entity.evidence.ReqEvidenceSave;
import com.lu.gademo.entity.evidence.SubmitEvidenceLocal;
import com.lu.gademo.entity.ruleCheck.SendRuleReq;
import com.lu.gademo.log.SendData;
import com.lu.gademo.service.ExcelParamService;
import com.lu.gademo.service.FileService;
import com.lu.gademo.utils.*;
import com.lu.gademo.utils.impl.DpUtilImpl;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Date;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

@Slf4j
@Service
@Data
public class FileServiceImpl implements FileService {

    private AlgorithmsFactory algorithmsFactory;

    private Dp dp;

    private Replace replacement;

    private Generalization generalization;

    private Anonymity anonymity;
    // 发送类
    private SendData sendData;

    private ExcelParamService excelParamService;
    // 工具类

    private Util util;

    private Integer systemID;

    private Integer evidenceRequestMainCommand;

    private Integer evidenceRequestSubCommand;

    private Integer evidenceRequestMsgVersion;


    private Integer evidenceSubmitMainCommand;

    private Integer evidenceSubmitSubCommand;

    private Integer evidenceSubmitMsgVersion;
    // 脱敏完成情况
//    private Boolean desenCom;
    // 脱敏对象大小
    // TODO: 删除该变量
//    private Integer objectSize;
    private Random randomNum;
    private String desenPerformer;

    private Path currentDirectory;
    private Path rawFileDirectory;
    private Path desenFileDirectory;

    @Autowired
    public FileServiceImpl(AlgorithmsFactory algorithmsFactory, Dp dp,
                           Replace replacement, Generalization generalization, Anonymity anonymity,
                           SendData sendData, Util util, ExcelParamService excelParamService,
                           @Value("${systemId.desenToolsetSystemId}") int systemID,
                           @Value("${evidenceSystem.evidenceRequest.mainCommand}") Integer evidenceRequestMainCommand,
                           @Value("${evidenceSystem.evidenceRequest.subCommand}") Integer evidenceRequestSubCommand,
                           @Value("${evidenceSystem.evidenceRequest.msgVersion}") Integer evidenceRequestMsgVersion,
                           @Value("${evidenceSystem.submitEvidence.mainCommand}") Integer evidenceSubmitMainCommand,
                           @Value("${evidenceSystem.submitEvidence.subCommand}") Integer evidenceSubmitSubCommand,
                           @Value("${evidenceSystem.submitEvidence.msgVersion}") Integer evidenceSubmitMsgVersion) throws IOException {
        this.algorithmsFactory = algorithmsFactory;
        this.dp = dp;
        this.replacement = replacement;
        this.generalization = generalization;
        this.anonymity = anonymity;
        this.sendData = sendData;
        this.util = util;
        this.excelParamService = excelParamService;
        this.systemID = systemID;
        this.evidenceRequestMainCommand = evidenceRequestMainCommand;
        this.evidenceRequestSubCommand = evidenceRequestSubCommand;
        this.evidenceRequestMsgVersion = evidenceRequestMsgVersion;
        this.evidenceSubmitMainCommand = evidenceSubmitMainCommand;
        this.evidenceSubmitSubCommand = evidenceSubmitSubCommand;
        this.evidenceSubmitMsgVersion = evidenceSubmitMsgVersion;
//        this.desenCom = false;
        this.randomNum = new Random();
        this.desenPerformer = "脱敏工具集";

        this.currentDirectory = Paths.get("");
        this.rawFileDirectory = Paths.get("raw_files");
        this.desenFileDirectory = Paths.get("desen_files");

        if (!Files.exists(rawFileDirectory)) {
            Files.createDirectory(rawFileDirectory);
        }
        if (!Files.exists(desenFileDirectory)) {
            Files.createDirectory(desenFileDirectory);
        }
        log.info("rawFileDirectory: " + rawFileDirectory.toAbsolutePath().toString());
        log.info("desenFileDirectory: " + desenFileDirectory.toAbsolutePath().toString());
    }

    // 构造存证请求
    private ReqEvidenceSave buildReqEvidenceSave(Long rawFileSize, String objectMode, String evidenceID) {
        ReqEvidenceSave reqEvidenceSave = new ReqEvidenceSave();
        reqEvidenceSave.setSystemID(systemID);
        reqEvidenceSave.setSystemIP(util.getIP());
        reqEvidenceSave.setMainCMD(evidenceRequestMainCommand);
        reqEvidenceSave.setSubCMD(evidenceRequestSubCommand);
        reqEvidenceSave.setMsgVersion(evidenceRequestMsgVersion);
        reqEvidenceSave.setObjectSize(rawFileSize);
        reqEvidenceSave.setObjectMode(objectMode);
        reqEvidenceSave.setEvidenceID(evidenceID);
        return reqEvidenceSave;
    }

    private SubmitEvidenceLocal buildSubmitEvidenceLocal(String evidenceID, StringBuilder desenAlg,
                                                         String rawFileName, byte[] rawFileBytes, Long rawFileSize,
                                                         byte[] desenFileBytes, String globalID,
                                                         String desenInfoPreIden, StringBuilder desenIntention,
                                                         StringBuilder desenRequirements, String desenControlSet,
                                                         StringBuilder desenAlgParam, String startTime,
                                                         String endTime, StringBuilder desenLevel,
                                                         Boolean desenCom) {
        SubmitEvidenceLocal submitEvidenceLocal = new SubmitEvidenceLocal();
        submitEvidenceLocal.setSystemID(systemID);
        submitEvidenceLocal.setSystemIP(util.getIP());
        submitEvidenceLocal.setMainCMD(evidenceSubmitMainCommand);
        submitEvidenceLocal.setSubCMD(evidenceSubmitSubCommand);
        submitEvidenceLocal.setEvidenceID(evidenceID);
        submitEvidenceLocal.setMsgVersion(evidenceSubmitMsgVersion);

        String rawFileHash = util.getSM3Hash(rawFileBytes);
        String fileTitle = "脱敏工具集脱敏" + rawFileName + "文件存证记录";
        String fileAbstract = "脱敏工具集采用算法" + desenAlg + "脱敏" + rawFileName + "文件存证记录";
        String fileKeyword = rawFileName + desenInfoPreIden;
        String desenFileHash = util.getSM3Hash(desenFileBytes);

        // 设置data中的内容
        submitEvidenceLocal.setGlobalID(globalID);
        submitEvidenceLocal.setStatus("数据已脱敏");
        // optTime在SendData中设置
        submitEvidenceLocal.setFileTitle(fileTitle);
        submitEvidenceLocal.setFileAbstract(fileAbstract);
        submitEvidenceLocal.setFileKeyword(fileKeyword);
        submitEvidenceLocal.setDesenAlg(desenAlg.toString());
        submitEvidenceLocal.setFileSize(rawFileSize);
        submitEvidenceLocal.setFileHASH(rawFileHash);
        // TODO: 文件签名是否需要变？
        submitEvidenceLocal.setFileSig(rawFileHash);
        submitEvidenceLocal.setDesenPerformer(desenPerformer);
        // TODO: 这里直接使用是否会引起问题？
        submitEvidenceLocal.setDesenCom(desenCom);
        submitEvidenceLocal.setDesenInfoPreID(rawFileHash);
        submitEvidenceLocal.setDesenInfoAfterID(desenFileHash);
        submitEvidenceLocal.setDesenIntention(desenIntention.toString());
        submitEvidenceLocal.setDesenRequirements(desenRequirements.toString());
        submitEvidenceLocal.setDesenControlSet(desenControlSet);
        submitEvidenceLocal.setDesenAlgParam(desenAlgParam.toString());
        submitEvidenceLocal.setDesenPerformStartTime(startTime);
        submitEvidenceLocal.setDesenPerformEndTime(endTime);
        submitEvidenceLocal.setDesenLevel(desenLevel.toString());
        return submitEvidenceLocal;
    }

    private SendEvaReq buildSendEvaReq(String globalID, String evidenceID,
                                       String rawFileName, byte[] rawFileBytes, Long rawFileSize,
                                       String desenFileName, byte[] desenFileBytes, Long desenFileSize,
                                       StringBuilder desenInfoPreIden, StringBuilder desenInfoAfterIden,
                                       StringBuilder desenIntention, StringBuilder desenRequirements,
                                       String desenControlSet, StringBuilder desenAlg,
                                       StringBuilder desenAlgParam, String startTime, String endTime,
                                       StringBuilder desenLevel, String fileType, String rawFileSuffix,
                                       Boolean desenCom) {
        SendEvaReq sendEvaReq = new SendEvaReq();
        sendEvaReq.setEvaRequestId(util.getSM3Hash((new String(desenFileBytes, StandardCharsets.UTF_8) + util.getTime()).getBytes()));
        sendEvaReq.setSystemID(systemID);
        sendEvaReq.setEvidenceID(evidenceID);
        sendEvaReq.setGlobalID(globalID);
        sendEvaReq.setDesenInfoPreIden(desenInfoPreIden.toString());
        sendEvaReq.setDesenInfoAfterIden(desenInfoAfterIden.toString());
        sendEvaReq.setDesenInfoPreId(util.getSM3Hash(rawFileBytes));
        sendEvaReq.setDesenInfoPre(rawFileName);
        sendEvaReq.setDesenInfoAfterId(util.getSM3Hash(desenFileBytes));
        sendEvaReq.setDesenInfoAfter(desenFileName);
        sendEvaReq.setDesenIntention(desenIntention.toString());
        sendEvaReq.setDesenRequirements(desenRequirements.toString());
        sendEvaReq.setDesenControlSet(desenControlSet);
        sendEvaReq.setDesenAlg(desenAlg.toString());
        sendEvaReq.setDesenAlgParam(desenAlgParam.toString());
        sendEvaReq.setDesenPerformStartTime(startTime);
        sendEvaReq.setDesenPerformEndTime(endTime);
        sendEvaReq.setDesenLevel(desenLevel.toString());
        sendEvaReq.setDesenPerformer(desenPerformer);
        sendEvaReq.setDesenCom(desenCom);
        sendEvaReq.setRawFileSize(rawFileSize);
        sendEvaReq.setDesenFileSize(desenFileSize);
        sendEvaReq.setFileType(fileType);
        sendEvaReq.setFileSuffix(rawFileSuffix);
        sendEvaReq.setStatus("数据已脱敏");
        return sendEvaReq;

    }

    private List<ExcelParam> jsonStringToParams(String params) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(params, new TypeReference<List<ExcelParam>>() {
        });

    }


    private SendRuleReq buildSendRuleReq(String evidenceID, byte[] rawFileBytes, byte[] desenFileBytes,
                                         StringBuilder desenInfoAfterIden, StringBuilder desenIntention,
                                         StringBuilder desenRequirements, String desenControlSet,
                                         StringBuilder desenAlg, StringBuilder desenAlgParam,
                                         String startTime, String endTime, StringBuilder desenLevel,
                                         Boolean desenCom
                                         ) {
        SendRuleReq sendRuleReq = new SendRuleReq();
        sendRuleReq.setEvidenceId(evidenceID);
        sendRuleReq.setDesenInfoAfterIden(desenInfoAfterIden.toString());
        sendRuleReq.setDesenInfoPre(util.getSM3Hash(rawFileBytes));
        sendRuleReq.setDesenInfoAfter(util.getSM3Hash(desenFileBytes));
        sendRuleReq.setDesenIntention(desenIntention.toString());
        sendRuleReq.setDesenRequirements(desenRequirements.toString());
        sendRuleReq.setDesenControlSet(desenControlSet);
        sendRuleReq.setDesenAlg(desenAlg.toString());
        sendRuleReq.setDesenAlgParam(desenAlgParam.toString());
        sendRuleReq.setDesenPerformStartTime(startTime);
        sendRuleReq.setDesenPerformEndTime(endTime);
        sendRuleReq.setDesenLevel(desenLevel.toString());
        sendRuleReq.setDesenPerformer(desenPerformer);
        // TODO: 改成变量的形式
        sendRuleReq.setDesenCom(desenCom);
        return sendRuleReq;
    }

    private void logExecutionTime(String executionTime, String objectMode) {
        log.info("Desensitization finished in " + executionTime + "ms");
        log.info(objectMode + " desen finished");
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

        log.info("Start image desen");
        // 脱敏开始时间
        String startTime = util.getTime();
        long startTimePoint = System.currentTimeMillis();

        if (algName.equals("retrieval")) {
            Path desenAppPath = currentDirectory.resolve("image").resolve("ImageRetrieval");
            Path desenApp = desenAppPath.resolve("FUNC.py");
            CommandExecutor.executePython(rawFilePathString + " " + desenFilePathString, "",
                    desenApp.toAbsolutePath().toString());
            infoBuilders.desenAlg.append("49");
            infoBuilders.desenAlgParam.append("无参");
            infoBuilders.desenLevel.append(0);
        } else {
            AlgorithmInfo algorithmInfo = algorithmsFactory.getAlgorithmInfoFromName(algName);
            List<Object> algorithmParams = algorithmInfo.getParams();
            infoBuilders.desenAlg.append(algorithmInfo.getId());
            if (algorithmInfo.getParams() == null) {
                infoBuilders.desenAlgParam.append("无参");
            } else {
                infoBuilders.desenAlgParam.append(algorithmParams.get(desenParam).toString());
            }
            // TODO: 非失真算法的脱敏级别
            infoBuilders.desenLevel.append(desenParam + 1);
            algorithmInfo.execute(dsObject, desenParam);
        }

        // 脱敏后收集信息
        // 结束时间
        long endTimePoint = System.currentTimeMillis();
        // 脱敏结束时间
        String endTime = util.getTime();
        // 脱敏耗时
        long executionTime = endTimePoint - startTimePoint;
        logExecutionTime(String.valueOf(executionTime), objectMode);

        // 标志脱敏完成
        desenCom = true;
        // 设置globalID
        String globalID = System.currentTimeMillis() + randomNum.nextInt() + "脱敏工具集";
        // 脱敏后文件字节流
        byte[] desenFileBytes = Files.readAllBytes(desenFilePath.toAbsolutePath());
        Long desenFileSize = Files.size(desenFilePath.toAbsolutePath());
        // 脱敏前类型
        infoBuilders.desenInfoPreIden.append("image");
        // 脱敏后类型
        infoBuilders.desenInfoAfterIden.append("image");
        // 脱敏意图
        infoBuilders.desenIntention.append("对图像脱敏");
        // 脱敏要求
        infoBuilders.desenRequirements.append("对图像脱敏");
        // 脱敏算法信息
        ObjectMapper objectMapper = new ObjectMapper();

        // 线程池
        ExecutorService executorService = Executors.newFixedThreadPool(4);
        // 本地存证
        // 存证系统
        String evidenceID = util.getSM3Hash((new String(desenFileBytes, StandardCharsets.UTF_8) + util.getTime()).getBytes());

        //存证请求  消息版本：中心0x1000，0x1010; 本地0x1100，0x1110
        ReqEvidenceSave reqEvidenceSave = buildReqEvidenceSave(rawFileSize, objectMode, evidenceID);

        // 上报本地存证内容
        SubmitEvidenceLocal submitEvidenceLocal = buildSubmitEvidenceLocal(evidenceID, infoBuilders.desenAlg,
                rawFileName, rawFileBytes, rawFileSize, desenFileBytes, globalID, infoBuilders.desenInfoPreIden.toString(), infoBuilders.desenIntention,
                infoBuilders.desenRequirements, infoBuilders.desenControlSet, infoBuilders.desenAlgParam, startTime, endTime,
                infoBuilders.desenLevel, desenCom);
        // 发送方法
        executorService.submit(() -> {
            sendData.send2Evidence(reqEvidenceSave, submitEvidenceLocal);
        });

        // 效果评测系统
        SendEvaReq sendEvaReq = buildSendEvaReq(globalID, evidenceID, rawFileName, rawFileBytes, rawFileSize,
                desenFileName, desenFileBytes, desenFileSize, infoBuilders.desenInfoPreIden, infoBuilders.desenInfoAfterIden,
                infoBuilders.desenIntention, infoBuilders.desenRequirements, infoBuilders.desenControlSet, infoBuilders.desenAlg,
                infoBuilders.desenAlgParam, startTime, endTime, infoBuilders.desenLevel, objectMode, rawFileSuffix, desenCom);

        ObjectNode effectEvaContent = (ObjectNode) objectMapper.readTree(objectMapper.writeValueAsString(sendEvaReq));
        executorService.submit(() -> {
            sendData.send2EffectEva(sendEvaReq, rawFileBytes,
                    desenFileBytes);
        });

        // 拆分重构系统

        // 合规检查系统
        SendRuleReq sendRuleReq = buildSendRuleReq(evidenceID, rawFileBytes, desenFileBytes, infoBuilders.desenInfoAfterIden,
                infoBuilders.desenIntention, infoBuilders.desenRequirements, infoBuilders.desenControlSet, infoBuilders.desenAlg,
                infoBuilders.desenAlgParam, startTime, endTime, infoBuilders.desenLevel, desenCom);

        executorService.submit(() -> {
            sendData.send2RuleCheck(sendRuleReq);
        });
        // 关闭线程池
        executorService.shutdown();
        // 读取文件返回
        HttpHeaders headers = new HttpHeaders();
        if ((rawFileSuffix.equals("png"))) {
            headers.setContentType(MediaType.IMAGE_PNG);
        } else {
            headers.setContentType(MediaType.IMAGE_JPEG);
        }
        return ResponseEntity.ok().headers(headers).body(desenFileBytes);

    }

    @Override
    public ResponseEntity<byte[]> dealExcel(MultipartFile file, String params, String sheetName) throws IOException {
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
        String rawFileName = fileTimeStamp + file.getOriginalFilename();
        String rawFileSuffix = rawFileName.substring(rawFileName.lastIndexOf(".") + 1);
        Path rawFilePath = rawFileDirectory.resolve(rawFileName);
        String rawFilePathString = rawFilePath.toAbsolutePath().toString();
        log.info(rawFilePath.toAbsolutePath().toString());
        byte[] rawFileBytes = file.getBytes();
        Long rawFileSize = file.getSize();

        // 读取excel文件
        InputStream inputStream = file.getInputStream();
        Workbook workbook = new XSSFWorkbook(inputStream);
        Sheet sheet = workbook.getSheetAt(0);

        // 保存源文件
        file.transferTo(rawFilePath.toAbsolutePath());
        // 设置脱敏后文件路径信息
        String desenFileName = "desen_" + rawFileName;
        Path desenFilePath = desenFileDirectory.resolve(desenFileName);
        log.info(desenFilePath.toAbsolutePath().toString());
        String desenFilePathString = desenFilePath.toAbsolutePath().toString();

        //脱敏参数处理,转为json
        List<ExcelParam> excelParamList = jsonStringToParams(params);
        // 保存参数到数据库中
//        excelParamService.deleteAll(sheetName + "_param");
//        excelParamService.insertAll(sheetName + "_param", excelParamList);

        // 保存脱敏后文件
        // 脱敏文件路径
        FileOutputStream fileOutputStream = new FileOutputStream(desenFilePathString);

        // 保存参数文件到本地
        Path paramDirectory = Paths.get("desen_params");
        if (!Files.exists(paramDirectory)) {
            Files.createDirectory(paramDirectory);
        }
        String paramsFileName = "params" + rawFileName.substring(0,rawFileName.lastIndexOf('.')) + ".txt";
        String paramsFilePath = paramDirectory.resolve(paramsFileName).toAbsolutePath().toString();

        // 数据类型
        List<Integer> dataType = new ArrayList<>();
        // 工具类
        DpUtil dpUtil = new DpUtilImpl();
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
            // 当前列操作脱敏参数
            ExcelParam excelParam = null;
            // 遍历模板中的列名，找到匹配的脱敏参数
            for (ExcelParam param : excelParamList) {
                //System.out.println(paramsDatum.getColumnName().trim());
                if (columnName.trim().equals(param.getColumnName().trim())) {
                    excelParam = param;
                }
            }
            if (excelParam == null) {
                throw new IOException("Param is null");
            }

            System.out.println(excelParam);
            dataType.add(excelParam.getDataType());

            int algoNum = excelParam.getK();
            AlgorithmInfo algorithmInfo = algorithmsFactory.getAlgorithmInfoFromId(algoNum);

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

            // 取列数据
            List<Object> objs = new ArrayList<>();
            // 从第一列开始取
            for (int rowIndex = 1; rowIndex <= totalRowNum; rowIndex++) {
                Row row = sheet.getRow(rowIndex);
                if (row != null) {
                    Cell cell = row.getCell(columnIndex);
                    if (excelParam.getDataType() == 4) {
                        objs.add(dataFormatter.formatCellValue(cell));
                    } else {
                        objs.add(cell);
                    }
                }
            }
            int columnDataType = excelParam.getDataType();

            switch (columnDataType) {
                case 0: {
                    System.out.println(objs.size());
                    List<Double> datas;
                    switch (algoNum) {
                        case 3: {
                            //差分隐私laplace噪声
                            // 脱敏算法参数
                            Map<Integer, String> map = new HashMap<>();
                            map.put(0, "没有脱敏,");
                            map.put(1, "10,");
                            map.put(2, "1,");
                            map.put(3, "0.1,");
                            // 添加脱敏参数
                            infoBuilders.desenAlgParam.append(map.get(excelParam.getTmParam()));
                            // 脱敏要求
                            infoBuilders.desenRequirements.append(excelParam.getColumnName()).append("添加差分隐私Laplace噪声,");
                            // 脱敏
                            DSObject rawData = new DSObject(objs);
                            datas = algorithmInfo.execute(rawData, excelParam.getTmParam()).getList()
                                    .stream()
                                    .map(item -> item != null ? (Double) item : null)
                                    .collect(Collectors.toList());
                            if (columnName.contains("年龄")) {
                                datas = datas.stream()
                                        .map(item -> item instanceof Double ? Math.floor(item) : null )
                                        .collect(Collectors.toList());
                            }
                            // 写列数据
                            util.write2Excel(sheet, totalRowNum, columnIndex, datas);
                            break;
                        }
                        case 5: {
                            Map<Integer, String> map = new HashMap<>();
                            map.put(0, "没有脱敏,");
                            map.put(1, "2.0,");
                            map.put(2, "10.0,");
                            map.put(3, "20.0,");
                            infoBuilders.desenAlgParam.append(map.get(excelParam.getTmParam()));
                            // 脱敏要求
                            infoBuilders.desenRequirements.append(excelParam.getColumnName()).append("添加随机均匀噪声,");
                            // 脱敏
                            DSObject rawData = new DSObject(objs);
                            datas = algorithmInfo.execute(rawData, excelParam.getTmParam()).getList()
                                    .stream()
                                    .map(item -> item != null ? (Double) item : null)
                                    .collect(Collectors.toList());
                            if (columnName.contains("年龄")) {
                                datas = datas.stream()
                                        .map(item -> item instanceof Double ? Math.floor(item) : null )
                                        .collect(Collectors.toList());
                            }
                            // 写列数据
                            util.write2Excel(sheet, totalRowNum, columnIndex, datas);
                            break;
                        }
                        case 6: {
                            Map<Integer, String> map = new HashMap<>();
                            map.put(0, "没有脱敏,");
                            map.put(1, "1.0,");
                            map.put(2, "5.0,");
                            map.put(3, "10.0,");
                            infoBuilders.desenAlgParam.append(map.get(excelParam.getTmParam()));
                            // 脱敏要求
                            infoBuilders.desenRequirements.append(excelParam.getColumnName()).append("添加随机laplace噪声,");
                            // 脱敏
                            DSObject rawData = new DSObject(objs);
                            datas = algorithmInfo.execute(rawData, excelParam.getTmParam()).getList()
                                    .stream()
                                    .map(item -> item != null ? (Double) item : null)
                                    .collect(Collectors.toList());
                            if (columnName.contains("年龄")) {
                                datas = datas.stream()
                                        .map(item -> item instanceof Double ? Math.floor(item) : null )
                                        .collect(Collectors.toList());
                            }
                            // 写列数据
                            util.write2Excel(sheet, totalRowNum, columnIndex, datas);
                            break;
                        }
                        case 7: {
                            Map<Integer, String> map = new HashMap<>();
                            map.put(0, "没有脱敏,");
                            map.put(1, "1.0,");
                            map.put(2, "5.0,");
                            map.put(3, "10.0,");
                            infoBuilders.desenAlgParam.append(map.get(excelParam.getTmParam()));
                            // 脱敏要求
                            infoBuilders.desenRequirements.append(excelParam.getColumnName()).append("添加随机高斯噪声,");
                            // 脱敏
                            DSObject rawData = new DSObject(objs);
                            datas = algorithmInfo.execute(rawData, excelParam.getTmParam()).getList()
                                    .stream()
                                    .map(item -> item != null ? (Double) item : null)
                                    .collect(Collectors.toList());
                            if (columnName.contains("年龄")) {
                                datas = datas.stream()
                                        .map(item -> item instanceof Double ? Math.floor(item) : null )
                                        .collect(Collectors.toList());
                            }
                            // 写列数据
                            util.write2Excel(sheet, totalRowNum, columnIndex, datas);
                            break;
                        }
                        case 8: {
                            Map<Integer, String> map = new HashMap<>();
                            map.put(0, "没有脱敏,");
                            map.put(1, "2.3,");
                            map.put(2, "11.3,");
                            map.put(3, "23.1,");
                            infoBuilders.desenAlgParam.append(map.get(excelParam.getTmParam()));
                            // 脱敏要求
                            infoBuilders.desenRequirements.append(excelParam.getColumnName()).append("数值偏移,");
                            // 脱敏
                            DSObject rawData = new DSObject(objs);
                            datas = algorithmInfo.execute(rawData, excelParam.getTmParam()).getList()
                                    .stream()
                                    .map(item -> item != null ? (Double) item : null)
                                    .collect(Collectors.toList());
                            // 写列数据
                            util.write2Excel(sheet, totalRowNum, columnIndex, datas);
                            break;

                        }
                        case 9: {
                            infoBuilders.desenAlgParam.append("无参,");
                            // 脱敏要求
                            infoBuilders.desenRequirements.append(excelParam.getColumnName()).append("数值取整,");
                            // 脱敏
                            DSObject rawData = new DSObject(objs);

                            List<Integer> list = algorithmInfo.execute(rawData, excelParam.getTmParam()).getList()
                                    .stream()
                                    .map(obj -> obj != null ? (Integer) obj : null)
                                    .collect(Collectors.toList());
                            // 写列数据
                            if (excelParam.getTmParam() == 0) {
                                util.write2Excel(sheet, totalRowNum, columnIndex, objs);
                            } else {
                                util.write2Excel(sheet, totalRowNum, columnIndex, list);
                            }
                            break;
                        }
                        case 10: {
                            infoBuilders.desenAlgParam.append("无参,");
                            // 脱敏要求
                            infoBuilders.desenRequirements.append(excelParam.getColumnName()).append("数值映射,");
                            // 脱敏
                            DSObject rawData = new DSObject(objs);
                            datas = algorithmInfo.execute(rawData, excelParam.getTmParam()).getList()
                                    .stream()
                                    .map(item -> item != null ? (Double) item : null)
                                    .collect(Collectors.toList());
                            // 写列数据
                            util.write2Excel(sheet, totalRowNum, columnIndex, datas);
                            break;
                        }
                        default: {
                            // 脱敏算法参数
                            Map<Integer, String> map = new HashMap<>();
                            map.put(0, "没有脱敏,");
                            map.put(1, "10,");
                            map.put(2, "30,");
                            map.put(3, "50,");
                            infoBuilders.desenAlgParam.append(map.get(excelParam.getTmParam()));
                            // 脱敏要求
                            infoBuilders.desenRequirements.append(excelParam.getColumnName()).append("进行分组置换,");
                            // 脱敏
                            datas = dpUtil.k_NumberCode(objs, excelParam.getTmParam());
                            // 写列数据
                            util.write2Excel(sheet, totalRowNum, columnIndex, datas);
                            break;
                        }
                    }
                    break;
                }

                case 1: {
                    // 脱敏算法参数
                    Map<Integer, String> map = new HashMap<>();
                    map.put(0, "没有脱敏,");
                    map.put(1, "3.6,");
                    map.put(2, "2,");
                    map.put(3, "0.7,");
                    infoBuilders.desenAlgParam.append(map.get(excelParam.getTmParam()));
                    // 脱敏要求
                    infoBuilders.desenRequirements.append(excelParam.getColumnName()).append("随机扰动,");
                    //
                    DSObject rawData = new DSObject(objs);
                    List<String> dpedCode = algorithmInfo.execute(rawData, excelParam.getTmParam()).getList()
                            .stream()
                            .map(obj -> obj != null ? (String) obj : null)
                            .collect(Collectors.toList());
                    // 写列数据
                    util.write2Excel(sheet, totalRowNum, columnIndex, dpedCode);
                    break;
                }

                case 3: {
                    List<String> datas;
                    int algNum = excelParam.getK();

                    if (excelParam.getTmParam() == 0) {
                        infoBuilders.desenAlgParam.append("没有脱敏,");
                    } else {
                        infoBuilders.desenAlgParam.append("无参,");
                    }

                    switch (algNum) {
                        case 11: {
                            // 脱敏要求
                            infoBuilders.desenRequirements.append(excelParam.getColumnName()).append("截断,");
                            // 脱敏
                            DSObject rawData = new DSObject(objs);
                            datas = algorithmInfo.execute(rawData, excelParam.getTmParam()).getList()
                                    .stream()
                                    .map(obj -> obj != null ? (String) obj : null)
                                    .collect(Collectors.toList());

                            // 写列数据
                            util.write2Excel(sheet, totalRowNum, columnIndex, datas);
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
                            datas = algorithmInfo.execute(rawData, excelParam.getTmParam()).getList()
                                    .stream()
                                    .map(obj -> obj != null ? (String) obj : null)
                                    .collect(Collectors.toList());
                            // 写列数据
                            util.write2Excel(sheet, totalRowNum, columnIndex, datas);
                            break;
                        }
                        case 17:
                        case 19:
                        case 20: {
                            // 脱敏要求
                            infoBuilders.desenRequirements.append(excelParam.getColumnName()).append("置换,");
                            // 脱敏
                            DSObject rawData = new DSObject(objs);
                            datas = algorithmInfo.execute(rawData, excelParam.getTmParam()).getList()
                                    .stream()
                                    .map(obj -> obj != null ? (String) obj : null)
                                    .collect(Collectors.toList());
                            // 写列数据
                            util.write2Excel(sheet, totalRowNum, columnIndex, datas);
                            break;
                        }

                    }

                    break;
                }

                case 4: {
                    List<Date> dates;
                    List<String> times;
                    // 加噪处理
                    // 基于差分隐私的日期加噪算法
                    int algNum = excelParam.getK();
                    switch (algNum) {
                        case 1: {
                            // 脱敏算法参数
                            Map<Integer, String> map = new HashMap<>();
                            map.put(0, "没有脱敏,");
                            map.put(1, "0.1,");
                            map.put(2, "0.01,");
                            map.put(3, "0.001,");
                            // 添加脱敏参数
                            infoBuilders.desenAlgParam.append(map.get(excelParam.getTmParam()));
                            // 脱敏要求: 列名+当前算法作用
                            infoBuilders.desenRequirements.append(excelParam.getColumnName()).append("添加Laplace噪声,");
                            // 脱敏
                            DSObject rawData = new DSObject(objs);
                            dates = algorithmInfo.execute(rawData, excelParam.getTmParam()).getList()
                                    .stream()
                                    .map(obj -> obj != null ? (Date) obj : null)
                                    .collect(Collectors.toList());
                            // 写列数据
                            util.write2Excel(sheet, totalRowNum, columnIndex, dates);
                            break;
                        }
                        case 18: {
                            // 脱敏算法参数
                            Map<Integer, String> map = new HashMap<>();
                            map.put(0, "没有脱敏,");
                            map.put(1, "10,");
                            map.put(2, "30,");
                            map.put(3, "50,");
                            infoBuilders.desenAlgParam.append(map.get(excelParam.getTmParam()));
                            // 脱敏要求
                            infoBuilders.desenRequirements.append(excelParam.getColumnName()).append("进行分组置换,");
                            // 脱敏
                            DSObject rawData = new DSObject(objs);
                            dates = algorithmInfo.execute(rawData, excelParam.getTmParam()).getList()
                                    .stream()
                                    .map(obj -> obj != null ? (Date) obj : null)
                                    .collect(Collectors.toList());                                // 写列数据
                            util.write2Excel(sheet, totalRowNum, columnIndex, dates);
                            break;
                        }
                        default: {
                            if (excelParam.getTmParam() == 0) {
                                infoBuilders.desenAlgParam.append("没有脱敏,");
                            } else {
                                infoBuilders.desenAlgParam.append("无参,");
                            }
                            // 脱敏要求:
                            infoBuilders.desenRequirements.append(excelParam.getColumnName()).append("取整处理,");
                            // 脱敏
                            DSObject rawData = new DSObject(objs);
                            times = algorithmInfo.execute(rawData, excelParam.getTmParam()).getList()
                                    .stream()
                                    .map(obj -> obj != null ? (String) obj : null)
                                    .collect(Collectors.toList());
                            // 写列数据
                            util.write2Excel(sheet, totalRowNum, columnIndex, times);
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

        log.info("Desensitization finished in" + (endTimePoint - startTimePoint) / 10e6 + "ms");
        long oneTime = (endTimePoint - startTimePoint) / columnCount / (totalRowNum - 1);
        // 打印单条运行时间
        log.info("Single data running time：" + oneTime + " ns");
        // 一秒数据量
        log.info("Number of dealt data per second:" + 10e9 / oneTime);
        log.info("Excel desen finished");

        // 保存处理后的Excel数据到ByteArrayOutputStream中
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        workbook.write(byteArrayOutputStream);
        byte[] newExcelData = byteArrayOutputStream.toByteArray();
        fileOutputStream.write(newExcelData);

        // 保存脱敏参数到文件
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(paramsFilePath))) {
            for (ExcelParam p : excelParamList) {
                // 将每个Person对象转换为字符串并写入文件
                String line = p.toString();
                writer.write(line);
                writer.newLine(); // 换行
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }

        // 标志脱敏完成
        desenCom = true;
        // 脱敏前信息
        // 将文件字节流转换为字符串
        String globalID = System.currentTimeMillis() + randomNum.nextInt() + "脱敏工具集";
        byte[] desenFileBytes = Files.readAllBytes(desenFilePath.toAbsolutePath());
        Long desenFileSize = Files.size(desenFilePath.toAbsolutePath());

        // 线程池
        ExecutorService executorService = Executors.newFixedThreadPool(4);

        // 存证系统
        String evidenceID = util.getSM3Hash((new String(newExcelData, StandardCharsets.UTF_8) + util.getTime()).getBytes());
//        String evidenceID = util.getSM3Hash((new String(desenFileBytes, StandardCharsets.UTF_8) + util.getTime()).getBytes());
        //存证请求  消息版本：中心0x1000，0x1010; 本地0x1100，0x1110
        ReqEvidenceSave reqEvidenceSave = buildReqEvidenceSave(rawFileSize, objectMode, evidenceID);

        // 上报本地存证内容
        SubmitEvidenceLocal submitEvidenceLocal = buildSubmitEvidenceLocal(evidenceID, infoBuilders.desenAlg, rawFileName,
                rawFileBytes, rawFileSize, desenFileBytes, globalID, infoBuilders.desenInfoPreIden.toString(),
                infoBuilders.desenIntention, infoBuilders.desenRequirements, infoBuilders.desenControlSet,
                infoBuilders.desenAlgParam, startTime, endTime, infoBuilders.desenLevel, desenCom);

        // 发送方法
        executorService.submit(() -> {
            sendData.send2Evidence(reqEvidenceSave, submitEvidenceLocal);
        });

        // 效果评测系统
        SendEvaReq sendEvaReq = buildSendEvaReq(globalID, evidenceID, rawFileName, rawFileBytes, rawFileSize,
                desenFileName, desenFileBytes, desenFileSize, infoBuilders.desenInfoPreIden, infoBuilders.desenInfoAfterIden,
                infoBuilders.desenIntention, infoBuilders.desenRequirements, infoBuilders.desenControlSet,
                infoBuilders.desenAlg, infoBuilders.desenAlgParam, startTime, endTime, infoBuilders.desenLevel,
                sheetName, rawFileSuffix, desenCom);

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode effectEvaContent = (ObjectNode) objectMapper.readTree(objectMapper.writeValueAsString(sendEvaReq));
        // 发送方法
        executorService.submit(() -> {
            sendData.send2EffectEva(sendEvaReq, rawFileBytes, desenFileBytes);
        });

        // 拆分重构系统

        // 合规检查系统
        SendRuleReq sendRuleReq = buildSendRuleReq(evidenceID, rawFileBytes, desenFileBytes,
                infoBuilders.desenInfoAfterIden, infoBuilders.desenIntention,
                infoBuilders.desenRequirements, infoBuilders.desenControlSet, infoBuilders.desenAlg,
                infoBuilders.desenAlgParam, startTime, endTime, infoBuilders.desenLevel, desenCom);
        // 发送
        executorService.submit(() -> {
            sendData.send2RuleCheck(sendRuleReq);
        });
        executorService.shutdown();
        // 关闭工作簿和流
        fileOutputStream.close();
        workbook.close();
        inputStream.close();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", desenFileName); // 设置文件名

        return new ResponseEntity<>(desenFileBytes, headers, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<byte[]> dealVideo(MultipartFile file, String params, String algName) throws IOException, SQLException, InterruptedException {
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
        infoBuilders.desenAlg.append(algorithmInfo.getId());
        infoBuilders.desenAlgParam.append(algorithmInfo.getParams().get(desenParam));
        infoBuilders.desenLevel.append(desenParam + 1);

        // 调用脱敏程序处理
        log.info("Start video desen");
        // 脱敏开始时间
        String startTime = util.getTime();
        long startTimePoint = System.currentTimeMillis();
        algorithmInfo.execute(dsObject, desenParam);

//        switch (algName) {
//            case "meanValueVideo" : {
//                desenAlg.append(50);
//                int[] paramsTemp = new int[] {9, 15, 21};
//                desenAlgParam.append(paramsTemp[desenParam]);
//                desenLevel.append(desenParam + 1);
//                DSObject result = generalization.service(dsObject, 17, desenParam);
//                break;
//            }
//            case "gaussian_blur_video" : {
//                desenAlg.append(51);
//                int[] paramsTemp = new int[] {3, 5, 8};
//                desenAlgParam.append(paramsTemp[desenParam]);
//                desenLevel.append(desenParam + 1);
//                DSObject result = generalization.service(dsObject, 15, desenParam);
//
//                break;
//            }
//            case "pixelate_video" : {
//                desenAlg.append(52);
//                int[] paramsTemp = new int[] {5, 10, 15};
//                desenAlgParam.append(paramsTemp[desenParam]);
//                desenLevel.append(desenParam + 1);
//                DSObject result = generalization.service(dsObject, 14, desenParam);
//
//                break;
//            }
//            case "box_blur_video" : {
//                desenAlg.append(53);
//                int[] paramsTemp = new int[] {2, 4, 8};
//                desenAlgParam.append(paramsTemp[desenParam]);
//                desenLevel.append(desenParam + 1);
//                DSObject result = generalization.service(dsObject, 16, desenParam);
//
//                break;
//            }
//            case "replace_region_video" : {
//                desenAlg.append(54);
//                int[][] paramsTemp = new int[][] {{100, 100, 200, 200}, {50, 50, 300, 300}, {25, 25, 400, 400}};
//                desenAlgParam.append(Arrays.toString(paramsTemp[desenParam]));
//                desenLevel.append(desenParam + 1);
//                DSObject result = generalization.service(dsObject, 18, desenParam);
//                break;
//            }
//
//            case "video_add_color_offset" :{
//                desenAlg.append(55);
//                int[] paramsTemp = new int[] {20, 50, 100};
//                desenAlgParam.append(paramsTemp[desenParam]);
//                desenLevel.append(desenParam + 1);
//                DSObject result = replacement.service(dsObject, 14, desenParam);
//                break;
//            }
//
//        }

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
        // 脱敏前类型
        infoBuilders.desenInfoPreIden.append("video");
        // 脱敏后类型
        infoBuilders.desenInfoAfterIden.append("video");
        // 脱敏意图
        infoBuilders.desenIntention.append("对视频脱敏");
        // 脱敏要求
        infoBuilders.desenRequirements.append("对视频脱敏");


        // 线程池
        ExecutorService executorService = Executors.newFixedThreadPool(4);

        // 存证系统
        String evidenceID = util.getSM3Hash((new String(desenFileBytes, StandardCharsets.UTF_8) + util.getTime()).getBytes());
        //存证请求  消息版本：中心0x1000，0x1010; 本地0x1100，0x1110

        ReqEvidenceSave reqEvidenceSave = buildReqEvidenceSave(rawFileSize, objectMode, evidenceID);

        // 上报本地存证内容
        SubmitEvidenceLocal submitEvidenceLocal = buildSubmitEvidenceLocal(evidenceID, infoBuilders.desenAlg, rawFileName, rawFileBytes, rawFileSize,
                desenFileBytes, globalID, infoBuilders.desenInfoPreIden.toString(), infoBuilders.desenIntention,
                infoBuilders.desenRequirements, infoBuilders.desenControlSet,
                infoBuilders.desenAlgParam, startTime, endTime, infoBuilders.desenLevel, desenCom);

        // 发送方法
        executorService.submit(() -> {
            sendData.send2Evidence(reqEvidenceSave, submitEvidenceLocal);
        });

        // 效果评测系统
        SendEvaReq sendEvaReq = buildSendEvaReq(globalID, evidenceID, rawFileName, rawFileBytes, rawFileSize,
                desenFileName, desenFileBytes, desenFileSize,
                infoBuilders.desenInfoPreIden, infoBuilders.desenInfoAfterIden, infoBuilders.desenIntention,
                infoBuilders.desenRequirements, infoBuilders.desenControlSet, infoBuilders.desenAlg, infoBuilders.desenAlgParam,
                startTime, endTime, infoBuilders.desenLevel, objectMode, rawFileSuffix, desenCom);

        executorService.submit(() -> {
            sendData.send2EffectEva(sendEvaReq, rawFileBytes, desenFileBytes);
        });

        // 拆分重构系统

        // 合规检查系统
        SendRuleReq sendRuleReq = buildSendRuleReq(evidenceID, rawFileBytes, desenFileBytes,
                infoBuilders.desenInfoAfterIden, infoBuilders.desenIntention,
                infoBuilders.desenRequirements, infoBuilders.desenControlSet,
                infoBuilders.desenAlg, infoBuilders.desenAlgParam,
                startTime, endTime, infoBuilders.desenLevel, desenCom);

        executorService.submit(() -> {
            sendData.send2RuleCheck(sendRuleReq);
        });

        // 关闭线程池
        executorService.shutdown();

        // 读取文件返回
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "video/mp4");
        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.parseMediaType("video/mp4")).body(desenFileBytes);
    }

    @Override
    public ResponseEntity<byte[]> dealAudio(MultipartFile file, String params, String algName, String sheet) throws IOException, SQLException, InterruptedException {
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
        String desenParam = paramsList[paramsList.length - 1];

        // 获取脱敏算法
        AlgorithmInfo algorithmInfo = algorithmsFactory.getAlgorithmInfoFromName(algName);
        // 添加脱敏算法信息
        infoBuilders.desenAlg.append(algorithmInfo.getId());
        List<String> paths = Arrays.asList(rawFilePathString, desenFilePathString);
        DSObject desenObj = new DSObject(paths);

        // 调用脱敏程序处理
        log.info("Start Audio desen");
        // 开始时间
        String startTime = util.getTime();
        // 脱敏开始时间
        long startTimePoint = System.currentTimeMillis();
        if (algorithmInfo.getParams() == null) {
            algorithmInfo.execute(desenObj);
            // 脱敏参数
            infoBuilders.desenAlgParam.append("无参");
            // 脱敏级别
            infoBuilders.desenLevel.append(4);
        } else {
            int paramIndex = Integer.parseInt(desenParam);
            List<Object> param = algorithmInfo.getParams();
            algorithmInfo.execute(desenObj, paramIndex);
            // 脱敏参数
            infoBuilders.desenAlgParam.append(param.get(paramIndex));
            // 脱敏级别
            infoBuilders.desenLevel.append(paramIndex + 1);

        }
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
        // 脱敏前类型
        infoBuilders.desenInfoPreIden.append("audio");
        // 脱敏意图
        infoBuilders.desenIntention.append("对音频脱敏");
        // 脱敏要求
        infoBuilders.desenRequirements.append("对声纹脱敏");
        ObjectMapper objectMapper = new ObjectMapper();

        // 线程池
        ExecutorService executorService = Executors.newFixedThreadPool(4);
        // 存证系统
        //存证请求  消息版本：中心0x1000，0x1010; 本地0x1100，0x1110
        String evidenceID = util.getSM3Hash((new String(desenFileBytes, StandardCharsets.UTF_8) + util.getTime()).getBytes());

        ReqEvidenceSave reqEvidenceSave = buildReqEvidenceSave(rawFileSize, objectMode, evidenceID);

        // 上报本地存证内容
        SubmitEvidenceLocal submitEvidenceLocal = buildSubmitEvidenceLocal(evidenceID, infoBuilders.desenAlg, rawFileName,
                rawFileBytes, rawFileSize, desenFileBytes, globalID, infoBuilders.desenInfoPreIden.toString(),
                infoBuilders.desenIntention, infoBuilders.desenRequirements, infoBuilders.desenControlSet,
                infoBuilders.desenAlgParam, startTime, endTime, infoBuilders.desenLevel, desenCom);

        // 发送方法
       /* Thread evidenceThread = new Thread(() -> sendData.send2Evidence(reqEvidenceSave, submitEvidenceLocal));
        evidenceThread.start();*/
        Future<?> future_evidence = executorService.submit(() -> {
            sendData.send2Evidence(reqEvidenceSave, submitEvidenceLocal);
        });

        // 效果评测系统
        SendEvaReq sendEvaReq = buildSendEvaReq(globalID, evidenceID, rawFileName, rawFileBytes, rawFileSize, desenFileName, desenFileBytes,
                desenFileSize, infoBuilders.desenInfoPreIden, infoBuilders.desenInfoAfterIden, infoBuilders.desenIntention,
                infoBuilders.desenRequirements, infoBuilders.desenControlSet, infoBuilders.desenAlg,
                infoBuilders.desenAlgParam, startTime, endTime, infoBuilders.desenLevel, objectMode, rawFileSuffix, desenCom);

        ObjectNode effectEvaContent = (ObjectNode) objectMapper.readTree(objectMapper.writeValueAsString(sendEvaReq));

        executorService.submit(() -> {
            sendData.send2EffectEva(sendEvaReq, rawFileBytes, desenFileBytes);
        });

        // TODO: 拆分重构系统

        // 合规检查系统
        SendRuleReq sendRuleReq = buildSendRuleReq(evidenceID, rawFileBytes, desenFileBytes, infoBuilders.desenInfoAfterIden,
                infoBuilders.desenIntention, infoBuilders.desenRequirements, infoBuilders.desenControlSet,
                infoBuilders.desenAlg, infoBuilders.desenAlgParam, startTime, endTime, infoBuilders.desenLevel, desenCom);

        executorService.submit(() -> {
            sendData.send2RuleCheck(sendRuleReq);
        });

        // 关闭线程池
        executorService.shutdown();

        // 设置HTTP响应头部信息
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", desenFileName);

        // 返回处理后的音频文件数据给前端
        return new ResponseEntity<>(desenFileBytes, headers, HttpStatus.OK);
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
        logExecutionTime(String.valueOf(executionTime), "Graph");
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
        infoBuilders.desenLevel.append(Integer.parseInt(params));
        // 脱敏前类型
        infoBuilders.desenInfoPreIden.append(objectMode);
        // 脱敏后类型
        infoBuilders.desenInfoAfterIden.append(objectMode);
        // 脱敏意图
        infoBuilders.desenIntention.append("对图形脱敏");
        // 脱敏要求
        infoBuilders.desenRequirements.append("对图形脱敏");

        // 脱敏文件流
        // 线程池
        ExecutorService executorService = Executors.newFixedThreadPool(4);

        // 存证系统
        //存证请求  消息版本：中心0x1000，0x1010; 本地0x1100，0x1110
        String evidenceID = util.getSM3Hash((new String(desenFileBytes, StandardCharsets.UTF_8) + util.getTime()).getBytes());

        ReqEvidenceSave reqEvidenceSave = buildReqEvidenceSave(rawFileSize, objectMode, evidenceID);

        // 上报本地存证内容
        SubmitEvidenceLocal submitEvidenceLocal = buildSubmitEvidenceLocal(evidenceID, infoBuilders.desenAlg, rawFileName,
                rawFileBytes, rawFileSize, desenFileBytes, globalID, infoBuilders.desenInfoPreIden.toString(),
                infoBuilders.desenIntention, infoBuilders.desenRequirements, infoBuilders.desenControlSet,
                infoBuilders.desenAlgParam, startTime, endTime, infoBuilders.desenLevel, desenCom);

        executorService.submit(() -> {
            sendData.send2Evidence(reqEvidenceSave, submitEvidenceLocal);
        });

        // 效果评测系统
        SendEvaReq sendEvaReq = buildSendEvaReq(globalID, evidenceID, rawFileName, rawFileBytes, rawFileSize, desenFileName, desenFileBytes,
                desenFileSize, infoBuilders.desenInfoPreIden, infoBuilders.desenInfoAfterIden, infoBuilders.desenIntention,
                infoBuilders.desenRequirements, infoBuilders.desenControlSet, infoBuilders.desenAlg,
                infoBuilders.desenAlgParam, startTime, endTime, infoBuilders.desenLevel, objectMode, rawFileSuffix, desenCom);

        executorService.submit(() -> {
            sendData.send2EffectEva(sendEvaReq, rawFileBytes, desenFileBytes);
        });

        // TODO: 拆分重构系统

        // 合规检查系统
        SendRuleReq sendRuleReq = buildSendRuleReq(evidenceID, rawFileBytes, desenFileBytes, infoBuilders.desenInfoAfterIden,
                infoBuilders.desenIntention, infoBuilders.desenRequirements, infoBuilders.desenControlSet,
                infoBuilders.desenAlg, infoBuilders.desenAlgParam, startTime, endTime, infoBuilders.desenLevel, desenCom);

        executorService.submit(() -> {
            sendData.send2RuleCheck(sendRuleReq);
        });

        // 关闭线程池
        executorService.shutdown();

        // 返回数据给前端

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", desenFileName); // 设置文件名

        return new ResponseEntity<>(desenFileBytes, headers, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<byte[]> dealCsv(MultipartFile file, String params, String algName) throws InterruptedException, IOException {
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
        String desenFilePathString = desenFilePath.toAbsolutePath().toString();// 读取文件
        InputStream inputStream = file.getInputStream();

        //脱敏参数处理,转为json
        ObjectMapper objectMapper = new ObjectMapper();


        // 脱敏参数
        String privacyLevel = String.valueOf(params.charAt(params.length() - 1));
        int[] k_anonymity_param = new int[]{5, 10, 20};
        int[] l_diversity_param = new int[]{2, 4, 6};
        double[] t_closeness_param = new double[]{0.6, 0.4, 0.2};
        String desenParam;
        DSObject dsObject = new DSObject(Arrays.asList(rawFilePathString, desenFilePathString));
        // 脱敏开始时间
        String startTime = util.getTime();
        // 开始时间
        // TODO: 整合该部分进AlgorithmsFactory
        long desenStartTime = System.currentTimeMillis();
        switch (algName) {
            case "k_anonymity":
                infoBuilders.desenAlg.append(25);
                anonymity.service(dsObject, 1, Integer.parseInt(privacyLevel));
                desenParam = String.valueOf(k_anonymity_param[Integer.parseInt(privacyLevel)]);
                break;
            case "l_diversity":
                infoBuilders.desenAlg.append(26);
                anonymity.service(dsObject, 7, Integer.parseInt(privacyLevel));
                desenParam = String.valueOf(l_diversity_param[Integer.parseInt(privacyLevel)]);
                break;
            case "t_closeness":
                infoBuilders.desenAlg.append(27);
                anonymity.service(dsObject, 10, Integer.parseInt(privacyLevel));
                desenParam = String.valueOf(t_closeness_param[Integer.parseInt(privacyLevel)]);
                break;

            default:
                desenParam = String.valueOf(k_anonymity_param[Integer.parseInt(privacyLevel)]);
        }
        infoBuilders.desenAlgParam.append(desenParam);
        // 脱敏级别
        infoBuilders.desenLevel.append(privacyLevel);
        // 脱敏结束时间
        String endTime = util.getTime();

        // 结束时间
        long desenEndTime = System.currentTimeMillis();
        long executionTime = desenEndTime - desenStartTime;
        logExecutionTime(String.valueOf(executionTime), "CSV");


        // 标志脱敏完成
        desenCom = true;
        String globalID = System.currentTimeMillis() + randomNum.nextInt() + "脱敏工具集";
        byte[] desenFileBytes = Files.readAllBytes(desenFilePath.toAbsolutePath());
        Long desenFileSize = Files.size(desenFilePath.toAbsolutePath());
        // 脱敏前类型
        infoBuilders.desenInfoPreIden.append("csv文件");
        // 脱敏后类型
        infoBuilders.desenInfoAfterIden.append("csv文件");
        // 脱敏意图
        infoBuilders.desenIntention.append("对csv文件脱敏");
        // 脱敏要求
        infoBuilders.desenRequirements.append("对csv文件脱敏");

        // 脱敏后信息
        // 脱敏后文件大小

        // 线程池
        ExecutorService executorService = Executors.newFixedThreadPool(4);

        // 存证系统
        //存证请求  消息版本：中心0x1000，0x1010; 本地0x1100，0x1110
        String evidenceID = util.getSM3Hash((new String(desenFileBytes, StandardCharsets.UTF_8) + util.getTime()).getBytes());

        ReqEvidenceSave reqEvidenceSave = buildReqEvidenceSave(rawFileSize, objectMode, evidenceID);

        // 上报本地存证内容
        SubmitEvidenceLocal submitEvidenceLocal = buildSubmitEvidenceLocal(evidenceID, infoBuilders.desenAlg, rawFileName,
                rawFileBytes, rawFileSize, desenFileBytes, globalID, infoBuilders.desenInfoPreIden.toString(),
                infoBuilders.desenIntention, infoBuilders.desenRequirements, infoBuilders.desenControlSet,
                infoBuilders.desenAlgParam, startTime, endTime, infoBuilders.desenLevel, desenCom);

        // 发送方法
        executorService.submit(() -> {
            sendData.send2Evidence(reqEvidenceSave, submitEvidenceLocal);
        });

        // 效果评测系统
        SendEvaReq sendEvaReq = buildSendEvaReq(globalID, evidenceID, rawFileName, rawFileBytes, rawFileSize, desenFileName, desenFileBytes,
                desenFileSize, infoBuilders.desenInfoPreIden, infoBuilders.desenInfoAfterIden, infoBuilders.desenIntention,
                infoBuilders.desenRequirements, infoBuilders.desenControlSet, infoBuilders.desenAlg,
                infoBuilders.desenAlgParam, startTime, endTime, infoBuilders.desenLevel, objectMode, rawFileSuffix, desenCom);
        executorService.submit(() -> {
            sendData.send2EffectEva(sendEvaReq, rawFileBytes, desenFileBytes);
        });

        // TODO: 拆分重构系统

        // 合规检查系统
        SendRuleReq sendRuleReq = buildSendRuleReq(evidenceID, rawFileBytes, desenFileBytes, infoBuilders.desenInfoAfterIden,
                infoBuilders.desenIntention, infoBuilders.desenRequirements, infoBuilders.desenControlSet,
                infoBuilders.desenAlg, infoBuilders.desenAlgParam, startTime, endTime, infoBuilders.desenLevel, desenCom);
        executorService.submit(() -> {
            sendData.send2RuleCheck(sendRuleReq);
        });

        // 关闭线程池
        executorService.shutdown();
        // 关闭工作簿和流
        inputStream.close();
        // 返回excel给前端

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", desenFileName); // 设置文件名

        return new ResponseEntity<>(desenFileBytes, headers, HttpStatus.OK);
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

        //脱敏参数处理,转为json
        ObjectMapper objectMapper = new ObjectMapper();

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
      /*  File paramDirectory = new File("desen_params");
        if (!paramDirectory.exists()) {
            paramDirectory.mkdir();
        }
        String paramsFilePath = currentPath + File.separator + "desen_params" + File.separator + "params" + rawFileName.substring(0, rawFileName.lastIndexOf('.')) + ".txt";*/
        int param = Integer.parseInt(String.valueOf(params.charAt(params.length() - 1)));
        // 数据类型
        List<Integer> dataType = new ArrayList<>();
        // 工具类
        DpUtil dpUtil = new DpUtilImpl();
        // 数据行数
        int totalRowNum = sheet.getLastRowNum();
        // 字段名行
        Row fieldRow = sheet.getRow(0);
        // 列数
        int columnCount = fieldRow.getPhysicalNumberOfCells(); // 获取列数
        log.info("Total column number is " + columnCount);
        // 调用脱敏程序处理
        log.info("Start Excel file desen");
        // 脱敏开始时间
        String startTime = util.getTime();
        long startTimePoint = System.nanoTime();

        //  逐列处理
        DataFormatter dataFormatter = new DataFormatter();
        for (int colIndex = 0; colIndex < columnCount; colIndex++) {

            // 取列名
            System.out.println(colIndex);
            String colName = fieldRow.getCell(colIndex).toString();
            System.out.println(colName);

            // 脱敏前信息类型标识
            infoBuilders.desenInfoPreIden.append(colName);
            infoBuilders.desenInfoAfterIden.append(colName);
            //System.out.println(param.getColumnName());
            // 读取脱敏级别
            infoBuilders.desenLevel.append(param);

            // 脱敏意图
            infoBuilders.desenIntention.append(colName).append("脱敏,");

            // 取列数据
            List<Object> objs = new ArrayList<>();
            for (int j = 1; j <= totalRowNum; j++) {
                Row row = sheet.getRow(j);
                if (row != null) {
                    Cell cell = row.getCell(colIndex);
                    objs.add(cell);
                }
            }
            AlgorithmInfo algorithmInfo = algorithmsFactory.getAlgorithmInfoFromName(algName);
            DSObject dsObject = new DSObject(objs);

            switch (algName.trim()) {
                case "dpDate": {
                    // 脱敏算法参数
                    Map<Integer, String> map = new HashMap<>();
                    map.put(1, "0.1");
                    map.put(2, "0.01");
                    map.put(3, "0.001");
                    infoBuilders.desenAlgParam.append(map.get(param));
                    // 脱敏要求
                    infoBuilders.desenRequirements.append(colName).append("添加Laplace噪声,");
                    infoBuilders.desenAlg.append(algorithmInfo.getId());

                    List<Date> dates = algorithmInfo.execute(dsObject, param).getList()
                            .stream()
                            .filter(obj -> obj instanceof Date)
                            .map(obj -> (Date) obj)
                            .collect(Collectors.toList());

                    util.write2Excel(sheet, totalRowNum, colIndex, dates);
                    break;
                }
                case "dpCode": {
                    // 脱敏算法参数
                    Map<Integer, String> map = new HashMap<>();
                    map.put(1, "3.6");
                    map.put(2, "2");
                    map.put(3, "0.7");
                    infoBuilders.desenAlgParam.append(map.get(param));
                    // 脱敏要求
                    infoBuilders.desenRequirements.append(colName).append("随机扰动,");
                    infoBuilders.desenAlg.append(algorithmInfo.getId());

                    List<String> datas = algorithmInfo.execute(dsObject, param).getList()
                            .stream()
                            .filter(obj -> obj instanceof String)
                            .map(obj -> (String) obj)
                            .collect(Collectors.toList());


                    util.write2Excel(sheet, totalRowNum, colIndex, datas);
                    break;
                }
                case "laplaceToValue": {
                    // 脱敏算法参数
                    Map<Integer, String> map = new HashMap<>();
                    map.put(0, "没有脱敏");
                    map.put(1, "10");
                    map.put(2, "1");
                    map.put(3, "0.1");
                    infoBuilders.desenAlgParam.append(map.get(param));
                    // 脱敏要求
                    infoBuilders.desenRequirements.append(colName).append("添加差分隐私Laplace噪声,");
                    infoBuilders.desenAlg.append(algorithmInfo.getId());
                    // 脱敏
                    List<Double> datas = algorithmInfo.execute(dsObject, 1, param).getList()
                            .stream()
                            .filter(obj -> obj instanceof Double)
                            .map(obj -> (Double) obj)
                            .collect(Collectors.toList());
                    if (colName.contains("年龄")) {
                        datas = datas.stream().map(Math::floor).collect(Collectors.toList());
                    }

                    // 写列数据
                    util.write2Excel(sheet, totalRowNum, colIndex, datas);
                    break;
                }
                case "gaussianToValue": {
                    Map<Integer, String> map = new HashMap<>();
                    map.put(0, "没有脱敏");
                    map.put(1, "10");
                    map.put(2, "1");
                    map.put(3, "0.1");

                    infoBuilders.desenAlgParam.append(map.get(param));
                    // 脱敏要求
                    infoBuilders.desenRequirements.append(colName).append("添加差分隐私高斯噪声,");
                    infoBuilders.desenAlg.append(algorithmInfo.getId());

                    List<Double> datas = algorithmInfo.execute(dsObject, param).getList()
                            .stream()
                            .filter(obj -> obj instanceof Double)
                            .map(obj -> (Double) obj)
                            .collect(Collectors.toList());
                    if (colName.contains("年龄")) {
                        datas = datas.stream().map(Math::floor).collect(Collectors.toList());
                    }
                    util.write2Excel(sheet, totalRowNum, colIndex, datas);
                    break;
                }
                case "randomUniformToValue": {

                    Map<Integer, String> map = new HashMap<>();
                    map.put(1, "2.0");
                    map.put(2, "10.0");
                    map.put(3, "20.0");
                    infoBuilders.desenAlgParam.append(map.get(param));
                    // 脱敏要求
                    infoBuilders.desenRequirements.append(colName).append("添加随机均匀噪声,");
                    infoBuilders.desenAlg.append(algorithmInfo.getId());

                    List<Double> datas = algorithmInfo.execute(dsObject, param).getList()
                            .stream()
                            .filter(obj -> obj instanceof Double)
                            .map(obj -> (Double) obj)
                            .collect(Collectors.toList());
                    if (colName.contains("年龄")) {
                        datas = datas.stream().map(Math::floor).collect(Collectors.toList());
                    }

                    util.write2Excel(sheet, totalRowNum, colIndex, datas);
                    break;
                }
                case "randomLaplaceToValue": {

                    Map<Integer, String> map = new HashMap<>();
                    map.put(1, "1.0");
                    map.put(2, "5.0");
                    map.put(3, "10.0");
                    infoBuilders.desenAlgParam.append(map.get(param));
                    // 脱敏要求
                    infoBuilders.desenRequirements.append(colName).append("添加随机laplace噪声,");
                    infoBuilders.desenAlg.append(algorithmInfo.getId());
                    List<Double> datas = algorithmInfo.execute(dsObject, param).getList()
                            .stream()
                            .filter(obj -> obj instanceof Double)
                            .map(obj -> (Double) obj)
                            .collect(Collectors.toList());
                    if (colName.contains("年龄")) {
                        datas = datas.stream().map(Math::floor).collect(Collectors.toList());
                    }
                    util.write2Excel(sheet, totalRowNum, colIndex, datas);
                    break;
                }
                case "randomGaussianToValue": {
                    Map<Integer, String> map = new HashMap<>();
                    map.put(1, "1.0");
                    map.put(2, "5.0");
                    map.put(3, "10.0");
                    infoBuilders.desenAlgParam.append(map.get(param));
                    // 脱敏要求
                    infoBuilders.desenRequirements.append(colName).append("添加随机高斯噪声,");
                    infoBuilders.desenAlg.append(algorithmInfo.getId());

                    List<Double> datas = algorithmInfo.execute(dsObject, param).getList()
                            .stream()
                            .filter(obj -> obj instanceof Double)
                            .map(obj -> (Double) obj)
                            .collect(Collectors.toList());
                    if (colName.contains("年龄")) {
                        datas = datas.stream().map(Math::floor).collect(Collectors.toList());
                    }

                    util.write2Excel(sheet, totalRowNum, colIndex, datas);
                    break;
                }
                case "valueShift": {
                    Map<Integer, String> map = new HashMap<>();
                    map.put(1, "2.3");
                    map.put(2, "11.3");
                    map.put(3, "23.1");
                    infoBuilders.desenAlgParam.append(map.get(param));
                    // 脱敏要求
                    infoBuilders.desenRequirements.append(colName).append("数值偏移,");
                    infoBuilders.desenAlg.append(algorithmInfo.getId());

                    List<Double> datas = algorithmInfo.execute(dsObject, param).getList()
                            .stream()
                            .filter(obj -> obj instanceof Double)
                            .map(obj -> (Double) obj)
                            .collect(Collectors.toList());

                    util.write2Excel(sheet, totalRowNum, colIndex, datas);
                    break;
                }
                case "floor": {
                    infoBuilders.desenAlgParam.append("无参,");
                    // 脱敏要求
                    infoBuilders.desenRequirements.append(colName).append("数值取整,");
                    infoBuilders.desenAlg.append(algorithmInfo.getId());
                    if (param == 0) {
                        util.write2Excel(sheet, totalRowNum, colIndex, objs);
                    } else {
                        List<Integer> datas = algorithmInfo.execute(dsObject, param).getList()
                                .stream()
                                .filter(obj -> obj instanceof Integer)
                                .map(obj -> (Integer) obj)
                                .collect(Collectors.toList());
                        util.write2Excel(sheet, totalRowNum, colIndex, datas);
                    }
                    break;
                }
                case "valueMapping": {
                    infoBuilders.desenAlgParam.append("无参,");
                    // 脱敏要求
                    infoBuilders.desenRequirements.append(colName).append("数值映射,");
                    infoBuilders.desenAlg.append(algorithmInfo.getId());
                    List<Double> datas = algorithmInfo.execute(dsObject, param).getList()
                            .stream()
                            .filter(obj -> obj instanceof Double)
                            .map(obj -> (Double) obj)
                            .collect(Collectors.toList());

                    util.write2Excel(sheet, totalRowNum, colIndex, datas);
                    break;
                }
                case "truncation": {
                    infoBuilders.desenAlgParam.append("无参,");
                    // 脱敏要求
                    infoBuilders.desenRequirements.append(colName).append("截断,");
                    infoBuilders.desenAlg.append(algorithmInfo.getId());
                    List<Double> datas = algorithmInfo.execute(dsObject, param).getList()
                            .stream()
                            .filter(obj -> obj instanceof Double)
                            .map(obj -> (Double) obj)
                            .collect(Collectors.toList());

                    util.write2Excel(sheet, totalRowNum, colIndex, datas);
                    break;
                }

                case "floorTime":
                case "suppressEmail":
                case "addressHide":
                case "nameHide":
                case "numberHide":
                case "suppressIpRandomParts":
                case "suppressAllIp": {

                    infoBuilders.desenAlgParam.append("无参,");
                    // 脱敏要求
                    infoBuilders.desenRequirements.append(colName).append("抑制,");
                    infoBuilders.desenAlg.append(algorithmInfo.getId());
                    List<String> datas = algorithmInfo.execute(dsObject, param).getList()
                            .stream()
                            .filter(obj -> obj instanceof String)
                            .map(obj -> (String) obj)
                            .collect(Collectors.toList());

                    util.write2Excel(sheet, totalRowNum, colIndex, datas);
                    break;
                }

                case "SHA512":
                case "passReplace":
                case "value_hide": {

                    infoBuilders.desenAlgParam.append("无参,");
                    // 脱敏要求
                    infoBuilders.desenRequirements.append(colName).append("置换,");
                    infoBuilders.desenAlg.append(algorithmInfo.getId());
                    List<String> datas = algorithmInfo.execute(dsObject, param).getList()
                            .stream()
                            .filter(obj -> obj instanceof String)
                            .map(obj -> (String) obj)
                            .collect(Collectors.toList());

                    util.write2Excel(sheet, totalRowNum, colIndex, datas);
                    break;
                }
            }
        }

        // 结束时间
        long endTimePoint = System.nanoTime();
        // 脱敏结束时间
        String endTime = util.getTime();

        log.info("Desensitization finished in" + (endTimePoint - startTimePoint) / 10e6 + "ms");
        long oneTime = (endTimePoint - startTimePoint) / columnCount / (totalRowNum - 1);
        // 打印单条运行时间
        log.info("Single data running time：" + oneTime + " ns");
        // 一秒数据量
        log.info("Number of dealt data per second:" + 10e9 / oneTime);
        log.info("Excel desen finished");

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
        String infoPre = util.inputStreamToString(inputStream);
        Long desenFileSize = Files.size(desenFilePath.toAbsolutePath());
        // 线程池
        ExecutorService executorService = Executors.newFixedThreadPool(4);

        // 存证系统
        String evidenceID = util.getSM3Hash((new String(newExcelData, StandardCharsets.UTF_8) + util.getTime()).getBytes());
        //存证请求  消息版本：中心0x1000，0x1010; 本地0x1100，0x1110
        ReqEvidenceSave reqEvidenceSave = buildReqEvidenceSave(rawFileSize, objectMode, evidenceID);

        // 上报本地存证内容
        SubmitEvidenceLocal submitEvidenceLocal = buildSubmitEvidenceLocal(evidenceID, infoBuilders.desenAlg, rawFileName,
                rawFileBytes, rawFileSize, desenFileBytes, globalID, infoBuilders.desenInfoPreIden.toString(),
                infoBuilders.desenIntention, infoBuilders.desenRequirements, infoBuilders.desenControlSet,
                infoBuilders.desenAlgParam, startTime, endTime, infoBuilders.desenLevel, desenCom);

        // 发送方法
       /* Thread evidenceThread = new Thread(() -> sendData.send2Evidence(reqEvidenceSave, submitEvidenceLocal));
        evidenceThread.start();*/
        Future<?> future_evidence = executorService.submit(() -> {
            sendData.send2Evidence(reqEvidenceSave, submitEvidenceLocal);
        });

        // 效果评测系统
        SendEvaReq sendEvaReq = buildSendEvaReq(globalID, evidenceID, rawFileName, rawFileBytes, rawFileSize, desenFileName, desenFileBytes,
                desenFileSize, infoBuilders.desenInfoPreIden, infoBuilders.desenInfoAfterIden, infoBuilders.desenIntention,
                infoBuilders.desenRequirements, infoBuilders.desenControlSet, infoBuilders.desenAlg,
                infoBuilders.desenAlgParam, startTime, endTime, infoBuilders.desenLevel, objectMode, rawFileSuffix, desenCom);

        ObjectNode effectEvaContent = (ObjectNode) objectMapper.readTree(objectMapper.writeValueAsString(sendEvaReq));

        Future<?> future_effect = executorService.submit(() -> {
            sendData.send2EffectEva(sendEvaReq, rawFileBytes, desenFileBytes);
        });

        // TODO: 拆分重构系统

        // 合规检查系统
        SendRuleReq sendRuleReq = buildSendRuleReq(evidenceID, rawFileBytes, desenFileBytes, infoBuilders.desenInfoAfterIden,
                infoBuilders.desenIntention, infoBuilders.desenRequirements, infoBuilders.desenControlSet,
                infoBuilders.desenAlg, infoBuilders.desenAlgParam, startTime, endTime, infoBuilders.desenLevel, desenCom);
        ObjectNode ruleCheckContent = (ObjectNode) objectMapper.readTree(objectMapper.writeValueAsString(sendRuleReq));

        executorService.submit(() -> {
            sendData.send2RuleCheck(sendRuleReq);
        });

        executorService.shutdown();

        // 关闭工作簿和流
        fileOutputStream.close();
        workbook.close();
        inputStream.close();


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", desenFileName); // 设置文件名

        return new ResponseEntity<>(desenFileBytes, headers, HttpStatus.OK);
    }


    @Override
    public ResponseEntity<byte[]> replaceVideoBackground(MultipartFile file, String params, String algName, MultipartFile sheet) throws IOException, SQLException, InterruptedException {
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
        String rawBgPathString  = rawBgPath.toAbsolutePath().toString();
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
        logExecutionTime(String.valueOf(executionTime), "Video");
        byte[] desenFileBytes = Files.readAllBytes(desenFilePath.toAbsolutePath());
        Long desenFileSize = Files.size(desenFilePath.toAbsolutePath());
        // 标志脱敏完成
        desenCom = true;
        String globalID = System.currentTimeMillis() + randomNum.nextInt() + "脱敏工具集";
        // 脱敏算法
        infoBuilders.desenAlg.append(55);
        // 脱敏参数
//        int[] param = algorithmInfo.getParams().toArray();
        List<Object> algParams = algorithmInfo.getParams();
        if (algParams == null) {
            infoBuilders.desenAlgParam.append("无参");
        } else {
            infoBuilders.desenAlgParam.append(algParams);
        }
        // 脱敏级别
        infoBuilders.desenLevel.append(Integer.parseInt(desenParam));
        // 脱敏前类型
        infoBuilders.desenInfoPreIden.append("video");
        // 脱敏后类型
        infoBuilders.desenInfoAfterIden.append("video");
        // 脱敏意图
        infoBuilders.desenIntention.append("对视频脱敏");
        // 脱敏要求
        infoBuilders.desenRequirements.append("对视频脱敏");

        ObjectMapper objectMapper = new ObjectMapper();

        // 线程池
        ExecutorService executorService = Executors.newFixedThreadPool(4);

        // 存证系统
        //存证请求  消息版本：中心0x1000，0x1010; 本地0x1100，0x1110
        String evidenceID = util.getSM3Hash((new String(rawFileBytes, StandardCharsets.UTF_8) + util.getTime()).getBytes());

        ReqEvidenceSave reqEvidenceSave = buildReqEvidenceSave(rawFileSize, objectMode, evidenceID);

        // 上报本地存证内容
        SubmitEvidenceLocal submitEvidenceLocal = buildSubmitEvidenceLocal(evidenceID, infoBuilders.desenAlg, rawFileName,
                rawFileBytes, rawFileSize, desenFileBytes, globalID, infoBuilders.desenInfoPreIden.toString(),
                infoBuilders.desenIntention, infoBuilders.desenRequirements, infoBuilders.desenControlSet,
                infoBuilders.desenAlgParam, startTime, endTime, infoBuilders.desenLevel, desenCom);

        // 发送方法

        executorService.submit(() -> {
            sendData.send2Evidence(reqEvidenceSave, submitEvidenceLocal);
        });

        // 效果评测系统
        SendEvaReq sendEvaReq = buildSendEvaReq(globalID, evidenceID, rawFileName, rawFileBytes, rawFileSize, desenFileName, desenFileBytes,
                desenFileSize, infoBuilders.desenInfoPreIden, infoBuilders.desenInfoAfterIden, infoBuilders.desenIntention,
                infoBuilders.desenRequirements, infoBuilders.desenControlSet, infoBuilders.desenAlg,
                infoBuilders.desenAlgParam, startTime, endTime, infoBuilders.desenLevel, objectMode, rawFileSuffix, desenCom);

        ObjectNode effectEvaContent = (ObjectNode) objectMapper.readTree(objectMapper.writeValueAsString(sendEvaReq));

        executorService.submit(() -> {
            sendData.send2EffectEva(sendEvaReq, rawFileBytes, desenFileBytes);
        });

        // TODO: 拆分重构系统

        // 合规检查系统
        SendRuleReq sendRuleReq = buildSendRuleReq(evidenceID, rawFileBytes, desenFileBytes, infoBuilders.desenInfoAfterIden,
                infoBuilders.desenIntention, infoBuilders.desenRequirements, infoBuilders.desenControlSet,
                infoBuilders.desenAlg, infoBuilders.desenAlgParam, startTime, endTime, infoBuilders.desenLevel, desenCom);
        ObjectNode ruleCheckContent = (ObjectNode) objectMapper.readTree(objectMapper.writeValueAsString(sendRuleReq));

        executorService.submit(() -> {
            sendData.send2RuleCheck(sendRuleReq);
        });
        // 关闭线程池
        executorService.shutdown();

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "video/mp4");
        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.parseMediaType("video/mp4")).body(desenFileBytes);

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
        String rawFacePathString  = rawFacePath.toAbsolutePath().toString();
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
        infoBuilders.desenAlgParam.append("无参,");
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
        logExecutionTime(String.valueOf(executionTime), "Image");

//        System.out.println("脱敏文件存放路径");
//        System.out.println(desenFilePath);

        // 脱敏结束时间
        String endTime = util.getTime();
        // 标志脱敏完成
        desenCom = true;
        String globalID = System.currentTimeMillis() + randomNum.nextInt() + "脱敏工具集";
        // 脱敏后文件字节流
        Long desenFileSize = Files.size(desenFilePath.toAbsolutePath());
        byte[] desenFileBytes = Files.readAllBytes(desenFilePath.toAbsolutePath());
        // 脱敏算法
        infoBuilders.desenAlg.append(48);
        // 脱敏前类型
        infoBuilders.desenInfoPreIden.append("image");
        // 脱敏后类型
        infoBuilders.desenInfoAfterIden.append("image");
        // 脱敏意图
        infoBuilders.desenIntention.append("对图像脱敏");
        // 脱敏要求
        infoBuilders.desenRequirements.append("对图像脱敏");
        // 脱敏后文件大小
        ObjectMapper objectMapper = new ObjectMapper();
        // 线程池
        ExecutorService executorService = Executors.newFixedThreadPool(4);

        // 存证系统
        //存证请求  消息版本：中心0x1000，0x1010; 本地0x1100，0x1110
        String evidenceID = util.getSM3Hash((new String(desenFileBytes, StandardCharsets.UTF_8) + util.getTime()).getBytes());

        ReqEvidenceSave reqEvidenceSave = buildReqEvidenceSave(rawFileSize, objectMode, evidenceID);

        // 上报本地存证内容
        SubmitEvidenceLocal submitEvidenceLocal = buildSubmitEvidenceLocal(evidenceID, infoBuilders.desenAlg, rawFileName,
                rawFileBytes, rawFileSize, desenFileBytes, globalID, infoBuilders.desenInfoPreIden.toString(),
                infoBuilders.desenIntention, infoBuilders.desenRequirements, infoBuilders.desenControlSet,
                infoBuilders.desenAlgParam, startTime, endTime, infoBuilders.desenLevel, desenCom);

        // 发送方法
        executorService.submit(() -> {
            sendData.send2Evidence(reqEvidenceSave, submitEvidenceLocal);
        });

        // 效果评测系统
        SendEvaReq sendEvaReq = buildSendEvaReq(globalID, evidenceID, rawFileName, rawFileBytes, rawFileSize, desenFileName, desenFileBytes,
                desenFileSize, infoBuilders.desenInfoPreIden, infoBuilders.desenInfoAfterIden, infoBuilders.desenIntention,
                infoBuilders.desenRequirements, infoBuilders.desenControlSet, infoBuilders.desenAlg,
                infoBuilders.desenAlgParam, startTime, endTime, infoBuilders.desenLevel, objectMode, rawFileSuffix, desenCom);

        ObjectNode effectEvaContent = (ObjectNode) objectMapper.readTree(objectMapper.writeValueAsString(sendEvaReq));

        executorService.submit(() -> {
            sendData.send2EffectEva(sendEvaReq, rawFileBytes, desenFileBytes);
        });

        // TODO: 拆分重构系统

        // 合规检查系统
        SendRuleReq sendRuleReq = buildSendRuleReq(evidenceID, rawFileBytes, desenFileBytes, infoBuilders.desenInfoAfterIden,
                infoBuilders.desenIntention, infoBuilders.desenRequirements, infoBuilders.desenControlSet,
                infoBuilders.desenAlg, infoBuilders.desenAlgParam, startTime, endTime, infoBuilders.desenLevel, desenCom);
        ObjectNode ruleCheckContent = (ObjectNode) objectMapper.readTree(objectMapper.writeValueAsString(sendRuleReq));

        executorService.submit(() -> {
            sendData.send2RuleCheck(sendRuleReq);
        });

        // 关闭线程池
        executorService.shutdown();
        HttpHeaders headers = new HttpHeaders();
        if (rawFileName.contains("jpg")) {
            headers.setContentType(MediaType.IMAGE_JPEG);
        } else if (rawFileName.contains("png")) {
            headers.setContentType(MediaType.IMAGE_PNG);
        }
        return ResponseEntity.ok()
                .headers(headers).body(desenFileBytes);
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
        Path rawFacePath = rawFilePath.resolve(imageFileName);
        String rawFilePathString = rawFilePath.toAbsolutePath().toString();
        String rawFacePathString  = rawFacePath.toAbsolutePath().toString();
        byte[] rawFileBytes = file.getBytes();
        Long rawFileSize = file.getSize();

        // 保存源文件
        file.transferTo(rawFilePath.toAbsolutePath());
        sheet.transferTo(rawFacePath.toFile());
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

        DSObject dsObject = new DSObject(Arrays.asList(rawFilePathString, rawFilePathString, desenFilePathString));
        algorithmInfo.execute(dsObject);
        // 结束时间
        endTimePoint = System.currentTimeMillis();
        // 脱敏耗时
        executionTime = endTimePoint - startTimePoint;
        log.info("脱敏用时" + executionTime + "ms");

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
        infoBuilders.desenAlgParam.append("无参,");
        // 脱敏级别
        infoBuilders.desenLevel.append(Integer.parseInt(desenParam));
        // 脱敏前类型
        infoBuilders.desenInfoPreIden.append("video");
        // 脱敏后类型
        infoBuilders.desenInfoAfterIden.append("video");
        // 脱敏意图
        infoBuilders.desenIntention.append("对视频脱敏");
        // 脱敏要求
        infoBuilders.desenRequirements.append("对视频脱敏");

        ObjectMapper objectMapper = new ObjectMapper();

        // 线程池
        ExecutorService executorService = Executors.newFixedThreadPool(4);

        // 存证系统
        //存证请求  消息版本：中心0x1000，0x1010; 本地0x1100，0x1110
        String evidenceID = util.getSM3Hash((new String(desenFileBytes, StandardCharsets.UTF_8) + util.getTime()).getBytes());


        ReqEvidenceSave reqEvidenceSave = buildReqEvidenceSave(rawFileSize, objectMode, evidenceID);

        // 上报本地存证内容
        SubmitEvidenceLocal submitEvidenceLocal = buildSubmitEvidenceLocal(evidenceID, infoBuilders.desenAlg, rawFileName,
                rawFileBytes, rawFileSize, desenFileBytes, globalID, infoBuilders.desenInfoPreIden.toString(),
                infoBuilders.desenIntention, infoBuilders.desenRequirements, infoBuilders.desenControlSet,
                infoBuilders.desenAlgParam, startTime, endTime, infoBuilders.desenLevel, desenCom);

        // 发送方法
       /* Thread evidenceThread = new Thread(() -> sendData.send2Evidence(reqEvidenceSave, submitEvidenceLocal));
        evidenceThread.start();*/
        Future<?> future_evidence = executorService.submit(() -> {
            sendData.send2Evidence(reqEvidenceSave, submitEvidenceLocal);
        });

        // 效果评测系统
        SendEvaReq sendEvaReq = buildSendEvaReq(globalID, evidenceID, rawFileName, rawFileBytes, rawFileSize, desenFileName, desenFileBytes,
                desenFileSize, infoBuilders.desenInfoPreIden, infoBuilders.desenInfoAfterIden, infoBuilders.desenIntention,
                infoBuilders.desenRequirements, infoBuilders.desenControlSet, infoBuilders.desenAlg,
                infoBuilders.desenAlgParam, startTime, endTime, infoBuilders.desenLevel, objectMode, rawFileSuffix, desenCom);

        ObjectNode effectEvaContent = (ObjectNode) objectMapper.readTree(objectMapper.writeValueAsString(sendEvaReq));

        Future<?> future_effect = executorService.submit(() -> {
            sendData.send2EffectEva(sendEvaReq, rawFileBytes, desenFileBytes);
        });

        // TODO: 拆分重构系统

        // 合规检查系统
        SendRuleReq sendRuleReq = buildSendRuleReq(evidenceID, rawFileBytes, desenFileBytes, infoBuilders.desenInfoAfterIden,
                infoBuilders.desenIntention, infoBuilders.desenRequirements, infoBuilders.desenControlSet,
                infoBuilders.desenAlg, infoBuilders.desenAlgParam, startTime, endTime, infoBuilders.desenLevel, desenCom);
        ObjectNode ruleCheckContent = (ObjectNode) objectMapper.readTree(objectMapper.writeValueAsString(sendRuleReq));

        executorService.submit(() -> {
            sendData.send2RuleCheck(sendRuleReq);
        });

        executorService.submit(() -> {
            sendData.send2RuleCheck(sendRuleReq);
        });

        // 关闭线程池
        executorService.shutdown();


        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "video/mp4");
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline;filename=" + desenFileName);
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

            case "laplaceToValue":
            case "gaussianToValue":
            case "randomLaplaceToValue":
            case "randomUniformToValue":
            case "randomGaussianToValue":
            case "valueShift":
            case "floor":
            case "valueMapping": {
                double val = Double.parseDouble(textInput);
                DSObject rawVal = new DSObject(Collections.singletonList(val));
                result = algorithmInfo.execute(rawVal, param).getList().get(0) + "";
                break;
            }
            case "dpDate":
            case "dpCode":
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
        System.out.println(result);
        return result;
    }
}


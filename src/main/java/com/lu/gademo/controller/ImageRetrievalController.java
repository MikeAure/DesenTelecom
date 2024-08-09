package com.lu.gademo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lu.gademo.entity.ga.effectEva.SendEvaReq;
import com.lu.gademo.entity.ga.evidence.ReqEvidenceSave;
import com.lu.gademo.entity.ga.evidence.SubmitEvidenceLocal;
import com.lu.gademo.entity.ga.ruleCheck.SendRuleReq;
import com.lu.gademo.model.LogSenderManager;
import com.lu.gademo.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@RestController
@RequestMapping("imageRetrieval")
public class ImageRetrievalController {

    private LogCollectUtil logCollectUtil;
    private LogSenderManager logSenderManager;
    private Util util;
    private Random randomNum;
    private Path currentDirectory;
    private Path rawFileDirectory;
    private Path desenFileDirectory;

    @Autowired
    public ImageRetrievalController(LogSenderManager logSenderManager, Util util, LogCollectUtil logCollectUtil) throws IOException {
//        this.desenCom = false;
        this.randomNum = new Random();
        this.logSenderManager = logSenderManager;
        this.util = util;
        this.logCollectUtil = logCollectUtil;
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
    }

    @PostMapping("/getImage")
    public ResponseEntity<byte[]> getImage(HttpServletRequest request, @RequestPart("file") MultipartFile file,
                                           @RequestParam("params") String params,
                                           @RequestParam("algName") String algName,
                                           @RequestParam("sheet") String sheet) throws IOException, NoSuchAlgorithmException, SignatureException, InvalidKeySpecException, NoSuchProviderException, InvalidKeyException {

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

        String eigenVectorFileName = fileTimeStamp + "_encrypted_eigenvector.txt";
        Path eigenVectorFilePath = desenFileDirectory.resolve(eigenVectorFileName).toAbsolutePath();
        String eigenVectorFilePathString = eigenVectorFilePath.toString();

        HttpSession httpSession = request.getSession();
        httpSession.setAttribute("eigenVectorFileName", eigenVectorFileName);
        // 脱敏参数处理
        Integer desenParam = Integer.valueOf(params);

        // 调用脱敏程序处理
        DSObject dsObject = new DSObject(Arrays.asList(rawFilePathString, desenFilePathString));

        log.info("Start image desen");
        // 脱敏开始时间
        String startTime = util.getTime();
        long startTimePoint = System.currentTimeMillis();

        Path desenAppPath = currentDirectory.resolve("image").resolve("ImageRetrieval");
        Path desenApp = desenAppPath.resolve("FUNC.py");
        CommandExecutor.executePython(rawFilePathString + " " + desenFilePathString + " " + eigenVectorFilePathString, "",
                desenApp.toAbsolutePath().toString());
        infoBuilders.desenAlg.append("103");
        infoBuilders.desenAlgParam.append("非失真图像脱敏算法");
        infoBuilders.desenLevel.append(0);
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
        // 脱敏前类型
        infoBuilders.desenInfoPreIden.append("image");
        // 脱敏后类型
        infoBuilders.desenInfoAfterIden.append("image");
        // 脱敏意图
        infoBuilders.desenIntention.append("对图像非失真脱敏");
        // 脱敏要求
        infoBuilders.desenRequirements.append("对图像非失真脱敏");
        // 脱敏数据类型
        infoBuilders.fileDataType.append(rawFileSuffix);
        // 脱敏算法信息
        ObjectMapper objectMapper = new ObjectMapper();

        // 线程池
        ExecutorService executorService = Executors.newFixedThreadPool(4);
        // 本地存证
        // 存证系统
        String evidenceID = util.getSM3Hash((new String(desenFileBytes, StandardCharsets.UTF_8) + util.getTime()).getBytes());

        //存证请求  消息版本：中心0x1000，0x1010; 本地0x1100，0x1110
        ReqEvidenceSave reqEvidenceSave = logCollectUtil.buildReqEvidenceSave(rawFileSize, objectMode, evidenceID);
        // 上报本地存证内容
        SubmitEvidenceLocal submitEvidenceLocal = logCollectUtil.buildSubmitEvidenceLocal(evidenceID, infoBuilders.desenAlg,
                rawFileName, rawFileBytes, rawFileSize, desenFileBytes, globalID, infoBuilders.desenInfoPreIden.toString(), infoBuilders.desenIntention,
                infoBuilders.desenRequirements, infoBuilders.desenControlSet, infoBuilders.desenAlgParam, startTime, endTime,
                infoBuilders.desenLevel, desenCom);
        // 发送方法
        executorService.submit(() -> {
            logSenderManager.send2Evidence(reqEvidenceSave, submitEvidenceLocal);
        });

        // 效果评测系统
        SendEvaReq sendEvaReq = logCollectUtil.buildSendEvaReq(globalID, evidenceID, rawFileName, rawFileBytes, rawFileSize,
                desenFileName, desenFileBytes, desenFileSize, infoBuilders.desenInfoPreIden, infoBuilders.desenInfoAfterIden,
                infoBuilders.desenIntention, infoBuilders.desenRequirements, infoBuilders.desenControlSet, infoBuilders.desenAlg,
                infoBuilders.desenAlgParam, startTime, endTime, infoBuilders.desenLevel, objectMode, rawFileSuffix, desenCom);

        executorService.submit(() -> {
            logSenderManager.send2EffectEva(sendEvaReq, rawFileBytes,
                    desenFileBytes);
        });

        // 拆分重构系统

        // 合规检查系统
        SendRuleReq sendRuleReq = logCollectUtil.buildSendRuleReq(evidenceID, rawFileBytes, desenFileBytes, infoBuilders.desenInfoAfterIden,
                infoBuilders.desenIntention, infoBuilders.desenRequirements, infoBuilders.desenControlSet, infoBuilders.desenAlg,
                infoBuilders.desenAlgParam, startTime, endTime, infoBuilders.desenLevel, desenCom, infoBuilders.fileDataType);

        executorService.submit(() -> {
            logSenderManager.send2RuleCheck(sendRuleReq);
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
        headers.setContentDispositionFormData("attachment", desenFileName); // 设置文件名

        return ResponseEntity.ok().headers(headers).body(desenFileBytes);


    }

    @PostMapping(value = "/getImageName", produces = "application/json;charset=UTF-8")
    public Map<String, String> getImagName(@RequestPart("file") MultipartFile file,
                                           @RequestParam("params") String params,
                                           @RequestParam("algName") String algName,
                                           @RequestParam("sheet") String sheet) throws IOException, NoSuchAlgorithmException, SignatureException, InvalidKeySpecException, NoSuchProviderException, InvalidKeyException {
        Map<String, String> resultMap = new HashMap<>();
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

        String eigenVectorFileName = fileTimeStamp + "_encrypted_eigenvector.txt";
        Path eigenVectorFilePath = desenFileDirectory.resolve(eigenVectorFileName).toAbsolutePath();
        String eigenVectorFilePathString = eigenVectorFilePath.toString();

        // 脱敏参数处理
        Integer desenParam = Integer.valueOf(params);

        // 调用脱敏程序处理
        DSObject dsObject = new DSObject(Arrays.asList(rawFilePathString, desenFilePathString));

        log.info("Start image desen");
        // 脱敏开始时间
        String startTime = util.getTime();
        long startTimePoint = System.currentTimeMillis();

        Path desenAppPath = currentDirectory.resolve("image").resolve("ImageRetrieval");
        Path desenApp = desenAppPath.resolve("FUNC.py");
        CommandExecutor.executePython(rawFilePathString + " " + desenFilePathString + " " + eigenVectorFilePathString, "",
                desenApp.toAbsolutePath().toString());
        infoBuilders.desenAlg.append("103");
        infoBuilders.desenAlgParam.append("非失真图像脱敏算法");
        infoBuilders.desenLevel.append(0);
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
        // 脱敏前类型
        infoBuilders.desenInfoPreIden.append("image");
        // 脱敏后类型
        infoBuilders.desenInfoAfterIden.append("image");
        // 脱敏意图
        infoBuilders.desenIntention.append("对图像非失真脱敏");
        // 脱敏要求
        infoBuilders.desenRequirements.append("对图像非失真脱敏");
        // 脱敏数据类型
        infoBuilders.fileDataType.append(rawFileSuffix);
        // 脱敏算法信息
        ObjectMapper objectMapper = new ObjectMapper();

        // 线程池
        ExecutorService executorService = Executors.newFixedThreadPool(4);
        // 本地存证
        // 存证系统
        String evidenceID = util.getSM3Hash((new String(desenFileBytes, StandardCharsets.UTF_8) + util.getTime()).getBytes());

        //存证请求  消息版本：中心0x1000，0x1010; 本地0x1100，0x1110
        ReqEvidenceSave reqEvidenceSave = logCollectUtil.buildReqEvidenceSave(rawFileSize, objectMode, evidenceID);
        // 上报本地存证内容
        SubmitEvidenceLocal submitEvidenceLocal = logCollectUtil.buildSubmitEvidenceLocal(evidenceID, infoBuilders.desenAlg,
                rawFileName, rawFileBytes, rawFileSize, desenFileBytes, globalID, infoBuilders.desenInfoPreIden.toString(), infoBuilders.desenIntention,
                infoBuilders.desenRequirements, infoBuilders.desenControlSet, infoBuilders.desenAlgParam, startTime, endTime,
                infoBuilders.desenLevel, desenCom);
        // 发送方法
        executorService.submit(() -> {
            logSenderManager.send2Evidence(reqEvidenceSave, submitEvidenceLocal);
        });

        // 效果评测系统
        SendEvaReq sendEvaReq = logCollectUtil.buildSendEvaReq(globalID, evidenceID, rawFileName, rawFileBytes, rawFileSize,
                desenFileName, desenFileBytes, desenFileSize, infoBuilders.desenInfoPreIden, infoBuilders.desenInfoAfterIden,
                infoBuilders.desenIntention, infoBuilders.desenRequirements, infoBuilders.desenControlSet, infoBuilders.desenAlg,
                infoBuilders.desenAlgParam, startTime, endTime, infoBuilders.desenLevel, objectMode, rawFileSuffix, desenCom);

        executorService.submit(() -> {
            logSenderManager.send2EffectEva(sendEvaReq, rawFileBytes,
                    desenFileBytes);
        });

        // 拆分重构系统

        // 合规检查系统
        SendRuleReq sendRuleReq = logCollectUtil.buildSendRuleReq(evidenceID, rawFileBytes, desenFileBytes, infoBuilders.desenInfoAfterIden,
                infoBuilders.desenIntention, infoBuilders.desenRequirements, infoBuilders.desenControlSet, infoBuilders.desenAlg,
                infoBuilders.desenAlgParam, startTime, endTime, infoBuilders.desenLevel, desenCom, infoBuilders.fileDataType);

        executorService.submit(() -> {
            logSenderManager.send2RuleCheck(sendRuleReq);
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
        headers.setContentDispositionFormData("attachment", desenFileName); // 设置文件名

        resultMap.put("imageFileName", desenFileName);
        resultMap.put("eigenVectorFileName", eigenVectorFileName);
        return resultMap;

    }

    @GetMapping("downloadFile")
    ResponseEntity<Resource> downloadFile(@RequestParam String imageFileName) throws MalformedURLException {
        Path desenImagePath = desenFileDirectory.resolve(imageFileName);
        Resource resource = new UrlResource(desenImagePath.toUri());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentDispositionFormData("attachment", imageFileName);
        if (resource.exists()) {
            return ResponseEntity.ok().headers(headers)
                    .body(resource);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

    @GetMapping("downloadEigenVector")
    ResponseEntity<byte[]> downloadEigenVector(HttpServletRequest request) throws IOException {
        HttpSession httpSession = request.getSession();
        String fileName = httpSession.getAttribute("eigenVectorFileName").toString();
        log.info("Download eigen vector file: " + fileName);
        Path filePath = desenFileDirectory.resolve(fileName);
        byte[] fileContent = Files.readAllBytes(filePath);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentDispositionFormData("attachment", fileName);
        return ResponseEntity.ok().headers(headers).body(fileContent);
    }
}

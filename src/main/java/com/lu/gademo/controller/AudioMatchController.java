package com.lu.gademo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lu.gademo.model.LogSenderManager;
import com.lu.gademo.service.impl.FileStorageService;
import com.lu.gademo.utils.CommandExecutor;
import com.lu.gademo.utils.DesenInfoStringBuilders;
import com.lu.gademo.utils.Util;
import com.lu.gademo.utils.impl.UtilImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.crypto.Cipher;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import static com.lu.gademo.utils.VideoUtil.videoAESEncOrDec;

@Slf4j
@RestController
@RequestMapping("/audioMatch")
public class AudioMatchController {
    Util util;
    String python;
    // 当前路径
    File directory;
    String currentPath;
    String appPath;
    // 脱敏程序路径
    String desenApp;
    String command;
    private FileStorageService fileStorageService;
    private LogSenderManager logSenderManager;
    private Random randomNum = new Random();

    @Autowired
    public AudioMatchController(Util util, FileStorageService fileStorageService, LogSenderManager logSenderManager) {
        this.util = util;
        this.fileStorageService = fileStorageService;
        this.logSenderManager = logSenderManager;

        if (this.util.isCondaInstalled(this.util.isLinux())) {
            this.python = "conda run -n torch_env python";
        } else if (this.util.isLinux()) {
            this.python = "python3";
        } else {
            this.python = "python";
        }
//        System.out.println(this.python);
        // 当前路径
        this.directory = new File("");
        this.currentPath = directory.getAbsolutePath();
        this.appPath = currentPath + File.separator + "audio_match";
        // 脱敏程序路径
        this.desenApp = appPath + File.separator + "sck.py";

        this.command = python + " " + desenApp;

    }

    private String saveFile(MultipartFile file) throws IOException {
        File directory = new File("");
        String currentPath = directory.getAbsolutePath();
        // 时间
        String time = String.valueOf(System.currentTimeMillis());
        // 源文件保存目录
        File rawDirectory = new File("raw_files");
        if (!rawDirectory.exists()) {
            rawDirectory.mkdir();
        }
        // 文件名
        String rawFileName = time + file.getOriginalFilename();

        String rawFilePath = currentPath + File.separator + "raw_files" + File.separator + rawFileName;

        file.transferTo(new File(rawFilePath));
        return rawFilePath;
    }

//    @PostMapping(value = "signUp", produces = "application/json;charset=UTF-8")
//    public ServerResponse signUp(@RequestPart MultipartFile file, @RequestParam String name) {
//        String rawFilePath;
//        // Save the file sent by frontend
//        try {
//            rawFilePath = saveFile(file);
//            System.out.println(rawFilePath);
//        } catch (IOException e) {
//            log.error(e.getMessage());
//            return new ServerResponse("error", "Save file failed");
//        }
//        // Invoke Python script to verify if the user has signed up
//        try {
//            String username = name + "&&" + "0" + "@@";
//            String parameter = "\"" + username + "\"" + " " + rawFilePath;
//
//            // 根据当前操作系统类型决定是否使用conda环境
////            if(!util.isLinux()){
////                String conda = "conda run -n torch_env ";
////                command = conda + command;
////            }
//            Process signUpProcess = Runtime.getRuntime().exec(command + " " + parameter);
//            System.out.println(command + " " + parameter);
//            BufferedReader reader = new BufferedReader(new InputStreamReader(signUpProcess.getInputStream()));
//            // 获得python脚本进程的输出
//            String line;
//            while ((line = reader.readLine()) != null) {
//                System.out.println("Python Output: " + line);
//                if (line.contains("status")) {
//                    break;
//                }
//            }
//            // 等待进程结束并获取退出码
//            boolean exitCode = signUpProcess.waitFor(60, TimeUnit.SECONDS);
//            System.out.println("Terminated normally : " + exitCode);
//            final ObjectMapper objectMapper = new ObjectMapper();
//            return objectMapper.readValue(line, ServerResponse.class);
//        } catch (Exception e) {
//            log.error(e.getMessage());
//            return new ServerResponse("error", "Server returns message error");
//        }
//    }

    @PostMapping(value = "signUp", produces = "application/json;charset=UTF-8")
    public ServerResponse signUp(@RequestPart MultipartFile file, @RequestParam String name) throws IOException {
        Path rawFileDirectory = Paths.get("raw_files");
        Path desenFileDirectory = Paths.get("desen_files");
        Boolean desenCom = true;
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
        // 调用视频加密方法
        String startTime = util.getTime();
        // 准备返回文件
        String globalID = System.currentTimeMillis() + randomNum.nextInt() + "脱敏工具集";

        infoBuilders.desenAlg.append("105");
        infoBuilders.desenAlgParam.append("非失真音频脱敏算法");
        infoBuilders.desenLevel.append(0);
        // 脱敏前类型
        infoBuilders.desenInfoPreIden.append("audio");
        // 脱敏后类型
        infoBuilders.desenInfoAfterIden.append("audio");
        // 脱敏意图
        infoBuilders.desenIntention.append("对音频非失真脱敏");
        // 脱敏要求
        infoBuilders.desenRequirements.append("对音频非失真脱敏");
        // 脱敏数据类型
        infoBuilders.fileDataType.append(rawFileSuffix);
        String username = name + "&&" + "0" + "@@";
        String parameter = "\"" + username + "\"" + " " + rawFilePath;
        try {
//            Process signUpProcess = Runtime.getRuntime().exec(command + " " + parameter);
            log.info("Python command {} {}", command, parameter);
            List<String> result = CommandExecutor.openExe(command + " " + parameter, "", 2);

//            BufferedReader reader = new BufferedReader(new InputStreamReader(signUpProcess.getInputStream()));
            // 获得python脚本进程的输出
            String line = "";
            for (String s : result) {
//                log.info("Python Output: {}", s);
                if (s.contains("status")) {
                    line = s;
                    break;
                }
            }
//            while ((line = reader.readLine()) != null) {
//                log.info("Python Output: {}", line);
//                if (line.contains("status")) {
//                    break;
//                }
//            }
            // 等待进程结束并获取退出码
//            int exitCode = signUpProcess.waitFor();
            String endTime = util.getTime();
//            log.info("Exited with code : {}", exitCode);
            String evidenceID = util.getSM3Hash((new String(rawFileBytes, StandardCharsets.UTF_8) + util.getTime()).getBytes());
            // 对于非失真脱敏，脱敏前后文件信息是相同的
            logSenderManager.submitToFourSystems(globalID, evidenceID, desenCom, objectMode, infoBuilders, rawFileName,
                    rawFileBytes, rawFileSize, rawFileName, rawFileBytes, rawFileSize, objectMode, rawFileSuffix,
                    startTime, endTime);
            final ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(line, ServerResponse.class);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ServerResponse("error", "Server returns message error");
        }

    }

    @PostMapping(value = "signIn", produces = "application/json;charset=UTF-8")
    public ServerResponse signIn(@RequestPart MultipartFile file, @RequestParam String name) {
        String rawFilePath;
        // Save the file sent by frontend
        try {
            rawFilePath = saveFile(file);
            System.out.println(rawFilePath);
        } catch (IOException e) {
            log.error(e.getMessage());
            return new ServerResponse("error", "Save file failed");
        }
        // Invoke Python script to verify if the user has signed up
        try {
            String username = name + "&&" + "1" + "@@";
            String parameter = "\"" + username + "\"" + " " + rawFilePath;

            log.info("Python command {} {}", command, parameter);
            List<String> result = CommandExecutor.openExe(command + " " + parameter, "", 2);
            String line = "";
            for (String s : result) {
//                log.info("Python Output: {}", s);
                if (s.contains("status")) {
                    line = s;
                    break;
                }
            }

            // 等待进程结束并获取退出码
//            boolean exitCode = signUpProcess.waitFor(60, TimeUnit.SECONDS);
//            System.out.println("Terminated normally : " + exitCode);
            final ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(line, ServerResponse.class);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ServerResponse("error", "Server returns message error");
        }

    }

    @PostMapping(value = "downloadEncryptedVoice")
    public ResponseEntity<byte[]> signIn(@RequestParam String name) {
        log.info("Download encrypted voiceprint");
        Path voicePrint = Paths.get("voiceprinttxt");
        String encryptedVoiceprintName = name + "_encrypted_voiceprint.txt";
        Path ecnryptedTextPath = voicePrint.resolve(encryptedVoiceprintName);
        try {
//            byte[] voicePrintBytes = Files.readAllBytes(voicePrint);
            byte[] ecnryptedTextBytes = Files.readAllBytes(ecnryptedTextPath);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment; filename=" + name + ".txt");
            return ResponseEntity.ok().headers(headers).body(ecnryptedTextBytes);
        } catch (IOException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(500).body("Download failed".getBytes());
        }
    }

}

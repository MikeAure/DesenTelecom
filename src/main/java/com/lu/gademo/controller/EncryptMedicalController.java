package com.lu.gademo.controller;

import com.lu.gademo.model.LogSenderManager;
import com.lu.gademo.timeSeries.MainTest;
import com.lu.gademo.utils.CommandExecutor;
import com.lu.gademo.utils.DesenInfoStringBuilders;
import com.lu.gademo.utils.Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * 医疗诊断数据加密

 */
@Slf4j
@RestController
@RequestMapping("/encryptMedical")
public class EncryptMedicalController {
    private Path currentDirectory;
    private Path rawFileDirectory;
    private Path desenFileDirectory;
    private Util util;
    private LogSenderManager logSenderManager;

    public EncryptMedicalController(Util util, LogSenderManager logSenderManager) {
        this.currentDirectory = Paths.get("");
        this.rawFileDirectory = Paths.get("raw_files");
        this.desenFileDirectory = Paths.get("desen_files");
        this.util = util;
        this.logSenderManager = logSenderManager;
    }

    /**
     *
     * @param file 上传的文件
     * @return 返回加密后的文件
     * @throws InterruptedException
     */
    @ResponseBody
    @RequestMapping(value = "/receiveMedicalCsv", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseEntity<Map<String, Object>> receiveMedicalCsv(@RequestPart("file") MultipartFile file) throws InterruptedException, IOException {
        Process process;
        Random randomNum = new Random();
        Boolean desenCom = true;
        DesenInfoStringBuilders infoBuilders = new DesenInfoStringBuilders();
        String objectMode = "sheet";
        // 设置文件时间戳
        String fileTimeStamp = String.valueOf(System.currentTimeMillis());
        String rawFileName = fileTimeStamp + file.getOriginalFilename();
        Path rawFilePath = rawFileDirectory.resolve(rawFileName);
        String rawFileSuffix = "csv";

        byte[] rawFileBytes = new byte[0];
        Long rawFileSize = 0L;
        String rawFilePathString = rawFilePath.toAbsolutePath().toString();
        log.info(rawFilePath.toAbsolutePath().toString());

        String desenFileName = "desen_" + rawFileName;
        Path desenFilePath = desenFileDirectory.resolve(desenFileName);

        log.info(desenFilePath.toAbsolutePath().toString());
        String desenFilePathString = desenFilePath.toAbsolutePath().toString();

        try {
            file.transferTo(rawFilePath.toAbsolutePath());
            rawFileBytes = Files.readAllBytes(rawFilePath);
            rawFileSize = Files.size(rawFilePath);
        } catch (IOException e) {
            log.error(e.getMessage());
            Map<String, Object> result = new HashMap<>();
            result.put("status", "error");
            result.put("data", "Save file failed");
            return ResponseEntity.ok().body(result);
        }

        Path desenAppPath = currentDirectory.resolve("nonlinearSVM");
        Map<String, Object> result = new HashMap<>();
        Path desenAppServer = desenAppPath.resolve("server.py");
        String command = CommandExecutor.getPythonCommand() + " " + desenAppServer.toAbsolutePath();

        // 启动对应的服务器
        try {
            log.info("Nondistortion excel start");
            log.info("Server execute command: {}", command);
            // 这里使用Runtime执行命令，不使用CommandExecutor
            process = Runtime.getRuntime().exec(command, null, desenAppPath.toFile());
//            new Thread(() -> {
//                while (process.isAlive()) {
//                    // 进程还在运行，执行其他任务或休眠一段时间
//                    try {
//                        Thread.sleep(1000); // 每秒检查一次
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//                // 进程结束后的操作
//                System.out.println("Process finished with exit code: " + process.exitValue());
//            }).start();
        } catch (IOException e) {
            log.error(e.getMessage());
            result.put("status", "error");
            result.put("data", "Start server failed");
            return ResponseEntity.ok().body(result);
        }
        // 等待服务器启动
        Thread.sleep(2000);

        Path desenAppClient = desenAppPath.resolve("client.py");
        String startTime = util.getTime();
        List<String> commandResult = CommandExecutor.executePython(
                rawFilePathString + " " + desenFilePathString, "",
                desenAppClient.toAbsolutePath().toString());
        String endTime = util.getTime();
        if (commandResult == null || !desenFilePath.toFile().exists()) {
            result.put("status", "error");
            result.put("data", "Execute Python script failed");
            process.destroy();
            return ResponseEntity.ok().body(result);
        } else {
            try {
                // 准备返回文件
                String globalID = System.currentTimeMillis() + randomNum.nextInt() + "脱敏工具集";

                infoBuilders.desenAlg.append("102");
                infoBuilders.desenAlgParam.append("非失真表格脱敏算法");
                infoBuilders.desenLevel.append(0);
                // 脱敏前类型
                infoBuilders.desenInfoPreIden.append("csv");
                // 脱敏后类型
                infoBuilders.desenInfoAfterIden.append("csv");
                // 脱敏意图
                infoBuilders.desenIntention.append("对表格非失真脱敏");
                // 脱敏要求
                infoBuilders.desenRequirements.append("对表格非失真脱敏");
                // 脱敏数据类型
                infoBuilders.fileDataType.append(rawFileSuffix);

                String evidenceID = util.getSM3Hash((new String(rawFileBytes, StandardCharsets.UTF_8) + util.getTime()).getBytes());

                byte[] desenFileBytes = Files.readAllBytes(desenFilePath);
                Long desenFileSize = Files.size(desenFilePath);
                logSenderManager.submitToFourSystems(globalID, evidenceID, desenCom, objectMode, infoBuilders, rawFileName,
                        rawFileBytes, rawFileSize, rawFileName, desenFileBytes, desenFileSize, objectMode, rawFileSuffix,
                        startTime, endTime);
                result.put("status", "ok");
                result.put("data", desenFileBytes);
                HttpHeaders headers = new HttpHeaders();
                headers.setContentDispositionFormData("attachment", desenFileName);
                return ResponseEntity.ok().headers(headers).body(result);
            } catch (IOException e) {
                log.error(e.getMessage());
                result.put("status", "error");
                result.put("data", "Read file failed");
                process.destroy();
                return ResponseEntity.ok().body(result);
            }
        }

//        byte[] fileContent = Files.readAllBytes(desenFilePath);
//        result.put("status", "ok");
//        result.put("data", fileContent);
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentDispositionFormData("attachment", desenFileName);
//        return ResponseEntity.ok().headers(headers).body(result);

    }
}

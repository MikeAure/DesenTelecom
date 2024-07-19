package com.lu.gademo.controller;

import com.lu.gademo.utils.CommandExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/encryptMedical")
public class EncryptMedicalController {
    private Path currentDirectory;
    private Path rawFileDirectory;
    private Path desenFileDirectory;

    public EncryptMedicalController() {
        this.currentDirectory = Paths.get("");
        this.rawFileDirectory = Paths.get("raw_files");
        this.desenFileDirectory = Paths.get("desen_files");

    }

    // 启动医疗诊断服务器
    @ResponseBody
    @RequestMapping(value = "/receiveMedicalCsv", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResponseEntity<Map<String, Object>> receiveMedicalCsv(@RequestPart("file") MultipartFile file) throws IOException {
        Process process;
        String fileTimeStamp = String.valueOf(System.currentTimeMillis());
        String rawFileName = fileTimeStamp + file.getOriginalFilename();
        Path rawFilePath = rawFileDirectory.resolve(rawFileName);
        String rawFilePathString = rawFilePath.toAbsolutePath().toString();
        log.info(rawFilePath.toAbsolutePath().toString());

        String desenFileName = "desen_" + rawFileName;
//        Path desenFilePath = desenFileDirectory.resolve(desenFileName);
        Path desenFilePath = desenFileDirectory.resolve(desenFileName);

        log.info(desenFilePath.toAbsolutePath().toString());
        String desenFilePathString = desenFilePath.toAbsolutePath().toString();

        try {
            file.transferTo(rawFilePath.toAbsolutePath());
        } catch (IOException e) {
            log.error(e.getMessage());
            Map<String, Object> result = new HashMap<>();
            result.put("status", "error");
            result.put("data", "Save file failed");
            return ResponseEntity.ok().body(result);
        }

        Path desenAppPath = currentDirectory.resolve("linearSVM");
        Map<String, Object> result = new HashMap<>();
        String command = CommandExecutor.getPythonCommand() + " " + this.currentDirectory.resolve("linearSVM").resolve("server.py").toAbsolutePath();
        // 在这里启动服务器
        try {
            log.info("Nondistortion excel start");
            log.info("Server execute command: {}", command);
            process = Runtime.getRuntime().exec(command, null, desenAppPath.toFile());
//            Thread.sleep(2000);
        } catch (IOException e) {
            log.error(e.getMessage());
            result.put("status", "error");
            result.put("data", "Start server failed");
            return ResponseEntity.ok().body(result);
        }

        Path desenApp = desenAppPath.resolve("client.py");
        List<String> commandResult = CommandExecutor.executePython(rawFilePathString + " " + desenFilePathString, "",
                desenApp.toAbsolutePath().toString());
        if (commandResult == null || !desenFilePath.toFile().exists()) {
            result.put("status", "error");
            result.put("data", "Execute Python script failed");
            process.destroy();
            return ResponseEntity.ok().body(result);
        } else {
            try {
                byte[] fileContent = Files.readAllBytes(desenFilePath);
                result.put("status", "ok");
                result.put("data", fileContent);
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

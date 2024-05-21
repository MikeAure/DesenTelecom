package com.lu.gademo.controller;


import com.lu.gademo.entity.Config;
import com.lu.gademo.service.FileService;
import com.sun.istack.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/telecom")
public class TelecomController {
    private final List<String> imageType;
    private final List<String> videoType;
    private final List<String> audioType;
    FileService fileService;

    public TelecomController(FileService fileService) {
        this.fileService = fileService;
        this.imageType = Arrays.asList("jpg", "jpeg", "png");
        this.videoType = Arrays.asList("mp4", "avi", "mkv");
        this.audioType = Arrays.asList("mp3", "wav");
    }

    @PostMapping(value = "/api/desenFile")
    @CrossOrigin("*")
    ResponseEntity<Map<String, Object>> fileDesen(@RequestPart("config") Config config,
                                                  @NotNull @RequestPart("file") MultipartFile file) {
        Map<String, Object> response = new HashMap<>();

        String params = config.getParams();
        String algName = config.getAlgName();
        String sheet = config.getSheet();

        log.info("File Size: " + file.getSize());
        log.info("File Name: " + file.getOriginalFilename());

        // 调用脱敏函数
        String fileName = file.getOriginalFilename();
        System.out.println(file.getOriginalFilename());
        // 获取文件后缀
        String[] names = new String[0];
        if (fileName != null) {
            names = fileName.split("\\.");
        }
        //System.out.println(Arrays.toString(names));
        String fileType = names[names.length - 1];
        log.info("File Type: " + fileType);
        log.info("File Sheet: " + sheet);

        byte[] responseData;
        // 判断数据模态
        try {
            if ("xlsx".equals(fileType)) {
                responseData = fileService.dealExcel(file, params, sheet).getBody();
            } else if (imageType.contains(fileType)) {
                responseData = fileService.dealImage(file, params, algName).getBody();
            } else if (videoType.contains(fileType)) {
                responseData = fileService.dealVideo(file, params, algName).getBody();
            } else if (audioType.contains(fileType)) {
                responseData = fileService.dealAudio(file, params, algName, sheet).getBody();
            } else if ("csv".equals(fileType)) {
                responseData = fileService.dealCsv(file, params, algName).getBody();
            } else {
                responseData = fileService.dealGraph(file, params).getBody();
            }
        } catch (Exception e) {
            response.put("message", "error");
            response.put("data", e.getMessage());
            return ResponseEntity.ok().body(response);
        }

        response.put("message", "ok");
        response.put("data", responseData);
        return ResponseEntity.ok().body(response);

    }

}

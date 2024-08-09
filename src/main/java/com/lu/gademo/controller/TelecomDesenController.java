package com.lu.gademo.controller;

import com.lu.gademo.entity.FileStorageDetails;
import com.lu.gademo.service.FileService;
import com.lu.gademo.service.impl.FileStorageService;
import com.lu.gademo.utils.Result;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.annotation.MultipartConfig;
import java.io.IOException;

// 用于接收电信Agent的请求
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/telecomDesen")
public class TelecomDesenController {
    private FileService fileService;
    private FileStorageService fileStorageService;


    // 电信Agent请求脱敏文件
    @PostMapping("/desenExcel")
    public Result<?> desenExcel(@RequestPart("file") MultipartFile file,
                           @RequestParam("params") String params,
                           @RequestParam("strategy") String strategy,
                           @RequestParam("sheetName") String sheetName) throws IOException {
        FileStorageDetails fileStorageDetails = null;
        int strategyInt = Integer.parseInt(strategy);
        String sheetNameWithStrategy = "";
        switch (strategyInt) {
            case 2: {
                sheetNameWithStrategy = sheetName + "_medium";
                break;
            }
            case 3: {
                sheetNameWithStrategy = sheetName + "_high";
                break;
            }
            default: {
                sheetNameWithStrategy = sheetName + "_low";
                break;
            }

        }
        try {
            fileStorageDetails = fileStorageService.saveRawFile(file);
        } catch (IOException e) {
            log.error("Failed to save raw file: {}", e.getMessage());
            return new Result<>(500, "Faild to save file", "");
        }
        try {
            fileService.dealExcel(fileStorageDetails, params, sheetNameWithStrategy, false);
        } catch (Exception e) {
            log.error("Failed to desen excel: {}", e.getMessage());
            return new Result<>(500, "Failed to desen excel", "");
        }

        return new Result<>(200, "Success", "");
    }
}

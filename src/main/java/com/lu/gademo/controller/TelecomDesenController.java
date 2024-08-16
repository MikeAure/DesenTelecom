package com.lu.gademo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lu.gademo.entity.ExcelParam;
import com.lu.gademo.entity.FileStorageDetails;
import com.lu.gademo.service.ExcelParamService;
import com.lu.gademo.service.FileService;
import com.lu.gademo.service.impl.FileStorageService;
import com.lu.gademo.utils.LogCollectUtil;
import com.lu.gademo.utils.Result;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

// 用于接收电信Agent的请求
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/telecomDesen")
public class TelecomDesenController {
    private FileService fileService;
    private FileStorageService fileStorageService;
    private ExcelParamService excelParamService;
    private LogCollectUtil logCollectUtil;

    private ObjectMapper objectMapper = new ObjectMapper();

    private void setStrategyConfig(List<ExcelParam> strategyConfig, List<ExcelParam> reqConfigs) {
        for (int i = 0; i < strategyConfig.size(); i++) {
            ExcelParam rawConfig = strategyConfig.get(i);
            String colName = rawConfig.getColumnName();
            String fieldName = rawConfig.getFieldName();
            int tmParam = rawConfig.getTmParam();
            ExcelParam reqConfig = null;
            for (ExcelParam item : reqConfigs) {
                if (item.getColumnName().equals(fieldName)) {
                    reqConfig = item;
                    break;
                }
            }
            if (reqConfig != null) {
                int reqConfigTmParam = reqConfig.getTmParam();
                rawConfig.setTmParam(Math.max(tmParam, reqConfigTmParam));
            }
        }
    }

    // 电信Agent请求脱敏文件
    @PostMapping("/desenExcel")
    public Result<?> desenExcel(@RequestPart("file") MultipartFile file,
                           @RequestParam("params") String params,
                           @RequestParam("algName") String algName,
                           @RequestParam("sheet") String sheetName) throws IOException {
        FileStorageDetails fileStorageDetails1 = null;
        FileStorageDetails fileStorageDetails2 = null;
        FileStorageDetails fileStorageDetails3 = null;
//        int strategyInt = Integer.parseInt(algName);
//        String sheetNameWithStrategy = "";

        String lowStrategy = sheetName + "_low";
        String mediumStrategy = sheetName + "_medium";
        String highStrategy = sheetName + "_high";

        List<ExcelParam> requestConfigs = logCollectUtil.jsonStringToParams(params);

        List<ExcelParam> lowStrategyConfig = excelParamService.getParamsByTableName(lowStrategy + "_param");
        List<ExcelParam> mediumStrategyConfig = excelParamService.getParamsByTableName(mediumStrategy + "_param");
        List<ExcelParam> highStrategyConfig = excelParamService.getParamsByTableName(highStrategy + "_param");

        setStrategyConfig(lowStrategyConfig, requestConfigs);
        setStrategyConfig(mediumStrategyConfig, requestConfigs);
        setStrategyConfig(highStrategyConfig, requestConfigs);

        String lowStrategyConfigString = objectMapper.writeValueAsString(lowStrategyConfig);
        String mediumStrategyConfigString = objectMapper.writeValueAsString(mediumStrategyConfig);
        String highStrategyConfigString = objectMapper.writeValueAsString(highStrategyConfig);
//        switch (strategyInt) {
//            case 2: {
//                sheetNameWithStrategy = sheetName + "_medium";
//                break;
//            }
//            case 3: {
//                sheetNameWithStrategy = sheetName + "_high";
//                break;
//            }
//            default: {
//                sheetNameWithStrategy = sheetName + "_low";
//                break;
//            }
//        }
        try {
            fileStorageDetails1 = fileStorageService.saveRawFileWithDesenInfo(file);
            fileStorageDetails2 = fileStorageService.saveRawFileWithDesenInfo(file);
            fileStorageDetails3= fileStorageService.saveRawFileWithDesenInfo(file);
        } catch (IOException e) {
            log.error("Failed to save raw file: {}", e.getMessage());
            return new Result<>(500, "Failed to save file", "");
        }
        try {
            log.info("正在使用低脱敏策略进行脱敏");
            ResponseEntity<byte[]> lowResponseEntity = fileService.dealExcel(fileStorageDetails1, lowStrategyConfigString,
                    lowStrategy, false);
            log.info("正在使用中脱敏策略进行脱敏");
            ResponseEntity<byte[]> mediumResponseEntity = fileService.dealExcel(fileStorageDetails2, mediumStrategyConfigString,
                    mediumStrategy, false);
            log.info("正在使用高脱敏策略进行脱敏");
            ResponseEntity<byte[]> highResponseEntity = fileService.dealExcel(fileStorageDetails3, highStrategyConfigString,
                    highStrategy, false);
            log.info("LowResponseEntity Status Code: {}", lowResponseEntity.getStatusCode());
            log.info("MediumResponseEntity Status Code: {}", mediumResponseEntity.getStatusCode());
            log.info("HighResponseEntity Status Code: {}", highResponseEntity.getStatusCode());

            if (lowResponseEntity.getStatusCode() != HttpStatus.OK ||
                    mediumResponseEntity.getStatusCode() != HttpStatus.OK ||
                    highResponseEntity.getStatusCode() != HttpStatus.OK) {
                // 如果有任意一个需求未完成，则返回异常结果
                return new Result<>(500, "Failed to complete all desen tasks", "");
            }
        } catch (Exception e) {
            log.error("Failed to desen excel: {}", e.getMessage());
            return new Result<>(500, "Failed to desen excel", "");
        }

        return new Result<>(200, "Success", "");
    }
}

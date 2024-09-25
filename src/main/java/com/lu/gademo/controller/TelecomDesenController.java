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
import java.util.Scanner;

/**
 * 用于接收电信Agent的请求
 */
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

    /**
     * 根据课题二的配置设置脱敏策略
     * @param strategyConfig
     * @param reqConfigs
     */
    private void setStrategyConfig(List<ExcelParam> strategyConfig, List<ExcelParam> reqConfigs) {
        for (int i = 0; i < strategyConfig.size(); i++) {
            ExcelParam rawConfig = strategyConfig.get(i);
            String colName = rawConfig.getColumnName();
            String fieldName = rawConfig.getFieldName();
            int tmParam = rawConfig.getTmParam();
            ExcelParam reqConfig = null;
            // 如果为主键列，则不脱敏
            if (colName.equals("CUST_ID")) {
                rawConfig.setTmParam(0);
            }
            for (ExcelParam item : reqConfigs) {
                if (item.getColumnName().equals(colName)) {
                    reqConfig = item;
                    break;
                }
            }
            // 避免设置CUST_ID
            if (reqConfig != null && !colName.equals("CUST_ID")) {
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
//        FileStorageDetails fileStorageDetails1 = null;
        FileStorageDetails fileStorageDetails2 = null;
//        FileStorageDetails fileStorageDetails3 = null;
//        int strategyInt = Integer.parseInt(algName);
//        String sheetNameWithStrategy = "";

//        String lowStrategy = sheetName + "_low";
        String mediumStrategy = sheetName + "_medium";
        String wrongStrategy = sheetName + "_wrong";
//        String highStrategy = sheetName + "_high";

        List<ExcelParam> requestConfigs = logCollectUtil.jsonStringToParams(params);

//        List<ExcelParam> lowStrategyConfig = excelParamService.getParamsByTableName(lowStrategy + "_param");
        List<ExcelParam> mediumStrategyConfig = excelParamService.getParamsByTableName(mediumStrategy + "_param");
        // 展示重脱敏逻辑
        List<ExcelParam> wrongStrategyConfig = excelParamService.getParamsByTableName(wrongStrategy + "_param");

//        List<ExcelParam> highStrategyConfig = excelParamService.getParamsByTableName(highStrategy + "_param");

//        setStrategyConfig(lowStrategyConfig, requestConfigs);
        // 与课题二配置文件进行比对，设置脱敏策略
        log.info("结合分类分级文件设置脱敏策略");
        setStrategyConfig(mediumStrategyConfig, requestConfigs);
//        setStrategyConfig(highStrategyConfig, requestConfigs);

//        String lowStrategyConfigString = objectMapper.writeValueAsString(lowStrategyConfig);
        String mediumStrategyConfigString = objectMapper.writeValueAsString(mediumStrategyConfig);
        String wrongStrategyConfigString = objectMapper.writeValueAsString(wrongStrategyConfig);
//        String highStrategyConfigString = objectMapper.writeValueAsString(highStrategyConfig);
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
//            fileStorageDetails1 = fileStorageService.saveRawFileWithDesenInfo(file);
            fileStorageDetails2 = fileStorageService.saveRawFileWithDesenInfo(file);
//            fileStorageDetails3 = fileStorageService.saveRawFileWithDesenInfo(file);
        } catch (IOException e) {
            log.error("Failed to save raw file: {}", e.getMessage());
            return new Result<>(500, "Failed to save file", "");
        }
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please press enter to continue:");
        scanner.nextLine();
        try {
//            log.info("正在使用低脱敏策略进行脱敏");
//            ResponseEntity<byte[]> lowResponseEntity = fileService.dealExcel(fileStorageDetails1, lowStrategyConfigString,
//                    lowStrategy, false);
            ResponseEntity<byte[]> responseEntity = null;
            if (algName.equals("wrong")) {
                log.info("重脱敏逻辑实现");
                responseEntity = fileService.dealExcel(fileStorageDetails2, wrongStrategyConfigString,
                        wrongStrategy, false);
            } else {
                log.info("正在使用脱敏策略进行脱敏");
                responseEntity = fileService.dealExcel(fileStorageDetails2, mediumStrategyConfigString,
                        mediumStrategy, false);
            }
//            log.info("重脱敏逻辑实现");
//            ResponseEntity<byte[]> wrongResponseEntity = fileService.dealExcel(fileStorageDetails2, wrongStrategyConfigString,
//                    wrongStrategy, false);
//            log.info("正在使用脱敏策略进行脱敏");
//            ResponseEntity<byte[]> mediumResponseEntity = fileService.dealExcel(fileStorageDetails2, mediumStrategyConfigString,
//                    mediumStrategy, false);
//            log.info("正在使用高脱敏策略进行脱敏");
//            ResponseEntity<byte[]> highResponseEntity = fileService.dealExcel(fileStorageDetails3, highStrategyConfigString,
//                    highStrategy, false);
//            log.info("LowResponseEntity Status Code: {}", lowResponseEntity.getStatusCode());
            if (responseEntity == null) {
                return new Result<>(500, "Return response is null", "");
            }
            log.info("ResponseEntity Status Code: {}", responseEntity.getStatusCode());
//            log.info("HighResponseEntity Status Code: {}", highResponseEntity.getStatusCode());

            if (
                    responseEntity.getStatusCode() != HttpStatus.OK
            ) {
                // 如果有任意一个需求未完成，则返回异常结果
                return new Result<>(500, "Failed to complete all desen tasks", "");
            }
        } catch (Exception e) {
            log.error("Failed to desen excel: {}", e.getMessage());
            return new Result<>(500, "Failed to desen excel", "");
        }

        return new Result<>(200, "Success", "");
    }
//        try {
//            log.info("正在使用低脱敏策略进行脱敏");
//            FileStorageDetails finalFileStorageDetails = fileStorageDetails1;
//            CompletableFuture<ResponseEntity<byte[]>> lowFuture = CompletableFuture.supplyAsync(() -> {
//                try {
//                    return fileService.dealExcel(finalFileStorageDetails, lowStrategyConfigString, lowStrategy, false);
//                } catch (Exception e) {
//                    throw new RuntimeException("Low strategy failed", e);
//                }
//            }).exceptionally(e -> {
//                log.error("Low strategy failed", e);
//                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//            });
//
//            log.info("正在使用中脱敏策略进行脱敏");
//            FileStorageDetails finalFileStorageDetails1 = fileStorageDetails2;
//            CompletableFuture<ResponseEntity<byte[]>> mediumFuture = CompletableFuture.supplyAsync(() -> {
//                try {
//                    return fileService.dealExcel(finalFileStorageDetails1, mediumStrategyConfigString, mediumStrategy, false);
//                } catch (Exception e) {
//                    throw new RuntimeException("Medium strategy failed", e);
//                }
//            }).exceptionally(e -> {
//                log.error("Medium strategy failed", e);
//                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//            });
//
//            log.info("正在使用高脱敏策略进行脱敏");
//            FileStorageDetails finalFileStorageDetails2 = fileStorageDetails3;
//            CompletableFuture<ResponseEntity<byte[]>> highFuture = CompletableFuture.supplyAsync(() -> {
//                try {
//                    return fileService.dealExcel(finalFileStorageDetails2, highStrategyConfigString, highStrategy, false);
//                } catch (Exception e) {
//                    throw new RuntimeException("High strategy failed", e);
//                }
//            }).exceptionally(e -> {
//                log.error("High strategy failed", e);
//                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//            });
//
//            CompletableFuture<Void> allFutures = CompletableFuture.allOf(lowFuture, mediumFuture, highFuture);
//
//            allFutures.join(); // 等待所有任务完成
//
//            ResponseEntity<byte[]> lowResponseEntity = lowFuture.get();
//            ResponseEntity<byte[]> mediumResponseEntity = mediumFuture.get();
//            ResponseEntity<byte[]> highResponseEntity = highFuture.get();
//
//            log.info("LowResponseEntity Status Code: {}", lowResponseEntity.getStatusCode());
//            log.info("MediumResponseEntity Status Code: {}", mediumResponseEntity.getStatusCode());
//            log.info("HighResponseEntity Status Code: {}", highResponseEntity.getStatusCode());
//
//            if (lowResponseEntity.getStatusCode() != HttpStatus.OK ||
//                    mediumResponseEntity.getStatusCode() != HttpStatus.OK ||
//                    highResponseEntity.getStatusCode() != HttpStatus.OK) {
//                // 如果有任意一个需求未完成，则返回异常结果
//                return new Result<>(500, "Failed to complete all desen tasks", "");
//            }
//        } catch (Exception e) {
//            log.error("Failed to desen excel: {}", e.getMessage());
//            return new Result<>(500, "Failed to desen excel", "");
//        }
//        return new Result<>(200, "Success", "");
//    }
}

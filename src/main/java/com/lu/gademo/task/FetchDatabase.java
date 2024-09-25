package com.lu.gademo.task;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lu.gademo.entity.ClassificationResult;
import com.lu.gademo.entity.ExcelParam;
import com.lu.gademo.entity.FileStorageDetails;
import com.lu.gademo.entity.dataplatform.SadaGdpiClickDtl;
import com.lu.gademo.service.ExcelParamService;
import com.lu.gademo.service.FileService;
import com.lu.gademo.service.impl.DataPlatformDesenServiceImpl;
import com.lu.gademo.service.impl.FileStorageService;
import com.lu.gademo.utils.LogCollectUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.sshd.client.SshClient;
import org.apache.sshd.client.keyverifier.AcceptAllServerKeyVerifier;
import org.apache.sshd.client.session.ClientSession;
import org.apache.sshd.scp.client.ScpClient;
import org.apache.sshd.scp.client.ScpClientCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@ConditionalOnProperty(name = "fetch.database.task.enabled", havingValue = "true", matchIfMissing = false)
public class FetchDatabase {
    @Autowired
    private DataPlatformDesenServiceImpl dataPlatformDesenService;
    @Autowired
    private FileService fileService;
    @Autowired
    private FileStorageService fileStorageService;
    @Autowired
    private ExcelParamService excelParamService;
    @Autowired
    private LogCollectUtil logCollectUtil;

    // 远程服务器信息
    @Value("${course2.host}")
    String remoteHost; // 远程服务器地址
    @Value("${course2.username}")
    String username;
    @Value("${course2.password}")// SSH 用户名
    String password;
    @Value("${course2.remotePath}")// SSH 密码
    String remoteFilePath;  // 远程文件路径
    Path localFilePath = Paths.get("dataplatform_config.json");           // 本地存储路径

    private ObjectMapper objectMapper = new ObjectMapper();

    @Scheduled(initialDelayString = "${fetch.database.task.initialDelay}", fixedRateString = "${fetch.database.task.fixedRate}")
    public boolean fetchDatabaseAndDesen() throws IOException, IllegalAccessException {
        log.info("定时任务：拉取电信数据平台数据库数据并脱敏");

        // 检查本地是否已经存在文件
        if (Files.exists(localFilePath)) {
            log.info("分类分级文件 {} 已存在", localFilePath);
        } else {
            // 使用 SCP 从远程服务器下载文件
            log.info("分类分级文件不存在，开始从课题二远程服务器下载分类分级文件...");
            downloadFileFromRemote();
        }
        Scanner scanner = new Scanner(System.in);
        System.out.println("Press Enter to continue...");
        scanner.nextLine();

        String sheetName = "sada_gdpi_click_dtl";
        // 1. 从目标数据库获取数据
        List<SadaGdpiClickDtl> allRecordsByTableName = dataPlatformDesenService.getAllRecordsByTableName(sheetName);
        log.info("已从电信数据平台数据库获取数据");
        // 2. 对数据进行脱敏处理并写入文件
        Path tempFilePath = Paths.get(sheetName + "_temp.xlsx");
        dataPlatformDesenService.writeToExcel(allRecordsByTableName, dataPlatformDesenService.getColumnMapping(), tempFilePath);
        System.out.println("Press Enter to continue...");
        scanner.nextLine();
        FileStorageDetails fileStorageDetails1 = null;
        FileStorageDetails fileStorageDetails2 = null;
        FileStorageDetails fileStorageDetails3 = null;
//        int strategyInt = Integer.parseInt(algName);
//        String sheetNameWithStrategy = "";

//        String lowStrategy = sheetName + "_low";
        String mediumStrategy = sheetName + "_medium";
//        String highStrategy = sheetName + "_high";

        log.info("正在读取课程二脱敏策略");
        Map<String, Integer> courseTwoMap = readFromCourseTwo();

//        List<ExcelParam> lowStrategyConfig = excelParamService.getParamsByTableName(lowStrategy + "_param");
        List<ExcelParam> mediumStrategyConfig = excelParamService.getParamsByTableName(mediumStrategy + "_param");
//        List<ExcelParam> highStrategyConfig = excelParamService.getParamsByTableName(highStrategy + "_param");
//        log.info("正在更新低脱敏策略");
//        updateExcelParam(courseTwoMap, lowStrategyConfig);
        log.info("正在更新脱敏策略");
        updateExcelParam(courseTwoMap, mediumStrategyConfig);
        System.out.println("Press Enter to continue...");
        scanner.nextLine();
//        log.info("正在更新高脱敏策略");
//        updateExcelParam(courseTwoMap, highStrategyConfig);

//        String lowStrategyConfigString = objectMapper.writeValueAsString(lowStrategyConfig);
        String mediumStrategyConfigString = objectMapper.writeValueAsString(mediumStrategyConfig);
//        String highStrategyConfigString = objectMapper.writeValueAsString(highStrategyConfig);

        try {
//            fileStorageDetails1 = fileStorageService.saveRawFileWithDesenInfo(tempFilePath);
            fileStorageDetails2 = fileStorageService.saveRawFileWithDesenInfo(tempFilePath);
//            fileStorageDetails3 = fileStorageService.saveRawFileWithDesenInfo(tempFilePath);
        } catch (IOException e) {
            if (Files.exists(tempFilePath)) {
                Files.delete(tempFilePath);
            }
            log.error("Failed to save raw file: {}", e.getMessage());
//            return new Result<>(500, "Failed to save file", "");
            return false;
        }
        try {
//            log.info("正在使用低脱敏策略进行脱敏");
//            ResponseEntity<byte[]> lowResponseEntity = fileService.dealExcel(fileStorageDetails1, lowStrategyConfigString,
//                    lowStrategy, false);
            log.info("正在使用脱敏策略进行脱敏");
            ResponseEntity<byte[]> mediumResponseEntity = fileService.dealExcel(fileStorageDetails2, mediumStrategyConfigString,
                    mediumStrategy, false);
//            log.info("正在使用高脱敏策略进行脱敏");
//            ResponseEntity<byte[]> highResponseEntity = fileService.dealExcel(fileStorageDetails3, highStrategyConfigString,
//                    highStrategy, false);
//            log.info("LowResponseEntity Status Code: {}", lowResponseEntity.getStatusCode());
            log.info("ResponseEntity Status Code: {}", mediumResponseEntity.getStatusCode());
//            log.info("HighResponseEntity Status Code: {}", highResponseEntity.getStatusCode());

            if (
                    mediumResponseEntity.getStatusCode() != HttpStatus.OK
            ) {
                // 如果有任意一个需求未完成，则返回异常结果
//                return new Result<>(500, "Failed to complete all desen tasks", "");
                return false;
            }
        } catch (Exception e) {
            log.error("Failed to desen excel: {}", e.getMessage());
//            return new Result<>(500, "Failed to desen excel", "");
            if (Files.exists(tempFilePath)) {
                Files.delete(tempFilePath);
            }
            return false;
        }
        if (Files.exists(tempFilePath)) {
            Files.delete(tempFilePath);
        }
//        return new Result<>(200, "Success", "");
        return true;

    }

    private Map<String, Integer> readFromCourseTwo() throws IOException {
        Path path = Paths.get("./dataplatform_config.json");
        JsonNode columnList = objectMapper.readTree(path.toFile()).get("columnList");
        List<ClassificationResult> courseTwoList = objectMapper.readValue(columnList.toString(), new TypeReference<List<ClassificationResult>>() {
        });
        Map<String, Integer> courseTwoMap = new HashMap<>();

        for (ClassificationResult item : courseTwoList) {
            courseTwoMap.put("f_" + item.getColumnName(), item.getColumnLevel());
        }
        return courseTwoMap;


    }

    private void updateExcelParam(Map<String, Integer> courseTwoMap, List<ExcelParam> strategyConfig) {
        for (ExcelParam item : strategyConfig) {
            if (item.getColumnName().equals("sid")) {
                continue;
            }
            Integer courseTwoMapTmParam = courseTwoMap.get(item.getColumnName());
            System.out.println(courseTwoMapTmParam);
            if (courseTwoMapTmParam == 4) {
                courseTwoMapTmParam = 3;
            }
            item.setTmParam(courseTwoMapTmParam > item.getTmParam() ? courseTwoMapTmParam : item.getTmParam());
        }
    }

//    private void downloadFileFromRemote(String remoteHost, String username, String password, String remoteFilePath, String localFilePath) {
//        JSch jsch = new JSch();
//        Session session = null;
//        ChannelExec channel = null;
//        try {
//            session = jsch.getSession(username, remoteHost, 22);
//            session.setPassword(password);
//            session.setConfig("StrictHostKeyChecking", "no");
//            session.connect();
//
//            String command = "scp -f " + remoteFilePath;
//            channel = (ChannelExec) session.openChannel("exec");
//            channel.setCommand(command);
//
//            InputStream inputStream = channel.getInputStream();
//            FileOutputStream fos = new FileOutputStream(localFilePath);
//            channel.connect();
//
//            byte[] buffer = new byte[6 * 1024];
//            int len;
//            while ((len = inputStream.read(buffer)) != -1) {
//                fos.write(buffer, 0, len);
//            }
//            fos.close();
//            log.info("配置文件 {} 下载完成。", localFilePath);
//        } catch (Exception e) {
//            log.error("下载配置文件时出错: ", e);
//        } finally {
//            if (channel != null) {
//                channel.disconnect();
//            }
//            if (session != null) {
//                session.disconnect();
//            }
//        }
//    }

    private void downloadFileFromRemote() throws IOException {
        SshClient client = SshClient.setUpDefaultClient();

        // 跳过主机密钥检查
        client.setServerKeyVerifier(AcceptAllServerKeyVerifier.INSTANCE);
        client.start();

        try (ClientSession session = client.connect(username, remoteHost, 22)
                .verify(7, TimeUnit.SECONDS).getSession()) {

            // 添加密码认证
            session.addPasswordIdentity(password);
            session.auth().verify(5, TimeUnit.SECONDS);

            // 创建 SCPClient
            ScpClientCreator creator = ScpClientCreator.instance();
            ScpClient scpClient = creator.createScpClient(session);

            // 下载文件
            scpClient.download(remoteFilePath, localFilePath);
            log.info("远程分类分级文件下载成功: {}", localFilePath.toString());

        } catch (Exception e) {
            log.error("SCP 文件传输过程中出现错误: ", e);
            throw new IOException("SCP 文件下载失败", e);
        } finally {
            client.stop();
        }
    }
}

package com.lu.gademo.controller;

import com.lu.gademo.model.LogSenderManager;
import com.lu.gademo.service.impl.FileStorageService;
import com.lu.gademo.timeSeries.MainTest;
import com.lu.gademo.trace.client.common.Vertex;
import com.lu.gademo.trace.client.user.Customer;
import com.lu.gademo.trace.client.user.Driver;
import com.lu.gademo.trace.client.util.CoordinateConversion;
import com.lu.gademo.trace.client.util.MapUtils;
import com.lu.gademo.trace.server.gui.ServerMain;
import com.lu.gademo.utils.DesenInfoStringBuilders;
import com.lu.gademo.utils.Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.crypto.Cipher;
import java.io.*;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.*;

import static com.lu.gademo.utils.VideoUtil.videoAESEncOrDec;

@Slf4j
@Controller
@RequestMapping("/Encrypt")
public class EncryptController {
    @Autowired
    private Customer customer;
    @Autowired
    private Driver driver0;
    @Autowired
    @Qualifier("driver1")
    private Driver driver1;
    @Autowired
    private ServerMain server = new ServerMain();
    @Autowired
    private Util util;
    @Autowired
    private LogSenderManager logSenderManager;

    private final double minLat = 34.14;
    private final double maxLat = 34.43;
    private final double minLon = 108.77;
    private final double maxLon = 109.12;
    private final int numberOfPoints = 500000;

    private static final String symbol1 = "%%";//分隔符1
    private static final String symbol2 = "&&";//分隔符2
    private final String symbol3 = "@@";//分隔符3
    private final int k3 = 75;

    private double[][] randomPoints = new double[numberOfPoints][2];
    private Random randomNum = new Random();

    // graph的非失真算法
    @ResponseBody
    @RequestMapping(value = "/desenGraph", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ServerResponse<Map<String, String>> desenGraph(@RequestParam String rawData) throws Exception {
        Boolean desenCom = true;
        DesenInfoStringBuilders infoBuilders = new DesenInfoStringBuilders();
        String objectMode = "graph";
        // 设置文件时间戳
        String fileTimeStamp = String.valueOf(System.currentTimeMillis());
        // 设置原文件信息

        // 设置原文件保存路径
        String rawFileName = fileTimeStamp + "UserInput";
        String rawFileSuffix = "plaintext";
        byte[] rawFileBytes = rawData.getBytes();
        Long rawFileSize = (long) rawFileBytes.length;
        String result = "";
        // 调用视频加密方法
        String startTime = util.getTime();
        try {
            result = MainTest.encryptGraph(rawData);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ServerResponse<>("error", null);
        }
        String endTime = util.getTime();
        // 准备返回文件
        String globalID = System.currentTimeMillis() + randomNum.nextInt() + "脱敏工具集";

        infoBuilders.desenAlg.append("106");
        infoBuilders.desenAlgParam.append("非失真图形脱敏算法");
        infoBuilders.desenLevel.append(0);
        // 脱敏前类型
        infoBuilders.desenInfoPreIden.append("graph");
        // 脱敏后类型
        infoBuilders.desenInfoAfterIden.append("graph");
        // 脱敏意图
        infoBuilders.desenIntention.append("对图形非失真脱敏");
        // 脱敏要求
        infoBuilders.desenRequirements.append("对图形非失真脱敏");
        // 脱敏数据类型
        infoBuilders.fileDataType.append(rawFileSuffix);

        String evidenceID = util.getSM3Hash((new String(rawFileBytes, StandardCharsets.UTF_8) + util.getTime()).getBytes());
        logSenderManager.submitToFourSystems(globalID, evidenceID, desenCom, objectMode, infoBuilders, rawFileName,
                rawFileBytes, rawFileSize, rawFileName, rawFileBytes, rawFileSize, objectMode, rawFileSuffix,
                startTime, endTime);
        Map<String, String> sendResult = new HashMap<>();
        sendResult.put("result", result);
        sendResult.put("kdTree", MainTest.encryptedKDTreeString.toString());
        return new ServerResponse<>("ok", sendResult);
    }

    @ResponseBody
    @RequestMapping(value = "/customerSetStartCoordinate", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public ServerResponse<String> customerSetStartCoordinate(@RequestParam String startLatitude, @RequestParam String startLongitude) throws Exception {
        try {
            customer.setStartLatitude(Double.parseDouble(startLatitude));
            customer.setStartLongitude(Double.parseDouble(startLongitude));
            System.out.println("Start: " + customer.getStartLatitude() + " " + customer.getStartLongitude());
            return new ServerResponse<>("ok", "");
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ServerResponse<>("error", "");
        }

    }

    @ResponseBody
    @RequestMapping(value = "/customerSetEndCoordinate", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public ServerResponse<String> customerSetEndCoordinate(@RequestParam String endLatitude, @RequestParam String endLongitude) throws Exception {
        try {
            customer.setEndLatitude(Double.parseDouble(endLatitude));
            customer.setEndLongitude(Double.parseDouble(endLongitude));
            System.out.println("Destination: " + customer.getEndLatitude() + " " + customer.getEndLongitude());
            customer.destInfoStr = "纬度：" + customer.getEndLatitude() + " 经度：" + customer.getEndLongitude();
            return new ServerResponse<>("ok", "");
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ServerResponse<>("error", "");
        }

    }

    @ResponseBody
    @RequestMapping(value = "/traceCustomerLogin", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public ServerResponse<String> traceCustomerLogin() throws Exception {
        try {
            customer.login();
            String encryptedMapData = customer.sendEncryptedMapData(customer.getStartLatitude(), customer.getEndLongitude());
            customer.sendSquareAndCircleData();

            Boolean desenCom = true;
            DesenInfoStringBuilders infoBuilders = new DesenInfoStringBuilders();
            String objectMode = "graph";
            // 设置文件时间戳
            String fileTimeStamp = String.valueOf(System.currentTimeMillis());
            // 设置原文件信息

            // 设置原文件保存路径
            String rawFileName = fileTimeStamp + "UserInput";
            String rawFileSuffix = "plaintext";
            // 使用乘客的起点和终点坐标作为原始数据
            String rawData = String.valueOf(customer.getStartLongitude()) + String.valueOf(customer.getStartLatitude()) + String.valueOf(customer.getEndLongitude()) + String.valueOf(customer.getEndLatitude());
            byte[] rawFileBytes = rawData.getBytes();
            Long rawFileSize = (long) rawFileBytes.length;

            // 调用视频加密方法
            String startTime = util.getTime();
//            String result = MainTest.encryptGraph(rawData);
            String endTime = util.getTime();
            // 准备返回文件
            String globalID = System.currentTimeMillis() + randomNum.nextInt() + "脱敏工具集";

            infoBuilders.desenAlg.append("101");
            infoBuilders.desenAlgParam.append("非失真文本脱敏算法");
            infoBuilders.desenLevel.append(0);
            // 脱敏前类型
            infoBuilders.desenInfoPreIden.append("text");
            // 脱敏后类型
            infoBuilders.desenInfoAfterIden.append("text");
            // 脱敏意图
            infoBuilders.desenIntention.append("对文本非失真脱敏");
            // 脱敏要求
            infoBuilders.desenRequirements.append("对文本非失真脱敏");
            // 脱敏数据类型
            infoBuilders.fileDataType.append(rawFileSuffix);

            String evidenceID = util.getSM3Hash((new String(rawFileBytes, StandardCharsets.UTF_8) + util.getTime()).getBytes());
            logSenderManager.submitToFourSystems(globalID, evidenceID, desenCom, objectMode, infoBuilders, rawFileName,
                    rawFileBytes, rawFileSize, rawFileName, rawFileBytes, rawFileSize, objectMode, rawFileSuffix,
                    startTime, endTime);

//            customer.connThread.join();
            return new ServerResponse<>("ok", encryptedMapData);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ServerResponse<>("error");
        }

    }

    @ResponseBody
    @RequestMapping(value = "/traceDriver1Login", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public ServerResponse<String> traceDriver1Login(@RequestParam String startLatitude, @RequestParam String startLongitude) throws Exception {
        System.out.println("Driver1: " + startLatitude + " " + startLongitude);

        driver0.setStartLatitude(Double.parseDouble(startLatitude));
        driver0.setStartLongitude(Double.parseDouble(startLongitude));
        try {
            driver0.login();
            driver0.sendEncryptedMapData(driver0.getStartLatitude(), driver0.getStartLongitude());
//            driver1.connThread.join();
            return new ServerResponse<>("ok", "");
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ServerResponse<>("error", "");
        }
    }

    @ResponseBody
    @RequestMapping(value = "/traceDriver2Login", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public ServerResponse<String> traceDriver2Login(@RequestParam String startLatitude, @RequestParam String startLongitude) throws Exception {
        System.out.println("Driver2: " + startLatitude + " " + startLongitude);
        driver1.setStartLatitude(Double.parseDouble(startLatitude));
        driver1.setStartLongitude(Double.parseDouble(startLongitude));
        try {
            driver1.login();
            driver1.sendEncryptedMapData(driver1.getStartLatitude(), driver1.getStartLongitude());
//            driver2.connThread.join();
            return new ServerResponse<>("ok");
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ServerResponse<>("error");
        }
    }

    @ResponseBody
    @RequestMapping(value = "/serverStart", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public ServerResponse<String> serverStart() {
        try {
            server.start();
            // template.convertAndSend("/topic/serverLog", new ServerResponse("running"));
            return new ServerResponse<>("ok", "");
        } catch (Exception e) {
            // template.convertAndSend("/topic/serverLog", new ServerResponse("error"));
            log.error(e.getMessage());
            return new ServerResponse<>("error", "");
        }
    }

    @ResponseBody
    @RequestMapping(value = "/serverStop", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public ServerResponse<String> serverStop() {
        try {
            server.stop();
            // template.convertAndSend("/topic/serverLog", new ServerResponse("stop"));
            return new ServerResponse<>("ok", "");
        } catch (Exception e) {
            // template.convertAndSend("/topic/serverLog", new ServerResponse("error"));
            log.error(e.getMessage());
            return new ServerResponse<>("error", "");
        }
    }

    @ResponseBody
    @RequestMapping(value = "/customerStop", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public ServerResponse<String> customerStop() {
        customer.IS_ORDER_ACCEPTED = false;
        try {
            customer.close();
            // template.convertAndSend("/topic/serverLog", new ServerResponse("stop"));
            return new ServerResponse<>("ok", "");
        } catch (Exception e) {
            // template.convertAndSend("/topic/serverLog", new ServerResponse("error"));
            log.error(e.getMessage());
            return new ServerResponse<>("error", "");
        }

    }

    @ResponseBody
    @RequestMapping(value = "/driver1Stop", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public ServerResponse<String> driver1Stop() {
        try {
            driver0.close();
            // template.convertAndSend("/topic/serverLog", new ServerResponse("stop"));
            return new ServerResponse<>("ok", "");
        } catch (Exception e) {
            // template.convertAndSend("/topic/serverLog", new ServerResponse("error"));
            log.error(e.getMessage());
            return new ServerResponse<>("error", "");
        }
    }

    @ResponseBody
    @RequestMapping(value = "/driver2Stop", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public ServerResponse<String> driver2Stop() {
        try {
            driver1.close();
            // template.convertAndSend("/topic/serverLog", new ServerResponse("stop"));
            return new ServerResponse<>("ok", "");
        } catch (Exception e) {
            // template.convertAndSend("/topic/serverLog", new ServerResponse("error"));
            log.error(e.getMessage());
            return new ServerResponse<>("error", "");
        }
    }

    @ResponseBody
    @RequestMapping(value = "/orderResult", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public ServerResponse<String> orderResult() {
        if (customer.IS_ORDER_ACCEPTED) {
            String driverName = customer.driverList.get(0).getUserName();
            log.info(driverName);
            if (driverName.contains("driver0")) {
                return new ServerResponse<>("ok", "司机：" + "driver1" + "接单了");
            } else if (driverName.equals("driver1")) {
                return new ServerResponse<>("ok", "司机：" + "driver2" + "接单了");
            } else {
                return new ServerResponse<>("error", "未知司机");
            }
        }
        return new ServerResponse<>("error", "没有司机接单");
    }

    @ResponseBody
    @GetMapping(value = "/generateTestData", produces = "application/json;charset=UTF-8")
    public ServerResponse<String> generateTestData() {
        generateRandomGeoPoints(minLat, maxLat, minLon, maxLon, numberOfPoints);
        StringBuilder sb = new StringBuilder();
        for (double[] randomPoint : randomPoints) {
            sb.append(randomPoint[0]).append(" ").append(randomPoint[1]).append("\n");
        }
        return new ServerResponse<>("ok", sb.toString());
    }

    private void generateRandomGeoPoints(double minLat, double maxLat, double minLon, double maxLon, int numberOfPoints) {
        Random random = new Random();

        for (int i = 0; i < numberOfPoints; i++) {
            double latitude = minLat + (maxLat - minLat) * random.nextDouble();
            double longitude = minLon + (maxLon - minLon) * random.nextDouble();
            randomPoints[i][0] = latitude;
            randomPoints[i][1] = longitude;
        }

//        return randomPoints;
    }

    @ResponseBody
    @PostMapping(value = "/performenceTest",
            consumes = "application/json;charset=UTF-8")
    public ResponseEntity<Resource> performenceTest(@RequestBody double[][] points) throws IOException {
        log.info("开始非失真类文本算法性能测试");
        Path desenFilePath = Paths.get("desen_files");
        MapUtils mapUtils = new MapUtils();
        List<String> result = new ArrayList<>();
        CoordinateConversion coor = new CoordinateConversion();
        String[] mapDatum = MapUtils.mapData;
        int numThreads = Runtime.getRuntime().availableProcessors();
        // Create a thread pool with the number of available processors
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        List<Future<String>> futures = new ArrayList<>();
        if (points.length == 0) {
            return ResponseEntity.badRequest().body(null);
        }
        long startTimePoint = System.nanoTime();
        System.out.println("points.length = " + points.length);
        for (int i = 0; i < points.length; i++) {
            String coorResult = coor.latLon2UTM(points[i][0], points[i][1]);
            String[] coorResults = coorResult.split(" ");
            Vertex vertex = new Vertex(Long.parseLong(coorResults[2]), Long.parseLong(coorResults[3]));
            Callable<String> task = getStringCallable(i, mapDatum, vertex);
            futures.add(executor.submit(task));
        }
        // 处理脱敏任务，记录时间
        for (Future<String> future : futures) {
            try {
                result.add(future.get()); // Retrieve the result of the task
                // 这里进行脱敏处理的时间统计
            } catch (InterruptedException | ExecutionException e) {
                log.error("Error: " + e.getMessage());
            }
        }
        futures.clear();
        long endTimePoint = System.nanoTime();
        double totalTime = (endTimePoint - startTimePoint) / 1e6;
        log.info("脱敏完成，共耗时：" + Double.toString(totalTime) + "ms");
        log.info("单条脱敏记录耗时：" + Double.toString(totalTime / points.length) + "ms");
        log.info("每秒钟脱敏可脱敏数据：{}", 1000 / (totalTime / points.length) + "条");
        // Shut down the executor
        executor.shutdown();
        try {
            if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
        }
        String timeString = "脱敏" + points.length + "条数据用时" + totalTime + " ms\n";
        String fileName = System.currentTimeMillis() + "test_result.txt";
        try (OutputStreamWriter fileWriter = new OutputStreamWriter(Files.newOutputStream(desenFilePath.resolve(fileName)))) {
            fileWriter.write(timeString);
            for(String s : result) {
                fileWriter.write(s);
                fileWriter.write("\n");
            }
        } catch (IOException e) {
            return ResponseEntity.badRequest().body(null);
        }
        result.clear();
        result = null;
        System.gc();
        // 准备返回文件
        Path tempFile = desenFilePath.resolve(fileName);
        log.info("脱敏后文件写入完成");
        points = null;
        System.gc();
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);
//        return ResponseEntity.ok()
//                .headers(headers)
//                .contentLength(tempFile.length())
//                .body(Files.readAllBytes(tempFile.toPath()));
        InputStream inputStream = Files.newInputStream(tempFile);
        InputStreamResource resource = new InputStreamResource(inputStream);

        //        try (InputStreamResource resource = new InputStreamResource(Files.newInputStream(tempFile.toPath()))) {
//            return ResponseEntity.ok()
//                    .headers(headers)
//                    .contentLength(tempFile.length())
//                    .body(resource);
//        } catch (IOException e) {
//            log.error("Error reading file: " + e.getMessage());
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
//        }
        return ResponseEntity.ok()
                .headers(headers)
                .body(resource);
    }

    private String simpilifiedOfAlgo(String message, Vertex UF_j) throws Exception {
        BigInteger x_j = BigInteger.valueOf(UF_j.getX());
        BigInteger y_j = BigInteger.valueOf(UF_j.getY());
        String[] messages = message.trim().split(symbol2);//以symbol2分开
        BigInteger alpha_QU = new BigInteger(messages[0]);
        BigInteger p_QU = new BigInteger(messages[1]);
        // EN
        String C_QUStr = messages[2];
        // EN_i1 - EN_i4

        String[] C_iStr = C_QUStr.trim().split(symbol3);
        String[] C_inStr = null;
        BigInteger[] D_in = new BigInteger[2];
        StringBuilder DSb = new StringBuilder();
        String D = null;
        String D_i = null;
        System.out.println("C_iStr.length = " + C_iStr.length);
        for (int i = 0; i < C_iStr.length; i++) {
            // EN_ij1-EN_ij6
            C_inStr = C_iStr[i].trim().split(symbol1);
            if (C_inStr.length != 6) {
                System.out.println("UF_AGRQ_P_RDC_Error C_in的长度不为6");
                return null;
            }
            BigInteger[] C_in = new BigInteger[6];
            for (int a = 0; a < 6; a++) {
                C_in[a] = new BigInteger(C_inStr[a]);
            }
            BigInteger r_i = new BigInteger(75, new Random(System.currentTimeMillis()));
            D_in[0] = x_j.multiply(C_in[3]).add(y_j.
                    multiply(C_in[0])).add(C_in[5]).multiply(alpha_QU).multiply(r_i).mod(p_QU);//question
            D_in[1] = x_j.multiply(C_in[1]).add(y_j.
                    multiply(C_in[2])).add(C_in[4]).multiply(alpha_QU).multiply(r_i).mod(p_QU);
            D_i = D_in[0] + symbol1 + D_in[1];//D_in 之间以symbol1分隔
            DSb.append(D_i + symbol3);//D_i 之间以symbol3分隔
        }
        String Dtemp = DSb.toString();
        D = Dtemp.substring(0, Dtemp.lastIndexOf(symbol3));
        return D;
    }

    private Callable<String> getStringCallable(int i, String[] mapDatum, Vertex vertex) {
        return () -> {
            StringBuffer stringBuffer = new StringBuffer();
            try {
                // 与地图块进行计算
                stringBuffer.append(simpilifiedOfAlgo(mapDatum[0], vertex)).append(";");
            } catch (Exception e) {
                log.error("EncryptedCalculatedData: " + "数据处理出错！");
                log.error(e.getMessage());
            }
            return stringBuffer.toString();
        };
    }

    @PostMapping(value = "/aesVideoEnc")
    public ResponseEntity<byte[]> aesVideoEnc(@RequestPart("file") MultipartFile file,
                                              @RequestParam("password") String password
    ) {
        try {
            Path rawFileDirectory = Paths.get("raw_files");
            Path desenFileDirectory = Paths.get("desen_files");
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
            // 调用视频加密方法
            String startTime = util.getTime();
            videoAESEncOrDec(rawFilePathString, desenFilePathString, password, Cipher.ENCRYPT_MODE);
            String endTime = util.getTime();
            // 准备返回文件
            String globalID = System.currentTimeMillis() + randomNum.nextInt() + "脱敏工具集";
            // 脱敏后文件字节流
            byte[] desenFileBytes = Files.readAllBytes(desenFilePath.toAbsolutePath());
            Long desenFileSize = Files.size(desenFilePath.toAbsolutePath());
            infoBuilders.desenAlg.append("104");
            infoBuilders.desenAlgParam.append("非失真视频脱敏算法");
            infoBuilders.desenLevel.append(0);
            // 脱敏前类型
            infoBuilders.desenInfoPreIden.append("video");
            // 脱敏后类型
            infoBuilders.desenInfoAfterIden.append("video");
            // 脱敏意图
            infoBuilders.desenIntention.append("对视频非失真脱敏");
            // 脱敏要求
            infoBuilders.desenRequirements.append("对视频非失真脱敏");
            // 脱敏数据类型
            infoBuilders.fileDataType.append(rawFileSuffix);
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + desenFileName);
            String evidenceID = util.getSM3Hash((new String(desenFileBytes, StandardCharsets.UTF_8) + util.getTime()).getBytes());
            logSenderManager.submitToFourSystems(globalID, evidenceID, desenCom, objectMode, infoBuilders, rawFileName,
                    rawFileBytes, rawFileSize, desenFileName, desenFileBytes, desenFileSize, objectMode, rawFileSuffix,
                    startTime, endTime);
            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(desenFileSize)
                    .body(desenFileBytes);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage().getBytes());
        }
    }

    @PostMapping(value = "/aesVideoDec")
    public ResponseEntity<byte[]> aesVideoDec(@RequestPart("file") MultipartFile file,
                                              @RequestParam("password") String password
    ) {
        try {
            Path rawFileDirectory = Paths.get("raw_files");
            Path desenFileDirectory = Paths.get("desen_files");
            // 设置文件时间戳
            String fileTimeStamp = String.valueOf(System.currentTimeMillis());
            // 设置原文件保存路径
            String rawFileName = fileTimeStamp + file.getOriginalFilename();
            Path rawFilePath = rawFileDirectory.resolve(rawFileName);
            String rawFilePathString = rawFilePath.toAbsolutePath().toString();

            // 保存源文件
            file.transferTo(rawFilePath.toAbsolutePath());
            // 设置脱敏后文件路径信息
            String desenFileName = "desen_" + rawFileName;
            Path desenFilePath = desenFileDirectory.resolve(desenFileName);
            String desenFilePathString = desenFilePath.toAbsolutePath().toString();
            // 调用视频加密方法

            videoAESEncOrDec(rawFilePathString, desenFilePathString, password, Cipher.DECRYPT_MODE);
            // 准备返回文件
            File tempFile = desenFilePath.toFile();
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + desenFileName);
            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(tempFile.length())
                    .body(Files.readAllBytes(tempFile.toPath()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage().getBytes());
        }
    }

}

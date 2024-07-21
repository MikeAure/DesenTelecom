package com.lu.gademo.controller;

import cn.hutool.core.io.resource.InputStreamResource;
import com.lu.gademo.timeSeries.MainTest;
import com.lu.gademo.trace.client.common.Vertex;
import com.lu.gademo.trace.client.user.Customer;
import com.lu.gademo.trace.client.user.Driver;
import com.lu.gademo.trace.client.util.CoordinateConversion;
import com.lu.gademo.trace.client.util.MapUtils;
import com.lu.gademo.trace.server.gui.ServerMain;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.crypto.Cipher;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
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

    private final double minLat = 34.14;
    private final double maxLat = 34.43;
    private final double minLon = 108.77;
    private final double maxLon = 109.12;
    private final int numberOfPoints = 20000;

    private static final String symbol1 = "%%";//分隔符1
    private static final String symbol2 = "&&";//分隔符2
    private final String symbol3 = "@@";//分隔符3
    private final int k3 = 75;

    private double[][] randomPoints = new double[numberOfPoints][2];

    @ResponseBody
    @RequestMapping(value = "/desenGraph", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String desenGraph(@RequestParam String rawData) throws Exception {
        return MainTest.encryptGraph(rawData);
    }

    @ResponseBody
    @RequestMapping(value = "/customerSetStartCoordinate", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public ServerResponse customerSetStartCoordinate(@RequestParam String startLatitude, @RequestParam String startLongitude) throws Exception {
        try {
            customer.setStartLatitude(Double.parseDouble(startLatitude));
            customer.setStartLongitude(Double.parseDouble(startLongitude));
            System.out.println("Start: " + customer.getStartLatitude() + " " + customer.getStartLongitude());
            return new ServerResponse("ok");
        } catch (Exception e) {
            e.printStackTrace();
            return new ServerResponse("error");
        }

    }

    @ResponseBody
    @RequestMapping(value = "/customerSetEndCoordinate", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public ServerResponse customerSetEndCoordinate(@RequestParam String endLatitude, @RequestParam String endLongitude) throws Exception {
        try {
            customer.setEndLatitude(Double.parseDouble(endLatitude));
            customer.setEndLongitude(Double.parseDouble(endLongitude));
            System.out.println("Destination: " + customer.getEndLatitude() + " " + customer.getEndLongitude());
            customer.destInfoStr = "纬度：" + customer.getEndLatitude() + " 经度：" + customer.getEndLongitude();
            return new ServerResponse("ok");
        } catch (Exception e) {
            e.printStackTrace();
            return new ServerResponse("error");
        }

    }

    @ResponseBody
    @RequestMapping(value = "/traceCustomerLogin", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public ServerResponse traceCustomerLogin() throws Exception {

        try {
            customer.login();

            customer.sendEncryptedMapData(customer.getStartLatitude(), customer.getEndLongitude());
            customer.sendSquareAndCircleData();

//            customer.connThread.join();
            return new ServerResponse("ok");
        } catch (Exception e) {
            e.printStackTrace();
            return new ServerResponse("error");
        }

    }

    @ResponseBody
    @RequestMapping(value = "/traceDriver1Login", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public ServerResponse traceDriver1Login(@RequestParam String startLatitude, @RequestParam String startLongitude) throws Exception {
        System.out.println("Driver1: " + startLatitude + " " + startLongitude);

        driver0.setStartLatitude(Double.parseDouble(startLatitude));
        driver0.setStartLongitude(Double.parseDouble(startLongitude));
        try {
            driver0.login();
            driver0.sendEncryptedMapData(driver0.getStartLatitude(), driver0.getStartLongitude());
//            driver1.connThread.join();
            return new ServerResponse("ok");
        } catch (Exception e) {
            e.printStackTrace();
            return new ServerResponse("error");
        }
    }

    @ResponseBody
    @RequestMapping(value = "/traceDriver2Login", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public ServerResponse traceDriver2Login(@RequestParam String startLatitude, @RequestParam String startLongitude) throws Exception {
        System.out.println("Driver2: " + startLatitude + " " + startLongitude);
        driver1.setStartLatitude(Double.parseDouble(startLatitude));
        driver1.setStartLongitude(Double.parseDouble(startLongitude));
        try {
            driver1.login();
            driver1.sendEncryptedMapData(driver1.getStartLatitude(), driver1.getStartLongitude());
//            driver2.connThread.join();
            return new ServerResponse("ok");
        } catch (Exception e) {
            e.printStackTrace();
            return new ServerResponse("error");
        }
    }

    @ResponseBody
    @RequestMapping(value = "/serverStart", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public ServerResponse serverStart() {
        try {
            server.start();
            // template.convertAndSend("/topic/serverLog", new ServerResponse("running"));
            return new ServerResponse("ok");
        } catch (Exception e) {
            // template.convertAndSend("/topic/serverLog", new ServerResponse("error"));
            e.printStackTrace();
            return new ServerResponse("error");
        }
    }

    @ResponseBody
    @RequestMapping(value = "/serverStop", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public ServerResponse serverStop() {
        try {
            server.stop();
            // template.convertAndSend("/topic/serverLog", new ServerResponse("stop"));
            return new ServerResponse("ok");
        } catch (Exception e) {
            // template.convertAndSend("/topic/serverLog", new ServerResponse("error"));
            e.printStackTrace();
            return new ServerResponse("error");
        }
    }

    @ResponseBody
    @RequestMapping(value = "/customerStop", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public ServerResponse customerStop() {
        customer.IS_ORDER_ACCEPTED = false;
        try {
            customer.close();
            // template.convertAndSend("/topic/serverLog", new ServerResponse("stop"));
            return new ServerResponse("ok");
        } catch (Exception e) {
            // template.convertAndSend("/topic/serverLog", new ServerResponse("error"));
            e.printStackTrace();
            return new ServerResponse("error");
        }

    }

    @ResponseBody
    @RequestMapping(value = "/driver1Stop", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public ServerResponse driver1Stop() {
        try {
            driver0.close();
            // template.convertAndSend("/topic/serverLog", new ServerResponse("stop"));
            return new ServerResponse("ok");
        } catch (Exception e) {
            // template.convertAndSend("/topic/serverLog", new ServerResponse("error"));
            e.printStackTrace();
            return new ServerResponse("error");
        }
    }

    @ResponseBody
    @RequestMapping(value = "/driver2Stop", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public ServerResponse driver2Stop() {
        try {
            driver1.close();
            // template.convertAndSend("/topic/serverLog", new ServerResponse("stop"));
            return new ServerResponse("ok");
        } catch (Exception e) {
            // template.convertAndSend("/topic/serverLog", new ServerResponse("error"));
            e.printStackTrace();
            return new ServerResponse("error");
        }
    }

    @ResponseBody
    @RequestMapping(value = "/orderResult", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public ServerResponse orderResult() {
        if (customer.IS_ORDER_ACCEPTED) {
            String driverName = customer.driverList.get(0).getUserName();
            log.info(driverName);
            if (driverName.contains("driver0")) {
                return new ServerResponse("ok", "司机：" + "driver1" + "接单了");
            } else if (driverName.equals("driver1")) {
                return new ServerResponse("ok", "司机：" + "driver2" + "接单了");
            } else {
                return new ServerResponse("error", "未知司机");
            }
        }
        return new ServerResponse("error", "没有司机接单");
    }

    @ResponseBody
    @RequestMapping(value = "/generateTestData", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public ServerResponse generateTestData() {
        generateRandomGeoPoints(minLat, maxLat, minLon, maxLon, numberOfPoints);
        StringBuilder sb = new StringBuilder();
        for (double[] randomPoint : randomPoints) {
            sb.append(randomPoint[0]).append(" ").append(randomPoint[1]).append("\n");
        }
        return new ServerResponse("ok", sb.toString());
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
    public ResponseEntity<byte[]> performenceTest(@RequestBody double[][] points) throws IOException {
        Path desenFilePath = Paths.get("desen_files");
        Map<String, String> returnResult = new HashMap<>();
        StringBuilder resultBuilder = new StringBuilder();
        MapUtils mapUtils = new MapUtils();
        CoordinateConversion coor = new CoordinateConversion();
        String[] mapDatum = MapUtils.mapData;
        int numThreads = Runtime.getRuntime().availableProcessors();
        // Create a thread pool with the number of available processors
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
//        System.out.println(numThreads);
//        System.out.println("points.length: " + points.length);
        List<Future<String>> futures = new ArrayList<>();
        if (points.length == 0) {
            return ResponseEntity.badRequest().body(null);
        }
        long startTimePoint = System.nanoTime();
        for (int i = 0; i < points.length; i++) {
            String coorResult = coor.latLon2UTM(points[i][0], points[i][1]);
            String[] coorResults = coorResult.split(" ");
            Vertex vertex = new Vertex(Long.parseLong(coorResults[2]), Long.parseLong(coorResults[3]));
            Callable<String> task = getStringCallable(i, mapDatum, vertex);
            futures.add(executor.submit(task));
        }

        // Process the results of each task
        for (Future<String> future : futures) {
            try {
                resultBuilder.append(future.get()); // Retrieve the result of the task
                resultBuilder.append("\n");
            } catch (InterruptedException | ExecutionException e) {
                log.error("Error: " + e.getMessage());
            }
        }
        long endTimePoint = System.nanoTime();
        double totalTime = (endTimePoint - startTimePoint) / 10e6;
        log.info("Desensitization finished in " + Double.toString(totalTime) + "ms");
        // Shut down the executor
        executor.shutdown();
        String resultString = "脱敏" + points.length + "条数据用时" + totalTime + " ms\n" + resultBuilder.toString();
        String fileName = System.currentTimeMillis() +"test_result.txt";
        try(FileWriter fileWriter = new FileWriter(desenFilePath.resolve(fileName).toFile())) {
            fileWriter.write(resultString);
        } catch (IOException e) {
            return ResponseEntity.badRequest().body(e.getMessage().getBytes());
        }

        // 准备返回文件
        File tempFile = desenFilePath.resolve(fileName).toFile();
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=result.txt");
        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(tempFile.length())
                .body(Files.readAllBytes(tempFile.toPath()));
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
        final int taskId = i;
        return () -> {
            StringBuffer stringBuffer = new StringBuffer();
//            System.out.println("Current running: " + taskId);

            try {
                stringBuffer.append(simpilifiedOfAlgo(mapDatum[0], vertex)).append(";");
            } catch (Exception e) {
                System.out.println("EncryptedCaledData: " + "数据处理出错！");
                System.out.println(e.getMessage());
            }


            return stringBuffer.toString();
        };
    }

    @PostMapping(value = "/aesVideoEnc")
    public ResponseEntity<byte[]> aesVideoEnc(@RequestPart("file") MultipartFile file,
                                            @RequestParam("password") String password
    ) {
        try {
            Path currentDirectory = Paths.get("");
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

            videoAESEncOrDec(rawFilePathString, desenFilePathString, password, Cipher.ENCRYPT_MODE);
            // 准备返回文件
            File tempFile = desenFilePath.toFile();
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + desenFileName);
            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(tempFile.length())
                    .body(Files.readAllBytes(tempFile.toPath()));
        } catch(Exception e) {
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
        } catch(Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage().getBytes());
        }
    }

}

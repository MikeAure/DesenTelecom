package com.lu.gademo;

import com.lu.gademo.trace.client.common.QU_AGRQ_P;
import com.lu.gademo.trace.client.common.Vertex;
import com.lu.gademo.trace.client.user.Driver;
import com.lu.gademo.trace.client.util.CoordinateConversion;
import com.lu.gademo.trace.client.util.MapUtils;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

public class DriverTest {
    private final Driver driver = new Driver();
    private static final String symbol1 = "%%";//分隔符1
    private static final String symbol2 = "&&";//分隔符2
    private final String symbol3 = "@@";//分隔符3
    private final int[] k = new int[]{512, 160, 75, 75};
    private final String key_QU = "BBF4244CECAAE7FC3F05329D2F279A7B";//会话密钥
    private final String QUID = "1";//用户标识

    private final double minLat = 34.14;
    private final double maxLat = 34.43;
    private final double minLon = 108.77;
    private final double maxLon = 109.12;
    private final int numberOfPoints = 20000;
//    private final BigInteger r_i = BigInteger.ZERO;//数组r

    @Test
    void testEncryptVertex() {
        MapUtils mapUtils = new MapUtils();
        QU_AGRQ_P qu_agrq_p = new QU_AGRQ_P();
        CoordinateConversion coor = new CoordinateConversion();
//        String coorResult = coor.latLon2UTM(driver.getStartLatitude(), driver.getStartLongitude());
//        String[] coorResults = coorResult.split(" ");
//        Vertex vertex = new Vertex(Long.parseLong(coorResults[2]), Long.parseLong(coorResults[3]));
        //这个Vertex是自身的位置，然后需要和84个加密的地图数据进行计算，得到一个mapdata的数据然后用Tmessage封装
        String[] mapDatum = MapUtils.mapData;

        double[][] points = generateRandomGeoPoints(minLat, maxLat, minLon, maxLon, numberOfPoints);

        for (int i = 0; i < points.length; i++) {
            String coorResult = coor.latLon2UTM(points[i][0], points[i][1]);
            String[] coorResults = coorResult.split(" ");
//            if (i % 1000 == 0) {
//                System.out.println("coorResults" + Arrays.toString(coorResults));
//            }
            Vertex vertex = new Vertex(Long.parseLong(coorResults[2]), Long.parseLong(coorResults[3]));
            StringBuffer stringBuffer = new StringBuffer();
//            System.out.println("Current running: " + taskId);
            try {
                stringBuffer.append(simpilifiedOfAlgo(mapDatum[0], vertex)).append(";");
            } catch (Exception e) {
                System.out.println("EncryptedCaledData: " + "数据处理出错！");
                System.out.println(e.getMessage());
            }

//            }
        }
    }

    String simpilifiedOfAlgo(String message, Vertex UF_j) throws Exception {
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
        /*
         * 以下是加密部分
         */
//        long nowTime = System.currentTimeMillis();
//        String H_pre = D + symbol2 + QUID + symbol2 + nowTime;
//        String H = SMUtils.encryptBySm3(H_pre);
//        String H_UF = SMUtils.encryptBySm4(H, key_QU);
//        String sendMessage = D + symbol2 + QUID + symbol2 + nowTime + symbol2 + H_UF;//同字符类型之间以symbol2隔开
//            System.out.println("UF_AGRQ_P_RDC发送数据生成成功：" + sendMessage);
        return D;
    }

    // 随机生成符合要求的2w个点
    public static double[][] generateRandomGeoPoints(double minLat, double maxLat, double minLon, double maxLon, int numberOfPoints) {
        double[][] points = new double[numberOfPoints][2];
        Random random = new Random();

        for (int i = 0; i < numberOfPoints; i++) {
            double latitude = minLat + (maxLat - minLat) * random.nextDouble();
            double longitude = minLon + (maxLon - minLon) * random.nextDouble();
            points[i][0] = latitude;
            points[i][1] = longitude;
        }

        return points;
    }

    @Test
        // 使用多线程的方式对加密坐标进行测试
    void testParallelEncryptVertex() {
        MapUtils mapUtils = new MapUtils();
//        QU_AGRQ_P qu_agrq_p = new QU_AGRQ_P();
        CoordinateConversion coor = new CoordinateConversion();
//        String coorResult = coor.latLon2UTM(driver.getStartLatitude(), driver.getStartLongitude());
//        String[] coorResults = coorResult.split(" ");
//        Vertex vertex = new Vertex(Long.parseLong(coorResults[2]), Long.parseLong(coorResults[3]));
        String[] mapDatum = MapUtils.mapData;

        int numTasks = 20000;
        int numThreads = Runtime.getRuntime().availableProcessors();

        // Create a thread pool with the number of available processors
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        System.out.println(numThreads);
        // List to hold Future objects representing the results of each task

        // Submit tasks to the executor

        for (int j = 0; j < 200; j++) {
            double[][] points = generateRandomGeoPoints(minLat, maxLat, minLon, maxLon, numberOfPoints);
//            System.out.println("points.length: " + points.length);
            List<Future<String>> futures = new ArrayList<>();

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
                    String result = future.get(); // Retrieve the result of the task
//                    System.out.println(result);
                    // Do something with the result if needed
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }
            long endTimePoint = System.nanoTime();
            System.out.println("Total 200 now" + j + " ,Desensitization finished in " + (endTimePoint - startTimePoint) / 10e6 + "ms");

        }

        // Shut down the executor
        executor.shutdown();
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

//    @Test
//    void testBigInteger() {
//        BigInteger x_j = BigInteger.valueOf(UF_j.getX());
//        BigInteger y_j = BigInteger.valueOf(UF_j.getY());
//        BigInteger[] D_in = new BigInteger[2];
//        StringBuilder DSb = new StringBuilder();
//        String D = null;
//        String D_i = null;
//        D_in[0] = x_j.multiply(C_in[3]).add(y_j.
//                multiply(C_in[0])).add(C_in[5]).multiply(alpha_QU).multiply(r_i).mod(p_QU);//question
//        D_in[1] = x_j.multiply(C_in[1]).add(y_j.
//                multiply(C_in[2])).add(C_in[4]).multiply(alpha_QU).multiply(r_i).mod(p_QU);
//        D_i = D_in[0] + symbol1 + D_in[1];//D_in 之间以symbol1分隔
//        DSb.append(D_i + symbol3);//D_i 之间以symbol3分隔
//    }
}

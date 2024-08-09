package com.lu.gademo;

import com.lu.gademo.utils.DSObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.lu.gademo.utils.Dp;
import com.lu.gademo.utils.impl.DpImpl;

import java.io.File;
import java.lang.reflect.Array;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@SpringBootTest
public class DpTest {

    private final Dp dp;

    @Autowired
    public DpTest(Dp dp) {
        this.dp = dp;
    }
    @Test
    public void testLaplaceMechanism()  {
        int numberOfElements = 500000;
        double maxValue = 10000.0;

        // 创建一个列表来存储生成的浮点数
        List<Double> randomFloats = new ArrayList<>(numberOfElements);

        // 创建一个Random对象
        Random random = new Random();

        // 生成随机浮点数并添加到列表中
        for (int i = 0; i < numberOfElements; i++) {
            double randomFloat = maxValue * random.nextDouble();
            randomFloats.add(randomFloat);
        }

        // 输出列表中的前10个数作为示例
        for (int i = 0; i < 10; i++) {
            System.out.println(randomFloats.get(i));
        }
//        // Dp dp = new DpImpl();
        DSObject dsObject = new DSObject(randomFloats);
        DSObject result = dp.service(dsObject, 1, 1);
        for (Object number : result.getList()) {
            System.out.println(number);
        }
    }

    @Test
    public void testReportNoisyMax1Laplace()  {
//        // Dp dp = new DpImpl();
        List<Double> rawData = Arrays.asList(8.0, 2.0, 3.0, 4.0, 5.0);
        DSObject dsObject = new DSObject(rawData);
        DSObject result = dp.service(dsObject, 2, 10, 10);
        for (Object number : result.getList()) {
            System.out.println(number);
        }
    }

    @Test
    public void testReportNoisyMax3Laplace()  {
//        // Dp dp = new DpImpl();
        List<Double> rawData = Arrays.asList(8.0, 2.0, 3.0, 4.0, 5.0);
        DSObject dsObject = new DSObject(rawData);
        DSObject result = dp.service(dsObject, 3, 10, 10);
        for (Object number : result.getList()) {
            System.out.println(number);
        }
    }

    @Test
    public void testSnappingMechanism()  {
        // Dp dp = new DpImpl();
        List<Double> rawData = Arrays.asList(8.0, 2.0, 3.0, 4.0, 5.0);
        DSObject dsObject = new DSObject(rawData);
        DSObject result = dp.service(dsObject, 4, 5);
        for (Object s : result.getList()) {
            System.out.println(s);
        }
    }

    @Test
    public void testDpImage()  {
        // Dp dp = new DpImpl();
        File directory = new File("");
        String currentPath = directory.getAbsolutePath();
        String path1 = Paths.get(currentPath, "image", "1.png").toString();
        String path2 = Paths.get(currentPath, "image", "2.png").toString();
        List<String> rawData = Arrays.asList(path1, path2);
        DSObject dsObject = new DSObject(rawData);
        DSObject result = dp.service(dsObject, 5, 1);
        for (Object s : result.getList()) {
            System.out.println(s);
        }
    }

    @Test
    public void testDpAudio()  {
        // Dp dp = new DpImpl();
        File directory = new File("");
        String currentPath = directory.getAbsolutePath();
        String path1 = Paths.get(currentPath, "audio", "0001.wav").toString();
        String path2 = Paths.get(currentPath, "audio", "0001_Laplace.wav").toString();
        List<String> rawData = Arrays.asList(path1, path2);
        DSObject dsObject = new DSObject(rawData);
        DSObject result = dp.service(dsObject, 6, 0);
        for (Object s : result.getList()) {
            System.out.println(s);
        }
    }

//    @Test
//    public void testDpGraph()  {
//        // Dp dp = new DpImpl();
//        File directory = new File("");
//        String currentPath = directory.getAbsolutePath();
//        String path1 = Paths.get(currentPath, "raw_files", "graph").toString();
//        String path2 = Paths.get(currentPath, "desen_files", "test_graph").toString();
//        List<String> rawData = Arrays.asList(path1, path2);
//        DSObject dsObject = new DSObject(rawData);
//        dp.service(dsObject, 7, 0);
//    }

    @Test
    public void testExponentialMechanism()  {
        // Dp dp = new DpImpl();
        List<Double> rawData = Arrays.asList(8.0, 2.0, 3.0, 4.0, 5.0);
        DSObject dsObject = new DSObject(rawData);
        DSObject result = dp.service(dsObject, 8, 2);
        for (Object s : result.getList()) {
            System.out.println(s);
        }
    }

    @Test
    public void testReportNoisyMax2Exponential()  {
        // Dp dp = new DpImpl();
        List<Double> rawData = Arrays.asList(8.0, 2.0, 3.0, 4.0, 5.0);
        DSObject dsObject = new DSObject(rawData);
        DSObject result = dp.service(dsObject, 9, 1);
        for (Object s : result.getList()) {
            System.out.println(s);
        }
    }

    @Test
    public void testReportNoisyMax4()  {
        // Dp dp = new DpImpl();
        List<Double> rawData = Arrays.asList(8.0, 2.0, 3.0, 4.0, 5.0);
        DSObject dsObject = new DSObject(rawData);
        DSObject result = dp.service(dsObject, 10, 4);
        for (Object s : result.getList()) {
            System.out.println(s);
        }
    }

    @Test
    public void testSparseVectorTechnique1()  {
        // Dp dp = new DpImpl();
        List<Double> rawData = Arrays.asList(8.0, 2.0, 3.0, 4.0, 5.0);
        DSObject dsObject = new DSObject(rawData);
        DSObject result = dp.service(dsObject, 11, 5, 6);
        for (Object s : result.getList()) {
            System.out.println(s);
        }
    }

    @Test
    public void testSparseVectorTechnique2()  {
        // Dp dp = new DpImpl();
        List<Double> rawData = Arrays.asList(8.0, 2.0, 3.0, 4.0, 5.0);
        DSObject dsObject = new DSObject(rawData);
        DSObject result = dp.service(dsObject, 12, 5, 6);
        for (Object s : result.getList()) {
            System.out.println(s);
        }
    }

    @Test
    public void testSparseVectorTechnique3()  {
        // Dp dp = new DpImpl();
        List<Double> rawData = Arrays.asList(8.0, 2.0, 3.0, 4.0, 5.0);
        DSObject dsObject = new DSObject(rawData);
        // c:5, t:6
        DSObject result = dp.service(dsObject, 13, 2, 5);
        for (Object s : result.getList()) {
            System.out.println(s);
        }
    }

    @Test
    public void testSparseVectorTechnique4()  {
        // Dp dp = new DpImpl();
        List<Double> rawData = Arrays.asList(8.0, 2.0, 3.0, 4.0, 5.0);
        DSObject dsObject = new DSObject(rawData);
        DSObject result = dp.service(dsObject, 14, 5, 6);
        for (Object s : result.getList()) {
            System.out.println(s);
        }
    }

    @Test
    public void testSparseVectorTechnique5()  {
        // Dp dp = new DpImpl();
        List<Double> rawData = Arrays.asList(8.0, 2.0, 3.0, 4.0, 5.0);
        DSObject dsObject = new DSObject(rawData);
        DSObject result = dp.service(dsObject, 15, 5, 6);
        for (Object s : result.getList()) {
            System.out.println(s);
        }
    }

    @Test
    public void testSparseVectorTechnique6()  {
        // Dp dp = new DpImpl();
        List<Double> rawData = Arrays.asList(8.0, 2.0, 3.0, 4.0, 5.0);
        DSObject dsObject = new DSObject(rawData);
        DSObject result = dp.service(dsObject, 16, 5, 6);
        for (Object s : result.getList()) {
            System.out.println(s);
        }
    }

    @Test
    public void testNumericalSparseVectorTechnique()  {
        // Dp dp = new DpImpl();
        List<Double> rawData = Arrays.asList(8.0, 2.0, 3.0, 4.0, 5.0);
        DSObject dsObject = new DSObject(rawData);
        DSObject result = dp.service(dsObject, 17, 5, 6);
        for (Object s : result.getList()) {
            System.out.println(s);
        }
    }

    @Test
    public void testRappor()  {
        // Dp dp = new DpImpl();
        DSObject dsObject = new DSObject(8.5);
        DSObject result = dp.service(dsObject, 18, 1);
        for (Object s : result.getList()) {
            System.out.println(s);
        }
    }

    @Test
    public void testOneTimeRappor()  {
        // Dp dp = new DpImpl();
        DSObject dsObject = new DSObject(8.5);
        DSObject result = dp.service(dsObject, 19, 6);
        for (Object s : result.getList()) {
            System.out.println(s);
        }
    }

    // 信工所
    @Test
    public void testDpCode()  {
        // Dp dp = new DpImpl();
        List<String> rawData = Arrays.asList("A", "B", "C", "D", "E");
        DSObject dsObject = new DSObject(rawData);
        DSObject result = dp.service(dsObject, 20, 2);
        for (Object s : result.getList()) {
            System.out.println(s);
        }
    }

    @Test
    public void testRandomUniformToValue()  {
        // Dp dp = new DpImpl();
        List<Double> rawData = Arrays.asList(8.0, 2.0, 3.0, 4.0, 5.0);
        DSObject dsObject = new DSObject(rawData);
        DSObject result = dp.service(dsObject, 21, 2);
        for (Object s : result.getList()) {
            System.out.println(s);
        }
    }

    // 信工所
    @Test
    public void testRandomLaplaceToValue()  {
        // Dp dp = new DpImpl();
        List<Double> rawData = Arrays.asList(8.0, 2.0, 3.0, 4.0, 5.0);
        DSObject dsObject = new DSObject(rawData);
        DSObject result = dp.service(dsObject, 22, 2);
        for (Object s : result.getList()) {
            System.out.println(s);
        }
    }

    @Test
    public void testGaussianToValue()  {
        // Dp dp = new DpImpl();
        List<Double> rawData = Arrays.asList(8.0, 2.0, 3.0, 4.0, 5.0);
        DSObject dsObject = new DSObject(rawData);
        DSObject result = dp.service(dsObject, 23, 2);
        for (Object s : result.getList()) {
            System.out.println(s);
        }
    }

    @Test
    public void testNoisyHistogram1()  {
        // Dp dp = new DpImpl();
        List<Double> rawData = Arrays.asList(8.0, 2.0, 3.0, 4.0, 5.0);
        DSObject dsObject = new DSObject(rawData);
        DSObject result = dp.service(dsObject, 24, 6);
        for (Object s : result.getList()) {
            System.out.println(s);
        }
    }

    @Test
    public void testNoisyHistogram2()  {
        // Dp dp = new DpImpl();
        List<Double> rawData = Arrays.asList(8.0, 2.0, 3.0, 4.0, 5.0);
        DSObject dsObject = new DSObject(rawData);
        DSObject result = dp.service(dsObject, 25, 6);
        for (Object s : result.getList()) {
            System.out.println(s);
        }
    }

    // 信工所
    @Test
    public void testDpDate() {
        // Dp dp = new DpImpl();
        List<String> rawData = Arrays.asList("2019-03-02 10:58:53", "2019-03-02 10:58:54", "2019-03-02 10:58:55", "2019-03-02 10:58:56", "2019-03-02 10:58:57");
        DSObject dsObject = new DSObject(rawData);
        DSObject result = dp.service(dsObject, 26, 1);
        for (Object s : result.getList()) {
            System.out.println(s);
        }
    }
}

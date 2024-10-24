package com.lu.gademo;

import com.lu.gademo.utils.AlgorithmInfo;
import com.lu.gademo.utils.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class AlgorithmsFactoryTest {
    private final AlgorithmsFactory algorithmsFactory;
    private final Dp dp;
    
    private final Generalization generalization;
    private final Replace replace;
    private final Anonymity anonymity;

    @Autowired
    public AlgorithmsFactoryTest(AlgorithmsFactory algorithmsFactory, Dp dp, Generalization generalization,
                                 Replace replace, Anonymity anonymity) {
        this.algorithmsFactory = algorithmsFactory;
        this.dp = dp;
        this.generalization = generalization;
        this.replace = replace;
        this.anonymity = anonymity;
    }

    @Test
    public void testDpDate() {
        AlgorithmInfo algorithmInfo = algorithmsFactory.getAlgorithmInfoFromName("dpDate");
        AlgorithmInfo algorithmInfo2 = algorithmsFactory.getAlgorithmInfoFromId(1);
        // // Dp dp = new DpImpl();
        List<String> rawData = Arrays.asList("2019-03-02 10:58:53", "2019-03-02 10:58:54", "2019-03-02 10:58:55", "2019-03-02 10:58:56", "2019-03-02 10:58:57");
        DSObject dsObject = new DSObject(rawData);
        DSObject result = dp.service(dsObject, 26, 1);
        for (Object s : result.getList()) {
            System.out.println(s);
        }
        DSObject newResult = algorithmInfo.execute(dsObject,  1);
        for (Object object : newResult.getList()) {
            System.out.println(object);
        }
        assertEquals(algorithmInfo, algorithmInfo2);
//        assertEquals(result, newResult);
    }

    @Test
    public void testDpCode() {
        AlgorithmInfo algorithmInfo = algorithmsFactory.getAlgorithmInfoFromName("dpCode");
        AlgorithmInfo algorithmInfo2 = algorithmsFactory.getAlgorithmInfoFromId(2);
        // Dp dp = new DpImpl();
        List<String> rawData = Arrays.asList("A", "B", "C", "D", "E");
        DSObject dsObject = new DSObject(rawData);
        DSObject result = dp.service(dsObject, 20, 2);
        for (Object s : result.getList()) {
            System.out.println(s);
        }
        DSObject newResult = algorithmInfo.execute(dsObject,  2);
        for (Object object : newResult.getList()) {
            System.out.println(object);
        }
        assertEquals(algorithmInfo, algorithmInfo2);
//        assertEquals(result, newResult);
    }

    @Test
    public void testLaplaceToValue() {
        AlgorithmInfo algorithmInfo = algorithmsFactory.getAlgorithmInfoFromName("laplaceToValue");
        AlgorithmInfo algorithmInfo2 = algorithmsFactory.getAlgorithmInfoFromId(3);
        // Dp dp = new DpImpl();
        DSObject dsObject = new DSObject(Arrays.asList(8.0, 2.0, 3.0, 4.0, 5.0));
        DSObject result = dp.service(dsObject, 1, 1);
        for (Object number : result.getList()) {
            System.out.println(number);
        }
        DSObject newResult = algorithmInfo.execute(dsObject,  1);
        for (Object object : newResult.getList()) {
            System.out.println(object);
        }
        assertEquals(algorithmInfo, algorithmInfo2);
//        assertEquals(result, newResult);
    }

    @Test
    public void testRandomUniformToValue() {
        AlgorithmInfo algorithmInfo = algorithmsFactory.getAlgorithmInfoFromName("randomUniformToValue");
        AlgorithmInfo algorithmInfo2 = algorithmsFactory.getAlgorithmInfoFromId(5);
        // Dp dp = new DpImpl();
        List<Double> rawData = Arrays.asList(8.0, 2.0, 3.0, 4.0, 5.0);
        DSObject dsObject = new DSObject(rawData);
        DSObject result = dp.service(dsObject, 21, 2);
        for (Object s : result.getList()) {
            System.out.println(s);
        }
        DSObject newResult = algorithmInfo.execute(dsObject,  1);
        for (Object object : newResult.getList()) {
            System.out.println(object);
        }
        assertEquals(algorithmInfo, algorithmInfo2);
//        assertEquals(result, newResult);
    }

    @Test
    public void testRandomLaplaceToValue() {
        AlgorithmInfo algorithmInfo = algorithmsFactory.getAlgorithmInfoFromName("randomLaplaceToValue");
        AlgorithmInfo algorithmInfo2 = algorithmsFactory.getAlgorithmInfoFromId(6);
        // Dp dp = new DpImpl();
        List<Double> rawData = Arrays.asList(8.0, 2.0, 3.0, 4.0, 5.0);
        DSObject dsObject = new DSObject(rawData);
        DSObject result = dp.service(dsObject, 22, 2);
        for (Object s : result.getList()) {
            System.out.println(s);
        }
        DSObject newResult = algorithmInfo.execute(dsObject,  1);
        for (Object object : newResult.getList()) {
            System.out.println(object);
        }
        assertEquals(algorithmInfo, algorithmInfo2);
//        assertEquals(result, newResult);
    }

    @Test
    public void testRandomGaussianToValue() {
        AlgorithmInfo algorithmInfo = algorithmsFactory.getAlgorithmInfoFromName("randomGaussianToValue");
        AlgorithmInfo algorithmInfo2 = algorithmsFactory.getAlgorithmInfoFromId(7);
        // Dp dp = new DpImpl();
        List<Double> rawData = Arrays.asList(8.0, 2.0, 3.0, 4.0, 5.0);
        DSObject dsObject = new DSObject(rawData);
        DSObject result = dp.service(dsObject, 23, 2);
        for (Object s : result.getList()) {
            System.out.println(s);
        }
        DSObject newResult = algorithmInfo.execute(dsObject,  1);
        for (Object object : newResult.getList()) {
            System.out.println(object);
        }
        assertEquals(algorithmInfo, algorithmInfo2);
//        assertEquals(result, newResult);
    }

    @Test
    public void testValueShift() {
        AlgorithmInfo algorithmInfo = algorithmsFactory.getAlgorithmInfoFromName("valueShift");
        AlgorithmInfo algorithmInfo2 = algorithmsFactory.getAlgorithmInfoFromId(8);
        // Replace replace = new ReplaceImpl();
        List<Double> rawData = Arrays.asList(1234567.0, 89102.0);
        DSObject dsObject = new DSObject(rawData);
        DSObject result = replace.service(dsObject, 2, 1);
        for (Object object : result.getList()) {
            System.out.println(object);
        }
        DSObject newResult = algorithmInfo.execute(dsObject,  1);
        for (Object object : newResult.getList()) {
            System.out.println(object);
        }
        assertEquals(algorithmInfo, algorithmInfo2);
        assertEquals(result, newResult);
    }

    @Test
    public void testFloor() {
        AlgorithmInfo algorithmInfo = algorithmsFactory.getAlgorithmInfoFromName("floor");
        AlgorithmInfo algorithmInfo2 = algorithmsFactory.getAlgorithmInfoFromId(9);
        // Generalization generalization = new GeneralizationImpl();
        List<Double> rawData = Arrays.asList(12345.0, 56789.0);
        DSObject dsObject = new DSObject(rawData);
        DSObject result = generalization.service(dsObject, 2, 1);
        for (Object object : result.getList()) {
            System.out.println(object);
        }
        DSObject newResult = algorithmInfo.execute(dsObject, 1);
        for (Object object : newResult.getList()) {
            System.out.println(object);
        }
        assertEquals(algorithmInfo, algorithmInfo2);
        assertEquals(result, newResult);
    }

    @Test
    public void testValueMapping() {
        AlgorithmInfo algorithmInfo = algorithmsFactory.getAlgorithmInfoFromName("valueMapping");
        AlgorithmInfo algorithmInfo2 = algorithmsFactory.getAlgorithmInfoFromId(10);
        // Replace replace = new ReplaceImpl();
        List<Double> rawData = Arrays.asList(123.0, 456.0, 237.0);
        DSObject dsObject = new DSObject(rawData);
        DSObject result = replace.service(dsObject, 4);
        for (Object object : result.getList()) {
            System.out.println(object);
        }
        DSObject newResult = algorithmInfo.execute(dsObject);
        for (Object object : newResult.getList()) {
            System.out.println(object);
        }
        assertEquals(algorithmInfo, algorithmInfo2);
        assertEquals(result, newResult);
    }

    @Test
    public void testTruncation() {
        AlgorithmInfo algorithmInfo = algorithmsFactory.getAlgorithmInfoFromName("truncation");
        AlgorithmInfo algorithmInfo2 = algorithmsFactory.getAlgorithmInfoFromId(11);
        // Generalization generalization = new GeneralizationImpl();
        List<String> rawData = Arrays.asList("REDIS", "MYSQL");
        DSObject dsObject = new DSObject(rawData);
        DSObject result = generalization.service(dsObject, 1);
        for (Object object : result.getList()) {
            System.out.println(object);
        }
        DSObject newResult = algorithmInfo.execute(dsObject);
        for (Object object : newResult.getList()) {
            System.out.println(object);
        }
        assertEquals(algorithmInfo, algorithmInfo2);
        assertEquals(result, newResult);
    }

    @Test
    public void testFloorTime() {
        AlgorithmInfo algorithmInfo = algorithmsFactory.getAlgorithmInfoFromName("floorTime");
        AlgorithmInfo algorithmInfo2 = algorithmsFactory.getAlgorithmInfoFromId(12);
        // Generalization generalization = new GeneralizationImpl();
        List<Object> rawData = Arrays.asList("12:30:45", "1:09:25");
        DSObject dsObject = new DSObject(rawData);
        DSObject result = generalization.service(dsObject, 3);
        for (Object object : result.getList()) {
            System.out.println(object);
        }
        DSObject newResult = algorithmInfo.execute(dsObject);
        for (Object object : newResult.getList()) {
            System.out.println(object);
        }
        assertEquals(algorithmInfo, algorithmInfo2);
        assertEquals(result, newResult);
    }

    @Test
    public void testSuppressEmail() {
        AlgorithmInfo algorithmInfo = algorithmsFactory.getAlgorithmInfoFromName("suppressEmail");
        AlgorithmInfo algorithmInfo2 = algorithmsFactory.getAlgorithmInfoFromId(13);
        // Replace replace = new ReplaceImpl();
        List<String> rawData = Arrays.asList("199@163.com", "qwertyuiop@qq.com", "217hdu1d17@gmail.com");
        DSObject dsObject = new DSObject(rawData);
        DSObject result = replace.service(dsObject, 8);
        for (Object object : result.getList()) {
            System.out.println(object);
        }
        DSObject newResult = algorithmInfo.execute(dsObject);
        for (Object object : newResult.getList()) {
            System.out.println(object);
        }
        assertEquals(algorithmInfo, algorithmInfo2);
        assertEquals(result, newResult);
    }

    @Test
    public void testAddressHide() {
        AlgorithmInfo algorithmInfo = algorithmsFactory.getAlgorithmInfoFromName("addressHide");
        AlgorithmInfo algorithmInfo2 = algorithmsFactory.getAlgorithmInfoFromId(14);
        // Generalization generalization = new GeneralizationImpl();
        List<String> rawData = Arrays.asList("陕西省西安市长安区西安电子科技大学南校区", "北京市海淀区北京大学");
        DSObject dsObject = new DSObject(rawData);
        DSObject result = generalization.service(dsObject, 4, 1);
        for (Object string : result.getList()) {
            System.out.println(string);
        }
        DSObject newResult = algorithmInfo.execute(dsObject, 1);
        for (Object object : newResult.getList()) {
            System.out.println(object);
        }
        assertEquals(algorithmInfo, algorithmInfo2);
        assertEquals(result, newResult);
    }

    @Test
    public void testNameHide() {
        AlgorithmInfo algorithmInfo = algorithmsFactory.getAlgorithmInfoFromName("nameHide");
        AlgorithmInfo algorithmInfo2 = algorithmsFactory.getAlgorithmInfoFromId(15);
        // Replace replace = new ReplaceImpl();
        List<String> rawData = Arrays.asList("赵一二", "钱三四", "孙五六");
        DSObject dsObject = new DSObject(rawData);
        DSObject result = replace.service(dsObject, 6, 1);
        for (Object object : result.getList()) {
            System.out.println(object);
        }
        DSObject newResult = algorithmInfo.execute(dsObject,  1);
        for (Object object : newResult.getList()) {
            System.out.println(object);
        }
        assertEquals(algorithmInfo, algorithmInfo2);
        assertEquals(result, newResult);
    }

    @Test
    public void testNumberHide() {
        AlgorithmInfo algorithmInfo = algorithmsFactory.getAlgorithmInfoFromName("numberHide");
        AlgorithmInfo algorithmInfo2 = algorithmsFactory.getAlgorithmInfoFromId(16);
        // Replace replace = new ReplaceImpl();
        List<String> rawData = Arrays.asList("199293845297", "7654321", "17789012345");
        DSObject dsObject = new DSObject(rawData);
        DSObject result = replace.service(dsObject, 7, 2);
        for (Object object : result.getList()) {
            System.out.println(object);
        }
        DSObject newResult = algorithmInfo.execute(dsObject,  1);
        for (Object object : newResult.getList()) {
            System.out.println(object);
        }
        assertEquals(algorithmInfo, algorithmInfo2);
        assertEquals(result, newResult);
    }

    @Test
    public void testSHA512() {
        AlgorithmInfo algorithmInfo = algorithmsFactory.getAlgorithmInfoFromName("SHA512");
        AlgorithmInfo algorithmInfo2 = algorithmsFactory.getAlgorithmInfoFromId(17);
        // Replace replace = new ReplaceImpl();
        List<String> rawData = Arrays.asList("123", "456");
        DSObject dsObject = new DSObject(rawData);
        DSObject result = replace.service(dsObject, 3);
        for (Object object : result.getList()) {
            System.out.println(object);
        }
        DSObject newResult = algorithmInfo.execute(dsObject);
        for (Object object : newResult.getList()) {
            System.out.println(object);
        }
        assertEquals(algorithmInfo, algorithmInfo2);
        assertEquals(result, newResult);
    }

    @Test
    public void testDateGroupReplace() {
        AlgorithmInfo algorithmInfo = algorithmsFactory.getAlgorithmInfoFromName("date_group_replace");
        AlgorithmInfo algorithmInfo2 = algorithmsFactory.getAlgorithmInfoFromId(18);
        // Generalization generalization = new GeneralizationImpl();
        List<String> rawData = Arrays.asList("2024-3-18", "2024-6-1");
        DSObject dsObject = new DSObject(rawData);
        DSObject result = generalization.service(dsObject, 5, 1);
        for (Object string : result.getList()) {
            System.out.println(string);
        }
        DSObject newResult = algorithmInfo.execute(dsObject,  1);
        for (Object object : newResult.getList()) {
            System.out.println(object);
        }
        assertEquals(algorithmInfo, algorithmInfo2);
        assertEquals(result, newResult);
    }

    @Test
    public void testPassReplace() {
        AlgorithmInfo algorithmInfo = algorithmsFactory.getAlgorithmInfoFromName("passReplace");
        AlgorithmInfo algorithmInfo2 = algorithmsFactory.getAlgorithmInfoFromId(19);
        // Replace replace = new ReplaceImpl();
        List<String> rawData = Arrays.asList("123", "456");
        DSObject dsObject = new DSObject(rawData);
        DSObject result = replace.service(dsObject, 5, 1);
        for (Object object : result.getList()) {
            System.out.println(object);
        }
        DSObject newResult = algorithmInfo.execute(dsObject,  1);
        for (Object object : newResult.getList()) {
            System.out.println(object);
        }
        assertEquals(algorithmInfo, algorithmInfo2);
//        assertEquals(result, newResult);
    }

    @Test
    public void testValueHide() {
        AlgorithmInfo algorithmInfo = algorithmsFactory.getAlgorithmInfoFromName("value_hide");
        AlgorithmInfo algorithmInfo2 = algorithmsFactory.getAlgorithmInfoFromId(20);
        // Replace replace = new ReplaceImpl();
        List<String> rawData = Arrays.asList("123", "456");
        DSObject dsObject = new DSObject(rawData);
        DSObject result = replace.service(dsObject, 1);
        for (Object object : result.getList()) {
            System.out.println(object);
        }
        DSObject newResult = algorithmInfo.execute(dsObject);
        for (Object object : newResult.getList()) {
            System.out.println(object);
        }
        assertEquals(algorithmInfo, algorithmInfo2);
        assertEquals(result, newResult);
    }

    @Test
    public void testSuppressAllIp() {
        AlgorithmInfo algorithmInfo = algorithmsFactory.getAlgorithmInfoFromName("suppressAllIp");
        AlgorithmInfo algorithmInfo2 = algorithmsFactory.getAlgorithmInfoFromId(21);
        // Replace replace = new ReplaceImpl();
        List<String> rawData = Arrays.asList("192.168.1.1", "10.1.1.1", "127.0.0.1");
        DSObject dsObject = new DSObject(rawData);
        DSObject result = replace.service(dsObject, 9);
        for (Object object : result.getList()) {
            System.out.println(object);
        }
        DSObject newResult = algorithmInfo.execute(dsObject);
        for (Object object : newResult.getList()) {
            System.out.println(object);
        }
        assertEquals(algorithmInfo, algorithmInfo2);
        assertEquals(newResult, result);
    }

    @Test
    public void testSuppressIpRandomParts() {
        AlgorithmInfo algorithmInfo = algorithmsFactory.getAlgorithmInfoFromName("suppressIpRandomParts");
        AlgorithmInfo algorithmInfo2 = algorithmsFactory.getAlgorithmInfoFromId(22);
        // Replace replace = new ReplaceImpl();
        List<String> rawData = Arrays.asList("192.168.1.1", "10.1.1.1", "127.0.0.1");
        DSObject dsObject = new DSObject(rawData);
        DSObject result = replace.service(dsObject, 10);
        for (Object object : result.getList()) {
            System.out.println(object);
        }
        DSObject newResult = algorithmInfo.execute(dsObject);
        for (Object object : newResult.getList()) {
            System.out.println(object);
        }
        assertEquals(algorithmInfo, algorithmInfo2);
//        assertEquals(newResult, result);
    }

    @Test
    public void testNoisy_Histogram2() {
        AlgorithmInfo algorithmInfo = algorithmsFactory.getAlgorithmInfoFromName("Noisy_Histogram2");
        AlgorithmInfo algorithmInfo2 = algorithmsFactory.getAlgorithmInfoFromId(23);
        // Dp dp = new DpImpl();
        List<Double> rawData = Arrays.asList(8.0, 2.0, 3.0, 4.0, 5.0);
        DSObject dsObject = new DSObject(rawData);
        DSObject result = dp.service(dsObject, 25, 6);
        for (Object s : result.getList()) {
            System.out.println(s);
        }
        DSObject newResult = algorithmInfo.execute(dsObject, 6);
        for (Object object : newResult.getList()) {
            System.out.println(object);
        }
        assertEquals(algorithmInfo, algorithmInfo2);
//        assertEquals(newResult, result);
    }

    @Test
    public void testNoisy_Histogram1() {
        AlgorithmInfo algorithmInfo = algorithmsFactory.getAlgorithmInfoFromName("Noisy_Histogram1");
        AlgorithmInfo algorithmInfo2 = algorithmsFactory.getAlgorithmInfoFromId(24);
        // Dp dp = new DpImpl();
        List<Double> rawData = Arrays.asList(8.0, 2.0, 3.0, 4.0, 5.0);
        DSObject dsObject = new DSObject(rawData);
        DSObject result = dp.service(dsObject, 24, 6);
        for (Object s : result.getList()) {
            System.out.println(s);
        }
        DSObject newResult = algorithmInfo.execute(dsObject, 6);
        for (Object object : newResult.getList()) {
            System.out.println(object);
        }
        assertEquals(algorithmInfo, algorithmInfo2);
//        assertEquals(newResult, result);
    }

    @Test
    public void testMeanValueImage() {
        AlgorithmInfo algorithmInfo = algorithmsFactory.getAlgorithmInfoFromName("meanValueImage");
        AlgorithmInfo algorithmInfo2 = algorithmsFactory.getAlgorithmInfoFromId(40);
        // Generalization generalization = new GeneralizationImpl();
        File directory = new File("");
        String currentPath = directory.getAbsolutePath();
        String path1 = Paths.get(currentPath, "image", "1.png").toString();
        String path2 = Paths.get(currentPath, "image", "2.png").toString();
        List<String> rawData = Arrays.asList(path1, path2);
        DSObject dsObject = new DSObject(rawData);
        DSObject result = generalization.service(dsObject, 12, 1);
        for (Object s : result.getList()) {
            System.out.println(s);
        }
        DSObject newResult = algorithmInfo.execute(dsObject, 1);
        for (Object object : newResult.getList()) {
            System.out.println(object);
        }
        assertEquals(algorithmInfo, algorithmInfo2);
//        assertEquals(newResult, result);
    }

    @Test
    public void testGaussianBlur() {
        AlgorithmInfo algorithmInfo = algorithmsFactory.getAlgorithmInfoFromName("gaussian_blur");
        AlgorithmInfo algorithmInfo2 = algorithmsFactory.getAlgorithmInfoFromId(41);
        // Generalization generalization = new GeneralizationImpl();
        File directory = new File("");
        String currentPath = directory.getAbsolutePath();
        String path1 = Paths.get(currentPath, "image", "1.png").toString();
        String path2 = Paths.get(currentPath, "image", "2.png").toString();
        List<String> rawData = Arrays.asList(path1, path2);
        DSObject dsObject = new DSObject(rawData);
        DSObject result = generalization.service(dsObject, 10, 1);
        for (Object s : result.getList()) {
            System.out.println(s);
        }
        DSObject newResult = algorithmInfo.execute(dsObject, 1);
        for (Object object : newResult.getList()) {
            System.out.println(object);
        }
        assertEquals(algorithmInfo, algorithmInfo2);
//        assertEquals(newResult, result);
    }

    @Test
    public void testPixelate() {
        AlgorithmInfo algorithmInfo = algorithmsFactory.getAlgorithmInfoFromName("pixelate");
        AlgorithmInfo algorithmInfo2 = algorithmsFactory.getAlgorithmInfoFromId(42);
        // Generalization generalization = new GeneralizationImpl();
        File directory = new File("");
        String currentPath = directory.getAbsolutePath();
        String path1 = Paths.get(currentPath, "image", "1.png").toString();
        String path2 = Paths.get(currentPath, "image", "2.png").toString();
        List<String> rawData = Arrays.asList(path1, path2);
        DSObject dsObject = new DSObject(rawData);
        DSObject result = generalization.service(dsObject, 9, 1);
        for (Object s : result.getList()) {
            System.out.println(s);
        }
        DSObject newResult = algorithmInfo.execute(dsObject, 1);
        for (Object object : newResult.getList()) {
            System.out.println(object);
        }
        assertEquals(algorithmInfo, algorithmInfo2);
//        assertEquals(newResult, result);
    }

    @Test
    public void testBoxBlur() {
        AlgorithmInfo algorithmInfo = algorithmsFactory.getAlgorithmInfoFromName("box_blur");
        AlgorithmInfo algorithmInfo2 = algorithmsFactory.getAlgorithmInfoFromId(43);
        // Generalization generalization = new GeneralizationImpl();
        File directory = new File("");
        String currentPath = directory.getAbsolutePath();
        String path1 = Paths.get(currentPath, "image", "1.png").toString();
        String path2 = Paths.get(currentPath, "image", "2.png").toString();
        List<String> rawData = Arrays.asList(path1, path2);
        DSObject dsObject = new DSObject(rawData);
        DSObject result = generalization.service(dsObject, 11, 1);
        for (Object s : result.getList()) {
            System.out.println(s);
        }
        DSObject newResult = algorithmInfo.execute(dsObject, 1);
        for (Object object : newResult.getList()) {
            System.out.println(object);
        }
        assertEquals(algorithmInfo, algorithmInfo2);
//        assertEquals(newResult, result);
    }

    @Test
    public void testDpImage() {
        AlgorithmInfo algorithmInfo = algorithmsFactory.getAlgorithmInfoFromName("dpImage");
        AlgorithmInfo algorithmInfo2 = algorithmsFactory.getAlgorithmInfoFromId(44);
        // Dp dp = new DpImpl();
        File directory = new File("");
        String currentPath = directory.getAbsolutePath();
        String path1 = Paths.get(currentPath, "image", "1.png").toString();
        String path2 = Paths.get(currentPath, "image", "2.png").toString();
        List<String> rawData = Arrays.asList(path1, path2);
        DSObject dsObject = new DSObject(rawData);
        DSObject result = dp.service(dsObject, 5, 0.5);
        for (Object s : result.getList()) {
            System.out.println(s);
        }
        DSObject newResult = algorithmInfo.execute(dsObject, 0.5);
        for (Object object : newResult.getList()) {
            System.out.println(object);
        }
        assertEquals(algorithmInfo, algorithmInfo2);
//        assertEquals(newResult, result);
    }

    @Test
    public void testReplaceRegion() {
        AlgorithmInfo algorithmInfo = algorithmsFactory.getAlgorithmInfoFromName("replace_region");
        AlgorithmInfo algorithmInfo2 = algorithmsFactory.getAlgorithmInfoFromId(45);
        // Generalization generalization = new GeneralizationImpl();
        File directory = new File("");
        String currentPath = directory.getAbsolutePath();
        String path1 = Paths.get(currentPath, "image", "1.png").toString();
        String path2 = Paths.get(currentPath, "image", "2.png").toString();
        List<String> rawData = Arrays.asList(path1, path2);
        DSObject dsObject = new DSObject(rawData);
        DSObject result = generalization.service(dsObject, 13, 1);
        for (Object s : result.getList()) {
            System.out.println(s);
        }
        DSObject newResult = algorithmInfo.execute(dsObject, 1);
        for (Object object : newResult.getList()) {
            System.out.println(object);
        }
        assertEquals(algorithmInfo, algorithmInfo2);
//        assertEquals(newResult, result);
    }

    @Test
    public void testImageExchangeChannel() {
        AlgorithmInfo algorithmInfo = algorithmsFactory.getAlgorithmInfoFromName("image_exchange_channel");
        AlgorithmInfo algorithmInfo2 = algorithmsFactory.getAlgorithmInfoFromId(46);
        // Replace replace = new ReplaceImpl();
        File directory = new File("");
        String currentPath = directory.getAbsolutePath();
        String path1 = Paths.get(currentPath, "image", "1.png").toString();
        String path2 = Paths.get(currentPath, "image", "2.png").toString();
        List<String> rawData = Arrays.asList(path1, path2);
        DSObject dsObject = new DSObject(rawData);
        DSObject result = replace.service(dsObject, 11);
        for (Object s : result.getList()) {
            System.out.println(s);
        }
        DSObject newResult = algorithmInfo.execute(dsObject);
        for (Object object : newResult.getList()) {
            System.out.println(object);
        }
        assertEquals(algorithmInfo, algorithmInfo2);
//        assertEquals(newResult, result);
    }

    @Test
    public void testImageAddColorOffset() {
        AlgorithmInfo algorithmInfo = algorithmsFactory.getAlgorithmInfoFromName("image_add_color_offset");
        AlgorithmInfo algorithmInfo2 = algorithmsFactory.getAlgorithmInfoFromId(47);
        // Replace replace = new ReplaceImpl();
        File directory = new File("");
        String currentPath = directory.getAbsolutePath();
        String path1 = Paths.get(currentPath, "image", "1.png").toString();
        String path2 = Paths.get(currentPath, "image", "2.png").toString();
        List<String> rawData = Arrays.asList(path1, path2);
        DSObject dsObject = new DSObject(rawData);
        DSObject result = replace.service(dsObject, 12, 1);
        for (Object s : result.getList()) {
            System.out.println(s);
        }
        DSObject newResult = algorithmInfo.execute(dsObject, 1);
        for (Object object : newResult.getList()) {
            System.out.println(object);
        }
        assertEquals(algorithmInfo, algorithmInfo2);
//        assertEquals(newResult, result);
    }

    @Test
    public void testImageFaceSub() {
        AlgorithmInfo algorithmInfo = algorithmsFactory.getAlgorithmInfoFromName("image_face_sub");
        AlgorithmInfo algorithmInfo2 = algorithmsFactory.getAlgorithmInfoFromId(48);
        // Replace replace = new ReplaceImpl();
        File directory = new File("");
        String currentPath = directory.getAbsolutePath();
        String path1 = Paths.get(currentPath, "image", "FaceReplace", "dataset", "image", "Honeyview_glass.jpg").toString();
        String path2 = Paths.get(currentPath, "image", "FaceReplace", "dataset", "image", "2.jpg").toString();
        String path3 = Paths.get(currentPath, "image", "FaceReplace", "dataset", "result", "result.jpg").toString();
        List<String> rawData = Arrays.asList(path1, path2, path3);
        DSObject dsObject = new DSObject(rawData);

        DSObject result = replace.service(dsObject, 13);
        for (Object s : result.getList()) {
            System.out.println(s);
        }

        DSObject newResult = algorithmInfo.execute(dsObject);
        for (Object object : newResult.getList()) {
            System.out.println(object);
        }
        assertEquals(algorithmInfo, algorithmInfo2);
//        assertEquals(newResult, result);
    }

    @Test
    public void testMeanValueVideo() {
        AlgorithmInfo algorithmInfo = algorithmsFactory.getAlgorithmInfoFromName("meanValueVideo");
        AlgorithmInfo algorithmInfo2 = algorithmsFactory.getAlgorithmInfoFromId(50);
        // Generalization generalization = new GeneralizationImpl();
        File directory = new File("");
        String currentPath = directory.getAbsolutePath();
        String path1 = Paths.get(currentPath, "raw_files", "3.mp4").toString();
        String path2 = Paths.get(currentPath, "raw_files", "4.mp4").toString();
        List<String> rawData = Arrays.asList(path1, path2);
        DSObject dsObject = new DSObject(rawData);
        DSObject result = generalization.service(dsObject, 17, 1);
        for (Object s : result.getList()) {
            System.out.println(s);
        }

        DSObject newResult = algorithmInfo.execute(dsObject, 1);
        for (Object object : newResult.getList()) {
            System.out.println(object);
        }
        assertEquals(algorithmInfo, algorithmInfo2);
//        assertEquals(newResult, result);
    }

    @Test
    public void testGaussianBlurVideo() {
        AlgorithmInfo algorithmInfo = algorithmsFactory.getAlgorithmInfoFromName("gaussian_blur_video");
        AlgorithmInfo algorithmInfo2 = algorithmsFactory.getAlgorithmInfoFromId(51);
        // Generalization generalization = new GeneralizationImpl();
        File directory = new File("");
        String currentPath = directory.getAbsolutePath();
        String path1 = Paths.get(currentPath, "raw_files", "3.mp4").toString();
        String path2 = Paths.get(currentPath, "raw_files", "4.mp4").toString();
        List<String> rawData = Arrays.asList(path1, path2);
        DSObject dsObject = new DSObject(rawData);
        DSObject result = generalization.service(dsObject, 15, 1);
        for (Object s : result.getList()) {
            System.out.println(s);
        }

        DSObject newResult = algorithmInfo.execute(dsObject, 1);
        for (Object object : newResult.getList()) {
            System.out.println(object);
        }
        assertEquals(algorithmInfo, algorithmInfo2);
//        assertEquals(newResult, result);
    }

    @Test
    public void testPixelateVideo() {
        AlgorithmInfo algorithmInfo = algorithmsFactory.getAlgorithmInfoFromName("pixelate_video");
        AlgorithmInfo algorithmInfo2 = algorithmsFactory.getAlgorithmInfoFromId(52);
        // Generalization generalization = new GeneralizationImpl();
        File directory = new File("");
        String currentPath = directory.getAbsolutePath();
        String path1 = Paths.get(currentPath, "raw_files", "3.mp4").toString();
        String path2 = Paths.get(currentPath, "raw_files", "4.mp4").toString();
        List<String> rawData = Arrays.asList(path1, path2);
        DSObject dsObject = new DSObject(rawData);
        DSObject result = generalization.service(dsObject, 14, 1);
        for (Object s : result.getList()) {
            System.out.println(s);
        }

        DSObject newResult = algorithmInfo.execute(dsObject, 1);
        for (Object object : newResult.getList()) {
            System.out.println(object);
        }
        assertEquals(algorithmInfo, algorithmInfo2);
//        assertEquals(newResult, result);
    }

    @Test
    public void testBoxBlurVideo() {
        AlgorithmInfo algorithmInfo = algorithmsFactory.getAlgorithmInfoFromName("box_blur_video");
        AlgorithmInfo algorithmInfo2 = algorithmsFactory.getAlgorithmInfoFromId(53);
        // Generalization generalization = new GeneralizationImpl();
        File directory = new File("");
        String currentPath = directory.getAbsolutePath();
        String path1 = Paths.get(currentPath, "raw_files", "3.mp4").toString();
        String path2 = Paths.get(currentPath, "raw_files", "4.mp4").toString();
        List<String> rawData = Arrays.asList(path1, path2);
        DSObject dsObject = new DSObject(rawData);
        DSObject result = generalization.service(dsObject, 16, 1);
        for (Object s : result.getList()) {
            System.out.println(s);
        }

        DSObject newResult = algorithmInfo.execute(dsObject, 1);
        for (Object object : newResult.getList()) {
            System.out.println(object);
        }
        assertEquals(algorithmInfo, algorithmInfo2);
//        assertEquals(newResult, result);
    }

    @Test
    public void testReplaceRegionVideo() {
        AlgorithmInfo algorithmInfo = algorithmsFactory.getAlgorithmInfoFromName("replace_region_video");
        AlgorithmInfo algorithmInfo2 = algorithmsFactory.getAlgorithmInfoFromId(54);
        // Generalization generalization = new GeneralizationImpl();
        File directory = new File("");
        String currentPath = directory.getAbsolutePath();
        String path1 = Paths.get(currentPath, "image", "1.png").toString();
        String path2 = Paths.get(currentPath, "image", "2.png").toString();
        List<String> rawData = Arrays.asList(path1, path2);
        DSObject dsObject = new DSObject(rawData);
        DSObject result = generalization.service(dsObject, 18, 1);
        for (Object s : result.getList()) {
            System.out.println(s);
        }

        DSObject newResult = algorithmInfo.execute(dsObject, 1);
        for (Object object : newResult.getList()) {
            System.out.println(object);
        }
        assertEquals(algorithmInfo, algorithmInfo2);
//        assertEquals(newResult, result);
    }

    @Test
    public void testVideoAddColorOffset() {
        AlgorithmInfo algorithmInfo = algorithmsFactory.getAlgorithmInfoFromName("video_add_color_offset");
        AlgorithmInfo algorithmInfo2 = algorithmsFactory.getAlgorithmInfoFromId(55);
        // Replace replace = new ReplaceImpl();
        File directory = new File("");
        String currentPath = directory.getAbsolutePath();
        String path1 = Paths.get(currentPath, "raw_files", "3.mp4").toString();
        String path2 = Paths.get(currentPath, "raw_files", "3-add-color.mp4").toString();
        List<String> rawData = Arrays.asList(path1, path2);
        DSObject dsObject = new DSObject(rawData);
        DSObject result = replace.service(dsObject, 14, 1);
        for (Object s : result.getList()) {
            System.out.println(s);
        }

        DSObject newResult = algorithmInfo.execute(dsObject, 1);
        for (Object object : newResult.getList()) {
            System.out.println(object);
        }
        assertEquals(algorithmInfo, algorithmInfo2);
//        assertEquals(newResult, result);
    }

    @Test
    public void testVideoRemoveBg() {
        AlgorithmInfo algorithmInfo = algorithmsFactory.getAlgorithmInfoFromName("video_remove_bg");
        AlgorithmInfo algorithmInfo2 = algorithmsFactory.getAlgorithmInfoFromId(56);
        // Replace replace = new ReplaceImpl();
        File directory = new File("");
        String currentPath = directory.getAbsolutePath();
        String path1 = Paths.get(currentPath, "image", "FaceReplace", "dataset", "video", "1.mp4").toString();
        String path2 = Paths.get(currentPath, "image", "FaceReplace", "dataset", "image", "loong2.png").toString();
        String path3 = Paths.get(currentPath, "image", "FaceReplace", "dataset", "result", "result.mp4").toString();
        List<String> rawData = Arrays.asList(path1, path2, path3);
        DSObject dsObject = new DSObject(rawData);
        DSObject result = replace.service(dsObject, 16, 1);
        for (Object s : result.getList()) {
            System.out.println(s);
        }

        DSObject newResult = algorithmInfo.execute(dsObject, 1);
        for (Object object : newResult.getList()) {
            System.out.println(object);
        }
        assertEquals(algorithmInfo, algorithmInfo2);
//        assertEquals(newResult, result);
    }

    @Test
    public void testVideoFaceSubTarget() {
        AlgorithmInfo algorithmInfo = algorithmsFactory.getAlgorithmInfoFromName("video_face_sub_target");
        AlgorithmInfo algorithmInfo2 = algorithmsFactory.getAlgorithmInfoFromId(57);
        // Replace replace = new ReplaceImpl();
        File directory = new File("");
        String currentPath = directory.getAbsolutePath();
        String path1 = Paths.get(currentPath, "image", "FaceReplace", "dataset", "video", "1.mp4").toString();
        String path2 = Paths.get(currentPath, "image", "FaceReplace", "dataset", "image", "2.png").toString();
        String path3 = Paths.get(currentPath, "image", "FaceReplace", "dataset", "result", "result.mp4").toString();
        List<String> rawData = Arrays.asList(path1, path2, path3);
        DSObject dsObject = new DSObject(rawData);
        DSObject result = replace.service(dsObject, 15);
        for (Object s : result.getList()) {
            System.out.println(s);
        }

        DSObject newResult = algorithmInfo.execute(dsObject);
        for (Object object : newResult.getList()) {
            System.out.println(object);
        }
        assertEquals(algorithmInfo, algorithmInfo2);
//        assertEquals(newResult, result);
    }

    @Test
    public void testDpAudio() {
        AlgorithmInfo algorithmInfo = algorithmsFactory.getAlgorithmInfoFromName("dpAudio");
        AlgorithmInfo algorithmInfo2 = algorithmsFactory.getAlgorithmInfoFromId(71);
        // Dp dp = new DpImpl();
        File directory = new File("");
        String currentPath = directory.getAbsolutePath();
        String path1 = Paths.get(currentPath, "audio", "0001.wav").toString();
        String path2 = Paths.get(currentPath, "audio", "0001_Laplace.wav").toString();
        List<String> rawData = Arrays.asList(path1, path2);
        DSObject dsObject = new DSObject(rawData);
        DSObject result = dp.service(dsObject, 6, 1);
        for (Object s : result.getList()) {
            System.out.println(s);
        }

        DSObject newResult = algorithmInfo.execute(dsObject, 1);
        for (Object object : newResult.getList()) {
            System.out.println(object);
        }
        assertEquals(algorithmInfo, algorithmInfo2);
//        assertEquals(newResult, result);
    }

    @Test
    public void testVoiceReplace() {
        AlgorithmInfo algorithmInfo = algorithmsFactory.getAlgorithmInfoFromName("voice_replace");
        AlgorithmInfo algorithmInfo2 = algorithmsFactory.getAlgorithmInfoFromId(72);
        // Replace replace = new ReplaceImpl();
        File directory = new File("");
        String currentPath = directory.getAbsolutePath();
        String path1 = Paths.get(currentPath, "audio", "0001.wav").toString();
        String path2 = Paths.get(currentPath, "audio", "0001_replace_voice_print.wav").toString();
        List<String> rawData = Arrays.asList(path1, path2);
        DSObject dsObject = new DSObject(rawData);
        DSObject result = replace.service(dsObject, 19);
        for (Object s : result.getList()) {
            System.out.println(s);
        }

        DSObject newResult = algorithmInfo.execute(dsObject);
        for (Object object : newResult.getList()) {
            System.out.println(object);
        }
        assertEquals(algorithmInfo, algorithmInfo2);
//        assertEquals(newResult, result);
    }

    @Test
    public void testApplyAudioEffects() {
        AlgorithmInfo algorithmInfo = algorithmsFactory.getAlgorithmInfoFromName("apply_audio_effects");
        AlgorithmInfo algorithmInfo2 = algorithmsFactory.getAlgorithmInfoFromId(73);
        // Replace replace = new ReplaceImpl();
        File directory = new File("");
        String currentPath = directory.getAbsolutePath();
        String path1 = Paths.get(currentPath, "audio", "0001.wav").toString();
        String path2 = Paths.get(currentPath, "audio", "0001_apply_effects.wav").toString();
        List<String> rawData = Arrays.asList(path1, path2);
        DSObject dsObject = new DSObject(rawData);
        DSObject result = replace.service(dsObject, 18, 1);
        for (Object s : result.getList()) {
            System.out.println(s);
        }

        DSObject newResult = algorithmInfo.execute(dsObject, 1);
        for (Object object : newResult.getList()) {
            System.out.println(object);
        }
        assertEquals(algorithmInfo, algorithmInfo2);
//        assertEquals(newResult, result);
    }

    @Test
    public void testAudioReshuffle() {
        AlgorithmInfo algorithmInfo = algorithmsFactory.getAlgorithmInfoFromName("audio_reshuffle");
        AlgorithmInfo algorithmInfo2 = algorithmsFactory.getAlgorithmInfoFromId(74);
        // Replace replace = new ReplaceImpl();
        File directory = new File("");
        String currentPath = directory.getAbsolutePath();
        String path1 = Paths.get(currentPath, "audio", "0001.wav").toString();
        String path2 = Paths.get(currentPath, "audio", "0001_reshuffle.wav").toString();
        List<String> rawData = Arrays.asList(path1, path2);
        DSObject dsObject = new DSObject(rawData);
        DSObject result = replace.service(dsObject, 17, 1);
        for (Object s : result.getList()) {
            System.out.println(s);
        }

        DSObject newResult = algorithmInfo.execute(dsObject, 1);
        for (Object object : newResult.getList()) {
            System.out.println(object);
        }
        assertEquals(algorithmInfo, algorithmInfo2);
//        assertEquals(newResult, result);
    }

    @Test
    public void testIfEqual() {
        AlgorithmInfo algorithmInfo1 = algorithmsFactory.getAlgorithmInfoFromName("suppressEmail");
        AlgorithmInfo algorithmInfo2 = algorithmsFactory.getAlgorithmInfoFromId(13);

        assertEquals(algorithmInfo1, algorithmInfo2);

    }

}

package com.lu.gademo;

import com.lu.gademo.utils.DSObject;
import com.lu.gademo.utils.DpUtil;
import com.lu.gademo.utils.Generalization;
import com.lu.gademo.utils.impl.DpUtilImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@SpringBootTest
public class GeneralizationTest {
    Generalization generalization;
    DpUtil dpUtil;

    @Autowired
    public GeneralizationTest(Generalization generalization, DpUtil dpUtil) {
        this.dpUtil = dpUtil;
        this.generalization = generalization;
    }

    public static List<String> generateDates(int count) {
        // 初始日期时间
        LocalDateTime startTime = LocalDateTime.of(2019, 3, 2, 10, 58, 53);
        // 日期时间格式器
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        List<String> dates = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            // 添加格式化后的日期时间到列表
            dates.add(startTime.plusDays(i).format(formatter));
        }
        return dates;
    }

    @Test
    public void testTruncation()  {
        // // Generalization generalization = new GeneralizationImpl();
        List<String> rawData = Arrays.asList("REDIS", "MYSQL");
        DSObject dsObject = new DSObject(rawData);
        DSObject result0 = generalization.service(dsObject, 1, 0);
        for (Object string : result0.getList()) {
            System.out.println(string);
        }
        DSObject result1 = generalization.service(dsObject, 1, 1);
        for (Object string : result1.getList()) {
            System.out.println(string);
        }
        DSObject result2 = generalization.service(dsObject, 1, 2);
        DSObject result3 = generalization.service(dsObject, 1, 3);
        result2.getList().forEach(System.out::println);
        result3.getList().forEach(System.out::println);
    }

    @Test
    public void testFloor()  {
        // // Generalization generalization = new GeneralizationImpl();
        List<Double> rawData = Arrays.asList(1.0, 123.0, 1234.0, 56789.0);
        DSObject dsObject = new DSObject(rawData);
        DSObject result0 = generalization.service(dsObject, 2, 0);
        DSObject result1 = generalization.service(dsObject, 2, 1);
        DSObject result2 = generalization.service(dsObject, 2, 2);
        DSObject result3 = generalization.service(dsObject, 2, 3);
        for (Object num : result0.getList()) {
            System.out.println(num);
        }
        for (Object num : result1.getList()) {
            System.out.println(num);
        }
        for (Object num : result2.getList()) {
            System.out.println(num);
        }
        for (Object num : result3.getList()) {
            System.out.println(num);
        }
    }

    @Test
    public void testFloorTime()  {
        // Generalization generalization = new GeneralizationImpl();
        List<Object> rawData = Arrays.asList("12:30:45", "1:09:25");
        DSObject dsObject = new DSObject(rawData);
        DSObject result = generalization.service(dsObject, 3, 1);
        for (Object num : result.getList()) {
            System.out.println(num);
        }
        DSObject result0 = generalization.service(dsObject, 3, 0);
        for (Object num : result0.getList()) {
            System.out.println(num);
        }
    }

    // 信工所
    @Test
    public void testAddressHide()  {
        // Generalization generalization = new GeneralizationImpl();
        List<String> rawData = Arrays.asList("陕西省西安市长安区西安电子科技大学南校区", "北京市海淀区北京大学",
                "广西壮族自治区玉林市北流市塘岸收费站入口(北海方向)", "广西壮族自治区桂林市七星区施家园路75号附近停车场",
                "海南省儋州市国营八一总场xxx地址", "海南省三沙市西沙群岛xxx村","广西壮族自治区北海市逢时花园重庆苑a区27号楼",
                "香港特别行政区7-11-裕旺大厦11号楼", "香港特别行政区中西区金钟添马添美道2号", "香港中西区金钟添马添美道2号",
                "重庆市江北区建北四支路2号北辰名都8-11层重庆市信息产业局", "黑龙江省哈尔滨市道里区哈尔滨市公安局", "香港特别行政区香港特别行政区石芳楼");
        DSObject dsObject = new DSObject(rawData);
        DSObject result0 = generalization.service(dsObject, 4, 0);
        DSObject result1 = generalization.service(dsObject, 4, 1);
        DSObject result2 = generalization.service(dsObject, 4, 2);
        DSObject result3 = generalization.service(dsObject, 4, 3);
        for (Object string : result0.getList()) {
            System.out.println(string);
        }
        for (Object string : result1.getList()) {
            System.out.println(string);
        }
        result2.getList().forEach(System.out::println);
        result3.getList().forEach(System.out::println);

    }

    @Test
    public void testDateGroupReplace()  {
        // Generalization generalization = new GeneralizationImpl();
        List<String> newRawData = generateDates(10);
        System.out.println(newRawData);
//        List<String> rawData = Arrays.asList("2024-3-18", "2024-6-1");
        DSObject dsObject = new DSObject(newRawData);
        DSObject result = generalization.service(dsObject, 5, 3);
        for (Object string : result.getList().stream().limit(10).collect(Collectors.toList())) {
            System.out.println(string);
        }
        DSObject result0 = generalization.service(dsObject, 5, 0);
        for (Object string : result.getList().stream().limit(10).collect(Collectors.toList())) {
            System.out.println(string);
        }
    }

    @Test
    public void testMixZone1()  {
        // Generalization generalization = new GeneralizationImpl();
        String position = "116.435842,39.941626";
        List<String> rawData = Arrays.asList("116.435842,39.941626", "116.353714,39.939588", "116.435806,39.908501", "116.356866,39.907242");
        DSObject dsObject = new DSObject(position, rawData);
        DSObject result = generalization.service(dsObject, 6, 115, 9.0);
        for (Object string : result.getList()) {
            System.out.println(string);
        }
    }

    @Test
    public void testMixZone3()  {
        // Generalization generalization = new GeneralizationImpl();
        String position = "116.435842,39.941626";
        List<String> rawData = Arrays.asList("116.435842,39.941626", "116.353714,39.939588", "116.435806,39.908501", "116.356866,39.907242");
        DSObject dsObject = new DSObject(position, rawData);
        DSObject result = generalization.service(dsObject, 7, 115, 9.0);
        for (Object string : result.getList()) {
            System.out.println(string);
        }
    }

    @Test
    public void testAccuracyReduction()  {
        // Generalization generalization = new GeneralizationImpl();
        String position = "116.435842,39.941626";
        DSObject dsObject = new DSObject(position);
        DSObject result = generalization.service(dsObject, 8);
        for (Object string : result.getList()) {
            System.out.println(string);
        }
    }

    @Test
    public void testPixLateImage()  {
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
    }

    @Test
    public void testGaussianBlurImage()  {
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
    }

    @Test
    public void testBoxBlurImage()  {
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
    }

    @Test
    public void testMeanValueBlurImage()  {
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
    }

    @Test
    public void testReplaceRegionImage()  {
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
    }

    @Test
    public void testPixLateVideo()  {
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
    }

    @Test
    public void testGaussianBlurVideo()  {
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
    }

    @Test
    public void testBoxBlurVideo()  {
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
    }

    @Test
    public void testMeanValueBlurVideo()  {
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
    }

    @Test
    public void testReplaceRegionVideo()  {
        // Generalization generalization = new GeneralizationImpl();
        File directory = new File("");
        String currentPath = directory.getAbsolutePath();
        String path1 = Paths.get(currentPath, "raw_files", "3.mp4").toString();
        String path2 = Paths.get(currentPath, "raw_files", "4.mp4").toString();
        List<String> rawData = Arrays.asList(path1, path2);
        DSObject dsObject = new DSObject(rawData);
        DSObject result = generalization.service(dsObject, 18, 1);
        for (Object s : result.getList()) {
            System.out.println(s);
        }
    }

    @Test
    public void testFloorAudio()  {
        // Generalization generalization = new GeneralizationImpl();
        File directory = new File("");
        String currentPath = directory.getAbsolutePath();
        String path1 = Paths.get(currentPath, "audio", "0001.wav").toString();
        String path2 = Paths.get(currentPath, "audio", "0001_floor.wav").toString();
        List<String> rawData = Arrays.asList(path1, path2);
        DSObject dsObject = new DSObject(rawData);
        DSObject result = generalization.service(dsObject, 19, 1);
        for (Object s : result.getList()) {
            System.out.println(s);
        }
    }

    @Test
    public void testSpecMaskAudio()  {
        // Generalization generalization = new GeneralizationImpl();
        File directory = new File("");
        String currentPath = directory.getAbsolutePath();
        String path1 = Paths.get(currentPath, "audio", "0001.wav").toString();
        String path2 = Paths.get(currentPath, "audio", "0001_spec_mask.wav").toString();
        List<String> rawData = Arrays.asList(path1, path2);
        DSObject dsObject = new DSObject(rawData);
        DSObject result = generalization.service(dsObject, 20, 1);
        for (Object s : result.getList()) {
            System.out.println(s);
        }
    }

    @Test
    public void testAugmentationAudio()  {
        // Generalization generalization = new GeneralizationImpl();
        File directory = new File("");
        String currentPath = directory.getAbsolutePath();
        String path1 = Paths.get(currentPath, "audio", "0001.wav").toString();
        String path2 = Paths.get(currentPath, "audio", "0001_aug.wav").toString();
        List<String> rawData = Arrays.asList(path1, path2);
        DSObject dsObject = new DSObject(rawData);
        DSObject result = generalization.service(dsObject, 21, 1);
        for (Object s : result.getList()) {
            System.out.println(s);
        }
    }

    @Test
    public void testMedianAudio()  {
        // Generalization generalization = new GeneralizationImpl();
        File directory = new File("");
        String currentPath = directory.getAbsolutePath();
        String path1 = Paths.get(currentPath, "audio", "0001.wav").toString();
        String path2 = Paths.get(currentPath, "audio", "0001_median.wav").toString();
        List<String> rawData = Arrays.asList(path1, path2);
        DSObject dsObject = new DSObject(rawData);
        DSObject result = generalization.service(dsObject, 22, 1);
        for (Object s : result.getList()) {
            System.out.println(s);
        }
    }

    @Test
    // 测试两种算法的实现是否相同
    public void testKNum() {
        Random random = new Random();
        boolean allTestsPassed = true;
        for (int i = 0; i < 100; i++) {
            List<Double> numList = new ArrayList<>();
            int numElements = random.nextInt(100) + 1; // 生成1到20个元素
            for (int j = 0; j < numElements; j++) {
                numList.add(random.nextDouble() * 100); // 生成0到100之间的随机浮点数
            }
            int k = random.nextInt(numElements) + 1; // 确保k不为0

            List<Double> doubleList = dpUtil.kNum(numList, k);
            List<Double> doubleList2 = dpUtil.kNumNew(numList, k);

            if (!doubleList.equals(doubleList2)) {
                allTestsPassed = false;
                System.out.println("Test failed for:");
                System.out.println("List: " + numList);
                System.out.println("k_num result: " + doubleList);
                System.out.println("kNumNew result: " + doubleList2);
                break;
            }
        }
    }

//    @Test
    void testSubstring() {
        System.out.println("广西壮族自治区北海市逢时花园重庆苑a区".indexOf("自治区"));
    }
}

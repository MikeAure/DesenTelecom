package com.lu.gademo;

import com.lu.gademo.utils.DSObject;
import com.lu.gademo.utils.Generalization;
import com.lu.gademo.utils.impl.GeneralizationImpl;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
public class GeneralizationTest {

    @Test
    public void testTruncation()  {
        Generalization generalization = new GeneralizationImpl();
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
    }

    @Test
    public void testFloor()  {
        Generalization generalization = new GeneralizationImpl();
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
        Generalization generalization = new GeneralizationImpl();
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
        Generalization generalization = new GeneralizationImpl();
        List<String> rawData = Arrays.asList("陕西省西安市长安区西安电子科技大学南校区", "北京市海淀区北京大学");
        DSObject dsObject = new DSObject(rawData);
        DSObject result = generalization.service(dsObject, 4, 1);
        for (Object string : result.getList()) {
            System.out.println(string);
        }
        DSObject result0 = generalization.service(dsObject, 4, 0);
        for (Object string : result0.getList()) {
            System.out.println(string);
        }
    }

    @Test
    public void testDateGroupPlace()  {
        Generalization generalization = new GeneralizationImpl();
        List<String> rawData = Arrays.asList("2024-3-18", "2024-6-1");
        DSObject dsObject = new DSObject(rawData);
        DSObject result = generalization.service(dsObject, 5, 1);
        for (Object string : result.getList()) {
            System.out.println(string);
        }
        DSObject result0 = generalization.service(dsObject, 5, 0);
        for (Object string : result.getList()) {
            System.out.println(string);
        }
    }

    @Test
    public void testMixZone1()  {
        Generalization generalization = new GeneralizationImpl();
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
        Generalization generalization = new GeneralizationImpl();
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
        Generalization generalization = new GeneralizationImpl();
        String position = "116.435842,39.941626";
        DSObject dsObject = new DSObject(position);
        DSObject result = generalization.service(dsObject, 8);
        for (Object string : result.getList()) {
            System.out.println(string);
        }
    }

    @Test
    public void testPixLateImage()  {
        Generalization generalization = new GeneralizationImpl();
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
        Generalization generalization = new GeneralizationImpl();
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
        Generalization generalization = new GeneralizationImpl();
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
        Generalization generalization = new GeneralizationImpl();
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
        Generalization generalization = new GeneralizationImpl();
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
        Generalization generalization = new GeneralizationImpl();
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
        Generalization generalization = new GeneralizationImpl();
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
        Generalization generalization = new GeneralizationImpl();
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
        Generalization generalization = new GeneralizationImpl();
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
        Generalization generalization = new GeneralizationImpl();
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
        Generalization generalization = new GeneralizationImpl();
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
        Generalization generalization = new GeneralizationImpl();
        File directory = new File("");
        String currentPath = directory.getAbsolutePath();
        String path1 = Paths.get(currentPath, "audio", "0001.wav").toString();
        String path2 = Paths.get(currentPath, "audio", "0001_spec_mask").toString();
        List<String> rawData = Arrays.asList(path1, path2);
        DSObject dsObject = new DSObject(rawData);
        DSObject result = generalization.service(dsObject, 20);
        for (Object s : result.getList()) {
            System.out.println(s);
        }
    }

    @Test
    public void testAugmentationAudio()  {
        Generalization generalization = new GeneralizationImpl();
        File directory = new File("");
        String currentPath = directory.getAbsolutePath();
        String path1 = Paths.get(currentPath, "audio", "0001.wav").toString();
        String path2 = Paths.get(currentPath, "audio", "0001_aug.wav").toString();
        List<String> rawData = Arrays.asList(path1, path2);
        DSObject dsObject = new DSObject(rawData);
        DSObject result = generalization.service(dsObject, 21);
        for (Object s : result.getList()) {
            System.out.println(s);
        }
    }

    @Test
    public void testMedianAudio()  {
        Generalization generalization = new GeneralizationImpl();
        File directory = new File("");
        String currentPath = directory.getAbsolutePath();
        String path1 = Paths.get(currentPath, "audio", "0001.wav").toString();
        String path2 = Paths.get(currentPath, "audio", "0001_median.wav").toString();
        List<String> rawData = Arrays.asList(path1, path2);
        DSObject dsObject = new DSObject(rawData);
        DSObject result = generalization.service(dsObject, 22, 16);
        for (Object s : result.getList()) {
            System.out.println(s);
        }
    }
}

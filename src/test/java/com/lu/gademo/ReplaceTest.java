package com.lu.gademo;

import com.lu.gademo.utils.DSObject;
import com.lu.gademo.utils.Replace;
import com.lu.gademo.utils.impl.ReplaceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
public class ReplaceTest {
    private Replace replace;

    @Autowired
    public ReplaceTest(Replace replace) {
        this.replace = replace;
    }

    @Test
    public void testValueHide()  {
        // Replace replace = new ReplaceImpl();
        List<String> rawData = Arrays.asList("123", "456", "asdas123454", "dad123dfd3009sda");
        DSObject dsObject = new DSObject(rawData);
        DSObject result = replace.service(dsObject, 1,0);
        DSObject result1 = replace.service(dsObject, 1,1);
        DSObject result2 = replace.service(dsObject, 1,2);
        DSObject result3 = replace.service(dsObject, 1,3);
        result.getList().forEach(System.out::println);

        for (Object object : result1.getList()) {
            System.out.println(object);
        }
        result2.getList().forEach(System.out::println);
        result3.getList().forEach(System.out::println);
    }

    @Test
    public void testShift()  {
        // Replace replace = new ReplaceImpl();
        List<Double> rawData = Arrays.asList(1234567.0, 89102.0);
        DSObject dsObject = new DSObject(rawData);
        DSObject result = replace.service(dsObject, 2, 1);
        for (Object object : result.getList()) {
            System.out.println(object);
        }
    }

    @Test
    public void testHash()  {
        // Replace replace = new ReplaceImpl();
        List<String> rawData = Arrays.asList("123", "456");
        DSObject dsObject = new DSObject(rawData);
        DSObject result = replace.service(dsObject, 3, 0);
        DSObject result1 = replace.service(dsObject, 3, 1);
        DSObject result2 = replace.service(dsObject, 3, 2);
        DSObject result3 = replace.service(dsObject, 3, 3);
        for (Object object : result.getList()) {
            System.out.println(object);
        }
        result1.getList().forEach(System.out::println);
        result2.getList().forEach(System.out::println);
        result3.getList().forEach(System.out::println);
    }

    @Test
    public void testEnumeration()  {
        // Replace replace = new ReplaceImpl();
        List<Double> rawData = Arrays.asList(123.0, 456.0, 237.0);
        DSObject dsObject = new DSObject(rawData);
        DSObject result = replace.service(dsObject, 4, 1);
        for (Object object : result.getList()) {
            System.out.println(object);
        }
    }

    @Test
    public void testRandomReplace()  {
        // Replace replace = new ReplaceImpl();
        List<String> rawData = Arrays.asList("123", "456");
        DSObject dsObject = new DSObject(rawData);
        DSObject result = replace.service(dsObject, 5, 0);
        DSObject result1 = replace.service(dsObject, 5, 1);
        DSObject result2 = replace.service(dsObject, 5, 2);
        DSObject result3 = replace.service(dsObject, 5, 3);
        for (Object object : result.getList()) {
            System.out.println(object);
        }
        result1.getList().forEach(System.out::println);
        result2.getList().forEach(System.out::println);
        result3.getList().forEach(System.out::println);
    }

    // 信工所
    @Test
    public void testNameHide()  {
        // Replace replace = new ReplaceImpl();
        List<String> rawData = Arrays.asList("赵一二", "钱三四", "孙五六", "欧阳七八", "赵一二钱三四孙五六欧阳七八");
        DSObject dsObject = new DSObject(rawData);
        DSObject result = replace.service(dsObject, 6, 0);

        DSObject result1 = replace.service(dsObject, 6, 1);
        DSObject result2 = replace.service(dsObject, 6, 2);
        DSObject result3 = replace.service(dsObject, 6, 3);
        for (Object object : result.getList()) {
            System.out.println(object);
        }
        result1.getList().forEach(System.out::println);
        result2.getList().forEach(System.out::println);
        result3.getList().forEach(System.out::println);
    }

    // 信工所
    @Test
    public void testNumberHide()  {
        // Replace replace = new ReplaceImpl();
        List<String> rawData = Arrays.asList("19929384529", "7654321", "17789012345", "1992938452977654321", "1", "2", "22", "333", "4444");
        DSObject dsObject = new DSObject(rawData);
        DSObject result = replace.service(dsObject, 7, 0);
        DSObject result1 = replace.service(dsObject, 7, 1);
        DSObject result2 = replace.service(dsObject, 7, 2);
        DSObject result3 = replace.service(dsObject, 7, 3);

        for (Object object : result.getList()) {
            System.out.println(object);
        }
        result1.getList().forEach(System.out::println);
        result2.getList().forEach(System.out::println);
        result3.getList().forEach(System.out::println);
    }

    // 信工所
    @Test
    public void testSuppressEmail()  {
        // Replace replace = new ReplaceImpl();
        List<String> rawData = Arrays.asList("199@163.com", "qwertyuiop@qq.com", "217hdu1d17@gmail.com");
        DSObject dsObject = new DSObject(rawData);
        DSObject result0 = replace.service(dsObject, 8, 0);
        DSObject result1 = replace.service(dsObject, 8, 1);
        DSObject result2 = replace.service(dsObject, 8, 2);
        DSObject result3 = replace.service(dsObject, 8, 3);
        for (Object object : result0.getList()) {
            System.out.println(object);
        }
        result1.getList().forEach(System.out::println);
        result2.getList().forEach(System.out::println);
        result3.getList().forEach(System.out::println);

    }

    @Test
    public void testSuppressAllIPAddress()  {
        // Replace replace = new ReplaceImpl();
        List<String> rawData = Arrays.asList("192.168.1.1", "10.1.1.1", "127.0.0.1");
        DSObject dsObject = new DSObject(rawData);
        DSObject result = replace.service(dsObject, 9, 0);
        DSObject result1 = replace.service(dsObject, 9, 1);
        DSObject result2 = replace.service(dsObject, 9, 2);
        DSObject result3 = replace.service(dsObject, 9, 3);
        for (Object object : result.getList()) {
            System.out.println(object);
        }
        result1.getList().forEach(System.out::println);
        result2.getList().forEach(System.out::println);
        result3.getList().forEach(System.out::println);
    }

    @Test
    public void testSupressRandomIPAddress()  {
        // Replace replace = new ReplaceImpl();
        List<String> rawData = Arrays.asList("192.168.1.1", "10.1.1.1", "127.0.0.1");
        DSObject dsObject = new DSObject(rawData);
        DSObject result = replace.service(dsObject, 10, 0);
        DSObject result1 = replace.service(dsObject, 10, 1);
        DSObject result2 = replace.service(dsObject, 10, 2);
        DSObject result3 = replace.service(dsObject, 10, 3);
        for (Object object : result.getList()) {
            System.out.println(object);
        }
        result1.getList().forEach(System.out::println);
        result2.getList().forEach(System.out::println);
        result3.getList().forEach(System.out::println);
    }

    @Test
    public void testExchangeChannelImage()  {
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
    }

    @Test
    public void testAddColorOffsetImage()  {
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
    }

    @Test
    public void testFaceReplaceImage()  {
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
    }

    @Test
    public void testAddColorOffsetVideo()  {
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
    }

    @Test
    public void testFaceReplaceVideo()  {
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
    }

    @Test
    public void testBackgroundReplaceVideo()  {
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
    }

    @Test
    public void testAudioReshuffle()  {
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
    }

    @Test
    public void testAudioApplyEffects()  {
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
    }

    @Test
    public void testAudioReplaceVoicePrint()  {
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
    }

}

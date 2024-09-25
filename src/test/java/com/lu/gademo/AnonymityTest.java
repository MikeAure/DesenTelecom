package com.lu.gademo;

import com.lu.gademo.utils.DSObject;
import com.lu.gademo.utils.Anonymity;
import com.lu.gademo.utils.impl.AnonymityImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
public class AnonymityTest {
    @Autowired
    private Anonymity anonymity;

    @Test
    public void tesKAnonymity() {
//        Anonymity anonymity = new AnonymityImpl();
        File directory = new File("");
        String currentPath = directory.getAbsolutePath();
        String path1 = Paths.get(currentPath, "raw_files", "adult.csv").toString();
        String path2 = Paths.get(currentPath, "raw_files", "adult_k_anonymity.csv").toString();
        List<String> rawData = Arrays.asList(path1, path2);
        DSObject dsObject = new DSObject(rawData);
        DSObject result = anonymity.service(dsObject, 1, 2);
        for (Object object : result.getList()) {
            System.out.println(object);
        }
    }
    @Test
    public void testCirDummy()  {
        // Anonymity anonymity = new AnonymityImpl();
        DSObject dsObject = new DSObject("116.435842,39.941626");
        DSObject result = anonymity.service(dsObject, 2, 3, 10, 0.5);
        for (Object object : result.getList()) {
            System.out.println(object);
        }
    }

    @Test
    public void testGirDummy()  {
        // Anonymity anonymity = new AnonymityImpl();
        DSObject dsObject = new DSObject("116.435842,39.941626");
        DSObject result = anonymity.service(dsObject, 3, 6, 16);
        for (Object object : result.getList()) {
            System.out.println(object);
        }
    }

    @Test
    public void testLocationControllerAdaptiveIntervalCloaking()  {
        // Anonymity anonymity = new AnonymityImpl();
        List<String> rawData = Arrays.asList("39.921459,116.210864", "39.962555,116.291789");
        DSObject dsObject = new DSObject(rawData);
        dsObject.setString("39.952695,116.231002");
        DSObject result = anonymity.service(dsObject, 4, 2);
        for (Object object : result.getList()) {
            System.out.println(object);
        }
    }

    @Test
    public void testCaDSA()  {
        // Anonymity anonymity = new AnonymityImpl();
        DSObject dsObject = new DSObject("116.359642,39.659324");
        DSObject result = anonymity.service(dsObject, 5, 2);
        for (Object object : result.getList()) {
            System.out.println(object);
        }
    }

    @Test
    public void testKAnonymityPosition()  {
        // Anonymity anonymity = new AnonymityImpl();
        DSObject dsObject = new DSObject("116.359642,39.659324");
        DSObject result = anonymity.service(dsObject, 6, 2);
        for (Object object : result.getList()) {
            System.out.println(object);
        }
    }

    @Test
    public void tesLDiversity() {
        // Anonymity anonymity = new AnonymityImpl();
        File directory = new File("");
        String currentPath = directory.getAbsolutePath();
        String path1 = Paths.get(currentPath, "raw_files", "adult.csv").toString();
        String path2 = Paths.get(currentPath, "raw_files", "adult_l_diversity.csv").toString();
        List<String> rawData = Arrays.asList(path1, path2);
        DSObject dsObject = new DSObject(rawData);
        DSObject result = anonymity.service(dsObject, 7, 2);
        for (Object object : result.getList()) {
            System.out.println(object);
        }
    }

    @Test
    public void testTClose() {
        // Anonymity anonymity = new AnonymityImpl();
        File directory = new File("");
        String currentPath = directory.getAbsolutePath();
        String path1 = Paths.get(currentPath, "raw_files", "adult.csv").toString();
        String path2 = Paths.get(currentPath, "raw_files", "adult_t_close.csv").toString();
        List<String> rawData = Arrays.asList(path1, path2);
        DSObject dsObject = new DSObject(rawData);
        DSObject result = anonymity.service(dsObject, 10, 2);
        for (Object object : result.getList()) {
            System.out.println(object);
        }
    }

    @Test
    public void testHilbert()  {
        // Anonymity anonymity = new AnonymityImpl();
        DSObject dsObject = new DSObject("3.56230,4.36520");
        DSObject result = anonymity.service(dsObject, 11, 4);
        for (Object object : result.getList()) {
            System.out.println(object);
        }
    }

    @Test
    public void testSpaceTwist()  {
        // Anonymity anonymity = new AnonymityImpl();
        List<String> rawData = Arrays.asList("39.959201,116.319997", "40.139234,115.892943", "40.310622,117.003926", "39.560823,116.790982");
        DSObject dsObject = new DSObject(rawData);
        dsObject.setString("116.359642,39.659324");
        DSObject result = anonymity.service(dsObject, 12, 2);
        for (Object object : result.getList()) {
            System.out.println(object);
        }
    }
}


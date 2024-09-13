package com.lu.gademo;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lu.gademo.entity.ClassificationResult;
import com.lu.gademo.entity.ExcelParam;
import com.lu.gademo.service.ExcelParamService;
import com.lu.gademo.utils.impl.UtilImpl;
import org.deidentifier.arx.*;
import org.deidentifier.arx.criteria.KAnonymity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SpringBootTest
public class UtilsTest {
    @Autowired
    private ExcelParamService excelParamService;

    @Test
    public void testConda() {
        UtilImpl util = new UtilImpl();

        if (util.isCondaInstalled(util.isLinux())) {
            System.out.println("conda installed on this machine");
        } else {
            System.out.println("conda not installed");
        }
    }

    @Test
    public void testCurrentPath() {
        Path current = Paths.get("");
        System.out.println(current.toAbsolutePath());

    }

    @Test
    public void testPath() throws IOException {
        // 当前路径

        Path currentPath = Paths.get("");
        // 时间
        String fileTimeStamp = String.valueOf(System.currentTimeMillis());
        // 源文件保存目录
        Path rawDirectory = currentPath.resolve("raw_files");
        Path desenDirectory = currentPath.resolve("desen_files");

        if (!Files.exists(rawDirectory)) {
            Files.createDirectory(rawDirectory);
        }

        if (!Files.exists(desenDirectory)) {
            Files.createDirectory(desenDirectory);
        }
        // 文件名
        String rawFileName = "test.txt";
        String rawFileSuffix = rawFileName.substring(rawFileName.lastIndexOf(".") + 1);


        // 源文件保存路径
        Path rawFilePath = rawDirectory.resolve(rawFileName);
        byte[] content = new byte[40];
        FileInputStream fileInputStream = new FileInputStream(rawFilePath.toFile());
        while (fileInputStream.available() > 0) {
            fileInputStream.read(content);
        }
        System.out.println(new String(content, StandardCharsets.UTF_8));

        // 脱敏后文件信息
        String desenFileName = "desen_" + rawFileName;
        Path desenFilePath = desenDirectory.resolve("desen_" + rawFileName);

        System.out.println(currentPath.toAbsolutePath());
        System.out.println(rawFilePath.toAbsolutePath());
        System.out.println(desenFilePath.toAbsolutePath());
    }

    @Test
    void extractInfo() throws IOException {
        Path filePath = Paths.get("./temp.txt");
        String content = new String(Files.readAllBytes(filePath), StandardCharsets.UTF_8);

        String regex = "/fifty_scene/(\\w+)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(content);

        List<String> matches = new ArrayList<>();
        while (matcher.find()) {
            matches.add("\"" + matcher.group(1) + "\"");
        }

        System.out.println(matches);
    }

    @Test
    void extrctExcelFilesName() throws IOException {
        List<String> result = new ArrayList<>();
        Path excelFileDirectory = Paths.get("src/test/resources/test_data/sheets/Table");
        Files.list(excelFileDirectory).forEach(p -> {
            result.add("\"" + p.getFileName().toString() + "\"");
        });
        System.out.println(result);
    }

    @Test
    void testMax() {
        List<Double> content = Arrays.asList(10.0, 9.0, 8.0, 7.0, null, null);

        Double max = Collections.max(content);
        System.out.println(max);
    }

    @Test
    void testArx() throws IOException {
        // Define data
        Data.DefaultData data = Data.create();
        data.add("age", "gender", "zipcode");
        data.add("34", "male", "81667");
        data.add("45", "female", "81675");
        data.add("66", "male", "81925");
        data.add("70", "female", "81931");
        data.add("34", "female", "81931");
        data.add("70", "male", "81931");
        data.add("45", "male", "81931");

        // Define hierarchies
        AttributeType.Hierarchy.DefaultHierarchy age = AttributeType.Hierarchy.create();
        age.add("34", "<50", "*");
        age.add("45", "<50", "*");
        age.add("66", ">=50", "*");
        age.add("70", ">=50", "*");

        AttributeType.Hierarchy.DefaultHierarchy gender = AttributeType.Hierarchy.create();
        gender.add("male", "*");
        gender.add("female", "*");

        // Only excerpts for readability
        AttributeType.Hierarchy.DefaultHierarchy zipcode = AttributeType.Hierarchy.create();
        zipcode.add("81667", "8166*", "816**", "81***", "8****", "*****");
        zipcode.add("81675", "8167*", "816**", "81***", "8****", "*****");
        zipcode.add("81925", "8192*", "819**", "81***", "8****", "*****");
        zipcode.add("81931", "8193*", "819**", "81***", "8****", "*****");

        data.getDefinition().setAttributeType("age", age);
        data.getDefinition().setAttributeType("gender", gender);
        data.getDefinition().setAttributeType("zipcode", zipcode);

        // Create an instance of the anonymizer
        ARXAnonymizer anonymizer = new ARXAnonymizer();
        ARXConfiguration config = ARXConfiguration.create();
        config.addPrivacyModel(new KAnonymity(3));
        config.setSuppressionLimit(0d);

        ARXResult result = anonymizer.anonymize(data, config);

        System.out.println(" - Transformed data:");
        Iterator<String[]> transformed = result.getOutput(false).iterator();
        while (transformed.hasNext()) {
            System.out.print("   ");
            System.out.println(Arrays.toString(transformed.next()));
        }

    }

    @Test
    void test2DArrayString() {
        List<List<Double>> testArray = Arrays.asList(Arrays.asList(0.1, 0.3), Arrays.asList(0.3, 0.5), Arrays.asList(0.5, 0.7));
        for (Object item : testArray) {
            System.out.println(item);
        }
    }

    @Test
    void testCourseTwo() throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        Path path = Paths.get("./dataplatform_config.json");
        JsonNode columnList = objectMapper.readTree(path.toFile()).get("columnList");
        List<ClassificationResult> courseTwoList = objectMapper.readValue(columnList.toString(), new TypeReference<List<ClassificationResult>>() {
        });
        Map<String, Integer> courseTwoMap = new HashMap<>();

        for (ClassificationResult item : courseTwoList) {
            courseTwoMap.put("f_" + item.getColumnName(), item.getColumnLevel());
        }

        List<ExcelParam> lowStrategyConfig = excelParamService.getParamsByTableName("sada_gdpi_click_dtl_low_param");
        courseTwoList.forEach(System.out::println);
        lowStrategyConfig.forEach(System.out::println);
        courseTwoMap.keySet().forEach(System.out::println);
        for (ExcelParam item : lowStrategyConfig) {
            System.out.println(item.getColumnName());
            if (item.getColumnName().equals("sid") || item.getColumnName().equals("f_dataid") || item.getColumnName().equals("f_ts")) {
                continue;
            }
            Integer courseTwoMapTmParam = courseTwoMap.get(item.getColumnName());
            System.out.println(courseTwoMapTmParam);
            if (courseTwoMapTmParam == 4) {
                courseTwoMapTmParam = 3;
            }
            item.setTmParam(courseTwoMapTmParam > item.getTmParam() ? courseTwoMapTmParam : item.getTmParam());
        }

        lowStrategyConfig.forEach(System.out::println);
    }
}

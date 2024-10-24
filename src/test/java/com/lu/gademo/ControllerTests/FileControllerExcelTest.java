package com.lu.gademo.ControllerTests;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lu.gademo.entity.ExcelParam;
import com.lu.gademo.utils.Util;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class FileControllerExcelTest {

    private static final List<String> FIFTY_SCENE = Arrays.asList("map", "onlinetaxi", "communication", "community",
            "onlinepayment", "onlineshopping", "takeaway", "express", "transportationticket", "marry", "employment",
            "onlinelending", "house", "usedcar", "consultation", "travel", "hotel", "game", "education", "locallife",
            "woman", "usecar", "investment", "bank", "mailbox", "meeting", "webcast", "onlinemovie", "shortvideo",
            "news", "sports", "browser", "input", "security", "ebook", "capture", "appstore", "tools", "performanceticket",
            "networkaccess", "telecommunication", "monitor", "pay", "customerservice", "schoolservice", "smarthome",
            "autonomousdriving", "telemedicine", "vr", "onlinevoting");

    private static final List<String> FIFTYTWO_SCENE = Arrays.asList("map", "onlinetaxi", "communication", "community",
            "onlinepayment", "onlineshopping", "takeaway", "express", "transportationticket", "marry", "employment",
            "onlinelending", "house", "usedcar", "consultation", "travel", "hotel", "game", "education", "locallife",
            "woman", "usecar", "investment", "bank", "mailbox", "meeting", "webcast", "onlinemovie", "shortvideo",
            "news", "sports", "browser", "input", "security", "ebook", "capture", "appstore", "tools", "performanceticket",
            "networkaccess", "telecommunication", "monitor", "pay", "customerservice", "schoolservice", "smarthome",
            "autonomousdriving", "telemedicine", "vr", "onlinevoting", "homerepair", "petcare");

    private static final List<String> EXCEL_FILE_NAMES = Arrays.asList("appStore.xlsx", "autonomousdriving.xlsx", "bank.xlsx",
            "browser.xlsx", "capture.xlsx", "communication.xlsx", "community.xlsx", "consultation.xlsx", "customerservice.xlsx",
            "ebook.xlsx", "education.xlsx", "employment.xlsx", "express.xlsx", "game.xlsx", "hotel.xlsx", "house.xlsx", "input.xlsx",
            "investment.xlsx", "localLife.xlsx", "mailbox.xlsx", "map.xlsx", "marry.xlsx", "meeting.xlsx", "monitor.xlsx",
            "networkaccess.xlsx", "news.xlsx", "onlineLending.xlsx", "onlineMovie.xlsx", "onlinePayment.xlsx", "onlineShopping.xlsx",
            "onlineTaxi.xlsx", "onlinevoting.xlsx",
            "pay.xlsx", "performanceTicket.xlsx", "schoolservice.xlsx", "security.xlsx", "shortVideo.xlsx", "smarthome.xlsx",
            "sports.xlsx", "takeaway.xlsx", "telecommunication.xlsx", "telemedicine.xlsx", "tools.xlsx", "transportationTicket.xlsx",
            "travel.xlsx", "useCar.xlsx", "usedCar.xlsx", "vr.xlsx", "webcast.xlsx", "woman.xlsx"
    );

    private static final List<String> EXCEL_FILE_NAMES_52 = Arrays.asList("appStore.xlsx", "autonomousdriving.xlsx", "bank.xlsx",
            "browser.xlsx", "capture.xlsx", "communication.xlsx", "community.xlsx", "consultation.xlsx", "customerservice.xlsx",
            "ebook.xlsx", "education.xlsx", "employment.xlsx", "express.xlsx", "game.xlsx", "hotel.xlsx", "house.xlsx", "input.xlsx",
            "investment.xlsx", "localLife.xlsx", "mailbox.xlsx", "map.xlsx", "marry.xlsx", "meeting.xlsx", "monitor.xlsx",
            "networkaccess.xlsx", "news.xlsx", "onlineLending.xlsx", "onlineMovie.xlsx", "onlinePayment.xlsx", "onlineShopping.xlsx",
            "onlineTaxi.xlsx", "onlinevoting.xlsx",
            "pay.xlsx", "performanceTicket.xlsx", "schoolservice.xlsx", "security.xlsx", "shortVideo.xlsx", "smarthome.xlsx",
            "sports.xlsx", "takeaway.xlsx", "telecommunication.xlsx", "telemedicine.xlsx", "tools.xlsx", "transportationTicket.xlsx",
            "travel.xlsx", "useCar.xlsx", "usedCar.xlsx", "vr.xlsx", "webcast.xlsx", "woman.xlsx", "petcare.xlsx", "homerepair.xlsx"
    );
    private static final String URL = "/File/desenFile";
    private static final String EXCEL_FILE_PATH = "src/test/resources/test_data/sheets/Table/";
    private static final String EXCEL_FILE_PATH_1000w = "D:\\50scenes1000wdata_old\\";
    private final MockMvc mvc;
    Path excelPath;
    Path excelPath1000w;
    Util util;

    @Autowired
    public FileControllerExcelTest(MockMvc mvc, Util util) {
        this.mvc = mvc;
        this.excelPath = Paths.get(EXCEL_FILE_PATH);
        this.excelPath1000w = Paths.get(EXCEL_FILE_PATH_1000w);
        this.util = util;
    }

    // 模板参数获取
    @Test
    public void getExcelParam() throws Exception {
        System.out.println(FIFTY_SCENE.size());
//        for (String sceneName : FIFTY_SCENE) {

        String url = "/" + "map" + "param/list";
        // 模拟multipart/form-data请求
        MvcResult result = mvc.perform(get(url))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
        ObjectMapper objectMapper = new ObjectMapper();
        List<ExcelParam> excelParamList = objectMapper.readValue(result.getResponse().getContentAsString(StandardCharsets.UTF_8), new TypeReference<List<ExcelParam>>() {
        });
        excelParamList.stream().collect(Collectors.toMap(ExcelParam::getColumnName, Function.identity())).forEach((k, v) -> System.out.println(k + ": " + v));
//            System.out.println(result.getResponse().getContentAsString(StandardCharsets.UTF_8));
//        }
    }

    @Test
    public void excelFilesTest() throws Exception {
        for (String sceneName : FIFTY_SCENE) {

            String url = "/" + sceneName + "param/list";
            // 模拟multipart/form-data请求
            MvcResult excelParam = mvc.perform(get(url))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andDo(MockMvcResultHandlers.print())
                    .andReturn();

            String params = excelParam.getResponse().getContentAsString(StandardCharsets.UTF_8);
            System.out.println("Params: " + params);
            String fileName = EXCEL_FILE_NAMES.stream()
                    .filter(name -> name.substring(0, name.lastIndexOf("."))
                            .equalsIgnoreCase(sceneName))
                    .findFirst().get();
            System.out.println(fileName);
            byte[] excelBytes = Files.readAllBytes(excelPath.resolve(fileName));
            MockMultipartFile excelFile = new MockMultipartFile("file", fileName, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", excelBytes);

            mvc.perform(multipart(URL)
                            .file(excelFile)
                            .param("sheet", sceneName)
                            .param("params", params)
                            .param("algName", "distortion"))
                    .andExpect(status().isOk())
                    .andDo(MockMvcResultHandlers.print())
                    .andReturn();

        }

    }

    @Test
    public void testExcelFiles1000w() throws Exception {
        Map<String, List<String>> failedResult = new HashMap<>();
        for (int i = 1; i < 10; i++) {
            Path currentDirectory = excelPath1000w.resolve(String.valueOf(i));
            System.out.println("CurrentDirectory: " + currentDirectory.toString());
            List<String> failedFileNameList = new ArrayList<>();
            for (String sceneName : FIFTY_SCENE) {
                String[] strategy = new String[]{"_low", "_medium", "_high"};
                for (String elem : strategy) {
                    System.out.println("正在获取：" + sceneName + elem + "参数");
                    String url = "/" + sceneName + elem + "param/list";
                    // 模拟multipart/form-data请求
                    MvcResult excelParam = mvc.perform(get(url))
                            .andExpect(status().isOk())
                            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                            .andDo(MockMvcResultHandlers.print())
                            .andReturn();

                    String params = excelParam.getResponse().getContentAsString(StandardCharsets.UTF_8);
                    System.out.println("Params: " + params);
                    System.out.println("获取" + sceneName + elem + "成功");
                    String fileName = EXCEL_FILE_NAMES.stream()
                            .filter(name -> name.substring(0, name.lastIndexOf("."))
                                    .equalsIgnoreCase(sceneName))
                            .findFirst().get();
                    System.out.println(fileName);
                    Path testFilePath = currentDirectory.resolve(fileName);
                    System.out.println("正在测试文件：" + testFilePath.toAbsolutePath());
                    byte[] excelBytes = Files.readAllBytes(testFilePath);
                    MockMultipartFile excelFile = new MockMultipartFile("file", fileName,
                            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", excelBytes);

                    try {
                        mvc.perform(multipart(URL)
                                        .file(excelFile)
                                        .param("sheet", sceneName + elem)
                                        .param("params", params)
                                        .param("algName", "distortion"))
                                .andExpect(status().isOk())
                                .andDo(MockMvcResultHandlers.print())
                                .andReturn();
                    } catch (Exception e) {
                        failedFileNameList.add(fileName);
                    }
                }
            }
            failedResult.put(currentDirectory.toAbsolutePath().toString(), failedFileNameList);
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("failed_files.txt", true))) {
                for (Map.Entry<String, List<String>> failedRecord : failedResult.entrySet()) {
                    writer.write(String.valueOf(i) + ": " + failedRecord.getKey() + ": " + failedRecord.getValue().toString());
                    writer.newLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

    @Test
    void buildA100wExcel() throws IOException {
        FileInputStream file = new FileInputStream(excelPath.resolve("takeaway.xlsx").toAbsolutePath().toFile());
        Workbook workbook = new XSSFWorkbook(file);
        Sheet sheet = workbook.getSheetAt(0);

        // 2. 获取第一行之后的所有行
        int firstRowNum = sheet.getFirstRowNum();
        int lastRowNum = sheet.getLastRowNum();

        // 3. 复制行并附加
        for (int i = 1; i <= 100; i++) { // 复制100次
            for (int j = firstRowNum + 1; j <= lastRowNum; j++) {
                Row sourceRow = sheet.getRow(j);
                Row newRow = sheet.createRow(sheet.getLastRowNum() + 1);

                // 复制每一列
                for (int k = 0; k < sourceRow.getLastCellNum(); k++) {
                    Cell oldCell = sourceRow.getCell(k);
                    Cell newCell = newRow.createCell(k);

                    if (oldCell != null) {
                        newCell.setCellStyle(oldCell.getCellStyle());

                        switch (oldCell.getCellType()) {
                            case STRING:
                                newCell.setCellValue(oldCell.getStringCellValue());
                                break;
                            case NUMERIC:
                                newCell.setCellValue(oldCell.getNumericCellValue());
                                break;
                            case BOOLEAN:
                                newCell.setCellValue(oldCell.getBooleanCellValue());
                                break;
                            case FORMULA:
                                newCell.setCellFormula(oldCell.getCellFormula());
                                break;
                            case BLANK:
                                newCell.setBlank();
                                break;
                            default:
                                break;
                        }
                    }
                }
            }
        }

        // 4. 保存文件
        file.close();
        FileOutputStream outFile = new FileOutputStream("output100w.xlsx");
        workbook.write(outFile);
        outFile.close();
        workbook.close();

        System.out.println("复制完成并保存到output.xlsx");
    }

    @Test
        // 对100w条数据的测试
    void test100w() throws Exception {

        Map<String, List<String>> failedResult = new HashMap<>();

        Path currentDirectory = excelPath1000w.resolve(String.valueOf(0));
        System.out.println("CurrentDirectory: " + currentDirectory.toString());
        List<String> failedFileNameList = new ArrayList<>();

        String[] strategy = new String[]{"_low", "_medium", "_high"};

        System.out.println("正在获取：" + "MAP" + "LOW" + "参数");
        String url = "/" + "map_low" + "param/list";
        // 模拟multipart/form-data请求
        MvcResult excelParam = mvc.perform(get(url))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        String params = excelParam.getResponse().getContentAsString(StandardCharsets.UTF_8);
        System.out.println("Params: " + params);
        System.out.println("获取" + "MAP" + "LOW参数" + "成功");
//                    String fileName = EXCEL_FILE_NAMES.stream()
//                            .filter(name -> name.substring(0, name.lastIndexOf("."))
//                                    .equalsIgnoreCase(sceneName))
//                            .findFirst().get();
//                    System.out.println(fileName);
        Path testFilePath = currentDirectory.resolve("map10w.xlsx");
        System.out.println("正在测试文件：" + testFilePath.toAbsolutePath());
        byte[] excelBytes = Files.readAllBytes(testFilePath);
        MockMultipartFile excelFile = new MockMultipartFile("file", "map10w.xlsx",
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", excelBytes);

        try {
            mvc.perform(multipart(URL)
                            .file(excelFile)
                            .param("sheet", "map_low")
                            .param("params", params)
                            .param("algName", "distortion"))
                    .andExpect(status().isOk())
                    .andDo(MockMvcResultHandlers.print())
                    .andReturn();
        } catch (Exception e) {
            failedFileNameList.add("map10w.xlsx");
        }


        failedResult.put(currentDirectory.toAbsolutePath().toString(), failedFileNameList);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("failed_files.txt"))) {
            for (Map.Entry<String, List<String>> failedRecord : failedResult.entrySet()) {
                writer.write(failedRecord.getKey() + ": " + failedRecord.getValue().toString());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testExcelFiles52scenes() throws Exception {
        Map<String, List<String>> failedResult = new HashMap<>();
        Path currentDirectory = Paths.get("D:\\52scenes100");
        System.out.println("CurrentDirectory: " + currentDirectory.toString());
        List<String> failedFileNameList = new ArrayList<>();
        for (String sceneName : FIFTYTWO_SCENE) {
            String[] strategy = new String[]{"_low", "_medium", "_high"};
            for (String elem : strategy) {
                System.out.println("正在获取：" + sceneName + elem + "参数");
                String url = "/" + sceneName + elem + "param/list";
                // 模拟multipart/form-data请求
                MvcResult excelParam = mvc.perform(get(url))
                        .andExpect(status().isOk())
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                        .andDo(MockMvcResultHandlers.print())
                        .andReturn();

                String params = excelParam.getResponse().getContentAsString(StandardCharsets.UTF_8);
                System.out.println("Params: " + params);
                System.out.println("获取" + sceneName + elem + "成功");
                String fileName = EXCEL_FILE_NAMES_52.stream()
                        .filter(name -> name.substring(0, name.lastIndexOf("."))
                                .equalsIgnoreCase(sceneName))
                        .findFirst().get();
                System.out.println(fileName);
                Path testFilePath = currentDirectory.resolve(fileName);
                System.out.println("正在测试文件：" + testFilePath.toAbsolutePath());
                byte[] excelBytes = Files.readAllBytes(testFilePath);
                MockMultipartFile excelFile = new MockMultipartFile("file", fileName,
                        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", excelBytes);

                try {
                    mvc.perform(multipart(URL)
                                    .file(excelFile)
                                    .param("sheet", sceneName + elem)
                                    .param("params", params)
                                    .param("algName", "distortion"))
                            .andExpect(status().isOk())
                            .andDo(MockMvcResultHandlers.print())
                            .andReturn();
                } catch (Exception e) {
                    failedFileNameList.add(fileName);
                }

                failedResult.put(currentDirectory.toAbsolutePath().toString(), failedFileNameList);

            }
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("failed52_files.txt", true))) {
            for (Map.Entry<String, List<String>> failedRecord : failedResult.entrySet()) {
                writer.write(failedRecord.getKey() + ": " + failedRecord.getValue().toString());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @Test
    // 对大数据平台相关数据表读写进行测试
    public void testBigDataScene() throws Exception {
        Map<String, List<String>> failedResult = new HashMap<>();
        Path currentDirectory = Paths.get("C:\\Users\\Mike\\Desktop");
        System.out.println("CurrentDirectory: " + currentDirectory.toString());
        List<String> failedFileNameList = new ArrayList<>();
        String sceneName = "sada_gdpi_click_dtl";
        String[] strategy = new String[]{"_low", "_medium", "_high"};
        for (String elem : strategy) {
            System.out.println("正在获取：" + sceneName + elem + "参数");
            String url = "/" + sceneName + elem + "param/list";
            // 模拟multipart/form-data请求
            MvcResult excelParam = mvc.perform(get(url))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andDo(MockMvcResultHandlers.print())
                    .andReturn();

            String params = excelParam.getResponse().getContentAsString(StandardCharsets.UTF_8);
            System.out.println("Params: " + params);
            System.out.println("获取" + sceneName + elem + "成功");
            String fileName = "sada_gdpi_click_dtl.xlsx";
            System.out.println(fileName);
            Path testFilePath = currentDirectory.resolve(fileName);
            System.out.println("正在测试文件：" + testFilePath.toAbsolutePath());
            byte[] excelBytes = Files.readAllBytes(testFilePath);
            MockMultipartFile excelFile = new MockMultipartFile("file", fileName,
                    "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", excelBytes);

            try {
                mvc.perform(multipart(URL)
                                .file(excelFile)
                                .param("sheet", sceneName + elem)
                                .param("params", params)
                                .param("algName", "distortion"))
                        .andExpect(status().isOk())
                        .andDo(MockMvcResultHandlers.print())
                        .andReturn();
            } catch (Exception e) {
                failedFileNameList.add(fileName);
            }

            failedResult.put(currentDirectory.toAbsolutePath().toString(), failedFileNameList);

        }


        try (BufferedWriter writer = new BufferedWriter(new FileWriter("failed52_files.txt", true))) {
            for (Map.Entry<String, List<String>> failedRecord : failedResult.entrySet()) {
                writer.write(util.getTime() + ": " + failedRecord.getKey() + ": " + failedRecord.getValue().toString());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    // 测试修改字段后的52个场景
    @Test
    public void testExcelFilesNew52scenes() throws Exception {
        Map<String, List<String>> failedResult = new HashMap<>();

        Path currentDirectory = Paths.get("D:\\52scenes1w\\51");
        System.out.println("CurrentDirectory: " + currentDirectory.toString());
        List<String> failedFileNameList = new ArrayList<>();
        for (String sceneName : FIFTYTWO_SCENE) {
            String[] strategy = new String[]{"_low", "_medium", "_high"};
            for (String elem : strategy) {
                System.out.println("正在获取：" + sceneName + elem + "参数");
                String url = "/" + sceneName + elem + "param/list";
                // 模拟multipart/form-data请求
                MvcResult excelParam = mvc.perform(get(url))
                        .andExpect(status().isOk())
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                        .andDo(MockMvcResultHandlers.print())
                        .andReturn();

                String params = excelParam.getResponse().getContentAsString(StandardCharsets.UTF_8);
                System.out.println("Params: " + params);
                System.out.println("获取" + sceneName + elem + "成功");
                String fileName = EXCEL_FILE_NAMES_52.stream()
                        .filter(name -> name.substring(0, name.lastIndexOf("."))
                                .equalsIgnoreCase(sceneName))
                        .findFirst().get();
                System.out.println(fileName);
                Path testFilePath = currentDirectory.resolve(fileName);
                System.out.println("正在测试文件：" + testFilePath.toAbsolutePath());
                byte[] excelBytes = Files.readAllBytes(testFilePath);
                MockMultipartFile excelFile = new MockMultipartFile("file", fileName,
                        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", excelBytes);

                try {
                    mvc.perform(multipart(URL)
                                    .file(excelFile)
                                    .param("sheet", sceneName + elem)
                                    .param("params", params)
                                    .param("algName", "distortion"))
                            .andExpect(status().isOk())
                            .andDo(MockMvcResultHandlers.print())
                            .andReturn();
                } catch (Exception e) {
                    failedFileNameList.add(fileName);
                }

                failedResult.put(currentDirectory.toAbsolutePath().toString(), failedFileNameList);

            }
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("failed52_files.txt", true))) {
            for (Map.Entry<String, List<String>> failedRecord : failedResult.entrySet()) {
                writer.write(util.getTime() + ": " + failedRecord.getKey() + ": " + failedRecord.getValue().toString());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void bigFileTest() throws Exception {
        final String sceneName = "meeting";
        System.out.println("正在获取：" + sceneName + "参数");
        String url = "/" + sceneName + "param/list";
        // 模拟multipart/form-data请求
        MvcResult excelParam = mvc.perform(get(url))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        String params = excelParam.getResponse().getContentAsString(StandardCharsets.UTF_8);
        System.out.println("Params: " + params);
        System.out.println("获取" + sceneName + "成功");
//        String fileName = EXCEL_FILE_NAMES_52.stream()
//                .filter(name -> name.substring(0, name.lastIndexOf("."))
//                        .equalsIgnoreCase(sceneName))
//                .findFirst().get();
//        System.out.println(fileName);
        Path testFilePath = Paths.get("D:\\meeting5000wv3Excel\\meeting3.xlsx");
        System.out.println(testFilePath.toAbsolutePath());
        System.out.println("正在测试文件：" + testFilePath.toAbsolutePath());
        byte[] excelBytes = Files.readAllBytes(testFilePath);
        MockMultipartFile excelFile = new MockMultipartFile("file", "meeting100w.xlsx",
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", excelBytes);

        try {
            mvc.perform(multipart("/File/bigExcelDesen")
                            .file(excelFile)
                            .param("sheet", sceneName)
                            .param("params", params)
                            .param("algName", "distortion"))
                    .andExpect(status().isOk())
                    .andDo(MockMvcResultHandlers.print())
                    .andReturn();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void bigFileTestAll() throws Exception {
        Map<String, String> failedRecord = new HashMap<>();
        final String sceneName = "meeting";
        System.out.println("正在获取：" + sceneName + "参数");
        String url = "/" + sceneName + "param/list";
        // 模拟multipart/form-data请求
        MvcResult excelParam = mvc.perform(get(url))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        String params = excelParam.getResponse().getContentAsString(StandardCharsets.UTF_8);
        System.out.println("Params: " + params);
        System.out.println("获取" + sceneName + "成功");
//        String fileName = EXCEL_FILE_NAMES_52.stream()
//                .filter(name -> name.substring(0, name.lastIndexOf("."))
//                        .equalsIgnoreCase(sceneName))
//                .findFirst().get();
//        System.out.println(fileName);
        Path directoryPath = Paths.get("D:\\meeting5000wv3Excel\\");
        for (int i = 0; i < 500; i++) {
            Path testFilePath = directoryPath.resolve("meeting" + i + ".xlsx");
            System.out.println(testFilePath.toAbsolutePath());
            System.out.println("正在测试文件：" + testFilePath.toAbsolutePath());

            MockMultipartFile excelFile = new MockMultipartFile("file", "meeting100w" + i + ".xlsx",
                    "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", Files.newInputStream(testFilePath.toFile().toPath()));

            try {

                mvc.perform(multipart("/File/bigExcelDesen")
                                .file(excelFile)
                                .param("sheet", sceneName)
                                .param("params", params)
                                .param("algName", "distortion"))
                        .andExpect(status().isOk());

            } catch (Exception e) {
                failedRecord.put(util.getTime(), testFilePath.toAbsolutePath().toString());
                e.printStackTrace();
            }
            excelFile = null;
            Thread.sleep(1000); // 暂停1秒
            System.gc();
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("failed_bigfiles.txt", true))) {
            for (Map.Entry<String, String> record : failedRecord.entrySet()) {
                writer.write(record.getKey() + ": " + record.getValue());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

package com.lu.gademo.ControllerTests;

import org.apache.commons.collections4.multimap.HashSetValuedHashMap;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class FileControllerTextTest {
    public static final String FILE_PATH = "src/test/resources/test_data/text/";

    public static final String DATE_PATH = FILE_PATH + "date.xlsx";
    public static final String ADDRESS_PATH = FILE_PATH + "address.xlsx";
    public static final String EMAIL_PATH = FILE_PATH + "email.xlsx";
    public static final String TIME_PATH = FILE_PATH + "random_times.xlsx";
    public static final String IP_ADDRESS_PATH = FILE_PATH + "ip_address.xlsx";
    public static final String NAME_PATH = FILE_PATH + "name.xlsx";
    public static final String NUMBER_PATH = FILE_PATH + "number.xlsx";
    public static final String ENCODING_PATH = FILE_PATH + "encodings.xlsx";
    public static final String SERIAL_NUMBER_PATH = FILE_PATH + "serial_number.xlsx";

    public static final String url = "/File/desenSingleExcel";
    private final MockMvc mvc;
    private final HashMap<String,Path> pathMapper;


    @Autowired
    public FileControllerTextTest(MockMvc mvc) {
        this.mvc = mvc;

        this.pathMapper = new HashMap<String, Path>();
        this.pathMapper.put("date", Paths.get(DATE_PATH));
        this.pathMapper.put("address", Paths.get(ADDRESS_PATH));
        this.pathMapper.put("email", Paths.get(EMAIL_PATH));
        this.pathMapper.put("time", Paths.get(TIME_PATH));
        this.pathMapper.put("ip_address", Paths.get(IP_ADDRESS_PATH));
        this.pathMapper.put("name", Paths.get(NAME_PATH));
        this.pathMapper.put("number", Paths.get(NUMBER_PATH));
        this.pathMapper.put("encoding", Paths.get(ENCODING_PATH));
        this.pathMapper.put("serial_number", Paths.get(SERIAL_NUMBER_PATH));

    }

    @Test
    public void testDate() throws Exception {
        Path filePath = pathMapper.get("date");
        String fileName = filePath.getFileName().toString();
        System.out.println(fileName);
        MockMultipartFile file = new MockMultipartFile("file", fileName,
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                Files.readAllBytes(filePath));
        List<String> algNames = Arrays.asList("dpDate", "SHA512");
        for (int i = 1; i <= 3; i++) {
            for(String algName : algNames) {
                mvc.perform(multipart(url)
                        .file(file)
                        .param("params", String.valueOf(i))
                        .param("algName", algName))
                        .andExpect(status().isOk())
                    .andDo(print())
                    .andReturn();
            }
        }

    }

    @Test
    public void testTime() throws Exception {
        Path filePath = pathMapper.get("time");
        String fileName = filePath.getFileName().toString();
        System.out.println(fileName);
        MockMultipartFile file = new MockMultipartFile("file", fileName,
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                Files.readAllBytes(filePath));
        List<String> algNames = Arrays.asList("floorTime", "SHA512");
        for (int i = 1; i <= 3; i++) {
            for(String algName : algNames) {
                mvc.perform(multipart(url)
                                .file(file)
                                .param("params", String.valueOf(i))
                                .param("algName", algName))
                        .andExpect(status().isOk())
                        .andDo(print())
                        .andReturn();
            }
        }

    }

    @Test
    public void testAddress() throws Exception {
        Path filePath = pathMapper.get("address");
        String fileName = filePath.getFileName().toString();
        System.out.println(fileName);
        MockMultipartFile file = new MockMultipartFile("file", fileName,
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                Files.readAllBytes(filePath));
        List<String> algNames = Arrays.asList("addressHide", "SHA512","truncation", "value_hide");

        for (int i = 1; i <= 3; i++) {
            for(String algName : algNames) {
                mvc.perform(multipart(url)
                                .file(file)
                                .param("params", String.valueOf(i))
                                .param("algName", algName))
                        .andExpect(status().isOk())
                        .andDo(print())
                        .andReturn();
            }
        }

    }

    @Test
    public void testSerialNumber() throws Exception {
        Path filePath = pathMapper.get("serial_number");
        String fileName = filePath.getFileName().toString();
        System.out.println(fileName);
        MockMultipartFile file = new MockMultipartFile("file", fileName,
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                Files.readAllBytes(filePath));
        List<String> algNames = Arrays.asList("numberHide", "SHA512","truncation",
                "value_hide", "passReplace");

        for (int i = 1; i <= 3; i++) {
            for(String algName : algNames) {
                mvc.perform(multipart(url)
                                .file(file)
                                .param("params", String.valueOf(i))
                                .param("algName", algName))
                        .andExpect(status().isOk())
                        .andDo(print())
                        .andReturn();
            }
        }

    }

    @Test
    public void testNumber() throws Exception {
        Path filePath = pathMapper.get("number");
        String fileName = filePath.getFileName().toString();
        System.out.println(fileName);
        MockMultipartFile file = new MockMultipartFile("file", fileName,
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                Files.readAllBytes(filePath));
        List<String> algNames = Arrays.asList("gaussianToValue", "laplaceToValue","randomUniformToValue",
                "randomLaplaceToValue", "randomGaussianToValue", "valueShift",
                "SHA512", "floor", "valueMapping");

        for (int i = 1; i <= 3; i++) {
            for(String algName : algNames) {
                mvc.perform(multipart(url)
                                .file(file)
                                .param("params", String.valueOf(i))
                                .param("algName", algName))
                        .andExpect(status().isOk())
                        .andDo(print())
                        .andReturn();
            }
        }

    }

    @Test
    public void testName() throws Exception {
        Path filePath = pathMapper.get("name");
        String fileName = filePath.getFileName().toString();
        System.out.println(fileName);
        MockMultipartFile file = new MockMultipartFile("file", fileName,
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                Files.readAllBytes(filePath));
        List<String> algNames = Arrays.asList("nameHide", "SHA512");

        for (int i = 1; i <= 3; i++) {
            for(String algName : algNames) {
                mvc.perform(multipart(url)
                                .file(file)
                                .param("params", String.valueOf(i))
                                .param("algName", algName))
                        .andExpect(status().isOk())
                        .andDo(print())
                        .andReturn();
            }
        }

    }

    @Test
    public void testEmail() throws Exception {
        Path filePath = pathMapper.get("email");
        String fileName = filePath.getFileName().toString();
        System.out.println(fileName);
        MockMultipartFile file = new MockMultipartFile("file", fileName,
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                Files.readAllBytes(filePath));
        List<String> algNames = Arrays.asList("nameHide", "SHA512", "suppressEmail", "numberHide");

        for (int i = 1; i <= 3; i++) {
            for(String algName : algNames) {
                mvc.perform(multipart(url)
                                .file(file)
                                .param("params", String.valueOf(i))
                                .param("algName", algName))
                        .andExpect(status().isOk())
                        .andDo(print())
                        .andReturn();
            }
        }

    }

    @Test
    public void testCode() throws Exception {
        Path filePath = pathMapper.get("encoding");
        String fileName = filePath.getFileName().toString();
        System.out.println(fileName);
        MockMultipartFile file = new MockMultipartFile("file", fileName,
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                Files.readAllBytes(filePath));
        List<String> algNames = Collections.singletonList("dpCode");

        for (int i = 1; i <= 3; i++) {
            for(String algName : algNames) {
                mvc.perform(multipart(url)
                                .file(file)
                                .param("params", String.valueOf(i))
                                .param("algName", algName))
                        .andExpect(status().isOk())
                        .andDo(print())
                        .andReturn();
            }
        }

    }


}

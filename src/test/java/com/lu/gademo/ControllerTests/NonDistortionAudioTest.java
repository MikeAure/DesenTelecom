package com.lu.gademo.ControllerTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.File;
import java.nio.file.Files;
import java.util.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class NonDistortionAudioTest {
    @Autowired
    private MockMvc mockMvc;

    private List<String> names;
    private File folder;

    @BeforeEach
    public void setUp() {
        // 初始化name列表
        names = Arrays.asList("38966723", "38794459"); // 这里填入你的名字列表
        // 指定文件夹路径
        folder = new File("D:\\Programming\\DesenTelecom\\src\\test\\resources\\test_data\\audio\\nondistortion"); // 修改为你的文件夹路径
    }

    @Test
    public void testSignIn() throws Exception {
        Map<String, List<Map<String, String>>> resultMap = new HashMap<>();

        for (String name : names) {
            List<Map<String, String>> fileResults = new ArrayList<>();
            for (File file : folder.listFiles()) {

                if (file.isFile()) {
                    // 创建MockMultipartFile
                    MockMultipartFile mockFile = new MockMultipartFile(
                            "file",
                            file.getName(),
                            Files.probeContentType(file.toPath()),
                            Files.readAllBytes(file.toPath())
                    );
                    for (int i = 0; i < 10; i++) {
                        // 发送POST请求并获取响应
                        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.multipart("/audioMatch/signIn")
                                        .file(mockFile)
                                        .param("name", name))
                                .andExpect(status().isOk())
                                .andReturn();

                        // 将结果存入字典
                        Map<String, String> fileResult = new HashMap<>();
                        fileResult.put(file.getName() + String.valueOf(i+1), result.getResponse().getContentAsString());
                        fileResults.add(fileResult);
                    }
                }
            }
            resultMap.put(name, fileResults);
        }

        // 打印结果
        resultMap.forEach((key, value) -> {
            System.out.println("Name: " + key);
            value.forEach(fileResult -> fileResult.forEach((fileName, response) ->
                    System.out.println("File: " + fileName + ", Response: " + response)));
        });
    }
}

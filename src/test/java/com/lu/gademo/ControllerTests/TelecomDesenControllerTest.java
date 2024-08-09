package com.lu.gademo.ControllerTests;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.web.multipart.MultipartFile;


import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.CountDownLatch;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TelecomDesenControllerTest {
    private final MockMvc mvc;

    @Autowired
    public TelecomDesenControllerTest(MockMvc mvc) {
        this.mvc = mvc;
    }

    @Test
    void testGetExcelParam() throws Exception {
        String paramName = "customer_desen_msg_low";
        String sceneName = "customer_desen_msg";
        String url = "/" + paramName + "param/list";
        // 模拟multipart/form-data请求
        MvcResult excelParam = mvc.perform(get(url))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    @Test
    void testDesenExcel() throws Exception {
        String paramName = "customer_desen_msg_low";
        String sceneName = "customer_desen_msg";
        String url = "/" + paramName + "param/list";
        // 模拟multipart/form-data请求
        MvcResult excelParam = mvc.perform(get(url))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        CountDownLatch latch = new CountDownLatch(1);

        String params = excelParam.getResponse().getContentAsString(StandardCharsets.UTF_8);
        Path rawFilePath = Paths.get("D:\\Programming\\DesenTelecom\\customerMsgReflect2.xlsx");
        MockMultipartFile multipartFile = new MockMultipartFile("file", "customerMsgReflect2.xlsx",
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", Files.readAllBytes(rawFilePath));
        String URL = "/telecomDesen/desenExcel";
        mvc.perform(multipart(URL)
                        .file(multipartFile)
                        .param("params", params)
                        .param("strategy", "1")
                        .param("sheetName", sceneName))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }
}

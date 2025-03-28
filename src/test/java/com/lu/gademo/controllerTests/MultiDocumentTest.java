package com.lu.gademo.controllerTests;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.io.File;
import java.nio.file.Files;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
/**
 * 对新增的/File/multiDocument进行测试，用于处理docx xlsx pdf文件
 */
public class MultiDocumentTest {
    @Autowired
    MockMvc mockMvc;
    @Test
    public void testFileUploadAndDownload() throws Exception {

        byte[] file1Bytes = Files.readAllBytes(new File("D:\\test_data\\sheets\\103985.docx").toPath());

        MockMultipartFile file1 = new MockMultipartFile(
                "file",
                "otest.docx",
                "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
                file1Bytes
        );


        MvcResult result = mockMvc.perform(
                multipart("/File/multiDocument")
                        .file(file1)
                        .param("fileType", "docx"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andDo(print())
                .andReturn();

        byte[] responseContent = result.getResponse().getContentAsByteArray();
        assertThat(responseContent).isNotEmpty();
    }
}

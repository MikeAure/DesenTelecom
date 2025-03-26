package com.lu.gademo;

import com.lu.gademo.dto.FileInfoDto;
import com.lu.gademo.entity.FileStorageDetails;
import com.lu.gademo.utils.MultiDocumentProcessor;
import org.bytedeco.opencv.opencv_core.FileStorage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
public class DocxProcessorTest {

    @Autowired
    private MultiDocumentProcessor docxProcessor;

    @Test
    void testDesen() {
        FileStorageDetails fileStorageDetails = new FileStorageDetails();
        fileStorageDetails.setRawFilePathString("testWord.docx");
        fileStorageDetails.setDesenFilePathString("output4.docx");
        try {
            docxProcessor.processDocx(fileStorageDetails, new FileInfoDto("23333", "23333"));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}

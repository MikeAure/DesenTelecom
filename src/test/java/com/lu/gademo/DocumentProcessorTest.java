package com.lu.gademo;

import com.lu.gademo.dto.FileInfoDto;
import com.lu.gademo.entity.FileStorageDetails;
import com.lu.gademo.utils.MultiDocumentProcessor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
public class DocumentProcessorTest {

    @Autowired
    private MultiDocumentProcessor docxProcessor;

    @Test
    void testDocxDesen() {
        FileStorageDetails fileStorageDetails = new FileStorageDetails();
        fileStorageDetails.setRawFilePathString("D:\\test_data\\sheets\\103985MultiNames.docx");
        fileStorageDetails.setDesenFilePathString("D:\\test_data\\sheets\\103985out.docx");
        try {
            docxProcessor.processDocx(fileStorageDetails, new FileInfoDto("23333", "23333"));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    void testXlsxDesen() {
        FileStorageDetails fileStorageDetails = new FileStorageDetails();
        fileStorageDetails.setRawFilePathString("D:\\test_data\\sheets\\138950.xlsx");
        fileStorageDetails.setDesenFilePathString("D:\\test_data\\sheets\\138950out.xlsx");
        try{
            docxProcessor.processXlsx(fileStorageDetails, new FileInfoDto("xlsx", "23333"));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    void testPdfDesen() {
        FileStorageDetails fileStorageDetails = new FileStorageDetails();
        fileStorageDetails.setRawFilePathString("D:\\test_data\\103985MultiNames.pdf");
        fileStorageDetails.setDesenFilePathString("D:\\test_data\\103983out.pdf");
        try{
            docxProcessor.processPdf(fileStorageDetails, new FileInfoDto("pdf", "23333"));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    void testExcel() {

    }
}

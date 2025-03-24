package com.lu.gademo;

import com.lu.gademo.utils.MultiDocumentProcessor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@SpringBootTest
public class PdfTest {
    @Autowired
    MultiDocumentProcessor processor;
    @Test
    public void testPdf() throws IOException {
        List<Map<String, String>> result = processor.readDataFromPDF(Paths.get("D:\\test_data\\103983.pdf"));
        System.out.println(result);
    }
}

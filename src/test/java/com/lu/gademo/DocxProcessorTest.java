package com.lu.gademo;

import com.lu.gademo.utils.DocxProcessorIceBlue;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
public class DocxProcessorTest {

    @Autowired
    private DocxProcessorIceBlue docxProcessor;

    @Test
    void testDesen() {
        try {
            docxProcessor.processDocx("testWord.docx", "output4.docx");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}

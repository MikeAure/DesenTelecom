package com.lu.gademo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import com.lu.gademo.service.FileService;

import java.util.List;
import java.util.concurrent.ExecutionException;

@SpringBootTest
public class FileServiceTest {
    @Autowired
    private FileService fileService;

    @Test
    void testFetchRandomPayAmounts() throws ExecutionException, InterruptedException {
        List<Double> payAmounts = fileService.fetchRandomPayAmounts(6000000,500000, 10);
        System.out.println(payAmounts.size());
    }
}

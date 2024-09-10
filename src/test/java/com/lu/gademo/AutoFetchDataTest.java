package com.lu.gademo;

import com.lu.gademo.task.FetchDatabase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

// 对于大数据平台的测试
@SpringBootTest
public class AutoFetchDataTest {
    @Autowired
    private FetchDatabase fetchDatabase;

    @Test
    void testAutoFetchData() {
        try {
            System.out.println(fetchDatabase.fetchDatabaseAndDesen());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

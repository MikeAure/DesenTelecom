package com.lu.gademo;

import com.lu.gademo.mapper.ExcelParamDao;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ExcelParamMyBatisTest {
    @Autowired
    ExcelParamDao excelParamDao;

    @Test
    void testExcelParamDao() {
        System.out.println(excelParamDao.getTableParamsByName("telecomclient_param"));
    }
}

package com.lu.gademo;

import com.lu.gademo.entity.dataplatform.SadaGdpiClickDtl;
import com.lu.gademo.service.impl.DataPlatformDesenServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

@SpringBootTest
public class DataPlatformTest {
    @Autowired
    private DataPlatformDesenServiceImpl sadaDao;

    @Test
    void testGetAllRecordsFromTable() throws IOException, IllegalAccessException {
        List<SadaGdpiClickDtl> sadaGdpiClickDtl = sadaDao.getAllRecordsByTableName("sada_gdpi_click_dtl");
        sadaDao.writeToExcel(sadaGdpiClickDtl, sadaDao.getColumnMapping(), Paths.get("./").resolve("sada_gdpi_click_dtl.xlsx"));
    }
}

package com.lu.gademo;

import com.lu.gademo.entity.dataplatform.SadaGdpiClickDtl;
import com.lu.gademo.mapper.crm.CrmParamDao;
import com.lu.gademo.mapper.dataplatform.SadaGdpiClickDtlParamDao;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class DataPlatformMybatisTest {
    @Autowired
    private SadaGdpiClickDtlParamDao sadaDao;

    @Test
    void testGetAllRecordsFromTable() {
        sadaDao.getAllRecordsByTableName("sada_gdpi_click_dtl").forEach(System.out::println);
    }

    @Test
    void testDeleteAllFromTable() {

        sadaDao.deleteAll("sada_gdpi_click_dtl_low");
        sadaDao.getAllRecordsByTableName("sada_gdpi_click_dtl_low").forEach(System.out::println);
    }

    @Test
    void testSum() {
        int customerDesenMsgHigh = sadaDao.getItemTotalNumberByTabelName("sada_gdpi_click_dtl");
        System.out.println(customerDesenMsgHigh);
    }

    @Test
    void testDeleteById() {
        int deleteNum = sadaDao.deleteById("sada_gdpi_click_dtl", 202139566018L);
        System.out.println(deleteNum);
    }


}

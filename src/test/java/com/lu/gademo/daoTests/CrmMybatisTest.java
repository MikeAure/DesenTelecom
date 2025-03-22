package com.lu.gademo.daoTests;

import com.lu.gademo.mapper.crm.CrmParamDao;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CrmMybatisTest {
    @Autowired
    private CrmParamDao crmParamDao;

    @Test
    void testGetAllRecordsFromTable() {
        crmParamDao.getAllRecordsByTableName("customer_desen_msg").forEach(System.out::println);
    }

    @Test
    void testDeleteAllFromTable() {

        crmParamDao.deleteAll("customer_desen_msg");
        crmParamDao.getAllRecordsByTableName("customer_desen_msg").forEach(System.out::println);
    }

    @Test
    void testSum() {
        int customerDesenMsgHigh = crmParamDao.getItemTotalNumberByTabelName("customer_desen_msg_high");
        System.out.println(customerDesenMsgHigh);
    }

    @Test
    void testDeleteById() {
        int deleteNum = crmParamDao.deleteById("customer_desen_msg_high", 202139566018L);
        System.out.println(deleteNum);
    }
}

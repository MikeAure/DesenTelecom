package com.lu.gademo;

import com.lu.gademo.dao.crm.CustomerMsgDao;
import com.lu.gademo.dao.crm.TestTableDao;
import com.lu.gademo.dao.ga.effectEva.SendEvaReqDao;
import com.lu.gademo.entity.crm.CustomerMsg;
import com.lu.gademo.entity.crm.TestTable;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import com.lu.gademo.dao.ga.effectEva.RecEvaReqReceiptDao;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
public class JpaTest {
    @Autowired
    private RecEvaReqReceiptDao recEvaReqReceiptDao;
    @Autowired
    private TestTableDao testTableDao;
    @Autowired
    private SendEvaReqDao sendEvaReqDao;
    @Autowired
    private CustomerMsgDao customerMsgDao;

    @Test
    @Transactional
    // 添加Commit注解以实现删除
    @Commit
    void testQueryAndSave() {
        if (recEvaReqReceiptDao.existsByCertificateID("1722393070084")) {
            recEvaReqReceiptDao.deleteByCertificateID("1722393070084");
        }
        // 插入数据库
//        recEvaReqReceiptDao.save(recEvaReqReceipt);
    }

    @Test
    @Transactional
    void testQueryAndInsertCrm() {
        TestTable newUser = new TestTable(4L, "hhh", 12);
        testTableDao.findAll().forEach(System.out::println);
        testTableDao.save(newUser);
        testTableDao.findAll().forEach(System.out::println);

    }

    @Test
    @Transactional
    void testSendEvaRequestDao() {
        sendEvaReqDao.findById("431d8d678652a2d1c6f8c06d8f9cb9f514f752ac79b332ff1ffd4f706549c2d5").ifPresent(System.out::println);
    }

    @Test
    @Transactional
    void testCustomerMsgDao() {
        List<CustomerMsg> resultList = customerMsgDao.findAll();
        System.out.println(resultList.size());
    }
}

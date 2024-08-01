package com.lu.gademo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import com.lu.gademo.dao.effectEva.RecEvaReqReceiptDao;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class JpaTest {
    @Autowired
    private RecEvaReqReceiptDao recEvaReqReceiptDao;

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
}

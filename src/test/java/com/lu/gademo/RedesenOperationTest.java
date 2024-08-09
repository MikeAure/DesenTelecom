package com.lu.gademo;// 用于测试重新脱敏的功能

import com.lu.gademo.entity.ga.effectEva.RecEvaResultInv;
import com.lu.gademo.service.FileService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class RedesenOperationTest {

    @Autowired
    private FileService fileService;

    @Test
    void redesenTest() throws Exception {
        RecEvaResultInv recEvaResultInv = new RecEvaResultInv();
        recEvaResultInv.setDesenInfoAfterID("166a838a226cbd20870e10e298b88b2510987a7bbbfc86eff5d2e39a1f7db2b3");
        fileService.redesenExcel(recEvaResultInv);
    }

}

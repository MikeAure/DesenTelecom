package com.lu.gademo;

import com.lu.gademo.entity.ga.split.SendSplitDesenData;
import com.lu.gademo.model.LogSenderManager;
import com.lu.gademo.service.SplitSystemLogSender;
import com.lu.gademo.utils.LogCollectUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class LogSenderManagerTest {
    @Autowired
    private SplitSystemLogSender logSenderManager;
    @Autowired
    private LogCollectUtil logCollectUtil;

    @Test
    void testSplitData() {
        StringBuilder desenInfoAfterIden = new StringBuilder("desenInfoAfterIden");
        StringBuilder desenAlg = new StringBuilder("desenAlg");
        StringBuilder desenIntention = new StringBuilder("desenIntention");
        StringBuilder desenRequirements = new StringBuilder("desenRequirements");
        String controlSet = new String("controlset");
        StringBuilder desenAlgParam = new StringBuilder("desenAlgParam");
        String startTime = new String("startTime");
        String endTime = new String("endTime");
        StringBuilder desenLevel = new StringBuilder("desenLevel");
        String rawFileName = "rawFileName";
        String desenFileName = "desenFileName";
        byte[] rawFileBytes = new byte[20];
        byte[] desenFileBytes = new byte[20];

//        SendSplitDesenData dataResult = logCollectUtil
//                .buildSendSplitReq(desenInfoAfterIden, desenAlg, rawFileName, rawFileBytes, desenFileName, desenFileBytes, desenIntention,
//                        desenRequirements, controlSet, desenAlgParam, startTime, endTime, desenLevel, true);
//        logSenderManager.send2Split(dataResult, rawFileBytes);
    }
}

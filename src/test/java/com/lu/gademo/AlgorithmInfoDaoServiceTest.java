package com.lu.gademo;

import com.lu.gademo.dto.AlgorithmInfoParamDto;
import com.lu.gademo.service.AlgorithmInfoDaoService;
import com.lu.gademo.utils.AlgorithmInfo;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
public class AlgorithmInfoDaoServiceTest {
    @Autowired
    AlgorithmInfoDaoService algorithmInfoDaoService;

    @Test
    void getAllAlgorithmsRawInfoTest() {
        System.out.println(algorithmInfoDaoService.getAllAlgorithmsRawInfo());
    }

    @Test
    void getAllAlgorithmInfoConvertObjectTest() {
        List<AlgorithmInfo> allAlgorithmInfoConvertObject = algorithmInfoDaoService.getAllAlgorithmInfoConvertObject();
        AlgorithmInfo imageExchangeChannel = allAlgorithmInfoConvertObject.stream()
                .filter(algorithmInfo -> algorithmInfo.getName().equals("image_exchange_channel"))
                .findFirst().orElse(null);
        System.out.println(imageExchangeChannel.getParams().getClass().getName());
        System.out.println(imageExchangeChannel.getParams());
        assert CollectionUtils.isEmpty(imageExchangeChannel.getParams());
    }

    @Test
    void getAllAlgorithmInfoConvertObjectFromRawInfoTest() {
        AlgorithmInfo imageExchangeChannel = algorithmInfoDaoService.getAllAlgorithmInfoConvertObjectFromRawInfo().stream()
                .filter(algorithmInfo -> algorithmInfo.getName().equals("image_exchange_channel"))
                .findFirst().orElse(null);
        System.out.println(imageExchangeChannel.getParams().getClass().getName());
        System.out.println(imageExchangeChannel.getParams());
        assert CollectionUtils.isEmpty(imageExchangeChannel.getParams());

    }

    @Test
    void getAllAlgorithmInfoMapTest() {
        System.out.println(algorithmInfoDaoService.getAllAlgorithmInfoMap());
    }

    @Test
    void getAlgorithmInfoByIdTest() {
        System.out.println(algorithmInfoDaoService.getAlgorithmInfoById(2));
    }

    @Test
    void getAlgorithmInfoConvertObjectByIdTest() {
        System.out.println(algorithmInfoDaoService.getAlgorithmInfoConvertObjectById(2));
    }

    @Test
    void getAlgorithmInfoByTypeAndOriginalIdTest() {
        System.out.println(algorithmInfoDaoService.getAlgorithmInfoByTypeAndOriginalId(1, 1));
    }

    @Test
    void getAlgorithmInfoConvertObjectByTypeAndOriginalIdTest() {
        System.out.println(algorithmInfoDaoService.getAlgorithmInfoConvertObjectByTypeAndOriginalId(1, 1));
    }

    @Test
    @Transactional(transactionManager = "gaMybatisTransactionManager")
    void updateAlgorithmParamsTest() {
        System.out.println(algorithmInfoDaoService.updateAlgorithmParams(10, "114", "514", "191"));
    }

    @Test
    @Transactional(transactionManager = "gaMybatisTransactionManager")
    void updateAlgorithmParamsTest2() {
        System.out.println(algorithmInfoDaoService.updateAlgorithmParams(11, "114", "514", "191"));
    }

    @Test
    @Transactional(transactionManager = "gaMybatisTransactionManager")
    void updateAlgorithmParamsInBatch() {
        AlgorithmInfoParamDto algorithmInfoParamDto = new AlgorithmInfoParamDto(1, "2", "3", "5");
        AlgorithmInfoParamDto algorithmInfoParamDto1 = new AlgorithmInfoParamDto(2, "2", "1", "3");
        AlgorithmInfoParamDto algorithmInfoParamDto2 = new AlgorithmInfoParamDto(3, "1", "2", "3");
        System.out.println(algorithmInfoDaoService.updateAlgorithmParamsInBatch(
                Arrays.asList(algorithmInfoParamDto, algorithmInfoParamDto1, algorithmInfoParamDto2)));
    }
}

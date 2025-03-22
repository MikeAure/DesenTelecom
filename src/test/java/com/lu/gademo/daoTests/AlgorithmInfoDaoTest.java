package com.lu.gademo.daoTests;

import com.lu.gademo.dto.AlgorithmInfoParamDto;
import com.lu.gademo.mapper.ga.AlgorithmInfoDao;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

@SpringBootTest
public class AlgorithmInfoDaoTest {
    @Autowired
    AlgorithmInfoDao algorithmInfoDao;

    @Test
    void getAllAlgorithmInfoConvertObjectTest() {
        System.out.println(algorithmInfoDao.getAllAlgorithmInfoConvertObject());
    }

    @Test
    void getAllAlgorithmInfoTest() {
        algorithmInfoDao.getAllAlgorithmInfo().forEach(System.out::println);
    }

    @Test
    void getAllAlgorithmInfoDisplayTest() {
        algorithmInfoDao.getAllAlgorithmInfoDisplay().forEach(System.out::println);
    }

    @Test
    void getAlgorithmInfoByIdTest() {
        System.out.println(algorithmInfoDao.getAlgorithmInfoById(2));
    }

    @Test
    void getAlgorithmInfoByTypeAndOriginalIdTest() {
        System.out.println(algorithmInfoDao.getAlgorithmInfoByTypeAndOriginalId(1, 1));
    }

    @Test
    @Transactional(transactionManager = "gaMybatisTransactionManager")
    void updateAlgorithmParamsTest() {
        System.out.println(algorithmInfoDao.updateAlgorithmParams(1, "114", "514", "191"));
    }

    @Test
    @Transactional(transactionManager = "gaMybatisTransactionManager")
    void updateAlgorithmParamsUpdateBatchTest() {
        AlgorithmInfoParamDto algorithmInfoParamDto = new AlgorithmInfoParamDto(1, "2", "3", "5");
        AlgorithmInfoParamDto algorithmInfoParamDto1 = new AlgorithmInfoParamDto(2, "2", "1", "3");
        AlgorithmInfoParamDto algorithmInfoParamDto2 = new AlgorithmInfoParamDto(3, "1", "2", "3");
        System.out.println(algorithmInfoDao.updateAlgorithmParamsUpdateBatch(Arrays.asList(algorithmInfoParamDto, algorithmInfoParamDto1, algorithmInfoParamDto2)));
    }
}

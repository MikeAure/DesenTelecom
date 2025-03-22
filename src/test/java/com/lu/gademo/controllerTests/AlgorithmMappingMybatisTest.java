package com.lu.gademo.controllerTests;

import com.lu.gademo.service.AlgorithmMappingDaoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AlgorithmMappingMybatisTest {
    @Autowired
    private AlgorithmMappingDaoService algorithmMappingDaoService;

    @Test
    void selectAlgorithmIdByAttributeNameTest() {
        System.out.println(algorithmMappingDaoService.selectAlgorithmIdByAttributeName("护照号"));
    }

    @Test
    void selectAlgorithmInfoByAttributeNameByTest() {
        System.out.println(algorithmMappingDaoService.selectAlgorithmInfoByAttributeName("护照号"));
    }

}

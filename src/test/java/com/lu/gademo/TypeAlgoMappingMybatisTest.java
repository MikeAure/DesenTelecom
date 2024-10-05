package com.lu.gademo;

import com.lu.gademo.entity.ga.TypeAlgoMapping;
import com.lu.gademo.mapper.ga.TypeAlgoMappingDao;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class TypeAlgoMappingMybatisTest {
    @Autowired
    private TypeAlgoMappingDao typaAlgoMappingDao;

    @Test
    public void getTypeAlgoMapping() {
        List<TypeAlgoMapping> typeAlgoMappingList1 = typaAlgoMappingDao.getTypeAlgoMappingInfoByTypeId(19);
        List<TypeAlgoMapping> typeAlgoMappingList2 = typaAlgoMappingDao.getTypeAlgoMappingInfoByTypeId(4);
        List<TypeAlgoMapping> typeAlgoMappingList3 = typaAlgoMappingDao.getTypeAlgoMappingInfoByTypeId(20);
        List<TypeAlgoMapping> typeAlgoMappingList4 = typaAlgoMappingDao.getTypeAlgoMappingInfoByTypeId(21);

        typeAlgoMappingList1.forEach(System.out::println);
        typeAlgoMappingList2.forEach(System.out::println);
        typeAlgoMappingList3.forEach(System.out::println);
        typeAlgoMappingList4.forEach(System.out::println);
    }

    @Test
    public void getAlgorithmsByTypeName() {
        List<String> algNames1 = typaAlgoMappingDao.getAlgNamesByTypeName("标识信息");
        List<String> algNames2 = typaAlgoMappingDao.getAlgNamesByTypeName("个人位置信息");
        List<String> algNames3 = typaAlgoMappingDao.getAlgNamesByTypeName("联系人信息");
        List<String> algNames4 = typaAlgoMappingDao.getAlgNamesByTypeName("日期时间");

        algNames1.forEach(System.out::println);
        algNames2.forEach(System.out::println);
        algNames3.forEach(System.out::println);
        algNames4.forEach(System.out::println);
    }
}

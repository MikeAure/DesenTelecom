package com.lu.gademo.daoTests;

import com.lu.gademo.entity.ga.TypeAlgoMapping;
import com.lu.gademo.mapper.ga.TypeAlgoMappingDao;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class TypeAlgoMappingMybatisTest {
    @Autowired
    private TypeAlgoMappingDao typeAlgoMappingDao;

    @Test
    public void getTypeAlgoMapping() {
        List<TypeAlgoMapping> typeAlgoMappingList1 = typeAlgoMappingDao.getTypeAlgoMappingInfoByTypeId(19);
        List<TypeAlgoMapping> typeAlgoMappingList2 = typeAlgoMappingDao.getTypeAlgoMappingInfoByTypeId(4);
        List<TypeAlgoMapping> typeAlgoMappingList3 = typeAlgoMappingDao.getTypeAlgoMappingInfoByTypeId(20);
        List<TypeAlgoMapping> typeAlgoMappingList4 = typeAlgoMappingDao.getTypeAlgoMappingInfoByTypeId(21);

        typeAlgoMappingList1.forEach(System.out::println);
        typeAlgoMappingList2.forEach(System.out::println);
        typeAlgoMappingList3.forEach(System.out::println);
        typeAlgoMappingList4.forEach(System.out::println);
    }

    @Test
    public void getAlgorithmsByTypeName() {
        List<String> algNames1 = typeAlgoMappingDao.getAlgNamesByTypeName("标识信息");
        List<String> algNames2 = typeAlgoMappingDao.getAlgNamesByTypeName("个人位置信息");
        List<String> algNames3 = typeAlgoMappingDao.getAlgNamesByTypeName("联系人信息");
        List<String> algNames4 = typeAlgoMappingDao.getAlgNamesByTypeName("日期时间");
        List<String> algNames5 = typeAlgoMappingDao.getAlgNamesByTypeName("个人上网信息");
        List<String> algNames6 = typeAlgoMappingDao.getAlgNamesByTypeName("个人终端设备信息");
        List<String> algNames7 = typeAlgoMappingDao.getAlgNamesByTypeName("个人身份信息");
        List<String> algNames9 = typeAlgoMappingDao.getAlgNamesByTypeName("个人账户信息");

        System.out.println("标识信息");
        algNames1.forEach(System.out::println);
        System.out.println("个人位置信息");
        algNames2.forEach(System.out::println);
        System.out.println("联系人信息");
        algNames3.forEach(System.out::println);
        System.out.println("日期时间");
        algNames4.forEach(System.out::println);
        System.out.println("个人上网信息");
        algNames5.forEach(System.out::println);
        System.out.println("个人终端设备信息");
        algNames6.forEach(System.out::println);
        System.out.println("个人身份信息:");
        algNames7.forEach(System.out::println);
        System.out.println("个人账户信息:");
        algNames9.forEach(System.out::println);
    }
}

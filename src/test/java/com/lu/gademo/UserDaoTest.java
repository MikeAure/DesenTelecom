package com.lu.gademo;

import com.lu.gademo.entity.templateParam.onlineTaxi2Param;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import com.lu.gademo.dao.templateParam.onlineTaxi2ParamDao;

import java.util.List;


@SpringBootTest
public class UserDaoTest {
    @Autowired
    onlineTaxi2ParamDao onlineTaxi2ParamDao;
    @Test
    public void getColumnName() {
        StringBuilder stringBuilder = new StringBuilder();
        List<onlineTaxi2Param> list = onlineTaxi2ParamDao.findAll();
        for (onlineTaxi2Param element : list){
            stringBuilder.append(element.toString());
            stringBuilder.append("\n");
        }
        System.out.println(stringBuilder.toString());
    }
}

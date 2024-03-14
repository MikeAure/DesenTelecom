package com.lu.gademo;

import com.lu.gademo.entity.templateParam.onlineTaxi2Param;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import com.lu.gademo.dao.templateParam.onlineTaxi2ParamDao;
import com.lu.gademo.entity.templateParam.onlineTaxi2Param;

import java.util.List;

@RunWith(SpringRunner.class)
@DataJpaTest
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

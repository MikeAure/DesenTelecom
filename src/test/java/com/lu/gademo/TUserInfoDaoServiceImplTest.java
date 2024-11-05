package com.lu.gademo;

import com.lu.gademo.entity.jmtLogStock.TUserInfo;
import com.lu.gademo.service.TUserInfoDaoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

@SpringBootTest
public class TUserInfoDaoServiceImplTest {
    @Autowired
    TUserInfoDaoService tUserInfoDaoService;

    @Test
    public void testGetAllRecordsByTableName() {
        tUserInfoDaoService.getAllRecordsByTableName("t_user_info").stream()
                .limit(10).forEach(System.out::println);
    }

    /**
     * 测试单次分页请求的结果
     */
    @Test
    public void testSinglePageGetRecordsByTableNameAndPageInfo() {
        tUserInfoDaoService.getRecordsByTableNameAndPageInfo("t_user_info", 1, 10000).getList().stream()
                .limit(10).forEach(System.out::println);
    }

    /**
     * 测试多次分页请求的结果
     */
    @Test
    public void testMultiTimesGetRecordsByTableNameAndPageInfo() {
        for (int i = 0; i < 10; i++) {
            tUserInfoDaoService.getRecordsByTableNameAndPageInfo("t_user_info", i, 10000).getList().stream()
                    .limit(10).forEach(System.out::println);
        }
    }

    @Test
    @Transactional(transactionManager = "jmtLogStockMybatisTransactionManager")
    public void testDeleteByTableNameAndUserId() {
        System.out.println(tUserInfoDaoService.deleteByTableNameAndUserId("t_user_info", "4930745d446f41d3aa479bc2da00553d"));
    }

    @Test
    public void testInsertList() {
        Date birthday = new Date(2000, Calendar.JANUARY, 2, 3, 4, 5);
        Date modifyTime = new Date(System.currentTimeMillis());
        Date lastLogin = new Date(System.currentTimeMillis());
        Date createTime = new Date(System.currentTimeMillis());
        TUserInfo newUser = new TUserInfo(666666, "233233", 1, "test", "test",1,
                birthday, 1, 1, "test", createTime, "test", "test",
                modifyTime, "test", 1, 1, lastLogin);

        System.out.println(tUserInfoDaoService.insertList("t_user_info_desen", Collections.singletonList(newUser)));
    }
}

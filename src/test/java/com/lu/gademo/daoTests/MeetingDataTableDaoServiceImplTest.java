package com.lu.gademo.daoTests;

import com.github.pagehelper.PageInfo;
import com.lu.gademo.entity.ga.MeetingDataTable;
import com.lu.gademo.service.impl.MeetingDataTableDaoServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class MeetingDataTableDaoServiceImplTest {
    @Autowired
    MeetingDataTableDaoServiceImpl meetingDataTableDaoServiceImpl;
    /**
     * 测试分页查询
     */
    @Test
    void testPageQuery() {
        PageInfo<MeetingDataTable> meetingDataTablePageInfo = meetingDataTableDaoServiceImpl.getRecordsByTableNameAndPageInfo("meeting_datatable", 1, 100);
        System.out.println("总页数：" + meetingDataTablePageInfo.getPages());
        System.out.println("导航页数：" + meetingDataTablePageInfo.getNavigatePages());
        System.out.println("当前页：" + meetingDataTablePageInfo.getPageNum());
        List<MeetingDataTable> results = meetingDataTablePageInfo.getList();
        results.stream().limit(10).forEach(System.out::println);
    }

    /**
     * 测试使用不同的页码进行多次分页查询
     */
    @Test
    void testLoopPageQuery() {
        for (int i = 1; i <= 2; i++) {
            PageInfo<MeetingDataTable> meetingDataTablePageInfo = meetingDataTableDaoServiceImpl.getRecordsByTableNameAndPageInfo("meeting_datatable", i, 50000);
            System.out.println("总页数：" + meetingDataTablePageInfo.getPages());
            System.out.println("导航页数：" + meetingDataTablePageInfo.getNavigatePages());
            System.out.println("当前页：" + meetingDataTablePageInfo.getPageNum());
            System.out.println("下一页：" + meetingDataTablePageInfo.getNextPage());
            System.out.println("是否为第一页：" + meetingDataTablePageInfo.isIsFirstPage());
            System.out.println("是否为最后一页：" + meetingDataTablePageInfo.isIsLastPage());
            List<MeetingDataTable> results = meetingDataTablePageInfo.getList();
            results.stream().limit(10).forEach(System.out::println);
        }
    }
}

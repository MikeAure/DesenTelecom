package com.lu.gademo.service;

import com.github.pagehelper.PageInfo;
import com.lu.gademo.entity.ga.MeetingDataTable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface MeetingDataTableDaoService {
    List<MeetingDataTable> getAllRecordsByTableName(String name);

    PageInfo<MeetingDataTable> getRecordsByTableNameAndPageInfo(String name, int pageNum, int pageSize);

    @Transactional(transactionManager = "gaMybatisTransactionManager")
    int deleteAll(String tableName);

    @Transactional(transactionManager = "gaMybatisTransactionManager")
    int deleteByTableNameAndPhoneNumber(String tableName, Long phoneNumber);

    int insertList(String tableName, List<MeetingDataTable> dataList);

    void insertListInBatch(String tableName, List<MeetingDataTable> dataList, int batchSize);

    @Transactional(transactionManager = "gaMybatisTransactionManager")
    void deleteAndInsertInBatch(String tableName, List<MeetingDataTable> list, int batchSize);

    @Transactional(transactionManager = "gaMybatisTransactionManager")
    void deleteAndInsert(String tableName, List<MeetingDataTable> list);
}

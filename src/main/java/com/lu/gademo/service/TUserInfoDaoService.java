package com.lu.gademo.service;

import com.github.pagehelper.PageInfo;
import org.springframework.transaction.annotation.Transactional;
import com.lu.gademo.entity.jmtLogStock.TUserInfo;

import java.util.List;

public interface TUserInfoDaoService {
    List<TUserInfo> getAllRecordsByTableName(String name);

    PageInfo<TUserInfo> getRecordsByTableNameAndPageInfo(String name, int pageNum, int pageSize);

    @Transactional(transactionManager = "jmtLogStockMybatisTransactionManager")
    int deleteAll(String tableName);

    @Transactional(transactionManager = "jmtLogStockMybatisTransactionManager")
    int deleteByTableNameAndUserId(String tableName, String userId);

    int insertList(String tableName, List<TUserInfo> dataList);

    void insertListInBatch(String tableName, List<TUserInfo> dataList, int batchSize);

    @Transactional(transactionManager = "jmtLogStockMybatisTransactionManager")
    void deleteAndInsertInBatch(String tableName, List<TUserInfo> list, int batchSize);

    @Transactional(transactionManager = "jmtLogStockMybatisTransactionManager")
    void deleteAndInsert(String tableName, List<TUserInfo> list);
}

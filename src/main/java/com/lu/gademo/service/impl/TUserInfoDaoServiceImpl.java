package com.lu.gademo.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lu.gademo.entity.jmtLogStock.TUserInfo;
import com.lu.gademo.mapper.jmtLogStock.TUserInfoDao;
import com.lu.gademo.service.TUserInfoDaoService;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Slf4j
@Getter
@Setter
@Service
public class TUserInfoDaoServiceImpl implements TUserInfoDaoService {
    private final TUserInfoDao tUserInfoDao;

    @Autowired
    public TUserInfoDaoServiceImpl(TUserInfoDao tUserInfoDao) {
        this.tUserInfoDao = tUserInfoDao;
    }

    @Override
    public List<TUserInfo> getAllRecordsByTableName(String name) {
        return tUserInfoDao.getAllRecordsByTableName(name);
    }

    @Override
    public PageInfo<TUserInfo> getRecordsByTableNameAndPageInfo(String name, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<TUserInfo> tUserInfoList = tUserInfoDao.getAllRecordsByTableName(name);

        return new PageInfo<>(tUserInfoList);
    }

    @Transactional(transactionManager = "jmtLogStockMybatisTransactionManager")
    @Override
    public int deleteAll(String tableName) {
        return tUserInfoDao.deleteAll(tableName);
    }

    @Transactional(transactionManager = "jmtLogStockMybatisTransactionManager")
    @Override
    public int deleteByTableNameAndUserId(String tableName, String userId) {
        return tUserInfoDao.deleteByTableNameAndUserId(tableName, userId);
    }


    @Override
    public int insertList(String tableName, List<TUserInfo> dataList) {
        return tUserInfoDao.insertList(tableName, dataList);
    }

    @Override
    public void insertListInBatch(String tableName, List<TUserInfo> dataList, int batchSize) {
        for (int i = 0; i < dataList.size(); i+=batchSize) {
            List<TUserInfo> batch = dataList.subList(i, Math.min(i + batchSize, dataList.size()));
            insertList(tableName, batch);
        }
    }

    @Transactional(transactionManager = "jmtLogStockMybatisTransactionManager")
    @Override
    public void deleteAndInsertInBatch(String tableName, List<TUserInfo> list, int batchSize) {
        if (tUserInfoDao.getItemTotalNumberByTableName(tableName) > 0) {
            tUserInfoDao.deleteAll(tableName);
        }
        // 分批次插入，一次性插入大量数据数据库会报错
        insertListInBatch(tableName, list, batchSize);
    }

    @Transactional(transactionManager = "jmtLogStockMybatisTransactionManager")
    @Override
    public void deleteAndInsert(String tableName, List<TUserInfo> list) {
        if (tUserInfoDao.getItemTotalNumberByTableName(tableName) > 0) {
            tUserInfoDao.deleteAll(tableName);
        }

        insertList(tableName, list);
    }
}

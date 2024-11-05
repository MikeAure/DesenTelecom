package com.lu.gademo.service.impl;

import com.github.pagehelper.PageInfo;
import com.lu.gademo.entity.ga.MeetingDataTable;
import com.lu.gademo.mapper.ga.MeetingDataTableDao;
import com.lu.gademo.service.MeetingDataTableDaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.github.pagehelper.PageHelper;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MeetingDataTableDaoServiceImpl implements MeetingDataTableDaoService {
    private final MeetingDataTableDao meetingDataTableDao;

    @Autowired
    public MeetingDataTableDaoServiceImpl(MeetingDataTableDao meetingDataTableDao) {
        this.meetingDataTableDao = meetingDataTableDao;
    }

    @Override
    public List<MeetingDataTable> getAllRecordsByTableName(String name) {
        return meetingDataTableDao.getAllRecordsByTableName(name);
    }

    @Override
    public PageInfo<MeetingDataTable> getRecordsByTableNameAndPageInfo(String name, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<MeetingDataTable> meetingList = meetingDataTableDao.getAllRecordsByTableName(name);

        return new PageInfo<>(meetingList);
    }

    @Transactional(transactionManager = "gaMybatisTransactionManager")
    @Override
    public int deleteAll(String tableName) {
        if (meetingDataTableDao.getItemTotalNumberByTableName(tableName) > 0) {
            return meetingDataTableDao.deleteAll(tableName);
        }
        return 0;
    }

    @Transactional(transactionManager = "gaMybatisTransactionManager")
    @Override
    public int deleteByTableNameAndPhoneNumber(String tableName, Long phoneNumber) {
        return meetingDataTableDao.deleteByTableNameAndPhoneNumber(tableName, phoneNumber);
    }

    @Override
    public int insertList(String tableName, List<MeetingDataTable> dataList) {
        return meetingDataTableDao.insertList(tableName, dataList);
    }

    @Override
    public void insertListInBatch(String tableName, List<MeetingDataTable> dataList, int batchSize) {
        for (int i = 0; i < dataList.size(); i+=batchSize) {
            List<MeetingDataTable> batch = dataList.subList(i, Math.min(i + batchSize, dataList.size()));
            insertList(tableName, batch);
        }
    }
    @Transactional(transactionManager = "gaMybatisTransactionManager")
    @Override
    public void deleteAndInsertInBatch(String tableName, List<MeetingDataTable> list, int batchSize) {
        if (meetingDataTableDao.getItemTotalNumberByTableName(tableName) > 0) {
            meetingDataTableDao.deleteAll(tableName);
        }
        // 分批次插入，一次性插入大量数据数据库会报错
        insertListInBatch(tableName, list, batchSize);
    }

    @Transactional(transactionManager = "gaMybatisTransactionManager")
    @Override
    public void deleteAndInsert(String tableName, List<MeetingDataTable> list) {
        if (meetingDataTableDao.getItemTotalNumberByTableName(tableName) > 0) {
            meetingDataTableDao.deleteAll(tableName);
        }

        insertList(tableName, list);
    }
}

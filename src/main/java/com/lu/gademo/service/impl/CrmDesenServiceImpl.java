package com.lu.gademo.service.impl;

import com.lu.gademo.entity.crm.CustomerDesenMsg;
import com.lu.gademo.mapper.crm.CrmParamDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CrmDesenServiceImpl implements com.lu.gademo.service.CrmDesenService {
    @Autowired
    private CrmParamDao crmParamDao;

    @Override
    public int deleteById(String tableName, Long id) {
        return crmParamDao.deleteById(tableName, id);
    }

    @Override
    public int deleteAll(String tableName) {
        return crmParamDao.deleteAll(tableName);
    }

    @Transactional(transactionManager = "crmMybatisTransactionManager")
    @Override
    public void deleteAndInsert(String tableName, List<CustomerDesenMsg> list) {
        if (crmParamDao.getItemTotalNumberByTabelName(tableName) > 0) {
            crmParamDao.deleteAll(tableName);
        }
        crmParamDao.insertList(tableName, list);
    }

    @Override
    public int insertList(String tableName, List<CustomerDesenMsg> list) {
        return crmParamDao.insertList(tableName, list);
    }

    @Override
    public List<CustomerDesenMsg> getAllRecordsByTableName(String tableName) {
        return crmParamDao.getAllRecordsByTableName(tableName);
    }
}

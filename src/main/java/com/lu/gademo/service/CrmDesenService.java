package com.lu.gademo.service;

import com.lu.gademo.entity.crm.CustomerDesenMsg;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CrmDesenService {
    int deleteById(String tableName, Long id);

    int deleteAll(String tableName);

    @Transactional(transactionManager = "crmMybatisTransactionManager")
    void deleteAndInsert(String tableName, List<CustomerDesenMsg> list);

    int insertList(String tableName, List<CustomerDesenMsg> list);

    List<CustomerDesenMsg> getAllRecordsByTableName(String tableName);
}

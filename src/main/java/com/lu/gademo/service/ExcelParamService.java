package com.lu.gademo.service;

import com.lu.gademo.entity.ExcelParam;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ExcelParamService {
    List<ExcelParam> getParamsByTableName(String name);

    @Transactional(transactionManager = "gaMybatisTransactionManager")
    void deleteAll(String name);

    @Transactional(transactionManager = "gaMybatisTransactionManager")
    void insertAll(String name, List<ExcelParam> dataList);

    List<ExcelParam> findTable(String name);

    void deleteByTabelNameAndId(String name, int id);

    List<ExcelParam> getByTableNameAndDataType(String name, Integer dataType);

}

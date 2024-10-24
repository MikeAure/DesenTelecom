package com.lu.gademo.service.impl;

import com.lu.gademo.entity.ExcelParam;
import com.lu.gademo.mapper.ga.ExcelParamDao;
import com.lu.gademo.service.ExcelParamService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ExcelParamServiceImpl implements ExcelParamService {

    private final ExcelParamDao excelParamDao;

    public ExcelParamServiceImpl (ExcelParamDao excelParamDao) {
        this.excelParamDao = excelParamDao;
    }

    public List<ExcelParam> getParamsByTableName(String name) {
        return excelParamDao.getTableParamsByName(name);
    }

    @Transactional(transactionManager = "gaMybatisTransactionManager")
    public void deleteAll(String name) {
        excelParamDao.deleteAll(name);
    }

    @Transactional(transactionManager = "gaMybatisTransactionManager")
    public void insertAll(String name, List<ExcelParam> dataList) {
        excelParamDao.insertAll(name, dataList);
    }

    public List<ExcelParam> findTable(String name) {
        return excelParamDao.findTable(name);
    }

    public void deleteByTabelNameAndId(String name, int id) {
        excelParamDao.deleteByTableNameAndId(name, id);
    }

    public List<ExcelParam> getByTableNameAndDataType(String name, Integer dataType) {
        return excelParamDao.getByTableNameAndDataType(name, dataType);
    }

}

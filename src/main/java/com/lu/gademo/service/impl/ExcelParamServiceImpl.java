package com.lu.gademo.service.impl;

import com.lu.gademo.entity.ExcelParam;
import com.lu.gademo.mapper.ExcelParamDao;
import com.lu.gademo.service.ExcelParamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExcelParamServiceImpl implements ExcelParamService {
    @Autowired
    private ExcelParamDao excelParamDao;

    public List<ExcelParam> getParams(String name) {
        return excelParamDao.getTableParamsByName(name);
    }

    public void deleteAll(String name) {
        excelParamDao.deleteAll(name);
    }

    public void insertAll(String name, List<ExcelParam> dataList) {
        excelParamDao.saveTableParams(name, dataList);
    }


}

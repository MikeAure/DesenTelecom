package com.lu.gademo.service.impl;

import com.lu.gademo.entity.ExcelParam;
import com.lu.gademo.mapper.ExcelParamDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ExcelParamServiceImpl {
    @Resource
    private ExcelParamDao excelParamDao;

    public List<ExcelParam> getParams(String name) {
        return excelParamDao.getParams(name);
    }

    public void deleteAll(String name) {
        excelParamDao.deleteAll(name);
    }


    public void insertAll(String name, List<ExcelParam> dataList) {
        excelParamDao.insertAll(name, dataList);
    }


}

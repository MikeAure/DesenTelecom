package com.lu.gademo.service;

import com.lu.gademo.entity.ExcelParam;

import java.util.List;

public interface ExcelParamService {
    List<ExcelParam> getParams(String name);
    void deleteAll(String name);
    void insertAll(String name, List<ExcelParam> dataList);
}

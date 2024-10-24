package com.lu.gademo.service;

import com.lu.gademo.entity.ExcelParam;

import java.util.List;

public interface ExcelParamService {
    List<ExcelParam> getParamsByTableName(String name);
    void deleteAll(String name);
    void insertAll(String name, List<ExcelParam> dataList);
    List<ExcelParam> findTable(String name);
    void deleteByTabelNameAndId(String name, int id);
    List<ExcelParam> getByTableNameAndDataType(String name, Integer dataType);

}

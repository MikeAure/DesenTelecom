package com.lu.gademo.dao.templateParam;

import com.lu.gademo.dao.support.IBaseDao;
import com.lu.gademo.entity.templateParam.HotelParam;
import org.springframework.stereotype.Repository;

import java.util.List;

//此文件开始定义查询函数接口
@Repository
public interface HotelParamDao extends IBaseDao<HotelParam, Integer> {
    //根据中文说明，查询出数据
    HotelParam findByColumnName(String columnName);

    List<HotelParam> findByDataType(Integer datatype);
}


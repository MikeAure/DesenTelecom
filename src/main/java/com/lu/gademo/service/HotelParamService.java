package com.lu.gademo.service;


import com.lu.gademo.entity.templateParam.HotelParam;
import com.lu.gademo.service.support.IBaseService;

import java.util.List;

public interface HotelParamService extends IBaseService<HotelParam, Integer> {

    //查询根据旅店查询
    public HotelParam findByChineseName(String ChineseName);
    public List<HotelParam> findBydatatype(Integer query);


}

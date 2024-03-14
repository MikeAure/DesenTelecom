package com.lu.gademo.service.impl;


import com.lu.gademo.dao.templateParam.HotelParamDao;
import com.lu.gademo.dao.support.IBaseDao;
import com.lu.gademo.entity.templateParam.HotelParam;
import com.lu.gademo.service.HotelParamService;
import com.lu.gademo.service.support.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HotelParamServiceImpl extends BaseServiceImpl<HotelParam,Integer> implements HotelParamService {
    @Autowired
    private HotelParamDao hotelParamDao;
    //查询根据旅店查询
    public HotelParam findByChineseName(String ChineseName){
        return  hotelParamDao.findByColumnName(ChineseName);}


    @Override
    public IBaseDao<HotelParam, Integer> getBaseDao() {
        return this.hotelParamDao;
    }
    @Override
    public List<HotelParam> findBydatatype(Integer query) {
        return hotelParamDao.findByDataType(query);
    }
}

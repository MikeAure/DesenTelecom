package com.lu.gademo.service.impl;


import com.lu.gademo.dao.DpHotelDao;
import com.lu.gademo.dao.support.IBaseDao;
import com.lu.gademo.entity.DpHotel;
import com.lu.gademo.service.DpHotelService;
import com.lu.gademo.service.support.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DpHotelServiceImpl extends BaseServiceImpl<DpHotel,Integer> implements DpHotelService {
    @Autowired
    private DpHotelDao dphotelDao;

    @Override
    public IBaseDao<DpHotel,Integer> getBaseDao() {
        return this.dphotelDao;
    }

    @Override
    public List<DpHotel> findByLvName(String lvname) {

        return dphotelDao.findByldmc(lvname);
    }

    @Override
    public Page<DpHotel> findAllLike1(String ldmc, String lddz,PageRequest pageRequest) {
        if (ldmc==null) {lddz="";}
        if(lddz==null){lddz="";}
        return dphotelDao.findByLdmcContainsAndLddzmcContains(ldmc,lddz,pageRequest);
    }
    @Override
    public Page<DpHotel> findByID(String id, PageRequest pageRequest) {
        return dphotelDao.findById(Integer.valueOf(id), pageRequest);
    }

    @Override
    public Page<DpHotel> findByName(String name, PageRequest pageRequest) {
        return dphotelDao.findByLdmcContains(name, pageRequest);
    }

}

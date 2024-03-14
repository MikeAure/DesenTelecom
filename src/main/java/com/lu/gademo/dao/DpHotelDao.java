package com.lu.gademo.dao;

import com.lu.gademo.dao.support.IBaseDao;
import com.lu.gademo.entity.DpHotel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

//此文件开始定义查询函数接口
@Repository
public interface DpHotelDao extends IBaseDao<DpHotel,Integer> {
    //这个需要自定义查询方法名,根据旅店名查询
    List<DpHotel> findByldmc(String ldmc);

    //   //根据旅店，地址，此方法可以传入空值
    Page<DpHotel> findByLdmcContainsAndLddzmcContains(String ldmc, String lddz, Pageable pageable);
    Page<DpHotel> findById(Integer id, Pageable pageable);
    // 根据名称
    Page<DpHotel> findByLdmcContains(String name, Pageable pageable);


}


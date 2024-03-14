package com.lu.gademo.service;

import com.lu.gademo.entity.DpHotel;
import com.lu.gademo.service.support.IBaseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface DpHotelService extends IBaseService<DpHotel, Integer> {

    //查询根据旅店查询
    List<DpHotel> findByLvName(String lvname);

    //按照旅店名称和所在地查询
    Page<DpHotel> findAllLike1(String ldmc, String lddz , PageRequest pageRequest);
    // ID
    Page<DpHotel> findByID(String id, PageRequest pageRequest);
    // 姓名
    Page<DpHotel> findByName(String name, PageRequest pageRequest);
}

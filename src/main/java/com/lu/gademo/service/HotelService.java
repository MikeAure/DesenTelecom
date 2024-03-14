package com.lu.gademo.service;

import com.lu.gademo.entity.Hotel;
import com.lu.gademo.entity.templateParam.HotelParam;
import com.lu.gademo.service.support.IBaseService;
import com.lu.gademo.vo.NumsView;
import com.lu.gademo.vo.SingleView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public interface HotelService extends IBaseService<Hotel, Integer> {

//    //查询根据旅店查询
//    List<Hotel> findByLvName(String lvname);

    //按照旅店名称和所在地查询
    Page<Hotel> findAllLike1(String ldmc, String lddz , PageRequest pageRequest);
    // ID
    Page<Hotel> findByID(String id, PageRequest pageRequest);
    // 姓名
    Page<Hotel> findByName(String name, PageRequest pageRequest);


    //脱敏处理
    public void tmOperate(List<HotelParam> params) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException;

    //统计数值型查询
    List<NumsView> statisticNums(String[]  colNames, String showType) throws  Exception;

    //统计单编码数据
    List<SingleView> statisticSingles(String[]  colNames) throws Exception;


}

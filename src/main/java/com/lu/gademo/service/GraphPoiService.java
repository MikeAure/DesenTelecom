package com.lu.gademo.service;

import com.lu.gademo.entity.Poi;

import java.util.List;

public interface GraphPoiService {
    /**
     * 通过ID获取POI信息
     * @param id POI ID
     * @return POI信息
     */
    Poi getPoiById(String id);
    List<Poi> selectAllPoi();
    List<Poi>selectPoisByIds(List<String> ids);
}

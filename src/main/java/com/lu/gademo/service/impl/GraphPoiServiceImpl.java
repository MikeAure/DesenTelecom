package com.lu.gademo.service.impl;

import com.lu.gademo.entity.Poi;
import com.lu.gademo.mapper.ga.GraphPoiDao;
import com.lu.gademo.service.GraphPoiService;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
@Data
public class GraphPoiServiceImpl implements GraphPoiService {
    private final GraphPoiDao graphPoiDao;
    @Override
    public Poi getPoiById(String id) {
        return graphPoiDao.selectPoiById(id);
    }

    @Override
    public List<Poi> selectAllPoi() {
        return graphPoiDao.selectAllPoi();
    }

    @Override
    public List<Poi> selectPoisByIds(List<String> ids) {
        return graphPoiDao.selectPoisByIds(ids);
    }
}

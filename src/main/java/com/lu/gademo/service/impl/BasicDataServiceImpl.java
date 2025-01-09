package com.lu.gademo.service.impl;

import com.lu.gademo.entity.ga.LatLong;
import com.lu.gademo.mapper.ga.BasicDataDao;
import com.lu.gademo.service.BasicDataService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@AllArgsConstructor
@Service
public class BasicDataServiceImpl implements BasicDataService {
    private final BasicDataDao basicDataDao;

    @Override
    public List<Double> getPayAmountInRange(int start, int range) {
        return basicDataDao.selectPayAmountInRange(start, range);
    }

    @Override
    public List<LatLong> getLatitudeAndLongitudeInRange(int start, int range) {
        return basicDataDao.selectLatitudeAndLongitudeInRange(start, range);
    }
}

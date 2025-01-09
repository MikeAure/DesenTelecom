package com.lu.gademo.service;

import com.lu.gademo.entity.ga.LatLong;

import java.util.List;

public interface BasicDataService {
    List<Double> getPayAmountInRange(int start, int range);
    List<LatLong> getLatitudeAndLongitudeInRange(int start, int range);
}

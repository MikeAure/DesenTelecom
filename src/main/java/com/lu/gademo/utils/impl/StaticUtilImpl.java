package com.lu.gademo.utils.impl;

import com.lu.gademo.utils.StaticUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StaticUtilImpl implements StaticUtil {

    @Override
    public Double average(List<Object> list) {
        List<Double> re_data = new ArrayList<>();
        for (Object data : list) {
            if (data instanceof Integer) {
                if (data == null) {
                    re_data.add(null);
                } else {
                    Integer data1 = (Integer) data;
                    re_data.add(data1.doubleValue());
                }
            } else if (data instanceof Double) {
                if (data == null) {
                    re_data.add(null);
                } else {
                    Double data1 = (Double) data;
                    re_data.add(data1.doubleValue());
                }
            }
        }
        //求和
        double sum = re_data.stream().reduce(Double::sum).orElse(0.0);
        double aver = sum / re_data.size();
        aver = (double) Math.round(aver * 1000) / 1000;
        return aver;
    }


    @Override
    public Double median(List<Object> list) {
        List<Double> re_data = new ArrayList<>();
        for (Object data : list) {
            if (data instanceof Integer) {
                if (data == null) {
                    re_data.add(null);
                } else {
                    Integer data1 = (Integer) data;
                    re_data.add(data1.doubleValue());
                }
            } else if (data instanceof Double) {
                if (data == null) {
                    re_data.add(null);
                } else {
                    Double data1 = (Double) data;
                    re_data.add(data1.doubleValue());
                }
            }
        }
        //排序
        Collections.sort(re_data);
        // 生成中位数
        double median;
        if (re_data.size() == 0) {
            median = 0;
        } else if (re_data.size() % 2 == 0) {
            median = (re_data.get(re_data.size() / 2 - 1) + re_data.get(re_data.size() / 2)) / 2;
        } else {
            median = re_data.get(re_data.size() / 2);
        }
        return median;
    }


    @Override
    public Double variance(List<Object> list) {
        List<Double> re_data = new ArrayList<>();
        for (Object data : list) {
            if (data instanceof Integer) {
                if (data == null) {
                    re_data.add(null);
                } else {
                    Integer data1 = (Integer) data;
                    re_data.add(data1.doubleValue());
                }
            } else if (data instanceof Double) {
                if (data == null) {
                    re_data.add(null);
                } else {
                    Double data1 = (Double) data;
                    re_data.add(data1.doubleValue());
                }
            }
        }
        double average = this.average(list);
        double var = 0.0;
        for (double p : re_data) {
            var += (p - average) * (p - average);
        }
        double variance = var / re_data.size();
        variance = (double) Math.round(variance * 1000) / 1000;
        return variance;

    }
}
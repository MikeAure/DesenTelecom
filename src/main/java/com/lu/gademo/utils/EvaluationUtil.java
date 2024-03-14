package com.lu.gademo.utils;

import java.util.List;
import java.util.Map;

//数据评测的算法
public interface EvaluationUtil {

    //偏差性度量
    //均方误差
    Double calculateMSE(List<Object> originalData, List<Object> desensitizedData);

    Double calculateMSE(Map<Object, Double> prob1, Map<Object, Double> prob2);

    //平均绝对误差
    Double calculateMAE(List<Object> originalData, List<Object> desensitizedData);

    Double calculateMAE(Map<Object, Double> prob1, Map<Object, Double> prob2);

    //分布频率
    //生成一个频率分布的直方图(折线图、饼图)
    Map<Object, Double> calculateHistogram(List<Object> data);

    //Kl散度
    Double calculateKLDivergence(Map<Object, Double> p, Map<Object, Double> q);

    //信息熵
    Double calculateEntropy(List<Object> list);

    Double calculateEntropy(Map<Object, Double> probabilities);

    // 信息熵差值
    Double calculateEntropyDifference(List<Object> originalData, List<Object> desensitizedData);

    double calculateEntropyDifference(
            Map<Object, Double> probabilityDistribution1,
            Map<Object, Double> probabilityDistribution2);

    //欧氏距离
    Double calculateEuclideanDistance(List<Object> originalData, List<Object> desensitizedData);

    double calculateEuclideanDistance(Map<Object, Double> prob1, Map<Object, Double> prob2);

}

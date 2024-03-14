package com.lu.gademo.utils;

import java.io.IOException;

public interface DimensionUtil {

    //复杂性，脱敏算法复杂性
    //根据 脱敏时间/脱敏条数 进行计算时间复杂性，级别越高越不复杂，脱敏效果越好
    Integer calculateComplexity(String StartTime, String EndTime) throws IOException;

    //延伸控制性，根据脱敏控制集合进行判断，存在脱敏控制集合则为5，脱敏效果好
    Integer calculateExtendedcontrol(String desenControlSet);

    //可逆性，目前全部不可逆
    Integer calculateReversibility(Integer desenAlg);

    //偏差性，使用欧式距离评判
    Integer calculateDeviation(Double EuclideanDistance);

    //信息损失性，使用信息熵差值进行评判，如果信息熵差值较大，则说明损失较大
    Integer calculateInformationloss(Double EntropyDifference);
}

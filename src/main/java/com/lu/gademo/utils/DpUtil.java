package com.lu.gademo.utils;

import java.sql.Date;
import java.text.ParseException;
import java.util.List;


//差分处理的算法
public interface DpUtil {
    List<Double> kNum(List<Double> array, int k);
    List<Double> kNumNew(List<Double> array, int k);
    //单编码数据的处理
    List<String> dpCode(List<Object> data, double epsilon);

    //多编码数据处理,字符串拼接,（待定）
    List<String> MulCode();

    //数值型数据处理  laplace dp
    List<Double> laplaceToValue(List<Object> data, double epsilon);

    //数值k-分组抑制
    List<Double> kNumberCode(List<Object> data, Integer privacyLevel);

    // 取整
    List<Double> getInt(List<Object> data, Integer privacyLevel);

    // 差分 添加高斯噪声
    List<Double> gaussianToValue(List<Object> data, Integer privacyLevel);

    //添加随机高斯噪声
    List<Double> randomGaussianToValue(List<Object> data, double stdDev);

    //添加随机拉普拉斯噪声
    List<Double> randomLaplaceToValue(List<Object> datas, double betad);

    // 添加随机均匀噪声
    List<Double> randomUniformToValue(List<Object> datas, double am);

    // 数值shift
    List<String> valueShift(List<Object> data, double shift);

    //日期处理
    List<Date> dpDate(List<Object> data, double epsilon) throws ParseException;

    // 日期分组置换
    List<Date> dateGroupReplace(List<Object> data, int k) throws ParseException;

    //名称脱敏算法，名字处理方式,(卢**)
    List<String> nameHide(List<Object> names, Integer privacyLevel);

    // 编号脱敏算法，电话号码或编号的处理，136****1203
    List<String> numberHide(List<Object> telephones, int denominator);

    // 身份号码的处理
    String IDCode(String ID, String zs, Date birthday, String sex);
//
    String IDCode(String ID, Date birthday, String sex);

    // 信息主键编号处理
    String infoID(String id, String name, Date time, String hotel);

    //文本区域的处理（eg.地址，公司名，车的类型）,商量
    List<String> TextCode(List<Object> texts);

    // 地址脱敏算法，地址处理
    List<String> addressHide(List<Object> addrs, Integer privacyLevel);

    // 地名处理
    List<String> desenAddressName(List<Object> addrs, Integer privacyLevel, String name);

    // 基于随机字符的字符串混淆方法
    List<String> passReplace(List<Object> addrs, int max);

    // 尾部截断只保留前3位
    List<String> truncation(List<Object> dataList, int remains);

    // 数值取整
    List<String> floor(List<Object> dataList, String privacyLevel);

    // 时间取整  12:00:00
    List<String> floorTime(List<Object> dataList, Integer privacyLevel);

    // 将字符串中数字替换为0
    List<String> valueHide(List<Object> dataList, Integer privacyLevel);

    //数值映射为新值  *50
    List<String> valueMapping(List<Object> dataList, double scale);

    //SHA512 哈希
    List<String> SHA512(List<Object> dataList, Integer privacyLevel);

    // 邮箱地址隐藏
    List<String> suppressEmail(List<Object> dataList, Integer privacyLevel);

    // IP地址随机替换
    List<String> suppressIpRandomParts(List<Object> dataList, Integer privacyLevel);

    /**
     * 隐藏所有IP地址
     * @param dataList
     * @param privacyLevel
     * @return
     */
    List<String> suppressAllIp(List<Object> dataList, Integer privacyLevel);


}


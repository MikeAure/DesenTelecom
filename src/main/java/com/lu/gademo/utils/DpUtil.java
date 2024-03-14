package com.lu.gademo.utils;

import java.sql.Date;
import java.text.ParseException;
import java.util.List;

//差分处理的算法
public interface DpUtil {
    //单编码数据的处理
    public List<String> dpCode(List<Object> data, Integer privacyLevel);

    //多编码数据处理,字符串拼接,（待定）
    public  List<String> MulCode();

    //数值型数据处理  laplace dp
    public  List<Double> laplaceToValue(List<Object> data, Integer privacyLevel);
    //数值k-分组抑制
    public  List<Double>  k_NumberCode(List<Object> data,Integer privacyLevel);
    // 取整
    public List<Double> getInt(List<Object> data, Integer privacyLevel);
    // 差分 添加高斯噪声
    public List<Double> gaussianToValue(List<Object> data, Integer privacyLevel);

    //添加随机高斯噪声
    public List<Double> randomGaussianToValue(List<Object> data, Integer privacyLevel);

    //添加随机拉普拉斯噪声
    public List<Double> randomLaplaceToValue(List<Object> data, Integer privacyLevel);
    // 添加随机均匀噪声
    public List<Double> randomUniformToValue(List<Object> data, Integer privacyLevel);

    // 数值shift
    public List<Double> valueShift(List<Object> data, Integer privacyLevel);

    //日期处理
    public  List<Date> dpDate(List<Object> data, Integer privacyLevel) throws ParseException;
    // 日期分组置换
    public  List<Date> date_group_replace(List<Object> data, Integer privacyLevel) throws ParseException;
    //名字处理方式,(卢**)
    public  List<String> nameHide(List<Object> names, Integer privacyLevel);

    //电话号码或编号的处理，136****1203
    public  List<String> numberHide(List<Object> telephones, Integer privacyLevel);

    //身份号码的处理
    public  String IDCode(String ID, String zs, Date birthday,String sex);
    public  String IDCode(String ID, Date birthday,String sex);
    // 信息主键编号处理
    public String infoID(String id, String name, Date time, String hotel);

    //文本区域的处理（eg.地址，公司名，车的类型）,商量
    public List<String> TextCode(List<Object> texts);
    // 地址处理
    public List<String> addressHide(List<Object> addrs, Integer privacyLevel);
    // 地名处理
    public List<String> desenAddressName(List<Object> addrs, Integer privacyLevel, String name);
    public List<String> passReplace(List<Object> addrs, Integer privacyLevel);

    // 尾部截断只保留前3位
    public List<String> truncation(List<Object> dataList);

    // 数值取整
    public List<Integer> floor(List<Object> dataList);
    // 时间取整  12:00:00
    public List<String> floorTime(List<Object> dataList);

    // 将字符串中数字替换为0
    public List<String> value_hide(List<Object> dataList);
    //数值映射为新值  *50
    public  List<Double> valueMapping(List<Object> dataList);

    //SHA512 哈希
    public  List<String> SHA512(List<Object> dataList);

    // 邮箱地址隐藏
    public  List<String> suppressEmail(List<Object> dataList);

    // IP地址随机抑制
    public List<String> suppressIpRandomParts(List<Object> dataList);

    //IP地址全抑制
    public List<String> suppressAllIp(List<Object> dataList);


}


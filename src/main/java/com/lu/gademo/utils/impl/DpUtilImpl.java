package com.lu.gademo.utils.impl;

import com.lu.gademo.utils.DpUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.math3.distribution.LaplaceDistribution;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.sql.Date;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import com.lu.gademo.utils.DateParseUtil;

@Slf4j
@Component
public class DpUtilImpl implements DpUtil {

//    private final WsAlgorithmLogService wsLogService;
//
//    @Autowired
//    public DpUtilImpl(WsAlgorithmLogService logService) {
//        this.wsLogService = logService;
//    }

//    private final List<SimpleDateFormat> dataFormats = Arrays.asList(
//            new SimpleDateFormat("dd/MM/yyyy HH:mm:ss"),
//            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"),
//            new SimpleDateFormat("yyyyMMddHHmmss"),
//            new SimpleDateFormat("yyyy.MM.dd HH:mm:ss"),
//            new SimpleDateFormat("yyyy-MM-dd"),
//            new SimpleDateFormat("yyyyMMdd"),
//            new SimpleDateFormat("MM/dd/yyyy"),
//            new SimpleDateFormat("dd-MM-yyyy"),
//            new SimpleDateFormat("dd/MM/yyyy"),
//            new SimpleDateFormat("yyyy/MM/dd")
//    );

    //k-匿名算法
    public static List<Double> k_num(List<Double> array, int k) {
        HashMap<Integer, Double> hashMap = new HashMap<>();

        // 将每个值的索引和值存储在哈希表中
        for (int i = 0; i < array.size(); i++) {
            hashMap.put(i, array.get(i));
        }

        // 将数组转换为原始类型数组
        Double[] arrayPrimitive = array.toArray(new Double[0]);

        // 对数组进行排序
        Arrays.sort(arrayPrimitive);

        // 将数组分割成每K个一组的子数组
        int numGroups = (int) Math.ceil((double) array.size() / k);
        List<Double>[] groups = new ArrayList[numGroups];

        for (int i = 0; i < numGroups; i++) {
            int start = i * k;
            int end = Math.min((i + 1) * k, array.size());
            groups[i] = new ArrayList<>(Arrays.asList(Arrays.copyOfRange(arrayPrimitive, start, end)));
        }

        // 最后一组若不足K个则和前一组合并
        if (groups[numGroups - 1].size() < k && numGroups > 1) {
            int lastGroupIndex = numGroups - 1;
            int secondLastGroupIndex = numGroups - 2;
            List<Double> mergedGroup = new ArrayList<>();
            mergedGroup.addAll(groups[secondLastGroupIndex]);
            mergedGroup.addAll(groups[lastGroupIndex]);
            groups[secondLastGroupIndex] = mergedGroup;
            groups = Arrays.copyOf(groups, numGroups - 1);
        }

        // 根据哈希表的数据项的值，确定在哪一个子数组，并用子数组的最大与最小值的平均值更新数据项的值
        for (HashMap.Entry<Integer, Double> entry : hashMap.entrySet()) {
            int index = entry.getKey();
            double value = entry.getValue();

            int groupIndex = -1;
            for (int i = 0; i < groups.length; i++) {
                if (groups[i].contains(value)) {
                    groupIndex = i;
                    break;
                }
            }

            if (groupIndex >= 0) {
                List<Double> group = groups[groupIndex];
                double min = group.get(0);
                double max = group.get(group.size() - 1);
                double average = (min + max) / 2.0;

                hashMap.put(index, average);
            }
        }

        // 根据哈希表的键构建新数组
        List<Double> updatedArray = new ArrayList<>(array.size());
        for (int i = 0; i < array.size(); i++) {
            updatedArray.add(hashMap.get(i));
        }

        return updatedArray;
    }

    @Override
    //单编码数据的处理
    public List<String> dpCode(List<Object> datas, Integer privacyLevel) {
        List<String> re_data = new ArrayList<>();
        //privacyLeve为0则返回
        if (privacyLevel == 0) {
            for (Object data : datas) {
                if (data == null) {
                    re_data.add(null);
                } else {
                    re_data.add(data + "");
                }
            }
            return re_data;
        }
        //读取数据，null为-1，方便处理
        for (Object data : datas) {
            if (data == null) {
                re_data.add("-1");
            } else {
                re_data.add(data + "");
            }
        }
        Set<Object> uniqueSet = new HashSet<>(re_data);
        List<String> code1 = new ArrayList<>();
        List<Integer> count1 = new ArrayList<>();
        List<String> data2 = new ArrayList<>();
        //处理前去重并统计
        for (Object temp : uniqueSet) {
            count1.add(Collections.frequency(re_data, temp));
            code1.add((String) temp);
        }
        //这种情况未执行脱敏
        if (code1.size() == 1) {
            if (Objects.equals(re_data.get(0), "-1")) {
                for (int i = 0; i < re_data.size(); i++) {
                    re_data.set(i, null);
                }
            }
            return re_data;
        } else {
            //执行脱敏 获取参数epsilon
            Random r = new Random();
            double epsilon = 0;
            double p;
            if (privacyLevel == 1) {
                epsilon = 3.6;
            } else if (privacyLevel == 2) {
                epsilon = 2;
            } else if (privacyLevel == 3) {
                epsilon = 0.7;
            }
            //扰动概率p
            double temp = Math.exp(epsilon);
            p = new BigDecimal(temp).divide(new BigDecimal(temp + code1.size() - 1), 6, RoundingMode.HALF_UP).doubleValue();
            //循环处理数据
            for (int i = 0; i < re_data.size(); i++) {
                //		获取一个小数 区间为 (0,1)若大于p，执行扰动
                double rr = r.nextDouble();
                if (rr >= p) {
                    String s = re_data.get(i);
                    code1.remove(re_data.get(i));
                    int size = code1.size();
                    int index = r.nextInt(size);
                    data2.add(code1.get(index));
                    code1.add(s);
                } else {
                    data2.add(re_data.get(i));
                }
            }
            //将-1值恢复null
            for (int i = 0; i < data2.size(); i++) {
                if (Objects.equals(data2.get(i), "-1")) {
                    data2.set(i, null);
                }
            }
        }
        return data2;
    }

    @Override
    //数值型数据处理()
    public List<Double> laplaceToValue(List<Object> datas, Integer privacyLevel) {
        log.info("LaplaceToValue Algorithm Start");
        List<Double> re_data = new ArrayList<>();
        //读取数据
        for (Object data : datas) {
            if (data == null) {
                re_data.add(null);
            } else {
                if (data instanceof Cell) {
                    Cell currentCell = (Cell) data;
                    if (currentCell.getCellType() == CellType.NUMERIC) {
                        double numericValue = currentCell.getNumericCellValue();
                        re_data.add(numericValue);
                    } else if (currentCell.getCellType() == CellType.STRING) {
                        String stringValue = currentCell.getStringCellValue();
                        try {
                            double numericValue = Double.parseDouble(stringValue);
                            re_data.add(numericValue);
                        } catch (NumberFormatException e) {
                            // 处理转换失败的情况，例如输出错误日志或采取其他适当措施
                            log.error("LaplaceToValue error: {}", e.getMessage());
                        }
                    }
                } else {
                    re_data.add(Double.valueOf(data.toString()));
                }
            }
        }
        //privacyLevel直接返回
        if (privacyLevel == 0)
            return re_data;
        //执行laplace加噪
        return NumberCode_s(re_data, privacyLevel);
    }

    //数值型处理
    private List<Double> NumberCode_s(List<Double> reData, Integer privacyLevel) {

        List<Double> newData = new ArrayList<>();
        double max, min;

        // 使用max - min作为敏感度
        max = Collections.max(reData.stream().filter(Objects::nonNull).collect(Collectors.toList()));
        min = Collections.min(reData.stream().filter(Objects::nonNull).collect(Collectors.toList()));

        BigDecimal sensitivity = new BigDecimal(max - min);
        log.info("Sensitivity: " + sensitivity);

        BigDecimal epsilon = new BigDecimal("0.1");
        if (privacyLevel == 1) {
            epsilon = new BigDecimal(10);
        } else if (privacyLevel == 2) {
            epsilon = new BigDecimal(1);
        }

        log.info("Epsilon: " + epsilon);
        BigDecimal beta = sensitivity.divide(epsilon, 6, RoundingMode.HALF_UP);
        double betad = beta.setScale(6, RoundingMode.HALF_UP).doubleValue();
        log.info("Beta: " + beta);

        //循环处理数据
        for (int i = 0; i < reData.size(); i++) {
            LaplaceDistribution ld = new LaplaceDistribution(0, betad);
            double noise = ld.sample();//随机采样一个拉普拉斯分布值
            if (i < 10) {
                log.info("Element {} Noise: {}", i, noise);
            }
            double d;
            //null值不处理
            if (reData.get(i) == null) {
                newData.add(null);
            } else {
                d = noise + reData.get(i);
                BigDecimal b = new BigDecimal(d);
                d = b.setScale(3, RoundingMode.HALF_UP).doubleValue();

                newData.add(d);
            }
        }
        return newData;
    }

    //数值数据k匿名
    public List<Double> k_NumberCode(List<Object> datas, Integer privacyLevel) {
        List<Double> re_data = new ArrayList<>();
        //privacyLevel为0，直接返回
        if (privacyLevel == 0) {
            for (Object data : datas) {
                if (data == null) {
                    re_data.add(null);
                } else {
                    re_data.add(Double.valueOf(data.toString()));
                }
            }
            return re_data;
        }
        //读取数据
        for (Object data : datas) {
            if (data == null) {
                re_data.add(0.0);
            } else {
                if (data instanceof Cell) {
                    Cell currentCell = (Cell) data;
                    if (currentCell.getCellType() == CellType.NUMERIC) {
                        double numericValue = currentCell.getNumericCellValue();
                        re_data.add(numericValue);
                    } else if (currentCell.getCellType() == CellType.STRING) {
                        String stringValue = currentCell.getStringCellValue();
                        try {
                            double numericValue = Double.parseDouble(stringValue);
                            re_data.add(numericValue);
                        } catch (NumberFormatException e) {
                            // 处理转换失败的情况，例如输出错误日志或采取其他适当措施
                            log.error(e.getMessage());
                        }
                    }
                } else {
                    re_data.add(Double.valueOf(data.toString()));
                }
            }
        }
        //获取参数k
        int k = 1;
        if (privacyLevel == 1) {
            k = 10;
        } else if (privacyLevel == 2) {
            k = 30;
        } else if (privacyLevel == 3) {
            k = 50;
        }
        List<Double> newData;
        //执行k-匿名
        newData = k_num(re_data, k);

        return newData;
    }

    // 数值取整
    @Override
    public List<Double> getInt(List<Object> datas, Integer privacyLevel) {
        List<Double> result = new ArrayList<>();
        List<Double> re_data = new ArrayList<>();
        //读取数据
        for (Object data : datas) {
            if (data == null) {
                re_data.add(0.0);
            } else {
                if (data instanceof Cell) {
                    Cell currentCell = (Cell) data;
                    if (currentCell.getCellType() == CellType.NUMERIC) {
                        double numericValue = currentCell.getNumericCellValue();
                        re_data.add(numericValue);
                    } else if (currentCell.getCellType() == CellType.STRING) {
                        String stringValue = currentCell.getStringCellValue();
                        try {
                            double numericValue = Double.parseDouble(stringValue);
                            re_data.add(numericValue);
                        } catch (NumberFormatException e) {
                            // 处理转换失败的情况，例如输出错误日志或采取其他适当措施
                            log.error(e.getMessage());
                        }
                    }
                } else {
                    re_data.add(Double.valueOf(data.toString()));
                }
            }
        }
        // 遍历原始列表，并将个位数置为0后添加到新的列表中
        for (Double value : re_data) {
            double newValue = value.intValue(); // 获取整数部分
            double decimalPart = value - newValue; // 获取小数部分
            result.add(newValue + Math.floor(decimalPart / 10) * 10); // 将个位数置为0
        }
        return result;
    }

    // 基于高斯机制差分隐私的数值加噪算法gaussianToValue
    @Override
    public List<Double> gaussianToValue(List<Object> datas, Integer privacyLevel) {
        System.out.println(datas.size());
        List<Double> re_data = new ArrayList<>();
        //读取数据
        for (Object data : datas) {
            if (data == null) {
                re_data.add(null);
            } else {
                if (data instanceof Cell) {
                    Cell currentCell = (Cell) data;
                    if (currentCell.getCellType() == CellType.NUMERIC) {
                        double numericValue = currentCell.getNumericCellValue();
                        re_data.add(numericValue);
                    } else if (currentCell.getCellType() == CellType.STRING) {
                        String stringValue = currentCell.getStringCellValue();
                        try {
                            double numericValue = Double.parseDouble(stringValue);
                            re_data.add(numericValue);
                        } catch (NumberFormatException e) {
                            // 处理转换失败的情况，例如输出错误日志或采取其他适当措施
                            log.error(e.getMessage());
                        }
                    }
                } else {
                    re_data.add(Double.valueOf(data.toString()));
                }
            }
        }
//        System.out.println(re_data.get(0));
        log.info("First element of reData List: {}", re_data.get(0));
        //privacyLevel直接返回
        if (privacyLevel == 0)
            return re_data;

        List<Double> newData = new ArrayList<>();
        //设置参数sensitivety和epsilon
        //double sensitivety = 0.1;

        double sensitivety;
        double max;

        max = Collections.max(re_data);
        if (max < 100) {
            sensitivety = 0.1;
        } else {
            sensitivety = max / 100;
        }
        double epsilon = 0.1;
        if (privacyLevel == 1) {
            epsilon = 10;
        } else if (privacyLevel == 2) {
            epsilon = 1;
        }

        double delta = 1e-5;  // delta 参数
        double scale = sensitivety * Math.sqrt(2 * Math.log(1.25 / delta)) / epsilon;

        Random random = new Random();
        //循环处理数据
        for (int i = 0; i < re_data.size(); i++) {
            // 生成高斯噪声
            double noise = random.nextGaussian() * scale;

            //null值不处理
            if (re_data.get(i) == null) {
                newData.add(null);
            } else {
                double d = noise + re_data.get(i);
                // 将添加差分隐私后的值保留三位小数
                DecimalFormat df = new DecimalFormat("#.###");
                String roundedValue = df.format(d);

                // 将字符串转换为 double 类型
                double result = Double.parseDouble(roundedValue);

                newData.add(result);
            }
        }
        return newData;
    }

    @Override
    //电话号码或编号的处理，136****1203
    public List<String> numberHide(List<Object> telephones, Integer privacyLevel) {
        List<String> reData = new ArrayList<>();
        //提取数据
        for (Object name : telephones) {
            if (name == null)
                reData.add(null);
            else {
                if (name instanceof Cell) {
                    Cell currentCell = (Cell) name;
                    DataFormatter dataFormatter = new DataFormatter();
                    String cellValue = dataFormatter.formatCellValue(currentCell);
                    reData.add(name + "");
                } else {
                    reData.add(String.valueOf(name));
                }
            }
        }
//        System.out.println(reData.get(0));
        //privacyLevel为0，直接返回
        if (privacyLevel == 0)
            return reData;
        List<String> num = new ArrayList<>();

        int denominator = 3;
        switch (privacyLevel) {
            case 1: {
                denominator = 3;
                break;
            }
            case 2: {
                denominator = 4;
                break;
            }
            case 3: {
                denominator = 5;
                break;
            }
        }

        //循环处理数据  方式是加*
        for (String reDatum : reData) {
            if (reDatum == null) {
                num.add(null);
            } else {
                int l = reDatum.length();
                int index = l / denominator;
                int index2 = reDatum.length() - index;


                StringBuilder substr2 = new StringBuilder();
                for (int j = 0; j < index2 - index; j++) {
                    substr2.append("*");
                }
                String str = reDatum.substring(0, index) + substr2 + reDatum.substring(index2, l);
                num.add(str);
            }
        }
        return num;
    }

    @Override//多编码数据处理,字符串拼接,（待定）
    public List<String> MulCode() {
        return null;
    }

    //名字处理方式,(卢**)
    @Override
    public List<String> nameHide(List<Object> names, Integer privacyLevel) {
        List<String> re_data = new ArrayList<>();
        for (Object name : names) {
            if (name == null)
                re_data.add(null);
            else
                re_data.add(name + "");
        }
        //privacyLevel为0，直接返回
        if (privacyLevel == 0)
            return re_data;
        List<String> nameC = new ArrayList<>();

        for (String reDatum : re_data) {

            if (reDatum == null || reDatum.isEmpty()) {
                nameC.add(null);
            } else {
                StringBuilder str = new StringBuilder(reDatum.substring(0, 1));
                if (privacyLevel == 1) {
                    for (int j = 0; j < reDatum.length() - 2; j++) {
                        str.append("*");
                    }
                    str.append(reDatum.substring(reDatum.length() - 1));
                    nameC.add(str.toString());
                }
                if (privacyLevel == 2) {
                    for (int j = 0; j < reDatum.length() - 1; j++) {
                        str.append("*");
                    }
                    nameC.add(str.toString());
                }
                if (privacyLevel == 3){
                    str.delete(0, str.length());
                    for (int j = 0; j < reDatum.length(); j++) {

                        str.append("*");
                    }
                    nameC.add(str.toString());
                }
            }
        }

        return nameC;
    }

    @Override
    public List<String> addressHide(List<Object> addrs, Integer privacyLevel) {
        // 取数据
        List<String> re_data = new ArrayList<>();
        for (Object addr : addrs) {
            if (addr == null)
                re_data.add(null);
            else
                re_data.add(addr + "");
        }
        //privacyLevel为0，直接返回
        if (privacyLevel == 0)
            return re_data;
        List<String> newAddrs = new ArrayList<>();
        // 脱敏
        for (int i = 0; i < re_data.size(); i++) {
            newAddrs.add(dealAddress(re_data.get(i), privacyLevel));
        }
        return newAddrs;
    }

    private String dealAddress(String addr, int privacyLevel) {
        if (addr != null && !addr.isEmpty()) {
            int length = addr.length();
            StringBuilder newAddr = new StringBuilder();

            if (addr.contains("省")) {
                int index = addr.indexOf("省");
                newAddr.append(addr, 0, index + 1);
                addr = addr.substring(index + 1);
                if (privacyLevel == 3) {
                    return newAddr.toString();
                }
                index = addr.indexOf("市");
                newAddr.append(addr, 0, index + 1);
                addr = addr.substring(index + 1);
                if (privacyLevel == 2) {
                    return newAddr.toString();
                }
                index = addr.indexOf("市");
                if (index == -1) {
                    index = addr.indexOf("县");
                }
                if (index == -1) {
                    index = addr.indexOf("区");
                }
                newAddr.append(addr, 0, index + 1);
                return newAddr.toString();
            } else if (addr.contains("北京") || addr.contains("上海") || addr.contains("重庆") || addr.contains("天津")) {
                int index = addr.indexOf("市");
                newAddr.append(addr, 0, index + 1);
                addr = addr.substring(index + 1);
                if (privacyLevel == 3) {
                    return newAddr.toString();
                }
                index = addr.indexOf("市");
                if (index == -1) {
                    index = addr.indexOf("县");
                }
                if (index == -1) {
                    index = addr.indexOf("区");
                }
                newAddr.append(addr, 0, index + 1);
                addr = addr.substring(index + 1);
                if (privacyLevel == 2) {
                    return newAddr.toString();
                }
                index = addr.indexOf("道");
                if (index == -1) {
                    index = addr.indexOf("镇");
                }
                if (index == -1) {
                    index = addr.indexOf("乡");
                }
                newAddr.append(addr, 0, index + 1);
                return newAddr.toString();
            } else if (addr.contains("自治区")) {
                int index = addr.indexOf("区");
                newAddr.append(addr, 0, index + 1);
                addr = addr.substring(index + 1);
                if (privacyLevel == 3) {
                    return newAddr.toString();
                }
                index = addr.indexOf("市");
                if (index == -1) {
                    index = addr.indexOf("盟");
                }
                if (index == -1) {
                    if (addr.contains("地区")) {
                        index = addr.indexOf("区");
                    } else if (addr.contains("自治州")) {
                        index = addr.indexOf("州");
                    }
                }
                newAddr.append(addr, 0, index + 1);
                addr = addr.substring(index + 1);
                if (privacyLevel == 2) {
                    return newAddr.toString();
                }
                index = addr.indexOf("市");
                if (index == -1) {
                    index = addr.indexOf("县");
                }
                if (index == -1) {
                    index = addr.indexOf("旗");
                }
                if (index == -1) {
                    index = addr.indexOf("区");
                }
                newAddr.append(addr, 0, index + 1);
                addr = addr.substring(index + 1);
                if (privacyLevel == 1) {
                    return newAddr.toString();
                }
            }
            return newAddr.toString();
        }
        return addr;
    }

    @Override
    public List<String> desenAddressName(List<Object> addrs, Integer privacyLevel, String name) {
        // 取数据
        List<String> re_data = new ArrayList<>();
        for (Object addr : addrs) {
            if (addr == null)
                re_data.add(null);
            else
                re_data.add(addr + "");
        }
        //privacyLevel为0，直接返回
        if (privacyLevel == 0)
            return re_data;
        List<String> newAddrs = new ArrayList<>();
        int len = 0;
        if (name.contains("派出所")) {
            len = 3;
        } else if (name.contains("酒店") || name.contains("旅店") || name.contains("宾馆") || name.contains("街道")) {
            len = 2;
        }
        /*else if (name.contains("公安局")){
            len =
        }*/
        // 脱敏
        for (int i = 0; i < re_data.size(); i++) {
            String item = re_data.get(i);
            if (item.contains("公安局")) {
                newAddrs.add(item.substring(0, item.indexOf("局") + 1));
            } else {
                newAddrs.add(item.substring(item.length() - len));
            }
        }
        return newAddrs;
    }

    @Override//身份号码的处理
    public String IDCode(String Id, String zs, Date birthday, String sex) {
        System.out.println("籍贯：" + zs);
        System.out.println("出生日期：" + birthday.toString());
        System.out.println("性别：" + sex);
        String ID, birth_Str, id17;
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
        Date date = new Date(0);
        try {
            String str = format.format(birthday);
            birth_Str = str.substring(0, 4) + str.substring(5, 7) + str.substring(8, 10);
        } catch (Exception e) {
            birth_Str = null;
        }
        Random rd = new Random();
        int last2;
        if (Integer.valueOf(sex) == 2) {
            last2 = (rd.nextInt(5)) * 2;//女性倒数第二位
        } else {
            last2 = (rd.nextInt(5)) * 2 + 1;//男性
        }
        System.out.println(last2);
        int last4 = (rd.nextInt(10));//倒数第四位
        int last3 = (rd.nextInt(10));//倒数第三位
        id17 = zs + birth_Str + last4 + last3 + last2;
        int[] arr = new int[17];
        //生成最后一位校验位
        int[] bit = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};//对应乘的数组
        for (int j = 0; j < arr.length; j++) {
            char c = id17.charAt(j); //'4'
            arr[j] = c - '0'; //数字字符转对应的数字
        }//步骤2完成
        // 对应与 7 9 10 5 8 4 2 1 6 3 7 9 10 5 8 4 2 相乘，并求和。
        int sum = 0;
        for (int j = 0; j < arr.length; j++) {
            sum = sum + arr[j] * bit[j];
        }//步骤3完成
        //对11取余数，用余数对应 1 0 X 9 8 7 6 5 4 3 2，并输出结果。
        char[] res = {'1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2'};
        String last1 = String.valueOf(res[sum % 11]);
        ID = id17 + last1;
        //}
        return ID;
    }

    @Override
    public String IDCode(String Id, Date birthday, String sex) {
        System.out.println("出生日期：" + birthday.toString());
        System.out.println("性别：" + sex);
        String ID, birth_Str, id17;
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
        Date date = new Date(0);
        try {
            String str = format.format(birthday);
            birth_Str = str.substring(0, 4) + str.substring(5, 7) + str.substring(8, 10);
        } catch (Exception e) {
            birth_Str = null;
        }
        Random rd = new Random();
        int last2;
        if (Integer.valueOf(sex) == 2) {
            last2 = (rd.nextInt(5)) * 2;//女性倒数第二位
        } else {
            last2 = (rd.nextInt(5)) * 2 + 1;//男性
        }
        System.out.println(last2);
        int last4 = (rd.nextInt(10));//倒数第四位
        int last3 = (rd.nextInt(10));//倒数第三位
        id17 = Id.substring(0, 7) + birth_Str + last4 + last3 + last2;
        int[] arr = new int[17];
        //生成最后一位校验位
        int[] bit = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};//对应乘的数组
        for (int j = 0; j < arr.length; j++) {
            char c = id17.charAt(j); //'4'
            arr[j] = c - '0'; //数字字符转对应的数字
        }//步骤2完成
        // 对应与 7 9 10 5 8 4 2 1 6 3 7 9 10 5 8 4 2 相乘，并求和。
        int sum = 0;
        for (int j = 0; j < arr.length; j++) {
            sum = sum + arr[j] * bit[j];
        }//步骤3完成
        //对11取余数，用余数对应 1 0 X 9 8 7 6 5 4 3 2，并输出结果。
        char[] res = {'1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2'};
        String last1 = String.valueOf(res[sum % 11]);
        ID = id17 + last1;
        //}
        return ID;
    }

    //日期处理
//    private java.util.Date parseDate(Object data) {
//        for (SimpleDateFormat format : dataFormats) {
//            try {
//                System.out.println("Trying format: " + format.toPattern());
//                java.util.Date date = format.parse(data.toString());
//                System.out.println("Parsed date: " + date);
//                return format.parse(data.toString());
//            } catch (ParseException e) {
//                e.getStackTrace();
//            }
//        }
//        return null;
//    }

    @Override
    public List<Date> dpDate(List<Object> datas, Integer privacyLevel) throws ParseException {

        List<Date> re_data = new ArrayList<>();
        java.util.Date tempDate;

        for (Object data : datas) {
            if (data == null) {
                re_data.add(null);
            } else {
                tempDate = DateParseUtil.parseDate(data);
                if (tempDate == null) {
                    // TODO: 应有更好的处理方法
//                    throw new ParseException("Parse date error : " + data, 0);
                    re_data.add(null);
                } else {
                    Date parsedDate = new Date(tempDate.getTime());
                    re_data.add(parsedDate);
                }
            }
        }
        //不保护，直接返回
        if (privacyLevel == 0) {
            return re_data;
        }
        List<Date> newDate = new ArrayList<>();
        BigDecimal si = new BigDecimal(1);
        BigDecimal epsilon = new BigDecimal("0.001");
        //获取epsilon值
        if (privacyLevel == 1) {
            epsilon = new BigDecimal("0.1");
        } else if (privacyLevel == 2) {
            epsilon = new BigDecimal("0.01");
        }
        BigDecimal beta = si.divide(epsilon, 6, RoundingMode.HALF_UP);
        double betad = beta.setScale(6, RoundingMode.HALF_UP).doubleValue();
        //循环处理数据
        for (int i = 0; i < re_data.size(); i++) {
            //空值不处理
            if (re_data.get(i) == null) {
                newDate.add(null);
            } else {
                //添加噪声，依次加day、hour、minute
                LaplaceDistribution ld = new LaplaceDistribution(0, betad);
                double noise = ld.sample();//随机采样一个拉普拉斯分布值
                String[] s = String.valueOf(noise).split("\\.");
                String day = s[0];
                double dvalue = Double.valueOf(("0." + s[1]));
                String[] s1 = String.valueOf(dvalue * 24).split("\\.");
                String hour = s1[0];
                double hvalue = Double.valueOf(("0." + s1[1]));
                String[] s2 = String.valueOf(hvalue * 60).split("\\.");
                String minute = s2[0];
                double mvalue = Double.valueOf(("0." + s2[1]));
                String[] s3 = String.valueOf(mvalue * 60).split("\\.");
                String second = s3[0];
                String msecond = s3[1].substring(0, 3);
                //日期格式
                SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                String d;
                DateFormat format1 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                Date datetemp = new Date(0);

                //根据noise干扰
                if (noise >= 0) {
                    d = df.format(new Date(re_data.get(i).getTime() + Integer.valueOf(day) * 24 * 60 * 60 * 1000 + Integer.valueOf(hour) * 60 * 60 * 1000 + Integer.valueOf(minute) * 60 * 1000 + Integer.valueOf(second) * 1000 + Integer.valueOf(msecond)));
                    datetemp = new Date(format1.parse(d).getTime());
                } else {
                    d = df.format(new Date(re_data.get(i).getTime() - (Integer.valueOf(day) * 24 * 60 * 60 * 1000 + Integer.valueOf(hour) * 60 * 60 * 1000 + Integer.valueOf(minute) * 60 * 1000 + Integer.valueOf(second) * 1000 + Integer.valueOf(msecond))));
                    datetemp = new Date(format1.parse(d).getTime());
                }

                newDate.add(datetemp);
            }
        }
        return newDate;
    }

    //日期分组置换
    public List<Date> date_group_replace(List<Object> datas, Integer privacyLevel) throws ParseException {
        //日期格式
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        //毫秒list
        List<Double> milliseconds = new ArrayList<>();
        for (Object data : datas) {
            if (data == null)
                milliseconds.add(0.0);
            else {
                //java.util.Date utilDate = dateFormat.parse(data + "");
                java.util.Date date = DateParseUtil.parseDate(data.toString());

                //Date sqlDate = new Date(utilDate.getTime());
                // 日期解析可能失败,如果失败则添加0
                if (date == null) {
                    milliseconds.add(0.0);
                } else {
                    milliseconds.add((double) date.getTime());
                }
            }
        }
        //判断匿名组k大小
        int k = 1;
        if (privacyLevel == 0) {
            List<Date> reData = new ArrayList<>();
            for (Object data : datas) {
                if (data == null) {
                    reData.add(null);
                } else {
                    java.util.Date date = DateParseUtil.parseDate(data.toString());
                    // 如果解析失败则添加null
                    if (date == null) {
                        reData.add(null);
                    } else {
                        reData.add(new Date(date.getTime()));
                    }
//                    reData.add(new Date(dateFormat.parse(data + "").getTime()));
                }
            }
            return reData;
        }

        //获取参数k
        if (privacyLevel == 1) {
            k = 1;
        } else if (privacyLevel == 2) {
            k = 3;
        } else if (privacyLevel == 3) {
            k = 5;
        }
        //执行k-匿名
        List<Double> newMilliseconds = k_num(milliseconds, k);
        //毫秒转为日期
        List<Date> newDate = new ArrayList<>();
        for (double m : newMilliseconds) {
            String date = dateFormat.format(new java.util.Date((long) m));
            newDate.add(new Date(dateFormat.parse(date).getTime()));
        }
        return newDate;
    }

    @Override//文本区域的处理（eg.地址，公司名，车的类型）,商量
    public List<String> TextCode(List<Object> texts) {
        List<String> re_data = new ArrayList<>();
        for (Object data : texts) {
            re_data.add((String) data);
        }
        return re_data;
    }

    @Override
    public String infoID(String id, String name, Date time, String hotel) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
        Date date = new Date(0);
        String time0;
        try {
            String str = format.format(time);
            time0 = str.substring(0, 4) + str.substring(5, 7) + str.substring(8, 10);
        } catch (Exception e) {
            time0 = null;
        }
        return id + "#" + name + "#" + time0 + "#" + hotel;
    }

    @Override
    public List<String> passReplace(List<Object> passwords, Integer privacyLevel) {
        List<String> re_data = new ArrayList<>();
        //提取数据
        for (Object password : passwords) {
            if (password == null)
                re_data.add(null);
            else
                re_data.add(password + "");
        }
        //privacyLevel为0，直接返回
        if (privacyLevel == 0)
            return re_data;
        List<String> new_passwords = new ArrayList<>();

        Random random = new Random();
        // 生成5到20之间的随机数
        int min = 5;
        int max = 15;
        switch (privacyLevel) {
            case 1: {
                max = 15;
                break;
            }
            case 2: {
                max = 20;
                break;
            }
            case 3: {
                max = 25;
                break;
            }
        }
        //循环处理数据  方式是加*
        for (int i = 0; i < re_data.size(); i++) {
            if (re_data.get(i) == null) {
                new_passwords.add(null);
            } else {
                new_passwords.add(getRandomStr(random.nextInt(max - min + 1) + min));
            }
        }
        return new_passwords;
    }

    public String getRandomStr(int len) {
        String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(len);

        for (int i = 0; i < len; i++) {
            int randomIndex = random.nextInt(CHARACTERS.length());
            char randomChar = CHARACTERS.charAt(randomIndex);
            sb.append(randomChar);
        }
        return sb.toString();
    }

    @Override
    public List<String> truncation(List<Object> dataList, Integer privacyLevel) {
        List<String> reBoxedData;

        List<String> result = new ArrayList<>();

        reBoxedData = dataList.stream().map(o -> o == null ? null : o + "").collect(Collectors.toList());

        int remains = 3;
        switch (privacyLevel) {
            case 1: {
                remains = 3;
                break;
            }
            case 2: {
                remains = 2;
                break;
            }

            case 3: {
                remains = 1;
                break;
            }
        }

        if (privacyLevel == 0) return reBoxedData;


        for (String reBoxedDatum : reBoxedData) {
            if (reBoxedDatum == null) {
                result.add(null);
            } else {

                if (reBoxedDatum.length() >= remains) {
                    result.add(reBoxedDatum.substring(0, remains));
                } else {
                    result.add(reBoxedDatum);
                }

            }
        }
        return result;
    }

//    @Override
//    public List<Integer> floor(List<Object> dataList, Integer privacyLevel) {
//        List<Integer> result = new ArrayList<>();
//        List<Double> re_data = new ArrayList<>();
//        for (Object data : dataList) {
//            if (data == null) {
//                re_data.add(null);
//            } else {
//                if (data instanceof Cell) {
//                    Cell currentCell = (Cell) data;
//                    if (currentCell.getCellType() == CellType.NUMERIC) {
//                        double numericValue = currentCell.getNumericCellValue();
//                        re_data.add(numericValue);
//                    } else if (currentCell.getCellType() == CellType.STRING) {
//                        String stringValue = currentCell.getStringCellValue();
//                        try {
//                            double numericValue = Double.parseDouble(stringValue);
//                            re_data.add(numericValue);
//                        } catch (NumberFormatException e) {
//                            // 处理转换失败的情况，例如输出错误日志或采取其他适当措施
//                            e.printStackTrace();
//                        }
//                    }
//                } else {
//                    re_data.add((Double) data);
//                }
//            }
//        }
//
////        if (privacyLevel == 0) {
////            return re_data;
////        }
//        double max_data = Collections.max(re_data);
//        for (Object item : re_data) {
//            if (item == null) {
//                result.add(null);
//            } else {
//                if (max_data <= 10) {
//                    result.add(10);
//                } else if (max_data <= 100) {
//                    result.add((int) Math.floor((Double) item) / 10 * 10);
//                } else if (max_data <= 1000) {
//                    if (privacyLevel == 1) {
//                        result.add((int) Math.floor((Double) item) / 10 * 10);
//                    } else {
//                        result.add((int) Math.floor((Double) item) / 100 * 100);
//                    }
//                } else if (max_data <= 10000) {
//                    if (privacyLevel == 1) {
//                        result.add((int) Math.floor((Double) item) / 10 * 10);
//                    } else if (privacyLevel == 2) {
//                        result.add((int) Math.floor((Double) item) / 100 * 100);
//                    } else if (privacyLevel == 3) {
//                        result.add((int) Math.floor((Double) item) / 1000 * 1000);
//                    }
//                } else if (max_data > 10000) {
//                    if (privacyLevel == 1) {
//                        result.add((int) Math.floor((Double) item) / 100 * 100);
//                    } else if (privacyLevel == 2) {
//                        result.add((int) Math.floor((Double) item) / 1000 * 1000);
//                    } else if (privacyLevel == 3) {
//                        result.add((int) Math.floor((Double) item) / 10000 * 10000);
//                    }
//                }
//                //result.add((int) Math.floor((Double) item) / 10 *10);
//            }
//        }
//
//        return result;
//    }

    @Override
    public List<String> floor(List<Object> dataList, Integer privacyLevel) {
        List<String> result = new ArrayList<>();
        List<BigDecimal> re_data = new ArrayList<>();

        for (Object data : dataList) {
            if (data == null) {
                re_data.add(null);
            } else {
                if (data instanceof Cell) {
                    Cell currentCell = (Cell) data;
                    if (currentCell.getCellType() == CellType.NUMERIC) {
                        BigDecimal numericValue = BigDecimal.valueOf(currentCell.getNumericCellValue());
                        re_data.add(numericValue);
                    } else if (currentCell.getCellType() == CellType.STRING) {
                        String stringValue = currentCell.getStringCellValue();
                        try {
                            BigDecimal numericValue = new BigDecimal(stringValue);
                            re_data.add(numericValue);
                        } catch (NumberFormatException e) {
                            // 处理转换失败的情况，例如输出错误日志或采取其他适当措施
                            e.printStackTrace();
                        }
                    }
                } else {
                    re_data.add(new BigDecimal(data.toString()));
                }
            }
        }

        BigDecimal max_data = Collections.max(re_data, Comparator.nullsFirst(Comparator.naturalOrder()));
        for (BigDecimal item : re_data) {
            if (item == null) {
                result.add(null);
            } else {
                BigDecimal flooredValue = BigDecimal.ZERO;
                if (max_data.compareTo(BigDecimal.valueOf(10)) <= 0) {
                    flooredValue = BigDecimal.TEN;
                } else if (max_data.compareTo(BigDecimal.valueOf(100)) <= 0) {
                    flooredValue = item.divide(BigDecimal.TEN, 0, RoundingMode.FLOOR).multiply(BigDecimal.TEN);
                } else if (max_data.compareTo(BigDecimal.valueOf(1000)) <= 0) {
                    if (privacyLevel == 1) {
                        flooredValue = item.divide(BigDecimal.TEN, 0, RoundingMode.FLOOR).multiply(BigDecimal.TEN);
                    } else {
                        flooredValue = item.divide(BigDecimal.valueOf(100), 0, RoundingMode.FLOOR).multiply(BigDecimal.valueOf(100));
                    }
                } else if (max_data.compareTo(BigDecimal.valueOf(10000)) <= 0) {
                    if (privacyLevel == 1) {
                        flooredValue = item.divide(BigDecimal.TEN, 0, RoundingMode.FLOOR).multiply(BigDecimal.TEN);
                    } else if (privacyLevel == 2) {
                        flooredValue = item.divide(BigDecimal.valueOf(100), 0, RoundingMode.FLOOR).multiply(BigDecimal.valueOf(100));
                    } else if (privacyLevel == 3) {
                        flooredValue = item.divide(BigDecimal.valueOf(1000), 0, RoundingMode.FLOOR).multiply(BigDecimal.valueOf(1000));
                    }
                } else {
                    if (privacyLevel == 1) {
                        flooredValue = item.divide(BigDecimal.valueOf(100), 0, RoundingMode.FLOOR).multiply(BigDecimal.valueOf(100));
                    } else if (privacyLevel == 2) {
                        flooredValue = item.divide(BigDecimal.valueOf(1000), 0, RoundingMode.FLOOR).multiply(BigDecimal.valueOf(1000));
                    } else if (privacyLevel == 3) {
                        flooredValue = item.divide(BigDecimal.valueOf(10000), 0, RoundingMode.FLOOR).multiply(BigDecimal.valueOf(10000));
                    }
                }
                result.add(flooredValue.toString());
            }
        }

        return result;
    }


    @Override
    // 时间取整算法
    public List<String> floorTime(List<Object> dataList, Integer privacyLevel) {
        List<String> result = new ArrayList<>();
        Pattern timePattern = Pattern.compile("^(?:(?:([01]?\\d|2[0-3]):)?([0-5]?\\d):)?([0-5]?\\d)$");
        for (Object o : dataList) {
            if (o == null) {
                result.add(null);
            } else {
                Matcher m = timePattern.matcher(o + "");
                if (m.matches()) {
                    String hour = m.group(1);
                    String minutes = m.group(2);

                    switch (privacyLevel) {
                        case 1: {
                            result.add(hour + ":" + minutes + ":" + "00");
                            break;
                        }
                        case 2: {
                            result.add(hour + ":00:00");
                            break;
                        }
                        case 3: {
                            result.add("00:00:00");
                            break;
                        }
                        default: {
                            result.add(hour + ":00:00");
                        }
                    }
                }
            }
        }

        if (privacyLevel == 0)
            return dataList.stream().map(o -> o == null ? null : o + "").collect(Collectors.toList());
        return result;
    }

    @Override
    public List<String> value_hide(List<Object> dataList, Integer privacyLevel) {
        ArrayList<String> result = new ArrayList<>();

        if (privacyLevel == 0)
            return dataList.stream().map(o -> o == null ? null : o + "").collect(Collectors.toList());

        for (Object data : dataList) {
            if (data == null) {
                result.add(null);
            } else {
                String tmp = data + "";
                switch (privacyLevel) {

                    case 1:
                        result.add(tmp.replaceAll("\\d", "0")); // 将数字替换为0
                        break;
                    case 2:
                        result.add(tmp.replaceAll("\\d", "x")); // 将数字替换为X
                        break;
                    case 3:
                        result.add(tmp.replaceAll("[a-zA-Z0-9]", "*")); // 将字母和数字替换为*
                        break;
                }
            }
        }

        return result;
    }

    @Override
    public List<Double> valueMapping(List<Object> dataList, Integer privacyLevel) {
        ArrayList<Double> result = new ArrayList<>();
        List<Double> reData = new ArrayList<>();
        //读取数据
        for (Object data : dataList) {
            if (data == null) {
                reData.add(null);
            } else {
                if (data instanceof Cell) {
                    Cell currentCell = (Cell) data;
                    if (currentCell.getCellType() == CellType.NUMERIC) {
                        double numericValue = currentCell.getNumericCellValue();
                        reData.add(numericValue);
                    } else if (currentCell.getCellType() == CellType.STRING) {
                        String stringValue = currentCell.getStringCellValue();
                        try {
                            double numericValue = Double.parseDouble(stringValue);
                            reData.add(numericValue);
                        } catch (NumberFormatException e) {
                            // 处理转换失败的情况，例如输出错误日志或采取其他适当措施
                            e.printStackTrace();
                        }
                    }
                } else {
                    reData.add(Double.valueOf(data.toString()));
                }
            }
        }
        if (privacyLevel == 0) return reData;

        int scale = 1;
        switch (privacyLevel) {
            case 1: {
                scale = 20;
                break;
            }
            case 2: {
                scale = 30;
                break;
            }
            case 3: {
                scale = 50;
                break;
            }
        }

        for (Object data : reData) {
            if (data == null) {
                result.add(null);
            } else {
                result.add((double) data * scale);
            }
        }
        return result;
    }

    @Override
    public List<String> SHA512(List<Object> dataList, Integer privacyLevel) {
        ArrayList<String> result = new ArrayList<>();
        String hashName = "SHA-512";
        switch (privacyLevel) {
            case 1: {
                hashName = "MD5";
                break;
            }
            case 2: {
                hashName = "SHA-1";
                break;
            }
            case 3: {
                hashName = "SHA-256";
                break;
            }
        }

        for (Object data : dataList) {
            if (data == null) {
                result.add(null);
            } else {
                String tmp = data + "";
                result.add(hashing(hashName, tmp));
            }
        }
        if (privacyLevel == 0)
            return dataList.stream().map(o -> o == null ? null : o + "").collect(Collectors.toList());
        return result;
    }

    // 哈希算法
    private String hashing(String hashingMethod, String s) {
        try {
            MessageDigest md = MessageDigest.getInstance(hashingMethod);
            byte[] hashBytes = md.digest(s.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hashBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<String> suppressEmail(List<Object> dataList, Integer privacyLevel) {
        String pat = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";
        Pattern pattern = Pattern.compile(pat);
        ArrayList<String> result = new ArrayList<>();

        for (Object data : dataList) {
            if (data == null) {
                result.add(null);
            } else {
                String tmp = data + "";
                switch (privacyLevel) {
                    case 0:
                        result.add(tmp);
                        break;
                    case 1:
                        result.add(partialMaskEmail(tmp));
                        break;
                    case 2:
                        result.add(domainOnlyMaskEmail(tmp));
                        break;

                    case 3:
                        result.add(tmp.replaceAll(pat, "***@***"));
                        break;
                    default:
                        return null;
                }
            }
        }

        return result;
    }

    private String partialMaskEmail(String input) {
        String maskedEmail;
        String[] parts = input.split("@");
        if (parts[0].length() >= 2) {
            maskedEmail = parts[0].substring(0, 2) + "***@" + parts[1];
        } else {
            maskedEmail = "***@" + parts[1];
        }
        return maskedEmail;
    }

    private String domainOnlyMaskEmail(String input) {
        String[] parts = input.split("@");
        return "***@" + parts[1];
    }

    // 基于随机高斯噪声的数值加噪算法
    @Override
    public List<Double> randomGaussianToValue(List<Object> datas, Integer privacyLevel) {
        System.out.println(datas.size());
        List<Double> re_data = new ArrayList<>();
        //读取数据
        for (Object data : datas) {
            if (data == null) {
                re_data.add(null);
            } else {
                if (data instanceof Cell) {
                    Cell currentCell = (Cell) data;
                    if (currentCell.getCellType() == CellType.NUMERIC) {
                        double numericValue = currentCell.getNumericCellValue();
                        re_data.add(numericValue);
                    } else if (currentCell.getCellType() == CellType.STRING) {
                        String stringValue = currentCell.getStringCellValue();
                        try {
                            double numericValue = Double.parseDouble(stringValue);
                            re_data.add(numericValue);
                        } catch (NumberFormatException e) {
                            // 处理转换失败的情况，例如输出错误日志或采取其他适当措施
                            e.printStackTrace();
                        }
                    }
                } else {
                    re_data.add(Double.valueOf(data.toString()));
                }
            }
        }
        //privacyLevel直接返回
        if (privacyLevel == 0)
            return re_data;

        List<Double> newData = new ArrayList<>();
        //设置参数sensitivety和epsilon
        double mean = 1.0; // 均值
        double stdDev = 2.0; // 标准差
        if (privacyLevel == 2) {
            stdDev = 5.0;
        } else if (privacyLevel == 3) {
            stdDev = 10.0;
        }

        Random random = new Random();

        //循环处理数据
        for (int i = 0; i < re_data.size(); i++) {
            // 生成高斯噪声
            double noise = random.nextGaussian() * stdDev;

            //null值不处理
            if (re_data.get(i) == null) {
                newData.add(null);
            } else {
                double d = noise + re_data.get(i);
                // 值保留三位小数
                DecimalFormat df = new DecimalFormat("#.###");
                String roundedValue = df.format(d);

                // 将字符串转换为 double 类型
                double result = Double.parseDouble(roundedValue);

                newData.add(result);
            }
        }
        return newData;
    }

    // 基于随机拉普拉斯噪声的数值加噪算法
    @Override
    public List<Double> randomLaplaceToValue(List<Object> datas, Integer privacyLevel) {
        List<Double> re_data = new ArrayList<>();
        List<Double> newData = new ArrayList<>();
        //读取数据
        for (Object data : datas) {
            if (data == null) {
                re_data.add(null);
            } else {
                if (data instanceof Cell) {
                    Cell currentCell = (Cell) data;
                    if (currentCell.getCellType() == CellType.NUMERIC) {
                        double numericValue = currentCell.getNumericCellValue();
                        re_data.add(numericValue);
                    } else if (currentCell.getCellType() == CellType.STRING) {
                        String stringValue = currentCell.getStringCellValue();
                        try {
                            double numericValue = Double.parseDouble(stringValue);
                            re_data.add(numericValue);
                        } catch (NumberFormatException e) {
                            // 处理转换失败的情况，例如输出错误日志或采取其他适当措施
                            e.printStackTrace();
                        }
                    }
                } else {
                    re_data.add(Double.valueOf(data.toString()));
                }
            }
        }
        //privacyLevel直接返回
        if (privacyLevel == 0)
            return re_data;
        //执行laplaces加噪
        double betad = 1.0;
        if (privacyLevel == 2) {
            betad = 5.0;
        } else if (privacyLevel == 3) {
            betad = 10.0;
        }
        Random r = new Random();
        //循环处理数据
        for (int i = 0; i < re_data.size(); i++) {
            LaplaceDistribution ld = new LaplaceDistribution(0, betad);
            double noise = ld.sample();//随机采样一个拉普拉斯分布值
            double d;
            //null值不处理
            if (re_data.get(i) == null) {
                newData.add(null);
            } else {
                d = noise + re_data.get(i);
                BigDecimal b = new BigDecimal(d);
                d = b.setScale(3, RoundingMode.HALF_UP).doubleValue();
                newData.add(d);
            }
        }
        return newData;
    }

    // 基于随机均匀噪声的数值加噪算法
    @Override
    public List<Double> randomUniformToValue(List<Object> datas, Integer privacyLevel) {
        List<Double> re_data = new ArrayList<>();
        List<Double> newData = new ArrayList<>();
        //读取数据
        for (Object data : datas) {
            if (data == null) {
                re_data.add(null);
            } else {
                if (data instanceof Cell) {
                    Cell currentCell = (Cell) data;
                    if (currentCell.getCellType() == CellType.NUMERIC) {
                        double numericValue = currentCell.getNumericCellValue();
                        re_data.add(numericValue);
                    } else if (currentCell.getCellType() == CellType.STRING) {
                        String stringValue = currentCell.getStringCellValue();
                        try {
                            double numericValue = Double.parseDouble(stringValue);
                            re_data.add(numericValue);
                        } catch (NumberFormatException e) {
                            // 处理转换失败的情况，例如输出错误日志或采取其他适当措施
                            log.error(e.getMessage());
                        }
                    }
                } else {
                    re_data.add(Double.valueOf(data.toString()));
                }
            }
        }
        //privacyLevel直接返回
        if (privacyLevel == 0)
            return re_data;
        //执行均匀加噪
        double am = 2.0;
        if (privacyLevel == 2) {
            am = 10.0;
        } else if (privacyLevel == 3) {
            am = 20.0;
        }

        SecureRandom secureRandom = new SecureRandom();
        for (int i = 0; i < re_data.size(); i++) {
            double noise = (secureRandom.nextDouble() * 2 * am) - am; // 生成均匀分布的噪声
            if (re_data.get(i) == null) {
                newData.add(null);
            } else {
                newData.add(noise + re_data.get(i));
            }
        }
        return newData;
    }

    //
    @Override
    public List<Double> valueShift(List<Object> datas, Integer privacyLevel) {
        List<Double> re_data = new ArrayList<>();
        List<Double> newData = new ArrayList<>();
        //读取数据
        for (Object data : datas) {
            if (data == null) {
                re_data.add(null);
            } else {
                if (data instanceof Cell) {
                    Cell currentCell = (Cell) data;
                    if (currentCell.getCellType() == CellType.NUMERIC) {
                        double numericValue = currentCell.getNumericCellValue();
                        re_data.add(numericValue);
                    } else if (currentCell.getCellType() == CellType.STRING) {
                        String stringValue = currentCell.getStringCellValue();
                        try {
                            double numericValue = Double.parseDouble(stringValue);
                            re_data.add(numericValue);
                        } catch (NumberFormatException e) {
                            // 处理转换失败的情况，例如输出错误日志或采取其他适当措施
                            e.printStackTrace();
                        }
                    }
                } else {
                    re_data.add(Double.valueOf(data.toString()));
                }
            }
        }
        //privacyLevel直接返回
        if (privacyLevel == 0)
            return re_data;
        double shift = 2.3;
        if (privacyLevel == 2) {
            shift = 11.3;
        } else if (privacyLevel == 3) {
            shift = 23.1;
        }

        Random random = new Random();
        for (int i = 0; i < re_data.size(); i++) {
            if (re_data.get(i) == null) {
                newData.add(null);
            } else {
                newData.add(shift + re_data.get(i));
            }
        }
        return newData;
    }

    @Override
    public List<String> suppressIpRandomParts(List<Object> dataList, Integer privacyLevel) {
        ArrayList<String> result = new ArrayList<>();
        String pat = "((?:2(?:5[0-5]|[0-4]\\d))|[0-1]?\\d{1,2})\\.((?:2(?:5[0-5]|[0-4]\\d))|[0-1]?\\d{1,2})\\.((?:2(?:5[0-5]|[0-4]\\d))|[0-1]?\\d{1,2})\\.((?:2(?:5[0-5]|[0-4]\\d))|[0-1]?\\d{1,2})";
        String[] ip = {"$1", "$2", "$3", "$4"};
        if (privacyLevel == 0)
            return dataList.stream().map(o -> o == null ? null : o + "").collect(Collectors.toList());
        List<Integer> randomList = new ArrayList<>();
        for (int i = 0; i < dataList.size(); i++) {
            randomList.add(ThreadLocalRandom.current().nextInt(4));
        }


        for (int i = 0; i < dataList.size(); i++) {
            ArrayList<String> patTemp = new ArrayList<>(Arrays.asList(ip));
            int random = randomList.get(i);

            switch (privacyLevel) {
                case 1: {
                    patTemp.set(random, "*");
                    break;
                }
                case 2: {
                    patTemp.set(random, "*");
                    patTemp.set((random + 1) % 4, "*");
                    break;
                }
                case 3: {
                    patTemp.set(random, "*");
                    patTemp.set((random + 1) % 4, "*");
                    patTemp.set((random + 2) % 4, "*");
                    break;
                }
            }

            result.add(getString(dataList.get(i), pat, patTemp));
        }

        return result;
    }


    @Override
    public List<String> suppressAllIp(List<Object> dataList, Integer privacyLevel) {
        ArrayList<String> result = new ArrayList<>();
        String pat = "((?:2(?:5[0-5]|[0-4]\\d))|[0-1]?\\d{1,2})\\.((?:2(?:5[0-5]|[0-4]\\d))|[0-1]?\\d{1,2})\\.((?:2(?:5[0-5]|[0-4]\\d))|[0-1]?\\d{1,2})\\.((?:2(?:5[0-5]|[0-4]\\d))|[0-1]?\\d{1,2})";
        String[] ip = {"$1", "$2", "$3", "$4"};

        for (Object data : dataList) {
            ArrayList<String> patTemp = new ArrayList<>(Arrays.asList(ip));
            switch (privacyLevel) {
                case 0:
                    result.add(data == null ? null : data.toString()); // 不进行脱敏处理
                    break;
                case 1:
                    patTemp.set(0, "*");
                    result.add(getString(data, pat, patTemp)); // 第一部分脱敏
                    break;
                case 2:
                    patTemp.set(0, "*");
                    patTemp.set(1, "*");
                    result.add(getString(data, pat, patTemp)); // 第一和第二部分脱敏
                    break;

                default:
                    patTemp.set(0, "*");
                    patTemp.set(1, "*");
                    patTemp.set(2, "*");
                    patTemp.set(3, "*");
                    result.add(getString(data, pat, patTemp)); // 前三部分脱敏
                    break;
            }
        }
        return result;

    }

    // 取字符串
    private String getString(Object data, String pat, ArrayList<String> keepPat) {
        String keepPatStr = String.join(".", keepPat);

        if (data == null) {
            return null;

        } else {
            String tmp = data + "";
            return tmp.replaceAll(pat, keepPatStr);
        }
    }
}


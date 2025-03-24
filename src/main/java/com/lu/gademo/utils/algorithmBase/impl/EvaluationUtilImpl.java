package com.lu.gademo.utils.algorithmBase.impl;


import com.lu.gademo.utils.EvaluationUtil;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;


//数值型评测处理工具
//脱敏前后对比
public class EvaluationUtilImpl implements EvaluationUtil {

    public static void main(String[] args) {
        // 定义测试数据
        List<Object> testData1 = new ArrayList<>();
        List<Object> testData2 = new ArrayList<>();
        testData1.add(1.0);
        testData1.add(2.0);
        testData1.add(3.0);
        testData1.add(1.0);
        testData1.add(2.0);
        testData1.add(null);

        testData2.add(1.5);
        testData2.add(2.5);
        testData2.add(0.0);
        testData2.add(1.0);
        testData2.add(2.5);
        testData2.add(3.0);

        EvaluationUtil evaluationUtil = new EvaluationUtilImpl();

        double distance = evaluationUtil.calculateEuclideanDistance(testData1, testData2);
        System.out.println("欧式距离：" + distance);

        double entropyDifference = evaluationUtil.calculateEntropyDifference(testData1, testData2);
        System.out.println("信息熵差值：" + entropyDifference);

        double MAE = evaluationUtil.calculateMAE(testData1, testData2);
        System.out.println("平均绝对误差：" + MAE);

        double MSE = evaluationUtil.calculateMSE(testData1, testData2);
        System.out.println("均方误差：" + MSE);

        Map<Object, Double> histogram1 = evaluationUtil.calculateHistogram(testData1);
        Map<Object, Double> histogram2 = evaluationUtil.calculateHistogram(testData2);
        for (Object key : histogram1.keySet()) {
            System.out.println("Value: " + key + ", Frequency: " + histogram1.get(key));
        }
        for (Object key : histogram2.keySet()) {
            System.out.println("Value: " + key + ", Frequency: " + histogram2.get(key));
        }

        double klDivergence = evaluationUtil.calculateKLDivergence(histogram1, histogram2);
        System.out.println("KL Divergence: " + klDivergence);


    }

    //偏差性度量
    //均方误差
    @Override
    public Double calculateMSE(List<Object> originalData, List<Object> desensitizedData) {
        if (originalData.size() != desensitizedData.size()) {
            throw new IllegalArgumentException("Data sets must have the same size.");
        }

        int dataSize = originalData.size();
        double sumSquaredError = 0.0;

        for (int i = 0; i < dataSize; i++) {
            Object original = originalData.get(i);
            Object desensitized = desensitizedData.get(i);

            if (original == null || desensitized == null) {
                // Skip if either value is null
                continue;
            }

            if (original instanceof Number && desensitized instanceof Number) {
                double originalValue = ((Number) original).doubleValue();
                double desensitizedValue = ((Number) desensitized).doubleValue();
                double squaredError = Math.pow(originalValue - desensitizedValue, 2);
                sumSquaredError += squaredError;
            } else {
                throw new IllegalArgumentException("Both originalData and desensitizedData must contain numeric values.");
            }
        }

        DecimalFormat df = new DecimalFormat("#.000");
        Double formattedValue = Double.parseDouble(df.format(sumSquaredError / dataSize));

        return formattedValue;
    }

    @Override
    public Double calculateMSE(Map<Object, Double> prob1, Map<Object, Double> prob2) {
        if (prob1.size() != prob2.size()) {
            throw new IllegalArgumentException("Probability distributions must have the same size");
        }

        double mse = 0.0;

        for (Map.Entry<Object, Double> entry1 : prob1.entrySet()) {
            Object key = entry1.getKey();
            Double value1 = entry1.getValue();
            Double value2 = prob2.get(key);

            if (value2 == null) {
                throw new IllegalArgumentException("Key not found in the second probability distribution: " + key);
            }

            mse += Math.pow(value1 - value2, 2);
        }

        DecimalFormat df = new DecimalFormat("#.000");
        Double formattedValue = Double.parseDouble(df.format(mse / prob1.size()));

        return formattedValue;
    }

    //平均绝对误差
    @Override
    public Double calculateMAE(List<Object> originalData, List<Object> desensitizedData) {
        if (originalData.size() != desensitizedData.size()) {
            throw new IllegalArgumentException("Data sets must have the same size.");
        }

        int n = originalData.size();
        double sumError = 0.0;

        for (int i = 0; i < n; i++) {
            Object original = originalData.get(i);
            Object desensitized = desensitizedData.get(i);

            if (original == null || desensitized == null) {
                // Skip if either value is null
                continue;
            }

            if (original instanceof Number && desensitized instanceof Number) {
                double originalValue = ((Number) original).doubleValue();
                double desensitizedValue = ((Number) desensitized).doubleValue();
                double error = Math.abs(originalValue - desensitizedValue);
                sumError += error;
            } else {
                throw new IllegalArgumentException("Both originalData and desensitizedData must contain numeric values.");
            }
        }

        DecimalFormat df = new DecimalFormat("#.000");
        Double formattedValue = Double.parseDouble(df.format(sumError / n));

        return formattedValue;
    }


    //信息损失性

    @Override
    public Double calculateMAE(Map<Object, Double> prob1, Map<Object, Double> prob2) {
        // 检查输入是否合法
        if (prob1 == null || prob2 == null || prob1.size() != prob2.size()) {
            throw new IllegalArgumentException("概率分布不合法或长度不一致");
        }

        // 计算 MAE
        double mae = 0.0;
        for (Map.Entry<Object, Double> entry : prob1.entrySet()) {
            Object key = entry.getKey();

            // 获取对应的概率值
            Double probValue1 = entry.getValue();
            Double probValue2 = prob2.get(key);

            // 如果概率值为 null，抛出异常
            if (probValue2 == null) {
                throw new IllegalArgumentException("概率分布不一致，缺少对应值");
            }

            // 计算绝对误差并累加
            mae += Math.abs(probValue1 - probValue2);
        }

        // 计算平均值
        mae /= prob1.size();

        DecimalFormat df = new DecimalFormat("#.000");
        Double formattedValue = Double.parseDouble(df.format(mae));


        return formattedValue;
    }

    //分布频率
    //生成一个频率分布的直方图(折线图、饼图)
    @Override
    public Map<Object, Double> calculateHistogram(List<Object> data) {
        Map<Object, Double> hist = new HashMap<>();
        int dataSize = 0;

        for (Object value : data) {
            if (value != null) {
                hist.put(value, hist.getOrDefault(value, 0.0) + 1.0);
                dataSize++;
            }
        }
        // 使用DecimalFormat格式化值，保留两位小数
        DecimalFormat df = new DecimalFormat("#.00");
        for (Object key : hist.keySet()) {
            Double formattedValue = Double.parseDouble(df.format(hist.get(key) / dataSize));
            hist.put(key, formattedValue);
        }
        return hist;
    }

    //Kl散度，需要先计算概率分布
    @Override
    public Double calculateKLDivergence(Map<Object, Double> p, Map<Object, Double> q) {
        double klDivergence = 0.0;
        for (Object key : p.keySet()) {
            double pValue = p.get(key);
            double qValue = q.getOrDefault(key, 0.0);

            if (pValue > 0 && qValue > 0) {
                klDivergence += pValue * Math.log(pValue / qValue);
            }
        }

        DecimalFormat df = new DecimalFormat("#.000");
        Double formattedValue = Double.parseDouble(df.format(klDivergence));

        return formattedValue;
    }

    //信息熵
    @Override
    public Double calculateEntropy(List<Object> list) {
        List<Double> re_data = new ArrayList<>();

        for (Object data : list) {
            if (data == null) {
                // 处理 null 值，例如将其跳过或赋予默认值
                // 这里的示例是跳过 null 值
                continue;
            }

            if (data instanceof Integer) {
                Integer data1 = (Integer) data;
                re_data.add(data1.doubleValue());
            } else if (data instanceof Double) {
                Double data1 = (Double) data;
                re_data.add(data1.doubleValue());
            }
        }

        int n = re_data.size(); // 这里使用 re_data 的大小而不是原始 list
        Map<Double, Integer> countMap = new HashMap<>();

        for (Double value : re_data) {
            countMap.put(value, countMap.getOrDefault(value, 0) + 1);
        }

        double entropy = 0.0;

        for (double value : countMap.keySet()) {
            double probability = (double) countMap.get(value) / n;
            entropy -= probability * Math.log(probability) / Math.log(2);
        }

        DecimalFormat df = new DecimalFormat("#.000");
        Double formattedValue = Double.parseDouble(df.format(entropy));

        return formattedValue;
    }

    @Override
    public Double calculateEntropy(Map<Object, Double> probabilities) {
        double entropy = 0.0;


        for (Map.Entry<Object, Double> entry : probabilities.entrySet()) {
            double probability = entry.getValue();
            if (probability > 0) { // 避免 log(0)
                entropy -= probability * (Math.log(probability) / Math.log(2));
            }
        }


        DecimalFormat df = new DecimalFormat("#.000");
        Double formattedValue = Double.parseDouble(df.format(entropy));

        return formattedValue;
    }

    // 信息熵差值
    @Override
    public Double calculateEntropyDifference(List<Object> originalData, List<Object> desensitizedData) {
        double entropy1 = calculateEntropy(originalData);
        double entropy2 = calculateEntropy(desensitizedData);


        DecimalFormat df = new DecimalFormat("#.000");
        Double formattedValue = Double.parseDouble(df.format(Math.abs(entropy1 - entropy2)));

        return formattedValue;
    }

    @Override
    public double calculateEntropyDifference(
            Map<Object, Double> probabilityDistribution1,
            Map<Object, Double> probabilityDistribution2) {
        double entropy1 = calculateEntropy(probabilityDistribution1);
        double entropy2 = calculateEntropy(probabilityDistribution2);

        DecimalFormat df = new DecimalFormat("#.000");
        Double formattedValue = Double.parseDouble(df.format(Math.abs(entropy1 - entropy2)));

        return formattedValue;
    }

    //欧氏距离
    @Override
    public Double calculateEuclideanDistance(List<Object> originalData, List<Object> desensitizedData) {
        if (originalData.size() != desensitizedData.size()) {
            throw new IllegalArgumentException("Data sets must have the same size.");
        }

        int dataSize = originalData.size();
        double sumSquaredError = 0.0;

        for (int i = 0; i < dataSize; i++) {

            Object originalObject = originalData.get(i);
            Double desensitizedValue = (Double) desensitizedData.get(i);
            if (originalObject instanceof Double) {
                Double originalValue = (Double) originalObject;
                if (originalValue != null && desensitizedValue != null) {
                    double squaredError = Math.pow(originalValue - desensitizedValue, 2);
                    sumSquaredError += squaredError;
                }

            } else if (originalObject instanceof Integer) {
                Integer originalValue = (Integer) originalObject;
                if (originalValue != null && desensitizedValue != null) {
                    double squaredError = Math.pow(originalValue - desensitizedValue, 2);
                    sumSquaredError += squaredError;
                }
            }
        }

        DecimalFormat df = new DecimalFormat("#.000");
        Double formattedValue = Double.parseDouble(df.format(Math.sqrt(sumSquaredError)));
        return formattedValue;
    }

    @Override
    public double calculateEuclideanDistance(Map<Object, Double> prob1, Map<Object, Double> prob2) {
        double sum = 0.0;

        for (Object key : prob1.keySet()) {
            double prob1Value = prob1.get(key);
            double prob2Value = prob2.getOrDefault(key, 0.0); // Default to 0 if key not present in prob2

            double diff = prob1Value - prob2Value;
            sum += diff * diff;
        }

        for (Object key : prob2.keySet()) {
            if (!prob1.containsKey(key)) {
                double prob2Value = prob2.get(key);
                sum += prob2Value * prob2Value;
            }
        }

        DecimalFormat df = new DecimalFormat("#.000");
        Double formattedValue = Double.parseDouble(df.format(Math.sqrt(Math.sqrt(sum))));

        return formattedValue;
    }

    public double[] readVectorFromFile(String filePath) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line = reader.readLine();
            if (line != null) {
                // Assuming the vector is stored as a JSON array
                String[] values = line.replace("[", "").replace("]", "").split(",");
                return Arrays.stream(values)
                        .mapToDouble(Double::parseDouble)
                        .toArray();
            }
        }
        throw new IOException("Failed to read vector from file: " + filePath);
    }
}

package com.lu.gademo.utils.impl;

import com.lu.gademo.utils.CommandExecutor;
import com.lu.gademo.utils.DSObject;
import com.lu.gademo.utils.Dp;
import com.lu.gademo.utils.DpUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class DpImpl implements Dp {

    private final DpUtil dpUtil;

    @Autowired
    public DpImpl(DpUtil dpUtil) {
        this.dpUtil = dpUtil;
    }

    public DSObject service(DSObject object, Integer alg, Number... params) {
        log.info("调用差分隐私算法统一接口");

        if (object == null) return null;

        File directory = new File("");
        String currentPath = directory.getAbsolutePath();
        String path1 = Paths.get(currentPath, "perturbation", "differential_privacy", "dp.py").toString();
        String path2 = Paths.get(currentPath, "perturbation", "other", "randomNoise.py").toString();

        switch (alg) {
            /*
                拉普拉斯机制 Laplace Mechanism
                随机添加拉普拉斯噪声
                功能：多个数值加噪
                参数：ε、全局敏感度
                输入：数值数值一维数组
                输出：数值一维数组（长度为n）

            */
//            case 1 : {
//               if (params.length != 2) return null;
//               List<Object> value = new ArrayList<>();
//               Double val = object.getDoubleVal();
//               for (int i = 0; i < params[1].intValue(); i++) {
//                   value.add(val);
//               }
//               return new DSObject(dpUtil.laplaceToValue(value, params[0].intValue()));
//            }

            case 1: {
                if (params.length != 1) return null;
//                List<Object> value = new ArrayList<>();
                List<Object> val = new ArrayList<>(object.getList());
                return new DSObject(dpUtil.laplaceToValue(val, params[0].intValue()));
            }

            /*
                Report Noisy Max1-Laplace
                随机添加拉普拉斯噪声后返回最大值的下标
                输入：数值列表、算法标识符 -> 2、隐私预算、采样次数 n
                输出：最大值下标列表
            */
            case 2: {
                if (params.length != 2) return null;
                List<Object> value = new ArrayList<>(object.getList());
                List<Integer> result = new ArrayList<>();
                for (int i = 0; i < params[0].intValue(); i++) {
                    List<Double> data = dpUtil.laplaceToValue(value, params[1].intValue());
                    result.add(data.indexOf(Collections.max(data)));
                }
                return new DSObject(result);
            }

            /*
                Report Noisy Max3
                随机添加拉普拉斯噪声后返回最大值
                输入：数值列表、算法标识符 -> 3、隐私预算、采样次数 n
                输出：最大值下标
            */
            case 3: {
                if (params.length != 2) return null;
                List<Object> value = new ArrayList<>(object.getList());
                List<Double> result = new ArrayList<>();
                for (int i = 0; i < params[0].intValue(); i++) {
                    result.add(Collections.max(dpUtil.laplaceToValue(value, params[1].intValue())));
                }
                return new DSObject(result);
            }

            /*
                Snapping Mechanism
                拉普拉斯机制的变体，对数值和结果进行截断处理。
                输入：数值列表、算法标识符 -> 4、采样次数 n
                输出：数值一维数组 length = n
            */
            case 4: {
                if (params.length != 2) return null;
                List<?> value = object.getList();
                String array = StringUtils.join(value, ",");
                List<String> results = CommandExecutor.executePython(array, "snapping", path1, params[0].toString(), params[1].toString());
                String s = "";

                for (int i = results.size() - 1; i >= 0; i--) {
                    if (StringUtils.isNotBlank(results.get(i))) {
                        s = results.get(i);
                        break;
                    }
                }

                return new DSObject(s);
            }

            /*
                IM-Coder1
                功能：为图片添加拉普拉斯差分噪声，保持图像可用性，降低机器学习模型识别精度
                参数：ε
                输入：原图片文件路径，脱敏图片存放路径，隐私级别
                输出：脱敏执行信息
            */
            case 5: {
                if (params.length != 1) return null;
                List<?> value = object.getList();
                String path3 = Paths.get(currentPath, "image", "dpImage.py").toString();
                List<String> results = CommandExecutor.executePython(value.get(0).toString() + " " + value.get(1).toString(), "", path3, params[0].toString());
                return new DSObject(results);
            }

            /*
                声纹特征脱敏算法
                功能：对声纹特征添加 Laplace 噪声
                参数：ε
                输入：原音频文件路径，脱敏音频存放路径，隐私级别
                输出：脱敏执行信息
            */
            case 6: {
                if (params.length != 1) return null;
                List<?> value = object.getList();
                String path4 = Paths.get(currentPath, "audio", "desenAudio.py").toString();
                List<String> results = CommandExecutor.executePython(value.get(0).toString() + " " + value.get(1).toString(), "dpAudio", path4, params[0].toString());
                return new DSObject(results);
            }

            /*
                图形差分隐私脱敏算法
                功能：对图形数据添加laplace噪声
                参数：ε
                输入：原图形文件路径，脱敏图形文件存放路径，隐私级别
                输出：脱敏后图形文件保存在脱敏图形文件存放路径

            */
            case 7: {
                if (params.length != 1) return null;
                List<?> value = object.getList();
                String path5 = Paths.get(currentPath, "graph", "desenGraph.py").toString();
                System.out.println(path5);
                List<String> result = CommandExecutor.executePython(value.get(0).toString() + " " + value.get(1).toString(), "", path5, params[0].toString());
                return new DSObject(result);
            }

            /*
                指数机制 Exponential Mechanism
                功能：一组数值中某个数值被选中的概率
                参数：ε、全局敏感度
                输入：数值、采样次数 n
                输出：数值一维数组（长度为 n）
            */
            case 8: {
                if (params.length != 2) return null;
                List<?> value = object.getList();
                String array = StringUtils.join(value, ",");
                List<String> results = CommandExecutor.executePython(array, "exponential", path1, params[0].toString(), params[1].toString());
                String s = "";

                for (int i = results.size() - 1; i >= 0; i--) {
                    if (StringUtils.isNotBlank(results.get(i))) {
                        s = results.get(i);
                        break;
                    }
                }
                s = s.replace("[", "");
                s = s.replace("]", "");
                List<Double> list = Arrays.stream(s.split(" ")).filter(string -> !string.isEmpty()).map(Double::parseDouble).collect(Collectors.toList());
                return new DSObject(list);
            }

            /*
                Report Noisy Max2-Exponential
                功能：给数组加指数噪声后返回最大值的下标
                参数：ε、全局敏感度
                输入：数值、采样次数 n
                输出：数值一维数组（长度为 n）
            */
            case 9: {
                if (params.length != 2) return null;
                List<?> value = object.getList();
                String array = StringUtils.join(value, ",");
                List<String> results = CommandExecutor.executePython(array, "report_noisy_max2", path1, params[0].toString(), params[1].toString());
                String s = "";

                for (int i = results.size() - 1; i >= 0; i--) {
                    if (StringUtils.isNotBlank(results.get(i))) {
                        s = results.get(i);
                        break;
                    }
                }
                s = s.replace("[", "");
                s = s.replace("]", "");
                List<Double> list = Arrays.stream(s.split(" ")).filter(string -> !string.isEmpty()).map(Double::parseDouble).collect(Collectors.toList());
                return new DSObject(list);
            }

            /*
                Report Noisy Max4
                功能：给数组加指数噪声后返回其中的最大值
                参数：ε、全局敏感度
                输入：数值、采样次数 n
                输出：数值一维数组（长度为 n）
            */
            case 10: {
                if (params.length != 2) return null;
                List<?> value = object.getList();
                String array = StringUtils.join(value, ",");
                List<String> results = CommandExecutor.executePython(array, "report_noisy_max4",
                        path2, params[0].toString(), params[1].toString());
                String s = "";

                for (int i = results.size() - 1; i >= 0; i--) {
                    if (StringUtils.isNotBlank(results.get(i))) {
                        s = results.get(i);
                        break;
                    }
                }
                s = s.replace("[", "");
                s = s.replace("]", "");
                List<Double> list = Arrays.stream(s.split(" ")).filter(string -> !string.isEmpty()).map(Double::parseDouble).collect(Collectors.toList());
                return new DSObject(list);
            }

            /*
                Sparse Vector Technique1
                给数组值和阈值 t 加噪后，返回数组中元素取值是否大于 t，计数大于 t 的结果超过 c 个后则终止，即后续结果返回 -1
                参数：ε、阈值 t、个数 c
                输入：数值数组、采样次数 n
                输出：数值 n 维数组（大小为 n × 输入数组长度）
            */
            case 11: {
                if (params.length != 3) return null;
                List<?> value = object.getList();
                String array = StringUtils.join(value, ",");
                List<String> results = CommandExecutor.executePython(array, "sparse_vector_technique1", path1,
                        params[0].toString(), params[1].toString(), params[2].toString());
                List<List<Integer>> lists = new ArrayList<>();
                String s = "";

                for (int i = results.size() - 1; i >= 0; i--) {
                    if (StringUtils.isNotBlank(results.get(i))) {
                        s = results.get(i);
                        break;
                    }
                }

                s = s.replace("[", "");
                s = s.replace("]", "");
                List<Integer> newList = Arrays.stream(s.split(" ")).filter(string -> !string.isEmpty()).map(Integer::parseInt).collect(Collectors.toList());
                if (!newList.isEmpty()) {
                    lists.add(newList);
                }
//                for (String s : results) {
//                    s = s.replace("[", "");
//                    s = s.replace("]", "");
//                    List<Integer> newList = Arrays.stream(s.split(" ")).filter(string -> !string.isEmpty())
//                            .map(Integer::parseInt).collect(Collectors.toList());
//                    if (!newList.isEmpty()) {
//                        lists.add(newList);
//                    }
//                }
                return new DSObject(lists);
            }

            /*
                Sparse Vector Technique2
                给数组值和阈值 t 加噪后，返回数组中元素取值是否大于 t，计数大于 t 的结果超过 c 个后则终止，即后续结果返回 -1
                参数：ε、阈值 t、个数 c
                输入：数值数组、采样次数 n
                输出：数值 n 维数组（大小为 n × 输入数组长度）
            */
            case 12: {
                if (params.length != 3) return null;
                List<?> value = object.getList();
                String array = StringUtils.join(value, ",");
                List<String> results = CommandExecutor.executePython(array, "sparse_vector_technique2",
                        path1, params[0].toString(), params[1].toString(), params[2].toString());
                List<List<Integer>> lists = new ArrayList<>();
                String s = "";

                for (int i = results.size() - 1; i >= 0; i--) {
                    if (StringUtils.isNotBlank(results.get(i))) {
                        s = results.get(i);
                        break;
                    }
                }

                s = s.replace("[", "");
                s = s.replace("]", "");
                List<Integer> newList = Arrays.stream(s.split(" ")).filter(string -> !string.isEmpty()).map(Integer::parseInt).collect(Collectors.toList());
                if (!newList.isEmpty()) {
                    lists.add(newList);
                }
//                for (String s : results) {
//                    s = s.replace("[", "");
//                    s = s.replace("]", "");
//                    List<Integer> newList = Arrays.stream(s.split(" ")).filter(string -> !string.isEmpty()).map(Integer::parseInt).collect(Collectors.toList());
//                    if (!newList.isEmpty()) {
//                        lists.add(newList);
//                    }
//                }
                return new DSObject(lists);
            }

            /*
                Sparse Vector Technique3
                功能：给数组值和阈值 t 加噪后，如果数组中元素小于 t 则返回 -1000，大于 t 则返回加噪后的元素值，计数大于 t 的结果超过 c 个后则终止，即后续结果返回 -2000
                参数：ε、阈值 t、个数 c
                输入：数值数组、采样次数 n
                输出：数值 n 维数组（大小为 n × 输入数组长度）
            */
            case 13: {
                if (params.length != 3) return null;
                List<?> value = object.getList();
                String array = StringUtils.join(value, ",");
                List<String> results = CommandExecutor.executePython(array, "sparse_vector_technique3", path2,
                        params[0].toString(), params[1].toString(), params[2].toString());
                List<List<Double>> lists = new ArrayList<>();
                for (String s : results) {
                    s = s.replace("[", "");
                    s = s.replace("]", "");
                    List<Double> newList = Arrays.stream(s.split(" ")).filter(string -> !string.isEmpty())
                            .map(Double::parseDouble).collect(Collectors.toList());
                    if (!newList.isEmpty()) {
                        lists.add(newList);
                    }
                }
                return new DSObject(lists);
            }

            /*
                Sparse Vector Technique4
                功能：功能：给数组值和阈值 t 加噪后，返回数组中元素取值是否大于 t，计数大于 t 的结果超过 c 个后则终止，即后续结果返回 -1
                参数：ε、阈值 t、个数 c
                输入：数值数组、采样次数 n
                输出：数值 n 维数组（大小为 n × 输入数组长度）
            */
            case 14: {
                if (params.length != 3) return null;
                List<?> value = object.getList();
                String array = StringUtils.join(value, ",");
                List<String> results = CommandExecutor.executePython(array, "sparse_vector_technique4",
                        path2, params[0].toString(), params[1].toString(), params[2].toString());
                List<List<Integer>> lists = new ArrayList<>();
                for (String s : results) {
                    s = s.replace("[", "");
                    s = s.replace("]", "");
                    List<Integer> newList = Arrays.stream(s.split(" ")).filter(string -> !string.isEmpty()).map(Integer::parseInt).collect(Collectors.toList());
                    if (!newList.isEmpty()) {
                        lists.add(newList);
                    }
                }
                return new DSObject(lists);
            }

            /*
                Sparse Vector Technique5
                功能：只给阈值 t 加噪后，返回数组中所有元素取值是否大于 t
                参数：ε、阈值 t、个数 c
                输入：数值数组、采样次数 n
                输出：数值 n 维数组（大小为 n × 输入数组长度）
            */
            case 15: {
                if (params.length != 3) return null;
                List<?> value = object.getList();
                String array = StringUtils.join(value, ",");
                List<String> results = CommandExecutor.executePython(array, "sparse_vector_technique5",
                        path2, params[0].toString(), params[1].toString(), params[2].toString());
                List<List<Integer>> lists = new ArrayList<>();
                for (String s : results) {
                    s = s.replace("[", "");
                    s = s.replace("]", "");
                    List<Integer> newList = Arrays.stream(s.split(" ")).filter(string -> !string.isEmpty()).map(Integer::parseInt).collect(Collectors.toList());
                    if (!newList.isEmpty()) {
                        lists.add(newList);
                    }
                }
                return new DSObject(lists);
            }

            /*
                Sparse Vector Technique6
                功能：给数组值和阈值 t 加噪后，返回数组所有元素取值是否大于 t
                参数：ε、阈值 t、个数 c
                输入：数值数组、采样次数 n
                输出：数值 n 维数组（大小为 n × 输入数组长度）
            */
            case 16: {
                if (params.length != 3) return null;
                List<?> value = object.getList();
                String array = StringUtils.join(value, ",");
                List<String> results = CommandExecutor.executePython(array, "sparse_vector_technique6", path2,
                        params[0].toString(), params[1].toString(), params[2].toString());
                List<List<Integer>> lists = new ArrayList<>();
                for (String s : results) {
                    s = s.replace("[", "");
                    s = s.replace("]", "");
                    List<Integer> newList = Arrays.stream(s.split(" ")).filter(string -> !string.isEmpty()).map(Integer::parseInt).collect(Collectors.toList());
                    if (!newList.isEmpty()) {
                        lists.add(newList);
                    }
                }
                return new DSObject(lists);
            }

            /*
                Numerical Sparse Vector Technique
                功能：给数组值和阈值 t 加噪后，如果数组中元素小于 t 则返回 0，大于 t 则返回加噪后的元素值，计数大于 t 的结果超过 c 个后则终止，即后续结果返回- 1000
                参数：ε、阈值 t、个数 c
                输入：数值数组、采样次数 n
                输出：数值 n 维数组（大小为 n × 输入数组长度）
            */
            case 17: {
                if (params.length != 3) return null;
                List<?> value = object.getList();
                String array = StringUtils.join(value, ",");
                List<String> results = CommandExecutor.executePython(array, "sparse_vector_technique_numerical",
                        path1, params[0].toString(), params[1].toString(), params[2].toString());
                List<List<Double>> lists = new ArrayList<>();
                for (String s : results) {
                    s = s.replace("[", "");
                    s = s.replace("]", "");
                    List<Double> newList = Arrays.stream(s.split(" ")).filter(string -> !string.isEmpty()).map(Double::parseDouble).collect(Collectors.toList());
                    if (!newList.isEmpty()) {
                        lists.add(newList);
                    }
                }
                return new DSObject(lists);
            }

            /*
                Rappor
                基于随机响应统计用户特征的直方图（频次）信息
                参数：哈希函数个数h、bloom 过滤器大小 k、纵向隐私保护预算f、概率p、概率q、采样次数n
                输入：数值 采样次数 n
                输出：n 维二进制数组（大小为 n × k）
            */
            case 18: {
                if (params.length != 2) return null;
                Double value = object.getDoubleVal();
                List<String> results = CommandExecutor.executePython(value.toString(), "rappor", path1,
                        params[0].toString(), params[1].toString());
                List<List<Integer>> lists = new ArrayList<>();
                for (String s : results) {
                    s = s.replace("[", "");
                    s = s.replace("]", "");
                    s = s.replace(".", "");
                    List<Integer> newList = Arrays.stream(s.split(" ")).filter(string -> !string.isEmpty()).map(Integer::parseInt).collect(Collectors.toList());
                    if (!newList.isEmpty()) {
                        lists.add(newList);
                    }
                }
                return new DSObject(lists);
            }

             /*
                One Time Rappor
                基于随机响应统计用户特征的直方图（频次）信息
                参数：哈希函数个数 h、bloom过滤器大小 k、纵向隐私保护预算 f、采样次数 n
                输入：数值 采样次数 n
                输出：n 维二进制数组（大小为 n × k）
            */
            case 19: {
                if (params.length != 2) return null;
                Double value = object.getDoubleVal();
                List<String> results = CommandExecutor.executePython(value.toString(), "onetimerappor", path1, params[0].toString(), params[1].toString());
                System.out.println(results);
                List<List<Integer>> lists = new ArrayList<>();
                for (String s : results) {
                    s = s.replace("[", "");
                    s = s.replace("]", "");
                    s = s.replace(".", "");
                    List<Integer> newList = Arrays.stream(s.split(" ")).filter(string -> !string.isEmpty()).map(Integer::parseInt).collect(Collectors.toList());
                    if (!newList.isEmpty()) {
                        lists.add(newList);
                    }

                }
                return new DSObject(lists);
            }

            /*
                编码型数据差分隐私脱敏算法
                功能：给编码型数据进行GRR扰动
                参数：ε
                输入：一维数组
                输出：一维数组
            */
            case 20: {
                if (params.length != 1) return null;
                List<?> list = object.getList();
                List<Object> value = new ArrayList<>(list);
                return new DSObject(dpUtil.dpCode(value, params[0].intValue()));
            }

            /*
                功能：随机均匀噪声
                参数：ε
                输入：一维数组
                输出：一维数组
            */
            case 21: {
                if (params.length != 1) return null;
                List<?> list = object.getList();
                List<Object> value = new ArrayList<>(list);
                return new DSObject(dpUtil.randomUniformToValue(value, params[0].intValue()));
            }

            /*
                功能：随机拉普拉斯噪声
                参数：ε
                输入：一维数组
                输出：一维数组
            */
            case 22: {
                if (params.length != 1) return null;
                List<?> list = object.getList();
                List<Object> value = new ArrayList<>(list);
                return new DSObject(dpUtil.randomLaplaceToValue(value, params[0].intValue()));
            }

            /*
                功能：随机随机高斯噪声
                参数：ε
                输入：一维数组
                输出：一维数组
            */
            case 23: {
                if (params.length != 1) return null;
                List<?> list = object.getList();
                List<Object> value = new ArrayList<>(list);
                return new DSObject(dpUtil.randomGaussianToValue(value, params[0].intValue()));
            }

            /*
                Noisy Histogram1
                功能：给直方图的每个值加噪
                参数：ε、全局敏感度
                输入：数值一维数组（直方图）、采样次数n
                输出：数值n维数组（大小为n × 直方图长度）
            */
            case 24: {
                if (params.length != 2) return null;
                List<?> value = object.getList();
                String array = StringUtils.join(value, ",");
                List<String> results = CommandExecutor.executePython(array, "noisy_hist1", path1, params[0].toString(), params[1].toString());
                List<List<Double>> lists = new ArrayList<>();
                for (String s : results) {
                    s = s.replace("[", "");
                    s = s.replace("]", "");
                    List<Double> newList = Arrays.stream(s.split(" ")).filter(string -> !string.isEmpty())
                            .map(Double::parseDouble).collect(Collectors.toList());
                    if (!newList.isEmpty()) {
                        lists.add(newList);
                    }
                }
                return new DSObject(lists);
            }

            /*
                Noisy Histogram2
                功能：给直方图的每个值加噪 反馈影响门限
                参数：ε、全局敏感度
                输入：数值一维数组（直方图）、采样次数n
                输出：数值n维数组（大小为n × 直方图长度）
            */
            case 25: {
                if (params.length != 2) return null;
                List<?> value = object.getList();
                String array = StringUtils.join(value, ",");
                List<String> results = CommandExecutor.executePython(array, "noisy_hist2", path2, params[0].toString(), params[1].toString());
                List<List<Double>> lists = new ArrayList<>();
                for (String s : results) {
                    s = s.replace("[", "");
                    s = s.replace("]", "");
                    List<Double> newList = Arrays.stream(s.split(" ")).filter(string -> !string.isEmpty()).map(Double::parseDouble).collect(Collectors.toList());
                    if (!newList.isEmpty()) {
                        lists.add(newList);
                    }
                }
                return new DSObject(lists);
            }

            /*
                基于差分隐私的日期加噪算法
                功能：对日期数据添加laplace噪声
                参数：ε
                输入：日期数组
                输出：日期数组
             */
            case 26: {
                if (params.length != 1) return null;
                List<?> list = object.getList();
                List<Object> value = new ArrayList<>(list);
                try {
                    return new DSObject(dpUtil.dpDate(value, params[0].intValue()));
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }

            }
            /*
                基于高斯机制差分隐私的数值加噪算法
                未使用

             */

            /*
             IM-Coder2
            * */
            case 27: {
                if (params.length != 1) return null;
                List<?> value = object.getList();
                String path3 = Paths.get(currentPath, "image", "canny.py").toString();
                List<String> results = CommandExecutor.executePython(value.get(0).toString() + " " + value.get(1).toString(), "", path3, params[0].toString());
                return new DSObject(results);
            }

//            case 27: {
//                if (params.length != 1) return null;
//                List<?> list = object.getList();
//                List<Object> value = new ArrayList<>(list);
//                return new DSObject(dpUtil.gaussianToValue(value, params[0].intValue()));
//            }

            default:
                return null;
        }
    }

//    public List<String> executePython(String rawData, String algName, String path, String...params) {
//
//        Util util = new UtilImpl();
//        String python = util.isLinux() ? "python3" : "python";
//
//        try {
//            // 指定Python脚本路径
//            System.out.println(path);
//
//            // 创建参数列表
//            StringBuilder command = new StringBuilder(python + " " + path + " " + algName + " " + rawData);
//            for (String param : params) {
//                command.append(" ").append(param);
//            }
//            System.out.println(command);
//
//            return CommanExecutor.openExe(command.toString());
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
}

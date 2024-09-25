package com.lu.gademo.utils.impl;

import com.lu.gademo.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Slf4j
@Component
public class AnonymityImpl implements Anonymity {
    private final KAnonymityUtil kAnonymityUtil;

    public AnonymityImpl() {
//        File directory = new File("");
        this.kAnonymityUtil = new KAnonymityUtil();
//        String currentPath = directory.getAbsolutePath();
//        String locationPrivacy = util.isLinux() ? "LocationPrivacy" : "LocationPrivacy.exe";
//        String path = Paths.get(currentPath, locationPrivacy).toString();
    }

    public DSObject service(DSObject object, Integer alg, Number... params) {
        log.info("调用匿名算法统一接口");
//        System.out.println(path);
        if (object == null) return null;
        switch (alg) {

            /*
                k-匿名
                功能：对csv文件进行k-匿名处理
                参数：k
                输入：数值或标识符型 csv 文件
                输出：csv文件
            */
            case 1: {
                if (params.length != 1) return null;
                List<?> value = object.getList();
                String param = Integer.toString(params[0].intValue());
                String baseName = value.get(0).toString();
                String dir = value.get(1).toString();
                String attribute = value.get(2).toString();
                String result;
                try {
                    result = kAnonymityUtil.kAnonymity(baseName, dir, param, attribute, object.getIntVal());
                } catch (Exception e) {
                    log.error(e.getMessage());
                    return null;
                }
                return new DSObject(result);
            }

            /*
                基于圆形匿名区域的虚假位置生成算法 CirDummy

                功能：
                基于包含用户真实位置的虚拟圆产生匿名位置。
                该算法首先根据匿名区域的面积来随机选取一个满足要求的圆心，然后根据匿名度 k 来确定每一个扇形的角度，
                最后根据偏移系数将位于扇形顶点处的匿名位置进行适当的偏移，从而生成了包含 k - 1 个匿名位置和 1 个真实位置的匿名位置集，
                并且位置集中位置点的分布近似于圆形。

                参数：匿名度 k、匿名区域的面积 s_cd、确定圆环内径的系数 rho
                输入：经度、纬度
                输出：经度数组、纬度数组
            */

//            case 2: {
//                if (params.length != 3) return null;
//                String position = object.getStringVal();
//                String[] s = position.split(",");
//                String cmd = path + " 2 " + s[0] + " " + s[1] + " " + params[0].toString() + " " + params[1].toString() + " " + params[2].toString();
//                return new DSObject(CommandExecutor.openExe(cmd));
//            }
            case 2: {
                if (object == null || params == null || params.length != 3) return null;

                String position = object.getStringVal();
                if (position == null || position.isEmpty()) return null;

                try {
                    String[] s = position.split(",");
                    if (s.length != 2) return null;

                    double x = Double.parseDouble(s[0]);
                    double y = Double.parseDouble(s[1]);
                    int k = params[0].intValue();
                    double sCd = params[1].doubleValue();
                    double rho = params[2].doubleValue();
                    double[] retArrX = new double[k];
                    double[] retArrY = new double[k];

                    if (kAnonymityUtil == null) {
                        log.error("kAnonymityUtil is not initialized");
                        return null;
                    }

                    kAnonymityUtil.cirDummy(x, y, k, sCd, rho, retArrX, retArrY);

                    List<String> result = new LinkedList<>();
                    for (int i = 0; i < k; ++i) {
                        result.add(String.format("%f,%f", retArrX[i], retArrY[i]));
                    }

                    return new DSObject(result);
                } catch (Exception e) {
                    log.error("Error in CirDummy method: ", e);
                    return null;
                }
            }

            /*
                基于网格匿名区域的虚假位置生成算法 GridDummy

                功能：
                基于包含用户真实位置的虚拟方格产生虚拟位置。
                基于覆盖用户位置的虚拟网格生成 k 个虚拟位置，并返回 k 个虚拟位置的集合。同时，生成的虚拟方格还需要满足用户预先定义的匿名区域的面积的要求。
                并且位置集中位置点的分布近似于圆形。

                参数：匿名度 k、匿名区域的面积 s_cd
                输入：经度、纬度
                输出：经度数组、纬度数组
            */
//            case 3: {
//                if (params.length != 2) return null;
//                String position = object.getStringVal();
//                String[] s = position.split(",");
//                String cmd = path + " 3 " + s[0] + " " + s[1] + " " + params[0].toString() + " " + params[1].toString();
//                return new DSObject(CommandExecutor.openExe(cmd));
//            }
            case 3: {
                if (object == null || params == null || params.length != 2) return null;

                String position = object.getStringVal();
                if (position == null || position.isEmpty()) return null;

                try {
                    String[] s = position.split(",");
                    if (s.length != 2) return null;

                    double x = Double.parseDouble(s[0]);
                    double y = Double.parseDouble(s[1]);
                    int k = params[0].intValue();
                    double sCd = params[1].doubleValue();
                    int len = (int) Math.ceil(Math.sqrt(k)) * (int) Math.ceil(Math.sqrt(k));
                    double[] retArrX = new double[len];
                    double[] retArrY = new double[len];

                    // 调用 gridDummy 方法生成虚假位置
                    kAnonymityUtil.gridDummy(x, y, k, sCd, retArrX, retArrY);

                    List<String> result = new ArrayList<>();
                    for (int i = 0; i < len; ++i) {
                        result.add(String.format("%f,%f", retArrX[i], retArrY[i]));
                    }

                    return new DSObject(result);
                } catch (Exception e) {
                    log.error("Error in GridDummy method: ", e);
                    return null;
                }
            }

            /*
                基于区域分割的虚假位置生成算法 Adaptive Interval Cloaking Algorithm

                功能：
                简单理解为四分法，由坐标边界可以形成一个包含所有缓存位置信息的矩形区域，然后根据用户的真实位置以及用户设置的最小匿名度，来将矩形区域进行划分。
                每次将矩形区域划分为四部分，分别为第一象限、第二象限、第三象限和第四象限。
                首先判断用户的真实位置所在的象限，然后判断该象限中包含的缓存位置信息数量是否大于最小匿名度，若大于，则继续划分；否则，将上一次的划分结果作为最终的矩形区域，
                输出该区域中所包含的缓存位置信息以及真实的用户位置信息。最终为用户形成一个包含用户数量刚好大于最小匿名度、且不可再分的隐藏区域。

                参数：最小匿名度（区域内包含的最小用户数量）、包含所有用户的矩形区域的横纵坐标范围（经度、纬度）、区域内所有用户 ID 及坐标
                输入：经度、纬度
                输出：经度数组、纬度数组

            */
//            case 4: {
//                if (params.length != 1) return null;
//                String position = object.getStringVal();
//                String[] s = position.split(",");
//                List<?> list = object.getList();
//                List<Object> value = new ArrayList<>(list);
//                String[] s1 = value.get(0).toString().split(",");
//                String[] s2 = value.get(1).toString().split(",");
//                String cmd = path + " 4 " + s[0] + " " + s[1] + " " + params[0].toString() + " " + s1[0] + " " + s1[1] + " " + s2[0] + " " + s2[1];
//                return new DSObject(CommandExecutor.openExe(cmd));
//            }
            case 4: {
                String position = object.getStringVal();
                String[] s = position.split(",");
                List<?> list = object.getList();
                List<Object> value = new ArrayList<>(list);
                String[] s1 = value.get(0).toString().split(",");
                String[] s2 = value.get(1).toString().split(",");
                try {
                    double KMin = params[0].doubleValue();
                    int kMin = (int) Math.ceil(KMin);
                    double x = Double.parseDouble(s[0]);
                    double y = Double.parseDouble(s[1]);
                    double xMin = Double.parseDouble(s1[0]);
                    double yMin = Double.parseDouble(s1[1]);
                    double xMax = Double.parseDouble(s2[0]);
                    double yMax = Double.parseDouble(s2[1]);
                    double[] retArrX = new double[kMin];
                    double[] retArrY = new double[kMin];
                    LinkedList<String> result = new LinkedList<>();
                    kAnonymityUtil.adaptiveIntervalCloakingWrapper(x, y, kMin, xMin, yMin, xMax, yMax, retArrX, retArrY);
                    for (int i = 0; i < kMin; ++i) {
                         result.add(String.format("%f,%f", retArrX[i], retArrY[i]));
                    }
                    return new DSObject(result);

                } catch (Exception e) {
                    log.error("Error in AdaptiveIntervalCloaking method: ", e);
                    return null;
                }
            }


            /*
                基于缓存信息的虚假位置生成算法 CaDSA

                功能：
                该算法基于缓存选择虚拟位置。
                该算法首先选择 4k 个查询概率与虚拟位置 Cr 最接近的单元格，然后从中随机选出2k个单元格，由此可以为当前查询实现高熵。
                最后从上述 2k 个单元格中选出对缓存贡献最大的 k - 1 个单元格，注意当此子集个数较多时，只随机选择 S 个子集。
                从而生成了包含 k - 1 个单元格和 1 个包含真实位置的单元格的匿名位置集。

                参数：用于空间划分的参数 N、系统参数 S、匿名度 K、N * N 个块的历史查询概率
                输入：经度、纬度、算法类型(1, 2)
                输出：经度向量、纬度向量
            */
//            case 5: {
//                if (params.length != 1) return null;
//                String[] s = object.getStringVal().split(",");
//                String param = "9 " + s[0] + " " + s[1] + " " + params[0].toString();
//                System.out.println(param);
//                String cmd = path + " " + param;
//                return new DSObject(CommandExecutor.openExe(cmd));
//            }
            case 5: {
                if (params.length != 1) return null;
                String position = object.getStringVal();
                String[] s = position.split(",");
                int op = params[0].intValue();
                double x = Double.parseDouble(s[0]);
                double y = Double.parseDouble(s[1]);
                List<Double> vecRetArrX = new ArrayList<>();
                List<Double> vecRetArrY = new ArrayList<>();

                KAnonymityUtil.caDsaAlgorithm(x, y, op, vecRetArrX, vecRetArrY);
                List<String> result = new ArrayList<>();
                for (int i = 0; i < vecRetArrX.size(); ++i) {
                    result.add(String.format("%f,%f", vecRetArrX.get(i), vecRetArrY.get(i)));
                }
                return new DSObject(result);

            }

            /*
                基于匿名位置库的虚假位置生成算法 K-anonymity
                功能：从预先生成的虚拟位置库中随机选择 K - 1 个匿名位置，与真实位置一起形成K-匿名位置集合。
                参数：匿名度、虚拟位置库经纬度
                输入：经度、纬度
                输出：经度数组、纬度数组
            */
            case 6: {
                if (params.length != 1) return null;
                String[] s = object.getStringVal().split(",");
//                String param = "1 " + s[0] + " " + s[1] + " " + params[0].toString();
//                String cmd = path + " " + param;
//                return new DSObject(CommandExecutor.openExe(cmd));


                double x = Double.parseDouble(s[0]);
                double y = Double.parseDouble(s[1]);
                int k = params[0].intValue();
                double[] retArrX = new double[k];
                double[] retArrY = new double[k];
                KAnonymityUtil.kAnonymityAlgorithm(x, y, k, retArrX, retArrY);
                List<String> result = new ArrayList<>();
                for (int i = 0; i < k; ++i) {
                    result.add(String.format("%f,%f", retArrX[i], retArrY[i]));
                }

                return new DSObject(result);

            }

            /*
                L-多样性
                功能：在同一个等价类中至少出现 L 不同的敏感属性值
                参数：k、l、泛化模板
                输入：数值或标识符型 csv 文件
                输出：csv文件
            */
            case 7: {
                if (params.length != 1) return null;
                List<?> value = object.getList();
                String param = Integer.toString(params[0].intValue());
                String baseName = value.get(0).toString();
                String dir = value.get(1).toString();
                String attribute = value.get(2).toString();
                int length = value.size();
                String result;
                try {
                    result = kAnonymityUtil.lDistinctDiversity(baseName, dir, param, attribute, length);
                } catch (Exception e) {
                    log.error(e.getMessage());
                    return null;
                }
                return new DSObject(result);
            }

            /*
                Entropy-l-diversity
                功能：在一个等价类中敏感数据分布熵的大小至少是log(L),返回处理后的csv文件
                参数：k、l、泛化模板
                输入：数值或标识符型csv文件
                输出：数值或标识符型csv文件
            */
            case 8: {
                if (params.length != 1) return null;
//                int[] l_diversity_param = new int[]{2, 4, 6};
                List<?> value = object.getList();
                String param = Integer.toString(params[0].intValue());
                String baseName = value.get(0).toString();
                String dir = value.get(1).toString();
                String attribute = value.get(2).toString();
                int length = value.size();
                String result;
                try {
                    result = kAnonymityUtil.lEntropyDiversity(baseName, dir, param, attribute, length);
                } catch (Exception e) {
                    log.error(e.getMessage());
                    return null;
                }
                return new DSObject(result);
            }

            /*
                Recursive-C- l-diversity
                功能：对 csv 文件进行 t-closeness 处理
                参数：k、t
                输入：数值或标识符型 csv 文件
                输出：csv文件
            */
            case 9: {
                if (params.length != 1) return null;
//                int[] l_diversity_param = new int[]{2, 4, 6};
                List<?> value = object.getList();
                String param = Integer.toString(params[0].intValue());
                String baseName = value.get(0).toString();
                String dir = value.get(1).toString();
                String attribute = value.get(2).toString();
                int length = value.size();
                String result;
                try {
                    result = kAnonymityUtil.lRecursiveCDiversity(baseName, dir, param, attribute, length);
                } catch (Exception e) {
                    return null;
                }
                return new DSObject(result);
            }

            /*
                t-closeness
                功能：对 csv 文件进行 t-closeness 处理
                参数：k、t
                输入：数值或标识符型 csv 文件
                输出：csv文件
            */
            case 10: {
                if (params.length != 1) return null;
                List<?> value = object.getList();
                String param = Integer.toString(params[0].intValue());
                String baseName = value.get(0).toString();
                String dir = value.get(1).toString();
                String attribute = value.get(2).toString();
                int length = value.size();
                String result;
                try {
                    result = kAnonymityUtil.tCloseness(baseName, dir, param, attribute, length);
                } catch (Exception e) {
                    return null;
                }
                return new DSObject(result);
            }

            /*
                基于Hilbert曲线的虚假位置生成算法 Hilbert
                功能：将用户的真实位置转换为 Hilbert 曲线上的值，然后找到 Hilbert 曲线上相邻的 K 个点，转换为真实坐标后输出。
                参数：匿名度、虚拟位置库经纬度
                输入：经度、纬度
                输出：经度数组、纬度数组
            */
            case 11: {
                if (params.length != 1) return null;
                String[] s = object.getStringVal().split(",");
//                String param = "8 " + s[0] + " " + s[1] + " " + params[0].toString();
//                String cmd = path + " " + param;
//                return new DSObject(CommandExecutor.openExe(cmd));


                double x = Double.parseDouble(s[0]);
                double y = Double.parseDouble(s[1]);
                int k = params[0].intValue();
                double[] retArrX = new double[k];
                double[] retArrY = new double[k];
                KAnonymityUtil.hilbertAlgorithm(x, y, k, retArrX, retArrY);

                LinkedList<String> retArrList = new LinkedList<>();
                for (int i = 0; i < k; i++) {
                    retArrList.add(retArrX[i] + "," + retArrY[i]);
                }
                return new DSObject(retArrList);

            }

            /*
                基于虚拟基准点的 PoI 查询隐私保护算法 SpaceTwist
                功能：
                用于返回距离用户最近的 k 个 POI，并且不暴露用户的真实位置。
                该算法可以概括为：首先生成一个虚拟位置 q_fake，将所有检索点按照距离 q_fake 从近到远排序；然后将排序后的检索点中的前 k 个放进结果集中，遍历后续所有点，
                若与真实位置 q 间的距离小于结果集中与 q 距离最大的点，则替换。
                结束条件为检索点集合遍历完毕或者当前点到真实位置 q 的距离大于 distance(q, q_fake) 与结果集中的点与 q 的最远距离之和。

                参数：匿名度、虚拟位置库经纬度
                输入：经度、纬度
                输出：经度数组、纬度数组
            */
//            case 12: {
//                if (params.length != 1) return null;
//                String[] s = object.getStringVal().split(",");
//                StringBuilder param = new StringBuilder("7 " + s[0] + " " + s[1] + " " + params[0].toString());
//                for (Object point : object.getList()) {
//                    String[] temp = point.toString().split(",");
//                    param.append(" ").append(temp[0]).append(" ").append(temp[1]);
//                }
//                String cmd = path + " " + param;
//                return new DSObject(CommandExecutor.openExe(cmd));
//            }
            case 12: {
                if (params.length != 1) return null;
                String[] s = object.getStringVal().split(",");
                double x = Double.parseDouble(s[0]);
                double y = Double.parseDouble(s[1]);

                int k = params[0].intValue();
                double[] retArrX = new double[k];
                double[] retArrY = new double[k];

                List<?> list = object.getList();
                List<Object> value = new ArrayList<>(list);

                // 拆分区域点集并构建点数组
                List<KAnonymityUtil.Point> points = new ArrayList<>();
                for (Object point : value) {
                    String[] temp = point.toString().split(",");
                    if (temp.length == 2) {
                        points.add(new KAnonymityUtil.Point(Double.parseDouble(temp[0]), Double.parseDouble(temp[1])));
                    }
                }

                LinkedList<String> retList = new LinkedList<>();
                if(KAnonymityUtil.spaceTwistWrapper(points, x, y, k, retArrX, retArrY) == -1){
                    retList.add("error");
                    return new DSObject(retList);
                }

                for (int i = 0; i < k; i++) {
                    retList.add(retArrX[i] + "," + retArrY[i]);
                }
                return new DSObject(retList);
            }

            default:
                return null;
        }
    }


}

package com.lu.gademo.utils.impl;

import com.lu.gademo.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Slf4j
@Component
public class GeneralizationImpl implements Generalization {
    private final DpUtil dpUtil;

    @Autowired
    public GeneralizationImpl(DpUtil dpUtil) {
        this.dpUtil = dpUtil;
    }

    public DSObject service(DSObject object, Integer alg, String... params) {
        log.info("调用数据泛化算法统一接口");
        if (object == null) return null;

        File directory = new File("");
        String currentPath = directory.getAbsolutePath();

        String path2 = Paths.get(currentPath, "image", "dealImage.py").toString();
        String path4 = Paths.get(currentPath, "video", "desenVideo.py").toString();
        String path6 = Paths.get(currentPath, "audio", "audio.py").toString();
        switch (alg) {
            /*
                Truncation
                尾部截断只保留前 3 位
                输入：字符串列表
                输出：字符串列表
            */
            case 1: {
                if (params.length != 1) return null;
                List<?> list = object.getList();
                List<Object> value = new ArrayList<>(list);
                return new DSObject(dpUtil.truncation(value, Integer.parseInt(params[0])));
            }

            /*
                Floor：取整
                泛化级别（泛化至十位）
                输入：数值列表
                输出：数值列表
            */
            case 2: {
                if (params.length != 1) return null;
                List<?> list = object.getList();
                List<Object> value = new ArrayList<>(list);
                if (Integer.parseInt(params[0]) == 0) {
                    return object;
                }
                return new DSObject(dpUtil.floor(value, params[0]));
            }

            /*
                FloorTime 时间取整 时间 12:30:45 -> 12:00:00
                泛化级别（固定为小时）
                输入：字符串列表
                输出：字符串列表
            */
            case 3: {
                if (params.length != 1) return null;
                List<?> list = object.getList();
                List<Object> value = new ArrayList<>(list);
                return new DSObject(dpUtil.floorTime(value, Integer.parseInt(params[0])));
            }

            /*
                AddressHide 隐藏具体地址信息  例：陕西省西安市长安区西安电子科技大学南校区 —> 陕西省西安市长安区
                泛化级别（固定为“区”）
                输入：字符串列表
                输出：字符串列表
            */
            case 4: {
                if (params.length != 1) return null;
                List<?> list = object.getList();
                List<Object> value = new ArrayList<>(list);
                return new DSObject(dpUtil.addressHide(value, Integer.parseInt(params[0])));
            }

            /*
                DateGroupPlace 将日期数据分组，将分组内日期替换为同一日期
                参数：分组大小
                输入：日期数组、分组大小 k = (1, 2, 3)
                输出：日期数组
            */
            case 5: {
                if (params.length != 1) return null;
                List<?> list = object.getList();
                List<Object> value = new ArrayList<>(list);
                try {
                    return new DSObject(dpUtil.dateGroupReplace(value, Integer.parseInt(params[0])));
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            }

            /*
                简化版 mixZone_1（已跑通）
                功能：根据用户不同的位置信息生成合适的假名位置信息。该算法根据用户位置判断其是否在虚拟圆的混合区域，
                若在混合区域，则服务器随机生成假名；若判断用户不在混合区，则直接返回用户真实位置信息。
                参数：混合区域的四个顶点的经纬度
                输入：经度、纬度、用户id、进入区域的时间、区域点集
                输出：字符串（用户假名id）
            */
//            case 6: {
//                if (params.length != 2) return null;
//
//                String position = object.getStringVal();
//                List<?> list = object.getList();
//                List<Object> value = new ArrayList<>(list);
//
//                int id = params[0].intValue();              // 参数 1 表示用户 ID
//                double time = params[1].doubleValue();      // 参数 2 表示进入时间
//
//                String[] s = position.split(",");
//                String param = "5 " + s[0] + " " + s[1] + " " + id + " " + time;
//                for (Object point : value) {
//                    String[] temp = point.toString().split(",");
//                    param += " " + temp[0] + " " + temp[1];
//                }
//                String cmd = path + " " + param;
//                return new DSObject(CommandExecutor.openExe(cmd));
//            }
            case 6: {
                if (params.length != 2) return null;

                String position = object.getStringVal();
                double x = Double.parseDouble(position.split(",")[0]);
                double y = Double.parseDouble(position.split(",")[1]);
                List<?> list = object.getList();
                List<Object> value = new ArrayList<>(list);

                int id = Integer.parseInt(params[0]);              // 用户 ID
                double time = Double.parseDouble(params[1]);      // 进入时间

                // 拆分区域点集并构建点数组
                List<Double> points = new ArrayList<>();
                for (Object point : value) {
                    String[] temp = point.toString().split(",");
                    if (temp.length == 2) {
                        points.add(Double.parseDouble(temp[0]));
                        points.add(Double.parseDouble(temp[1]));
                    }
                }

                // 将 List 转换为 double 数组
                double[] pointsArray = new double[points.size()];
                for (int i = 0; i < points.size(); i++) {
                    pointsArray[i] = points.get(i);
                }

                // 调用混合区域算法
                LinkedList<String> retFidName = LocationUtil.mixzone1(x, y, id, time, pointsArray[0], pointsArray[1], pointsArray[2], pointsArray[3], pointsArray[4], pointsArray[5], pointsArray[6], pointsArray[7]);

                // 返回结果
                return new DSObject(retFidName);
            }


            /*
                简化版 mixZone_3（已跑通）
                功能：首先根据用户位置判断其是否在虚拟圆的混合区域，若在混合区域，则客户端直接从假名数据库中得到随机假名；若判断用户不在混合区，则直接返回用户真实位置信息。
                参数：混合区域的四个顶点的经纬度
                输入：经度、纬度、用户id、进入区域的时间、区域点集
                输出：字符串（用户假名id）
            */
//            case 7: {
//                if (params.length != 2) return null;
//
//                String position = object.getStringVal();
//                List<?> list = object.getList();
//                List<Object> value = new ArrayList<>(list);
//
//                int id = params[0].intValue();              // 参数 1 表示用户 ID
//                double time = params[1].doubleValue();      // 参数 2 表示进入时间
//
//                String[] s = position.split(",");
//                String param = "6 " + s[0] + " " + s[1] + " " + id + " " + time;
//                for (Object point : value) {
//                    String[] temp = point.toString().split(",");
//                    param += " " + temp[0] + " " + temp[1];
//                }
//                String cmd = path + " " + param;
//                return new DSObject(CommandExecutor.openExe(cmd));
//            }
            case 7: {
                if (params.length != 2) return null;

                String position = object.getStringVal();
                double x = Double.parseDouble(position.split(",")[0]);
                double y = Double.parseDouble(position.split(",")[1]);
                List<?> list = object.getList();
                List<Object> value = new ArrayList<>(list);

                int id = Integer.parseInt(params[0]);              // 用户 ID
                double time = Double.parseDouble(params[1]);      // 进入时间

                // 拆分区域点集并构建点数组
                List<Double> points = new ArrayList<>();
                for (Object point : value) {
                    String[] temp = point.toString().split(",");
                    if (temp.length == 2) {
                        points.add(Double.parseDouble(temp[0]));
                        points.add(Double.parseDouble(temp[1]));
                    }
                }

                // 将 List 转换为 double 数组
                double[] pointsArray = new double[points.size()];
                for (int i = 0; i < points.size(); i++) {
                    pointsArray[i] = points.get(i);
                }

                // 调用混合区域算法
                LinkedList<String> retFidName = LocationUtil.mixzone3(x, y, id, time, pointsArray[0], pointsArray[1], pointsArray[2],
                        pointsArray[3], pointsArray[4], pointsArray[5], pointsArray[6], pointsArray[7]);
                // 返回结果
                return new DSObject(retFidName);
            }

            /*
                简化版 Accuracy Reduction（已跑通）
                功能：该算法根据特定的精度需求对用户发送过来的位置信息进行模糊处理。简单地说，就是对用户的真实位置信息进行“精度消失”处理。
                输入：用户真实位置
                输出：处理后的位置
            */
            case 8: {
//                String position = object.getStringVal();
//
//                String[] s = position.split(",");
//                String param = "10 " + s[0] + " " + s[1] + " 1000";
//                String cmd = path + " " + param;
//                return new DSObject(CommandExecutor.openExe(cmd));

//                if (params.length != 1) return null;
//                List<?> value = object.getList();\
                if (params.length != 1) return null;
                String position = object.getStringVal();
                String[] s = position.split(",");
                double x = Double.parseDouble(s[0]);
                double y = Double.parseDouble(s[1]);
                int accuracy = Integer.parseInt(params[params.length - 1]);
                List<String> result = new LinkedList<>();
                LocationUtil.Point point = LocationUtil.accuracyReduction(x, y, accuracy);
                result.add(point.getX() + "," + point.getY());
                return new DSObject(result);
            }

            /*
                基于像素化滤波器的图像像素替换方法
                功能：对图像打马赛克
                参数：像素块大小
                输入：原图片文件路径，脱敏图片存放路径，像素块大小：0 1 2
                输出：脱敏执行信息
            */
            case 9: {
                if (params.length != 1) return null;
                List<?> value = object.getList();
                List<String> results = CommandExecutor.executePython(value.get(0).toString() + " " + value.get(1).toString(), "pixelate", path2, params[0]);
                return new DSObject(results);
            }


            /*
                基于高斯滤波器的图像像素替换方法
                功能：对图像进行高斯模糊
                参数：模糊半径
                输入：原图片文件路径，脱敏图片存放路径，模糊半径：0 1 2
                输出：脱敏执行信息
            */
            case 10: {
                if (params.length != 1) return null;
                List<?> value = object.getList();
                List<String> results = CommandExecutor.executePython(value.get(0).toString() + " " +
                        value.get(1).toString(), "gaussian_blur", path2, params[0]);
                return new DSObject(results);
            }

            /*
                基于盒式滤波器的图像像素替换方法
                功能：对图像进行盒式模糊
                参数：模糊半径
                输入：原图片文件路径，脱敏图片存放路径，模糊半径：0 1 2
                输出：脱敏执行信息
            */
            case 11: {
                if (params.length != 1) return null;
                List<?> value = object.getList();
                List<String> results = CommandExecutor.executePython(value.get(0).toString() + " " +
                        value.get(1).toString(), "box_blur", path2, params[0]);
                return new DSObject(results);
            }

            /*
                基于均值滤波器的图像像素替换方法
                功能：对图像进行均值模糊
                参数：滤波核大小
                输入：原图片文件路径，脱敏图片存放路径，滤波核大小：0 1 2
                输出：脱敏执行信息
            */
            case 12: {
                if (params.length != 1) return null;
                List<?> value = object.getList();
                List<String> results = CommandExecutor.executePython(value.get(0).toString() + " " +
                        value.get(1).toString(), "meanValueImage", path2, params[0]);
                return new DSObject(results);
            }

            /*
                基于像素块的图像像素替换方法
                功能：将图像的一部分像素替换颜色
                参数：颜色
                输入：原图片文件路径，脱敏图片存放路径，级别 0 1 2
                输出：脱敏执行信息
            */
            case 13: {
                if (params.length != 1) return null;
                List<?> value = object.getList();
                List<String> results = CommandExecutor.executePython(value.get(0).toString() + " " +
                        value.get(1).toString(), "replace_region", path2, params[0]);
                return new DSObject(results);
            }

            /*
                基于像素化滤波器的视频帧像素替换方法
                功能：打马赛克
                参数：像素块大小
                输入：原视频文件路径，脱敏视频存放路径，像素块大小 0 1 2
                输出：脱敏执行信息
            */
            case 14: {
                if (params.length != 1) return null;
                List<?> value = object.getList();
                List<String> results = CommandExecutor.executePython(value.get(0).toString() + " " +
                        value.get(1).toString(), "pixelate_video", path4, params[0]);
                return new DSObject(results);
            }

            /*
                基于高斯滤波器的视频帧像素替换方法
                功能：高斯模糊
                参数：模糊半径
                输入：原视频文件路径，脱敏视频存放路径，模糊半径 0 1 2
                输出：脱敏执行信息
            */
            case 15: {
                if (params.length != 1) return null;
                List<?> value = object.getList();
                List<String> results = CommandExecutor.executePython(value.get(0).toString() + " " +
                        value.get(1).toString(), "gaussian_blur_video", path4, params[0]);
                return new DSObject(results);
            }

            /*
                基于盒式滤波器的视频帧像素替换方法
                功能：盒式模糊
                参数：模糊半径
                输入：原视频文件路径，脱敏视频存放路径，模糊半径 0 1 2
                输出：脱敏执行信息
            */
            case 16: {
                if (params.length != 1) return null;
                List<?> value = object.getList();
                List<String> results = CommandExecutor.executePython(value.get(0).toString() + " "
                        + value.get(1).toString(), "box_blur_video", path4, params[0]);
                return new DSObject(results);
            }

            /*
                基于均值滤波器的视频像素替换方法
                功能：对视频进行均值模糊
                参数：滤波核大小
                输入：原视频文件路径，脱敏视频存放路径，滤波核大小：0 1 2
                输出：脱敏执行信息
            */
            case 17: {
                if (params.length != 1) return null;
                List<?> value = object.getList();
                List<String> results = CommandExecutor.executePython(value.get(0).toString() + " " +
                        value.get(1).toString(), "meanValueVideo", path4, params[0]);
                return new DSObject(results);
            }

            /*
                基于像素块的视频像素替换方法
                功能：将视频的一部分像素替换颜色
                参数：颜色
                输入：原视频文件路径，脱敏视频存放路径，级别 0 1 2
                输出：脱敏执行信息
            */
            case 18: {
                if (params.length != 1) return null;
                List<?> value = object.getList();
                List<String> results = CommandExecutor.executePython(value.get(0).toString() + " " +
                        value.get(1).toString(), "replace_region_video", path4, params[0]);
                return new DSObject(results);
            }

            /*
                音频取整
                功能：对采样点数据进行取整操作
                参数：取整的精度
                输入：原音频文件路径，脱敏音频存放路径，级别 0 1 2 ...
                输出：脱敏执行信息
            */
            case 19: {
                if (params.length != 1) return null;
                List<?> value = object.getList();
                List<String> results = CommandExecutor.executePython(value.get(0).toString() + " " +
                        value.get(1).toString(), "floor", path6, params[0]);
                return new DSObject(results);
            }

            /*
                频域遮掩
                功能：删除特定频域段音频
                参数：遮掩频域区间
                输入：原音频文件路径，脱敏音频存放路径
                输出：脱敏执行信息
            */
            case 20: {
                List<?> value = object.getList();
                List<String> results = CommandExecutor.executePython(value.get(0).toString() + " " +
                        value.get(1).toString(), "spec", path6, params[0]);
                return new DSObject(results);
            }

            /*
                音频失真
                功能：使用滤波器等对音频进行失真处理，操作包括低通滤波器、高通滤波器、归一化、双曲正切失真等。
                参数：滤波器类型、频率、失真率
                输入：原音频文件路径，脱敏音频存放路径
                输出：脱敏执行信息
            */
            case 21: {
                List<?> value = object.getList();
                List<String> results = CommandExecutor.executePython(value.get(0).toString() + " " + value.get(1).toString(),
                        "augmentation", path6, params[0]);
                return new DSObject(results);
            }

            /*
                基于均值的采样点替换：
                功能：对采样点进行分块，块内采样点的均值作为新的采样点
                参数：分块长度
                输入：原音频文件路径，脱敏音频存放路径，分块长度 0 1 2 ...
                输出：脱敏执行信息
            */
            case 22: {
                if (params.length != 1) return null;
                List<?> value = object.getList();
                List<String> results = CommandExecutor.executePython(value.get(0).toString() + " " + value.get(1).toString(),
                        "median", path6, params[0]);
                return new DSObject(results);
            }

            default:
                return null;
        }
    }

}

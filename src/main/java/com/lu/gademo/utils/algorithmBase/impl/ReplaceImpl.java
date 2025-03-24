package com.lu.gademo.utils.algorithmBase.impl;

import com.lu.gademo.utils.CommandExecutor;
import com.lu.gademo.utils.DSObject;
import com.lu.gademo.utils.DpUtil;
import com.lu.gademo.utils.algorithmBase.Replace;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class ReplaceImpl implements Replace {
    private final DpUtil dpUtil;
    private final String currentPath;
    private final String dealImagePath;
    private final String path_audio;

    @Autowired
    public ReplaceImpl(DpUtil dpUtil) {
        this.dpUtil = dpUtil;
        this.currentPath = Paths.get("").normalize().toAbsolutePath().toString();
        this.dealImagePath = Paths.get(currentPath, "image", "dealImage.py").toString();
        this.path_audio = Paths.get(currentPath, "audio", "desenAudio.py").toString();
    }

    public DSObject service(DSObject object, Integer alg, String... params) {
        log.info("调用数据置换算法统一接口");
        if (object == null) return null;
        switch (alg) {
            /*
                Hiding
                将数据替换成一个常量，常用作不需要该敏感字段时
                替换字符（固定将数值替换为0） 仅限于数值字符 1 - 9
                输入：字符串列表
                输出：字符串列表
            */
            case 1: {
                if (params.length != 1) return null;
                List<?> list = object.getList();
                List<Object> value = new ArrayList<>(list);
                return new DSObject(dpUtil.valueHide(value, Integer.parseInt(params[0])));
            }

            /*
                Shift
                功能：给数值增加一个固定的偏移量
                参数：偏移量
                输入：Double 数值列表
                输出：Double 数值列表
            */
            case 2: {
                if (params.length != 1) return null;
                List<?> list = object.getList();
                List<Object> value = new ArrayList<>(list);
                return new DSObject(dpUtil.valueShift(value, Double.parseDouble(params[0])));
            }

            /*
                假名化-哈希函数
                功能：将数据映射为定长哈希值
                参数：哈希算法
                输入：字符串列表
                输出：字符串列表
            */
            case 3: {
                if (params.length != 1) return null;
                List<?> list = object.getList();
                List<Object> value = new ArrayList<>(list);
                return new DSObject(dpUtil.SHA512(value, Integer.parseInt(params[0])));
            }

            /*
                Enumeration
                功能：映射为新值同时保持数据顺序，如 40 -> 2000、50 -> 2500
                参数：映射倍数
                输入：Double 数值列表
                输出：Double 数值列表
            */
            case 4: {
                if (params.length != 1) return null;
                List<?> list = object.getList();
                List<Object> value = new ArrayList<>(list);
                return new DSObject(dpUtil.valueMapping(value, Double.parseDouble(params[0])));
            }

            /*
                基于随机字符的字符串混淆方法
                功能：使用随机数据替代原始数据，适合需要提供足够的保护且不需要保留可识别部分的场景。
                参数：隐私级别
                输入：字符串列表
                输出：字符串列表
            */
            case 5: {
                if (params.length != 1) return null;
                List<?> list = object.getList();
                List<Object> value = new ArrayList<>(list);
                return new DSObject(dpUtil.passReplace(value, Integer.parseInt(params[0])));
            }

            /*
                姓名遮掩
                功能：从第 2 个字符用 * 代替  例：张三 —> 张*
                参数：隐私级别
                输入：字符串列表
                输出：字符串列表
            */
            case 6: {
                if (params.length != 1) return null;
                List<?> list = object.getList();
                List<Object> value = new ArrayList<>(list);
                return new DSObject(dpUtil.nameHide(value, Integer.parseInt(params[0])));
            }

            /*
                数字遮掩
                功能：字符串中间的字符用 * 代替  例：241784257 —> 241***257
                参数：隐私级别
                输入：数字字符串列表
                输出：数字字符串列表
            */
            case 7: {
                if (params.length != 1) return null;
                List<?> list = object.getList();
                List<Object> value = new ArrayList<>(list);
                return new DSObject(dpUtil.numberHide(value, Integer.parseInt(params[0])));
            }

            /*
                邮箱地址隐藏
                功能：隐藏邮箱信息  例：afasf@qq.com —> ***@***
                输入：字符串列表
                输出：字符串列表
            */
            case 8: {
                if (params.length != 1) return null;
                List<?> list = object.getList();
                List<Object> value = new ArrayList<>(list);
                return new DSObject(dpUtil.suppressEmail(value, Integer.parseInt(params[0])));
            }

            /*
                IP 地址全部隐藏
                功能：将 IP 地址全部替换为 *  例：1.1.1.1—> *.*.*.*
                输入：字符串列表
                输出：字符串列表
            */
            case 9: {
                if (params.length != 1) return null;
                List<?> list = object.getList();
                List<Object> value = new ArrayList<>(list);
                return new DSObject(dpUtil.suppressAllIp(value, Integer.parseInt(params[0])));
            }

            /*
                IP 地址随机隐藏
                功能：将 IP 地址随机替换为 *  例：1.1.1.1 —> 1.1.1.* 或者1.*.1.1
                输入：字符串列表
                输出：字符串列表
            */
            case 10: {
                if (params.length != 1) return null;
                List<?> list = object.getList();
                List<Object> value = new ArrayList<>(list);
                return new DSObject(dpUtil.suppressIpRandomParts(value, Integer.parseInt(params[0])));
            }

            /*
                图像颜色随机替换方法
                功能：将图像的每个像素的RGB通道值随机打乱后作为当前像素新的RGB值
                输入：原图片文件路径，输出图片文件路径
                输出：图像文件（存放在指定路径）
            */
            case 11: {
                List<?> list = object.getList();
                List<Object> value = new ArrayList<>(list);
                String rawData = value.get(0).toString() + " " + value.get(1).toString();
                return new DSObject(CommandExecutor.executePython(rawData, "image_exchange_channel", dealImagePath));
            }

            /*
                图像颜色偏移
                功能：将图像的每个像素的RGB通道值加上一个偏移量后作为当前像素新的RGB值
                参数：偏移量
                输入：原图片文件路径，输出图片文件路径
                输出：图片文件（存放在指定路径）

            */
            case 12: {
                if (params.length != 1) return null;
                List<?> list = object.getList();
                List<Object> value = new ArrayList<>(list);
                String rawData = value.get(0).toString() + " " + value.get(1).toString();

                return new DSObject(CommandExecutor.executePython(rawData, "image_add_color_offset", dealImagePath, String.valueOf(params[0])));
            }

            /*
                图像人脸替换算法
                功能：将源图像人脸替换为目标图像人脸
                参数：属性维度（固定512）
                输入：原图片文件路径，输出图片文件路径
                输出：图像文件[存放在指定路径]
            */
            case 13: {
                List<?> list = object.getList();
                List<Object> value = new ArrayList<>(list);
                String rawData = value.get(0).toString() + " " + value.get(1).toString() + " " + value.get(2).toString();
                String path2 = Paths.get(currentPath, "image", "FaceReplace", "image_inference.py").toString();
                return new DSObject(CommandExecutor.executePython(rawData, "", path2));
            }

            /*
                基于像素块的视频帧像素颜色偏移方法
                功能：将每一帧的每个像素RGB通道值加上一个固定的偏移量后作为当前像素新的 RGB 值
                参数：偏移量
                输入：原视频文件路径，输出视频文件路径
                输出：视频文件[存放在指定路径]
            */
            case 14: {
                if (params.length != 1) return null;
                List<?> list = object.getList();
                List<Object> value = new ArrayList<>(list);
                String rawData = value.get(0).toString() + " " + value.get(1).toString();
                String path3 = Paths.get(currentPath, "video", "desenVideo.py").toString();
                return new DSObject(CommandExecutor.executePython(rawData, "video_add_color_offset",
                        path3, params[0]));
            }

            /*
                视频人脸替换算法
                功能：将原视频人脸替换为目标图像人脸
                参数：属性维度（固定512）
                输入：原视频文件路径，输出视频文件路径，目标人脸图像路径
                输出：视频文件[存放在指定路径]
            */
            case 15: {
                List<?> list = object.getList();
                List<Object> value = new ArrayList<>(list);
                String rawData = value.get(0).toString() + " " + value.get(1).toString() + " " + value.get(2).toString();
                String path4 = Paths.get(currentPath, "image", "FaceReplace", "video_inference.py").toString();
                return new DSObject(CommandExecutor.executePython(rawData, "", path4));
            }

            /*
                视频背景替换算法
                功能：将视频背景进行替换
                参数：人物轮廓阈值（0-1的浮点数）
                输入：视频文件路径，背景图像路径
                输出：视频文件[存放在指定路径]
            */
            case 16: {
                if (params.length != 1) return null;
                List<?> list = object.getList();
                List<Object> value = new ArrayList<>(list);
                String rawData = value.get(0).toString() + " " + value.get(1).toString() + " " + value.get(2).toString();
                String path5 = Paths.get(currentPath, "video", "substitude_background.py").toString();
                return new DSObject(CommandExecutor.executePython(rawData, "video_remove_bg", path5,
                        params[0]));
            }

            /*
                音频重排
                功能：对音频进行分块，随机重排所有分块后合并为一个音频
                参数：分块数量
                输入：原音频文件路径、输出音频文件路径，音频分块数量
                输出：音频文件[存放在指定路径]
            */
            case 17: {
                if (params.length != 1) return null;
                List<?> list = object.getList();
                List<Object> value = new ArrayList<>(list);
                String rawData = value.get(0).toString() + " " + value.get(1).toString();
                return new DSObject(CommandExecutor.executePython(rawData, "audio_reshuffle", path_audio,
                        "0", params[0]));
            }

            /*
                音频变形
                功能：对音频进行拉伸、移位和增益
                参数：拉伸率、移位步数、增益调整最小值与最大值
                输入：原音频文件路径、输出音频文件路径 参数 0 1 2
                输出：音频文件[存放在指定路径]
            */
            case 18: {
                if (params.length != 1) return null;
                List<?> list = object.getList();
                List<Object> value = new ArrayList<>(list);
                String rawData = value.get(0).toString() + " " + value.get(1).toString();
                return new DSObject(CommandExecutor.executePython(rawData, "apply_audio_effects",
                        path_audio, params[0]));
            }

            /*
                声纹替换方法
                功能：用固定声纹替换原始音频的声纹
                参数：声纹维度（固定512），特征维度（固定64）
                输入：原音频文件路径，输出音频文件路径
                输出：音频文件[存放在指定路径]
            */
            case 19: {
                List<?> list = object.getList();
                List<Object> value = new ArrayList<>(list);
                String rawData = value.get(0).toString() + " " + value.get(1).toString();
                return new DSObject(CommandExecutor.executePython(rawData, "voice_replace", path_audio, "0"));
            }

            default:
                return null;
        }
    }

}

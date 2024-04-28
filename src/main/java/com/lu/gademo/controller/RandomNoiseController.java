package com.lu.gademo.controller;

import com.lu.gademo.utils.DSObject;
import com.lu.gademo.utils.Dp;
import com.lu.gademo.utils.Util;
import com.lu.gademo.utils.impl.UtilImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/***
 * 随机加噪算法
 */
@Controller
@RequestMapping("/RandomNoise")
public class RandomNoiseController {
    // 工具类
//    Util util = new UtilImpl();
    @Autowired
    Dp dp;
    @ResponseBody
    @RequestMapping(value = "/desenValue", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String desenValue(@RequestParam String rawData,
                             @RequestParam String samples,
                             @RequestParam String algName) {
        String[] types = algName.split(",");
        algName = types[types.length - 1];
        System.out.println(algName);
        System.out.println(rawData);
//        // python命令
//        String python = util.isLinux() ? "python3" : "conda run -n torch_env python";
//        // 当前路径
//        File directory = new File("");
//        String currentPath = directory.getAbsolutePath();
//
//        try {
//            // 指定Python脚本路径
//            String desenApp = Paths.get(currentPath, "perturbation", "other", "randomNoise.py").toString();
//            System.out.println(desenApp);
//
//            // 创建参数列表
//            String[] command = {python, desenApp, algName, rawData, samples};
//            System.out.println(Arrays.toString(command));
//
//            // 创建ProcessBuilder对象
////            ProcessBuilder processBuilder = new ProcessBuilder(command);
//
//            // 启动进程
//            System.out.println(String.join(" ", command));
//            Process process = Runtime.getRuntime().exec(String.join(" ", command));
//
//            // 获取进程的输出流
//            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
//
//            // 存储结果
//            StringBuilder result = new StringBuilder();
//
//            // 读取输出
//            String line;
//            while ((line = reader.readLine()) != null) {
//                System.out.println("Python输出: " + line);
//                result.append(line);
//            }
//
//            // 等待进程执行结束
//            int exitCode = process.waitFor();
//            System.out.println("Python脚本执行完毕，退出码: " + exitCode);
//
//            System.out.println(result);
//            return result.toString();
//
//        } catch (IOException | InterruptedException e) {
//            e.printStackTrace();
//        }
//        return rawData;
        int algNum = 0;
        switch (algName) {
            //
            case "report_noisy_max4": {
                algNum = 10;
                break;
            }
            case "report_noisy_max3": {
                algNum = 3;
                break;
            }
            //
            case "noisy_hist2": {
                algNum = 25;
                break;
            }
            default:
                throw new RuntimeException("Unkown algorithm: " + algName);

        }
        List<Double> rawDataList = Arrays.stream(rawData.split(",")).filter(x -> !x.isEmpty()).map(Double::valueOf).collect(Collectors.toList());
        DSObject resultDS = null;
        DSObject rawObject = rawDataList.size() == 1 ? new DSObject(rawDataList.get(0)) : new DSObject(rawDataList);

        if (algName.equals("report_noisy_max3")) {
            resultDS = dp.service(rawObject, algNum, Integer.parseInt(samples), 1);
        } else {
            resultDS = dp.service(rawObject, algNum, Integer.parseInt(samples));
        }

        StringBuilder resultString = new StringBuilder();
        for (Object s : resultDS.getList()) {
            resultString.append(s).append("\n");
        }
        return resultString.toString();
    }
    @ResponseBody
    @RequestMapping(value = "/desenValue2", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
    public String desenValue2(@RequestParam String rawData,
                              @RequestParam String samples,
                              @RequestParam String algName,
                              @RequestParam String c,
                              @RequestParam String t){

        String[] types = algName.split(",");
        algName = types[types.length - 1];
        System.out.println(algName);
        System.out.println(rawData);
//        // python命令
//        String python = util.isLinux() ? "python3" : "conda run -n torch_env python";
//        // 当前路径
//        File directory = new File("");
//        String currentPath = directory.getAbsolutePath();
//
//        try {
//            // 指定Python脚本路径
//            //String pythonScriptPath = "path/to/your/python/script.py";
//            String desenApp = Paths.get(currentPath, "perturbation", "other", "randomNoise.py").toString();
//            System.out.println(desenApp);
//
//            // 创建参数列表
//            String[] command = {python, desenApp, algName, rawData, samples, c, t};
//            System.out.println(Arrays.toString(command));
//
//            // 创建ProcessBuilder对象
////            ProcessBuilder processBuilder = new ProcessBuilder(command);
//
//            // 启动进程
//            System.out.println(String.join(" ", command));
//            Process process = Runtime.getRuntime().exec(String.join(" ", command));
//
//            // 获取进程的输出流
//            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
//
//            // 存储结果
//            StringBuilder result = new StringBuilder();
//
//            // 读取输出
//            String line;
//            while ((line = reader.readLine()) != null) {
//                System.out.println("Python输出: " + line);
//                result.append(line);
//            }
//
//            // 等待进程执行结束
//            int exitCode = process.waitFor();
//            System.out.println("Python脚本执行完毕，退出码: " + exitCode);
//
//            System.out.println(result);
//            return result.toString();
//
//        } catch (IOException | InterruptedException e) {
//            e.printStackTrace();
//        }
//        return rawData;
        int algNum;
        switch (algName) {
            //
            case "sparse_vector_technique3": {
                algNum = 13;
                break;
            }
            case "sparse_vector_technique4": {
                algNum = 14;
                break;
            }
            //
            case "sparse_vector_technique5": {
                algNum = 15;
                break;
            }
            //
            case "sparse_vector_technique6": {
                algNum = 16;
                break;
            }
            default:
                throw new RuntimeException("Unkown algorithm: " + algName);

        }

        List<Double> rawDataList = Arrays.stream(rawData.split(",")).filter(x -> !x.isEmpty()).map(Double::valueOf).collect(Collectors.toList());

        DSObject rawObject = rawDataList.size() == 1 ? new DSObject(rawDataList.get(0)) : new DSObject(rawDataList);
        DSObject resultDS = dp.service(rawObject, algNum, Integer.parseInt(c), Integer.parseInt(t));
        StringBuilder resultString = new StringBuilder();
        for (Object s : resultDS.getList()) {
            resultString.append(s).append("\n");
        }
        return resultString.toString();
    }
}



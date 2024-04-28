package com.lu.gademo.controller;

import com.lu.gademo.utils.DSObject;
import com.lu.gademo.utils.Dp;
import com.lu.gademo.utils.Util;
import com.lu.gademo.utils.impl.UtilImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/***
 * 差分隐私算法
 */
@Controller
@RequestMapping("/DP")
public class DPController {
    @Autowired
    Dp dp;
    // 工具类
    Util util = new UtilImpl();

    @ResponseBody
    @RequestMapping(value = "/desenValue", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
    public String desenValue(@RequestParam String rawData,
                             @RequestParam String samples,
                             @RequestParam String algName) {
        String[] types = algName.split(",");
        algName = types[types.length - 1];
        System.out.println(algName);
//        // python命令
//        String python = util.isLinux() ? "python3" : "conda run -n torch_env python";
//        // 当前路径
//        File directory = new File("");
//        String currentPath = directory.getAbsolutePath();

//        try {
//            // 指定Python脚本路径
//            //String pythonScriptPath = "path/to/your/python/script.py";
//            String desenApp = Paths.get(currentPath, "perturbation", "differential_privacy", "dp.py").toString();
//
//            // 创建参数列表
//            String[] command = {python, desenApp, algName, rawData, samples};
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

        int algNum = 0;
        switch (algName) {
            //
            case "snapping": {
                algNum = 4;
                break;
            }
            case "exponential": {
                algNum = 8;
                break;
            }
            //
            case "report_noisy_max1": {
                algNum = 2;
                break;
            }
            //
            case "report_noisy_max2": {
                algNum = 9;
                break;
            }
            //
            case "noisy_hist1": {
                algNum = 24;
                break;
            }

            //
            case "rappor": {
                algNum = 18;
                break;
            }
            //
            case "onetimerappor": {
                algNum = 19;
                break;
            }
            default:
                throw new RuntimeException("Unkown algorithm: " + algName);

        }
        List<Double> rawDataList = Arrays.stream(rawData.split(",")).filter(x -> !x.isEmpty()).map(Double::valueOf).collect(Collectors.toList());
        DSObject resultDS = null;
        DSObject rawObject = rawDataList.size() == 1 ? new DSObject(rawDataList.get(0)) : new DSObject(rawDataList);
        if (algName.equals("report_noisy_max1")) {
            resultDS = dp.service(rawObject, algNum,  1, Integer.parseInt(samples));
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
                              @RequestParam String t) {

        String[] types = algName.split(",");
        algName = types[types.length - 1];
        System.out.println(algName);
        // python命令
        String python = util.isLinux() ? "python3" : "conda run -n torch_env python";
        // 当前路径
        File directory = new File("");
        String currentPath = directory.getAbsolutePath();

//        try {
//            // 指定Python脚本路径
//            //String pythonScriptPath = "path/to/your/python/script.py";
//            String scriptPath = Paths.get(currentPath, "perturbation", "differential_privacy").toString();
//            String desenApp = Paths.get(currentPath, "perturbation", "differential_privacy", "dp.py").toString();
//
//            // 创建参数列表
//            String[] command = {python, desenApp, algName, rawData, samples, c, t};
//
//            // 创建ProcessBuilder对象
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
            case "sparse_vector_technique1": {
                algNum = 11;
                break;
            }
            case "sparse_vector_technique2": {
                algNum = 12;
                break;
            }
            case "sparse_vector_technique_numerical": {
                algNum = 17;
                break;
            }
        }
        List<Double> rawDataList = Arrays.stream(rawData.split(",")).filter(x -> !x.isEmpty()).map(Double::valueOf).collect(Collectors.toList());
        DSObject rawObject = rawDataList.size() == 1 ? new DSObject(rawDataList.get(0)) : new DSObject(rawDataList);
        DSObject result = dp.service(rawObject, algNum, Integer.parseInt(c), Integer.parseInt(t));
        StringBuilder resultString = new StringBuilder();
        for (Object s : result.getList()) {
            resultString.append(s).append("\n");
        }
        return resultString.toString();

    }


}

package com.lu.gademo.controller;

import com.lu.gademo.utils.Util;
import com.lu.gademo.utils.impl.UtilImpl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.Arrays;

/***
 * 随机加噪算法
 */
@Controller
@RequestMapping("/RandomNoise")
public class RandomNoiseController {
    // 工具类
    Util util = new UtilImpl();

    @ResponseBody
    @RequestMapping(value = "/desenValue", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String desenValue(@RequestParam String rawData,
                             @RequestParam String samples,
                             @RequestParam String algName) {
        String[] types = algName.split(",");
        algName = types[types.length - 1];
        System.out.println(algName);
        System.out.println(rawData);
        // python命令
        String python = util.isLinux() ? "python3" : "python";
        // 当前路径
        File directory = new File("");
        String currentPath = directory.getAbsolutePath();

        try {
            // 指定Python脚本路径
            String desenApp = Paths.get(currentPath, "perturbation", "other", "randomNoise.py").toString();
            System.out.println(desenApp);

            // 创建参数列表
            String[] command = {python, desenApp, algName, rawData, samples};
            System.out.println(Arrays.toString(command));

            // 创建ProcessBuilder对象
            ProcessBuilder processBuilder = new ProcessBuilder(command);

            // 启动进程
            Process process = processBuilder.start();

            // 获取进程的输出流
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            // 存储结果
            StringBuilder result = new StringBuilder();

            // 读取输出
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println("Python输出: " + line);
                result.append(line);
            }

            // 等待进程执行结束
            int exitCode = process.waitFor();
            System.out.println("Python脚本执行完毕，退出码: " + exitCode);

            System.out.println(result);
            return result.toString();

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return rawData;
    }
    @ResponseBody
    @RequestMapping(value = "/desenValue2", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String desenValue2(@RequestParam String rawData,
                              @RequestParam String samples,
                              @RequestParam String algName,
                              @RequestParam String c,
                              @RequestParam String t){

        String[] types = algName.split(",");
        algName = types[types.length - 1];
        System.out.println(algName);
        System.out.println(rawData);
        // python命令
        String python = util.isLinux() ? "python3" : "python";
        // 当前路径
        File directory = new File("");
        String currentPath = directory.getAbsolutePath();

        try {
            // 指定Python脚本路径
            //String pythonScriptPath = "path/to/your/python/script.py";
            String desenApp = Paths.get(currentPath, "perturbation", "other", "randomNoise.py").toString();
            System.out.println(desenApp);

            // 创建参数列表
            String[] command = {python, desenApp, algName, rawData, samples, c, t};
            System.out.println(Arrays.toString(command));

            // 创建ProcessBuilder对象
            ProcessBuilder processBuilder = new ProcessBuilder(command);

            // 启动进程
            Process process = processBuilder.start();

            // 获取进程的输出流
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            // 存储结果
            StringBuilder result = new StringBuilder();

            // 读取输出
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println("Python输出: " + line);
                result.append(line);
            }

            // 等待进程执行结束
            int exitCode = process.waitFor();
            System.out.println("Python脚本执行完毕，退出码: " + exitCode);

            System.out.println(result);
            return result.toString();

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return rawData;
    }
}



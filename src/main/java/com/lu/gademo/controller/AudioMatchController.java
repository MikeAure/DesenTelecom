package com.lu.gademo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lu.gademo.utils.Util;
import com.lu.gademo.utils.impl.UtilImpl;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

@RestController
@RequestMapping("/audioMatch")
public class AudioMatchController {
    Util util = new UtilImpl();
    String python = util.isLinux() ? "python3" : "python";
    // 当前路径
    File directory = new File("");
    String currentPath = directory.getAbsolutePath();
    String appPath = currentPath + File.separator + "audio_match";
    // 脱敏程序路径
    String desenApp = appPath + File.separator + "sck.py";

    String command = python + " " + desenApp;

    private String saveFile(MultipartFile file) throws IOException {
        File directory = new File("");
        String currentPath = directory.getAbsolutePath();
        // 时间
        String time = String.valueOf(System.currentTimeMillis());
        // 源文件保存目录
        File rawDirectory = new File("raw_files");
        if (!rawDirectory.exists()) {
            rawDirectory.mkdir();
        }
        // 文件名
        String rawFileName = time + file.getOriginalFilename();

        String rawFilePath = currentPath + File.separator + "raw_files" + File.separator + rawFileName;

        file.transferTo(new File(rawFilePath));
        return rawFilePath;
    }

    @PostMapping(value = "signUp", produces = "application/json;charset=UTF-8")
    public ServerResponse signUp(@RequestPart MultipartFile file, @RequestParam String name) {
        String rawFilePath;
        // Save the file sent by frontend
        try {
            rawFilePath = saveFile(file);
            System.out.println(rawFilePath);
        } catch (IOException e) {
            e.printStackTrace();
            return new ServerResponse("error", "Save file failed");
        }
        // Invoke Python script to verify if the user has signed up
        try {
            String username = name + "&&" + "0" + "@@";
            String parameter = "\"" + username + "\"" + " " + rawFilePath;

            // 根据当前操作系统类型决定是否使用conda环境
            if(!util.isLinux()){
                String conda = "conda run -n torch_env ";
                command = conda + command;
            }
            Process signUpProcess = Runtime.getRuntime().exec(command + " " + parameter);
            System.out.println(command + " " + parameter);
            BufferedReader reader = new BufferedReader(new InputStreamReader(signUpProcess.getInputStream()));
            // 获得python脚本进程的输出
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println("Python Output: " + line);
                if (line.contains("status")) {
                    break;
                }
            }
            // 等待进程结束并获取退出码
            int exitCode = signUpProcess.waitFor();
            System.out.println("Exited with code : " + exitCode);
            final ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(line, ServerResponse.class);
        } catch (Exception e) {
            e.printStackTrace();
            return new ServerResponse("error", "Server returns message error");
        }
    }

    @PostMapping(value = "signIn", produces = "application/json;charset=UTF-8")
    public ServerResponse signIn(@RequestPart MultipartFile file, @RequestParam String name) {
        String rawFilePath;
        // Save the file sent by frontend
        try {
            rawFilePath = saveFile(file);
            System.out.println(rawFilePath);
        } catch (IOException e) {
            e.printStackTrace();
            return new ServerResponse("error", "Save file failed");
        }
        // Invoke Python script to verify if the user has signed up
        try {
            String username = name + "&&" + "1" + "@@";
            String parameter = "\"" + username + "\"" + " " + rawFilePath;

            // 根据当前操作系统类型决定是否使用conda环境
            if(!util.isLinux()){
                String conda = "conda run -n torch_env ";
                command = conda + command;
            }
            Process signUpProcess = Runtime.getRuntime().exec(command + " " + parameter);
            BufferedReader reader = new BufferedReader(new InputStreamReader(signUpProcess.getInputStream()));
            // 获得python脚本进程的输出
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println("Python Output: " + line);
                if (line.contains("status")) {
                    break;
                }
            }

            // 等待进程结束并获取退出码
            int exitCode = signUpProcess.waitFor();
            System.out.println("Exited with code : " + exitCode);
            final ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(line, ServerResponse.class);
        } catch (Exception e) {
            e.printStackTrace();
            return new ServerResponse("error", "Server returns message error");
        }

    }

}

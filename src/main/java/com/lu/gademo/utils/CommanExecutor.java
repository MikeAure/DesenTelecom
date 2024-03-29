package com.lu.gademo.utils;

import com.lu.gademo.utils.impl.UtilImpl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

public class CommanExecutor {
    public static List<String> openExe(String cmd) {
        BufferedReader bufferReader = null;
        BufferedReader bufferReaderError;
        List<String> result = new LinkedList<>();
        try {
            Process p = Runtime.getRuntime().exec(cmd);
            String line;
            bufferReader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            bufferReaderError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
            while ((line = bufferReader.readLine()) != null  || (line = bufferReaderError.readLine()) != null) {
                result.add(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bufferReader != null) {
                try {
                    bufferReader.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    public static List<String> executePython(String rawData, String algName, String path, String...params) {

        Util util = new UtilImpl();
        String python = util.isLinux() ? "python3" : "python";

        try {
            // 指定Python脚本路径
            System.out.println(path);

            // 创建参数列表
            StringBuilder command = new StringBuilder(python + " " + path + " " + algName + " " + rawData);
            for (String param : params) {
                command.append(" ").append(param);
            }
            System.out.println(command);

            return CommanExecutor.openExe(command.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

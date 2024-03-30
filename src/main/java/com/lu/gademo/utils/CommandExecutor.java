package com.lu.gademo.utils;

import com.lu.gademo.utils.impl.UtilImpl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

public class CommandExecutor {

    public static List<String> openExe(String cmd) {
        return openExe(cmd, "");
    }
    public static List<String> openExe(String cmd, String context) {
        BufferedReader bufferReader = null;
        BufferedReader bufferReaderError;
        List<String> result = new LinkedList<>();
        Process p;
        try {
            if (context.isEmpty()) {
                p = Runtime.getRuntime().exec(cmd);
            }
            else {
                p = Runtime.getRuntime().exec(cmd, null, new java.io.File(context));
            }
            String line;
            bufferReader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            bufferReaderError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
            while ((line = bufferReader.readLine()) != null  || (line = bufferReaderError.readLine()) != null) {
                result.add(line);
                if (line.contains("Error") || line.contains("Traceback")) {
                    return null;
                }
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

    /**
     * Execute a Python script with specified parameters.
     *
     * @param  rawData    the input and output file path
     * @param  algName    the name of the algorithm to be executed
     * @param  path       the path to the Python script
     * @param  params     additional parameters to be passed to the Python script
     * @return            the list of strings returned by the Python script execution
     */

    public static List<String> executePython(String rawData, String algName, String path, String...params) {

        Util util = new UtilImpl();
//        String python = util.isLinux() ? "python3" : "python";
        String python = util.isLinux() ? "python3" : "conda run -n torch_env python";

        try {
            // 指定Python脚本路径
            System.out.println(path);

            // 创建参数列表
            StringBuilder command = new StringBuilder(python + " " + path + " " + algName + " " + rawData);
            for (String param : params) {
                command.append(" ").append(param);
            }
            System.out.println(command);

            Path fileParent = Paths.get(path).getParent();
            System.out.println(fileParent.toString());
            return CommandExecutor.openExe(command.toString(), fileParent.normalize().toFile().getAbsolutePath());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

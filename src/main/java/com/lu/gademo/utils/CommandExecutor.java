package com.lu.gademo.utils;

import com.lu.gademo.utils.impl.UtilImpl;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

@Slf4j
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
            } else {
                p = Runtime.getRuntime().exec(cmd, null, new java.io.File(context));
            }
//            String line;
//            bufferReader = new BufferedReader(new InputStreamReader(p.getInputStream(), StandardCharsets.UTF_8));
//            bufferReaderError = new BufferedReader(new InputStreamReader(p.getErrorStream(), StandardCharsets.UTF_8));
//            while ((line = bufferReader.readLine()) != null || (line = bufferReaderError.readLine()) != null) {
//                log.info("Process output: " + line);
//                result.add(line);
//                if (line.contains("Error") || line.contains("Traceback")) {
//                    return null;
//                }
//            }

            StreamGobbler outputGobbler = new StreamGobbler(p.getInputStream(), result);
            StreamGobbler errorGobbler = new StreamGobbler(p.getErrorStream(), result);

            outputGobbler.start();
            errorGobbler.start();

            // 等待脚本执行完毕，阻塞
            int exitCode = p.waitFor();
            outputGobbler.join();
            errorGobbler.join();

            if (exitCode != 0) {
                log.error("Process exited with code: " + exitCode);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            if (bufferReader != null) {
                try {
                    bufferReader.close();
                } catch (Exception e) {
                    log.error(e.getMessage());
                }
            }
        }
        return result;
    }



    /**
     * Execute a Python script with specified parameters.
     *
     * @param rawData the input and output file path
     * @param algName the name of the algorithm to be executed
     * @param path    the path to the Python script
     * @param params  additional parameters to be passed to the Python script
     * @return the list of strings returned by the Python script execution
     */

    public static List<String> executePython(String rawData, String algName, String path, String... params) {


//        String python = util.isLinux() ? "python3" : "python";
        String python;
        python = getPythonCommand();
        try {
            // 指定Python脚本路径
            log.info("Python execution path: " + path);

            // 创建参数列表
            StringBuilder command = new StringBuilder(python + " " + path + " " + algName + " " + rawData);
            for (String param : params) {
                command.append(" ").append(param);
            }
            log.info("Python command info: " + command);

            Path fileParent = Paths.get(path).getParent();
            // System.out.println(fileParent.toString());
            // 设置Python working directory
//            System.out.println(fileParent.normalize().toFile().getAbsolutePath());
            return CommandExecutor.openExe(command.toString(), fileParent.toFile().getAbsolutePath());

        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

    public static String getPythonCommand() {
        String python;
        Util util = new UtilImpl();
        if (util.isCondaInstalled(util.isLinux())) {
            python = "conda run -n torch_env python";
        } else if (util.isLinux()) {
            python = "python3";
        } else {
            python = "python";
        }
        return python;
    }

    private static class StreamGobbler extends Thread {
        private BufferedReader reader;
        private List<String> outputList;

        public StreamGobbler(InputStream stream, List<String> outputList) {
            this.reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8));
            this.outputList = outputList;
        }

        @Override
        public void run() {
            try {
                String line;
                while ((line = reader.readLine()) != null) {
                    log.info("Process output: " + line);
                    synchronized (outputList) {
                        outputList.add(line);
                    }
                    if (line.contains("Error") || line.contains("Traceback")) {
                        // You can set a flag or handle error output here if needed
                    }
                }
            } catch (Exception e) {
                log.error(e.getMessage());
            } finally {
                try {
                    reader.close();
                } catch (Exception e) {
                    log.error(e.getMessage());
                }
            }
        }
    }
}

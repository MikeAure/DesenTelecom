package com.lu.gademo.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

public class ExecutePython {
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
}

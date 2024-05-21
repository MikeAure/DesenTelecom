package com.lu.gademo.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;

public class TimeUtils {

    //获取时间戳
    public static String getCurrentTime() {
        //创建 SimpleDateFormat 对象以定义所需的日期时间格式
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // 获取当前时间
        Date currentDate = new Date();
        // 格式化日期时间为字符串
        String formattedDate = dateFormat.format(currentDate);
        return formattedDate;
    }

    //计算时间差值，以double形式输出，精确到小数秒
    public static double calculateTime(String startTimeStr, String endTimeStr) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        double timeDifferenceInSeconds;
        try {
            Date startTime = dateFormat.parse(startTimeStr);
            Date endTime = dateFormat.parse(endTimeStr);

            // 计算时间差，得到毫秒数
            long timeDifferenceInMilliseconds = (endTime.getTime() - startTime.getTime());
            // 转换为 double 形式，精确到小数秒
            timeDifferenceInSeconds = (double) timeDifferenceInMilliseconds / 1000.0;

            System.out.println("时间差为 " + timeDifferenceInSeconds + " 秒");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return timeDifferenceInSeconds;
    }

    // 将图片文件转换为Base64编码字符串
    public static String encodeImageToBase64String(Path imagePath) throws IOException {
        byte[] imageBytes = Files.readAllBytes(imagePath);
        return Base64.getEncoder().encodeToString(imageBytes);
    }
}

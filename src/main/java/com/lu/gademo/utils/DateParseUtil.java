package com.lu.gademo.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Component
public class DateParseUtil {
    //    public static final List<SimpleDateFormat> dataFormats = Arrays.asList(
//            new SimpleDateFormat("dd/MM/yyyy HH:mm:ss"),
//            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"),
//            new SimpleDateFormat("yyyyMMddHHmmss"),
//            new SimpleDateFormat("yyyy.MM.dd HH:mm:ss"),
//            new SimpleDateFormat("yyyy-MM-dd"),
//            new SimpleDateFormat("yyyyMMdd"),
//            new SimpleDateFormat("MM/dd/yyyy"),
//            new SimpleDateFormat("dd-MM-yyyy"),
//            new SimpleDateFormat("dd/MM/yyyy"),
//            new SimpleDateFormat("yyyy/MM/dd")
//    );
//
//    //日期处理
//    public static java.util.Date parseDate(Object data) {
//        for (SimpleDateFormat format : dataFormats) {
//            try {
//                return format.parse(data.toString());
//            } catch (ParseException e) {
//                e.getStackTrace();
//            }
//        }
//        return null;
//    }
    public static final List<DateTimeFormatter> dateFormatters = Arrays.asList(
            DateTimeFormatter.ofPattern("dd/MM/yyyy H:mm:ss"),
            DateTimeFormatter.ofPattern("yyyy-MM-dd H:mm:ss"),
            DateTimeFormatter.ofPattern("yyyyMMddHHmmss"),
            DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss"),
            DateTimeFormatter.ofPattern("yyyy-MM-dd"),
            DateTimeFormatter.ofPattern("yyyyMMdd"),
            DateTimeFormatter.ofPattern("MM/dd/yyyy"),
            DateTimeFormatter.ofPattern("dd-MM-yyyy"),
            DateTimeFormatter.ofPattern("dd/MM/yyyy"),
            DateTimeFormatter.ofPattern("yyyy/MM/dd")
    );

    // 日期处理
    public java.util.Date parseDate(Object data) {
        String dateString = data.toString();

        // 修正无效日期，将 "00" 替换为 "01"
        dateString = fixInvalidDay(dateString);

        // 修正时间部分，标准化为 HH:mm:ss
        dateString = fixTime(dateString);

        // 遍历格式化器进行解析
        for (DateTimeFormatter formatter : dateFormatters) {
            try {
                LocalDateTime dateTime = LocalDateTime.parse(dateString, formatter);
                return java.util.Date.from(dateTime.atZone(java.time.ZoneId.systemDefault()).toInstant());
            } catch (DateTimeParseException e) {
                // 继续尝试下一个格式
            }

            try {
                LocalDate date = LocalDate.parse(dateString, formatter);
                return java.util.Date.from(date.atStartOfDay(java.time.ZoneId.systemDefault()).toInstant());
            } catch (DateTimeParseException e) {
                // 继续尝试下一个格式
            }
        }
        return null; // 如果没有匹配的格式，返回null
    }

    // 修正无效的日期，将 "00" 替换为 "01"
    private String fixInvalidDay(String dateString) {
        // 匹配 yyyy-MM-dd 或 dd/MM/yyyy 形式的无效日 "00"
        Pattern pattern = Pattern.compile("(\\d{4}[-/.]\\d{2}|\\d{2}[-/.]\\d{2}[-/.]\\d{4})-00");
        Matcher matcher = pattern.matcher(dateString);

        if (matcher.find()) {
            // 将无效日 "00" 替换为 "01"
            dateString = matcher.replaceFirst(matcher.group(1) + "-01");
        }

        // 处理 yyyyMMdd 形式的日期，检查是否为无效日 "00"
        if (dateString.matches("\\d{8}")) {  // 匹配 yyyyMMdd 格式
            String year = dateString.substring(0, 4);
            String month = dateString.substring(4, 6);
            String day = dateString.substring(6, 8);

            // 如果日为 "00"，则将其替换为 "01"
            if (day.equals("00")) {
                day = "01";
                dateString = year + month + day;
            }
        }

        return dateString;
    }

    // 修正时间部分，标准化为 HH:mm:ss 格式
    private String fixTime(String dateString) {
        // 正则表达式匹配时间部分
        Pattern timePattern = Pattern.compile("(\\d{1,2}):(\\d{1,2}):(\\d{1,2})");
        Matcher timeMatcher = timePattern.matcher(dateString);

        if (timeMatcher.find()) {
            // 获取时间部分，并标准化为 HH:mm:ss 格式
            String hours = String.format("%02d", Integer.parseInt(timeMatcher.group(1)));
            String minutes = String.format("%02d", Integer.parseInt(timeMatcher.group(2)));
            String seconds = String.format("%02d", Integer.parseInt(timeMatcher.group(3)));

            // 替换原始时间部分为标准化的时间
            dateString = dateString.replace(timeMatcher.group(0), hours + ":" + minutes + ":" + seconds);
        }

        return dateString;
    }

}

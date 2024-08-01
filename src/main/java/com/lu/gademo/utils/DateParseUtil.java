package com.lu.gademo.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

public class DateParseUtil {
    public static final List<SimpleDateFormat> dataFormats = Arrays.asList(
            new SimpleDateFormat("dd/MM/yyyy HH:mm:ss"),
            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"),
            new SimpleDateFormat("yyyyMMddHHmmss"),
            new SimpleDateFormat("yyyy.MM.dd HH:mm:ss"),
            new SimpleDateFormat("yyyy-MM-dd"),
            new SimpleDateFormat("yyyyMMdd"),
            new SimpleDateFormat("MM/dd/yyyy"),
            new SimpleDateFormat("dd-MM-yyyy"),
            new SimpleDateFormat("dd/MM/yyyy"),
            new SimpleDateFormat("yyyy/MM/dd")
    );

    //日期处理
    public static java.util.Date parseDate(Object data) {
        for (SimpleDateFormat format : dataFormats) {
            try {
                return format.parse(data.toString());
            } catch (ParseException e) {
                e.getStackTrace();
            }
        }
        return null;
    }

}

package com.lu.gademo.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.write.style.*;
import com.alibaba.excel.enums.poi.FillPatternTypeEnum;
import com.lu.gademo.utils.CustomDateConverter;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.apache.poi.ss.usermodel.IndexedColors;

import java.util.Date;

@Getter
@Setter
@EqualsAndHashCode
@ContentRowHeight(14)
@HeadRowHeight(14)
@ColumnWidth(9)
// 头背景设置成红色 IndexedColors.RED.getIndex()
@HeadStyle(fillPatternType = FillPatternTypeEnum.SOLID_FOREGROUND, fillForegroundColor = 9)
// 头字体设置成20
@HeadFontStyle(fontHeightInPoints = 11)
public class Meeting {
    @ExcelProperty(value = "用户名", index = 0)
    private String yhm;

    @ExcelProperty(value = "用户ID", index = 1)
    private String yhid;

    @ExcelProperty(value = "密码", index = 2)
    private String mm;

    @ExcelProperty(value = "姓名", index = 3)
    private String xm;

    @ExcelProperty(value = "手机号码", index = 4)
    private String sjhm;

    @ExcelProperty(value = "邮箱", index = 5)
    private String yx;

    @ExcelProperty(value = "微信昵称", index = 6)
    private String hyzcr;

    @ExcelProperty(value = "设备标识符", index = 7)
    private String sbbsf;

    @ExcelProperty(value = "会议主题", index = 8)
    private String hyzt;

    @ExcelProperty(value = "会议号", index = 9)
    private String hyh;

//    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    @ExcelProperty(value = "会议时间", index = 10, converter = CustomDateConverter.class)
    private Date hysj;

    @ExcelProperty(value = "参会人昵称", index = 11)
    private String chrzc;

    @ExcelProperty(value = "短信验证码", index = 12)
    private String verCode;

    @ExcelProperty(value = "微信号", index = 13)
    private String wechatNumber;

    @ExcelProperty(value = "IP地址", index = 14)
    private String ip;
}

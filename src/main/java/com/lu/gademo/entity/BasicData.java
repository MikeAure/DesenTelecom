package com.lu.gademo.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.*;
import com.alibaba.excel.enums.poi.FillPatternTypeEnum;
import com.lu.gademo.utils.BirthdayDateConverter;
import com.lu.gademo.utils.CustomDateConverter;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

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
public class BasicData {
    @ExcelProperty(value = "身份证号", index = 0)
    private String idcardNum;
    @ExcelProperty(value = "姓名", index = 1)
    private String name;
    @ExcelProperty(value = "性别", index = 2)
    private String gender;
    @ExcelProperty(value = "年龄", index = 3)
    private double age;
    @ExcelProperty(value = "出生日期", index = 4, converter = BirthdayDateConverter.class)
    private Date birthDate;
    @ExcelProperty(value = "身高（cm）", index = 5)
    private double height;
    @ExcelProperty(value = "体重（kg）", index = 6)
    private double weight;
    @ExcelProperty(value = "职业", index = 7)
    private String occupation;
    @ExcelProperty(value = "手机号码", index = 8)
    private String phoneNum;
    @ExcelProperty(value = "MAC地址", index = 9)
    private String macAddress;
    @ExcelProperty(value = "用户ID", index = 10)
    private String userIdentifier;
    @ExcelProperty(value = "邮箱", index = 11)
    private String email;
    @ExcelProperty(value = "昵称", index = 12)
    private String nickname;
    @ExcelProperty(value = "密码", index = 13)
    private String password;
    @ExcelProperty(value = "设备标识符", index = 14)
    private String deviceId;
    @ExcelProperty(value = "IP地址", index = 15)
    private String ipAddress;
    @ExcelProperty(value = "国籍", index = 16)
    private String nationality;
    @ExcelProperty(value = "银行名称", index = 17)
    private String bankName;
    @ExcelProperty(value = "支付账号", index = 18)
    private String payAccount;
    @ExcelProperty(value = "支付时间", index = 19, converter = CustomDateConverter.class)
    private Date payTime;
    @ExcelProperty(value = "支付金额", index = 20)
    private double payAmount;
    @ExcelProperty(value = "订单编号", index = 21)
    private String orderNumber;
    @ExcelProperty(value = "支付密码", index = 22)
    private String payPassword;
    @ExcelProperty(value = "支付方式", index = 23)
    private String payMethod;
    @ExcelProperty(value = "当前地址", index = 24)
    private String currentAddress;
    @ExcelProperty(value = "当前经度", index = 25)
    private double currentLongitude;
    @ExcelProperty(value = "当前纬度", index = 26)
    private double currentLatitude;
    @ExcelProperty(value = "所在国家", index = 27)
    private String currentCountry;
    @ExcelProperty(value = "所在城市", index = 28)
    private String currentCity;
    @ExcelProperty(value = "目的地址", index = 29)
    private String destinationAddress;
    @ExcelProperty(value = "家庭住址", index = 30)
    private String homeAddress;
    @ExcelProperty(value = "常用地址", index = 31)
    private String frequentAddress;
}

package com.lu.gademo.entity.ga;

import com.alibaba.excel.annotation.ExcelProperty;
import com.lu.gademo.utils.CustomDateConverter;
import lombok.Data;

import java.util.Date;

@Data
public class MeetingDataTable {
    private Long sjhm;

    private String yhm;

    private String yhid;

    private String mm;

    private String xm;

    private String yx;

    private String hyzcr;

    private String sbbsf;

    private String hyzt;

    private String hyh;

    private Date hysj;

    private String chrnc;

    private String verCode;

    private String wechatNumber;

    private String ip;
}

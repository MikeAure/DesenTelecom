package com.lu.gademo.utils;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

/**
 *
 */
@Slf4j
@Data
public class DesenInfoStringBuilders {
    // 脱敏前信息类型标识
    public StringBuilder desenInfoPreIden;
    // 脱敏后信息类型标识
    public StringBuilder desenInfoAfterIden;
    // 脱敏意图
    public StringBuilder desenIntention;
    // 脱敏要求
    public StringBuilder desenRequirements;
    // 脱敏控制集合
    public String desenControlSet;
    // 脱敏参数
    public StringBuilder desenAlgParam;
    // 脱敏级别
    public StringBuilder desenLevel;
    // 脱敏算法
    public StringBuilder desenAlg;
    // 表格脱敏每列数据类型
    public StringBuilder fileDataType;

    public DesenInfoStringBuilders() {
        this.desenInfoPreIden = new StringBuilder();
        this.desenInfoAfterIden = new StringBuilder();
        this.desenIntention = new StringBuilder();
        this.desenRequirements = new StringBuilder();
        this.desenControlSet = "desencontrolset";
        this.desenAlgParam = new StringBuilder();
        this.desenLevel = new StringBuilder();
        this.desenAlg = new StringBuilder();
        this.fileDataType = new StringBuilder();
    }

}

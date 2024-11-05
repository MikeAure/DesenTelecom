package com.lu.gademo.entity.ga;

import lombok.Data;

@Data
public class ExcelAlgorithm {
    private int id;
    private Integer originalId; // 使用 Integer 以支持 null 值
    private String chineseName;
    private String name;
    private String type;
    private String params;

}

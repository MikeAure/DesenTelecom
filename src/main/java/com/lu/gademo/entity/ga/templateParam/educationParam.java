package com.lu.gademo.entity.ga.templateParam;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lu.gademo.entity.ga.support.BaseEntity;

import javax.persistence.*;
import java.io.IOException;

@Entity
@Table(name = "education" + "_param")
public class educationParam extends BaseEntity {
    private static final long serialVersionUID = 1L;
    //数据存储id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    //数据库中所存的字段名
    private String fieldName;

    //英文缩写的中文说明
    private String columnName;

    //数据类型：数值(0)，单编码(1)，文本(3)，日期(4)
    private Integer dataType;

    // 算法
    private Integer k;

    //高中低 0,1,2,3
    private Integer tmParam;


    public educationParam(String json) throws IOException {
//        System.out.println("json数据："+json);
        educationParam param = new ObjectMapper().readValue(json, educationParam.class);
        this.id = param.id;
        this.fieldName = param.fieldName;
        this.columnName = param.columnName;
        this.dataType = param.dataType;
        this.k = param.k;
        this.tmParam = param.tmParam;
    }

    public educationParam() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String filedName) {
        this.fieldName = filedName;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public Integer getDataType() {
        return dataType;
    }

    public void setDataType(Integer dataType) {
        this.dataType = dataType;
    }

    public Integer getTmParam() {
        return tmParam;
    }

    public void setTmParam(Integer tmParam) {
        this.tmParam = tmParam;
    }


    public Integer getK() {
        return k;
    }

    public void setK(Integer k) {
        this.k = k;
    }
}

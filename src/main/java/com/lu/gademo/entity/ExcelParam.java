package com.lu.gademo.entity;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lu.gademo.entity.support.BaseEntity;

import javax.persistence.*;
import java.io.IOException;
//文件脱敏参数实体
@Entity
public class ExcelParam extends BaseEntity {
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;
    private String fieldName;
    private String columnName;
    private Integer dataType;
    private Integer k;
    private Integer tmParam;
    //有参构造
    public ExcelParam(String json) throws IOException {
        ExcelParam param = new ObjectMapper().readValue(json, ExcelParam.class);
        this.id = param.id;
        this.fieldName = param.fieldName;
        this.columnName = param.columnName;
        this.dataType = param.dataType;
        this.tmParam = param.tmParam;
        this.k = param.k;
    }
    //无参构造
    public ExcelParam(){}
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
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

    public Integer getK() {
        return k;
    }

    public void setK(Integer k) {
        this.k = k;
    }

    public Integer getTmParam() {
        return tmParam;
    }

    public void setTmParam(Integer tmParam) {
        this.tmParam = tmParam;
    }

    @Override
    public String toString() {
        return "ExcelParam{" +
                "id=" + id +
                ", fieldName='" + fieldName + '\'' +
                ", columnName='" + columnName + '\'' +
                ", dataType=" + dataType +
                ", k=" + k +
                ", tmParam=" + tmParam +
                '}';
    }
}

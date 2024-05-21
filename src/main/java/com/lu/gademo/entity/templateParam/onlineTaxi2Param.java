package com.lu.gademo.entity.templateParam;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lu.gademo.entity.support.BaseEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.IOException;

@Entity
@Table(name = "onlinetaxi2_param")
@Data
@NoArgsConstructor
public class onlineTaxi2Param extends BaseEntity {
    private static final long serialVersionUID = 1L;
    //数据存储id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Basic
    @Column(name = "field_name")
    //数据库中所存的字段名
    private String fieldName;

    //英文缩写的中文说明
    @Column(name = "column_name")
    private String columnName;

    //数据类型：数值(0)，单编码(1)，文本(3)，日期(4)
    @Column(name = "data_type")
    private Integer dataType;

    // 算法
    @Column(name = "k")
    private Integer k;

    //高中低 0,1,2,3
    @Column(name = "tm_param")
    private Integer tmParam;

    public onlineTaxi2Param(String json) throws IOException {
//        System.out.println("json数据："+json);
        onlineTaxi2Param param = new ObjectMapper().readValue(json, onlineTaxi2Param.class);
        this.id = param.id;
        this.fieldName = param.fieldName;
        this.columnName = param.columnName;
        this.dataType = param.dataType;
        this.k = param.k;
        this.tmParam = param.tmParam;
    }


}

package com.lu.gademo.utils;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;


public class DSObject {

    Integer intVal;

    Double doubleVal;

    String string;

    List<?> list;

    MultipartFile file;

    public DSObject(Integer intVal) {
        this.intVal = intVal;
    }

    public DSObject(Double doubleVal) {
        this.doubleVal = doubleVal;
    }

    public DSObject(String string) {
        this.string = string;
    }

    public DSObject(List<?> list) {
        this.list = list;
    }

    public DSObject(String string, List<?> list) {
        this.string = string;
        this.list = list;
    }

    public Integer getIntVal() {
        return intVal;
    }

    public void setIntVal(Integer intVal) {
        this.intVal = intVal;
    }

    public Double getDoubleVal() {
        return this.doubleVal;
    }

    public void setDoubleVal(Double doubleVal) {
        this.doubleVal = doubleVal;
    }

    public String getStringVal() {
        return this.string;
    }

    public List<?> getList() {
        return list;
    }

    public void setList(List<?> list) {
        this.list = list;
    }

    public void setString(String string) {
        this.string = string;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof DSObject) {
            DSObject dsObject = (DSObject) obj;

            // 比较整数和双精度值
            boolean intsEqual = this.intVal == dsObject.intVal;
            boolean doublesEqual = this.doubleVal == dsObject.doubleVal;

            // 安全地比较字符串，处理可能的null值
            boolean stringsEqual = (Objects.equals(this.string, dsObject.string));

            // 安全地比较列表，处理可能的null值
            boolean listsEqual = (Objects.equals(this.list, dsObject.list));

            return intsEqual && doublesEqual && stringsEqual && listsEqual;

        } else {
            return false;
        }
    }
}

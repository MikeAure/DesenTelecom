package com.lu.gademo.utils;

import org.springframework.web.multipart.MultipartFile;
import java.util.List;


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

    public DSObject(String string) { this.string = string; }

    public DSObject(List<?> list) {
        this.list = list;
    }

    public DSObject(String string, List<?> list) { this.string = string; this.list = list; }

    public Integer getIntVal() {
        return intVal;
    }

    public Double getDoubleVal() {
        return this.doubleVal;
    }

    public String getStringVal() {
        return this.string;
    }

    public List<?> getList() {
        return list;
    }

    public void setIntVal(Integer intVal) {
        this.intVal = intVal;
    }

    public void setDoubleVal(Double doubleVal) {
        this.doubleVal = doubleVal;
    }

    public void setString(String string) { this.string = string; }

    public void setList(List<?> list) {
        this.list = list;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof DSObject) {
            DSObject dsObject = (DSObject) obj;

            // 比较整数和双精度值
            boolean intsEqual = this.intVal == dsObject.intVal;
            boolean doublesEqual = this.doubleVal == dsObject.doubleVal;

            // 安全地比较字符串，处理可能的null值
            boolean stringsEqual = (this.string == null ? dsObject.string == null : this.string.equals(dsObject.string));

            // 安全地比较列表，处理可能的null值
            boolean listsEqual = (this.list == null ? dsObject.list == null : this.list.equals(dsObject.list));

            return intsEqual && doublesEqual && stringsEqual && listsEqual;

        }
        else {
            return false;
        }
    }
}

package com.lu.gademo.utils;

import org.springframework.web.multipart.MultipartFile;
import java.util.List;

public class DSObject {

    Integer intVal;
    
    Double doubleVal;

    List<?> list;

    MultipartFile file;

    public DSObject(Integer intVal) {
        this.intVal = intVal;
    }

    public DSObject(Double doubleVal) {
        this.doubleVal = doubleVal;
    }


    public DSObject(List<?> list) {
        this.list = list;
    }

    public Integer getIntVal() {
        return intVal;
    }

    public Double getDoubleVal() {
        return this.doubleVal;
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

    public void setList(List<?> list) {
        this.list = list;
    }

}

package com.lu.gademo.trace.model;

public class ListDriver {
    private String name;
    private String phone;
//    private int imgId;

    public ListDriver(String name, String phone) {
        this.name = name;
        this.phone = phone;
        // this.imgId = imgId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

//    public int getImgId() {
//        return imgId;
//    }
//
//    public void setImgId(int imgId) {
//        this.imgId = imgId;
//    }
}

package com.lu.gademo.vo;

public class NumsView {
    private String colNname;
    private Double pre;
    private Double post;

    public String getColNname() {
        return colNname;
    }

    public void setColNname(String colNname) {
        this.colNname = colNname;
    }

    public Double getPre() {
        return pre;
    }

    public void setPre(Double pre) {
        this.pre = pre;
    }

    public Double getPost() {
        return post;
    }

    public void setPost(Double post) {
        this.post = post;
    }

    @Override
    public String toString() {
        return "NumsView{" +
                "colNname='" + colNname + '\'' +
                ", pre=" + pre +
                ", post=" + post +
                '}';
    }
}

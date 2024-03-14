package com.lu.gademo.vo;

import java.util.HashMap;

public class SingleView {
    private String colName;
    //用来存储单编码数据的展示，key:为编码值，value为统计数量
     public static class Node{
        private Integer preNums=0;
        private Integer postNums=0;

        public Integer getPreNums() {
            return preNums;
        }

        public void setPreNums(Integer preNums) {
            this.preNums = preNums;
        }

        public Integer getPostNums() {
            return postNums;
        }

        public void setPostNums(Integer postNums) {
            this.postNums = postNums;
        }
        public void PreAdd(){
            this.preNums++;
        }
        public void  postAdd(){
            this.postNums++;
        }
    }
    private HashMap<String,Node> TongMap;

    public String getColName() {
        return colName;
    }

    public void setColName(String colName) {
        this.colName = colName;
    }

    public HashMap<String, Node> getTongMap() {
        return TongMap;
    }

    public void setTongMap(HashMap<String, Node> tongMap) {
        TongMap = tongMap;
    }

}

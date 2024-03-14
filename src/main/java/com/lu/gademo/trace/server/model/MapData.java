package com.lu.gademo.trace.server.model;

import java.io.Serializable;

public class MapData implements Serializable {

    /**
     * Eighty Four Encrypted Maps 84个加密的地图数据
     */
    public static final int EFEM = 1;

    /**
     * Encrypted Calculated Position and Map 自身位置与加密地图的计算结果
     */
    public static final int ECPM = 2;

    /**
     * Encrypted Circle Data 加密的圆形数据
     */
    public static final int ECD = 3;

    /**
     * Encrypted Circum Square Data 加密的外接正方形数据
     */
    public static final int ECSD = 4;


    private StringBuffer encryptedMap = null;//加密后的地图数据或者计算结果，由于数值庞大，只能用StringBuffer
    private int dataType;//数据类型，84个地图块数据 还是 圆形数据：上面的常量值

    public MapData() {
    }

    public StringBuffer getEncryptedMap() {
        return encryptedMap;
    }

    public void setEncryptedMap(StringBuffer encryptedMap) {
        this.encryptedMap = encryptedMap;
    }

    public int getDataType() {
        return dataType;
    }

    public void setDataType(int dataType) {
        this.dataType = dataType;
    }


}

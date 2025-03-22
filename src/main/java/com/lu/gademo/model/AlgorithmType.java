package com.lu.gademo.model;


public enum AlgorithmType {
    DP(1, "扰动"),
    GENERALIZATION(2, "泛化"),
    ANONYMITY(3, "匿名"),
    REPLACEMENT(4, "置换"),
    UNKNOWN(10, "未知类型");

    private final int value;
    private final String name;

    AlgorithmType(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public String getName() {return name;}

    @Override
    public String toString() {
        return "AlgorithmType{" +
                "value=" + value +
                ", name=" + name +
                '}';
    }

    public static AlgorithmType fromValue(int value) {
        for (AlgorithmType type : AlgorithmType.values()) {
            if (type.getValue() == value) {
                return type;
            }
        }
        return UNKNOWN;
    }
}

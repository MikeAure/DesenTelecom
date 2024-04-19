package com.lu.gademo.utils;

public enum AlgorithmType {
    DP(1),
    GENERALIZATION(2),
    ANONYMITY(3),
    REPLACEMENT(4),
    UNKNOWN(10);

    private final int value;

    AlgorithmType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}

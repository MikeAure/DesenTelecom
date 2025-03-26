package com.lu.gademo.model;

public enum DocumentTypeMapping {
    TEXT("文本", 3),
    NUMBER("数字", 0),
    UNKNOWN("未知类型", -1);

    private final String name;
    private final int value;

    DocumentTypeMapping(String name, int value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public int getValue() {
        return value;
    }

    public static int getValueByName(String targetName) {
        for (DocumentTypeMapping elem : DocumentTypeMapping.values()) {
            if (elem.getName().equals(targetName)) {
                return elem.getValue();
            }
        }
        return -1;
    }

    public static DocumentTypeMapping getValueByValue(int targetValue) {
        for (DocumentTypeMapping elem : DocumentTypeMapping.values()) {
            if (elem.getValue() == targetValue) {
                return elem;
            }
        }
        return UNKNOWN;
    }

}

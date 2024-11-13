package com.lu.gademo.model;

import lombok.Getter;

@Getter
public enum ModalTypes {
    TEXT(1, "text", "文本"),
    SHEET(2, "sheet", "表格"),
    IMAGE(3, "image", "图像"),
    VIDEO(4, "video", "视频"),
    AUDIO(5, "audio", "音频"),
    GRAPH(6, "graph", "图形"),
    UNKNOWN(10, "unknown", "未知");

    int id;
    String type;
    String name;

    ModalTypes(int id, String type, String name) {
        this.id = id;
        this.type = type;
        this.name = name;
    }

    public static ModalTypes fromId(int id) {
        for (ModalTypes type : ModalTypes.values()) {
            if (type.getId() == id) {
                return type;
            }
        }
        return UNKNOWN;
    }

    public static ModalTypes fromType(String type) {
        for (ModalTypes modalType : ModalTypes.values()) {
            if (modalType.getType().equals(type)) {
                return modalType;
            }
        }
        return UNKNOWN;
    }

    public static ModalTypes fromName(String name) {
        for (ModalTypes modalType : ModalTypes.values()) {
            if (modalType.getName().equals(name)) {
                return modalType;
            }
        }
        return UNKNOWN;
    }
}

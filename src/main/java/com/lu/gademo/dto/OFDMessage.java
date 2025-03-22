package com.lu.gademo.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.format.DateTimeFormatter;

@Setter
@Getter
public class OFDMessage {
    @Getter(onMethod_={@JsonIgnore})
    private final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // Getters and Setters
    private final int systemID = 0x31000000;
    private String systemIP;
    private final int mainCMD =0x0008;
    private final int subCMD = 0x0031;
    private String evidenceID;
    private final int msgVersion = 0x3110;
    private String submittime = DATE_FORMATTER.format(java.time.LocalDateTime.now());
    private Data data;
    private String dataHash;
    private String datasign;
    private String randomidentification;

    @Getter
    @Setter
    public static class Data {
        private String globalID;
        @JsonProperty("DataType")
        private int DataType;
        private int status;
        private int maxHops = 5;
        private long parent_systemID;
        private String parent_systemIP;
        private String parent_dataPath;
        private String parent_dataID;
        private long self_systemID;
        private String self_systemIP;
        private String self_dataPath;
        private String self_dataID;
        private long child_systemID;
        private String child_systemIP;
        private String child_dataPath;
        private String child_dataID;

    }
}

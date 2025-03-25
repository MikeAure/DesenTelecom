package com.lu.gademo.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.format.DateTimeFormatter;

@Setter
@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class OFDMessage {
    @Getter(onMethod_={@JsonIgnore})
    private final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // Getters and Setters
    private long systemID;
    private String systemIP;
    private int mainCMD;
    private int subCMD;
    private String evidenceID;
    private int msgVersion;
    private String submittime = DATE_FORMATTER.format(java.time.LocalDateTime.now());
    private Data data;
    private String dataHash;
    private String datasign;
    private String randomidentification;

    @Getter
    @Setter
    @Builder
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Data {
        @JsonProperty("globalID")
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

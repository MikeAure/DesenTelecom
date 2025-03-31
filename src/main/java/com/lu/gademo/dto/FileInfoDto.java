package com.lu.gademo.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.tomcat.jni.FileInfo;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FileInfoDto {
    String fileType;
    @JsonProperty("globalID")
    String globalID;
    ObjectNode evaluationLog;
    boolean random;
    boolean redesen;

    public FileInfoDto(String fileType, String globalID) {
        this.fileType = fileType;
        this.globalID = globalID;
        this.evaluationLog = null;
        this.random = false;
        this.redesen = false;
    }
}

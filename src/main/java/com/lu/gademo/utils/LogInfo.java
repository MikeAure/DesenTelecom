package com.lu.gademo.utils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LogInfo {
    private String globalID;
    private String evidenceID;
    private Boolean desenCom;
    private String objectMode;
    private DesenInfoStringBuilders infoBuilders;
    private String rawFileName;
    private byte[] rawFileBytes;
    private long rawFileSize;
    private String desenFileName;
    private byte[] desenFileBytes;
    private long desenFileSize;
    private String fileType;
    private String rawFileSuffix;
    private String startTime;
    private String endTime;
}

package com.lu.gademo.model;

import com.lu.gademo.utils.DesenInfoStringBuilders;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class LogInfo {
    private String evidenceID;
    private DesenInfoStringBuilders infoBuilders;
    private byte[] rawFileBytes;
    private byte[] desenFileBytes;
    private Long rawFileSize;
    private Long desenFileSize;
    private String globalID;
    private String startTime;
    private String endTime;
    private boolean desenCom;
}

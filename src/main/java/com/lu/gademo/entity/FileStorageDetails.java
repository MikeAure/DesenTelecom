package com.lu.gademo.entity;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.nio.file.Path;

/**
 * 用于存储脱敏前后文件信息的结构体，包括脱敏前后文件名、文件后缀、文件路径、文件大小、文件字节数组
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Slf4j
@Builder
public class FileStorageDetails {
    private String rawFileName;
    private String rawFileSuffix;
    private Path rawFilePath;
    private String rawFilePathString;
    private byte[] rawFileBytes;
    private Long rawFileSize;

    private String desenFileName;
    private String desenFileSuffix;
    private Path desenFilePath;
    private String desenFilePathString;
    private byte[] desenFileBytes;
    private Long desenFileSize;
}

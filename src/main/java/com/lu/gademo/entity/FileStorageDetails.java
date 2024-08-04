package com.lu.gademo.entity;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.nio.file.Path;

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

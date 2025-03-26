package com.lu.gademo.service;


import com.lu.gademo.dto.FileInfoDto;
import com.lu.gademo.dto.OFDMessage;
import com.lu.gademo.dto.SendToCourse4Dto;
import org.springframework.core.io.Resource;

import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;

public interface RemoteCallService {
    CompletableFuture<Resource> sendMultipartData(Path rawFilePath, Path desenFilePath, FileInfoDto fileType, String url);
    CompletableFuture<String> sendCirculationLog(OFDMessage ofdMessage, String url);
    CompletableFuture<String> sendLevels(SendToCourse4Dto sendToCourse4Dto, String url);
}

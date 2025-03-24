package com.lu.gademo.service;


import com.lu.gademo.dto.FileInfoDto;
import com.lu.gademo.dto.OFDMessage;
import com.lu.gademo.dto.SendToClass4Dto;
import org.springframework.core.io.Resource;

import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;

public interface RemoteCallService {
    CompletableFuture<Resource> sendMultipartData(Path rawFilePath, Path desenFilePath, FileInfoDto fileType, String url);
    CompletableFuture<String> sendCirculationLog(OFDMessage ofdMessage, String url);
    CompletableFuture<String> sendLevels(SendToClass4Dto sendToClass4Dto, String url);
}

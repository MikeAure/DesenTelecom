package com.lu.gademo.service;

import java.nio.file.Path;

public interface RemoteCallService {
    void sendMultipartData(Path rawFilePath, Path desenFilePath, String fileType);
    void sendJsonMessage(Path desenFilePath);
}

package com.lu.gademo.utils;

import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;

public interface RecvFileDesen {

    byte[] desenRecvFile(MultipartFile file) throws Exception;
}

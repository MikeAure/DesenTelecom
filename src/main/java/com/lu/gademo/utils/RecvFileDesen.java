package com.lu.gademo.utils;

import org.springframework.web.multipart.MultipartFile;

public interface RecvFileDesen {

    // 接收信工所Office文档
    byte[] desenRecvFile(MultipartFile file) throws Exception;
}

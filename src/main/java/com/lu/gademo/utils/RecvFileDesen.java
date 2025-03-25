package com.lu.gademo.utils;

import com.lu.gademo.entity.DocxDesenRequirement;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigInteger;
import java.nio.file.Path;
import java.util.Map;

public interface RecvFileDesen {

    // 接收信工所Office文档
    byte[] desenRecvFile(MultipartFile file) throws Exception;
    void processWord(Path rawFilePath, Path desenFilePath);
    void processXlsx(Path rawFilePath, Path desenFilePath) throws Exception;
    Map<String, String> extractCommentMapFromWord(Path rawFilePath);
    Map<BigInteger, DocxDesenRequirement> getDocxDesenRequirement(Path rawFilePath,
                                                                  Map<String, String> commentMap);
    void desenWord(Path rawFilePath, Path desenFilePath, Map<BigInteger, DocxDesenRequirement> commentMap);
}

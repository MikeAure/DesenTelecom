package com.lu.gademo.service;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.lu.gademo.entity.FileStorageDetails;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public interface FileStorageService {
    FileStorageDetails saveRawFileWithDesenInfo(MultipartFile file) throws IOException;

    FileStorageDetails saveRawFileTruncationWithDesenInfo(MultipartFile file) throws IOException;

    FileStorageDetails saveRawFileWithDesenInfoByLog(ObjectNode evaluationLog) throws IOException;

    FileStorageDetails saveRawFileWithDesenInfoForBigFile(MultipartFile file) throws IOException;

    FileStorageDetails saveRawFileWithDesenInfo(Path file) throws IOException;
    FileStorageDetails saveRawFileWithDesenInfo(List<Path> file) throws IOException;

    FileStorageDetails saveRawFile(MultipartFile file) throws IOException;

    FileStorageDetails saveRawFile(List<MultipartFile> file) throws IOException;

    FileStorageDetails saveRawFile(String fileName, byte[] rawFileBytes) throws IOException;

    FileStorageDetails saveRawFile(String fileName) throws IOException;

    void saveDesenFile(Path desenFilePath, byte[] desenFileBytes) throws IOException;

    void saveDesenFile(FileStorageDetails fileStorageDetails, byte[] desenFileBytes) throws IOException;

    Path getCurrentDirectory();

    Path getRawFileDirectory();

    Path getDesenFileDirectory();

    void setCurrentDirectory(Path currentDirectory);

    void setRawFileDirectory(Path rawFileDirectory);

    void setDesenFileDirectory(Path desenFileDirectory);
}

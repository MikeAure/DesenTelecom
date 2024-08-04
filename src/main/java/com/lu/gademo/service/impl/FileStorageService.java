package com.lu.gademo.service.impl;

import com.lu.gademo.entity.FileStorageDetails;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

@Slf4j
@Data
@Service
public class FileStorageService {
    private Path currentDirectory;
    private Path rawFileDirectory;
    private Path desenFileDirectory;

    public FileStorageService() throws IOException {
        this.currentDirectory = Paths.get("");
        this.rawFileDirectory = Paths.get("raw_files");
        this.desenFileDirectory = Paths.get("desen_files");

        try {
        if (!Files.exists(rawFileDirectory)) {
            Files.createDirectory(rawFileDirectory);
        }
        if (!Files.exists(desenFileDirectory)) {
            Files.createDirectory(desenFileDirectory);
        }
        } catch (IOException e) {
            log.error("创建原始文件和脱敏后文件保存目录失败：{}", e.getMessage());
        }
    }

    public FileStorageDetails saveRawFile(MultipartFile file) throws IOException {
        String fileTimeStamp = String.valueOf(System.currentTimeMillis());

        if (file.getOriginalFilename() == null) {
            throw new IOException("Input file name is null");
        }

        String originalFileName = file.getOriginalFilename();
        String fileName = originalFileName.substring(0, originalFileName.lastIndexOf("."));
        String rawFileSuffix = originalFileName.substring(originalFileName.lastIndexOf(".") + 1);
        String rawFileNameTemp = fileTimeStamp + "_" + fileName;
        String rawFileName = fileTimeStamp + "_" + originalFileName;
        Path rawFilePath = rawFileDirectory.resolve(rawFileName);
        String rawFilePathString = rawFilePath.toAbsolutePath().toString();
        byte[] rawFileBytes = file.getBytes();
        Long rawFileSize = file.getSize();

        // Path for the desensitized file
        String desenFileTimeStamp = String.valueOf(System.currentTimeMillis());
        String desenFileName = rawFileNameTemp + "_" + desenFileTimeStamp + "." + rawFileSuffix;
        Path desenFilePath = desenFileDirectory.resolve(desenFileName);
        String desenFilePathString = desenFilePath.toAbsolutePath().toString();

        // Save the original file
        file.transferTo(rawFilePath);

        return FileStorageDetails.builder()
                .rawFileName(rawFileName)
                .rawFileSuffix(rawFileSuffix)
                .rawFilePath(rawFilePath)
                .rawFilePathString(rawFilePathString)
                .rawFileBytes(rawFileBytes)
                .rawFileSize(rawFileSize)
                .desenFileName(desenFileName)
                .desenFileSuffix(rawFileSuffix)
                .desenFilePath(desenFilePath)
                .desenFilePathString(desenFilePathString)
                .build();
    }

    public void saveDesenFile(Path desenFilePath, byte[] desenFileBytes) throws IOException {
        // 确保父目录存在
        if (Files.notExists(desenFilePath.getParent())) {
            Files.createDirectories(desenFilePath.getParent());
        }
        // 使用Files.write来保存文件
        Files.write(desenFilePath, desenFileBytes, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE);
    }

    public void saveDesenFile(FileStorageDetails fileStorageDetails, byte[] desenFileBytes) throws IOException {
        // 确保父目录存在
        Path desenFilePath = fileStorageDetails.getDesenFilePath();
        if (Files.notExists(desenFilePath.getParent())) {
            Files.createDirectories(desenFilePath.getParent());
        }
        // 使用Files.write来保存文件
        Files.write(desenFilePath, desenFileBytes, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE);
        fileStorageDetails.setDesenFileBytes(desenFileBytes);
    }
}

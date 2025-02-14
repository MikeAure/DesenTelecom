package com.lu.gademo.service.impl;

import com.lu.gademo.entity.FileStorageDetails;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;

@Slf4j
@Data
@Service
public class FileStorageServiceImpl implements com.lu.gademo.service.FileStorageService {
    private Path currentDirectory;
    private Path rawFileDirectory;
    private Path desenFileDirectory;

    public FileStorageServiceImpl() throws IOException {
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

    /**
     * @param file 保存MultipartFile并生成原始文件和脱敏文件相关的信息
     * @return 包含原始文件路径信息和文件内容、脱敏文件路径信息的FileStorageDetails
     * @throws IOException
     */
    @Override
    public FileStorageDetails saveRawFileWithDesenInfo(MultipartFile file) throws IOException {
        String fileTimeStamp = String.valueOf(System.currentTimeMillis());

        if (file.getOriginalFilename() == null) {
            throw new IOException("Input file name is null");
        }

        String originalFileName = Paths.get(file.getOriginalFilename()).getFileName().toString();
        String fileName = originalFileName.substring(0, originalFileName.lastIndexOf("."));
        String rawFileSuffix = originalFileName.substring(originalFileName.lastIndexOf(".") + 1);
        String rawFileNameTemp = fileTimeStamp + "_" + fileName;
        String rawFileName = fileTimeStamp + "_" + originalFileName;
//        log.info("RawFileName: {}", rawFileName);
        Path rawFilePath = rawFileDirectory.resolve(rawFileName);
        String rawFilePathString = rawFilePath.toAbsolutePath().toString();
        byte[] rawFileBytes = file.getBytes();
        Long rawFileSize = file.getSize();
        log.info("rawFileSize: {}", rawFileSize);
        // Path for the desensitized file
        String desenFileTimeStamp = String.valueOf(System.currentTimeMillis());
        String desenFileName = rawFileNameTemp + "_" + desenFileTimeStamp + "." + rawFileSuffix;
        Path desenFilePath = desenFileDirectory.resolve(desenFileName);
        String desenFilePathString = desenFilePath.toAbsolutePath().toString();

        // Save the original file
        file.transferTo(rawFilePath);
//        Files.write(rawFilePath, rawFileBytes);

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

    /**
     * @param file 保存MultipartFile并生成原始文件和脱敏文件相关的信息
     * @return 包含原始文件路径信息和文件内容、脱敏文件路径信息的FileStorageDetails
     * @throws IOException
     */
    @Override
    public FileStorageDetails saveRawFileWithDesenInfoForBigFile(MultipartFile file) throws IOException {
        String fileTimeStamp = String.valueOf(System.currentTimeMillis());

        if (file.getOriginalFilename() == null) {
            throw new IOException("Input file name is null");
        }

        String originalFileName = Paths.get(file.getOriginalFilename()).getFileName().toString();
        String fileName = originalFileName.substring(0, originalFileName.lastIndexOf("."));
        String rawFileSuffix = originalFileName.substring(originalFileName.lastIndexOf(".") + 1);
        String rawFileNameTemp = fileTimeStamp + "_" + fileName;
        String rawFileName = fileTimeStamp + "_" + originalFileName;
//        log.info("RawFileName: {}", rawFileName);
        Path rawFilePath = rawFileDirectory.resolve(rawFileName);
        String rawFilePathString = rawFilePath.toAbsolutePath().toString();
        Long rawFileSize = file.getSize();
        log.info("rawFileSize: {}", rawFileSize);
        // Path for the desensitized file
        String desenFileTimeStamp = String.valueOf(System.currentTimeMillis());
        String desenFileName = rawFileNameTemp + "_" + desenFileTimeStamp + "." + rawFileSuffix;
        Path desenFilePath = desenFileDirectory.resolve(desenFileName);
        String desenFilePathString = desenFilePath.toAbsolutePath().toString();

        // Save the original file
        file.transferTo(rawFilePath);
//        Files.write(rawFilePath, rawFileBytes);

        return FileStorageDetails.builder()
                .rawFileName(rawFileName)
                .rawFileSuffix(rawFileSuffix)
                .rawFilePath(rawFilePath)
                .rawFilePathString(rawFilePathString)
                .rawFileSize(rawFileSize)
                .desenFileName(desenFileName)
                .desenFileSuffix(rawFileSuffix)
                .desenFilePath(desenFilePath)
                .desenFilePathString(desenFilePathString)
                .build();
    }

    /**
     * 将原始文件复制到raw_files，并设置脱敏文件路径信息
     *
     * @param file
     * @return 包含原始文件路径信息和文件内容、脱敏后文件路径信息的FileStorageDetails
     * @throws IOException
     */
    @Override
    public FileStorageDetails saveRawFileWithDesenInfo(Path file) throws IOException {
        String fileTimeStamp = String.valueOf(System.currentTimeMillis());

        if (!Files.exists(file)) {
            throw new IOException("Input file is wrong");
        }

        String originalFileName = file.getFileName().toString();
        String fileName = originalFileName.substring(0, originalFileName.lastIndexOf("."));
        String rawFileSuffix = originalFileName.substring(originalFileName.lastIndexOf(".") + 1);
        String rawFileNameTemp = fileTimeStamp + "_" + fileName;
        String rawFileName = fileTimeStamp + "_" + originalFileName;
//        log.info("RawFileName: {}", rawFileName);
        Path rawFilePath = rawFileDirectory.resolve(rawFileName);
        String rawFilePathString = rawFilePath.toAbsolutePath().toString();
        byte[] rawFileBytes = Files.readAllBytes(file);
        Long rawFileSize = Files.size(file);

        // Path for the desensitized file
        String desenFileTimeStamp = String.valueOf(System.currentTimeMillis());
        String desenFileName = rawFileNameTemp + "_" + desenFileTimeStamp + "." + rawFileSuffix;
        Path desenFilePath = desenFileDirectory.resolve(desenFileName);
        String desenFilePathString = desenFilePath.toAbsolutePath().toString();

        // Save the original file
        Files.copy(file, rawFilePath, StandardCopyOption.REPLACE_EXISTING);

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

    /**
     * 保存MultipartFile并仅生成原始文件相关的信息
     *
     * @param file 上传的文件
     * @return 仅包含原始文件信息的FileStorageDetails
     * @throws IOException
     */
    @Override
    public FileStorageDetails saveRawFile(MultipartFile file) throws IOException {
        String fileTimeStamp = String.valueOf(System.currentTimeMillis());

        if (file.getOriginalFilename() == null) {
            throw new IOException("Input file name is null");
        }

        String originalFileName = file.getOriginalFilename();
        String rawFileSuffix = originalFileName.substring(originalFileName.lastIndexOf(".") + 1);
        String rawFileName = fileTimeStamp + "_" + originalFileName;
        Path rawFilePath = rawFileDirectory.resolve(rawFileName);
        String rawFilePathString = rawFilePath.toAbsolutePath().toString();
        byte[] rawFileBytes = file.getBytes();
        Long rawFileSize = file.getSize();

        // Save the original file
        file.transferTo(rawFilePath);

        return FileStorageDetails.builder()
                .rawFileName(rawFileName)
                .rawFileSuffix(rawFileSuffix)
                .rawFilePath(rawFilePath)
                .rawFilePathString(rawFilePathString)
                .rawFileBytes(rawFileBytes)
                .rawFileSize(rawFileSize)
                .build();
    }

    @Override
    public FileStorageDetails saveRawFile(List<MultipartFile> files) throws IOException {

        if (CollectionUtils.isEmpty(files)) {
            throw new IOException("Input files is null");
        }

        FileStorageDetails fileStorageDetails = new FileStorageDetails();

        String fileTimeStamp = String.valueOf(System.currentTimeMillis());

        for (MultipartFile file : files) {
            String originalFileName = file.getOriginalFilename();
            if (originalFileName != null) {
                String rawFileSuffix = originalFileName.substring(originalFileName.lastIndexOf(".") + 1);
                String rawFileName = fileTimeStamp + "_" + originalFileName;
                Path rawFilePath = rawFileDirectory.resolve(rawFileName);
                String rawFilePathString = rawFilePath.toAbsolutePath().toString();
                byte[] rawFileBytes = file.getBytes();
                Long rawFileSize = file.getSize();
                file.transferTo(rawFilePath);

                if (originalFileName.endsWith(".shp")) {
                    fileStorageDetails = FileStorageDetails.builder()
                            .rawFileName(rawFileName)
                            .rawFileSuffix(rawFileSuffix)
                            .rawFilePath(rawFilePath)
                            .rawFilePathString(rawFilePathString)
                            .rawFileBytes(rawFileBytes)
                            .rawFileSize(rawFileSize)
                            .build();
                }
            }
        }

        return fileStorageDetails;
    }

    /**
     * 将字节数组的内容保存到原始文件中并返回原始文件信息
     *
     * @param fileName     文件名
     * @param rawFileBytes 文件的字节数组
     * @return 仅包含原始文件信息的FileStorageDetails
     * @throws IOException
     */
    @Override
    public FileStorageDetails saveRawFile(String fileName, byte[] rawFileBytes) throws IOException {
        String fileTimeStamp = String.valueOf(System.currentTimeMillis());

        String rawFileSuffix = fileName.substring(fileName.lastIndexOf(".") + 1);
        String rawFileName = fileTimeStamp + "_" + fileName;
        Path rawFilePath = rawFileDirectory.resolve(rawFileName);
        String rawFilePathString = rawFilePath.toAbsolutePath().toString();
        Files.write(rawFilePath, rawFileBytes, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE);
        Long rawFileSize = Files.size(rawFilePath);

        return FileStorageDetails.builder()
                .rawFileName(rawFileName)
                .rawFileSuffix(rawFileSuffix)
                .rawFilePath(rawFilePath)
                .rawFilePathString(rawFilePathString)
                .rawFileBytes(rawFileBytes)
                .rawFileSize(rawFileSize)
                .build();
    }

    /**
     * 返回仅包含原始文件存储路径信息的FileStorageDetails，文件内容为空
     *
     * @param fileName 文件名
     * @return 仅包含原始文件存储路径信息的FileStorageDetails
     * @throws IOException
     */
    @Override
    public FileStorageDetails saveRawFile(String fileName) throws IOException {
        String fileTimeStamp = String.valueOf(System.currentTimeMillis());

        String rawFileSuffix = fileName.substring(fileName.lastIndexOf(".") + 1);
        String rawFileName = fileTimeStamp + "_" + fileName;
        Path rawFilePath = rawFileDirectory.resolve(rawFileName);
        String rawFilePathString = rawFilePath.toAbsolutePath().toString();

        return FileStorageDetails.builder()
                .rawFileName(rawFileName)
                .rawFileSuffix(rawFileSuffix)
                .rawFilePath(rawFilePath)
                .rawFilePathString(rawFilePathString)
                .build();
    }


    @Override
    public void saveDesenFile(Path desenFilePath, byte[] desenFileBytes) throws IOException {
        // 确保父目录存在
        if (Files.notExists(desenFilePath.getParent())) {
            Files.createDirectories(desenFilePath.getParent());
        }
        // 使用Files.write来保存文件
        Files.write(desenFilePath, desenFileBytes, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE);
    }

    @Override
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

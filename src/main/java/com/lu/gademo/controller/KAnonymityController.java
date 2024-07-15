package com.lu.gademo.controller;

import com.lu.gademo.utils.Anonymity;
import com.lu.gademo.utils.DSObject;
import com.lu.gademo.utils.KAnonymityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

@Controller
@RequestMapping("/KAnonymity")
public class KAnonymityController {
    private File directory;
    private String currentPath;
    private String dir;

    @Autowired
    private Anonymity anonymity;


    public KAnonymityController() throws IOException {
        this.directory = new File("");
        this.currentPath = directory.getAbsolutePath();
        Path dirPath = Paths.get(currentPath, "templates");
        this.dir = Files.exists(dirPath) ? dirPath.toAbsolutePath().toString()
                : Files.createDirectories(dirPath).toAbsolutePath().toString();
    }


    @PostMapping("/LDiversity/Entropy")
    public ResponseEntity<?> LDiversityEntropy(@RequestParam("csvFile") MultipartFile csvFile,
                                               @RequestParam Map<String, MultipartFile> templates,
                                               @RequestParam("params") String params,
                                               @RequestParam("attribute") String attribute) {
        try {
            String originalCsvFileName = csvFile.getOriginalFilename();
            saveFile(csvFile, originalCsvFileName);
            if (originalCsvFileName != null) {
                String baseName = originalCsvFileName.substring(0, originalCsvFileName.lastIndexOf('.'));
                String extension = originalCsvFileName.substring(originalCsvFileName.lastIndexOf('.'));
                for (Map.Entry<String, MultipartFile> entry : templates.entrySet()) {
                    if (!Objects.equals(entry.getKey(), "csvFile")) {
                        String fileName = baseName + "_hierarchy_" + entry.getKey() + extension;
                        saveFile(entry.getValue(), fileName);
                    }
                }
                KAnonymityUtil kAnonymityUtil = new KAnonymityUtil();
                DSObject dsObject = new DSObject(Arrays.asList(baseName, dir, attribute));
                String output = anonymity.service(dsObject, 8, Integer.parseInt(params)).getStringVal();
//                String output = kAnonymityUtil.lEntropyDiversity(baseName, dir, params, attribute);
                byte[] fileContent = Files.readAllBytes(Paths.get(output));

                HttpHeaders headers = new HttpHeaders();
                headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + output);
                headers.add(HttpHeaders.CONTENT_TYPE, "application/octet-stream");

                return new ResponseEntity<>(fileContent, headers, HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return null;
    }

    @PostMapping("/LDiversity/RecursiveC")
    public ResponseEntity<?> LDiversityRecursiveC(@RequestParam("csvFile") MultipartFile csvFile,
                                                  @RequestParam Map<String, MultipartFile> templates,
                                                  @RequestParam("params") String params,
                                                  @RequestParam("attribute") String attribute) {
        try {
            String originalCsvFileName = csvFile.getOriginalFilename();
            saveFile(csvFile, originalCsvFileName);
            if (originalCsvFileName != null) {
                String baseName = originalCsvFileName.substring(0, originalCsvFileName.lastIndexOf('.'));
                String extension = originalCsvFileName.substring(originalCsvFileName.lastIndexOf('.'));
                for (Map.Entry<String, MultipartFile> entry : templates.entrySet()) {
                    if (!Objects.equals(entry.getKey(), "csvFile")) {
                        String fileName = baseName + "_hierarchy_" + entry.getKey() + extension;
                        saveFile(entry.getValue(), fileName);
                    }
                }
                KAnonymityUtil kAnonymityUtil = new KAnonymityUtil();
                DSObject dsObject = new DSObject(Arrays.asList(baseName, dir, attribute));
                String output = anonymity.service(dsObject, 9, Integer.parseInt(params)).getStringVal();
//                String output = kAnonymityUtil.lRecursiveCDiversity(baseName, dir, params, attribute);
                byte[] fileContent = Files.readAllBytes(Paths.get(output));
                HttpHeaders headers = new HttpHeaders();
                headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + output);
                headers.add(HttpHeaders.CONTENT_TYPE, "application/octet-stream");

                return new ResponseEntity<>(fileContent, headers, HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return null;
    }

    public void saveFile(MultipartFile file, String fileName) throws IOException {
        if (file.isEmpty()) {
            throw new RuntimeException("Empty File Error.");
        }
        Path path = Paths.get(dir, fileName);
        Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
    }
}

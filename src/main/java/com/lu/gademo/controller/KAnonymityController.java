package com.lu.gademo.controller;

import com.lu.gademo.utils.AlgorithmsFactory;
import com.lu.gademo.utils.Anonymity;
import com.lu.gademo.utils.DSObject;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@Controller
@RequestMapping("/KAnonymity")
public class KAnonymityController {
    private final String dir;

    private final Anonymity anonymity;
    private final AlgorithmsFactory algorithmsFactory;

    public KAnonymityController(Anonymity anonymity, AlgorithmsFactory algorithmsFactory) throws IOException {
        File directory = new File("");
        String currentPath = directory.getAbsolutePath();
        Path dirPath = Paths.get(currentPath, "templates");
        this.dir = Files.exists(dirPath) ? dirPath.toAbsolutePath().toString()
                : Files.createDirectories(dirPath).toAbsolutePath().toString();
        this.anonymity = anonymity;
        this.algorithmsFactory = algorithmsFactory;
    }

    @PostMapping("/KAnonymity")
    public ResponseEntity<?> kAnonymity(@RequestParam("csvFile") MultipartFile csvFile,
                                              @RequestParam Map<String, MultipartFile> templates,
                                              @RequestParam("params") String params,
                                              @RequestParam("attribute") String attribute) {
        try {
            System.out.println(templates);
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
                DSObject dsObject = new DSObject(Arrays.asList(baseName, dir, attribute));
                dsObject.setIntVal(templates.size() - 1);
//                String output = anonymity.service(dsObject, 1, params).getStringVal();
                String output = algorithmsFactory.getAlgorithmInfoFromId(61).execute(dsObject, params).getStringVal();
                byte[] fileContent = Files.readAllBytes(Paths.get(output));

                HttpHeaders headers = new HttpHeaders();
                headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + output);
                headers.add(HttpHeaders.CONTENT_TYPE, "application/octet-stream");

                return new ResponseEntity<>(fileContent, headers, HttpStatus.OK);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(org.springframework.http.MediaType.TEXT_PLAIN);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).headers(headers).body(e.getMessage());
        }
        return null;
    }

    @PostMapping("/LDiversity/Distinct")
    public ResponseEntity<?> LDistinctEntropy(@RequestParam("csvFile") MultipartFile csvFile,
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
                DSObject dsObject = new DSObject(Arrays.asList(baseName, dir, attribute));
//                String output = anonymity.service(dsObject, 7, params).getStringVal();
                String output = algorithmsFactory.getAlgorithmInfoFromId(62).execute(dsObject, params).getStringVal();
                byte[] fileContent = Files.readAllBytes(Paths.get(output));

                HttpHeaders headers = new HttpHeaders();
                headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + output);
                headers.add(HttpHeaders.CONTENT_TYPE, "application/octet-stream");

                return new ResponseEntity<>(fileContent, headers, HttpStatus.OK);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(org.springframework.http.MediaType.TEXT_PLAIN);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).headers(headers).body(e.getMessage());
        }
        return null;
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
                DSObject dsObject = new DSObject(Arrays.asList(baseName, dir, attribute));
//                String output = anonymity.service(dsObject, 8, params).getStringVal();
                String output = algorithmsFactory.getAlgorithmInfoFromId(63).execute(dsObject, params).getStringVal();
                byte[] fileContent = Files.readAllBytes(Paths.get(output));

                HttpHeaders headers = new HttpHeaders();
                headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + output);
                headers.add(HttpHeaders.CONTENT_TYPE, "application/octet-stream");

                return new ResponseEntity<>(fileContent, headers, HttpStatus.OK);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(org.springframework.http.MediaType.TEXT_PLAIN);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).headers(headers).body(e.getMessage());        }
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
                DSObject dsObject = new DSObject(Arrays.asList(baseName, dir, attribute));
//                String output = anonymity.service(dsObject, 9, params).getStringVal();
                String output = algorithmsFactory.getAlgorithmInfoFromId(64).execute(dsObject, params).getStringVal();
                byte[] fileContent = Files.readAllBytes(Paths.get(output));
                HttpHeaders headers = new HttpHeaders();
                headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + output);
                headers.add(HttpHeaders.CONTENT_TYPE, "application/octet-stream");

                return new ResponseEntity<>(fileContent, headers, HttpStatus.OK);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(org.springframework.http.MediaType.TEXT_PLAIN);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).headers(headers).body(e.getMessage());        }
        return null;
    }

    @PostMapping("/TCloseness")
    public ResponseEntity<?> tCloseness(@RequestParam("csvFile") MultipartFile csvFile,
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
                DSObject dsObject = new DSObject(Arrays.asList(baseName, dir, attribute));
//                String output = anonymity.service(dsObject, 10, params).getStringVal();
                String output = algorithmsFactory.getAlgorithmInfoFromId(65).execute(dsObject, params).getStringVal();
                byte[] fileContent = Files.readAllBytes(Paths.get(output));

                HttpHeaders headers = new HttpHeaders();
                headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + output);
                headers.add(HttpHeaders.CONTENT_TYPE, "application/octet-stream");

                return new ResponseEntity<>(fileContent, headers, HttpStatus.OK);
            }
        } catch (Exception e) {
            log.error(e.getMessage());

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(org.springframework.http.MediaType.TEXT_PLAIN);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).headers(headers).body(e.getMessage());
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

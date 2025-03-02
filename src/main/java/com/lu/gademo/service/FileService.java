package com.lu.gademo.service;

import com.lu.gademo.entity.FileStorageDetails;
import com.lu.gademo.event.ReDesensitizeEvent;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public interface FileService {
    /**
     * excel文件脱敏函数
     * param：
     * file 原始excel文件
     * params 脱敏参数
     */
    void redesenExcel(ReDesensitizeEvent event) throws Exception;
    void redesenSingleText(ReDesensitizeEvent event) throws Exception;
    void redesenGraph(ReDesensitizeEvent event) throws Exception;
    void redesenImage(ReDesensitizeEvent event) throws Exception;
    void redesenVideo(ReDesensitizeEvent event) throws Exception;
    void redesenAudio(ReDesensitizeEvent event) throws Exception;
    void redesenDocument(ReDesensitizeEvent event) throws Exception;

    List<Double> fetchRandomPayAmounts(int tableRecords, int totalRecords, int sampleTimes) throws InterruptedException, ExecutionException;

    FileStorageDetails generateTextTestFile(int totalNumber) throws IOException, ExecutionException, InterruptedException;

    ResponseEntity<byte[]> dealImage(FileStorageDetails fileStorageDetails, String params, String algName) throws IOException, ExecutionException, InterruptedException, TimeoutException;

    ResponseEntity<byte[]> dealImage(MultipartFile file, String params, String algName) throws IOException;

    ResponseEntity<byte[]> dealExcel(FileStorageDetails file, String params, String sheet, Boolean ifSaveExcel) throws IOException, ExecutionException, InterruptedException, TimeoutException;

    ResponseEntity<byte[]> dealExcel(MultipartFile file, String params, String sheet, Boolean ifSaveExcel) throws IOException;

    ResponseEntity<byte[]> dealVideo(FileStorageDetails fileStorageDetails, String params, String algName) throws IOException, ExecutionException, InterruptedException, TimeoutException;

    ResponseEntity<byte[]> dealVideo(MultipartFile file, String params, String algName) throws IOException, SQLException, InterruptedException;

    ResponseEntity<byte[]> dealAudio(FileStorageDetails fileStorageDetails, String params, String algName) throws IOException, ExecutionException, InterruptedException, TimeoutException;

    ResponseEntity<byte[]> dealAudio(MultipartFile file, String params, String algName) throws IOException, SQLException, InterruptedException;

    ResponseEntity<byte[]> dealGraph(FileStorageDetails fileStorageDetails, String params) throws IOException, ExecutionException, InterruptedException, TimeoutException;

    ResponseEntity<byte[]> dealGraph(MultipartFile file, String params) throws IOException, SQLException, InterruptedException;

//    ResponseEntity<byte[]> dealCsv(FileStorageDetails fileStorageDetails, String params, String algName) throws IOException, SQLException, InterruptedException, InvocationTargetException, IllegalAccessException, ClassNotFoundException, NoSuchMethodException;
//
//    ResponseEntity<byte[]> dealCsv(MultipartFile file, String params, String algName) throws IOException, SQLException, InterruptedException, InvocationTargetException, IllegalAccessException, ClassNotFoundException, NoSuchMethodException;

    ResponseEntity<byte[]> dealSingleColumnTextFile(FileStorageDetails fileStorageDetails, String params, String algName, boolean ifSkipFirstRow) throws IOException, ParseException, ExecutionException, InterruptedException, TimeoutException;

    ResponseEntity<byte[]> dealDocument(FileStorageDetails fileStorageDetails, String params, String algName) throws IOException, ParseException, ExecutionException, InterruptedException, TimeoutException;

    ResponseEntity<byte[]> dealSingleExcel(FileStorageDetails fileStorageDetails, String params, String algName) throws IOException, ParseException;

    ResponseEntity<byte[]> dealSingleExcel(MultipartFile file, String params, String algName) throws IOException, ParseException;

    ResponseEntity<byte[]> replaceVideoBackground(FileStorageDetails fileStorageDetails, String params, String algName, FileStorageDetails sheetStorageDetails) throws IOException, SQLException, InterruptedException, ExecutionException, TimeoutException;

    ResponseEntity<byte[]> replaceVideoBackground(MultipartFile file, String params, String algName, MultipartFile sheet) throws IOException, SQLException, InterruptedException;

    ResponseEntity<byte[]> replaceFace(FileStorageDetails fileStorageDetails, String params, String algName, FileStorageDetails sheetStorageDetails) throws IOException, ExecutionException, InterruptedException, TimeoutException;

    ResponseEntity<byte[]> replaceFace(MultipartFile file, String params, String algName, MultipartFile sheet) throws IOException, SQLException, InterruptedException;

    ResponseEntity<byte[]> replaceFaceVideo(FileStorageDetails fileStorageDetails, String params, String algName, FileStorageDetails sheetStorageDetails) throws IOException, ExecutionException, InterruptedException, TimeoutException;

    ResponseEntity<byte[]> replaceFaceVideo(MultipartFile file, String params, String algName, MultipartFile sheet) throws IOException;

    String desenText(String textInput, String textType, String privacyLevel, String algName) throws ParseException;
}

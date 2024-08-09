package com.lu.gademo.service;

import com.lu.gademo.entity.FileStorageDetails;
import com.lu.gademo.entity.ga.effectEva.RecEvaResultInv;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.concurrent.ExecutionException;

public interface FileService {
    /**
     * excel文件脱敏函数
     * param：
     * file 原始excel文件
     * params 脱敏参数
     */
    void redesenExcel(RecEvaResultInv recEvaResultInv) throws Exception;
    ResponseEntity<byte[]> dealImage(FileStorageDetails fileStorageDetails, String params, String algName) throws IOException;

    ResponseEntity<byte[]> dealImage(MultipartFile file, String params, String algName) throws IOException, SQLException, InterruptedException;

    ResponseEntity<byte[]> dealExcel(MultipartFile file, String params, String sheet, Boolean ifSaveExcel) throws IOException;

    ResponseEntity<byte[]> dealExcel(FileStorageDetails file, String params, String sheet, Boolean ifSaveExcel) throws IOException, SQLException, InterruptedException, InvocationTargetException, IllegalAccessException, ClassNotFoundException, NoSuchMethodException, ExecutionException;

    ResponseEntity<byte[]> dealVideo(MultipartFile file, String params, String algName) throws IOException, SQLException, InterruptedException;

    ResponseEntity<byte[]> dealAudio(MultipartFile file, String params, String algName, String sheet) throws IOException, SQLException, InterruptedException;

    ResponseEntity<byte[]> dealGraph(MultipartFile file, String params) throws IOException, SQLException, InterruptedException;

    ResponseEntity<byte[]> dealCsv(MultipartFile file, String params, String algName) throws IOException, SQLException, InterruptedException, InvocationTargetException, IllegalAccessException, ClassNotFoundException, NoSuchMethodException;

    ResponseEntity<byte[]> dealSingleExcel(MultipartFile file, String params, String algName) throws IOException, ParseException;

    ResponseEntity<byte[]> replaceVideoBackground(MultipartFile file, String params, String algName, MultipartFile sheet) throws IOException, SQLException, InterruptedException;

    ResponseEntity<byte[]> replaceFace(MultipartFile file, String params, String algName, MultipartFile sheet) throws IOException, SQLException, InterruptedException;

    ResponseEntity<byte[]> replaceFaceVideo(MultipartFile file, String params, String algName, MultipartFile sheet) throws IOException, SQLException, InterruptedException;

    String desenText(String textInput, String textType, String privacyLevel, String algName) throws ParseException;
}

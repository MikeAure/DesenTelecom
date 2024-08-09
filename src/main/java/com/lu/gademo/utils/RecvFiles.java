package com.lu.gademo.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lu.gademo.dao.ga.templateParam.onlineTaxi2ParamDao;
import com.lu.gademo.entity.ga.RecvFilesEntity.ExcelEntity;
import com.lu.gademo.entity.ga.templateParam.onlineTaxi2Param;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class RecvFiles {
    @Autowired
    @Lazy
    private onlineTaxi2ParamDao onlineTaxi2Dao;

    public static JsonNode recvJson(String url) throws Exception {
        HttpResponse<JsonNode> jsonResponse = null;
        try {
            jsonResponse = Unirest.get(url)
                    .header("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36")
                    .asJson();
        } catch (UnirestException e) {
            throw new RuntimeException(e);
        }
        return jsonResponse.getBody();
    }

    public static List<ExcelEntity> parseJsonToEntities(JsonNode responseBody) throws JsonProcessingException, JSONException {
        ObjectMapper mapper = new ObjectMapper();
        List<ExcelEntity> entities = new ArrayList<>();

        // Assuming the responseBody is an array
        if (responseBody.isArray()) {
            JSONArray jsonArray = responseBody.getArray();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject element = jsonArray.getJSONObject(i);
                entities.add(mapper.readValue(element.toString(), ExcelEntity.class));
            }

        }

        return entities;

    }

    public static void createExcelFile(List<ExcelEntity> entities, String filePath) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Data");
        // 根据
        HashMap<String, ArrayList<String>> columnMap = new HashMap<>();

        // 对每个键生成一个列表存储对应的infoContent
        if (!entities.isEmpty()) {
            for (ExcelEntity entity : entities) {
                columnMap.computeIfAbsent(entity.getAttributeName(), k -> new ArrayList<>()).add(entity.getInfoContent());
            }
        }
        int colIdx = 0;
        for (Map.Entry<String, ArrayList<String>> entry : columnMap.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
            Row headerRow = sheet.getRow(0);
            if (headerRow == null) {
                headerRow = sheet.createRow(0);
            }
            headerRow.createCell(colIdx).setCellValue(entry.getKey());

            int rowIdx = 1;
            for (String value : entry.getValue()) {

                Row row = sheet.getRow(rowIdx);
                if (row == null) {
                    row = sheet.createRow(rowIdx);
                }
                row.createCell(colIdx).setCellValue(value); // Write the value to the cell in the header row.
                rowIdx++;
            }
            colIdx++;
        }

        try (FileOutputStream outputStream = new FileOutputStream(filePath)) {
            workbook.write(outputStream);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        workbook.close();
    }

    public static void main(String[] args) throws Exception {
        RecvFiles recvFiles = new RecvFiles();
        recvFiles.updateTaxiParams();
    }

    public void updateTaxiParams() {
        onlineTaxi2Param columnNameFromTable = onlineTaxi2Dao.getOne(1);
//        for (onlineTaxi2Param name : columnNameFromTable) {
//            System.out.println(name);
//        }
        System.out.println(columnNameFromTable.getColumnName());

    }
}

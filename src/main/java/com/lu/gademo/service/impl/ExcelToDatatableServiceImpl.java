package com.lu.gademo.service.impl;

import com.lu.gademo.entity.FileStorageDetails;
import com.lu.gademo.entity.crm.CustomerDesenMsg;
import com.lu.gademo.entity.dataplatform.SadaGdpiClickDtl;
import com.lu.gademo.event.SaveExcelToDatabaseEvent;
import com.lu.gademo.service.CrmDesenService;
import com.lu.gademo.service.DataPlatformDesenService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.persistence.Column;
import java.io.FileInputStream;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class ExcelToDatatableServiceImpl {

    private CrmDesenService crmDesenService;
    private DataPlatformDesenService dataPlatformDesenService;

    @Autowired
    public ExcelToDatatableServiceImpl(CrmDesenService crmDesenService, DataPlatformDesenService dataPlatformDesenService) {
        this.crmDesenService = crmDesenService;
        this.dataPlatformDesenService = dataPlatformDesenService;
    }

    @EventListener
    public void saveToDatabaseAfterEvaluation(SaveExcelToDatabaseEvent saveExcelToDatabaseEvent) {
        CompletableFuture<ResponseEntity<byte[]>> futureResult = saveExcelToDatabaseEvent.getFutureResult();
        ResponseEntity<byte[]> responseEntity = saveExcelToDatabaseEvent.getResponseEntity();
        log.info("Saving to database");
        FileStorageDetails fileStorageDetails = saveExcelToDatabaseEvent.getFileStorageDetails();
        String entityClassName = saveExcelToDatabaseEvent.getEntityClassName();

        log.info("EntityName: {}", entityClassName);

        if (entityClassName.contains("sada_gdpi_click_dtl")) {
            try {
                exportSadaExcelToDatabase(fileStorageDetails.getDesenFilePathString(), entityClassName);
                log.info("{} 已存储到数据库中", entityClassName);
            } catch (Exception e) {
                HttpHeaders httpHeaders = new HttpHeaders();
                httpHeaders.add(HttpHeaders.CONTENT_TYPE, "text/plain");
                log.error("电子表格导出到数据库失败：{}", e.getMessage());
                String errorMsg = "电子表格导出到数据库失败";
                futureResult.complete(ResponseEntity.status(500).headers(httpHeaders).body(errorMsg.getBytes()));
            }
            // 保存数据库成功后再发送对前端的响应
            futureResult.complete(responseEntity);
        } else if (entityClassName.contains("customer_desen_msg")) {
            try {
                exportCustomerDesenMsgExcelToDatabase(fileStorageDetails.getDesenFilePathString(), entityClassName);
                log.info("{} 已存储到数据库中", entityClassName);
            } catch (Exception e) {
                HttpHeaders httpHeaders = new HttpHeaders();
                httpHeaders.add(HttpHeaders.CONTENT_TYPE, "text/plain");
                log.error("电子表格导出到数据库失败：{}", e.getMessage());
                String errorMsg = "电子表格导出到数据库失败";
                futureResult.complete(ResponseEntity.status(500).headers(httpHeaders).body(errorMsg.getBytes()));
            }
            // 保存数据库成功后再发送对前端的响应
            futureResult.complete(responseEntity);
        }
//        try {
////            exportExcelToDatabase(CustomerDesenMsg.class, fileStorageDetails.getDesenFilePathString());
//            exportCustomerDesenMsgExcelToDatabase(fileStorageDetails.getDesenFilePathString(), entityClassName);
//            log.info("{} 已存储到数据库中", entityClassName);
//
//        } catch (Exception e) {
//            HttpHeaders httpHeaders = new HttpHeaders();
//            httpHeaders.add(HttpHeaders.CONTENT_TYPE, "text/plain");
//            log.error("电子表格导出到数据库失败：{}", e.getMessage());
//            String errorMsg = "电子表格导出到数据库失败";
//            futureResult.complete(ResponseEntity.status(500).headers(httpHeaders).body(errorMsg.getBytes()));
//        }
//        // 保存数据库成功后再发送对前端的响应
//        futureResult.complete(responseEntity);
    }

//    /**
//     * 获取用于表示CustomerDesenMsg数据表不同脱敏策略的实体类
//     * @param entityName 实体类名称，根据其中的部分字符串确定脱敏策略的种类
//     * @return 所选策略对应的CustomerDesenMsg实体类
//     */
//    private Class<?> getEntityByName(String entityName) {
//        if (entityName.contains("Low")) {
//            return CustomerDesenMsgLow.class;
//        } else if (entityName.contains("Medium")) {
//            return CustomerDesenMsgMedium.class;
//        } else {
//            return CustomerDesenMsgHigh.class;
//        }
//    }

//    public <T, ID> JpaRepository<T, ID> getRepository(Class<T> entityClass) {
//        String repositoryName = entityClass.getSimpleName() + "Dao";
//        repositoryName = Character.toLowerCase(repositoryName.charAt(0)) + repositoryName.substring(1);
//        try {
//            return (JpaRepository<T, ID>) applicationContext.getBean(repositoryName);
//        } catch (Exception e) {
//            log.error("Failed to get repository: {}", e.getMessage());
//        }
//        return null;
//    }

//    /**
//     * 从Excel导出数据到数据库
//     *
//     * @param entityClass 数据库表对应的JPA实体类
//     * @param filePath    Excel文件字符串
//     * @param <T>         泛型类型
//     */
//    public <T> void exportExcelToDatabase(Class<T> entityClass, String filePath) throws Exception {
//        try (FileInputStream fis = new FileInputStream(filePath);
//             Workbook workbook = new XSSFWorkbook(fis)) {
//
//            Sheet sheet = workbook.getSheetAt(0);
//            List<T> entities = new ArrayList<>();
//
//            // 获取列名到字段的映射
//            Map<String, Field> fieldMap = getFieldMap(entityClass);
//
//            // 解析Excel数据
//            int rowCount = 0;
//            for (Row row : sheet) {
//                if (rowCount++ == 0) {
//                    continue; // Skip header row
//                }
//                T entity = entityClass.getDeclaredConstructor().newInstance();
//
//                for (Cell cell : row) {
//                    String columnName = sheet.getRow(0).getCell(cell.getColumnIndex()).getStringCellValue();
//                    Field field = fieldMap.get(columnName);
//                    if (field != null) {
//                        field.setAccessible(true);
//                        Object value = getCellValueAsType(cell, field.getType());
//                        field.set(entity, value);
//                    }
//                }
//                entities.add(entity);
//            }
//
//            // 获取对应的repository
//            JpaRepository<T, ?> repository = getRepository(entityClass);
//            repository.deleteAll();
//            repository.saveAll(entities);
//
//        } catch (Exception e) {
//            log.error("将Excel文件存储到数据库中出现错误");
//            throw new Exception(e);
//        }
//    }

    // 使用MyBatis做数据持久化
    public void exportCustomerDesenMsgExcelToDatabase(String filePath, String tableName) throws Exception {
        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);
            List<CustomerDesenMsg> entities = new ArrayList<>();

            // 获取列名到字段的映射
            Map<String, Field> fieldMap = getFieldMap(CustomerDesenMsg.class);
            // 解析Excel数据
            int rowCount = 0;
            for (Row row : sheet) {
                if (rowCount++ == 0) {
                    continue; // Skip header row
                }
                CustomerDesenMsg entity = new CustomerDesenMsg();

                for (Cell cell : row) {
                    String columnName = sheet.getRow(0).getCell(cell.getColumnIndex()).getStringCellValue();
                    Field field = fieldMap.get(columnName);
                    if (field != null) {
                        field.setAccessible(true);
                        Object value = getCellValueAsType(cell, field.getType());
                        field.set(entity, value);
                    }
                }
                entities.add(entity);
            }
            // 获取对应的repository
            crmDesenService.deleteAndInsert(tableName, entities);

        } catch (Exception e) {
            log.error("将Excel文件存储到数据库中出现错误");
            throw new Exception(e);
        }
    }

    public void exportSadaExcelToDatabase(String filePath, String tableName) throws Exception {
        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);
            List<SadaGdpiClickDtl> entities = new ArrayList<>();

            // 获取列名到字段的映射
            Map<String, Field> fieldMap = getFieldMap(SadaGdpiClickDtl.class);
            // 解析Excel数据
            int rowCount = 0;
            for (Row row : sheet) {
                if (rowCount++ == 0) {
                    continue; // Skip header row
                }
                SadaGdpiClickDtl entity = new SadaGdpiClickDtl();

                for (Cell cell : row) {
                    String columnName = sheet.getRow(0).getCell(cell.getColumnIndex()).getStringCellValue();
                    Field field = fieldMap.get(columnName);
                    if (field != null) {
                        field.setAccessible(true);
                        Object value = getCellValueAsType(cell, field.getType());
                        field.set(entity, value);
                    }
                }
                entities.add(entity);
            }
            // 获取对应的repository
            log.info("正在保存的表名为：{}", tableName);
            dataPlatformDesenService.deleteAndInsert(tableName, entities);

        } catch (Exception e) {
            log.error("将Excel文件存储到数据库中出现错误");
            throw new Exception(e);
        }
    }

    // 使用MyBatis做数据持久化
//    public <T> void exportExcelToDatabase(String filePath, String tableName, List<T> entities, Class<T> clazz) throws Exception {
//        try (FileInputStream fis = new FileInputStream(filePath);
//             Workbook workbook = new XSSFWorkbook(fis)) {
//
//            Sheet sheet = workbook.getSheetAt(0);
//
//            // 获取列名到字段的映射
//            Map<String, Field> fieldMap = getFieldMap(CustomerDesenMsg.class);
//            // 解析Excel数据
//            int rowCount = 0;
//            for (Row row : sheet) {
//                if (rowCount++ == 0) {
//                    continue; // Skip header row
//                }
//                T entity = clazz.getDeclaredConstructor().newInstance();
//
//                for (Cell cell : row) {
//                    String columnName = sheet.getRow(0).getCell(cell.getColumnIndex()).getStringCellValue();
//                    Field field = fieldMap.get(columnName);
//                    if (field != null) {
//                        field.setAccessible(true);
//                        Object value = getCellValueAsType(cell, field.getType());
//                        field.set(entity, value);
//                    }
//                }
//                entities.add(entity);
//            }
//            // 获取对应的repository
//            crmDesenService.deleteAndInsert(tableName, entities);
//
//        } catch (Exception e) {
//            log.error("将Excel文件存储到数据库中出现错误");
//            throw new Exception(e);
//        }
//    }

    private <T> Map<String, Field> getFieldMap(Class<T> entityClass) {
        Map<String, Field> fieldMap = new HashMap<>();
        for (Field field : entityClass.getDeclaredFields()) {
            if (field.isAnnotationPresent(Column.class)) {
                Column column = field.getAnnotation(Column.class);
                fieldMap.put(column.name(), field);
            }
        }
        return fieldMap;
    }

//    private <T> Map<String, Field> getFieldMap(Map<String, String> fieldMap, Class<T> entityClass) {
//
//        Map<String, Field> fieldMapResult = new HashMap<>();
//        for (Field field : entityClass.getDeclaredFields()) {
//            if (field.isAnnotationPresent(Column.class)) {
//                Column column = field.getAnnotation(Column.class);
//                fieldMapResult.put(column.name(), field);
//            }
//        }
//        return fieldMapResult;
//    }

    /**
     * 获取单元格的值并转换为指定类型
     *
     * @param cell 电子表格中的单元格
     * @param type 实体类中对应字段的类型
     * @return 返回单元格的数据
     */
    private Object getCellValueAsType(Cell cell, Class<?> type) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        if (type == String.class) {
            return cell.getCellType() == CellType.STRING ? cell.getStringCellValue() : cell.toString();
        } else if (type == LocalDateTime.class) {
            if (cell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell)) {
                return cell.getLocalDateTimeCellValue();
            } else {
                return LocalDateTime.parse(cell.getStringCellValue(), formatter);
            }
        } else if (type == int.class || type == Integer.class) {
            return cell.getCellType() == CellType.STRING ? Integer.parseInt(cell.getStringCellValue()) : (int) cell.getNumericCellValue();
        } else if (type == long.class || type == Long.class) {
            return cell.getCellType() == CellType.STRING ? Long.parseLong(cell.getStringCellValue()) : (long) cell.getNumericCellValue();
        } else if (type == double.class || type == Double.class) {
            return cell.getCellType() == CellType.STRING ? Double.parseDouble(cell.getStringCellValue()) : cell.getNumericCellValue();
        } else if (type == boolean.class || type == Boolean.class) {
            return cell.getBooleanCellValue();
        }
        return null;
    }
}

package com.lu.gademo.service.impl;

import com.lu.gademo.entity.FileStorageDetails;
import com.lu.gademo.entity.crm.CustomerDesenMsgHigh;
import com.lu.gademo.entity.crm.CustomerDesenMsgLow;
import com.lu.gademo.entity.crm.CustomerDesenMsgMedium;
import com.lu.gademo.event.SaveExcelToDatabaseEvent;
import javafx.util.StringConverter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import javax.persistence.Column;
import java.io.FileInputStream;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j

public class ExcelToDatatableServiceImpl {
    @Autowired
    private ApplicationContext applicationContext;

    @EventListener
    public <T> void saveToDatabaseAfterEvaluation(SaveExcelToDatabaseEvent saveExcelToDatabaseEvent) {
        log.info("Saving to database");
        FileStorageDetails fileStorageDetails = saveExcelToDatabaseEvent.getFileStorageDetails();
        String entityClassName = saveExcelToDatabaseEvent.getEntityClassName();
        String[] entityNameList = entityClassName.split("_");
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < entityNameList.length; i++) {
            if (i == 0) {
                builder.append(entityNameList[i]);
            } else {
                builder.append(StringUtils.capitalize(entityNameList[i]));
            }
        }
        String entityName = builder.toString();
        log.info("EntityName: {}", entityName);
        log.info("DesenFilePathString: {}", fileStorageDetails.getDesenFilePathString());

        importExcelToDatabase(getEntityByName(entityName), fileStorageDetails.getDesenFilePathString());
    }

    private  Class<?> getEntityByName(String entityName) {
        if (entityName.contains("Low")) {
            return CustomerDesenMsgLow.class;
        } else if (entityName.contains("Medium")) {
            return CustomerDesenMsgMedium.class;
        } else{
            return CustomerDesenMsgHigh.class;
        }
    }

    public <T, ID> JpaRepository<T, ID> getRepository(Class<T> entityClass) {
        String repositoryName = entityClass.getSimpleName() + "Dao";
        repositoryName = Character.toLowerCase(repositoryName.charAt(0)) + repositoryName.substring(1);
        try {
            return (JpaRepository<T, ID>) applicationContext.getBean(repositoryName);
        } catch (Exception e) {
            log.error("Failed to get repository: {}", e.getMessage());
        }
        return null;
    }

    /**
     * 从Excel导入数据到数据库
     * @param entityClass 数据库表对应的JPA实体类
     * @param filePath Excel文件字符串
     * @param <T> 泛型类型
     */
    public <T> void importExcelToDatabase(Class<T> entityClass, String filePath) {
        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);
            List<T> entities = new ArrayList<>();

            // 获取列名到字段的映射
            Map<String, Field> fieldMap = getFieldMap(entityClass);

            // 解析Excel数据
            int rowCount = 0;
            for (Row row : sheet) {
                if (rowCount++ == 0) {
                    continue; // Skip header row
                }
                T entity = entityClass.getDeclaredConstructor().newInstance();

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
            JpaRepository<T, ?> repository = getRepository(entityClass);
            repository.deleteAll();
            repository.saveAll(entities);

        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

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

    /**
     * 获取单元格的值并转换为指定类型
     * @param cell 电子表格中的单元格
     * @param type 实体类中对应字段的类型
     * @return 返回单元格的数据
     */
    private Object getCellValueAsType(Cell cell, Class<?> type) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        if (type == String.class) {
            return cell.getCellType() == CellType.STRING ? cell.getStringCellValue() : cell.toString();
        } else if (type == LocalDateTime.class) {
            if (cell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell)) {
                return cell.getLocalDateTimeCellValue();
            } else {
                LocalDate parsed = LocalDate.parse(cell.getStringCellValue(), formatter);
                return parsed.atStartOfDay();
            }
        } else if (type == int.class || type == Integer.class) {
            return (int) cell.getNumericCellValue();
        } else if (type == long.class || type == Long.class) {
            return (long) cell.getNumericCellValue();
        } else if (type == double.class || type == Double.class) {
            return cell.getNumericCellValue();
        } else if (type == boolean.class || type == Boolean.class) {
            return cell.getBooleanCellValue();
        }
        return null;
    }
}

package com.lu.gademo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lu.gademo.entity.FileStorageDetails;
import com.lu.gademo.entity.crm.CustomerDesenMsg;
import com.lu.gademo.entity.crm.CustomerDesenMsgLow;
import com.lu.gademo.entity.crm.CustomerMsg;
import com.lu.gademo.service.ExcelParamService;
import com.lu.gademo.service.FileService;
import com.lu.gademo.service.impl.FileStorageService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.FileInputStream;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Path;

import javax.persistence.Column;
import java.lang.reflect.Field;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
public class ExportDatatableToExcelTest {
    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private FileService fileService;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private ExcelParamService excelParamService;

    private ObjectMapper objectMapper = new ObjectMapper();

    public <T, ID> JpaRepository<T, ID> getRepository(Class<T> entityClass) {
        String repositoryName = entityClass.getSimpleName() + "Dao";
        repositoryName = Character.toLowerCase(repositoryName.charAt(0)) + repositoryName.substring(1);
        return (JpaRepository<T, ID>) applicationContext.getBean(repositoryName);
    }

    public <T> Path exportToExcel(Class<T> entityClass, String filePath) {
        JpaRepository<T, ?> repository = getRepository(entityClass);
        List<T> entities = repository.findAll();

        try (Workbook workbook = new XSSFWorkbook();
            FileOutputStream fos = new FileOutputStream(filePath)) {
            Sheet sheet = workbook.createSheet(entityClass.getSimpleName());
            createHeaderRow(entityClass, sheet);

            int rowCount = 0;

            for (T entity : entities) {
                Row row = sheet.createRow(++rowCount);
                writeEntityToRow(entity, row);
            }

            workbook.write(fos);
            return Paths.get(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private <T> void createHeaderRow(Class<T> entityClass, Sheet sheet) {
        Row headerRow = sheet.createRow(0);
        Field[] fields = entityClass.getDeclaredFields();

        int colCount = 0;
        for (Field field : fields) {
            if (field.isAnnotationPresent(Column.class)) {
                Column column = field.getAnnotation(Column.class);
                String columnName = column.name();
                Cell cell = headerRow.createCell(colCount++);
                cell.setCellValue(columnName);
            }
        }
    }

    private <T> void writeEntityToRow(T entity, Row row) {
        Field[] fields = entity.getClass().getDeclaredFields();

        int colCount = 0;
        for (Field field : fields) {
            if (field.isAnnotationPresent(Column.class)) {
                field.setAccessible(true);
                try {
                    Object value = field.get(entity);
                    Cell cell = row.createCell(colCount++);
                    if (value != null) {
                        if (value instanceof String) {
                            cell.setCellValue((String) value);
                        } else if (value instanceof Number) {
                            if (value instanceof Long) {
                                cell.setCellValue(((Number) value).longValue());
                            }
                            else if (value instanceof Integer) {
                                cell.setCellValue(((Number) value).intValue());
                            }
                            else if (value instanceof Double) {
                                cell.setCellValue(((Number) value).doubleValue());
                            }
                            else if (value instanceof Float) {
                                cell.setCellValue(((Number) value).floatValue());
                            }
                            else if (value instanceof Short) {
                                cell.setCellValue(((Number) value).shortValue());
                            }
                            else if (value instanceof Byte) {
                                cell.setCellValue(((Number) value).byteValue());
                            }
                        } else if (value instanceof LocalDateTime) {
                            cell.setCellValue(value.toString());
                        } else {
                            cell.setCellValue(value.toString());
                        }
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

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
            e.printStackTrace();
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

    @Test
    void writeExcel() {
        exportToExcel(CustomerMsg.class, "customerMsgReflect2.xlsx");
    }

//    @Test
//    void testDatatableToExcel() throws IOException, SQLException, InterruptedException, ClassNotFoundException, InvocationTargetException, IllegalAccessException, NoSuchMethodException {
//        String sheetName = "telecomdatatable_param";
//        Path rawFilePath = exportToExcel(CustomerMsg.class, "customerMsgReflect2.xlsx");
//        FileStorageDetails datatableExcelStorageDetails = fileStorageService.getRawFileStorageDetails(rawFilePath);
//        System.out.println(datatableExcelStorageDetails.getRawFileName());
//        System.out.println(datatableExcelStorageDetails.getDesenFileName());
//        // 模拟请求文件脱敏配置
//        String desenParams = objectMapper.writeValueAsString(excelParamService.getParamsByTableName(sheetName));
//        System.out.println(desenParams);
//        fileService.dealExcel(datatableExcelStorageDetails, desenParams, sheetName, false);
//
//    }

    @Test
    void testExcelToDatatable() {
        Path desenFilePath = Paths.get("desen_files").resolve("1723121007221_customerMsgReflect2_1723128149665.xlsx");
        importExcelToDatabase(CustomerDesenMsgLow.class, desenFilePath.toString());

    }

}

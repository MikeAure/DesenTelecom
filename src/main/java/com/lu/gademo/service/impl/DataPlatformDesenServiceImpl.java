package com.lu.gademo.service.impl;

import com.lu.gademo.entity.dataplatform.SadaGdpiClickDtl;
import com.lu.gademo.mapper.dataplatform.SadaGdpiClickDtlParamDao;
import com.lu.gademo.mapper.userlog.SadaGdpiClickDtlParamUserLogDao;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@Getter
public class DataPlatformDesenServiceImpl {
    private SadaGdpiClickDtlParamUserLogDao sadaGdpiClickDtlParamUserLogDao;
    private SadaGdpiClickDtlParamDao sadaDtlDao;

    private Map<String, String> columnMapping = new LinkedHashMap<>();

    @Autowired
    public DataPlatformDesenServiceImpl(SadaGdpiClickDtlParamDao sadaGdpiClickDtlParamDao,
                                        SadaGdpiClickDtlParamUserLogDao sadaGdpiClickDtlParamUserLogDao) {
        this.sadaDtlDao = sadaGdpiClickDtlParamDao;
        this.sadaGdpiClickDtlParamUserLogDao = sadaGdpiClickDtlParamUserLogDao;
        columnMapping.put("sid", "sid");
        columnMapping.put("f_srcip", "fSrcip");
        columnMapping.put("f_ad", "fAd");
        columnMapping.put("f_ts", "fTs");
        columnMapping.put("f_url", "fUrl");
        columnMapping.put("f_ref", "fRef");
        columnMapping.put("f_ua", "fUa");
        columnMapping.put("f_dstip", "fDstip");
        columnMapping.put("f_cookie", "fCookie");
        columnMapping.put("f_src_port", "fSrcPort");
        columnMapping.put("f_json", "fJson");
        columnMapping.put("f_update_time", "fUpdateTime");
        columnMapping.put("f_dataid", "fDataid");
    }

    public int deleteById(String tableName, Long id) {
        return sadaGdpiClickDtlParamUserLogDao.deleteById(tableName, id);
    }

    public int deleteAll(String tableName) {
        return sadaGdpiClickDtlParamUserLogDao.deleteAll(tableName);
    }

    // 批量插入
    public void insertListBatch(String tableName, List<SadaGdpiClickDtl> list, int batchSize) {
        for (int i = 0; i < list.size(); i += batchSize) {
            List<SadaGdpiClickDtl> batchList = list.subList(i, Math.min(i + batchSize, list.size()));
            insertList(tableName, batchList);
        }
    }

    //    @Transactional(transactionManager = "dataPlatformMybatisTransactionManager")
//    public void deleteAndInsert(String tableName, List<SadaGdpiClickDtl> list) {
//        if (sadaDtlDao.getItemTotalNumberByTabelName(tableName) > 0) {
//            sadaDtlDao.deleteAll(tableName);
//        }
//        insertListBatch(tableName, list, 1000);
//    }
    // 删除与插入
    @Transactional(transactionManager = "dataPlatformMybatisTransactionManager")
    public void deleteAndInsert(String tableName, List<SadaGdpiClickDtl> list) {
        if (sadaGdpiClickDtlParamUserLogDao.getItemTotalNumberByTabelName(tableName) > 0) {
            sadaGdpiClickDtlParamUserLogDao.deleteAll(tableName);
        }
        insertListBatch(tableName, list, 1000);
    }

    // 使用指向另一个数据的Dao进行插入
    public int insertList(String tableName, List<SadaGdpiClickDtl> list) {
        return sadaGdpiClickDtlParamUserLogDao.insertList(tableName, list);
    }

    // 从dtl中读取记录
    public List<SadaGdpiClickDtl> getAllRecordsByTableName(String tableName) {
        return sadaDtlDao.getAllRecordsByTableName(tableName);
    }

    public void writeToExcel(List<SadaGdpiClickDtl> dataList, Map<String, String> columnMapping, Path filePath) throws IOException, IllegalAccessException, FileNotFoundException {
        // 创建一个新的工作簿
        Workbook workbook = new XSSFWorkbook();
        // 创建一个新的工作表
        Sheet sheet = workbook.createSheet("Data");
        // 创建表头行
        Row headerRow = sheet.createRow(0);

        // 写入数据库列名作为表头
        int colIndex = 0;
        for (String dbColumn : columnMapping.keySet()) {
            Cell cell = headerRow.createCell(colIndex++);
            cell.setCellValue(dbColumn);  // 使用数据库列名
        }

        // 写入数据行
        int rowIndex = 1;
        for (SadaGdpiClickDtl entity : dataList) {
            Row row = sheet.createRow(rowIndex++);

            colIndex = 0;
            for (String fieldName : columnMapping.values()) {
                Cell cell = row.createCell(colIndex++);
                Field field = getFieldByName(entity, fieldName);
                if (field != null) {
                    field.setAccessible(true);
                    Object value = field.get(entity);

                    if (value != null) {
                        // 格式化LocalDateTime
                        if (value instanceof LocalDateTime) {
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                            cell.setCellValue(((LocalDateTime) value).format(formatter));
                        } else {
                            cell.setCellValue(value.toString());
                        }
                    }
                }
            }
        }

        // 写入 Excel 文件
        try (FileOutputStream fileOut = new FileOutputStream(filePath.toAbsolutePath().toString())) {
            log.info(filePath.toAbsolutePath().toString());
            workbook.write(fileOut);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 关闭工作簿
        workbook.close();
    }

    // 动态获取字段
    private Field getFieldByName(Object obj, String fieldName) {
        try {
            return obj.getClass().getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return null;
    }
}

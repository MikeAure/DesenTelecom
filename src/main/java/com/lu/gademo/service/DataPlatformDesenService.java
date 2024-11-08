package com.lu.gademo.service;

import com.lu.gademo.entity.dataplatform.SadaGdpiClickDtl;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public interface DataPlatformDesenService {
    int deleteById(String tableName, Long id);

    int deleteAll(String tableName);

    // 批量插入
    void insertListInBatch(String tableName, List<SadaGdpiClickDtl> list, int batchSize);

    //    @Transactional(transactionManager = "dataPlatformMybatisTransactionManager")
//    public void deleteAndInsert(String tableName, List<SadaGdpiClickDtl> list) {
//        if (sadaDtlDao.getItemTotalNumberByTabelName(tableName) > 0) {
//            sadaDtlDao.deleteAll(tableName);
//        }
//        insertListBatch(tableName, list, 1000);
//    }
    // 删除与插入
    @Transactional(transactionManager = "dataPlatformMybatisTransactionManager")
    void deleteAndInsert(String tableName, List<SadaGdpiClickDtl> list);

    // 使用指向另一个数据的Dao进行插入
    int insertList(String tableName, List<SadaGdpiClickDtl> list);

    // 从dtl中读取记录
    List<SadaGdpiClickDtl> getAllRecordsByTableName(String tableName);

    void writeToExcel(List<SadaGdpiClickDtl> dataList, Map<String, String> columnMapping, Path filePath) throws IOException, IllegalAccessException, FileNotFoundException;

    com.lu.gademo.mapper.userlog.SadaGdpiClickDtlParamUserLogDao getSadaGdpiClickDtlParamUserLogDao();

    com.lu.gademo.mapper.dataplatform.SadaGdpiClickDtlParamDao getSadaDtlDao();

    Map<String, String> getColumnMapping();
}

package com.lu.gademo.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.apache.poi.ss.usermodel.Sheet;
import org.bouncycastle.crypto.CryptoException;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface Util {

    //获取ip地址
    String getIP();

    //SHA256哈希
    String getSHA256Hash(String input);

    //获取时间
    String getTime();

    // SM3哈希
    String getSM3Hash(byte[] input);

    // 构建数据库并插入数据
    void createDB(Sheet sheet, List<Integer> dataType, String tableName) throws IOException, SQLException;

    // SM2签名
    String getSM2Sign(byte[] input) throws Exception;

    // 系统类型
    boolean isLinux();

    void saveExcel(String tableName, String fileName);

    //将对象序列化为字节数组
    <T> byte[] serializeToByteArray(List<T> list);

    // 字节数组转字符串
    String convertToString(byte[] byteArray);

    // 字符串转字节数组
    byte[] convertToByteArray(String byteString);

    // 反序列化
    <T extends Serializable> List<T> convertToList(byte[] byteArray);

    /**
     * @param sheet      sheet
     * @param lastRowNum 行数
     * @param i          第i列
     * @param datas      脱敏数据
     * @param <T>
     */
    <T> void write2Excel(Sheet sheet, int lastRowNum, int i, List<T> datas);
    <T> void write2Excel(Sheet originalSheet, Sheet newSheet, int lastRowNum, int columnIndex, List<T> datas);

    public void copySheet(Sheet sourceSheet, Sheet targetSheet);

    /**
     * @param inputStream 输入流
     * @return 对应字符串
     */
    String inputStreamToString(InputStream inputStream);

    void mySqlDump(String tableName);

//    public void excelToMysql(String tableName, String excelFilePath);
    // 检测本机是否安装Conda
    Boolean isCondaInstalled(boolean isLinux);

    ArrayNode trimCommaAndReturnArrayNode(String rawString, ObjectMapper objectMapper);

    String convertTxtToShp(String shapefilePath);

    List<String> converShpToTxt(String shapefilePath) throws IOException;

    Map<String, List<String>> groupByLineNumber(List<String> data);
    String formatData(Map<String, List<String>> groupedData);
    void saveToFile(String data, String filePath);
}

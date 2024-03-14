package com.lu.gademo.utils;

import org.apache.poi.ss.usermodel.Sheet;

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

public interface Util {

    //获取ip地址
    public String getIP();

    //SHA256哈希
    public String getSHA256Hash(String input);
    //获取时间
    public  String getTime();

    // SM3哈希
    public String getSM3Hash(byte[] input);

    // 构建数据库并插入数据
    public void createDB(Sheet sheet, List<Integer> dataType, String tableName) throws IOException, SQLException;
    // SM3签名
    public String sm2Sign(byte[] input) throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException, SignatureException, InvalidKeySpecException;
    // 系统类型
    public boolean isLinux();

    public void saveExcel(String tableName, String fileName);

    //将对象序列化为字节数组
    public <T> byte[] serializeToByteArray(List<T> list);

    // 字节数组转字符串
    public String convertToString(byte[] byteArray);

    // 字符串转字节数组
    public byte[] convertToByteArray(String byteString);

    // 反序列化
    public <T extends Serializable> List<T> convertToList(byte[] byteArray);
    /**
     *
     * @param sheet sheet
     * @param lastRowNum 行数
     * @param i 第i列
     * @param datas 脱敏数据
     * @param <T>
     */
    public <T> void write2Excel(Sheet sheet, int lastRowNum, int i, List<T> datas);

    /**
     *
     * @param inputStream 输入流
     * @return 对应字符串
     */
    public  String inputStreamToString(InputStream inputStream);

    public  void myssqlDump(String tableName);

    public void excelToMysql(String tableName, String excelFilePath);
}

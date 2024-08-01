package com.lu.gademo.model;

import lombok.extern.slf4j.Slf4j;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

@Slf4j
// 用于构建向拆分重构系统发送的Tcp数据包
public class TcpPacketSplit {
    // 版本号
    private static final short VERSION = 0x0001;
    private static final byte ENCRYPTION_MODE = 0x00;
    // 认证校验模式
    private static final byte AUTHENTICATION_MODE = 0x00;
    // 保留字段
    private static final int RESERVED = 0;
    // 命令类别
    private short MAIN_CMD = 0x02;
    private String jsonData = "";
    private byte[] authenticationAndValidationField = new byte[16];

    /**
     * 构造函数，传入数据
     * @param jsonData 字符串形式的json数据
     * @param MAIN_CMD 主命令码
     *
     *
     */
    public TcpPacketSplit(short MAIN_CMD, String jsonData) {
        this.MAIN_CMD = MAIN_CMD;
        this.jsonData = jsonData;
        this.authenticationAndValidationField = new byte[16];

    }

    public TcpPacketSplit(String jsonData) {
        this.jsonData = jsonData;
        this.authenticationAndValidationField = new byte[16];
    }

    /**
     * 构建数据包，
     *
     * @return 数据包字节数组
     */
    public byte[] buildPacket(byte[] jsonDataBytes, byte[] fileBytes){
        ByteBuffer packet = ByteBuffer.allocate(jsonDataBytes.length + 34 + 12 + fileBytes.length);
        // 版本号
        packet.putShort(VERSION);
        // 命令类别
        packet.putShort(this.MAIN_CMD);
        // 加密模式
        packet.put(ENCRYPTION_MODE);
        // 认证与校验模式
        packet.put(AUTHENTICATION_MODE);
        // 保留字段
        packet.putInt(RESERVED);
        // 数据包长度
        packet.putLong(packet.capacity());
        packet.putInt(jsonDataBytes.length);
        packet.putLong(fileBytes.length);
        packet.put(jsonDataBytes);
        packet.put(fileBytes);
        packet.put(authenticationAndValidationField);

        log.info("数据包长度: {}", packet.capacity());
        log.info("Json数据字节数: {}", jsonDataBytes.length);
        log.info("文件数据字节数: {}", fileBytes.length);

        return packet.array();
    }

    /**
     * 构建数据包，
     * @param fileBytes 文件字节数组
     * @return 数据包字节数组
     */

    public byte[] buildPacket(byte[] fileBytes) {
        // 数据域
        byte[] jsonDataBytes = jsonData.getBytes(StandardCharsets.UTF_8);
        //byte[] packet = new byte[packetLength];
        ByteBuffer packet = ByteBuffer.allocate(jsonDataBytes.length + 34 + 12 + fileBytes.length);
        // 版本号
        packet.putShort(VERSION);
        // 命令类别
        packet.putShort(this.MAIN_CMD);
        // 加密模式
        packet.put(ENCRYPTION_MODE);
        // 认证与校验模式
        packet.put(AUTHENTICATION_MODE);
        // 保留字段
        packet.putInt(RESERVED);
        // 数据包长度
        packet.putLong(packet.capacity());
        packet.putInt(jsonDataBytes.length);
        packet.putLong(fileBytes.length);
        packet.put(jsonDataBytes);
        packet.put(fileBytes);
        packet.put(authenticationAndValidationField);

        log.info("数据包长度: {}", packet.capacity());
        log.info("Json数据字节数: {}", jsonDataBytes.length);
        log.info("文件数据字节数: {}", fileBytes.length);

        return packet.array();
    }


    public int getCOMMAND_CATEGORY() {
        return MAIN_CMD;
    }

    public void setCOMMAND_CATEGORY(short COMMAND_CATEGORY) {
        this.MAIN_CMD = COMMAND_CATEGORY;
    }

    public String getJsonData() {
        return jsonData;
    }

    public void setJsonData(String jsonData) {
        this.jsonData = jsonData;
    }

    public byte[] getAuthenticationAndValidationField() {
        return authenticationAndValidationField;
    }

    public void setAuthenticationAndValidationField(byte[] authenticationAndValidationField) {
        this.authenticationAndValidationField = authenticationAndValidationField;
    }
}

package com.lu.gademo.model;

import lombok.extern.slf4j.Slf4j;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
@Slf4j
public class TcpPacket {
    //
    private static final short VERSION = 0x0001;
    private static final byte ENCRYPTION_MODE = 0x00;
    private static final byte AUTHENTICATION_MODE = 0x00;
    private static final int RESERVED = 0;
    private short MAIN_CMD;
    private short SUB_CMD;
    private short MESSAGE_VERSION;
    private int packetLength;
    private String jsonData;
    private byte[] authenticationAndValidationField;

    /**
     * 构造函数，传入数据
     * @param jsonData 字符串形式的json数据
     * @param MAIN_CMD 主命令码
     *
     *
     */
    public TcpPacket(String jsonData, short MAIN_CMD, short SUB_CMD, short MESSAGE_VERSION) {
        this.MAIN_CMD = MAIN_CMD;
        this.SUB_CMD = SUB_CMD;
        this.MESSAGE_VERSION = MESSAGE_VERSION;
        this.jsonData = jsonData;
        this.packetLength = jsonData.getBytes(StandardCharsets.UTF_8).length + 16 + 18;
        this.authenticationAndValidationField = new byte[16];

    }

    public TcpPacket(String jsonData) {
        this.jsonData = jsonData;
        this.packetLength = jsonData.getBytes(StandardCharsets.UTF_8).length + 16 + 18;
        this.authenticationAndValidationField = new byte[16];
    }

    /**
     * 构建数据包，
     *
     * @return 数据包字节数组
     */
    public byte[] buildPacket() throws UnsupportedEncodingException {
        // 数据域
        byte[] jsonDataBytes = jsonData.getBytes(StandardCharsets.UTF_8);
        //byte[] packet = new byte[packetLength];
        byte[] packet = new byte[jsonDataBytes.length + 34];
        // Set header
        packet[0] = (byte) ((VERSION >> 8) & 0xFF);
        packet[1] = (byte) (VERSION & 0xFF);
        packet[2] = (byte) ((MAIN_CMD >> 8) & 0xFF);
        packet[3] = (byte) (MAIN_CMD & 0xFF);
        packet[4] = (byte) ((SUB_CMD >> 8) & 0xFF);
        packet[5] = (byte) (SUB_CMD & 0xFF);
        packet[6] = (byte) ((MESSAGE_VERSION >> 8) & 0xFF);
        packet[7] = (byte) (MESSAGE_VERSION & 0xFF);
        packet[8] = ENCRYPTION_MODE;
        packet[9] = AUTHENTICATION_MODE;
        packet[10] = (byte) ((RESERVED >> 24) & 0xFF);
        packet[11] = (byte) ((RESERVED >> 16) & 0xFF);
        packet[12] = (byte) ((RESERVED >> 8) & 0xFF);
        packet[13] = (byte) ((RESERVED & 0xFF) & 0xFF);
        packet[14] = (byte) ((packetLength >> 24) & 0xFF);
        packet[15] = (byte) ((packetLength >> 16) & 0xFF);
        packet[16] = (byte) ((packetLength >> 8) & 0xFF);
        packet[17] = (byte) (packetLength & 0xFF);


        log.info("数据包长度: {}", packetLength);
        log.info("Json数据字节数: {}", jsonDataBytes.length);
        System.arraycopy(jsonDataBytes, 0, packet, 18, jsonDataBytes.length);

        // 认证与校验
        System.arraycopy(authenticationAndValidationField, 0, packet, 18 + jsonDataBytes.length, 16);
        return packet;
    }

    public int getCOMMAND_CATEGORY() {
        return MAIN_CMD;
    }

    public void setCOMMAND_CATEGORY(short COMMAND_CATEGORY) {
        this.MAIN_CMD = COMMAND_CATEGORY;
    }

    public short getSUB_CMD() {
        return SUB_CMD;
    }

    public void setSUB_CMD(short SUB_CMD) {
        this.SUB_CMD = SUB_CMD;
    }

    public short getMESSAGE_VERSION() {
        return MESSAGE_VERSION;
    }

    public void setMESSAGE_VERSION(short MESSAGE_VERSION) {
        this.MESSAGE_VERSION = MESSAGE_VERSION;
    }

    public int getPacketLength() {
        return packetLength;
    }

    public void setPacketLength(int packetLength) {
        this.packetLength = packetLength;
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

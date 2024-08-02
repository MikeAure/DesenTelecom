package com.lu.gademo.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.lu.gademo.dao.split.SendSplitDesenDataDao;
import com.lu.gademo.entity.split.SendSplitDesenData;
import com.lu.gademo.model.TcpPacketSplit;
import com.lu.gademo.utils.Util;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;
import com.lu.gademo.service.SplitSystemLogSender;

@Data
@Service
@Slf4j
public class SplitSystemLogSenderImpl implements SplitSystemLogSender {
    // 拆分重构系统ip和端口
    @Value("${splitReconstruct.address}")
    String splitReconstructAddress;
    @Value("${splitReconstruct.port}")
    int splitReconstructPort;

    // 拆分重构Dao
    @Autowired
    SendSplitDesenDataDao sendSplitDesenDataDao;
    @Autowired
    Util util;
    ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void send2Split(SendSplitDesenData sendSplitDesenData, byte[] rawFileData) {
        // 保存sendSplitDesenData
        if (sendSplitDesenDataDao.existsById(sendSplitDesenData.getDesenInfoAfterID())) {
            sendSplitDesenDataDao.deleteById(sendSplitDesenData.getDesenInfoAfterID());
        }
        sendSplitDesenDataDao.save(sendSplitDesenData);
        // 构造需要发送的Json数据
        ObjectNode splitJsonData = objectMapper.valueToTree(sendSplitDesenData);
//        System.out.println(splitJsonData.toPrettyString());
        ObjectNode allSplitJsonData = objectMapper.createObjectNode();
        allSplitJsonData.put("DataType", 0x3122);
        allSplitJsonData.set("content", splitJsonData);
        ObjectNode dataJson = objectMapper.createObjectNode();
        dataJson.set("data", allSplitJsonData);

        TcpPacketSplit tcpPacketSplit = new TcpPacketSplit(dataJson.toPrettyString());
//        System.out.println(dataJson.toPrettyString());
        byte[] tcpBytePacket = tcpPacketSplit.buildPacket(rawFileData);

        try (
                Socket socket = new Socket(splitReconstructAddress, splitReconstructPort);
                InputStream inputStream = socket.getInputStream();
                OutputStream outputStream = socket.getOutputStream();
                DataInputStream dataInputStream = new DataInputStream(inputStream);
        ) {
            outputStream.write(tcpBytePacket);
            outputStream.flush();
            log.info("已发送拆分重构请求");

        } catch (ConnectException e) {
            log.error("未与拆分重构系统建立连接");
        } catch (UnknownHostException e) {
            log.error(e.getMessage());
        } catch (IOException e) {
            log.error(e.getMessage());
        }

    }

}

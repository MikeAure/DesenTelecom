package com.lu.gademo.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.lu.gademo.dao.ga.split.SendSplitDesenDataDao;
import com.lu.gademo.entity.ga.split.SendSplitDesenData;
import com.lu.gademo.event.ThreeSystemsEvent;
import com.lu.gademo.model.TcpPacketSplit;
import com.lu.gademo.utils.Util;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
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

    @EventListener
    @Async
    @Override
    public void splitHandleThreeSystemEvent(ThreeSystemsEvent logManagerEvent) {
        // 发送给拆分重构系统
        send2Split(logManagerEvent.getSendSplitDesenData(), logManagerEvent.getDesenFileData(), true);
    }

    @Override
    public void send2Split(SendSplitDesenData sendSplitDesenData, byte[] desenFileData, boolean ifSendFile) {
        // 保存sendSplitDesenData
        if (sendSplitDesenDataDao.existsById(sendSplitDesenData.getDesenInfoAfterID())) {
            sendSplitDesenDataDao.deleteById(sendSplitDesenData.getDesenInfoAfterID());
        }
        sendSplitDesenDataDao.save(sendSplitDesenData);
        // 构造需要发送的Json数据
        ObjectNode splitJsonData = objectMapper.valueToTree(sendSplitDesenData);
        ObjectNode allSplitJsonData = objectMapper.createObjectNode();
        allSplitJsonData.put("DataType", 0x3122);
        allSplitJsonData.set("content", splitJsonData);
        ObjectNode dataJson = objectMapper.createObjectNode();
        dataJson.set("data", allSplitJsonData);
//        log.info("拆分重构请求数据: {}", dataJson.toPrettyString());
        TcpPacketSplit tcpPacketSplit = new TcpPacketSplit(dataJson.toPrettyString());
        byte[] tcpBytePacket = new byte[0];
        if (ifSendFile) {
            tcpBytePacket = tcpPacketSplit.buildPacket(desenFileData);
        } else {
            tcpBytePacket = tcpPacketSplit.buildPacket(new byte[0]);
        }
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
            e.getMessage();
        } catch (UnknownHostException e) {
//            log.error(e.getMessage());
            e.getMessage();
        } catch (IOException e) {
//            log.error(e.getMessage());
            e.getMessage();
        }

    }

}

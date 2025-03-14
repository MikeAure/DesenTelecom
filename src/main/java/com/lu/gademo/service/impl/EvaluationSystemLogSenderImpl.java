package com.lu.gademo.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.lu.gademo.dao.ga.effectEva.*;
import com.lu.gademo.entity.ga.effectEva.*;
import com.lu.gademo.model.TcpPacket;
import com.lu.gademo.model.effectEva.EvaluationSystemReturnResult;
import com.lu.gademo.service.EvaluationSystemLogSender;
import com.lu.gademo.utils.Util;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * 用于将日志发送给脱敏评测系统
 */
@Slf4j
@Data
@Service
public class EvaluationSystemLogSenderImpl implements EvaluationSystemLogSender {
    @Value("${systemId.evaluationSystemId}")
    int evaluationSystemId;
    @Value("${effectEva.address}")
    String effectEvaAddress;
    @Value("${effectEva.port}")
    int effectEvaPort;
    @Autowired
    Util util;
    ObjectMapper objectMapper = new ObjectMapper();
    // 效果评测Dao
    @Autowired
    private SendEvaReqDao sendEvaReqDao;
    @Autowired
    private SendEvaReceiptDao sendEvaReceiptDao;
    @Autowired
    private RecEvaBgDao recEvaBgDao;
    @Autowired
    private RecEvaResultDao recEvaResultDao;
    @Autowired
    private RecEvaResultInvDao recEvaResultInvDao;
    @Autowired
    private RecEvaReqReceiptDao recEvaReqReceiptDao;
    @Autowired
    private ApplicationEventPublisher eventPublisher;

    /**
     * 向评测系统发送日志
     *
     * @param sendEvaReq 评测请求
     */
    @Override
    public EvaluationSystemReturnResult send2EffectEva(SendEvaReq sendEvaReq, byte[] rawFileData,
                                                       byte[] desenFileData, Boolean ifSendFile) {
        EvaluationSystemReturnResult evaluationSystemReturnResult = null;
        try {
            // 本地保存请求
            sendEvaReqDao.save(sendEvaReq);

            ObjectNode content = objectMapper.valueToTree(sendEvaReq);
            String evaRequestDesenIntention = content.get("desenIntention").asText();
            String evaRequestDesenRequirements = content.get("desenRequirements").asText();

            ArrayNode evaRequestDesenIntentionArrayNode = util.trimCommaAndReturnArrayNode(evaRequestDesenIntention, objectMapper);
            ArrayNode evaRequestDesenRequirementsArrayNode = util.trimCommaAndReturnArrayNode(evaRequestDesenRequirements, objectMapper);

            content.remove("desenIntention");
            content.remove("desenRequirements");

            content.set("desenIntention", evaRequestDesenIntentionArrayNode);
            content.set("desenRequirements", evaRequestDesenRequirementsArrayNode);
            // 构造数据域
            ObjectNode data = objectMapper.createObjectNode();
            // 增加Pathtree
            ObjectNode pathTree = objectMapper.createObjectNode();
            ObjectNode parent = objectMapper.createObjectNode();

            parent.put("systemID", sendEvaReq.getSystemID());
            parent.put("globalID", sendEvaReq.getGlobalID());
            ObjectNode self = objectMapper.createObjectNode();
            self.put("systemID", sendEvaReq.getSystemID());
            self.put("globalID", sendEvaReq.getGlobalID());
            self.put("evidenceID", sendEvaReq.getEvidenceID());
            ObjectNode child = objectMapper.createObjectNode();
            child.put("systemID", evaluationSystemId);
            child.put("globalID", sendEvaReq.getGlobalID());
            //self.put("status", "数据已脱敏");
            pathTree.set("parent", parent);
            pathTree.set("self", self);
            pathTree.set("child", child);

            data.put("DataType", 0x3130);
            data.set("content", content);
            data.set("pathtree", pathTree);
            ObjectNode dataJson = objectMapper.createObjectNode();
            dataJson.set("data", data);

            log.info("脱敏效果评测系统请求: {}", dataJson.toPrettyString());
            TcpPacket tcpPacket = new TcpPacket(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(dataJson));
            byte[] evaRequestTcpPacket = tcpPacket.buildPacket();
            // 发送
            // 连接服务器
            try (
                    Socket socket = new Socket(effectEvaAddress, effectEvaPort);
                    OutputStream outputStream = socket.getOutputStream();
                    // 接收效果评测系统返回信息
                    InputStream inputStream = socket.getInputStream();
                    DataInputStream dataInputStream = new DataInputStream(inputStream))
            {
                outputStream.write(evaRequestTcpPacket);
                outputStream.flush();

                log.info("已发送脱敏效果评测系统请求");

                // TODO: 增加发送文件的开关
                if (ifSendFile) {
                    // 发送原始文件
                    outputStream.write(rawFileData);
                    // 发送脱敏后的文件
                    outputStream.write(desenFileData);
                    outputStream.flush();
                }
                log.info("Chosen algorithm：" + sendEvaReq.getDesenAlg());

                // 各个实体对象
                RecEvaResultInv recEvaResultInv = new RecEvaResultInv();
                RecEvaResult recEvaResult = new RecEvaResult();
                RecEvaReqReceipt recEvaReqReceipt = new RecEvaReqReceipt();
                SendEvaReceipt sendEvaReceipt = new SendEvaReceipt();

                ObjectMapper mapper = new ObjectMapper();
                String evaResultId = null;

                int recvNum = 2;
                // 依次读取
                for (int i = 0; i < recvNum; i++) {
                    // tcp头
                    // 读取头部
                    byte[] header = new byte[14];
                    dataInputStream.read(header);
                    // 响应数据域长度
                    int dataLength = dataInputStream.readInt();
                    log.info("DataLength: " + dataLength);
                    // 读取数据域内容
                    byte[] dataBytes = new byte[dataLength - 34];
                    dataInputStream.read(dataBytes);
                    String jsonData = new String(dataBytes, StandardCharsets.UTF_8);
                    log.info(jsonData);
                    // 认证与校验
                    byte[] auth = new byte[16];
                    dataInputStream.read(auth);
                    //String 转 json
                    JsonNode jsonNode = mapper.readTree(jsonData);
                    JsonNode recvContent = jsonNode.get("content");

                    int dataTypeNum = jsonNode.get("dataType").asInt();
                    log.info("脱敏评估系统DataType: " + dataTypeNum);
                    // 接收收据
                    if (dataTypeNum == 0x3131) {
                        // 获取实体
                        recEvaReqReceipt = mapper.treeToValue(recvContent, RecEvaReqReceipt.class);
                        String certificateID = recEvaReqReceipt.getCertificateID();
                        log.info("CertificateID: " + certificateID);
                        // 检测重复
                        if (recEvaReqReceiptDao.existsById(certificateID)) {
                            recEvaReqReceiptDao.deleteById(certificateID);
                        }
                        // 插入数据库
                        recEvaReqReceiptDao.save(recEvaReqReceipt);
                        log.info("已接收脱敏效果评测请求收据");
                    }
                    // 接收脱敏效果评测结果
                    else if (dataTypeNum == 0x3132) {
                        // 获取实体
                        recEvaResult = mapper.treeToValue(recvContent, RecEvaResult.class);
                        evaResultId = recvContent.get("evaResultID").asText();
//                     检测重复
                        if (recEvaResultDao.existsById(recEvaResult.getEvaResultID())) {
                            recEvaResultDao.deleteById(recEvaResult.getEvaResultID());
                        }
                        // 插入数据库
                        recEvaResultDao.save(recEvaResult);
                        log.info("脱敏效果评测结果：{}", recvContent.toPrettyString());
                        log.info("已接收脱敏效果评测结果");
                        evaluationSystemReturnResult = new EvaluationSystemReturnResult(recEvaReqReceipt, recEvaResult, null);
                    }
                    // 接收脱敏效果测评结果无效异常消息
                    else if (dataTypeNum == 0x3401) {
                        // 获取实体
                        recEvaResultInv = mapper.treeToValue(recvContent, RecEvaResultInv.class);
                        evaResultId = recvContent.get("evaResultID").asText();
//                     检测重复
                        if (recEvaResultInvDao.existsById(recEvaResultInv.getEvaResultID())) {
                            recEvaResultInvDao.deleteById(recEvaResultInv.getEvaResultID());
                        }
                        // 插入数据库
                        recEvaResultInvDao.save(recEvaResultInv);
                        log.info("已接收脱敏效果测评结果无效异常消息");
                        evaluationSystemReturnResult = new EvaluationSystemReturnResult(recEvaReqReceipt, null, recEvaResultInv);
                    }
                }

                // 发送收据
                log.info("发送脱敏效果收据");
                sendEvaReceipt.setEvaResultID(evaResultId);
                sendEvaReceipt.setCertificateID(util.getSM3Hash((sendEvaReceipt.getEvaResultID() + util.getTime()).getBytes()));
                sendEvaReceipt.setHash(util.getSM3Hash((sendEvaReceipt.getEvaResultID() + sendEvaReceipt.getCertificateID()).getBytes()));
                ObjectNode evaReceiptContent = (ObjectNode) objectMapper.readTree(objectMapper.writeValueAsString(sendEvaReceipt));
                ObjectNode data1 = objectMapper.createObjectNode();
                data1.put("DataType", 0x3130);
                data1.set("content", evaReceiptContent);
                ObjectNode dataJson1 = objectMapper.createObjectNode();
                dataJson1.set("data", data1);
                TcpPacket tcpPacket1 = new TcpPacket(objectMapper.writeValueAsString(dataJson1));
                byte[] tcp1 = tcpPacket1.buildPacket();
                // 发送
                outputStream.write(tcp1);
                outputStream.flush();
                outputStream.flush();
                outputStream.close();
                inputStream.close();
                dataInputStream.close();
                socket.close();
                // 本地保存收据
                sendEvaReceiptDao.save(sendEvaReceipt);
                return evaluationSystemReturnResult;
            } catch (ConnectException connectException) {
                log.info("未与脱敏效果评测系统建立连接");
            } catch (IOException e) {
                log.info(e.getMessage());
            }
        } catch (Exception e) {
            log.info(e.getMessage());
        }
        return null;
    }

    @Override
    public EvaluationSystemReturnResult send2EffectEva(SendEvaReq sendEvaReq, Path rawFilePath,
                                                       Path desenFilePath, Boolean ifSendFile) {
        EvaluationSystemReturnResult evaluationSystemReturnResult = null;
        try {
            // 本地保存请求
            sendEvaReqDao.save(sendEvaReq);

            ObjectNode content = objectMapper.valueToTree(sendEvaReq);
            String evaRequestDesenIntention = content.get("desenIntention").asText();
            String evaRequestDesenRequirements = content.get("desenRequirements").asText();

            ArrayNode evaRequestDesenIntentionArrayNode = util.trimCommaAndReturnArrayNode(evaRequestDesenIntention, objectMapper);
            ArrayNode evaRequestDesenRequirementsArrayNode = util.trimCommaAndReturnArrayNode(evaRequestDesenRequirements, objectMapper);

//            content.remove("desenIntention");
//            content.remove("desenRequirements");

            content.set("desenIntention", evaRequestDesenIntentionArrayNode);
            content.set("desenRequirements", evaRequestDesenRequirementsArrayNode);
            // 构造数据域
            ObjectNode data = objectMapper.createObjectNode();
            // 增加Pathtree
            ObjectNode pathTree = objectMapper.createObjectNode();
            ObjectNode parent = objectMapper.createObjectNode();

            parent.put("systemID", sendEvaReq.getSystemID());
            parent.put("globalID", sendEvaReq.getGlobalID());
            ObjectNode self = objectMapper.createObjectNode();
            self.put("systemID", sendEvaReq.getSystemID());
            self.put("globalID", sendEvaReq.getGlobalID());
            self.put("evidenceID", sendEvaReq.getEvidenceID());
            ObjectNode child = objectMapper.createObjectNode();
            child.put("systemID", evaluationSystemId);
            child.put("globalID", sendEvaReq.getGlobalID());
            //self.put("status", "数据已脱敏");
            pathTree.set("parent", parent);
            pathTree.set("self", self);
            pathTree.set("child", child);

            data.put("DataType", 0x3130);
            data.set("content", content);
            data.set("pathtree", pathTree);
            ObjectNode dataJson = objectMapper.createObjectNode();
            dataJson.set("data", data);

            log.info("脱敏效果评测系统请求: {}", dataJson.toPrettyString());
            TcpPacket tcpPacket = new TcpPacket(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(dataJson));
            byte[] evaRequestTcpPacket = tcpPacket.buildPacket();
            // 发送
            // 连接服务器
            Socket socket = new Socket(effectEvaAddress, effectEvaPort);
            OutputStream outputStream = socket.getOutputStream();
            // 接收效果评测系统返回信息
            InputStream inputStream = socket.getInputStream();
            DataInputStream dataInputStream = new DataInputStream(inputStream);
            outputStream.write(evaRequestTcpPacket);
            outputStream.flush();

            log.info("已发送脱敏效果评测系统请求");

            // TODO: 增加发送文件的开关
            if (ifSendFile) {
                // 发送原始文件
                try (InputStream rawInputStream = Files.newInputStream(rawFilePath)) {
                    IOUtils.copy(rawInputStream, outputStream);
                }
                // 发送脱敏后的文件
                try (InputStream desenInputStream = Files.newInputStream(desenFilePath)) {
                    IOUtils.copy(desenInputStream, outputStream);
                }
            }
            log.info("Chosen algorithm：" + sendEvaReq.getDesenAlg());

            // 各个实体对象
            RecEvaResultInv recEvaResultInv = new RecEvaResultInv();
            RecEvaResult recEvaResult = new RecEvaResult();
            RecEvaReqReceipt recEvaReqReceipt = new RecEvaReqReceipt();
            SendEvaReceipt sendEvaReceipt = new SendEvaReceipt();

            ObjectMapper mapper = new ObjectMapper();
            String evaResultId = null;

            int recvNum = 2;
            // 依次读取
            for (int i = 0; i < recvNum; i++) {
                // tcp头
                // 读取头部
                byte[] header = new byte[14];
                dataInputStream.read(header);
                // 响应数据域长度
                int dataLength = dataInputStream.readInt();
                log.info("DataLength: " + dataLength);
                // 读取数据域内容
                byte[] dataBytes = new byte[dataLength - 34];
                dataInputStream.read(dataBytes);
                String jsonData = new String(dataBytes, StandardCharsets.UTF_8);
                log.info(jsonData);
                // 认证与校验
                byte[] auth = new byte[16];
                dataInputStream.read(auth);
                //String 转 json
                JsonNode jsonNode = mapper.readTree(jsonData);
                JsonNode recvContent = jsonNode.get("content");

                int dataTypeNum = jsonNode.get("dataType").asInt();
                log.info("脱敏评估系统DataType: " + dataTypeNum);
                // 接收收据
                if (dataTypeNum == 0x3131) {
                    // 获取实体
                    recEvaReqReceipt = mapper.treeToValue(recvContent, RecEvaReqReceipt.class);
                    String certificateID = recEvaReqReceipt.getCertificateID();
                    log.info("CertificateID: " + certificateID);
                    // 检测重复
                    if (recEvaReqReceiptDao.existsById(certificateID)) {
                        recEvaReqReceiptDao.deleteById(certificateID);
                    }
                    // 插入数据库
                    recEvaReqReceiptDao.save(recEvaReqReceipt);
                    log.info("已接收脱敏效果评测请求收据");
                }
                // 接收脱敏效果评测结果
                else if (dataTypeNum == 0x3132) {
                    // 获取实体
                    recEvaResult = mapper.treeToValue(recvContent, RecEvaResult.class);
                    evaResultId = recvContent.get("evaResultID").asText();
//                     检测重复
                    if (recEvaResultDao.existsById(recEvaResult.getEvaResultID())) {
                        recEvaResultDao.deleteById(recEvaResult.getEvaResultID());
                    }
                    // 插入数据库
                    recEvaResultDao.save(recEvaResult);
//                    log.info("脱敏效果评测结果：{}", recvContent.toPrettyString());
                    log.info("已接收脱敏效果评测结果");
                    evaluationSystemReturnResult = new EvaluationSystemReturnResult(recEvaReqReceipt, recEvaResult, null);
                }
                // 接收脱敏效果测评结果无效异常消息
                else if (dataTypeNum == 0x3401) {
                    // 获取实体
                    recEvaResultInv = mapper.treeToValue(recvContent, RecEvaResultInv.class);
//                     检测重复
                    if (recEvaResultInvDao.existsById(recEvaResultInv.getEvaResultID())) {
                        recEvaResultInvDao.deleteById(recEvaResultInv.getEvaResultID());
                    }
                    // 插入数据库
                    recEvaResultInvDao.save(recEvaResultInv);
                    log.info("已接收脱敏效果测评结果无效异常消息");
                    evaluationSystemReturnResult = new EvaluationSystemReturnResult(recEvaReqReceipt, null, recEvaResultInv);
                }
            }

            // 发送收据
            log.info("发送脱敏效果收据");
            sendEvaReceipt.setEvaResultID(evaResultId);
            sendEvaReceipt.setCertificateID(util.getSM3Hash((sendEvaReceipt.getEvaResultID() + util.getTime()).getBytes()));
            sendEvaReceipt.setHash(util.getSM3Hash((sendEvaReceipt.getEvaResultID() + sendEvaReceipt.getCertificateID()).getBytes()));
            ObjectNode evaReceiptContent = (ObjectNode) objectMapper.readTree(objectMapper.writeValueAsString(sendEvaReceipt));
            ObjectNode data1 = objectMapper.createObjectNode();
            data1.put("DataType", 0x3130);
            data1.set("content", evaReceiptContent);
            ObjectNode dataJson1 = objectMapper.createObjectNode();
            dataJson1.set("data", data1);
            TcpPacket tcpPacket1 = new TcpPacket(objectMapper.writeValueAsString(dataJson1));
            byte[] tcp1 = tcpPacket1.buildPacket();
            // 发送
            outputStream.write(tcp1);
            outputStream.flush();
            outputStream.flush();
            outputStream.close();
            inputStream.close();
            dataInputStream.close();
            socket.close();
            // 本地保存收据
            sendEvaReceiptDao.save(sendEvaReceipt);
            return evaluationSystemReturnResult;
        } catch (ConnectException connectException) {
            log.info("未与脱敏效果评测系统建立连接");
        } catch (IOException e) {
            log.info(e.getMessage());

        }
        return null;
    }
}

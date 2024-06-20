package com.lu.gademo.model;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.lu.gademo.dao.ruleCheck.*;
import com.lu.gademo.entity.ruleCheck.*;
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
import java.net.Socket;
import java.nio.charset.StandardCharsets;

@Slf4j
@Data
@Service
public class RuleCheckSystemLogSender {
    @Value("${systemId.ruleCheckSystemId}")
    int ruleCheckSystemId;
    @Value("${ruleCheck.address}")
    String ruleCheckAddress;
    @Value("${ruleCheck.port}")
    int ruleCheckPort;

    @Autowired
    Util util;
    ObjectMapper objectMapper = new ObjectMapper();
    // 合规检查Dao
    @Autowired
    private SendRuleReqDao sendRuleReqDao;
    @Autowired
    private SendRuleReceiptDao sendRuleReceiptDao;
    @Autowired
    private RecRuleAlgDao recRuleAlgDao;
    @Autowired
    private RecRuleOperateDao recRuleOperateDao;
    @Autowired
    private RecRuleInfoTypeDao recRuleInfoTypeDao;
    @Autowired
    private RecRuleReqReceiptDao recRuleReqReceiptDao;
    @Autowired
    private RecRuleResultDao recRuleResultDao;
    @Autowired
    private RecRuleTimeDao recRuleTimeDao;

    public void send2RuleCheck(SendRuleReq sendRuleReq) {
        try {
            ObjectNode content = (ObjectNode) objectMapper.readTree(objectMapper.writeValueAsString(sendRuleReq));

            // 连接服务器
            Socket socket = new Socket(ruleCheckAddress, ruleCheckPort);
            // 构造数据域
            ObjectNode data = objectMapper.createObjectNode();
            data.put("DataType", 0x3140);
            data.set("content", content);
            ObjectNode dataJson = objectMapper.createObjectNode();
            dataJson.set("data", data);
            TcpPacket tcpPacket = new TcpPacket(objectMapper.writeValueAsString(dataJson));
            byte[] tcp = tcpPacket.buildPacket();
            // 发送
            OutputStream outputStream = socket.getOutputStream();
            outputStream.write(tcp);
            outputStream.flush();
            System.out.println("合规请求发送");
            // 合规检查请求信息存储到数据库
            sendRuleReqDao.save(sendRuleReq);

            // 接收合规检查系统返回信息
            InputStream inputStream = socket.getInputStream();
            DataInputStream dataInputStream = new DataInputStream(inputStream);

            // 各个实体对象
            RecRuleResult ruleResult = new RecRuleResult();
            RecRuleAlg ruleAlg;
            RecRuleInfoType ruleInfoType;
            RecRuleOperate ruleOperate;
            RecRuleReqReceipt ruleReqReceipt;
            RecRuleTime ruleTime;
            SendRuleReceipt ruleReceipt = new SendRuleReceipt();

            // 标识是否收到对应信息
            boolean first = true;
            boolean second = true;
            boolean third = true;
            boolean forth = true;
            boolean fifth = true;
            boolean sixth = true;
            // 依次读取
            //while(first || second || third || forth || fifth || sixth){
            int i = 0;
            while (i < 6) {
                // 读取tcp头
                // 读取头部
                byte[] header = new byte[14];
                dataInputStream.read(header);
                // 响应数据域长度
                int dataLength = dataInputStream.readInt();
                // 读取数据域内容
                byte[] dataBytes = new byte[dataLength];
                inputStream.read(dataBytes);
                String jsonData = new String(dataBytes, StandardCharsets.UTF_8);
                System.out.println(jsonData);
                // 认证与校验
                byte[] auth = new byte[16];
                inputStream.read(auth);
                //String 转 json
                JsonNode jsonNode = objectMapper.readTree(jsonData);
                JsonNode recData = jsonNode.get("data");
                JsonNode recContent = jsonNode.get("content");
                // 接收收据
                if (recData.get("DataType").asInt() == 0x3141) {
                    // 获取实体
                    ruleReqReceipt = objectMapper.treeToValue(recContent, RecRuleReqReceipt.class);
                    // 检测重复
                    //recRuleReqReceiptDao.deleteById(ruleReqReceipt.getCertificateId());
                    // 插入数据库
                    //recRuleReqReceiptDao.save(ruleReqReceipt);
                    System.out.println("接收收据");
                    first = false;
                }
                // 接收合规检查结果
                else if (recData.get("DataType").asInt() == 0x3142) {
                    // 获取实体
                    /*ruleResult = objectMapper.treeToValue(recContent, RecRuleResult.class);
                    // 检测重复
                    //recRuleResultDao.deleteById(ruleResult.getReportId());
                    // 插入数据库
                    recRuleResultDao.save(ruleResult);*/
                    ruleResult.setReportId(jsonNode.get("data").get("content").get("reportID").asText());
                    second = false;
                    System.out.println("接收合规检查结果");
                }
                // 接收脱敏算法、参数异常消息
                else if (recData.get("DataType").asInt() == 0x3201) {
                    // 获取实体
                   /* ruleAlg = objectMapper.treeToValue(recContent, RecRuleAlg.class);
                    // 检测重复
                    //recRuleAlgDao.deleteById(ruleAlg.getReportId());
                    // 插入数据库
                    recRuleAlgDao.save(ruleAlg);*/
                    third = false;
                    System.out.println("接收脱敏算法、参数异常消息");
                }
                // 脱敏后信息类型不符合脱敏要求异常消息
                else if (recData.get("DataType").asInt() == 0x3202) {
                    // 获取实体
                   /* ruleInfoType = objectMapper.treeToValue(recContent, RecRuleInfoType.class);
                    // 检测重复
                    //recRuleInfoTypeDao.deleteById(ruleInfoType.getReportId());
                    // 插入数据库
                    recRuleInfoTypeDao.save(ruleInfoType);*/
                    forth = false;
                    System.out.println("接收信息类型不符合脱敏要求异常消息");
                }
                // 接收脱敏过程操作不合规异常消息
                else if (recData.get("DataType").asInt() == 0x3203) {
                    // 获取实体
                  /*  ruleOperate = objectMapper.treeToValue(recContent, RecRuleOperate.class);
                    // 检测重复
                    //recRuleOperateDao.deleteById(ruleOperate.getReportId());
                    // 插入数据库
                    recRuleOperateDao.save(ruleOperate);*/
                    fifth = false;
                    System.out.println("接收脱敏过程操作不合规异常消息");
                }
                // 接收脱敏时间不符合脱敏要求异常消息
                else if (recData.get("DataType").asInt() == 0x3204) {
                    // 获取实体
                   /* ruleTime = objectMapper.treeToValue(recContent, RecRuleTime.class);
                    // 检测重复
                    //recRuleTimeDao.deleteById(ruleTime.getLogId());
                    // 插入数据库
                    recRuleTimeDao.save(ruleTime);*/
                    sixth = false;
                    System.out.println("接收脱敏时间不符合脱敏要求异常消息");
                }
                i++;

            }

            // 发送收据
            ruleReceipt.setReportId(ruleResult.getReportId());
            ruleReceipt.setCertificateId(util.getSM3Hash((ruleResult.getReportId() + util.getTime()).getBytes()));
            ruleReceipt.setHash(util.getSM3Hash((ruleReceipt.getCertificateId() + ruleReceipt.getReportId()).getBytes()));
            ObjectNode ruleRecepitContent = (ObjectNode) objectMapper.readTree(objectMapper.writeValueAsString(ruleReceipt));
            ObjectNode data1 = objectMapper.createObjectNode();
            data1.put("DataType", 0x3043);
            data1.set("content", ruleRecepitContent);
            ObjectNode dataJson1 = objectMapper.createObjectNode();
            dataJson1.set("data", data1);
            TcpPacket tcpPacket1 = new TcpPacket(objectMapper.writeValueAsString(dataJson1));
            byte[] tcp1 = tcpPacket1.buildPacket();
            // 发送
            outputStream.write(tcp1);
            outputStream.flush();
            // 保存发送的收据
            sendRuleReceiptDao.save(ruleReceipt);

            outputStream.close();
            inputStream.close();
            dataInputStream.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

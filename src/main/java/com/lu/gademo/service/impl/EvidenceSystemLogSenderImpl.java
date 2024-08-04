package com.lu.gademo.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.lu.gademo.dao.evidence.*;
import com.lu.gademo.entity.evidence.*;
import com.lu.gademo.event.ThreeSystemsEvent;
import com.lu.gademo.model.TcpPacket;
import com.lu.gademo.service.EvidenceSystemLogSender;
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
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;

@Slf4j
@Data
@Service
public class EvidenceSystemLogSenderImpl implements EvidenceSystemLogSender {
    // 存证系统id
    @Value("${systemId.evidenceSystemId}")
    int evidenceSystemId;
    // 本地存证系统ip和端口
    @Value("${evidence.localAddress}")
    String evidenceLocalAddress;
    @Value("${evidence.localPort}")
    int evidenceLocalPort;

    // 中心存证系统ip和端口
    @Value("${evidence.remoteAddress}")
    String evidenceRemoteAddress;
    @Value("${evidence.remotePort}")
    int evidenceRemotePort;

    // 存证系统Dao
    @Autowired
    EvidenceReceiptErrDao evidenceReceiptErrDao;
    @Autowired
    EvidenceReceiptNormalDao evidenceReceiptNormalDao;
    @Autowired
    EvidenceResponseDao evidenceResponseDao;
    @Autowired
    ReqEvidenceSaveDao reqEvidenceSaveDao;
    @Autowired
    SubmitEvidenceLocalDao submitEvidenceLocalDao;
    @Autowired
    SubmitEvidenceRemoteDao submitEvidenceRemoteDao;

    // 向存证系统发起请求时使用的mainCommand和subCommand
    @Value("${evidenceSystem.evidenceRequest.mainCommand}")
    Integer evidenceRequestMainCommand;
    @Value("${evidenceSystem.evidenceRequest.subCommand}")
    Integer evidenceRequestSubCommand;
    @Value("${evidenceSystem.evidenceRequest.msgVersion}")
    Integer evidenceRequestMsgVersion;

    @Autowired
    Util util;

    ObjectMapper objectMapper = new ObjectMapper();

    @EventListener
    @Async
    @Override
    public void evidenceHandleThressSystemEvent(ThreeSystemsEvent threeSystemsEvent) {
        ReqEvidenceSave reqEvidenceSave = threeSystemsEvent.getReqEvidenceSave();
        SubmitEvidenceLocal submitEvidenceLocal = threeSystemsEvent.getSubmitEvidenceLocal();
        send2Evidence(reqEvidenceSave, submitEvidenceLocal);

    }

    /**
     * @param reqEvidenceSave     请求存证
     * @param submitEvidenceLocal 本地存证
     * @param submitEvidenceLocal 中心存证
     */
    public void send2Evidence(ReqEvidenceSave reqEvidenceSave, SubmitEvidenceLocal submitEvidenceLocal) {

        // 中心存证
        ObjectNode reqData = objectMapper.createObjectNode();
        // objectSize: 处置对象的大小
        reqData.put("objectSize", reqEvidenceSave.getObjectSize());
        // objectMode: 处置对象的模态
        reqData.put("objectMode", reqEvidenceSave.getObjectMode());
        reqEvidenceSave.setDatasign(util.getSM3Hash(reqData.toString().getBytes()));
        ObjectNode reqEvidence = objectMapper.createObjectNode();
        // systemID: 系统ID
        reqEvidence.put("systemID", reqEvidenceSave.getSystemID());
        // systemIP: 上报系统的IP地址
        reqEvidence.put("systemIP", reqEvidenceSave.getSystemIP());
        // mainCMD: 消息类型编码（主命令码）
        reqEvidence.put("mainCMD", reqEvidenceSave.getMainCMD());
        // subCMD: 监管事项表
        reqEvidence.put("subCMD", reqEvidenceSave.getSubCMD());
        // evidenceID: 上报证据内部业务的唯一ID
        reqEvidence.put("evidenceID", reqEvidenceSave.getEvidenceID());
        // msgVersion: 消息类型编码
        reqEvidence.put("msgVersion", reqEvidenceSave.getMsgVersion());
        // reqtime: 请求提交时间
        reqEvidenceSave.setReqtime(util.getTime());
        reqEvidence.put("reqtime", reqEvidenceSave.getReqtime());
        reqEvidence.set("data", reqData);
        // dataSign: 对data字段的签名
        reqEvidence.put("datasign", reqEvidenceSave.getDatasign());
        log.info("发送给中心存证系统的请求: {}", String.valueOf(reqEvidenceSave));
        log.info("发送给本地存证系统的请求: {}", String.valueOf(submitEvidenceLocal));
        // 相关请求信息存储到数据库
        reqEvidenceSaveDao.save(reqEvidenceSave);

        // 存证响应
        EvidenceResponse evidenceResponse = new EvidenceResponse();

        try (
                // 发起请求
                Socket remoteSocket = new Socket(evidenceRemoteAddress, evidenceRemotePort);
                // 向中心存证系统发送信息
                OutputStream remoteOutputStream = remoteSocket.getOutputStream();
                // 接收中心存证系统返回信息
                InputStream remoteInputStream = remoteSocket.getInputStream();
                DataInputStream remoteDataInputStream = new DataInputStream(remoteInputStream);
        ) {

            TcpPacket reqEvidenceTcpPacket = new TcpPacket(objectMapper.writeValueAsString(reqEvidence), (short) 0x0001, (short) 0x0031, (short) 0x1000);
            byte[] reqEvidenceTcp = reqEvidenceTcpPacket.buildPacket();
            // 发送
            remoteOutputStream.write(reqEvidenceTcp);
            remoteOutputStream.flush();
            log.info("存证请求发送成功");
            // 读取头部
            byte[] header = new byte[14];
            remoteDataInputStream.read(header);
            // 响应数据域长度
            int responseDataLength = remoteDataInputStream.readInt();
            log.info("中心存证系统响应的长度：{}", responseDataLength);
            // 读取数据域内容
            byte[] responseDataBytes = new byte[responseDataLength - 34];
            remoteInputStream.read(responseDataBytes);
            // 认证与校验
            //byte[] auth = new byte[16];
            //remoteInputStream.read(auth);
            String response = new String(responseDataBytes, StandardCharsets.UTF_8);
            //String转json 存储响应信息
            log.info("读取校验");
            JsonNode responseJson = objectMapper.readTree(response);
            log.info("存证系统响应：{}", responseJson.toPrettyString());
            // systemID: 系统ID
            evidenceResponse.setSystemID(responseJson.get("systemID").asInt());
            // mainCMD: 消息类型编码（主命令码）
            evidenceResponse.setMainCMD(responseJson.get("mainCMD").asInt());
            // subCMD: 监管事项表
            evidenceResponse.setSubCMD(responseJson.get("subCMD").asInt());
            // evidenceID: 上报证据内部业务的唯一ID
            evidenceResponse.setEvidenceID(responseJson.get("evidenceID").asText());
            // msgVersion: 消息类型编码
            evidenceResponse.setMsgVersion(responseJson.get("msgVersion").asInt());
            // responsetime: 响应时间
            evidenceResponse.setResponsetime(responseJson.get("responsetime").asText());
            // nonce: 随机内容
            evidenceResponse.setNonce(responseJson.get("data").get("nonce").asText());
            // position: 随机内容添加位置
            evidenceResponse.setPosition(responseJson.get("data").get("optmodel").get("position").asText());
            // dataSign: 对data字段的签名
            evidenceResponse.setDataSign(responseJson.get("datasign").asText());
            // randomIdentification: 随机标识
            evidenceResponse.setRandomIdentification(responseJson.get("randomidentification").asText());
            // 保存到数据库
            evidenceResponseDao.save(evidenceResponse);
            log.info("中心存证响应Random Identification {}", evidenceResponse.getRandomIdentification());
            log.info("中心存证结束");
        } catch (ConnectException connectException) {
            log.error("未与中心存证系统建立连接");
        } catch (IOException e) {
            log.error(e.getMessage());
        }

        // 构造本地存证信息
        // 设置pathTree
        ObjectNode pathTree = objectMapper.createObjectNode();
        ObjectNode parent = objectMapper.createObjectNode();

        submitEvidenceLocal.setParentSystemId(submitEvidenceLocal.getSystemID());
        submitEvidenceLocal.setStatus("数据已脱敏");
        submitEvidenceLocal.setChildSystemId(evidenceSystemId);

        parent.put("systemID", submitEvidenceLocal.getParentSystemId());
        parent.put("globalID", submitEvidenceLocal.getGlobalID());
        ObjectNode self = objectMapper.createObjectNode();
        self.put("systemID", submitEvidenceLocal.getSystemID());
        self.put("globalID", submitEvidenceLocal.getGlobalID());
        self.put("evidenceID", submitEvidenceLocal.getEvidenceID());
        ObjectNode child = objectMapper.createObjectNode();
        child.put("systemID", submitEvidenceLocal.getChildSystemId());
        child.put("globalID", submitEvidenceLocal.getGlobalID());
        //self.put("status", "数据已脱敏");
        pathTree.set("parent", parent);
        pathTree.set("self", self);
        pathTree.set("child", child);
        // 设置data
        ObjectNode localEvidenceData = objectMapper.createObjectNode();
        // globalID: 文件全局ID
        localEvidenceData.put("globalID", submitEvidenceLocal.getGlobalID());
        // status: 状态
        localEvidenceData.put("status", submitEvidenceLocal.getStatus());
        // optTime: 操作时间
        submitEvidenceLocal.setOptTime(util.getTime());
        localEvidenceData.put("optTime", submitEvidenceLocal.getOptTime());
        // fileTitle: 脱敏对象名称
        localEvidenceData.put("fileTitle", submitEvidenceLocal.getFileTitle());
        // fileAbstract: 脱敏对象摘要
        localEvidenceData.put("fileAbstract", submitEvidenceLocal.getFileAbstract());
        // fileKeyword: 脱敏对象检索关键词
        localEvidenceData.put("fileKeyword", submitEvidenceLocal.getFileKeyword());
        // desenAlg: 采用的脱敏算法
        localEvidenceData.put("desenAlg", submitEvidenceLocal.getDesenAlg());
        // fileSize: 脱敏对象大小
        localEvidenceData.put("fileSize", submitEvidenceLocal.getFileSize());
        // fileHASH: 脱敏对象hash
        localEvidenceData.put("fileHash", submitEvidenceLocal.getFileHash());
        // fileSig: 脱敏对象签名
        localEvidenceData.put("fileSig", submitEvidenceLocal.getFileSig());
        // desenPerformer: 脱敏执行主体
        localEvidenceData.put("desenPerformer", submitEvidenceLocal.getDesenPerformer());
        // desenCom: 脱敏操作完成情况
        localEvidenceData.put("desenCom", submitEvidenceLocal.getDesenCom());
        // desenInfoPreID: 脱敏前信息ID
        localEvidenceData.put("desenInfoPreID", submitEvidenceLocal.getDesenInfoPreID());
        // desenInfoAfterID: 脱敏后信息ID
        localEvidenceData.put("desenInfoAfterID", submitEvidenceLocal.getDesenInfoAfterID());
        // desenIntention: 脱敏意图
        String tempDesenIntentionString = submitEvidenceLocal.getDesenIntention();
        ArrayNode desenIntentionArrayNode = util.trimCommaAndReturnArrayNode(tempDesenIntentionString, objectMapper);
        localEvidenceData.set("desenIntention", desenIntentionArrayNode);
        // desenRequirements: 脱敏要求
        String tempDesenRequirementsString = submitEvidenceLocal.getDesenRequirements();
        ArrayNode desenDesenRequirementsArrayNode = util.trimCommaAndReturnArrayNode(tempDesenRequirementsString, objectMapper);
        localEvidenceData.set("desenRequirements", desenDesenRequirementsArrayNode);
        // desenControlSet: 脱敏控制集合（操作配置）
        localEvidenceData.put("desenControlSet", submitEvidenceLocal.getDesenControlSet());
        // pathTree: 保存父子节点及自身节点ID
        localEvidenceData.set("pathtree", pathTree);
        // desenAlgParam: 脱敏算法参数
        localEvidenceData.put("desenAlgParam", submitEvidenceLocal.getDesenAlgParam());
        // desenPerformStartTime: 脱敏执行开始时间
        localEvidenceData.put("desenPerformStartTime", submitEvidenceLocal.getDesenPerformStartTime());
        // desenPerformEndTime: 脱敏执行结束时间
        localEvidenceData.put("desenPerformEndTime", submitEvidenceLocal.getDesenPerformEndTime());
        // desenLevel: 脱敏级别
        localEvidenceData.put("desenLevel", submitEvidenceLocal.getDesenLevel());

        // 整个json
        ObjectNode localEvidenceJson = objectMapper.createObjectNode();
        localEvidenceJson.put("systemID", submitEvidenceLocal.getSystemID());
        localEvidenceJson.put("systemIP", submitEvidenceLocal.getSystemIP());
//            localEvidenceJson.put("mainCMD", submitEvidenceLocal.getMainCMD());
        localEvidenceJson.put("mainCMD", 0x0003);
        //localEvidenceJson.put("subCMD", submitEvidenceLocal.getSubCMD());
        localEvidenceJson.put("subCMD", 0x0031);
        localEvidenceJson.put("evidenceID", submitEvidenceLocal.getEvidenceID());
        submitEvidenceLocal.setSubmittime(util.getTime());
        localEvidenceJson.put("submittime", submitEvidenceLocal.getSubmittime());
        localEvidenceJson.put("msgVersion", 0x3110);
        //localEvidenceJson.put("submittime", util.getTime());
        //localEvidenceJson.set("data", localEvidenceData);
        localEvidenceJson.set("data", localEvidenceData);
        // dataHash: 对数据域部分进行hash
        localEvidenceJson.put("dataHash", util.getSM3Hash(localEvidenceData.toString().getBytes()));
        //localEvidenceJson.put("datasign", evidenceResponse.getDataSign());
        // datasign: 中心存证对随机防伪内容的签名
        localEvidenceJson.put("datasign", evidenceResponse.getDataSign());
        // 密文字段（随机标识），确保存证上报前做过请求应答
        localEvidenceJson.put("randomidentification", evidenceResponse.getRandomIdentification());
        submitEvidenceLocal.setDataHash(localEvidenceJson.get("dataHash").asText());
        submitEvidenceLocal.setDatasign(evidenceResponse.getDataSign());
        submitEvidenceLocal.setRandomidentification(evidenceResponse.getRandomIdentification());
        // 相关请求信息存储到数据库
        submitEvidenceLocalDao.save(submitEvidenceLocal);

        try (
                Socket localSocket = new Socket(evidenceLocalAddress, evidenceLocalPort);
                // 接受收据
                InputStream localInputStream = localSocket.getInputStream();
                DataInputStream localDataInputStream = new DataInputStream(localInputStream);
                OutputStream localOutputStream = localSocket.getOutputStream();
        ) {
            // 打印发送json
            String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(localEvidenceJson);
            log.info("本地存证请求Json数据: {}", json);

            TcpPacket localTcpPacket = new TcpPacket(objectMapper.writeValueAsString(localEvidenceJson), (short) 0x0003, (short) 0x0031, (short) 0x3110);
            byte[] localTcp = localTcpPacket.buildPacket();
            // 发送
            log.info("向本地存证系统发送");
            localOutputStream.write(localTcp);
            localOutputStream.flush();
            log.info("发送成功");

            EvidenceReceiptErr evidenceReceiptErr = new EvidenceReceiptErr();
            EvidenceReceiptNormal evidenceReceiptNormal = new EvidenceReceiptNormal();
            // 读取头部
            byte[] recHeader = new byte[14];
            localDataInputStream.read(recHeader);
            // 响应数据域长度
            int recDataLength = localDataInputStream.readInt();
            // 读取数据域内容
            byte[] recDataBytes = new byte[recDataLength - 34];
            localInputStream.read(recDataBytes);
            // 认证与校验
            //byte[] auth = new byte[16];
            //localInputStream.read(auth);
            String receipt = new String(recDataBytes, StandardCharsets.UTF_8);
            //String转json 存储响应信息
            JsonNode recJson = objectMapper.readTree(receipt);
            //打印
            String recjson = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(recJson);
            log.info("接收隐私数据流转状态管理与存证系统收据: {}", recjson);
            //  发生异常
            if (recJson.has("errCode")) {
                // 系统ID
                evidenceReceiptErr.setSystemID(recJson.get("systemID").asInt());
                evidenceReceiptErr.setMainCMD(recJson.get("mainCMD").asInt());
                evidenceReceiptErr.setSubCMD(recJson.get("subCMD").asInt());
                // 上报证据内部业务的唯一ID
                evidenceReceiptErr.setEvidenceID(recJson.get("evidenceID").asText());
                evidenceReceiptErr.setMsgVersion(recJson.get("msgVersion").asInt());
                evidenceReceiptErr.setStatus("ok");
                evidenceReceiptErr.setErrCode(recJson.get("errCode").asInt());
                //存储
                evidenceReceiptErrDao.save(evidenceReceiptErr);
                if (recJson.get("errCode").asInt() == 0x01) {
                    log.error("未进行请求认证");
                } else {
                    log.error("上报数据格式不正确");
                }
            }
            // 正常接收
            else {
                evidenceReceiptNormal.setSystemID(recJson.get("systemID").asInt());
                evidenceReceiptNormal.setMainCMD(recJson.get("mainCMD").asInt());
                evidenceReceiptNormal.setSubCMD(recJson.get("subCMD").asInt());
                evidenceReceiptNormal.setEvidenceID(recJson.get("evidenceID").asText());
                evidenceReceiptNormal.setMsgVersion(recJson.get("msgVersion").asInt());
                evidenceReceiptNormal.setStatus("ok");
                // 上报信息在存证系统中存储的唯一ID
                evidenceReceiptNormal.setCertificateID(recJson.get("data").get("certificateID").asText());
                // 收条反馈时间
                evidenceReceiptNormal.setCertificateTime(recJson.get("data").get("certificateTime").asText());
                // 收条返回信息data字段的hash值
                evidenceReceiptNormal.setCertificateHash(recJson.get("certificateHash").asText());
                // 收条返回信息data字段的sign值
                evidenceReceiptNormal.setCertificateSign(recJson.get("certificateSign").asText());
                // 存储
                evidenceReceiptNormalDao.save(evidenceReceiptNormal);
                log.info("本地存证收据正常接收");
            }

            localDataInputStream.close();
            localOutputStream.close();
        } catch (ConnectException connectException) {
            log.error("未与本地存证系统建立连接");
        } catch (IOException e) {
            log.error(e.getMessage());
        }

    }
}

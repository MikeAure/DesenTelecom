package com.lu.gademo.model;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.lu.gademo.dao.evidence.*;
import com.lu.gademo.entity.evidence.*;
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
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;

@Slf4j
@Data
@Service
public class EvidenceSystemLogSender {
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

    // command
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

    // 发送并接收请求
    public void send2Evidence(ReqEvidenceSave reqEvidenceSave, SubmitEvidenceLocal submitEvidenceLocal) {
        try {
            ObjectNode reqEvidence = objectMapper.createObjectNode();
            ObjectNode reqData = objectMapper.createObjectNode();
            // 中心存证
            // TODO: 确认systemID到底是哪个系统的
            reqEvidence.put("systemID", reqEvidenceSave.getSystemID());
            reqEvidence.put("systemIP", reqEvidenceSave.getSystemIP());
            reqEvidence.put("mainCMD", reqEvidenceSave.getMainCMD());
            reqEvidence.put("subCMD", reqEvidenceSave.getSubCMD());
            reqEvidence.put("evidenceID", reqEvidenceSave.getEvidenceID());
            reqEvidence.put("msgVersion", reqEvidenceSave.getMsgVersion());
            reqEvidence.put("reqtime", util.getTime());


            reqData.put("objectSize", reqEvidenceSave.getObjectSize());
            reqData.put("objectMode", reqEvidenceSave.getObjectMode());
            reqEvidenceSave.setDatasign(util.getSM3Hash(reqData.toString().getBytes()));


            reqEvidence.set("data", reqData);
            reqEvidence.put("datasign", reqEvidenceSave.getDatasign());

            log.info(reqEvidenceSave.toString());

            // 发起请求
            Socket remoteSocket = new Socket(evidenceRemoteAddress, evidenceRemotePort);
            TcpPacket reqEvidenceTcpPacket = new TcpPacket(objectMapper.writeValueAsString(reqEvidence), (short) 0x0001, (short) 0x0031, (short) 0x1000);
            byte[] reqEvidenceTcp = reqEvidenceTcpPacket.buildPacket();
            // 发送
            OutputStream remoteOutputStream = remoteSocket.getOutputStream();
            remoteOutputStream.write(reqEvidenceTcp);
            remoteOutputStream.flush();
            System.out.println("存证请求发送成功");

            // 存证响应
            EvidenceResponse evidenceResponse = new EvidenceResponse();

            // 接收中心存证系统返回信息
            InputStream remoteInputStream = remoteSocket.getInputStream();
            DataInputStream remoteDataInputStream = new DataInputStream(remoteInputStream);

            // 读取头部
            byte[] header = new byte[14];
            remoteDataInputStream.read(header);
            // 响应数据域长度
            int responseDataLength = remoteDataInputStream.readInt();
            System.out.println(responseDataLength);
            // 读取数据域内容
            byte[] responseDataBytes = new byte[responseDataLength - 34];
            remoteInputStream.read(responseDataBytes);
            // 认证与校验
            //byte[] auth = new byte[16];
            //remoteInputStream.read(auth);
            String response = new String(responseDataBytes, StandardCharsets.UTF_8);
            //String转json 存储响应信息
            System.out.println("读取校验");
            JsonNode responseJson = objectMapper.readTree(response);
            evidenceResponse.setSystemID(responseJson.get("systemID").asInt());
            evidenceResponse.setMainCMD(responseJson.get("mainCMD").asInt());
            evidenceResponse.setSubCMD(responseJson.get("subCMD").asInt());
            evidenceResponse.setEvidenceID(responseJson.get("evidenceID").asText());
            evidenceResponse.setMsgVersion(responseJson.get("msgVersion").asInt());
            evidenceResponse.setResponsetime(responseJson.get("responsetime").asText());
            evidenceResponse.setNonce(responseJson.get("data").get("nonce").asText());
            evidenceResponse.setPosition(responseJson.get("data").get("optmodel").get("position").asText());
            evidenceResponse.setDataSign(responseJson.get("datasign").asText());
            evidenceResponse.setRandomIdentification(responseJson.get("randomidentification").asText());
            // 保存到数据库
            //evidenceResponseDao.save(evidenceResponse);
            System.out.println(evidenceResponse.getRandomIdentification());

            remoteOutputStream.close();
            remoteInputStream.close();
            System.out.println("中心存证结束");

            // 向本地存证发送存证信息
            Socket localSocket = new Socket(evidenceLocalAddress, evidenceLocalPort);

            // pathTree
            ObjectNode pathTree = objectMapper.createObjectNode();
            ObjectNode parent = objectMapper.createObjectNode();
            parent.put("systemID", submitEvidenceLocal.getSystemID());
            parent.put("globalID", submitEvidenceLocal.getGlobalID());
            ObjectNode self = objectMapper.createObjectNode();
            self.put("systemID", submitEvidenceLocal.getSystemID());
            self.put("globalID", submitEvidenceLocal.getGlobalID());
            self.put("evidenceID", submitEvidenceLocal.getEvidenceID());
            ObjectNode child = objectMapper.createObjectNode();
            child.put("systemID", evidenceSystemId);
            child.put("globalID", submitEvidenceLocal.getGlobalID());
            //self.put("status", "数据已脱敏");
            pathTree.set("parent", parent);
            pathTree.set("self", self);
            pathTree.set("child", child);

            // 填充submitEvidenceLocal
            submitEvidenceLocal.setParentSystemId(submitEvidenceLocal.getSystemID());
            submitEvidenceLocal.setStatus("数据已脱敏");
            submitEvidenceLocal.setChildSystemId(evidenceSystemId);
            // data
            ObjectNode localEvidenceData = objectMapper.createObjectNode();
            localEvidenceData.put("globalID", submitEvidenceLocal.getGlobalID());
            localEvidenceData.put("status", submitEvidenceLocal.getStatus());
            localEvidenceData.put("optTime", util.getTime());
            submitEvidenceLocal.setOptTime(localEvidenceData.get("optTime").asText());
            localEvidenceData.put("fileTitle", submitEvidenceLocal.getFileTitle());
            localEvidenceData.put("fileAbstract", submitEvidenceLocal.getFileAbstract());
            localEvidenceData.put("fileKeyword", submitEvidenceLocal.getFileKeyword());
            localEvidenceData.put("desenAlg", submitEvidenceLocal.getDesenAlg());
            localEvidenceData.put("fileSize", submitEvidenceLocal.getFileSize());
            localEvidenceData.put("fileHASH", submitEvidenceLocal.getFileHASH());
            localEvidenceData.put("fileSig", submitEvidenceLocal.getFileSig());
            localEvidenceData.put("desenPerformer", submitEvidenceLocal.getDesenPerformer());
            localEvidenceData.put("desenCom", submitEvidenceLocal.getDesenCom() + "");
            localEvidenceData.put("desenInfoPreID", submitEvidenceLocal.getDesenInfoPreID());
            localEvidenceData.put("desenInfoAfterID", submitEvidenceLocal.getDesenInfoAfterID());
            localEvidenceData.put("desenIntention", submitEvidenceLocal.getDesenIntention());
            localEvidenceData.put("desenRequirements", submitEvidenceLocal.getDesenRequirements());
            localEvidenceData.put("desenControlSet", submitEvidenceLocal.getDesenControlSet());
            localEvidenceData.put("desenAlgParam", submitEvidenceLocal.getDesenAlgParam());
            localEvidenceData.put("desenPerformStartTime", submitEvidenceLocal.getDesenPerformStartTime());
            localEvidenceData.put("desenPerformEndTime", submitEvidenceLocal.getDesenPerformEndTime());
            localEvidenceData.put("desenLevel", submitEvidenceLocal.getDesenLevel());
            localEvidenceData.set("pathtree", pathTree);
            // 整个json
            ObjectNode localEvidenceJson = objectMapper.createObjectNode();
            localEvidenceJson.put("systemID", submitEvidenceLocal.getSystemID());
            localEvidenceJson.put("systemIP", submitEvidenceLocal.getSystemIP());
            //localEvidenceJson.put("mainCMD", submitEvidenceLocal.getMainCMD());
            localEvidenceJson.put("mainCMD", 0x0003);
            //localEvidenceJson.put("subCMD", submitEvidenceLocal.getSubCMD());
            localEvidenceJson.put("subCMD", 0x0031);
            localEvidenceJson.put("evidenceID", submitEvidenceLocal.getEvidenceID());
            localEvidenceJson.put("submittime", util.getTime());
            localEvidenceJson.put("msgVersion", 0x3110);
            //localEvidenceJson.put("submittime", util.getTime());
            //localEvidenceJson.set("data", localEvidenceData);
            localEvidenceJson.set("data", localEvidenceData);

            localEvidenceJson.put("dataHash", util.getSM3Hash(localEvidenceData.toString().getBytes()));
            //localEvidenceJson.put("datasign", evidenceResponse.getDataSign());
            // 公私钥怎么同步的？
            localEvidenceJson.put("datasign", util.sm2Sign(localEvidenceData.toString().getBytes()));
            localEvidenceJson.put("randomidentification", evidenceResponse.getRandomIdentification());
            // 打印发送json
            String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(localEvidenceJson);
            System.out.println(json);

            TcpPacket localTcpPacket = new TcpPacket(objectMapper.writeValueAsString(localEvidenceJson), (short) 0x0003, (short) 0x0031, (short) 0x3110);
            byte[] localTcp = localTcpPacket.buildPacket();
            // 发送
            System.out.println("向本地存证系统发送");
            OutputStream localOutputStream = localSocket.getOutputStream();
            localOutputStream.write(localTcp);
            localOutputStream.flush();
            System.out.println("发送成功");

            // 相关请求信息存储到数据库
            submitEvidenceLocal.setDataHash(localEvidenceJson.get("dataHash").asText());
            submitEvidenceLocal.setDatasign(evidenceResponse.getDataSign());
            submitEvidenceLocal.setRandomidentification(evidenceResponse.getRandomIdentification());
            submitEvidenceLocalDao.save(submitEvidenceLocal);

            // 接受收据
            InputStream localInputStream = localSocket.getInputStream();
            DataInputStream localDataInputStream = new DataInputStream(localInputStream);

            EvidenceReceiptErr evidenceReceiptErr = new EvidenceReceiptErr();
            EvidenceReceiptNormal evidenceReceiptNormal = new EvidenceReceiptNormal();

            // 读取头部
            byte[] recHeader = new byte[14];
            localDataInputStream.read(recHeader);
            //System.out.println("接受收据");
            // 响应数据域长度
            int recDataLength = localDataInputStream.readInt();
            System.out.println(recDataLength);
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
            System.out.println("接收到的json");
            String recjson = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(recJson);
            System.out.println(recjson);
            //  发生异常
            if (recJson.has("errCode")) {
                evidenceReceiptErr.setSystemID(recJson.get("systemID").asInt());
                evidenceReceiptErr.setMainCMD(recJson.get("mainCMD").asInt());
                evidenceReceiptErr.setSubCMD(recJson.get("subCMD").asInt());
                evidenceReceiptErr.setEvidenceID(recJson.get("evidenceID").asText());
                evidenceReceiptErr.setMsgVersion(recJson.get("msgVersion").asInt());
                // 这里是否应该变动
                evidenceReceiptErr.setStatus("ok");
                evidenceReceiptErr.setErrCode(recJson.get("errCode").asInt());
                //存储
                evidenceReceiptErrDao.save(evidenceReceiptErr);
                if (recJson.get("errCode").asInt() == 0x01) {
                    System.out.println("未进行请求认证");
                } else {
                    System.out.println("上报数据格式不正确");
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
                evidenceReceiptNormal.setCertificateID(recJson.get("data").get("certificateID").asText());
                evidenceReceiptNormal.setCertificateTime(recJson.get("data").get("certificateTime").asText());
                evidenceReceiptNormal.setCertificateHash(recJson.get("certificateHash").asText());
                evidenceReceiptNormal.setCertificateSign(recJson.get("certificateSign").asText());
                // 存储
                evidenceReceiptNormalDao.save(evidenceReceiptNormal);
                System.out.println("存证正常接受");
            }

            localDataInputStream.close();
            localOutputStream.close();

            // 相关请求信息存储到数据库
            reqEvidenceSaveDao.save(reqEvidenceSave);

        } catch (IOException | NoSuchAlgorithmException | InvalidKeySpecException | SignatureException |
                 NoSuchProviderException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }
}

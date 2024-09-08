package com.lu.gademo.model;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.lu.gademo.dao.ga.effectEva.*;
import com.lu.gademo.dao.ga.evidence.*;
import com.lu.gademo.dao.ga.ruleCheck.*;
import com.lu.gademo.dao.ga.split.SendSplitDesenDataDao;
import com.lu.gademo.entity.FileStorageDetails;
import com.lu.gademo.entity.LogCollectResult;
import com.lu.gademo.entity.ga.effectEva.*;
import com.lu.gademo.entity.ga.evidence.*;
import com.lu.gademo.entity.ga.ruleCheck.*;
import com.lu.gademo.entity.ga.split.SendSplitDesenData;
import com.lu.gademo.event.*;
import com.lu.gademo.model.effectEva.EvaluationSystemReturnResult;
import com.lu.gademo.service.*;
import com.lu.gademo.utils.DesenInfoStringBuilders;
import com.lu.gademo.utils.LogCollectUtil;
import com.lu.gademo.utils.LogInfo;
import com.lu.gademo.utils.Util;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Service
@Data
public class LogSenderManager {
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

    // 合规检查系统ip和端口
    @Value("${ruleCheck.address}")
    String ruleCheckAddress;
    @Value("${ruleCheck.port}")
    int ruleCheckPort;

    // 脱敏效果评测系统ip和端口
    @Value("${effectEva.address}")
    String effectEvaAddress;
    @Value("${effectEva.port}")
    int effectEvaPort;

    // 拆分重构系统ip和端口
    @Value("${splitReconstruct.address}")
    String splitReconstructAddress;
    @Value("${splitReconstruct.port}")
    int splitReconstructPort;

    // 存证系统id
    @Value("${systemId.evidenceSystemId}")
    int evidenceSystemId;
    // 评估系统的ID
    @Value("${systemId.evaluationSystemId}")
    int evaluationSystemId;

    // 接收课题2的请求
//    @Autowired
//    SocketRecvService socketRecvService;
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
    /**
     * 工具类
     */
    @Autowired
    Util util;
    ObjectMapper objectMapper = new ObjectMapper();

    // 效果评测Dao
    @Autowired
    private SendEvaReqDao sendEvaReqDao;
    @Autowired
    private RecEvaBgDao recEvaBgDao;
    @Autowired
    private RecEvaResultDao recEvaResultDao;
    @Autowired
    private RecEvaResultInvDao recEvaResultInvDao;
    @Autowired
    private RecEvaReqReceiptDao recEvaReqReceiptDao;
    @Autowired
    private SendEvaReceiptDao sendEvaReceiptDao;

    // 拆分重构Dao
    @Autowired
    SendSplitDesenDataDao sendSplitDesenDataDao;
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

    @Autowired
    private ExcelParamService excelParamService;
    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Autowired
    private EvidenceSystemLogSender evidenceSystemLogSender;
    @Autowired
    private EvaluationSystemLogSender evaluationSystemLogSender;
    @Autowired
    private SplitSystemLogSender splitSystemLogSender;
    @Autowired
    private RuleCheckSystemLogSender ruleCheckSystemLogSender;

    @Value("${logSenderManager.ifSaveToDatabase}")
    private Boolean ifSaveToDatabase;
    @Value("${logSenderManager.ifSendFile}")
    private Boolean ifSaveFile;
    @Autowired
    private LogCollectUtil logCollectUtil;

    @EventListener
    @Async
    public void handleLogManagerEvent(LogManagerEvent logManagerEvent) {
        FileStorageDetails fileStorageDetails = logManagerEvent.getFileStorageDetails();
        SendEvaReq sendEvaReq = logManagerEvent.getSendEvaReq();
        SendRuleReq sendRuleReq = logManagerEvent.getSendRuleReq();
        SubmitEvidenceLocal submitEvidenceLocal = logManagerEvent.getSubmitEvidenceLocal();
        ReqEvidenceSave reqEvidenceSave = logManagerEvent.getReqEvidenceSave();
        SendSplitDesenData sendSplitDesenData = logManagerEvent.getSendSplitDesenData();
        byte[] rawFileBytes = fileStorageDetails.getRawFileBytes();
        byte[] desenFileBytes = fileStorageDetails.getDesenFileBytes();
        CompletableFuture<ResponseEntity<byte[]>> responseEntityCompletableFuture = logManagerEvent.getResponseEntityCompletableFuture();

        String fileDataType = sendRuleReq.getFileDataType();
        String fileType = sendEvaReq.getFileType();
        String fileSuffix = sendEvaReq.getFileSuffix();

        log.info("fileDataType: {}", fileDataType);
        log.info("fileType: {}", fileType);
        log.info("fileSuffix: {}", fileSuffix);

        // TODO: 测试，最后需要改成发送文件
        EvaluationSystemReturnResult evaluationSystemReturnResult = evaluationSystemLogSender.send2EffectEva(
                sendEvaReq, rawFileBytes, desenFileBytes, ifSaveFile);
        if (evaluationSystemReturnResult != null) {
            RecEvaResult recEvaResult = evaluationSystemReturnResult.getRecEvaResult();
            RecEvaResultInv recEvaResultInv = evaluationSystemReturnResult.getRecEvaResultInv();

            if (recEvaResult != null && recEvaResultInv == null) {
                // TODO: 实现不同的数据模态
                HttpHeaders headers = new HttpHeaders();
                switch (fileSuffix) {
                    case "jpg":
                    case "jpeg":
                        headers.setContentType(MediaType.IMAGE_JPEG);
                        break;
                    case "png":
                        headers.setContentType(MediaType.IMAGE_PNG);
                        break;
                    case "mp4":
                        headers.setContentType(MediaType.parseMediaType("video/mp4"));
                        break;
                    default:
                        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
                        break;
                }
                headers.setContentDispositionFormData("attachment", fileStorageDetails.getDesenFileName()); // 示例文件名，可根据实际情况调整
                ResponseEntity<byte[]> responseEntityResult = new ResponseEntity<>(desenFileBytes, headers, HttpStatus.OK);

                // 向其他系统发送日志信息
                eventPublisher.publishEvent(new ThreeSystemsEvent(this, submitEvidenceLocal, reqEvidenceSave, sendRuleReq, sendSplitDesenData, desenFileBytes));
                String entityName = sendEvaReq.getFileType();
                // 将脱敏后的表格文件内容保存到数据库表中
                if (ifSaveToDatabase && (entityName.contains("customer_desen_msg") || entityName.contains("sada_gdpi_click_dtl"))) {

                    eventPublisher.publishEvent(new SaveExcelToDatabaseEvent(this, entityName, fileStorageDetails, responseEntityCompletableFuture, responseEntityResult));
                } else {
                    responseEntityCompletableFuture.complete(responseEntityResult);
                }
            }
            if (recEvaResult == null && recEvaResultInv != null) {
                // 保存脱敏效果评测结果无效异常消息
                eventPublisher.publishEvent(new ReDesensitizeEvent(this, recEvaResultInv, logManagerEvent));
            }
        } else {
            responseEntityCompletableFuture.complete(ResponseEntity.status(500).
                    contentType(MediaType.TEXT_PLAIN).body("与评测系统建立连接失败".getBytes()));
        }
    }

    /**
     * 向评测系统发送日志
     *
     * @param sendEvaReq 评测请求
     */
    public void send2EffectEva(SendEvaReq sendEvaReq, byte[] rawFileData,
                               byte[] desenFileData) {
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
            Socket socket = new Socket(effectEvaAddress, effectEvaPort);
            OutputStream outputStream = socket.getOutputStream();
            // 接收效果评测系统返回信息
            InputStream inputStream = socket.getInputStream();
            DataInputStream dataInputStream = new DataInputStream(inputStream);
            outputStream.write(evaRequestTcpPacket);
            outputStream.flush();

            log.info("已发送脱敏效果评测系统请求");
            // 发送原始文件
                outputStream.write(rawFileData);
                // 发送脱敏后的文件
                outputStream.write(desenFileData);
                outputStream.flush();


            log.info("Chosen algorithm：" + sendEvaReq.getDesenAlg());

            // 效果评测请求信息存储到数据库
            //sendEvaReqDao.save(sendEvaReq);
           /* // 发送前后数据
            DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
            // 原数据
            // 发送数组大小（占四个字节）
            dataOutputStream.writeInt(pre.length);
            System.out.println("原数组" + pre.length);
            // 发送字节数组本身
            dataOutputStream.write(pre);
            dataOutputStream.flush();
            // 脱敏数据
            // 发送数组大小（占四个字节）
            dataOutputStream.writeInt(newExcelData.length);
            System.out.println("脱敏数组" +newExcelData.length);
            // 发送字节数组本身
            dataOutputStream.write(newExcelData);
            dataOutputStream.flush();

            // 参数数据
            // 发送数组大小（占四个字节）
            dataOutputStream.writeInt(params.length);
            System.out.println("参数数组" + params.length);
            // 发送字节数组本身
            dataOutputStream.write(params);
            dataOutputStream.flush();*/


            // 各个实体对象
            RecEvaResultInv recEvaResultInv = new RecEvaResultInv();
            RecEvaResult recEvaResult = new RecEvaResult();
            RecEvaReqReceipt recEvaReqReceipt = new RecEvaReqReceipt();
            SendEvaReceipt sendEvaReceipt = new SendEvaReceipt();

            ObjectMapper mapper = new ObjectMapper();
            String evaResultId = null;

            int recvNum = 2;
            // 依次读取
            for (int i = 0; i < 2; i++) {
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
                // Write dataBytes to file
//                try (OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream("D:\\test.json"), "UTF-8")) {
//                    writer.write(jsonData);
//                    System.out.println("JSON数据已成功以UTF-8编码写入文件");
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
                // 认证与校验
                byte[] auth = new byte[16];
                dataInputStream.read(auth);
                //String 转 json
                JsonNode jsonNode = mapper.readTree(jsonData);
                JsonNode recContent = jsonNode.get("content");

                int dataTypeNum = jsonNode.get("dataType").asInt();
                log.info("脱敏评估系统DataType: " + dataTypeNum);
                // 接收收据
                if (dataTypeNum == 0x3131) {
                    // 获取实体
                    recEvaReqReceipt = mapper.treeToValue(recContent, RecEvaReqReceipt.class);
                    String certificateID = recEvaReqReceipt.getCertificateID();
                    log.info("CertificateID: " + certificateID);
                    // 检测重复
                    if (recEvaReqReceiptDao.existsById(certificateID)) {
                        log.info("Before delete");
                        recEvaReqReceiptDao.deleteById(certificateID);
                        log.info("After delete");
                    }
                    // 插入数据库
                    recEvaReqReceiptDao.save(recEvaReqReceipt);
                    log.info("已接收脱敏效果评测请求收据");

                }
                // 接收脱敏效果评测结果
                else if (dataTypeNum == 0x3132) {
                    // 获取实体
                    recEvaResult = mapper.treeToValue(recContent, RecEvaResult.class);
//                    System.out.println(recEvaResult.toString());
                    evaResultId = recContent.get("evaResultID").asText();
//                    System.out.println(evaResultId);
//                     检测重复
                    if (recEvaResultDao.existsById(recEvaResult.getEvaResultID())) {
                        recEvaResultDao.deleteById(recEvaResult.getEvaResultID());
                    }
                    // 插入数据库
                    recEvaResultDao.save(recEvaResult);
                    recEvaResultInv.setDesenInfoAfterID(recEvaResult.getDesenInfoAfterID());

                    log.info("脱敏效果评测结果：{}", recContent.toPrettyString());
                    log.info("已接收脱敏效果评测结果");
                }
                // 接收脱敏效果测评结果无效异常消息
                else if (dataTypeNum == 0x3401) {
                    // 获取实体
                    recEvaResultInv = mapper.treeToValue(recContent, RecEvaResultInv.class);
//                     检测重复
                    if (recEvaResultInvDao.existsById(recEvaResultInv.getEvaResultID())) {
                        recEvaResultInvDao.deleteById(recEvaResultInv.getEvaResultID());
                    }
//                    eventPublisher.publishEvent(new ReDesensitizeEvent(this, recEvaResultInv));
                    // 插入数据库
                    recEvaResultInvDao.save(recEvaResultInv);
                    log.info("已接收脱敏效果测评结果无效异常消息");
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
        } catch (ConnectException connectException) {
            log.error("未与脱敏效果评测系统建立连接");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
    /**
     * @param sendRuleReq 合规检查请求
     */
    public void send2RuleCheck(SendRuleReq sendRuleReq) {
        // 合规检查请求信息存储到数据库
        sendRuleReqDao.save(sendRuleReq);
//            System.out.println(dataJson.toPrettyString());
        try (
                // 连接服务器
                Socket socket = new Socket(ruleCheckAddress, ruleCheckPort);
                // 发送
                OutputStream outputStream = socket.getOutputStream();
                // 接收合规检查系统返回信息
                InputStream inputStream = socket.getInputStream();
                DataInputStream dataInputStream = new DataInputStream(inputStream);
        ) {
            ObjectNode data = objectMapper.createObjectNode();
            data.put("DataType", 0x3140);
            ObjectNode content = (ObjectNode) objectMapper.readTree(objectMapper.writeValueAsString(sendRuleReq));
            data.set("content", content);
            ObjectNode dataJson = objectMapper.createObjectNode();
            dataJson.set("data", data);
            // 构造数据域
            TcpPacket tcpPacket = new TcpPacket(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(dataJson));
            byte[] tcp = tcpPacket.buildPacket();
            outputStream.write(tcp);
            outputStream.flush();
            log.info("合规请求发送");

            // 各个实体对象
            RecRuleResult ruleResult = new RecRuleResult();
            RecRuleAlg ruleAlg;
            RecRuleInfoType ruleInfoType;
            RecRuleOperate ruleOperate;
            RecRuleReqReceipt ruleReqReceipt = new RecRuleReqReceipt();
            RecRuleTime ruleTime;
            SendRuleReceipt ruleReceipt = new SendRuleReceipt();
            // 读取tcp头
            // 读取头部
            byte[] header = new byte[14];
            dataInputStream.read(header);
            // 响应数据域长度
            int dataLength = dataInputStream.readInt();
            // 读取数据域内容
//            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] dataBytes = new byte[dataLength - 34];
            dataInputStream.read(dataBytes);
//            byteArrayOutputStream.flush();
//            byte[] jsonBytes = byteArrayOutputStream.toByteArray();
            String jsonData = new String(dataBytes, StandardCharsets.UTF_8);
            // 认证与校验
            byte[] auth = new byte[16];
            dataInputStream.read(auth);
            //String 转 json
            JsonNode jsonNode = objectMapper.readTree(jsonData);
            log.info("接收到的合规检查收据：{}", jsonNode.toPrettyString());
            JsonNode recData = jsonNode.get("data");
            JsonNode recContent = recData.get("content");
            log.info("recContent: {}", recContent.toPrettyString());
            int dataType = recData.get("DataType").asInt();
            log.info("合规检查收据DataType: {}", dataType);
            // 接收收据

            // 获取实体
            if (dataType == 0x3141) {
                ruleReqReceipt = objectMapper.treeToValue(recContent, RecRuleReqReceipt.class);
                log.info("ruleReqReceipt: {}", ruleReqReceipt.toString());
                // 检测重复
                if (recRuleReqReceiptDao.existsByCertificateID(ruleReqReceipt.getCertificateID())) {
                    recRuleReqReceiptDao.deleteByCertificateID(ruleReqReceipt.getCertificateID());
                }
                recRuleReqReceiptDao.save(ruleReqReceipt);
                log.info("接收脱敏技术合规请求收据");
            }


//
//            // 标识是否收到对应信息
//            boolean first = true;
//            boolean second = true;
//            boolean third = true;
//            boolean forth = true;
//            boolean fifth = true;
//            boolean sixth = true;
//            // 依次读取
//            //while(first || second || third || forth || fifth || sixth){
//            int i = 0;
//            while (i < 6) {
//                // 读取tcp头
//                // 读取头部
//                byte[] header = new byte[14];
//                dataInputStream.read(header);
//                // 响应数据域长度
//                int dataLength = dataInputStream.readInt();
//                // 读取数据域内容
//                byte[] dataBytes = new byte[dataLength];
//                inputStream.read(dataBytes);
//                String jsonData = new String(dataBytes, StandardCharsets.UTF_8);
//                System.out.println(jsonData);
//                // 认证与校验
//                byte[] auth = new byte[16];
//                inputStream.read(auth);
//                //String 转 json
//                JsonNode jsonNode = objectMapper.readTree(jsonData);
//                JsonNode recData = jsonNode.get("data");
//                JsonNode recContent = jsonNode.get("content");
//                // 接收收据
//                if (recData.get("DataType").asInt() == 0x3141) {
//                    // 获取实体
//                    ruleReqReceipt = objectMapper.treeToValue(recContent, RecRuleReqReceipt.class);
////                     检测重复
////                    recRuleReqReceiptDao.deleteById(ruleReqReceipt.getCertificateId());
////                     插入数据库
////                    recRuleReqReceiptDao.save(ruleReqReceipt);
//                    log.info("接收脱敏技术合规请求收据");
//                    first = false;
//                }
//                // 接收合规检查结果
//                else if (recData.get("DataType").asInt() == 0x3142) {
//                    // 获取实体
//                    /*ruleResult = objectMapper.treeToValue(recContent, RecRuleResult.class);
//                    // 检测重复
//                    //recRuleResultDao.deleteById(ruleResult.getReportId());
//                    // 插入数据库
//                    recRuleResultDao.save(ruleResult);*/
//                    ruleResult.setReportId(jsonNode.get("data").get("content").get("reportID").asText());
//                    second = false;
//                    System.out.println("接收合规检查结果");
//                }
//                // 接收脱敏算法、参数异常消息
//                else if (recData.get("DataType").asInt() == 0x3201) {
//                    // 获取实体
//                   /* ruleAlg = objectMapper.treeToValue(recContent, RecRuleAlg.class);
//                    // 检测重复
//                    //recRuleAlgDao.deleteById(ruleAlg.getReportId());
//                    // 插入数据库
//                    recRuleAlgDao.save(ruleAlg);*/
//                    third = false;
//                    System.out.println("接收脱敏算法、参数异常消息");
//                }
//                // 脱敏后信息类型不符合脱敏要求异常消息
//                else if (recData.get("DataType").asInt() == 0x3202) {
//                    // 获取实体
//                   /* ruleInfoType = objectMapper.treeToValue(recContent, RecRuleInfoType.class);
//                    // 检测重复
//                    //recRuleInfoTypeDao.deleteById(ruleInfoType.getReportId());
//                    // 插入数据库
//                    recRuleInfoTypeDao.save(ruleInfoType);*/
//                    forth = false;
//                    System.out.println("接收信息类型不符合脱敏要求异常消息");
//                }
//                // 接收脱敏过程操作不合规异常消息
//                else if (recData.get("DataType").asInt() == 0x3203) {
//                    // 获取实体
//                  /*  ruleOperate = objectMapper.treeToValue(recContent, RecRuleOperate.class);
//                    // 检测重复
//                    //recRuleOperateDao.deleteById(ruleOperate.getReportId());
//                    // 插入数据库
//                    recRuleOperateDao.save(ruleOperate);*/
//                    fifth = false;
//                    System.out.println("接收脱敏过程操作不合规异常消息");
//                }
//                // 接收脱敏时间不符合脱敏要求异常消息
//                else if (recData.get("DataType").asInt() == 0x3204) {
//                    // 获取实体
//                   /* ruleTime = objectMapper.treeToValue(recContent, RecRuleTime.class);
//                    // 检测重复
//                    //recRuleTimeDao.deleteById(ruleTime.getLogId());
//                    // 插入数据库
//                    recRuleTimeDao.save(ruleTime);*/
//                    sixth = false;
//                    System.out.println("接收脱敏时间不符合脱敏要求异常消息");
//                }
//                i++;
//
//            }
//
//            // 发送收据
//            ruleReceipt.setReportId(ruleResult.getReportId());
//            ruleReceipt.setCertificateId(util.getSM3Hash((ruleResult.getReportId() + util.getTime()).getBytes()));
//            ruleReceipt.setHash(util.getSM3Hash((ruleReceipt.getCertificateId() + ruleReceipt.getReportId()).getBytes()));
//            ObjectNode ruleRecepitContent = (ObjectNode) objectMapper.readTree(objectMapper.writeValueAsString(ruleReceipt));
//            ObjectNode data1 = objectMapper.createObjectNode();
//            data1.put("DataType", 0x3043);
//            data1.set("content", ruleRecepitContent);
//            ObjectNode dataJson1 = objectMapper.createObjectNode();
//            dataJson1.set("data", data1);
//            TcpPacket tcpPacket1 = new TcpPacket(objectMapper.writeValueAsString(dataJson1));
//            byte[] tcp1 = tcpPacket1.buildPacket();
//            // 发送
//            outputStream.write(tcp1);
//            outputStream.flush();
//            // 保存发送的收据
//            sendRuleReceiptDao.save(ruleReceipt);
            socket.close();
            outputStream.close();
            inputStream.close();
            dataInputStream.close();
        } catch (ConnectException connectException) {
            log.error("未与合规检查请求服务器建立连接");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

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
        log.info(String.valueOf(reqEvidenceSave));
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

    /**
     * 构造发送给四个系统的日志信息
     * @param logInfo 存储日志信息所需要的结构体
     * @return 包含四个系统日志信息的结构体
     */
    public LogCollectResult buildLogCollectResults(LogInfo logInfo) {
        return buildLogCollectResults(logInfo.getGlobalID(), logInfo.getEvidenceID(), logInfo.getDesenCom(), logInfo.getObjectMode(),
                logInfo.getInfoBuilders(), logInfo.getRawFileName(), logInfo.getRawFileBytes(), logInfo.getRawFileSize(),
                logInfo.getDesenFileName(), logInfo.getDesenFileBytes(), logInfo.getDesenFileSize(),
                logInfo.getFileType(), logInfo.getRawFileSuffix(), logInfo.getStartTime(), logInfo.getEndTime());
    }

    /**
     * 构造发送给四个系统的日志信息
     * @param globalID
     * @param evidenceID
     * @param desenCom
     * @param objectMode
     * @param infoBuilders
     * @param rawFileName
     * @param rawFileBytes
     * @param rawFileSize
     * @param desenFileName
     * @param desenFileBytes
     * @param desenFileSize
     * @param fileType
     * @param rawFileSuffix
     * @param startTime
     * @param endTime
     * @return 包含四个系统日志信息的结构体
     */
    public LogCollectResult buildLogCollectResults(String globalID, String evidenceID, Boolean desenCom, String objectMode,
                                                   DesenInfoStringBuilders infoBuilders,
                                                   String rawFileName, byte[] rawFileBytes, long rawFileSize,
                                                   String desenFileName, byte[] desenFileBytes, long desenFileSize,
                                                   String fileType, String rawFileSuffix,
                                                   String startTime, String endTime) {
        ReqEvidenceSave reqEvidenceSave = logCollectUtil.buildReqEvidenceSave(rawFileSize, objectMode, evidenceID);
        SubmitEvidenceLocal submitEvidenceLocal = logCollectUtil.buildSubmitEvidenceLocal(evidenceID, infoBuilders.desenAlg, rawFileName,
                rawFileBytes, rawFileSize, desenFileName, desenFileBytes, globalID, infoBuilders.desenInfoPreIden.toString(),
                infoBuilders.desenIntention, infoBuilders.desenRequirements, infoBuilders.desenControlSet,
                infoBuilders.desenAlgParam, startTime, endTime, infoBuilders.desenLevel, desenCom, infoBuilders.fileDataType);
        SendEvaReq sendEvaReq = logCollectUtil.buildSendEvaReq(globalID, evidenceID, rawFileName, rawFileBytes, rawFileSize,
                desenFileName, desenFileBytes, desenFileSize, infoBuilders.desenInfoPreIden, infoBuilders.desenInfoAfterIden,
                infoBuilders.desenIntention, infoBuilders.desenRequirements, infoBuilders.desenControlSet,
                infoBuilders.desenAlg, infoBuilders.desenAlgParam, startTime, endTime, infoBuilders.desenLevel,
                fileType, rawFileSuffix, desenCom);
        SendRuleReq sendRuleReq = logCollectUtil.buildSendRuleReq(evidenceID, rawFileName, rawFileBytes, desenFileName, desenFileBytes,
                infoBuilders.desenInfoAfterIden, infoBuilders.desenIntention,
                infoBuilders.desenRequirements, infoBuilders.desenControlSet, infoBuilders.desenAlg,
                infoBuilders.desenAlgParam, startTime, endTime, infoBuilders.desenLevel, desenCom, infoBuilders.fileDataType);
        SendSplitDesenData sendSplitDesenData = logCollectUtil.buildSendSplitReq(infoBuilders.desenInfoAfterIden, infoBuilders.desenAlg,
                rawFileName, rawFileBytes, desenFileName, desenFileBytes, infoBuilders.desenIntention, infoBuilders.desenRequirements, infoBuilders.desenControlSet,
                infoBuilders.desenAlgParam, startTime, endTime, infoBuilders.desenLevel, desenCom);

        return new LogCollectResult(reqEvidenceSave, submitEvidenceLocal, sendEvaReq, sendRuleReq, sendSplitDesenData);
    }


    /**
     * 接收已构建好的四个日志和原始文件、脱敏后文件字节数组，将日志和文件信息发送给四个系统
     * @param reqEvidenceSave
     * @param submitEvidenceLocal
     * @param sendEvaReq
     * @param sendRuleReq
     * @param sendSplitDesenData
     * @param rawFileBytes
     * @param desenFileBytes
     */
    public void submitToFourSystems(ReqEvidenceSave reqEvidenceSave, SubmitEvidenceLocal submitEvidenceLocal, SendEvaReq sendEvaReq,
                                    SendRuleReq sendRuleReq, SendSplitDesenData sendSplitDesenData, byte[] rawFileBytes, byte[] desenFileBytes){


        ExecutorService executorService = Executors.newFixedThreadPool(4);
        executorService.submit(() -> {
            evidenceSystemLogSender.send2Evidence(reqEvidenceSave, submitEvidenceLocal);
        });
        executorService.submit(() -> {
            evaluationSystemLogSender.send2EffectEva(sendEvaReq, rawFileBytes, desenFileBytes, true);
        });
        executorService.submit(() -> {
            ruleCheckSystemLogSender.send2RuleCheck(sendRuleReq);
        });
        executorService.submit(() -> {
            splitSystemLogSender.send2Split(sendSplitDesenData, desenFileBytes);
        });
        executorService.shutdown();
    }

    /**
     * 接收各种需要的信息，在方法内部构建日志集合，再发送给对应的四个系统
     * @param globalID
     * @param desenCom
     * @param objectMode
     * @param infoBuilders
     * @param rawFileName
     * @param rawFileBytes
     * @param rawFileSize
     * @param desenFileName
     * @param desenFileBytes
     * @param desenFileSize
     * @param fileType
     * @param rawFileSuffix
     * @param startTime
     * @param endTime
     */
    public void submitToFourSystems(String globalID, String evidenceID, Boolean desenCom, String objectMode,
                                    DesenInfoStringBuilders infoBuilders,
                                    String rawFileName, byte[] rawFileBytes, long rawFileSize,
                                    String desenFileName, byte[] desenFileBytes, long desenFileSize,
                                    String fileType, String rawFileSuffix,
                                    String startTime, String endTime) {
        LogCollectResult logCollectResult = buildLogCollectResults(globalID, evidenceID, desenCom, objectMode, infoBuilders,
                rawFileName, rawFileBytes, rawFileSize, desenFileName, desenFileBytes, desenFileSize, fileType, rawFileSuffix,
                startTime, endTime);
        submitToFourSystems(logCollectResult.getReqEvidenceSave(), logCollectResult.getSubmitEvidenceLocal(),
                logCollectResult.getSendEvaReq(), logCollectResult.getSendRuleReq(), logCollectResult.getSendSplitDesenData(),
                rawFileBytes, desenFileBytes);

    }

    /**
     * 使用已构建好的日志集合，直接将日志发送给四个系统
     * @param logCollectResult
     * @param rawFileBytes
     * @param desenFileBytes
     */
    public void submitToFourSystems(LogCollectResult logCollectResult, byte[] rawFileBytes, byte[] desenFileBytes) {
        submitToFourSystems(logCollectResult.getReqEvidenceSave(), logCollectResult.getSubmitEvidenceLocal(),
                logCollectResult.getSendEvaReq(), logCollectResult.getSendRuleReq(), logCollectResult.getSendSplitDesenData(),
                rawFileBytes, desenFileBytes);

    }
}

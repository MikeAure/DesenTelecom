package com.lu.gademo.model;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.lu.gademo.dao.effectEva.*;
import com.lu.gademo.dao.evidence.*;
import com.lu.gademo.dao.ruleCheck.*;
import com.lu.gademo.entity.effectEva.*;
import com.lu.gademo.entity.evidence.*;
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
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.util.LinkedList;
import java.util.List;

@Slf4j
@Service
@Data
public class SendData {
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
    // TODO

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

    /**
     * @param sendEvaReq 评测请求
     */
    public void send2EffectEva(SendEvaReq sendEvaReq, byte[] rawFileData,
                               byte[] desenFileData) {
        try {
            ObjectNode content = (ObjectNode) objectMapper.readTree(objectMapper.writeValueAsString(sendEvaReq));

            // 连接服务器
            Socket socket = new Socket(effectEvaAddress, effectEvaPort);
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
            //System.out.println(dataJson);
            TcpPacket tcpPacket = new TcpPacket(objectMapper.writeValueAsString(dataJson));

//            try (OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream("D:\\test_request.json"), "UTF-8")) {
//                    writer.write(objectMapper.writeValueAsString(dataJson));
//                    System.out.println("JSON数据已成功以UTF-8编码写入文件");
//                } catch (IOException e) {
//                    e.printStackTrace();
//            }

//            BufferedReader bfReader = new BufferedReader(new FileReader("src\\main\\resources\\test_request.json"));
//            BufferedWriter bfWriter = new BufferedWriter((new FileWriter("src\\main\\resources\\test_request2.json")));

            byte[] evaRequestTcpPacket = tcpPacket.buildPacket();
            // 发送
            OutputStream outputStream = socket.getOutputStream();
            outputStream.write(evaRequestTcpPacket);
            outputStream.flush();
            log.info("Send the desensitization request to the evaluation");
            // 本地保存请求
            sendEvaReqDao.save(sendEvaReq);

            // 发送原始文件
            outputStream.write(rawFileData);
            // 发送脱敏后的文件
            outputStream.write(desenFileData);
            outputStream.flush();

//            // 存储文件数据改为存储文件地址
//            sendEvaReq.setDesenInfoAfter(desenFileName);
//            sendEvaReq.setDesenInfoPre(rawFileName);
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

            // 接收效果评测系统返回信息
            InputStream inputStream = socket.getInputStream();
            DataInputStream dataInputStream = new DataInputStream(inputStream);

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
                System.out.println("DataLength: " + dataLength);
                // 读取数据域内容
                byte[] dataBytes = new byte[dataLength - 34];

                dataInputStream.read(dataBytes);
                String jsonData = new String(dataBytes, StandardCharsets.UTF_8);
                System.out.println(jsonData);
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
                JsonNode recData = jsonNode.get("data");
                JsonNode recContent = jsonNode.get("content");

                int dataTypeNum = jsonNode.get("dataType").asInt();
                // 接收收据
                if (dataTypeNum == 0x3131) {

                    // 获取实体
                    recEvaReqReceipt = mapper.treeToValue(recContent, RecEvaReqReceipt.class);
//                     检测重复
                    if (recEvaReqReceiptDao.existsById(recEvaReqReceipt.getCertificateID())) {
                        recEvaReqReceiptDao.deleteById(recEvaReqReceipt.getCertificateID());
                    }
                    // 插入数据库
                    recEvaReqReceiptDao.save(recEvaReqReceipt);

                }
                // 接收脱敏效果评测结果
                else if (dataTypeNum == 0x3132) {

                    // 获取实体
                    recEvaResult = mapper.treeToValue(recContent, RecEvaResult.class);
                    System.out.println(recEvaResult.toString());
                    evaResultId = recContent.get("evaResultID").asText();
                    System.out.println(evaResultId);
//                     检测重复
                    if (recEvaResultDao.existsById(recEvaResult.getEvaResultID())) {
                        recEvaResultDao.deleteById(recEvaResult.getEvaResultID());
                    }
                    // 插入数据库
                    recEvaResultDao.save(recEvaResult);

                }
//                 接收脱敏效果测评结果无效异常消息
                else if (dataTypeNum == 0x3401) {
                    // 获取实体
                    recEvaResultInv = mapper.treeToValue(recContent, RecEvaResultInv.class);
//                     检测重复
                    if (recEvaResultInvDao.existsById(recEvaResultInv.getEvaResultID())) {
                        recEvaResultInvDao.deleteById(recEvaResultInv.getEvaResultID());
                    }
                    // 插入数据库
                    recEvaResultInvDao.save(recEvaResultInv);
                    System.out.println("接收脱敏效果测评结果无效异常消息");

                }
            }

            // 发送收据
            System.out.println("发送脱敏效果收据");
            //System.out.println(recEvaResult.getEvaResultId());
            //sendEvaReceipt.setEvaResultId(recEvaResult.getEvaResultId());
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param sendRuleReq 合规检查请求
     */
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

    public void send2Split() {
        List<Integer> integers = new LinkedList<>();


    }

    /**
     * @param reqEvidenceSave     请求存证
     * @param submitEvidenceLocal 本地存证
     * @param submitEvidenceLocal 中心存证
     */
    public void send2Evidence(ReqEvidenceSave reqEvidenceSave, SubmitEvidenceLocal submitEvidenceLocal) {
        try {
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
            reqEvidence.put("reqtime", util.getTime());
            reqEvidence.set("data", reqData);

            // dataSign: 对data字段的签名
            reqEvidence.put("datasign", reqEvidenceSave.getDatasign());

            System.out.println(reqEvidenceSave);

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
            System.out.println(evidenceResponse.getRandomIdentification());

            remoteOutputStream.close();
            remoteInputStream.close();
            System.out.println("中心存证结束");

            // 向本地存证发送存证信息
            Socket localSocket = new Socket(evidenceLocalAddress, evidenceLocalPort);

            // 填充submitEvidenceLocal
            submitEvidenceLocal.setParentSystemId(submitEvidenceLocal.getSystemID());

            submitEvidenceLocal.setChildSystemId(evidenceSystemId);

            // pathTree
            ObjectNode pathTree = objectMapper.createObjectNode();
            ObjectNode parent = objectMapper.createObjectNode();
            // TODO: 这里的父节点子节点是什么意思？
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

            // data
            ObjectNode localEvidenceData = objectMapper.createObjectNode();
            // globalID: 文件全局ID
            localEvidenceData.put("globalID", submitEvidenceLocal.getGlobalID());
            // status: 状态
            localEvidenceData.put("status", submitEvidenceLocal.getStatus());
            // optTime: 提交日志时间
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
            localEvidenceData.put("fileHASH", submitEvidenceLocal.getFileHASH());
            // fileSig: 脱敏对象签名
            localEvidenceData.put("fileSig", submitEvidenceLocal.getFileSig());
            // desenPerformer: 脱敏执行主体
            localEvidenceData.put("desenPerformer", submitEvidenceLocal.getDesenPerformer());
            // desenCom: 脱敏操作完成情况
            localEvidenceData.put("desenCom", submitEvidenceLocal.getDesenCom() + "");
            // desenInfoPreID: 脱敏前信息ID
            localEvidenceData.put("desenInfoPreID", submitEvidenceLocal.getDesenInfoPreID());
            // desenInfoAfterID: 脱敏后信息ID
            localEvidenceData.put("desenInfoAfterID", submitEvidenceLocal.getDesenInfoAfterID());
            // desenIntention: 脱敏意图
            localEvidenceData.put("desenIntention", submitEvidenceLocal.getDesenIntention());
            // desenRequirements: 脱敏要求
            localEvidenceData.put("desenRequirements", submitEvidenceLocal.getDesenRequirements());
            // desenControlSet: 脱敏控制集合（操作配置）
            localEvidenceData.put("desenControlSet", submitEvidenceLocal.getDesenControlSet());
            // desenAlgParam: 脱敏算法参数
            localEvidenceData.put("desenAlgParam", submitEvidenceLocal.getDesenAlgParam());
            // desenPerformStartTime: 脱敏执行开始时间
            localEvidenceData.put("desenPerformStartTime", submitEvidenceLocal.getDesenPerformStartTime());
            // desenPerformEndTime: 脱敏执行结束时间
            localEvidenceData.put("desenPerformEndTime", submitEvidenceLocal.getDesenPerformEndTime());
            // desenLevel: 脱敏级别
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
            localEvidenceJson.put("submittime", submitEvidenceLocal.getSubmittime());
            localEvidenceJson.put("msgVersion", 0x3110);
            //localEvidenceJson.put("submittime", util.getTime());
            //localEvidenceJson.set("data", localEvidenceData);
            localEvidenceJson.set("data", localEvidenceData);
            // dataHash: 对数据域部分进行hash
            localEvidenceJson.put("dataHash", util.getSM3Hash(localEvidenceData.toString().getBytes()));
            //localEvidenceJson.put("datasign", evidenceResponse.getDataSign());
            // 公私钥怎么同步的？
            // datasign: 中心存证对随机防伪内容的签名
            localEvidenceJson.put("datasign", util.sm2Sign(localEvidenceData.toString().getBytes()));
            // 密文字段（随机标识），确保存证上报前做过请求应答
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

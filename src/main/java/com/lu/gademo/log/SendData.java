package com.lu.gademo.log;

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
import com.lu.gademo.utils.impl.UtilImpl;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
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

    // 拆分重构Dao

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
    Util util = new UtilImpl();
    ObjectMapper objectMapper = new ObjectMapper();

    /**
     *
     * @param content     json数据
     * @param sendEvaReq  评测请求
     * @param rawFileName 原始文件名
     * @param desenFileName 脱敏文件名
     */
    public void send2EffectEva(ObjectNode content, SendEvaReq sendEvaReq, String rawFileName, String desenFileName, byte[] rawFileData,
                               byte[] desenFileData, byte[] params){
        try {

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

            data.put("DataType",0x3130);

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

            BufferedReader bfReader = new BufferedReader(new FileReader("src\\main\\resources\\test_request.json"));
            BufferedWriter bfWriter = new BufferedWriter((new FileWriter("src\\main\\resources\\test_request2.json")));

            byte[] evaRequestTcpPacket = tcpPacket.buildPacket();
            // 发送
            OutputStream outputStream = socket.getOutputStream();
            outputStream.write(evaRequestTcpPacket);
            outputStream.flush();
            log.info("Send the desensitization request to the evaluation");

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
                System.out.println("DataLength: " + String.valueOf(dataLength));
                // 读取数据域内容
                byte[] dataBytes = new byte[dataLength - 34];

                dataInputStream.read(dataBytes);
                String jsonData = new String(dataBytes, "UTF-8");
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
                if (dataTypeNum  == 0x3131){

                    // 获取实体
                    recEvaReqReceipt = mapper.treeToValue(recContent, RecEvaReqReceipt.class);
//                     检测重复
                    if (recEvaReqReceiptDao.existsById(recEvaReqReceipt.getCertificateID())){
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
                    if (recEvaResultDao.existsById(recEvaResult.getEvaResultID())){
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
            sendEvaReceipt.setCertificateID(util.getSM3Hash( (sendEvaReceipt.getEvaResultID()+ util.getTime()).getBytes()));
            sendEvaReceipt.setHash(util.getSM3Hash((sendEvaReceipt.getEvaResultID()+sendEvaReceipt.getCertificateID()).getBytes()));
            ObjectNode evaReceiptContent = (ObjectNode) objectMapper.readTree(objectMapper.writeValueAsString(sendEvaReceipt));
            ObjectNode data1 = objectMapper.createObjectNode();
            data1.put("DataType",0x3130);
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

            // 本地保存请求
            sendEvaReqDao.save(sendEvaReq);
            // 本地保存收据
            sendEvaReceiptDao.save(sendEvaReceipt);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     *
     * @param content       json数据
     * @param sendRuleReq   合规检查请求
     */
    public void send2RuleCheck(ObjectNode content, SendRuleReq sendRuleReq){
        try {
            // 连接服务器
            Socket socket = new Socket(ruleCheckAddress, ruleCheckPort);
            // 构造数据域
            ObjectNode data = objectMapper.createObjectNode();
            data.put("DataType",0x3140);
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
            while(i < 6){
                // 读取tcp头
                // 读取头部
                byte[] header = new byte[14];
                dataInputStream.read(header);
                // 响应数据域长度
                int dataLength = dataInputStream.readInt();
                // 读取数据域内容
                byte[] dataBytes = new byte[dataLength];
                inputStream.read(dataBytes);
                String jsonData = new String(dataBytes, "UTF-8");
                System.out.println(jsonData);
                // 认证与校验
                byte[] auth = new byte[16];
                inputStream.read(auth);
                //String 转 json
                JsonNode jsonNode = objectMapper.readTree(jsonData);
                JsonNode recData = jsonNode.get("data");
                JsonNode recContent = jsonNode.get("content");
                // 接收收据
                if (recData.get("DataType").asInt() == 0x3141){
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
                else if (recData.get("DataType").asInt() == 0x3202){
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
                else if (recData.get("DataType").asInt() == 0x3203){
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
                else if (recData.get("DataType").asInt() == 0x3204){
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
            ruleReceipt.setCertificateId(util.getSM3Hash( (ruleResult.getReportId() + util.getTime()).getBytes()));
            ruleReceipt.setHash(util.getSM3Hash((ruleReceipt.getCertificateId() + ruleReceipt.getReportId()).getBytes()));
            ObjectNode ruleRecepitContent = (ObjectNode) objectMapper.readTree(objectMapper.writeValueAsString(ruleReceipt));
            ObjectNode data1 = objectMapper.createObjectNode();
            data1.put("DataType",0x3043);
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

    public void send2Split(){
        List<Integer> integers = new LinkedList<>();


    }

    /**
     *
     * @param reqEvidenceSave      请求存证
     * @param submitEvidenceLocal   本地存证
     * @param submitEvidenceLocal   中心存证
     */
    public void send2Evidence(ReqEvidenceSave reqEvidenceSave, SubmitEvidenceLocal submitEvidenceLocal){
        try {
            // 中心存证
            ObjectNode reqData = objectMapper.createObjectNode();
            reqData.put("objectSize", reqEvidenceSave.getObjectSize());
            reqData.put("objectMode", reqEvidenceSave.getObjectMode());
            reqEvidenceSave.setDatasign(util.getSM3Hash(reqData.toString().getBytes()));
            ObjectNode reqEvidence = objectMapper.createObjectNode();
            reqEvidence.put("systemID", reqEvidenceSave.getSystemID());
            reqEvidence.put("systemIP", reqEvidenceSave.getSystemIP());
            reqEvidence.put("mainCMD", reqEvidenceSave.getMainCMD());
            reqEvidence.put("subCMD", reqEvidenceSave.getSubCMD());
            reqEvidence.put("evidenceID", reqEvidenceSave.getEvidenceID());
            reqEvidence.put("msgVersion", 0x1000);
            reqEvidence.put("reqtime", util.getTime());
            reqEvidence.set("data", reqData);
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
            localEvidenceData.put("status",submitEvidenceLocal.getStatus());
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
            String receipt = new String(recDataBytes, "UTF-8");
            //String转json 存储响应信息
            JsonNode recJson = objectMapper.readTree(receipt);
            //打印
            System.out.println("接收到的json");
            String recjson = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(recJson);
            System.out.println(recjson);
            //  发生异常
            if (recJson.has("errCode")){
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
                if (recJson.get("errCode").asInt() == 0x01){
                    System.out.println("未进行请求认证");
                }else{
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

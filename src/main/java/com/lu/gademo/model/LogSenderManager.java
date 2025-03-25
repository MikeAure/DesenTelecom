package com.lu.gademo.model;

import cn.hutool.core.io.resource.FileResource;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lu.gademo.dao.ga.effectEva.*;
import com.lu.gademo.dao.ga.evidence.*;
import com.lu.gademo.dao.ga.ruleCheck.*;
import com.lu.gademo.dao.ga.split.SendSplitDesenDataDao;
import com.lu.gademo.entity.FileStorageDetails;
import com.lu.gademo.entity.LogCollectResult;
import com.lu.gademo.entity.ga.effectEva.RecEvaResult;
import com.lu.gademo.entity.ga.effectEva.RecEvaResultInv;
import com.lu.gademo.entity.ga.effectEva.SendEvaReq;
import com.lu.gademo.entity.ga.evidence.ReqEvidenceSave;
import com.lu.gademo.entity.ga.evidence.SubmitEvidenceLocal;
import com.lu.gademo.entity.ga.ruleCheck.SendRuleReq;
import com.lu.gademo.entity.ga.split.SendSplitDesenData;
import com.lu.gademo.event.LogManagerEvent;
import com.lu.gademo.event.ReDesensitizeEvent;
import com.lu.gademo.event.SaveExcelToDatabaseEvent;
import com.lu.gademo.event.ThreeSystemsEvent;
import com.lu.gademo.model.effectEva.EvaluationSystemReturnResult;
import com.lu.gademo.service.*;
import com.lu.gademo.utils.DesenInfoStringBuilders;
import com.lu.gademo.utils.LogCollectUtil;
import com.lu.gademo.utils.LogInfo;
import com.lu.gademo.utils.Util;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.ArrayUtils;
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

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

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
    @Value("${effectEva.waitingTime}")
    int effectEvaWaitingTime;
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
    private final SendEvaReqService sendEvaReqService;
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
    private Boolean ifSendFile;
    @Value("${logSenderManager.ifSendToEvaFirst}")
    private Boolean ifSendToEvaFirst;
    @Value("${logSenderManager.ifPlatformTest}")
    private Boolean ifPlatformTest;
    @Autowired
    private LogCollectUtil logCollectUtil;

    @Value("${pythonMock.evaSendFile}")
    private boolean evaMockSendFile;
    @Value("${pythonMock.splitSendFile}")
    private boolean splitMockSendFile;


    @EventListener
    @Async
    public void handleLogManagerEvent(LogManagerEvent logManagerEvent) {
        FileStorageDetails fileStorageDetails = logManagerEvent.getFileStorageDetails();
        SendEvaReq sendEvaReq = logManagerEvent.getSendEvaReq();
        SendRuleReq sendRuleReq = logManagerEvent.getSendRuleReq();
        SubmitEvidenceLocal submitEvidenceLocal = logManagerEvent.getSubmitEvidenceLocal();
        ReqEvidenceSave reqEvidenceSave = logManagerEvent.getReqEvidenceSave();
        SendSplitDesenData sendSplitDesenData = logManagerEvent.getSendSplitDesenData();
        String entityName = sendEvaReq.getFileType();

        byte[] rawFileBytes = fileStorageDetails.getRawFileBytes();
        byte[] desenFileBytes = fileStorageDetails.getDesenFileBytes();

        CompletableFuture<ResponseEntity<byte[]>> responseEntityCompletableFuture = logManagerEvent.getResponseEntityCompletableFuture();

        String fileDataType = sendRuleReq.getFileDataType();
        String fileType = sendEvaReq.getFileType();
        String fileSuffix = sendEvaReq.getFileSuffix();

        log.info("fileDataType: {}", fileDataType);
        log.info("fileType: {}", fileType);
        log.info("fileSuffix: {}", fileSuffix);
        // 不经过评测系统评测仅发送日志并保存到数据库
        if (ifPlatformTest && (entityName.contains("customer_desen_msg") || entityName.contains("sada_gdpi_click_dtl"))) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            ResponseEntity<byte[]> responseEntityResultTemp = new ResponseEntity<>(desenFileBytes, headers, HttpStatus.OK);
            eventPublisher.publishEvent(new SaveExcelToDatabaseEvent(this, entityName, fileStorageDetails,
                    responseEntityCompletableFuture, responseEntityResultTemp));
            return;
        }

        // 超时自动结束
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<EvaluationSystemReturnResult> future = executorService.submit(() ->
                evaluationSystemLogSender.send2EffectEva(sendEvaReq, rawFileBytes, desenFileBytes, ifSendFile));

//        EvaluationSystemReturnResult evaluationSystemReturnResult = evaluationSystemLogSender.send2EffectEva(
//                sendEvaReq, rawFileBytes, desenFileBytes, ifSendFile);
        try {
            EvaluationSystemReturnResult evaluationSystemReturnResult = future.get(this.effectEvaWaitingTime, TimeUnit.MINUTES);

            if (evaluationSystemReturnResult != null) {
                RecEvaResult recEvaResult = evaluationSystemReturnResult.getRecEvaResult();
                RecEvaResultInv recEvaResultInv = evaluationSystemReturnResult.getRecEvaResultInv();

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
                    case "docx":
                        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
                    default:
                        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
                        break;
                }
                headers.setContentDispositionFormData("attachment", fileStorageDetails.getDesenFileName()); // 示例文件名，可根据实际情况调整
                ResponseEntity<byte[]> responseEntityResult = new ResponseEntity<>(desenFileBytes, headers, HttpStatus.OK);

                if (recEvaResult != null && recEvaResultInv == null) {

                    // 向其他系统发送日志信息
                    eventPublisher.publishEvent(new ThreeSystemsEvent(this, submitEvidenceLocal, reqEvidenceSave, sendRuleReq, sendSplitDesenData, desenFileBytes));
                    // 将脱敏后的表格文件内容保存到数据库表中
                    if (ifSaveToDatabase && (entityName.contains("customer_desen_msg") || entityName.contains("sada_gdpi_click_dtl"))) {
                        eventPublisher.publishEvent(new SaveExcelToDatabaseEvent(this, entityName, fileStorageDetails,
                                responseEntityCompletableFuture, responseEntityResult));
                    } else {
                        responseEntityCompletableFuture.complete(responseEntityResult);
                    }
                }
                if (recEvaResult == null && recEvaResultInv != null) {
                    switch (fileType) {
                        case "image":
                        case "audio":
                        case "video":
                        case "text":
                            if (fileSuffix.equals("docx")) {
                                eventPublisher.publishEvent(new ReDesensitizeEvent(this, recEvaResultInv, logManagerEvent));
                            } else {
                                int desenLevel = Integer.parseInt(recEvaResultInv.getDesenLevel());
                                if (desenLevel == 3) {
                                    responseEntityCompletableFuture.complete(ResponseEntity.status(500).
                                            contentType(MediaType.TEXT_PLAIN).body("已将脱敏等级调整至最高仍无法通过评测，请更换脱敏算法".getBytes()));
                                } else {
                                    eventPublisher.publishEvent(new ReDesensitizeEvent(this, recEvaResultInv, logManagerEvent));
                                }
                            }
                            break;
                        case "graph":
                            int desenLevel = Integer.parseInt(recEvaResultInv.getDesenLevel());
                            if (desenLevel == 3) {
                                responseEntityCompletableFuture.complete(ResponseEntity.status(500).
                                        contentType(MediaType.TEXT_PLAIN).body("已将脱敏等级调整至最高仍无法通过评测，请更换脱敏算法".getBytes()));
                            } else {
                                eventPublisher.publishEvent(new ReDesensitizeEvent(this, recEvaResultInv, logManagerEvent));
                            }
                            break;
                        default:
                            String desenInfoAfterID = recEvaResultInv.getDesenInfoAfterID();
                            String[] updateFieldList = recEvaResultInv.getDesenFailedColName().split(",");
                            log.info("DesenFailedColName {}", recEvaResultInv.getDesenFailedColName());
                            log.info("updateFieldList: {}", Arrays.toString(updateFieldList));
                            SendEvaReq evaReq = sendEvaReqService.findByDesenInfoAfterId(desenInfoAfterID);
                            // 字段名列表
                            String[] attributeNameList = evaReq.getDesenInfoPreIden().split(",");
                            String[] desenLevelList = evaReq.getDesenLevel().split(",");

                            Map<String, Integer> attributeDesenMap = new HashMap<>();
                            for (int i = 0; i < attributeNameList.length; i++) {
                                attributeDesenMap.put(attributeNameList[i], Integer.parseInt(desenLevelList[i]));
                            }
                            int flag = 1;
                            if (ArrayUtils.isEmpty(updateFieldList)) {
                                responseEntityCompletableFuture.complete(ResponseEntity.status(500).
                                        contentType(MediaType.TEXT_PLAIN).body("评测系统返回的脱敏失败列为空".getBytes()));
                            }
                            for (String updateField : updateFieldList) {
                                flag = flag & (attributeDesenMap.get(updateField) == 3 ? 1 : 0);
                            }
                            // 如果所有需要脱敏的字段脱敏等级都已经达到了最高，就直接返回当前的脱敏结果
                            if (flag == 1) {
                                responseEntityCompletableFuture.complete(ResponseEntity.status(500).
                                        contentType(MediaType.TEXT_PLAIN).body("所有失败列已将脱敏等级调整至最高仍无法通过评测，请更换脱敏算法".getBytes()));
                            } else {
                                eventPublisher.publishEvent(new ReDesensitizeEvent(this, recEvaResultInv, logManagerEvent));
                            }
                            break;
                    }

                    // 保存脱敏效果评测结果无效异常消息
                }
            } else {
                responseEntityCompletableFuture.complete(ResponseEntity.status(500).
                        contentType(MediaType.TEXT_PLAIN).body("与评测系统建立连接失败".getBytes()));
            }
        } catch (TimeoutException e) {
            log.error("评测系统超时，未能在规定时间内返回结果", e);
            responseEntityCompletableFuture.complete(ResponseEntity.status(500).
                    contentType(MediaType.TEXT_PLAIN).body("评测系统超时".getBytes()));
        } catch (InterruptedException | ExecutionException e) {
            log.error("调用评测系统时发生错误", e);
            responseEntityCompletableFuture.complete(ResponseEntity.status(500).
                    contentType(MediaType.TEXT_PLAIN).body("调用评测系统时发生错误".getBytes()));
        } finally {
            executorService.shutdown();
        }
    }

    /**
     * 构造发送给四个系统的日志信息
     *
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
     *
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
        String desenFileHash = util.getSM3Hash(ArrayUtils.addAll(desenFileBytes, desenFileName.getBytes(StandardCharsets.UTF_8)));
        String rawFileHash = util.getSM3Hash(ArrayUtils.addAll(rawFileBytes, rawFileName.getBytes(StandardCharsets.UTF_8)));
        String rawFileSig = "";
        try {
            rawFileSig = util.getSM2Sign(rawFileBytes);
        } catch (Exception e) {
            rawFileSig = rawFileHash;
            log.error(e.getMessage());
        }
        ReqEvidenceSave reqEvidenceSave = logCollectUtil.buildReqEvidenceSave(rawFileSize, objectMode, evidenceID);
        SubmitEvidenceLocal submitEvidenceLocal = logCollectUtil.buildSubmitEvidenceLocal(evidenceID, infoBuilders.desenAlg, rawFileName,
                rawFileHash, rawFileSize, desenFileHash, globalID, infoBuilders.desenInfoPreIden.toString(),
                infoBuilders.desenIntention, infoBuilders.desenRequirements, infoBuilders.desenControlSet,
                infoBuilders.desenAlgParam, startTime, endTime, infoBuilders.desenLevel, desenCom, infoBuilders.fileDataType, rawFileSig);
        SendEvaReq sendEvaReq = logCollectUtil.buildSendEvaReq(globalID, evidenceID, rawFileName, rawFileHash, rawFileSize,
                desenFileName, desenFileHash, desenFileSize, infoBuilders.desenInfoPreIden, infoBuilders.desenInfoAfterIden,
                infoBuilders.desenIntention, infoBuilders.desenRequirements, infoBuilders.desenControlSet,
                infoBuilders.desenAlg, infoBuilders.desenAlgParam, startTime, endTime, infoBuilders.desenLevel,
                fileType, rawFileSuffix, desenCom);
        SendRuleReq sendRuleReq = logCollectUtil.buildSendRuleReq(evidenceID, rawFileHash, desenFileHash,
                infoBuilders.desenInfoAfterIden, infoBuilders.desenIntention,
                infoBuilders.desenRequirements, infoBuilders.desenControlSet, infoBuilders.desenAlg,
                infoBuilders.desenAlgParam, startTime, endTime, infoBuilders.desenLevel, desenCom, infoBuilders.fileDataType);
        SendSplitDesenData sendSplitDesenData = logCollectUtil.buildSendSplitReq(infoBuilders.desenInfoAfterIden, infoBuilders.desenAlg,
                rawFileHash, desenFileHash, infoBuilders.desenIntention, infoBuilders.desenRequirements, infoBuilders.desenControlSet,
                infoBuilders.desenAlgParam, startTime, endTime, infoBuilders.desenLevel, desenCom);
        log.info("Build logCollectResults successfully");
        return new LogCollectResult(reqEvidenceSave, submitEvidenceLocal, sendEvaReq, sendRuleReq, sendSplitDesenData);
    }


    /**
     * 接收已构建好的四个日志和原始文件、脱敏后文件字节数组，将日志和文件信息发送给四个系统
     *
     * @param reqEvidenceSave
     * @param submitEvidenceLocal
     * @param sendEvaReq
     * @param sendRuleReq
     * @param sendSplitDesenData
     * @param rawFileBytes
     * @param desenFileBytes
     */
    public void submitToFourSystems(ReqEvidenceSave reqEvidenceSave, SubmitEvidenceLocal submitEvidenceLocal,
                                    SendEvaReq sendEvaReq, SendRuleReq sendRuleReq,
                                    SendSplitDesenData sendSplitDesenData, byte[] rawFileBytes, byte[] desenFileBytes) {

        ExecutorService executorService = Executors.newFixedThreadPool(4);
        executorService.submit(() -> {
            evidenceSystemLogSender.send2Evidence(reqEvidenceSave, submitEvidenceLocal);
        });
        executorService.submit(() -> {
            evaluationSystemLogSender.send2EffectEva(sendEvaReq, rawFileBytes, desenFileBytes, evaMockSendFile);
        });
        executorService.submit(() -> {
            ruleCheckSystemLogSender.send2RuleCheck(sendRuleReq);
        });
        executorService.submit(() -> {
            splitSystemLogSender.send2Split(sendSplitDesenData, desenFileBytes, splitMockSendFile);
        });
        executorService.shutdown();
    }

    /**
     * 接收各种需要的信息，在方法内部构建日志集合，再发送给对应的四个系统
     *
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
     *
     * @param logCollectResult
     * @param rawFileBytes
     * @param desenFileBytes
     */
    public void submitToFourSystems(LogCollectResult logCollectResult, byte[] rawFileBytes, byte[] desenFileBytes) {
        submitToFourSystems(logCollectResult.getReqEvidenceSave(), logCollectResult.getSubmitEvidenceLocal(),
                logCollectResult.getSendEvaReq(), logCollectResult.getSendRuleReq(), logCollectResult.getSendSplitDesenData(),
                rawFileBytes, desenFileBytes);

    }

//    public void submitToTwoSystems(LogCollectResult logCollectResult, FileResource rawFileBytes, byte[] desenFileBytes) {
//        submitToFourSystems(logCollectResult.getReqEvidenceSave(), logCollectResult.getSubmitEvidenceLocal(),
//                logCollectResult.getSendEvaReq(), logCollectResult.getSendRuleReq(), logCollectResult.getSendSplitDesenData(),
//                rawFileBytes, desenFileBytes);
//
//    }
}

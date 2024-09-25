package com.lu.gademo.model;

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

        EvaluationSystemReturnResult evaluationSystemReturnResult = evaluationSystemLogSender.send2EffectEva(
                sendEvaReq, rawFileBytes, desenFileBytes, ifSaveFile);
        if (evaluationSystemReturnResult != null) {
            RecEvaResult recEvaResult = evaluationSystemReturnResult.getRecEvaResult();
            RecEvaResultInv recEvaResultInv = evaluationSystemReturnResult.getRecEvaResultInv();

            if (recEvaResult != null && recEvaResultInv == null) {
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
                    eventPublisher.publishEvent(new SaveExcelToDatabaseEvent(this, entityName, fileStorageDetails,
                            responseEntityCompletableFuture, responseEntityResult));
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
     *
     * @param reqEvidenceSave
     * @param submitEvidenceLocal
     * @param sendEvaReq
     * @param sendRuleReq
     * @param sendSplitDesenData
     * @param rawFileBytes
     * @param desenFileBytes
     */
    public void submitToFourSystems(ReqEvidenceSave reqEvidenceSave, SubmitEvidenceLocal submitEvidenceLocal, SendEvaReq sendEvaReq,
                                    SendRuleReq sendRuleReq, SendSplitDesenData sendSplitDesenData, byte[] rawFileBytes, byte[] desenFileBytes) {

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
}

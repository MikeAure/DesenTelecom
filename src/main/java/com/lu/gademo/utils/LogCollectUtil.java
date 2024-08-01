package com.lu.gademo.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lu.gademo.entity.ExcelParam;
import com.lu.gademo.entity.effectEva.SendEvaReq;
import com.lu.gademo.entity.evidence.ReqEvidenceSave;
import com.lu.gademo.entity.evidence.SubmitEvidenceLocal;
import com.lu.gademo.entity.ruleCheck.SendRuleReq;
import com.lu.gademo.entity.split.SendSplitDesenData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Slf4j
@Component
public class LogCollectUtil {
    private Util util;

    private Integer systemID;

    private Integer evidenceRequestMainCommand;

    private Integer evidenceRequestSubCommand;

    private Integer evidenceRequestMsgVersion;

    private Integer evidenceSubmitMainCommand;

    private Integer evidenceSubmitSubCommand;

    private Integer evidenceSubmitMsgVersion;

    private String desenPerformer;

    @Autowired
    public LogCollectUtil(
            Util util, @Value("${systemId.desenToolsetSystemId}") int systemID,
            @Value("${evidenceSystem.evidenceRequest.mainCommand}") Integer evidenceRequestMainCommand,
            @Value("${evidenceSystem.evidenceRequest.subCommand}") Integer evidenceRequestSubCommand,
            @Value("${evidenceSystem.evidenceRequest.msgVersion}") Integer evidenceRequestMsgVersion,
            @Value("${evidenceSystem.submitEvidence.mainCommand}") Integer evidenceSubmitMainCommand,
            @Value("${evidenceSystem.submitEvidence.subCommand}") Integer evidenceSubmitSubCommand,
            @Value("${evidenceSystem.submitEvidence.msgVersion}") Integer evidenceSubmitMsgVersion) {

        this.util = util;
        this.systemID = systemID;
        this.evidenceRequestMainCommand = evidenceRequestMainCommand;
        this.evidenceRequestSubCommand = evidenceRequestSubCommand;
        this.evidenceRequestMsgVersion = evidenceRequestMsgVersion;
        this.evidenceSubmitMainCommand = evidenceSubmitMainCommand;
        this.evidenceSubmitSubCommand = evidenceSubmitSubCommand;
        this.evidenceSubmitMsgVersion = evidenceSubmitMsgVersion;
        this.desenPerformer = "脱敏工具集";

    }

    // 构造存证请求
    public ReqEvidenceSave buildReqEvidenceSave(Long rawFileSize, String objectMode, String evidenceID) {
        ReqEvidenceSave reqEvidenceSave = new ReqEvidenceSave();
        reqEvidenceSave.setSystemID(systemID);
        reqEvidenceSave.setSystemIP(util.getIP());
        reqEvidenceSave.setMainCMD(evidenceRequestMainCommand);
        reqEvidenceSave.setSubCMD(evidenceRequestSubCommand);
        reqEvidenceSave.setMsgVersion(evidenceRequestMsgVersion);
        reqEvidenceSave.setObjectSize(rawFileSize);
        reqEvidenceSave.setObjectMode(objectMode);
        reqEvidenceSave.setEvidenceID(evidenceID);
        return reqEvidenceSave;
    }

    public SubmitEvidenceLocal buildSubmitEvidenceLocal(String evidenceID, StringBuilder desenAlg,
                                                        String rawFileName, byte[] rawFileBytes, Long rawFileSize,
                                                        byte[] desenFileBytes, String globalID,
                                                        String desenInfoPreIden, StringBuilder desenIntention,
                                                        StringBuilder desenRequirements, String desenControlSet,
                                                        StringBuilder desenAlgParam, String startTime,
                                                        String endTime, StringBuilder desenLevel,
                                                        Boolean desenCom) {
        SubmitEvidenceLocal submitEvidenceLocal = new SubmitEvidenceLocal();
        submitEvidenceLocal.setSystemID(systemID);
        submitEvidenceLocal.setSystemIP(util.getIP());
        submitEvidenceLocal.setMainCMD(evidenceSubmitMainCommand);
        submitEvidenceLocal.setSubCMD(evidenceSubmitSubCommand);
        submitEvidenceLocal.setEvidenceID(evidenceID);
        submitEvidenceLocal.setMsgVersion(evidenceSubmitMsgVersion);

        String rawFileHash = util.getSM3Hash(rawFileBytes);
        String rawFileSig = "";

        try {
            rawFileSig = util.getSM2Sign(rawFileBytes);
        } catch (Exception e) {
            rawFileSig = rawFileHash;
            log.error(e.getMessage());
        }
        String fileTitle = "脱敏工具集脱敏" + rawFileName + "文件存证记录";
        String fileAbstract = "脱敏工具集采用算法" + desenAlg + "脱敏" + rawFileName + "文件存证记录";
        String fileKeyword = rawFileName + "," + desenInfoPreIden;
        String desenFileHash = util.getSM3Hash(desenFileBytes);

        // 设置data中的内容
        submitEvidenceLocal.setGlobalID(globalID);
        submitEvidenceLocal.setStatus("数据已脱敏");
        // optTime在SendData中设置
        submitEvidenceLocal.setFileTitle(fileTitle);
        submitEvidenceLocal.setFileAbstract(fileAbstract);
        submitEvidenceLocal.setFileKeyword(fileKeyword);
        submitEvidenceLocal.setDesenAlg(desenAlg.toString());
        submitEvidenceLocal.setFileSize(rawFileSize);
        submitEvidenceLocal.setFileHash(rawFileHash);
        // TODO: 文件签名是否需要变？
        submitEvidenceLocal.setFileSig(rawFileSig);
        submitEvidenceLocal.setDesenPerformer(desenPerformer);
        // TODO: 这里直接使用是否会引起问题？
        submitEvidenceLocal.setDesenCom(desenCom);
        submitEvidenceLocal.setDesenInfoPreID(rawFileHash);
        submitEvidenceLocal.setDesenInfoAfterID(desenFileHash);
        submitEvidenceLocal.setDesenIntention(desenIntention.toString());
        submitEvidenceLocal.setDesenRequirements(desenRequirements.toString());
        submitEvidenceLocal.setDesenControlSet(desenControlSet);
        submitEvidenceLocal.setDesenAlgParam(desenAlgParam.toString());
        submitEvidenceLocal.setDesenPerformStartTime(startTime);
        submitEvidenceLocal.setDesenPerformEndTime(endTime);
        submitEvidenceLocal.setDesenLevel(desenLevel.toString());
        return submitEvidenceLocal;
    }

    public SendEvaReq buildSendEvaReq(String globalID, String evidenceID,
                                      String rawFileName, byte[] rawFileBytes, Long rawFileSize,
                                      String desenFileName, byte[] desenFileBytes, Long desenFileSize,
                                      StringBuilder desenInfoPreIden, StringBuilder desenInfoAfterIden,
                                      StringBuilder desenIntention, StringBuilder desenRequirements,
                                      String desenControlSet, StringBuilder desenAlg,
                                      StringBuilder desenAlgParam, String startTime, String endTime,
                                      StringBuilder desenLevel, String fileType, String rawFileSuffix,
                                      Boolean desenCom) {
        SendEvaReq sendEvaReq = new SendEvaReq();
        sendEvaReq.setEvaRequestId(util.getSM3Hash((new String(desenFileBytes, StandardCharsets.UTF_8) + util.getTime()).getBytes()));
        sendEvaReq.setSystemID(systemID);
        sendEvaReq.setEvidenceID(evidenceID);
        sendEvaReq.setGlobalID(globalID);
        sendEvaReq.setDesenInfoPreIden(desenInfoPreIden.toString());
        sendEvaReq.setDesenInfoAfterIden(desenInfoAfterIden.toString());
        sendEvaReq.setDesenInfoPreId(util.getSM3Hash(rawFileBytes));
        sendEvaReq.setDesenInfoPre(rawFileName);
        sendEvaReq.setDesenInfoAfterId(util.getSM3Hash(desenFileBytes));
        sendEvaReq.setDesenInfoAfter(desenFileName);
        sendEvaReq.setDesenIntention(desenIntention.toString());
        sendEvaReq.setDesenRequirements(desenRequirements.toString());
        sendEvaReq.setDesenControlSet(desenControlSet);
        sendEvaReq.setDesenAlg(desenAlg.toString());
        sendEvaReq.setDesenAlgParam(desenAlgParam.toString());
        sendEvaReq.setDesenPerformStartTime(startTime);
        sendEvaReq.setDesenPerformEndTime(endTime);
        sendEvaReq.setDesenLevel(desenLevel.toString());
        sendEvaReq.setDesenPerformer(desenPerformer);
        sendEvaReq.setDesenCom(desenCom);
        sendEvaReq.setRawFileSize(rawFileSize);
        sendEvaReq.setDesenFileSize(desenFileSize);
        sendEvaReq.setFileType(fileType);
        sendEvaReq.setFileSuffix(rawFileSuffix);
        sendEvaReq.setStatus("数据已脱敏");
        return sendEvaReq;
    }

    public List<ExcelParam> jsonStringToParams(String params) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(params, new TypeReference<List<ExcelParam>>() {
        });

    }

    public SendRuleReq buildSendRuleReq(String evidenceID, byte[] rawFileBytes, byte[] desenFileBytes,
                                        StringBuilder desenInfoAfterIden, StringBuilder desenIntention,
                                        StringBuilder desenRequirements, String desenControlSet,
                                        StringBuilder desenAlg, StringBuilder desenAlgParam,
                                        String startTime, String endTime, StringBuilder desenLevel,
                                        Boolean desenCom, StringBuilder dataType
    ) {
        SendRuleReq sendRuleReq = new SendRuleReq();
        sendRuleReq.setEvidenceId(evidenceID);
        sendRuleReq.setDesenInfoAfterIden(desenInfoAfterIden.toString());
        sendRuleReq.setDataType(dataType.toString());
        sendRuleReq.setDesenInfoPre(util.getSM3Hash(rawFileBytes));
        sendRuleReq.setDesenInfoAfter(util.getSM3Hash(desenFileBytes));
        sendRuleReq.setDesenIntention(desenIntention.toString());
        sendRuleReq.setDesenRequirements(desenRequirements.toString());
        sendRuleReq.setDesenControlSet(desenControlSet);
        sendRuleReq.setDesenAlg(desenAlg.toString());
        sendRuleReq.setDesenAlgParam(desenAlgParam.toString());
        sendRuleReq.setDesenPerformStartTime(startTime);
        sendRuleReq.setDesenPerformEndTime(endTime);
        sendRuleReq.setDesenLevel(desenLevel.toString());
        sendRuleReq.setDesenPerformer(desenPerformer);
        // TODO: 改成变量的形式
        sendRuleReq.setDesenCom(desenCom);
        return sendRuleReq;
    }

    public SendSplitDesenData buildSendSplitReq(StringBuilder desenInfoAfterIden, StringBuilder desenAlg,
                                                byte[] rawFileBytes, byte[] desenFileBytes, StringBuilder desenIntention,
                                                StringBuilder desenRequirements, String desenControlSet,
                                                StringBuilder desenAlgParam, String startTime,
                                                String endTime, StringBuilder desenLevel,
                                                Boolean desenCom) {
        SendSplitDesenData sendSplitDesenData = new SendSplitDesenData();

        String rawFileHash = util.getSM3Hash(rawFileBytes);
        String desenFileHash = util.getSM3Hash(desenFileBytes);

        sendSplitDesenData.setDesenInfoAfterIden(desenInfoAfterIden.toString());
        sendSplitDesenData.setDesenInfoPreID(rawFileHash);
        sendSplitDesenData.setDesenInfoAfterID(desenFileHash);
        sendSplitDesenData.setDesenIntention(desenIntention.toString());
        sendSplitDesenData.setDesenRequirements(desenRequirements.toString());
        sendSplitDesenData.setDesenControlSet(desenControlSet);
        sendSplitDesenData.setDesenAlg(desenAlg.toString());
        sendSplitDesenData.setDesenAlgParam(desenAlgParam.toString());
        sendSplitDesenData.setDesenPerformStartTime(startTime);
        sendSplitDesenData.setDesenPerformEndTime(endTime);
        sendSplitDesenData.setDesenLevel(desenLevel.toString());
        sendSplitDesenData.setDesenPerformer(desenPerformer);
        sendSplitDesenData.setDesenCom(desenCom);

        return sendSplitDesenData;
    }

    public void logExecutionTime(String executionTime, String objectMode) {
        log.info("Desensitization finished in " + executionTime + "ms");
        log.info(objectMode + " desensitization finished");
    }
}

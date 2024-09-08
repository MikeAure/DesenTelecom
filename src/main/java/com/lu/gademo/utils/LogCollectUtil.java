package com.lu.gademo.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lu.gademo.entity.ExcelParam;
import com.lu.gademo.entity.ga.effectEva.SendEvaReq;
import com.lu.gademo.entity.ga.evidence.ReqEvidenceSave;
import com.lu.gademo.entity.ga.evidence.SubmitEvidenceLocal;
import com.lu.gademo.entity.ga.ruleCheck.SendRuleReq;
import com.lu.gademo.entity.ga.split.SendSplitDesenData;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
                                                        String desenFileName, byte[] desenFileBytes, String globalID,
                                                        String desenInfoPreIden, StringBuilder desenIntention,
                                                        StringBuilder desenRequirements, String desenControlSet,
                                                        StringBuilder desenAlgParam, String startTime,
                                                        String endTime, StringBuilder desenLevel,
                                                        Boolean desenCom, StringBuilder fileDataType) {
        SubmitEvidenceLocal submitEvidenceLocal = new SubmitEvidenceLocal();
        String desenFileHash = util.getSM3Hash(ArrayUtils.addAll(desenFileBytes, desenFileName.getBytes(StandardCharsets.UTF_8)));
        submitEvidenceLocal.setSystemID(systemID);
        submitEvidenceLocal.setSystemIP(util.getIP());
        submitEvidenceLocal.setMainCMD(evidenceSubmitMainCommand);
        submitEvidenceLocal.setSubCMD(evidenceSubmitSubCommand);
        submitEvidenceLocal.setEvidenceID(desenFileHash);
        submitEvidenceLocal.setMsgVersion(evidenceSubmitMsgVersion);


        String rawFileHash = util.getSM3Hash(ArrayUtils.addAll(rawFileBytes, rawFileName.getBytes(StandardCharsets.UTF_8)));
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
        submitEvidenceLocal.setFileDataType(fileDataType.toString());
        log.info("Build submitEvidenceLocal finished");
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
//        byte[] rawFileNameBytes = rawFileName.getBytes(StandardCharsets.UTF_8);
//        byte[] desenFileNameBytes = desenFileName.getBytes(StandardCharsets.UTF_8);
//        byte[] combined = new byte[rawFileBytes.length + rawFileNameBytes.length];
//        byte[] desenCombined = new byte[desenFileNameBytes.length + desenFileBytes.length];
//        System.arraycopy(rawFileNameBytes, 0, combined, 0, rawFileNameBytes.length);
//        System.arraycopy(rawFileBytes, 0, combined, rawFileNameBytes.length, rawFileBytes.length);
//        System.arraycopy(desenFileNameBytes, 0, desenCombined, 0, desenFileNameBytes.length);
//        System.arraycopy(desenFileBytes, 0, desenCombined, desenFileNameBytes.length, desenFileBytes.length);
        String desenInfoSm3Hash = util.getSM3Hash(ArrayUtils.addAll(desenFileBytes, desenFileName.getBytes(StandardCharsets.UTF_8)));
        sendEvaReq.setEvaRequestId(desenInfoSm3Hash);
        sendEvaReq.setSystemID(systemID);
        sendEvaReq.setEvidenceID(desenInfoSm3Hash);
        sendEvaReq.setGlobalID(globalID);
        sendEvaReq.setDesenInfoPreIden(desenInfoPreIden.toString());
        sendEvaReq.setDesenInfoAfterIden(desenInfoAfterIden.toString());
        sendEvaReq.setDesenInfoPreId(util.getSM3Hash(ArrayUtils.addAll(rawFileBytes, rawFileName.getBytes(StandardCharsets.UTF_8))));
        sendEvaReq.setDesenInfoPre(rawFileName);
        sendEvaReq.setDesenInfoAfterId(desenInfoSm3Hash);
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

    public Map<String, ExcelParam> jsonStringToParamsMap(String params) throws IOException {
        Map<String, ExcelParam> result = new HashMap<>();
        ObjectMapper objectMapper = new ObjectMapper();
        List<ExcelParam> excelParamList = objectMapper.readValue(params, new TypeReference<List<ExcelParam>>() {
        });
        for (ExcelParam excelParam :
                excelParamList) {
            result.put(excelParam.getColumnName(), excelParam);
        }

        return result;
    }

    public SendRuleReq buildSendRuleReq(String evidenceID, String rawFileName, byte[] rawFileBytes,
                                        String desenFileName, byte[] desenFileBytes,
                                        StringBuilder desenInfoAfterIden, StringBuilder desenIntention,
                                        StringBuilder desenRequirements, String desenControlSet,
                                        StringBuilder desenAlg, StringBuilder desenAlgParam,
                                        String startTime, String endTime, StringBuilder desenLevel,
                                        Boolean desenCom, StringBuilder dataType
    ) {
        SendRuleReq sendRuleReq = new SendRuleReq();
        String desenInfoSm3Hash = util.getSM3Hash(ArrayUtils.addAll(desenFileBytes, desenFileName.getBytes(StandardCharsets.UTF_8)));
        sendRuleReq.setEvidenceId(desenInfoSm3Hash);
        sendRuleReq.setDesenInfoAfterIden(desenInfoAfterIden.toString());
        sendRuleReq.setFileDataType(dataType.toString());
        sendRuleReq.setDesenInfoPre(util.getSM3Hash(ArrayUtils.addAll(rawFileBytes, rawFileName.getBytes(StandardCharsets.UTF_8))));
        sendRuleReq.setDesenInfoAfter(desenInfoSm3Hash);
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
                                                String rawFileName, byte[] rawFileBytes,
                                                String desenFileName, byte[] desenFileBytes, StringBuilder desenIntention,
                                                StringBuilder desenRequirements, String desenControlSet,
                                                StringBuilder desenAlgParam, String startTime,
                                                String endTime, StringBuilder desenLevel,
                                                Boolean desenCom) {
        SendSplitDesenData sendSplitDesenData = new SendSplitDesenData();

        String rawFileHash = util.getSM3Hash(ArrayUtils.addAll(rawFileBytes, rawFileName.getBytes(StandardCharsets.UTF_8)));
        String desenFileHash = util.getSM3Hash(ArrayUtils.addAll(desenFileBytes, desenFileName.getBytes(StandardCharsets.UTF_8)));

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

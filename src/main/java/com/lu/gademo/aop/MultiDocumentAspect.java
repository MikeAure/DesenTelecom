package com.lu.gademo.aop;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lu.gademo.dto.FileInfoDto;
import com.lu.gademo.dto.OFDMessage;
import com.lu.gademo.dto.officeComment.DesensitizationOperation;
import com.lu.gademo.dto.officeComment.InformationRecognition;
import com.lu.gademo.dto.officeComment.ProcessDocumentResult;
import com.lu.gademo.dto.officeComment.WordComment;
import com.lu.gademo.entity.FileStorageDetails;
import com.lu.gademo.entity.LogCollectResult;
import com.lu.gademo.model.DocumentTypeMapping;
import com.lu.gademo.model.LogSenderManager;
import com.lu.gademo.service.EvaluationSystemLogSender;
import com.lu.gademo.service.FileStorageService;
import com.lu.gademo.service.RemoteCallService;
import com.lu.gademo.service.impl.EvaluationSystemLogSenderImpl;
import com.lu.gademo.utils.DesenInfoStringBuilders;
import com.lu.gademo.utils.LogInfo;
import com.lu.gademo.utils.OFDMessageFactory;
import com.lu.gademo.utils.Util;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.util.List;
import java.util.UUID;

@Slf4j
@Aspect
@Component
public class MultiDocumentAspect {

    private final EvaluationSystemLogSender evaluationSystemLogSender;
    private Util util;
    private RemoteCallService remoteCallService;
    private OFDMessageFactory ofdMessageFactory;


    private final String evaluationUrl;
    private final String deleteUrl;
    private final String evidenceUrl;
    private final String deleteLevelUrl;
    private final LogSenderManager logSenderManager;
    private final FileStorageService fileStorageService;
    private final ObjectMapper MAPPER = new ObjectMapper();
    public MultiDocumentAspect(Util util, RemoteCallService remoteCallService,
                               OFDMessageFactory ofdMessageFactory,
                               @Value("${sendUrl.evaluation}") String evaluationUrl,
                               @Value("${sendUrl.localEvidence}") String evidenceUrl,
                               @Value("${sendUrl.delete}") String deleteUrl,
                               @Value("${sendUrl.delegeLevels}")String deleteLevelUrl,
                               LogSenderManager logSenderManager, EvaluationSystemLogSenderImpl evaluationSystemLogSenderImpl, FileStorageService fileStorageService) {
        this.util = util;
        this.remoteCallService = remoteCallService;
        this.ofdMessageFactory = ofdMessageFactory;
        this.evaluationUrl = evaluationUrl;
        this.deleteUrl = deleteUrl;
        this.deleteLevelUrl = deleteLevelUrl;
        this.evidenceUrl = evidenceUrl;
        this.logSenderManager = logSenderManager;
        this.evaluationSystemLogSender = evaluationSystemLogSenderImpl;
        this.fileStorageService = fileStorageService;
    }

    @Pointcut("execution(public * com.lu.gademo.utils.MultiDocumentProcessor.process*(..)) && @annotation(com.lu.gademo.annotation.SendLog)")
    public void addLog() {

    }
    @Around("addLog()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        FileStorageDetails fileStorageDetails = (FileStorageDetails) args[0];
        FileInfoDto fileInfoDto = (FileInfoDto) args[1];


        String startTime = util.getTime();
        ProcessDocumentResult processDocumentResult = null;
        try {
            processDocumentResult = (ProcessDocumentResult) joinPoint.proceed();
        } catch (Exception e) {
            log.error(e.getMessage());
            throw e;
        }
        if (processDocumentResult == null) {
            throw new IllegalArgumentException("processResult is null");
        }
        // 如果是重新脱敏的文件，需要发送对应的原始文件信息
        if (fileInfoDto.isRedesen()) {
            FileStorageDetails temp = fileStorageService.saveRawFileWithDesenInfoByLog(fileInfoDto.getEvaluationLog());
            fileStorageDetails.setRawFilePathString(temp.getRawFilePathString());
            fileStorageDetails.setRawFileName(temp.getRawFileName());
            fileStorageDetails.setRawFileSize(temp.getRawFileSize());
            fileStorageDetails.setRawFileSuffix(temp.getRawFileSuffix());
            fileStorageDetails.setRawFilePath(temp.getRawFilePath());
        }
        String endTime = util.getTime();
        fileStorageDetails.setDesenFileSize(Files.size(fileStorageDetails.getDesenFilePath()));
        if (Files.exists(fileStorageDetails.getDesenFilePath()) &&
                Files.exists(fileStorageDetails.getRawFilePath())) {
            byte[] rawFileBytes = Files.readAllBytes(fileStorageDetails.getRawFilePath());
            byte[] desenFileBytes = Files.readAllBytes(fileStorageDetails.getDesenFilePath());
            String objectMode = fileStorageDetails.getRawFileSuffix();

            String parentFileId = util.getSM3Hash(rawFileBytes);
            String selfFileId = util.getSM3Hash(desenFileBytes);

            String evidenceID = util.getSM3Hash(ArrayUtils.addAll(desenFileBytes, util.getTime().getBytes()));
            OFDMessage ofdMessage = ofdMessageFactory.createOfdMessage(
                    evidenceID, fileInfoDto.getGlobalID(),
                    fileStorageDetails.getRawFilePathString(), parentFileId,
                    fileStorageDetails.getDesenFilePathString(), selfFileId,
                    fileStorageDetails.getDesenFilePathString(), selfFileId,
                    endTime,
                    UUID.randomUUID().toString());


            System.out.println(ofdMessage);
            // 调用异步发送JSON结构数据
            remoteCallService.sendCirculationLog(ofdMessage, deleteUrl);
            remoteCallService.sendCirculationLog(ofdMessage, evidenceUrl);
            remoteCallService.sendLevels(processDocumentResult.getSendToCourse(), deleteLevelUrl);
            // 构造对应信息
            DesenInfoStringBuilders infoStringBuilders = buildDesenInfoBuilders(processDocumentResult, parentFileId,
                    selfFileId, objectMode);
            System.out.println(infoStringBuilders);

            LogInfo logInfo = generateLogInfo(fileStorageDetails, objectMode, fileInfoDto.getGlobalID(),
                    evidenceID, startTime, endTime, infoStringBuilders);
            LogCollectResult logCollectResult = logSenderManager.buildLogCollectResultsForDocuments(logInfo,
                    parentFileId, selfFileId, ofdMessage.getDatasign());
            // 设置发送给评测系统的日志
            fileInfoDto.setEvaluationLog(
                    evaluationSystemLogSender.createSendEvaReqObjectNode(logCollectResult.getSendEvaReq()));

            System.out.println(MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(fileInfoDto));
            remoteCallService.sendMultipartData (
                    fileStorageDetails.getRawFilePath(),
                    fileStorageDetails.getDesenFilePath(),
                    fileInfoDto, evaluationUrl
            );
            logSenderManager.submitToThreeSystems(logCollectResult);

        }
        return processDocumentResult;
    }

    private DesenInfoStringBuilders buildDesenInfoBuilders(ProcessDocumentResult processDocxResult,
                                                           String rawFileDataHash, String desenFileDataHash,
                                                           String fileDataType) {
        DesenInfoStringBuilders desenInfoStringBuilders = new DesenInfoStringBuilders();
        List<WordComment> wordCommentList = processDocxResult.getWordComments();
        desenInfoStringBuilders.desenInfoPreIden.append(rawFileDataHash);
        desenInfoStringBuilders.desenInfoAfterIden.append(desenFileDataHash);
        desenInfoStringBuilders.fileDataType.append(fileDataType);
        desenInfoStringBuilders.desenIntention.append("对").append(fileDataType).append("脱敏");
        for (int i = 0; i < wordCommentList.size(); i++) {
            WordComment wordComment = wordCommentList.get(i);
            InformationRecognition informationRecognition = wordComment.getInformationRecognition();
            int dataType = DocumentTypeMapping.getValueByName(informationRecognition.getContentType());
            DesensitizationOperation.AlgorithmChosen algorithmChosen = wordComment.getDesensitizationOperation().getAlgorithmChosen();
            String requirementString = String.format("%d-%s-%d-%d-%s", i, informationRecognition.getAttributeName(),
                    dataType, algorithmChosen.getDesenAlgNum(), algorithmChosen.getDesenAlgParam());
            desenInfoStringBuilders.desenRequirements.append(requirementString).append(",");
            desenInfoStringBuilders.desenAlgParam.append(algorithmChosen.getDesenAlgParam()).append(",");
            desenInfoStringBuilders.desenLevel.append(algorithmChosen.getParameterMagnitude()).append(",");
            desenInfoStringBuilders.desenAlg.append(algorithmChosen.getDesenAlgNum()).append(",");
        }
        return desenInfoStringBuilders;
    }
    private LogInfo generateLogInfo(FileStorageDetails fileStorageDetails, String objectMode, String globalId,
                                    String evidenceId, String startTime, String endTime, DesenInfoStringBuilders infoBuilders) {
        return LogInfo.builder().fileType(objectMode).desenFileSize(fileStorageDetails.getDesenFileSize())
                .desenFileName(fileStorageDetails.getDesenFileName()).desenCom(true).evidenceID(evidenceId)
                .globalID(globalId).objectMode(objectMode).rawFileName(fileStorageDetails.getRawFileName())
                .rawFileSize(fileStorageDetails.getRawFileSize())
                .desenFileName(fileStorageDetails.getDesenFileName())
                .desenFileSize(fileStorageDetails.getDesenFileSize())
                .startTime(startTime).endTime(endTime)
                .infoBuilders(infoBuilders).rawFileSuffix(fileStorageDetails.getRawFileSuffix()).build();

    }
}

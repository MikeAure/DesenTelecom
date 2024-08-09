package com.lu.gademo.event;

import com.lu.gademo.entity.FileStorageDetails;
import com.lu.gademo.entity.LogCollectResult;
import com.lu.gademo.entity.ga.effectEva.SendEvaReq;
import com.lu.gademo.entity.ga.evidence.ReqEvidenceSave;
import com.lu.gademo.entity.ga.evidence.SubmitEvidenceLocal;
import com.lu.gademo.entity.ga.ruleCheck.SendRuleReq;
import com.lu.gademo.entity.ga.split.SendSplitDesenData;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEvent;

import java.util.Arrays;
import java.util.Objects;

@Getter
@Slf4j

// 包含发送给四个系统的所有请求
public class LogManagerEvent extends ApplicationEvent {
    private final FileStorageDetails fileStorageDetails;
    private final SubmitEvidenceLocal submitEvidenceLocal;
    private final ReqEvidenceSave reqEvidenceSave;
    private final SendEvaReq sendEvaReq;
    private final SendSplitDesenData sendSplitDesenData;
    private final SendRuleReq sendRuleReq;
    private final byte[] rawFileBytes;
    private final byte[] desenFileBytes;

    public LogManagerEvent(Object source, FileStorageDetails fileStorageDetails, LogCollectResult logCollectResult,
                           byte[] rawFileBytes, byte[] desenFileBytes) {
        super(source);
        this.fileStorageDetails = fileStorageDetails;
        this.submitEvidenceLocal = logCollectResult.getSubmitEvidenceLocal();
        this.reqEvidenceSave = logCollectResult.getReqEvidenceSave();
        this.sendEvaReq = logCollectResult.getSendEvaReq();
        this.sendSplitDesenData = logCollectResult.getSendSplitDesenData();
        this.sendRuleReq = logCollectResult.getSendRuleReq();
        this.rawFileBytes = rawFileBytes;
        this.desenFileBytes = desenFileBytes;
    }

    public LogManagerEvent(Object source, SubmitEvidenceLocal submitEvidenceLocal, ReqEvidenceSave reqEvidenceSave,
                           SendEvaReq sendEvaReq, SendSplitDesenData sendSplitDesenData, SendRuleReq sendRuleReq,
                           byte[] rawFileBytes, byte[] desenFileBytes) {
        super(source);
        this.fileStorageDetails = null;
        this.submitEvidenceLocal = submitEvidenceLocal;
        this.reqEvidenceSave = reqEvidenceSave;
        this.sendEvaReq = sendEvaReq;
        this.sendSplitDesenData = sendSplitDesenData;
        this.sendRuleReq = sendRuleReq;
        this.rawFileBytes = rawFileBytes;
        this.desenFileBytes = desenFileBytes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LogManagerEvent)) return false;
        LogManagerEvent that = (LogManagerEvent) o;
        return Objects.equals(getFileStorageDetails(), that.getFileStorageDetails()) && Objects.equals(getSubmitEvidenceLocal(), that.getSubmitEvidenceLocal()) && Objects.equals(getReqEvidenceSave(), that.getReqEvidenceSave()) && Objects.equals(getSendEvaReq(), that.getSendEvaReq()) && Objects.equals(getSendSplitDesenData(), that.getSendSplitDesenData()) && Objects.equals(getSendRuleReq(), that.getSendRuleReq()) && Arrays.equals(getRawFileBytes(), that.getRawFileBytes()) && Arrays.equals(getDesenFileBytes(), that.getDesenFileBytes());
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(getFileStorageDetails(), getSubmitEvidenceLocal(), getReqEvidenceSave(), getSendEvaReq(), getSendSplitDesenData(), getSendRuleReq());
        result = 31 * result + Arrays.hashCode(getRawFileBytes());
        result = 31 * result + Arrays.hashCode(getDesenFileBytes());
        return result;
    }

    @Override
    public String toString() {
        return "LogManagerEvent{" +
                "fileStorageDetails=" + fileStorageDetails +
                ", submitEvidenceLocal=" + submitEvidenceLocal +
                ", reqEvidenceSave=" + reqEvidenceSave +
                ", sendEvaReq=" + sendEvaReq +
                ", sendSplitDesenData=" + sendSplitDesenData +
                ", sendRuleReq=" + sendRuleReq +
                ", rawFileBytes=" + Arrays.toString(rawFileBytes) +
                ", desenFileBytes=" + Arrays.toString(desenFileBytes) +
                '}';
    }
}

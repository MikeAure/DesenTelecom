package com.lu.gademo.event;

import com.lu.gademo.entity.ga.evidence.ReqEvidenceSave;
import com.lu.gademo.entity.ga.evidence.SubmitEvidenceLocal;
import com.lu.gademo.entity.ga.ruleCheck.SendRuleReq;
import com.lu.gademo.entity.ga.split.SendSplitDesenData;
import org.springframework.context.ApplicationEvent;

import java.util.Arrays;
import java.util.Objects;

/**
 * 该事件用于保存脱敏前后文件信息和发送给其它系统的实体类信息

 */
public class ThreeSystemsEvent extends ApplicationEvent {
    private final SubmitEvidenceLocal submitEvidenceLocal;
    private final ReqEvidenceSave reqEvidenceSave;
    private final SendRuleReq sendRuleReq;
    private final SendSplitDesenData sendSplitDesenData;
    private final byte[] desenFileData;

    public ThreeSystemsEvent(Object source, SubmitEvidenceLocal submitEvidenceLocal, ReqEvidenceSave reqEvidenceSave, SendRuleReq sendRuleReq, SendSplitDesenData sendSplitDesenData, byte[] desenFileData) {
        super(source);
        this.submitEvidenceLocal = submitEvidenceLocal;
        this.reqEvidenceSave = reqEvidenceSave;
        this.sendRuleReq = sendRuleReq;
        this.sendSplitDesenData = sendSplitDesenData;
        this.desenFileData = desenFileData;
    }

    public SubmitEvidenceLocal getSubmitEvidenceLocal() {
        return submitEvidenceLocal;
    }

    public ReqEvidenceSave getReqEvidenceSave() {
        return reqEvidenceSave;
    }

    public SendRuleReq getSendRuleReq() {
        return sendRuleReq;
    }

    public SendSplitDesenData getSendSplitDesenData() {
        return sendSplitDesenData;
    }

    public byte[] getDesenFileData() {
        return desenFileData;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ThreeSystemsEvent)) return false;
        ThreeSystemsEvent that = (ThreeSystemsEvent) o;
        return Objects.equals(getSubmitEvidenceLocal(), that.getSubmitEvidenceLocal()) && Objects.equals(getReqEvidenceSave(), that.getReqEvidenceSave()) && Objects.equals(getSendRuleReq(), that.getSendRuleReq()) && Objects.equals(getSendSplitDesenData(), that.getSendSplitDesenData()) && Arrays.equals(getDesenFileData(), that.getDesenFileData());
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(getSubmitEvidenceLocal(), getReqEvidenceSave(), getSendRuleReq(), getSendSplitDesenData());
        result = 31 * result + Arrays.hashCode(getDesenFileData());
        return result;
    }
}

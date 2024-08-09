package com.lu.gademo.entity;

import com.lu.gademo.entity.ga.effectEva.SendEvaReq;
import com.lu.gademo.entity.ga.evidence.ReqEvidenceSave;
import com.lu.gademo.entity.ga.evidence.SubmitEvidenceLocal;
import com.lu.gademo.entity.ga.ruleCheck.SendRuleReq;
import com.lu.gademo.entity.ga.split.SendSplitDesenData;

import java.util.Objects;

public class LogCollectResult {
    private ReqEvidenceSave reqEvidenceSave;
    private SubmitEvidenceLocal submitEvidenceLocal;
    private SendEvaReq sendEvaReq;
    private SendRuleReq sendRuleReq;
    private SendSplitDesenData sendSplitDesenData;

    public LogCollectResult(ReqEvidenceSave reqEvidenceSave, SubmitEvidenceLocal submitEvidenceLocal, SendEvaReq sendEvaReq, SendRuleReq sendRuleReq, SendSplitDesenData sendSplitDesenData) {
        this.reqEvidenceSave = reqEvidenceSave;
        this.submitEvidenceLocal = submitEvidenceLocal;
        this.sendEvaReq = sendEvaReq;
        this.sendRuleReq = sendRuleReq;
        this.sendSplitDesenData = sendSplitDesenData;
    }

    public ReqEvidenceSave getReqEvidenceSave() {
        return reqEvidenceSave;
    }

    public SubmitEvidenceLocal getSubmitEvidenceLocal() {
        return submitEvidenceLocal;
    }

    public SendEvaReq getSendEvaReq() {
        return sendEvaReq;
    }

    public SendRuleReq getSendRuleReq() {
        return sendRuleReq;
    }

    public SendSplitDesenData getSendSplitDesenData() {
        return sendSplitDesenData;
    }

    public void setReqEvidenceSave(ReqEvidenceSave reqEvidenceSave) {
        this.reqEvidenceSave = reqEvidenceSave;
    }

    public void setSubmitEvidenceLocal(SubmitEvidenceLocal submitEvidenceLocal) {
        this.submitEvidenceLocal = submitEvidenceLocal;
    }

    public void setSendEvaReq(SendEvaReq sendEvaReq) {
        this.sendEvaReq = sendEvaReq;
    }

    public void setSendRuleReq(SendRuleReq sendRuleReq) {
        this.sendRuleReq = sendRuleReq;
    }

    public void setSendSplitDesenData(SendSplitDesenData sendSplitDesenData) {
        this.sendSplitDesenData = sendSplitDesenData;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LogCollectResult)) return false;
        LogCollectResult that = (LogCollectResult) o;
        return Objects.equals(getReqEvidenceSave(), that.getReqEvidenceSave()) && Objects.equals(getSubmitEvidenceLocal(), that.getSubmitEvidenceLocal()) && Objects.equals(getSendEvaReq(), that.getSendEvaReq()) && Objects.equals(getSendRuleReq(), that.getSendRuleReq()) && Objects.equals(getSendSplitDesenData(), that.getSendSplitDesenData());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getReqEvidenceSave(), getSubmitEvidenceLocal(), getSendEvaReq(), getSendRuleReq(), getSendSplitDesenData());
    }
}


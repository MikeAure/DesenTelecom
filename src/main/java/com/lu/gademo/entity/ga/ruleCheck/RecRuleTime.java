package com.lu.gademo.entity.ga.ruleCheck;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "rec_rule_time")
public class RecRuleTime {

    @Id
    @Column(name = "logid")
    private String logId;
    @Basic
    @Column(name = "logtype")
    private Integer logType;
    @Basic
    @Column(name = "logowner")
    private String logOwner;
    @Basic
    @Column(name = "logcreatetime")
    private String logCreateTime;
    @Basic
    @Column(name = "conforcheckperformer")
    private String conforCheckPerformer;
    @Basic
    @Column(name = "conforcheckperformtime")
    private String conforCheckPerformTime;
    @Basic
    @Column(name = "modelalgid")
    private Integer modelAlgId;
    @Basic
    @Column(name = "modelalgparam")
    private String modelAlgParam;
    @Basic
    @Column(name = "checkalgid")
    private Integer checkAlgId;
    @Basic
    @Column(name = "checkalgparam")
    private String checkAlgParam;
    @Basic
    @Column(name = "fittraceset")
    private String fitTraceSet;
    @Basic
    @Column(name = "unfittraceset")
    private String unfitTraceSet;
    @Basic
    @Column(name = "fitness")
    private Double fitness;
    @Basic
    @Column(name = "desenreqduration")
    private String desenReqDuration;
    @Basic
    @Column(name = "desenperformduration")
    private String desenPerformDuration;

    public String getLogId() {
        return logId;
    }

    public void setLogId(String logId) {
        this.logId = logId;
    }

    public Integer getLogType() {
        return logType;
    }

    public void setLogType(Integer logType) {
        this.logType = logType;
    }

    public String getLogOwner() {
        return logOwner;
    }

    public void setLogOwner(String logOwner) {
        this.logOwner = logOwner;
    }

    public String getLogCreateTime() {
        return logCreateTime;
    }

    public void setLogCreateTime(String logCreateTime) {
        this.logCreateTime = logCreateTime;
    }

    public String getConforCheckPerformer() {
        return conforCheckPerformer;
    }

    public void setConforCheckPerformer(String conforCheckPerformer) {
        this.conforCheckPerformer = conforCheckPerformer;
    }

    public String getConforCheckPerformTime() {
        return conforCheckPerformTime;
    }

    public void setConforCheckPerformTime(String conforCheckPerformTime) {
        this.conforCheckPerformTime = conforCheckPerformTime;
    }

    public Integer getModelAlgId() {
        return modelAlgId;
    }

    public void setModelAlgId(Integer modelAlgId) {
        this.modelAlgId = modelAlgId;
    }

    public String getModelAlgParam() {
        return modelAlgParam;
    }

    public void setModelAlgParam(String modelAlgParam) {
        this.modelAlgParam = modelAlgParam;
    }

    public Integer getCheckAlgId() {
        return checkAlgId;
    }

    public void setCheckAlgId(Integer checkAlgId) {
        this.checkAlgId = checkAlgId;
    }

    public String getCheckAlgParam() {
        return checkAlgParam;
    }

    public void setCheckAlgParam(String checkAlgParam) {
        this.checkAlgParam = checkAlgParam;
    }

    public String getFitTraceSet() {
        return fitTraceSet;
    }

    public void setFitTraceSet(String fitTraceSet) {
        this.fitTraceSet = fitTraceSet;
    }

    public String getUnfitTraceSet() {
        return unfitTraceSet;
    }

    public void setUnfitTraceSet(String unfitTraceSet) {
        this.unfitTraceSet = unfitTraceSet;
    }

    public Double getFitness() {
        return fitness;
    }

    public void setFitness(Double fitness) {
        this.fitness = fitness;
    }

    public String getDesenReqDuration() {
        return desenReqDuration;
    }

    public void setDesenReqDuration(String desenReqDuration) {
        this.desenReqDuration = desenReqDuration;
    }

    public String getDesenPerformDuration() {
        return desenPerformDuration;
    }

    public void setDesenPerformDuration(String desenPerformDuration) {
        this.desenPerformDuration = desenPerformDuration;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RecRuleTime that = (RecRuleTime) o;
        return Objects.equals(logId, that.logId) && Objects.equals(logType, that.logType) && Objects.equals(logOwner, that.logOwner) && Objects.equals(logCreateTime, that.logCreateTime) && Objects.equals(conforCheckPerformer, that.conforCheckPerformer) && Objects.equals(conforCheckPerformTime, that.conforCheckPerformTime) && Objects.equals(modelAlgId, that.modelAlgId) && Objects.equals(modelAlgParam, that.modelAlgParam) && Objects.equals(checkAlgId, that.checkAlgId) && Objects.equals(checkAlgParam, that.checkAlgParam) && Objects.equals(fitTraceSet, that.fitTraceSet) && Objects.equals(unfitTraceSet, that.unfitTraceSet) && Objects.equals(fitness, that.fitness) && Objects.equals(desenReqDuration, that.desenReqDuration) && Objects.equals(desenPerformDuration, that.desenPerformDuration);
    }

    @Override
    public int hashCode() {
        return Objects.hash(logId, logType, logOwner, logCreateTime, conforCheckPerformer, conforCheckPerformTime, modelAlgId, modelAlgParam, checkAlgId, checkAlgParam, fitTraceSet, unfitTraceSet, fitness, desenReqDuration, desenPerformDuration);
    }
}

package com.lu.gademo.entity.ga.ruleCheck;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "rec_rule_operate")
public class RecRuleOperate {

    @Id
    @Column(name = "reportid")
    private String reportId;
    @Basic
    @Column(name = "infoid")
    private String infoId;
    @Basic
    @Column(name = "infotype")
    private Integer infoType;
    @Basic
    @Column(name = "infocontent")
    private String infoContent;
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
    @Column(name = "deseninfopre")
    private String desenInfoPre;
    @Basic
    @Column(name = "deseninfocreator")
    private String desenInfoCreator;
    @Basic
    @Column(name = "deseninfocreatetime")
    private String desenInfoCreateTime;
    @Basic
    @Column(name = "desencontrolset")
    private String desenControlSet;
    @Basic
    @Column(name = "desenintention")
    private String desenIntention;
    @Basic
    @Column(name = "desenrequirements")
    private String desenRequirements;
    @Basic
    @Column(name = "desenperformer")
    private String desenPerformer;
    @Basic
    @Column(name = "desenperformtime")
    private String desenPerformTime;
    @Basic
    @Column(name = "desenlevel")
    private Integer desenLevel;
    @Basic
    @Column(name = "desenalgid")
    private String desenAlgId;
    @Basic
    @Column(name = "desenalgparam")
    private String desenAlgParam;
    @Basic
    @Column(name = "deseninfoafter")
    private String desenInfoAfter;
    @Basic
    @Column(name = "deseninfoaftercomp")
    private Integer desenInfoAfterComp;

    public String getReportId() {
        return reportId;
    }

    public void setReportId(String reportId) {
        this.reportId = reportId;
    }

    public String getInfoId() {
        return infoId;
    }

    public void setInfoId(String infoId) {
        this.infoId = infoId;
    }

    public Integer getInfoType() {
        return infoType;
    }

    public void setInfoType(Integer infoType) {
        this.infoType = infoType;
    }

    public String getInfoContent() {
        return infoContent;
    }

    public void setInfoContent(String infoContent) {
        this.infoContent = infoContent;
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

    public String getDesenInfoPre() {
        return desenInfoPre;
    }

    public void setDesenInfoPre(String desenInfoPre) {
        this.desenInfoPre = desenInfoPre;
    }

    public String getDesenInfoCreator() {
        return desenInfoCreator;
    }

    public void setDesenInfoCreator(String desenInfoCreator) {
        this.desenInfoCreator = desenInfoCreator;
    }

    public String getDesenInfoCreateTime() {
        return desenInfoCreateTime;
    }

    public void setDesenInfoCreateTime(String desenInfoCreateTime) {
        this.desenInfoCreateTime = desenInfoCreateTime;
    }

    public String getDesenControlSet() {
        return desenControlSet;
    }

    public void setDesenControlSet(String desenControlSet) {
        this.desenControlSet = desenControlSet;
    }

    public String getDesenIntention() {
        return desenIntention;
    }

    public void setDesenIntention(String desenIntention) {
        this.desenIntention = desenIntention;
    }

    public String getDesenRequirements() {
        return desenRequirements;
    }

    public void setDesenRequirements(String desenRequirements) {
        this.desenRequirements = desenRequirements;
    }

    public String getDesenPerformer() {
        return desenPerformer;
    }

    public void setDesenPerformer(String desenPerformer) {
        this.desenPerformer = desenPerformer;
    }

    public String getDesenPerformTime() {
        return desenPerformTime;
    }

    public void setDesenPerformTime(String desenPerformTime) {
        this.desenPerformTime = desenPerformTime;
    }

    public Integer getDesenLevel() {
        return desenLevel;
    }

    public void setDesenLevel(Integer desenLevel) {
        this.desenLevel = desenLevel;
    }

    public String getDesenAlgId() {
        return desenAlgId;
    }

    public void setDesenAlgId(String desenAlgId) {
        this.desenAlgId = desenAlgId;
    }

    public String getDesenAlgParam() {
        return desenAlgParam;
    }

    public void setDesenAlgParam(String desenAlgParam) {
        this.desenAlgParam = desenAlgParam;
    }

    public String getDesenInfoAfter() {
        return desenInfoAfter;
    }

    public void setDesenInfoAfter(String desenInfoAfter) {
        this.desenInfoAfter = desenInfoAfter;
    }

    public Integer getDesenInfoAfterComp() {
        return desenInfoAfterComp;
    }

    public void setDesenInfoAfterComp(Integer desenInfoAfterComp) {
        this.desenInfoAfterComp = desenInfoAfterComp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RecRuleOperate that = (RecRuleOperate) o;
        return Objects.equals(reportId, that.reportId) && Objects.equals(infoId, that.infoId) && Objects.equals(infoType, that.infoType) && Objects.equals(infoContent, that.infoContent) && Objects.equals(conforCheckPerformer, that.conforCheckPerformer) && Objects.equals(conforCheckPerformTime, that.conforCheckPerformTime) && Objects.equals(modelAlgId, that.modelAlgId) && Objects.equals(modelAlgParam, that.modelAlgParam) && Objects.equals(checkAlgId, that.checkAlgId) && Objects.equals(checkAlgParam, that.checkAlgParam) && Objects.equals(fitTraceSet, that.fitTraceSet) && Objects.equals(unfitTraceSet, that.unfitTraceSet) && Objects.equals(fitness, that.fitness) && Objects.equals(desenInfoPre, that.desenInfoPre) && Objects.equals(desenInfoCreator, that.desenInfoCreator) && Objects.equals(desenInfoCreateTime, that.desenInfoCreateTime) && Objects.equals(desenControlSet, that.desenControlSet) && Objects.equals(desenIntention, that.desenIntention) && Objects.equals(desenRequirements, that.desenRequirements) && Objects.equals(desenPerformer, that.desenPerformer) && Objects.equals(desenPerformTime, that.desenPerformTime) && Objects.equals(desenLevel, that.desenLevel) && Objects.equals(desenAlgId, that.desenAlgId) && Objects.equals(desenAlgParam, that.desenAlgParam) && Objects.equals(desenInfoAfter, that.desenInfoAfter) && Objects.equals(desenInfoAfterComp, that.desenInfoAfterComp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(reportId, infoId, infoType, infoContent, conforCheckPerformer, conforCheckPerformTime, modelAlgId, modelAlgParam, checkAlgId, checkAlgParam, fitTraceSet, unfitTraceSet, fitness, desenInfoPre, desenInfoCreator, desenInfoCreateTime, desenControlSet, desenIntention, desenRequirements, desenPerformer, desenPerformTime, desenLevel, desenAlgId, desenAlgParam, desenInfoAfter, desenInfoAfterComp);
    }
}

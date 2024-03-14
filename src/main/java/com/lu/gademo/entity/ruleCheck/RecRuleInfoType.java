package com.lu.gademo.entity.ruleCheck;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "rec_rule_info_type")
public class RecRuleInfoType {
    @Id
    @Column(name = "reportid")
    private String reportId;
    @Basic
    @Column(name = "infotype")
    private Integer infoType;
    @Basic
    @Column(name = "infocontent")
    private String infoContent;
    @Basic
    @Column(name = "infoowner")
    private String infoOwner;
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
    @Column(name = "deseninfoafteriden")
    private String desenInfoAfterIden;
    @Basic
    @Column(name = "deseninfoaftercomp")
    private Integer desenInfoAfterComp;

    public String getReportId() {
        return reportId;
    }

    public void setReportId(String reportId) {
        this.reportId = reportId;
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

    public String getInfoOwner() {
        return infoOwner;
    }

    public void setInfoOwner(String infoOwner) {
        this.infoOwner = infoOwner;
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

    public String getDesenInfoAfterIden() {
        return desenInfoAfterIden;
    }

    public void setDesenInfoAfterIden(String desenInfoAfterIden) {
        this.desenInfoAfterIden = desenInfoAfterIden;
    }

    public Integer getDesenInfoAfterComp() {
        return desenInfoAfterComp;
    }

    public void setDesenInfoAfterComp(Integer desenInfoAfterComp) {
        this.desenInfoAfterComp = desenInfoAfterComp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecRuleInfoType that = (RecRuleInfoType) o;
        return Objects.equals(reportId, that.reportId) && Objects.equals(infoType, that.infoType) && Objects.equals(infoContent, that.infoContent) && Objects.equals(infoOwner, that.infoOwner) && Objects.equals(desenInfoPre, that.desenInfoPre) && Objects.equals(desenInfoCreator, that.desenInfoCreator) && Objects.equals(desenInfoCreateTime, that.desenInfoCreateTime) && Objects.equals(desenControlSet, that.desenControlSet) && Objects.equals(desenIntention, that.desenIntention) && Objects.equals(desenRequirements, that.desenRequirements) && Objects.equals(desenPerformer, that.desenPerformer) && Objects.equals(desenPerformTime, that.desenPerformTime) && Objects.equals(desenLevel, that.desenLevel) && Objects.equals(desenAlgId, that.desenAlgId) && Objects.equals(desenAlgParam, that.desenAlgParam) && Objects.equals(desenInfoAfter, that.desenInfoAfter) && Objects.equals(desenInfoAfterIden, that.desenInfoAfterIden) && Objects.equals(desenInfoAfterComp, that.desenInfoAfterComp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(reportId, infoType, infoContent, infoOwner, desenInfoPre, desenInfoCreator, desenInfoCreateTime, desenControlSet, desenIntention, desenRequirements, desenPerformer, desenPerformTime, desenLevel, desenAlgId, desenAlgParam, desenInfoAfter, desenInfoAfterIden, desenInfoAfterComp);
    }
}

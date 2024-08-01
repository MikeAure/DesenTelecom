package com.lu.gademo.entity.effectEva;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "rec_eva_result_inv")
// 脱敏效果测评结果无效异常消息
public class RecEvaResultInv {

    @Id
    @Column(name = "evaresultid")
    private String evaResultID;
    @Basic
    @Column(name = "evaperformer")
    private String evaPerformer;
    @Basic
    @Column(name = "deseninfopreid")
    private String desenInfoPreID;
    @Basic
    @Column(name = "deseninfoafterid")
    private String desenInfoAfterID;
    @Basic
    @Column(name = "desenintention")
    private String desenIntention;
    @Basic
    @Column(name = "desenrequirements")
    private String desenRequirements;
    @Basic
    @Column(name = "desencontrolset")
    private String desenControlSet;
    @Basic
    @Column(name = "desenalg")
    private String desenAlg;
    @Basic
    @Column(name = "desenalgparam")
    private String desenAlgParam;
    @Basic
    @Column(name = "desenperformstarttime")
    private String desenPerformStartTime;
    @Basic
    @Column(name = "desenperformendtime")
    private String desenPerformEndTime;
    @Basic
    @Column(name = "desenlevel")
    private String desenLevel;
    @Basic
    @Column(name = "desenperformer")
    private String desenPerformer;
    @Basic
    @Column(name = "desencom")
    private Boolean desenCom;
//    @Basic
//    @Column(name = "desentestperformtime")
//    private String desenTestPerformTime;
    @Basic
    @Column(name = "desendeviation")
    private Integer desenDeviation;
    @Basic
    @Column(name = "desenextendedcontrol")
    private Integer desenExtendedcontrol;
    @Basic
    @Column(name = "deseninformationloss")
    private Integer desenInformationloss;
    @Basic
    @Column(name = "desenusability")
    private Integer desenUsability;
    @Basic
    @Column(name = "desencomplexity")
    private Integer desenComplexity;
    @Basic
    @Column(name = "deseneffectevaret")
    private Boolean desenEffectEvaRet;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RecEvaResultInv)) return false;
        RecEvaResultInv that = (RecEvaResultInv) o;
        return Objects.equals(evaResultID, that.evaResultID) && Objects.equals(evaPerformer, that.evaPerformer) && Objects.equals(desenInfoPreID, that.desenInfoPreID) && Objects.equals(desenInfoAfterID, that.desenInfoAfterID) && Objects.equals(desenIntention, that.desenIntention) && Objects.equals(desenRequirements, that.desenRequirements) && Objects.equals(desenControlSet, that.desenControlSet) && Objects.equals(desenAlg, that.desenAlg) && Objects.equals(desenAlgParam, that.desenAlgParam) && Objects.equals(desenPerformStartTime, that.desenPerformStartTime) && Objects.equals(desenPerformEndTime, that.desenPerformEndTime) && Objects.equals(desenLevel, that.desenLevel) && Objects.equals(desenPerformer, that.desenPerformer) && Objects.equals(desenCom, that.desenCom) && Objects.equals(desenDeviation, that.desenDeviation) && Objects.equals(desenExtendedcontrol, that.desenExtendedcontrol) && Objects.equals(desenInformationloss, that.desenInformationloss) && Objects.equals(desenUsability, that.desenUsability) && Objects.equals(desenComplexity, that.desenComplexity) && Objects.equals(desenEffectEvaRet, that.desenEffectEvaRet);
    }

    public String getEvaPerformer() {
        return evaPerformer;
    }

    public void setEvaPerformer(String evaPerformer) {
        this.evaPerformer = evaPerformer;
    }

    public String getDesenInfoPreID() {
        return desenInfoPreID;
    }

    public void setDesenInfoPreID(String desenInfoPreID) {
        this.desenInfoPreID = desenInfoPreID;
    }

    public String getDesenInfoAfterID() {
        return desenInfoAfterID;
    }

    public void setDesenInfoAfterID(String desenInfoAfterID) {
        this.desenInfoAfterID = desenInfoAfterID;
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

    public String getDesenControlSet() {
        return desenControlSet;
    }

    public void setDesenControlSet(String desenControlSet) {
        this.desenControlSet = desenControlSet;
    }

    public String getDesenAlg() {
        return desenAlg;
    }

    public void setDesenAlg(String desenAlg) {
        this.desenAlg = desenAlg;
    }

    public String getDesenAlgParam() {
        return desenAlgParam;
    }

    public void setDesenAlgParam(String desenAlgParam) {
        this.desenAlgParam = desenAlgParam;
    }

    public String getDesenPerformStartTime() {
        return desenPerformStartTime;
    }

    public void setDesenPerformStartTime(String desenPerformStartTime) {
        this.desenPerformStartTime = desenPerformStartTime;
    }

    public String getDesenPerformEndTime() {
        return desenPerformEndTime;
    }

    public void setDesenPerformEndTime(String desenPerformEndTime) {
        this.desenPerformEndTime = desenPerformEndTime;
    }

    public String getDesenLevel() {
        return desenLevel;
    }

    public void setDesenLevel(String desenLevel) {
        this.desenLevel = desenLevel;
    }

    public String getDesenPerformer() {
        return desenPerformer;
    }

    public void setDesenPerformer(String desenPerformer) {
        this.desenPerformer = desenPerformer;
    }

    public Boolean getDesenCom() {
        return desenCom;
    }

    public void setDesenCom(Boolean desenCom) {
        this.desenCom = desenCom;
    }

    public Integer getDesenDeviation() {
        return desenDeviation;
    }

    public void setDesenDeviation(Integer desenDeviation) {
        this.desenDeviation = desenDeviation;
    }

    public Integer getDesenExtendedcontrol() {
        return desenExtendedcontrol;
    }

    public void setDesenExtendedcontrol(Integer desenExtendedcontrol) {
        this.desenExtendedcontrol = desenExtendedcontrol;
    }

    public Integer getDesenInformationloss() {
        return desenInformationloss;
    }

    public void setDesenInformationloss(Integer desenInformationloss) {
        this.desenInformationloss = desenInformationloss;
    }

    public Integer getDesenUsability() {
        return desenUsability;
    }

    public void setDesenUsability(Integer desenUsability) {
        this.desenUsability = desenUsability;
    }

    public Integer getDesenComplexity() {
        return desenComplexity;
    }

    public void setDesenComplexity(Integer desenComplexity) {
        this.desenComplexity = desenComplexity;
    }

    public Boolean getDesenEffectEvaRet() {
        return desenEffectEvaRet;
    }

    public void setDesenEffectEvaRet(Boolean desenEffectEvaRet) {
        this.desenEffectEvaRet = desenEffectEvaRet;
    }

    public String getEvaResultID() {
        return evaResultID;
    }

    public void setEvaResultID(String evaResultID) {
        this.evaResultID = evaResultID;
    }

    @Override
    public int hashCode() {
        return Objects.hash(evaResultID, evaPerformer, desenInfoPreID, desenInfoAfterID, desenIntention, desenRequirements, desenControlSet, desenAlg, desenAlgParam, desenPerformStartTime, desenPerformEndTime, desenLevel, desenPerformer, desenCom, desenDeviation, desenExtendedcontrol, desenInformationloss, desenUsability, desenComplexity, desenEffectEvaRet);
    }
}

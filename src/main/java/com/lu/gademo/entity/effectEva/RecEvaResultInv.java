package com.lu.gademo.entity.effectEva;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "rec_eva_result_inv")
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
    private Integer desenAlg;
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
    private Integer desenLevel;
    @Basic
    @Column(name = "desenperformer")
    private String desenPerformer;
    @Basic
    @Column(name = "desencom")
    private Integer desenCom;
    @Basic
    @Column(name = "desentestperformtime")
    private String desenTestPerformTime;
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
    @Column(name = "desenreversibility")
    private Integer desenReversibility;
    @Basic
    @Column(name = "desencomplexity")
    private Integer desenComplexity;
    @Basic
    @Column(name = "deseneffectevaret")
    private Integer desenEffectEvaRet;

    public String getEvaResultID() {
        return evaResultID;
    }

    public void setEvaResultID(String evaResultId) {
        this.evaResultID = evaResultId;
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

    public void setDesenInfoPreID(String desenInfoPre) {
        this.desenInfoPreID = desenInfoPre;
    }

    public String getDesenInfoAfterID() {
        return desenInfoAfterID;
    }

    public void setDesenInfoAfterID(String desenInfoAfter) {
        this.desenInfoAfterID = desenInfoAfter;
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

    public Integer getDesenAlg() {
        return desenAlg;
    }

    public void setDesenAlg(Integer desenAlg) {
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

    public Integer getDesenLevel() {
        return desenLevel;
    }

    public void setDesenLevel(Integer desenLevel) {
        this.desenLevel = desenLevel;
    }

    public String getDesenPerformer() {
        return desenPerformer;
    }

    public void setDesenPerformer(String desenPerformer) {
        this.desenPerformer = desenPerformer;
    }

    public Integer getDesenCom() {
        return desenCom;
    }

    public void setDesenCom(Integer desenCom) {
        this.desenCom = desenCom;
    }

    public String getDesenTestPerformTime() {
        return desenTestPerformTime;
    }

    public void setDesenTestPerformTime(String desenTestPerformTime) {
        this.desenTestPerformTime = desenTestPerformTime;
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

    public Integer getDesenReversibility() {
        return desenReversibility;
    }

    public void setDesenReversibility(Integer desenUsability) {
        this.desenReversibility = desenUsability;
    }

    public Integer getDesenComplexity() {
        return desenComplexity;
    }

    public void setDesenComplexity(Integer desenComplexity) {
        this.desenComplexity = desenComplexity;
    }

    public Integer getDesenEffectEvaRet() {
        return desenEffectEvaRet;
    }

    public void setDesenEffectEvaRet(Integer desenEffectEvaRet) {
        this.desenEffectEvaRet = desenEffectEvaRet;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RecEvaResultInv that = (RecEvaResultInv) o;
        return Objects.equals(evaResultID, that.evaResultID) && Objects.equals(evaPerformer, that.evaPerformer) && Objects.equals(desenInfoPreID, that.desenInfoPreID) && Objects.equals(desenInfoAfterID, that.desenInfoAfterID) && Objects.equals(desenIntention, that.desenIntention) && Objects.equals(desenRequirements, that.desenRequirements) && Objects.equals(desenControlSet, that.desenControlSet) && Objects.equals(desenAlg, that.desenAlg) && Objects.equals(desenAlgParam, that.desenAlgParam) && Objects.equals(desenPerformStartTime, that.desenPerformStartTime) && Objects.equals(desenPerformEndTime, that.desenPerformEndTime) && Objects.equals(desenLevel, that.desenLevel) && Objects.equals(desenPerformer, that.desenPerformer) && Objects.equals(desenCom, that.desenCom) && Objects.equals(desenTestPerformTime, that.desenTestPerformTime) && Objects.equals(desenDeviation, that.desenDeviation) && Objects.equals(desenExtendedcontrol, that.desenExtendedcontrol) && Objects.equals(desenInformationloss, that.desenInformationloss) && Objects.equals(desenReversibility, that.desenReversibility) && Objects.equals(desenComplexity, that.desenComplexity) && Objects.equals(desenEffectEvaRet, that.desenEffectEvaRet);
    }

    @Override
    public int hashCode() {
        return Objects.hash(evaResultID, evaPerformer, desenInfoPreID, desenInfoAfterID, desenIntention, desenRequirements, desenControlSet, desenAlg, desenAlgParam, desenPerformStartTime, desenPerformEndTime, desenLevel, desenPerformer, desenCom, desenTestPerformTime, desenDeviation, desenExtendedcontrol, desenInformationloss, desenReversibility, desenComplexity, desenEffectEvaRet);
    }

    @Override
    public String toString() {
        return "RecEvaResultInv{" +
                "evaResultID='" + evaResultID + '\'' +
                ", evaPerformer='" + evaPerformer + '\'' +
                ", desenInfoPre='" + desenInfoPreID + '\'' +
                ", desenInfoAfter='" + desenInfoAfterID + '\'' +
                ", desenIntention='" + desenIntention + '\'' +
                ", desenRequirements='" + desenRequirements + '\'' +
                ", desenControlSet='" + desenControlSet + '\'' +
                ", desenAlg=" + desenAlg +
                ", desenAlgParam='" + desenAlgParam + '\'' +
                ", desenPerformStartTime='" + desenPerformStartTime + '\'' +
                ", desenPerformEndTime='" + desenPerformEndTime + '\'' +
                ", desenLevel=" + desenLevel +
                ", desenPerformer='" + desenPerformer + '\'' +
                ", desenCom=" + desenCom +
                ", desenTestPerformTime='" + desenTestPerformTime + '\'' +
                ", desenDeviation=" + desenDeviation +
                ", desenExtendedcontrol=" + desenExtendedcontrol +
                ", desenInformationloss=" + desenInformationloss +
                ", desenUsability=" + desenReversibility +
                ", desenComplexity=" + desenComplexity +
                ", desenEffectEvaRet=" + desenEffectEvaRet +
                '}';
    }
}

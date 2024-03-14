package com.lu.gademo.entity.effectEva;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "rec_eva_bg")
public class RecEvaBg {
    @Id
    @Column(name = "evaresultid")
    private String evaResultID;
    @Basic
    @Column(name = "evaperformer")
    private String evaPerformer;
    @Basic
    @Column(name = "deseninfopre")
    private String desenInfoPre;
    @Basic
    @Column(name = "deseninfoafter")
    private String desenInfoAfter;
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
    @Column(name = "desenusability")
    private Integer desenUsability;
    @Basic
    @Column(name = "desencomplexity")
    private Integer desenComplexity;
    @Basic
    @Column(name = "desenbgeffectevaret")
    private Integer desenBgEffectEvaRet;

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

    public String getDesenInfoPre() {
        return desenInfoPre;
    }

    public void setDesenInfoPre(String desenInfoPre) {
        this.desenInfoPre = desenInfoPre;
    }

    public String getDesenInfoAfter() {
        return desenInfoAfter;
    }

    public void setDesenInfoAfter(String desenInfoAfter) {
        this.desenInfoAfter = desenInfoAfter;
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

    public Integer getDesenBgEffectEvaRet() {
        return desenBgEffectEvaRet;
    }

    public void setDesenBgEffectEvaRet(Integer desenBgEffectEvaRet) {
        this.desenBgEffectEvaRet = desenBgEffectEvaRet;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RecEvaBg recEvaBg = (RecEvaBg) o;
        return Objects.equals(evaResultID, recEvaBg.evaResultID) && Objects.equals(evaPerformer, recEvaBg.evaPerformer) && Objects.equals(desenInfoPre, recEvaBg.desenInfoPre) && Objects.equals(desenInfoAfter, recEvaBg.desenInfoAfter) && Objects.equals(desenIntention, recEvaBg.desenIntention) && Objects.equals(desenRequirements, recEvaBg.desenRequirements) && Objects.equals(desenControlSet, recEvaBg.desenControlSet) && Objects.equals(desenAlg, recEvaBg.desenAlg) && Objects.equals(desenAlgParam, recEvaBg.desenAlgParam) && Objects.equals(desenPerformStartTime, recEvaBg.desenPerformStartTime) && Objects.equals(desenPerformEndTime, recEvaBg.desenPerformEndTime) && Objects.equals(desenLevel, recEvaBg.desenLevel) && Objects.equals(desenPerformer, recEvaBg.desenPerformer) && Objects.equals(desenCom, recEvaBg.desenCom) && Objects.equals(desenTestPerformTime, recEvaBg.desenTestPerformTime) && Objects.equals(desenDeviation, recEvaBg.desenDeviation) && Objects.equals(desenExtendedcontrol, recEvaBg.desenExtendedcontrol) && Objects.equals(desenInformationloss, recEvaBg.desenInformationloss) && Objects.equals(desenUsability, recEvaBg.desenUsability) && Objects.equals(desenComplexity, recEvaBg.desenComplexity) && Objects.equals(desenBgEffectEvaRet, recEvaBg.desenBgEffectEvaRet);
    }

    @Override
    public int hashCode() {
        return Objects.hash(evaResultID, evaPerformer, desenInfoPre, desenInfoAfter, desenIntention, desenRequirements, desenControlSet, desenAlg, desenAlgParam, desenPerformStartTime, desenPerformEndTime, desenLevel, desenPerformer, desenCom, desenTestPerformTime, desenDeviation, desenExtendedcontrol, desenInformationloss, desenUsability, desenComplexity, desenBgEffectEvaRet);
    }
}

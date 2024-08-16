package com.lu.gademo.entity.ga.effectEva;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Setter
@Entity
@ToString
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
    @Basic
    @Column(name = "desenbgeffectevaret")
    private Boolean desenBGEffectEvaRet;
    @Basic
    @Column(name = "desenFailedColName")
    private String desenFailedColName;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RecEvaResultInv)) return false;
        RecEvaResultInv that = (RecEvaResultInv) o;
        return Objects.equals(getEvaResultID(), that.getEvaResultID()) && Objects.equals(getEvaPerformer(), that.getEvaPerformer()) && Objects.equals(getDesenInfoPreID(), that.getDesenInfoPreID()) && Objects.equals(getDesenInfoAfterID(), that.getDesenInfoAfterID()) && Objects.equals(getDesenIntention(), that.getDesenIntention()) && Objects.equals(getDesenRequirements(), that.getDesenRequirements()) && Objects.equals(getDesenControlSet(), that.getDesenControlSet()) && Objects.equals(getDesenAlg(), that.getDesenAlg()) && Objects.equals(getDesenAlgParam(), that.getDesenAlgParam()) && Objects.equals(getDesenPerformStartTime(), that.getDesenPerformStartTime()) && Objects.equals(getDesenPerformEndTime(), that.getDesenPerformEndTime()) && Objects.equals(getDesenLevel(), that.getDesenLevel()) && Objects.equals(getDesenPerformer(), that.getDesenPerformer()) && Objects.equals(getDesenCom(), that.getDesenCom()) && Objects.equals(getDesenDeviation(), that.getDesenDeviation()) && Objects.equals(getDesenExtendedcontrol(), that.getDesenExtendedcontrol()) && Objects.equals(getDesenInformationloss(), that.getDesenInformationloss()) && Objects.equals(getDesenUsability(), that.getDesenUsability()) && Objects.equals(getDesenComplexity(), that.getDesenComplexity()) && Objects.equals(getDesenEffectEvaRet(), that.getDesenEffectEvaRet()) && Objects.equals(getDesenBGEffectEvaRet(), that.getDesenBGEffectEvaRet()) && Objects.equals(getDesenFailedColName(), that.getDesenFailedColName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getEvaResultID(), getEvaPerformer(), getDesenInfoPreID(), getDesenInfoAfterID(), getDesenIntention(), getDesenRequirements(), getDesenControlSet(), getDesenAlg(), getDesenAlgParam(), getDesenPerformStartTime(), getDesenPerformEndTime(), getDesenLevel(), getDesenPerformer(), getDesenCom(), getDesenDeviation(), getDesenExtendedcontrol(), getDesenInformationloss(), getDesenUsability(), getDesenComplexity(), getDesenEffectEvaRet(), getDesenBGEffectEvaRet(), getDesenFailedColName());
    }

    @Override
    public String toString() {
        return "RecEvaResultInv{" +
                "evaResultID='" + evaResultID + '\'' +
                ", evaPerformer='" + evaPerformer + '\'' +
                ", desenInfoPreID='" + desenInfoPreID + '\'' +
                ", desenInfoAfterID='" + desenInfoAfterID + '\'' +
                ", desenIntention='" + desenIntention + '\'' +
                ", desenRequirements='" + desenRequirements + '\'' +
                ", desenControlSet='" + desenControlSet + '\'' +
                ", desenAlg='" + desenAlg + '\'' +
                ", desenAlgParam='" + desenAlgParam + '\'' +
                ", desenPerformStartTime='" + desenPerformStartTime + '\'' +
                ", desenPerformEndTime='" + desenPerformEndTime + '\'' +
                ", desenLevel='" + desenLevel + '\'' +
                ", desenPerformer='" + desenPerformer + '\'' +
                ", desenCom=" + desenCom +
                ", desenDeviation=" + desenDeviation +
                ", desenExtendedcontrol=" + desenExtendedcontrol +
                ", desenInformationloss=" + desenInformationloss +
                ", desenUsability=" + desenUsability +
                ", desenComplexity=" + desenComplexity +
                ", desenEffectEvaRet=" + desenEffectEvaRet +
                ", desenBGEffectEvaRet=" + desenBGEffectEvaRet +
                ", desenFailedColName='" + desenFailedColName + '\'' +
                '}';
    }
}

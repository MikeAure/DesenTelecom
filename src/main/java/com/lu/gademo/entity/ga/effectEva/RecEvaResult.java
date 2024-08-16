package com.lu.gademo.entity.ga.effectEva;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "rec_eva_result")
// 脱敏效果评测结果
public class RecEvaResult {
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
}

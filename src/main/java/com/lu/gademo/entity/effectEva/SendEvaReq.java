package com.lu.gademo.entity.effectEva;

import com.lu.gademo.entity.support.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "send_eva_req")
public class SendEvaReq extends BaseEntity {
    // 评测申请ID
    @Id
    @Column(name = "evarequestid")
    private String evaRequestId;
    // 系统ID
    @Basic
    @Column(name = "systemid")
    private Integer systemID;

    @Basic
    @Column(name = "evidenceid")
    private String evidenceID;

    @Basic
    @Column(name = "globalid")
    private String globalID;
    @Basic
    @Column(name = "deseninfopreiden")
    private String desenInfoPreIden;
    @Basic
    @Column(name = "deseninfoafteriden")
    private String desenInfoAfterIden;
    @Basic
    @Column(name = "deseninfopreid")
    private String desenInfoPreId;
    @Basic
    @Column(name = "deseninfopre")
    private String desenInfoPre;
    @Basic
    @Column(name = "deseninfoafterid")
    private String desenInfoAfterId;
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

    @Basic
    @Column(name = "rawfilesize")
    private Long rawFileSize;

    @Basic
    @Column(name = "desenfilesize")
    private Long desenFileSize;

    @Basic
    @Column(name = "filetype")
    private String fileType;

    @Basic
    @Column(name = "filesuffix")
    private String fileSuffix;

    @Basic
    @Column(name = "status")
    private String status;

}

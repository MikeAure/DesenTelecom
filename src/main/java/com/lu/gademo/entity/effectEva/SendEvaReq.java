package com.lu.gademo.entity.effectEva;

import com.lu.gademo.entity.support.BaseEntity;

import javax.persistence.*;
import java.util.Objects;

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
    private Integer desenCom;

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getGlobalID() {
        return globalID;
    }

    public void setGlobalID(String fileGloID) {
        this.globalID = fileGloID;
    }

    public String getEvaRequestId() {
        return evaRequestId;
    }

    public void setEvaRequestId(String evaRequestId) {
        this.evaRequestId = evaRequestId;
    }

    public String getDesenInfoPreIden() {
        return desenInfoPreIden;
    }

    public void setDesenInfoPreIden(String desenInfoPreIden) {
        this.desenInfoPreIden = desenInfoPreIden;
    }

    public String getDesenInfoAfterIden() {
        return desenInfoAfterIden;
    }

    public void setDesenInfoAfterIden(String desenInfoAfterIden) {
        this.desenInfoAfterIden = desenInfoAfterIden;
    }

    public String getDesenInfoPreId() {
        return desenInfoPreId;
    }

    public void setDesenInfoPreId(String desenInfoPreId) {
        this.desenInfoPreId = desenInfoPreId;
    }

    public String getDesenInfoPre() {
        return desenInfoPre;
    }

    public void setDesenInfoPre(String desenInfoPre) {
        this.desenInfoPre = desenInfoPre;
    }

    public String getDesenInfoAfterId() {
        return desenInfoAfterId;
    }

    public void setDesenInfoAfterId(String desenInfoAfterId) {
        this.desenInfoAfterId = desenInfoAfterId;
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

    public Integer getDesenCom() {
        return desenCom;
    }

    public void setDesenCom(Integer desenCom) {
        this.desenCom = desenCom;
    }

    public Long getRawFileSize() {
        return rawFileSize;
    }

    public void setRawFileSize(Long rawFileSize) {
        this.rawFileSize = rawFileSize;
    }

    public Long getDesenFileSize() {
        return desenFileSize;
    }

    public void setDesenFileSize(Long desenFileSize) {
        this.desenFileSize = desenFileSize;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public Integer getSystemID() {
        return systemID;
    }

    public void setSystemID(Integer systemID) {
        this.systemID = systemID;
    }

    public String getEvidenceID() {
        return evidenceID;
    }

    public void setEvidenceID(String evidenceID) {
        this.evidenceID = evidenceID;
    }

    public String getFileSuffix() {
        return fileSuffix;
    }

    public void setFileSuffix(String fileSuffix) {
        this.fileSuffix = fileSuffix;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SendEvaReq that = (SendEvaReq) o;
        return Objects.equals(evaRequestId, that.evaRequestId) && Objects.equals(desenInfoPreIden, that.desenInfoPreIden) && Objects.equals(desenInfoAfterIden, that.desenInfoAfterIden) && Objects.equals(desenInfoPreId, that.desenInfoPreId) && Objects.equals(desenInfoPre, that.desenInfoPre) && Objects.equals(desenInfoAfterId, that.desenInfoAfterId) && Objects.equals(desenInfoAfter, that.desenInfoAfter) && Objects.equals(desenIntention, that.desenIntention) && Objects.equals(desenRequirements, that.desenRequirements) && Objects.equals(desenControlSet, that.desenControlSet) && Objects.equals(desenAlg, that.desenAlg) && Objects.equals(desenAlgParam, that.desenAlgParam) && Objects.equals(desenPerformStartTime, that.desenPerformStartTime) && Objects.equals(desenPerformEndTime, that.desenPerformEndTime) && Objects.equals(desenLevel, that.desenLevel) && Objects.equals(desenPerformer, that.desenPerformer) && Objects.equals(desenCom, that.desenCom);
    }

    @Override
    public int hashCode() {
        return Objects.hash(evaRequestId, desenInfoPreIden, desenInfoAfterIden, desenInfoPreId, desenInfoPre, desenInfoAfterId, desenInfoAfter, desenIntention, desenRequirements, desenControlSet, desenAlg, desenAlgParam, desenPerformStartTime, desenPerformEndTime, desenLevel, desenPerformer, desenCom);
    }

    @Override
    public String toString() {
        return "SendEvaReq{" +
                "evaRequestId='" + evaRequestId + '\'' +
                ", desenInfoPreIden='" + desenInfoPreIden + '\'' +
                ", desenInfoAfterIden='" + desenInfoAfterIden + '\'' +
                ", desenInfoPreId='" + desenInfoPreId + '\'' +
                ", desenInfoPre='" + desenInfoPre + '\'' +
                ", desenInfoAfterId='" + desenInfoAfterId + '\'' +
                ", desenInfoAfter='" + desenInfoAfter + '\'' +
                ", desenIntention='" + desenIntention + '\'' +
                ", desenRequirements='" + desenRequirements + '\'' +
                ", desenControlSet='" + desenControlSet + '\'' +
                ", desenAlg='" + desenAlg + '\'' +
                ", desenAlgParam='" + desenAlgParam + '\'' +
                ", desenPerformStartTime='" + desenPerformStartTime + '\'' +
                ", desenPerformEndTime='" + desenPerformEndTime + '\'' +
                ", desenLevel='" + desenLevel + '\'' +
                ", desenPerformer='" + desenPerformer + '\'' +
                ", desenCom=" + desenCom +'\'' +
                ", rawFileSize='" + rawFileSize + '\'' +
                ", desenFileSize='" + desenFileSize + '\'' +
                ", fileType='" + fileType + '\'' +
                ", systemID=" + systemID + '\'' +
                ", evidenceID='" + evidenceID + '\'' +
                ", fileSuffix='" + fileSuffix + '\'' +
                '}';
    }
}

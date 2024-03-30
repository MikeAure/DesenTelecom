package com.lu.gademo.entity.evidence;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "req_evidence_save")
public class ReqEvidenceSave {
    @Basic
    @Column(name = "system_id")
    private int systemID;
    @Basic
    @Column(name = "system_ip")
    private String systemIP;
    @Basic
    @Column(name = "main_cmd")
    private Integer mainCMD;
    @Basic
    @Column(name = "sub_cmd")
    private Integer subCMD;

    @Id
    @Column(name = "evidence_id")
    private String evidenceID;
    @Basic
    @Column(name = "msg_version")
    private Integer msgVersion;
    @Basic
    @Column(name = "req_time")
    private String reqtime;
    @Basic
    @Column(name = "object_size")
    private Long objectSize;
    @Basic
    @Column(name = "object_mode")
    private String objectMode;
    @Basic
    @Column(name = "data_sign")
    private String datasign;

    public int getSystemID() {
        return systemID;
    }

    public void setSystemID(int systemId) {
        this.systemID = systemId;
    }

    public String getSystemIP() {
        return systemIP;
    }

    public void setSystemIP(String systemIp) {
        this.systemIP = systemIp;
    }

    public Integer getMainCMD() {
        return mainCMD;
    }

    public void setMainCMD(Integer mainCmd) {
        this.mainCMD = mainCmd;
    }

    public Integer getSubCMD() {
        return subCMD;
    }

    public void setSubCMD(Integer subCmd) {
        this.subCMD = subCmd;
    }

    public String getEvidenceID() {
        return evidenceID;
    }

    public void setEvidenceID(String evidenceId) {
        this.evidenceID = evidenceId;
    }

    public Integer getMsgVersion() {
        return msgVersion;
    }

    public void setMsgVersion(Integer msgVersion) {
        this.msgVersion = msgVersion;
    }

    public String getReqtime() {
        return reqtime;
    }

    public void setReqtime(String reqTime) {
        this.reqtime = reqTime;
    }

    public Long getObjectSize() {
        return objectSize;
    }

    public void setObjectSize(Long objectSize) {
        this.objectSize = objectSize;
    }

    public String getObjectMode() {
        return objectMode;
    }

    public void setObjectMode(String objectMode) {
        this.objectMode = objectMode;
    }

    public String getDatasign() {
        return datasign;
    }

    public void setDatasign(String dataSign) {
        this.datasign = dataSign;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReqEvidenceSave that = (ReqEvidenceSave) o;
        return systemID == that.systemID && Objects.equals(systemIP, that.systemIP) && Objects.equals(mainCMD, that.mainCMD) && Objects.equals(subCMD, that.subCMD) && Objects.equals(evidenceID, that.evidenceID) && Objects.equals(msgVersion, that.msgVersion) && Objects.equals(reqtime, that.reqtime) && Objects.equals(objectSize, that.objectSize) && Objects.equals(objectMode, that.objectMode) && Objects.equals(datasign, that.datasign);
    }

    @Override
    public int hashCode() {
        return Objects.hash(systemID, systemIP, mainCMD, subCMD, evidenceID, msgVersion, reqtime, objectSize, objectMode, datasign);
    }

    @Override
    public String toString() {
        return "ReqEvidenceSave{" +
                "systemID=" + systemID +
                ", systemIP='" + systemIP + '\'' +
                ", mainCMD=" + mainCMD +
                ", subCMD=" + subCMD +
                ", evidenceID='" + evidenceID + '\'' +
                ", msgVersion=" + msgVersion +
                ", reqtime='" + reqtime + '\'' +
                ", objectSize=" + objectSize +
                ", objectMode='" + objectMode + '\'' +
                ", datasign='" + datasign + '\'' +
                '}';
    }
}

package com.lu.gademo.entity.evidence;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "evidence_receipt_normal")
public class EvidenceReceiptNormal {
    @Basic
    @Column(name = "system_id")
    private Integer systemID;
    @Basic
    @Column(name = "main_cmd")
    private Integer mainCMD;
    @Basic
    @Column(name = "sub_cmd")
    private Integer subCMD;
    @Basic
    @Column(name = "evidence_id")
    private String evidenceID;
    @Basic
    @Column(name = "msg_version")
    private Integer msgVersion;
    @Basic
    @Column(name = "status")
    private String status;
    @Id
    @Column(name = "certificate_id")
    private String certificateID;
    @Basic
    @Column(name = "certificate_time")
    private String certificateTime;
    @Basic
    @Column(name = "certificate_hash")
    private String certificateHash;
    @Column(name = "certificate_sign")
    private  String certificateSign;

    public String getCertificateSign() {
        return certificateSign;
    }

    public void setCertificateSign(String certificateSign) {
        this.certificateSign = certificateSign;
    }

    public Integer getSystemID() {
        return systemID;
    }

    public void setSystemID(Integer systemId) {
        this.systemID = systemId;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCertificateID() {
        return certificateID;
    }

    public void setCertificateID(String certificateId) {
        this.certificateID = certificateId;
    }

    public String getCertificateTime() {
        return certificateTime;
    }

    public void setCertificateTime(String certificateTime) {
        this.certificateTime = certificateTime;
    }

    public String getCertificateHash() {
        return certificateHash;
    }

    public void setCertificateHash(String certificateHash) {
        this.certificateHash = certificateHash;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EvidenceReceiptNormal that = (EvidenceReceiptNormal) o;
        return Objects.equals(systemID, that.systemID) && Objects.equals(mainCMD, that.mainCMD) && Objects.equals(subCMD, that.subCMD) && Objects.equals(evidenceID, that.evidenceID) && Objects.equals(msgVersion, that.msgVersion) && Objects.equals(status, that.status) && Objects.equals(certificateID, that.certificateID) && Objects.equals(certificateTime, that.certificateTime) && Objects.equals(certificateHash, that.certificateHash);
    }

    @Override
    public int hashCode() {
        return Objects.hash(systemID, mainCMD, subCMD, evidenceID, msgVersion, status, certificateID, certificateTime, certificateHash);
    }
}

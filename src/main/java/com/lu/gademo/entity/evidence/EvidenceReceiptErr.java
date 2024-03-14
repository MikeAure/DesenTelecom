package com.lu.gademo.entity.evidence;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "evidence_receipt_err")
public class EvidenceReceiptErr {
    @Basic
    @Column(name = "system_id")
    private int systemID;
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
    @Column(name = "status")
    private String status;
    @Basic
    @Column(name = "err_code")
    private Integer errCode;

    public int getSystemID() {
        return systemID;
    }

    public void setSystemID(int systemId) {
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

    public Integer getErrCode() {
        return errCode;
    }

    public void setErrCode(Integer errCode) {
        this.errCode = errCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EvidenceReceiptErr that = (EvidenceReceiptErr) o;
        return systemID == that.systemID && Objects.equals(mainCMD, that.mainCMD) && Objects.equals(subCMD, that.subCMD) && Objects.equals(evidenceID, that.evidenceID) && Objects.equals(msgVersion, that.msgVersion) && Objects.equals(status, that.status) && Objects.equals(errCode, that.errCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(systemID, mainCMD, subCMD, evidenceID, msgVersion, status, errCode);
    }
}

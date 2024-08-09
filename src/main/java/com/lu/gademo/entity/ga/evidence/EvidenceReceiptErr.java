package com.lu.gademo.entity.ga.evidence;

import org.hibernate.proxy.HibernateProxy;

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

    public void setSystemID(int systemID) {
        this.systemID = systemID;
    }

    public Integer getMainCMD() {
        return mainCMD;
    }

    public void setMainCMD(Integer mainCMD) {
        this.mainCMD = mainCMD;
    }

    public Integer getSubCMD() {
        return subCMD;
    }

    public void setSubCMD(Integer subCMD) {
        this.subCMD = subCMD;
    }

    public String getEvidenceID() {
        return evidenceID;
    }

    public void setEvidenceID(String evidenceID) {
        this.evidenceID = evidenceID;
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
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        EvidenceReceiptErr that = (EvidenceReceiptErr) o;
        return getEvidenceID() != null && Objects.equals(getEvidenceID(), that.getEvidenceID());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }

    @Override
    public String toString() {
        return "EvidenceReceiptErr{" +
                "systemID=" + systemID +
                ", mainCMD=" + mainCMD +
                ", subCMD=" + subCMD +
                ", evidenceID='" + evidenceID + '\'' +
                ", msgVersion=" + msgVersion +
                ", status='" + status + '\'' +
                ", errCode=" + errCode +
                '}';
    }
}

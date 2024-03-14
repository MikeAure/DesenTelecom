package com.lu.gademo.entity.effectEva;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "send_eva_receipt")
public class SendEvaReceipt {

    @Id
    @Column(name = "certificateid")
    private String certificateID;
    @Basic
    @Column(name = "evaresultid")
    private String evaResultID;
    @Basic
    @Column(name = "hash")
    private String hash;

    public String getCertificateID() {
        return certificateID;
    }

    public void setCertificateID(String certificateId) {
        this.certificateID = certificateId;
    }

    public String getEvaResultID() {
        return evaResultID;
    }

    public void setEvaResultID(String evaResultId) {
        this.evaResultID = evaResultId;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SendEvaReceipt that = (SendEvaReceipt) o;
        return Objects.equals(certificateID, that.certificateID) && Objects.equals(evaResultID, that.evaResultID) && Objects.equals(hash, that.hash);
    }

    @Override
    public int hashCode() {
        return Objects.hash(certificateID, evaResultID, hash);
    }

    @Override
    public String toString() {
        return "SendEvaReceipt{" +
                "certificateID='" + certificateID + '\'' +
                ", evaResultID='" + evaResultID + '\'' +
                ", hash='" + hash + '\'' +
                '}';
    }
}

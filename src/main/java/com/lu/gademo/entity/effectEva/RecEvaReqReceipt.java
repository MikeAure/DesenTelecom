package com.lu.gademo.entity.effectEva;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "rec_eva_req_receipt")
public class RecEvaReqReceipt {

    @Id
    @Column(name = "certificateid")
    private String certificateID;
    @Basic
    @Column(name = "evarequestid")
    private String evaRequestID;
    @Basic
    @Column(name = "hash")
    private String hash;

    public RecEvaReqReceipt() {
    }

    public String getCertificateID() {
        return certificateID;
    }

    public void setCertificateID(String certificateId) {
        this.certificateID = certificateId;
    }

    public String getEvaRequestID() {
        return evaRequestID;
    }

    public void setEvaRequestID(String evaRequestId) {
        this.evaRequestID = evaRequestId;
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
        RecEvaReqReceipt that = (RecEvaReqReceipt) o;
        return Objects.equals(certificateID, that.certificateID) && Objects.equals(evaRequestID, that.evaRequestID) && Objects.equals(hash, that.hash);
    }

    @Override
    public int hashCode() {
        return Objects.hash(certificateID, evaRequestID, hash);
    }

    @Override
    public String toString() {
        return "RecEvaReqReceipt{" +
                "certificateID='" + certificateID + '\'' +
                ", evaRequestID='" + evaRequestID + '\'' +
                ", hash='" + hash + '\'' +
                '}';
    }
}

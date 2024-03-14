package com.lu.gademo.entity.split;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "send_split_receipt")
public class SendSplitReceipt {

    @Id
    @Column(name = "certificateid")
    private String certificateId;
    @Basic
    @Column(name = "recstoinfoid")
    private String recStoInfoId;
    @Basic
    @Column(name = "hash")
    private String hash;

    public String getCertificateId() {
        return certificateId;
    }

    public void setCertificateId(String certificateId) {
        this.certificateId = certificateId;
    }

    public String getRecStoInfoId() {
        return recStoInfoId;
    }

    public void setRecStoInfoId(String recStoInfoId) {
        this.recStoInfoId = recStoInfoId;
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
        SendSplitReceipt that = (SendSplitReceipt) o;
        return Objects.equals(certificateId, that.certificateId) && Objects.equals(recStoInfoId, that.recStoInfoId) && Objects.equals(hash, that.hash);
    }

    @Override
    public int hashCode() {
        return Objects.hash(certificateId, recStoInfoId, hash);
    }
}

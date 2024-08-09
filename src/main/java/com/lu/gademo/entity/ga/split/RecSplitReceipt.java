package com.lu.gademo.entity.ga.split;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "rec_split_receipt")
public class RecSplitReceipt {

    @Id
    @Column(name = "certificateid")
    private String certificateId;
    @Basic
    @Column(name = "deseninfoafter")
    private String desenInfoAfter;
    @Basic
    @Column(name = "hash")
    private String hash;

    public String getCertificateId() {
        return certificateId;
    }

    public void setCertificateId(String certificateId) {
        this.certificateId = certificateId;
    }

    public String getDesenInfoAfter() {
        return desenInfoAfter;
    }

    public void setDesenInfoAfter(String desenInfoAfter) {
        this.desenInfoAfter = desenInfoAfter;
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
        RecSplitReceipt that = (RecSplitReceipt) o;
        return Objects.equals(certificateId, that.certificateId) && Objects.equals(desenInfoAfter, that.desenInfoAfter) && Objects.equals(hash, that.hash);
    }

    @Override
    public int hashCode() {
        return Objects.hash(certificateId, desenInfoAfter, hash);
    }
}

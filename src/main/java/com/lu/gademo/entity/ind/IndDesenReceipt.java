package com.lu.gademo.entity.ind;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "ind_desen_receipt")
public class IndDesenReceipt {
    @Id
    @Column(name = "certificateid")
    private String certificateId;
    @Basic
    @Column(name = "recflaginfoid")
    private String recFlagInfoId;
    @Basic
    @Column(name = "hash")
    private String hash;

    public String getCertificateId() {
        return certificateId;
    }

    public void setCertificateId(String certificateId) {
        this.certificateId = certificateId;
    }

    public String getRecFlagInfoId() {
        return recFlagInfoId;
    }

    public void setRecFlagInfoId(String recFlagInfoId) {
        this.recFlagInfoId = recFlagInfoId;
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
        IndDesenReceipt that = (IndDesenReceipt) o;
        return Objects.equals(certificateId, that.certificateId) && Objects.equals(recFlagInfoId, that.recFlagInfoId) && Objects.equals(hash, that.hash);
    }

    @Override
    public int hashCode() {
        return Objects.hash(certificateId, recFlagInfoId, hash);
    }
}

package com.lu.gademo.entity.ga.ruleCheck;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "rec_rule_req_receipt")
public class RecRuleReqReceipt {

    @Id
    @Column(name = "certificateid")
    private String certificateID;
    @Basic
    @Column(name = "logid")
    private String logID;
    @Basic
    @Column(name = "hash")
    private String hash;

    public String getCertificateID() {
        return certificateID;
    }

    public void setCertificateID(String certificateID) {
        this.certificateID = certificateID;
    }

    public String getLogID() {
        return logID;
    }

    public void setLogID(String logID) {
        this.logID = logID;
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
        RecRuleReqReceipt that = (RecRuleReqReceipt) o;
        return Objects.equals(certificateID, that.certificateID) && Objects.equals(logID, that.logID) && Objects.equals(hash, that.hash);
    }

    @Override
    public int hashCode() {
        return Objects.hash(certificateID, logID, hash);
    }
}

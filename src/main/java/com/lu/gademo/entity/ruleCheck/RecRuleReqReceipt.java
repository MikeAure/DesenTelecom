package com.lu.gademo.entity.ruleCheck;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "rec_rule_req_receipt")
public class RecRuleReqReceipt {

    @Id
    @Column(name = "certificateid")
    private String certificateId;
    @Basic
    @Column(name = "logid")
    private String logId;
    @Basic
    @Column(name = "hash")
    private String hash;

    public String getCertificateId() {
        return certificateId;
    }

    public void setCertificateId(String certificateId) {
        this.certificateId = certificateId;
    }

    public String getLogId() {
        return logId;
    }

    public void setLogId(String logId) {
        this.logId = logId;
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
        return Objects.equals(certificateId, that.certificateId) && Objects.equals(logId, that.logId) && Objects.equals(hash, that.hash);
    }

    @Override
    public int hashCode() {
        return Objects.hash(certificateId, logId, hash);
    }
}

package com.lu.gademo.entity.ga.ruleCheck;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "send_rule_receipt")
public class SendRuleReceipt {

    @Id
    @Column(name = "certificateid")
    private String certificateId;
    @Basic
    @Column(name = "reportid")
    private String reportId;
    @Basic
    @Column(name = "hash")
    private String hash;

    public String getCertificateId() {
        return certificateId;
    }

    public void setCertificateId(String certificateId) {
        this.certificateId = certificateId;
    }

    public String getReportId() {
        return reportId;
    }

    public void setReportId(String reportId) {
        this.reportId = reportId;
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
        SendRuleReceipt that = (SendRuleReceipt) o;
        return Objects.equals(certificateId, that.certificateId) && Objects.equals(reportId, that.reportId) && Objects.equals(hash, that.hash);
    }

    @Override
    public int hashCode() {
        return Objects.hash(certificateId, reportId, hash);
    }
}

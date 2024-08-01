package com.lu.gademo.entity.evidence;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "evidence_response")
public class EvidenceResponse {
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
    @Column(name = "response_time")
    private String responsetime;
    @Basic
    @Column(name = "nonce")
    private String nonce;
    @Basic
    @Column(name = "position")
    private String position;
    @Basic
    @Column(name = "data_sign")
    private String dataSign;
    @Basic
    @Column(name = "random_identification")
    private String randomIdentification;

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

    public String getResponsetime() {
        return responsetime;
    }

    public void setResponsetime(String responseTime) {
        this.responsetime = responseTime;
    }

    public String getNonce() {
        return nonce;
    }

    public void setNonce(String nonce) {
        this.nonce = nonce;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getDataSign() {
        return dataSign;
    }

    public void setDataSign(String dataSign) {
        this.dataSign = dataSign;
    }

    public String getRandomIdentification() {
        return randomIdentification;
    }

    public void setRandomIdentification(String randomIdentification) {
        this.randomIdentification = randomIdentification;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EvidenceResponse that = (EvidenceResponse) o;
        return systemID == that.systemID && Objects.equals(mainCMD, that.mainCMD) && Objects.equals(subCMD, that.subCMD) && Objects.equals(evidenceID, that.evidenceID) && Objects.equals(msgVersion, that.msgVersion) && Objects.equals(responsetime, that.responsetime) && Objects.equals(nonce, that.nonce) && Objects.equals(position, that.position) && Objects.equals(dataSign, that.dataSign) && Objects.equals(randomIdentification, that.randomIdentification);
    }

    @Override
    public int hashCode() {
        return Objects.hash(systemID, mainCMD, subCMD, evidenceID, msgVersion, responsetime, nonce, position, dataSign, randomIdentification);
    }
}

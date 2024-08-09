package com.lu.gademo.entity.ga.evidence;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "submit_evidence_remote")
public class SubmitEvidenceRemote {
    @Basic
    @Column(name = "system_id")
    private int systemID;
    @Basic
    @Column(name = "system_ip")
    private String systemIP;
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
    @Column(name = "submit_time")
    private String submittime;
    @Basic
    @Column(name = "file_glo_id")
    private String fileGloID;
    @Basic
    @Column(name = "file_title")
    private String fileTitle;
    @Basic
    @Column(name = "file_abstract")
    private String fileAbstract;
    @Basic
    @Column(name = "file_keyword")
    private String fileKeyword;
    @Basic
    @Column(name = "desen_alg")
    private String desenAlg;
    @Basic
    @Column(name = "file_size")
    private Integer fileSize;
    @Basic
    @Column(name = "file_hash")
    private String fileHASH;
    @Basic
    @Column(name = "file_sig")
    private String fileSig;
    @Basic
    @Column(name = "desen_performer")
    private String desenPerformer;
    @Basic
    @Column(name = "desen_com")
    private Integer desenCom;
    @Basic
    @Column(name = "data_hash")
    private String dataHash;
    @Basic
    @Column(name = "random_identification")
    private String randomidentification;
    @Basic
    @Column(name = "data_sign")
    private String datasign;

    public int getSystemID() {
        return systemID;
    }

    public void setSystemID(int systemIId) {
        this.systemID = systemIId;
    }

    public String getSystemIP() {
        return systemIP;
    }

    public void setSystemIP(String systemIp) {
        this.systemIP = systemIp;
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

    public String getSubmittime() {
        return submittime;
    }

    public void setSubmittime(String submitTime) {
        this.submittime = submitTime;
    }

    public String getFileGloID() {
        return fileGloID;
    }

    public void setFileGloID(String fileGloId) {
        this.fileGloID = fileGloId;
    }

    public String getFileTitle() {
        return fileTitle;
    }

    public void setFileTitle(String fileTitle) {
        this.fileTitle = fileTitle;
    }

    public String getFileAbstract() {
        return fileAbstract;
    }

    public void setFileAbstract(String fileAbstract) {
        this.fileAbstract = fileAbstract;
    }

    public String getFileKeyword() {
        return fileKeyword;
    }

    public void setFileKeyword(String fileKeywords) {
        this.fileKeyword = fileKeywords;
    }

    public String getDesenAlg() {
        return desenAlg;
    }

    public void setDesenAlg(String desenAlg) {
        this.desenAlg = desenAlg;
    }

    public Integer getFileSize() {
        return fileSize;
    }

    public void setFileSize(Integer fileSize) {
        this.fileSize = fileSize;
    }

    public String getFileHASH() {
        return fileHASH;
    }

    public void setFileHASH(String fileHash) {
        this.fileHASH = fileHash;
    }

    public String getFileSig() {
        return fileSig;
    }

    public void setFileSig(String fileSig) {
        this.fileSig = fileSig;
    }

    public String getDesenPerformer() {
        return desenPerformer;
    }

    public void setDesenPerformer(String desenPerformer) {
        this.desenPerformer = desenPerformer;
    }

    public Integer getDesenCom() {
        return desenCom;
    }

    public void setDesenCom(Integer desenCom) {
        this.desenCom = desenCom;
    }

    public String getDataHash() {
        return dataHash;
    }

    public void setDataHash(String dataHash) {
        this.dataHash = dataHash;
    }

    public String getRandomidentification() {
        return randomidentification;
    }

    public void setRandomidentification(String randomIdentification) {
        this.randomidentification = randomIdentification;
    }

    public String getDatasign() {
        return datasign;
    }

    public void setDatasign(String dataSign) {
        this.datasign = dataSign;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SubmitEvidenceRemote that = (SubmitEvidenceRemote) o;
        return systemID == that.systemID && Objects.equals(systemIP, that.systemIP) && Objects.equals(mainCMD, that.mainCMD) && Objects.equals(subCMD, that.subCMD) && Objects.equals(evidenceID, that.evidenceID) && Objects.equals(msgVersion, that.msgVersion) && Objects.equals(submittime, that.submittime) && Objects.equals(fileGloID, that.fileGloID) && Objects.equals(fileTitle, that.fileTitle) && Objects.equals(fileAbstract, that.fileAbstract) && Objects.equals(fileKeyword, that.fileKeyword) && Objects.equals(desenAlg, that.desenAlg) && Objects.equals(fileSize, that.fileSize) && Objects.equals(fileHASH, that.fileHASH) && Objects.equals(fileSig, that.fileSig) && Objects.equals(desenPerformer, that.desenPerformer) && Objects.equals(desenCom, that.desenCom) && Objects.equals(dataHash, that.dataHash) && Objects.equals(randomidentification, that.randomidentification) && Objects.equals(datasign, that.datasign);
    }

    @Override
    public int hashCode() {
        return Objects.hash(systemID, systemIP, mainCMD, subCMD, evidenceID, msgVersion, submittime, fileGloID, fileTitle, fileAbstract, fileKeyword, desenAlg, fileSize, fileHASH, fileSig, desenPerformer, desenCom, dataHash, randomidentification, datasign);
    }
}

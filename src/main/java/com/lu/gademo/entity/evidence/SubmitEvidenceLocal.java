package com.lu.gademo.entity.evidence;

import javax.persistence.*;

@Entity
@Table(name = "submit_evidence_local", schema = "ga")
public class SubmitEvidenceLocal {
    @Basic
    @Column(name = "system_id")
    private Integer systemID;
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
    @Column(name = "global_id")
    private String globalID;
    @Column(name = "opt_time")
    private  String optTime;

    public String getOptTime() {
        return optTime;
    }

    public void setOptTime(String submitTime) {
        this.optTime = submitTime;
    }

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
    @Column(name = "desen_info_pre_id")
    private String desenInfoPreID;
    @Basic
    @Column(name = "desen_info_after_id")
    private String desenInfoAfterID;
    @Basic
    @Column(name = "desen_intention")
    private String desenIntention;
    @Basic
    @Column(name = "desen_requirements")
    private String desenRequirements;
    @Basic
    @Column(name = "desen_control_set")
    private String desenControlSet;
    @Basic
    @Column(name = "desen_alg_param")
    private String desenAlgParam;
    @Basic
    @Column(name = "desen_perform_start_time")
    private String desenPerformStartTime;
    @Basic
    @Column(name = "desen_perform_end_time")
    private String desenPerformEndTime;
    @Basic
    @Column(name = "desen_level")
    private String desenLevel;
    @Basic
    @Column(name = "data_hash")
    private String dataHash;
    @Basic
    @Column(name = "random_identification")
    private String randomidentification;
    @Basic
    @Column(name = "data_sign")
    private String datasign;
    @Column(name = "parent_system_id")
    private Integer parentSystemId;
    @Column(name = "status")
    private String status;
    @Column(name = "child_system_id")
    private Integer childSystemId;

    public Integer getSystemID() {
        return systemID;
    }

    public void setSystemID(Integer systemId) {
        this.systemID = systemId;
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

    public String getGlobalID() {
        return globalID;
    }

    public void setGlobalID(String fileGloId) {
        this.globalID = fileGloId;
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

    public void setFileKeyword(String fileKeyword) {
        this.fileKeyword = fileKeyword;
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

    public String getDesenInfoPreID() {
        return desenInfoPreID;
    }

    public void setDesenInfoPreID(String desenInfoPreId) {
        this.desenInfoPreID = desenInfoPreId;
    }

    public String getDesenInfoAfterID() {
        return desenInfoAfterID;
    }

    public void setDesenInfoAfterID(String desenInfoAfterId) {
        this.desenInfoAfterID = desenInfoAfterId;
    }

    public String getDesenIntention() {
        return desenIntention;
    }

    public void setDesenIntention(String desenIntention) {
        this.desenIntention = desenIntention;
    }

    public String getDesenRequirements() {
        return desenRequirements;
    }

    public void setDesenRequirements(String desenRequirements) {
        this.desenRequirements = desenRequirements;
    }

    public String getDesenControlSet() {
        return desenControlSet;
    }

    public void setDesenControlSet(String desenControlSet) {
        this.desenControlSet = desenControlSet;
    }

    public String getDesenAlgParam() {
        return desenAlgParam;
    }

    public void setDesenAlgParam(String desenAlgParam) {
        this.desenAlgParam = desenAlgParam;
    }

    public String getDesenPerformStartTime() {
        return desenPerformStartTime;
    }

    public void setDesenPerformStartTime(String desenPerformStartTime) {
        this.desenPerformStartTime = desenPerformStartTime;
    }

    public String getDesenPerformEndTime() {
        return desenPerformEndTime;
    }

    public void setDesenPerformEndTime(String desenPerformEndTime) {
        this.desenPerformEndTime = desenPerformEndTime;
    }

    public String getDesenLevel() {
        return desenLevel;
    }

    public void setDesenLevel(String desenLevel) {
        this.desenLevel = desenLevel;
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

    public Integer getParentSystemId() {
        return parentSystemId;
    }

    public void setParentSystemId(Integer parentSystemId) {
        this.parentSystemId = parentSystemId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String selfStatus) {
        this.status = selfStatus;
    }

    public Integer getChildSystemId() {
        return childSystemId;
    }

    public void setChildSystemId(Integer childSystemId) {
        this.childSystemId = childSystemId;
    }



}

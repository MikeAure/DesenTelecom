package com.lu.gademo.entity.ga.evidence;

import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import javax.persistence.*;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "submit_evidence_local")
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
    private String optTime;
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
    private Long fileSize;
    @Basic
    @Column(name = "file_hash")
    private String fileHash;
    @Basic
    @Column(name = "file_sig")
    private String fileSig;
    @Basic
    @Column(name = "desen_performer")
    private String desenPerformer;
    @Basic
    @Column(name = "desen_com")
    private Boolean desenCom;
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
    @Basic
    @Column(name = "fileDataType", length = 255)
    private String fileDataType;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SubmitEvidenceLocal)) return false;
        SubmitEvidenceLocal that = (SubmitEvidenceLocal) o;
        return Objects.equals(getSystemID(), that.getSystemID()) && Objects.equals(getSystemIP(), that.getSystemIP()) && Objects.equals(getMainCMD(), that.getMainCMD()) && Objects.equals(getSubCMD(), that.getSubCMD()) && Objects.equals(getEvidenceID(), that.getEvidenceID()) && Objects.equals(getMsgVersion(), that.getMsgVersion()) && Objects.equals(getSubmittime(), that.getSubmittime()) && Objects.equals(getGlobalID(), that.getGlobalID()) && Objects.equals(getOptTime(), that.getOptTime()) && Objects.equals(getFileTitle(), that.getFileTitle()) && Objects.equals(getFileAbstract(), that.getFileAbstract()) && Objects.equals(getFileKeyword(), that.getFileKeyword()) && Objects.equals(getDesenAlg(), that.getDesenAlg()) && Objects.equals(getFileSize(), that.getFileSize()) && Objects.equals(getFileHash(), that.getFileHash()) && Objects.equals(getFileSig(), that.getFileSig()) && Objects.equals(getDesenPerformer(), that.getDesenPerformer()) && Objects.equals(getDesenCom(), that.getDesenCom()) && Objects.equals(getDesenInfoPreID(), that.getDesenInfoPreID()) && Objects.equals(getDesenInfoAfterID(), that.getDesenInfoAfterID()) && Objects.equals(getDesenIntention(), that.getDesenIntention()) && Objects.equals(getDesenRequirements(), that.getDesenRequirements()) && Objects.equals(getDesenControlSet(), that.getDesenControlSet()) && Objects.equals(getDesenAlgParam(), that.getDesenAlgParam()) && Objects.equals(getDesenPerformStartTime(), that.getDesenPerformStartTime()) && Objects.equals(getDesenPerformEndTime(), that.getDesenPerformEndTime()) && Objects.equals(getDesenLevel(), that.getDesenLevel()) && Objects.equals(getDataHash(), that.getDataHash()) && Objects.equals(getRandomidentification(), that.getRandomidentification()) && Objects.equals(getDatasign(), that.getDatasign()) && Objects.equals(getParentSystemId(), that.getParentSystemId()) && Objects.equals(getStatus(), that.getStatus()) && Objects.equals(getChildSystemId(), that.getChildSystemId()) && Objects.equals(getFileDataType(), that.getFileDataType());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSystemID(), getSystemIP(), getMainCMD(), getSubCMD(), getEvidenceID(), getMsgVersion(), getSubmittime(), getGlobalID(), getOptTime(), getFileTitle(), getFileAbstract(), getFileKeyword(), getDesenAlg(), getFileSize(), getFileHash(), getFileSig(), getDesenPerformer(), getDesenCom(), getDesenInfoPreID(), getDesenInfoAfterID(), getDesenIntention(), getDesenRequirements(), getDesenControlSet(), getDesenAlgParam(), getDesenPerformStartTime(), getDesenPerformEndTime(), getDesenLevel(), getDataHash(), getRandomidentification(), getDatasign(), getParentSystemId(), getStatus(), getChildSystemId(), getFileDataType());
    }
}

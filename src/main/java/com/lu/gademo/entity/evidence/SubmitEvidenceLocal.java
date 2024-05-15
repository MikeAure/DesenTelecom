package com.lu.gademo.entity.evidence;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
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
    private  String optTime;
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
    private String fileHASH;
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


}

package com.lu.gademo.entity.evidence;

import lombok.Data;

import javax.persistence.*;
import java.util.Objects;

@Data
@Entity
@Table(name = "evidence_receipt_err")
public class EvidenceReceiptErr {
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
    @Column(name = "status")
    private String status;
    @Basic
    @Column(name = "err_code")
    private Integer errCode;
}

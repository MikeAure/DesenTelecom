package com.lu.gademo.entity.evidence;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Value;

import javax.persistence.*;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "req_evidence_save")
// 存证请求格式
public class ReqEvidenceSave {
    @Basic
    @Column(name = "system_id")
    private int systemID;
    @Basic
    @Column(name = "system_ip")
    private String systemIP;
    @Basic
    @Value("system_port")
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
    @Column(name = "req_time")
    private String reqtime;
    @Basic
    @Column(name = "object_size")
    private Long objectSize;
    @Basic
    @Column(name = "object_mode")
    private String objectMode;
    @Basic
    @Column(name = "data_sign")
    private String datasign;

    public ReqEvidenceSave(String evidenceID, Long objectSize, String objectMode) {
        this.evidenceID = evidenceID;
        this.objectSize = objectSize;
        this.objectMode = objectMode;
    }


}

package com.lu.gademo.entity.evidence;

import lombok.*;
import org.hibernate.proxy.HibernateProxy;
import org.springframework.beans.factory.annotation.Value;

import javax.persistence.*;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
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

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        ReqEvidenceSave that = (ReqEvidenceSave) o;
        return getEvidenceID() != null && Objects.equals(getEvidenceID(), that.getEvidenceID());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}

package com.lu.gademo.entity.ga.effectEva;

import com.lu.gademo.entity.ga.support.BaseEntity;
import com.lu.gademo.service.EvaluationSystemLogSender;
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
@Table(name = "send_eva_req")
public class SendEvaReq extends BaseEntity {
    // 评测申请ID
    @Id
    @Column(name = "evarequestid")
    private String evaRequestId;
    // 系统ID
    @Basic
    @Column(name = "systemid")
    private Integer systemID;
    @Basic
    @Column(name = "evidenceid")
    private String evidenceID;
    @Basic
    @Column(name = "globalid")
    private String globalID;
    @Basic
    @Column(name = "deseninfopreiden")
    private String desenInfoPreIden;
    @Basic
    @Column(name = "deseninfoafteriden")
    private String desenInfoAfterIden;
    @Basic
    @Column(name = "deseninfopreid")
    private String desenInfoPreId;
    @Basic
    @Column(name = "deseninfopre")
    private String desenInfoPre;
    @Basic
    @Column(name = "deseninfoafterid")
    private String desenInfoAfterId;
    @Basic
    @Column(name = "deseninfoafter")
    private String desenInfoAfter;
    @Basic
    @Column(name = "desenintention")
    private String desenIntention;
    @Basic
    @Column(name = "desenrequirements")
    private String desenRequirements;
    @Basic
    @Column(name = "desencontrolset")
    private String desenControlSet;
    @Basic
    @Column(name = "desenalg")
    private String desenAlg;
    @Basic
    @Column(name = "desenalgparam")
    private String desenAlgParam;
    @Basic
    @Column(name = "desenperformstarttime")
    private String desenPerformStartTime;
    @Basic
    @Column(name = "desenperformendtime")
    private String desenPerformEndTime;
    @Basic
    @Column(name = "desenlevel")
    private String desenLevel;
    @Basic
    @Column(name = "desenperformer")
    private String desenPerformer;
    @Basic
    @Column(name = "desencom")
    private Boolean desenCom;
    @Basic
    @Column(name = "rawfilesize")
    private Long rawFileSize;
    @Basic
    @Column(name = "desenfilesize")
    private Long desenFileSize;
    @Basic
    @Column(name = "filetype")
    private String fileType;
    @Basic
    @Column(name = "filesuffix")
    private String fileSuffix;
    @Basic
    @Column(name = "status")
    private String status;
    @Column(name = "parentSystemId")
    private Integer parentSystemId;
    @Column(name = "childSystemId")
    private Integer childSystemId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SendEvaReq that = (SendEvaReq) o;
        return Objects.equals(getEvaRequestId(), that.getEvaRequestId()) && Objects.equals(getSystemID(), that.getSystemID()) && Objects.equals(getEvidenceID(), that.getEvidenceID()) && Objects.equals(getGlobalID(), that.getGlobalID()) && Objects.equals(getDesenInfoPreIden(), that.getDesenInfoPreIden()) && Objects.equals(getDesenInfoAfterIden(), that.getDesenInfoAfterIden()) && Objects.equals(getDesenInfoPreId(), that.getDesenInfoPreId()) && Objects.equals(getDesenInfoPre(), that.getDesenInfoPre()) && Objects.equals(getDesenInfoAfterId(), that.getDesenInfoAfterId()) && Objects.equals(getDesenInfoAfter(), that.getDesenInfoAfter()) && Objects.equals(getDesenIntention(), that.getDesenIntention()) && Objects.equals(getDesenRequirements(), that.getDesenRequirements()) && Objects.equals(getDesenControlSet(), that.getDesenControlSet()) && Objects.equals(getDesenAlg(), that.getDesenAlg()) && Objects.equals(getDesenAlgParam(), that.getDesenAlgParam()) && Objects.equals(getDesenPerformStartTime(), that.getDesenPerformStartTime()) && Objects.equals(getDesenPerformEndTime(), that.getDesenPerformEndTime()) && Objects.equals(getDesenLevel(), that.getDesenLevel()) && Objects.equals(getDesenPerformer(), that.getDesenPerformer()) && Objects.equals(getDesenCom(), that.getDesenCom()) && Objects.equals(getRawFileSize(), that.getRawFileSize()) && Objects.equals(getDesenFileSize(), that.getDesenFileSize()) && Objects.equals(getFileType(), that.getFileType()) && Objects.equals(getFileSuffix(), that.getFileSuffix()) && Objects.equals(getStatus(), that.getStatus()) && Objects.equals(getParentSystemId(), that.getParentSystemId()) && Objects.equals(getChildSystemId(), that.getChildSystemId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getEvaRequestId(), getSystemID(), getEvidenceID(), getGlobalID(), getDesenInfoPreIden(), getDesenInfoAfterIden(), getDesenInfoPreId(), getDesenInfoPre(), getDesenInfoAfterId(), getDesenInfoAfter(), getDesenIntention(), getDesenRequirements(), getDesenControlSet(), getDesenAlg(), getDesenAlgParam(), getDesenPerformStartTime(), getDesenPerformEndTime(), getDesenLevel(), getDesenPerformer(), getDesenCom(), getRawFileSize(), getDesenFileSize(), getFileType(), getFileSuffix(), getStatus(), getParentSystemId(), getChildSystemId());
    }
}

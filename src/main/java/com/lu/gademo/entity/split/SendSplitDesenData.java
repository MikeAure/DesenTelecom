package com.lu.gademo.entity.split;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "send_split_desen_data")
public class SendSplitDesenData {

    @Id
    @Column(name = "deseninfoafter")
    private String desenInfoAfter;
    @Basic
    @Column(name = "deseninfoafteriden")
    private String desenInfoAfterIden;
    @Basic
    @Column(name = "deseninfopre")
    private String desenInfoPre;
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
    private Integer desenAlg;
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
    private Integer desenLevel;
    @Basic
    @Column(name = "desenperformer")
    private String desenPerformer;
    @Basic
    @Column(name = "desencom")
    private Integer desenCom;

    public String getDesenInfoAfter() {
        return desenInfoAfter;
    }

    public void setDesenInfoAfter(String desenInfoAfter) {
        this.desenInfoAfter = desenInfoAfter;
    }

    public String getDesenInfoAfterIden() {
        return desenInfoAfterIden;
    }

    public void setDesenInfoAfterIden(String desenInfoAfterIden) {
        this.desenInfoAfterIden = desenInfoAfterIden;
    }

    public String getDesenInfoPre() {
        return desenInfoPre;
    }

    public void setDesenInfoPre(String desenInfoPre) {
        this.desenInfoPre = desenInfoPre;
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

    public Integer getDesenAlg() {
        return desenAlg;
    }

    public void setDesenAlg(Integer desenAlg) {
        this.desenAlg = desenAlg;
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

    public Integer getDesenLevel() {
        return desenLevel;
    }

    public void setDesenLevel(Integer desenLevel) {
        this.desenLevel = desenLevel;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SendSplitDesenData that = (SendSplitDesenData) o;
        return Objects.equals(desenInfoAfter, that.desenInfoAfter) && Objects.equals(desenInfoAfterIden, that.desenInfoAfterIden) && Objects.equals(desenInfoPre, that.desenInfoPre) && Objects.equals(desenIntention, that.desenIntention) && Objects.equals(desenRequirements, that.desenRequirements) && Objects.equals(desenControlSet, that.desenControlSet) && Objects.equals(desenAlg, that.desenAlg) && Objects.equals(desenAlgParam, that.desenAlgParam) && Objects.equals(desenPerformStartTime, that.desenPerformStartTime) && Objects.equals(desenPerformEndTime, that.desenPerformEndTime) && Objects.equals(desenLevel, that.desenLevel) && Objects.equals(desenPerformer, that.desenPerformer) && Objects.equals(desenCom, that.desenCom);
    }

    @Override
    public int hashCode() {
        return Objects.hash(desenInfoAfter, desenInfoAfterIden, desenInfoPre, desenIntention, desenRequirements, desenControlSet, desenAlg, desenAlgParam, desenPerformStartTime, desenPerformEndTime, desenLevel, desenPerformer, desenCom);
    }
}

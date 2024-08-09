package com.lu.gademo.entity.ga.ind;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "ind_desen")
public class IndDesen {

    @Id
    @Column(name = "recflaginfoid")
    private String recFlagInfoId;
    @Basic
    @Column(name = "filetypeiden")
    private Integer fileTypeIden;
    @Basic
    @Column(name = "fileinfo")
    private String fileInfo;
    @Basic
    @Column(name = "filedata")
    private String fileData;
    @Basic
    @Column(name = "desenintention")
    private String desenIntention;
    @Basic
    @Column(name = "desenrequirements")
    private String desenRequirements;
    @Basic
    @Column(name = "desencontrolset")
    private String desenControlSet;


    public String getRecFlagInfoId() {
        return recFlagInfoId;
    }

    public void setRecFlagInfoId(String recFlagInfoId) {
        this.recFlagInfoId = recFlagInfoId;
    }

    public Integer getFileTypeIden() {
        return fileTypeIden;
    }

    public void setFileTypeIden(Integer fileTypeIden) {
        this.fileTypeIden = fileTypeIden;
    }

    public String getFileInfo() {
        return fileInfo;
    }

    public void setFileInfo(String fileInfo) {
        this.fileInfo = fileInfo;
    }

    public String getFileData() {
        return fileData;
    }

    public void setFileData(String fileData) {
        this.fileData = fileData;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        IndDesen indDesen = (IndDesen) o;
        return Objects.equals(recFlagInfoId, indDesen.recFlagInfoId) && Objects.equals(fileTypeIden, indDesen.fileTypeIden) && Objects.equals(fileInfo, indDesen.fileInfo) && Objects.equals(fileData, indDesen.fileData) && Objects.equals(desenIntention, indDesen.desenIntention) && Objects.equals(desenRequirements, indDesen.desenRequirements) && Objects.equals(desenControlSet, indDesen.desenControlSet);
    }

    @Override
    public int hashCode() {
        return Objects.hash(recFlagInfoId, fileTypeIden, fileInfo, fileData, desenIntention, desenRequirements, desenControlSet);
    }
}

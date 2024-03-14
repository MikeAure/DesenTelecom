package com.lu.gademo.entity.split;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "rec_split_desen")
public class RecSplitDesen {
    @Id
    @Column(name = "recstoinfoid")
    private String recStoInfoID;

    @Column(name = "filetypeiden")
    private int fileTypeIden;

    @Column(name = "fileinfo")
    private String fileInfo;

    @Column(name = "filedata")
    private String fileData;

    @Column(name = "desenintention")
    private String desenIntention;

    @Column(name = "desenrequirements")
    private String desenRequirements;

    @Column(name = "desencontrolset")
    private String desenControlSet;

    public String getRecStoInfoID() {
        return recStoInfoID;
    }

    public void setRecStoInfoID(String recStoInfoID) {
        this.recStoInfoID = recStoInfoID;
    }

    public int getFileTypeIden() {
        return fileTypeIden;
    }

    public void setFileTypeIden(int fileTypeIden) {
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
}

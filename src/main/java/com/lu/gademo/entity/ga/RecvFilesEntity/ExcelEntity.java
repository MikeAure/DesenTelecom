package com.lu.gademo.entity.ga.RecvFilesEntity;


import org.springframework.stereotype.Component;

@Component
public class ExcelEntity {//返回给京东的数据格式
    String source;
    String database;
    String datatable;
    String attributeName;
    String infoContent;
    String infoOwner;
    String infoOwnerID;
    String infoCreateTime;
    String applicationScene;
    String dataHierClassifyPerformTime;
    String dataTypeResult;
    String dataLevelResult;

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getDatatable() {
        return datatable;
    }

    public void setDatatable(String datatable) {
        this.datatable = datatable;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    public String getInfoContent() {
        return infoContent;
    }

    public void setInfoContent(String infoContent) {
        this.infoContent = infoContent;
    }

    public String getInfoOwner() {
        return infoOwner;
    }

    public void setInfoOwner(String infoOwner) {
        this.infoOwner = infoOwner;
    }

    public String getInfoOwnerID() {
        return infoOwnerID;
    }

    public void setInfoOwnerID(String infoOwnerID) {
        this.infoOwnerID = infoOwnerID;
    }

    public String getInfoCreateTime() {
        return infoCreateTime;
    }

    public void setInfoCreateTime(String infoCreateTime) {
        this.infoCreateTime = infoCreateTime;
    }

    public String getApplicationScene() {
        return applicationScene;
    }

    public void setApplicationScene(String applicationScene) {
        this.applicationScene = applicationScene;
    }

    public String getDataHierClassifyPerformTime() {
        return dataHierClassifyPerformTime;
    }

    public void setDataHierClassifyPerformTime(String dataHierClassifyPerformTime) {
        this.dataHierClassifyPerformTime = dataHierClassifyPerformTime;
    }

    public String getDataTypeResult() {
        return dataTypeResult;
    }

    public void setDataTypeResult(String dataTypeResult) {
        this.dataTypeResult = dataTypeResult;
    }

    public String getDataLevelResult() {
        return dataLevelResult;
    }

    public void setDataLevelResult(String dataLevelResult) {
        this.dataLevelResult = dataLevelResult;
    }

    @Override
    public String toString() {
        return "ExcelEntity{" +
                "source='" + source + '\'' +
                ", database='" + database + '\'' +
                ", datatable='" + datatable + '\'' +
                ", attributeName='" + attributeName + '\'' +
                ", infoContent='" + infoContent + '\'' +
                ", infoOwner='" + infoOwner + '\'' +
                ", infoOwnerID='" + infoOwnerID + '\'' +
                ", infoCreateTime='" + infoCreateTime + '\'' +
                ", applicationScene='" + applicationScene + '\'' +
                ", dataHierClassifyPerformTime='" + dataHierClassifyPerformTime + '\'' +
                ", dataTypeResult='" + dataTypeResult + '\'' +
                ", dataLevelResult='" + dataLevelResult + '\'' +
                '}';
    }
}

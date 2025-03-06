package com.lu.gademo.dto.officeComment;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;

import java.util.Objects;


public class InformationRecognition {
    private String contentType;
    private String content;
    private String attributeName;
    private String informationRecognitionTime;

    @JsonCreator
    public InformationRecognition(
            @JsonProperty("内容类型") String contentType,
            @JsonProperty("内容") String content,
            @JsonProperty("属性名称") String attributeName,
            @JsonProperty("信息识别时间") String informationRecognitionTime) {
        this.contentType = contentType;
        this.content = content;
        this.attributeName = attributeName;
        this.informationRecognitionTime = informationRecognitionTime;
    }

    public InformationRecognition() {
    }

    @JsonGetter("内容类型")
    public String getContentType() {
        return contentType;
    }

    @JsonSetter("内容类型")
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    @JsonGetter("内容")
    public String getContent() {
        return content;
    }

    @JsonSetter("内容")
    public void setContent(String content) {
        this.content = content;
    }

    @JsonGetter("属性名称")
    public String getAttributeName() {
        return attributeName;
    }

    @JsonSetter("属性名称")
    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    @JsonGetter("信息识别时间")
    public String getInformationRecognitionTime() {
        return informationRecognitionTime;
    }

    @JsonSetter("信息识别时间")
    public void setInformationRecognitionTime(String informationRecognitionTime) {
        this.informationRecognitionTime = informationRecognitionTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InformationRecognition that = (InformationRecognition) o;
        return Objects.equals(getContentType(), that.getContentType()) && Objects.equals(getContent(), that.getContent()) && Objects.equals(getAttributeName(), that.getAttributeName()) && Objects.equals(getInformationRecognitionTime(), that.getInformationRecognitionTime());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getContentType(), getContent(), getAttributeName(), getInformationRecognitionTime());
    }

    @Override
    public String toString() {
        return "InformationRecognition{" +
                "contentType='" + contentType + '\'' +
                ", content='" + content + '\'' +
                ", attributeName='" + attributeName + '\'' +
                ", informationRecognitionTime='" + informationRecognitionTime + '\'' +
                '}';
    }
}

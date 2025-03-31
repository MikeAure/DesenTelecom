package com.lu.gademo.dto.officeComment;

import com.fasterxml.jackson.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InformationRecognition extends BaseDateFormatter {
    @JsonProperty("内容类型")
    private String contentType;
    @JsonProperty("内容")
    private String content;
    @JsonProperty("属性名称")
    private String attributeName;
    @JsonProperty("信息识别时间")
    private LocalDateTime informationRecognitionTime;

    @JsonCreator
    public InformationRecognition(
            @JsonProperty("内容类型") String contentType,
            @JsonProperty("内容") String content,
            @JsonProperty("属性名称") String attributeName,
            @JsonProperty("信息识别时间") String informationRecognitionTime) {
        this.contentType = contentType;
        this.content = content;
        this.attributeName = attributeName;
        this.informationRecognitionTime = LocalDateTime.parse(informationRecognitionTime, sdf);
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
        return sdf.format(informationRecognitionTime);
    }

    @JsonSetter("信息识别时间")
    public void setInformationRecognitionTime(String informationRecognitionTime) {
        this.informationRecognitionTime = LocalDateTime.parse(informationRecognitionTime, sdf);
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

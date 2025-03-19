package com.lu.gademo.dto.officeComment;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;

import java.util.Objects;

public class WordComment {
    private InformationRecognition informationRecognition;
    private CategoryAndGrade categoryAndGrade;
    private DesensitizationOperation desensitizationOperation;
    private DesensitizationEvaluation desensitizationEvaluation;

    @JsonCreator
    public WordComment(
            @JsonProperty("信息识别")InformationRecognition informationRecognition,
            @JsonProperty("分类分级") CategoryAndGrade categoryAndGrade,
            @JsonProperty("脱敏操作") DesensitizationOperation desensitizationOperation,
            @JsonProperty("脱敏效果评估") DesensitizationEvaluation desensitizationEvaluation) {
        this.informationRecognition = informationRecognition;
        this.categoryAndGrade = categoryAndGrade;
        this.desensitizationOperation = desensitizationOperation;
        this.desensitizationEvaluation = desensitizationEvaluation;
    }

    @JsonCreator
    public WordComment(
            @JsonProperty("信息识别")InformationRecognition informationRecognition,
            @JsonProperty("分类分级") CategoryAndGrade categoryAndGrade) {
        this.informationRecognition = informationRecognition;
        this.categoryAndGrade = categoryAndGrade;
        this.desensitizationOperation = null;
        this.desensitizationEvaluation = null;
    }

    @JsonGetter("信息识别")
    public InformationRecognition getInformationRecognition() {
        return informationRecognition;
    }

    @JsonSetter("信息识别")
    public void setInformationRecognition(InformationRecognition informationRecognition) {
        this.informationRecognition = informationRecognition;
    }

    @JsonGetter("分类分级")
    public CategoryAndGrade getCategoryAndGrade() {
        return categoryAndGrade;
    }

    @JsonSetter("分类分级")
    public void setCategoryAndGrade(CategoryAndGrade categoryAndGrade) {
        this.categoryAndGrade = categoryAndGrade;
    }

    @JsonGetter("脱敏操作")
    public DesensitizationOperation getDesensitizationOperation() {
        return desensitizationOperation;
    }

    @JsonSetter("脱敏操作")
    public void setDesensitizationOperation(DesensitizationOperation desensitizationOperation) {
        this.desensitizationOperation = desensitizationOperation;
    }

    @JsonGetter("脱敏效果评估")
    public DesensitizationEvaluation getDesensitizationEvaluation() {
        return desensitizationEvaluation;
    }

    @JsonSetter("脱敏效果评估")
    public void setDesensitizationEvaluation(DesensitizationEvaluation desensitizationEvaluation) {
        this.desensitizationEvaluation = desensitizationEvaluation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WordComment that = (WordComment) o;
        return Objects.equals(getInformationRecognition(), that.getInformationRecognition()) && Objects.equals(getCategoryAndGrade(), that.getCategoryAndGrade()) && Objects.equals(getDesensitizationOperation(), that.getDesensitizationOperation()) && Objects.equals(getDesensitizationEvaluation(), that.getDesensitizationEvaluation());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getInformationRecognition(), getCategoryAndGrade(), getDesensitizationOperation(), getDesensitizationEvaluation());
    }

    @Override
    public String toString() {
        return "WordComment{" +
                "informationRecognition=" + informationRecognition +
                ", categoryAndGrade=" + categoryAndGrade +
                ", desensitizationOperation=" + desensitizationOperation +
                ", desensitizationEvaluation=" + desensitizationEvaluation +
                '}';
    }
}

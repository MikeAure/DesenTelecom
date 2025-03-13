package com.lu.gademo.dto.officeComment;

import com.fasterxml.jackson.annotation.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class DesensitizationEvaluation {
    public static class EvaluationResult extends BaseDateFormatter {
        private String evaluationMethod = "";
        private String evaluationConclusion = "";
        private LocalDateTime  desensitizationEvaluationTime = LocalDateTime.now();

        @JsonCreator
        public EvaluationResult(
                @JsonProperty("评估方法") String evaluationMethod,
                @JsonProperty("评估结论") String evaluationConclusion,
                @JsonProperty("脱敏评估时间") String desensitizationEvaluationTime) {
            this.evaluationMethod = evaluationMethod;
            this.evaluationConclusion = evaluationConclusion;
            this.desensitizationEvaluationTime = LocalDateTime.parse(desensitizationEvaluationTime, sdf);
        }

        public EvaluationResult(String evaluationMethod, String evaluationConclusion, LocalDateTime desensitizationEvaluationTime) {
            this.evaluationMethod = evaluationMethod;
            this.evaluationConclusion = evaluationConclusion;
            this.desensitizationEvaluationTime = desensitizationEvaluationTime;
        }

        public EvaluationResult() {
        }

        @JsonGetter("评估方法")
        public String getEvaluationMethod() {
            return evaluationMethod;
        }

        @JsonSetter("评估方法")
        public void setEvaluationMethod(String evaluationMethod) {
            this.evaluationMethod = evaluationMethod;
        }

        @JsonGetter("评估结论")
        public String getEvaluationConclusion() {
            return evaluationConclusion;
        }

        @JsonSetter("评估结论")
        public void setEvaluationConclusion(String evaluationConclusion) {
            this.evaluationConclusion = evaluationConclusion;
        }

        @JsonGetter("脱敏评估时间")
        public String getDesensitizationEvaluationTimeString() {
            return sdf.format(desensitizationEvaluationTime);
        }

        public LocalDateTime getDesensitizationEvaluationTime() {
            return desensitizationEvaluationTime;
        }

        @JsonSetter("脱敏评估时间")
        public void setDesensitizationEvaluationTime(String desensitizationEvaluationTimeString) {
            this.desensitizationEvaluationTime = LocalDateTime.parse(desensitizationEvaluationTimeString, sdf);
        }


        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            EvaluationResult that = (EvaluationResult) o;
            return Objects.equals(getEvaluationMethod(), that.getEvaluationMethod()) && Objects.equals(getEvaluationConclusion(), that.getEvaluationConclusion()) && Objects.equals(getDesensitizationEvaluationTime(), that.getDesensitizationEvaluationTime());
        }

        @Override
        public int hashCode() {
            return Objects.hash(getEvaluationMethod(), getEvaluationConclusion(), getDesensitizationEvaluationTime());
        }

        @Override
        public String toString() {
            return "DesensitizationResult{" +
                    "evaluationMethod='" + evaluationMethod + '\'' +
                    ", evaluationConclusion='" + evaluationConclusion + '\'' +
                    ", desensitizationEvaluationTime=" + desensitizationEvaluationTime +
                    '}';
        }
    }
    private EvaluationResult evaluationResult;

    @JsonCreator
    public DesensitizationEvaluation(
            @JsonProperty("评估结果") EvaluationResult evaluationResult) {
        this.evaluationResult = evaluationResult;
    }

    public DesensitizationEvaluation() {
    }

    @JsonGetter("评估结果")
    public EvaluationResult getDesensitizationResult() {
        return evaluationResult;
    }

    @JsonSetter("评估结果")
    public void setDesensitizationResult(EvaluationResult evaluationResult) {
        this.evaluationResult = evaluationResult;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DesensitizationEvaluation that = (DesensitizationEvaluation) o;
        return Objects.equals(getDesensitizationResult(), that.getDesensitizationResult());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getDesensitizationResult());
    }

    @Override
    public String toString() {
        return "DesensitizationEvaluation{" +
                "desensitizationResult=" + evaluationResult +
                '}';
    }
}

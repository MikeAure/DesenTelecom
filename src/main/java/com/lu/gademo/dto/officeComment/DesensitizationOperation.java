package com.lu.gademo.dto.officeComment;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class DesensitizationOperation {
    private AlgorithmChosen algorithmChosen;

    @JsonCreator
    public DesensitizationOperation(
            @JsonProperty("算法选择") AlgorithmChosen algorithmChosen) {
        this.algorithmChosen = algorithmChosen;
    }

    @JsonGetter("算法选择")
    public AlgorithmChosen getAlgorithmChosen() {
        return algorithmChosen;
    }

    @JsonSetter("算法选择")
    public void setAlgorithmChosen(AlgorithmChosen algorithmChosen) {
        this.algorithmChosen = algorithmChosen;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DesensitizationOperation that = (DesensitizationOperation) o;
        return Objects.equals(getAlgorithmChosen(), that.getAlgorithmChosen());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getAlgorithmChosen());
    }

    @Override
    public String toString() {
        return "DesensitizationOperation{" +
                "algorithmChosen=" + algorithmChosen +
                '}';
    }

    public static class AlgorithmChosen extends BaseDateFormatter {
        private String algorithmCategory = "";
        private String algorithmName = "";
        private String parameterMagnitude = "";
        private LocalDateTime desensitizationOperationTime = LocalDateTime.now();

        @JsonCreator
        public AlgorithmChosen(
                @JsonProperty("算法类别") String algorithmCategory,
                @JsonProperty("算法名称") String algorithmName,
                @JsonProperty("参数强度") String parameterMagnitude,
                @JsonProperty("脱敏操作时间") String desensitizationOperationTime) {
            this.algorithmCategory = algorithmCategory;
            this.algorithmName = algorithmName;
            this.parameterMagnitude = parameterMagnitude;
            this.desensitizationOperationTime = LocalDateTime.parse(desensitizationOperationTime, sdf);
        }

        public AlgorithmChosen(String algorithmCategory, String algorithmName, String parameterMagnitude, LocalDateTime desensitizationOperationTime) {
            this.algorithmCategory = algorithmCategory;
            this.algorithmName = algorithmName;
            this.parameterMagnitude = parameterMagnitude;
            this.desensitizationOperationTime = desensitizationOperationTime;
        }

        public AlgorithmChosen() {
        }

        @JsonGetter("算法类别")
        public String getAlgorithmCategory() {
            return algorithmCategory;
        }

        @JsonSetter("算法类别")
        public void setAlgorithmCategory(String algorithmCategory) {
            this.algorithmCategory = algorithmCategory;
        }

        @JsonGetter("算法名称")
        public String getAlgorithmName() {
            return algorithmName;
        }

        @JsonSetter("算法名称")
        public void setAlgorithmName(String algorithmName) {
            this.algorithmName = algorithmName;
        }

        @JsonGetter("参数强度")
        public String getParameterMagnitude() {
            return parameterMagnitude;
        }

        @JsonSetter("参数强度")
        public void setParameterMagnitude(String parameterMagnitude) {
            this.parameterMagnitude = parameterMagnitude;
        }

        @JsonGetter("脱敏操作时间")
        public String getDesensitizationOperationTime() {
            return sdf.format(desensitizationOperationTime);
        }

        @JsonSetter("脱敏操作时间")
        public void setDesensitizationOperationTime(String desensitizationOperationTime) {
            this.desensitizationOperationTime = LocalDateTime.parse(desensitizationOperationTime, sdf);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            AlgorithmChosen that = (AlgorithmChosen) o;
            return Objects.equals(getAlgorithmCategory(), that.getAlgorithmCategory()) && Objects.equals(getAlgorithmName(), that.getAlgorithmName()) && Objects.equals(getParameterMagnitude(), that.getParameterMagnitude()) && Objects.equals(getDesensitizationOperationTime(), that.getDesensitizationOperationTime()) && Objects.equals(sdf, that.sdf);
        }

        @Override
        public int hashCode() {
            return Objects.hash(getAlgorithmCategory(), getAlgorithmName(), getParameterMagnitude(), getDesensitizationOperationTime(), sdf);
        }

        @Override
        public String toString() {
            return "AlgorithmChosen{" +
                    "algorithmCategory='" + algorithmCategory + '\'' +
                    ", algorithmName='" + algorithmName + '\'' +
                    ", parameterMagnitude='" + parameterMagnitude + '\'' +
                    ", desensitizationOperationTime=" + desensitizationOperationTime +
                    '}';
        }
    }
}

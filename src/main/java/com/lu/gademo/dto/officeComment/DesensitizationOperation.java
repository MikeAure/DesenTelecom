package com.lu.gademo.dto.officeComment;


import com.fasterxml.jackson.annotation.*;
import lombok.*;

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

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Getter
    @Setter
    @ToString
    @NoArgsConstructor
    public static class AlgorithmChosen extends BaseDateFormatter {
        @JsonProperty("算法类别")
        private String algorithmCategory = "";
        @JsonProperty("算法名称")
        private String algorithmName = "";
        @JsonProperty("参数强度")
        private String parameterMagnitude = "";
        @JsonProperty("脱敏操作时间")
        private LocalDateTime desensitizationOperationTime = LocalDateTime.now();
        @JsonIgnore
        private int desenAlgNum;
        @JsonIgnore
        private String desenAlgParam;
        
        public AlgorithmChosen(
                String algorithmCategory,
                String algorithmName,
                String parameterMagnitude,
                String desensitizationOperationTime){
            this.algorithmCategory = algorithmCategory;
            this.algorithmName = algorithmName;
            this.parameterMagnitude = parameterMagnitude;
            this.desensitizationOperationTime = LocalDateTime.parse(desensitizationOperationTime, sdf);
            this.desenAlgNum = 0;
            this.desenAlgParam = "0";
        }

        public AlgorithmChosen(
                String algorithmCategory,
                String algorithmName,
                String parameterMagnitude,
                String desensitizationOperationTime,
                int desenAlgNum,
                String desenAlgParam){
            this.algorithmCategory = algorithmCategory;
            this.algorithmName = algorithmName;
            this.parameterMagnitude = parameterMagnitude;
            this.desensitizationOperationTime = LocalDateTime.parse(desensitizationOperationTime, sdf);
            this.desenAlgNum = desenAlgNum;
            this.desenAlgParam = desenAlgParam;
        }

        public AlgorithmChosen(
                String algorithmCategory,
                String algorithmName,
                String parameterMagnitude,
                LocalDateTime desensitizationOperationTime,
                int desenAlgNum,
                String desenAlgParam) {
            this.algorithmCategory = algorithmCategory;
            this.algorithmName = algorithmName;
            this.parameterMagnitude = parameterMagnitude;
            this.desensitizationOperationTime = desensitizationOperationTime;
            this.desenAlgNum = desenAlgNum;
            this.desenAlgParam = desenAlgParam;

        }

        @JsonProperty("脱敏操作时间")
        public String getTimeString() {
            return sdf.format(desensitizationOperationTime);
        }


    }
}

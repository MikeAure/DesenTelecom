package com.lu.gademo.dto.officeComment;


import com.fasterxml.jackson.annotation.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DesensitizationOperation {
    @JsonProperty("算法选择")
    private AlgorithmChosen algorithmChosen;

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
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
        private LocalDateTime desensitizationOperationTime = LocalDateTime.now();
        @JsonIgnore
        private int desenAlgNum;
        @JsonIgnore
        private String desenAlgParam;

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


        public AlgorithmChosen(
                String algorithmCategory,
                String algorithmName,
                String parameterMagnitude,
                String desensitizationOperationTime,
                int desenAlgNum,
                String desenAlgParam) {
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

        public AlgorithmChosen(
                String algorithmCategory,
                String algorithmName,
                String parameterMagnitude,
                LocalDateTime desensitizationOperationTime
        ) {
            this.algorithmCategory = algorithmCategory;
            this.algorithmName = algorithmName;
            this.parameterMagnitude = parameterMagnitude;
            this.desensitizationOperationTime = desensitizationOperationTime;
            this.desenAlgNum = 1;
            this.desenAlgParam = "1";

        }

        @JsonProperty("脱敏操作时间")
        public String getDesensitizationOperationTime() {
            return sdf.format(desensitizationOperationTime);
        }

        @JsonProperty("脱敏操作时间")
        public void setDesensitizationOperationTime(String desensitizationOperationTime) {
            this.desensitizationOperationTime = LocalDateTime.parse(desensitizationOperationTime, sdf);
        }
    }
}

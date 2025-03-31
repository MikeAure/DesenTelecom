package com.lu.gademo.dto.officeComment;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.lu.gademo.json.deserializer.StringToBoolDeserializer;
import com.lu.gademo.json.serializer.BoolToStringSerializer;
import lombok.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DesensitizationEvaluation {
    @Getter
    @Setter
    @ToString
    @EqualsAndHashCode(callSuper = false)
    @AllArgsConstructor
    @NoArgsConstructor
    public static class EvaluationConclusion {
        @JsonProperty("可逆性")
        public String reversible = "";
        @JsonProperty("偏差性")
        public String deviate = "";
        @JsonProperty("损失性")
        public String infoLoss = "";
    }
    @Getter
    @Setter
    @ToString
    @EqualsAndHashCode(callSuper = false)
    @AllArgsConstructor
    @NoArgsConstructor
    public static class EvaluationResult  {

        @JsonProperty("评估方法")
        private String evaluationMethod = "";
        @JsonProperty("评估结论")
        private EvaluationConclusion evaluationConclusion;
        @JsonProperty("结果")
//        @JsonSerialize(using= BoolToStringSerializer.class)
//        @JsonDeserialize(using= StringToBoolDeserializer.class)
        private boolean evalResult = false;
        @JsonProperty("脱敏评估时间")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
        private LocalDateTime  desensitizationEvaluationTime = LocalDateTime.now();
        @JsonIgnore
        private final DateTimeFormatter sdf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


        public EvaluationResult(
                @JsonProperty("评估方法") String evaluationMethod,
                @JsonProperty("评估结论") EvaluationConclusion evaluationConclusion,
                @JsonProperty("结果") boolean evaResult,
                @JsonProperty("脱敏评估时间") String desensitizationEvaluationTime) {
            this.evaluationMethod = evaluationMethod;
            this.evaluationConclusion = evaluationConclusion;
            this.evalResult = evaResult;
            this.desensitizationEvaluationTime = LocalDateTime.parse(desensitizationEvaluationTime, sdf);
        }

        @JsonCreator
        public EvaluationResult(
                @JsonProperty("评估方法") String evaluationMethod,
                @JsonProperty("评估结论") EvaluationConclusion evaluationConclusion,
                @JsonProperty("结果") String evaResult,
                @JsonProperty("脱敏评估时间") String desensitizationEvaluationTime) {
            this.evaluationMethod = evaluationMethod;
            this.evaluationConclusion = evaluationConclusion;
            this.evalResult = evaResult.equals("正确");
            this.desensitizationEvaluationTime = LocalDateTime.parse(desensitizationEvaluationTime, sdf);
        }

        @JsonProperty("脱敏评估时间")
        public String getDesensitizationEvaluationTimeString() {
            return sdf.format(desensitizationEvaluationTime);
        }

        @JsonProperty("脱敏评估时间")
        public void setDesensitizationEvaluationTime(String desensitizationEvaluationTimeString) {
            this.desensitizationEvaluationTime = LocalDateTime.parse(desensitizationEvaluationTimeString, sdf);
        }

        @JsonProperty("结果")
        public String getEvalResultString() {
            return evalResult ? "正确" : "错误";
        }

        @JsonProperty("结果")
        public void setEvalResultString(String evalResultString) {
            this.evalResult = evalResultString.equals("正确");
        }

    }
    @JsonProperty("评估结果")
    private EvaluationResult evaluationResult;

}

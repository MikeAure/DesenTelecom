package com.lu.gademo.dto.officeComment;

import com.fasterxml.jackson.annotation.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CategoryAndGrade {
    @JsonProperty("属性分类")
    private AttributeCategory attributeCategory;
    @JsonProperty("属性分级")
    private AttributeGrade attributeGrade;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class AttributeCategory extends BaseDateFormatter {
        @JsonProperty("一级类别")
        private String firstCategory = "";
        @JsonProperty("二级类别")
        private String secondCategory = "";
        @JsonProperty("信息分类时间")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
        private LocalDateTime informationCategoryTime = LocalDateTime.now();

        @JsonCreator
        public AttributeCategory(
                @JsonProperty("一级类别") String firstCategory,
                @JsonProperty("二级类别") String secondCategory,
                @JsonProperty("信息分类时间") String informationCategoryTime) {
            this.firstCategory = firstCategory;
            this.secondCategory = secondCategory;
            this.informationCategoryTime = LocalDateTime.parse(informationCategoryTime, sdf);
        }

        @JsonProperty("信息分类时间")
        public String getInformationCategoryTime() {
            return sdf.format(informationCategoryTime);
        }

        @JsonProperty("信息分类时间")
        public void setInformationCategoryTime(String informationCategoryTime) {
            this.informationCategoryTime = LocalDateTime.parse(informationCategoryTime);
        }
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class AttributeGrade extends BaseDateFormatter {
        @JsonProperty("总级数")
        private int totalGrade;
        @JsonProperty("当前等级")
        private int currentGrade;
        @JsonProperty("信息分级时间")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
        private LocalDateTime informationGradeTime;

        @JsonCreator
        public AttributeGrade(
                @JsonProperty("总级数") String totalGrade,
                @JsonProperty("当前等级") String currentGrade,
                @JsonProperty("信息分级时间") String informationGradeTime) {
            this.totalGrade = Integer.parseInt(totalGrade);
            this.currentGrade = Integer.parseInt(currentGrade);
            this.informationGradeTime = LocalDateTime.parse(informationGradeTime, sdf);

        }

//        public AttributeGrade() {
//
//        }

        public AttributeGrade(
                int totalGrade,
                int currentGrade,
                String informationGradeTime) {
            this.totalGrade = totalGrade;
            this.currentGrade = currentGrade;
            this.informationGradeTime = LocalDateTime.parse(informationGradeTime, sdf);

        }


        @JsonProperty("总级数")
        public String getTotalGradeString() {
            return String.valueOf(totalGrade);
        }

//        @JsonSetter("总级数")
//        public void setTotalGrade(int totalGrade) {
//            this.totalGrade = totalGrade;
//        }

        @JsonProperty("总级数")
        public void setTotalGrade(String totalGrade) {
            this.totalGrade = Integer.parseInt(totalGrade);
        }


        @JsonProperty("当前等级")
        public String getCurrentGradeString() {
            return String.valueOf(currentGrade);
        }


        @JsonProperty("当前等级")
        public void setCurrentGrade(String currentGrade) {
            this.currentGrade = Integer.parseInt(currentGrade);
        }

        @JsonProperty("信息分级时间")
        public String getInformationGradeTime() {
            return sdf.format(informationGradeTime);
        }

        @JsonProperty("信息分级时间")
        public void setInformationGradeTime(String informationGradeTime) {
            this.informationGradeTime = LocalDateTime.parse(informationGradeTime, sdf);
        }
    }

}

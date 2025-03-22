package com.lu.gademo.dto.officeComment;

import com.fasterxml.jackson.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CategoryAndGrade {
    private AttributeCategory attributeCategory;
    private AttributeGrade attributeGrade;

    public CategoryAndGrade() {

    }

    @JsonCreator
    public CategoryAndGrade(
            @JsonProperty("属性分类") AttributeCategory attributeCategory,
            @JsonProperty("属性分级") AttributeGrade attributeGrade) {
        this.attributeCategory = attributeCategory;
        this.attributeGrade = attributeGrade;
    }

    @JsonGetter("属性分类")
    public AttributeCategory getAttributeCategory() {
        return attributeCategory;
    }

    @JsonSetter("属性分类")
    public void setAttributeCategory(AttributeCategory attributeCategory) {
        this.attributeCategory = attributeCategory;
    }

    @JsonGetter("属性分级")
    public AttributeGrade getAttributeGrade() {
        return attributeGrade;
    }

    @JsonSetter("属性分级")
    public void setAttributeGrade(AttributeGrade attributeGrade) {
        this.attributeGrade = attributeGrade;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CategoryAndGrade that = (CategoryAndGrade) o;
        return Objects.equals(getAttributeCategory(), that.getAttributeCategory()) && Objects.equals(getAttributeGrade(), that.getAttributeGrade());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getAttributeCategory(), getAttributeGrade());
    }

    @Override
    public String toString() {
        return "CategoryAndGrade{" +
                "attributeCategory=" + attributeCategory +
                ", attributeGrade=" + attributeGrade +
                '}';
    }

    public static class AttributeCategory extends BaseDateFormatter {
        private String firstCategory = "";
        private String secondCategory = "";
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

//        public AttributeCategory() {
//        }

        @JsonProperty("一级类别")
        public String getFirstCategory() {
            return firstCategory;
        }

        @JsonProperty("一级类别")
        public void setFirstCategory(String firstCategory) {
            this.firstCategory = firstCategory;
        }

        @JsonProperty("二级类别")
        public String getSecondCategory() {
            return secondCategory;
        }

        @JsonProperty("二级类别")
        public void setSecondCategory(String secondCategory) {
            this.secondCategory = secondCategory;
        }

        @JsonProperty("信息分类时间")
        public String getInformationCategoryTime() {
            return sdf.format(informationCategoryTime);
        }

        @JsonProperty("信息分类时间")
        public void setInformationCategoryTime(String informationCategoryTime) {
            this.informationCategoryTime = LocalDateTime.parse(informationCategoryTime);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            AttributeCategory that = (AttributeCategory) o;
            return Objects.equals(getFirstCategory(), that.getFirstCategory()) && Objects.equals(getSecondCategory(), that.getSecondCategory()) && Objects.equals(getInformationCategoryTime(), that.getInformationCategoryTime());
        }

        @Override
        public int hashCode() {
            return Objects.hash(getFirstCategory(), getSecondCategory(), getInformationCategoryTime());
        }

        @Override
        public String toString() {
            return "AttributeCategory{" +
                    "firstCategory='" + firstCategory + '\'' +
                    ", secondCategory='" + secondCategory + '\'' +
                    ", informationCategoryTime='" + informationCategoryTime + '\'' +
                    '}';
        }
    }

    public static class AttributeGrade extends BaseDateFormatter {
        private int totalGrade;
        private int currentGrade;
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

        public int getTotalGrade() {
            return totalGrade;
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
            this.totalGrade = Integer.valueOf(totalGrade);
        }


        public int getCurrentGrade() {
            return currentGrade;
        }

        @JsonProperty("当前等级")
        public String getCurrentGradeString() {
            return String.valueOf(currentGrade);
        }

//        @JsonSetter("当前等级")
//        public void setCurrentGrade(int currentGrade) {
//            this.currentGrade = currentGrade;
//        }

        @JsonProperty("当前等级")
        public void setCurrentGrade(String currentGrade) {
            this.currentGrade = Integer.valueOf(currentGrade);
        }

        @JsonProperty("信息分级时间")
        public String getInformationGradeTime() {
            return sdf.format(informationGradeTime);
        }

        @JsonProperty("信息分级时间")
        public void setInformationGradeTime(String informationGradeTime) {
            this.informationGradeTime = LocalDateTime.parse(informationGradeTime, sdf);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            AttributeGrade that = (AttributeGrade) o;
            return getTotalGrade() == that.getTotalGrade() && getCurrentGrade() == that.getCurrentGrade() && Objects.equals(getInformationGradeTime(), that.getInformationGradeTime()) && Objects.equals(sdf, that.sdf);
        }

        @Override
        public int hashCode() {
            return Objects.hash(getTotalGrade(), getCurrentGrade(), getInformationGradeTime(), sdf);
        }

        @Override
        public String toString() {
            return "AttributeGrade{" +
                    "totalGrade=" + totalGrade +
                    ", currentGrade=" + currentGrade +
                    ", informationGradeTime=" + informationGradeTime +
                    '}';
        }
    }

}

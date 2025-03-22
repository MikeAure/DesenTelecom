package com.lu.gademo.dto;

import com.lu.gademo.model.AlgorithmType;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class AlgorithmDisplayInfoDto {
    private int id;

    private String algorithmName;

    private String algorithmAbbreviation;

    private String low;

    private String medium;

    private String high;

    private AlgorithmType type;

    private String requirement;

    private Boolean ifInteger;

    private Boolean ifMinus;

    private int paramsLength;

    private String min;

    private String max;

    public String getAlgorithmName() {
        return algorithmName;
    }

    public Boolean getIfInteger() {
        return ifInteger;
    }

    public void setIfInteger(Boolean ifInteger) {
        this.ifInteger = ifInteger;
    }

    public Boolean getIfMinus() {
        return ifMinus;
    }

    public void setIfMinus(Boolean ifMinus) {
        this.ifMinus = ifMinus;
    }

    public int getParamsLength() {
        return paramsLength;
    }

    public void setParamsLength(int paramsLength) {
        this.paramsLength = paramsLength;
    }

    public String getMin() {
        return min;
    }

    public void setMin(String min) {
        this.min = min;
    }

    public String getMax() {
        return max;
    }

    public void setMax(String max) {
        this.max = max;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setAlgorithmName(String algorithmName) {
        this.algorithmName = algorithmName;
    }

    public String getAlgorithmAbbreviation() {
        return algorithmAbbreviation;
    }

    public void setAlgorithmAbbreviation(String algorithmAbbreviation) {
        this.algorithmAbbreviation = algorithmAbbreviation;
    }

    public String getLow() {
        return low;
    }

    public void setLow(String low) {
        this.low = low;
    }

    public String getMedium() {
        return medium;
    }

    public void setMedium(String medium) {
        this.medium = medium;
    }

    public String getHigh() {
        return high;
    }

    public void setHigh(String high) {
        this.high = high;
    }

    public int getType() {
        return type.getValue();
    }

    public void setType(AlgorithmType type) {
        this.type = type;
    }

    public String getRequirement() {
        return requirement;
    }

    public void setRequirement(String requirement) {
        this.requirement = requirement;
    }

    @Override
    public String toString() {
        return "AlgorithmDisplayInfoDto{" +
                "id=" + id +
                ", algorithmName='" + algorithmName + '\'' +
                ", algorithmAbbreviation='" + algorithmAbbreviation + '\'' +
                ", low='" + low + '\'' +
                ", medium='" + medium + '\'' +
                ", high='" + high + '\'' +
                ", type=" + type +
                ", requirement='" + requirement + '\'' +
                ", ifInteger=" + ifInteger +
                ", ifMinus=" + ifMinus +
                ", paramsLength=" + paramsLength +
                ", min='" + min + '\'' +
                ", max='" + max + '\'' +
                '}';
    }
}

package com.lu.gademo.dto;

import com.lu.gademo.utils.AlgorithmType;
import com.lu.gademo.utils.AlgorithmsFactory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class AlgorithmDisplayInfoDto {
    private int id;

    @Getter
    private String algorithmName;

    private String algorithmAbbreviation;

    private String low;

    private String medium;

    private String high;

    private AlgorithmType type;

    private String requirement;

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
                ", type=" + type.getValue() +
                ", requirement='" + requirement + '\'' +
                '}';
    }
}

package com.lu.gademo.entity.ga;

import com.lu.gademo.utils.AlgorithmType;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DesensitizationAlgorithm {
    private Integer id;

    private String algorithmName;

    private String algorithmAbbreviation;

    private String applicableDataModes;

    private Byte reversible;

    private String optionalParameters;

    private String low;

    private String medium;

    private String high;

    private AlgorithmType type;

    private Integer originalId;

    private Integer modal;

    private String requirement;

    private Boolean ifModify;

}
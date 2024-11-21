package com.lu.gademo.entity.ga;

import com.lu.gademo.model.ModalTypes;
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

    private String optionalParameters;

    private String low;

    private String medium;

    private String high;

    private AlgorithmType type;

    private Integer originalId;

    private ModalTypes modal;

    private String requirement;

    private Boolean ifModify;

    private Boolean ifInteger;

    private Boolean ifMinus;

    private int paramsLength;

    private String min;

    private String max;
}
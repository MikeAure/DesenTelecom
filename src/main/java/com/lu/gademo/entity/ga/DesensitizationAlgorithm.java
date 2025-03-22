package com.lu.gademo.entity.ga;

import com.lu.gademo.model.ModalTypes;
import com.lu.gademo.model.AlgorithmType;
import lombok.*;

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
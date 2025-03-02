package com.lu.gademo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DocxDesenRequirement {
    String commentId;
    String desenRequirementItemName;
    int dataType;
    int algoNum;
    int privacyLevel;
    String target;
}

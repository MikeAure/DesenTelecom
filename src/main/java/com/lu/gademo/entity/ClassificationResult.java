package com.lu.gademo.entity;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class ClassificationResult {
    String columnType;
    String columnName;
    int columnLevel;
}

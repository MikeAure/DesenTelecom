package com.lu.gademo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
@AllArgsConstructor
@NoArgsConstructor
public class QueryDesenLevel {
    private String infoID;
    private String attributeName;
    private String dataType;
    private String applicationScene;
}

package com.lu.gademo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

/**
 * 用于课题四传输数据，结构化数据脱敏等级信息
 */
@Data
@Slf4j
@AllArgsConstructor
@NoArgsConstructor
public class StructuredDataDesenLevelInfo {
    private LocalDateTime time;
    private String applicationScene;
    private String fieldName;
    private int fieldLevel;
}

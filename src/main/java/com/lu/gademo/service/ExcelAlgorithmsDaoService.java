package com.lu.gademo.service;

import java.util.List;
import java.util.Map;

public interface ExcelAlgorithmsDaoService {
    Map<String, List<Map<String, Object>>> getAlgorithmsByType();
}

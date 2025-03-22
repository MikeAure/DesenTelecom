package com.lu.gademo.service;

import com.lu.gademo.entity.ga.AlgorithmMapping;
import com.lu.gademo.entity.ga.DesensitizationAlgorithm;
import org.apache.ibatis.annotations.Param;

public interface AlgorithmMappingDaoService {
    AlgorithmMapping selectAlgorithmIdByAttributeName(String attributeName);
    DesensitizationAlgorithm selectAlgorithmInfoByAttributeName(String attributeName);
}

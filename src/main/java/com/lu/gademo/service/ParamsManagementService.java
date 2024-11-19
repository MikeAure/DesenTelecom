package com.lu.gademo.service;

import com.lu.gademo.dto.AlgorithmDisplayInfoDto;
import com.lu.gademo.dto.AlgorithmInfoParamDto;
import com.lu.gademo.utils.AlgorithmInfo;

import java.util.List;
import java.util.Map;

public interface ParamsManagementService {
    List<AlgorithmDisplayInfoDto> getAllAlgorithmInfoDisplay();

    int updateAlgorithmParamsInBatch(List<AlgorithmInfoParamDto> algorithms);

    boolean reloadAlgorithmFactory();

    Map<String, AlgorithmInfo> getAlgorithmMap();
}

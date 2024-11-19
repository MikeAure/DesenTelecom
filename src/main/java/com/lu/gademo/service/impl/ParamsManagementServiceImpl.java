package com.lu.gademo.service.impl;

import com.lu.gademo.dto.AlgorithmDisplayInfoDto;
import com.lu.gademo.dto.AlgorithmInfoParamDto;
import com.lu.gademo.service.AlgorithmInfoDaoService;
import com.lu.gademo.service.ParamsManagementService;
import com.lu.gademo.utils.AlgorithmInfo;
import com.lu.gademo.utils.AlgorithmsFactory;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@Data
public class ParamsManagementServiceImpl implements ParamsManagementService {

    private final AlgorithmInfoDaoService algorithmInfoDaoService;

    private final AlgorithmsFactory algorithmsFactory;

    @Autowired
    public ParamsManagementServiceImpl(AlgorithmInfoDaoService algorithmInfoDaoService, AlgorithmsFactory algorithmsFactory) {
        this.algorithmInfoDaoService = algorithmInfoDaoService;
        this.algorithmsFactory = algorithmsFactory;
    }

    @Override
    public List<AlgorithmDisplayInfoDto> getAllAlgorithmInfoDisplay() {
        return algorithmInfoDaoService.getAllAlgorithmInfoDisplay();
    }

    @Override
    public int updateAlgorithmParamsInBatch(List<AlgorithmInfoParamDto> algorithms) {
        return algorithmInfoDaoService.updateAlgorithmParamsInBatch(algorithms);
    }

    @Override
    public boolean reloadAlgorithmFactory() {
        return algorithmsFactory.reload();
    }

    @Override
    public Map<String, AlgorithmInfo> getAlgorithmMap() {
        return algorithmsFactory.getAlgorithmInfoMap();
    }

}

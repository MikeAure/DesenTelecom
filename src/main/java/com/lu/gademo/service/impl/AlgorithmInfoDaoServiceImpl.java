package com.lu.gademo.service.impl;

import com.lu.gademo.dto.AlgorithmDisplayInfoDto;
import com.lu.gademo.dto.AlgorithmInfoParamDto;
import com.lu.gademo.entity.ga.DesensitizationAlgorithm;
import com.lu.gademo.mapper.ga.AlgorithmInfoDao;
import com.lu.gademo.service.AlgorithmInfoDaoService;
import com.lu.gademo.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AlgorithmInfoDaoServiceImpl implements AlgorithmInfoDaoService {
    private final AlgorithmInfoDao algorithmInfoDao;
    private final Dp dp;
    private final Generalization generalization;
    private final Anonymity anonymity;
    private final Replace replace;

    public AlgorithmInfoDaoServiceImpl(AlgorithmInfoDao algorithmInfoDao, Dp dp, Generalization generalization, Anonymity anonymity, Replace replace) {
        this.algorithmInfoDao = algorithmInfoDao;
        this.dp = dp;
        this.generalization = generalization;
        this.anonymity = anonymity;
        this.replace = replace;
    }

    @Override
    public List<DesensitizationAlgorithm> getAllAlgorithmsRawInfo() {
        return algorithmInfoDao.getAllAlgorithmInfo();
    }

    @Override
    public List<AlgorithmInfo> getAllAlgorithmInfoConvertObject() {
        return algorithmInfoDao.getAllAlgorithmInfoConvertObject();
    }

    @Override
    public List<AlgorithmInfo> getAllAlgorithmInfoConvertObjectFromRawInfo() {
        List<DesensitizationAlgorithm> rawInfo = algorithmInfoDao.getAllAlgorithmInfo();
        return rawInfoList2AlgorithmInfoList(rawInfo);
    }

    @Override
    public List<AlgorithmDisplayInfoDto> getAllAlgorithmInfoDisplay() {
        return algorithmInfoDao.getAllAlgorithmInfoDisplay();
    }

    @Override
    public Map<String, AlgorithmInfo> getAllAlgorithmInfoMap() {
        return getAllAlgorithmInfoConvertObjectFromRawInfo().stream()
                .collect(Collectors.toMap(AlgorithmInfo::getName, Function.identity()));
    }

    @Override
    public List<DesensitizationAlgorithm> getAlgorithmInfoById(int id) {
        return algorithmInfoDao.getAlgorithmInfoById(id);
    }

    @Override
    public List<AlgorithmInfo> getAlgorithmInfoConvertObjectById(int id) {
        return rawInfoList2AlgorithmInfoList(algorithmInfoDao.getAllAlgorithmInfo());
    }

    @Override
    public List<DesensitizationAlgorithm> getAlgorithmInfoByTypeAndOriginalId(AlgorithmType type, int originalId) {
        return algorithmInfoDao.getAlgorithmInfoByTypeAndOriginalId(type.getValue(), originalId);
    }

    @Override
    public List<AlgorithmInfo> getAlgorithmInfoConvertObjectByTypeAndOriginalId(AlgorithmType type, int originalId) {
        return rawInfoList2AlgorithmInfoList(algorithmInfoDao.getAlgorithmInfoByTypeAndOriginalId(type.getValue(), originalId));
    }

    @Override
    public List<DesensitizationAlgorithm> getAlgorithmInfoByTypeAndOriginalId(int type, int originalId) {
        return algorithmInfoDao.getAlgorithmInfoByTypeAndOriginalId(type, originalId);
    }

    @Override
    public List<AlgorithmInfo> getAlgorithmInfoConvertObjectByTypeAndOriginalId(int type, int originalId) {
        return rawInfoList2AlgorithmInfoList(algorithmInfoDao.getAlgorithmInfoByTypeAndOriginalId(type, originalId));
    }

    @Override
    public int updateAlgorithmParams(int id, String low, String medium, String high) {
        return algorithmInfoDao.updateAlgorithmParams(id, low, medium, high);
    }

    @Override
    public int updateAlgorithmParams(AlgorithmInfo algorithmInfo) {
        String low = (String) algorithmInfo.getParams().get(0);
        String medium = (String) algorithmInfo.getParams().get(1);
        String high = (String) algorithmInfo.getParams().get(2);
        int id = algorithmInfo.getId();

        return algorithmInfoDao.updateAlgorithmParams(id, low, medium, high);
    }

    @Override
    public int updateAlgorithmParamsInBatch(List<AlgorithmInfoParamDto> algorithms) {
        return algorithmInfoDao.updateAlgorithmParamsUpdateBatch(algorithms);
    }

    private BaseDesenAlgorithm getExecutorByType(AlgorithmType type) {
        switch (type) {
            case DP:
                return dp;
            case GENERALIZATION:
                return generalization;
            case ANONYMITY:
                return anonymity;
            case REPLACEMENT:
                return replace;
            default:
                return null;
        }
    }

    private List<AlgorithmInfo> rawInfoList2AlgorithmInfoList(List<DesensitizationAlgorithm> desensitizationAlgorithms) {
        return desensitizationAlgorithms.stream().map(
                algorithm -> new AlgorithmInfo(
                        algorithm.getAlgorithmName(),
                        algorithm.getId(),
                        algorithm.getType(),
                        algorithm.getOriginalId(),
                        (algorithm.getLow() == null && algorithm.getMedium() == null && algorithm.getHigh() == null)?
                                Collections.emptyList() :
                        Arrays.asList(algorithm.getLow(), algorithm.getMedium(), algorithm.getHigh()),
                        getExecutorByType(algorithm.getType()),
                        algorithm.getRequirement(),
                        algorithm.getModal()
                )
        ).collect(Collectors.toList());
    }


}

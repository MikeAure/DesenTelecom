package com.lu.gademo.service.impl;

import com.lu.gademo.entity.ga.AlgorithmMapping;
import com.lu.gademo.entity.ga.DesensitizationAlgorithm;
import com.lu.gademo.mapper.ga.AlgorithmMappingDao;
import com.lu.gademo.service.AlgorithmMappingDaoService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Data
public class AlgorithmMappingDaoServiceImpl implements AlgorithmMappingDaoService {
    private final AlgorithmMappingDao algorithmMappingDao;

    @Override
    public AlgorithmMapping selectAlgorithmIdByAttributeName(String attributeName) {
        return algorithmMappingDao.selectAlgorithmIdByAttributeName(attributeName);
    }

    @Override
    public DesensitizationAlgorithm selectAlgorithmInfoByAttributeName(String attributeName) {
        return algorithmMappingDao.selectAlgorithmInfoByAttributeName(attributeName);
    }
}

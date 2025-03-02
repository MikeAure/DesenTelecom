package com.lu.gademo.service.impl;

import com.lu.gademo.entity.ga.ExcelAlgorithm;
import com.lu.gademo.mapper.ga.ExcelAlgorithmsDao;
import com.lu.gademo.service.ExcelAlgorithmsDaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ExcelAlgorithmsDaoServiceImpl implements ExcelAlgorithmsDaoService {
    ExcelAlgorithmsDao excelAlgorithmsDao;

    @Autowired
    public ExcelAlgorithmsDaoServiceImpl(ExcelAlgorithmsDao excelAlgorithmsDao) {
        this.excelAlgorithmsDao = excelAlgorithmsDao;
    }

    /**
     * 获取支持Excel工具的所有算法
     * @return
     */
    public Map<String, List<Map<String, Object>>> getAlgorithmsByType() {
        List<ExcelAlgorithm> algorithms = excelAlgorithmsDao.getAllAlgorithms();
        Map<String, List<Map<String, Object>>> result = new HashMap<>();

        for (ExcelAlgorithm algorithm : algorithms) {
            String type = algorithm.getType();
            Map<String, Object> valueMap = new HashMap<>();
            valueMap.put("value", algorithm.getOriginalId());
            valueMap.put("label", algorithm.getChineseName());
            // 将每个 type 映射到相应的 List 中
            result.computeIfAbsent(type, k -> new ArrayList<>()).add(valueMap);
        }

        return result;
    }
}

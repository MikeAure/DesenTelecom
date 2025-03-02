package com.lu.gademo.utils;

import com.lu.gademo.service.AlgorithmInfoDaoService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Data
@Component
public class AlgorithmsFactory {

    private Map<String, AlgorithmInfo> algorithmInfoMap;
    private AlgorithmInfoDaoService algorithmInfoDaoService;

    @Autowired
    public AlgorithmsFactory(AlgorithmInfoDaoService algorithmInfoDaoService) {
        this.algorithmInfoMap = new HashMap<>();
        this.algorithmInfoDaoService = algorithmInfoDaoService;
    }

    @PostConstruct
    public void init() {
        algorithmInfoMap = algorithmInfoDaoService.getAllAlgorithmInfoMap();
        log.info(algorithmInfoMap.toString());
    }

    /**
     * 重新加载算法信息
     * @return 重新加载是否成功
     */
    public synchronized boolean reload() {
        int previousSize = algorithmInfoMap.size();
        algorithmInfoMap = algorithmInfoDaoService.getAllAlgorithmInfoMap();
        return algorithmInfoMap.size() == previousSize;
    }

    public AlgorithmInfo getAlgorithmInfoFromName(String name) {
        return algorithmInfoMap.get(name);
    }

    public AlgorithmInfo getAlgorithmInfoFromId(int id) {
        return algorithmInfoMap.values().stream()
                .filter(algorithmInfo -> algorithmInfo.getId() == id)
                .findFirst().orElse(null);
    }

    public String getAlgorithmInfoNameById(int id) {
        AlgorithmInfo target =  algorithmInfoMap.values().stream()
                .filter(algorithmInfo -> algorithmInfo.getId() == id)
                .findFirst().orElse(null);
        if (target != null) {
            return target.getName();
        }
        return "";
    }
}

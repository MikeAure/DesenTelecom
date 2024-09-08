package com.lu.gademo.controller;

import com.lu.gademo.dao.ga.effectEva.SendEvaReqDao;
import com.lu.gademo.dto.QueryDesenLevel;
import com.lu.gademo.dto.StructuredDataDesenLevelInfo;
import com.lu.gademo.entity.ga.effectEva.SendEvaReq;
import com.lu.gademo.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("desenLevel")
public class QueryDesenLevelController {
    private SendEvaReqDao sendEvaReqDao;
    private Map<String, String> sceneNameMapper;

    @Autowired
    public QueryDesenLevelController(SendEvaReqDao sendEvaReqDao) {
        this.sendEvaReqDao = sendEvaReqDao;
        this.sceneNameMapper = new HashMap<>();
        this.sceneNameMapper.put("客户资源管理", "customer_desen_msg");

    }

    @PostMapping(value = "structuredQuery", produces = "application/json;charset=UTF-8")
    Result<?> queryDesenLevel(@RequestBody QueryDesenLevel req) {
        // TODO: 实现对于对方发送的场景名请求的转换
        String rawSceneName = req.getApplicationScene();
        String sceneName = this.sceneNameMapper.get(rawSceneName);
        String fieldName = req.getAttributeName();

        if (Objects.equals(sceneName, "") || sceneName == null) {
            return new Result<>(500, "Can not find requst scene name", "");
        }

        List<SendEvaReq> resultList = sendEvaReqDao.findByFileTypeContains(sceneName, Sort.by(Sort.Direction.DESC,
                "desenPerformEndTime"));
        SendEvaReq newestResult = resultList.get(0);
        // 找到一个脱敏等级最低的策略返回
        for (SendEvaReq item : resultList) {
            log.info("获取到的记录的脱敏完成时间: {}", item.getDesenPerformEndTime());
            if (item.getFileType().contains("low")) {
                newestResult = item;
                break;
            }
        }
        log.info("获取到的记录的脱敏完成时间: {}", newestResult.getDesenPerformEndTime());
        if (resultList.isEmpty()) {
            return new Result<>(500, "Find no result", "");
        }
        StructuredDataDesenLevelInfo infoResult = new StructuredDataDesenLevelInfo();
        // 设置返回结果内容
        infoResult.setTime(LocalDateTime.now());
        infoResult.setFieldName(fieldName);
        infoResult.setFieldLevel(-1);
        infoResult.setApplicationScene(rawSceneName);
        // 从发送给评测系统的请求日志中获取最新的日志
        // 从Entity中获取到desenInfoPreIden的内容，并分割为List
        List<String> desenInfoPreIden = Arrays.asList(newestResult.getDesenInfoPreIden().split(","));
        // 从Entity中获取desenLevel，并分割为List
        List<String> desenLevelList = Arrays.asList(newestResult.getDesenLevel().split(","));
        Map<String, String> paramMap = new HashMap<>();
        for (int i = 0; i < desenInfoPreIden.size(); i++) {
            paramMap.put(desenInfoPreIden.get(i), desenLevelList.get(i));
        }

        for (Map.Entry<String, String> entry : paramMap.entrySet()) {
            if (entry.getKey().contains(fieldName)) {
                infoResult.setFieldLevel(Integer.parseInt(entry.getValue()));
                break;
            }
        }
        return new Result<>(200, "Success", infoResult);

    }
}

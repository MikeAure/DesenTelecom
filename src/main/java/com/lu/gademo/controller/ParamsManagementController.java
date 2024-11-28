package com.lu.gademo.controller;

import com.lu.gademo.dto.AlgorithmDisplayInfoDto;
import com.lu.gademo.dto.AlgorithmInfoParamDto;
import com.lu.gademo.entity.ga.DesensitizationAlgorithm;
import com.lu.gademo.service.AlgorithmInfoDaoService;
import com.lu.gademo.service.ParamsManagementService;
import com.lu.gademo.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequestMapping("paramsManagement")
public class ParamsManagementController {

    private final ParamsManagementService paramsManagementService;
    private final AlgorithmInfoDaoService algorithmInfoDaoService;

    @Autowired
    public ParamsManagementController(ParamsManagementService paramsManagementService, AlgorithmInfoDaoService algo) {
        this.paramsManagementService = paramsManagementService;
        this.algorithmInfoDaoService = algo;
    }

    @GetMapping("page")
    String returnPage() {
        return "params_management";
    }

    @ResponseBody
    @GetMapping("reloadAlgorithmFactory")
    public Result<?> reloadAlgorithmFactory() {
        return paramsManagementService.reloadAlgorithmFactory() ?
                Result.success(paramsManagementService.getAlgorithmMap()) : Result.error();
    }

    @ResponseBody
    @PostMapping("updateAlgorithmParams")
    public Result<?> updateAlgorithmParamsInBatch(@RequestBody List<AlgorithmInfoParamDto> algorithms) {
        try {
            for (AlgorithmInfoParamDto paramsDto : algorithms) {
                // 根据ID获取算法的校验规则
                DesensitizationAlgorithm algorithmInfo = algorithmInfoDaoService.getAlgorithmInfoById(paramsDto.getId()).get(0);
                if (algorithmInfo == null) {
                    return Result.error("算法ID " + paramsDto.getId() + " 不存在");
                }
                // 校验参数
                String errorMessage = validateParams(algorithmInfo, paramsDto);
                if (!StringUtils.isBlank(errorMessage)) {
                    return Result.error(errorMessage);
                }
            }
            // 所有参数校验通过，执行更新操作
            int updateCount = paramsManagementService.updateAlgorithmParamsInBatch(algorithms);
            return (updateCount != 0) ? Result.success() : Result.error("更新失败");
        } catch (Exception e) {
            // 处理异常
            return Result.error("服务器内部错误：" + e.getMessage());
        }
    }

    @ResponseBody
    @GetMapping("getAllAlgorithmInfoDisplay")
    public Result<List<AlgorithmDisplayInfoDto>> getAllAlgorithmInfoDisplay() {
        return Result.success(paramsManagementService.getAllAlgorithmInfoDisplay());
    }

    private String validateParams(DesensitizationAlgorithm algorithmInfo, AlgorithmInfoParamDto paramsDto) {
        StringBuilder errorMessage = new StringBuilder();

        // 获取校验规则
        int id = algorithmInfo.getId();
        boolean ifInteger = algorithmInfo.getIfInteger();
        boolean ifMinus = algorithmInfo.getIfMinus();
        int paramsLength = algorithmInfo.getParamsLength();
        List<Double> minValues = parseParams(algorithmInfo.getMin());
        List<Double> maxValues = parseParams(algorithmInfo.getMax());

        // 校验 low、medium、high 参数
        Map<String, String> paramsToValidate = new HashMap<>();
        paramsToValidate.put("low", paramsDto.getLow());
        paramsToValidate.put("medium", paramsDto.getMedium());
        paramsToValidate.put("high", paramsDto.getHigh());

        for (Map.Entry<String, String> entry : paramsToValidate.entrySet()) {
            String paramName = entry.getKey();
            String paramValue = entry.getValue();

            if (StringUtils.isEmpty(paramValue)) {
                continue; // 跳过空参数
            }

            List<String> paramValues = Arrays.asList(paramValue.split(","));

            // 校验参数数量
            if (paramValues.size() != paramsLength) {
                errorMessage.append(String.format("算法%d: %s 参数数量应为 %d 个。", id, paramName, paramsLength));
                continue;
            }

            for (int i = 0; i < paramValues.size(); i++) {
                String valueStr = paramValues.get(i).trim();
                double value;

                try {
                    value = Double.parseDouble(valueStr);
                } catch (NumberFormatException e) {
                    errorMessage.append(String.format("算法%d: %s 的第 %d 个参数不是有效的数字。", id, paramName, i + 1));
                    continue;
                }

                // 校验是否为整数
                if (ifInteger && value % 1 != 0) {
                    errorMessage.append(String.format("算法%d: %s 的第 %d 个参数应为整数。", id, paramName, i + 1));
                }

                // 校验是否包含负数
                if (!ifMinus && value < 0) {
                    errorMessage.append(String.format("算法%d: %s 的第 %d 个参数不应为负数。", id, paramName, i + 1));
                }

                // 获取对应的最小值和最大值
                double minValue = (minValues.size() == 1) ? minValues.get(0) : minValues.get(i);
                double maxValue = (maxValues.size() == 1) ? maxValues.get(0) : maxValues.get(i);

                // 校验参数范围
                if (value < minValue || value > maxValue) {
                    errorMessage.append(String.format("算法%d: %s 的第 %d 个参数应在 %f 到 %f 之间。", id, paramName, i + 1, minValue, maxValue));
                }
            }
        }

        return errorMessage.toString();
    }

    private List<Double> parseParams(String paramsStr) {
        return Arrays.stream(paramsStr.split(","))
                .map(String::trim)
                .map(Double::parseDouble)
                .collect(Collectors.toList());
    }

}

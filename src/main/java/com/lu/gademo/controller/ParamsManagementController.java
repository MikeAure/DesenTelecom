package com.lu.gademo.controller;

import com.lu.gademo.dto.AlgorithmDisplayInfoDto;
import com.lu.gademo.dto.AlgorithmInfoParamDto;
import com.lu.gademo.service.ParamsManagementService;
import com.lu.gademo.utils.AlgorithmsFactory;
import com.lu.gademo.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("paramsManagement")
public class ParamsManagementController {

    private final ParamsManagementService paramsManagementService;

    @Autowired
    public ParamsManagementController(ParamsManagementService paramsManagementService) {
        this.paramsManagementService = paramsManagementService;
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
        return (paramsManagementService.updateAlgorithmParamsInBatch(algorithms) != 0) ? Result.success() : Result.error();
    }

    @ResponseBody
    @GetMapping("getAllAlgorithmInfoDisplay")
    public Result<List<AlgorithmDisplayInfoDto>> getAllAlgorithmInfoDisplay() {
        return Result.success(paramsManagementService.getAllAlgorithmInfoDisplay());
    }
}

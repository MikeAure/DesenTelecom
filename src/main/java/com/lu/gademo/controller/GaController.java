package com.lu.gademo.controller;

import com.lu.gademo.dto.AlgorithmDisplayInfoDto;
import com.lu.gademo.dto.AlgorithmInfoDto;
import com.lu.gademo.dto.AlgorithmInfoParamDto;
import com.lu.gademo.entity.ga.DesensitizationAlgorithm;
import com.lu.gademo.entity.ga.SceneInfo;
import com.lu.gademo.service.AlgorithmInfoDaoService;
import com.lu.gademo.service.SceneInfoDaoService;
import com.lu.gademo.service.impl.ExcelAlgorithmsDaoServiceImpl;
import com.lu.gademo.service.impl.ToolsetServiceImpl;
import com.lu.gademo.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
public class GaController extends BaseController {

    private final ToolsetServiceImpl toolsetServiceImpl;
    private final SceneInfoDaoService sceneInfoDaoService;
    private final List<SceneInfo> allSceneInfos;
    private final ExcelAlgorithmsDaoServiceImpl excelAlgorithmsDaoService;
    private final AlgorithmInfoDaoService algorithmInfoDaoService;

    @Autowired
    public GaController(ToolsetServiceImpl toolsetServiceImpl, SceneInfoDaoService sceneInfoDaoService, List<SceneInfo> allSceneInfos, ExcelAlgorithmsDaoServiceImpl excelAlgorithmsDaoService, AlgorithmInfoDaoService algorithmInfoDaoService) {
        this.toolsetServiceImpl = toolsetServiceImpl;
        this.sceneInfoDaoService = sceneInfoDaoService;
        this.allSceneInfos = allSceneInfos;
        this.excelAlgorithmsDaoService = excelAlgorithmsDaoService;
        this.algorithmInfoDaoService = algorithmInfoDaoService;
    }

    @RequestMapping(value = {"/", "/index"})
    public String index(Model model) {
        model.addAttribute("scenes", allSceneInfos);
        return "index.html";
    }

    @RequestMapping(value = {"/home"})
    public String home() {
        return "home.html";
    }


    // 五十场景模板
    @GetMapping(value = {"/fifty_scene/{name}"})
    public String getFiftyScenesView(@PathVariable String name, Model model) {
        model.addAttribute("sheet", name);
//        return "fifty_scene/" + name;
        return "fifty_scene/fifty_scene";

    }

    // 脱敏算法验证  按照模态分类
    @GetMapping(value = {"/verify/{name}"})
    public String getVerifyViews(@PathVariable String name, Model model) {

        Map<String, List<Map<String, Object>>> algorithmsByType = excelAlgorithmsDaoService.getAlgorithmsByType();
        String defaultAlgName = "";
        if (!name.equals("graph")) {
            defaultAlgName = toolsetServiceImpl.getDefaultTool(name);
        }
        switch (name) {
            case "text":
                break;
            case "excel":
                model.addAttribute("scenes", allSceneInfos);
                model.addAttribute("algorithmsByType", algorithmsByType);
                break;
            case "audio":
                break;
            case "image":
                break;
            case "video":
                break;
            case "graph":
                break;
            default:
                break;
        }
        log.info("defaultAlgName: {}", defaultAlgName);
        model.addAttribute("defaultAlgName", defaultAlgName);

        return "verify/" + name;
    }

    // 脱敏工具设置
    @GetMapping(value = {"/desentools/{toolName}"})
    public String getDesenToolsView(@PathVariable String toolName, Model model) {
        switch (toolName) {
            case "differential_privacy_laplace": {
                DesensitizationAlgorithm laplace = algorithmInfoDaoService.getAlgorithmInfoById(3).get(0);
                AlgorithmInfoParamDto laplaceDto = new AlgorithmInfoParamDto(laplace.getId(), laplace.getLow(),
                        laplace.getMedium(), laplace.getHigh());
                DesensitizationAlgorithm laplaceImage = algorithmInfoDaoService.getAlgorithmInfoById(44).get(0);
                AlgorithmInfoParamDto laplaceImageDto = new AlgorithmInfoParamDto(laplaceImage.getId(), laplaceImage.getLow(),
                        laplaceImage.getMedium(), laplaceImage.getHigh());
                DesensitizationAlgorithm laplaceGraph = algorithmInfoDaoService.getAlgorithmInfoById(60).get(0);
                AlgorithmInfoParamDto laplaceGraphDto = new AlgorithmInfoParamDto(laplaceGraph.getId(), laplaceGraph.getLow(),
                        laplaceGraph.getMedium(), laplaceGraph.getHigh());
                DesensitizationAlgorithm laplaceAudio = algorithmInfoDaoService.getAlgorithmInfoById(70).get(0);
                AlgorithmInfoParamDto laplaceAudioDto = new AlgorithmInfoParamDto(laplaceAudio.getId(), laplaceAudio.getLow(),
                        laplaceAudio.getMedium(), laplaceAudio.getHigh());
                DesensitizationAlgorithm laplaceImage2 = algorithmInfoDaoService.getAlgorithmInfoById(45).get(0);
                AlgorithmInfoParamDto laplaceImage2Dto = new AlgorithmInfoParamDto(laplaceImage2.getId(), laplaceImage2.getLow(),
                        laplaceImage2.getMedium(), laplaceImage2.getHigh());
                DesensitizationAlgorithm laplaceDpDate = algorithmInfoDaoService.getAlgorithmInfoById(1).get(0);
                AlgorithmInfoParamDto laplaceDpDateDto = new AlgorithmInfoParamDto(laplaceDpDate.getId(), laplaceDpDate.getLow(),
                        laplaceDpDate.getMedium(), laplaceDpDate.getHigh());

                model.addAttribute("laplace", laplaceDto);
                model.addAttribute("laplaceImage", laplaceImageDto);
                model.addAttribute("laplaceGraph", laplaceGraphDto);
                model.addAttribute("laplaceAudio", laplaceAudioDto);
                model.addAttribute("laplaceImage2", laplaceImage2Dto);
                model.addAttribute("laplaceDpDate", laplaceDpDateDto);
                break;
            }
            default: {
                break;
            }
        }
        return "desentools/" + toolName;
    }

    @GetMapping(value = {"/templateManagement"})
    public String getTemplateManagementView(Model model) {
        model.addAttribute("scenes", allSceneInfos);
        return "templateManagement/scenario.html";
    }

    @GetMapping(value = {"/performenceTest"})
    public String getPerformanceTestView() {
        return "performancetest.html";
    }

    @ResponseBody
    @GetMapping(value = "/toolset/getDefaultSelection")
    public Result<?> getDefaultSelectionView(String toolsetName) {
        String defaultAlgName = toolsetServiceImpl.getDefaultTool(toolsetName);
        if (defaultAlgName != null) {
            return new Result<>(200, "success", defaultAlgName);
        }
        return new Result<>(500, "failed", null);
    }

    @ResponseBody
    @PostMapping(value = {"/toolset/setDefaultToolset"})
    public Result<?> setDefaultToolset(String toolsetName, String defaultAlgName) {
        if (toolsetServiceImpl.setDefaultTool(toolsetName, defaultAlgName)) {
            return new Result<>(200, "success", null);
        }
        return new Result<>(500, "failed", null);
    }


}

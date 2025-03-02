package com.lu.gademo.controller;

import com.lu.gademo.dto.AlgorithmInfoParamDto;
import com.lu.gademo.entity.ga.DesensitizationAlgorithm;
import com.lu.gademo.entity.ga.SceneInfo;
import com.lu.gademo.service.*;
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

    private final ToolsetService toolsetService;
    private final SceneInfoDaoService sceneInfoDaoService;
    private final List<SceneInfo> allSceneInfos;
    private final ExcelAlgorithmsDaoService excelAlgorithmsDaoService;
    private final AlgorithmInfoDaoService algorithmInfoDaoService;
    private final GraphPoiService graphPoiService;

    @Autowired
    public GaController(ToolsetServiceImpl toolsetService, SceneInfoDaoService sceneInfoDaoService, List<SceneInfo> allSceneInfos, ExcelAlgorithmsDaoServiceImpl excelAlgorithmsDaoService, AlgorithmInfoDaoService algorithmInfoDaoService, GraphPoiService graphPoiService) {
        this.toolsetService = toolsetService;
        this.sceneInfoDaoService = sceneInfoDaoService;
        this.allSceneInfos = sceneInfoDaoService.getAllSceneInfos();
        this.excelAlgorithmsDaoService = excelAlgorithmsDaoService;
        this.algorithmInfoDaoService = algorithmInfoDaoService;
        this.graphPoiService = graphPoiService;
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
        Map<String, List<Map<String, Object>>> algorithmsByType = excelAlgorithmsDaoService.getAlgorithmsByType();
        model.addAttribute("scenes", allSceneInfos);
        model.addAttribute("algorithmsByType", algorithmsByType);
        model.addAttribute("sheet", name);
        return "fifty_scene/fifty_scene";
    }

    // 脱敏算法验证  按照模态分类
    @GetMapping(value = {"/verify/{name}"})
    public String getVerifyViews(@PathVariable String name, Model model) {

        Map<String, List<Map<String, Object>>> algorithmsByType = excelAlgorithmsDaoService.getAlgorithmsByType();
        String defaultAlgName = "";
        if (!name.equals("graph")) {
            defaultAlgName = toolsetService.getDefaultTool(name);
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
                DesensitizationAlgorithm reportNoiseMax1 = algorithmInfoDaoService.getAlgorithmInfoById(25).get(0);
                AlgorithmInfoParamDto reportNoiseMax1Dto = new AlgorithmInfoParamDto(reportNoiseMax1.getId(), reportNoiseMax1.getLow(),
                        reportNoiseMax1.getMedium(), reportNoiseMax1.getHigh());
                DesensitizationAlgorithm reportNoiseMax3 = algorithmInfoDaoService.getAlgorithmInfoById(34).get(0);
                AlgorithmInfoParamDto reportNoiseMax3Dto = new AlgorithmInfoParamDto(reportNoiseMax3.getId(), reportNoiseMax3.getLow(),
                        reportNoiseMax3.getMedium(), reportNoiseMax3.getHigh());
                DesensitizationAlgorithm snapping = algorithmInfoDaoService.getAlgorithmInfoById(23).get(0);
                AlgorithmInfoParamDto snappingDto = new AlgorithmInfoParamDto(snapping.getId(), snapping.getLow().split(",")[0],
                        snapping.getMedium().split(",")[0], snapping.getHigh().split(",")[0]);

                model.addAttribute("laplace", laplaceDto);
                model.addAttribute("laplaceImage", laplaceImageDto);
                model.addAttribute("laplaceGraph", laplaceGraphDto);
                model.addAttribute("laplaceAudio", laplaceAudioDto);
                model.addAttribute("laplaceImage2", laplaceImage2Dto);
                model.addAttribute("laplaceDpDate", laplaceDpDateDto);
                model.addAttribute("reportNoiseMax1", reportNoiseMax1Dto);
                model.addAttribute("reportNoiseMax3", reportNoiseMax3Dto);
                model.addAttribute("snapping", snappingDto);

                break;
            }
            case "differential_privacy_exp": {
                DesensitizationAlgorithm exponential = algorithmInfoDaoService.getAlgorithmInfoById(24).get(0);
                AlgorithmInfoParamDto exponentialDto = new AlgorithmInfoParamDto(exponential.getId(), exponential.getLow(),
                        exponential.getMedium(), exponential.getHigh());
                DesensitizationAlgorithm reportNoiseMax2 = algorithmInfoDaoService.getAlgorithmInfoById(26).get(0);
                AlgorithmInfoParamDto reportNoiseMax2Dto = new AlgorithmInfoParamDto(reportNoiseMax2.getId(), reportNoiseMax2.getLow(),
                        reportNoiseMax2.getMedium(), reportNoiseMax2.getHigh());
                DesensitizationAlgorithm reportNoiseMax4 = algorithmInfoDaoService.getAlgorithmInfoById(33).get(0);
                AlgorithmInfoParamDto reportNoiseMax4Dto = new AlgorithmInfoParamDto(reportNoiseMax4.getId(), reportNoiseMax4.getLow(),
                        reportNoiseMax4.getMedium(), reportNoiseMax4.getHigh());

                model.addAttribute("exponential", exponentialDto);
                model.addAttribute("reportNoiseMax2", reportNoiseMax2Dto);
                model.addAttribute("reportNoiseMax4", reportNoiseMax4Dto);
                break;
            }
            case "differential_privacy_svt": {
                DesensitizationAlgorithm svt1 = algorithmInfoDaoService.getAlgorithmInfoById(30).get(0);
                AlgorithmInfoParamDto svt1Dto = new AlgorithmInfoParamDto(svt1.getId(), svt1.getLow(),
                        svt1.getMedium(), svt1.getHigh());
                DesensitizationAlgorithm svt2 = algorithmInfoDaoService.getAlgorithmInfoById(31).get(0);
                AlgorithmInfoParamDto svt2Dto = new AlgorithmInfoParamDto(svt2.getId(), svt2.getLow(),
                        svt2.getMedium(), svt2.getHigh());
                DesensitizationAlgorithm svt3 = algorithmInfoDaoService.getAlgorithmInfoById(36).get(0);
                AlgorithmInfoParamDto svt3Dto = new AlgorithmInfoParamDto(svt3.getId(), svt3.getLow(),
                        svt3.getMedium(), svt3.getHigh());
                DesensitizationAlgorithm svt4 = algorithmInfoDaoService.getAlgorithmInfoById(37).get(0);
                AlgorithmInfoParamDto svt4Dto = new AlgorithmInfoParamDto(svt4.getId(), svt4.getLow(),
                        svt4.getMedium(), svt4.getHigh());
                DesensitizationAlgorithm svt5 = algorithmInfoDaoService.getAlgorithmInfoById(38).get(0);
                AlgorithmInfoParamDto svt5Dto = new AlgorithmInfoParamDto(svt5.getId(), svt5.getLow(),
                        svt5.getMedium(), svt5.getHigh());
                DesensitizationAlgorithm svt6 = algorithmInfoDaoService.getAlgorithmInfoById(39).get(0);
                AlgorithmInfoParamDto svt6Dto = new AlgorithmInfoParamDto(svt6.getId(), svt6.getLow(),
                        svt6.getMedium(), svt6.getHigh());
                DesensitizationAlgorithm svtNumerical = algorithmInfoDaoService.getAlgorithmInfoById(32).get(0);
                AlgorithmInfoParamDto svtNumericalDto = new AlgorithmInfoParamDto(svtNumerical.getId(), svtNumerical.getLow(),
                        svtNumerical.getMedium(), svtNumerical.getHigh());

                model.addAttribute("svt1", svt1Dto);
                model.addAttribute("svt2", svt2Dto);
                model.addAttribute("svt3", svt3Dto);
                model.addAttribute("svt4", svt4Dto);
                model.addAttribute("svt5", svt5Dto);
                model.addAttribute("svt6", svt6Dto);
                model.addAttribute("svtNumerical", svtNumericalDto);

                break;
            }
            case "differential_privacy_ldp": {
                DesensitizationAlgorithm dpCode = algorithmInfoDaoService.getAlgorithmInfoById(2).get(0);
                AlgorithmInfoParamDto dpCodeDto = new AlgorithmInfoParamDto(dpCode.getId(), dpCode.getLow(),
                        dpCode.getMedium(), dpCode.getHigh());

                model.addAttribute("dpCode", dpCodeDto);
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
        String defaultAlgName = toolsetService.getDefaultTool(toolsetName);
        if (defaultAlgName != null) {
            return Result.success(defaultAlgName);
        }
        return new Result<>(500, "failed", null);
    }

    @ResponseBody
    @PostMapping(value = {"/toolset/setDefaultToolset"})
    public Result<?> setDefaultToolset(String toolsetName, String defaultAlgName) {
        if (toolsetService.setDefaultTool(toolsetName, defaultAlgName)) {
            return new Result<>(200, "success", null);
        }
        return new Result<>(500, "failed", null);
    }

    @ResponseBody
    @PostMapping(value = "getPoiInfo")
    public Result<?> getPoiInfo(String id) {
        return Result.success(graphPoiService.getPoiById(id));
    }

    @ResponseBody
    @PostMapping(value = "getPoiInfos")
    public Result<?> getPoiInfos(@RequestBody List<String> ids) {
        return Result.success(graphPoiService.selectPoisByIds(ids));
    }

}

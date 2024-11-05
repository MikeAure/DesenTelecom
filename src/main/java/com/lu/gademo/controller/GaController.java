package com.lu.gademo.controller;

import com.lu.gademo.entity.ga.SceneInfo;
import com.lu.gademo.service.SceneInfoDaoService;
import com.lu.gademo.service.impl.ExcelAlgorithmsDaoServiceImpl;
import com.lu.gademo.service.impl.ToolsetService;
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
    private final ExcelAlgorithmsDaoServiceImpl excelAlgorithmsDaoService;

    @Autowired
    public GaController(ToolsetService toolsetService, SceneInfoDaoService sceneInfoDaoService, List<SceneInfo> allSceneInfos, ExcelAlgorithmsDaoServiceImpl excelAlgorithmsDaoService) {
        this.toolsetService = toolsetService;
        this.sceneInfoDaoService = sceneInfoDaoService;
        this.allSceneInfos = sceneInfoDaoService.getAllSceneInfos();
        this.excelAlgorithmsDaoService = excelAlgorithmsDaoService;
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
//        List<AlgorithmInfoDto> audioAlgorithmInfoDtoList = new ArrayList<AlgorithmInfoDto>(Arrays.asList(
//                new AlgorithmInfoDto(70, "dpAudio", "差分-基于差分隐私的声纹特征脱敏算法"),
//                new AlgorithmInfoDto(71, "voice_replace", "置换-声纹替换算法"),
//                new AlgorithmInfoDto(72, "apply_audio_effects", "置换-音频变形"),
//                new AlgorithmInfoDto(73, "audio_reshuffle", "置换-音频重排"),
//                new AlgorithmInfoDto(74, "audio_floor", "泛化-音频取整"),
//                new AlgorithmInfoDto(75, "audio_spec", "泛化-频域遮掩"),
//                new AlgorithmInfoDto(76, "audio_augmentation", "泛化-音频失真"),
//                new AlgorithmInfoDto(77, "audio_median", "泛化-基于均值的采样点替换")
//                ));
//        if (name.equals("audio")) {
//            model.addAttribute("distortionAudioAlgoList", audioAlgorithmInfoDtoList);
//            model.addAttribute("defaultOption", "audio_spec");
//        }
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
    public String getDesenToolsView(@PathVariable String toolName) {
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
            return new Result<>(200, "success", defaultAlgName);
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


}

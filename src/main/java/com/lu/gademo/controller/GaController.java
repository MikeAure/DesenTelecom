package com.lu.gademo.controller;

import com.lu.gademo.dao.ga.ToolsetDefaultToolDao;
import com.lu.gademo.service.impl.ToolsetService;
import com.lu.gademo.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class GaController extends BaseController {
    @Autowired
    private ToolsetService toolsetService;

    @RequestMapping(value = {"/", "/index"})
    public String index() {
        return "index";
    }

    @RequestMapping(value = {"/home"})
    public String home() {
        return "home";
    }

    @RequestMapping(value = {"/welcome"})
    public String welcome() {
        return "welcome";
    }

    // 五十场景模板
    @GetMapping(value = {"/fifty_scene/{name}"})
    public String getFiftyScenesView(@PathVariable String name) {
        return "fifty_scene/" + name;
    }

    // 脱敏算法验证  按照模态分类
    @GetMapping(value = {"/verify/{name}"})
    public String getVerifyViews(@PathVariable String name) {
        return "verify/" + name;
    }

    // 脱敏工具设置
    @GetMapping(value = {"/desentools/{toolName}"})
    public String getDesenToolsView(@PathVariable String toolName) {
        return "desentools/" + toolName;
    }

    @RequestMapping(value = {"/templateManagement"})
    public String getTemplateManagementView() {
        return "templateManagement/scenario";
    }

    @RequestMapping(value = {"/performenceTest"})
    public String getPerformanceTestView() {
        return "performancetest";
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

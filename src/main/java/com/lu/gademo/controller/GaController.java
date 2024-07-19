package com.lu.gademo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class GaController extends BaseController {


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
    @RequestMapping(value = {"/fifty_scene/{name}"})
    public String getFiftyScenesView(@PathVariable String name) {
        return "fifty_scene/" + name;
    }

    // 脱敏算法验证  按照模态分类
    @RequestMapping(value = {"/verify/{name}"})
    public String getVerifyViews(@PathVariable String name) {
        return "verify/" + name;
    }

    // 脱敏工具设置
    @RequestMapping(value = {"/desentools/{toolName}"})
    public String getDesenToolsView(@PathVariable String toolName) {
        return "desentools/" + toolName;
    }

    @RequestMapping(value = {"/templateManagement"})
    public String getTemplateManagementView() {
        return "templateManagement/scenario";
    }


}

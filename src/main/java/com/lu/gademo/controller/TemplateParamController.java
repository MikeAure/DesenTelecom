package com.lu.gademo.controller;

import com.lu.gademo.entity.ExcelParam;
import com.lu.gademo.service.ExcelParamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/")
@CrossOrigin("*")
public class TemplateParamController {
    @Autowired
    private ExcelParamService excelParamService;

    @ResponseBody
    @RequestMapping("/{typeName}param/list")
    public List<ExcelParam> getSceneParams(@PathVariable String typeName) {
        return excelParamService.getParamsByTableName(typeName + "_param");
    }

}

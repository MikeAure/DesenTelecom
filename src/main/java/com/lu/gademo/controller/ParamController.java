package com.lu.gademo.controller;

import com.lu.gademo.mapper.ExcelParamDao;
import com.lu.gademo.entity.ExcelParam;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;


//管理参数字段，param表
@Controller
@RequestMapping("/Param")
@Slf4j
public class ParamController {


    @Autowired
    private ExcelParamDao excelParamDao;

    //参数列表，
    /*使用到页面
    * 1.应用场景模板管理，可以展示50个场景参数表 list()，可以编辑，增加或者删除
    * 1.需求模板管理页面展示参数字段表，并且能够修改隐私保护等级
    * 2.样本管理展示参数表？
    *
    *

    */

    @ResponseBody
    @RequestMapping("/tablelist")
    public List<ExcelParam> tablelist(@RequestParam String tableName) {
        // 将 tableName 转换为小写
        String lowerCaseTableName = tableName.toLowerCase();
        // 查询参数
        List<ExcelParam> params = excelParamDao.findTable(lowerCaseTableName); // 这里传入表名
        return params;
    }


    //这里保存更新参数表
    @PostMapping("/saveData")
    public ResponseEntity<String> saveData(@RequestParam String tableName,@RequestBody List<ExcelParam> tableParams) {
        try {

            excelParamDao.deleteAll(tableName);
            // 在服务层中处理保存逻辑
            excelParamDao.saveTableParams(tableName,tableParams);

            return ResponseEntity.ok("数据保存成功！");
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("数据保存失败，请重试。");
        }
    }

    //删除数据库中的数，场景管理中用到
    @DeleteMapping("/deleteData/{id}")
    public ResponseEntity<String> deleteData(@RequestParam String tableName,@PathVariable int id) {
        try {
            excelParamDao.deleteById(tableName,id);
            return ResponseEntity.ok("数据删除成功");
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("数据删除失败");
        }
    }


    //返回类型参数,用来给出字段的函数
    @ResponseBody
    @RequestMapping("/statisticParam")
    public List<ExcelParam> statisticParam(
            @RequestParam String tableName,
            @RequestParam(value="dataType") Integer DataType
    ){
        //统计参数，DataType 表示统计参数类型
        List<ExcelParam> params= excelParamDao.getByDataType(tableName,DataType);
        return params;
    }

}

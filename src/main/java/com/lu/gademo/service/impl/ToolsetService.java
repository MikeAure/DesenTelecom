package com.lu.gademo.service.impl;

import com.lu.gademo.dao.ga.ToolsetDefaultToolDao;
import com.lu.gademo.entity.ga.ToolsetDefaultTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 对数据库中工具集默认工具进行操作
 */
@Service
public class ToolsetService {
    @Autowired
    private ToolsetDefaultToolDao toolsetDefaultToolDao;

    /**
     * 根据工具集名称获取默认工具
     * @param toolsetName 工具集名称
     * @return 默认工具名称
     */
    public String getDefaultTool(String toolsetName) {
        return toolsetDefaultToolDao.findByToolsetName(toolsetName).getDefaultAlgName();
    }

    /**
     * 设置工具集默认工具
     * @param toolsetName 工具集名称
     * @param defaultAlgName 默认工具名称
     */
    public boolean setDefaultTool(String toolsetName, String defaultAlgName) {
        ToolsetDefaultTool toolsetDefaultTool = toolsetDefaultToolDao.findByToolsetName(toolsetName);
        if (toolsetDefaultTool == null) {
            return false;
        }
        toolsetDefaultTool.setDefaultAlgName(defaultAlgName);
        toolsetDefaultToolDao.saveAndFlush(toolsetDefaultTool);
        return true;
    }

}

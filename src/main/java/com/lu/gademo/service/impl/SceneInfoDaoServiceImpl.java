package com.lu.gademo.service.impl;

import com.lu.gademo.entity.ga.SceneInfo;
import com.lu.gademo.mapper.ga.SceneinfoDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SceneInfoDaoServiceImpl {
    SceneinfoDao sceneInfoDao;

    @Autowired
    public SceneInfoDaoServiceImpl(SceneinfoDao sceneInfoDao) {
        this.sceneInfoDao = sceneInfoDao;
    }

    public List<SceneInfo> getAllSceneInfos() {
        return sceneInfoDao.getAllSceneInfos();
    }
}

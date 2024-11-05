package com.lu.gademo.service.impl;

import com.lu.gademo.entity.ga.SceneInfo;
import com.lu.gademo.mapper.ga.SceneinfoDao;
import com.lu.gademo.service.SceneInfoDaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SceneInfoDaoServiceImpl implements SceneInfoDaoService {
    SceneinfoDao sceneInfoDao;

    @Autowired
    public SceneInfoDaoServiceImpl(SceneinfoDao sceneInfoDao) {
        this.sceneInfoDao = sceneInfoDao;
    }

    @Override
    public List<SceneInfo> getAllSceneInfos() {
        return sceneInfoDao.getAllSceneInfos();
    }
}

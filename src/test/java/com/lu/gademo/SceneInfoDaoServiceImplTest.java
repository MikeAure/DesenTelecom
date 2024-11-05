package com.lu.gademo;

import com.lu.gademo.entity.ga.SceneInfo;
import com.lu.gademo.service.SceneInfoDaoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class SceneInfoDaoServiceImplTest {
    @Autowired
    SceneInfoDaoService sceneInfoDaoService;

    @Test
    void testGetAllInfo() {
        List<SceneInfo> allSceneInfos = sceneInfoDaoService.getAllSceneInfos();
        allSceneInfos.forEach(System.out::println);
    }
}

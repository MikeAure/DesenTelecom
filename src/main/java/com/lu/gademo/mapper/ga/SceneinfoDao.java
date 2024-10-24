package com.lu.gademo.mapper.ga;

import com.lu.gademo.entity.ga.SceneInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.type.JdbcType;

import java.util.List;

@Mapper
public interface SceneinfoDao {
    @Select({"SELECT * FROM sceneinfo"})
    @Results(
            id = "SceneinfoMapping",
            value = {
                    @Result(column = "sceneId", property = "sceneId", jdbcType = JdbcType.BIGINT),
                    @Result(column = "sceneName", property = "sceneName", jdbcType = JdbcType.VARCHAR),
                    @Result(column = "sceneChineseName", property = "sceneChineseName", jdbcType = JdbcType.VARCHAR),
                    @Result(column = "fieldNumber", property = "fieldNumber", jdbcType = JdbcType.INTEGER),
            })
    List<SceneInfo> getAllSceneInfos();
}

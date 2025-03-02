package com.lu.gademo.mapper.ga;

import com.lu.gademo.entity.Poi;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface GraphPoiDao {
    @Select("SELECT * FROM poi WHERE id = #{id}")
    Poi selectPoiById(String id);

    @Select("SELECT * FROM poi")
    List<Poi> selectAllPoi();

    @Select({
            "<script>",
            "SELECT * FROM poi WHERE id IN",
            "<foreach collection='ids' item='id' open='(' separator=',' close=')'>",
            "#{id}",
            "</foreach>",
            "</script>"
    })
    List<Poi>selectPoisByIds(List<String> ids);
}

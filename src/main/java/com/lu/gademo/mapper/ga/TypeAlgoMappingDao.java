package com.lu.gademo.mapper.ga;

import com.lu.gademo.entity.ga.TypeAlgoMapping;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

import java.util.List;

@Mapper
public interface TypeAlgoMappingDao {
    @Select({"SELECT * FROM type_algo_mapping WHERE typeId = #{typeId}"})
    @Results(
            id = "typeAlgoMapping",
            value = {
                    @Result(column = "id", property = "id", jdbcType = JdbcType.INTEGER),
                    @Result(column = "typeId", property = "typeId", jdbcType = JdbcType.INTEGER),
                    @Result(column = "algId", property = "algId", jdbcType = JdbcType.INTEGER),
                    @Result(column = "weight", property = "weight", jdbcType = JdbcType.INTEGER),
            })
    List<TypeAlgoMapping> getTypeAlgoMappingInfoByTypeId(int typeId);

    @Select({
            "SELECT a.chineseName",
            "FROM type_info t",
            "JOIN type_algo_mapping tam ON t.typeId = tam.typeId",
            "JOIN algo_info a ON tam.algoId = a.originalId",
            "WHERE t.typeName = #{typeName}"
    })
    List<String> getAlgNamesByTypeName(@Param("typeName") String typeName);

}

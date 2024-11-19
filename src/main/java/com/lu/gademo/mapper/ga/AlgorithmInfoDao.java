package com.lu.gademo.mapper.ga;

import com.lu.gademo.dto.AlgorithmDisplayInfoDto;
import com.lu.gademo.dto.AlgorithmInfoParamDto;
import com.lu.gademo.entity.ga.DesensitizationAlgorithm;
import com.lu.gademo.handler.AlgorithmTypeHandler;
import com.lu.gademo.handler.ExecutorTypeHandler;
import com.lu.gademo.handler.ModalTypesHandler;
import com.lu.gademo.handler.ParamsTypeHandler;
import com.lu.gademo.model.ModalTypes;
import com.lu.gademo.utils.AlgorithmInfo;
import com.lu.gademo.utils.AlgorithmType;
import com.lu.gademo.utils.BaseDesenAlgorithm;
import org.apache.ibatis.annotations.*;
import org.springframework.context.annotation.Lazy;

import java.util.List;

@Lazy
@Mapper
public interface AlgorithmInfoDao {
    @Select("SELECT id, algorithm_name AS name, type, original_id, " +
            "CONCAT_WS(';', low, medium, high) AS params, requirement " +
            "FROM desensitization_algorithms")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "name", column = "name"),
            @Result(property = "type", column = "type", javaType = AlgorithmType.class,
                    typeHandler = AlgorithmTypeHandler.class),
            @Result(property = "originalId", column = "original_id"),
            @Result(property = "params", column = "params",
                    javaType = List.class, typeHandler = ParamsTypeHandler.class),
            @Result(property = "requirement", column = "requirement"),
            @Result(property = "executor", column = "type",
                    javaType = BaseDesenAlgorithm.class, typeHandler = ExecutorTypeHandler.class)
    })
    List<AlgorithmInfo> getAllAlgorithmInfoConvertObject();

    @Select("SELECT * FROM desensitization_algorithms")
    @Results(id = "DesensitizationAlgorithmMap", value =
            {
                    @Result(property = "id", column = "id"),
                    @Result(property = "algorithmName", column = "algorithm_name"),
                    @Result(property = "algorithmAbbreviation", column = "algorithm_abbreviation"),
                    @Result(property = "applicableDataModes", column = "applicable_dataModes"),
                    @Result(property = "reversible", column = "reversible"),
                    @Result(property = "optionalParameters", column = "optional_parameters"),
                    @Result(property = "low", column = "low"),
                    @Result(property = "medium", column = "medium"),
                    @Result(property = "high", column = "high"),
                    @Result(property = "type", column = "type", javaType=AlgorithmType.class, typeHandler = AlgorithmTypeHandler.class),
                    @Result(property = "originalId", column = "original_id"),
                    @Result(property = "modal", column = "modal", javaType= ModalTypes.class, typeHandler = ModalTypesHandler.class),
                    @Result(property = "requirement", column = "requirement"),
                    @Result(property = "ifModify", column = "if_modify"),
            })
    List<DesensitizationAlgorithm> getAllAlgorithmInfo();

    @Select("SELECT id, algorithm_name, algorithm_abbreviation, low, medium, high, type, requirement FROM desensitization_algorithms where if_modify=1")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "algorithmName", column = "algorithm_name"),
            @Result(property = "algorithmAbbreviation", column = "algorithm_abbreviation"),
            @Result(property = "low", column = "low"),
            @Result(property = "medium", column = "medium"),
            @Result(property = "high", column = "high"),
            @Result(property = "type", column = "type", javaType=AlgorithmType.class, typeHandler = AlgorithmTypeHandler.class),
            @Result(property = "requirement", column = "requirement"),
    })
    List<AlgorithmDisplayInfoDto> getAllAlgorithmInfoDisplay();

    @Select("SELECT * FROM desensitization_algorithms WHERE id = #{id}")
    @ResultMap("DesensitizationAlgorithmMap")
    List<DesensitizationAlgorithm> getAlgorithmInfoById(int id);

    @Select("SELECT * FROM desensitization_algorithms WHERE type = #{type} AND original_id = #{originalId}")
    @ResultMap("DesensitizationAlgorithmMap")
    List<DesensitizationAlgorithm> getAlgorithmInfoByTypeAndOriginalId(int type, int originalId);

    @Update("UPDATE desensitization_algorithms SET low = #{low}, medium = #{medium}, high = #{high} WHERE id = #{id}")
    int updateAlgorithmParams(int id, String low, String medium, String high);

    @Update({
            "<script>",
            "UPDATE desensitization_algorithms SET",
            "<foreach collection='list' item='item' separator=','>",
            "low = CASE WHEN id = #{item.id} THEN #{item.low} ELSE low END,",
            "medium = CASE WHEN id = #{item.id} THEN #{item.medium} ELSE medium END,",
            "high = CASE WHEN id = #{item.id} THEN #{item.high} ELSE high END",
            "</foreach>",
            "WHERE id IN",
            "<foreach collection='list' item='item' separator=',' open='(' close=')'>",
            "#{item.id}",
            "</foreach>",
            "</script>"
    })
    int updateAlgorithmParamsUpdateBatch(List<AlgorithmInfoParamDto> algorithmInfos);
}

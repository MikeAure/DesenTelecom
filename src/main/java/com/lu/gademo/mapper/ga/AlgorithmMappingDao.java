package com.lu.gademo.mapper.ga;

import com.lu.gademo.entity.ga.AlgorithmMapping;
import com.lu.gademo.entity.ga.DesensitizationAlgorithm;
import com.lu.gademo.handler.AlgorithmTypeHandler;
import com.lu.gademo.handler.ModalTypesHandler;
import com.lu.gademo.model.ModalTypes;
import com.lu.gademo.model.AlgorithmType;
import org.apache.ibatis.annotations.*;

@Mapper

public interface AlgorithmMappingDao {
    @Select("SELECT * FROM algorithm_mapping WHERE attributeName = #{attributeName}")
    AlgorithmMapping selectAlgorithmIdByAttributeName(@Param("attributeName")String attributeName);

    /**
     * 通过数据属性获取对应的算法信息
     * @param attributeName
     * @return
     */
    @Select("SELECT da.* FROM algorithm_mapping am INNER JOIN desensitization_algorithms da ON am.algorithmId = da.id WHERE am.attributeName = #{attributeName}")
    @Results(id = "DesensitizationAlgorithmMap", value =
            {
                    @Result(property = "id", column = "id"),
                    @Result(property = "algorithmName", column = "algorithm_name"),
                    @Result(property = "algorithmAbbreviation", column = "algorithm_abbreviation"),
                    @Result(property = "optionalParameters", column = "optional_parameters"),
                    @Result(property = "low", column = "low"),
                    @Result(property = "medium", column = "medium"),
                    @Result(property = "high", column = "high"),
                    @Result(property = "type", column = "type", javaType= AlgorithmType.class, typeHandler = AlgorithmTypeHandler.class),
                    @Result(property = "originalId", column = "original_id"),
                    @Result(property = "modal", column = "modal", javaType= ModalTypes.class, typeHandler = ModalTypesHandler.class),
                    @Result(property = "requirement", column = "requirement"),
                    @Result(property = "ifModify", column = "if_modify"),
                    @Result(property = "ifInteger", column = "if_integer"),
                    @Result(property = "ifMinus", column = "if_minus"),
                    @Result(property = "paramsLength", column = "params_length"),
                    @Result(property = "min", column = "min"),
                    @Result(property = "max", column = "max")
            })
    DesensitizationAlgorithm selectAlgorithmInfoByAttributeName(@Param("attributeName")String attributeName);

}

package com.lu.gademo.mapper.ga;

import com.lu.gademo.entity.ExcelParam;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

import java.util.List;
import java.util.Map;

/**
 * 使用MyBatis访问模板数据表
 */
@Mapper
public interface  ExcelParamDao {
    /**
     * 通过表名获取表中所有ExcelParam实体
     * @param name 表名
     * @return List<ExcelParam> 表中所有ExcelParam实体
     */
    @Select({"SELECT * FROM ${name} "})
    @Results(
            id = "ExcelParamMap",
            value = {
            @Result(column = "id", property = "id", jdbcType = JdbcType.VARCHAR),
            @Result(column = "field_name", property = "fieldName", jdbcType = JdbcType.VARCHAR),
            @Result(column = "column_name", property = "columnName", jdbcType = JdbcType.VARCHAR),
            @Result(column = "data_type", property = "dataType", jdbcType = JdbcType.VARCHAR),
            @Result(column = "k", property = "k", jdbcType = JdbcType.VARCHAR),
            @Result(column = "tm_param", property = "tmParam", jdbcType = JdbcType.VARCHAR),
    })
    List<ExcelParam> getTableParamsByName(String name);

    /**
     * 删除表中所有数据
     * @param name 表名
     */
    @Delete("DELETE FROM ${name}")
    void deleteAll(String name);

    /**
     *
     * @param tableName
     * @param dataList
     */
    @Insert({
            "<script>",
            "INSERT INTO ${tableName} (id, field_name, column_name, data_type, k, tm_param)",
            "VALUES",
            "<foreach collection='list' item='item' separator=','>",
            "(#{item.id}, #{item.fieldName}, #{item.columnName}, #{item.dataType}, #{item.k}, #{item.tmParam})",
            "</foreach>",
            "</script>"
    })
    void insertAll(@Param("tableName") String tableName, @Param("list") List<ExcelParam> dataList);

    /**
     * 通过数据类型和表名获取ExcelParam实体列表
     * @param name 表名
     * @param dataType 数据类型
     * @return
     */
    @ResultMap("ExcelParamMap")
    @Select({"SELECT * FROM ${name} WHERE data_type = #{dataType}"})
    List<ExcelParam> getByTableNameAndDataType(String name, @Param("dataType") Integer dataType);

    /**
     * 通过列名获取ExcelParam实体列表
     * @param name
     * @param colName
     * @return
     */
    @ResultMap("ExcelParamMap")
    @Select({"SELECT * FROM ${name} WHERE column_name = #{colName}"})

    List<ExcelParam> getByColName(String name, @Param("colName") String colName);

    @ResultMap("ExcelParamMap")
    @Select({"SELECT * FROM ${name}"})
    List<ExcelParam> findTable(String name);


    @Delete({"DELETE FROM ${name} WHERE id = #{id}"})
    void deleteByTableNameAndId(@Param("name") String name, @Param("id") int id);

    @Results(
            id = "FieldNameAndColumnNameMapping",
            value = {
                    @Result(column = "field_name", property = "fieldName", jdbcType = JdbcType.VARCHAR),
                    @Result(column = "column_name", property = "columnName", jdbcType = JdbcType.VARCHAR),
            })
    @MapKey("column_name")
    @Select({"SELECT field_name, column_name FROM ${name}"})
    Map<String, String> getFieldNameAndColumnNameMapping(String name);
}

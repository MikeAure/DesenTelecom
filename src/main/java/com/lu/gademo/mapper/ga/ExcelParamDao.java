package com.lu.gademo.mapper.ga;

import com.lu.gademo.entity.ExcelParam;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

import java.util.List;

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
    @Results({
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
    void saveTableParams(@Param("tableName") String tableName, @Param("list") List<ExcelParam> dataList);

    /**
     * 通过数据类型和表名获取ExcelParam实体列表
     * @param name 表名
     * @param DataType 数据类型
     * @return
     */
    @Select({"SELECT * FROM ${name} WHERE data_type = #{dataType}"})
    @Results({
            @Result(column = "id", property = "id", jdbcType = JdbcType.INTEGER),
            @Result(column = "field_name", property = "fieldName", jdbcType = JdbcType.VARCHAR),
            @Result(column = "column_name", property = "columnName", jdbcType = JdbcType.VARCHAR),
            @Result(column = "data_type", property = "dataType", jdbcType = JdbcType.INTEGER),
            @Result(column = "tm_param", property = "tmParam", jdbcType = JdbcType.INTEGER)
    })
    List<ExcelParam> getByDataType(String name, @Param("dataType") Integer DataType);

    /**
     * 通过列名获取ExcelParam实体列表
     * @param name
     * @param colName
     * @return
     */
    @Select({"SELECT * FROM ${name} WHERE column_name = #{colName}"})
    @Results({
            @Result(column = "id", property = "id", jdbcType = JdbcType.INTEGER),
            @Result(column = "field_name", property = "fieldName", jdbcType = JdbcType.VARCHAR),
            @Result(column = "column_name", property = "columnName", jdbcType = JdbcType.VARCHAR),
            @Result(column = "data_type", property = "dataType", jdbcType = JdbcType.INTEGER),
            @Result(column = "tm_param", property = "tmParam", jdbcType = JdbcType.INTEGER)
    })
    List<ExcelParam> getByColName(String name, @Param("colName") String colName);


    @Select({"SELECT * FROM ${name}"})
    @Results({
            @Result(column = "id", property = "id", jdbcType = JdbcType.INTEGER),
            @Result(column = "field_name", property = "fieldName", jdbcType = JdbcType.VARCHAR),
            @Result(column = "column_name", property = "columnName", jdbcType = JdbcType.VARCHAR),
            @Result(column = "data_type", property = "dataType", jdbcType = JdbcType.INTEGER),
            @Result(column = "tm_param", property = "tmParam", jdbcType = JdbcType.INTEGER),
            @Result(column = "k", property = "k", jdbcType = JdbcType.INTEGER)
    })
    List<ExcelParam> findTable(String name);


    @Delete({"DELETE FROM ${name} WHERE id = #{id}"})
    void deleteById(@Param("name") String name, @Param("id") int id);

}

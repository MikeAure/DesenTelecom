package com.lu.gademo.mapper;

import com.lu.gademo.entity.ExcelParam;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

import java.util.List;

@Mapper
public interface  ExcelParamDao {
    // 查询所有数据
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

    //删除所有数据
    @Delete("DELETE FROM ${name}")
    void deleteAll(String name);

    // 插入数据
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

    @Select({"SELECT * FROM ${name} WHERE data_type = #{dataType}"})
    @Results({
            @Result(column = "id", property = "id", jdbcType = JdbcType.INTEGER),
            @Result(column = "field_name", property = "fieldName", jdbcType = JdbcType.VARCHAR),
            @Result(column = "column_name", property = "columnName", jdbcType = JdbcType.VARCHAR),
            @Result(column = "data_type", property = "dataType", jdbcType = JdbcType.INTEGER),
            @Result(column = "tm_param", property = "tmParam", jdbcType = JdbcType.INTEGER)
    })
    public List<ExcelParam> getByDataType(String name, @Param("dataType") Integer DataType);

    @Select({"SELECT * FROM ${name} WHERE column_name = #{colName}"})
    @Results({
            @Result(column = "id", property = "id", jdbcType = JdbcType.INTEGER),
            @Result(column = "field_name", property = "fieldName", jdbcType = JdbcType.VARCHAR),
            @Result(column = "column_name", property = "columnName", jdbcType = JdbcType.VARCHAR),
            @Result(column = "data_type", property = "dataType", jdbcType = JdbcType.INTEGER),
            @Result(column = "tm_param", property = "tmParam", jdbcType = JdbcType.INTEGER)
    })
    public List<ExcelParam> getByColName(String name, @Param("colName") String colName);


    @Select({"SELECT * FROM ${name}"})
    @Results({
            @Result(column = "id", property = "id", jdbcType = JdbcType.INTEGER),
            @Result(column = "field_name", property = "fieldName", jdbcType = JdbcType.VARCHAR),
            @Result(column = "column_name", property = "columnName", jdbcType = JdbcType.VARCHAR),
            @Result(column = "data_type", property = "dataType", jdbcType = JdbcType.INTEGER),
            @Result(column = "tm_param", property = "tmParam", jdbcType = JdbcType.INTEGER),
            @Result(column = "k", property = "k", jdbcType = JdbcType.INTEGER)
    })
    public List<ExcelParam> findTable(String name);


    @Delete({"DELETE FROM ${name} WHERE id = #{id}"})
    void deleteById(@Param("name") String name, @Param("id") int id);

}

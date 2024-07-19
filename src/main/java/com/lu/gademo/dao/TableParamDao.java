//package com.lu.gademo.dao;
//
//import com.lu.evaluate.model.TableParam;
//import org.apache.ibatis.annotations.*;
//import org.apache.ibatis.type.JdbcType;
//import org.mybatis.spring.annotation.MapperScan;
//import org.springframework.data.jpa.repository.JpaRepository;
//
//import java.util.List;
//
//@Mapper
//public interface TableParamDao{
//    @Select({"SELECT * FROM ${name} WHERE data_type = #{dataType}"})
//    @Results({
//            @Result(column = "id", property = "id", jdbcType = JdbcType.INTEGER),
//            @Result(column = "field_name", property = "fieldName", jdbcType = JdbcType.VARCHAR),
//            @Result(column = "column_name", property = "columnName", jdbcType = JdbcType.VARCHAR),
//            @Result(column = "data_type", property = "dataType", jdbcType = JdbcType.INTEGER),
//            @Result(column = "tm_param", property = "tmParam", jdbcType = JdbcType.INTEGER)
//    })
//    public List<TableParam> getByDataType(String name, @Param("dataType") Integer DataType);
//
//    @Select({"SELECT * FROM ${name} WHERE column_name = #{colName}"})
//    @Results({
//            @Result(column = "id", property = "id", jdbcType = JdbcType.INTEGER),
//            @Result(column = "field_name", property = "fieldName", jdbcType = JdbcType.VARCHAR),
//            @Result(column = "column_name", property = "columnName", jdbcType = JdbcType.VARCHAR),
//            @Result(column = "data_type", property = "dataType", jdbcType = JdbcType.INTEGER),
//            @Result(column = "tm_param", property = "tmParam", jdbcType = JdbcType.INTEGER)
//    })
//    public List<TableParam> getByColName(String name, @Param("colName") String colName);
//
//
//    @Select({"SELECT * FROM ${name}"})
//    @Results({
//            @Result(column = "id", property = "id", jdbcType = JdbcType.INTEGER),
//            @Result(column = "field_name", property = "fieldName", jdbcType = JdbcType.VARCHAR),
//            @Result(column = "column_name", property = "columnName", jdbcType = JdbcType.VARCHAR),
//            @Result(column = "data_type", property = "dataType", jdbcType = JdbcType.INTEGER),
//            @Result(column = "tm_param", property = "tmParam", jdbcType = JdbcType.INTEGER),
//            @Result(column = "k", property = "k", jdbcType = JdbcType.INTEGER)
//    })
//    public List<TableParam> findTable(String name);
//
//
//    @Delete({"DELETE FROM ${name} WHERE id = #{id}"})
//    void deleteById(@Param("name") String name, @Param("id") int id);
//
//
//    // 清空现有数据
//    @Delete({"DELETE FROM ${name}"})
//    void deleteAll(@Param("name") String tableName);
//
//    // 保存新的数据列表
//    @Insert({
//            "<script>",
//            "INSERT INTO ${name} (id, field_name, column_name, data_type, tm_param, k) VALUES ",
//            "<foreach collection='tableParams' item='param' separator=','>",
//            "(#{param.id}, #{param.fieldName}, #{param.columnName}, #{param.dataType}, #{param.tmParam}, #{param.k})",
//            "</foreach>",
//            "</script>"
//    })
//    void saveTableParams(@Param("name") String tableName, @Param("tableParams") List<TableParam> tableParams);
//}
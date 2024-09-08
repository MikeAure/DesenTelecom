package com.lu.gademo.mapper.dataplatform;

import com.lu.gademo.entity.crm.CustomerDesenMsg;
import com.lu.gademo.entity.dataplatform.SadaGdpiClickDtl;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

import java.util.List;

@Mapper
public interface SadaGdpiClickDtlParamDao {
    /**
     * 通过表名获取表中所有SadaGdpiClickDtlParam实体
     * @param tableName 表名
     * @return List<CustomerDesenMsg> 表中所有ExcelParam实体
     */
    @Select({"SELECT * FROM ${name} "})
    @Results({
            @Result(column = "sid", property = "sid", jdbcType = JdbcType.BIGINT),
            @Result(column = "f_srcip", property = "fSrcip", jdbcType = JdbcType.CLOB),
            @Result(column = "f_ad", property = "fAd", jdbcType = JdbcType.CLOB),
            @Result(column = "f_ts", property = "fTs", jdbcType = JdbcType.BIGINT),
            @Result(column = "f_url", property = "fUrl", jdbcType = JdbcType.CLOB),
            @Result(column = "f_ref", property = "fRef", jdbcType = JdbcType.CLOB),
            @Result(column = "f_ua", property = "fUa", jdbcType = JdbcType.CLOB),
            @Result(column = "f_dstip", property = "fDstip", jdbcType = JdbcType.CLOB),
            @Result(column = "f_cookie", property = "fCookie", jdbcType = JdbcType.CLOB),
            @Result(column = "f_src_port", property = "fSrcPort", jdbcType = JdbcType.CLOB),
            @Result(column = "f_json", property = "fJson", jdbcType = JdbcType.CLOB),
            @Result(column = "f_update_time", property = "fUpdateTime", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "f_dataid", property = "fDataid", jdbcType = JdbcType.BIGINT),

    })
    List<SadaGdpiClickDtl> getAllRecordsByTableName(String tableName);

    /**
     * 删除表中所有数据
     * @param name 表名
     */
    @Delete("DELETE FROM ${name}")
    int deleteAll(String name);

    @Delete("DELETE FROM ${tableName} WHERE sid = #{id}")
    int deleteById(String tableName, Long id);

    /**
     *
     * @param tableName
     * @param dataList
     */
    @Insert({
            "<script>",
            "INSERT INTO ${tableName} (sid, f_srcip, f_ad, f_ts, f_url, f_ref, f_ua, f_dstip, f_cookie, f_src_port, " +
                    "f_json, f_update_time, f_dataid)",
            "VALUES",
            "<foreach collection='list' item='item' separator=','>",
            "(#{item.sid}, #{item.fSrcip}, #{item.fAd}, #{item.fTs}, #{item.fUrl}, #{item.fRef}, #{item.fUa}, " +
                    "#{item.fDstip}, #{item.fCookie}, #{item.fSrcPort}, #{item.fJson}, #{item.fUpdateTime}, #{item.fDataid})",
            "</foreach>",
            "</script>"
    })
    int insertList(@Param("tableName") String tableName, @Param("list") List<SadaGdpiClickDtl> dataList);

//    /**
//     * 通过数据类型和表名获取ExcelParam实体列表
//     * @param name 表名
//     * @param DataType 数据类型
//     * @return
//     */
//    @Select({"SELECT * FROM ${name} WHERE data_type = #{dataType}"})
//    @Results({
//            @Result(column = "id", property = "id", jdbcType = JdbcType.INTEGER),
//            @Result(column = "field_name", property = "fieldName", jdbcType = JdbcType.VARCHAR),
//            @Result(column = "column_name", property = "columnName", jdbcType = JdbcType.VARCHAR),
//            @Result(column = "data_type", property = "dataType", jdbcType = JdbcType.INTEGER),
//            @Result(column = "tm_param", property = "tmParam", jdbcType = JdbcType.INTEGER)
//    })
//    List<ExcelParam> getByDataType(String name, @Param("dataType") Integer DataType);


    @Select({"SELECT COUNT(*) FROM ${tableName}"})
//    @ResultType(java.lang.Integer.class)
    Integer getItemTotalNumberByTabelName(String tableName);

//    /**
//     * 通过列名获取ExcelParam实体列表
//     * @param name
//     * @param colName
//     * @return
//     */
//    @Select({"SELECT * FROM ${name} WHERE column_name = #{colName}"})
//    @Results({
//            @Result(column = "id", property = "id", jdbcType = JdbcType.INTEGER),
//            @Result(column = "field_name", property = "fieldName", jdbcType = JdbcType.VARCHAR),
//            @Result(column = "column_name", property = "columnName", jdbcType = JdbcType.VARCHAR),
//            @Result(column = "data_type", property = "dataType", jdbcType = JdbcType.INTEGER),
//            @Result(column = "tm_param", property = "tmParam", jdbcType = JdbcType.INTEGER)
//    })
//    List<ExcelParam> getByColName(String name, @Param("colName") String colName);
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
//    List<ExcelParam> findTable(String name);
//
//
//    @Delete({"DELETE FROM ${name} WHERE id = #{id}"})
//    void deleteById(@Param("name") String name, @Param("id") int id);
}

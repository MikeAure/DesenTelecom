package com.lu.gademo.mapper.ga;

import com.lu.gademo.entity.ga.MeetingDataTable;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

import java.util.List;

@Mapper
public interface MeetingDataTableDao {
    @Select({"SELECT * FROM ${name} "})
    @Results(
            id = "MeetingDataTableMapping",
            value = {
                    @Result(column = "sjhm", property = "sjhm", jdbcType = JdbcType.BIGINT),
                    @Result(column = "yhm", property = "yhm", jdbcType = JdbcType.VARCHAR),
                    @Result(column = "yhid", property = "yhid", jdbcType = JdbcType.VARCHAR),
                    @Result(column = "mm", property = "mm", jdbcType = JdbcType.VARCHAR),
                    @Result(column = "xm", property = "xm", jdbcType = JdbcType.VARCHAR),
                    @Result(column = "yx", property = "yx", jdbcType = JdbcType.VARCHAR),
                    @Result(column = "hyzcr", property = "hyzcr", jdbcType = JdbcType.VARCHAR),
                    @Result(column = "sbbsf", property = "sbbsf", jdbcType = JdbcType.VARCHAR),
                    @Result(column = "hyzt", property = "hyzt", jdbcType = JdbcType.DATE),
                    @Result(column = "hyh", property = "hyh", jdbcType = JdbcType.VARCHAR),
                    @Result(column = "hysj", property = "hysj", jdbcType = JdbcType.VARCHAR),
                    @Result(column = "chrnc", property = "chrnc", jdbcType = JdbcType.VARCHAR),
                    @Result(column = "verCode", property = "verCode", jdbcType = JdbcType.VARCHAR),
                    @Result(column = "wechatNumber", property = "wechatNumber", jdbcType = JdbcType.VARCHAR),
                    @Result(column = "ip", property = "ip", jdbcType = JdbcType.VARCHAR),
            })
    List<MeetingDataTable> getAllRecordsByTableName(String name);

    @Delete("DELETE FROM ${name}")
    int deleteAll(String name);

    @Delete("DELETE FROM ${tableName} WHERE sjhm = #{phoneNumber}")
    int deleteByTableNameAndPhoneNumber(String tableName, Long phoneNumber);

    @Insert({
            "<script>",
            "INSERT INTO ${tableName} (sjhm, yhm, yhid, mm, xm, yx, hyzcr, sbbsf, hyzt, hyh, hysj, " +
                    "chrnc, verCode, wechatNumber, ip)",
            "VALUES",
            "<foreach collection='list' item='item' separator=','>",
            "(#{item.sjhm}, #{item.yhm}, #{item.yhid}, #{item.mm}, #{item.xm}, #{item.yx}, #{item.hyzcr}, #{item.sbbsf}," +
                    "#{item.hyzt}, #{item.hyh}, #{item.hysj}, #{item.chrnc}, #{item.verCode}, #{item.wechatNumber}" +
                    ", #{item.ip})",
            "</foreach>",
            "</script>"
    })
    int insertList(@Param("tableName") String tableName, @Param("list") List<MeetingDataTable> dataList);

    @Select({"SELECT COUNT(*) FROM ${tableName}"})
    Integer getItemTotalNumberByTableName(String tableName);

}

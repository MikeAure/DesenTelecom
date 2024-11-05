package com.lu.gademo.mapper.jmtLogStock;

import com.lu.gademo.entity.ga.MeetingDataTable;
import com.lu.gademo.entity.jmtLogStock.TUserInfo;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

import java.util.List;

@Mapper
public interface TUserInfoDao {
    @Select({"SELECT * FROM ${name} "})
    @Results(
            id = "TUserInfoMapping",
            value = {
                    @Result(column = "id", property = "id", jdbcType = JdbcType.INTEGER),
                    @Result(column = "user_id", property = "userId", jdbcType = JdbcType.VARCHAR),
                    @Result(column = "auth_flag", property = "authFlag", jdbcType = JdbcType.INTEGER),
                    @Result(column = "wx_open_id", property = "wxOpenId", jdbcType = JdbcType.VARCHAR),
                    @Result(column = "name", property = "name", jdbcType = JdbcType.VARCHAR),
                    @Result(column = "sex", property = "sex", jdbcType = JdbcType.INTEGER),
                    @Result(column = "birthday", property = "birthday", jdbcType = JdbcType.DATE),
                    @Result(column = "in_Beijing", property = "inBeijing", jdbcType = JdbcType.INTEGER),
                    @Result(column = "status_type", property = "statusType", jdbcType = JdbcType.INTEGER),
                    @Result(column = "create_user", property = "createUser", jdbcType = JdbcType.VARCHAR),
                    @Result(column = "create_time", property = "createTime", jdbcType = JdbcType.TIMESTAMP),
                    @Result(column = "create_app", property = "createApp", jdbcType = JdbcType.VARCHAR),
                    @Result(column = "modify_user", property = "modifyUser", jdbcType = JdbcType.VARCHAR),
                    @Result(column = "modify_time", property = "modifyTime", jdbcType = JdbcType.TIMESTAMP),
                    @Result(column = "modify_app", property = "modifyApp", jdbcType = JdbcType.VARCHAR),
                    @Result(column = "del_stat", property = "delStat", jdbcType = JdbcType.TINYINT),
                    @Result(column = "version_no", property = "versionNo", jdbcType = JdbcType.INTEGER),
                    @Result(column = "last_login", property = "lastLogin", jdbcType = JdbcType.TIMESTAMP),

            })
    List<TUserInfo> getAllRecordsByTableName(String name);

    @Delete("DELETE FROM ${name}")
    int deleteAll(String name);

    @Delete("DELETE FROM ${tableName} WHERE user_id = #{userId}")
    int deleteByTableNameAndUserId(String tableName, String userId);

    @Insert({
            "<script>",
            "INSERT INTO ${tableName} (id, user_id, auth_flag, wx_open_id, name, sex, birthday, in_Beijing, status_type, create_user, create_time, " +
                    "create_app, modify_user, modify_time, modify_app, del_stat, version_no, last_login) ",
            "VALUES",
            "<foreach collection='list' item='item' separator=','>",
            "(#{item.id}, #{item.userId}, #{item.authFlag}, #{item.wxOpenId}, #{item.name}, #{item.sex}, #{item.birthday}, #{item.inBeijing}," +
                    "#{item.statusType}, #{item.createUser}, #{item.createTime}, #{item.createApp}, #{item.modifyUser}, #{item.modifyTime}" +
                    ", #{item.modifyApp}, #{item.delStat}, #{item.versionNo}, #{item.lastLogin})",
            "</foreach>",
            "</script>"
    })
    int insertList(@Param("tableName") String tableName, @Param("list") List<TUserInfo> dataList);

    @Select({"SELECT COUNT(*) FROM ${tableName}"})
    Integer getItemTotalNumberByTableName(String tableName);
}

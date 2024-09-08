package com.lu.gademo.mapper.crm;

import com.lu.gademo.entity.crm.CustomerDesenMsg;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

import java.util.List;

@Mapper
public interface CrmParamDao {
    /**
     * 通过表名获取表中所有ExcelParam实体
     * @param tableName 表名
     * @return List<CustomerDesenMsg> 表中所有ExcelParam实体
     */
    @Select({"SELECT * FROM ${name} "})
    @Results({
            @Result(column = "CUST_ID", property = "id", jdbcType = JdbcType.BIGINT),
            @Result(column = "CUST_ADDR", property = "custAddr", jdbcType = JdbcType.VARCHAR),
            @Result(column = "CUST_AREA_GRADE", property = "custAreaGrade", jdbcType = JdbcType.VARCHAR),
            @Result(column = "CUST_CONTROL_LEVEL", property = "custControlLevel", jdbcType = JdbcType.VARCHAR),
            @Result(column = "CUST_NAME", property = "custName", jdbcType = JdbcType.VARCHAR),
            @Result(column = "CUST_TYPE", property = "custType", jdbcType = JdbcType.VARCHAR),
            @Result(column = "CUST_GROUP", property = "custGroup", jdbcType = JdbcType.VARCHAR),
            @Result(column = "MOBILE_PHONE", property = "mobilePhone", jdbcType = JdbcType.VARCHAR),
            @Result(column = "CERT_TYPE", property = "certType", jdbcType = JdbcType.VARCHAR),
            @Result(column = "CERT_NUM", property = "certNum", jdbcType = JdbcType.VARCHAR),
            @Result(column = "FAX", property = "fax", jdbcType = JdbcType.VARCHAR),
            @Result(column = "E_MAIL", property = "eMail", jdbcType = JdbcType.VARCHAR),
            @Result(column = "POST_CODE", property = "postCode", jdbcType = JdbcType.VARCHAR),
            @Result(column = "CREATE_STAFF", property = "createStaff", jdbcType = JdbcType.VARCHAR),
            @Result(column = "CREATE_DATE", property = "createDate", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "UPDATE_STAFF", property = "updateStaff", jdbcType = JdbcType.VARCHAR),
            @Result(column = "UPDATE_DATE", property = "updateDate", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "STATUS_DATE", property = "statusDate", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "STATUS_CD", property = "statusCd", jdbcType = JdbcType.VARCHAR),
            @Result(column = "REMARK", property = "remark", jdbcType = JdbcType.VARCHAR),
    })
    List<CustomerDesenMsg> getAllRecordsByTableName(String tableName);

    /**
     * 删除表中所有数据
     * @param name 表名
     */
    @Delete("DELETE FROM ${name}")
    int deleteAll(String name);

    @Delete("DELETE FROM ${tableName} WHERE CUST_ID = #{id}")
    int deleteById(String tableName, Long id);

    /**
     *
     * @param tableName
     * @param dataList
     */
    @Insert({
            "<script>",
            "INSERT INTO ${tableName} (CUST_ID, CUST_ADDR, CUST_AREA_GRADE, CUST_CONTROL_LEVEL, CUST_NAME, CUST_TYPE, CUST_GROUP, " +
                    "MOBILE_PHONE, CERT_TYPE, CERT_NUM, FAX, E_MAIL, POST_CODE, CREATE_STAFF, CREATE_DATE, UPDATE_STAFF, " +
                    "UPDATE_DATE, STATUS_DATE, STATUS_CD, REMARK)",
            "VALUES",
            "<foreach collection='list' item='item' separator=','>",
            "(#{item.id}, #{item.custAddr}, #{item.custAreaGrade}, #{item.custControlLevel}, #{item.custName}, #{item.custType}," +
            "#{item.custGroup}, #{item.mobilePhone}, #{item.certType}, #{item.certNum}, #{item.fax}, #{item.eMail}," +
            "#{item.postCode}, #{item.createStaff}, #{item.createDate}, #{item.updateStaff}, #{item.updateDate}, #{item.statusDate}," +
            "#{item.statusCd}, #{item.remark})",
            "</foreach>",
            "</script>"
    })
    int insertList(@Param("tableName") String tableName, @Param("list") List<CustomerDesenMsg> dataList);

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

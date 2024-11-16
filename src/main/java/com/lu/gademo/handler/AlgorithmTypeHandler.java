package com.lu.gademo.handler;

import com.lu.gademo.utils.AlgorithmType;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 将数据库表中的整数自动转换为对应的AlgorithmType Enum
 */
public class AlgorithmTypeHandler extends BaseTypeHandler<AlgorithmType> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, AlgorithmType parameter, JdbcType jdbcType) throws SQLException {

    }

    @Override
    public AlgorithmType getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return AlgorithmType.fromValue(rs.getInt(columnName));
    }

    @Override
    public AlgorithmType getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return AlgorithmType.fromValue(rs.getInt(columnIndex));
    }

    @Override
    public AlgorithmType getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return AlgorithmType.fromValue(cs.getInt(columnIndex));
    }
}

package com.lu.gademo.handler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

public class ParamsTypeHandler extends BaseTypeHandler<List<Object>>{
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, List<Object> parameter, JdbcType jdbcType) throws SQLException {

    }

    @Override
    public List<Object> getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String result = rs.getString(columnName);
        return StringUtils.isBlank(result) ? Collections.emptyList() : Arrays.asList(result.split(";"));
    }

    @Override
    public List<Object> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String result = rs.getString(columnIndex);
        return StringUtils.isBlank(result) ? Collections.emptyList() : Arrays.asList(result.split(";"));
    }

    @Override
    public List<Object> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String result = cs.getString(columnIndex);
        return StringUtils.isBlank(result) ? Collections.emptyList() : Arrays.asList(result.split(";"));
    }
}

package com.lu.gademo.handler;

import com.lu.gademo.model.ModalTypes;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ModalTypesHandler extends BaseTypeHandler<ModalTypes> {
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, ModalTypes parameter, JdbcType jdbcType) throws SQLException {

    }

    @Override
    public ModalTypes getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return ModalTypes.fromId(rs.getInt(columnName));
    }

    @Override
    public ModalTypes getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return ModalTypes.fromId(rs.getInt(columnIndex));
    }

    @Override
    public ModalTypes getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return ModalTypes.fromId(cs.getInt(columnIndex));
    }
}

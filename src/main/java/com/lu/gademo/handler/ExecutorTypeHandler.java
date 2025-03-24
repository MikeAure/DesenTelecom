package com.lu.gademo.handler;

import com.lu.gademo.model.AlgorithmType;
import com.lu.gademo.utils.algorithmBase.*;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
@MappedTypes(BaseDesenAlgorithm.class)
public class ExecutorTypeHandler extends BaseTypeHandler<BaseDesenAlgorithm> {

    private Dp dp;

    private Generalization generalization;

    private Anonymity anonymity;

    private Replace replace;

    @Autowired
    public ExecutorTypeHandler(Dp dp, Generalization generalization, Anonymity anonymity, Replace replace) {
        this.dp = dp;
        this.generalization = generalization;
        this.anonymity = anonymity;
        this.replace = replace;
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, BaseDesenAlgorithm parameter, JdbcType jdbcType) {
        // 无需设置数据库中的 `executor`，这里留空

    }

    @Override
    public BaseDesenAlgorithm getNullableResult(ResultSet rs, String columnName) throws SQLException {
        int typeValue = rs.getInt(columnName);
        return createExecutor(typeValue);
    }

    @Override
    public BaseDesenAlgorithm getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        int typeValue = rs.getInt(columnIndex);
        return createExecutor(typeValue);
    }

    @Override
    public BaseDesenAlgorithm getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        int typeValue = cs.getInt(columnIndex);
        return createExecutor(typeValue);
    }

    private BaseDesenAlgorithm createExecutor(int typeValue) {
        AlgorithmType type = AlgorithmType.fromValue(typeValue);
        switch (type) {
            case DP:
                return dp;
            case GENERALIZATION:
                return generalization;
            case ANONYMITY:
                return anonymity;
            case REPLACEMENT:
                return replace;
            default:
                return null;
        }
    }
}

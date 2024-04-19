package com.lu.gademo.utils;

import com.lu.gademo.utils.BaseDesenAlgorithm;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.List;


@Data
public class AlgorithmInfo {
    // 算法名称
    private String name;
    // 算法id
    private int id;
    // 算法种类
    private AlgorithmType type;
    // 在分类表中的Id
    private int originalId;
    // 参数
    private List<Object> params;
    //
    private BaseDesenAlgorithm executor;


    public AlgorithmInfo(String name, int id, AlgorithmType type, int originalId, List<Object> params, BaseDesenAlgorithm executor) {
        this.name = name;
        this.id = id;
        this.type = type;
        this.originalId = originalId;
        this.params = params;
        switch (type) {
            case DP: {
                assert (executor instanceof Dp);
                break;
            }
            case GENERALIZATION: {
                assert (executor instanceof Generalization);
                break;
            }
            case ANONYMITY: {
                assert (executor instanceof Anonymity);
                break;
            }
            case REPLACEMENT: {
                assert (executor instanceof Replace);
                break;
            }
            default:
        }
        this.executor = executor;
    }

    public DSObject execute(DSObject rawData, Number... params) {
        return executor.service(rawData, originalId, params);
    }


    // 获取分类表中的算法ID
    public String getOriginalIdInfo() {
        return String.valueOf(type.getValue()) + String.valueOf(originalId);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof AlgorithmInfo) {
            AlgorithmInfo algInfo = (AlgorithmInfo) obj;
            // params可能为空
            boolean four = name.equals(algInfo.name) && id == algInfo.id && type == algInfo.type && originalId == algInfo.originalId && params == algInfo.params;
            boolean executorEquals = algInfo.executor.getClass().equals(this.executor.getClass());
            return four && executorEquals;
        } else {
            return false;
        }
    }

}

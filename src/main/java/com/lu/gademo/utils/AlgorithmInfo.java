package com.lu.gademo.utils;

import com.lu.gademo.dto.AlgorithmInfoParamDto;
import com.lu.gademo.entity.ga.DesensitizationAlgorithm;
import lombok.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 算法信息类，用于存储算法名称、id、算法类型、统一接口中的id、算法参数列表、脱敏需求
 */
@Data
@NoArgsConstructor
public class AlgorithmInfo {
    // 算法名称
    private String name;
    // 算法ID
    private int id;
    // 算法种类
    private AlgorithmType type;
    // 在分类表中的Id，同时也是统一接口调用相关算法的ID
    private int originalId;
    // 参数
    private List<Object> params;
    // 四种类型的算法接口的基接口
    private BaseDesenAlgorithm executor;

    private String requirement;

    public AlgorithmInfo(String name, int id, AlgorithmType type, int originalId,
                         List<Object> params, BaseDesenAlgorithm executor, String requirement) {
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
        this.requirement = requirement;
    }

    /**
     * 对于统一接口中execute方法的代理
     * @param rawData 封装后的原始数据
     * @param params 算法对应的参数
     * @return 封装后的脱敏数据
     */
    public DSObject execute(DSObject rawData, Number... params) {
        return executor.service(rawData, originalId, params);
    }

    /**
     * 获取算法的原始ID信息
     * @return 算法的原始ID信息
     */
    public String getOriginalIdInfo() {
        return String.valueOf(type.getValue()) + originalId;
    }


    @Override
    public boolean equals(Object obj) {
        if (obj instanceof AlgorithmInfo) {
            AlgorithmInfo algInfo = (AlgorithmInfo) obj;
            // params可能为空
            boolean six = name.equals(algInfo.name) && id == algInfo.id && type == algInfo.type
                    && originalId == algInfo.originalId && params == algInfo.params && requirement.equals(algInfo.requirement);
            boolean executorEquals = algInfo.executor.getClass().equals(this.executor.getClass());
            return six && executorEquals;
        } else {
            return false;
        }
    }

}

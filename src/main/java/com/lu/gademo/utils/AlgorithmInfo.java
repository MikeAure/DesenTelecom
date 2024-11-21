package com.lu.gademo.utils;

import com.lu.gademo.dto.AlgorithmInfoParamDto;
import com.lu.gademo.entity.ga.DesensitizationAlgorithm;
import com.lu.gademo.model.ModalTypes;
import lombok.*;
import org.apache.commons.collections4.CollectionUtils;

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

    private ModalTypes modalType;

    public AlgorithmInfo(String name, int id, AlgorithmType type, int originalId,
                         List<Object> params, BaseDesenAlgorithm executor, String requirement, ModalTypes modalType) {
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
        this.modalType = modalType;
    }

    /**
     * 对于统一接口中execute方法的代理
     * @param rawData 封装后的原始数据
     * @param params 算法对应的参数
     * @return 封装后的脱敏数据
     */
    public DSObject execute(DSObject rawData, int params) {
        if (CollectionUtils.isEmpty(this.getParams())) {
            return executor.service(rawData, originalId);
        }
        if (this.modalType == ModalTypes.SHEET) {
            if (params == 0) {
                return executor.service(rawData, originalId, "0");
            } else {
                return executor.service(rawData, originalId, this.params.get(params - 1).toString());
            }
        } else {
            return executor.service(rawData, originalId, this.params.get(params).toString());
        }
    }

    public DSObject execute(DSObject rawData, String... params) {
        if (CollectionUtils.isEmpty(this.getParams())) {
            return executor.service(rawData, originalId, params);
        } else {
            int size = params.length;
            int paramIndex = Integer.parseInt(params[size - 1]); // 获取最后一个参数的索引

            // 替换最后一个参数
            String[] newParams = Arrays.copyOf(params, size); // 复制原参数数组
            if (this.modalType == ModalTypes.SHEET) {
                if (paramIndex == 0) {
                    newParams[size - 1] = "0"; // 替换最后一个参数为 "0"
                } else {
                    newParams[size - 1] = this.params.get(paramIndex - 1).toString(); // 替换为指定参数
                }
            } else {
                newParams[size - 1] = this.params.get(paramIndex).toString(); // 替换为指定参数
            }

            // 调用服务
            return executor.service(rawData, originalId, newParams);
        }
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
                    && originalId == algInfo.originalId && params == algInfo.params && requirement.equals(algInfo.requirement) && modalType == algInfo.modalType;
            boolean executorEquals = algInfo.executor.getClass().equals(this.executor.getClass());
            return six && executorEquals;
        } else {
            return false;
        }
    }

}

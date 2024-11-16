package com.lu.gademo.dto;

import com.lu.gademo.utils.AlgorithmInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlgorithmInfoParamDto {
    private int id;
    private String low;
    private String medium;
    private String high;

    /**
     * 构造一个新的AlgorithmInfoParamDto实例
     * @param id 算法ID
     * @param low 低等级对应的参数
     * @param medium 中等级对应的参数
     * @param high 高等级对应的参数
     * @return 一个新的AlgorithmInfoParamDto实例
     */
    public static AlgorithmInfoParamDto create (int id, String low, String medium, String high) {
        return new AlgorithmInfoParamDto(id, low, medium, high);
    }

    /**
     * 从AlgorithmInfo实例中构造一个新的AlgorithmInfoParamDto实例
     * @param algorithmInfo 包含算法信息的AlgorithmInfo实例
     * @return 一个新的AlgorithmInfoParamDto实例
     */
    public static AlgorithmInfoParamDto from (AlgorithmInfo algorithmInfo){
        return new AlgorithmInfoParamDto(algorithmInfo.getId(), algorithmInfo.getParams().get(0).toString(),
                algorithmInfo.getParams().get(1).toString(), algorithmInfo.getParams().get(2).toString());
    }

    /**
     * 从AlgorithmInfo实例列表中构造一个新的AlgorithmInfoParamDto实例列表
     * @param algorithmInfos 包含算法信息的AlgorithmInfo实例列表
     * @return 一个新的AlgorithmInfoParamDto实例列表
     */
    public static List<AlgorithmInfoParamDto> from(List<AlgorithmInfo> algorithmInfos){
        return algorithmInfos.stream().map(AlgorithmInfoParamDto::from).collect(Collectors.toList());
    }
}

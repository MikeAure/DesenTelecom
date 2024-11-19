package com.lu.gademo.service;

import com.lu.gademo.dto.AlgorithmDisplayInfoDto;
import com.lu.gademo.dto.AlgorithmInfoParamDto;
import com.lu.gademo.entity.ga.DesensitizationAlgorithm;
import com.lu.gademo.utils.AlgorithmInfo;
import com.lu.gademo.utils.AlgorithmType;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

public interface AlgorithmInfoDaoService {
    /**
     * 获取所有算法信息，使用DesensitizationAlgorithm存储，对应数据库中每一列的信息
     * @return 包含所有算法信息的列表
     */
    List<DesensitizationAlgorithm> getAllAlgorithmsRawInfo();

    /**
     * 获取所有算法信息，使用AlgorithmInfo存储，直接使用Mapper转换
     * @return 包含所有算法信息的列表，算法信息使用AlgorithmInfo存储
     */
    List<AlgorithmInfo> getAllAlgorithmInfoConvertObject();

    /**
     * 获取所有算法信息，使用AlgorithmInfo存储，在Service中手动转换
     * @return 包含所有算法信息的列表，算法信息使用AlgorithmInfo存储
     */
    List<AlgorithmInfo> getAllAlgorithmInfoConvertObjectFromRawInfo();

    /**
     * 获取所有算法信息，使用AlgorithmInfo存储，转换为Map
     * @return 包含所有算法名的Map，key为算法名称
     */
    Map<String, AlgorithmInfo> getAllAlgorithmInfoMap();

    /**
     * 获取所有算法信息，使用AlgorithmDisplayInfoDto存储，用于前端显示
     * @return 包含所有用于前端显示可被更改参数的算法信息的列表
     */
    List<AlgorithmDisplayInfoDto> getAllAlgorithmInfoDisplay();

    /**
     * 通过算法ID获取算法信息
     * @param id 算法ID
     * @return 算法在数据表中的所有信息
     */
    List<DesensitizationAlgorithm> getAlgorithmInfoById(int id);

    /**
     * 通过算法ID获取算法信息，封装为AlgorithmInfo对象
     * @param id
     * @return
     */
    List<AlgorithmInfo> getAlgorithmInfoConvertObjectById(int id);

    /**
     * 通过算法类型和算法统一接口中对应的ID获取对应的算法
     * @param type 四种算法类型之一
     * @param originalId 算法统一接口中对应的ID
     * @return 算法在数据表中的信息
     */
    List<DesensitizationAlgorithm> getAlgorithmInfoByTypeAndOriginalId(AlgorithmType type, int originalId);

    /**
     * 通过算法类型和算法统一接口中对应的ID获取对应的算法，相关的信息存储在AlgorithmInfo类中
     * @param type 四种算法类型之一
     * @param originalId 算法统一接口中对应的ID
     * @return 算法在数据表中的信息，可使用execute方法执行算法
     */
    List<AlgorithmInfo> getAlgorithmInfoConvertObjectByTypeAndOriginalId(AlgorithmType type, int originalId);

    /**
     * 通过算法类型和算法统一接口中对应的ID获取对应的算法
     * @param type 四种算法类型之一对应的整数
     * @param originalId 算法统一接口中对应的ID
     * @return 算法在数据表中的信息
     */
    List<DesensitizationAlgorithm> getAlgorithmInfoByTypeAndOriginalId(int type, int originalId);

    /**
     * 通过算法类型和算法统一接口中对应的ID获取对应的算法，相关的信息存储在AlgorithmInfo类中
     * @param type 四种算法类型之一对应的整数
     * @param originalId 算法统一接口中对应的ID
     * @return 算法在数据表中的信息，可使用execute方法执行算法
     */
    List<AlgorithmInfo> getAlgorithmInfoConvertObjectByTypeAndOriginalId(int type, int originalId);

    /**
     * 更新算法的参数
     * @param id 算法id
     * @param low 低等级对应的脱敏参数
     * @param medium 中等级对应的脱敏参数
     * @param high 高等级对应的脱敏参数
     * @return 返回改变的行的数量
     */
    @Transactional(transactionManager = "gaMybatisTransactionManager")
    int updateAlgorithmParams(int id, String low, String medium, String high);

    /**
     * 更新算法的参数
     * @param algorithmInfo 算法信息
     * @return 返回改变的行的数量
     */
    @Transactional(transactionManager = "gaMybatisTransactionManager")
    int updateAlgorithmParams(AlgorithmInfo algorithmInfo);

    /**
     * 更新多个算法的参数
     * @param algorithms 包含多个AlgorithmInfoParamDto的列表
     * @return 返回改变的行的数量
     */
    @Transactional(transactionManager = "gaMybatisTransactionManager")
    int updateAlgorithmParamsInBatch(List<AlgorithmInfoParamDto> algorithms);
}

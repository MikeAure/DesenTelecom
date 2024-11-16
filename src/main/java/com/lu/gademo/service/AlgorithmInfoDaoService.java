package com.lu.gademo.service;

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

    List<DesensitizationAlgorithm> getAlgorithmInfoByTypeAndOriginalId(AlgorithmType type, int originalId);

    List<AlgorithmInfo> getAlgorithmInfoConvertObjectByTypeAndOriginalId(AlgorithmType type, int originalId);

    List<DesensitizationAlgorithm> getAlgorithmInfoByTypeAndOriginalId(int type, int originalId);

    List<AlgorithmInfo> getAlgorithmInfoConvertObjectByTypeAndOriginalId(int type, int originalId);

    @Transactional(transactionManager = "gaMybatisTransactionManager")
    int updateAlgorithmParams(int id, String low, String medium, String high);

    @Transactional(transactionManager = "gaMybatisTransactionManager")
    int updateAlgorithmParams(AlgorithmInfo algorithmInfo);

    @Transactional(transactionManager = "gaMybatisTransactionManager")
    int updateAlgorithmParamsInBatch(List<AlgorithmInfoParamDto> algorithms);
}

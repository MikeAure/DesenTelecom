package com.lu.gademo.service;

import com.lu.gademo.entity.ga.TypeAlgoMapping;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TypeAlgoMappingDaoService {
    List<TypeAlgoMapping> getTypeAlgoMappingInfoByTypeId(int typeId);
    List<String> getAlgNamesByTypeName(@Param("typeName") String typeName);

}

package com.lu.gademo.service.impl;

import com.lu.gademo.entity.ga.TypeAlgoMapping;
import com.lu.gademo.mapper.ga.TypeAlgoMappingDao;
import com.lu.gademo.service.TypeAlgoMappingDaoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class TypeAlgoMappingDaoServiceImpl implements TypeAlgoMappingDaoService {
    private final TypeAlgoMappingDao typeAlgoMappingDao;

    public TypeAlgoMappingDaoServiceImpl(TypeAlgoMappingDao typeAlgoMappingDao) {
        this.typeAlgoMappingDao = typeAlgoMappingDao;
    }

    @Override
    public List<TypeAlgoMapping> getTypeAlgoMappingInfoByTypeId(int typeId) {
        return typeAlgoMappingDao.getTypeAlgoMappingInfoByTypeId(typeId);
    }

    @Override
    public List<String> getAlgNamesByTypeName(String typeName) {
        return typeAlgoMappingDao.getAlgNamesByTypeName(typeName);
    }
}

package com.lu.gademo.mapper.ga;

import com.lu.gademo.entity.ga.ExcelAlgorithm;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ExcelAlgorithmsDao {

    @Select("SELECT * FROM excel_algos")
    List<ExcelAlgorithm> getAllAlgorithms();

}

package com.lu.gademo.dao.ga.templateParam;

import com.lu.gademo.entity.ga.templateParam.onlineTaxi2Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface onlineTaxi2ParamDao extends JpaRepository<onlineTaxi2Param, Integer> {

    @Query("select columnName from onlineTaxi2Param ")
    List<String> getColumnName();
}

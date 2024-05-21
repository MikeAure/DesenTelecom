package com.lu.gademo.dao.templateParam;

import com.lu.gademo.entity.templateParam.onlineTaxiParam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface onlineTaxiParamDao extends JpaRepository<onlineTaxiParam, Integer> {
}

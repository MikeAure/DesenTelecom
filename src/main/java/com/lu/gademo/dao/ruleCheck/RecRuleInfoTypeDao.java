package com.lu.gademo.dao.ruleCheck;

import com.lu.gademo.entity.ruleCheck.RecRuleInfoType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecRuleInfoTypeDao extends JpaRepository<RecRuleInfoType, String> {
}

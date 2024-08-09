package com.lu.gademo.dao.ga.ruleCheck;

import com.lu.gademo.entity.ga.ruleCheck.RecRuleInfoType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecRuleInfoTypeDao extends JpaRepository<RecRuleInfoType, String> {
}

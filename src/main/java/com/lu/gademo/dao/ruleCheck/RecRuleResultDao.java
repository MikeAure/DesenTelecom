package com.lu.gademo.dao.ruleCheck;

import com.lu.gademo.entity.ruleCheck.RecRuleResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecRuleResultDao extends JpaRepository<RecRuleResult, String> {
}

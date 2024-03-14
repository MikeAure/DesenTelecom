package com.lu.gademo.dao.ruleCheck;

import com.lu.gademo.entity.ruleCheck.RecRuleTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecRuleTimeDao extends JpaRepository<RecRuleTime, String> {
}

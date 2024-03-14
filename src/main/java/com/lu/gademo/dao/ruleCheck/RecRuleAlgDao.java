package com.lu.gademo.dao.ruleCheck;

import com.lu.gademo.entity.ruleCheck.RecRuleAlg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecRuleAlgDao extends JpaRepository<RecRuleAlg, String> {
}

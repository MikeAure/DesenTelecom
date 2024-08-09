package com.lu.gademo.dao.ga.ruleCheck;

import com.lu.gademo.entity.ga.ruleCheck.RecRuleAlg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecRuleAlgDao extends JpaRepository<RecRuleAlg, String> {
}

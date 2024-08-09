package com.lu.gademo.dao.ga.ruleCheck;

import com.lu.gademo.entity.ga.ruleCheck.RecRuleOperate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecRuleOperateDao extends JpaRepository<RecRuleOperate, String> {
}

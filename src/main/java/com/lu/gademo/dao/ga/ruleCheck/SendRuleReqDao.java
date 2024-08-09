package com.lu.gademo.dao.ga.ruleCheck;

import com.lu.gademo.entity.ga.ruleCheck.SendRuleReq;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SendRuleReqDao extends JpaRepository<SendRuleReq, String> {
}

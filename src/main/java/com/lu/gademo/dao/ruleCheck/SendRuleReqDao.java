package com.lu.gademo.dao.ruleCheck;

import com.lu.gademo.entity.ruleCheck.SendRuleReq;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SendRuleReqDao extends JpaRepository<SendRuleReq, String> {
}

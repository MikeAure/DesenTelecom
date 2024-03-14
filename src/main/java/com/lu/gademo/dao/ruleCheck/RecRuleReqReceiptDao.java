package com.lu.gademo.dao.ruleCheck;

import com.lu.gademo.entity.ruleCheck.RecRuleReqReceipt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecRuleReqReceiptDao extends JpaRepository<RecRuleReqReceipt, String> {
}

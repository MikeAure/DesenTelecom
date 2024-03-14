package com.lu.gademo.dao.ruleCheck;

import com.lu.gademo.entity.ruleCheck.SendRuleReceipt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SendRuleReceiptDao extends JpaRepository<SendRuleReceipt, String> {
}

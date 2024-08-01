package com.lu.gademo.dao.ruleCheck;

import com.lu.gademo.entity.ruleCheck.RecRuleReqReceipt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecRuleReqReceiptDao extends JpaRepository<RecRuleReqReceipt, String> {
    List<RecRuleReqReceipt> deleteByCertificateID(String certificateID);

    boolean existsByCertificateID(String certificateID);
}

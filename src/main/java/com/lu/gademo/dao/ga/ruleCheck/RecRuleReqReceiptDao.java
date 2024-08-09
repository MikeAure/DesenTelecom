package com.lu.gademo.dao.ga.ruleCheck;

import com.lu.gademo.entity.ga.ruleCheck.RecRuleReqReceipt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecRuleReqReceiptDao extends JpaRepository<RecRuleReqReceipt, String> {
    List<RecRuleReqReceipt> deleteByCertificateID(String certificateID);

    boolean existsByCertificateID(String certificateID);
}

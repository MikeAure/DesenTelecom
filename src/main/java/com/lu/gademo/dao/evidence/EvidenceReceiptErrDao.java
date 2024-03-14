package com.lu.gademo.dao.evidence;

import com.lu.gademo.entity.evidence.EvidenceReceiptErr;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EvidenceReceiptErrDao extends JpaRepository<EvidenceReceiptErr, String> {
}

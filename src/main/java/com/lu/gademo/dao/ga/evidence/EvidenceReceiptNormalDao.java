package com.lu.gademo.dao.ga.evidence;

import com.lu.gademo.entity.ga.evidence.EvidenceReceiptNormal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EvidenceReceiptNormalDao extends JpaRepository<EvidenceReceiptNormal, String> {
}

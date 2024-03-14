package com.lu.gademo.dao.evidence;

import com.lu.gademo.entity.evidence.SubmitEvidenceLocal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubmitEvidenceLocalDao extends JpaRepository<SubmitEvidenceLocal, String> {
}

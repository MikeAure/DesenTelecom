package com.lu.gademo.dao.ga.evidence;

import com.lu.gademo.entity.ga.evidence.SubmitEvidenceRemote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubmitEvidenceRemoteDao extends JpaRepository<SubmitEvidenceRemote, String> {
}

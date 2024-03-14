package com.lu.gademo.dao.evidence;

import com.lu.gademo.entity.evidence.SubmitEvidenceRemote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubmitEvidenceRemoteDao extends JpaRepository<SubmitEvidenceRemote, String > {
}

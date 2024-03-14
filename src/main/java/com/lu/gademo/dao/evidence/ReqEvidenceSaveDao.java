package com.lu.gademo.dao.evidence;

import com.lu.gademo.entity.evidence.ReqEvidenceSave;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReqEvidenceSaveDao extends JpaRepository<ReqEvidenceSave, String> {
}

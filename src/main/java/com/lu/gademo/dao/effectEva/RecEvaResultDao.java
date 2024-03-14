package com.lu.gademo.dao.effectEva;

import com.lu.gademo.entity.effectEva.RecEvaResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecEvaResultDao extends JpaRepository<RecEvaResult, String> {
}

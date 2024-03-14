package com.lu.gademo.dao.effectEva;

import com.lu.gademo.entity.effectEva.RecEvaBg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecEvaBgDao extends JpaRepository<RecEvaBg, String> {
}

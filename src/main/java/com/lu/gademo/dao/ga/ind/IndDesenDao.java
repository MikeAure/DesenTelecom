package com.lu.gademo.dao.ga.ind;

import com.lu.gademo.entity.ga.ind.IndDesen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IndDesenDao extends JpaRepository<IndDesen, String> {

}

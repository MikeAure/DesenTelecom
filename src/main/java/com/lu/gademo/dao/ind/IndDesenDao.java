package com.lu.gademo.dao.ind;

import com.lu.gademo.entity.ind.IndDesen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface IndDesenDao extends JpaRepository<IndDesen, String> {

}

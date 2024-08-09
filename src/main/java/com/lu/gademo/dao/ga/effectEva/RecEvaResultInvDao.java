package com.lu.gademo.dao.ga.effectEva;

import com.lu.gademo.entity.ga.effectEva.RecEvaResultInv;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecEvaResultInvDao extends JpaRepository<RecEvaResultInv, String> {
    RecEvaResultInv findByDesenInfoAfterID(String desenInfoAfterID);
}

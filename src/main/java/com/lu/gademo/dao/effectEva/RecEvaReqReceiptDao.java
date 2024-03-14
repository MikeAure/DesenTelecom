package com.lu.gademo.dao.effectEva;

import com.lu.gademo.entity.effectEva.RecEvaReqReceipt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RecEvaReqReceiptDao extends JpaRepository<RecEvaReqReceipt, String> {
}

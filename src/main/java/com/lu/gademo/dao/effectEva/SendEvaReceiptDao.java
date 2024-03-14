package com.lu.gademo.dao.effectEva;

import com.lu.gademo.entity.effectEva.SendEvaReceipt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SendEvaReceiptDao extends JpaRepository<SendEvaReceipt, String> {
}

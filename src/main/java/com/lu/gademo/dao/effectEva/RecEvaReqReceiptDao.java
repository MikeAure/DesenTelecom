package com.lu.gademo.dao.effectEva;

import com.lu.gademo.entity.effectEva.RecEvaReqReceipt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
public interface RecEvaReqReceiptDao extends JpaRepository<RecEvaReqReceipt, String> {
    boolean existsByCertificateID(String certificateID);

    @Transactional
    void deleteByCertificateID(String certificateID);

    boolean existsByEvaRequestID(String evaRequestID);

    long deleteByEvaRequestID(String evaRequestID);
}

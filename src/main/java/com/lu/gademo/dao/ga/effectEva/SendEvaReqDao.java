package com.lu.gademo.dao.ga.effectEva;

import com.lu.gademo.entity.ga.effectEva.SendEvaReq;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
/*public interface SendEvaReqDao extends JpaRepository<SendEvaReq, String> {
}*/
public interface SendEvaReqDao extends JpaRepository<SendEvaReq, String> {

    SendEvaReq findByDesenInfoAfterIden(String desenInfoAfterIden);

    SendEvaReq findByDesenInfoAfterId(String desenInfoAfterId);

    List<SendEvaReq> findByFileTypeContains(String fileType, Sort sort);

}
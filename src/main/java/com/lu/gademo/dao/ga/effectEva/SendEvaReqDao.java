package com.lu.gademo.dao.ga.effectEva;

import com.lu.gademo.dao.ga.support.IBaseDao;
import com.lu.gademo.entity.ga.effectEva.SendEvaReq;
import org.springframework.stereotype.Repository;

@Repository
/*public interface SendEvaReqDao extends JpaRepository<SendEvaReq, String> {
}*/
public interface SendEvaReqDao extends IBaseDao<SendEvaReq, String> {

    SendEvaReq findByDesenInfoAfterIden(String desenInfoAfterIden);

    SendEvaReq findByDesenInfoAfterId(String desenInfoAfterId);
}
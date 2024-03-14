package com.lu.gademo.dao.effectEva;

import com.lu.gademo.dao.support.IBaseDao;
import com.lu.gademo.entity.effectEva.SendEvaReq;
import org.springframework.stereotype.Repository;

@Repository
/*public interface SendEvaReqDao extends JpaRepository<SendEvaReq, String> {
}*/
public interface SendEvaReqDao extends IBaseDao<SendEvaReq,String>{

}
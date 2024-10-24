package com.lu.gademo.service.impl;

import com.lu.gademo.dao.ga.effectEva.SendEvaReqDao;
import com.lu.gademo.entity.ga.effectEva.SendEvaReq;
import com.lu.gademo.service.SendEvaReqService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SendEvaReqServiceImpl implements SendEvaReqService {
    private final SendEvaReqDao sendEvaReqDao;

    @Autowired
    public SendEvaReqServiceImpl(SendEvaReqDao sendEvaReqDao) {
        this.sendEvaReqDao = sendEvaReqDao;
    }

    @Override
    public SendEvaReq findByDesenInfoAfterIden(String desenInfoAfterIden) {
        return sendEvaReqDao.findByDesenInfoAfterIden(desenInfoAfterIden);
    }

    @Override
    public SendEvaReq findByDesenInfoAfterId(String desenInfoAfterId) {
        return sendEvaReqDao.findByDesenInfoAfterId(desenInfoAfterId);
    }

    @Override
    public List<SendEvaReq> findByFileTypeContains(String fileType, Sort sort) {
        return sendEvaReqDao.findByFileTypeContains(fileType, sort);
    }
}

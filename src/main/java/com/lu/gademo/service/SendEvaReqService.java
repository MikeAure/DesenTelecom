package com.lu.gademo.service;

import com.lu.gademo.entity.ga.effectEva.SendEvaReq;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface SendEvaReqService {
    SendEvaReq findByDesenInfoAfterIden(String desenInfoAfterIden);
    SendEvaReq findByDesenInfoAfterId(String desenInfoAfterId);
    List<SendEvaReq> findByFileTypeContains(String fileType, Sort sort);
}

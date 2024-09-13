package com.lu.gademo.service;

import com.lu.gademo.entity.ga.evidence.ReqEvidenceSave;
import com.lu.gademo.entity.ga.evidence.SubmitEvidenceLocal;
import com.lu.gademo.event.ThreeSystemsEvent;

/**
 * 向本地存证和中心存证系统发送日志
 */
public interface EvidenceSystemLogSender {
    void evidenceHandleThreeSystemEvent(ThreeSystemsEvent threeSystemsEvent);
    void send2Evidence(ReqEvidenceSave reqEvidenceSave, SubmitEvidenceLocal submitEvidenceLocal);
}

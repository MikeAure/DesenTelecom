package com.lu.gademo.service;

import com.lu.gademo.entity.evidence.ReqEvidenceSave;
import com.lu.gademo.entity.evidence.SubmitEvidenceLocal;
import com.lu.gademo.event.ThreeSystemsEvent;

public interface EvidenceSystemLogSender {
    void evidenceHandleThressSystemEvent(ThreeSystemsEvent threeSystemsEvent);
    void send2Evidence(ReqEvidenceSave reqEvidenceSave, SubmitEvidenceLocal submitEvidenceLocal);
}

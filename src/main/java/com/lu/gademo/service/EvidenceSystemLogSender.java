package com.lu.gademo.service;

import com.lu.gademo.entity.evidence.ReqEvidenceSave;
import com.lu.gademo.entity.evidence.SubmitEvidenceLocal;

public interface EvidenceSystemLogSender {
    void send2Evidence(ReqEvidenceSave reqEvidenceSave, SubmitEvidenceLocal submitEvidenceLocal);
}

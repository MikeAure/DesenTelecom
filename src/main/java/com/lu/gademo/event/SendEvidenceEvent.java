package com.lu.gademo.event;

import com.lu.gademo.entity.ga.evidence.SubmitEvidenceLocal;
import com.lu.gademo.entity.ga.evidence.SubmitEvidenceRemote;
import org.springframework.context.ApplicationEvent;

/**
 * 发送给存证日志系统的事件
 */

public class SendEvidenceEvent extends ApplicationEvent {
    private final SubmitEvidenceLocal submitEvidenceLocal;
    private final SubmitEvidenceRemote submitEvidenceRemote;

    public SendEvidenceEvent(Object source, SubmitEvidenceRemote submitEvidenceRemote, SubmitEvidenceLocal submitEvidenceLocal) {
        super(source);
        this.submitEvidenceRemote = submitEvidenceRemote;
        this.submitEvidenceLocal = submitEvidenceLocal;
    }

    public SubmitEvidenceLocal getSubmitEvidenceLocal() {
        return submitEvidenceLocal;
    }

    public SubmitEvidenceRemote getSubmitEvidenceRemote() {
        return submitEvidenceRemote;
    }
}

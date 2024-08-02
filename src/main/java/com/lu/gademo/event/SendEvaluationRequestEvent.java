package com.lu.gademo.event;

import com.lu.gademo.entity.effectEva.SendEvaReq;
import org.springframework.context.ApplicationEvent;

import java.util.Objects;

public class SendEvaluationRequestEvent extends ApplicationEvent {
    private final SendEvaReq sendEvaReq;

    public SendEvaluationRequestEvent(Object source, SendEvaReq sendEvaReq) {
        super(source);
        this.sendEvaReq = sendEvaReq;
    }

    public SendEvaReq getSendEvaReq() {
        return sendEvaReq;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SendEvaluationRequestEvent)) return false;
        SendEvaluationRequestEvent that = (SendEvaluationRequestEvent) o;
        return Objects.equals(getSendEvaReq(), that.getSendEvaReq());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSendEvaReq());
    }
}

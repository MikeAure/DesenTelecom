package com.lu.gademo.event;

import com.lu.gademo.entity.ruleCheck.SendRuleReq;
import org.springframework.context.ApplicationEvent;

import java.util.Objects;

public class SendRuleCheckRequestEvent extends ApplicationEvent {
    private final SendRuleReq sendRuleReq;

    public SendRuleCheckRequestEvent(Object source, SendRuleReq sendRuleReq) {
        super(source);
        this.sendRuleReq = sendRuleReq;
    }

    public SendRuleReq getSendRuleReq() {
        return sendRuleReq;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SendRuleCheckRequestEvent)) return false;
        SendRuleCheckRequestEvent that = (SendRuleCheckRequestEvent) o;
        return Objects.equals(getSendRuleReq(), that.getSendRuleReq());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSendRuleReq());
    }
}

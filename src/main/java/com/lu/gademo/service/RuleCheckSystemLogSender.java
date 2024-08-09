package com.lu.gademo.service;

import com.lu.gademo.entity.ga.ruleCheck.SendRuleReq;
import com.lu.gademo.event.ThreeSystemsEvent;

public interface RuleCheckSystemLogSender {
    void ruleCheckHandleThreeSystemEvent(ThreeSystemsEvent threeSystemsEvent);
    void send2RuleCheck(SendRuleReq sendRuleReq);
}

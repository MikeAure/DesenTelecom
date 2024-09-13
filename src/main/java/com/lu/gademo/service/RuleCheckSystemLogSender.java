package com.lu.gademo.service;

import com.lu.gademo.entity.ga.ruleCheck.SendRuleReq;
import com.lu.gademo.event.ThreeSystemsEvent;

/**
 * 向合规检测系统发送日志
 */
public interface RuleCheckSystemLogSender {
    void ruleCheckHandleThreeSystemEvent(ThreeSystemsEvent threeSystemsEvent);
    void send2RuleCheck(SendRuleReq sendRuleReq);
}

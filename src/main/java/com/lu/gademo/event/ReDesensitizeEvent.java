package com.lu.gademo.event;

import com.lu.gademo.entity.ga.effectEva.RecEvaResultInv;
import org.springframework.context.ApplicationEvent;

/**
 * 重脱敏事件
 */
public class ReDesensitizeEvent extends ApplicationEvent {
    private final RecEvaResultInv recEvaResultInv;
    private final LogManagerEvent logManagerEvent;

    public ReDesensitizeEvent(Object source, RecEvaResultInv recEvaResultInv, LogManagerEvent logManagerEvent) {
        super(source);
        this.recEvaResultInv = recEvaResultInv;
        this.logManagerEvent = logManagerEvent;
    }

    public RecEvaResultInv getRecEvaResultInv() {
        return recEvaResultInv;
    }

    public LogManagerEvent getLogManagerEvent() {
        return logManagerEvent;
    }
}

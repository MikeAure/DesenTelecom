package com.lu.gademo.event;

import com.lu.gademo.entity.effectEva.RecEvaResultInv;
import org.springframework.context.ApplicationEvent;

public class ReDesensitizeEvent extends ApplicationEvent {
    private final RecEvaResultInv recEvaResultInv;

    public ReDesensitizeEvent(Object source, RecEvaResultInv recEvaResultInv) {
        super(source);
        this.recEvaResultInv = recEvaResultInv;
    }

    public RecEvaResultInv getRecEvaResultInv() {
        return recEvaResultInv;
    }
}

package com.lu.gademo.event;

import com.lu.gademo.entity.effectEva.RecEvaResultInv;
import org.springframework.context.ApplicationEvent;

import java.util.Objects;

// 评测结果无效发送的事件
public class RecvEvaluationInvalidResultEvent extends ApplicationEvent {
    private final RecEvaResultInv recEvaResultInv;

    public RecvEvaluationInvalidResultEvent(Object source, RecEvaResultInv recEvaResultInv) {
        super(source);
        this.recEvaResultInv = recEvaResultInv;
    }

    public RecEvaResultInv getRecEvaResultInv() {
        return recEvaResultInv;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RecvEvaluationInvalidResultEvent)) return false;
        RecvEvaluationInvalidResultEvent that = (RecvEvaluationInvalidResultEvent) o;
        return Objects.equals(getRecEvaResultInv(), that.getRecEvaResultInv());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getRecEvaResultInv());
    }
}

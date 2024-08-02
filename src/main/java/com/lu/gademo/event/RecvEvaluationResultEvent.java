package com.lu.gademo.event;

import com.lu.gademo.entity.effectEva.RecEvaResult;
import org.springframework.context.ApplicationEvent;

import java.util.Objects;

// 正常评测结果返回的事件
public class RecvEvaluationResultEvent extends ApplicationEvent {
    private final RecEvaResult recEvaResult;

    public RecvEvaluationResultEvent(Object source, RecEvaResult recEvaResult) {
        super(source);
        this.recEvaResult = recEvaResult;
    }

    public RecEvaResult getRecEvaResult() {
        return recEvaResult;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RecvEvaluationResultEvent)) return false;
        RecvEvaluationResultEvent that = (RecvEvaluationResultEvent) o;
        return Objects.equals(getRecEvaResult(), that.getRecEvaResult());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getRecEvaResult());
    }
}

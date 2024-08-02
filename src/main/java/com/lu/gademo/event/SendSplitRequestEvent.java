package com.lu.gademo.event;

import com.lu.gademo.entity.split.SendSplitDesenData;
import org.springframework.context.ApplicationEvent;

import java.util.Objects;

// 发送给拆分重构系统的请求事件
public class SendSplitRequestEvent extends ApplicationEvent {
    private final SendSplitDesenData sendSplitDesenData;

    public SendSplitRequestEvent(Object source, SendSplitDesenData sendSplitDesenData) {
        super(source);
        this.sendSplitDesenData = sendSplitDesenData;
    }

    public SendSplitDesenData getSendSplitDesenData() {
        return sendSplitDesenData;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SendSplitRequestEvent)) return false;
        SendSplitRequestEvent that = (SendSplitRequestEvent) o;
        return Objects.equals(getSendSplitDesenData(), that.getSendSplitDesenData());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSendSplitDesenData());
    }
}

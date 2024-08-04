package com.lu.gademo.service;

import com.lu.gademo.entity.split.SendSplitDesenData;
import com.lu.gademo.event.ThreeSystemsEvent;

public interface SplitSystemLogSender {
    void splitHandleThreeSystemEvent(ThreeSystemsEvent logManagerEvent);
    void send2Split(SendSplitDesenData sendSplitDesenData, byte[] rawFileData);
}

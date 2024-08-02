package com.lu.gademo.service;

import com.lu.gademo.entity.split.SendSplitDesenData;

public interface SplitSystemLogSender {
    void send2Split(SendSplitDesenData sendSplitDesenData, byte[] rawFileData);
}

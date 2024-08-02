package com.lu.gademo.service;

import com.lu.gademo.entity.effectEva.SendEvaReq;

public interface EvaluationSystemLogSender {
    void send2EffectEva(SendEvaReq sendEvaReq, byte[] rawFileData,
                        byte[] desenFileData);
}

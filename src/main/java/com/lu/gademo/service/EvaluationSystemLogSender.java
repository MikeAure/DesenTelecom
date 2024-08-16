package com.lu.gademo.service;

import com.lu.gademo.entity.ga.effectEva.SendEvaReq;
import com.lu.gademo.model.effectEva.EvaluationSystemReturnResult;

import java.io.IOException;

public interface  EvaluationSystemLogSender {
    EvaluationSystemReturnResult send2EffectEva(SendEvaReq sendEvaReq, byte[] rawFileData,
                                                byte[] desenFileData, Boolean ifSendFile);
}

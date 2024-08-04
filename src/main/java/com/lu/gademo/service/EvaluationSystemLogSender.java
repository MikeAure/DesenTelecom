package com.lu.gademo.service;

import com.lu.gademo.entity.effectEva.SendEvaReq;
import com.lu.gademo.model.effectEva.EvaluationSystemReturnResult;

public interface  EvaluationSystemLogSender {
    EvaluationSystemReturnResult send2EffectEva(SendEvaReq sendEvaReq, byte[] rawFileData,
                                                byte[] desenFileData, Boolean ifSendFile);
}

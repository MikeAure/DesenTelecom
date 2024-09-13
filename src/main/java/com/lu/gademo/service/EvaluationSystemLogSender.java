package com.lu.gademo.service;

import com.lu.gademo.entity.ga.effectEva.SendEvaReq;
import com.lu.gademo.model.effectEva.EvaluationSystemReturnResult;

import java.io.IOException;

/**
 * 向评测系统发送日志
 */
public interface  EvaluationSystemLogSender {
    EvaluationSystemReturnResult send2EffectEva(SendEvaReq sendEvaReq, byte[] rawFileData,
                                                byte[] desenFileData, Boolean ifSendFile);
}

package com.lu.gademo.controller;

import com.lu.gademo.service.WsAlgorithmLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
@Slf4j
public class WsAlgorithmLogController {
    private WsAlgorithmLogService wsAlgorithmLogService;

    @Autowired
    public WsAlgorithmLogController(WsAlgorithmLogService wsAlgorithmLogService) {
        this.wsAlgorithmLogService = wsAlgorithmLogService;
    }

    @MessageMapping("/startAlgorithm")
    public void sendStartLog(String algorithmName) {
        wsAlgorithmLogService.sendLog(algorithmName, algorithmName + " started logging");
    }

}

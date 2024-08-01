//package com.lu.gademo.service.impl;
//
//import com.lu.gademo.service.WsAlgorithmLogService;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
//import org.springframework.messaging.simp.SimpMessagingTemplate;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//@Service
//@Slf4j
//public class WsAlgorithmLogServiceImpl implements WsAlgorithmLogService {
//
//    private final SimpMessagingTemplate simpMessageTemplate;
//
//    public WsAlgorithmLogServiceImpl(SimpMessagingTemplate simpMessageTemplate) {
//        this.simpMessageTemplate = simpMessageTemplate;
//    }
//
//    @Override
//    public void sendLog(String algorithmName, String content) {
//        simpMessageTemplate.convertAndSend("/topic/algorithmLog/" + algorithmName, content);
//    }
//}

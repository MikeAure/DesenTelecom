package com.lu.gademo.service.impl;//package com.lu.gademo.service.impl;
//
//import com.lu.gademo.model.TcpPacket;
//import com.lu.gademo.service.LogService;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import org.springframework.messaging.MessageChannel;
//import org.springframework.messaging.support.MessageBuilder;
//import org.springframework.stereotype.Service;
//
//@Data
//@AllArgsConstructor
//@Service
//public class LogServiceImpl implements LogService {
//
//    private MessageChannel logRequestChannel;
//    private MessageChannel logResponseChannel;
//    @Override
//    public void sendLogToServers(TcpPacket payload) {
//        logRequestChannel.send(MessageBuilder.withPayload(payload).build());
//    }
//
//    @Override
//    public void handleResponse(TcpPacket payload) {
//
//    }
//
//
//
//}

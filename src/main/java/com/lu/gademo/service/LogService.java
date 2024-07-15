package com.lu.gademo.service;

import com.lu.gademo.model.TcpPacket;

public interface LogService {
    void sendLogToServers(TcpPacket payload);
    void handleResponse(TcpPacket payload);

}

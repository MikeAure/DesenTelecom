package com.lu.gademo.service.impl;

import com.lu.gademo.service.SocketRecvService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PreDestroy;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

@Slf4j
@Data

public class SocketRecvServiceImpl implements SocketRecvService {
    private int port;

    private ServerSocket serverSocket;

    public SocketRecvServiceImpl(@Value("${socketrecv.port}") int port) throws IOException {
        this.port = port;
        this.serverSocket = new ServerSocket(port);
        log.info("Serversocket binds to {}", port);
    }


    @Override
    public void listen() {
        int counter = 0;
        while (counter <= 5) {
            try {
                Socket socket = serverSocket.accept();
                log.info("New connection from {}", socket.getInetAddress());
                counter++;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @PreDestroy
    public void closeSeverSocket() {
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
                log.info("ServerSocket closed");
            }
        } catch (IOException e) {
            log.error("Error closing ServerSocket", e);
        }
    }
}

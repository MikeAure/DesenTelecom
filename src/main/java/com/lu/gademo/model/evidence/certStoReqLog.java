package com.lu.gademo.model.evidence;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.net.Socket;

public class certStoReqLog {
    //文件全局ID
    private final String fileGloID;
    //脱敏对象大小
    private final int objectSize;
    //脱敏对象模态
    private final String objectMode = "text";
    //套接字
    private final Socket socket;

    //构造函数
    public certStoReqLog(String fileGloID, int objectSize, Socket socket) {
        this.fileGloID = fileGloID;
        this.objectSize = objectSize;
        this.socket = socket;
    }

    public void send() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        //json数据
        ObjectNode json = objectMapper.createObjectNode();
        json.put("fileGloID", fileGloID);
        json.put("objectSize", objectSize);
        json.put("objectMode", objectMode);
        //包裹json
        ObjectNode jsonData = objectMapper.createObjectNode();
        jsonData.set("data", json);

        /*//构建tcp数据包
        tcpPacket tcpPacket = new tcpPacket(jsonData.toString());
        tcpPacket.setCOMMAND_CATEGORY((short) 0x0001);
        tcpPacket.setITEM_CATEGORY((short) 0x0000);
        tcpPacket.setMESSAGE_VERSION((short) 0x1000);
        //数据包字节数组
        byte[] tcp = tcpPacket.buildPacket();

        //发送
        OutputStream socketOutputStream = socket.getOutputStream();
        socketOutputStream.write(tcp);
*/
    }


}

package com.lu.gademo.config;

import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class MyWebSocketHandler extends TextWebSocketHandler {

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        try {
            // 后端读取本地Excel文件并发送给前端
            File excelFile = new File("path/to/your/excel/file.xlsx");
            byte[] fileBytes = readFileToBytes(excelFile);
            session.sendMessage(new BinaryMessage(fileBytes));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) {
        try {
            // 前端传递的操作数据，这里简单演示，您可以根据实际需求处理
            byte[] data = message.getPayload().array();
            // 在这里进行属性操作并修改Excel文件
            // ...

            // 修改后的Excel文件发送回前端
            File modifiedExcelFile = new File("path/to/modified/excel/file.xlsx");
            byte[] modifiedFileBytes = readFileToBytes(modifiedExcelFile);
            session.sendMessage(new BinaryMessage(modifiedFileBytes));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private byte[] readFileToBytes(File file) throws Exception {
        try (InputStream inputStream = new FileInputStream(file)) {
            byte[] bytes = new byte[(int) file.length()];
            int offset = 0;
            int bytesRead;
            while ((bytesRead = inputStream.read(bytes, offset, bytes.length - offset)) > 0) {
                offset += bytesRead;
            }
            return bytes;
        }
    }
}

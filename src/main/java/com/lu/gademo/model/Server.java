package com.lu.gademo.model;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lu.gademo.utils.Util;
import com.lu.gademo.utils.impl.UtilImpl;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class Server {
    public static void main(String[] args) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            ServerSocket serverSocket = new ServerSocket(30002);
            Util util = new UtilImpl();
            //发送t_huji
            util.mySqlDump("t_huji.sql");
/*// 通过 Socket 发送 SQL 文件
            BufferedOutputStream outputStream = new BufferedOutputStream(remoteSocket.getOutputStream());
            FileInputStream fileInputStream = new FileInputStream("t_huji.sql");
// 发送文件大小
            out.writeLong(new File("t_huji.sql").length());
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            outputStream.flush();


//发送p_huji
            MySQLDump("ga","p_huji");
// 通过 Socket 发送 SQL 文件
            outputStream = new BufferedOutputStream(remoteSocket.getOutputStream());
            fileInputStream = new FileInputStream("p_huji.sql");
// 发送文件大小
            out.writeLong(new File("p_huji.sql").length());
            while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            outputStream.flush();
            System.out.println("File sent successfully");*/
            while (true) {
                System.out.println("等待客户端连接...");
                Socket socket = serverSocket.accept();
                System.out.println("客户端已连接");
                // 接收请求信息
                // 读取头部
                InputStream inputStream = socket.getInputStream();
                DataInputStream dataInputStream = new DataInputStream(inputStream);
                byte[] header = new byte[14];
                System.out.println(dataInputStream.read(header, 0, 14));

                // 响应数据域长度
                int dataLength = dataInputStream.readInt();
                System.out.println(dataLength);

                // 读取数据域内容
                byte[] dataBytes = new byte[dataLength - 34];
                System.out.println(dataLength - 34);
                System.out.println(inputStream.read(dataBytes));
                String jsonData = new String(dataBytes, StandardCharsets.UTF_8);
                System.out.println(jsonData);
                // 认证与校验
                byte[] auth = new byte[16];
                inputStream.read(auth);

                //String 转 json
                JsonNode jsonNode = objectMapper.readTree(jsonData);
                JsonNode recData = jsonNode.get("data");
                JsonNode recContent = recData.get("content");

                // 接收实体数组
                ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);

                /*// 接收 hujis 对象列表
                System.out.println("Huji信息");
                int hujisSize = (int) objectInputStream.readObject(); // 读取列表大小
                //int hujisSize = dataInputStream.readInt();
                System.out.println(hujisSize);
                List<Huji> receivedHujis = new ArrayList<>();
                for (int i = 0; i < hujisSize; i++) {
                    Huji h = (Huji) objectInputStream.readObject(); // 逐个读取 hujis 对象
                    receivedHujis.add(h);
                    System.out.println(h.toString());
                }
                System.out.println("DpHuji信息");
                int dphujisSize = (int) objectInputStream.readObject(); // 读取列表大小
                //int dphujisSize = dataInputStream.readInt();
                List<DpHuji> receivedDpHujis = new ArrayList<>();
                for (int i = 0; i < dphujisSize; i++) {
                    DpHuji h = (DpHuji) objectInputStream.readObject(); // 逐个读取 hujis 对象
                    receivedDpHujis.add(h);
                    System.out.println(h.toString());
                }

                System.out.println("参数信息");
                int hujiParamSize = (int) objectInputStream.readObject(); // 读取列表大小
                System.out.println(hujiParamSize);
                List<HujiParam> receivedHujiParam = new ArrayList<>();
                for (int i = 0; i < hujiParamSize; i++) {
                    HujiParam h = (HujiParam) objectInputStream.readObject(); // 逐个读取 hujis 对象
                    receivedHujiParam.add(h);
                    System.out.println(h.toString());
                }*/

                // 关闭流和Socket
                objectInputStream.close();
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } /*catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }*/
    }
}

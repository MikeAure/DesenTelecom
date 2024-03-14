package com.lu.gademo.thread;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.lu.gademo.entity.ind.IndDesen;
import com.lu.gademo.entity.split.RecSplitDesen;
import com.lu.gademo.log.tcpPacket;
import com.lu.gademo.utils.Util;
import com.lu.gademo.utils.impl.UtilImpl;

import java.io.DataInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ClientHandler implements Runnable {

    private Socket clientSocket;
    String jdbcUrl = "jdbc:mysql://localhost:3306/ga?characterEncoding=utf8&useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    String jdbcUsername = "root";
    //String jdbcPassword = "123456";
    String jdbcPassword = "123456QWer!!";

    /**
     * 工具类
     */
    Util generalUtil = new UtilImpl();
    Path currentPath = Paths.get("").toAbsolutePath();
    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
   }

    @Override
    public void run() {
        try {
            DataInputStream inputStream = new DataInputStream(clientSocket.getInputStream());

            byte[] headerBytes = new byte[18];
            int bytesRead = inputStream.read(headerBytes);
            // 没有读取到数据
            if (bytesRead == -1) {
                System.out.println("no data");
            }

            // 获取数据包长度packetLength之后 -16 - 18得到数据域长度
            int dataLength = (headerBytes[14] & 0xFF) << 24 |
                    (headerBytes[15] & 0xFF) << 16 |
                    (headerBytes[16] & 0xFF) << 8 |
                    (headerBytes[17] & 0xFF) - 16 -18;

            // 读取数据域内容
            byte[] dataBytes = new byte[dataLength];
            inputStream.read(dataBytes);
            String jsonData = new String(dataBytes, "UTF-8");
            //String 转 json
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(jsonData);
            JsonNode data = jsonNode.get("data");

            // 判断来自哪个系统，0x3216表示敏感个人信息识别， 0x3120表示拆分重构
            if (data.get("DataType").asInt() == 0x3216){
                JsonNode content = data.get("content");
                //byte[] fileData = content.get("fileData").binaryValue();
                IndDesen indDesen = mapper.treeToValue(content, IndDesen.class);
                // 文件保存的路径
                String fileName = generalUtil.getTime();
                fileName = "ind-" + fileName.replace(':','-');
                String outputPath = currentPath + "raw_files\\"+fileName+".xlsx";
                FileOutputStream fileOutputStream = new FileOutputStream(outputPath);
                fileOutputStream.write(indDesen.getFileData().getBytes());

                /*//  更改json的文件内容为文件路径
                ObjectNode objectNode = (ObjectNode)content;
                objectNode.put("fileData",outputPath);*/
                try {
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    Connection connection = DriverManager.getConnection(jdbcUrl, jdbcUsername, jdbcPassword);
                    // 相关信息插入ind_desen数据库
                    PreparedStatement insert = connection.prepareStatement("INSERT INTO ind_desen VALUES (?, ?, ?, ?, ?, ?, ?)");
                    insert.setString(1, indDesen.getRecFlagInfoId());
                    insert.setInt(2, indDesen.getFileTypeIden());
                    insert.setString(3, indDesen.getFileInfo());
                    insert.setString(4, outputPath);
                    insert.setString(5, indDesen.getDesenIntention());
                    insert.setString(6, indDesen.getDesenRequirements());
                    insert.setString(7, indDesen.getDesenControlSet());

                    // 发送收据
                    String certificateID = generalUtil.getSHA256Hash(indDesen.getFileData()+generalUtil.getTime());

                    //  构建发送数据包
                    ObjectNode contentRec = mapper.createObjectNode();
                    contentRec.put("certificateID", certificateID);
                    contentRec.put("recFlagInfoID", indDesen.getRecFlagInfoId());
                    contentRec.put("hash", generalUtil.getSHA256Hash(certificateID + indDesen.getRecFlagInfoId()));
                    ObjectNode dataJson = mapper.createObjectNode();
                    dataJson.put("DataType",0x2011);
                    dataJson.set("content", contentRec);

                    ObjectNode json = mapper.createObjectNode();
                    json.set("data", dataJson);

                    tcpPacket tcpPacket = new tcpPacket(mapper.writeValueAsString(json), (short)0x0100, (short)0x0020, (short)0);
                    byte[] tcp = tcpPacket.buildPacket();
                    //  发送
                    OutputStream outputStream = clientSocket.getOutputStream();
                    // 存储
                    PreparedStatement insertRec = connection.prepareStatement("INSERT INTO ind_desen_receipt VALUES (?, ?, ?)");
                    insertRec.setString(1, certificateID);
                    insertRec.setString(2, indDesen.getRecFlagInfoId());
                    insertRec.setString(3, generalUtil.getSHA256Hash(certificateID + indDesen.getRecFlagInfoId()));
                    outputStream.write(tcp);
                    outputStream.close();
                    clientSocket.close();

                } catch (ClassNotFoundException | SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            else if (data.get("DataType").asInt() == 0x3120) {
                JsonNode content = data.get("content");
                //byte[] fileData = content.get("fileData").binaryValue();
                RecSplitDesen recSplitDesen = mapper.treeToValue(content, RecSplitDesen.class);
                // 文件保存的路径
                String fileName = generalUtil.getTime();
                fileName = "split-" + fileName.replace(':','-');
                String outputPath = currentPath + "raw_files\\"+fileName+".xlsx";
                FileOutputStream fileOutputStream = new FileOutputStream(outputPath);
                fileOutputStream.write(recSplitDesen.getFileData().getBytes());

                try {
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    Connection connection = DriverManager.getConnection(jdbcUrl, jdbcUsername, jdbcPassword);
                    // 相关信息插入ind_desen数据库
                    PreparedStatement insert = connection.prepareStatement("INSERT INTO rec_split_desen VALUES (?, ?, ?, ?, ?, ?, ?)");
                    insert.setString(1, recSplitDesen.getRecStoInfoID());
                    insert.setInt(2, recSplitDesen.getFileTypeIden());
                    insert.setString(3, recSplitDesen.getFileInfo());
                    insert.setString(4, outputPath);
                    insert.setString(5, recSplitDesen.getDesenIntention());
                    insert.setString(6, recSplitDesen.getDesenRequirements());
                    insert.setString(7, recSplitDesen.getDesenControlSet());

                    // 收据生成、存储
                    PreparedStatement insertRec = connection.prepareStatement("INSERT INTO  VALUES (?, ?, ?)");
                    String certificateID = generalUtil.getSHA256Hash(recSplitDesen.getFileData()+generalUtil.getTime());
                    insertRec.setString(1, certificateID);
                    insertRec.setString(2, recSplitDesen.getRecStoInfoID());
                    insertRec.setString(3, generalUtil.getSHA256Hash(certificateID + recSplitDesen.getRecStoInfoID()));

                    //  构建发送数据包
                    ObjectNode contentRec = mapper.createObjectNode();
                    contentRec.put("certificateID", certificateID);
                    contentRec.put("recStoInfoID", recSplitDesen.getRecStoInfoID());
                    contentRec.put("hash", generalUtil.getSHA256Hash(certificateID + recSplitDesen.getRecStoInfoID()));

                    ObjectNode dataJson = mapper.createObjectNode();
                    dataJson.put("DataType",0x2011);
                    dataJson.set("content", contentRec);

                    ObjectNode json = mapper.createObjectNode();
                    json.set("data", dataJson);

                    tcpPacket tcpPacket = new tcpPacket(mapper.writeValueAsString(json), (short)0x0100, (short)0x0020, (short)0);
                    byte[] tcp = tcpPacket.buildPacket();
                    //  发送
                    OutputStream outputStream = clientSocket.getOutputStream();
                    outputStream.write(tcp);
                    outputStream.close();
                    clientSocket.close();

                } catch (ClassNotFoundException | SQLException e) {
                    throw new RuntimeException(e);
                }
            }

            //认证与校验
            byte[] Auth = new byte[16];
            inputStream.read(Auth);
            inputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}


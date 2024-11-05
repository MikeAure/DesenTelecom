//package com.lu.gademo.utils;
//
//import com.fasterxml.jackson.core.util.ByteArrayBuilder;
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.databind.json.JsonMapper;
//import com.lu.gademo.model.TcpPacket;
//
//import java.io.DataInputStream;
//import java.io.DataOutputStream;
//import java.io.IOException;
//import java.net.ServerSocket;
//import java.net.Socket;
//import java.nio.charset.StandardCharsets;
//import java.util.Arrays;
//
//public class MockEva {
//    public static void readRemoteContent(ByteArrayBuilder byteArrayBuilder, DataInputStream dataInputStream) throws IOException {
//        byte[] header = new byte[14];
//        dataInputStream.read(header);
//        int dataLength = dataInputStream.readInt();
//        byte[] dataBytes = new byte[dataLength - 34];
//        dataInputStream.read(dataBytes);
//        String jsonData = new String(dataBytes, StandardCharsets.UTF_8);
//        System.out.println(jsonData);
//        byte[] auth = new byte[16];
//        dataInputStream.read(auth);
//
//    }
//
//    public static void main(String[] args) throws IOException {
////        String evaReceipt = "{\"content\":{\"certificateID\":\"1722316999761\"," +
////                "\"evaRequestID\":\"b753b81ca4af980e1540b34f5ac25cde903248009b664d801cc911ad3c900f48\"," +
////                "\"hash\":\"-153222702\"}," +
////                "\"dataType\":12593}";
////
////        String evaResult = "{\"content\":" +
////                "{\"evaResultID\":\"b753b81ca4af980e1540b34f5ac25cde903248009b664d801cc911ad3c900f48\"," +
////                "\"evaPerformer\":\"脱敏工具集\"," +
////                "\"desenInfoPreID\":\"cce8307aac7570835b840ad304d015427bf20de6778051a5d0a1fa385dc90c5d\"," +
////                "\"desenInfoAfterID\":\"b753b81ca4af980e1540b34f5ac25cde903248009b664d801cc911ad3c900f48\"," +
////                "\"desenIntention\":\"注册手机号码脱敏, 用户名脱敏, 密码脱敏, 姓名脱敏, 证件号码脱敏, 性别脱敏, 邮箱脱敏, 设备标识符脱敏, 订单号脱敏, 订单金额（元）脱敏, 乘车人姓名脱敏, 乘车人证件号码脱敏, 乘车人电话号码脱敏, 车次脱敏, 座位号脱敏, 定位地址脱敏, 发车时间脱敏, 下单时间脱敏,\"," +
////                "\"desenRequirements\": \"注册手机号码抑制, 用户名抑制, 密码置换, 姓名抑制, 证件号码抑制, 性别随机扰动, 邮箱抑制, 设备标识符抑制, 订单号抑制, 订单金额（元）添加差分隐私Laplace噪声, 乘车人姓名抑制, 乘车人证件号码抑制, 乘车人电话号码抑制, 车次随机扰动, 座位号随机扰动, 定位地址抑制, 发车时间日期添加Laplace噪声, 下单时间日期添加Laplace噪声\"," +
////                "\"desenControlSet\":\"desencontrolset\"," +
////                "\"desenAlg\":\"16,15,19,15,16,2,13,16,16,3,15,16,16,2,2,14,1,1,\"," +
////                "\"desenAlgParam\" : \"4,first,20,first,4,2,2,4,4,1,first,4,4,2,2,city,0.01,0.01,\","+
////                "\"desenPerformStartTime\" : \"2024-08-04 10:40:19\"," +
////                "\"desenPerformEndTime\" : \"2024-08-04 10:40:27\"," +
////                "\"desenLevel\" : \"2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,\"," +
////                "\"desenPerformer\":\"脱敏工具集\"," +
////                "\"desenCom\":true," +
////                "\"desenDeviation\":1,\"desenExtendedcontrol\":5,\"desenInformationloss\":1,\"desenUsability\":0," +
////                "\"desenComplexity\":1,\"desenEffectEvaRet\":true,\"desenBGEffectEvaRet\":true},\"dataType\":12594}";
////
////        String evaResultInv = "{\"content\":{\"evaResultID\":\"ee6cfdf56bd74bda0652d430fc0f2f9e0dd07cd5c9e3aaa5d33bdc0517c440d6\"," +
////                "\"evaPerformer\":\"脱敏工具集\",\"desenInfoPreID\":\"cce8307aac7570835b840ad304d015427bf20de6778051a5d0a1fa385dc90c5d\"," +
////                "\"desenInfoAfterID\":\"ee6cfdf56bd74bda0652d430fc0f2f9e0dd07cd5c9e3aaa5d33bdc0517c440d6\"," +
////                "\"desenIntention\":\"注册手机号码脱敏, 用户名脱敏, 密码脱敏, 姓名脱敏, 证件号码脱敏, 性别脱敏, 邮箱脱敏, 设备标识符脱敏, 订单号脱敏, 订单金额（元）脱敏, 乘车人姓名脱敏, 乘车人证件号码脱敏, 乘车人电话号码脱敏, 车次脱敏, 座位号脱敏, 定位地址脱敏, 发车时间脱敏, 下单时间脱敏,\"," +
////                "\"desenRequirements\": \"注册手机号码抑制, 用户名抑制, 密码置换, 姓名抑制, 证件号码抑制, 性别随机扰动, 邮箱抑制, 设备标识符抑制, 订单号抑制, 订单金额（元）添加差分隐私Laplace噪声, 乘车人姓名抑制, 乘车人证件号码抑制, 乘车人电话号码抑制, 车次随机扰动, 座位号随机扰动, 定位地址抑制, 发车时间日期添加Laplace噪声, 下单时间日期添加Laplace噪声\"," +
////                "\"desenControlSet\":\"desencontrolset\"," +
////                "\"desenAlg\":\"16,15,19,15,16,2,13,16,16,3,15,16,16,2,2,14,1,1,\"," +
////                "\"desenAlgParam\" : \"4,first,20,first,4,2,2,4,4,1,first,4,4,2,2,city,0.01,0.01,\","+
////                "\"desenPerformStartTime\":\"2024-07-31 10:31:01\"," +
////                "\"desenPerformEndTime\":\"2024-07-31 10:31:05\"," +
////                "\"desenLevel\" : \"2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,\"," +
////                "\"desenPerformer\":\"脱敏工具集\"," +
////                "\"desenCom\":true," +
////                "\"desenFailedColName\" :\"用户名,密码,\" ," +
////                "\"desenDeviation\":1,\"desenExtendedcontrol\":5,\"desenInformationloss\":1," +
////                "\"desenUsability\":0,\"desenComplexity\":1,\"desenEffectEvaRet\":false}," +
////                "\"dataType\":13313}";
//
//        String evaReceipt = "{\"content\":{\"certificateID\":\"1722316999761\"," +
//                "\"evaRequestID\":\"1f19b73a3bb77c7a3b3aed6600209a3420e9fb4887937b69f2412177b244a9a3\"," +
//                "\"hash\":\"-153222702\"}," +
//                "\"dataType\":12593}";
//
//        String evaResult = "{\"content\":" +
//                "{\"evaResultID\":\"1f19b73a3bb77c7a3b3aed6600209a3420e9fb4887937b69f2412177b244a9a3\"," +
//                "\"evaPerformer\":\"脱敏工具集\"," +
//                "\"desenInfoPreID\":\"b0628b1ed1656d69fce19d04abfda0ac357d2945948563c737e31d1fbff0c599\"," +
//                "\"desenInfoAfterID\":\"1f19b73a3bb77c7a3b3aed6600209a3420e9fb4887937b69f2412177b244a9a3\"," +
//                "\"desenIntention\":\"CUST_ID脱敏,CUST_ADDR脱敏,CUST_AREA_GRADE脱敏,CUST_CONTROL_LEVEL脱敏,CUST_NAME脱敏,CUST_TYPE脱敏,CUST_GROUP脱敏,MOBILE_PHONE脱敏,CERT_TYPE脱敏,CERT_NUM脱敏,FAX脱敏,E_MAIL脱敏,POST_CODE脱敏,CREATE_STAFF脱敏,CREATE_DATE脱敏,UPDATE_STAFF脱敏,UPDATE_DATE脱敏,STATUS_DATE脱敏,STATUS_CD脱敏,REMARK脱敏,\"," +
//                "\"desenRequirements\": \"CUST_ID抑制,CUST_ADDR抑制,CUST_AREA_GRADE随机扰动,CUST_CONTROL_LEVEL随机扰动,CUST_NAME置换,CUST_TYPE随机扰动,CUST_GROUP随机扰动,MOBILE_PHONE抑制,CERT_TYPE抑制,CERT_NUM尾部截断,FAX尾部截断,E_MAIL尾部截断,POST_CODE尾部截断,CREATE_STAFF尾部截断,CREATE_DATE日期添加Laplace噪声,UPDATE_STAFF尾部截断,UPDATE_DATE日期添加Laplace噪声,STATUS_DATE日期添加Laplace噪声,STATUS_CD尾部截断,REMARK尾部截断,\"," +
//                "\"desenControlSet\":\"desencontrolset\"," +
//                "\"desenAlg\":\"16,16,2,2,19,2,2,16,16,11,11,11,11,11,1,11,1,1,11,11,\"," +
//                "\"desenAlgParam\" : \"3,3,3.6,3.6,15,3.6,3.6,3,3,3,3,3,3,3,0.001,3,0.1,0.1,3,3,\","+
//                "\"desenPerformStartTime\" : \"2024-08-08 20:43:33\"," +
//                "\"desenPerformEndTime\" : \"2024-08-08 20:43:46\"," +
//                "\"desenLevel\" : \"0,1,1,1,1,1,1,1,1,1,1,1,1,1,3,1,1,1,1,1,\"," +
//                "\"desenPerformer\":\"脱敏工具集\"," +
//                "\"desenCom\":true," +
//                "\"desenDeviation\":1,\"desenExtendedcontrol\":5,\"desenInformationloss\":1,\"desenUsability\":0," +
//                "\"desenComplexity\":1,\"desenEffectEvaRet\":true,\"desenBGEffectEvaRet\":true},\"dataType\":12594}";
//
//        String evaResultInv = "{\"content\":" +
//                "{\"evaResultID\":\"1f19b73a3bb77c7a3b3aed6600209a3420e9fb4887937b69f2412177b244a9a3\"," +
//                "\"evaPerformer\":\"脱敏工具集\"," +
//                "\"desenInfoPreID\":\"b0628b1ed1656d69fce19d04abfda0ac357d2945948563c737e31d1fbff0c599\"," +
//                "\"desenInfoAfterID\":\"1f19b73a3bb77c7a3b3aed6600209a3420e9fb4887937b69f2412177b244a9a3\"," +
//                "\"desenIntention\":\"CUST_ID脱敏,CUST_ADDR脱敏,CUST_AREA_GRADE脱敏,CUST_CONTROL_LEVEL脱敏,CUST_NAME脱敏,CUST_TYPE脱敏,CUST_GROUP脱敏,MOBILE_PHONE脱敏,CERT_TYPE脱敏,CERT_NUM脱敏,FAX脱敏,E_MAIL脱敏,POST_CODE脱敏,CREATE_STAFF脱敏,CREATE_DATE脱敏,UPDATE_STAFF脱敏,UPDATE_DATE脱敏,STATUS_DATE脱敏,STATUS_CD脱敏,REMARK脱敏,\"," +
//                "\"desenRequirements\": \"CUST_ID抑制,CUST_ADDR抑制,CUST_AREA_GRADE随机扰动,CUST_CONTROL_LEVEL随机扰动,CUST_NAME置换,CUST_TYPE随机扰动,CUST_GROUP随机扰动,MOBILE_PHONE抑制,CERT_TYPE抑制,CERT_NUM尾部截断,FAX尾部截断,E_MAIL尾部截断,POST_CODE尾部截断,CREATE_STAFF尾部截断,CREATE_DATE日期添加Laplace噪声,UPDATE_STAFF尾部截断,UPDATE_DATE日期添加Laplace噪声,STATUS_DATE日期添加Laplace噪声,STATUS_CD尾部截断,REMARK尾部截断,\"," +
//                "\"desenControlSet\":\"desencontrolset\"," +
//                "\"desenAlg\":\"16,16,2,2,19,2,2,16,16,11,11,11,11,11,1,11,1,1,11,11,\"," +
//                "\"desenAlgParam\" : \"3,3,3.6,3.6,15,3.6,3.6,3,3,3,3,3,3,3,0.001,3,0.1,0.1,3,3,\","+
//                "\"desenPerformStartTime\" : \"2024-08-08 20:43:33\"," +
//                "\"desenPerformEndTime\" : \"2024-08-08 20:43:46\"," +
//                "\"desenLevel\" : \"0,1,1,1,1,1,1,1,1,1,1,1,1,1,3,1,1,1,1,1,\"," +
//                "\"desenPerformer\":\"脱敏工具集\"," +
//                "\"desenCom\":true," +
//                "\"desenFailedColName\" :\"CUST_CONTROL_LEVEL,CUST_AREA_GRADE,\" ," +
//                "\"desenDeviation\":1,\"desenExtendedcontrol\":5,\"desenInformationloss\":1," +
//                "\"desenUsability\":0,\"desenComplexity\":1,\"desenEffectEvaRet\":false}," +
//                "\"dataType\":13313}";
//
//        ObjectMapper objectMapper = new ObjectMapper();
//        JsonNode evaReceiptJson = objectMapper.readTree(evaReceipt);
//        System.out.println(evaReceiptJson.toPrettyString());
//        JsonNode evaResultJson = objectMapper.readTree(evaResult);
//        System.out.println(evaResultJson.toPrettyString());
//        JsonNode evaResultInvJson = objectMapper.readTree(evaResultInv);
//        System.out.println(evaResultInvJson.toPrettyString());
//        byte[] evaReceiptBytes = new TcpPacket(evaReceipt).buildPacket();
//        byte[] evaResultBytes = new TcpPacket(evaResultJson.toPrettyString()).buildPacket();
//        byte[] evaResultInvBytes = new TcpPacket(evaResultInvJson.toPrettyString()).buildPacket();
//        int port = 10005;
//        ServerSocket serverSocket = new ServerSocket(port);
//
//        int i = 0;
//        while (true) {
//            Socket socket = serverSocket.accept();
//            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
//            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
//            ByteArrayBuilder byteArrayBuilder = new ByteArrayBuilder();
//            int read = 0;
//            readRemoteContent(byteArrayBuilder, dataInputStream);
//
//            if (i % 3 == 0) {
//
//                // 接收tcp
//                // 发送收据
//                System.out.println("第一次发送收据");
//                System.out.println(Arrays.toString(evaReceiptBytes));
//                dataOutputStream.write(evaReceiptBytes);
//                dataOutputStream.flush();
//                // 接收tcp
////            readRemoteContent(byteArrayBuilder, dataInputStream);
////            dataOutputStream.write(evaResultBytes);
//                System.out.println("第一次发送无效结果");
//                System.out.println(Arrays.toString(evaResultInvBytes));
//                dataOutputStream.write(evaResultInvBytes);
//                dataOutputStream.flush();
//            }
//            else if (i % 3 == 1) {
//                // 发送收据
//                System.out.println("第二次发送收据");
//                System.out.println(Arrays.toString(evaReceiptBytes));
//                dataOutputStream.write(evaReceiptBytes);
//                dataOutputStream.flush();
//                // 接收tcp
////            readRemoteContent(byteArrayBuilder, dataInputStream);
////            dataOutputStream.write(evaResultBytes);
//                System.out.println("第二次发送无效结果");
//                System.out.println(Arrays.toString(evaResultInvBytes));
//                dataOutputStream.write(evaResultInvBytes);
//                dataOutputStream.flush();
//            } else {
//                // 发送收据
//                System.out.println("第三次发送收据");
//                System.out.println(Arrays.toString(evaReceiptBytes));
//                dataOutputStream.write(evaReceiptBytes);
//                dataOutputStream.flush();
//                // 接收tcp
////            readRemoteContent(byteArrayBuilder, dataInputStream);
////            dataOutputStream.write(evaResultBytes);
//                System.out.println("第三次发送有效结果");
//                System.out.println(Arrays.toString(evaResultBytes));
//                dataOutputStream.write(evaResultBytes);
//                dataOutputStream.flush();
//            }
//            i++;
//
//        }
//
//    }
//}
package com.lu.gademo.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.util.ByteArrayBuilder;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lu.gademo.entity.ga.effectEva.SendEvaReq;
import com.lu.gademo.model.TcpPacket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class MockEva {

    public static void main(String[] args) throws IOException {
        int port = 10005;
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("服务器启动，监听端口：" + port);
        int counter = 0;

        try {
            while (true) {
                Socket socket = serverSocket.accept();
                new Thread(new ClientHandler(socket, counter)).start();
                counter ++;
            }
        } finally {
            serverSocket.close();
        }
    }

    static class ClientHandler implements Runnable {
        private final Socket socket;
        private final int counter;

        public ClientHandler(Socket socket, int counter) {
            this.socket = socket;
            this.counter = counter;
        }

        @Override
        public void run() {
            try {
                DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());

                // 读取客户端数据
                ByteArrayBuilder byteArrayBuilder = new ByteArrayBuilder();
                Map<String, String> info = readRemoteContent(byteArrayBuilder, dataInputStream);

                // 根据情况发送数据
                byte[] evaReceiptBytes = getReceiptBytes(info);
                byte[] evaResultBytes = getResultBytes(info);
                byte[] evaResultInvBytes = getResultInvBytes(info);

//                int chosenNum = ThreadLocalRandom.current().nextInt(2);
//                if (chosenNum == 0) {
//                sendThirdResponse(dataOutputStream, evaReceiptBytes, evaResultBytes);
//                } else {
                if (counter % 2 == 0) {
                    sendSecondResponse(dataOutputStream, evaReceiptBytes, evaResultInvBytes);
                } else {
                    sendThirdResponse(dataOutputStream, evaReceiptBytes, evaResultBytes);
                }

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private void sendFirstResponse(DataOutputStream dataOutputStream, byte[] evaReceiptBytes, byte[] evaResultInvBytes) throws IOException {
            dataOutputStream.write(evaReceiptBytes);
            dataOutputStream.flush();
            dataOutputStream.write(evaResultInvBytes);
            dataOutputStream.flush();
        }

        private void sendSecondResponse(DataOutputStream dataOutputStream, byte[] evaReceiptBytes, byte[] evaResultInvBytes) throws IOException {
            dataOutputStream.write(evaReceiptBytes);
            dataOutputStream.flush();
            dataOutputStream.write(evaResultInvBytes);
            dataOutputStream.flush();
        }

        private void sendThirdResponse(DataOutputStream dataOutputStream, byte[] evaReceiptBytes, byte[] evaResultBytes) throws IOException {
            dataOutputStream.write(evaReceiptBytes);
            dataOutputStream.flush();
            dataOutputStream.write(evaResultBytes);
            dataOutputStream.flush();
        }
    }

    public static Map<String, String> readRemoteContent(ByteArrayBuilder byteArrayBuilder, DataInputStream dataInputStream) throws IOException {
        Map<String, String> result = new HashMap<>();
        byte[] header = new byte[14];
        dataInputStream.readFully(header);
        int dataLength = dataInputStream.readInt();
        byte[] dataBytes = new byte[dataLength - 34];
        dataInputStream.readFully(dataBytes);
        byteArrayBuilder.write(dataBytes);
        JsonNode jsonNodes = new ObjectMapper().readTree(dataBytes);
        System.out.println(jsonNodes.toPrettyString());
//        SendEvaReq sendEvaReq = new ObjectMapper().treeToValue(jsonNodes.get("data").get("content"), SendEvaReq.class);
//        System.out.println(sendEvaReq.toString());
        List<String> desenInfoPreIden = Arrays.asList(jsonNodes.get("data").get("content").get("desenInfoPreIden").asText().split(","));

        Collections.shuffle(desenInfoPreIden, new Random());
        int chooseNum = new Random().nextInt(desenInfoPreIden.size());
        if (chooseNum <= 1) {
            chooseNum = 2;
        }
        List<String> selectedList = new ArrayList<>(desenInfoPreIden.subList(0, chooseNum));
        selectedList.remove("CUST_ID");
        selectedList.remove("sid");
        selectedList.remove("id");
        // 对于大数据平台的测试
        if (jsonNodes.get("data").get("content").get("fileType").asText().contains("sada")) {
            selectedList = Arrays.asList("f_srcip", "f_dstip");
        }
//        if (selectedList.contains("手机号码")) {
//            selectedList.remove("手机号码");
//        }
        result.put("desenFailedColName", String.join(",", selectedList));
        result.put("desenIntention", jsonNodes.get("data").get("content").get("desenIntention").asText());
        result.put("desenRequirements", jsonNodes.get("data").get("content").get("desenRequirements").asText());
        result.put("fileType", jsonNodes.get("data").get("content").get("fileType").asText());
        result.put("evaRequestId", jsonNodes.get("data").get("content").get("evaRequestId").asText());
        result.put("desenInfoPreId", jsonNodes.get("data").get("content").get("desenInfoPreId").asText());
        result.put("desenInfoAfterId", jsonNodes.get("data").get("content").get("desenInfoAfterId").asText());
        System.out.println("fileType" + jsonNodes.get("data").get("content").get("fileType").asText());
        System.out.println("desenFailedColName" + result.get("desenFailedColName"));
        byte[] auth = new byte[16];
        dataInputStream.readFully(auth);
        return result;
    }

    public static byte[] getReceiptBytes(Map<String, String> info) throws JsonProcessingException, UnsupportedEncodingException {
        // 应该从实际的数据源加载或生成
        String evaReceipt = "";
        Random random = new Random();
        String certificateID = System.currentTimeMillis() + String.valueOf((100 + random.nextInt(900)));
        evaReceipt = "{\"content\":{\"certificateID\":" + "\"" + certificateID + "\"" + "," +
                "\"evaRequestID\":\"" +
                info.get("evaRequestId") +
                "\"," +
                "\"hash\":\"-15322703\"}," +
                "\"dataType\":12593}";
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode evaReceiptJson = objectMapper.readTree(evaReceipt);
        System.out.println(evaReceiptJson.toPrettyString());
        return new TcpPacket(evaReceiptJson.toPrettyString()).buildPacket();
    }

    public static byte[] getResultBytes(Map<String, String> info) throws JsonProcessingException, UnsupportedEncodingException {
        // 应该从实际的数据源加载或生成
        String fileType = info.get("fileType");
        String evaResultID = info.get("evaRequestId");
        String desenInfoPreID = info.get("desenInfoPreId");
        String desenInfoAfterID = info.get("desenInfoAfterId");
        String evaResult = "";
        switch (fileType) {
            case "image":
                evaResult = "{\"content\":" +
                        "{\"evaResultID\":\"" +
                        desenInfoAfterID +
                        "\"," +
                        "\"evaPerformer\":\"脱敏工具集\"," +
                        "\"desenInfoPreID\":\"" +
                        desenInfoPreID +
                        "\"," +
                        "\"desenInfoAfterID\":\"" +
                        desenInfoAfterID +
                        "\"," +
                        "\"desenIntention\":\"对图像脱敏\"," +
                        "\"desenRequirements\": \"对图像脱敏\"," +
                        "\"desenControlSet\":\"desencontrolset\"," +
                        "\"desenAlg\":\"42\"," +
                        "\"desenAlgParam\" : \"5\"," +
                        "\"desenPerformStartTime\" : \"2024-08-16 15:36:11\"," +
                        "\"desenPerformEndTime\" : \"2024-08-16 15:36:15\"," +
                        "\"desenLevel\" : \"1\"," +
                        "\"desenPerformer\":\"脱敏工具集\"," +
                        "\"desenCom\":true," +
                        "\"desenDeviation\":1,\"desenExtendedcontrol\":5,\"desenInformationloss\":1,\"desenUsability\":0," +
                        "\"desenComplexity\":1,\"desenEffectEvaRet\":true,\"desenBGEffectEvaRet\":true},\"dataType\":12594}";
                break;
            case "audio":
                evaResult = "{\"content\":" +
                        "{\"evaResultID\":\"" +
                        desenInfoAfterID +
                        "\"," +
                        "\"evaPerformer\":\"脱敏工具集\"," +
                        "\"desenInfoPreID\":\"" +
                        desenInfoPreID +
                        "\"," +
                        "\"desenInfoAfterID\":\"" +
                        desenInfoAfterID +
                        "\"," +
                        "\"desenIntention\":\"对音频脱敏\"," +
                        "\"desenRequirements\": \"对声纹脱敏\"," +
                        "\"desenControlSet\":\"desencontrolset\"," +
                        "\"desenAlg\":\"70\"," +
                        "\"desenAlgParam\" : \"1.0\"," +
                        "\"desenPerformStartTime\" : \"2024-08-16 15:36:11\"," +
                        "\"desenPerformEndTime\" : \"2024-08-16 15:36:15\"," +
                        "\"desenLevel\" : \"2\"," +
                        "\"desenPerformer\":\"脱敏工具集\"," +
                        "\"desenCom\":true," +
                        "\"desenDeviation\":1,\"desenExtendedcontrol\":5,\"desenInformationloss\":1,\"desenUsability\":0," +
                        "\"desenComplexity\":1,\"desenEffectEvaRet\":true,\"desenBGEffectEvaRet\":true},\"dataType\":12594}";
                break;
            case "video":
                evaResult = "{\"content\":" +
                        "{\"evaResultID\":\"" +
                        desenInfoAfterID +
                        "\"," +
                        "\"evaPerformer\":\"脱敏工具集\"," +
                        "\"desenInfoPreID\":\"" +
                        desenInfoPreID +
                        "\"," +
                        "\"desenInfoAfterID\":\"" +
                        desenInfoAfterID +
                        "\"," +
                        "\"desenIntention\":\"对视频脱敏\"," +
                        "\"desenRequirements\": \"对视频脱敏\"," +
                        "\"desenControlSet\":\"desencontrolset\"," +
                        "\"desenAlg\":\"52\"," +
                        "\"desenAlgParam\" : \"5\"," +
                        "\"desenPerformStartTime\" : \"2024-08-16 15:36:11\"," +
                        "\"desenPerformEndTime\" : \"2024-08-16 15:36:15\"," +
                        "\"desenLevel\" : \"1\"," +
                        "\"desenPerformer\":\"脱敏工具集\"," +
                        "\"desenCom\":true," +
                        "\"desenDeviation\":1,\"desenExtendedcontrol\":5,\"desenInformationloss\":1,\"desenUsability\":0," +
                        "\"desenComplexity\":1,\"desenEffectEvaRet\":true,\"desenBGEffectEvaRet\":true},\"dataType\":12594}";
                break;
            case "text":
                evaResult = "{\"content\":" +
                        "{\"evaResultID\":\"" +
                        desenInfoAfterID +
                        "\"," +
                        "\"evaPerformer\":\"脱敏工具集\"," +
                        "\"desenInfoPreID\":\"" +
                        desenInfoPreID +
                        "\"," +
                        "\"desenInfoAfterID\":\"" +
                        desenInfoAfterID +
                        "\"," + "\"desenIntention\":\"身高脱敏,\"," +
                        "\"desenRequirements\": \"身高添加差分隐私Laplace噪声,\"," +
                        "\"desenControlSet\":\"desencontrolset\"," +
                        "\"desenAlg\":\"3\"," +
                        "\"desenAlgParam\" : \"10\"," +
                        "\"desenPerformStartTime\" : \"2024-08-16 14:37:24\"," +
                        "\"desenPerformEndTime\" : \"2024-08-16 14:37:24\"," +
                        "\"desenLevel\" : \"1\"," +
                        "\"desenPerformer\":\"脱敏工具集\"," +
                        "\"desenCom\":true," +
                        "\"desenDeviation\":1,\"desenExtendedcontrol\":5,\"desenInformationloss\":1,\"desenUsability\":0," +
                        "\"desenComplexity\":1,\"desenEffectEvaRet\":true,\"desenBGEffectEvaRet\":true},\"dataType\":12594}";
                break;
            case "graph":
                evaResult = "{\"content\":" +
                        "{\"evaResultID\":\"" +
                        desenInfoAfterID +
                        "\"," +
                        "\"evaPerformer\":\"脱敏工具集\"," +
                        "\"desenInfoPreID\":\"" +
                        desenInfoPreID +
                        "\"," +
                        "\"desenInfoAfterID\":\"" +
                        desenInfoAfterID +
                        "\"," + "\"desenIntention\":\"对图形脱敏\"," +
                        "\"desenRequirements\": \"对图形脱敏\"," +
                        "\"desenControlSet\":\"desencontrolset\"," +
                        "\"desenAlg\":\"60\"," +
                        "\"desenAlgParam\" : \"5\"," +
                        "\"desenPerformStartTime\" : \"2024-08-16 15:36:11\"," +
                        "\"desenPerformEndTime\" : \"2024-08-16 15:36:15\"," +
                        "\"desenLevel\" : \"1\"," +
                        "\"desenPerformer\":\"脱敏工具集\"," +
                        "\"desenCom\":true," +
                        "\"desenDeviation\":1,\"desenExtendedcontrol\":5,\"desenInformationloss\":1,\"desenUsability\":0," +
                        "\"desenComplexity\":1,\"desenEffectEvaRet\":true,\"desenBGEffectEvaRet\":true},\"dataType\":12594}";
                break;
            default:
                evaResult = "{\"content\":" +
                        "{\"evaResultID\":\"1f19b73a3bb77c7a3b3aed6600209a3420e9fb4887937b69f2412177b244a9a3\"," +
                        "\"evaPerformer\":\"脱敏工具集\"," +
                        "\"desenInfoPreID\":\"b0628b1ed1656d69fce19d04abfda0ac357d2945948563c737e31d1fbff0c599\"," +
                        "\"desenInfoAfterID\":\"1f19b73a3bb77c7a3b3aed6600209a3420e9fb4887937b69f2412177b244a9a3\"," +
                        "\"desenIntention\":\"CUST_ID脱敏,CUST_ADDR脱敏,CUST_AREA_GRADE脱敏,CUST_CONTROL_LEVEL脱敏,CUST_NAME脱敏,CUST_TYPE脱敏,CUST_GROUP脱敏,MOBILE_PHONE脱敏,CERT_TYPE脱敏,CERT_NUM脱敏,FAX脱敏,E_MAIL脱敏,POST_CODE脱敏,CREATE_STAFF脱敏,CREATE_DATE脱敏,UPDATE_STAFF脱敏,UPDATE_DATE脱敏,STATUS_DATE脱敏,STATUS_CD脱敏,REMARK脱敏,\"," +
                        "\"desenRequirements\": \"CUST_ID抑制,CUST_ADDR抑制,CUST_AREA_GRADE随机扰动,CUST_CONTROL_LEVEL随机扰动,CUST_NAME置换,CUST_TYPE随机扰动,CUST_GROUP随机扰动,MOBILE_PHONE抑制,CERT_TYPE抑制,CERT_NUM尾部截断,FAX尾部截断,E_MAIL尾部截断,POST_CODE尾部截断,CREATE_STAFF尾部截断,CREATE_DATE日期添加Laplace噪声,UPDATE_STAFF尾部截断,UPDATE_DATE日期添加Laplace噪声,STATUS_DATE日期添加Laplace噪声,STATUS_CD尾部截断,REMARK尾部截断,\"," +
                        "\"desenControlSet\":\"desencontrolset\"," +
                        "\"desenAlg\":\"16,16,2,2,19,2,2,16,16,11,11,11,11,11,1,11,1,1,11,11,\"," +
                        "\"desenAlgParam\" : \"3,3,3.6,3.6,15,3.6,3.6,3,3,3,3,3,3,3,0.001,3,0.1,0.1,3,3,\"," +
                        "\"desenPerformStartTime\" : \"2024-08-08 20:43:33\"," +
                        "\"desenPerformEndTime\" : \"2024-08-08 20:43:46\"," +
                        "\"desenLevel\" : \"0,1,1,1,1,1,1,1,1,1,1,1,1,1,3,1,1,1,1,1,\"," +
                        "\"desenPerformer\":\"脱敏工具集\"," +
                        "\"desenCom\":true," +
                        "\"desenDeviation\":1,\"desenExtendedcontrol\":5,\"desenInformationloss\":1,\"desenUsability\":0," +
                        "\"desenComplexity\":1,\"desenEffectEvaRet\":true,\"desenBGEffectEvaRet\":true},\"dataType\":12594}";
                break;
        }
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode evaResultJson = objectMapper.readTree(evaResult);
        System.out.println(evaResultJson.toPrettyString());
        return new TcpPacket(evaResultJson.toPrettyString()).buildPacket();
    }

    public static byte[] getResultInvBytes(Map<String, String> info) throws
            JsonProcessingException, UnsupportedEncodingException {
        String fileType = info.get("fileType");
        String evaResultID = info.get("evaRequestId");
        String desenInfoPreID = info.get("desenInfoPreId");
        String desenInfoAfterID = info.get("desenInfoAfterId");
        String evaResultInv = "";

        switch (fileType) {
            case "image":
                evaResultInv = "{\"content\":" +
                        "{\"evaResultID\":\"" +
                        desenInfoAfterID +
                        "\"," +
                        "\"evaPerformer\":\"脱敏工具集\"," +
                        "\"desenInfoPreID\":\"" +
                        desenInfoPreID +
                        "\"," +
                        "\"desenInfoAfterID\":\"" +
                        desenInfoAfterID +
                        "\"," + "\"desenIntention\":\"对图像脱敏\"," +
                        "\"desenRequirements\": \"对图像脱敏\"," +
                        "\"desenControlSet\":\"desencontrolset\"," +
                        "\"desenAlg\":\"42\"," +
                        "\"desenAlgParam\" : \"5\"," +
                        "\"desenPerformStartTime\" : \"2024-08-16 15:36:11\"," +
                        "\"desenPerformEndTime\" : \"2024-08-16 15:36:15\"," +
                        "\"desenLevel\" : \"1\"," +
                        "\"desenPerformer\":\"脱敏工具集\"," +
                        "\"desenCom\":true," +
                        "\"desenFailedColName\" :\"\" ," +
                        "\"desenDeviation\":1,\"desenExtendedcontrol\":5,\"desenInformationloss\":1," +
                        "\"desenUsability\":0,\"desenComplexity\":1,\"desenEffectEvaRet\":false}," +
                        "\"dataType\":13313}";
                break;
            case "audio":
                evaResultInv = "{\"content\":" +
                        "{\"evaResultID\":\"" +
                        desenInfoAfterID +
                        "\"," +
                        "\"evaPerformer\":\"脱敏工具集\"," +
                        "\"desenInfoPreID\":\"" +
                        desenInfoPreID +
                        "\"," +
                        "\"desenInfoAfterID\":\"" +
                        desenInfoAfterID +
                        "\"," + "\"desenIntention\":\"对音频脱敏\"," +
                        "\"desenRequirements\": \"对声纹脱敏\"," +
                        "\"desenControlSet\":\"desencontrolset\"," +
                        "\"desenAlg\":\"70\"," +
                        "\"desenAlgParam\" : \"1.0\"," +
                        "\"desenPerformStartTime\" : \"2024-08-16 15:36:11\"," +
                        "\"desenPerformEndTime\" : \"2024-08-16 15:36:15\"," +
                        "\"desenLevel\" : \"2\"," +
                        "\"desenPerformer\":\"脱敏工具集\"," +
                        "\"desenCom\":true," +
                        "\"desenFailedColName\" :\"\" ," +
                        "\"desenDeviation\":1,\"desenExtendedcontrol\":5,\"desenInformationloss\":1," +
                        "\"desenUsability\":0,\"desenComplexity\":1,\"desenEffectEvaRet\":false}," +
                        "\"dataType\":13313}";
                break;
            case "video":
                evaResultInv = "{\"content\":" +
                        "{\"evaResultID\":\"" +
                        desenInfoAfterID +
                        "\"," +
                        "\"evaPerformer\":\"脱敏工具集\"," +
                        "\"desenInfoPreID\":\"" +
                        desenInfoPreID +
                        "\"," +
                        "\"desenInfoAfterID\":\"" +
                        desenInfoAfterID +
                        "\"," + "\"desenIntention\":\"对视频脱敏\"," +
                        "\"desenRequirements\": \"对视频脱敏\"," +
                        "\"desenControlSet\":\"desencontrolset\"," +
                        "\"desenAlg\":\"52\"," +
                        "\"desenAlgParam\" : \"5\"," +
                        "\"desenPerformStartTime\" : \"2024-08-16 15:36:11\"," +
                        "\"desenPerformEndTime\" : \"2024-08-16 15:36:15\"," +
                        "\"desenLevel\" : \"1\"," +
                        "\"desenPerformer\":\"脱敏工具集\"," +
                        "\"desenCom\":true," +
                        "\"desenFailedColName\" :\"\" ," +
                        "\"desenDeviation\":1,\"desenExtendedcontrol\":5,\"desenInformationloss\":1," +
                        "\"desenUsability\":0,\"desenComplexity\":1,\"desenEffectEvaRet\":false}," +
                        "\"dataType\":13313}";
                break;
            case "text":
                evaResultInv = "{\"content\":" +
                        "{\"evaResultID\":\"" +
                        desenInfoAfterID +
                        "\"," +
                        "\"evaPerformer\":\"脱敏工具集\"," +
                        "\"desenInfoPreID\":\"" +
                        desenInfoPreID +
                        "\"," +
                        "\"desenInfoAfterID\":\"" +
                        desenInfoAfterID +
                        "\"," + "\"desenIntention\":\"身高脱敏,\"," +
                        "\"desenRequirements\": \"身高添加差分隐私Laplace噪声,\"," +
                        "\"desenControlSet\":\"desencontrolset\"," +
                        "\"desenAlg\":\"3\"," +
                        "\"desenAlgParam\" : \"10\"," +
                        "\"desenPerformStartTime\" : \"2024-08-16 14:37:24\"," +
                        "\"desenPerformEndTime\" : \"2024-08-16 14:37:24\"," +
                        "\"desenLevel\" : \"1\"," +
                        "\"desenPerformer\":\"脱敏工具集\"," +
                        "\"desenCom\":true," +
                        "\"desenFailedColName\" :\"\" ," +
                        "\"desenDeviation\":1,\"desenExtendedcontrol\":5,\"desenInformationloss\":1," +
                        "\"desenUsability\":0,\"desenComplexity\":1,\"desenEffectEvaRet\":false}," +
                        "\"dataType\":13313}";
                break;
            case "graph":
                evaResultInv = "{\"content\":" +
                        "{\"evaResultID\":\"" +
                        desenInfoAfterID +
                        "\"," +
                        "\"evaPerformer\":\"脱敏工具集\"," +
                        "\"desenInfoPreID\":\"" +
                        desenInfoPreID +
                        "\"," +
                        "\"desenInfoAfterID\":\"" +
                        desenInfoAfterID +
                        "\"," + "\"desenIntention\":\"对图形脱敏\"," +
                        "\"desenRequirements\": \"对图形脱敏\"," +
                        "\"desenControlSet\":\"desencontrolset\"," +
                        "\"desenAlg\":\"16,16,2,2,19,2,2,16,16,11,11,11,11,11,1,11,1,1,11,11,\"," +
                        "\"desenAlgParam\" : \"3,3,3.6,3.6,15,3.6,3.6,3,3,3,3,3,3,3,0.001,3,0.1,0.1,3,3,\"," +
                        "\"desenPerformStartTime\" : \"2024-08-08 20:43:33\"," +
                        "\"desenPerformEndTime\" : \"2024-08-08 20:43:46\"," +
                        "\"desenLevel\" : \"0,1,1,1,1,1,1,1,1,1,1,1,1,1,3,1,1,1,1,1,\"," +
                        "\"desenPerformer\":\"脱敏工具集\"," +
                        "\"desenCom\":true," +
                        "\"desenFailedColName\" :\"CUST_CONTROL_LEVEL,CUST_AREA_GRADE,\" ," +
                        "\"desenDeviation\":1,\"desenExtendedcontrol\":5,\"desenInformationloss\":1," +
                        "\"desenUsability\":0,\"desenComplexity\":1,\"desenEffectEvaRet\":false}," +
                        "\"dataType\":13313}";
                break;
            default:
                evaResultInv = "{\"content\":" +
                        "{\"evaResultID\":\"" +
                        desenInfoAfterID +
                        "\"," +
                        "\"evaPerformer\":\"脱敏工具集\"," +
                        "\"desenInfoPreID\":\"" +
                        desenInfoPreID +
                        "\"," +
                        "\"desenInfoAfterID\":\"" +
                        desenInfoAfterID +
                        "\"," + "\"desenIntention\":\"CUST_ID脱敏,CUST_ADDR脱敏,CUST_AREA_GRADE脱敏,CUST_CONTROL_LEVEL脱敏,CUST_NAME脱敏,CUST_TYPE脱敏,CUST_GROUP脱敏,MOBILE_PHONE脱敏,CERT_TYPE脱敏,CERT_NUM脱敏,FAX脱敏,E_MAIL脱敏,POST_CODE脱敏,CREATE_STAFF脱敏,CREATE_DATE脱敏,UPDATE_STAFF脱敏,UPDATE_DATE脱敏,STATUS_DATE脱敏,STATUS_CD脱敏,REMARK脱敏,\"," +
                        "\"desenRequirements\": \"CUST_ID抑制,CUST_ADDR抑制,CUST_AREA_GRADE随机扰动,CUST_CONTROL_LEVEL随机扰动,CUST_NAME置换,CUST_TYPE随机扰动,CUST_GROUP随机扰动,MOBILE_PHONE抑制,CERT_TYPE抑制,CERT_NUM尾部截断,FAX尾部截断,E_MAIL尾部截断,POST_CODE尾部截断,CREATE_STAFF尾部截断,CREATE_DATE日期添加Laplace噪声,UPDATE_STAFF尾部截断,UPDATE_DATE日期添加Laplace噪声,STATUS_DATE日期添加Laplace噪声,STATUS_CD尾部截断,REMARK尾部截断,\"," +
                        "\"desenControlSet\":\"desencontrolset\"," +
                        "\"desenAlg\":\"16,16,2,2,19,2,2,16,16,11,11,11,11,11,1,11,1,1,11,11,\"," +
                        "\"desenAlgParam\" : \"3,3,3.6,3.6,15,3.6,3.6,3,3,3,3,3,3,3,0.001,3,0.1,0.1,3,3,\"," +
                        "\"desenPerformStartTime\" : \"2024-08-08 20:43:33\"," +
                        "\"desenPerformEndTime\" : \"2024-08-08 20:43:46\"," +
                        "\"desenLevel\" : \"0,1,1,1,1,1,1,1,1,1,1,1,1,1,3,1,1,1,1,1,\"," +
                        "\"desenPerformer\":\"脱敏工具集\"," +
                        "\"desenCom\":true," +
                        "\"desenFailedColName\" :\"" +
                        info.get("desenFailedColName") +
                        "\" ," +
                        "\"desenDeviation\":1,\"desenExtendedcontrol\":5,\"desenInformationloss\":1," +
                        "\"desenUsability\":0,\"desenComplexity\":1,\"desenEffectEvaRet\":false}," +
                        "\"dataType\":13313}";

        }
        // 应该从实际的数据源加载或生成
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode evaResultInvJson = objectMapper.readTree(evaResultInv);
        System.out.println(evaResultInvJson.toPrettyString());
        return new TcpPacket(evaResultInvJson.toPrettyString()).buildPacket();
    }
}

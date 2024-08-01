package com.lu.gademo.utils;

import com.fasterxml.jackson.core.util.ByteArrayBuilder;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.lu.gademo.model.TcpPacket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class MockEva {
    public static void readRemoteContent(ByteArrayBuilder byteArrayBuilder, DataInputStream dataInputStream) throws IOException {
        byte[] header = new byte[14];
        dataInputStream.read(header);
        int dataLength = dataInputStream.readInt();
        byte[] dataBytes = new byte[dataLength - 34];
        dataInputStream.read(dataBytes);
        String jsonData = new String(dataBytes, StandardCharsets.UTF_8);
        System.out.println(jsonData);
        byte[] auth = new byte[16];
        dataInputStream.read(auth);

    }

    public static void main(String[] args) throws IOException {
        String evaReceipt = "{\"content\":{\"certificateID\":\"1722316999761\",\"evaRequestID\":\"262190e0c2bd5153571413884f8ac6d7a1df7c4441f236bbc6474fb48ab0b75e\",\"hash\":\"-153222702\"},\"dataType\":12593}";
        String evaResult = "{\"content\":{\"evaResultID\":\"89fd77ab0db98098be1526410377c344363a8eed2ed9d3df466448148e99705d\",\"evaPerformer\":\"脱敏工具集\",\"desenInfoPreID\":\"267b6811a737fc5631f5f69be1e5f8ae19e8cc2a3393b0a757b4837bdab219f6\",\"desenInfoAfterID\":\"89fd77ab0db98098be1526410377c344363a8eed2ed9d3df466448148e99705d\",\"desenIntention\":\"用户名脱敏,用户ID脱敏,密码脱敏,姓名脱敏,手机号码脱敏,邮箱脱敏,微信号脱敏,GPS地理位置脱敏,设备品牌脱敏,设备型号脱敏,设备版本脱敏,设备SIM卡号脱敏,IP地址脱敏,MAC地址脱敏,运营商脱敏,学校脱敏,院系脱敏,班级脱敏,年级脱敏,学号脱敏,校园卡消费密码脱敏,消费金额脱敏,消费时间脱敏\",\"desenRequirements\":\"用户名抑制,用户ID抑制,密码置换,姓名抑制,手机号码抑制,邮箱抑制,微信号抑制,GPS地理位置抑制,设备品牌随机扰动,设备型号随机扰动,设备版本随机扰动,设备SIM卡号抑制,IP地址抑制,MAC地址抑制,运营商随机扰动,学校随机扰动,院系随机扰动,班级随机扰动,年级随机扰动,学号抑制,校园卡消费密码置换,消费金额添加差分隐私Laplace噪声,消费时间日期添加Laplace噪声\",\"desenControlSet\":\"desencontrolset\",\"desenAlg\":\"15,16,19,15,16,13,16,14,2,2,2,16,22,16,2,2,2,2,2,16,19,3,1,\",\"desenAlgParam\":\"没有脱敏,没有脱敏,没有脱敏,没有脱敏,没有脱敏,没有脱敏,没有脱敏,没有脱敏,没有脱敏,没有脱敏,没有脱敏,没有脱敏,没有脱敏,没有脱敏,没有脱敏,没有脱敏,没有脱敏,没有脱敏,没有脱敏,没有脱敏,没有脱敏,没有脱敏,没有脱敏,\",\"desenPerformStartTime\":\"2024-07-31 10:31:01\",\"desenPerformEndTime\":\"2024-07-31 10:31:05\",\"desenLevel\":\"0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,\",\"desenPerformer\":\"脱敏工具集\",\"desenCom\":true,\"desenDeviation\":1,\"desenExtendedcontrol\":5,\"desenInformationloss\":1,\"desenUsability\":0,\"desenComplexity\":1,\"desenEffectEvaRet\":true,\"desenBGEffectEvaRet\":true},\"dataType\":12594}";
        String evaResultInv = "{\"content\":{\"evaResultID\":\"89fd77ab0db98098be1526410377c344363a8eed2ed9d3df466448148e99705d\",\"evaPerformer\":\"脱敏工具集\",\"desenInfoPreID\":\"267b6811a737fc5631f5f69be1e5f8ae19e8cc2a3393b0a757b4837bdab219f6\",\"desenInfoAfterID\":\"89fd77ab0db98098be1526410377c344363a8eed2ed9d3df466448148e99705d\",\"desenIntention\":\"用户名脱敏,用户ID脱敏,密码脱敏,姓名脱敏,手机号码脱敏,邮箱脱敏,微信号脱敏,GPS地理位置脱敏,设备品牌脱敏,设备型号脱敏,设备版本脱敏,设备SIM卡号脱敏,IP地址脱敏,MAC地址脱敏,运营商脱敏,学校脱敏,院系脱敏,班级脱敏,年级脱敏,学号脱敏,校园卡消费密码脱敏,消费金额脱敏,消费时间脱敏\",\"desenRequirements\":\"用户名抑制,用户ID抑制,密码置换,姓名抑制,手机号码抑制,邮箱抑制,微信号抑制,GPS地理位置抑制,设备品牌随机扰动,设备型号随机扰动,设备版本随机扰动,设备SIM卡号抑制,IP地址抑制,MAC地址抑制,运营商随机扰动,学校随机扰动,院系随机扰动,班级随机扰动,年级随机扰动,学号抑制,校园卡消费密码置换,消费金额添加差分隐私Laplace噪声,消费时间日期添加Laplace噪声\",\"desenControlSet\":\"desencontrolset\",\"desenAlg\":\"15,16,19,15,16,13,16,14,2,2,2,16,22,16,2,2,2,2,2,16,19,3,1,\",\"desenAlgParam\":\"没有脱敏,没有脱敏,没有脱敏,没有脱敏,没有脱敏,没有脱敏,没有脱敏,没有脱敏,没有脱敏,没有脱敏,没有脱敏,没有脱敏,没有脱敏,没有脱敏,没有脱敏,没有脱敏,没有脱敏,没有脱敏,没有脱敏,没有脱敏,没有脱敏,没有脱敏,没有脱敏,\",\"desenPerformStartTime\":\"2024-07-31 10:31:01\",\"desenPerformEndTime\":\"2024-07-31 10:31:05\",\"desenLevel\":\"0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,\",\"desenPerformer\":\"脱敏工具集\",\"desenCom\":true,\"desenDeviation\":1,\"desenExtendedcontrol\":5,\"desenInformationloss\":1,\"desenUsability\":0,\"desenComplexity\":1,\"desenEffectEvaRet\":false},\"dataType\":13313}";
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(evaReceipt);

        System.out.println(jsonNode.toPrettyString());
        JsonNode evaResultInvJson = objectMapper.readTree(evaResultInv);
        System.out.println(evaResultInvJson.toPrettyString());
        byte[] evaReceiptBytes = new TcpPacket(evaReceipt).buildPacket();

        byte[] evaResultBytes = new TcpPacket(jsonNode.toPrettyString()).buildPacket();
        byte[] evaResultInvBytes = new TcpPacket(evaResultInvJson.toPrettyString()).buildPacket();
        int port = 10005;
        ServerSocket serverSocket = new ServerSocket(port);

        while (true) {
            Socket socket = serverSocket.accept();
            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
            ByteArrayBuilder byteArrayBuilder = new ByteArrayBuilder();
            int read = 0;
            readRemoteContent(byteArrayBuilder, dataInputStream);

            // 发送收据
            System.out.println(Arrays.toString(evaReceiptBytes));
            dataOutputStream.write(evaReceiptBytes);
            dataOutputStream.flush();
            // 接收tcp
//            readRemoteContent(byteArrayBuilder, dataInputStream);
//            dataOutputStream.write(evaResultBytes);
            System.out.println(Arrays.toString(evaResultInvBytes));
            dataOutputStream.write(evaResultInvBytes);
            dataOutputStream.flush();

        }

    }
}

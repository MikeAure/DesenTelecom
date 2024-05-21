package com.lu.gademo.trace.server.gui;

import com.ping.thingsjournalclient.server.ServerConClientThread;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class StatusList {
    // 所有用户的通信socket
    public static Map<Integer, ServerConClientThread> socketListMap = new HashMap<>();
    // 存储矩形区域中的所有司机的socket
    public static Map<Integer, ServerConClientThread> destListMap = new HashMap<>();
    // 存储接单的司机的socket
    public static Map<Integer, ServerConClientThread> driverAquiredMap = new HashMap<>();

    public static int consumerCount = 0;
    public static int driverCount = 0;
    public static boolean noSyncSend = true;

    public static synchronized boolean canSend() {
        if (noSyncSend) {
            noSyncSend = false;
            return true;
        } else {
            return false;
        }
    }

    public static void put(int id, ServerConClientThread scct) {
        int roleId = scct.getUser().getRoleId();
        if (roleId == 1) {
            consumerCount++;
        } else if (roleId == 2) {
            driverCount++;
        }
        socketListMap.put(id, scct);
    }

    public static void remove(int id) {
        int roleId = get(id).getUser().getRoleId();
        if (roleId == 1) {
            consumerCount--;
        } else if (roleId == 2) {
            driverCount--;
        }
        socketListMap.remove(id);
        destListMap.remove(id);
        driverAquiredMap.remove(id);
    }

    public static ServerConClientThread get(int id) {
        if (socketListMap.containsKey(id)) {
            return socketListMap.get(id);
        } else {
            return null;
        }
    }

    /**
     * 获取当前在线的所有车主的ServerConClientThread
     *
     * @return
     */
    public static ArrayList<ServerConClientThread> getOnlineDriver() {
        ArrayList<ServerConClientThread> onlineCCT = new ArrayList<ServerConClientThread>();
        for (ServerConClientThread scct : socketListMap.values()) {
            if (scct.getUser().getRoleId() == 2) {
                //System.out.println("一个司机");
                onlineCCT.add(scct);
            } else continue;
        }
        return onlineCCT;
    }

    /**
     * 获取当前在线的所有打车用户的ServerConClientThread
     *
     * @return
     */
    public static ArrayList<ServerConClientThread> getOnlineUser() {
        ArrayList<ServerConClientThread> onlineCCT = new ArrayList<ServerConClientThread>();
        for (ServerConClientThread scct : socketListMap.values()) {
            if (scct.getUser().getRoleId() == 1) {
                onlineCCT.add(scct);
            } else continue;
        }
        return onlineCCT;
    }

}

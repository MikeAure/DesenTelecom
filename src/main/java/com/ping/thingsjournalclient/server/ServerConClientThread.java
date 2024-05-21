package com.ping.thingsjournalclient.server;

import com.lu.gademo.trace.model.MapData;
import com.lu.gademo.trace.model.MessageType;
import com.lu.gademo.trace.model.TMessage;
import com.lu.gademo.trace.model.TraceUser;
import com.lu.gademo.trace.server.gui.StatusList;
import com.ping.thingsjournalclient.common.QU_AGRQ_P;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class ServerConClientThread extends Thread {

    private final int[][] mapBlockIndex = {{21, 22, 25, 26, 37, 38, 41, 42}, {23, 24, 27, 28, 39, 40, 43, 44},
            {29, 30, 33, 34, 45, 46, 49, 50}, {31, 32, 35, 36, 47, 48, 51, 52}, {53, 54, 57, 58, 69, 70, 73, 74},
            {55, 56, 59, 60, 71, 72, 75, 76}, {61, 62, 65, 66, 77, 78, 81, 82},
            {63, 64, 67, 68, 79, 80, 83, 84}};
    //    private final DbUtil dbUtil = new DbUtil();
    // private final Connection conn = dbUtil.getCon();
    private final QU_AGRQ_P p_func = new QU_AGRQ_P();
    private Socket socket;
    private TraceUser user;

    public ServerConClientThread(Socket socket, TraceUser user) {
        this.socket = socket;
        this.user = user;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public TraceUser getUser() {
        return user;
    }

    public void setUser(TraceUser user) {
        this.user = user;
    }

    /**
     * 对收到的包含位置计算的地图数据进行处理并找到所在块
     *
     * @param mapData 这里在客户端要做一次处理使得按照顺序传送
     * @return 返回的是一个包含具体块号的ArrayList
     */
    public ArrayList<Integer> findPos(MapData mapData) {
        StringBuffer strEncryptedMap = mapData.getEncryptedMap();
        ArrayList<Integer> pos = new ArrayList<>();
        String[] ems = strEncryptedMap.toString().split(";");
        System.out.println(ems[0]);
//        ServerMain.serverGui.appendLog(new StringBuffer(ems[0]));
        // 对于m个A进行计算
        for (int i = 1; i <= ems.length; i++) {
            try {
                if (p_func.QU_AGRQ_P_QRR(ems[i - 1])) {
                    pos.add(i % 84 == 0 ? 84 : i % 84);// 数据库中的地图序号按照84来的，
                }
            } catch (Exception e) {
                System.out.println("地图数据错误！！！");
                e.printStackTrace();
            }
        }
//		if(pos.size()!=1) System.out.println("地图数据错误！！！");
//		System.out.println("数组长度：" + ems.length);

        System.out.println("-------------------");
        boolean inMap = false;
        for (int j = 0; j < pos.size(); j++) {
            if (pos.get(j) < 21) {
                pos.remove(j);
                j--;
            } else {
                inMap = true;
            }
        }

        if (pos.size() == 1) {
            System.out.println("用户：“" + user.getUserName() + "” 所在地图块号:" + pos.get(0));
            String loc = "";
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if (mapBlockIndex[i][j] == pos.get(0)) {
                        loc = j + "" + i;
                    }
                }
            }
//            ServerMain.serverGui.userLocation.put(user, loc);
//            ServerMain.serverGui.appendLog(new StringBuffer("用户：“" + user.getUserName() + "” 所在地图块号:" + pos.get(0)));
        } else
            for (int j = 0; j < pos.size(); j++) {
                System.out.println("外接正方形顶点所在地图块号：" + pos.get(j));
//                ServerMain.serverGui.appendLog(new StringBuffer("外接正方形顶点所在地图块号：" + pos.get(j)));
            }

        if (!inMap) {
            System.out.println("用户：“" + user.getUserName() + "” 当前不在地图中。");
            // ServerMain.serverGui.appendLog(new StringBuffer("用户：“" + user.getUserName() + "” 当前不在地图中。"));
        }

        solve(pos);

        System.out.println("-------------------");

//		System.out.println("块号数量:" + pos.size());
        if (pos.size() == 0) pos.add(-1);
        return pos;
    }

    public void solve(ArrayList<Integer> pos) {
        if (pos.size() == 1) {
            // System.out.println(pos.get(0));
        } else if (pos.size() == 2) {
            int[] p1 = new int[2];
            int[] p2 = new int[2];

            // 找到对应的mapBlockIndex数组中的位置
            p1 = findCoordination(pos.get(0));
            p2 = findCoordination(pos.get(1));

            if (p1[0] == p2[0] && p1[0] < 4) {
                for (int i = 0; i <= p1[0]; i++) {
                    for (int j = p1[1]; j <= p2[1]; j++) {
                        pos.add(mapBlockIndex[i][j]);
                    }
                }
            } else if (p1[0] == p2[0] && p1[0] > 4) {
                for (int i = p1[0]; i < 8; i++) {
                    for (int j = p1[1]; j <= p2[1]; j++) {
                        pos.add(mapBlockIndex[i][j]);
                    }
                }
            } else if (p1[1] == p2[1] && p1[1] < 4) {
                for (int i = p1[0]; i <= p2[0]; i++) {
                    for (int j = 0; j <= p1[1]; j++) {
                        pos.add(mapBlockIndex[i][j]);
                    }
                }
            } else if (p1[1] == p2[1] && p1[1] > 4) {
                for (int i = p1[0]; i <= p2[0]; i++) {
                    for (int j = p1[1]; j < 8; j++) {
                        pos.add(mapBlockIndex[i][j]);
                    }
                }
            }
        } else if (pos.size() == 4) {
            int[] p1 = new int[2];
            int[] p2 = new int[2];
            int[] p3 = new int[2];
            int[] p4 = new int[2];

            p1 = findCoordination(pos.get(0));
            p2 = findCoordination(pos.get(1));
            p3 = findCoordination(pos.get(2));
            p4 = findCoordination(pos.get(3));

            for (int i = p1[0]; i <= p3[0]; i++) {
                for (int j = p1[1]; j <= p3[1]; j++) {
                    pos.add(mapBlockIndex[i][j]);
                }
            }
        }
    }

    public int[] findCoordination(int num) {
        int[] result = new int[2];
        int i = 0;
        int j = 0;
        boolean flag = false;
        for (; i < 8; i++) {
            for (; j < 8; j++) {
                if (mapBlockIndex[i][j] == num) {
                    flag = true;
                    break;
                }
            }
            if (flag)
                break;
            j = 0;
        }
        result[0] = i;
        result[1] = j;
        //System.out.println(i + ":" + j);
        return result;
    }

    /**
     * 找这个ArrayList中的四个块中的所有司机，用destListMap保存司机的id和SCCT
     *
     * @param pos
     */
    public void putDriverInSquare(ArrayList<Integer> pos) {
        ArrayList<ServerConClientThread> onlineCCT = StatusList.getOnlineDriver();
        for (ServerConClientThread scct : onlineCCT) {
            TraceUser tempUser = scct.getUser();
            ArrayList<Integer> posarr = tempUser.getPos();
            // 正常情况下，这里只会有一个数据
            int tempPos = posarr.get(0);
            if (pos.contains(tempPos))
                StatusList.destListMap.put(tempUser.getID(), scct);
        }
        System.out.println("外接正方形范围内的司机数量：" + StatusList.destListMap.size());
//        ServerMain.serverGui.appendLog(new StringBuffer("外接正方形范围内的司机数量：" + StatusList.destListMap.size()));
    }

    /**
     * 将在圆内的消息发送给司机，并从destListMap中移除该司机
     *
     * @param tmsg
     */
    public void sendResponseToCircleDriver(TMessage tmsg) {
        int id = tmsg.getToUser().getID();
        //System.out.println("发送打车回应时的司机id：" + id);
        ServerConClientThread scct = StatusList.destListMap.get(id);
        //System.out.println("发送打车回应时的司机数量：" + StatusList.destListMap.size());
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(scct.socket.getOutputStream());
            oos.writeObject(tmsg);
        } catch (IOException e) {
            System.out.println("发送 在圆形内 消息时读/写对象失败");
//            ServerMain.serverGui.appendLog(new StringBuffer("发送 在圆形内 消息时读/写对象失败"));
            e.printStackTrace();
        }
        System.out.println("在圆内的接单消息成功发送给司机：" + tmsg.getToUser().getUserName());
//        ServerMain.serverGui.appendLog(new StringBuffer("在圆内的接单消息成功发送给司机：" + tmsg.getToUser().getUserName()));
        StatusList.destListMap.remove(id);
    }

    /**
     * 将圆形数据发送给四个块内的司机
     *
     * @param tmsg
     */
    public void sendCircleDataToDrivers(TMessage tmsg) {
        for (ServerConClientThread scct : StatusList.destListMap.values()) {
            ObjectOutputStream oos = null;
            try {
                oos = new ObjectOutputStream(scct.socket.getOutputStream());
                oos.writeObject(tmsg);
            } catch (IOException e) {
                System.out.println("从司机列表中读/写对象失败");
                e.printStackTrace();
            }
        }
    }

    public void sendCircleDataToUsers(TMessage tmsg) {
        ArrayList<ServerConClientThread> userList = StatusList.getOnlineUser();
        ServerConClientThread scct = userList.get(0);  // 目前我们演示的时候只有一个人打车
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(scct.socket.getOutputStream());
            oos.writeObject(tmsg);
            System.out.println("成功将司机计算结果转发给打车用户");
            StatusList.noSyncSend = true;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            System.out.println("向打车用户的输出流中写对象失败");
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (true) {
            ObjectInputStream ois = null;
            try {
                ois = new ObjectInputStream(socket.getInputStream());
                TMessage tmsg = (TMessage) ois.readObject();
                int msgType = tmsg.getMsgType();
                MapData mapData = tmsg.getMapData();
                //System.out.println("消息类型:" + msgType);
                switch (msgType) {
                    case 1:// 如果接收的是将自身位置与加密地图进行计算的数据
                        System.out.println("接收到  “" + user.getUserName() + "” 位置加密过的位置数据：");
                        // ServerMain.serverGui.appendLog(new StringBuffer("接收到  “" + user.getUserName() + "” 位置加密过的位置数据："));
                        ArrayList<Integer> dest = new ArrayList<Integer>();
                        dest = findPos(mapData);
                        this.user.setPos(dest);
                        System.out.println(user.getPos());
                        break;
                    case 2:// 如果接收的是加密的圆的数据
                        TMessage tmsgu2 = new TMessage();// tmessage to u2
                        tmsgu2.setFromUser(user);// 实际上这样设置可能把密码等信息都设置进去，但是先能演示再说
                        tmsgu2.setMapData(mapData);
                        tmsgu2.setMsgType(MessageType.CIRCLE_DATA);
                        sendCircleDataToDrivers(tmsgu2);
                        System.out.println("接收到用户 “" + user.getUserName() + "” 加密的圆形数据：\n" + mapData.getEncryptedMap().toString());
//                        ServerMain.serverGui.appendLog(new StringBuffer(
//                                "接收到用户 “" + user.getUserName() + "” 加密的圆形数据：\n" + mapData.getEncryptedMap().toString()));
                        break;
                    case 3:// 如果接收的是加密的外接正方形的数据,客户端点击打车的时候才会发这个数据，并且在之后发送加密圆的数据
                        ArrayList<Integer> pos = new ArrayList<>();
                        System.out.println("接收到  “" + user.getUserName() + "” 外接正方形顶点加密过的位置数据：");
//                        ServerMain.serverGui
//                                .appendLog(new StringBuffer("接收到  “ " + user.getUserName() + " ” 外接正方形顶点加密过的位置数据："));
                        pos = findPos(mapData);
                        this.user.setFuzzyPos(pos);
                        // 这里取到的块号正常情况有四个，除了第一个，另外三个需要对84取余
                        putDriverInSquare(pos);
                        // 这里用到一个方法将符合条件的车主放到一个map里面。
                        break;
                    case 4:// 如果接收的是司机和圆形数据进行计算之后的数据
                        TMessage tmsgu1 = new TMessage();// tmessage to u2
                        tmsgu1.setMapData(mapData);
                        System.out.println("司机  “" + user.getUserName() + "” 和圆计算的结果：" + mapData.getEncryptedMap().toString());
//                        ServerMain.serverGui.appendLog(new StringBuffer(
//                                "司机  “" + user.getUserName() + "” 和圆计算的结果：" + mapData.getEncryptedMap().toString()));
                        tmsgu1.setFromUser(tmsg.getFromUser());
                        tmsgu1.setMsgType(MessageType.CIRCLE_CALED);
                        while (!StatusList.canSend()) {  // OutputStream 只能同时打开一次，防止多线程在sendCircleDataToUsers同时多次打开报错
                            Thread.sleep(200);
                            System.out.println("wait for another open");
                        }
                        sendCircleDataToUsers(tmsgu1);
                        break;
                    case 5:// 用来处理打车用户在计算圆的结果之后返回给车主的数据，用来判断车主是否在圆圈内5
                        String result = mapData.getEncryptedMap().toString();
                        TraceUser tempUser = tmsg.getToUser();
                        if (result.equals("true")) {
                            System.out.println("用户判断：司机  “ " + tmsg.getToUser().getUserName() + " ” 在圆内，即将转发接单消息。");
//                            ServerMain.serverGui.appendLog(
//                                    new StringBuffer("用户判断：司机  “ " + tmsg.getToUser().getUserName() + " ” 在圆内，即将转发接单消息。"));
                            TMessage tmsgResp = new TMessage();
                            tmsgResp.setFromUser(tmsg.getFromUser());
                            tmsgResp.setToUser(tmsg.getToUser());
                            tmsgResp.setMapData(mapData);
                            tmsgResp.setDestData(tmsg.getDestData());
                            tmsgResp.setMsgType(MessageType.IN_CIRCLE);
                            sendResponseToCircleDriver(tmsgResp);// 如果在圆圈内则发送，不在圆圈内的不发送
                        } else {
                            // System.out.println("司机 “ "+user.getUserName()+" ” 不在圆内，不会转发接单消息。");
//						Server1.serverGui
//								.appendLog(new StringBuffer("司机  “ " + user.getUserName() + " ” 不在圆内，不会转发接单消息。"));
                            StatusList.destListMap.remove(tempUser.getID());
                        }
                        break;
                    case 6:// 用来处理在圆内的司机的应答。
                        TMessage tmsgResp = new TMessage();
                        tmsgResp.setMsgType(MessageType.POSITIVE_RESP);
                        tmsgResp.setFromUser(tmsg.getFromUser());
                        sendCircleDataToUsers(tmsgResp);// 演示的时候只有一个用户打车
                        StatusList.driverAquiredMap.put(tmsgResp.getFromUser().getID(), StatusList.socketListMap.get(tmsgResp.getFromUser().getID()));
                        break;
                    case 7:// 用户给司机发的真实位置
                        TMessage userPosMsg = new TMessage();
                        userPosMsg.setMsgType(MessageType.REAL_USER_POS);
                        userPosMsg.setFromUser(tmsg.getFromUser());
                        userPosMsg.setDestData(tmsg.getDestData());
                        String driverName = tmsg.getToUser().getUserName();
                        sendRealPosToDriver(userPosMsg, driverName);
                        break;
                    case 8:
                        TMessage driverPosMsg = new TMessage();
                        driverPosMsg.setMsgType(MessageType.REAL_DRIVER_POS);
                        driverPosMsg.setFromUser(tmsg.getFromUser());
                        driverPosMsg.setDestData(tmsg.getDestData());
                        TraceUser userReal = tmsg.getToUser();
                        sendRealPosToUser(driverPosMsg, userReal);
                        // *******************************************************
                        break;
                    default:
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("ServerConClientThread线程中从流读取对象中断！");
                System.out.println("用户,id:" + this.user.getID() + ",已经离线");
//                ServerMain.serverGui.appendLog(new StringBuffer("用户,id:" + this.user.getID() + ",已经离线。"));

//                ServerMain.serverGui.userLocation.remove(this.user);
                StatusList.remove(this.user.getID());

                try {
                    if (socket != null)
                        socket.close();
                } catch (IOException e1) {
                    System.out.println("socket closed failed");
                }
                break;
            }
        }
    }

    private void sendRealPosToUser(TMessage driverPosMsg, TraceUser userReal) {
        ServerConClientThread scct = StatusList.socketListMap.get(userReal.getID());
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(scct.socket.getOutputStream());
            oos.writeObject(driverPosMsg);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            System.out.println("司机真实地理位置的输出流中写对象失败");
//            ServerMain.serverGui.appendLog(new StringBuffer("司机真实地理位置的输出流中写对象失败"));
            e.printStackTrace();
        }
    }

    private void sendRealPosToDriver(TMessage userPosMsg, String driverName) {
        ArrayList<ServerConClientThread> onlineCCT = StatusList.getOnlineDriver();
        ServerConClientThread tempScct = null;
        System.out.println("-------------------");
        // 选择特定的司机
        for (ServerConClientThread scct : onlineCCT) {
            TraceUser tempUser = scct.getUser();
            if (tempUser.getUserName().equals(driverName)) {
                tempScct = scct;
                System.out.println("选择的司机名为：" + tempUser.getUserName());
//                ServerMain.serverGui.appendLog(new StringBuffer("选择的司机名为：" + tempUser.getUserName()));
                StatusList.driverAquiredMap.remove(tempUser.getID());
                break;
            }
        }
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(tempScct.socket.getOutputStream());
            oos.writeObject(userPosMsg);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            System.out.println("用户真实地理位置的输出流中写对象失败");
            e.printStackTrace();
        }
        System.out.println("-------------------");
        // 向其余司机发送拒绝信息
        for (ServerConClientThread scct : StatusList.driverAquiredMap.values()) {
            ObjectOutputStream denialOos = null;
            ServerConClientThread denialScct = scct;
            try {
                denialOos = new ObjectOutputStream(denialScct.socket.getOutputStream());
                TMessage denialmsg = new TMessage();
                denialmsg.setMsgType(MessageType.USER_DENIAL);
                denialmsg.setFromUser(userPosMsg.getFromUser());
                denialmsg.setDestData(userPosMsg.getDestData());
                denialOos.writeObject(denialmsg);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                System.out.println("用户拒绝接单的输出流中写对象失败");
                e.printStackTrace();
            }
        }

    }

}

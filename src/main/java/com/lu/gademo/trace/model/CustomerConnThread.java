package com.lu.gademo.trace.model;

//import android.content.Intent;
//import android.util.Log;
//import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import com.lu.gademo.trace.client.user.Customer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;


public class CustomerConnThread extends Thread {
    private Customer customer = null;
    private volatile boolean running = true;

    private static final Logger logger = LogManager.getLogger(CustomerConnThread.class);

//    private final Context context;
    private Socket socket;
    private int roleId;

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public CustomerConnThread(Customer customer, Socket socket, int roleId) {
        super();
        this.customer = customer;
        this.socket = socket;
        this.roleId = roleId;
    }

    public void run() {
//        new Thread(() -> {
//            long timeCount = System.currentTimeMillis();
//            try {
//                while (true) {
//                    if (System.currentTimeMillis() - timeCount > 1000 * 60) {
//                        timeCount = System.currentTimeMillis();
//                        TMessage tmCount = new TMessage();
//                        tmCount.setMsgType(MessageType.BEAT);
//                        new ObjectOutputStream(socket.getOutputStream()).writeObject(tmCount);
//                    }
//                }
//            } catch (IOException e) {
//                try {
//                    if (socket != null) {
//                        socket.close();
//                    }
//
////                        Intent intent = new Intent("notify-to-refresh");
////                        intent.putExtra("message", "SocketFailed");
////                        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
//
//                } catch (IOException e1) {
//                    logger.error("try to close socket" + e1.toString());
//                }
//            }
//        });
        //.start();

        while (!isInterrupted()) {//这里其实可以不用分成两种角色来判断，因为服务器会根据角色来转发信息。
            ObjectInputStream ois = null;
            try {
                ois = new ObjectInputStream(socket.getInputStream());
                TMessage tmsg = (TMessage) ois.readObject();
                int msgType = tmsg.getMsgType();
                logger.info("消息类型" + "" + msgType);
                switch (msgType) {
//                    case 10: {//如果车主收到加密的圆形数据，则应该在这里处理自身与该圆形数据进行计算并返回的过程，但是不一定能提供服务（在圆内）
//                        String[] names = {"message", "TmessageEM"};
//                        String[] values = {"Tmessage", tmsg.getMapData().getEncryptedMap().toString()};
////                        intentStart(context, "notify-to-refresh-driver", names, values);
//                        //这里只能车主的Activity接收
//                        break;
//                    }
                    case 11: { //乘客收到和圆形加密数据计算的结果，在这里处理在圆内的结果
                        String EMData = tmsg.getMapData().getEncryptedMap().toString();
                        String fromUserName = tmsg.getFromUser().getUserName();
                        int fromUserId = Integer.parseInt(tmsg.getFromUser().getID() + "");
                        customer.OnReceivedCalculatedCircleData(EMData, fromUserName, fromUserId);

                        logger.info("司机id" + tmsg.getFromUser().getID());
                        break;
                    }
//                    case 20: {//这里用来处理司机收到圆形加密结果的方法
//                        String[] names = {"message", "toUserId", "fromUserId", "fromUserName", "destination"};
//                        String[] values = {"TmCircleData", tmsg.getToUser().getID() + "", tmsg.getFromUser().getID() + "",
//                                tmsg.getFromUser().getUserName(), tmsg.getDestData()};
////                        intentStart(context, "notify-to-refresh-driver", names, values);
//                        break;
//                    }
                    case 21: {
                        String userName = tmsg.getFromUser().getUserName();
                        int userID = tmsg.getFromUser().getID();
                        customer.driverAccept(userName, userID);
//                        intentStart(context, "notify-to-refresh", names, values);
                        logger.info("意愿司机id" + tmsg.getFromUser().getID());
                        break;
                    }
//                    case 30: {
//                        String[] names = {"message", "userName", "userPos", "userId"};
//                        String[] values = {"UserRealPos", tmsg.getFromUser().getUserName(), tmsg.getDestData(),
//                                tmsg.getFromUser().getID() + ""};
////                        intentStart(context, "notify-to-refresh-driver", names, values);
//                        break;
//                    }
                    case 31: {
                        String userName = tmsg.getFromUser().getUserName();
                        String[] realPos = tmsg.getDestData().split(",");
                        double realLat = Double.parseDouble(realPos[0]);
                        double realLng = Double.parseDouble(realPos[1]);
                        customer.OnReceivedRealDriverPos(userName, realLat, realLng);
                        break;
                    }
//                    case 32: {
//                        String[] names = {"message", "userName"};
//                        String[] values = {"UserDenial", tmsg.getFromUser().getUserName()};
////                        intentStart(context, "notify-to-refresh-driver", names, values);
//                        break;
//                    }
                    default:
                        break;
                }
            } catch (IOException e) {//连接断开时，关闭socket，提示用户重新登录
                logger.error(e.toString());
                try {
                    if (socket != null) {
                        socket.close();
                    }
//                    Intent intent = new Intent("notify-to-refresh");
//                    intent.putExtra("message", "SocketFailed");
//                    LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
//                    Intent intent1 = new Intent("notify-to-refresh-driver");
//                    intent1.putExtra("message", "SocketFailed");
//                    LocalBroadcastManager.getInstance(context).sendBroadcast(intent1);
                    break;
                } catch (IOException e1) {
                    logger.error(e1.toString());
                }
            } catch (ClassNotFoundException e) {
                logger.error(e.toString());
            }
        }

        if (interrupted()) {
            try {
                if (socket != null) {
                    socket.close();
                }
            } catch (IOException e) {
                logger.error(e.toString());
            }
        }

    }

//    public void intentStart(Context context, String action, String[] names, String[] values) {
//        Intent intent = new Intent(action);
//        for (int i = 0; i < names.length; i++) {
//            intent.putExtra(names[i], values[i]);
//        }
//        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
//    }

}

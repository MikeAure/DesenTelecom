package com.lu.gademo.trace.model;

import com.lu.gademo.trace.client.user.Driver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class DriverConnThread extends Thread {
    private Driver driver = null;
    private volatile boolean running = true;

    private static final Logger logger = LogManager.getLogger(DriverConnThread.class);

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

    public DriverConnThread() {
    }

    public DriverConnThread(Driver driver, Socket socket, int roleId) {
        super();
        this.driver = driver;
        this.socket = socket;
        this.roleId = roleId;
    }

    public void run() {
        while (!isInterrupted()) {//这里其实可以不用分成两种角色来判断，因为服务器会根据角色来转发信息。
            ObjectInputStream ois = null;
            try {
                ois = new ObjectInputStream(socket.getInputStream());
                TMessage tmsg = (TMessage) ois.readObject();
                int msgType = tmsg.getMsgType();
                logger.info("消息类型" + "" + msgType);
                switch (msgType) {
                    case 10: {//如果车主收到加密的圆形数据，则应该在这里处理自身与该圆形数据进行计算并返回的过程，但是不一定能提供服务（在圆内）
                        String emMapData = tmsg.getMapData().getEncryptedMap().toString();
                        driver.OnReceivedCircleData(emMapData);
//                        String[] names = {"message", "TmessageEM"};
//                        String[] values = {"Tmessage", tmsg.getMapData().getEncryptedMap().toString()};
//                        intentStart(context, "notify-to-refresh-driver", names, values);
                        //这里只能车主的Activity接收
                        break;
                    }
//                    case 11: { //乘客收到和圆形加密数据计算的结果，在这里处理在圆内的结果
//                        String EMData = tmsg.getMapData().getEncryptedMap().toString();
//                        String fromUserName = tmsg.getFromUser().getUserName();
//                        int fromUserId = Integer.parseInt(tmsg.getFromUser().getID() + "");
//                        customer.OnReceivedCalculatedCircleData(EMData, fromUserName, fromUserId);
//
//                        logger.info("司机id" + tmsg.getFromUser().getID());
//                        break;
//                    }
                    case 20: {//这里用来处理司机收到圆形加密结果的方法
                        int fromUserID = tmsg.getFromUser().getID();
                        String fromUserName = tmsg.getFromUser().getUserName();
                        String destData = tmsg.getDestData();
                        driver.OnReceivedRespCircleData(fromUserID, fromUserName, destData);

//                        String[] names = {"message", "toUserId", "fromUserId", "fromUserName", "destination"};
//                        String[] values = {"TmCircleData", tmsg.getToUser().getID() + "", tmsg.getFromUser().getID() + "",
//                                tmsg.getFromUser().getUserName(), tmsg.getDestData()};
//                        intentStart(context, "notify-to-refresh-driver", names, values);
                        break;
                    }
//                    case 21: {
//                        String userName = tmsg.getFromUser().getUserName();
//                        int userID = tmsg.getFromUser().getID();
//                        customer.driverAccept(userName, userID);
////                        intentStart(context, "notify-to-refresh", names, values);
//                        logger.info("意愿司机id" + tmsg.getFromUser().getID());
//                        break;
//                    }
                    case 30: {
                        String userNameReal = tmsg.getFromUser().getUserName();
                        String[] realPos = tmsg.getDestData().split(",");
                        int userIdReal = Integer.parseInt(tmsg.getFromUser().getID() + "");
                        double realPosLat = Double.parseDouble(realPos[0]);
                        double realPosLng = Double.parseDouble(realPos[1]);
                        driver.OnReceivedRealUserPos(userIdReal, userNameReal, realPosLat, realPosLng);
//                        String[] names = {"message", "userName", "userPos", "userId"};
//                        String[] values = {"UserRealPos", tmsg.getFromUser().getUserName(), tmsg.getDestData(),
//                                tmsg.getFromUser().getID() + ""};
//                        intentStart(context, "notify-to-refresh-driver", names, values);
                        break;
                    }
//                    case 31: {
//                        String userName = tmsg.getFromUser().getUserName();
//                        String[] realPos = tmsg.getDestData().split(",");
//                        double realLat = Double.parseDouble(realPos[0]);
//                        double realLng = Double.parseDouble(realPos[1]);
//                        customer.OnReceivedRealDriverPos(userName, realLat, realLng);
//                        break;
//                    }
                    case 32: {
                        String userNameDenial = tmsg.getFromUser().getUserName();
                        driver.OnReceivedUserDenial(userNameDenial);
//                        String[] names = {"message", "userName"};
//                        String[] values = {"UserDenial", tmsg.getFromUser().getUserName()};
//                        intentStart(context, "notify-to-refresh-driver", names, values);
                        break;
                    }
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

}

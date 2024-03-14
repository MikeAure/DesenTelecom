package com.lu.gademo.trace.client.user;

import com.lu.gademo.trace.client.common.QU_AGRQ_C;
import com.lu.gademo.trace.client.common.QU_AGRQ_P;
import com.lu.gademo.trace.client.common.Vertex;

import com.lu.gademo.trace.client.util.CoordinateConversion;
import com.lu.gademo.trace.client.util.MapUtils;
import com.lu.gademo.trace.model.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

@Component
@Primary
public class Driver {
    TraceUser user = new TraceUser("driver0", "123456", 2);
    public Socket connSocket;
    public boolean loginFlag = false;
    public DriverConnThread connThread;
    public MapUtils mapUtils;
    private static final Logger logger = LogManager.getLogger(Driver.class);

    private double startLatitude = 34.201;
    private double startLongitude = 109.013;

    private final String ADDRESS = "127.0.0.1";//地址
    private final int PORT = 20006;//使用端口

    public Driver() {}
    public Driver(String userName, String password, int roleId, double startLatitude, double startLongitude) {
        user.setUserName(userName);
        user.setPassword(password);
        user.setRoleId(roleId);
        this.startLatitude = startLatitude;
        this.startLongitude = startLongitude;

    }

    public double getStartLatitude() {
        return startLatitude;
    }

    public void setStartLatitude(double startLatitude) {
        this.startLatitude = startLatitude;
    }

    public double getStartLongitude() {
        return startLongitude;
    }

    public void setStartLongitude(double startLongitude) {
        this.startLongitude = startLongitude;
    }
    public void login() throws IOException{
//        if (android.os.Build.VERSION.SDK_INT > 9) {
//            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//            StrictMode.setThreadPolicy(policy);
//        }

//        user.setUserName(userName);
//        user.setPassword(psd);
//        user.setRoleId(roleId);
        user.setMsgType(MessageType.LOGIN);
        logger.info("account: " + user.getUserName());
        logger.info("psd: " + user.getPassword());
        logger.info("role id: " + String.valueOf(user.getRoleId()));
        boolean isLoginSuc = sendLoginInfo(user);
//        Log.e("isLoginSuc",isLoginSuc+"");
        if (isLoginSuc) {
            logger.info(user.getUserName() +"注册成功");
        } else {
            logger.error(user.getUserName() +"注册失败");
            throw new IOException("Login Failed");
        }
    }


    public boolean sendLoginInfo(TraceUser user) {

        connSocket = new Socket();
        logger.info("ip address: " + ADDRESS);

        try {
            connSocket.connect(new InetSocketAddress(ADDRESS, PORT), 60000 * 3);
            connSocket.setSoTimeout(1000 * 60 * 20);
        } catch (UnknownHostException e1) {
            logger.error("Host not found");
            logger.error(e1.getMessage());
            return false;
        } catch (IOException e1) {
            logger.error("IOException");
            logger.error(e1.getMessage());
            return false;
        }

        ObjectOutputStream oos;
        ObjectInputStream ois;

        try {
            oos = new ObjectOutputStream(connSocket.getOutputStream());
            oos.writeObject(user);
            ois = new ObjectInputStream(connSocket.getInputStream());
            TMessage message = (TMessage) ois.readObject();
            //这里没有用tm传回来用户的id，需要设置一下，不然后面取到的id都是0
            if (message.getMsgType() == MessageType.LOGIN_SUC) {
                loginFlag = true;
                user.setID(message.getToUser().getID());
                // 启动监听线程
                connThread = new DriverConnThread(this, connSocket, user.getRoleId());
                connThread.start();

                mapUtils = new MapUtils();//初始化地图数据

                logger.info("sendLoginInfo_id: " + user.getID());

            } else if (message.getMsgType() == MessageType.LOGIN_FAIL) {
                loginFlag = false;
            }

        } catch (Exception e) {
            try {
                if (connSocket != null) connSocket.close();
            } catch (IOException e1) {
                logger.error(e.getMessage());
                logger.error(e1.getMessage());
            }
        }
        return loginFlag;
    }

    public void sendEncryptedMapData(double latitude, double longitude) throws IOException{
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(connSocket.getOutputStream());
            TMessage tMessage = new TMessage();
            tMessage.setMsgType(1);
            MapData mapData = new MapData();
            mapData.setEncryptedMap(encryptedCalculatedData(latitude, longitude));
            mapData.setDataType(MapData.ECPM);
            tMessage.setMapData(mapData);
            oos.writeObject(tMessage);
            logger.info("sendEncryptedMapData: 写入数据完成！");
        } catch (IOException e) {
            logger.error("ccst init: 读取输出流失败！");
            throw new IOException(e);
        }

    }

    public void sendEncryptedData(StringBuffer stringBuffer, int type) {
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(connSocket.getOutputStream());
            TMessage tMessage = new TMessage();
            tMessage.setMsgType(type);
            tMessage.setFromUser(user);
            MapData mapData = new MapData();
            mapData.setEncryptedMap(stringBuffer);

            if (type == 3) mapData.setDataType(MapData.ECSD);
            else if (type == 2) mapData.setDataType(MapData.ECD);//Encrypted Circle Data

            tMessage.setMapData(mapData);
            oos.writeObject(tMessage);
            logger.info("sendED: 写入数据完成！msgType: " + String.valueOf(type));
        } catch (IOException e) {
            logger.error("ED: 读取输出流失败！");
        }
    }

    public StringBuffer encryptedCalculatedData(double latitude, double longitude) {
        CoordinateConversion coor = new CoordinateConversion();
        String coorResult = coor.latLon2UTM(latitude, longitude);
        String[] coorResults = coorResult.split(" ");
        Vertex vertex = new Vertex(Long.parseLong(coorResults[2]), Long.parseLong(coorResults[3]));
        //这个Vertex是自身的位置，然后需要和84个加密的地图数据进行计算，得到一个mapdata的数据然后用Tmessage封装
        String[] mapDatum = MapUtils.mapData;

        StringBuffer stringBuffer = new StringBuffer();
        QU_AGRQ_P qu_agrq_p = new QU_AGRQ_P();

        for (String str : mapDatum) {
            try {
                stringBuffer.append(qu_agrq_p.UF_AGRQ_P_RDC(str, vertex) + ";");
            } catch (Exception e) {
                logger.info("EncryptedCaledData: ", "数据处理出错！");
                e.printStackTrace();
            }
        }

        return stringBuffer;
    }

    // 这里要执行一个将自身位置与加密的圆形数据进行计算的方法。
    public void OnReceivedCircleData(String emMapData) {
        QU_AGRQ_C qu_agrq_c = new QU_AGRQ_C();
        StringBuffer stringBuffer = new StringBuffer(emMapData);
        CoordinateConversion coor = new CoordinateConversion();
        String coorResult = coor.latLon2UTM(startLatitude, startLongitude);
        String[] coorResults = coorResult.split(" ");
        Vertex vertex = new Vertex(Long.parseLong(coorResults[2]), Long.parseLong(coorResults[3]));
        StringBuffer queryMessage = null;
        try {
            queryMessage = new StringBuffer(qu_agrq_c.UF_AGRQ_C_RDC(stringBuffer.toString(), vertex));
        } catch (Exception e) {
            logger.info("OnReceivedCircleData: DriverActivity中数据处理出错！");
            e.printStackTrace();
        }
        sendEncryptedData(queryMessage, 4);
        logger.info("与圆进行计算:计算数据发送完毕！");
    }

    // 这里还有一个判断是当车主位于圆内的时候，客户端通过服务器返回True让车主选择是否同意
    public void OnReceivedRespCircleData(int fromUserId, String fromUserName, String destData) {
        final TraceUser fromUser = new TraceUser();
        fromUser.setID(fromUserId);
        logger.info("用户“" + fromUserName + "”正在打车，目的地：" + destData);
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(connSocket.getOutputStream());
            TMessage ntMessage = new TMessage();
            ntMessage.setMsgType(6);
            ntMessage.setFromUser(user);
            ntMessage.setToUser(fromUser);//寄回给打车用户
            oos.writeObject(ntMessage);
            logger.info("dialog: 写入数据完成！");
        } catch (IOException e) {
            logger.info("dialog: 读取输出流失败！");
        }
        //弹窗用来表达是否愿意接单
//        AlertDialog.Builder dialog = new AlertDialog.Builder(DriverActivity.this);
//        dialog.setTitle("周围的用户");
//
//        dialog.setMessage("用户“" + fromUserName + "”正在打车，目的地：" + destData + ",是否愿意接单？");
//
//
//        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int which) {
//
//                Toast.makeText(getApplicationContext(), "接单信息已发送至服务器。", Toast.LENGTH_LONG).show();
//            }
//        });
//
//        dialog.setNegativeButton("拒绝",
//                new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        Toast.makeText(getApplicationContext(), "您已拒绝对方的下单申请。", Toast.LENGTH_LONG).show();
//                    }
//                }
//        );
//        dialog.setCancelable(false);
//        dialog.show();
    }

    public void OnReceivedRealUserPos(int userIdReal, String userNameReal, double realPosLat, double realPosLng) {//******************
        logger.info("OnReceivedRealUserPos:  收到用户真实位置:" + realPosLat + "," + realPosLng);

        TraceUser user1Real = new TraceUser();
        user1Real.setUserName(userNameReal);
        user1Real.setID(userIdReal);
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(connSocket.getOutputStream());
            TMessage ntMessage = new TMessage();
            ntMessage.setMsgType(8);
            ntMessage.setFromUser(user);
            ntMessage.setToUser(user1Real);//寄回给打车用户
            ntMessage.setDestData(startLatitude + "," + startLongitude);
            oos.writeObject(ntMessage);
            logger.info("dialog: 写入数据完成！");
        } catch (IOException e) {
            logger.info("dialog: 读取输出流失败！");
        }
        double distance = Math.sqrt(Math.pow((realPosLat - startLatitude), 2) + Math.pow((realPosLng - startLongitude), 2));
        logger.info("两人距离： " + String.valueOf(distance));
    }

    public void OnReceivedUserDenial(String userNameDenial) {
        logger.info("用户：" + userNameDenial + " 拒绝了您的接单。");
    }

    public void close() {
        try {
            if (connSocket != null) connSocket.close();
            if (connThread.isAlive() && !connThread.isInterrupted()) connThread.interrupt();

        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

//    public static void main(String[] args) {
//        Driver driver = new Driver();
//        Driver driver1 = new Driver("driver1", "123456", 2, 34.247, 108.946);
//        driver.login();
//        driver1.login();
//        driver.sendEncryptedMapData(driver.startLatitude, driver.startLongitude);
//        driver1.sendEncryptedMapData(driver1.startLatitude, driver1.startLongitude);
//        try {
//            driver.connThread.join();
//            driver1.connThread.join();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}

package com.lu.gademo.trace.client.user;

import com.lu.gademo.trace.client.common.CircleArea;
import com.lu.gademo.trace.client.common.QU_AGRQ_C;
import com.lu.gademo.trace.client.common.QU_AGRQ_P;
import com.lu.gademo.trace.client.common.Vertex;
import com.lu.gademo.trace.client.util.CoordinateConversion;
import com.lu.gademo.trace.client.util.MapUtils;
import com.lu.gademo.trace.model.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class Customer {

    private static final Logger logger = LogManager.getLogger(Customer.class);
    private final String ADDRESS = "127.0.0.1";//地址
    private final int PORT = 20006;//使用端口
    public List<TraceUser> driverList = new ArrayList<TraceUser>();
    public CoordinateConversion coor = new CoordinateConversion();
    //    private final Context context;
    public Socket connSocket;
    public boolean loginFlag = false;
    public CustomerConnThread connThread;
    public MapUtils mapUtils;
    public boolean IS_ORDER_ACCEPTED = false;
    TraceUser user = new TraceUser("user0", "123456", 1);
    private final int radius = 4000;
    private double startLatitude = 34.20;
    private double startLongitude = 109.00;
    private double endLatitude = 34.20;
    private double endLongitude = 109.10;
    public String destInfoStr = "纬度：" + endLatitude + " 经度：" + endLongitude;
    private LatLng latLngLeftTop;
    private LatLng latLngRightTop;
    private LatLng latLngRightBottom;
    private LatLng latLngLeftBottom;
    private CircleArea queryCircle;

    public Customer() {
    }

    public Customer(String userName, String password, int roleId, double startLatitude, double startLongitude, double endLatitude, double endLongitude) {
        user.setUserName(userName);
        user.setPassword(password);
        user.setRoleId(roleId);
        this.startLatitude = startLatitude;
        this.startLongitude = startLongitude;
        this.endLatitude = endLatitude;
        this.endLongitude = endLongitude;
    }

    public Customer(String userName, String password, int roleId) {
        user.setUserName(userName);
        user.setPassword(password);
        user.setRoleId(roleId);
    }

    /**
     * 已知距离，第一个点的坐标和两点的夹角，得到另一个点的坐标
     *
     * @param distance 距离单位为km
     * @param latlngA  第一个点的经纬度
     * @param angle    从正北顺时针开始的角度
     * @return
     */
    public static LatLng getLatlng(float distance, LatLng latlngA, double angle) {
        return new LatLng(latlngA.latitude + (distance * Math.cos(angle * Math.PI / 180)) / 111,
                latlngA.longitude + (distance * Math.sin(angle * Math.PI / 180)) / (111 * Math.cos(latlngA.latitude * Math.PI / 180))
        );
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

    public double getEndLatitude() {
        return endLatitude;
    }

    public void setEndLatitude(double endLatitude) {
        this.endLatitude = endLatitude;
    }

    public double getEndLongitude() {
        return endLongitude;
    }

    public void setEndLongitude(double endLongitude) {
        this.endLongitude = endLongitude;
    }

    public void driverAccept(String driverName, int userId) {
        logger.info("司机: " + driverName + " " + userId + " " + "接单了");
        TraceUser driver = new TraceUser(driverName, "", 2, userId);
        driverList.add(driver);
        IS_ORDER_ACCEPTED = true;
    }

    public void login() throws IOException {
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
        logger.info("role id: " + user.getRoleId());
        boolean isLoginSuc = sendLoginInfo(user);
//        Log.e("isLoginSuc",isLoginSuc+"");
        if (isLoginSuc) {
            logger.info(user.getUserName() + "注册成功");
        } else {
            logger.error(user.getUserName() + "注册失败");
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
                connThread = new CustomerConnThread(this, connSocket, user.getRoleId());
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

    public String sendEncryptedMapData(double latitude, double longitude) {
        ObjectOutputStream oos = null;
        TMessage tMessage = new TMessage();
        try {
            oos = new ObjectOutputStream(connSocket.getOutputStream());
            tMessage.setMsgType(1);
            MapData mapData = new MapData();
            mapData.setEncryptedMap(encryptedCalculatedData(latitude, longitude));
            mapData.setDataType(MapData.ECPM);
            tMessage.setMapData(mapData);
            log.info("用户发送的tMessage: {}", tMessage.getMapData().toString());
            oos.writeObject(tMessage);
            logger.info("sendEncryptedMapData: 写入数据完成！");

        } catch (IOException e) {
            logger.info("ccst init: 读取输出流失败！");
        }
        return tMessage.getMapData().toString();

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
            logger.info("sendED: 写入数据完成！msgType: " + type);
        } catch (IOException e) {
            logger.error("ED: 读取输出流失败！");
        }
    }

    public void sendSquareAndCircleData() {

        StringBuffer stringBuffer = new StringBuffer();

        //以自身为圆点，distance为半径的圆，下方的LatLng值分别为圆的外接正方形与圆的切点坐标
        int distance = radius;
        LatLng latLngTopCenter = getLatlng((float) distance / (float) 1000, new LatLng(startLatitude, startLongitude), 0);
        LatLng latLngRightCenter = getLatlng((float) distance / (float) 1000, new LatLng(startLatitude, startLongitude), 90);
        LatLng latLngBottomCenter = getLatlng((float) distance / (float) 1000, new LatLng(startLatitude, startLongitude), 180);
        LatLng latLngLeftCenter = getLatlng((float) distance / (float) 1000, new LatLng(startLatitude, startLongitude), 270);

        latLngLeftTop = new LatLng(latLngTopCenter.latitude, latLngLeftCenter.longitude);
        latLngRightTop = new LatLng(latLngTopCenter.latitude, latLngRightCenter.longitude);
        latLngRightBottom = new LatLng(latLngBottomCenter.latitude, latLngRightCenter.longitude);
        latLngLeftBottom = new LatLng(latLngBottomCenter.latitude, latLngLeftCenter.longitude);

        // 使用四个线程完成四个不同点的加密，并最后将加密的地图数据发给服务器

        Map<Integer, StringBuffer> encryptedData = new HashMap<>();
        double[][] cornerLatLng = new double[][]{{latLngLeftTop.latitude, latLngLeftTop.longitude},
                {latLngRightTop.latitude, latLngRightTop.longitude},
                {latLngRightBottom.latitude, latLngRightBottom.longitude},
                {latLngLeftBottom.latitude, latLngLeftBottom.longitude}};
        Thread[] threads = new Thread[4];
        for (int i = 0; i < 4; i++) {
            EncryptThread thread = new EncryptThread(encryptedData, i, cornerLatLng[i][0],
                    cornerLatLng[i][1]);
            threads[i] = thread;
            thread.start();
        }
        for (int i = 0; i < 4; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            stringBuffer.append(encryptedData.get(Integer.valueOf(i)));
        }

        sendEncryptedData(stringBuffer, 3);

        logger.info("模糊位置数据发送完毕");

        logger.info("onClick: " +
                distance + "m:" + startLatitude + "," + startLongitude + ";" + latLngTopCenter.latitude + "," + latLngLeftCenter.longitude + ";" +
                +latLngTopCenter.latitude + "," + latLngRightCenter.longitude + ";"
                + latLngBottomCenter.latitude + "," + latLngRightCenter.longitude + ";"
                + latLngBottomCenter.latitude + "," + latLngLeftCenter.longitude);
        //****************下面为发送的圆形数据，通过服务器发送给所有范围内的车主
        StringBuffer stringBufferCircle = encryptedCircleData(distance, startLatitude, startLongitude);
        sendEncryptedData(stringBufferCircle, 2);
    }

    public StringBuffer encryptedCircleData(int distance, double lat, double lng) {
        String[] posString = coor.latLon2UTM(lat, lng).split(" ");
        queryCircle = new CircleArea(distance, Long.parseLong(posString[2]), Long.parseLong(posString[3]));
        QU_AGRQ_C qu_agrq_c = new QU_AGRQ_C();
        String queryMessage = qu_agrq_c.QU_AGRQ_C_QDC(queryCircle);
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(queryMessage);
        return stringBuffer;
    }

    public StringBuffer encryptedCalculatedData(double latitude, double longitude) {
        CoordinateConversion coor = new CoordinateConversion();
        String coorResult = coor.latLon2UTM(latitude, longitude);
        String[] coorResults = coorResult.split(" ");
        Vertex vertex = new Vertex(Long.parseLong(coorResults[2]), Long.parseLong(coorResults[3]));
        //这个Vertex是自身的位置，然后需要和84个加密的地图数据进行计算，得到一个mapdata的数据然后用Tmessage封装
        String[] mapDatas = MapUtils.mapData;

        StringBuffer stringBuffer = new StringBuffer();
        QU_AGRQ_P qu_agrq_p = new QU_AGRQ_P();

        for (String str : mapDatas) {
            try {
                stringBuffer.append(qu_agrq_p.UF_AGRQ_P_RDC(str, vertex) + ";");
            } catch (Exception e) {
                logger.info("EncryptedCaledData: ", "数据处理出错！");
                logger.error(e.getMessage());
            }
        }

        return stringBuffer;
    }

    /*
     * 判断司机是否在圆内
     */
    public synchronized void OnReceivedCalculatedCircleData(String EMData, String fromUserName, int fromUserId) {
        StringBuffer stringBuffer = new StringBuffer(EMData);
        QU_AGRQ_C qu_agrq_c = new QU_AGRQ_C();
        boolean result = false;
        try {
            result = qu_agrq_c.QU_AGRQ_P_QRR(stringBuffer.toString(), queryCircle);
            logger.info("收到与圆计算的结果:" + stringBuffer);
            logger.info("解密圆计算的结果: " + result);
        } catch (Exception e) {
            logger.error("用户圆形范围车辆判断: 数据处理出错！");
            logger.error(e.getMessage());
        }
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(connSocket.getOutputStream());
            TMessage respMsg = new TMessage();
            respMsg.setMsgType(5);
            respMsg.setDestData(destInfoStr);
            respMsg.setFromUser(user);
            TraceUser fromUser = new TraceUser();
            fromUser.setID(fromUserId);
            fromUser.setUserName(fromUserName);
            respMsg.setToUser(fromUser);
            MapData mapData = new MapData();
            if (result) mapData.setEncryptedMap(new StringBuffer("true"));
            else mapData.setEncryptedMap(new StringBuffer("false"));
            respMsg.setMapData(mapData);
            oos.writeObject(respMsg);
        } catch (IOException e) {
            logger.error("ED: 读取输出流失败！");
            logger.error(e.getMessage());
        }
    }

    public void OnReceivedRealDriverPos(String driverNameReal, double realPosLat, double realPosLng) {
        double distance = Math.sqrt(Math.pow((realPosLat - startLatitude), 2) + Math.pow((realPosLng - startLongitude), 2));
        logger.info("司机名称" + driverNameReal + "两人距离： " + distance);
    }

    public void close() {
        try {
            if (connSocket != null) connSocket.close();
            if (connThread.isAlive() && !connThread.isInterrupted()) connThread.interrupt();
            if (!driverList.isEmpty()) driverList.clear();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

//    public static void main(String[] args) {
////        System.out.println(Paths.get(".").toAbsolutePath());
//        Customer customer = new Customer();
//
//        customer.login();
//
//        customer.sendEncryptedMapData(customer.startLatitude, customer.startLongitude);
//        customer.sendSquareAndCircleData();
//
//        try {
//            customer.connThread.join();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
////        customer.close();
////        customer.connThread.interrupt();
//    }

    class EncryptThread extends Thread {
        int index;
        double latitude;
        double longitude;
        Map<Integer, StringBuffer> resultMap;

        EncryptThread(Map<Integer, StringBuffer> resultMap, int index, double latitude, double longitude) {
            this.resultMap = resultMap;
            this.index = index;
            this.latitude = latitude;
            this.longitude = longitude;
        }

        @Override
        public void run() {
            resultMap.put(this.index, encryptedCalculatedData(this.latitude, this.longitude));
        }
    }

}

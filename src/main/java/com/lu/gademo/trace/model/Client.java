package com.lu.gademo.trace.model;

//import android.content.Context;
//import android.util.Log;
//

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.Socket;

public class Client {

    private static final Logger logger = LogManager.getLogger(Client.class);
    private final String ADDRESS = "127.0.0.1";//地址
    private final int PORT = 20006;//使用端口
    private final Context context;
    public Socket s;

    public Client(Context context) {
        this.context = context;
    }

    /**
     * 处理登录请求
     *
     * @param user
     * @return
     */
//    public boolean sendLoginInfo(TraceUser user) {
//        boolean loginFlag = false;
//        s = new Socket();
//        logger.error("ip address: " + ADDRESS);
//
//        try {
//            s.connect(new InetSocketAddress(ADDRESS, PORT), 60000 * 3);
//            s.setSoTimeout(1000 * 60 * 20);
//        } catch (UnknownHostException e1) {
//            System.out.println("host not find");
//            return false;
//        } catch (IOException e1) {
//            System.out.println("IOException");
//            e1.printStackTrace();
//            return false;
//        }
//
//        ObjectOutputStream oos;
//        ObjectInputStream ois;
//        try {
//            oos = new ObjectOutputStream(s.getOutputStream());
//            oos.writeObject(user);
//            ois = new ObjectInputStream(s.getInputStream());
//            TMessage tm = (TMessage) ois.readObject();
//            //这里没有用tm传回来用户的id，需要设置一下，不然后面取到的id都是0
//            if (tm.getMsgType() == MessageType.LOGIN_SUC) {
//                loginFlag = true;
//                ClientConnThread ccst = new ClientConnThread(context, s, user.getRoleId());
//                ccst.start();
//                MapUtils mapUtils = new MapUtils(context);//初始化地图数据
//                MyActivityManager.user = tm.getToUser();
//                logger.error("sendLoginInfo_id: " + MyActivityManager.user.getID());
//                MyActivityManager.curClientThread = ccst;
//            } else if (tm.getMsgType() == MessageType.LOGIN_FAIL) loginFlag = false;
//
//        } catch (Exception e) {
//            try {
//                if (s != null) s.close();
//            } catch (IOException e1) {
//                e.printStackTrace();
//            }
//        }
//        return loginFlag;
//    }

}

package com.lu.gademo.trace.server.gui;

import com.lu.gademo.trace.client.user.Customer;
import com.lu.gademo.trace.model.MessageType;
import com.lu.gademo.trace.model.TMessage;
import com.lu.gademo.trace.model.TraceUser;
import com.ping.thingsjournalclient.dao.UserDao;
import com.ping.thingsjournalclient.server.ServerConClientThread;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
public class ServerMain {

    class DataProcessThread implements Runnable {
        private final Socket socket;
        private ObjectInputStream ois;
        private ObjectOutputStream oos;
        private TraceUser user;
        private UserDao userDao;
        private final Logger logger = LogManager.getLogger(DataProcessThread.class);
        public DataProcessThread(Socket socket) {
            super();
            this.socket = socket;
        }

        /**
         * 预处理
         */
        public void preprocess() {
            try {
                ois = new ObjectInputStream(this.socket.getInputStream());
                oos = new ObjectOutputStream(this.socket.getOutputStream());
                user = (TraceUser) ois.readObject();
                userDao = new UserDao();
            } catch (ClassNotFoundException e) {
                logger.error("输入流找不到对象！");
                logger.error(e.getMessage());
            } catch (IOException e) {
                logger.error(e.getMessage());
            }
        }

        @Override
        public void run() {
            this.preprocess();
            if (user.getMsgType() == MessageType.LOGIN) {
                // roleId：1 乘客，2 司机
                TraceUser userLogged = userDao.UserLog(user.getUserName(), user.getPassword(), user.getRoleId());
                TMessage tMessage = new TMessage();
                if (userLogged != null) {// 登录成功, userLogged
                    logger.info("用户：" + user.getUserName() + "成功登录！");
                    // serverGui.appendLog(new StringBuffer("用户：" + user.getUserName() + "成功登录！"));
                    logger.info("用户ID：" + userLogged.getID());
                    ServerConClientThread scct = new ServerConClientThread(this.socket, userLogged);
                    StatusList.put(userLogged.getID(), scct);
                    scct.start();
                    tMessage.setMsgType(MessageType.LOGIN_SUC);
                    tMessage.setToUser(userLogged);
                } else {// 登录失败
                    tMessage.setMsgType(MessageType.LOGIN_FAIL);
                }
                try {
                    oos.writeObject(tMessage);
                } catch (IOException e) {
                    logger.error("登录反馈消息写入失败！");
                    logger.error(e.getMessage());
                }
            } else {// 如果消息类型为别的,暂时没别的，主动抛出异常
                logger.error(user.getUserName());
                logger.error(user.getPassword());
                logger.error(user.getRoleId());
                logger.error(user.getMsgType());
                throw new RuntimeException("未知消息内容！");
            }
        }

    }
    private static final Logger logger = LogManager.getLogger(Customer.class);
    public Thread ownThread;
    private ServerSocket listenSocket;
    private Socket acceptSocket;
    private final static int SERVER_PORT = 20006;

    public void start() {
        ownThread = new Thread(() -> {  // 用来开启端口监听，以及读取一张加密地图的数据
            try {
                listenSocket = new ServerSocket(SERVER_PORT);

                InputStream mapDataStream = getClass().getClassLoader().getResourceAsStream("MapData.txt");
                if (mapDataStream == null) {
                    throw new FileNotFoundException("MapData.txt not found");
                }
                BufferedReader bufferReader = new BufferedReader(new InputStreamReader(mapDataStream));
                StringBuilder singleMap = new StringBuilder();
                int data;
                while ((data = bufferReader.read()) != ';') {
                    singleMap.append((char) data);
                }
                while (!ownThread.isInterrupted()) {
                    acceptSocket = listenSocket.accept();
                    acceptSocket.setSoTimeout(1000 * 60 * 20);
                    new Thread(new DataProcessThread(acceptSocket)).start();
                }
            } catch (IOException e1) {
                logger.error(e1.getMessage());
            }
        });
        try {
            ownThread.start();
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        logger.info("服务器已启动，端口：" + SERVER_PORT);
    }

    public void stop() {
        try {
            if (listenSocket != null && !listenSocket.isClosed()) listenSocket.close();
            if (acceptSocket != null && !acceptSocket.isClosed()) acceptSocket.close();
            if (ownThread != null && ownThread.isAlive() && !ownThread.isInterrupted()) ownThread.interrupt();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        logger.info("服务器已关闭，端口：" + SERVER_PORT);
    }

    public static void main(String[] args) {
        ServerMain server = new ServerMain();
        server.ownThread.start();

    }
}
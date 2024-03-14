package com.ping.thingsjournalclient.util;

import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbUtil {

    public DbUtil() {

    }

    public Connection getCon() {
        try {
            Class.forName(PropertiesUtil.getValue("jdbcName"));
        } catch (ClassNotFoundException | FileNotFoundException e) {
            // TODO Auto-generated catch block
            System.out.println("数据库驱动加载失败！");
            e.printStackTrace();
        }
        Connection con = null;
        try {
            con = DriverManager.getConnection(PropertiesUtil.getValue("dbUrl"), PropertiesUtil.getValue("dbUserName"), PropertiesUtil.getValue("dbPassword"));
        } catch (FileNotFoundException | SQLException e) {
            // TODO Auto-generated catch block
            System.out.println("数据库连接失败！");
            e.printStackTrace();
        }
        return con;
    }

    public void closeCon(Connection con) throws Exception {
        if (con != null) {
            con.close();
        }
    }

    public static void main(String[] args) {
        DbUtil dbUtil = new DbUtil();
        try {
            dbUtil.getCon();
            System.out.println("connected success");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("connected failed");
        }
    }
}

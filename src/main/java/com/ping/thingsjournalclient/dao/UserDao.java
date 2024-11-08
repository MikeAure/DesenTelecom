package com.ping.thingsjournalclient.dao;

import com.lu.gademo.trace.model.TraceUser;
import com.ping.thingsjournalclient.util.DbUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDao {

    public DbUtil dbu = new DbUtil();
    public UserDao() {

    }

    public TraceUser UserLog(String username, String psd, int roleId) {
        TraceUser resultUser = null;
        String sql = "select * from t_user where username=? and password=? and role_id =?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = dbu.getCon();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.setString(2, psd);
            pstmt.setInt(3, roleId);
            rs = pstmt.executeQuery();
        } catch (Exception e) {
            System.out.println("Dao层中获取数据库链接失败！");
            e.printStackTrace();
        }

        try {
            if (rs.next()) {
                resultUser = new TraceUser();
                resultUser.setID(rs.getInt("user_id"));
                resultUser.setUserName(rs.getString("username"));
                resultUser.setRoleId(rs.getInt("role_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Dao层中执行sql语句失败！");
        }
        return resultUser;
    }


}

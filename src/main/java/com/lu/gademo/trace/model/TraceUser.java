package com.lu.gademo.trace.model;

import java.io.Serializable;
import java.util.ArrayList;


public class TraceUser implements Serializable {

    private static final long serialVersionUID = -3069119706991213460L;
    private int ID;//用户ID
    private String userName;//用户名，登录用
    private String password;//密码，登录用
    private int roleId;//角色信息，打车 1 还是车主 2

    /**
     * 用来标记登录与成功信息类别，【登录信息？地图数据？……】等用登录后新线程中的TMsg保存
     */
    private int msgType;
    /**
     * 具体地址，也就是处于哪个块
     */
    private ArrayList<Integer> pos;
    /**
     * 模糊地址，也就是外接正方形的四个点所在的块
     */
    private ArrayList<Integer> fuzzyPos;

    public TraceUser() {
    }

    public TraceUser(String userName, String password, int roleId, int ID) {
        this.userName = userName;
        this.password = password;
        this.roleId = roleId;
        this.ID = ID;
    }
    public TraceUser(String userName, String password, int roleId) {
        this.userName = userName;
        this.password = password;
        this.roleId = roleId;
    }

    public ArrayList<Integer> getFuzzyPos() {
        return fuzzyPos;
    }

    public void setFuzzyPos(ArrayList<Integer> fuzzyPos) {
        this.fuzzyPos = fuzzyPos;
    }

    public ArrayList<Integer> getPos() {
        return pos;
    }

    public void setPos(ArrayList<Integer> pos) {
        this.pos = pos;
    }

    public int getID() {
        return ID;
    }

    public void setID(int iD) {
        ID = iD;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public int getMsgType() {
        return msgType;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }

}

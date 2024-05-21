package com.lu.gademo.trace.model;

import java.io.Serializable;

@SuppressWarnings("serial")
public class TMessage implements Serializable {
    private int msgType;
    private MapData mapData;
    private String destData;
    private TraceUser fromUser;
    private TraceUser toUser;

    public TMessage() {

    }

    public TMessage(int msgType) {
        this.msgType = msgType;
    }

    public String getDestData() {
        return destData;
    }

    public void setDestData(String destData) {
        this.destData = destData;
    }

    public TraceUser getToUser() {
        return toUser;
    }

    public void setToUser(TraceUser toUser) {
        this.toUser = toUser;
    }

    public TraceUser getFromUser() {
        return fromUser;
    }

    public void setFromUser(TraceUser fromUser) {
        this.fromUser = fromUser;
    }

    public int getMsgType() {
        return msgType;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }

    public MapData getMapData() {
        return mapData;
    }

    public void setMapData(MapData mapData) {
        this.mapData = mapData;
    }

}

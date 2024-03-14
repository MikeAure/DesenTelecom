package com.lu.gademo.trace.client.common;

public class Vertex {

    private long x;
    private long y;

    /**
     * 坐标类
     *
     * @param x
     * @param y
     */
    public Vertex(long x, long y) {
        this.x = x;
        this.y = y;
    }

    Vertex() {

    }

    public long getX() {
        return x;
    }

    public void setX(long x) {
        this.x = x;
    }

    public long getY() {
        return y;
    }

    public void setY(long y) {
        this.y = y;
    }
}

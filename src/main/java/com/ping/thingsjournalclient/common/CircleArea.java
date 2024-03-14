package com.ping.thingsjournalclient.common;

public class CircleArea {

    private long radius;//半径
    private long center_x;//圆心x坐标
    private long center_y;//圆心y坐标

    /**
     * 生成一个圆形区域
     *
     * @param radius   半径
     * @param center_x 圆心x坐标
     * @param center_y 圆心y坐标
     */

    public CircleArea(long radius, long center_x, long center_y) {
        super();
        this.radius = radius;
        this.center_x = center_x;
        this.center_y = center_y;
    }

    public long getRadius() {
        return radius;
    }

    public void setRadius(long radius) {
        this.radius = radius;
    }

    public long getCenter_x() {
        return center_x;
    }

    public void setCenter_x(long center_x) {
        this.center_x = center_x;
    }

    public long getCenter_y() {
        return center_y;
    }

    public void setCenter_y(long center_y) {
        this.center_y = center_y;
    }
}

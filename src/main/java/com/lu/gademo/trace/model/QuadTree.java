package com.lu.gademo.trace.model;

public class QuadTree {

    public QuadTree firstChild = null;
    public QuadTree secondChild = null;
    public QuadTree thirdChild = null;
    public QuadTree fourthChild = null;

    private String mapData;
    private int depth;
    private boolean isRead = false;

    public boolean isAllChildrenRead() {
        return this.firstChild.isRead && this.secondChild.isRead && this.thirdChild.isRead && this.fourthChild.isRead;
    }

    public boolean isAllChildrenSet() {
        return this.firstChild != null && this.secondChild != null && this.thirdChild != null && this.fourthChild != null;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public QuadTree getFirstChild() {
        return firstChild;
    }

    public void setFirstChild(QuadTree firstChild) {
        this.firstChild = firstChild;
    }

    public QuadTree getSecondChild() {
        return secondChild;
    }

    public void setSecondChild(QuadTree secondChild) {
        this.secondChild = secondChild;
    }

    public QuadTree getThirdChild() {
        return thirdChild;
    }

    public void setThirdChild(QuadTree thirdChild) {
        this.thirdChild = thirdChild;
    }

    public QuadTree getFourthChild() {
        return fourthChild;
    }

    public void setFourthChild(QuadTree fourthChild) {
        this.fourthChild = fourthChild;
    }

    public String getMapData() {
        return mapData;
    }

    public void setMapData(String mapData) {
        this.mapData = mapData;
    }

    public QuadTree() {

    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean isRead) {
        this.isRead = isRead;
    }

}

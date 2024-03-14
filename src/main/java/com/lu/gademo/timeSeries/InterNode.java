package com.lu.gademo.timeSeries;

public class InterNode extends Node{
	
	public int c;
	public int val;
	public Node left;
	public Node right;
	
	
	public InterNode(int c, int val, Node left, Node right, boolean isLeafNode) {
		super();
		this.c = c;
		this.val = val;
		this.left = left;
		this.right = right;
		this.isLeafNode = isLeafNode;
	}
	
	

}

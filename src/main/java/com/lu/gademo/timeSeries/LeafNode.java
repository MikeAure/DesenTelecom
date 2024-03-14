package com.lu.gademo.timeSeries;

import java.util.Set;


public class LeafNode extends Node {

	public Set<Integer> IDSet;

	public LeafNode(Set<Integer> IDSet, boolean isLeafNode) {
		
		this.IDSet = IDSet;
		this.isLeafNode = isLeafNode;
		
	}
	
}

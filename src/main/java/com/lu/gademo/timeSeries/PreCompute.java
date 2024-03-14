package com.lu.gademo.timeSeries;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


public class PreCompute {
	
	public static HashMap<Integer, ArrayList<Integer>> ChoosePivots(HashMap<Integer, ArrayList<Integer>> intDataList, int k) {
		
		Set<Integer> pivotsID = new HashSet<Integer>();
		int n = intDataList.size();
		
		while(pivotsID.size() < k) {
			int loc = (int)(Math.random()*n);
			pivotsID.add(loc);
		}
		
		HashMap<Integer, ArrayList<Integer>> pivotsList = new HashMap<Integer, ArrayList<Integer>>();
		int i = 0;
		for (Iterator iterator = pivotsID.iterator(); iterator.hasNext();) {
			int loc = (int) iterator.next();
			pivotsList.put(i, intDataList.get(loc));
			i++;
		}
		
		return pivotsList;
		
	}
	
	public static HashMap<Integer, int[]> PreComputeDistance(HashMap<Integer, ArrayList<Integer>> intDataList, HashMap<Integer, ArrayList<Integer>> pivotsList, int lamda) {
		
		int k = pivotsList.size();
		int n = intDataList.size();
		
		HashMap<Integer, int[]> distanceList = new HashMap<Integer, int[]>(); 
		
		for(int i = 0; i < n; i++) {
			int[] vector = new int[k];
			for(int j = 0; j < k; j++) {
				int distance = TWED.TWEDComputePlain(intDataList.get(i), pivotsList.get(j), lamda);
				vector[j] = distance;
			}
			distanceList.put(i, vector);
		}
		
		return distanceList; 
	}
}

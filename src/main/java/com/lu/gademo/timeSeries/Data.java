package com.lu.gademo.timeSeries;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Data {

    public static HashMap<Integer, ArrayList<Double>> ReadFixNumData(String filename, String splitString, int n, int l) throws Exception {

        HashMap<Integer, ArrayList<Double>> dataList = new HashMap<Integer, ArrayList<Double>>();

        String line = null;
        BufferedReader in = new BufferedReader(new FileReader(filename));

        int count = 0;
        while (count < n && (line = in.readLine()) != null) {
            line = line.trim();
            String[] tempStrings = line.split(splitString);
            ArrayList<Double> tempDouble = new ArrayList<Double>();

            for (int i = 1; i < l; i++) {
                tempDouble.add(new Double(tempStrings[i]));
            }

            dataList.put(count, tempDouble);
            count++;

        }

        return dataList;

    }

    public static HashMap<Integer, ArrayList<Double>> ReadAllData(String filename, String splitString) throws Exception {

        HashMap<Integer, ArrayList<Double>> dataList = new HashMap<Integer, ArrayList<Double>>();

        String line = null;
        BufferedReader in = new BufferedReader(new FileReader(filename));

        int count = 0;

        while ((line = in.readLine()) != null) {
            line = line.trim();
            String[] tempStrings = line.split(splitString);
            int l = tempStrings.length;
            ArrayList<Double> tempDouble = new ArrayList<Double>();

            for (int i = 1; i < l; i++) {
                tempDouble.add(new Double(tempStrings[i]));
            }

            dataList.put(count, tempDouble);
            count++;
        }

        return dataList;
    }

    public static HashMap<Integer, ArrayList<Integer>> TransformDoubleToInt(HashMap<Integer, ArrayList<Double>> doubleDataList, int magnification) {

        HashMap<Integer, ArrayList<Integer>> intDataList = new HashMap<Integer, ArrayList<Integer>>();

        for (int i = 0; i < doubleDataList.size(); i++) {
            ArrayList<Integer> tempData = new ArrayList<Integer>();
            for (int j = 0; j < doubleDataList.get(i).size(); j++) {
                int val = (int) (doubleDataList.get(i).get(j) * magnification);
                tempData.add(val);
            }

            intDataList.put(i, tempData);
        }
        return intDataList;

    }


    public static void main(String[] args) throws Exception {

        String filename = "src/ElectricDevices_TEST.tsv";
        String splitString = "\\s+";
        HashMap<Integer, ArrayList<Double>> dataList = Data.ReadAllData(filename, splitString);
        HashMap<Integer, ArrayList<Integer>> intDataList = Data.TransformDoubleToInt(dataList, 100);

        System.out.println(dataList.size());
        System.out.println(dataList.get(0).size());


        for (int j = 0; j < intDataList.size(); j++) {

            ArrayList<Integer> pivot = intDataList.get(j);

            int[] distanceList = new int[intDataList.size()];

            for (int i = 0; i < dataList.size(); i++) {
                distanceList[i] = TWED.TWEDComputePlain(intDataList.get(i), pivot, 0);
            }

            Arrays.sort(distanceList);

            for (int i = 0; i < 4; i++) {
                System.out.print(distanceList[i] + ",");
            }
            System.out.println();

        }


    }


}

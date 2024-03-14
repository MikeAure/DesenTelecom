package com.lu.gademo.trace.server.gui;

public class Solution {

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        for (int i = -10; i < 11; i++) {
            for (int j = -10; j < 11; j++) {
                if (243 * i + 198 * j == 909) {
                    System.out.println(i + "," + j);
                    break;
                }
            }
        }

    }

}

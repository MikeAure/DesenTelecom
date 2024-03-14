package com.lu.gademo.trace.server.test;

import java.util.ArrayList;

public class TestFindMap {

    static int[][] a = {
            {21, 22, 25, 26, 37, 38, 41, 42},
            {23, 24, 27, 28, 39, 40, 43, 44},
            {29, 30, 33, 34, 45, 46, 49, 50},
            {31, 32, 35, 36, 47, 48, 51, 52},
            {53, 54, 57, 58, 69, 70, 73, 74},
            {55, 56, 59, 60, 71, 72, 75, 76},
            {61, 62, 65, 66, 77, 78, 81, 82},
            {63, 64, 67, 68, 79, 80, 83, 84}
    };

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        int[] aa = new int[2];
        aa = findCoordination(22);
        System.out.println(aa[0] + "" + aa[1]);

        ArrayList<Integer> list = new ArrayList<Integer>();
        list.add(35);
        list.add(47);
        list.add(71);
        list.add(59);
        solve(list);
        for (int sa : list) {
            System.out.println(sa);
        }
//		findCoordination(24);
    }

    public static void solve(ArrayList<Integer> pos) {
        if (pos.size() == 1) {
            System.out.println(pos.get(0));
        } else if (pos.size() == 2) {
            int[] p1 = new int[2];
            int[] p2 = new int[2];

            p1 = findCoordination(pos.get(0));
            p2 = findCoordination(pos.get(1));
            if (p1[0] == p2[0] && p1[0] < 4) {
                for (int i = 0; i <= p1[0]; i++) {
                    for (int j = p1[1]; j <= p2[1]; j++) {
                        pos.add(a[i][j]);
                    }
                }
            } else if (p1[0] == p2[0] && p1[0] > 4) {
                for (int i = p1[0]; i < 8; i++) {
                    for (int j = p1[1]; j <= p2[1]; j++) {
                        pos.add(a[i][j]);
                    }
                }
            } else if (p1[1] == p2[1] && p1[1] < 4) {
                for (int i = p1[0]; i <= p2[0]; i++) {
                    for (int j = 0; j <= p1[1]; j++) {
                        pos.add(a[i][j]);
                    }
                }
            } else if (p1[1] == p2[1] && p1[1] > 4) {
                for (int i = p1[0]; i <= p2[0]; i++) {
                    for (int j = p1[1]; j < 8; j++) {
                        pos.add(a[i][j]);
                    }
                }
            }
        } else if (pos.size() == 4) {
            int[] p1 = new int[2];
            int[] p2 = new int[2];
            int[] p3 = new int[2];
            int[] p4 = new int[2];

            p1 = findCoordination(pos.get(0));
            p2 = findCoordination(pos.get(1));
            p3 = findCoordination(pos.get(2));
            p4 = findCoordination(pos.get(3));

            for (int i = p1[0]; i <= p3[0]; i++) {
                for (int j = p1[1]; j <= p3[1]; j++) {
                    pos.add(a[i][j]);
                }
            }
        }
    }

    public static int[] findCoordination(int num) {
        int[] result = new int[2];
        int i = 0;
        int j = 0;
        boolean flag = false;
        for (; i < 8; i++) {
            for (; j < 8; j++) {
                if (a[i][j] == num) {
                    flag = true;
                    break;
                }
            }
            if (flag) break;
            j = 0;
        }
        result[0] = i;
        result[1] = j;
        System.out.println(i + ":" + j);
        return result;
    }

}

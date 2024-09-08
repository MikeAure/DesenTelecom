package com.lu.gademo.utils;

import java.util.Random;

public class LocationUtil {
    private static final int BACKGROUNDLENGTH = 45;
    private static final double PI = 3.14159265358979323846;
    private static final double[][] bgknowledge = {
            {116.51172,39.92123}, {116.51135,39.93883}, {116.51175,39.93883}, {116.51627,39.91034},
            {116.51782,39.91226}, {116.51892,39.91496}, {116.460036,39.928437}, {116.353964,39.944923},
            {116.316019,39.929654}, {116.251772,39.932918}, {116.281381,39.946638}, {116.254503,39.95322},
            {116.2775,39.950123}, {116.344765,39.947468}, {116.336572,39.924564}, {116.349939,39.958308},
            {116.316738,39.947468}, {116.357269,39.938285}, {116.329961,39.915046}, {116.385871,39.932863},
            {116.304952,39.90334}, {116.493956,39.876878}, {116.417492,39.88773}, {116.353245,39.854504},
            {116.354682,39.873666}, {116.421804,39.959885}, {116.383141,39.959}, {116.463773,39.968622},
            {116.407431,39.991843}, {116.410449,39.942517}, {116.314582,39.968401}, {116.372792,39.973599},
            {116.317457,39.929792}, {116.372792,39.973599}, {116.35497,39.902787}, {116.364312,39.913053},
            {116.294675,39.944647}, {116.369989,39.929599}, {116.283177,39.976944}, {116.372289,39.921188},
            {116.468875,39.941549}, {116.406209,39.877791}, {116.43553,39.910563}, {116.363665,39.911006},
            {116.341244,39.934468}
    };

    public static void kAnonymityAlgorithm(double x, double y, int k, double[] retArrX, double[] retArrY) {
        Random rand = new Random();
        retArrX[0] = x;
        retArrY[0] = y;
        for (int i = 1; i < k; i++) {
            int randNum = rand.nextInt(BACKGROUNDLENGTH);
            retArrX[i] = bgknowledge[randNum][0];
            retArrY[i] = bgknowledge[randNum][1];
        }
    }

    private static double distance2(double x1, double x2, double y1, double y2) {
        double tmp = Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2);
        return Math.sqrt(tmp);
    }

    public static void cirDummy(double x, double y, int k, double s_cd, double rho,
                                double[] retArrX, double[] retArrY) {
        double theta = 2 * PI / k; // 由面积计算等分后每个扇形的角度
        double r = Math.sqrt(2 * s_cd / (k * Math.sin(theta))) / 111; // 由面积计算圆环区域的外径 line
        double randtmp;
        double centerX, centerY;
        Random rand = new Random();

        // 设置随机数种子，使每次产生的随机序列不同
        while (true) {
            centerX = x - r * (rand.nextInt(10)) / 10.0;
            centerY = y;
            // 若真实位置和圆心确定的半径在[ρr,r]的圆环范围内，则选取圆心成功，退出此while循环
            if (distance2(centerX, x, centerY, y) >= rho * r && distance2(centerX, x, centerY, y) <= r) break;
        }

        retArrX[0] = x; // 暂时把真实位置放在array的第一个
        retArrY[0] = y; // 暂时把真实位置放在array的第一个

        // centre作为极坐标的极点
        double angle = 0; // 角度

        // 循环生成k-1个虚拟位置，每个位置和centre间的距离属于[ρr,r]，夹角比上一个位置大theta
        for (int i = 1; i < k; i++) {
            angle += theta;
            randtmp = (rand.nextDouble() * (1 - rho) * r) + (rho * r); // [ρr,r]随机数
            retArrX[i] = randtmp * Math.cos(angle) + centerX;
            retArrY[i] = randtmp * Math.sin(angle) + centerY;
        }

        // TODO 随机生成序号idx，把array[0]和数组idx位置的元素交换
    }

    public static void main(String[] args) {
        if (args.length < 4) {
            System.exit(-1);
        }

        int t = Integer.parseInt(args[0]);
        double x = Double.parseDouble(args[1]);
        double y = Double.parseDouble(args[2]);

        double[] retArrX;
        double[] retArrY;

        switch (t) {
            case 1:
                int k = Integer.parseInt(args[3]);
                retArrX = new double[k];
                retArrY = new double[k];
                kAnonymityAlgorithm(x, y, k, retArrX, retArrY);
                for (int i = 0; i < k; i++) {
                    System.out.printf("%f,%f\n", retArrX[i], retArrY[i]);
                }
                break;
            // 你可以在这里扩展更多的 case
            default:
                System.out.println("Invalid option");
                break;
        }
    }


}

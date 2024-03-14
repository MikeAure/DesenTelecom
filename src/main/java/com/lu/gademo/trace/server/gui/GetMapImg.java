package com.lu.gademo.trace.server.gui;

import javax.imageio.ImageIO;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;

public class GetMapImg {

    public static void main(String[] args) {
        // TODO Auto-generated method stub
//		double lgtdStart = 108.77, lattdStart = 34.43;
//		double lgtdEnd = 109.12, lattdEnd = 34.14;
//		double lgtdStart = 108.90125;
//		double lattdStart = 34.32125;
//		double lgtdEnd = 108.945;
//		double lattdEnd = 34.285;
//
//		double lgtdCenter = (lgtdStart + lgtdEnd) / 2;
//		double lattdCenter = (lattdStart + lattdEnd) / 2;
//
//		System.out.println(lgtdCenter + "," + lattdCenter);

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                ArrayList<String> result = findCoor(j + "" + i);
                double lgtdCenter = (Double.parseDouble(result.get(0)) + Double.parseDouble(result.get(2))) / 2;
                double lattdCenter = (Double.parseDouble(result.get(1)) + Double.parseDouble(result.get(3))) / 2;
                String lgtdStart = result.get(0);
                String lattdStart = result.get(1);
                String lgtdEnd = result.get(2);
                String lattdEnd = result.get(3);
                String urlPattern = "https://restapi.amap.com/v3/staticmap?location=LONGITUDE,LATITUDE"
                        + "&zoom=ZOOMLEVEL&size=SIZEX*SIZEY" + "&key=USER_KEY";
                String key = "a13c0fc9aa6451749224de45827036f3";

                try {
                    URL url = new URL(urlPattern.replace("LONGITUDE", lgtdCenter + "")
                            .replace("LATITUDE", lattdCenter + "").replace("ZOOMLEVEL", "12").replace("SIZEX", "255")
                            .replace("SIZEY", "255").replace("USER_KEY", key));
                    boolean flag = ImageIO.write(ImageIO.read(url), "png", new File("img/" + j + i + "sub.png"));
                    System.out.println("img/" + j + i + "sub.png:" + flag);
                    Thread.sleep(1000);
                } catch (Exception e) {

                }
            }
        }

    }

    public static ArrayList<String> findCoor(String name) {
        // 地图的经纬度范围
        double lgtdStart = 108.77, lattdStart = 34.43;
        double lgtdEnd = 109.12, lattdEnd = 34.14;
        ArrayList<String> coors = new ArrayList<String>();
        int x = Integer.parseInt(name.charAt(0) + "");
        int y = Integer.parseInt(name.charAt(1) + "");
        System.out.println("x,y:" + x + y);
        double subLgtdStart = lgtdStart + ((lgtdEnd - lgtdStart) / 8) * x;
        double subLgtdEnd = lgtdStart + ((lgtdEnd - lgtdStart) / 8) * (x + 1);
        double subLattdStart = lattdStart + ((lattdEnd - lattdStart) / 8) * y;
        double subLattdEnd = lattdStart + ((lattdEnd - lattdStart) / 8) * (y + 1);
        coors.add(subLgtdStart + "");
        coors.add(subLattdStart + "");
        coors.add(subLgtdEnd + "");
        coors.add(subLattdEnd + "");
        return coors;
    }

}

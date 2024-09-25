package com.lu.gademo.utils;

import java.util.*;

public class LocationUtil {
    /**
     * 将用户信息和区域设置混合处理后返回处理结果
     * 此方法用于特定业务逻辑处理，即将用户的位置和身份信息
     * 与预设的区域信息结合，进行某种处理并返回结果
     *
     * @param x 用户的x坐标
     * @param y 用户的y坐标
     * @param id 用户的标识符
     * @param time 时间戳
     * @param x1 区域的左上角x坐标
     * @param y1 区域的左上角y坐标
     * @param x2 区域的右上角x坐标
     * @param y2 区域的右上角y坐标
     * @param x3 区域的左下角x坐标
     * @param y3 区域的左下角y坐标
     * @param x4 区域的右下角x坐标
     * @param y4 区域的右下角y坐标
     * @return 处理结果字符串
     */
    public static LinkedList<String> mixzone1(double x, double y, int id, double time, double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4) {
        // 设置区域坐标，为后续的处理做准备
        setZone(x1, y1, x2, y2, x3, y3, x4, y4);

        // 创建用户对象，用于承载用户信息
        User user = new User();
        // 设置用户标识符
        user.id = String.valueOf(id);
        // 设置时间戳
        user.time = String.valueOf(time);
        // 设置用户x坐标
        user.x = x;
        // 设置用户y坐标
        user.y = y;

        // 调用mixzone_fun函数处理用户信息，并返回处理结果
        return mixzone_fun(user);
    }

    /**
     * 计算特定区域中用户的混合区域3
     *
     * 此方法主要用于在给定的区域内，根据用户的位置和时间，计算出特定的混合区域3
     * 它首先设置区域的边界，然后创建一个用户对象，并根据传入的用户ID和时间以及位置坐标，调用计算混合区域3的函数
     *
     * @param x 用户的x坐标
     * @param y 用户的y坐标
     * @param id 用户的ID
     * @param time 用户的时间戳
     * @param x1 区域左下角的x坐标
     * @param y1 区域左下角的y坐标
     * @param x2 区域右上角的x坐标
     * @param y2 区域右上角的y坐标
     * @param x3 区域左上角的x坐标
     * @param y3 区域左上角的y坐标
     * @param x4 区域右下角的x坐标
     * @param y4 区域右下角的y坐标
     * @return 混合区域3的计算结果
     */
    public static  LinkedList<String> mixzone3(double x, double y, int id, double time, double x1, double y1, double x2,
                                               double y2, double x3, double y3, double x4, double y4) {
        setZone(x1, y1, x2, y2, x3, y3, x4, y4);
        User user = new User();
        user.id = String.valueOf(id);
        user.time = String.valueOf(time);
        user.x = x;
        user.y = y;
        return mixzone_fun_3(user);
    }

    private static final int HUNDRED = 10;

    public static class PointST {
        double x;
        double y;

        public PointST(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }

    private static double distance1(PointST p, PointST q) {
        return Math.sqrt(Math.pow(p.x - q.x, 2) + Math.pow(p.y - q.y, 2));
    }

//    private static boolean cmpSpaceTwist2(PointST p, PointST q, PointST qfake) {
//        return distance1(p, qfake) < distance1(q, qfake);
//    }

    private static double topDistance(PointST base, List<PointST> v, int[] num) {
        double max = 0;
        int i = 0;
        for (PointST point : v) {
            if (distance1(point, base) > max) {
                max = distance1(point, base);
                num[0] = i;
            }
            i++;
        }
        return max;
    }

    /**
     * 算法主体。
     * 概括为：生成一个虚拟位置qfake，将所有检索点（vec）按照距离q从近到远排序。
     * 先将前k个放进w，遍历后续所有点，若与真实位置q间的距离小于w中与q距离最大的点，则替换。
     * 结束条件：vec遍历完毕或者 当前点到q的距离 > distance(q,qfake)+w中的点与q的最远距离。
     */
    private static int spaceTwistClient(List<PointST> input, int k, PointST q, List<PointST> w) {
        PointST qfake = new PointST(Math.random() * HUNDRED, Math.random() * HUNDRED);
        List<PointST> vec = new ArrayList<>(input);
        Collections.sort(vec, Comparator.comparingDouble(p -> distance1(p, qfake)));

        int cnt;
        for (cnt = 0; cnt <= k - 1; cnt++) {
            w.add(vec.get(cnt));
            if (cnt == vec.size() - 1) {
                System.out.println("Elements less than k");
                return -1;
            }
        }

        int[] num = new int[1];
        double gamma = topDistance(q, w, num);
        int[] num1 = new int[1];
        double tau = topDistance(qfake, vec, num1);////num1目前没有用

        while (gamma + distance1(q, qfake) > tau) {
            if (distance1(vec.get(cnt), q) < gamma) {
                w.get(num[0]).x = vec.get(cnt).x;
                w.get(num[0]).y = vec.get(cnt).y;
                gamma = topDistance(q, w, num);
            }
            cnt++;
            if (cnt == vec.size()) break;
        }
        return 0;
    }

    public static int spaceTwistWrapper(List<PointST> input, double x, double y, int k, double[] retArrX, double[] retArrY) {
        PointST q = new PointST(x, y);
        List<PointST> w = new ArrayList<>();
        if (spaceTwistClient(input, k, q, w) == -1) {
            return -1;
        }

        for (int i = 0; i < w.size(); i++) {
            retArrX[i] = w.get(i).x;
            retArrY[i] = w.get(i).y;
        }
        return 0;
    }


    private static final int NUM = 20;
    private static final int N_HILBERT = 16;

    private static class Hilbert {
        int hilbertValue;
        double xMargin; // 存储小数部分
        double yMargin;

        public Hilbert(int hilbertValue, double xMargin, double yMargin) {
            this.hilbertValue = hilbertValue;
            this.xMargin = xMargin;
            this.yMargin = yMargin;
        }

        public Hilbert(Hilbert addHilbert) {
            this.hilbertValue = addHilbert.hilbertValue;
            this.xMargin = addHilbert.xMargin;
            this.yMargin = addHilbert.yMargin;
        }

        public Hilbert() {
        }
    }

    private static double[] coordinateX = {1.2342, 2.3658, 5.2573, 2.1471, 9.4847, 7.5625, 13.2121, 3.1873, 2.1952, 6.1749, 9.1272,
            4.5943, 10.2346, 11.2593, 0.3672, 15.1247, 3.2157, 1.4947, 2.5947, 8.4657};
    private static double[] coordinateY = {4.5946, 8.2375, 5.1245, 1.2642, 2.8969, 3.7812, 10.2675, 7.8925, 9.5643, 8.2612, 11.2327,
            14.2345, 15.2971, 2.3982, 4.5672, 2.1816, 1.8943, 4.8973, 2.4843, 4.5672};

    private static void rot1(int n, int[] x, int[] y, int rx, int ry) {
        if (ry == 0) {
            if (rx == 1) {
                x[0] = n - 1 - x[0];
                y[0] = n - 1 - y[0];
            }
            // Swap x and y
            int t = x[0];
            x[0] = y[0];
            y[0] = t;
        }
    }

    private static int xy2d1(int n, double x0, double y0) {
        int x = (int) x0;
        int y = (int) y0;
        int[] rx = new int[1];
        int[] ry = new int[1];
        int s, d = 0;
        for (s = n / 2; s > 0; s /= 2) {
            rx[0] = (x & s) > 0 ? 1 : 0;
            ry[0] = (y & s) > 0 ? 1 : 0;
            d += s * s * ((3 * rx[0]) ^ ry[0]);
            rot1(s, new int[]{x}, new int[]{y}, rx[0], ry[0]);
        }
        return d;
    }

    private static void d2xy1(int n, int d, int[] x, int[] y) {
        int[] rx = new int[1];
        int[] ry = new int[1];
        int s, t = d;
        x[0] = y[0] = 0;
        for (s = 1; s < n; s *= 2) {
            rx[0] = 1 & (t / 2);
            ry[0] = 1 & (t ^ rx[0]);
            rot1(s, new int[]{x[0]}, new int[]{y[0]}, rx[0], ry[0]);
            x[0] += s * rx[0];
            y[0] += s * ry[0];
            t /= 4;
        }
    }

    private static class HilbertComparator implements Comparator<Hilbert> {
        @Override
        public int compare(Hilbert a1, Hilbert a2) {
            if (a1.hilbertValue != a2.hilbertValue) {
                return Integer.compare(a1.hilbertValue, a2.hilbertValue);
            } else if (a1.xMargin != a2.xMargin) {
                return Double.compare(a1.xMargin, a2.xMargin);
            } else {
                return Double.compare(a1.yMargin, a2.yMargin);
            }
        }
    }

    public static class User {
        String id;
        String time;
        double x;
        double y;
    }

    private static final double PI = 3.1415926;
    private static final double DEG2RAD = PI / 180;
    private static final double EARTH_RADIUS = 6378137.0;

    private static double zone1_x = 116.435842;
    private static double zone1_y = 39.941626;
    private static double zone2_x = 116.353714;
    private static double zone2_y = 39.939588;
    private static double zone3_x = 116.435806;
    private static double zone3_y = 39.908501;
    private static double zone4_x = 116.356866;
    private static double zone4_y = 39.907242;

    public static void setZone(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4) {
        zone1_x = x1;
        zone1_y = y1;
        zone2_x = x2;
        zone2_y = y2;
        zone3_x = x3;
        zone3_y = y3;
        zone4_x = x4;
        zone4_y = y4;
    }

    private static double convertLonLatToRangeAzim(double lon1, double lat1, double lon2, double lat2) {
        double lon1_rad = lon1 * DEG2RAD;
        double lon2_rad = lon2 * DEG2RAD;
        double lat1_rad = lat1 * DEG2RAD;
        double lat2_rad = lat2 * DEG2RAD;
        double lon_rad_diff_2to1 = lon2_rad - lon1_rad;
        double lat_rad_diff_2to1 = lat2_rad - lat1_rad;

        double range_2to1 = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(lat_rad_diff_2to1 / 2.0), 2) +
                Math.cos(lat1_rad) * Math.cos(lat2_rad) * Math.pow(Math.sin(lon_rad_diff_2to1 / 2.0), 2))) *
                EARTH_RADIUS;

        return range_2to1;
    }

    private static int inMixzone(double x, double y) {
        double range_2to1_1 = convertLonLatToRangeAzim(x, y, zone1_x, zone1_y);
        double range_2to1_2 = convertLonLatToRangeAzim(x, y, zone2_x, zone2_y);
        double range_2to1_3 = convertLonLatToRangeAzim(x, y, zone3_x, zone3_y);
        double range_2to1_4 = convertLonLatToRangeAzim(x, y, zone4_x, zone4_y);

        if (range_2to1_1 > 1000 && range_2to1_2 > 1000 && range_2to1_3 > 1000 && range_2to1_4 > 1000) {
            return 0;
        }
        return 1;
    }

    private static String genRandomString(int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append((char) ('0' + (int) (Math.random() * 10)));
        }
        return sb.toString();
    }

    public static LinkedList<String> mixzone_fun(User user) {
        // 创建一个 LinkedList 用于保存结果
        LinkedList<String> resultList = new LinkedList<>();

        // 使用 String.format 创建要输出的字符串
        String output1 = String.format("TID:%s", user.id);

        String userID = user.id;
        double x = user.x;
        double y = user.y;
        StringBuilder info = new StringBuilder(); //存放用户位置坐标和时间信息
        String sdate = user.time;

        String output2= String.format("time:%s  ", sdate);
//        info.append(sdate).append(",(").append(x).append(",").append(y).append(")");
//        System.out.printf("info:%s  ", info);

        if (inMixzone(x, y) == 1) { // 在混合区域
            String output3 = String.format("inMixZone");
            resultList.add(output1+","+output2+","+output3);
            String randCode = genRandomString(16); // 得到15位的随机码
            String result = "result: " + randCode;
            resultList.add(result);
            return resultList;
        } else { // 不在混合区域
            String output3 = String.format("outofMixZone");
            resultList.add(output1+","+output2+","+output3);
//判断用户不在混合区，则直接返回用户真实位置信息。
            resultList.add("result: ".format("(%f,%f)", x, y));
            return resultList;
        }
    }

    private static final String[] lines = {
            "4",
            "2",
            "3",
            "1",
            "5",
    };

    private static String ReadSpeacialLine(int whichLine) {
        return lines[whichLine - 1];
    }

    public static LinkedList<String> mixzone_fun_3(User user) {
//        System.out.printf("TID:%s  ", user.id);
//        String result = new String();
//        String userID = user.id;
//        double x = user.x;
//        double y = user.y;
//        StringBuilder info = new StringBuilder(); //存放用户位置坐标和时间信息
//        String sdate = user.time;
//
//        System.out.printf("time:%s  ", sdate);
//        info.append(sdate).append(",(").append(x).append(",").append(y).append(")");
//        System.out.printf("info:%s  ", info);

        // 创建一个 LinkedList 用于保存结果
        LinkedList<String> resultList = new LinkedList<>();

        // 使用 String.format 创建要输出的字符串
        String output1 = String.format("TID:%s", user.id);

        String userID = user.id;
        double x = user.x;
        double y = user.y;
        StringBuilder info = new StringBuilder(); //存放用户位置坐标和时间信息
        String sdate = user.time;

        String output2= String.format("time:%s  ", sdate);

        if (inMixzone(x, y) == 1) { // 在混合区域
//            System.out.println("inMixZone");
            String output3 = String.format("inMixZone");
            resultList.add(output1+","+output2+","+output3) ;
            int row = (int) (Math.random() * 5) + 1; // 产生1-5的随机数
            String result = "result: " + row;
            resultList.add(result) ;

//            System.out.printf("result: %s\n", result);
            return resultList;

        } else { // 不在混合区域
            String output3 = String.format("outofMixZone");
            resultList.add(output1+","+output2+","+output3) ;
//            输出：字符串（用户假名id）
            resultList.add("result: ".format("(%s)", user.id));
            return resultList;
        }
    }

    /**
     * 函数用于减少点的坐标精度
     *
     * @param x        原始横坐标
     * @param y        原始纵坐标
     * @param accuracy 精度等级，用于控制坐标精度的降低程度
     * @return Point   返回一个新的点，该点的坐标精度根据指定的精度等级进行减少
     */
    public static Point accuracyReduction(double x, double y, int accuracy) {
        // 根据精度等级调整横坐标，通过四舍五入减少精度
        double ret_x = (double) ((int) (x * accuracy)) / accuracy;
        // 根据精度等级调整纵坐标，通过四舍五入减少精度
        double ret_y = (double) ((int) (y * accuracy)) / accuracy;
        // 创建一个新的点对象，用于保存调整后的坐标
        Point ret = new Point();
        // 设置新点的横坐标
        ret.x = ret_x;
        // 设置新点的纵坐标
        ret.y = ret_y;
        // 返回精度减少后的点
        return ret;
    }


    /**
     * Point类用于表示一个点在二维空间中的位置
     */
    public static class Point {
        int id; // 点的标识符
        double x; // 点的x坐标
        double y; // 点的y坐标

        /**
         * 构造函数，初始化一个点的位置
         *
         * @param id 点的标识符
         * @param x  点的x坐标
         * @param y  点的y坐标
         */
        Point(int id, double x, double y) {
            this.id = id;
            this.x = x;
            this.y = y;
        }

        Point() {
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public double getX() {
            return x;
        }

        public void setX(double x) {
            this.x = x;
        }

        public double getY() {
            return y;
        }

        public void setY(double y) {
            this.y = y;
        }

        /**
         * 重写equals方法，使得两个点如果标识符相同，则认为它们相等
         *
         * @param obj 要比较的对象
         * @return 如果两个点的标识符相同，则返回true；否则返回false
         */
        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null || getClass() != obj.getClass())
                return false;
            Point other = (Point) obj;
            return id == other.id;
        }
    }

}

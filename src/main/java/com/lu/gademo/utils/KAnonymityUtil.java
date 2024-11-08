package com.lu.gademo.utils;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.extern.slf4j.Slf4j;
import org.deidentifier.arx.*;
import org.deidentifier.arx.AttributeType.Hierarchy;
import org.deidentifier.arx.criteria.*;
import org.deidentifier.arx.io.CSVHierarchyInput;
import org.deidentifier.arx.metric.Metric;

@Slf4j
public class KAnonymityUtil {

    public static Data createData(final String dataset, String dir) throws IOException {

        Data data = Data.create(dir + File.separator + dataset + ".csv", StandardCharsets.UTF_8, ';');

        // Read generalization hierarchies
        FilenameFilter hierarchyFilter = (dir1, name) -> name.matches(dataset + "_hierarchy_(.)+.csv");

        // Create definition
        File testDir = new File(dir + File.separator);
        File[] genHierFiles = testDir.listFiles(hierarchyFilter);
        Pattern pattern = Pattern.compile("_hierarchy_(.*?).csv");
        if (genHierFiles != null) {
            for (File file : genHierFiles) {
                Matcher matcher = pattern.matcher(file.getName());
                if (matcher.find()) {
                    CSVHierarchyInput hier = new CSVHierarchyInput(file, StandardCharsets.UTF_8, ';');
                    String attributeName = matcher.group(1);
                    data.getDefinition().setAttributeType(attributeName, Hierarchy.create(hier.getHierarchy()));
                }
            }
        }
        return data;
    }

    public String kAnonymity(final String dataset, String dir, String params, String attribute, int length) throws Exception {
        Data data = createData(dataset, dir);
        // data.getDefinition().setAttributeType(attribute, AttributeType.SENSITIVE_ATTRIBUTE);
        ARXAnonymizer anonymizer = new ARXAnonymizer();
        ARXConfiguration config = ARXConfiguration.create();
        int level = 2;
        switch (params) {
            case "1": {
                level = 4;
                break;
            }
            case "2": {
                level = 8;
                break;
            }
        }

        int k = (level * length / 10) <= 1 ? 2 : level * length / 10;
        log.info("k = {}", k);
        config.addPrivacyModel(new KAnonymity(k));
        config.setSuppressionLimit(0d);
        config.setQualityModel(Metric.createEntropyMetric());
        ARXResult result = anonymizer.anonymize(data, config);
        DataHandle optimal = result.getOutput();
        optimal.save(dir + File.separator + "output_" + dataset + ".csv", ';');
        return dir + File.separator + "output_" + dataset + ".csv";
    }

    public String lDistinctDiversity(final String dataset, String dir, String params, String attribute, int length) throws Exception {
        Data data = createData(dataset, dir);
        data.getDefinition().setAttributeType(attribute, AttributeType.SENSITIVE_ATTRIBUTE);
        ARXAnonymizer anonymizer = new ARXAnonymizer();
        ARXConfiguration config = ARXConfiguration.create();
        int level = 8;
        switch (params) {
            case "1": {
                level = 10;
                break;
            }
            case "2": {
                level = 16;
                break;
            }
        }

        int l = length * level / 10;
        log.info("l = {}", l);
        config.addPrivacyModel(new DistinctLDiversity(attribute, l));
        config.setSuppressionLimit(0.04d);
        config.setQualityModel(Metric.createEntropyMetric());
        ARXResult result = anonymizer.anonymize(data, config);
        DataHandle optimal = result.getOutput();
        optimal.save(dir + File.separator + "output_" + dataset + ".csv", ';');
        return dir + File.separator + "output_" + dataset + ".csv";
    }

    public String lEntropyDiversity(final String dataset, String dir, String params, String attribute, int length) throws Exception {
        Data data = createData(dataset, dir);
        data.getDefinition().setAttributeType(attribute, AttributeType.SENSITIVE_ATTRIBUTE);
        ARXAnonymizer anonymizer = new ARXAnonymizer();
        ARXConfiguration config = ARXConfiguration.create();
        double level = 8;
        switch (params) {
            case "1": {
                level = 10;
                break;
            }
            case "2": {
                level = 16;
                break;
            }
        }
        double l = length * level / 10;
        log.info("l = {}", l);
        config.addPrivacyModel(new EntropyLDiversity(attribute, l));
        config.setSuppressionLimit(0.04d);
        config.setQualityModel(Metric.createEntropyMetric());
        ARXResult result = anonymizer.anonymize(data, config);
        DataHandle optimal = result.getOutput();
        optimal.save(dir + File.separator + "output_" + dataset + ".csv", ';');
        return dir + File.separator + "output_" + dataset + ".csv";
    }

    public String lRecursiveCDiversity(final String dataset, String dir, String params, String attribute, int length) throws Exception {
        Data data = createData(dataset, dir);
        data.getDefinition().setAttributeType(attribute, AttributeType.SENSITIVE_ATTRIBUTE);
        ARXAnonymizer anonymizer = new ARXAnonymizer();
        ARXConfiguration config = ARXConfiguration.create();
        double level = 8;
        switch (params) {
            case "1": {
                level = 10;
                break;
            }
            case "2": {
                level = 16;
                break;
            }
        }
        double c = length * level / 10;
        log.info("c = {}, l = 2", c);
        config.addPrivacyModel(new RecursiveCLDiversity(attribute, c, 2));
        config.setSuppressionLimit(0.04d);
        config.setQualityModel(Metric.createEntropyMetric());
        ARXResult result = anonymizer.anonymize(data, config);
        DataHandle optimal = result.getOutput();
        optimal.save(dir + File.separator + "output_" + dataset + ".csv", ';');
        return dir + File.separator + "output_" + dataset + ".csv";
    }

    public String tCloseness(final String dataset, String dir, String params, String attribute, int length) throws Exception {
        Data data = createData(dataset, dir);
        data.getDefinition().setAttributeType(attribute, AttributeType.SENSITIVE_ATTRIBUTE);
        ARXAnonymizer anonymizer = new ARXAnonymizer();
        ARXConfiguration config = ARXConfiguration.create();
        double level = 0.4;
        switch (params) {
            case "1": {
                level = 0.2;
                break;
            }
            case "2": {
                level = 0.1;
                break;
            }
        }
//        double t = length * (1 - level / 10.0);
        double t = level;
        log.info("t = {}", t);
        config.addPrivacyModel(new EqualDistanceTCloseness(attribute, t));
        config.setSuppressionLimit(0.04d);
        config.setQualityModel(Metric.createEntropyMetric());
        ARXResult result = anonymizer.anonymize(data, config);
        DataHandle optimal = result.getOutput();
        optimal.save(dir + File.separator + "output_" + dataset + ".csv", ';');
        return dir + File.separator + "output_" + dataset + ".csv";
    }

    public void cirDummy(double x, double y, int k, double s_cd, double rho, double[] retArrX, double[] retArrY) {
        double theta = 2 * Math.PI / k;
        double r = Math.sqrt(2 * s_cd / (k * Math.sin(theta))) / 111;
        double centerX, centerY;
        Random random = new Random();

        while (true) {
            centerX = x - r * random.nextDouble();
            centerY = y;
            if ((distance2(centerX, x, centerY, y) >= rho * r) && (distance2(centerX, x, centerY, y) <= r)) {
                break;
            }
        }

        retArrX[0] = x;
        retArrY[0] = y;

        double angle = 0;

        for (int i = 1; i < k; i++) {
            angle += theta;
            double randTmp = random.nextDouble() * (1 - rho) * r + rho * r;
            retArrX[i] = randTmp * Math.cos(angle) + centerX;
            retArrY[i] = randTmp * Math.sin(angle) + centerY;
        }

        // Randomly swap the real location with another position
        int idx = random.nextInt(k);
        double tempX = retArrX[0];
        double tempY = retArrY[0];
        retArrX[0] = retArrX[idx];
        retArrY[0] = retArrY[idx];
        retArrX[idx] = tempX;
        retArrY[idx] = tempY;
    }

    private double distance2(double x1, double x2, double y1, double y2) {
        return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
    }

    public static void gridDummy(double x, double y, int k, double s_cd, double[] retArrX, double[] retArrY) {
        int c = (int) Math.ceil(Math.sqrt(k)); // 若k不是完全平方数，取比它大的最小的完全平方数
        int idx, idy; // 把真实位置随机放进某个网格，idx idy表示真实位置所在的网格在c个网格中的横纵坐标
        Random random = new Random();
        idx = random.nextInt(c);
        idy = random.nextInt(c);

        double g = Math.sqrt(s_cd) / (c - 1);

        // 循环，把其余c-1个网格对应位置的坐标放进array
        for (int i = 0; i < c; i++) {
            for (int j = 0; j < c; j++) {
                double tmpX = x + g * (i - idx) / 111;
                double tmpY = y + g * (j - idy) / 111;
                retArrX[j * c + i] = tmpX;
                retArrY[j * c + i] = tmpY;
            }
        }
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

        public Point(double x, double y) {
            this.x = x;
            this.y = y;
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

        @Override
        public String toString() {
            return "Point{" +
                    "id=" + id +
                    ", x=" + x +
                    ", y=" + y +
                    '}';
        }
    }

    // 用于存储点的集合
    private static List<Point> v = new ArrayList<>();

    /**
     * 初始化函数，向集合v中添加一些预定义的点
     *
     * @param x 要添加的新点的x坐标
     * @param y 要添加的新点的y坐标
     */
    private static void initial(double x, double y) {
        // 添加一系列预定义的点
        v.add(new Point(1, 39.962555, 116.228719));
        v.add(new Point(2, 39.946548, 116.289783));
        v.add(new Point(3, 39.953864, 116.210864));
        v.add(new Point(4, 39.954031, 116.269265));
        v.add(new Point(5, 39.921459, 116.230495));
        v.add(new Point(6, 39.952159, 116.249069));
        v.add(new Point(7, 39.932648, 116.291789));
        v.add(new Point(8, 39.951485, 116.250649));
        v.add(new Point(9, 39.950485, 116.270649));
        v.add(new Point(10, 39.949485, 116.261649));
        // 添加新的点，其标识符为集合当前大小加一
        v.add(new Point(v.size() + 1, x, y));
    }
    private static void initial(double x, double y, int kMin, double xMin, double yMin, double xMax, double yMax) {
        // 添加一系列预定义的点
//        v.add(new Point(1, 39.962555, 116.228719));
//        v.add(new Point(2, 39.946548, 116.289783));
//        v.add(new Point(3, 39.953864, 116.210864));
//        v.add(new Point(4, 39.954031, 116.269265));
//        v.add(new Point(5, 39.921459, 116.230495));
//        v.add(new Point(6, 39.952159, 116.249069));
//        v.add(new Point(7, 39.932648, 116.291789));
//        v.add(new Point(8, 39.951485, 116.250649));
//        v.add(new Point(9, 39.950485, 116.270649));
//        v.add(new Point(10, 39.949485, 116.261649));
        // 添加新的点，其标识符为集合当前大小加一
        for (int i = 0; i < kMin - 1; i++) {
            double randX = xMin + (xMax - xMin) * Math.random();
            double randY = yMin + (yMax - yMin) * Math.random();
            v.add(new Point(v.size() + 1, randX, randY));
        }


        for (int i = 0; i < kMin; i++) {
            double randX = xMin + (xMax - xMin) * Math.random();
            double randY = yMin + (yMax - yMin) * Math.random();
            v.add(new Point(v.size() + 1, randX, randY));
        }
        v.add(new Point(v.size() + 1, x, y));

    }

    /**
     * 采用自适应区间隐藏算法对点集进行处理
     *
     * @param qprev 之前的时间点集
     * @param id    要处理的点的标识符
     * @param rst   结果集
     * @param kMin  最小点集大小
     * @param xMin  x坐标的最小值
     * @param yMin  y坐标的最小值
     * @param xMax  x坐标的最大值
     * @param yMax  y坐标的最大值
     * @return 如果找不到指定id的点，则返回-1；否则返回0
     */
    public static int adaptiveIntervalCloakingAlgorithm(List<Point> qprev, int id, List<Point> rst, int kMin, double xMin, double yMin, double xMax, double yMax) {
        Random random = new Random();
        Point req = new Point(id, 0, 0);
        List<Point> q = new ArrayList<>(qprev);

        // Find the requested point
        Point requestedPoint = qprev.stream()
                .filter(p -> p.id == id)
                .findFirst()
                .orElse(null);
        log.info("requestedPoint: {}", requestedPoint);

        if (requestedPoint == null) {
            return -1;
        }

        double xMid, yMid;

        while (q.size() >= kMin) {
            xMid = (xMin + xMax) / 2;
            yMid = (yMin + yMax) / 2;

            qprev = new ArrayList<>(q);
            q.clear();

            if (requestedPoint.x >= xMid && requestedPoint.y >= yMid) {
                for (Point p : qprev) {
                    if (p.x >= xMid && p.y >= yMid) {
                        q.add(p);
                    }
                }
                xMin = xMid;
                yMin = yMid;
            } else if (requestedPoint.x < xMid && requestedPoint.y >= yMid) {
                for (Point p : qprev) {
                    if (p.x < xMid && p.y >= yMid) {
                        q.add(p);
                    }
                }
                xMax = xMid;
                yMin = yMid;
            } else if (requestedPoint.x <= xMid && requestedPoint.y < yMid) {
                for (Point p : qprev) {
                    if (p.x <= xMid && p.y < yMid) {
                        q.add(p);
                    }
                }
                xMax = xMid;
                yMax = yMid;
            } else {
                for (Point p : qprev) {
                    if (p.x > xMid && p.y < yMid) {
                        q.add(p);
                    }
                }
                xMin = xMid;
                yMax = yMid;
            }
        }

        rst.clear();
        rst.addAll(qprev);
        log.info("rst.size() = {}", rst.size());
        int index = rst.indexOf(requestedPoint);
        Collections.swap(rst, index, kMin - 1);
        log.info("rst: {}", rst);
//        rst.remove(index);
//        rst.add(requestedPoint);
        return 0;
    }


    /**
     * 自适应区间隐藏算法的包装器函数
     *
     * @param x         点的x坐标
     * @param y         点的y坐标
     * @param kMin      最小点集大小
     * @param xMin      x坐标的最小值
     * @param yMin      y坐标的最小值
     * @param xMax      x坐标的最大值
     * @param yMax      y坐标的最大值
     * @param ret_arr_x 用于存储结果的x坐标数组
     * @param ret_arr_y 用于存储结果的y坐标数组
     * @return 如果找不到指定id的点，则返回-1；否则返回0
     */
    public static int adaptiveIntervalCloakingWrapper(double x, double y, int kMin, double xMin, double yMin, double xMax, double yMax, double[] ret_arr_x, double[] ret_arr_y) {
        // 初始化点集
//        initial(x, y);
        initial(x, y, kMin, xMin, yMin, xMax, yMax);
        // 获取新添加的点的标识符
        int id = v.size();
        log.info("id = {}", id);
        // 用于存储算法结果的点集
        List<Point> rst = new ArrayList<>();

        // 如果算法返回-1，则表示找不到指定id的点
        if (adaptiveIntervalCloakingAlgorithm(v, id, rst, kMin, xMin, yMin, xMax, yMax) == -1) {
            System.out.println("id不存在");
            return -1;
        }

        // 将结果集中前kMin个点的坐标存储到输出数组中
        for (int i = 0; i < kMin && i < rst.size(); i++) {
            ret_arr_x[i] = rst.get(i).x;
            ret_arr_y[i] = rst.get(i).y;
        }

        // 清空点集
        v.clear();
        return 0;
    }



    private static final int N = 4; // N*N grid
    private static final int K = 3; // Size of dummy set
    private static final int L = 1; // Normalization factor
    private static final int S = 4; // Maximum number of candidate subsets

    /**
     * 执行CaDSA算法的核心方法。
     * 根据给定的坐标(x, y)和操作类型op，计算并返回候选位置。
     *
     * @param x       输入坐标的x值
     * @param y       输入坐标的y值
     * @param op      操作类型，决定使用的算法版本
     * @param retArrX 用于返回候选位置的x坐标列表
     * @param retArrY 用于返回候选位置的y坐标列表
     */
    public static void caDsaAlgorithm(double x, double y, int op, List<Double> retArrX, List<Double> retArrY) {
        // 将(x, y)转换为网格中的一个单元格索引
        int Cr = xyToCeil(x, y, 4, 4);
//        System.out.printf("Current location cell: %d%n", Cr);

        // 构造虚拟集的单元格列表
        List<Cell> v = constructCells();

        // 根据操作类型选择不同的算法
        switch (op) {
            case 1:
                CaDSA(v, Cr, S, x, y, retArrX, retArrY);
                break;
            case 2:
                // 使用增强的CaDSA算法
                enhancedCaDSA(v, Cr, S, x, y, retArrX, retArrY);
                break;
            default:
                System.out.println("Invalid operation");
        }
    }

    /**
     * 核心算法函数，用于计算和选择最佳的细胞集合
     *
     * @param v       细胞列表，包含所有可选的细胞
     * @param Cr      当前参考细胞的索引，用于定位参考细胞在列表中的位置
     * @param S       算法处理步骤的数量，用于限制组合数量
     * @param x       x 坐标，用于计算偏移量
     * @param y       y 坐标，用于计算偏移量
     * @param retArrX x 坐标偏移结果列表，将计算出的x偏移量存储在此列表中
     * @param retArrY y 坐标偏移结果列表，将计算出的y偏移量存储在此列表中
     * @return 返回0表示执行成功，-1表示未找到参考细胞
     */
//    private static int CaDSA(List<Cell> v, int Cr, int S, double x, double y, List<Double> retArrX, List<Double> retArrY) {
//        // 创建一个临时细胞，用于比较以找到参考细胞
//        Cell cellTmp = new Cell(Cr, 0, 0, 0, 0);
//        int cnt = 0;
//        List<Cell> Cc = new ArrayList<>();
//        Iterator<Cell> it, it1;
//        // 对细胞列表按Q值进行排序
//        v.sort(Comparator.comparingDouble(Cell::getQ));
//        // 查找参考细胞
//        it = v.iterator();
//        while (it.hasNext() && !it.next().equals(cellTmp)) ;
//        if (!it.hasNext()) {
//            System.out.println("Cr not found");
//            return -1;
//        }
//
//        // 选取参考细胞前2k个细胞（不包括参考细胞）
//        it = v.iterator();
//        while (it.hasNext() && cnt < K * 2) {
//            Cell cell = it.next();
//            if (!cell.equals(cellTmp)) {
//                Cc.add(cell);
//                cnt++;
//            }
//        }
//
//        // 选取参考细胞后2k个细胞（不包括参考细胞）
//        it = v.iterator();
//        while (it.hasNext() && !it.next().equals(cellTmp)) ;
//        cnt = 0;
//        while (it.hasNext() && cnt < K * 2) {
//            Cc.add(it.next());
//            cnt++;
//        }
//
//        // 打乱细胞列表顺序
//        Collections.shuffle(Cc);
//
//        //指向第2k+1个
//        it1 = Cc.iterator();
//
//
//        // 移除超出2k范围的细胞
//        for (int i = 0; i <= 2 * K - 1; i++) {
//            Cc.remove(0);
//        }
//
//        // 生成细胞列表的所有子集
//        List<List<Cell>> Cc1 = new ArrayList<>();
//        subsets(Cc, K - 1, Cc1);
//
//        // 如果子集数量超过S，则移除部分子集
//        if (combination(2 * K, K - 1) > S) {
//            for (int i = 0; i <= S; i++) {
//                Cc1.remove(Cc1.size() - 1);
//            }
//        }
//
//        // 寻找最佳细胞组合
//        double maxSum = -1;
//        List<Cell> itrst = null;
//        for (List<Cell> subset : Cc1) {
//            double sumcon = 0;
//            for (Cell cell : subset) {
//                double contribution = cell.getQ() * (cell.isFlag() ? 1 : 0);
//                sumcon += contribution;
//            }
//            if (sumcon >= maxSum) {
//                maxSum = sumcon;
//                itrst = subset;
//            }
//        }
//
//        // 计算并存储每个细胞的偏移量
//        for (Cell cell : itrst) {
//            double fidX = ((int) x % 4 - cell.getX() % 4) * 0.1 + x;
//            double fidY = ((int) y / 4 - cell.getX() / 4) * 0.006 + y;
//            retArrX.add(fidX);
//            retArrY.add(fidY);
//        }
//
//        return 0;
//    }

    public static int CaDSA(List<Cell> v, int Cr, int S, double x, double y, List<Double> ret_arr_x, List<Double> ret_arr_y) {
        Cell cell_tmp = new Cell();
        cell_tmp.x = Cr;
        int cnt = 0;
        List<Cell> Cc = new ArrayList<>(); // candidate set

        Collections.sort(v, (a, b) -> Double.compare(a.q, b.q)); // 按q排序
        Iterator<Cell> it = v.iterator();
        while (it.hasNext() && !it.next().equals(cell_tmp));
        if (!it.hasNext()) {
            System.out.println("未找到Cr");
            return -1;
        }

        // 取cr前面2k个(不含cr块)
        ListIterator<Cell> listIt = v.listIterator(v.indexOf(cell_tmp));
        while (listIt.hasPrevious() && cnt < K * 2) {
            Cc.add(listIt.previous());
            cnt++;
        }

        // 取cr后面2k个(不含cr块)
        listIt = v.listIterator(v.indexOf(cell_tmp));
        cnt = 0;
        while (listIt.hasNext() && cnt < K * 2) {
            listIt.next(); // Skip Cr
            if (listIt.hasNext()) {
                Cc.add(listIt.next());
                cnt++;
            }
        }

        // 随机排序
        Collections.shuffle(Cc);

        // 删除后面所有 还剩2k个
        if (Cc.size() > 2 * K) {
            Cc = new ArrayList<>(Cc.subList(0, 2 * K));
        }

        List<List<Cell>> Cc1 = new ArrayList<>(); // Cc^
        List<List<Cell>> temp = new ArrayList<>();

        // 求Cc的所有K-1个元素子集 Cc1是结果
        subsets(Cc, K - 1, Cc1);

        if (combination(2 * K, K - 1) > S) {
            // 保留前s个子集
            if (Cc1.size() > S) {
                Cc1 = new ArrayList<>(Cc1.subList(0, S));
            }
        }

        double maxsum = -1;
        double contribution, sumcon;
        List<Cell> itrst = null; // 指向结果Cdummy

        // 计算每个子集的贡献和
        for (List<Cell> subset : Cc1) {
            sumcon = 0;
            for (Cell cell : subset) {
                // δ = q · g  g = 0 if this location is already cached and 1 otherwise
                contribution = cell.getQ() * (cell.isFlag() ? 1 : 0);
                sumcon += contribution;
            }
            if (sumcon >= maxsum) {
                maxsum = sumcon;
                itrst = subset;
            }
        }

        // print （用编号表示位置）
        if (itrst != null) {
            for (Cell cell : itrst) {
                double fid_x = ((int) x % 4 - cell.x % 4) * 0.1 + x;
                double fid_y = ((int) y / 4 - cell.x / 4) * 0.006 + y;
                ret_arr_x.add(fid_x);
                ret_arr_y.add(fid_y);
            }
        }

        return 0;
    }

    /**
     * 增强版的CaDSA算法,该方法用于在给定的细胞列表中进行筛选，以找到最优的细胞组合
     *
     * @param v       细胞列表
     * @param Cr      当前参考细胞的索引
     * @param S       采样大小
     * @param x       用于计算的坐标
     * @param y       用于计算的坐标
     * @param retArrX 用于存储结果的数组
     * @param retArrY 用于存储结果的数组
     * @return 如果Cr未找到则返回-1，否则返回0
     */
    private static int enhancedCaDSA(List<Cell> v, int Cr, int S, double x, double y, List<Double> retArrX, List<Double> retArrY) {
        // 创建一个临时细胞，用于后续的比较操作
        Cell cellTmp = new Cell(Cr, 0, 0, 0, 0);
        // 用于计数细胞的数目
        int cnt = 0;
        // Cc用于存储候选细胞
        List<Cell> Cc = new ArrayList<>();
        // 迭代器，用于遍历细胞列表
        Iterator<Cell> it, it1;

        // 根据细胞的质量Q对列表进行排序
        v.sort(Comparator.comparingDouble(Cell::getQ));
        // 寻找当前参考细胞Cr的位置
        it = v.iterator();
        while (it.hasNext() && !it.next().equals(cellTmp)) ;
        // 如果找不到Cr，则输出错误信息并返回
        if (!it.hasNext()) {
            System.out.println("Cr not found");
            return -1;
        }

        // 获取到实际的参考细胞
        Cell real = it.next();

        // 从Cr之前选取2k个细胞
        it = v.iterator();
        while (it.hasNext() && cnt < K * 2) {
            Cell cell = it.next();
            if (!cell.equals(cellTmp)) {
                Cc.add(cell);
                cnt++;
            }
        }

        // 从Cr之后选取2k个细胞
        it = v.iterator();
        while (it.hasNext() && !it.next().equals(cellTmp)) ;
        cnt = 0;
        while (it.hasNext() && cnt < K * 2) {
            Cell cell = it.next();
            if (!cell.equals(cellTmp)) {
                Cc.add(cell);
                cnt++;
            }
        }

        // 打乱Cc列表，以随机化顺序
        Collections.shuffle(Cc);

        // 移除超出2k范围的细胞
        for (int i = 0; i <= 2 * K - 1; i++) {
            Cc.remove(0);
        }

        // 生成Cc的所有子集
        List<List<Cell>> Cc1 = new ArrayList<>();
        subsets(Cc, K - 1, Cc1);

        // 如果子集数量超过S，则移除部分子集
        if (combination(2 * K, K - 1) > S) {
            // 保留前s个子集
            if (Cc1.size() > S) {
                Cc1 = new ArrayList<>(Cc1.subList(0, S));
            }
        }

        // 寻找最大贡献值和对应的细胞集合
        double maxContribution = -1;
        List<Cell> itrst = null;
        for (List<Cell> subset : Cc1) {
            double sumdelta = 0;
            double d = 0;
            for (Cell cell : subset) {
                double delta = cell.getQ() * (cell.isFlag() ? 1 : 0);
                sumdelta += delta;
                d += physicalDistance(real, cell);
            }

            double D = 1;
            d /= K;
            double F = 1;
            double sumfi = 0;
            for (Cell cell : subset) {
                double tmp = d - physicalDistance(real, cell);
                double di = Math.exp(-tmp * tmp / 2);
                D *= di;

                double f = Math.sqrt(1 - Math.pow(cell.getT(), 2) / Math.pow(cell.getT(), 2));
                sumfi += f;
            }

            F = sumfi / (L * K);

            double contribution = sumdelta * (1 - D) * (1 - F);

            if (contribution >= maxContribution) {
                maxContribution = contribution;
                itrst = subset;
            }
        }

        // 根据最优细胞集合更新结果数组
        for (Cell cell : itrst) {
            double fidX = ((int) x % 4 - cell.getX() % 4) * 0.1 + x;
            double fidY = ((int) y / 4 - cell.getX() / 4) * 0.006 + y;
            retArrX.add(fidX);
            retArrY.add(fidY);
        }

        // 执行成功，返回0
        return 0;
    }


    private static double physicalDistance(Cell c1, Cell c2) {
        int x1 = (c1.getX() - 1) / N;
        int y1 = (c1.getX() - 1) % N;
        int x2 = (c2.getX() - 1) / N;
        int y2 = (c2.getX() - 1) % N;
        int square = (y1 - y2) * (y1 - y2) + (x1 - x2) * (x1 - x2);
        return Math.sqrt(square);
    }

    private static double lncombin(int n, int m) {
        if (m > n) return 0;
        if (m < n / 2.0) {
            m = n - m;
        }
        double s1 = 0, s2 = 0;
        for (int i = m + 1; i <= n; i++) {
            s1 += Math.log(i);
        }
        int dif = n - m;
        for (int i = 2; i <= dif; i++) {
            s2 += Math.log(i);
        }
        return s1 - s2;
    }

    private static double combination(int n, int m) {
        if (m > n) return 0;
        return Math.exp(lncombin(n, m));
    }

    private static void select(List<Cell> nums, int m, int idx, List<List<Cell>> rst, List<Cell> item) {
        if (idx <= nums.size()) {
            item.add(nums.get(idx - 1));

            if (item.size() == m) {
                rst.add(new ArrayList<>(item));
            } else {
                select(nums, m, idx + 1, rst, item);
            }

            item.remove(item.size() - 1);
            select(nums, m, idx + 1, rst, item);
        }
    }

    private static void subsets(List<Cell> nums, int m, List<List<Cell>> rst) {
        List<Cell> item = new ArrayList<>();
        select(nums, m, 1, rst, item);
    }

    private static int xyToCeil(double x, double y, int xNum, int yNum) {
        int intX = (int) x;
        double dotX = x - intX;
        int intY = (int) y;
        double dotY = y - intY;

        return (intX % xNum) + (intY % yNum) * yNum + 1;
    }

    /**
     * 构建并返回一个Cell对象列表
     * 该方法主要用于初始化或配置细胞集合，为后续的操作提供数据源
     *
     * @return 返回一个List类型的细胞集合，包含所有细胞对象
     */
    private static List<Cell> constructCells() {
        List<Cell> v = new ArrayList<>();
        switch (K) {
//            case 2:
//                v.add(new Cell(1, 0.05, 1, 9, 6.5));
//                v.add(new Cell(2, 0.04, 0, 7, 6));
//                v.add(new Cell(3, 0.06, 1, 10, 8));
//                v.add(new Cell(4, 0.05, 0, 9, 3));
//                v.add(new Cell(5, 0.1, 0, 5, 3.5));
//                v.add(new Cell(6, 0.12, 0, 4, 1));
//                v.add(new Cell(7, 0.12, 0, 7, 6.5));
//                v.add(new Cell(8, 0.12, 1, 8, 7));
//                v.add(new Cell(9, 0.34, 0, 6.5, 4.5));
//                break;
            case 3:
                for (int i = 1; i <= 16; i++) {
                    v.add(new Cell(i, 0.03, 1, 9, 6.5));
                }
                v.set(7, new Cell(8, 0.01, 1, 9, 6.5));
                v.set(8, new Cell(9, 0.08, 1, 9, 6.5));
                for (int i = 10; i <= 16; i++) {
                    v.set(i - 1, new Cell(i, 0.1, 1, 9, 6.5));
                }
                break;
        }
        return v;
    }

    private static class Cell {
        private int x;
        private double q;
        private boolean flag;
        private double T;
        private double t;

        public Cell(int x, double q, int flag, double T, double t) {
            this.x = x;
            this.q = q;
            this.flag = flag == 1;
            this.T = T;
            this.t = t;
        }

        public Cell(int x)
        {
            this.x = x;
        }

        public Cell(){
        }

        public int getX() {
            return x;
        }

        public double getQ() {
            return q;
        }

        public boolean isFlag() {
            return flag;
        }

        public double getT() {
            return T;
        }


        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            Cell cell = (Cell) obj;
            return x == cell.x;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x);
        }
    }




    // 虚拟位置库。当前的虚拟位置库是基于北京城区的经纬度坐标构建的，参考的是北京市出租车轨迹数据。
    private static final double[][] bgknowledge = {
            {116.51172, 39.92123},
            {116.51135, 39.93883},
            {116.51175, 39.93883},
            {116.51627, 39.91034},
            {116.51782, 39.91226},
            {116.51892, 39.91496},
            {116.460036, 39.928437},
            {116.353964, 39.944923},
            {116.316019, 39.929654},
            {116.251772, 39.932918},
            {116.281381, 39.946638},
            {116.254503, 39.95322},
            {116.2775, 39.950123},
            {116.344765, 39.947468},
            {116.336572, 39.924564},
            {116.349939, 39.958308},
            {116.316738, 39.947468},
            {116.357269, 39.938285},
            {116.329961, 39.915046},
            {116.385871, 39.932863},
            {116.304952, 39.90334},
            {116.493956, 39.876878},
            {116.417492, 39.88773},
            {116.353245, 39.854504},
            {116.354682, 39.873666},
            {116.421804, 39.959885},
            {116.383141, 39.959},
            {116.463773, 39.968622},
            {116.407431, 39.991843},
            {116.410449, 39.942517},
            {116.314582, 39.968401},
            {116.372792, 39.973599},
            {116.317457, 39.929792},
            {116.372792, 39.973599},
            {116.35497, 39.902787},
            {116.364312, 39.913053},
            {116.294675, 39.944647},
            {116.369989, 39.929599},
            {116.283177, 39.976944},
            {116.372289, 39.921188},
            {116.468875, 39.941549},
            {116.406209, 39.877791},
            {116.43553, 39.910563},
            {116.363665, 39.911006},
            {116.341244, 39.934468}
    };

    private static final int BACKGROUNDLENGTH = 45;
    private static final double PI = Math.PI;

    /**
     * 基于匿名位置库的虚假位置生成算法
     *
     * @param x       实际x坐标
     * @param y       实际y坐标
     * @param k       匿名度
     * @param retArrX 返回的x坐标数组
     * @param retArrY 返回的y坐标数组
     */
    public static void kAnonymityAlgorithm(double x, double y, int k, double[] retArrX, double[] retArrY) {
        // 创建一个随机数生成器对象
        Random random = new Random();

        // 将初始坐标(x, y)存入返回数组的第一个位置
        retArrX[0] = x;
        retArrY[0] = y;

        // 从第二个位置开始，直到第k个位置，填充随机选取的坐标
        for (int i = 1; i < k; i++) {
            // 生成一个随机数，用于索引背景知识库
            int randNum = random.nextInt(BACKGROUNDLENGTH);
            // 将随机选取的坐标存入返回数组
            retArrX[i] = bgknowledge[randNum][0];
            retArrY[i] = bgknowledge[randNum][1];
        }
    }

    private static final int HUNDRED = 10;

    private static double distance1(Point p, Point q) {
        return Math.sqrt(Math.pow(p.x - q.x, 2) + Math.pow(p.y - q.y, 2));
    }

//    private static boolean cmpSpaceTwist2(Point p, Point q, Point qfake) {
//        return distance1(p, qfake) < distance1(q, qfake);
//    }

    private static double topDistance(Point base, List<Point> v, int[] num) {
        double max = 0;
        int i = 0;
        for (Point point : v) {
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
    private static int spaceTwistClient(List<Point> input, int k, Point q, List<Point> w) {
        Point qfake = new Point(Math.random() * HUNDRED, Math.random() * HUNDRED);
        List<Point> vec = new ArrayList<>(input);
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

    public static int spaceTwistWrapper(List<Point> input, double x, double y, int k, double[] retArrX, double[] retArrY) {
        Point q = new Point(x, y);
        List<Point> w = new ArrayList<>();
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

    public static int hilbertAlgorithm(double x, double y, int k, double[] retArrX, double[] retArrY) {
        List<Hilbert> hilbertZone = new ArrayList<>(); // 存放所有位置的Hilbert值和小数部分信息
        Hilbert addHilbert = new Hilbert();

        // 将每个格子加入hilbert表hilbertZone
        for (int i = 0; i < NUM; i++) {
            addHilbert.hilbertValue = xy2d1(N_HILBERT, coordinateX[i], coordinateY[i]);
            addHilbert.xMargin = coordinateX[i] - (int) coordinateX[i];
            addHilbert.yMargin = coordinateY[i] - (int) coordinateY[i];
            hilbertZone.add(new Hilbert(addHilbert));
        }

        addHilbert.hilbertValue = xy2d1(N_HILBERT, x, y);
        addHilbert.xMargin = x - (int) x;
        addHilbert.yMargin = y - (int) y;
        hilbertZone.add(new Hilbert(addHilbert));

        // 按照cmp函数的规则排序
        Collections.sort(hilbertZone, new HilbertComparator());

        int ranku = 0;
        // 找真实用户坐标在hilbert表中的位置
        for (int i = 0; i < hilbertZone.size(); i++) {
            if (hilbertZone.get(i).hilbertValue == xy2d1(N_HILBERT, x, y) &&
                    hilbertZone.get(i).xMargin == x - (int) x &&
                    hilbertZone.get(i).yMargin == y - (int) y) {
                ranku = i;
                break;
            }
        }
        int startNum, endNum;

        // 随机选取真实位置周围的k个位置
        startNum = ranku - (ranku) % k;
        endNum = startNum + k - 1;

        // 将选中的位置的hilbert值转换回二维坐标，并输出
        for (int i = startNum; i <= endNum; i++) {
            if (i >= hilbertZone.size())
                break;
            int[] x2 = new int[1];
            int[] y2 = new int[1];
            d2xy1(N_HILBERT, hilbertZone.get(i).hilbertValue, x2, y2);
            double x3 = x2[0] + addHilbert.xMargin;
            double y3 = y2[0] + addHilbert.yMargin;
            retArrX[i - startNum] = x3;
            retArrY[i - startNum] = y3;
        }

        return 0;
    }




}

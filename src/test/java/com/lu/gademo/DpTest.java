package com.lu.gademo;

import com.lu.gademo.utils.DSObject;
import com.lu.gademo.utils.DateParseUtil;
import com.lu.gademo.utils.Dp;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@SpringBootTest
public class DpTest {

    private final Dp dp;
    private final DateParseUtil dateParseUtil;

    @Autowired
    public DpTest(Dp dp, DateParseUtil dateParseUtil) {
        this.dp = dp;
        this.dateParseUtil = dateParseUtil;
    }

    @Test
    public void testLaplaceMechanism() {
        List<Double> randomFloats = getDoubleList(50000, 10000.0);
//        // Dp dp = new DpImpl();
        DSObject dsObject = new DSObject(randomFloats);
        DSObject result = dp.service(dsObject, 1, "1");
        for (Object number : result.getList()) {
            System.out.println(number);
        }
    }

    private List<Double> getDoubleList(int numberOfElements, double maxValue) {

        // 创建一个列表来存储生成的浮点数
        List<Double> randomFloats = new ArrayList<>(numberOfElements);

        // 创建一个Random对象
        Random random = new Random();

        // 生成随机浮点数并添加到列表中
        for (int i = 0; i < numberOfElements; i++) {
            double randomFloat = maxValue * random.nextDouble();
            randomFloats.add(randomFloat);
        }

        // 输出列表中的前10个数作为示例
        for (int i = 0; i < 10; i++) {
            System.out.println(randomFloats.get(i));
        }
        return randomFloats;
    }

    @Test
    public void testReportNoisyMax1Laplace() {
//        // Dp dp = new DpImpl();

        List<Double> rawData = Arrays.asList(8.0, 2.0, 3.0, 4.0, 5.0);
        DSObject dsObject = new DSObject(rawData);
        DSObject result = dp.service(dsObject, 2, "10", "10");
        for (Object number : result.getList()) {
            System.out.println(number);
        }
    }

    @Test
    public void testReportNoisyMax3Laplace() {
//        // Dp dp = new DpImpl();
        List<Double> rawData = Arrays.asList(8.0, 2.0, 3.0, 4.0, 5.0);
        DSObject dsObject = new DSObject(rawData);
        DSObject result = dp.service(dsObject, 3, "10", "10");
        for (Object number : result.getList()) {
            System.out.println(number);
        }
    }

    @Test
    public void testSnappingMechanism() {
        // Dp dp = new DpImpl();
        List<Double> rawData = Arrays.asList(8.0, 2.0, 3.0, 4.0, 5.0);
        DSObject dsObject = new DSObject(rawData);
        DSObject result = dp.service(dsObject, 4, "2");
        for (Object s : result.getList()) {
            System.out.println(s);
        }
    }

    @Test
    public void testDpImage() {
        // Dp dp = new DpImpl();
        File directory = new File("");
        String currentPath = directory.getAbsolutePath();
        String path1 = Paths.get(currentPath, "image", "1.png").toString();
        String path2 = Paths.get(currentPath, "image", "2.png").toString();
        List<String> rawData = Arrays.asList(path1, path2);
        DSObject dsObject = new DSObject(rawData);
        DSObject result = dp.service(dsObject, 5, "1.0");
        for (Object s : result.getList()) {
            System.out.println(s);
        }
    }

    @Test
    public void testDpAudio() {
        // Dp dp = new DpImpl();
        File directory = new File("");
        String currentPath = directory.getAbsolutePath();
        String path1 = Paths.get(currentPath, "audio", "0001.wav").toString();
        String path2 = Paths.get(currentPath, "audio", "0001_Laplace.wav").toString();
        List<String> rawData = Arrays.asList(path1, path2);
        DSObject dsObject = new DSObject(rawData);
        DSObject result = dp.service(dsObject, 6, "5.0");
        for (Object s : result.getList()) {
            System.out.println(s);
        }
    }

    // 图形类脱敏算法运行时间过长
//    @Test
//    public void testDpGraph()  {
//        // Dp dp = new DpImpl();
//        File directory = new File("");
//        String currentPath = directory.getAbsolutePath();
//        String path1 = Paths.get(currentPath, "raw_files", "graph").toString();
//        String path2 = Paths.get(currentPath, "desen_files", "test_graph").toString();
//        List<String> rawData = Arrays.asList(path1, path2);
//        DSObject dsObject = new DSObject(rawData);
//        dp.service(dsObject, 7, 0);
//    }

    @Test
    public void testExponentialMechanism() {
        // Dp dp = new DpImpl();
        List<Double> rawData = Arrays.asList(8.0, 2.0, 3.0, 4.0, 5.0);
        DSObject dsObject = new DSObject(rawData);
        DSObject result = dp.service(dsObject, 8, "1", "1");
        for (Object s : result.getList()) {
            System.out.println(s);
        }
    }

    @Test
    public void testReportNoisyMax2Exponential() {
        // Dp dp = new DpImpl();
        List<Double> rawData = Arrays.asList(8.0, 2.0, 3.0, 4.0, 5.0);
        DSObject dsObject = new DSObject(rawData);
        DSObject result = dp.service(dsObject, 9, "1", "1");
        for (Object s : result.getList()) {
            System.out.println(s);
        }
    }

    @Test
    public void testReportNoisyMax4() {
        // Dp dp = new DpImpl();
        List<Double> rawData = Arrays.asList(8.0, 2.0, 3.0, 4.0, 5.0);
        DSObject dsObject = new DSObject(rawData);
        DSObject result = dp.service(dsObject, 10, "1", "1");
        for (Object s : result.getList()) {
            System.out.println(s);
        }
    }

    @Test
    public void testSparseVectorTechnique1() {
        // Dp dp = new DpImpl();
        List<Double> rawData = Arrays.asList(8.0, 2.0, 3.0, 4.0, 5.0);
        DSObject dsObject = new DSObject(rawData);
        DSObject result = dp.service(dsObject, 11, "5", "6");
        for (Object s : result.getList()) {
            System.out.println(s);
        }
    }

    @Test
    public void testSparseVectorTechnique2() {
        // Dp dp = new DpImpl();
        List<Double> rawData = Arrays.asList(8.0, 2.0, 3.0, 4.0, 5.0);
        DSObject dsObject = new DSObject(rawData);
        DSObject result = dp.service(dsObject, 12, "5", "6");
        for (Object s : result.getList()) {
            System.out.println(s);
        }
    }

    @Test
    public void testSparseVectorTechnique3() {
        // Dp dp = new DpImpl();
        List<Double> rawData = Arrays.asList(8.0, 2.0, 3.0, 4.0, 5.0);
        DSObject dsObject = new DSObject(rawData);
        // c:5, t:6
        DSObject result = dp.service(dsObject, 13, "2", "5");
        for (Object s : result.getList()) {
            System.out.println(s);
        }
    }

    @Test
    public void testSparseVectorTechnique4() {
        // Dp dp = new DpImpl();
        List<Double> rawData = Arrays.asList(8.0, 2.0, 3.0, 4.0, 5.0);
        DSObject dsObject = new DSObject(rawData);
        DSObject result = dp.service(dsObject, 14, "5", "6");
        for (Object s : result.getList()) {
            System.out.println(s);
        }
    }

    @Test
    public void testSparseVectorTechnique5() {
        // Dp dp = new DpImpl();
        List<Double> rawData = Arrays.asList(8.0, 2.0, 3.0, 4.0, 5.0);
        DSObject dsObject = new DSObject(rawData);
        DSObject result = dp.service(dsObject, 15, "5", "6");
        for (Object s : result.getList()) {
            System.out.println(s);
        }
    }

    @Test
    public void testSparseVectorTechnique6() {
        // Dp dp = new DpImpl();
        List<Double> rawData = Arrays.asList(8.0, 2.0, 3.0, 4.0, 5.0);
        DSObject dsObject = new DSObject(rawData);
        DSObject result = dp.service(dsObject, 16, "5", "6");
        for (Object s : result.getList()) {
            System.out.println(s);
        }
    }

    @Test
    public void testNumericalSparseVectorTechnique() {
        // Dp dp = new DpImpl();
        List<Double> rawData = Arrays.asList(8.0, 2.0, 3.0, 4.0, 5.0);
        DSObject dsObject = new DSObject(rawData);
        DSObject result = dp.service(dsObject, 17, "5", "6");
        for (Object s : result.getList()) {
            System.out.println(s);
        }
    }

    @Test
    public void testRappor() {
        // Dp dp = new DpImpl();
        DSObject dsObject = new DSObject(8.5);
        DSObject result = dp.service(dsObject, 18, "6", "6");
        for (Object s : result.getList()) {
            System.out.println(s);
        }
    }

    @Test
    public void testOneTimeRappor() {
        // Dp dp = new DpImpl();
        DSObject dsObject = new DSObject(8.5);
        DSObject result = dp.service(dsObject, 19, "6", "6");
        for (Object s : result.getList()) {
            System.out.println(s);
        }
    }

    // 信工所
    @Test
    public void testDpCode() {
        // Dp dp = new DpImpl();
        Random random = new Random();
        String[] chosenList = new String[]{"A", "B", "C", "D", "E"};

        List<String> newRawData = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            newRawData.add(null);
            newRawData.add(chosenList[random.nextInt(5)]);
        }
        List<String> rawData = Arrays.asList("A", "B", "C", "D", "E");
        DSObject dsObject = new DSObject(newRawData);
        DSObject result = dp.service(dsObject, 20, "2");
        for (Object s : result.getList()) {
            System.out.println(s);
        }
    }

    @Test
    public void testRandomUniformToValue() {
        // Dp dp = new DpImpl();
        List<Double> randomFloats = getDoubleList(100000, 10000.0);
        List<Double> rawData = Arrays.asList(8.0, 2.0, 3.0, 4.0, 5.0);
        DSObject dsObject = new DSObject(randomFloats);
        DSObject result = dp.service(dsObject, 21, "2");
        for (Object s : result.getList()) {
            System.out.println(s);
        }
    }

    // 信工所
    @Test
    public void testRandomLaplaceToValue() {
        // Dp dp = new DpImpl();

        List<Double> rawData = Arrays.asList(8.0, 2.0, 3.0, 4.0, 5.0);
        DSObject dsObject = new DSObject(rawData);
        DSObject result = dp.service(dsObject, 22, "2");
        for (Object s : result.getList()) {
            System.out.println(s);
        }
    }

    @Test
    public void testGaussianToValue() {
        // Dp dp = new DpImpl();
        List<Double> rawData = Arrays.asList(8.0, 2.0, 3.0, 4.0, 5.0);
        DSObject dsObject = new DSObject(rawData);
        DSObject result = dp.service(dsObject, 23, "2");
        for (Object s : result.getList()) {
            System.out.println(s);
        }
    }

    @Test
    public void testNoisyHistogram1() {
        // Dp dp = new DpImpl();
        List<Double> rawData = Arrays.asList(8.0, 2.0, 3.0, 4.0, 5.0);
        DSObject dsObject = new DSObject(rawData);
        DSObject result = dp.service(dsObject, 24, "1", "1");
        for (Object s : result.getList()) {
            System.out.println(s);
        }
    }

    @Test
    public void testNoisyHistogram2() {
        // Dp dp = new DpImpl();
        List<Double> rawData = Arrays.asList(8.0, 2.0, 3.0, 4.0, 5.0);
        DSObject dsObject = new DSObject(rawData);
        DSObject result = dp.service(dsObject, 25, "1", "1");
        for (Object s : result.getList()) {
            System.out.println(s);
        }
    }

    public static List<String> generateDates(int count) {
        // 初始日期时间
        LocalDateTime startTime = LocalDateTime.of(2019, 3, 2, 10, 58, 53);
        // 日期时间格式器
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        List<String> dates = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            // 添加格式化后的日期时间到列表
            dates.add(startTime.plusDays(i).format(formatter));
        }
        return dates;
    }

    private static List<String> generateRandomDates(int count) {
        List<String> dates = new ArrayList<>(count);
        ThreadLocal<SimpleDateFormat> dateFormat = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyyMMdd"));

        // 设置日期范围，比如从1970年到2023年
        long startMillis = parseDate("1970-01-01").getTime();
        long endMillis = parseDate("2023-12-31").getTime();
        Random random = new Random();

        for (int i = 0; i < count; i++) {
            long randomMillis = ThreadLocalRandom.current().nextLong(startMillis, endMillis);
            Date randomDate = new Date(randomMillis);
            String formattedDate = dateFormat.get().format(randomDate);
            dates.add(formattedDate);
        }

        return dates;
    }

    private static Date parseDate(String date) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd").parse(date);
        } catch (Exception e) {
            throw new RuntimeException("日期解析失败: " + date, e);
        }
    }

    // 信工所
    @Test
    public void testDpDate() throws ParseException {
        // Dp dp = new DpImpl();
        List<String> rawData = Arrays.asList("2019-03-02 10:58:53", "2019-03-02 10:58:54", "2019-03-02 10:58:55", "2019-03-02 10:58:56", "2019-03-02 10:58:57");
        int numberOfDates = 50000;
        List<String> dateList = generateRandomDates(numberOfDates);
        // 输出前10个日期作为示例
        List<String> resultList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            System.out.println(dateList.get(i));
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat resultFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (String dateString : dateList) {
            resultList.add(resultFormat.format(dateFormat.parse(dateString)));
        }
        for (int i = 0; i < 10; i++) {
            System.out.println(resultList.get(i));
        }
        List<String> newRawData = generateDates(50000);
        DSObject dsObject = new DSObject(resultList);
        DSObject result = dp.service(dsObject, 26, "0.1");
//        for (Object s : result.getList()) {
//            System.out.println(s);
//        }
    }

    @Test
    public void testDelimiter() {
        String testTemp = "16.8";
        String[] testTempList = testTemp.split("\\.");
        String second = testTempList[0];
        String msecond = testTempList[1];
        System.out.println(second);
        System.out.println(msecond);
        Double noise = 2.137;
        Double noiseResult = 2.137 * 86400000;
        String day = "2";
        String hour = "3";
        String minute = "17";
        String seconds = "16";
        String mSeconds = "800";
        int result = Integer.valueOf(day) * 24 * 60 * 60 * 1000 +
                Integer.valueOf(hour) * 60 * 60 * 1000 +
                Integer.valueOf(minute) * 60 * 1000 +
                Integer.valueOf(seconds) * 1000 + Integer.valueOf(mSeconds);
        System.out.println(result);
        DecimalFormat fmt = new DecimalFormat("#");
        System.out.println(fmt.format(noiseResult));
    }

    @Test
    void testDateSpeed() throws IOException {
        Path rawFilePath = Paths.get("D:\\Programming\\DesenTelecom\\src\\test\\resources\\test_data\\text\\生日数据1w条.xlsx");
        InputStream inputStream = Files.newInputStream(rawFilePath);
        Workbook workbook = new XSSFWorkbook(inputStream);
        Sheet sheet = workbook.getSheetAt(0);
        // 数据行数
        int totalRowNum = sheet.getLastRowNum();
        // 字段名行
        Row fieldNameRow = sheet.getRow(0);
        // 列数
        int columnCount = fieldNameRow.getPhysicalNumberOfCells(); // 获取列数
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        DataFormatter dataFormatter = new DataFormatter();

        for (int columnIndex = 0; columnIndex < columnCount; columnIndex++) {
            List<Object> objs = new ArrayList<>();
            Long part1Total = 0L;
            Long part2Total = 0L;
            Long part3Total = 0L;
            for (int rowIndex = 1; rowIndex <= totalRowNum; rowIndex++) {
                Row row = sheet.getRow(rowIndex);
                if (row != null) {
                    Cell cell = row.getCell(columnIndex);
                    // 如果单元格为空
                    if (cell == null || cell.getCellType() == CellType.BLANK || cell.getCellType() == CellType._NONE) {
                        objs.add(null);
                        continue;
                    }
                    // 如果表格中出现单元格中有null字符串的情况
                    if (cell.getCellType() == CellType.STRING && cell.getStringCellValue().equalsIgnoreCase("null")) {
                        objs.add(null);
                        continue;
                    }
                    if (cell.getCellType() == CellType.STRING && cell.getStringCellValue().equalsIgnoreCase("")) {
                        objs.add(null);
                        continue;
                    }
                    // 日期类型
//                        objs.add(dataFormatter.formatCellValue(cell));
                    switch (cell.getCellType()) {
                        case STRING:
                            // 如果单元格是字符串类型，尝试解析为日期
                            String dateString = cell.getStringCellValue();
                            java.util.Date date = dateParseUtil.parseDate(dateString);
                            if (date != null) {
                                String formattedDate = sdf.format(date);
                                objs.add(formattedDate);
                            } else {
                                System.out.println("Invalid Date String: " + dateString);
                            }
                            break;
                        case NUMERIC:
                            if (DateUtil.isCellDateFormatted(cell)) {
                                // 如果单元格是日期类型
                                java.util.Date numericDate = cell.getDateCellValue();
                                String formattedDate = sdf.format(numericDate);
//                                    System.out.println("Formatted Date from Numeric: " + formattedDate);
                                objs.add(formattedDate);
                            } else {
                                Long part1Start = System.currentTimeMillis();
                                String formatCellValue = dataFormatter.formatCellValue(cell);
                                Long part1End = System.currentTimeMillis();
                                part1Total += part1End - part1Start;

                                Long part2Start = System.currentTimeMillis();
                                java.util.Date date2 = dateParseUtil.parseDate(formatCellValue);
                                Long part2End = System.currentTimeMillis();
                                part2Total += part2End - part2Start;

                                Long part3Start = System.currentTimeMillis();
                                if (date2 != null) {
                                    String formattedDate = sdf.format(date2);
                                    objs.add(formattedDate);
                                } else {
                                    System.out.println("Invalid Date String: " + formatCellValue);
                                }
                                Long part3End = System.currentTimeMillis();
                                part3Total += part3End - part3Start;
                            }
                            break;
                        default:
                            System.out.println("Unsupported Cell Type: " + cell.getCellType());
                            break;
                    }

                }

            }
//            DSObject dsObject = new DSObject(objs);
//            dp.service(dsObject, 26, 2);
            System.out.println("Part1 total: " + part1Total);
            System.out.println("Part2 total: " + part2Total);
            System.out.println("Part3 total: " + part3Total);
        }

    }

    @Test
        // 日期解析工具测试
    void testDateUtil() {
        List<String> dateString = Arrays.asList("2016-03-19 2:1:14", "2020-09-08 4:4:41", "2022-11-16 16:36:6",
                "2020-08-12 23:29:5", "2020-11-00 1:49:37", "2021-05-05 3:50",
                "2023-08-01 0:33",
                "2020-10-23 22:41",
                "2023-06-28 12:14",
                "19890510");
        for (String date : dateString) {
            java.util.Date date2 = dateParseUtil.parseDate(date);
            if (date2 != null) {
                System.out.println(date2);
            } else {
                System.out.println("Invalid Date String: " + date);
            }
        }

    }
}

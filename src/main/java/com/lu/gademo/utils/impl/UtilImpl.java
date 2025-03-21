package com.lu.gademo.utils.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.lu.gademo.utils.CommandExecutor;
import com.lu.gademo.utils.Util;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.bouncycastle.crypto.digests.SM3Digest;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Hex;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.locationtech.jts.geom.Geometry;
import org.opengis.feature.simple.SimpleFeature;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.sql.*;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Slf4j
@Component
public class UtilImpl implements Util {
    @Override
    public String getIP() {
        try {
            InetAddress localhost = InetAddress.getLocalHost();
            return localhost.getHostAddress();
        } catch (UnknownHostException e) {
            log.error(e.getMessage());
            return null;
        }
    }

    @Override
    public String getTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime currentTime = LocalDateTime.now();
        return currentTime.format(formatter);
    }

    @Override
    public String getSHA256Hash(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = md.digest(input.getBytes(StandardCharsets.UTF_8));
            //System.out.println(Arrays.toString(hashBytes));

            // Convert the byte array to a hexadecimal string representation
            StringBuilder stringBuilder = new StringBuilder();
            for (byte hashByte : hashBytes) {
                String hex = String.format("%02x", hashByte);
                stringBuilder.append(hex);
            }

            //return sb.toString();
            return Arrays.toString(hashBytes);
        } catch (NoSuchAlgorithmException e) {
            log.error(e.getMessage());
            return null;
        }
    }

    @Override
    public String getSM3Hash(byte[] input) {
        SM3Digest digest = new SM3Digest();
        digest.update(input, 0, input.length);
        byte[] bytes = new byte[digest.getDigestSize()];
        digest.doFinal(bytes, 0);

        // Convert the byte array to a hexadecimal string representation
        StringBuilder sb = new StringBuilder();
        for (byte hashByte : bytes) {
            String hex = String.format("%02x", hashByte);
            sb.append(hex);
        }
        return sb.toString();
    }

    @Override
    public void createDB(Sheet sheet, List<Integer> dataType, String tableName) throws IOException, SQLException {
        //Workbook workbook = new XSSFWorkbook(inputStream);
        //Sheet sheet = workbook.getSheetAt(0); // 获取第一个sheet
        Row headerRow = sheet.getRow(0); // 获取第一行作为字段名

        // 获取字段名
        List<String> columnNames = new ArrayList<>();
        for (Cell cell : headerRow) {
            columnNames.add(cell.getStringCellValue());
        }

        // 从第二行开始读取数据并存储到数据库
        for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
            Row dataRow = sheet.getRow(rowIndex);

            // 使用columnNames中的字段名创建数据表和插入数据
            //createTableIfNotExists(tableName, columnNames);
            insertDataIntoTable(tableName, columnNames, dataRow);
        }
    }

    @Override
    public String getSM2Sign(byte[] input) throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException, SignatureException, InvalidKeySpecException {
        // 添加Bouncy Castle作为安全提供程序
        Security.addProvider(new BouncyCastleProvider());
        // 私钥字符串
        String sk = "30818d020100301306072a8648ce3d020106082a8648ce3d03010404733071020101041e227e11283308222c5c6ede135b" +
                "8fa41c8655037ffa2cfc7dd45b23514b6da00a06082a8648ce3d030104a140033e00046d12ce22dcde68a79a833033a4532dee2" +
                "c7fd2442b3d5434cb61c41b707b186de567ad4ca81fbbf575ca0f39f065b6dc724bf2ac47d0446e79e06cde";
        // 公钥字符串
        String pk = "3055301306072a8648ce3d020106082a8648ce3d030104033e00046d12ce22dcde68a79a833033a4532dee2c7fd2442b3d5434cb61c41b707b186de567ad4ca81fbbf575ca0f39f065b6dc724bf2ac47d0446e79e06cde";
        // 转字节数组
        byte[] privateKeyBytes = Hex.decode(sk);
        byte[] publicKeyBytes = Hex.decode(pk);
        // 恢复私钥
        KeyFactory keyFactory = KeyFactory.getInstance("EC");
        PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
        PrivateKey privateKey = keyFactory.generatePrivate(privateKeySpec);

        // 恢复公钥
        X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKeyBytes);
        PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);

        // 生成密钥对
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("EC", "BC");
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
       /* PrivateKey privateKey = keyPair.getPrivate();
        PublicKey publicKey = keyPair.getPublic();*/

        // 打印私钥的16进制表示
        String privateKeyHex = new String(Hex.encode(privateKey.getEncoded()));
//        System.out.println("Private Key (Hex): " + privateKeyHex);

        // 打印公钥的16进制表示
        String publicKeyHex = new String(Hex.encode(publicKey.getEncoded()));
//        System.out.println("Public Key (Hex): " + publicKeyHex);

        // 初始化SM3withSM2签名对象
        Signature signature = Signature.getInstance("SM3withSM2", "BC");
        signature.initSign(privateKey);
        // 更新签名对象的数据
        signature.update(input);
        // 签名
        byte[] signatureBytes = signature.sign();

        // 打印签名结果的16进制表示
        String signatureHex = new String(Hex.encode(signatureBytes));
        return signatureHex;
    }

    @Override
    public boolean isLinux() {
        return System.getProperty("os.name").toLowerCase().contains("linux");
    }


    public void insertDataIntoTable(String tableName, List<String> columnNames, Row dataRow) throws SQLException {
        // 创建数据库连接
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/ga", "root", "123456QWer!!")) {

            // 创建表格
            String createTableSQL = "CREATE TABLE IF NOT EXISTS " + tableName + " (";
            for (String columnName : columnNames) {
                createTableSQL += columnName + " VARCHAR(255), ";
            }
            createTableSQL += "PRIMARY KEY (" + columnNames.get(0) + "))";

            try (PreparedStatement createTableStatement = connection.prepareStatement(createTableSQL)) {
                createTableStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            // 插入数据
            String insertDataSQL = "INSERT INTO " + tableName + " VALUES (";
            for (int i = 0; i < columnNames.size(); i++) {
                insertDataSQL += "?, ";
            }
            insertDataSQL = insertDataSQL.substring(0, insertDataSQL.length() - 2); // 移除最后一个逗号
            insertDataSQL += ")";

            try (PreparedStatement insertDataStatement = connection.prepareStatement(insertDataSQL)) {
                for (int i = 0; i < columnNames.size(); i++) {
                    insertDataStatement.setString(i + 1, dataRow.getCell(i).getStringCellValue());
                }
                insertDataStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void saveExcel(String tableName, String fileName) {
        String jdbcURL = "jdbc:mysql://localhost:3306/ga";
        String username = "root";
        String password = "123456QWer!!";

        try (Connection connection = DriverManager.getConnection(jdbcURL, username, password)) {
            String sql = "SELECT * FROM " + tableName; // 替换为你的表名

            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(sql)) {

                // 创建工作簿和表格
                Workbook workbook = new XSSFWorkbook();
                Sheet sheet = workbook.createSheet("Data");

                // 获取列名
                ResultSetMetaData metaData = resultSet.getMetaData();
                int columnCount = metaData.getColumnCount();
                Row headerRow = sheet.createRow(0);
                for (int i = 2; i <= columnCount; i++) {
                    String columnName = metaData.getColumnName(i);
                    Cell cell = headerRow.createCell(i - 2);
                    cell.setCellValue(columnName);
                }

                // 写入数据
                int rowNum = 1;
                while (resultSet.next()) {
                    Row row = sheet.createRow(rowNum++);
                    for (int i = 2; i <= columnCount; i++) {
                        Object value = resultSet.getObject(i);
                        Cell cell = row.createCell(i - 2);
                        if (value != null) {
                            cell.setCellValue(value.toString());
                        } else {
                            cell.setCellValue("");
                        }
                    }
                }

                // 保存 Excel 文件
                FileOutputStream outputStream = new FileOutputStream(fileName);
                workbook.write(outputStream);
                workbook.close();
                outputStream.close();

                System.out.println("Excel 文件已创建成功！");

            } catch (SQLException | IOException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    // 将对象序列化为字节数组
    public <T> byte[] serializeToByteArray(List<T> list) {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(list);
            objectOutputStream.close();
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return new byte[0];
        }
    }

    @Override
    // 反序列化
    public <T extends Serializable> List<T> convertToList(byte[] byteArray) {
        try {
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray);
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);

            // 从输入流中读取对象列表
            @SuppressWarnings("unchecked")
            List<T> entityList = (List<T>) objectInputStream.readObject();

            // 关闭流
            objectInputStream.close();
            byteArrayInputStream.close();

            return entityList;

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    // 字节数组转字符串
    public String convertToString(byte[] byteArray) {
        return new String(Base64.getEncoder().encodeToString(byteArray).getBytes(), StandardCharsets.UTF_8);
    }

    @Override
    // 字符串转字节数组
    public byte[] convertToByteArray(String byteString) {
        return Base64.getDecoder().decode(byteString);
    }

    public <T> void write2Excel(Sheet originalSheet, Sheet newSheet, int lastRowNum, int columnIndex, List<T> data) {
        // 遍历原始Sheet的每一行
        for (int j = 0; j <= lastRowNum; j++) {
            Row originalRow = originalSheet.getRow(j);
            // 检查目标Sheet的行是否已经存在，如果存在则复用，否则创建新行
            Row newRow = newSheet.getRow(j);
            if (newRow == null) {
                newRow = newSheet.createRow(j);
            }

            if (originalRow != null) {
                // 遍历每个单元格并复制到新Sheet中（只针对指定列写脱敏值或原始值）
                Cell originalCell = originalRow.getCell(columnIndex);
                Cell newCell = newRow.createCell(columnIndex);

                if (j > 0) { // 检查是否为指定列并忽略标题行
                    T element = data.get(j - 1);  // 获取脱敏后的数据
                    if (element == null) {
                        // 脱敏数据为null时，写入原始值
                        writeOriginalValueToCell(originalCell, newCell);
                    } else {
                        // 否则写入脱敏数据
                        writeDesensitizedValueToCell(newCell, element);
                    }
                } else if (j == 0) {
//                        // 复制非脱敏列的原始值
                    writeOriginalValueToCell(originalCell, newCell);
                }

            }
        }
    }

    // 方法：写入原始值
    private void writeOriginalValueToCell(Cell originalCell, Cell newCell) {
        // 检查 originalCell 是否为 null
        if (originalCell == null) {
            newCell.setBlank();  // 如果原始单元格为空，设置新单元格为空
            return;
        }
        switch (originalCell.getCellType()) {
            case STRING:
                newCell.setCellValue(originalCell.getStringCellValue());
                break;
            case NUMERIC:
                newCell.setCellValue(originalCell.getNumericCellValue());
                break;
            case BOOLEAN:
                newCell.setCellValue(originalCell.getBooleanCellValue());
                break;
            case FORMULA:
                newCell.setCellFormula(originalCell.getCellFormula());
                break;
            case BLANK:
                newCell.setBlank();
                break;
            default:
                break;
        }
        // 复制原单元格的样式
//        CellStyle newCellStyle = newCell.getSheet().getWorkbook().createCellStyle();
//        newCellStyle.cloneStyleFrom(originalCell.getCellStyle());
//        newCell.setCellStyle(newCellStyle);
    }

    // 方法：写入脱敏数据
    private <T> void writeDesensitizedValueToCell(Cell newCell, T element) {
        ThreadLocal<SimpleDateFormat> fmt = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        if (element instanceof Double) {
            newCell.setCellValue((Double) element);
        } else if (element instanceof Integer) {
            newCell.setCellValue((Integer) element);
        } else if (element instanceof String) {
            newCell.setCellValue((String) element);
        } else if (element instanceof java.util.Date) {
            newCell.setCellValue(fmt.get().format(element));
        }
    }

    @Override
    /**
     *
     * @param sheet sheet
     * @param lastRowNum 行数
     * @param i 第i列
     * @param datas 脱敏数据
     * @param <T>
     */
    public <T> void write2Excel(Sheet sheet, int lastRowNum, int i, List<T> datas) {
        for (int j = 1; j <= lastRowNum; j++) {
            Row row = sheet.getRow(j);
            if (row != null) {
                Cell cell = row.getCell(i);
                if (cell != null) {
                    T element = datas.get(j - 1);
                    //if (datas.get(j-1)!=null) {
                    if (element instanceof Double) {
                        cell.setCellValue((Double) element);
                    } else if (element instanceof Integer) {
                        cell.setCellValue((Integer) element);
                    } else if (element instanceof Date) {
                        //cell.setCellValue((Date) element);
                        cell.setCellValue(element.toString());
                    } else if (element instanceof String) {
                        cell.setCellValue((String) element);
                    }
                }

            }
        }
    }

    public void copySheet(Sheet sourceSheet, Sheet targetSheet) {
        for (int i = 0; i <= sourceSheet.getLastRowNum(); i++) {
            Row sourceRow = sourceSheet.getRow(i);
            Row targetRow = targetSheet.createRow(i);

            if (sourceRow != null) {
                for (int j = 0; j < sourceRow.getLastCellNum(); j++) {
                    Cell sourceCell = sourceRow.getCell(j);
                    Cell targetCell = targetRow.createCell(j);

                    if (sourceCell != null) {
                        // 复制单元格的类型和内容
                        switch (sourceCell.getCellType()) {
                            case STRING:
                                targetCell.setCellValue(sourceCell.getStringCellValue());
                                break;
                            case NUMERIC:
                                targetCell.setCellValue(sourceCell.getNumericCellValue());
                                break;
                            case BOOLEAN:
                                targetCell.setCellValue(sourceCell.getBooleanCellValue());
                                break;
                            case FORMULA:
                                targetCell.setCellFormula(sourceCell.getCellFormula());
                                break;
                            case BLANK:
                                targetCell.setBlank();
                                break;
                            default:
                                break;
                        }

                        // 复制单元格样式
                        CellStyle newCellStyle = targetSheet.getWorkbook().createCellStyle();
                        newCellStyle.cloneStyleFrom(sourceCell.getCellStyle());
                        targetCell.setCellStyle(newCellStyle);
                    }
                }
            }
        }
    }

    public <T> Sheet writeDataToClonedSheet(Workbook workbook, Sheet originalSheet, int columnIndex, List<T> datas) {
        // 使用 Workbook.cloneSheet 方法快速复制原始的 Sheet
        Sheet clonedSheet = workbook.cloneSheet(workbook.getSheetIndex(originalSheet));

        // 在克隆的 Sheet 中插入脱敏数据
        for (int j = 1; j <= clonedSheet.getLastRowNum(); j++) {
            Row row = clonedSheet.getRow(j);
            if (row != null) {
                Cell cell = row.getCell(columnIndex);
                if (cell == null) {
                    cell = row.createCell(columnIndex);
                }
                T element = datas.get(j - 1);
                // 如果脱敏后的数据列表中该元素不为null则更改当前单元格中的元素
                if (element != null) {
                    if (element instanceof Double) {
                        cell.setCellValue((Double) element);
                    } else if (element instanceof Integer) {
                        cell.setCellValue((Integer) element);
                    } else if (element instanceof String) {
                        cell.setCellValue((String) element);
                    } else if (element instanceof java.util.Date) {
                        cell.setCellValue(element.toString());
                    }
                }
            }
        }
        return clonedSheet;
    }

    @Override
    /**
     *
     * @param inputStream 输入流
     * @return 对应字符串
     */
    public String inputStreamToString(InputStream inputStream) {
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

        try {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
                stringBuilder.append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return stringBuilder.toString();
    }

    @Override
    public void mySqlDump(String table) {
        String username = "root";
        String password = "123456QWer!!";

        // 设置 mysqldump 命令参数
        List<String> command = new ArrayList<>();
        command.add("mysqldump");
        command.add("--user=" + username);
        command.add("--password=" + password);
        command.add("--add-drop-table");
        command.add("--result-file=" + table);
        command.add("ga");
        command.add(table.split("\\.")[0]);

        // 构建 ProcessBuilder
        ProcessBuilder processBuilder = new ProcessBuilder(command);
        processBuilder.redirectErrorStream(true);

        try {
            // 执行命令
            Process process = processBuilder.start();

            // 等待命令执行完成
            int exitCode = process.waitFor();
            if (exitCode == 0) {
                System.out.println("Export successful");

            } else {
                System.out.println("Export failed");
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
//    @Override
//    public void excelToMysql(String tableName, String excelFilePath){
//        String jdbcURL = "jdbc:mysql://localhost:3306/ga";
//        String username = "root";
//        String password = "123456QWer!!";
//
//
//        try (Connection connection = DriverManager.getConnection(jdbcURL, username, password)) {
//            // 清空数据表
//            String truncateQuery = "TRUNCATE TABLE " + tableName;
//            Statement truncateStatement = connection.createStatement();
//            truncateStatement.executeUpdate(truncateQuery);
//
//            FileInputStream file = new FileInputStream(excelFilePath);
//            XSSFWorkbook workbook = new XSSFWorkbook(file);
//            XSSFSheet sheet = workbook.getSheetAt(0);
//
//            // 获取列数，这里第一行是列名
//            int columnCount = sheet.getRow(0).getLastCellNum();
//
//            // 遍历每一行（第一行是列名，从第二行开始读取数据）
//            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
//                Row row = sheet.getRow(i);
//
//                // 准备 INSERT 语句
//                StringBuilder insertQuery = new StringBuilder("INSERT INTO " + tableName + " VALUES (");
//                insertQuery.append(i + ",");
//                for (int j = 0; j < columnCount; j++) {
//                    Cell cell = row.getCell(j);
//                    if (cell != null) {
//                        insertQuery.append("'" + cell + "'");
//                        // 如果不是最后一列，添加逗号分隔
//                        if (j < columnCount - 1) {
//                            insertQuery.append(",");
//                        }
//                    } else {
//                        insertQuery.append("NULL");
//                        // 如果不是最后一列，添加逗号分隔
//                        if (j < columnCount - 1) {
//                            insertQuery.append(",");
//                        }
//                    }
//                }
//                insertQuery.append(")");
//
//                // 执行 INSERT 语句
//                PreparedStatement preparedStatement = connection.prepareStatement(insertQuery.toString());
//                preparedStatement.executeUpdate();
//            }
//
//            workbook.close();
//            file.close();
//
//            System.out.println("数据已成功导入到 MySQL 数据库中!");
//
//        } catch (IOException | SQLException e) {
//            e.printStackTrace();
//        }
//    }

    @Override
    public Boolean isCondaInstalled(boolean isLinux) {
        List<String> command = new ArrayList<>();
        if (isLinux) {
            command.add("/bin/bash");
            command.add("-c");
            command.add("conda env list | grep \"torch_env\"");
        } else {
            command.add("cmd.exe");
            command.add("/c");
            command.add("conda env list | findstr \"torch_env\"");
        }
        try {
            ProcessBuilder processBuilder = new ProcessBuilder(command);
            Process process = processBuilder.start();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                if (line.contains("torch_env")) {
                    return true;
                }
            }
        } catch (IOException e) {
            return false;
        }
        return false;
    }

    private ArrayNode getArrayNode(String[] rawList, ObjectMapper objectMapper) {
        ArrayNode desenIntentionArrayNode = objectMapper.createArrayNode();
        for (String singleDesenIntention : rawList) {
            desenIntentionArrayNode.add(singleDesenIntention);
        }
        return desenIntentionArrayNode;
    }

    @Override
    public ArrayNode trimCommaAndReturnArrayNode(String rawString, ObjectMapper objectMapper) {
        String[] desenIntentionList = new String[0];
        if (rawString.endsWith(",")) {
            desenIntentionList = rawString.substring(0, rawString.length() - 1).split(",");
        } else {
            desenIntentionList = rawString.split(",");
        }
//        System.out.println(Arrays.toString(desenIntentionList));
        return getArrayNode(desenIntentionList, objectMapper);
    }

    @Override
    public String convertTxtToShp(String shapefilePath) {
        String python = CommandExecutor.getPythonCommand();
        String userDir = System.getProperty("user.dir");
        String filePath = userDir + File.separator + "graph" + File.separator + "poi.py";
        System.out.println("poi.py file path: " + filePath);

        String txtFilePath = shapefilePath.replace(".shp", ".txt");
        String cmd = String.format("%s %s %s %s", python, filePath, shapefilePath, txtFilePath);
        CommandExecutor.openExe(cmd);

        return txtFilePath;
    }

    public List<String> converShpToTxt(String shapefilePath) throws IOException {
        File file = Paths.get(shapefilePath).toFile();

        Map<String, Object> params = new HashMap<>();
        params.put("url", file.toURI().toURL());

        DataStore dataStore = DataStoreFinder.getDataStore(params);
        String typeName = dataStore.getTypeNames()[0]; // 获取第一个图层
        SimpleFeatureCollection features = dataStore.getFeatureSource(typeName).getFeatures();

        List<String> elements = new ArrayList<>();
        List<String> result = new ArrayList<>();
        // 遍历特征集合
        try (SimpleFeatureIterator featureIterator = features.features()) {
            while (featureIterator.hasNext()) {
                SimpleFeature feature = featureIterator.next();
                String poiString = (String) feature.getAttribute("id");
                long lineNum = (Long) feature.getAttribute("line");
                String typeCode = (String) feature.getAttribute("type_code");
//                System.out.println(lineNum  + " " + poiString + " " + typeCode);
                result.add(lineNum  + " " + poiString + " " + typeCode);
                Geometry geometry = (Geometry) feature.getDefaultGeometry();
                if (geometry != null) {

                    for (int i = 0; i < geometry.getNumPoints(); i++) {
                        double latitude = geometry.getCoordinates()[i].y;
                        double longitude = geometry.getCoordinates()[i].x;
                        elements.add(String.valueOf(longitude) + " " + String.valueOf(latitude));
                    }
                }
            }
        }

        dataStore.dispose();

//        String rawData = String.join(",", elements);
//        System.out.println(result);
        return result;
    }

    public Map<String, List<String>> groupByLineNumber(List<String> data) {
        Map<String, List<String>> groupedData = new HashMap<>();
        for (String item : data) {
            String[] parts = item.split(" ");
            String lineNum = parts[0]; // Line number
            String poiAndAttribute = parts[1] + "," + parts[2]; // POI and attribute

            // Group by line number
            groupedData.putIfAbsent(lineNum, new ArrayList<>());
            groupedData.get(lineNum).add(poiAndAttribute);
        }
        return groupedData;
    }

    public String formatData(Map<String, List<String>> groupedData) {
        StringBuilder formattedData = new StringBuilder();
        for (String lineNum : groupedData.keySet()) {
            List<String> items = groupedData.get(lineNum);
            formattedData.append(String.join(";", items)) // Join items with semicolon
                    .append(";\n"); // Add semicolon and newline
        }
        return formattedData.toString();
    }

    public void saveToFile(String data, String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(data);
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }

}

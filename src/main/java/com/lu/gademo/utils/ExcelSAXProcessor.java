package com.lu.gademo.utils;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.eventusermodel.ReadOnlySharedStringsTable;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler;
import org.apache.poi.xssf.usermodel.XSSFComment;

import org.apache.poi.xssf.model.StylesTable;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;
import com.lu.gademo.entity.ExcelParam;


public class ExcelSAXProcessor {

    private Map<Integer, List<Object>> preprocessedData = new HashMap<>();
    private Map<String, ExcelParam> excelParamMap = new HashMap<>();
    private DataFormatter dataFormatter = new DataFormatter();

    // 初始化 ExcelSAXProcessor 类时传入脱敏参数
    public ExcelSAXProcessor(List<ExcelParam> excelParamList) {
        for (ExcelParam item : excelParamList) {
            excelParamMap.put(item.getColumnName().trim(), item);
        }
    }

    public Map<Integer, List<Object>> processExcel(String filePath) throws Exception {
        try (OPCPackage pkg = OPCPackage.open(filePath)) {
            XSSFReader reader = new XSSFReader(pkg);
            StylesTable styles = reader.getStylesTable();
            ReadOnlySharedStringsTable strings = new ReadOnlySharedStringsTable(pkg);

            XSSFReader.SheetIterator iter = (XSSFReader.SheetIterator) reader.getSheetsData();
            while (iter.hasNext()) {
                try (InputStream stream = iter.next()) {
                    InputSource sheetSource = new InputSource(stream);
                    XMLReader parser = XMLReaderFactory.createXMLReader();

                    // 创建自定义的 SheetHandler，用于处理每一行数据
                    SheetHandler sheetHandler = new SheetHandler(excelParamMap, preprocessedData);
                    parser.setContentHandler(new XSSFSheetXMLHandler(styles, strings, sheetHandler, dataFormatter, false));
                    parser.parse(sheetSource);
                }
            }
        }
        return preprocessedData;
    }

    // 自定义的 SheetHandler 用于处理每一行的内容
    private static class SheetHandler implements XSSFSheetXMLHandler.SheetContentsHandler {
        private final Map<String, ExcelParam> excelParamMap;
        private final Map<Integer, List<Object>> preprocessedData;
        private List<Object> rowData;
        private int currentRow = 0;
        private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


        public SheetHandler(Map<String, ExcelParam> excelParamMap, Map<Integer, List<Object>> preprocessedData) {
            this.excelParamMap = excelParamMap;
            this.preprocessedData = preprocessedData;
        }

        @Override
        public void startRow(int rowNum) {
            rowData = new ArrayList<>(); // 初始化当前行数据
            currentRow = rowNum;
        }

        @Override
        public void endRow(int rowNum) {
            for (int i = 0; i < rowData.size(); i++) {
                preprocessedData.computeIfAbsent(i, k -> new ArrayList<>()).add(rowData.get(i));
            }
        }
        @Override
        public void cell(String cellReference, String formattedValue, XSSFComment comment) {
            int columnIndex = getColumnIndex(cellReference);

            ExcelParam excelParam = excelParamMap.get(cellReference);
            Object cellValue = handleCellData(formattedValue, excelParam);
            rowData.add(columnIndex, cellValue); // 将处理后的单元格数据添加到行数据中
        }

        @Override
        public void headerFooter(String text, boolean isHeader, String tagName) {
            // 可以忽略页眉页脚的处理
        }

        private Object handleCellData(String formattedValue, ExcelParam excelParam) {
            // 如果 cell 为空或有特定的 null 字符串，则返回 null
            if (formattedValue == null || formattedValue.equalsIgnoreCase("null") || formattedValue.isEmpty()) {
                return null;
            }

            switch (excelParam.getDataType()) {
                case 4: // 日期类型
                    try {
                        Date date = new SimpleDateFormat("yyyy-MM-dd").parse(formattedValue);
                        return sdf.format(date);
                    } catch (Exception e) {
                        return formattedValue; // 无法解析为日期时返回原始值
                    }
                case 3: // 文本型数据
                    try {
                        double num = Double.parseDouble(formattedValue);
                        return num == (long) num ? (long) num : num;
                    } catch (NumberFormatException e) {
                        return formattedValue;
                    }
                case 1: // 编码型数据
                case 0: // 数值型数据
                    return formattedValue;
                default:
                    return formattedValue; // 默认返回原始值
            }
        }

        private int getColumnIndex(String cellReference) {
            int column = 0;
            for (char ch : cellReference.toCharArray()) {
                if (Character.isLetter(ch)) {
                    column = column * 26 + (ch - 'A' + 1);
                }
            }
            return column - 1; // Excel column index starts from 1, so subtract 1
        }
    }
}

package com.lu.gademo.utils.impl;

import com.lu.gademo.utils.*;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Comment;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.xslf.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.XWPFComment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.xmlbeans.XmlCursor;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkupRange;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.xml.namespace.QName;
import java.awt.*;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Component
@Data
public class RecvFileDesenImpl implements RecvFileDesen {


    private final Replace replacement;
    private final Dp dp;
    private final DpUtil dpUtil;
    private final Util util;
    private final Anonymity anonymity;
    private final Generalization generalization;
    private final AlgorithmsFactory algorithmsFactory;

    private static Map<Integer, XWPFRun> getPosToRuns(XWPFParagraph paragraph) {
        int pos = 0;
        Map<Integer, XWPFRun> map = new HashMap<Integer, XWPFRun>(10);
        for (XWPFRun run : paragraph.getRuns()) {
            String runText = run.text();
            if (runText != null) {
                for (int i = 0; i < runText.length(); i++) {
                    map.put(pos + i, run);
                }
                pos += runText.length();
            }
        }
        return (map);
    }

    @Override
    public byte[] desenRecvFile(MultipartFile file) throws Exception {
        String time = String.valueOf(System.currentTimeMillis());
        String fileName = time + file.getOriginalFilename();
        Path currentPath = Paths.get("").toAbsolutePath();
        Path rawFilePath = Paths.get(currentPath + "/raw_files" + "/" + fileName);
        Path desenFilePath = Paths.get(currentPath + "/desen_files" + "/desen_" + fileName);
        file.transferTo(rawFilePath.toFile());
        String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
        switch (suffix) {
            case "xlsx": {
                extractExcel(rawFilePath, desenFilePath);
                break;
            }
            case "docx": {
                extractWord(rawFilePath, desenFilePath);
                break;
            }
            case "pptx": {
                extractPowerPoint(rawFilePath, desenFilePath);
                break;
            }
            default: {
                throw new Exception("File type not supported");
            }

        }

        return Files.readAllBytes(desenFilePath);
    }

    private void extractExcel(Path rawFilePath, Path desenFilePath) throws Exception {
        try (
                InputStream rawFileInputStream = Files.newInputStream(rawFilePath);
                XSSFWorkbook wb = new XSSFWorkbook(rawFileInputStream);
                OutputStream outputStream = Files.newOutputStream(desenFilePath)
        ) {

            Sheet sheet = wb.getSheetAt(0);

            // 使用正则表达式匹配中文
            String patternString = "([\\u4e00-\\u9fa5]+)-([\\u4e00-\\u9fa5]+)-([\\u4e00-\\u9fa5]+)-([\\u4e00-\\u9fa5]+)";
            Pattern pattern = Pattern.compile(patternString);
            // 存储全部Comments
            Map<CellAddress, Comment> comments = (Map<CellAddress, Comment>) sheet.getCellComments();
            // 逐一遍历comments
            for (Map.Entry<CellAddress, ? extends Comment> e : comments.entrySet()) {
                CellAddress loc = e.getKey();
                Cell cell = sheet.getRow(loc.getRow()).getCell(loc.getColumn());
                int privacyLevel = getPrivacyLevel(e, pattern);
                // 根据Comment修改算法
                DSObject result = desenData(cell, replacement, 3, privacyLevel);
                String desenResult = result.getList().get(0).toString();
                if (desenResult.equals(cell.getStringCellValue())) {
                    continue;
                }
                cell.setCellValue(result.getList().get(0).toString());
            }

            // 写入excel
            wb.write(outputStream);
        }

    }

    private void extractWord(Path rawFilePath, Path desenFilePath) throws Exception {
        Map<String, String> commentMap = new HashMap<>();

        try (
                InputStream rawFileInputStream = Files.newInputStream(rawFilePath);
                XWPFDocument document = new XWPFDocument(rawFileInputStream);
                OutputStream outputStream = Files.newOutputStream(desenFilePath)) {
            XmlCursor cursor = document.getDocument().getBody().newCursor();
            QName idQname = new javax.xml.namespace.QName(
                    "http://schemas.openxmlformats.org/wordprocessingml/2006/main",
                    "id");
            while (cursor.toNextToken() != XmlCursor.TokenType.NONE) {
                // save the cursor
                cursor.push();
                if (cursor.isStart()) {
                    // 确保光标位于元素的开始标记
                    QName nodeName = cursor.getName();
                    if (nodeName != null) {
                        String localName = nodeName.getLocalPart();
                        if ("commentRangeStart".equals(localName)) {
                            // 获取 commentRangeStart 的 ID
                            String commentStartId = cursor.getAttributeText(idQname);
                            System.out.println("Comment ID: " + commentStartId);
                            StringBuilder stringBuilder = new StringBuilder();
                            while (cursor.toNextToken() != XmlCursor.TokenType.NONE) {
                                if (cursor.isStart()) { // 再次检查是否为开始标记
                                    QName innerNodeName = cursor.getName();
                                    String commentEndId = cursor.getAttributeText(idQname);
                                    if ("commentRangeEnd".equals(innerNodeName.getLocalPart()) && commentEndId.equals(commentStartId)) { // 检查是否为结束标记，可能需要调整逻辑以识别commentRangeEnd
                                        break;
                                    }
                                    if ("t".equals(innerNodeName.getLocalPart())) {
                                        stringBuilder.append(cursor.getTextValue()); // 累加文本值
                                    }
                                }
                            }
                            if (stringBuilder.length() > 0) {
                                commentMap.put(commentStartId, stringBuilder.toString());
                                System.out.println(commentStartId + "\t" + stringBuilder); // 打印收集到的文本
                            }

                        }
                    }
                }
                cursor.pop();
            }
            cursor.dispose();

            for (XWPFParagraph paragraph : document.getParagraphs()) {
                XWPFComment comment;
                StringBuilder commentText = new StringBuilder();
                for (CTMarkupRange anchor : paragraph.getCTP().getCommentRangeStartList()) {
                    BigInteger id = anchor.getId();

                    if (id != null &&
                            (comment = paragraph.getDocument().getCommentByID(id.toString())) != null) {
                        System.out.println("Comment ID: " + id);

                        // TODO: 根据comment内容判断脱敏等级
                        System.out.println("Comment: " + comment.getText());
                        String target = commentMap.get(id.toString());
                        if (target == null) {
                            System.out.println("id: " + id + " target is null");
                            continue;
                        }
                        System.out.println("Target: " + target);
                        String desenResult = desenData(target, replacement, 3, 1).getList().get(0).toString();
                        System.out.println("desenResult: " + desenResult);
                        if (desenResult.equals(target)) {
                            continue;
                        }
                        replace(paragraph, target, desenResult);
                    }
                }
            }
            document.write(outputStream);
        }


    }

    private void extractPowerPoint(Path rawFilePath, Path desenFilePath) throws Exception {
        try (
                InputStream rawFileInputStream = Files.newInputStream(rawFilePath);
                XMLSlideShow ppt = new XMLSlideShow(rawFileInputStream);
                OutputStream outputStream = Files.newOutputStream(desenFilePath)
        ) {
            // 遍历PPT全部批注
            for (XSLFSlide slide : ppt.getSlides()) {
                Dimension slideSize = slide.getSlideShow().getPageSize();
                // System.out.println("Slide Width: " + slideSize.width + " Slide Height: " + slideSize.height);
                List<XSLFComment> comments = slide.getComments();
                // 逐个文本框扫描分析批注位置是否在文本框内
                for (XSLFComment comment : comments) {
                    // 提取commentText中的脱敏内容
                    String commentText = comment.getText();
                    String patternString = "([\\u4e00-\\u9fa5]+)-([\\u4e00-\\u9fa5]+)-([\\u4e00-\\u9fa5]+)-([\\u4e00-\\u9fa5]+)\\s+(.*)";
                    Pattern pattern = Pattern.compile(patternString);
                    Matcher matcher = pattern.matcher(commentText);
                    if (!matcher.matches()) {
                        continue;
                    }
                    // 属性需求
                    String attribute = matcher.group(2);
                    // 脱敏内容
                    String rawContent = matcher.group(5);

                    System.out.println("Comment Text: " + commentText);
                    System.out.println("Attribute: " + attribute);
                    System.out.println("Content: " + rawContent);
                    // 设置privacy_level
                    int privacyLevel = 1;
                    if (attribute.contains("独特属性")) {
                        privacyLevel = 3;
                    }

                    for (XSLFShape shape : slide.getShapes()) {
                        if (shape instanceof XSLFTextShape) {
                            System.out.println("Anchor: " + shape.getAnchor());
                            String shapeText = ((XSLFTextShape) shape).getText();
                            System.out.println("Shape Text: " + ((XSLFTextShape) shape).getText());
                            // 匹配文本
                            if (shapeText.contains(rawContent)) {
                                String preString = shapeText.substring(0, shapeText.indexOf(rawContent));
                                String afterString = shapeText.substring(shapeText.indexOf(rawContent) + rawContent.length());
                                String desenResult = desenData(rawContent, generalization, 1, privacyLevel).getList().get(0).toString();
                                String stringBuilder = preString +
                                        desenResult +
                                        afterString;
                                ((XSLFTextShape) shape).setText(stringBuilder);
                            }
                        }
                    }
                }
            }

            ppt.write(outputStream);
        }
    }

    private <V> void replace(XWPFParagraph paragraph, String searchText, V replacement) {
        boolean found = true;
        while (found) {
            found = false;
            int pos = paragraph.getText().indexOf(searchText);
            if (pos >= 0) {
                found = true;
                // 每个XWPFRun的位置
                Map<Integer, XWPFRun> posToRuns = getPosToRuns(paragraph);
                XWPFRun run = posToRuns.get(pos);
                XWPFRun lastRun = posToRuns.get(pos + searchText.length() - 1);
                int runNum = paragraph.getRuns().indexOf(run);
                int lastRunNum = paragraph.getRuns().indexOf(lastRun);
                String[] texts = replacement.toString().split("\n");
                run.setText(texts[0], 0);
                XWPFRun newRun = run;
                for (int i = 1; i < texts.length; i++) {
                    newRun.addCarriageReturn();
                    newRun = paragraph.insertNewRun(runNum + i);
                /*
                    We should copy all style attributes
                    to the newRun from run
                    also from background color, ...
                    Here we duplicate only the simple attributes...
                 */
                    newRun.setText(texts[i]);
                    newRun.setBold(run.isBold());
                    newRun.setCapitalized(run.isCapitalized());
                    // newRun.setCharacterSpacing(run.getCharacterSpacing());
                    newRun.setColor(run.getColor());
                    newRun.setDoubleStrikethrough(run.isDoubleStrikeThrough());
                    newRun.setEmbossed(run.isEmbossed());
                    newRun.setFontFamily(run.getFontFamily());
                    newRun.setFontSize(run.getFontSize());
                    newRun.setImprinted(run.isImprinted());
                    newRun.setItalic(run.isItalic());
                    newRun.setKerning(run.getKerning());
                    newRun.setShadow(run.isShadowed());
                    newRun.setSmallCaps(run.isSmallCaps());
                    newRun.setStrikeThrough(run.isStrikeThrough());
                    newRun.setUnderline(run.getUnderline());
                }
                for (int i = lastRunNum + texts.length - 1; i > runNum + texts.length - 1; i--) {
                    paragraph.removeRun(i);
                }
            }
        }
    }

    /**
     * 获取数据脱敏结果
     *
     * @param cell         电子表格单元格
     * @param algorithm    选择的脱敏算法
     * @param algoNum      脱敏算法编号
     * @param privacyLevel 隐私保护级别
     * @return 脱敏结果
     */
    private DSObject desenData(Cell cell, BaseDesenAlgorithm algorithm, int algoNum, int privacyLevel) {

        String cellContent = cell.getStringCellValue();
        // 构建脱敏算法输入数据
        DSObject rawData = new DSObject(Collections.singletonList(cellContent));
        // 使用编号脱敏算法
//        return algorithm.service(rawData, algoNum, privacyLevel);
        return algorithmsFactory.getAlgorithmInfoFromId(algoNum).execute(rawData, privacyLevel);
    }


    /**
     * @param content
     * @param algorithm
     * @param algoNum
     * @param privacyLevel
     * @return
     */
    private DSObject desenData(String content, BaseDesenAlgorithm algorithm, int algoNum, int privacyLevel) {
        // 构建脱敏算法输入数据
        DSObject rawData = new DSObject(Collections.singletonList(content));
        // 使用编号脱敏算法
        return algorithmsFactory.getAlgorithmInfoFromId(algoNum).execute(rawData, privacyLevel);
    }

    /**
     * 获取电子表格中带有批注的单元格的隐私保护等级
     *
     * @param e       Comment单元格位置
     * @param pattern 用于匹配文本的Pattern
     * @return 返回脱敏级别
     */
    private int getPrivacyLevel(Map.Entry<CellAddress, ? extends Comment> e, Pattern pattern) {
        Comment comment = e.getValue();
        // 使用正则表达式匹配中文字符串
        String commentContent = comment.getString().getString();
        Matcher matcher = pattern.matcher(commentContent);
        String attribute;
        // 设置privacy_level
        int privacyLevel = 1;
        if (matcher.matches()) {
            attribute = matcher.group(2);
            if (attribute.equals("独特属性")) {
                privacyLevel = 3;
            }
        }
        return privacyLevel;
    }

    private int getPrivacyLevel(String commentContent) {
        String patternString = "([\\u4e00-\\u9fa5]+)-([\\u4e00-\\u9fa5]+)-([\\u4e00-\\u9fa5]+)-([\\u4e00-\\u9fa5]+)";
        Pattern pattern = Pattern.compile(patternString);
        // 使用正则表达式匹配中文字符串
        Matcher matcher = pattern.matcher(commentContent);
        String attribute;
        // 设置privacy_level
        int privacyLevel = 1;
        if (matcher.matches()) {
            attribute = matcher.group(2);
            if (attribute.equals("独特属性")) {
                privacyLevel = 3;
            }
        }
        return privacyLevel;
    }


}

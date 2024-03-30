package com.lu.gademo;

import com.lu.gademo.utils.BaseDesenAlgorithm;
import com.lu.gademo.utils.DSObject;
import com.lu.gademo.utils.Dp;
import com.lu.gademo.utils.Replace;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.sl.usermodel.PlaceableShape;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Comment;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xslf.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.xwpf.usermodel.*;
import org.apache.xmlbeans.XmlCursor;
import org.junit.jupiter.api.Test;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkupRange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.xml.namespace.QName;
import java.awt.*;
import java.awt.geom.Point2D;
import java.io.*;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SpringBootTest
@Slf4j
public class OfficeComentExtractTest {
    @Autowired
    Replace replacement;

    @Autowired
    Dp dp;

    @Test
    public void extractExcelTest() {
        Path rawFilePath = Paths.get("D:\\test_data\\sheets\\comment.xlsx");
        Path desenFilePath = Paths.get("D:\\test_data\\sheets\\DealtTable\\comment_desen.xlsx");

        try (
                InputStream rawFileInputStream = Files.newInputStream(rawFilePath);
                XSSFWorkbook wb = new XSSFWorkbook(rawFileInputStream);
                OutputStream outputStream = Files.newOutputStream(desenFilePath);)
        {
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
                DSObject result = desenData(cell, replacement, 7, privacyLevel);
                cell.setCellValue(result.getList().get(0).toString());
            }
            // 写入excel
            wb.write(outputStream);
            
        } catch (Exception e) {
            log.error(e.getMessage());
        }

    }

    @Test
    public void extractWordTest() {
        Path rawFilePath = Paths.get("D:\\test_data\\sheets\\comment.docx");
        Path desenFilePath = Paths.get("D:\\test_data\\sheets\\DealtTable\\comment_desen.docx");

        Map<String, String> commentMap = new HashMap<>();

        try (
                InputStream rawFileInputStream = Files.newInputStream(rawFilePath);
                XWPFDocument document = new XWPFDocument(rawFileInputStream);
                OutputStream outputStream = Files.newOutputStream(desenFilePath);)
        {
            XmlCursor cursor = document.getDocument().getBody().newCursor();
            while (cursor.toNextToken() != XmlCursor.TokenType.NONE) {
                if (cursor.isStart()) { // 确保光标位于元素的开始标记
                    QName nodeName = cursor.getName();
                    if (nodeName != null) {
                        String localName = nodeName.getLocalPart();
                        if ("commentRangeStart".equals(localName)) {
                            // 获取 commentRangeStart 的 ID
                            String id = cursor.getAttributeText(new javax.xml.namespace.QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "id"));
                            System.out.println("Comment ID: " + id);
                            StringBuilder stringBuilder = new StringBuilder();
                            while (cursor.toNextToken() != XmlCursor.TokenType.NONE) {
                                if (cursor.isStart()) { // 再次检查是否为开始标记
                                    QName innerNodeName = cursor.getName();
                                    if ("commentRangeEnd".equals(innerNodeName.getLocalPart())) { // 检查是否为结束标记，可能需要调整逻辑以识别commentRangeEnd
                                        break;
                                    }
                                    if ("t".equals(innerNodeName.getLocalPart())) {
                                        stringBuilder.append(cursor.getTextValue()); // 累加文本值
                                    }
                                }
                            }
                            if (stringBuilder.length() > 0) {
                                commentMap.put(id, stringBuilder.toString());
                                System.out.println(id + "\t" + stringBuilder); // 打印收集到的文本
                            }
                        }
                    }
                }
            }
            cursor.close();

            for (XWPFParagraph paragraph : document.getParagraphs()) {
                XWPFComment comment;
                StringBuilder commentText = new StringBuilder();
                for (CTMarkupRange anchor : paragraph.getCTP().getCommentRangeStartArray()) {
                    BigInteger id = anchor.getId();

                    if (id != null &&
                            (comment = paragraph.getDocument().getCommentByID(id.toString())) != null) {
                        System.out.println("Comment ID: " + id);

                        // TODO: 根据comment内容判断脱敏等级
                        System.out.println("Comment: " + comment.getText());
                        String target = commentMap.get(id.toString());
                        System.out.println("Target: " + target);
                        String desenResult  = desenData(target, replacement, 7, 1).getList().get(0).toString();
                        System.out.println("desenResult: " + desenResult);
                        replace(paragraph, target, desenResult);
                    }
                }
            }
            document.write(outputStream);
        } catch (Exception e) {
            log.error(e.getMessage());
        }

    }

    @Test
    public void extractPowerPointTest() {
        Path rawFilePath = Paths.get("D:\\test_data\\sheets\\commentppt.pptx");
        Path desenFilePath = Paths.get("D:\\test_data\\sheets\\DealtTable\\comment2_desen.pptx");

        try (
                InputStream rawFileInputStream = Files.newInputStream(rawFilePath);
                XMLSlideShow  ppt = new XMLSlideShow (rawFileInputStream);
                OutputStream outputStream = Files.newOutputStream(desenFilePath);)
        {
            // 遍历PPT全部批注
            for (XSLFSlide slide : ppt.getSlides()) {
                Dimension slideSize = slide.getSlideShow().getPageSize();
                System.out.println("Slide Width: " + slideSize.width + " Slide Height: " + slideSize.height);
                List<XSLFComment> comments = slide.getComments();
                // 逐个文本框扫描分析批注位置是否在文本框内
                for(XSLFComment comment : comments) {
                    Point2D commentAnchor = comment.getOffset();
                    System.out.println("Comment Offset: " + commentAnchor);
                    String commentText = comment.getText();
                    System.out.println("Comment Text: " + commentText);
                    int privacyLevel = 1;
                    if (commentText.contains("独特属性")) {
                        privacyLevel = 3;
                    }

                    double absoluteX = commentAnchor.getX() * slideSize.width;
                    double absoluteY = commentAnchor.getY() * slideSize.height;
                    System.out.println("Comment Absolute location: " + absoluteX + " " + absoluteY);

                    for(XSLFShape shape : slide.getShapes()) {
                        if (shape instanceof XSLFTextShape){
                            System.out.println("Anchor: " + shape.getAnchor());
                            System.out.println("Shape Text: " + ((XSLFTextShape) shape).getText());
                            if (shape.getAnchor().contains(absoluteX, absoluteY))  {
                                String contentText = ((XSLFTextShape) shape).getText();
                                System.out.println("Content Text: " + contentText);
                                DSObject result = desenData(contentText, replacement, 7, privacyLevel);
                                ((XSLFTextShape) shape).setText(result.getList().get(0).toString());
                                System.out.println("After replacement: " + ((XSLFTextShape) shape).getText());
                            }
                        }
                    }
                }
//                for (XSLFShape sh : slide.getShapes()) {
//                    // name of the shape
//                    String name = sh.getShapeName();
//                    // shapes's anchor which defines the position of this shape in the slide
//                    if (sh instanceof PlaceableShape) {
//                        java.awt.geom.Rectangle2D anchor = ((PlaceableShape) sh).getAnchor();
//                    }
//                    if (sh instanceof XSLFConnectorShape) {
//                        XSLFConnectorShape line = (XSLFConnectorShape) sh;
//                        // work with Line
//                    } else if (sh instanceof XSLFTextShape) {
//                        XSLFTextShape shape = (XSLFTextShape) sh;
//                        System.out.println(shape.getText());
//                        // work with a shape that can hold text
//                    } else if (sh instanceof XSLFPictureShape) {
//                        XSLFPictureShape shape = (XSLFPictureShape) sh;
//                        // work with Picture
//                    }
//                }
            }

            ppt.write(outputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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

    public static <V> void replace(XWPFDocument document, Map<String, V> map) {
        List<XWPFParagraph> paragraphs = document.getParagraphs();
        for (XWPFParagraph paragraph : paragraphs) {
            replace(paragraph, map);
        }
    }

    public static <V> void replace(XWPFDocument document, String searchText, V replacement) {
        List<XWPFParagraph> paragraphs = document.getParagraphs();
        for (XWPFParagraph paragraph : paragraphs) {
            replace(paragraph, searchText, replacement);
        }
    }

    private static <V> void replace(XWPFParagraph paragraph, Map<String, V> map) {
        for (Map.Entry<String, V> entry : map.entrySet()) {
            replace(paragraph, entry.getKey(), entry.getValue());
        }
    }

    public static <V> void replace(XWPFParagraph paragraph, String searchText, V replacement) {
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
                String texts[] = replacement.toString().split("\n");
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

    private static DSObject desenData(Cell cell, BaseDesenAlgorithm algorithm, int algoNum, int privacyLevel) {

        String cellContent = cell.getStringCellValue();
        // 构建脱敏算法输入数据
        DSObject rawData = new DSObject(Collections.singletonList(cellContent));
        // 使用编号脱敏算法
        return algorithm.service(rawData, 7, privacyLevel);
    }

    private static DSObject desenData(String content, BaseDesenAlgorithm algorithm, int algoNum, int privacyLevel) {


        // 构建脱敏算法输入数据
        DSObject rawData = new DSObject(Collections.singletonList(content));
        // 使用编号脱敏算法
        return algorithm.service(rawData, algoNum, privacyLevel);
    }

    private static int getPrivacyLevel(Map.Entry<CellAddress, ? extends Comment> e, Pattern pattern) {
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

    @Test
    public void regexTest() {

        String cellContent = "个人数字身份类别-独特属性-身份证-身份证号码";
        String patternString = "([\\u4E00-\\u9EA5]+)-([\\u4E00-\\u9EA5]+)-([\\u4E00-\\u9EA5]+)-([\\u4E00-\\u9EA5]+)";
        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher = pattern.matcher(cellContent);
        if (matcher.matches()) {
            String attribute = matcher.group(1);
            System.out.println(attribute);
        } else {
            System.out.println("Failed");
        }



    }

    @Test
    public void readText() throws IOException {
        String filePath = "D:\\Programming\\Desen\\src\\main\\resources\\testfile.txt";
        try {
            String allBytes = new String (Files.readAllBytes(Paths.get(filePath)), StandardCharsets.UTF_8);
            int[][] locations = new int[][] {
                    {5, 7},
                    {24, 26},
                    {37, 39},
            };

            for (int[] location : locations) {
                System.out.println(allBytes.substring(location[0], location[1])); // 假设文件编码为UTF-8
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

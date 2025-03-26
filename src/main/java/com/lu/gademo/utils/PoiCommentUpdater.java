package com.lu.gademo.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lu.gademo.dto.officeComment.DesensitizationOperation;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.usermodel.XWPFComment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTComment;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class PoiCommentUpdater {

    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private static final ObjectMapper mapper = new ObjectMapper();

    public static void modifyCommentsClass(String inputPath, String outputPath, Map<XWPFComment, DesensitizationOperation> commentsMapping) throws Exception {
        try (FileInputStream fis = new FileInputStream(inputPath);
             // 1. 打开文档
             XWPFDocument doc = new XWPFDocument(fis)) {
            modifyCommentsClass(doc, outputPath, commentsMapping);
        }
    }

    public static void modifyCommentsClass(XWPFDocument doc, String outputPath, Map<XWPFComment, DesensitizationOperation> commentsMapping) throws Exception {
        // 2. 获取所有批注，对批注进行处理
        processComments(commentsMapping);
        try (FileOutputStream fos = new FileOutputStream(outputPath)) {
            doc.write(fos);
        }
    }

    public static void modifyCommentsClass(String inputPath, String outputPath) throws Exception {
        try (FileInputStream fis = new FileInputStream(inputPath);
             // 1. 打开文档
             XWPFDocument doc = new XWPFDocument(fis)) {
            // 2. 获取所有批注，对批注进行处理
            processComments(Arrays.asList(doc.getComments()));

            try (FileOutputStream fos = new FileOutputStream(outputPath)) {
                doc.write(fos);
            }
        }
    }

    private static void processComments(Map<XWPFComment, DesensitizationOperation> commentsMapping)
            throws JsonProcessingException {

        for (Map.Entry<XWPFComment, DesensitizationOperation> commentEntry : commentsMapping.entrySet()) {
            String jsonContent = extractCommentContent(commentEntry.getKey());
            // 3. 判断是否为有效的 JSON
            if (isNotValidJson(jsonContent)) continue;
            // 4. 将读取到的内容序列化为JsonNode
            JsonNode rootNode = mapper.readTree(jsonContent);
            addDesensitizationOperation(rootNode, commentEntry.getValue());

            updateCommentContent(commentEntry.getKey(), mapper.writerWithDefaultPrettyPrinter()
                    .writeValueAsString(rootNode));
        }
    }

    private static void processComments(List<XWPFComment> comments) throws Exception {
        for (XWPFComment comment : comments) {
            String jsonContent = extractCommentContent(comment);
            if (isNotValidJson(jsonContent)) continue;

            JsonNode rootNode = mapper.readTree(jsonContent);
//            addDesensitizationOperation(rootNode);

            updateCommentContent(comment, mapper.writerWithDefaultPrettyPrinter()
                    .writeValueAsString(rootNode));
        }
    }

    private static String extractCommentContent(XWPFComment comment) {
        StringBuilder content = new StringBuilder();
        comment.getParagraphs().forEach(p ->
                p.getRuns().forEach(r -> {
                    if (r.getText(0) != null) {
                        content.append(r.getText(0));
                    }
                }));
        return content.toString();
    }

    private static void addDesensitizationOperation(JsonNode rootNode, DesensitizationOperation operation) {
        ((com.fasterxml.jackson.databind.node.ObjectNode) rootNode)
                .set("脱敏操作", new ObjectMapper().valueToTree(operation));
    }

//    private static void addDesensitizationOperation(JsonNode rootNode) {
//        DesensitizationOperation operation = new DesensitizationOperation(
//                new DesensitizationOperation.AlgorithmChosen(
//                        "xx", "xx", "xx",
//                        LocalDateTime.now().format(DATE_FORMATTER)
//                )
//        );
//        ((com.fasterxml.jackson.databind.node.ObjectNode) rootNode)
//                .set("脱敏操作", new ObjectMapper().valueToTree(operation));
//    }

    private static void updateCommentContent(XWPFComment comment, String newContent) {
        // 清空原有内容
        CTComment ctComment = comment.getCtComment();
        ctComment.getDomNode().removeChild(ctComment.getPArray(0).getDomNode());

        // 添加新内容
        XWPFParagraph paragraph = comment.createParagraph();
        String[] lines = newContent.split("\n");

        for (int i = 0; i < lines.length; i++) {
            XWPFRun run = paragraph.createRun();
            run.setText(lines[i]);
            if (i < lines.length - 1) run.addBreak();
        }
    }

    public static boolean isNotValidJson(String json) {
        try {
            PoiCommentUpdater.mapper.readTree(json);
            return false;
        } catch (Exception e) {
            return true;
        }
    }

    public static <T> boolean isNotValidJson(String json, Class<T> clazz) {
        try {
            PoiCommentUpdater.mapper.readValue(json, clazz);
            return false;
        } catch (Exception e) {
            return true;
        }
    }

    public static void main(String[] args) throws Exception {
        XWPFDocument doc = new XWPFDocument(OPCPackage.open("D:\\Programming\\DesenTelecom\\comment.docx"));
        List<XWPFComment> comments = new ArrayList<>(Arrays.asList(doc.getComments()));
        Map<XWPFComment, DesensitizationOperation> commentMapping = new HashMap<>(comments.size());
        for (int i = 0; i < comments.size(); i++) {
            System.out.println("Comment " + i + ": " + extractCommentContent(comments.get(i)));
            commentMapping.put(comments.get(i), new DesensitizationOperation(
                    new DesensitizationOperation.AlgorithmChosen("test", "test",
                            "2", "2", 2, LocalDateTime.now().format(DATE_FORMATTER))));
        }
        modifyCommentsClass(doc, "D:\\Programming\\DesenTelecom\\comment_output.docx", commentMapping);
        System.out.println("文档处理完成");
    }
}
package com.lu.gademo.service.impl;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.poi.xwpf.usermodel.XWPFComment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkupRange;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTP;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTText;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class DocxProcessor {
    private static final String WORD_NAMESPACE_URI =
            "http://schemas.openxmlformats.org/wordprocessingml/2006/main";
    private static final String WORD_NAMESPACE_PREFIX = "w";

    public static class CommentRange {
        public CTMarkupRange commentStart;
        public CTMarkupRange commentEnd;
        public List<XWPFRun> runsInRange = new ArrayList<>();
    }

    public static void main(String[] args) throws Exception {
        processComments("comment2.docx", "output2.docx");
    }

    public static boolean ifHasComment(XWPFDocument document) {
        return document.getComments().length > 0;
    }

    public static void processComments(String inputPath, String outputPath) throws Exception {
        try (FileInputStream fis = new FileInputStream(inputPath);
             XWPFDocument doc = new XWPFDocument(fis)) {
            if (!ifHasComment(doc)) {
                return;
            }
            // 存储批注ID与内容的映射
            Map<String, XWPFComment> commentMap = new HashMap<>();

            // 第一步：收集所有批注
            for (XWPFComment comment : doc.getComments()) {
                commentMap.put(comment.getId(), comment);
            }

            // 第二步：处理正文中的批注引用
            for (XWPFParagraph p : doc.getParagraphs()) {
                processParagraph(p, commentMap);
            }

            // 第三步：保存修改后的文档
            try (FileOutputStream fos = new FileOutputStream(outputPath)) {
                doc.write(fos);
            }
        }
    }

    private static void processParagraph(XWPFParagraph p, Map<String, XWPFComment> commentMap) {
        List<XWPFRun> runs = p.getRuns();
        CTP ctp = p.getCTP();
        Map<BigInteger, CTMarkupRange> startCommentRangeMap = ctp.getCommentRangeStartList().stream().collect(Collectors.toMap(CTMarkupRange::getId, Function.identity()));
        Map<BigInteger, CTMarkupRange> endCommentRangeMap = ctp.getCommentRangeEndList().stream().collect(Collectors.toMap(CTMarkupRange::getId, Function.identity()));

        List<XWPFRun> runResult = new ArrayList<>();
        List<CommentRange> commentRanges = new ArrayList<>();

        startCommentRangeMap.forEach((k, v) -> {

            CTMarkupRange end = endCommentRangeMap.get(k);
            CommentRange commentTemp = new CommentRange();
            commentTemp.commentStart = v;
            commentTemp.commentEnd = end;
            Node currentNode = v.getDomNode();
            while (currentNode.getNextSibling() != end.getDomNode()) {
                currentNode = currentNode.getNextSibling();
                NodeList nodeList = currentNode.getChildNodes();

                for (int i = 0; i < nodeList.getLength(); i++) {
                    Node node = nodeList.item(i);

                    if (node.getNodeType() == Node.ELEMENT_NODE) {
                        Element element = (Element) node;
                        String localName = element.getLocalName();
                        String namespaceURI = element.getNamespaceURI();

                        // 处理 w:t 节点
                        if (WORD_NAMESPACE_URI.equals(namespaceURI) && "t".equals(localName)) {
                            // 尝试从 runResult 中找到包含该节点的 XWPFRun
//                            try {
//                                CTText ctText = (CTText) element;
//                                String textContent = ctText.getStringValue();
//                                System.out.println("Found text in comment range: " + textContent);
//                            } catch (ClassCastException e) {
//                                System.err.println("Could not cast element to CTText: " + e.getMessage());
//                            }
                            for (XWPFRun run : runs) {
                                for (CTText textNode : run.getCTR().getTArray()) {
                                    if (textNode.getDomNode() == node) {
                                        commentTemp.runsInRange.add(run);
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }
            commentRanges.add(commentTemp);
//            CTMarkupRange endRange = endCommentRangeMap.get(k);
//            if (endRange != null) {
//                Node startNode = v.getDomNode();
//                Node endNode = endRange.getDomNode();
//
//                boolean inRange = false;
//                for (XWPFRun run : runs) {
//
//                    System.out.println(run.getText(0));
//                    Node runNode = run.getCTR().getDomNode();
//                    if (runNode == startNode) {
//                        inRange = true;
//                        // Skip the start marker itself
//                        continue;
//                    }
//                    if (runNode == endNode) {
//                        inRange = false;
//                        break;
//                    }
//                    if (inRange) {
//                        System.out.println("Found text in comment range (via XWPFRun iteration): " + run.getText(0));
//                    }
//                }
//            }
//            System.out.println(runResult.size());
//            for (XWPFRun run : runResult) {
//                System.out.println(run.getText(0));
//            }

        });
        System.out.printf("Found %d runs in comment range%n", runResult.size());

        for (CommentRange comment : commentRanges) {
            StringBuilder stringBuilder = new StringBuilder();
            for (XWPFRun run : comment.runsInRange) {
                stringBuilder.append(run.getText(0));
            }
            String a = RandomStringUtils.randomAlphabetic(2 * stringBuilder.toString().length(), 4 * stringBuilder.toString().length());
            System.out.println(a);
            int aLength = a.length();
            int current = 0;
            for (XWPFRun run : comment.runsInRange) {
                int currentRunLength = run.getText(0).length();
                run.setText(a.substring(Math.min(current, aLength), Math.min(current + currentRunLength, aLength)), 0);
                current += currentRunLength;
            }
        }


//        for (int i = 0; i < runs.size(); i++) {
//            XWPFRun run = runs.get(i);
//
//            CTR ctr = run.getCTR();
//            ctr.getCommentReferenceList();
//
//            // 获取批注引用
//            List<CTMarkup> commentReferences = run.getCTR().getCommentReferenceList();
//            for (CTMarkup ref : commentReferences) {
//                String commentId = ref.getId().toString();
//
//                // 获取关联的批注
//                XWPFComment comment = commentMap.get(commentId);
//                if (comment != null) {
//                    // 第四步：处理被批注文本和批注内容
//                    handleCommentContent(run, comment, i, p);
//                }
//            }
//        }
    }

    private static void handleCommentContent(XWPFRun originalRun,
                                             XWPFComment comment,
                                             int runIndex,
                                             XWPFParagraph paragraph) {
        // 获取被批注的原始文本
        String originalText = originalRun.getText(0);
        System.out.println("找到批注关联的原文: " + originalText);

        // 修改被批注的文本（示例：添加前缀）
        originalRun.setText("[已审核] " + originalText, 0);

        // 修改批注内容（示例：添加修改标记）
        XWPFParagraph commentPara = comment.getParagraphs().get(0);
        commentPara.getRuns().forEach(r -> r.setText(
                r.getText(0) + "\n[系统自动处理]", 0));
    }
}
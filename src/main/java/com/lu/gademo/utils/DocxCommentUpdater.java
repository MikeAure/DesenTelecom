package com.lu.gademo.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.lu.gademo.dto.officeComment.DesensitizationOperation;
import com.lu.gademo.dto.officeComment.InformationRecognition;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class DocxCommentUpdater {

    private static final String WORD_NAMESPACE = "http://schemas.openxmlformats.org/wordprocessingml/2006/main";
    private static final String XML_NAMESPACE = "http://www.w3.org/XML/1998/namespace";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static void modifyCommentsClass(String inputDocx, String outputDocx) throws Exception {
        try (ZipInputStream zin = new ZipInputStream(Files.newInputStream(Paths.get(inputDocx)));
             ZipOutputStream zout = new ZipOutputStream(Files.newOutputStream(Paths.get(outputDocx)))) {

            ZipEntry entry;
            while ((entry = zin.getNextEntry()) != null) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int len;
                while ((len = zin.read(buffer)) > -1) {
                    baos.write(buffer, 0, len);
                }
                byte[] data = baos.toByteArray();

                if ("word/comments.xml".equals(entry.getName())) {
                    data = processCommentsXml(data);
                }

                ZipEntry newEntry = new ZipEntry(entry.getName());
                zout.putNextEntry(newEntry);
                zout.write(data);
                zout.closeEntry();
            }
        }
    }

    private static byte[] processCommentsXml(byte[] xmlData) throws Exception {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(new ByteArrayInputStream(xmlData));

        NodeList comments = doc.getElementsByTagNameNS(WORD_NAMESPACE, "comment");
        for (int i = 0; i < comments.getLength(); i++) {
            Element comment = (Element) comments.item(i);
            processCommentElement(comment, doc);
        }

        return transformDocToBytes(doc);
    }

    private static void processCommentElement(Element comment, Document doc) throws Exception {
        try {
            // 提取并删除所有t元素
            List<Element> tElements = getChildElements(comment, WORD_NAMESPACE, "t");
            String combinedText = combineTextElements(tElements);
            removeElements(tElements);

            if (!isValidJson(combinedText)) return;

            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(combinedText);
            JsonNode infoNode = rootNode.path("信息识别");
            String attributeKey = infoNode.path("属性名称").asText("");

//            if (!keywordDict.containsKey(attributeKey)) return;

            // 更新分类信息
//            updateClassification(rootNode, keywordDict.get(attributeKey));
            // 添加脱敏操作
            DesensitizationOperation desensitizationOperation = new DesensitizationOperation(
                    new DesensitizationOperation.AlgorithmChosen("xx", "xx", "xx","xx", 2, LocalDateTime.now().format(DATE_FORMATTER)));
            addDesensitization(rootNode, desensitizationOperation);

            String updatedJson = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(rootNode);
            insertFormattedText(comment, updatedJson, doc);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    private static void updateClassification(JsonNode rootNode, String[] categories) {
//        ObjectNode classification = ((ObjectNode) rootNode).putObject("分类分级")
//                .putObject("属性分类")
//                .put("一级类别", categories[0])
//                .put("二级类别", categories[1])
//                .put("信息分类时间", LocalDateTime.now().format(DATE_FORMATTER));
//    }

    private static void addDesensitization(JsonNode rootNode, DesensitizationOperation desensitizationOperation) {
        ObjectNode desensitization = ((ObjectNode) rootNode);
        JsonNode operation = new ObjectMapper().valueToTree(desensitizationOperation);
        desensitization.set("脱敏操作", operation);
//        desensitization.putObject("算法选择")
//                .put("算法类别", "xx")
//                .put("算法名称", "xx")
//                .put("参数强度", "xx")
//                .put("脱敏操作时间", LocalDateTime.now().format(DATE_FORMATTER));
    }

    private static void insertFormattedText(Element comment, String text, Document doc) {
        Element pElement = findOrCreateElement(comment, WORD_NAMESPACE, "p");
        String[] lines = text.split("\n");

        for (int i = 0; i < lines.length; i++) {
            Element rElement = doc.createElementNS(WORD_NAMESPACE, "r");

            Element tElement = doc.createElementNS(WORD_NAMESPACE, "t");
            tElement.setAttributeNS(XML_NAMESPACE, "space", "preserve");
            tElement.setTextContent(lines[i]);
            rElement.appendChild(tElement);

            if (i < lines.length - 1) {
                Element brElement = doc.createElementNS(WORD_NAMESPACE, "br");
                rElement.appendChild(brElement);
            }

            pElement.appendChild(rElement);
        }
    }

    // Helper methods
    private static List<Element> getChildElements(Element parent, String namespace, String localName) {
        NodeList nodes = parent.getElementsByTagNameNS(namespace, localName);
        List<Element> elements = new ArrayList<>();
        for (int i = 0; i < nodes.getLength(); i++) {
            elements.add((Element) nodes.item(i));
        }
        return elements;
    }

    private static void removeElements(List<Element> elements) {
        for (Element el : elements) {
            el.getParentNode().removeChild(el);
        }
    }

    private static String combineTextElements(List<Element> elements) {
        StringBuilder sb = new StringBuilder();
        for (Element el : elements) {
            sb.append(el.getTextContent());
        }
        return sb.toString();
    }

    private static Element findOrCreateElement(Element parent, String namespace, String localName) {
        NodeList nodes = parent.getElementsByTagNameNS(namespace, localName);
        if (nodes.getLength() > 0) {
            return (Element) nodes.item(0);
        }
        Element newElement = parent.getOwnerDocument().createElementNS(namespace, localName);
        parent.appendChild(newElement);
        return newElement;
    }

    private static boolean isValidJson(String json) {
        try {
            new ObjectMapper().readTree(json);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private static byte[] transformDocToBytes(Document doc) throws Exception {
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(OutputKeys.STANDALONE, "yes");

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        transformer.transform(new DOMSource(doc), new StreamResult(out));
        return out.toByteArray();
    }

    // 示例用法
    public static void main(String[] args) throws Exception {
        Map<String, String[]> keywordDict = new HashMap<>();
//        keywordDict.put("手机号码", new String[]{"移动通信信息类", "通话信息类别"});

        modifyCommentsClass("D:\\Programming\\DesenTelecom\\comment.docx", "output.docx");
        System.out.println("处理完成，已生成新文件");
    }
}
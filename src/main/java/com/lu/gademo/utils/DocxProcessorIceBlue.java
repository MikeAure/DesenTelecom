package com.lu.gademo.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.lu.gademo.dto.officeComment.CategoryAndGrade;
import com.lu.gademo.dto.officeComment.DesensitizationOperation;
import com.lu.gademo.dto.officeComment.InformationRecognition;
import com.lu.gademo.dto.officeComment.WordComment;
import com.lu.gademo.entity.ga.AlgorithmMapping;
import com.lu.gademo.service.AlgorithmMappingDaoService;
import com.spire.doc.Document;
import com.spire.doc.collections.ParagraphCollection;
import com.spire.doc.documents.CommentMark;
import com.spire.doc.documents.Paragraph;
import com.spire.doc.fields.Comment;
import com.spire.doc.fields.TextRange;
import lombok.Data;
import org.springframework.stereotype.Component;


import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
@Data
public class DocxProcessorIceBlue {
    private final ObjectMapper MAPPER;
    private final DateTimeFormatter DATE_FORMATTER;
    private final AlgorithmMappingDaoService algorithmMappingDaoService;
    private final AlgorithmsFactory algorithmsFactory;

    public DocxProcessorIceBlue(AlgorithmsFactory algorithmsFactory, AlgorithmMappingDaoService algorithmMappingDaoService) {
        this.algorithmsFactory = algorithmsFactory;
        this.algorithmMappingDaoService = algorithmMappingDaoService;
        this.MAPPER = new ObjectMapper();
        this.DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    }

    private DSObject desenData(String content, AlgorithmInfo algorithmInfo, int privacyLevel) {
        // 构建脱敏算法输入数据
        DSObject rawData = new DSObject(Collections.singletonList(content));
        // 使用编号脱敏算法
        return algorithmInfo.execute(rawData, privacyLevel);
    }

    private String extractCommentContent(Comment comment) {
        int commentParagraphsNum = comment.getBody().getParagraphs().getCount();
        StringBuilder sb = new StringBuilder();
        // 遍历批注正文中的每个段落
        for (int j = 0; j < commentParagraphsNum; j++) {
            // 获取当前的段落
            Paragraph para = comment.getBody().getParagraphs().get(j);
            for (Object object : para.getChildObjects()) {
                //读取文本
                if (object instanceof TextRange) {
                    TextRange textRange = (TextRange) object;
                    sb.append(textRange.getText());
                }
            }
        }

        return sb.toString();
    }

    private int calculateDesenLevel(CategoryAndGrade categoryAndGrade) {
        double totalGrade = categoryAndGrade.getAttributeGrade().getTotalGrade();
        double currentGrade = categoryAndGrade.getAttributeGrade().getCurrentGrade();
        return (int) Math.ceil(currentGrade / totalGrade * 3);
    }

    private AlgorithmInfo getAlgorithmInfo(InformationRecognition informationRecognition) {
        String attributeName = informationRecognition.getAttributeName();
        AlgorithmMapping algorithmMapping = algorithmMappingDaoService.selectAlgorithmIdByAttributeName(attributeName);
        return algorithmsFactory.getAlgorithmInfoFromId(algorithmMapping.getAlgorithmId());
    }

    private void appendDesensitiveOperation(Comment comment, AlgorithmInfo algorithmInfo, int desenLevel, WordComment wordComment) throws JsonProcessingException {
        DesensitizationOperation desensitizationOperation = new DesensitizationOperation(
                new DesensitizationOperation.AlgorithmChosen(algorithmInfo.getType().getName(),
                        algorithmInfo.getName(), String.valueOf(desenLevel),
                        LocalDateTime.now().format(DATE_FORMATTER)));
        wordComment.setDesensitizationOperation(desensitizationOperation);

        ParagraphCollection paragraphCollection = comment.getBody().getParagraphs();
        while (paragraphCollection.getCount() > 1) {
            comment.getBody().getParagraphs().removeAt(paragraphCollection.getCount() - 1);
        }
        paragraphCollection.get(0).setText(MAPPER.writerWithDefaultPrettyPrinter()
                .writeValueAsString(wordComment));
    }

    private void appendNoneDesensitiveOperation(Comment comment, WordComment wordComment) throws JsonProcessingException {
        DesensitizationOperation desensitizationOperation = new DesensitizationOperation(
                new DesensitizationOperation.AlgorithmChosen(
                        "无", "无", "0",
                        LocalDateTime.now().format(DATE_FORMATTER)));
        wordComment.setDesensitizationOperation(desensitizationOperation);

        ParagraphCollection paragraphCollection = comment.getBody().getParagraphs();
        while (paragraphCollection.getCount() > 1) {
            comment.getBody().getParagraphs().removeAt(paragraphCollection.getCount() - 1);
        }
        paragraphCollection.get(0).setText(MAPPER.writerWithDefaultPrettyPrinter()
                .writeValueAsString(wordComment));
    }

    private void refillTextRanges(String targetString, List<TextRange> textRanges) {
        int targetLength = targetString.length();
        int totalTextRanges = textRanges.size();
        // Calculate the base length and remainder for even distribution
        int baseLength = targetLength / totalTextRanges;
        int remainder = targetLength % totalTextRanges;

        int startIndex = 0;
        for (int i = 0; i < totalTextRanges; i++) {
            int length = baseLength;
            // 将余数个字符平均分配到每个分组中
            if (i < remainder) {
                length++;
            }

            String subTarget = "";
            if (startIndex < targetLength) {
                int endIndex = Math.min(startIndex + length, targetLength);
                subTarget = targetString.substring(startIndex, endIndex);
                startIndex = endIndex;
            }

            if (i < textRanges.size()) {
                textRanges.get(i).setText(subTarget);
            }
        }

        for (int i = targetLength; i < textRanges.size(); i++) {
            textRanges.get(i).setText("");
        }
    }

    private List<TextRange> getCommentedTextRanges(Comment comment) {
        Paragraph para = comment.getOwnerParagraph();
        CommentMark start = comment.getCommentMarkStart();
        CommentMark end = comment.getCommentMarkEnd();
        //获取开始标记和结束标记在段落中的索引
        int indexOfStart = para.getChildObjects().indexOf(start);
        int indexOfEnd = para.getChildObjects().indexOf(end);


        List<TextRange> textRanges = new ArrayList<>();
        //根据索引获取批注的开始标记和结束标记之间的文字
        for (int i = indexOfStart + 1; i < indexOfEnd; i++) {
            if (para.getChildObjects().get(i) instanceof TextRange) {
                TextRange range = (TextRange) para.getChildObjects().get(i);
                textRanges.add(range);
            }
        }
        return textRanges;
    }

    private String getCommentedText(List<TextRange> textRanges) {
        StringBuilder sb = new StringBuilder();
        for (TextRange textRange : textRanges) {
            sb.append(textRange.getText());
        }
        return sb.toString();
    }

    public void processDocx(String inputFilePath, String outputFilePath) throws IOException {
        Document doc = new Document();
        doc.loadFromFile(inputFilePath);

        if (doc.getComments().getCount() == 0) {
            throw new IOException("No comments found in the document");
        }
        for (int i = 0; i < doc.getComments().getCount(); i++) {
            Comment comment = doc.getComments().get(i);
            int commentParagraphsNum = comment.getBody().getParagraphs().getCount();
            String commentContent = extractCommentContent(comment);
            // 对读取到的批注进行序列化
            WordComment wordComment = MAPPER.readValue(commentContent, WordComment.class);
            // 获取信息识别内容
            InformationRecognition informationRecognition = wordComment.getInformationRecognition();
            // 获取分类分级结果
            CategoryAndGrade categoryAndGrade = wordComment.getCategoryAndGrade();

            AlgorithmInfo algorithmInfo = getAlgorithmInfo(informationRecognition);
            int desenLevel = calculateDesenLevel(categoryAndGrade);
            System.out.println("desenLevel: " + desenLevel);
            if (desenLevel <= 0) {
                appendNoneDesensitiveOperation(comment, wordComment);
                continue;
            }
            // 脱敏，修改对应的文字
            modifyCommentedContent(comment, algorithmInfo, desenLevel);
            // 向注释中添加脱敏操作日志
            appendDesensitiveOperation(comment, algorithmInfo, desenLevel, wordComment);
        }

        doc.saveToFile(outputFilePath);
        doc.dispose();
    }

    public void modifyCommentedContent(Comment comment, AlgorithmInfo algorithmInfo, int desenLevel) {

        // 获取属于当前批注的所有TextRange
        List<TextRange> textRanges = getCommentedTextRanges(comment);
        // 获取被批注的文字内容
        String markedText = getCommentedText(textRanges);
        System.out.println("marked text: " + markedText);
        String targetString = "";

        // TODO: 日期类型
        targetString = desenData(markedText, algorithmInfo, desenLevel).getList().get(0).toString();
        // 将脱敏后的文字重新填充到对应的区域
        refillTextRanges(targetString, textRanges);
    }

}

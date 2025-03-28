package com.lu.gademo.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lu.gademo.annotation.SendLog;
import com.lu.gademo.dto.FileInfoDto;
import com.lu.gademo.dto.SendToCourse4Dto;
import com.lu.gademo.dto.officeComment.*;
import com.lu.gademo.entity.FileStorageDetails;
import com.lu.gademo.entity.ga.AlgorithmMapping;
import com.lu.gademo.model.CommonComment;
import com.lu.gademo.model.SpireCommentAdapter;
import com.lu.gademo.model.XlsxPoiCommentAdapter;
import com.lu.gademo.service.AlgorithmMappingDaoService;
import com.spire.doc.Document;
import com.spire.doc.collections.ParagraphCollection;
import com.spire.doc.documents.CommentMark;
import com.spire.doc.documents.Paragraph;
import com.spire.doc.fields.Comment;
import com.spire.doc.fields.TextRange;
import com.spire.pdf.PdfDocument;
import com.spire.pdf.PdfPageBase;
import com.spire.pdf.annotations.PdfAnnotation;
import com.spire.pdf.annotations.PdfAnnotationCollection;
import com.spire.pdf.annotations.PdfTextMarkupAnnotationWidget;
import com.spire.pdf.texts.*;
import com.spire.pdf.widget.PdfPageCollection;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Component
@Data
public class MultiDocumentProcessor {

    private final ObjectMapper MAPPER;
    private final DateTimeFormatter DATE_FORMATTER;
    private final AlgorithmMappingDaoService algorithmMappingDaoService;
    private final AlgorithmsFactory algorithmsFactory;
    private final PdfTextExtractOptions extractOptions;


    public MultiDocumentProcessor(AlgorithmsFactory algorithmsFactory, AlgorithmMappingDaoService algorithmMappingDaoService) {
        this.algorithmsFactory = algorithmsFactory;
        this.algorithmMappingDaoService = algorithmMappingDaoService;
        this.MAPPER = new ObjectMapper();
        this.DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        this.extractOptions = new PdfTextExtractOptions();
    }

    private <T> DSObject desenData(T content, AlgorithmInfo algorithmInfo, int privacyLevel) throws TypeNotPresentException {
        // 构建脱敏算法输入数据
        DSObject rawData = null;
        if (content instanceof String) {
            rawData = new DSObject(Collections.singletonList(content));
            // 使用编号脱敏算法

        } else if (content instanceof Cell) {
            rawData = new DSObject(Collections.singletonList(((Cell) content).getStringCellValue()));

        } else if (content == null) {
            throw new TypeNotPresentException("Content type not supported", null);
        }
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

    /**
     * 填充脱敏操作日志
     * @param comment
     * @param algorithmInfo
     * @param desenLevel
     * @param wordComment
     * @return
     * @throws JsonProcessingException
     */
    private void appendDesensitiveOperation(CommonComment comment, AlgorithmInfo algorithmInfo, int desenLevel, WordComment wordComment) throws JsonProcessingException {
        DesensitizationOperation desensitizationOperation = new DesensitizationOperation(
                new DesensitizationOperation.AlgorithmChosen(algorithmInfo.getType().getName(),
                        algorithmInfo.getName(), String.valueOf(desenLevel),
                        LocalDateTime.now().format(DATE_FORMATTER), algorithmInfo.getId(),
                        algorithmInfo.getParams().get(desenLevel - 1).toString()));
        wordComment.setDesensitizationOperation(desensitizationOperation);
        String content = MAPPER.writerWithDefaultPrettyPrinter()
                .writeValueAsString(wordComment);

        comment.setComment(content);
//        return desensitizationOperation;
    }

    /**
     * 对分级结果小于等于0的批注进行脱敏
     * @param comment
     * @param wordComment
     * @return
     * @throws JsonProcessingException
     */
    private void appendNoneDesensitiveOperation(CommonComment comment, WordComment wordComment) throws JsonProcessingException {
        DesensitizationOperation desensitizationOperation = new DesensitizationOperation(
                new DesensitizationOperation.AlgorithmChosen(
                        "无", "无", "0",
                        LocalDateTime.now().format(DATE_FORMATTER),
                        0, "无"));
        wordComment.setDesensitizationOperation(desensitizationOperation);
        String content = MAPPER.writerWithDefaultPrettyPrinter()
                .writeValueAsString(wordComment);

        comment.setComment(content);

//        return desensitizationOperation;
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

    private DesenInfoAggregate getDesenInfoAggregate(String commentContent) throws JsonProcessingException {
        // 对读取到的批注进行序列化
        WordComment wordComment = MAPPER.readValue(commentContent, WordComment.class);
        // 获取信息识别内容
        InformationRecognition informationRecognition = wordComment.getInformationRecognition();
        // 获取分类分级结果
        CategoryAndGrade categoryAndGrade = wordComment.getCategoryAndGrade();

        AlgorithmInfo algorithmInfo = getAlgorithmInfo(informationRecognition);
        int desenLevel = calculateDesenLevel(categoryAndGrade);
        return new DesenInfoAggregate(desenLevel, algorithmInfo);
    }

    @SendLog
    public ProcessDocumentResult processDocx(FileStorageDetails fileStorageDetails, FileInfoDto fileInfoDto) throws IOException {
        Document doc = new Document();
        String inputFilePath = fileStorageDetails.getRawFilePathString();
        String outputFilePath = fileStorageDetails.getDesenFilePathString();
        doc.loadFromFile(inputFilePath);

        if (doc.getComments().getCount() == 0) {
            throw new IOException("No comments found in the document");
        }
        List<Integer> desenLevels = new ArrayList<>();
        List<Integer> categoryGrades = new ArrayList<>();
        List<WordComment> wordComments = new ArrayList<>();

        for (int i = 0; i < doc.getComments().getCount(); i++) {
            Comment comment = doc.getComments().get(i);
            CommonComment commonComment = new SpireCommentAdapter(comment);
            String commentContent = extractCommentContent(comment).trim();
            System.out.println(commentContent);
            // 对读取到的批注进行序列化
            WordComment wordComment = MAPPER.readValue(commentContent, WordComment.class);
            // 获取信息识别内容
            InformationRecognition informationRecognition = wordComment.getInformationRecognition();
            // 获取分类分级结果
            CategoryAndGrade categoryAndGrade = wordComment.getCategoryAndGrade();
            System.out.println(categoryAndGrade);

            AlgorithmInfo algorithmInfo = getAlgorithmInfo(informationRecognition);
            int desenLevel = calculateDesenLevel(categoryAndGrade);

            System.out.println("desenLevel: " + desenLevel);
            if (desenLevel <= 0) {
                appendNoneDesensitiveOperation(commonComment, wordComment);
//                continue;
            } else {
                desenLevels.add(desenLevel);
                categoryGrades.add(categoryAndGrade.getAttributeGrade().getCurrentGrade());
                // 脱敏，修改对应的文字
                modifyCommentedContent(comment, algorithmInfo, desenLevel);
                // 向注释中添加脱敏操作日志
                appendDesensitiveOperation(commonComment, algorithmInfo, desenLevel, wordComment);
            }
            wordComments.add(wordComment);
        }

        doc.saveToFile(outputFilePath);
        doc.dispose();
        SendToCourse4Dto sendToCourse4Dto = new SendToCourse4Dto();
        SendToCourse4Dto.Class4Data class4Data = new SendToCourse4Dto.Class4Data();
        class4Data.setMinDesenLevel(Collections.min(desenLevels));
        class4Data.setMaxCategoryLevel(Collections.max(categoryGrades));
        class4Data.setGlobalID(fileInfoDto.getGlobalID());
        sendToCourse4Dto.setData(class4Data);
        return new ProcessDocumentResult(sendToCourse4Dto, wordComments);

    }

    public ProcessDocumentResult processXlsx(FileStorageDetails fileStorageDetails, FileInfoDto fileInfoDto) throws Exception {
        Path rawFilePath = Paths.get(fileStorageDetails.getRawFilePathString());
        Path desenFilePath = Paths.get(fileStorageDetails.getDesenFilePathString());
        try (
                InputStream rawFileInputStream = Files.newInputStream(rawFilePath);
                XSSFWorkbook wb = new XSSFWorkbook(rawFileInputStream);
                OutputStream outputStream = Files.newOutputStream(desenFilePath)
        ) {
            // TODO:多个工作表
            int sheetsTotalNum = wb.getNumberOfSheets();
            Sheet sheet = wb.getSheetAt(0);
            // 存储全部Comments
            Map<CellAddress, org.apache.poi.ss.usermodel.Comment> comments = (Map<CellAddress, org.apache.poi.ss.usermodel.Comment>) sheet.getCellComments();
            if (comments == null || comments.entrySet().isEmpty()) {
                throw new IOException("No comments found in the document");
            }
            List<Integer> desenLevels = new ArrayList<>();
            List<Integer> categoryGrades = new ArrayList<>();
            List<WordComment> wordComments = new ArrayList<>();
            // 逐一遍历comments
            for (Map.Entry<CellAddress, ? extends org.apache.poi.ss.usermodel.Comment> e : comments.entrySet()) {
                CellAddress loc = e.getKey();
                org.apache.poi.ss.usermodel.Comment comment = e.getValue();
                CommonComment commonComment = new XlsxPoiCommentAdapter(comment);
                String commentContent = comment.getString().getString();
                // 对读取到的批注进行序列化
                WordComment wordComment = MAPPER.readValue(commentContent, WordComment.class);
                // 获取信息识别内容
                InformationRecognition informationRecognition = wordComment.getInformationRecognition();
                // 获取分类分级结果
                CategoryAndGrade categoryAndGrade = wordComment.getCategoryAndGrade();

                AlgorithmInfo algorithmInfo = getAlgorithmInfo(informationRecognition);
                int desenLevel = calculateDesenLevel(categoryAndGrade);

                // 根据Comment修改算法

                if (desenLevel <= 0) {
                    appendNoneDesensitiveOperation(commonComment, wordComment);
//                continue;
                } else {
                    desenLevels.add(desenLevel);
                    categoryGrades.add(categoryAndGrade.getAttributeGrade().getCurrentGrade());
                    // 脱敏，修改对应的文字
                    Cell cell = sheet.getRow(loc.getRow()).getCell(loc.getColumn());
                    DSObject result = desenData(cell.getStringCellValue(), algorithmInfo, desenLevel);
                    cell.setCellValue(result.getList().get(0).toString());
                    // 向注释中添加脱敏操作日志
                    appendDesensitiveOperation(commonComment, algorithmInfo, desenLevel, wordComment);
                }
                wordComments.add(wordComment);
            }

            // 写入excel
            wb.write(outputStream);
            SendToCourse4Dto sendToCourse4Dto = new SendToCourse4Dto();
            SendToCourse4Dto.Class4Data class4Data = new SendToCourse4Dto.Class4Data();
            class4Data.setMinDesenLevel(Collections.min(desenLevels));
            class4Data.setMaxCategoryLevel(Collections.max(categoryGrades));
            class4Data.setGlobalID(fileInfoDto.getGlobalID());
            sendToCourse4Dto.setData(class4Data);
            return new ProcessDocumentResult(sendToCourse4Dto, wordComments);
        }

    }

    public void modifyCommentedContent(Comment comment, AlgorithmInfo algorithmInfo, int desenLevel) throws TypeNotPresentException {

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


    public List<Map<String, String>> readDataFromPDF(Path path) throws IOException {
        List<Map<String, String>> pdfMapList = new LinkedList<>();
        PdfDocument pdfDocument = new PdfDocument();
        pdfDocument.loadFromFile(path.toString());
        PdfPageCollection pdfPageCollection = pdfDocument.getPages();
        for (Object objPage : pdfPageCollection) {
            PdfPageBase pdfPageBase = (PdfPageBase) objPage;
            PdfAnnotationCollection pdfAnnotationCollection = pdfPageBase.getAnnotationsWidget();
            Map<String, String> pdfMap = new LinkedHashMap<>();
            for (Object objAnnotation : pdfAnnotationCollection) {
                PdfAnnotation pdfAnnotation = (PdfAnnotation) objAnnotation;
                if (pdfAnnotation instanceof PdfTextMarkupAnnotationWidget) {
                    PdfTextMarkupAnnotationWidget pdfTextMarkupAnnotation = (PdfTextMarkupAnnotationWidget) objAnnotation;
//                    String text = pdfTextMarkupAnnotation.getText();
//                    System.out.println(text);
                    Rectangle2D rectangle2D = pdfTextMarkupAnnotation.getBounds();
                    PdfTextExtractor textExtractor = new PdfTextExtractor(pdfPageBase);
//                    String text = pdfPageBase.extractText(rectangle2D);
                    extractOptions.setExtractArea(rectangle2D);
                    String text = textExtractor.extract(extractOptions);
//                    page.getCanvas().drawRectangle(PdfBrushes.getWhite(), rec);
//                    page.getCanvas().drawString(NewText, font, brush, position);
                    String markedText = pdfTextMarkupAnnotation.getText();
                    pdfTextMarkupAnnotation.setText(text);
                    pdfMap.put(text, markedText);

                }
            }
            pdfMapList.add(pdfMap);
        }
        pdfDocument.saveToFile(path.toString());
        return pdfMapList;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DesenInfoAggregate {
        private int desenLevel;
        private AlgorithmInfo algorithmMapping;
    }
}

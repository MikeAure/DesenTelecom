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
import com.lu.gademo.model.PdfCommentAdapter;
import com.lu.gademo.model.SpireCommentAdapter;
import com.lu.gademo.model.XlsxPoiCommentAdapter;
import com.lu.gademo.service.AlgorithmMappingDaoService;
import com.lu.gademo.service.FileStorageService;
import com.spire.doc.Document;
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
import lombok.Data;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.concurrent.ThreadLocalRandom;

@Component
@Data
public class MultiDocumentProcessor {

    private final ObjectMapper MAPPER;
    private final DateTimeFormatter DATE_FORMATTER;
    private final AlgorithmMappingDaoService algorithmMappingDaoService;
    private final AlgorithmsFactory algorithmsFactory;
    private final PdfTextExtractOptions extractOptions;
    private final ThreadLocalRandom RANDOM;
    private final FileStorageService fileStorageService;


    @Autowired
    public MultiDocumentProcessor(AlgorithmsFactory algorithmsFactory, AlgorithmMappingDaoService algorithmMappingDaoService, FileStorageService fileStorageService) {
        this.algorithmsFactory = algorithmsFactory;
        this.algorithmMappingDaoService = algorithmMappingDaoService;
        this.MAPPER = new ObjectMapper();
        this.DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        this.extractOptions = new PdfTextExtractOptions();
        RANDOM = ThreadLocalRandom.current();
//        RANDOM.setSeed(System.currentTimeMillis());
        this.fileStorageService = fileStorageService;
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
     *
     * @param comment
     * @param algorithmInfo
     * @param desenLevel
     * @param wordComment
     * @return
     * @throws JsonProcessingException
     */
    private void appendDesensitiveOperation(CommonComment comment, AlgorithmInfo algorithmInfo, int desenLevel, WordComment wordComment) throws JsonProcessingException {
        DesensitizationOperation desensitizationOperation = null;
        if (desenLevel > 0) {
            desensitizationOperation = new DesensitizationOperation(
                    new DesensitizationOperation.AlgorithmChosen(algorithmInfo.getType().getName(),
                            algorithmInfo.getName(), String.valueOf(desenLevel),
                            LocalDateTime.now().format(DATE_FORMATTER), algorithmInfo.getId(),
                            algorithmInfo.getParams().get(desenLevel - 1).toString()));
        } else {
            desensitizationOperation = new DesensitizationOperation(
                    new DesensitizationOperation.AlgorithmChosen(
                            "无", "无", "0",
                            LocalDateTime.now().format(DATE_FORMATTER),
                            0, "无"));
        }
        wordComment.setDesensitizationOperation(desensitizationOperation);
        String content = MAPPER.writerWithDefaultPrettyPrinter()
                .writeValueAsString(wordComment);

        comment.setComment(content);
//        return desensitizationOperation;
    }

    /**
     * 对分级结果小于等于0的批注进行脱敏
     *
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
            String markedContent = informationRecognition.getContent();
            // 获取分类分级结果
            CategoryAndGrade categoryAndGrade = wordComment.getCategoryAndGrade();
            DesensitizationEvaluation desensitizationEvaluation = wordComment.getDesensitizationEvaluation();
            DesensitizationOperation desensitizationOperation = wordComment.getDesensitizationOperation();
            System.out.println(categoryAndGrade);

            AlgorithmInfo algorithmInfo = getAlgorithmInfo(informationRecognition);
            int desenLevel = calculateDesenLevel(categoryAndGrade);
            categoryGrades.add(categoryAndGrade.getAttributeGrade().getCurrentGrade());
            System.out.println("desenLevel: " + desenLevel);
            // 如果没有脱敏评估结果
            if (desensitizationEvaluation == null) {
                // 进行第一次脱敏
                // 如果读取到的分类分级结果小于0，不进行脱敏
                desenLevel = updatePrivacyLevel(desenLevel, fileInfoDto.isRandom());
//                continue;
                desenLevels.add(desenLevel);
                // 脱敏，修改对应的文字
                modifyCommentedContent(comment, algorithmInfo, desenLevel);
                // 向注释中添加脱敏操作日志
                appendDesensitiveOperation(commonComment, algorithmInfo, desenLevel, wordComment);
                wordComments.add(wordComment);
            } else {
                // 如果有脱敏评估结果且执行过脱敏
                if (desensitizationOperation != null && desensitizationOperation.getAlgorithmChosen() != null) {
                    DesensitizationEvaluation.EvaluationResult evaluationResult = desensitizationEvaluation.getEvaluationResult();
                    DesensitizationOperation.AlgorithmChosen algorithmChosen = desensitizationOperation.getAlgorithmChosen();

                    int magnitude = Integer.parseInt(algorithmChosen.getParameterMagnitude());
                    // 如果评估失败
                    if (!evaluationResult.isEvalResult()) {
                        desenLevel = magnitude < 3 ? magnitude + 1 : 3;
                        desenLevels.add(desenLevel);
                        // 脱敏，修改对应的文字
                        redesenModifyCommentedContent(markedContent, comment, algorithmInfo, desenLevel);
                        // 向注释中添加脱敏操作日志
                        appendDesensitiveOperation(commonComment, algorithmInfo, desenLevel, wordComment);
                    }
                    // 如果评估成功，对当前批注的文本不做任何修改
                    else {
                        desenLevels.add(magnitude);
                        // 脱敏，修改对应的文字
                        redesenModifyCommentedContent(markedContent, comment, algorithmInfo, magnitude);
                        // 向注释中添加脱敏操作日志
                        appendDesensitiveOperation(commonComment, algorithmInfo, magnitude, wordComment);
                    }
                    wordComments.add(wordComment);
                } else {
                    throw new IOException("No desensitization operation found in the comment");
                }
            }
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

    @SendLog
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
                Cell cell = sheet.getRow(loc.getRow()).getCell(loc.getColumn());
                org.apache.poi.ss.usermodel.Comment comment = e.getValue();
                CommonComment commonComment = new XlsxPoiCommentAdapter(comment);
                String commentContent = comment.getString().getString();
                // 对读取到的批注进行序列化
                WordComment wordComment = MAPPER.readValue(commentContent, WordComment.class);
                // 获取信息识别内容
                InformationRecognition informationRecognition = wordComment.getInformationRecognition();
                String markedContent = informationRecognition.getContent();
                // 获取分类分级结果
                CategoryAndGrade categoryAndGrade = wordComment.getCategoryAndGrade();
                DesensitizationEvaluation desensitizationEvaluation = wordComment.getDesensitizationEvaluation();
                DesensitizationOperation desensitizationOperation = wordComment.getDesensitizationOperation();
                System.out.println(categoryAndGrade);
                CellStyle style = wb.createCellStyle();
                AlgorithmInfo algorithmInfo = getAlgorithmInfo(informationRecognition);
                int desenLevel = calculateDesenLevel(categoryAndGrade);
                categoryGrades.add(categoryAndGrade.getAttributeGrade().getCurrentGrade());
                // 根据Comment修改算法
                // 如果没有脱敏评估结果
                if (desensitizationEvaluation == null) {
                    desenLevel = updatePrivacyLevel(desenLevel, fileInfoDto.isRandom());
                    desenLevels.add(desenLevel);
                    if (desenLevel > 0) {
                        DSObject result = desenData(cell.getStringCellValue(), algorithmInfo, desenLevel);
                        System.out.println(result.getList().get(0).toString());
                        System.out.println(cell.getCellType());
                        cell.setCellStyle(style);
                        cell.setCellValue(new XSSFRichTextString(result.getList().get(0).toString()));
//                        cell.setCellValue(result.getList().get(0).toString());
                    }
                    appendDesensitiveOperation(commonComment, algorithmInfo, desenLevel, wordComment);
                    wordComments.add(wordComment);
                } else {
                    // 如果有脱敏评估结果且执行过脱敏
                    if (desensitizationOperation != null && desensitizationOperation.getAlgorithmChosen() != null) {
                        DesensitizationEvaluation.EvaluationResult evaluationResult = desensitizationEvaluation.getEvaluationResult();
                        DesensitizationOperation.AlgorithmChosen algorithmChosen = desensitizationOperation.getAlgorithmChosen();

                        int magnitude = Integer.parseInt(algorithmChosen.getParameterMagnitude());
                        // 如果评估失败
                        if (!evaluationResult.isEvalResult()) {
                            // 提升脱敏等级
                            desenLevel = magnitude < 3 ? magnitude + 1 : 3;
                            desenLevels.add(desenLevel);
                            // 脱敏，修改对应的文字
                            DSObject result = desenData(markedContent, algorithmInfo, desenLevel);
                            cell.setCellValue(result.getList().get(0).toString());
                            // 向注释中添加脱敏操作日志
                            appendDesensitiveOperation(commonComment, algorithmInfo, desenLevel, wordComment);
                        }
                        // 如果评估成功
                        else {
                            desenLevels.add(magnitude);
                            DSObject result = desenData(markedContent, algorithmInfo, magnitude);
                            cell.setCellValue(result.getList().get(0).toString());
                            // 向注释中添加脱敏操作日志
                            appendDesensitiveOperation(commonComment, algorithmInfo, magnitude, wordComment);
                        }
                        wordComments.add(wordComment);
                    } else {
                        throw new IOException("No desensitization operation found in the comment");
                    }
                }
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

    /**
     * 更新脱敏等级，根据参数决定是否随机产生错误
     *
     * @param privacyLevel 原始脱敏等级
     * @param ifRandom     是否随机产生错误开关
     * @return 新脱敏等级
     */
    private int updatePrivacyLevel(int privacyLevel, boolean ifRandom) {
        if (privacyLevel <= 0) {
            return 0;
        }
        if (ifRandom && RANDOM.nextDouble() > 0.7) {
            return 0;
        }
        return privacyLevel;
    }

    public void modifyCommentedContent(Comment comment, AlgorithmInfo algorithmInfo, int desenLevel) throws TypeNotPresentException {
        if (desenLevel <= 0) return;
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

    public void redesenModifyCommentedContent(String markedText, Comment comment, AlgorithmInfo algorithmInfo, int desenLevel) throws TypeNotPresentException {
        if (desenLevel <= 0) return;
        // 获取属于当前批注的所有TextRange
        List<TextRange> textRanges = getCommentedTextRanges(comment);
        // 获取被批注的文字内容
        System.out.println("marked text: " + markedText);
        String targetString = "";

        // TODO: 日期类型
        targetString = desenData(markedText, algorithmInfo, desenLevel).getList().get(0).toString();
        // 将脱敏后的文字重新填充到对应的区域
        refillTextRanges(targetString, textRanges);
    }

    public List<List<String>> readDataFromPDF(Path path) throws IOException {
        List<List<String>> pdfMapList = new LinkedList<>();
        PdfDocument pdfDocument = new PdfDocument();
        pdfDocument.loadFromFile(path.toString());
        PdfPageCollection pdfPageCollection = pdfDocument.getPages();
        for (Object objPage : pdfPageCollection) {
            PdfPageBase pdfPageBase = (PdfPageBase) objPage;
            PdfTextExtractor textExtractor = new PdfTextExtractor(pdfPageBase);
            PdfAnnotationCollection pdfAnnotationCollection = pdfPageBase.getAnnotationsWidget();
            System.out.println(pdfPageBase.getAnnotationsWidget().getCount());
            List<String> pdfMap = new ArrayList<>();
            for (Object objAnnotation : pdfAnnotationCollection) {
                PdfAnnotation pdfAnnotation = (PdfAnnotation) objAnnotation;
                if (pdfAnnotation instanceof PdfTextMarkupAnnotationWidget) {
                    PdfTextMarkupAnnotationWidget pdfTextMarkupAnnotation = (PdfTextMarkupAnnotationWidget) objAnnotation;
                    Rectangle2D rectangle2D = pdfTextMarkupAnnotation.getBounds();
                    extractOptions.setExtractArea(rectangle2D);
                    String text = textExtractor.extract(extractOptions);
//                    String text = pdfPageBase.extractText(rectangle2D);
                    String comment = pdfTextMarkupAnnotation.getText();
                    pdfMap.add(comment);
                }
            }
            pdfMapList.add(pdfMap);
        }
        return pdfMapList;
    }

    @SendLog
    public ProcessDocumentResult processPdf(FileStorageDetails fileStorageDetails, FileInfoDto fileInfoDto) throws IOException {
        String lastDesenedFile = fileStorageDetails.getRawFilePathString();
        String desenFilePath = fileStorageDetails.getDesenFilePathString();
        // 保存脱敏前文件信息
        FileStorageDetails desenFileStorageDetails = fileInfoDto.isRedesen() ?
                fileStorageService.saveRawFileWithDesenInfoByLog(fileInfoDto.getEvaluationLog()) :
                fileStorageDetails;
        // 脱敏需求
        List<List<String>> commentList = readDataFromPDF(Paths.get(lastDesenedFile));
        PdfDocument pdfDocument = new PdfDocument();
        pdfDocument.loadFromFile(desenFileStorageDetails.getRawFilePathString());
//        PdfPageCollection pdfPageCollection = pdfDocument.getPages();

        List<Integer> desenLevels = new ArrayList<>();
        List<Integer> categoryGrades = new ArrayList<>();
        List<WordComment> wordComments = new ArrayList<>();

        if (commentList.size() != pdfDocument.getPages().getCount()) {
            throw new IOException("The number of comments does not match the number of pages in the document");
        }

        for (int i = 0; i < commentList.size(); i++) {
            PdfPageBase pdfPageBase = pdfDocument.getPages().get(i);
            PdfTextReplaceOptions textReplaceOptions = new PdfTextReplaceOptions();

            // 指定文本替换的选项
            textReplaceOptions.setReplaceType(EnumSet.of(ReplaceActionType.IgnoreCase));
            textReplaceOptions.setReplaceType(EnumSet.of(ReplaceActionType.WholeWord));

            PdfTextReplacer textReplacer = new PdfTextReplacer(pdfPageBase);
            textReplacer.setOptions(textReplaceOptions);
            List<PdfTextMarkupAnnotationWidget> pdfTextMarkupAnnotationWidgets = new ArrayList<>();
            for (Object object : pdfPageBase.getAnnotationsWidget()) {
                if (object instanceof PdfTextMarkupAnnotationWidget) {
                    pdfTextMarkupAnnotationWidgets.add((PdfTextMarkupAnnotationWidget) object);
                }
            }

            if (commentList.get(i).size() != pdfTextMarkupAnnotationWidgets.size()) {
                throw new IOException("The number of comments does not match the number of annotations in the document");
            }
            Iterator<PdfTextMarkupAnnotationWidget> pdfAnnotationIterator = pdfTextMarkupAnnotationWidgets.iterator();
            Iterator<String> commentIterator = commentList.get(i).iterator();
            while (pdfAnnotationIterator.hasNext() && commentIterator.hasNext()) {
                PdfTextMarkupAnnotationWidget pdfTextMarkupAnnotation = pdfAnnotationIterator.next();
                String commentContent = commentIterator.next();
                PdfCommentAdapter commonComment = new PdfCommentAdapter(pdfTextMarkupAnnotation);
                System.out.println(commentContent);
// 对读取到的批注进行序列化
                WordComment wordComment = MAPPER.readValue(commentContent, WordComment.class);
                // 获取信息识别内容
                InformationRecognition informationRecognition = wordComment.getInformationRecognition();
                String markedContent = informationRecognition.getContent();
                // 获取分类分级结果
                CategoryAndGrade categoryAndGrade = wordComment.getCategoryAndGrade();
                DesensitizationEvaluation desensitizationEvaluation = wordComment.getDesensitizationEvaluation();
                DesensitizationOperation desensitizationOperation = wordComment.getDesensitizationOperation();
                System.out.println(categoryAndGrade);
                AlgorithmInfo algorithmInfo = getAlgorithmInfo(informationRecognition);

                int desenLevel = calculateDesenLevel(categoryAndGrade);
                categoryGrades.add(categoryAndGrade.getAttributeGrade().getCurrentGrade());
                // 根据Comment修改算法
                // 如果没有脱敏评估结果
                if (desensitizationEvaluation == null) {
                    desenLevel = updatePrivacyLevel(desenLevel, fileInfoDto.isRandom());
                    desenLevels.add(desenLevel);
                    if (desenLevel > 0) {
                        DSObject result = desenData(markedContent, algorithmInfo, desenLevel);
                        textReplacer.replaceText(markedContent, result.getList().get(0).toString());
                    }
                    appendDesensitiveOperation(commonComment, algorithmInfo, desenLevel, wordComment);
                    wordComments.add(wordComment);
                } else {
                    // 如果有脱敏评估结果且执行过脱敏
                    if (desensitizationOperation != null && desensitizationOperation.getAlgorithmChosen() != null) {
                        DesensitizationEvaluation.EvaluationResult evaluationResult = desensitizationEvaluation.getEvaluationResult();
                        DesensitizationOperation.AlgorithmChosen algorithmChosen = desensitizationOperation.getAlgorithmChosen();

                        int magnitude = Integer.parseInt(algorithmChosen.getParameterMagnitude());
                        // 如果评估失败
                        if (!evaluationResult.isEvalResult()) {
                            // 提升脱敏等级
                            desenLevel = magnitude < 3 ? magnitude + 1 : 3;
                            desenLevels.add(desenLevel);
                            // 脱敏，修改对应的文字
                            DSObject result = desenData(markedContent, algorithmInfo, desenLevel);
                            textReplacer.replaceText(markedContent, result.getList().get(0).toString());
                            // 向注释中添加脱敏操作日志
                            appendDesensitiveOperation(commonComment, algorithmInfo, desenLevel, wordComment);
                        }
                        // 如果评估成功，执行原有的脱敏策略
                        else {
                            desenLevels.add(magnitude);
                            DSObject result = desenData(markedContent, algorithmInfo, magnitude);
                            textReplacer.replaceText(markedContent, result.getList().get(0).toString());
                            // 向注释中添加脱敏操作日志
                            appendDesensitiveOperation(commonComment, algorithmInfo, magnitude, wordComment);
                        }
                        wordComments.add(wordComment);
                    } else {
                        throw new IOException("No desensitization operation found in the comment");
                    }

                }
            }
        }


//        for (Object objPage : pdfPageCollection) {
//            PdfPageBase pdfPageBase = (PdfPageBase) objPage;
//            PdfTextExtractor textExtractor = new PdfTextExtractor(pdfPageBase);
//            PdfAnnotationCollection pdfAnnotationCollection = pdfPageBase.getAnnotationsWidget();
//            // 创建一个PdfTextReplaceOptions对象
//            PdfTextReplaceOptions textReplaceOptions = new PdfTextReplaceOptions();
//
//            // 指定文本替换的选项
//            textReplaceOptions.setReplaceType(EnumSet.of(ReplaceActionType.IgnoreCase));
//            textReplaceOptions.setReplaceType(EnumSet.of(ReplaceActionType.WholeWord));
//
//            PdfTextReplacer textReplacer = new PdfTextReplacer(pdfPageBase);
//            textReplacer.setOptions(textReplaceOptions);
//
//            Map<String, String> pdfMap = new LinkedHashMap<>();
//            for (Object objAnnotation : pdfAnnotationCollection) {
//                PdfAnnotation pdfAnnotation = (PdfAnnotation) objAnnotation;
//                if (pdfAnnotation instanceof PdfTextMarkupAnnotationWidget) {
//                    PdfTextMarkupAnnotationWidget pdfTextMarkupAnnotation = (PdfTextMarkupAnnotationWidget) objAnnotation;
//                    PdfCommentAdapter commonComment = new PdfCommentAdapter(pdfTextMarkupAnnotation);
////                    pdfTextMarkupAnnotation.setTextMarkupColor(new PdfRGBColor(Color.WHITE));
////                    String text = pdfTextMarkupAnnotation.getText();
////                    System.out.println(text);
//                    Rectangle2D rectangle2D = pdfTextMarkupAnnotation.getBounds();
//                    extractOptions.setExtractArea(rectangle2D);
//                    String markedText = textExtractor.extract(extractOptions);
//                    String commentContent = pdfTextMarkupAnnotation.getText();
//
//                    // 对读取到的批注进行序列化
//                    WordComment wordComment = MAPPER.readValue(commentContent, WordComment.class);
//                    // 获取信息识别内容
//                    InformationRecognition informationRecognition = wordComment.getInformationRecognition();
//                    String markedContent = informationRecognition.getContent();
//                    // 获取分类分级结果
//                    CategoryAndGrade categoryAndGrade = wordComment.getCategoryAndGrade();
//                    DesensitizationEvaluation desensitizationEvaluation = wordComment.getDesensitizationEvaluation();
//                    DesensitizationOperation desensitizationOperation = wordComment.getDesensitizationOperation();
//
//                    AlgorithmInfo algorithmInfo = getAlgorithmInfo(informationRecognition);
//
//                    int desenLevel = calculateDesenLevel(categoryAndGrade);
//                    categoryGrades.add(categoryAndGrade.getAttributeGrade().getCurrentGrade());
//                    // 根据Comment修改算法
//                    // 如果没有脱敏评估结果
//                    if (desensitizationEvaluation == null) {
//                        desenLevel = updatePrivacyLevel(desenLevel, fileInfoDto.isRandom());
//                        desenLevels.add(desenLevel);
//                        if (desenLevel > 0) {
//                            DSObject result = desenData(markedContent, algorithmInfo, desenLevel);
//                            textReplacer.replaceText(markedContent, result.getList().get(0).toString());
//                        }
//                        appendDesensitiveOperation(commonComment, algorithmInfo, desenLevel, wordComment);
//                        wordComments.add(wordComment);
//                    } else {
//                        // 如果有脱敏评估结果且执行过脱敏
//                        if (desensitizationOperation != null && desensitizationOperation.getAlgorithmChosen() != null) {
//                            DesensitizationEvaluation.EvaluationResult evaluationResult = desensitizationEvaluation.getEvaluationResult();
//                            DesensitizationOperation.AlgorithmChosen algorithmChosen = desensitizationOperation.getAlgorithmChosen();
//
//                            int magnitude = Integer.parseInt(algorithmChosen.getParameterMagnitude());
//                            // 如果评估失败
//                            if (!evaluationResult.isEvalResult()) {
//                                // 提升脱敏等级
//                                desenLevel = magnitude < 3 ? magnitude + 1 : 3;
//                                desenLevels.add(desenLevel);
//                                // 脱敏，修改对应的文字
//                                DSObject result = desenData(markedContent, algorithmInfo, desenLevel);
//                                textReplacer.replaceText(markedContent, result.getList().get(0).toString());
//                                // 向注释中添加脱敏操作日志
//                                appendDesensitiveOperation(commonComment, algorithmInfo, desenLevel, wordComment);
//                            }
//                            // 如果评估成功，对当前批注的文本不做任何修改
//                            else {
//                                desenLevels.add(magnitude);
//                            }
//                            wordComments.add(wordComment);
//                        } else {
//                            throw new IOException("No desensitization operation found in the comment");
//                        }
//
//
//                        //创建专色
////                    PdfRGBColor pdfRGBColor = new PdfRGBColor(new Color(255,255,255));
////                    PdfSeparationColorSpace cs = new PdfSeparationColorSpace("MySpotColor",pdfRGBColor);
////                    PdfSeparationColor color = new PdfSeparationColor(cs, 1f);
////
////                    //创建truetype字体
////                    PdfTrueTypeFont font = new PdfTrueTypeFont(new Font("宋体", Font.PLAIN, 10), true);
////
////                    PdfRGBColor pdfRGBColor2 = new PdfRGBColor(new Color(255,255,255));
////                    PdfSeparationColorSpace cs2 = new PdfSeparationColorSpace("MySpotColor2",pdfRGBColor2);
////                    PdfSeparationColor color2 = new PdfSeparationColor(cs2, 1f);
////                    // 在覆盖的区域上添加新文字
//////                    pdfPageBase.getCanvas().drawString(NewText, font, brush, position);
////
////                    //根据颜色创建画刷
////                    PdfSolidBrush brush = new PdfSolidBrush(color);
////                    PdfSolidBrush brush2 = new PdfSolidBrush(color2);
////
////                    PdfPen pen = new PdfPen(new PdfRGBColor(Color.black),0.1);
////                    Rectangle2D.Double rect1 = new Rectangle2D.Double(rectangle2D.getX(), rectangle2D.getY(), rectangle2D.getWidth(), rectangle2D.getHeight());
//////                    Rectangle2D.Float rect1 = new Rectangle2D.Float(0, 20, 120, 50);
//////                    PdfLinearGradientBrush linearGradientBrush = new PdfLinearGradientBrush(rectangle2D,new PdfRGBColor(Color.white),new PdfRGBColor(Color.blue),PdfLinearGradientMode.Horizontal);
//////                    pdfPageBase.getCanvas().drawRectangle(pen, linearGradientBrush, rectangle2D);
////                    pdfPageBase.getCanvas().drawRectangle(pen, PdfBrushes.getWhite(), rect1);
////                    pdfPageBase.getCanvas().drawString(markedText + "114514", font, brush,
////                            new Point2D.Double(rectangle2D.getX() + 10, rectangle2D.getY()));
//                        pdfMap.put(commentContent, markedText);
//                    }
//                }
//
//            }
//
//        }
            pdfDocument.saveToFile(desenFilePath);
            SendToCourse4Dto sendToCourse4Dto = new SendToCourse4Dto();
            SendToCourse4Dto.Class4Data class4Data = new SendToCourse4Dto.Class4Data();
            class4Data.setMinDesenLevel(Collections.min(desenLevels));
            class4Data.setMaxCategoryLevel(Collections.max(categoryGrades));
            class4Data.setGlobalID(fileInfoDto.getGlobalID());
            sendToCourse4Dto.setData(class4Data);
            return new ProcessDocumentResult(sendToCourse4Dto, wordComments);
        }
    }

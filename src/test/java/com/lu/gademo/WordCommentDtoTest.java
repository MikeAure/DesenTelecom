package com.lu.gademo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lu.gademo.dto.officeComment.*;
import org.junit.jupiter.api.Test;

public class WordCommentDtoTest {
    final private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testInformationRecognitionToValue() throws Exception {
        String json = "{\"内容类型\":\"type\",\"内容\":\"content\",\"属性名称\":\"name\",\"信息识别时间\":\"2021-08-01 00:00:00\"}";
        InformationRecognition informationRecognition = objectMapper.readValue(json, InformationRecognition.class);
        System.out.println(informationRecognition);
    }

    @Test
    void testInformationRecognitionToJson() throws Exception {
        InformationRecognition informationRecognition = new InformationRecognition("type", "content", "name", "2021-08-01 00:00:00");
        String json = objectMapper.writeValueAsString(informationRecognition);
        System.out.println(json);
    }

    @Test
    void testCategoryAndGradeToValue() throws Exception {
        String json = "{\"属性分类\":" +
                "{\"一级类别\": \"移动通信信息类\",\"二级类别\": \"通话信息类别\",\"信息分类时间\": \"2021-08-01 11:45\"}," +
                "\"属性分级\":" +
                "{\"总级数\": 10,\"当前级数\": 2,\"信息分级时间\": \"2021-08-01 11:45\"}" +
                "}";
        CategoryAndGrade categoryAndGrade = objectMapper.readValue(json, CategoryAndGrade.class);
        System.out.println(categoryAndGrade);
    }

    @Test
    void testCategoryAndGradeToJson() throws Exception {
        CategoryAndGrade categoryAndGrade = new CategoryAndGrade(
                new CategoryAndGrade.AttributeCategory("移动通信信息类", "通话信息类别", "2021-08-01 11:45"),
                new CategoryAndGrade.AttributeGrade(10, 2, "2021-08-01 11:45"));
        String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(categoryAndGrade);
        System.out.println(json);
    }

    @Test
    void testDesensitizationOperationToValue() throws JsonProcessingException {
        String json = " {\n" +
                "    \"算法选择\": {\n" +
                "      \"算法类别\": \"xx\",\n" +
                "      \"算法名称\": \"xx\",\n" +
                "      \"参数强度\": \"xx\",\n" +
                "      \"脱敏操作时间\": \"1919-08-10 11:45\"\n" +
                "    }\n" +
                "  }\n";
        DesensitizationOperation desensitizationOperation = objectMapper.readValue(json, DesensitizationOperation.class);
        System.out.println(desensitizationOperation);
    }

    @Test
    void testDesensitizationOperationToJson() throws JsonProcessingException {
        DesensitizationOperation desensitizationOperation = new DesensitizationOperation(
                new DesensitizationOperation.AlgorithmChosen("xx", "xx", "xx", "1919-08-10 11:45"));
        String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(desensitizationOperation);
        System.out.println(json);
    }

    @Test
    void testDesensitizationEvaluationToValue() throws JsonProcessingException {
        String json = " {\n" +
                "    \"评估结果\": {\n" +
                "      \"评估方法\": \"xx\",\n" +
                "      \"评估结论\": \"xx\",\n" +
                "      \"脱敏评估时间\": \"1919-08-10 11:45\"\n" +
                "    }\n" +
                "  }\n";
        DesensitizationEvaluation evaluation = objectMapper.readValue(json, DesensitizationEvaluation.class);
        System.out.println(evaluation);
    }
    @Test
    void testDesensitizationEvaluationToJson() throws JsonProcessingException {
        DesensitizationEvaluation evaluation = new DesensitizationEvaluation(
                new DesensitizationEvaluation.EvaluationResult("xx", "xx", "1919-08-10 11:45"));
        String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(evaluation);
        System.out.println(json);
    }

    @Test
    void testWordCommentToJson() throws JsonProcessingException {
        WordComment wordComment = new WordComment(
                new InformationRecognition("type", "content", "name", "2021-08-01 00:00"),
                new CategoryAndGrade(
                        new CategoryAndGrade.AttributeCategory("移动通信信息类", "通话信息类别", "2021-08-01 11:45"),
                        new CategoryAndGrade.AttributeGrade(10, 2, "2021-08-01 11:45")),
                new DesensitizationOperation(
                        new DesensitizationOperation.AlgorithmChosen("xx", "xx", "xx", "1919-08-10 11:45")),
                new DesensitizationEvaluation(
                        new DesensitizationEvaluation.EvaluationResult("xx", "xx", "1919-08-10 11:45")));
        String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(wordComment);
        System.out.println(json);
    }

    @Test
    void testWordCommentToValue() throws JsonProcessingException {
        String json = "{\n" +
                "  \"信息识别\": {\n" +
                "    \"内容类型\": \"数字\",\n" +
                "    \"内容\": \"10132118876\",\n" +
                "    \"属性名称\": \"手机号码\",\n" +
                "    \"信息识别时间\": \"2021-08-01 11:45\"\n" +
                "  },\n" +
                "\n" +
                "  \"分类分级\": {\n" +
                "    \"属性分类\": {\n" +
                "      \"一级类别\": \"移动通信信息类\",\n" +
                "      \"二级类别\": \"通话信息类别\",\n" +
                "      \"信息分类时间\": \"2021-08-01 11:45\"\n" +
                "    },\n" +
                "    \"属性分级\": {\n" +
                "      \"总级数\": 10,\n" +
                "      \"当前级数\": 2,\n" +
                "      \"信息分级时间\": \"2021-08-01 11:45\"\n" +
                "    }\n" +
                "  },\n" +
                "\n" +
                "  \"脱敏操作\": {\n" +
                "    \"算法选择\": {\n" +
                "      \"算法类别\": \"xx\",\n" +
                "      \"算法名称\": \"xx\",\n" +
                "      \"参数强度\": \"xx\",\n" +
                "      \"脱敏操作时间\": \"2021-08-01 11:45\"\n" +
                "    }\n" +
                "  },\n" +
                "\n" +
                "  \"脱敏效果评估\": {\n" +
                "    \"评估结果\": {\n" +
                "      \"评估方法\": \"xx\",\n" +
                "      \"评估结论\": \"xx\",\n" +
                "      \"脱敏评估时间\": \"2021-08-01 11:45\"\n" +
                "    }\n" +
                "  }\n" +
                "}\n";
        WordComment wordComment = objectMapper.readValue(json, WordComment.class);
        System.out.println(wordComment);
    }
}

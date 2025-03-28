package com.lu.gademo.dto.officeComment;

import com.lu.gademo.dto.SendToCourse4Dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class ProcessDocumentResult {
    SendToCourse4Dto sendToCourse;
    List<WordComment> wordComments;
//    List<CategoryAndGrade> categoryAndGrades;
//    List<InformationRecognition> recognitions;
}

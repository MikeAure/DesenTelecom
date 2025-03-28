package com.lu.gademo.model;

import org.apache.poi.xssf.usermodel.XSSFRichTextString;

/**
 * Xlsx批注包装器
 */
public class XlsxPoiCommentAdapter implements CommonComment{
    private final org.apache.poi.ss.usermodel.Comment comment;
    public XlsxPoiCommentAdapter(org.apache.poi.ss.usermodel.Comment comment) {
        this.comment = comment;
    }

    @Override
    public void setComment(String content) {
        comment.setString(new XSSFRichTextString(content));
    }
}

package com.lu.gademo.model;

import com.spire.pdf.annotations.PdfTextMarkupAnnotationWidget;

public class PdfCommentAdapter implements CommonComment{
    private final PdfTextMarkupAnnotationWidget pdfTextMarkupAnnotationWidget;

    public PdfCommentAdapter(PdfTextMarkupAnnotationWidget pdfTextMarkupAnnotationWidget) {
        this.pdfTextMarkupAnnotationWidget = pdfTextMarkupAnnotationWidget;
    }

    @Override
    public void setComment(String content) {
        pdfTextMarkupAnnotationWidget.setText(content);
    }
}

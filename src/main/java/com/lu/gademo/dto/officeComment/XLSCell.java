package com.lu.gademo.dto.officeComment;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class XLSCell {
    private String text;

    private String mark;

    private int row;

    private int col;

    public XLSCell(String text, String mark, int row, int col) {
        this.text = text;
        this.mark = mark;
        this.row = row;
        this.col = col;
    }
}
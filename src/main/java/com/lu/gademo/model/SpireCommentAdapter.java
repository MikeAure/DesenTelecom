package com.lu.gademo.model;

import com.spire.doc.collections.ParagraphCollection;
import com.spire.doc.fields.Comment;

/**
 * Word批注包装器
 */
public class SpireCommentAdapter implements CommonComment{
    private final com.spire.doc.fields.Comment comment;

    public SpireCommentAdapter(Comment comment) {
        this.comment = comment;
    }

    @Override
    public void setComment(String content) {
        ParagraphCollection paragraphCollection = comment.getBody().getParagraphs();
        while (paragraphCollection.getCount() > 1) {
            comment.getBody().getParagraphs().removeAt(paragraphCollection.getCount() - 1);
        }
        paragraphCollection.get(0).setText(content);
    }
}

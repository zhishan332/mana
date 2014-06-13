package com.wq.ui;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

public class NumOnlyDocument extends PlainDocument {
    public void insertString(int offset, String s, AttributeSet attrSet) throws BadLocationException {
        try {
            Integer.parseInt(s);
        } catch (NumberFormatException ex) {
            return;
        }
        super.insertString(offset, s, attrSet);
    }
}
package com.wq.ui;

import com.wq.constans.Constan;

import javax.swing.*;

/**
 * Created with IntelliJ IDEA.
 * User: wangq
 * Date: 12-8-20
 * Time: 上午9:06
 * To change this template use File | Settings | File Templates.
 */
public class HtmlPanel extends JEditorPane implements Page {

    public HtmlPanel(String html) {
        super("text/html",html);
        System.out.println("html==="+html);
        constructPlate();
        constructPage();
    }

    @Override
    public void constructPlate() {
        this.setOpaque(false);
        this.setEditable(false);
        this.setBackground(Constan.darkColor);
    }

    @Override
    public void constructPage() {

    }
}

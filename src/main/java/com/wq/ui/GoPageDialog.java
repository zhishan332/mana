package com.wq.ui;

import com.wq.constans.Constan;
import com.wq.context.SystemContext;
import com.wq.service.ListServiceImpl;
import com.wq.util.FontUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: wangq
 * Date: 14-6-25
 * Time: 上午10:18
 * To change this template use File | Settings | File Templates.
 */
public class GoPageDialog extends JWindow implements Page {
    private static GoPageDialog goPageDialog = null;
    private int Height = 60;

    private GoPageDialog() {
        super(VpicFrame.getInstance());
        constructPlate();
        constructPage();
    }

    public static GoPageDialog getInstance() {
        if (goPageDialog == null)
            goPageDialog = new GoPageDialog();
        return goPageDialog;
    }

    @Override
    public void constructPlate() {
        setPreferredSize(new Dimension(660, Height));
        toFront();
        pack();
    }

    @Override
    public void constructPage() {

    }

    public int getHeight() {
        return Height;
    }

    public void make() {
        int start = SystemContext.getInstance().getIntVal("PageStart");
        int total = SystemContext.getInstance().getIntVal("ImageTotal");
        if (total <= 0) return;
        start = (int) Math.ceil((double)start / Constan.PAGE_SHOW_NUM);
        final int page = (int) Math.ceil((double)total / Constan.PAGE_SHOW_NUM);
        Container con = this.getContentPane();
        con.removeAll();
        con.setLayout(new GridLayout(1, page + 3));
        con.setBackground(Color.DARK_GRAY);
        JButton preBtn = new JButton("上页");
        preBtn.setForeground(Color.WHITE);
        preBtn.setFont(FontUtil.getWei(14));
        preBtn.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String folder = ViewScrollPanel.getInstance().getViewContentPanel().getFolder();
                List<String> list = ViewScrollPanel.getInstance().getViewContentPanel().getList();
                ListServiceImpl.getInstance().loadPic(folder, list, SystemContext.getInstance().getIntVal("PageStart") - Constan.PAGE_SHOW_NUM);
            }
        });
        if (start == 0) preBtn.setEnabled(false);
        con.add(preBtn);
        int loopstart = start > 5 ? start - 5 : 0;
        for (int i = loopstart; i < page && i < (loopstart + 10); i++) {
            final JButton jb = new JButton(String.valueOf(i + 1));
            jb.setForeground(Color.WHITE);
            jb.setFont(FontUtil.getWei(14));
            jb.addActionListener(new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String folder = ViewScrollPanel.getInstance().getViewContentPanel().getFolder();
                    List<String> list = ViewScrollPanel.getInstance().getViewContentPanel().getList();
                    ListServiceImpl.getInstance().loadPic(folder, list,(Integer.parseInt(jb.getText())-1) * Constan.PAGE_SHOW_NUM);
                }
            });
            if (i == start) {
                jb.setBackground(Color.BLUE);
            }
            con.add(jb);
        }
        JButton nextBtn = new JButton("下页");
        nextBtn.setForeground(Color.WHITE);
        nextBtn.setFont(FontUtil.getWei(14));
        nextBtn.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String folder = ViewScrollPanel.getInstance().getViewContentPanel().getFolder();
                List<String> list = ViewScrollPanel.getInstance().getViewContentPanel().getList();
                ListServiceImpl.getInstance().loadPic(folder, list, SystemContext.getInstance().getIntVal("PageStart") + Constan.PAGE_SHOW_NUM);
            }
        });
        if ((start) >= page-1) nextBtn.setEnabled(false);
        con.add(nextBtn);
        final JTextField goFiled = new JTextField(String.valueOf(page));
        goFiled.setFont(FontUtil.getWei(14));
        goFiled.setEnabled(true);
        goFiled.setEditable(true);
        goFiled.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    String numstr = goFiled.getText();
                    if (null == numstr || "".equals(numstr.trim())) return;
                    int num = 0;
                    try {
                        num = Integer.parseInt(numstr);
                    } catch (NumberFormatException e1) {
                        return;
                    }
                    if (num > page || num<1) return;
                    String folder = ViewScrollPanel.getInstance().getViewContentPanel().getFolder();
                    List<String> list = ViewScrollPanel.getInstance().getViewContentPanel().getList();
                    ListServiceImpl.getInstance().loadPic(folder, list, (num-1) * Constan.PAGE_SHOW_NUM);
                }
            }
        });
        con.add(goFiled);
    }
}

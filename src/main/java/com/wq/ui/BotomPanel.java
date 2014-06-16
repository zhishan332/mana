//package com.wq.ui;
//
//import com.wq.constans.Constan;
//import com.wq.util.ButtonUtil;
//import com.wq.util.FontUtil;
//
//import javax.swing.*;
//import java.awt.*;
//import java.awt.event.ActionEvent;
//
///**
// * Created with IntelliJ IDEA.
// * User: wangq
// * Date: 12-8-17
// * Time: 上午11:37
// * To change this template use File | Settings | File Templates.
// */
//public class BotomPanel extends JPanel implements Page {
//    private static BotomPanel contentPanel;
//    private final MemoryPanel memoryPanel = new MemoryPanel();
//    private int num;
//    private JLabel numField;
//    private JLabel infoLabel;
//
//    private BotomPanel() {
//        constructPlate();
//        constructPage();
//    }
//
//    public static BotomPanel getInstance() {
//        if (contentPanel == null) contentPanel = new BotomPanel();
//        return contentPanel;
//    }
//
//    @Override
//    public void constructPlate() {
//        this.setLayout(new BorderLayout());
//        this.setMinimumSize(new Dimension(430, 600));
//    }
//
//    @Override
//    public void constructPage() {
//        this.add(BorderLayout.CENTER, ViewScrollPanel.getInstance());
//        JToolBar jToolBar = new JToolBar();
//        jToolBar.setPreferredSize(new Dimension(430, 60));
//        jToolBar.setFloatable(false);
//        infoLabel = new JLabel("加载成功");
//        infoLabel.setPreferredSize(new Dimension(100, 20));
//        infoLabel.setFont(FontUtil.getSong12());
//        infoLabel.setMaximumSize(new Dimension(100, 20));
//        numField = new JLabel("图片总数:0");
//        numField.setPreferredSize(new Dimension(100, 20));
//        numField.setMaximumSize(new Dimension(100, 20));
//        memoryPanel.setPreferredSize(new Dimension(100, 16));
//        memoryPanel.setMaximumSize(new Dimension(100, 16));
//        JButton rubBtn = ButtonUtil.createJButton(Constan.RESPAHT+ "res/img/rub.png", null, null);
//        rubBtn.setPreferredSize(new Dimension(16, 16));
//        rubBtn.setMaximumSize(new Dimension(16, 16));
//        rubBtn.setToolTipText("清理内存");
//        rubBtn.addActionListener(new java.awt.event.ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                System.gc();
//                memoryPanel.repaint();
//            }
//        });
//        jToolBar.add(Box.createHorizontalGlue());
//        jToolBar.add(infoLabel);
//        jToolBar.addSeparator();
//        jToolBar.add(numField);
//        jToolBar.addSeparator();
////        jToolBar.add(Box.createHorizontalGlue());
//        jToolBar.add(memoryPanel);
//        jToolBar.add(rubBtn);
//        this.add(BorderLayout.SOUTH, jToolBar);
//    }
//
//    public void setNum(int num) {
//        numField.setText("总数:" + num);
//    }
//}

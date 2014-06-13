package com.wq.ui;

import com.wq.constans.Constan;
import com.wq.util.FontUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Created with IntelliJ IDEA.
 * User: wangq
 * Date: 12-8-3
 * Time: 下午1:22
 * 面板右键菜单
 */
public class ViewPanelMenu extends JPopupMenu implements Page {
    private static ViewPanelMenu viewPanelMenu = null;
    private JMenuItem hideItem;
    private JMenuItem showItem;
    private JMenuItem saveItem;

    private ViewPanelMenu() {
        constructPlate();
        constructPage();
    }

    public static ViewPanelMenu getInstance() {
        if (viewPanelMenu == null) {
            viewPanelMenu = new ViewPanelMenu();
        }
        return viewPanelMenu;
    }

    @Override
    public void constructPlate() {
        this.setSize(200, 55);
        this.setPreferredSize(new Dimension(200,55));
    }

    @Override
    public void constructPage() {
        saveItem = new JMenuItem("全部保存");
        saveItem.setFont(FontUtil.getSong12());
        saveItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {

            }
        });
        saveItem.setEnabled(false);
//        this.add(saveItem);//TODO
        hideItem = new JMenuItem("隐藏右方菜单",new ImageIcon(Constan.RESPAHT + "img/left.png"));
        hideItem.setFont(FontUtil.getSong12());
        hideItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ListPanel.getInstance().hidePanel();
                hideItem.setEnabled(false);
                showItem.setEnabled(true);
                PicMenu.getInstance().getHideItem().setEnabled(false);
                PicMenu.getInstance().getShowItem().setEnabled(true);
            }
        });
        this.add(hideItem);
        showItem = new JMenuItem("显示右方菜单",new ImageIcon(Constan.RESPAHT + "img/right.png"));
        showItem.setFont(FontUtil.getSong12());
        showItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ListPanel.getInstance().showPanel();
                hideItem.setEnabled(true);
                showItem.setEnabled(false);
                PicMenu.getInstance().getHideItem().setEnabled(true);
                PicMenu.getInstance().getShowItem().setEnabled(false);
            }
        });
        if (ListPanel.getInstance().isVisible()) {
            hideItem.setEnabled(true);
            showItem.setEnabled(false);
        } else {
            hideItem.setEnabled(false);
            showItem.setEnabled(true);
        }
        this.add(new JPopupMenu.Separator());
        this.add(showItem);
    }

    public JMenuItem getHideItem() {
        return hideItem;
    }

    public JMenuItem getShowItem() {
        return showItem;
    }
}

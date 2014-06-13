package com.wq.ui;

import com.wq.constans.Constan;
import com.wq.service.ListService;
import com.wq.service.ListServiceImpl;
import com.wq.util.FileUtil;
import com.wq.util.FontUtil;
import com.wq.util.MesBox;

import javax.swing.*;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: wangq
 * Date: 13-5-22
 * Time: 下午3:53
 * To change this template use File | Settings | File Templates.
 */
public class TreeMenu extends JPopupMenu implements Page {
    private static TreeMenu treeMenu = null;
    private JMenuItem delItem;
    private String filePath;
    private JMenuItem fireItem;
    private TreePath treePath;
    private ListService listService = ListServiceImpl.getInstance();
    private TreeMenu() {
        constructPlate();
        constructPage();
    }

    public static TreeMenu getInstance() {
        if (treeMenu == null) {
            treeMenu = new TreeMenu();
        }
        return treeMenu;
    }

    @Override
    public void constructPlate() {
        this.setSize(200, 55);
        this.setPreferredSize(new Dimension(200, 55));
    }

    @Override
    public void constructPage() {
        delItem = new JMenuItem("从磁盘删除",new ImageIcon(Constan.RESPAHT + "res/img/del.png"));
        delItem.setFont(FontUtil.getSong12());
        delItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (MesBox.confirm("确定删除吗")) {
                    FileUtil.deleteDir(getFilePath());
                    listService.reloadTreeData();
                }
            }
        });

        fireItem = new JMenuItem("进入磁盘目录",new ImageIcon(Constan.RESPAHT + "res/img/fire.png"));
        fireItem.setFont(FontUtil.getSong12());
        fireItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    java.awt.Desktop.getDesktop().open(new File(getFilePath()));
                } catch (IOException exp) {
                    // TODO Auto-generated catch block
                    exp.printStackTrace();
                }
            }
        });
        this.add(delItem);
        this.add(new JPopupMenu.Separator());
        this.add(fireItem);
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public void setTreePath(TreePath treePath) {
        this.treePath = treePath;
    }
}

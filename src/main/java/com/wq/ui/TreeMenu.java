package com.wq.ui;

import com.wq.constans.Constan;
import com.wq.model.DirMenu;
import com.wq.service.ListService;
import com.wq.service.ListServiceImpl;
import com.wq.ui.module.FolderChooser;
import com.wq.util.FileUtil;
import com.wq.util.FontUtil;
import com.wq.util.MesBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
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
    private static final Logger log = LoggerFactory.getLogger(TreeMenu.class);
    private String filePath;
    private ListService listService = ListServiceImpl.getInstance();

    public TreeMenu() {
        constructPlate();
        constructPage();
    }
    public TreeMenu(String filePath) {
        this.filePath=filePath;
        constructPlate();
        constructPage();
    }

    @Override
    public void constructPlate() {
        if (filePath != null) {
            this.setSize(180, 130);
            this.setPreferredSize(new Dimension(180, 130));
        } else {
            this.setSize(180, 40);
            this.setPreferredSize(new Dimension(180, 40));
        }
    }

    @Override
    public void constructPage() {
        JMenuItem delItem = new JMenuItem("移除文件夹", new ImageIcon(Constan.RESPAHT + "res/img/fuckdel.png"));
        delItem.setFont(FontUtil.getDefault());
        delItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (ListPanel.getInstance().getTree().getLastSelectedPathComponent() == null) {
                    MesBox.warn("请选择要删除的映射文件夹");
                    return;
                }
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) ListPanel.getInstance().getTree()
                        .getLastSelectedPathComponent();
                if (node.getLevel() != 1) {
                    MesBox.warn("您只能删除映射文件夹");
                } else {
                    DirMenu dirMenu = (DirMenu) node.getUserObject();//获得这个结点的名称
                    String path = dirMenu.getFilePath();
                    listService.delTreeData(path);
                    listService.reloadTreeData();
                }
            }
        });

        JMenuItem delDiskItem = new JMenuItem("从磁盘删除", new ImageIcon(Constan.RESPAHT + "res/img/del.png"));
        delDiskItem.setFont(FontUtil.getDefault());
        delDiskItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (MesBox.confirm("确定删除吗")) {
                    FileUtil.deleteDir(getFilePath());
                    listService.reloadTreeData();
                }
            }
        });

        JMenuItem fireItem = new JMenuItem("进入文件目录", new ImageIcon(Constan.RESPAHT + "res/img/fire.png"));
        fireItem.setFont(FontUtil.getDefault());
        fireItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    java.awt.Desktop.getDesktop().open(new File(getFilePath()));
                } catch (IOException exp) {
                    log.error("进入文件目录失败", exp);
                }
            }
        });
        if (filePath != null) {
            this.add(fireItem);
            this.add(delItem);
            this.add(new JPopupMenu.Separator());
            this.add(delDiskItem);
        }
    }

    public String getFilePath() {
        return filePath;
    }
}

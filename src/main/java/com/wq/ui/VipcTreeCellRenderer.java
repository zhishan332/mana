package com.wq.ui;

import com.wq.constans.Constan;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeNode;
import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: wangq
 * Date: 12-8-8
 * Time: 下午4:29
 * JTREE的图标渲染
 */
public class VipcTreeCellRenderer extends DefaultTreeCellRenderer {
    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
//        if (leaf) { //叶子节点设置图标
//            setLeafIconByValue(value);
//        } else {
//            setParentIcon();
//        }
        setIconByValue(leaf, value);
        return this;
    }

    public void setIconByValue(boolean leaf, Object value) {
//        int length = value.toString().length();  //深度
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
        TreeNode[] paths = node.getPath();
        Toolkit tk = Toolkit.getDefaultToolkit();
        if (paths.length == 2) {//映射文件夹
            this.setIcon(new ImageIcon(tk.createImage(Constan.RESPAHT+"img/fuck.png")));
        } else if (leaf) {
            this.setIcon(new ImageIcon(tk.createImage(Constan.RESPAHT+"img/folder.png")));
        } else {
            this.setIcon(new ImageIcon(tk.createImage(Constan.RESPAHT+"img/bag.png")));
        }
    }

//    public void setParentIcon() {
//        Toolkit tk = Toolkit.getDefaultToolkit();
//        Image mImage = tk.createImage(Constan.RESPAHT+"img/bag.png");
//        ImageIcon ii = new ImageIcon(mImage);
//        this.setIcon(ii);
//    }
}

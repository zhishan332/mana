package com.wq.util;

import javax.swing.*;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import java.util.Enumeration;

/**
 * Swing组件JTree的工具类
 * @author wangqing
 * @since 1.0.0
 */
public class JTreeUtil {

    /**
     * 完全展开一个JTree
     *
     * @param tree JTree
     */
    public static void expandTree(JTree tree, boolean expand) {
        TreeNode root = (TreeNode) tree.getModel().getRoot();
        expandAll(tree, new TreePath(root), expand);
    }

    /**
     * 完全展开或关闭一个树,用于递规执行
     *
     * @param tree   JTree
     * @param parent 父节点
     * @param expand 为true则表示展开树,否则为关闭整棵树
     */
    private static void expandAll(JTree tree, TreePath parent, boolean expand) {
        //Traverse children
        TreeNode node = (TreeNode) parent.getLastPathComponent();
        if (node.getChildCount() >= 0) {
            for (Enumeration e = node.children(); e.hasMoreElements(); ) {
                TreeNode n = (TreeNode) e.nextElement();
                TreePath path = parent.pathByAddingChild(n);
                expandAll(tree, path, expand);
            }
        }
        Object[] paths = parent.getPath();
        if (expand || paths.length == 1) { //如果不展开则展开第一层
            tree.expandPath(parent);
        }
    }
}

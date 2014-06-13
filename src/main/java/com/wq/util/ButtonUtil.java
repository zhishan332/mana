package com.wq.util;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Created with IntelliJ IDEA.
 * User: wangq
 * Date: 12-8-17
 * Time: 上午10:32
 * To change this template use File | Settings | File Templates.
 */
public class ButtonUtil {
    /**
     * 根据一些参数快速地构造出按钮来
     * 这些按钮从外观上看都是一些特殊的按钮
     *
     * @param name     按钮图片的相对地址
     * @param cmd      命令
     * @param listener 监听器
     * @return 按钮
     */
    public static JButton createJButton(String name, String cmd, ActionListener listener) {
        ImageIcon icons = new ImageIcon(name);
        JButton jb = new JButton();
        jb.setBorderPainted(false);
        jb.setFocusPainted(false);
        jb.setContentAreaFilled(false);
        jb.setDoubleBuffered(true);
        jb.setIcon(icons);
        jb.setRolloverIcon(icons);
        jb.setPressedIcon(icons);
        jb.setOpaque(false);
        jb.setFocusable(false);
        jb.setActionCommand(cmd);
        jb.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        jb.addActionListener(listener);
        return jb;
    }
    /**
     * 根据一些参数快速地构造出按钮来
     * 这些按钮从外观上看都是一些特殊的按钮
     * @param name 按钮图片的相对地址
     * @param cmd 命令
     * @param listener 监听器
     * @return 按钮
     */
//    public static JButton createJButton(String name, String cmd, ActionListener listener) {
//        Image[] icons = Util.getImages(name, 3);
//        JButton jb = new JButton();
//        jb.setBorderPainted(false);
//        jb.setFocusPainted(false);
//        jb.setContentAreaFilled(false);
//        jb.setDoubleBuffered(true);
//        jb.setIcon(new ImageIcon(icons[0]));
//        jb.setRolloverIcon(new ImageIcon(icons[1]));
//        jb.setPressedIcon(new ImageIcon(icons[2]));
//        jb.setOpaque(false);
//        jb.setFocusable(false);
//        jb.setActionCommand(cmd);
//        jb.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
//        jb.addActionListener(listener);
//        return jb;
//    }
    /**
     * 根据一些参数快速地构造出按钮来
     * 这些按钮从外观上看都是一些特殊的按钮
     * @param name 按钮图片的相对地址
     * @param cmd 命令
     * @param listener 监听器
     * @param selected 是否被选中了
     * @return 按钮
     */
//    public static JToggleButton createJToggleButton(String name, String cmd, ActionListener listener, boolean selected) {
//        Image[] icons = Util.getImages(name, 3);
//        JToggleButton jt = new JToggleButton();
//        jt.setBorder(null);
//        jt.setContentAreaFilled(false);
//        jt.setFocusPainted(false);
//        jt.setDoubleBuffered(true);
//        jt.setIcon(new ImageIcon(icons[0]));
//        jt.setRolloverIcon(new ImageIcon(icons[1]));
//        jt.setSelectedIcon(new ImageIcon(icons[2]));
//        jt.setOpaque(false);
//        jt.setFocusable(false);
//        jt.setActionCommand(cmd);
//        jt.setSelected(selected);
//        jt.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
//        jt.addActionListener(listener);
//        return jt;
//    }
    /**
     * 根据一些参数快速地构造出按钮来
     * 这些按钮从外观上看都是一些特殊的按钮
     * @param name 按钮图片的相对地址
     * @param cmd 命令
     * @param listener 监听器
     * @param selected 是否被选中了
     * @return 按钮
     */
    public static JToggleButton createJToggleButton(String name, String cmd, ActionListener listener, boolean selected) {
        ImageIcon icons =new ImageIcon(name);
        JToggleButton jt = new JToggleButton();
        jt.setBorder(null);
        jt.setContentAreaFilled(false);
        jt.setFocusPainted(false);
        jt.setDoubleBuffered(true);
        jt.setIcon(icons);
        jt.setRolloverIcon(icons);
        jt.setSelectedIcon(icons);
        jt.setOpaque(false);
        jt.setFocusable(false);
        jt.setActionCommand(cmd);
        jt.setSelected(selected);
        jt.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        jt.addActionListener(listener);
        return jt;
    }
}

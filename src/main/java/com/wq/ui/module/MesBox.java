package com.wq.ui.module;

import javax.swing.*;
import java.awt.*;

/**
 * 方便的使用JOptionPane
 *
 * @author wangqing
 * @since 1.0.0
 */
public class MesBox {
    //通用的错误提示框

    public static void comAlert(String mes) {
        if (mes == null || "".equals(mes)) {
            mes = "数据有误，请检查";
        }
        Toolkit.getDefaultToolkit().beep();
        JOptionPane.showMessageDialog(null, "<html><font face='微软雅黑' size='4'>" + mes + "</font></html>", "错误提示", JOptionPane.ERROR_MESSAGE);
    }

    //通用的确认框，只有点击确定，才返回true

    public static boolean confirm(String mes) {
        if (mes == null || "".equals(mes)) {
            mes = "您确定执行该操作吗";
        }
        int result = JOptionPane.showConfirmDialog(null, "<html><font face='微软雅黑' size='4'>" + mes + "</font></html>", "提示", JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION)
            return true;
        if (result == JOptionPane.NO_OPTION)
            return false;
        if (result == JOptionPane.CANCEL_OPTION)
            return false;
        if (result == JOptionPane.CLOSED_OPTION)
            return false;
        return false;
    }
    //通用的成功消息提示框

    public static void success(String mes) {
        if (mes == null || "".equals(mes)) {
            mes = "操作成功！";
        }
        JOptionPane.showMessageDialog(null, "<html><font face='微软雅黑' size='4'>" + mes + "</font></html>", "提示", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void success() {
        JOptionPane.showMessageDialog(null, "<html><font face='微软雅黑' size='4'>操作成功</font></html>", "提示", JOptionPane.INFORMATION_MESSAGE);
    }

    //通用的警告提示框
    public static void warn(String mes) {
        if (mes == null || "".equals(mes)) {
            mes = "警告！";
        }
        JOptionPane.showMessageDialog(null, "<html><font face='微软雅黑' size='4'>" + mes + "</font></html>", "提示", JOptionPane.WARNING_MESSAGE);
    }
}

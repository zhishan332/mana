package com.wq.util;

import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: wangq
 * Date: 12-8-17
 * Time: 上午11:45
 * To change this template use File | Settings | File Templates.
 */
public class FontUtil {
    public static Font getLittleFont() {
        return new Font(Font.DIALOG_INPUT, Font.PLAIN, 11);
    }

    public static Font getDefault() {
        return getWei12();
    }

    public static Font getSong12() {
        return new Font("宋体", Font.PLAIN, 12);
    }

    public static Font getSong13() {
        return new Font("宋体", Font.PLAIN, 13);
    }

    public static Font getSong14() {
        return new Font("宋体", Font.PLAIN, 14);
    }

    public static Font getWei12() {
        return new Font("微软雅黑", Font.PLAIN, 12);
    }

    public static Font getArial13() {
        return new Font("Aria", Font.PLAIN, 13);
    }

    public static Font getArial12() {
        return new Font("Aria", Font.PLAIN, 12);
    }
}

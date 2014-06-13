package com.wq.ui;

import com.wq.constans.Constan;
import com.wq.util.FontUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created with IntelliJ IDEA.
 * User: wangq
 * Date: 12-8-14
 * Time: 下午1:16
 * 图片属性
 */
public class PropDialog extends JWindow implements Page {
    private static PropDialog propDialog;
    private JLabel picName;//图片名称
    private JLabel path;//图片路径
    private JLabel date;//修改时间
    private JLabel len;//大小
    private JLabel xp;//像素
    private JLabel type;//文件类型
    private Container con;//容器

    private PropDialog() {
        constructPlate();
        constructPage();
    }

    public static PropDialog getInstance() {
        if (propDialog == null) propDialog = new PropDialog();
        return propDialog;
    }

    @Override
    public void constructPlate() {
        setPreferredSize(new Dimension(400, 300));
        setSize(new Dimension(400, 300));
        con = this.getContentPane();
        con.setBackground(Color.DARK_GRAY);
        con.setLayout(new GridLayout(6, 1));
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();//获得屏幕的大小
        setLocation((screen.width - getSize().width) / 2, (screen.height - getSize().height) / 2);//使启动窗口居中显示
//        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);//设置关闭操作
        toFront();
        // 添加窗口事件
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                PropDialog.getInstance().setVisible(false);
            }
        });
        pack();
    }

    @Override
    public void constructPage() {
        picName = new JLabel("  图片名称：");//图片名称
        picName.setForeground(Color.white);
        picName.setFont(FontUtil.getArial12());
        path = new JLabel("  图片路径：");//图片路径
        path.setForeground(Color.white);
        path.setFont(FontUtil.getArial12());
        date = new JLabel("  修改时间：");//修改时间
        date.setForeground(Color.white);
        date.setFont(FontUtil.getArial12());
        len = new JLabel("  文件大小：");//大小
        len.setForeground(Color.white);
        len.setFont(FontUtil.getArial12());
        xp = new JLabel("  分辨率：");//像素
        xp.setForeground(Color.white);
        xp.setFont(FontUtil.getArial12());
        type = new JLabel("  文件类型：");//文件类型
        type.setForeground(Color.white);
        type.setFont(FontUtil.getArial12());
        con.add(picName);
        con.add(path);
        con.add(date);
        con.add(len);
        con.add(xp);
        con.add(type);

    }

    public JLabel getPicName() {
        return picName;
    }

    public JLabel getPath() {
        return path;
    }

    public JLabel getDate() {
        return date;
    }

    public JLabel getLen() {
        return len;
    }

    public JLabel getXp() {
        return xp;
    }

    public JLabel getType() {
        return type;
    }
}

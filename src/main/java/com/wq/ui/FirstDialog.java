package com.wq.ui;

import com.wq.constans.Constan;
import com.wq.service.SysDataHandler;
import com.wq.util.FontUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Created with IntelliJ IDEA.
 * User: wangq
 * Date: 12-8-14
 * Time: 下午5:33
 * To change this template use File | Settings | File Templates.
 */
public class FirstDialog extends JDialog implements Page {
    private Container con;//容器
    private static FirstDialog firstDialog;
    private SysDataHandler handler = SysDataHandler.getInstance();

    private FirstDialog() {
        constructPlate();
        constructPage();
    }

    public static FirstDialog getInstance() {
        if (firstDialog == null) firstDialog = new FirstDialog();
        return firstDialog;
    }

    @Override
    public void constructPlate() {
        setPreferredSize(new Dimension(415, 150));
        setSize(new Dimension(415, 150));
        con = this.getContentPane();
        con.setLayout(null);
        this.setTitle("请选择一个图片文件夹");
        Toolkit tk = Toolkit.getDefaultToolkit();
        Image mImage = tk.createImage(Constan.RESPAHT + "res/img/logo.png");
        setIconImage(mImage);
        this.setLocationRelativeTo(null);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);//设置关闭操作
        toFront();
        pack();
    }

    @Override
    public void constructPage() {
        JButton chBtn = new JButton("选择一个存放图片的文件夹");
        chBtn.setFont(FontUtil.getSong14());
        chBtn.setBounds(30, 25, 200, 50);
        chBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ListPanel.getInstance().chooseFile();
                FirstDialog.getInstance().dispose();
            }
        });
        JButton caBtn = new JButton("稍后再说");
        caBtn.setFont(FontUtil.getSong13());
        caBtn.setBounds(250, 30, 100, 40);
        caBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                FirstDialog.getInstance().dispose();
            }
        });
        con.add(chBtn);
        con.add(caBtn);
    }
}

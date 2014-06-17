package com.wq.ui;

import com.wq.constans.Constan;
import com.wq.util.FontUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * To change this template use File | Settings | File Templates.
 * @author wangqing
 * @since 1.0.0
 */
public class AboutDialog extends JWindow implements Page {
    private static AboutDialog aboutDialog = null;

    private AboutDialog() {
        constructPlate();
        constructPage();
    }

    public static AboutDialog getInstance() {
        if (aboutDialog == null) aboutDialog = new AboutDialog();
        return aboutDialog;
    }

    @Override
    public void constructPlate() {
        setPreferredSize(new Dimension(400, 200));
        toFront();
        pack();
//        this.setTitle("关于Mana");
        Toolkit tk = Toolkit.getDefaultToolkit();
        Image mImage = tk.createImage(Constan.RESPAHT + "res/img/logo.png");
        setIconImage(mImage);
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();//获得屏幕的大小
        setLocation((screen.width - getSize().width) / 2, (screen.height - getSize().height) / 2);//使启动窗口居中显示
        toFront();
        pack();
    }

    @Override
    public void constructPage() {
        Container con = this.getContentPane();
        con.setLayout(new GridLayout(7, 1));
        con.setBackground(Color.DARK_GRAY);
        JLabel web = new JLabel(" JDK版本：1.6及以上");//图片名称
        web.setForeground(Color.white);
        web.setFont(FontUtil.getDefault());
        JLabel bb = new JLabel(" 版本号：1.0.0");//图片名称
        bb.setForeground(Color.white);
        bb.setFont(FontUtil.getDefault());
        JLabel sm = new JLabel(" 说明：该软件管理电脑中的图片文件夹，并能给您在网页浏览图片的感觉");//图片名称
        sm.setForeground(Color.white);
        sm.setFont(FontUtil.getDefault());
        JLabel sm2 = new JLabel(" 您只需要将本地图片文件夹映射到Mana中即可。");//图片名称
        sm2.setForeground(Color.white);
        sm2.setFont(FontUtil.getDefault());
        JLabel sm3 = new JLabel(" 声明：本软件为开源软件，您可以自由使用并修改源码，也可以用于商业");//图片名称
        sm3.setForeground(Color.white);
        sm3.setFont(FontUtil.getDefault());
        JLabel sm4 = new JLabel(" 用途，并非常欢迎与作者进行技术交流。");//图片名称
        sm4.setForeground(Color.white);
        sm4.setFont(FontUtil.getDefault());
        con.add(web);
        con.add(bb);
        con.add(sm);
        con.add(sm2);
        con.add(sm3);
        con.add(sm4);
    }
}

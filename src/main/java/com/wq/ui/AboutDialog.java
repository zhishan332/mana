package com.wq.ui;

import com.wq.constans.Constan;
import com.wq.util.FontUtil;

import javax.swing.*;
import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: wangq
 * Date: 12-8-10
 * Time: 下午5:18
 * To change this template use File | Settings | File Templates.
 */
public class AboutDialog extends JDialog implements Page {
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
        setPreferredSize(new Dimension(400, 300));
        toFront();
        pack();
        this.setTitle("关于Mana");
        Toolkit tk = Toolkit.getDefaultToolkit();
        Image mImage = tk.createImage(Constan.RESPAHT + "res/img/logo.png");
        setIconImage(mImage);
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();//获得屏幕的大小
        setLocation((screen.width - getSize().width) / 2, (screen.height - getSize().height) / 2);//使启动窗口居中显示
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);//设置关闭操作
        setResizable(false);
    }

    @Override
    public void constructPage() {
        Container con = this.getContentPane();
        con.setLayout(new GridLayout(7, 1));
        con.setBackground(Color.DARK_GRAY);
        JLabel name = new JLabel("作者：王庆");//图片名称
        name.setForeground(Color.white);
        name.setFont(FontUtil.getArial12());
        JLabel email = new JLabel("Email：zhishan99@163.com");//图片名称
        email.setForeground(Color.white);
        email.setFont(FontUtil.getArial12());
        JLabel web = new JLabel("博客：http://www.cnblogs.com/zhishan/");//图片名称
        web.setForeground(Color.white);
        web.setFont(FontUtil.getArial12());
        JLabel bb = new JLabel("版本号：1.0.0");//图片名称
        bb.setForeground(Color.white);
        bb.setFont(FontUtil.getArial12());
        JLabel sm = new JLabel("说明：该软件可以完整无损的浏览大图片，并能给您浏览网页图片的感觉");//图片名称
        sm.setForeground(Color.white);
        sm.setFont(FontUtil.getArial12());
        JLabel sm2 = new JLabel("您只需要将本地图片文件夹映射到Mana中即可。");//图片名称
        sm2.setForeground(Color.white);
        sm2.setFont(FontUtil.getArial12());
        JLabel sm3 = new JLabel("声明：未经作者同意，不可将本软件用于商业用途。");//图片名称
        sm3.setForeground(Color.white);
        sm3.setFont(FontUtil.getArial12());
        con.add(name);
        con.add(email);
        con.add(web);
        con.add(bb);
        con.add(sm);
        con.add(sm2);
        con.add(sm3);
    }
}

package com.wq.ui;

import com.wq.cache.SystemCache;
import com.wq.constans.Constan;
import com.wq.ui.module.MesBox;
import com.wq.util.FontUtil;
import com.wq.util.MD5Util;
import com.wq.util.SwingUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * 密码认证窗体
 *
 * @author wangqing
 * @since 1.0.0
 */
public class ValidateDialog extends JFrame {
    private JPasswordField jf;
    private ValidateDialog vd;

    public ValidateDialog() {
        this.addWindowFocusListener(new WindowAdapter() {
            public void windowLostFocus(WindowEvent e) {
                e.getWindow().toFront();
            }
        });
        this.setSize(new Dimension(320, 160));
        this.setTitle("Mana已被加密");
        this.setFont(FontUtil.getDefault());
        this.setLocationRelativeTo(null);
        Toolkit tk = Toolkit.getDefaultToolkit();
        Image mImage = tk.createImage(Constan.RESPAHT + "res/img/logo.png");
        setIconImage(mImage);
        Container con = this.getContentPane();
        con.setLayout(null);
        JLabel lu = new JLabel("密 码：", JLabel.RIGHT);
        lu.setBounds(10, 30, 50, 28);
        lu.setFont(FontUtil.getWei(13));
        jf = new JPasswordField();
        jf.setBounds(70, 30, 160, 28);
        JButton jb = new JButton("进 入");
        jb.setBounds(240, 25, 60, 35);
        jb.setFont(FontUtil.getWei(13));
        vd = this;
        jb.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String pass = new String(jf.getPassword());
                if (MD5Util.validPassword(pass, SystemCache.getInstance().getData().getPassword())) {
                    VpicFrame vpic = VpicFrame.getInstance();
                    vpic.setVisible(true);
                    vd.setVisible(false);
                    vd.dispose();
//                    SwingUtilities.invokeLater(new Runnable() {
//                        @Override
//                        public void run() {
////                            listService.reloadTreeData(); //页面展开后刷新一下TREE，在JSplitPane作用下，不刷新，无法显示JTree
//                        }
//                    });
                } else {
                    MesBox.comAlert("密码错误，请重试或者重新安装本软件");
                }
            }
        }
        );
        SwingUtils.enterPressesWhenFocused(jf, new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                String pass = new String(jf.getPassword());
                if (MD5Util.validPassword(pass, SystemCache.getInstance().getData().getPassword())) {
                    VpicFrame vpic = VpicFrame.getInstance();
                    vpic.setVisible(true);
                    vd.setVisible(false);
                    vd.dispose();
//                    SwingUtilities.invokeLater(new Runnable() {
//                        @Override
//                        public void run() {
//                            listService.reloadTreeData(); //页面展开后刷新一下TREE，在JSplitPane作用下，不刷新，无法显示JTree
//                        }
//                    });
                } else {
                    MesBox.comAlert("密码错误，请重试或者重新安装本软件。");
                }
            }
        });
        con.add(lu);
        con.add(jf);
        con.add(jb);
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose();
                System.exit(1);
            }
        });
    }
}

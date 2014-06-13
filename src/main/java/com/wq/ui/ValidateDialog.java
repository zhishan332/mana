package com.wq.ui;

import com.wq.constans.Constan;
import com.wq.service.ListService;
import com.wq.service.ListServiceImpl;
import com.wq.service.SysDataHandler;
import com.wq.util.MD5Util;
import com.wq.util.MesBox;
import com.wq.util.SwingUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Created by IntelliJ IDEA.
 * User: wq
 * Date: 11-7-14
 * Time: 11:25
 * To change this template use File | Settings | File Templates.
 */
public class ValidateDialog extends JDialog {
    private JPasswordField jf;
    private ValidateDialog vd;
    private static ListService listService = ListServiceImpl.getInstance();

    public ValidateDialog() {
        this.addWindowFocusListener(new WindowAdapter() {
            public void windowLostFocus(WindowEvent e) {
                e.getWindow().toFront();
            }
        });
        this.setSize(new Dimension(290, 100));
        this.setTitle("软件已加密");
        this.setLocationRelativeTo(null);
        Toolkit tk = Toolkit.getDefaultToolkit();
        Image mImage = tk.createImage(Constan.RESPAHT+"img/logo.png");
        setIconImage(mImage);
        Container con = this.getContentPane();
        con.setLayout(null);
        JLabel lu = new JLabel("密码：", JLabel.RIGHT);
        lu.setBounds(10, 10, 50, 28);
        jf = new JPasswordField();
        jf.setBounds(70, 10, 120, 28);
        JButton jb = new JButton("提交");
        jb.setBounds(200, 10, 60, 28);
        vd = this;
        jb.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String pass = new String(jf.getPassword());
                if (MD5Util.validPassword(pass, SysDataHandler.getInstance().getData().getPassword())) {
                    VpicFrame vpic = VpicFrame.getInstance();
                    vpic.setVisible(true);
                    vd.setVisible(false);
                    vd.dispose();
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            listService.reloadTreeData(); //页面展开后刷新一下TREE，在JSplitPane作用下，不刷新，无法显示JTree
                        }
                    });
                } else {
                    MesBox.comAlert("密码错误，请重试或者重新安装本软件");
                }
            }
        }
        );
        SwingUtils.enterPressesWhenFocused(jf, new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                String pass = new String(jf.getPassword());
                if (MD5Util.validPassword(pass, SysDataHandler.getInstance().getData().getPassword())) {
                    VpicFrame vpic = VpicFrame.getInstance();
                    vpic.setVisible(true);
                    vd.setVisible(false);
                    vd.dispose();
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            listService.reloadTreeData(); //页面展开后刷新一下TREE，在JSplitPane作用下，不刷新，无法显示JTree
                        }
                    });
                } else {
                    MesBox.comAlert("密码错误，请重试或者重新安装本软件。");
                }
            }
        });
        con.add(lu);
        con.add(jf);
        con.add(jb);
        this.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });
    }
}

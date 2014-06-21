package com.wq.ui;

import com.wq.cache.SystemCache;
import com.wq.constans.Constan;
import com.wq.service.ListService;
import com.wq.service.ListServiceImpl;
import com.wq.util.FontUtil;
import com.wq.util.MD5Util;
import com.wq.util.SwingUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;

/**
 * 选项设置窗体
 *
 * @author wangqing
 * @since 1.0.0
 */
public class SetPanel extends JFrame implements Page {
    private static final Logger log = LoggerFactory.getLogger(SetPanel.class);
    private static SetPanel setPanel;
    private JPanel generalPanel;
    private Container container;
    private JLabel fileJlable;
    private JTextField speed;
    private JCheckBox jpg;
    private JCheckBox win;
    private JCheckBox png;
    private JCheckBox gif;
    private JTextField savePath;
    private JButton chBtn;
    private JButton saBtn;
    private JButton csBtn;
    private JCheckBox isExp;
    private JCheckBox ignoreEmptyFolder;
    private JCheckBox autoFit;
    private JLabel passJlable;
    private JPasswordField jf;
    private ListService listService = ListServiceImpl.getInstance();
    private com.wq.model.SysData sysdata;
    private JPanel proxyPanel;
    private JLabel proxyHostLabel;
    private JTextField proxyHostField;
    private JLabel proxyPortLabel;
    private JTextField proxyPortField;
    private JLabel proxyUserNameLable;
    private JTextField proxyUserNameField;
    private JLabel proxyPasswordLabel;
    private JPasswordField proxyPasswordField;
    private JCheckBox userProxyBox;
    private JCheckBox isHModeBox;

    private SetPanel() {
        constructPlate();
        constructPage();
    }

    public static SetPanel getInstance() {
        if (setPanel == null) {
            setPanel = new SetPanel();
        }
        return setPanel;
    }

    @Override
    public void constructPlate() {
        this.setTitle("选项设置");
        this.setSize(new Dimension(550, 500));
        this.setLocationRelativeTo(null);
        this.setLayout(null);
        this.setResizable(false);
        Toolkit tk = Toolkit.getDefaultToolkit();
        Image mImage = tk.createImage(Constan.RESPAHT + "res/img/logo.png");
        setIconImage(mImage);
        container = this.getContentPane();
        this.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });
        this.setVisible(true);
    }

    @Override
    public void constructPage() {
        generalPanel = new JPanel();
        generalPanel.setBounds(4, 1, 535, 290);
        generalPanel.setLayout(null);
        TitledBorder generalPanelBorder = new TitledBorder("常规设置");
        generalPanelBorder.setTitleFont(FontUtil.getSong12());
        generalPanel.setBorder(generalPanelBorder);

        fileJlable = new JLabel("图片类型：", SwingConstants.RIGHT);
        fileJlable.setFont(FontUtil.getSong12());
        fileJlable.setBounds(10, 12, 60, 35);
        jpg = new JCheckBox("JPEG文件(*.jpg;*jpeg)", true);
        jpg.setBounds(10, 40, 260, 25);
        win = new JCheckBox("Windows位图(*.bmp)", true);
        win.setBounds(10, 65, 260, 25);
        png = new JCheckBox("Portable Network Graphics(*.png)", true);
        png.setBounds(280, 40, 240, 25);
        gif = new JCheckBox("CompuServe GIF(*.gif)", true);
        gif.setBounds(280, 65, 240, 25);
        JLabel speedJlable = new JLabel("滚动速度：", SwingConstants.RIGHT);
        speedJlable.setFont(FontUtil.getSong12());
        speedJlable.setBounds(10, 90, 60, 35);
        speed = new JTextField();
        speed.setBounds(66, 93, 90, 28);
        speed.setDocument(new NumOnlyDocument());
        JLabel tip = new JLabel("(数值越大越快,默认35)");
        tip.setFont(FontUtil.getSong12());
        tip.setBounds(163, 95, 300, 25);
        JLabel pathJlable = new JLabel("保存路径：", SwingConstants.RIGHT);
        pathJlable.setFont(FontUtil.getSong12());
        pathJlable.setBounds(10, 120, 60, 35);
        savePath = new JTextField();
        savePath.setBounds(70, 125, 350, 28);
        chBtn = new JButton("更改..");
        chBtn.setFont(FontUtil.getSong12());
        chBtn.setBounds(425, 122, 75, 33);
        chBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser jChooser = new JFileChooser();
                SwingUtils.updateFont(jChooser, FontUtil.getArial12());
                File tem = new File(savePath.getText());
                if (!tem.exists()) {
                    tem = new File("C:/");
                }
                jChooser.setCurrentDirectory(tem);//设置默认打开路径
                jChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);//只能选择目录
                jChooser.setDialogType(JFileChooser.OPEN_DIALOG);//
                jChooser.setDialogTitle("选择默认保存路径");
                int index = jChooser.showDialog(SetPanel.getInstance(), "确定");
                if (index == JFileChooser.APPROVE_OPTION) {
                    try {
                        String selPath = jChooser.getSelectedFile().getCanonicalPath();
                        savePath.setText(selPath);
                    } catch (IOException e1) {
                        log.error("选择默认保存路径失败", e1);
                    }
                }
            }
        });
        isExp = new JCheckBox("是否展开目录");
        isExp.setFont(FontUtil.getSong12());
        isExp.setBounds(10, 155, 260, 28);
        ignoreEmptyFolder = new JCheckBox("忽略空文件夹");
        ignoreEmptyFolder.setFont(FontUtil.getSong12());
        ignoreEmptyFolder.setBounds(10, 185, 260, 28);
        autoFit = new JCheckBox("图片自适应");
        autoFit.setFont(FontUtil.getSong12());
        autoFit.setBounds(10, 215, 150, 25);
        isHModeBox = new JCheckBox("水平浏览模式");
        isHModeBox.setFont(FontUtil.getSong12());
        isHModeBox.setBounds(160, 215, 260, 25);
        passJlable = new JLabel("设置密码：", SwingConstants.RIGHT);
        passJlable.setFont(FontUtil.getSong12());
        passJlable.setBounds(10, 245, 60, 35);
        jf = new JPasswordField();
        jf.setBounds(66, 248, 150, 28);
        JLabel tip2 = new JLabel("(软件重启后生效)");
        tip2.setFont(FontUtil.getSong12());
        tip2.setBounds(217, 250, 200, 25);
        proxyPanel = new JPanel();
        proxyPanel.setBounds(4, 290, 535, 140);
        proxyPanel.setLayout(null);
        TitledBorder titledBorder = new TitledBorder("代理设置");
        titledBorder.setTitleFont(FontUtil.getSong12());
        proxyPanel.setBorder(titledBorder);
        userProxyBox = new JCheckBox("使用HTTP代理");
        userProxyBox.setBounds(13, 20, 150, 35);
        userProxyBox.setFont(FontUtil.getSong12());
        userProxyBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                userProxyBox();
            }
        });
        proxyHostLabel = new JLabel("地址：", SwingConstants.RIGHT);
        proxyHostLabel.setFont(FontUtil.getSong12());
        proxyHostLabel.setBounds(6, 50, 60, 35);
        proxyHostField = new JTextField();
        proxyHostField.setBounds(68, 55, 120, 28);
        proxyPortLabel = new JLabel("端口：", SwingConstants.RIGHT);
        proxyPortLabel.setFont(FontUtil.getSong12());
        proxyPortField = new JTextField();
        proxyPortField.setDocument(new NumOnlyDocument());
        proxyPortLabel.setBounds(200, 50, 60, 35);
        proxyPortField = new JTextField();
        proxyPortField.setBounds(260, 55, 120, 28);
        proxyUserNameLable = new JLabel("用户名：", SwingConstants.RIGHT);
        proxyUserNameLable.setFont(FontUtil.getSong12());
        proxyUserNameLable.setBounds(6, 80, 60, 35);
        proxyUserNameField = new JTextField();
        proxyUserNameField.setBounds(68, 85, 120, 28);
        proxyPasswordLabel = new JLabel("密码：", SwingConstants.RIGHT);
        proxyPasswordLabel.setFont(FontUtil.getSong12());
        proxyPasswordLabel.setBounds(200, 80, 60, 35);
        proxyPasswordField = new JPasswordField();
        proxyPasswordField.setBounds(260, 85, 120, 28);

        proxyPanel.add(userProxyBox);
        proxyPanel.add(proxyHostLabel);
        proxyPanel.add(proxyHostField);
        proxyPanel.add(proxyPortLabel);
        proxyPanel.add(proxyPortField);
        proxyPanel.add(proxyUserNameLable);
        proxyPanel.add(proxyUserNameField);
        proxyPanel.add(proxyPasswordLabel);
        proxyPanel.add(proxyPasswordField);
        csBtn = new JButton("取消");
        csBtn.setFont(FontUtil.getSong12());
        csBtn.setBounds(450, 430, 75, 33);
        csBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SetPanel.getInstance().setVisible(false);
            }
        });
        saBtn = new JButton("确定");
        saBtn.setFont(FontUtil.getSong12());
        saBtn.setBounds(375, 430, 75, 33);
        saBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                save();
                listService.reloadTreeData();
                SetPanel.getInstance().setVisible(false);
            }
        });
        generalPanel.add(fileJlable);
        generalPanel.add(jpg);
        generalPanel.add(png);
        generalPanel.add(win);
        generalPanel.add(gif);
        generalPanel.add(speedJlable);
        generalPanel.add(speed);
        generalPanel.add(tip);
        generalPanel.add(pathJlable);
        generalPanel.add(isExp);
        generalPanel.add(ignoreEmptyFolder);
        generalPanel.add(autoFit);
        generalPanel.add(passJlable);
        generalPanel.add(jf);
        generalPanel.add(tip2);
        generalPanel.add(isHModeBox);
        container.add(savePath);
        container.add(chBtn);
        container.add(csBtn);
        container.add(saBtn);
        container.add(generalPanel);
        container.add(proxyPanel);
    }

    private void userProxyBox() {
        if (!userProxyBox.isSelected()) {
            proxyHostField.setEnabled(false);
            proxyPortField.setEnabled(false);
            proxyUserNameField.setEnabled(false);
            proxyPasswordField.setEnabled(false);
        } else {
            proxyHostField.setEnabled(true);
            proxyPortField.setEnabled(true);
            proxyUserNameField.setEnabled(true);
            proxyPasswordField.setEnabled(true);
        }
    }

    private void save() {
        java.util.Set<String> list = new HashSet<String>();
        if (jpg.isSelected()) {
            list.add("jpg");
            list.add("JPG");
            list.add("jpeg");
            list.add("JPEG");
        }
        if (gif.isSelected()) {
            list.add("gif");
            list.add("GIF");
        }
        if (win.isSelected()) {
            list.add("bmp");
            list.add("BMP");
        }
        if (png.isSelected()) {
            list.add("png");
            list.add("PNG");
        }
        SystemCache handler = SystemCache.getInstance();
        sysdata = handler.getData();
        if (!isExp.isSelected()) {
            sysdata.setExpland(false);
        } else {
            sysdata.setExpland(true);
        }
        if (!autoFit.isSelected()) {
            sysdata.setAutoFit(false);
        } else {
            sysdata.setAutoFit(true);
        }
        if (!ignoreEmptyFolder.isSelected()) {
            sysdata.setIgnoreEmptyFolder(false);
        } else {
            sysdata.setIgnoreEmptyFolder(true);
        }
        if (!isHModeBox.isSelected()) {
            sysdata.setHMode(false);
        } else {
            sysdata.setHMode(true);
        }

        String pass = new String(jf.getPassword());
        if (!"".equals(pass.trim())) {
            pass = MD5Util.getEncryptedPwd(pass); //MD5加密
        }
        if (userProxyBox.isSelected()) {
            sysdata.setUseProxy(true);
        } else {
            sysdata.setUseProxy(false);
        }
        proxyHostField.setEnabled(false);
        proxyPortField.setEnabled(false);
        proxyUserNameField.setEnabled(false);
        proxyPasswordField.setEnabled(false);
        sysdata.setProxyHost(proxyHostField.getText());
        String port = proxyPortField.getText();
        port = port == null ? "0" : port;
        port = "".equals(port.trim()) ? "0" : port.trim();
        sysdata.setProxyPort(Integer.parseInt(port));
        sysdata.setProxyUserName(proxyUserNameField.getText());
        sysdata.setProxyUserPassword(new String(proxyPasswordField.getPassword()));
        sysdata.setTypeList(list);
        sysdata.setSpeed(speed.getText() == null || "".equals(speed.getText()) ? 30 : Integer.parseInt(speed.getText()));
        sysdata.setSavePath(savePath.getText());
        sysdata.setPassword(pass);
        handler.update();
    }

    public JTextField getSpeed() {
        return speed;
    }

    public JCheckBox getJpg() {
        return jpg;
    }

    public JCheckBox getWin() {
        return win;
    }

    public JCheckBox getPng() {
        return png;
    }

    public JCheckBox getGif() {
        return gif;
    }

    public JTextField getSavePath() {
        return savePath;
    }

    public JCheckBox getExp() {
        return isExp;
    }

    public JPasswordField getJf() {
        return jf;
    }

    public JCheckBox getIgnoreEmptyFolder() {
        return ignoreEmptyFolder;
    }

    public JCheckBox getAutoFit() {
        return autoFit;
    }

    public JTextField getProxyHostField() {
        return proxyHostField;
    }

    public JTextField getProxyPortField() {
        return proxyPortField;
    }

    public JTextField getProxyUserNameField() {
        return proxyUserNameField;
    }

    public JPasswordField getProxyPasswordField() {
        return proxyPasswordField;
    }

    public JCheckBox getUserProxyBox() {
        return userProxyBox;
    }

    public JCheckBox getHModeBox() {
        return isHModeBox;
    }
}

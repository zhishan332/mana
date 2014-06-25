package com.wq.ui;

import com.wq.constans.Constan;
import com.wq.tools.Arranger;
import com.wq.util.FontUtil;
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

/**
 * Created with IntelliJ IDEA.
 * User: wangq
 * Date: 14-6-21
 * Time: 下午9:55
 * To change this template use File | Settings | File Templates.
 */
public class ToolPanel extends JFrame implements Page {
    private static final Logger log = LoggerFactory.getLogger(ToolPanel.class);
    private static ToolPanel toolPanel;

    private ToolPanel() {
        constructPlate();
        constructPage();
    }

    public static ToolPanel getInstance() {
        if (toolPanel == null) {
            toolPanel = new ToolPanel();
        }
        return toolPanel;
    }

    @Override
    public void constructPlate() {
        this.setTitle("工具");
        this.setSize(new Dimension(650, 350));
        this.setLocationRelativeTo(null);
        this.setLayout(null);
        this.setResizable(false);
        Toolkit tk = Toolkit.getDefaultToolkit();
        Image mImage = tk.createImage(Constan.RESPAHT + "res/img/logo.png");
        setIconImage(mImage);
        this.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });
    }

    @Override
    public void constructPage() {
        Container container = this.getContentPane();
        createArrangeToolPanel(container);
    }

    private void createArrangeToolPanel(final Container container) {
        JPanel arrangePanel = new JPanel();
        arrangePanel.setBounds(4, 1, 635, 290);
        arrangePanel.setLayout(null);
        TitledBorder generalPanelBorder = new TitledBorder("文件夹整合");
        generalPanelBorder.setTitleFont(FontUtil.getSong12());
        arrangePanel.setBorder(generalPanelBorder);

        JLabel srcLabel = new JLabel("源文件夹：", SwingConstants.RIGHT);
        srcLabel.setBounds(10, 12, 80, 35);
        final JTextField srcTextField = new JTextField();
        srcTextField.setBounds(90, 17, 460, 28);
        JButton srcChBtn = new JButton("选择..");
        srcChBtn.setFont(FontUtil.getSong12());
        srcChBtn.setBounds(555, 14, 70, 33);
        srcChBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser jChooser = new JFileChooser();
                SwingUtils.updateFont(jChooser, FontUtil.getArial12());
                File tem = new File(srcTextField.getText());
                if (!tem.exists()) {
                    tem = new File("C:/");
                }
                jChooser.setCurrentDirectory(tem);//设置默认打开路径
                jChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);//只能选择目录
                jChooser.setDialogType(JFileChooser.OPEN_DIALOG);//
                jChooser.setDialogTitle("选择默认保存路径");
                jChooser.setMultiSelectionEnabled(true);
                int index = jChooser.showDialog(ToolPanel.getInstance(), "确定");
                if (index == JFileChooser.APPROVE_OPTION) {
                    File[] selectedFiles = jChooser.getSelectedFiles();
                    if (null != selectedFiles) {
                        StringBuilder fileNames = new StringBuilder();
                        for (File file : selectedFiles) {
                            fileNames.append(file.getAbsolutePath());
                            fileNames.append(";");
                        }
                        srcTextField.setText(fileNames.toString());
                    }
                }
            }
        });
        JLabel destLabel = new JLabel("目的文件夹：", SwingConstants.RIGHT);
        destLabel.setBounds(10, 47, 80, 35);
        final JTextField destTextField = new JTextField();
        destTextField.setBounds(90, 52, 460, 28);
        JButton destChBtn = new JButton("选择..");
        destChBtn.setFont(FontUtil.getSong12());
        destChBtn.setBounds(555, 49, 70, 33);
        destChBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser jChooser = new JFileChooser();
                SwingUtils.updateFont(jChooser, FontUtil.getArial12());
                File tem = new File(destTextField.getText());
                if (!tem.exists()) {
                    tem = new File("C:/");
                }
                jChooser.setCurrentDirectory(tem);//设置默认打开路径
                jChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);//只能选择目录
                jChooser.setDialogType(JFileChooser.OPEN_DIALOG);//
                jChooser.setDialogTitle("选择默认保存路径");
                int index = jChooser.showDialog(ToolPanel.getInstance(), "确定");
                if (index == JFileChooser.APPROVE_OPTION) {
                    String selPath = jChooser.getSelectedFile().getAbsolutePath();
                    destTextField.setText(selPath);
                }
            }
        });
        final JCheckBox renameCb = new JCheckBox("是否重命名", true);
        renameCb.setFont(FontUtil.getSong12());
        renameCb.setBounds(15, 90, 150, 28);
        final JCheckBox deleteCb = new JCheckBox("是否删除源文件夹", true);
        deleteCb.setFont(FontUtil.getSong12());
        deleteCb.setBounds(170, 90, 150, 28);
        JLabel typeLabel = new JLabel("文件类型：", SwingConstants.RIGHT);
        typeLabel.setBounds(325, 85, 80, 35);
        String labels[] = {"只是图片", "只是视频", "全部"};
        final JComboBox comboBox = new JComboBox(labels);
        comboBox.setBounds(410, 90, 140, 28);
        JButton subBtn = new JButton("整合");
        subBtn.setFont(FontUtil.getSong12());
        subBtn.setBounds(475, 130, 75, 33);
        subBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                VpicFrame.getInstance().getRunInfo().setText("正在整合文件...");
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        arrange(srcTextField, destTextField, renameCb, deleteCb, comboBox);
                        VpicFrame.getInstance().getRunInfo().setText("整合成功");
                    }
                });
                ToolPanel.getInstance().setVisible(false);
            }
        });
        arrangePanel.add(srcLabel);
        arrangePanel.add(srcTextField);
        arrangePanel.add(srcChBtn);
        arrangePanel.add(destLabel);
        arrangePanel.add(destTextField);
        arrangePanel.add(destChBtn);
        arrangePanel.add(renameCb);
        arrangePanel.add(deleteCb);
        arrangePanel.add(typeLabel);
        arrangePanel.add(comboBox);
        arrangePanel.add(subBtn);
        container.add(arrangePanel);
    }

    private void arrange(JTextField srcTextField, JTextField destTextField, JCheckBox renameCb, JCheckBox deleteCb, JComboBox comboBox) {
        String files = srcTextField.getText();
        boolean delete = true;
        boolean rename = true;
        if (!renameCb.isSelected()) rename = false;
        if (!deleteCb.isSelected()) delete = false;
        String destFolder = destTextField.getText();
        if (destFolder == null || "".endsWith(destFolder.trim())) return;
        String selectItem = (String) comboBox.getSelectedItem();
        String[] ext;
        if ("只是图片".equals(selectItem)) {
            ext = Arranger.imgExtension;
        } else if ("只是视频".equals(selectItem)) {
            ext = Arranger.videoExtension;
        } else {
            ext = null;
        }
        if (files != null) {
            String[] fileArry = files.split(";");
            for (String folder : fileArry) {
                if (null == folder || "".equals(folder.trim())) continue;
                Arranger arranger = new Arranger();
                arranger.simpleArrange(folder.trim(), destFolder.trim(), delete, rename, ext);
            }
        }
    }
}

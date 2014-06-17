package com.wq.ui;

import com.sun.awt.AWTUtilities;
import com.wq.cache.SystemCache;
import com.wq.constans.Constan;
import com.wq.ui.module.MesBox;
import com.wq.util.FileUtil;
import com.wq.util.FontUtil;
import com.wq.util.ImageUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created with IntelliJ IDEA.
 * User: wangq
 * Date: 12-8-3
 * Time: 下午1:47
 * 右键图片菜单
 */
public class PicMenu extends JPopupMenu implements Page {
    private static final Logger log = LoggerFactory.getLogger(PicMenu.class);
    private static PicMenu picMenu = null;
    private JMenuItem hideItem;
    private JMenuItem showItem;
    private String filePath;
    private JLabel jLabel;

    private PicMenu() {
        constructPlate();
        constructPage();
    }

    public static PicMenu getInstance() {
        if (picMenu == null) {
            picMenu = new PicMenu();
        }
        return picMenu;
    }

    @Override
    public void constructPlate() {
        this.setSize(180, 190);
        this.setPreferredSize(new Dimension(180, 190));
    }

    @Override
    public void constructPage() {
//        saveItem = new JMenuItem("全部保存");
//        saveItem.setFont(FontUtil.getSong12());
//        saveItem.addActionListener(new java.awt.event.ActionListener() {
//            public void actionPerformed(ActionEvent e) {
////                Component[] comps = ViewContentPanel.getInstance().getComponents();
////                for (Component component : comps) {
////                    System.out.println("1111=" + component.getName());
////                }
//            }
//        });
//        saveItem.setVisible(false);//网页取图后显示
//        KeyStroke keyStroke1 = KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK);
//        saveItem.setAccelerator(keyStroke1);
//        this.add(saveItem);
        hideItem = new JMenuItem("隐藏右方菜单", new ImageIcon(Constan.RESPAHT + "res/img/left.png"));
        hideItem.setFont(FontUtil.getDefault());
        hideItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ListPanel.getInstance().hidePanel();
                hideItem.setEnabled(false);
                showItem.setEnabled(true);
                ViewPanelMenu.getInstance().getHideItem().setEnabled(false);
                ViewPanelMenu.getInstance().getShowItem().setEnabled(true);
            }
        });
        this.add(hideItem);
        showItem = new JMenuItem("显示右方菜单", new ImageIcon(Constan.RESPAHT + "res/img/right.png"));
        showItem.setFont(FontUtil.getDefault());
        showItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ListPanel.getInstance().showPanel();
                hideItem.setEnabled(true);
                showItem.setEnabled(false);
                ViewPanelMenu.getInstance().getHideItem().setEnabled(true);
                ViewPanelMenu.getInstance().getShowItem().setEnabled(false);
            }
        });
        if (ListPanel.getInstance().isVisible()) {
            hideItem.setEnabled(true);
            showItem.setEnabled(false);
        } else {
            hideItem.setEnabled(false);
            showItem.setEnabled(true);
        }
        this.add(showItem);
        this.add(new JPopupMenu.Separator());

        JMenuItem copyItem = new JMenuItem("复制");
        copyItem.setFont(FontUtil.getDefault());
        copyItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (filePath.endsWith(".gif") || filePath.endsWith(".GIF")) return;//todo 不支持GIF复制粘贴
                Toolkit tk = Toolkit.getDefaultToolkit();
                Image image = tk.createImage(filePath);
                ImageUtils.copy(image);
            }
        });
        JMenuItem saveAsItem = new JMenuItem("另存为");
        saveAsItem.setFont(FontUtil.getDefault());
        saveAsItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser jChooser2 = new JFileChooser();
                jChooser2.setSelectedFile(new File(SystemCache.getInstance().getData().getSavePath() + new File(filePath).getName()));//设置默认打开路径
                jChooser2.setDialogType(JFileChooser.SAVE_DIALOG);//设置保存对话框
                int index = jChooser2.showDialog(VpicFrame.getInstance(), "另存为");
                if (index == JFileChooser.APPROVE_OPTION) {
                    try {
                        String writePath = jChooser2.getSelectedFile().getCanonicalPath();
                        FileUtil.copyFile(new File(filePath), new File(writePath));
                    } catch (IOException e1) {
                        log.error("另存为失败", e1);
                    }
                }
            }
        });

        JMenuItem delItem = new JMenuItem("删除(慎：磁盘删除)", new ImageIcon(Constan.RESPAHT + "res/img/del.png"));
        delItem.setFont(FontUtil.getDefault());
        delItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String path = PicMenu.getInstance().filePath;
                File file = new File(path);
                if (jLabel != null) {
                    ViewScrollPanel.getInstance().getViewContentPanel().remove(jLabel);
                    if (file.exists()) {
                        boolean flag = file.delete();
                        if(!flag){
                            MesBox.warn("删除失败，请到磁盘删除");
                        }
                    }
//                        ViewContentPanel.getInstance().remove(jLabel); //移除组件刷新
                    jLabel = null;
                }
                ViewScrollPanel.getInstance().getViewContentPanel().repaint();
//                    ViewContentPanel.getInstance().updateUI();
            }
        });

        JMenuItem openItem = new JMenuItem("文件夹中显示", new ImageIcon(Constan.RESPAHT + "res/img/fire.png"));
        openItem.setFont(FontUtil.getDefault());
        openItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    String path = PicMenu.getInstance().filePath;
                    File file = new File(path);
                    java.awt.Desktop.getDesktop().open(file.getParentFile());
                } catch (IOException exp) {
                    log.error("文件夹中显示失败", exp);
                }
            }
        });
        this.add(openItem);
        this.add(copyItem);
        this.add(saveAsItem);
        this.add(delItem);
        this.add(new JPopupMenu.Separator());
        JMenuItem propItem = new JMenuItem("属性");
        propItem.setFont(FontUtil.getDefault());
        propItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                PropDialog propDialog = PropDialog.getInstance();
                propDialog.setVisible(true);
                AWTUtilities.setWindowOpacity(propDialog, 0.85f);
                File file = new File(filePath);
                String lenText = "";
                Long len = file.length() / 1024;
                if (len > 1024) {
                    len = len / 1024;
                    lenText = len + "Mb";
                } else {
                    lenText = len + "Kb";
                }
                Calendar cal = Calendar.getInstance();
                long time = file.lastModified();
                cal.setTimeInMillis(time);
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                FileInputStream fis = null;
                try {
                    fis = new FileInputStream(file);
                } catch (FileNotFoundException e1) {
                    log.error("属性显示失败", e1);
                }
                BufferedImage bufferedImg = null;
                try {
                    bufferedImg = ImageIO.read(fis);
                } catch (IOException e1) {
                    log.error("属性显示失败", e1);
                }
                assert bufferedImg != null;
                int imgWidth = bufferedImg.getWidth();
                int imgHeight = bufferedImg.getHeight();
                propDialog.getPicName().setText("  图片名称：" + file.getName());
                propDialog.getPath().setText("  图片路径：" + file.getAbsolutePath());
                propDialog.getDate().setText("  修改时间：" + formatter.format(cal.getTime()));
                propDialog.getLen().setText("  文件大小：" + lenText);
                propDialog.getType().setText("  文件类型：" + FileUtil.getFileType(file.getName()));
                propDialog.getXp().setText("  分辨率：" + imgWidth + "*" + imgHeight);
//                com.sun.awt.AWTUtilities.setWindowOpacity(propDialog, 0.9f);
            }
        });
        this.add(propItem);
    }

    public JMenuItem getHideItem() {
        return hideItem;
    }

    public JMenuItem getShowItem() {
        return showItem;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public void setjLabel(JLabel jLabel) {
        this.jLabel = jLabel;
    }
}


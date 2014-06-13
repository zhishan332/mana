package com.wq.ui;

import com.wq.cache.AllCache;
import com.wq.constans.Constan;
import com.wq.model.SysData;
import com.wq.service.SysDataHandler;
import com.wq.util.FontUtil;
import com.wq.util.ImageUtils;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: wangq
 * Date: 12-8-2
 * Time: 下午4:02
 * 图床 ,对JAVA6.0,它支持得图象格式有[BMP, bmp, jpg, JPG, wbmp, jpeg, png, PNG, JPEG, WBMP, GIF, gif]
 */
public class ViewContentPanel extends JPanel implements Page {
    private static ViewContentPanel viewContentPanel;
    private SysData data = SysDataHandler.getInstance().getData();
//    public static ViewContentPanel getInstance() {
//        if (viewContentPanel == null) {
//            viewContentPanel = new ViewContentPanel();
//        }
//        return viewContentPanel;
//    }

    private List<String> list = new LinkedList<String>();

    public ViewContentPanel(List<String> list) {
        this.list = list;
        constructPlate();
        constructPage();
    }

    @Override
    public void constructPlate() {
        if (data.isHMode()) {
            BoxLayout layout = new BoxLayout(this, BoxLayout.X_AXIS);
            this.setLayout(layout);
            this.add(Box.createHorizontalGlue());
        } else {
            BoxLayout layout = new BoxLayout(this, BoxLayout.Y_AXIS);
            this.setLayout(layout);
            this.add(Box.createVerticalGlue());
        }
        this.setBackground(Constan.lightColor);
    }


    @Override
    public void constructPage() {
        loadPic(list);
        if (!data.isHMode()) {
            this.add(Box.createVerticalGlue());
        } else {
            this.add(Box.createHorizontalGlue());
        }
        this.repaint();
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if ((e.getModifiers() & MouseEvent.BUTTON3_MASK) != 0
                        && !e.isControlDown() && !e.isShiftDown()) {
                    showMenu(e);
                }
            }
        });
    }

    /**
     * 处理一般图片
     *
     * @param path
     */
    private void dealCommonPic(final String path) {
        BufferedImage bufferedImage = null;
        Image img = null;
        boolean isgif = false;
        try {
            if (path.endsWith(".gif") || path.endsWith(".GIF")) {
                isgif = true;
                Toolkit tk = Toolkit.getDefaultToolkit();
                img = tk.createImage(path);
                bufferedImage = ImageUtils.toBufferedImage(img);
            } else {
                bufferedImage = ImageIO.read(new FileInputStream(path));
            }

        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        if (bufferedImage == null) return;
        final JLabel jLabel = new JLabel();
        ImageIcon icon = null;
        if (data.isHMode()) {
            int maxHeight = AllCache.getInstance().getMaxPicPanelHeight();
            int imgHeight = bufferedImage.getHeight();
            //等比缩放
            if (data.isAutoFit() && imgHeight > maxHeight) {
                double bo = (double) maxHeight / (double) imgHeight;
                bo = Math.round(bo * 10000) / 10000.0;
                bufferedImage = ImageUtils.reduceImg(bufferedImage, bo);
                icon = new ImageIcon(bufferedImage);
            } else {
                if (!isgif) {
                    icon = new ImageIcon(bufferedImage);
                } else {
                    icon = new ImageIcon(img);
                }
            }
        } else {
            int maxWidth = AllCache.getInstance().getMaxPicPanelWidth();
            int imgWidth = bufferedImage.getWidth();
            //等比缩放
            if (data.isAutoFit() && imgWidth > maxWidth) {
                double bo = (double) maxWidth / (double) imgWidth;
                bo = Math.round(bo * 10000) / 10000.0;
                bufferedImage = ImageUtils.reduceImg(bufferedImage, bo);
                icon = new ImageIcon(bufferedImage);
            } else {
                if (!isgif) {
                    icon = new ImageIcon(bufferedImage);
                } else {
                    icon = new ImageIcon(img);
                }
            }
        }
        jLabel.setIcon(icon);
        jLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if ((e.getModifiers() & MouseEvent.BUTTON3_MASK) != 0
                        && !e.isControlDown() && !e.isShiftDown()) {
                    PicMenu.getInstance().show(jLabel, e.getX(), e.getY());
                    PicMenu.getInstance().setFilePath(path);
                    PicMenu.getInstance().setjLabel(jLabel);
                }
            }
        });
        if (!data.isHMode()) {
            jLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        } else {
            jLabel.setAlignmentY(Component.CENTER_ALIGNMENT);
        }
        this.add(jLabel);
        if (!data.isHMode()) {
            this.add(Box.createVerticalStrut(7));
        } else {
            this.add(Box.createHorizontalStrut(7));
        }
        this.validate();
    }

    public void loadPic(final List<String> list) {
        if (list != null) {
            int i = 0;
            for (final String path : list) {
                File file = new File(path);
                if (!file.exists()) continue;
                if (i == 0 || i == 1) {
                    dealCommonPic(path);
                } else {
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            dealCommonPic(path);
                        }
                    });
                }
                i++;
            }
        } else {
            JLabel jLabel = new JLabel();
            jLabel.setFont(FontUtil.getSong13());
            jLabel.setText("<html><h1>没有打开任何映射文件夹(No Files are open)</h1><hr>" +
                    "<li>点击左上方按钮映射本地图片文件夹(Choose the folder with add button)</li>" +
                    "<li>选择左侧映射文件夹查看图片(Open the photo by click left folder tree)</li>" +
                    "</html>");
            jLabel.setForeground(Color.white);
            jLabel.setHorizontalAlignment(JLabel.CENTER);
            this.add(jLabel);
        }
        this.validate();
    }

    private void showMenu(MouseEvent e) {
        ViewPanelMenu.getInstance().show(this, e.getX(), e.getY()); //右键菜单显示
    }

    public void setList(List<String> list) {
        this.list = list;
    }
}

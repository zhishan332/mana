package com.wq.ui;

import com.wq.cache.LocalFileCache;
import com.wq.cache.SystemCache;
import com.wq.constans.Constan;
import com.wq.exception.ManaException;
import com.wq.model.FileCacheModel;
import com.wq.service.ListService;
import com.wq.service.ListServiceImpl;
import com.wq.util.ButtonUtil;
import com.wq.util.FontUtil;
import com.wq.util.ImageLabelUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * 图片展示Panel,对JAVA6.0,它支持得图象格式有[BMP, bmp, jpg, JPG, wbmp, jpeg, png, PNG, JPEG, WBMP, GIF, gif]
 *
 * @author wangqing
 * @since 1.0.0
 */
public class ViewContentPanel extends JPanel implements Page {
    private static final Logger log = LoggerFactory.getLogger(ViewContentPanel.class);
    private JLabel jWaitLabel = new JLabel();
    private ListService listService = ListServiceImpl.getInstance();
    private com.wq.model.SysData data = SystemCache.getInstance().getData();
    private List<String> list = new LinkedList<String>();
    private int start;
    private String folder;

    public ViewContentPanel(String folder, List<String> list, int start) {
        this.list = list;
        this.start = start;
        this.folder = folder;
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
        log.info("开始加载图片....................");
        this.removeAll();
        if (list != null && !list.isEmpty() && list.size() > 5) {
            showWait();
        }
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                if (list == null) {
                    log.warn("图片list为空");
                    return;
                }
                long beg = System.currentTimeMillis();
                List<String> loadList = new ArrayList<String>();
                for (int i = start; i < list.size() && i < start +  Constan.PAGE_SHOW_NUM; i++) {
                    loadList.add(list.get(i));
                }
                if (start > 0) {
                    addPreButton(list, start);
                }
                loadPic(loadList);
                hideWait();
                if (list.size() > (start +  Constan.PAGE_SHOW_NUM)) {
                    addNextButton(list, start);
                }
                log.info("图片加载完成，耗时：" + (System.currentTimeMillis() - beg) + "ms");
            }
        });
        this.repaint();
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if ((e.getModifiers() & MouseEvent.BUTTON3_MASK) != 0
                        && !e.isControlDown() && !e.isShiftDown()) {
                    showMenu(e);
                }
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                AboutDialog.getInstance().setVisible(false);
            }
        });
    }

    public void loadPic(final List<String> list) {
        if (list != null) {
            File folderFile = new File(folder);
            final long mdate = folderFile.lastModified();
            final long hashCode = list.hashCode();
            log.info("查询缓存，hashcode:" + hashCode + "，修改时间：" + mdate);
            FileCacheModel cache = LocalFileCache.get(hashCode);
            if (null != cache) {
                log.info("命中缓存，缓存修改时间：" + cache.getModifyDate() + ",当前修改时间：" + mdate);
            }
            List<JLabel> labels = new ArrayList<JLabel>();
            if (cache != null && cache.getModifyDate() == mdate) {
                log.info("从res/cache下加载图片>>>>>>>>>>>>>>>>>>");
                labels = (List<JLabel>) cache.getObject();
                log.info("从cache中获取图片数："+labels.size());
            } else {
                boolean isSuc = true;
                for (final String path : list) {
                    File file = new File(path);
                    if (!file.exists()) continue;
                    JLabel imgLabel;
                    try {
                        imgLabel = ImageLabelUtil.getImageLabel(path);
                    } catch (ManaException e) {
                        isSuc = false;
                        break;
                    }
                    if (null != imgLabel) labels.add(imgLabel);
                }
                if (!labels.isEmpty() && isSuc) {
                    final FileCacheModel fileCacheModel = new FileCacheModel();
                    fileCacheModel.setModifyDate(mdate);
                    fileCacheModel.setObject(labels);
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            LocalFileCache.save(fileCacheModel, list.hashCode());
                        }
                    });
                }
            }
            for (JLabel jLabel : labels) {
                String imgPath = jLabel.getName();
                if (imgPath.endsWith(".gif") || imgPath.endsWith(".GIF")) {
                    log.info("动态图片，重新构造..............");
                    Toolkit tk = Toolkit.getDefaultToolkit();
                    Image img = tk.createImage(imgPath);
                    ImageIcon icon = new ImageIcon(img);
                    jLabel = new JLabel(icon);
                }
                jLabel.addMouseListener(getImgLabelAction(jLabel));
                if (!data.isHMode()) {
                    jLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                    jLabel.setVerticalAlignment(SwingConstants.CENTER);
                } else {
                    jLabel.setAlignmentY(Component.CENTER_ALIGNMENT);
                    jLabel.setHorizontalAlignment(SwingConstants.CENTER);
                }
                if (!data.isHMode()) {
                    this.add(Box.createVerticalStrut(3));
                } else {
                    this.add(Box.createHorizontalStrut(3));
                }
                this.add(jLabel);
                if (!data.isHMode()) {
                    this.add(Box.createVerticalStrut(3));
                } else {
                    this.add(Box.createHorizontalStrut(3));
                }
                this.validate();
            }
        } else {
            JLabel jLabel = new JLabel();
            jLabel.setFont(FontUtil.getSong13());
            jLabel.setText("<html><h1>没有打开任何映射文件夹(No Files are open)</h1><hr>" +
                    "<li>点击左上方按钮映射本地图片文件夹(Choose the folder with add button)</li>" +
                    "<li>选择左侧映射文件夹查看图片(Open the photo by click left folder tree)</li>" +
                    "</html>");
            jLabel.setForeground(Color.white);
            jLabel.setHorizontalAlignment(SwingConstants.CENTER);
            this.add(jLabel);
        }
        this.validate();
    }

    private MouseAdapter getImgLabelAction(final JLabel jLabel) {
        return new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if ((e.getModifiers() & MouseEvent.BUTTON3_MASK) != 0
                        && !e.isControlDown() && !e.isShiftDown()) {
                    PicMenu.getInstance().show(jLabel, e.getX(), e.getY());
                    PicMenu.getInstance().setFilePath(jLabel.getName());
                    PicMenu.getInstance().setjLabel(jLabel);
                }
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                AboutDialog.getInstance().setVisible(false);
            }
        };
    }

    private void showWait() {
        jWaitLabel.setFont(FontUtil.getSong13());
        jWaitLabel.setText("<html><h1>图片略多，正在加载(waiting..)</h1>");
        jWaitLabel.setForeground(Color.white);
        jWaitLabel.setHorizontalAlignment(SwingConstants.CENTER);
        this.add(jWaitLabel);
        this.validate();
    }

    private void hideWait() {
        this.remove(jWaitLabel);
        this.validate();
    }

    private void addPreButton(final List<String> list, final int start) {
        String imgPath = Constan.RESPAHT + "res/img/lt.png";
        String pressImgPath = Constan.RESPAHT + "res/img/lt-b.png";
        if (!data.isHMode()) {
            imgPath = Constan.RESPAHT + "res/img/vlt.png";
            pressImgPath = Constan.RESPAHT + "res/img/vlt-b.png";
        }
        JButton preButton = ButtonUtil.createJButton(imgPath, null, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                listService.loadPic(folder, list, start - 10);
            }
        });
        preButton.setPressedIcon(new ImageIcon(pressImgPath));
        preButton.setToolTipText("上一页");
        this.add(preButton);
        this.validate();
    }

    private void addNextButton(final List<String> list, final int start) {
        String imgPath = Constan.RESPAHT + "res/img/rt.png";
        String pressImgPath = Constan.RESPAHT + "res/img/rt-b.png";
        if (!data.isHMode()) {
            imgPath = Constan.RESPAHT + "res/img/vrt.png";
            pressImgPath = Constan.RESPAHT + "res/img/vrt-b.png";
        }
        JButton nextButton = ButtonUtil.createJButton(imgPath, null, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                listService.loadPic(folder, list, start + 10);
            }
        });
        nextButton.setPressedIcon(new ImageIcon(pressImgPath));
        nextButton.setToolTipText("下一页");
        this.add(nextButton);
        this.validate();
    }

    private void showMenu(MouseEvent e) {
        ViewPanelMenu.getInstance().show(this, e.getX(), e.getY()); //右键菜单显示
    }

    public void setList(List<String> list) {
        this.list = list;
    }
}

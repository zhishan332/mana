package com.wq.ui;

import com.wq.cache.AllCache;
import com.wq.cache.ImageCacheManager;
import com.wq.constans.Constan;
import com.wq.model.SysData;
import com.wq.service.ListService;
import com.wq.service.ListServiceImpl;
import com.wq.service.SysDataHandler;
import com.wq.util.FontUtil;
import com.wq.util.ImageUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
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
    private static final Logger log = LoggerFactory.getLogger(ViewContentPanel.class);
    private static ViewContentPanel viewContentPanel;
    private JLabel jWaitLabel = new JLabel();
    private JButton nextButton;
    private JButton preButton;
    private ListService listService= ListServiceImpl.getInstance();
    private SysData data = SysDataHandler.getInstance().getData();
//    public static ViewContentPanel getInstance() {
//        if (viewContentPanel == null) {
//            viewContentPanel = new ViewContentPanel();
//        }
//        return viewContentPanel;
//    }

    private List<String> list = new LinkedList<String>();
    private int start;
    public ViewContentPanel(List<String> list,int start) {
        this.list = list;
        this.start=start;
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
        showWait();
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                long beg=System.currentTimeMillis();
                List<String> loadList=new ArrayList<String>();
                for(int i=start;i<list.size()&&i<start+10;i++){
                    loadList.add(list.get(i));
                }
                if(start>0){
                    addPreButton(list,start);
                }
                loadPic(loadList);
                hideWait();
                if(list.size()>(start+10)){
                    addNextButton(list,start);
                }
                log.info("图片加载完成，耗时："+(System.currentTimeMillis()-beg)+"ms");
            }
        });

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

        boolean isgif = false;
        Image img = null;
        Object cache = ImageCacheManager.getInstance().getCache().get(path);
        if (path.endsWith(".gif") || path.endsWith(".GIF")) {
            isgif = true;
            if(cache!=null){
                img= (Image) cache;
                log.info("从缓存中加载："+path);
            }else{
                Toolkit tk = Toolkit.getDefaultToolkit();
                img = tk.createImage(path);
                if(null!=img) {
                    ImageCacheManager.getInstance().getCache().put(path,img);
                    log.info("放入缓存："+path);
                }
            }
            bufferedImage = ImageUtils.toBufferedImage(img);
        }else{
            if(cache!=null){
                bufferedImage= (BufferedImage) cache;
                log.info("从缓存中加载："+path);
            }else{
                try {
                    bufferedImage = ImageIO.read(new FileInputStream(path));
                    if(null!=bufferedImage) {
                        ImageCacheManager.getInstance().getCache().put(path,bufferedImage);
                        log.info("放入缓存："+path);
                    }
                } catch (Throwable e) {
                    log.error("读取图片异常", e);
                }
            }
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
            for (final String path : list) {
                File file = new File(path);
                if (!file.exists()) continue;
                dealCommonPic(path);
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

    private void showWait(){
        jWaitLabel.setFont(FontUtil.getSong13());
        jWaitLabel.setText("<html><h1>正在加载(waiting..)</h1>");
        jWaitLabel.setForeground(Color.white);
        jWaitLabel.setHorizontalAlignment(JLabel.CENTER);
        this.add(jWaitLabel);
        this.validate();
    }

    private void hideWait(){
        this.remove(jWaitLabel);
        this.validate();
    }
    private void addPreButton(final List<String> list, final int start){
        preButton=new JButton(new ImageIcon(Constan.RESPAHT + "res/img/pre.png"));
        preButton.setToolTipText("上一页");
        preButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                listService.loadPic(list,start-10);
            }
        });
        this.add(preButton);
        this.validate();
    }

    private void addNextButton(final List<String> list, final int start){
        nextButton=new JButton(new ImageIcon(Constan.RESPAHT + "res/img/next.png"));
        nextButton.setToolTipText("下一页");
        nextButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                listService.loadPic(list,start+10);
            }
        });
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

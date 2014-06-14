package com.wq.ui;

import com.wq.cache.AllCache;
import com.wq.cache.FileCacheHelper;
import com.wq.cache.ImageCacheManager;
import com.wq.constans.Constan;
import com.wq.model.FileCacheModel;
import com.wq.model.SysData;
import com.wq.service.ListService;
import com.wq.service.ListServiceImpl;
import com.wq.service.SysDataHandler;
import com.wq.util.ButtonUtil;
import com.wq.util.FontUtil;
import com.wq.util.ImageLabelUtil;
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
    private String folder;
    public ViewContentPanel(String folder,List<String> list,int start) {
        this.list = list;
        this.start=start;
        this.folder=folder;
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
        if(list!=null && !list.isEmpty() && list.size()>5){
            showWait();
        }
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


    public void loadPic(final List<String> list) {
        if (list != null) {
            File folderFile=new File(folder);
            final long mdate= folderFile.lastModified();
            final long hashCode=list.hashCode();
            log.info("查询缓存，hashcode:"+hashCode+"，修改时间："+mdate);
            FileCacheModel cache = FileCacheHelper.get(hashCode);
            if(null!=cache){
                log.info("命中缓存，缓存修改时间："+cache.getModifyDate()+",当前修改时间："+mdate);
            }
            List<JLabel> labels=new ArrayList<JLabel>();
            if(cache!=null && cache.getModifyDate()==mdate){
                log.info("从res/cache下加载图片>>>>>>>>>>>>>>>>>>");
                labels= (List<JLabel>) cache.getObject();
            }else{
                for (final String path : list) {
                    File file = new File(path);
                    if (!file.exists()) continue;
                    JLabel imgLabel = ImageLabelUtil.getImageLabel(path);
                    if(null!=imgLabel) labels.add(imgLabel);
                }
                final FileCacheModel fileCacheModel=new FileCacheModel();
                fileCacheModel.setModifyDate(mdate);
                fileCacheModel.setObject(labels);
               if(!labels.isEmpty()){
                   SwingUtilities.invokeLater(new Runnable() {
                       @Override
                       public void run() {
                           FileCacheHelper.save(fileCacheModel, list.hashCode());
                       }
                   });
               }
            }
            for(JLabel jLabel:labels){
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
        jWaitLabel.setText("<html><h1>图片略多，正在加载(waiting..)</h1>");
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
        JButton preButton = ButtonUtil.createJButton(Constan.RESPAHT + "res/img/pre.png", null, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                listService.loadPic(folder,list,start-10);
            }
        });
        preButton.setToolTipText("上一页");
        this.add(preButton);
        this.validate();
    }

    private void addNextButton(final List<String> list, final int start){
        JButton nextButton = ButtonUtil.createJButton(Constan.RESPAHT + "res/img/next.png", null, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                listService.loadPic(folder,list, start + 10);
            }
        });
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

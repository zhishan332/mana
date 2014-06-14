package com.wq.run;

import com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel;
import com.wq.cache.AllCache;
import com.wq.cache.FileCacheHelper;
import com.wq.service.SysDataHandler;
import com.wq.ui.FirstDialog;
import com.wq.ui.ValidateDialog;
import com.wq.ui.VpicFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: wangq
 * Date: 12-8-2
 * Time: 上午10:55
 * Mana图关键软件是管理图片的软件，让用户浏览本机图片时，如同浏览网页图片一样，方便快捷
 * 界面启动
 */
public class StartUi {
    private static final Logger log = LoggerFactory.getLogger(StartUi.class);
//    private static LightLog logger = LightLog.getInstance(StartUi.class);
    public static void main(String args[]) {
        //设置样式
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    JFrame.setDefaultLookAndFeelDecorated(true);
                    UIManager.setLookAndFeel(new NimbusLookAndFeel());//nimbus样式
                } catch (Exception e) {
                   e.printStackTrace();
                }
            }
        });
        //获取用户密码
        String password = SysDataHandler.getInstance().getData().getPassword();
        if (password == null || "".equals(password)) {
            VpicFrame vpic = VpicFrame.getInstance();
            vpic.setVisible(true);
            if (AllCache.getInstance().getMenu().isEmpty()) { //无文件夹映射
                FirstDialog.getInstance().setVisible(true);
            }
//            SwingUtilities.invokeLater(new Runnable() {
//                @Override
//                public void run() {
//                    listService.reloadTreeData(); //页面展开后刷新一下TREE，在JSplitPane作用下，不刷新，无法显示JTree
//                    ListPanel.getInstance().showPanel();
//                }
//            });
        } else {
            ValidateDialog dialog = new ValidateDialog();
            dialog.setVisible(true);
        }
        FileCacheHelper.asynIndex();//异步构建缓存索引
//        SwingUtilities.invokeLater();
    }
}

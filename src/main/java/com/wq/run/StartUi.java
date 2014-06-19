package com.wq.run;

import com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel;
import com.wq.cache.AllCache;
import com.wq.cache.LocalFileCache;
import com.wq.cache.SystemCache;
import com.wq.service.ListServiceImpl;
import com.wq.ui.FirstDialog;
import com.wq.ui.ValidateDialog;
import com.wq.ui.VpicFrame;
import org.pushingpixels.substance.api.SubstanceLookAndFeel;
import org.pushingpixels.substance.api.skin.*;
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

    public static void main(String args[]) {

        //设置样式
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    JFrame.setDefaultLookAndFeelDecorated(true);
                    JDialog.setDefaultLookAndFeelDecorated(true);
//                    UIManager.setLookAndFeel(new SubstanceCremeCoffeeLookAndFeel());
//                    UIManager.setLookAndFeel(new SubstanceDustLookAndFeel());
//                    UIManager.setLookAndFeel(new SubstanceEmeraldDuskLookAndFeel());
                    UIManager.setLookAndFeel(new SubstanceGraphiteAquaLookAndFeel());//默认
//                    SubstanceLookAndFeel.setSkin(new BusinessBlackSteelSkin());
//                    UIManager.setLookAndFeel(new NimbusLookAndFeel());//nimbus样式
//                    UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
                    //获取用户密码
                    String password = SystemCache.getInstance().getData().getPassword();
                    if (password == null || "".equals(password)) {
                        VpicFrame vpic = VpicFrame.getInstance();
                        vpic.setVisible(true);
                        if (AllCache.getInstance().getMenu().isEmpty()) { //无文件夹映射
                            FirstDialog.getInstance().setVisible(true);
                        }
                    } else {
                        ValidateDialog dialog = new ValidateDialog();
                        dialog.setVisible(true);
                    }
                } catch (Exception e) {
                    log.error("加载样式失败", e);
                }
                ListServiceImpl.getInstance().reloadTreeData();
            }
        });
        LocalFileCache.asynIndex();//异步构建缓存索引
    }
}

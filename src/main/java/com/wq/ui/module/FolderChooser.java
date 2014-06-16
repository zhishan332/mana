package com.wq.ui.module;

import com.wq.cache.FileCacheHelper;
import com.wq.cache.SystemCache;
import com.wq.service.ListServiceImpl;
import com.wq.ui.VpicFrame;
import com.wq.util.FontUtil;
import com.wq.util.SwingUtils;
import com.wq.util.SystemUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * To change this template use File | Settings | File Templates.
 *
 * @author wangqing
 * @since 1.0.0
 */
public class FolderChooser extends JFileChooser {
    private static final Logger log = LoggerFactory.getLogger(FolderChooser.class);

    public FolderChooser() {
        init();
    }

    private void init() {
        SwingUtils.updateFont(this, FontUtil.getDefault());
        File tem = new File(SystemCache.getInstance().getData().getLastOpenPath());
        if (!tem.exists()) {
            tem = new File(SystemUtil.getDefaultUserFile());
        }
        this.setCurrentDirectory(tem);//设置默认打开路径
        this.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);//只能选择目录
        this.setDialogType(JFileChooser.OPEN_DIALOG);//设置此对话框的类型
        this.setDialogTitle("选择映射文件夹");
        this.setSize(new Dimension(650, 500));
        this.setPreferredSize(new Dimension(650, 500));
        this.setMultiSelectionEnabled(true);//可以选择多个文件
        int index = this.showDialog(VpicFrame.getInstance(), "确定");
        if (index == JFileChooser.APPROVE_OPTION) {
            try {
                SystemCache.getInstance().getData().
                        setLastOpenPath(this.getCurrentDirectory().getCanonicalPath());
                SystemCache.getInstance().update();
                File[] files = this.getSelectedFiles();
                for (File sFile : files) {
                    ListServiceImpl.getInstance().addTreeData(sFile.getCanonicalPath());
                }
                ListServiceImpl.getInstance().reloadTreeData();
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        FileCacheHelper.asynIndex();
                    }
                });
            } catch (IOException e1) {
                log.error("选择文件夹失败", e1);
            }
        }
    }
}

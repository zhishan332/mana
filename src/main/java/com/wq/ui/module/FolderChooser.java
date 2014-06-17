package com.wq.ui.module;

import com.wq.cache.LocalFileCache;
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
            File[] files = this.getSelectedFiles();
            if (files == null || files.length == 0) return;
            SystemCache.getInstance().getData().
                    setLastOpenPath(this.getCurrentDirectory().getAbsolutePath());
            SystemCache.getInstance().update();
            for (File sFile : files) {
                ListServiceImpl.getInstance().addTreeData(sFile.getAbsolutePath());
            }
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    VpicFrame.getInstance().getRunInfo().setText("正在建立索引...");
                    LocalFileCache.index();
                    ListServiceImpl.getInstance().reloadTreeData();
                    VpicFrame.getInstance().getRunInfo().setText("已完成");
                }
            });
        }
    }
}

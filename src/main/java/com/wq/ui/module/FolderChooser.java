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
            VpicFrame.getInstance().getRunInfo().setText("添加新文件夹成功");
//            new IndexAndReloadWorker().execute();
        }
    }

    class IndexAndReloadWorker extends SwingWorker {

        @Override
        protected Object doInBackground(){
            log.info("添加文件夹映射成功，开始创建索引..");
            try {
                LocalFileCache.index();
            } catch (Throwable e) {
               log.warn("一点异常");
            }
            log.info("添加文件夹映射成功，开始刷新菜单数据..");
            return null;
        }

        @Override
        protected void done() {
            try {
                ListServiceImpl.getInstance().reloadTreeData();
                log.info("添加文件夹映射成功>>>>>>>>>>>>>>>>>>");
                VpicFrame.getInstance().getRunInfo().setText("索引已完成");
            } catch (Exception e) {
                log.error("更新Runinfo失败", e);
            }
        }
    }
}

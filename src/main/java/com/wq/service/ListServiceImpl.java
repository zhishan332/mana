package com.wq.service;

import com.wq.cache.AllCache;
import com.wq.cache.SystemCache;
import com.wq.context.SystemContext;
import com.wq.ui.*;
import com.wq.util.JTreeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.tree.TreePath;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 左侧列表服务
 *
 * @author wangqing
 * @since 1.0.0
 */
public class ListServiceImpl implements ListService {
    private static final Logger log = LoggerFactory.getLogger(ListServiceImpl.class);
    private static ListServiceImpl ourInstance = new ListServiceImpl();
    private SystemCache handler = SystemCache.getInstance();

    private ListServiceImpl() {

    }

    public static ListServiceImpl getInstance() {
        return ourInstance;
    }

    @Override
    public void addTreeData(String path) {
        handler.getData().getFileList().add(path);
        handler.update();
    }

    @Override
    public void delTreeData(String path) {
        handler.getData().getFileList().remove(path);
        handler.update();
    }

    @Override
    public void reloadTreeData() {
        try {
            AllCache.getInstance().reloadListCache();
        } catch (IOException e) {
            log.error("reloadListCache失败", e);
        }
        ListPanel.getInstance().loadTreeModel();
        ListPanel.getInstance().getTree().updateUI();
        JTreeUtil.expandTree(ListPanel.getInstance().getTree(), SystemCache.getInstance().getData().isExpland());  //展开所有子节点
        ViewScrollPanel.getInstance().getVerticalScrollBar().setValue(1);//滚动条设置为0
        loadPic(null, null, 0);
        Map<String, java.util.List<String>> map2 = AllCache.getInstance().getMenu();
        if (map2 != null) {
            for (Map.Entry<String, java.util.List<String>> entry : map2.entrySet()) {
                VpicFrame.getInstance().setInfo(entry.getKey());
                break;
            }
        }
        TreePath path = ListPanel.getInstance().getLastTreePath();
        if (path != null) {
            ListPanel.getInstance().getTree().setSelectionPath(path);
        } else {
            ListPanel.getInstance().getTree().setSelectionRow(0);//重新选中树
        }
        VpicFrame.getInstance().repaint();//测试释放内存
    }

    /**
     * 根据图片列表加载图片
     */
    public void loadPic(String folder, final List<String> list, int start) {
        ViewContentPanel viewContentPanel = new ViewContentPanel(folder, list, start);
        viewContentPanel.repaint();
        ViewScrollPanel.getInstance().clear();
        ViewScrollPanel.getInstance().constructPlate();
        ViewScrollPanel.getInstance().setViewContentPanel(viewContentPanel);
        ViewScrollPanel.getInstance().setViewportView(viewContentPanel);
//        ViewScrollPanel.getInstance().getHorizontalScrollBar().updateUI();
        ViewScrollPanel.getInstance().revalidate();
        ViewScrollPanel.getInstance().repaint();
        ViewScrollPanel.getInstance().doLayout();
        ViewScrollPanel.getInstance().getVerticalScrollBar().setValue(0);//滚动条设置为0
        SystemContext.getInstance().put("PageStart",start);
        GoPageDialog.getInstance().setVisible(false);
        VpicFrame.getInstance().refreshNum();
//        ViewScrollPanel.getInstance().updateUI();
        VpicFrame.getInstance().repaint();//测试释放内存
    }

    @Override
    public void saveAll() {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}

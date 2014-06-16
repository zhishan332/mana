package com.wq.service;

import com.wq.cache.AllCache;
import com.wq.cache.SystemCache;
import com.wq.ui.ListPanel;
import com.wq.ui.ViewContentPanel;
import com.wq.ui.ViewScrollPanel;
import com.wq.ui.VpicFrame;
import com.wq.util.JTreeUtil;
import com.wq.util.LightLog;

import javax.swing.tree.TreePath;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: wangq
 * Date: 12-8-2
 * Time: 下午3:45
 * To change this template use File | Settings | File Templates.
 */
public class ListServiceImpl implements ListService {
    private static ListServiceImpl ourInstance = new ListServiceImpl();
    private CacheService cacheService = CacheServiceImpl.getInstance();
    private static LightLog logger = LightLog.getInstance(ListServiceImpl.class);
    private SystemCache handler = SystemCache.getInstance();

    public static ListServiceImpl getInstance() {
        return ourInstance;
    }

    private ListServiceImpl() {

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
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        ListPanel.getInstance().loadTreeModel();
        ListPanel.getInstance().getTree().updateUI();
        JTreeUtil.expandTree(ListPanel.getInstance().getTree(), SystemCache.getInstance().getData().isExpland());  //展开所有子节点
        ViewScrollPanel.getInstance().getVerticalScrollBar().setValue(1);//滚动条设置为0
        Map<String, List<String>> map = AllCache.getInstance().getMenu();
        if (map != null && map.size() > 0) {
            for (Map.Entry<String, java.util.List<String>> entry : map.entrySet()) {
                List<String> list = entry.getValue();
                loadPic(entry.getKey(),list,0);
                break;
            }
        } else loadPic(null,null,0);
        Map<String, java.util.List<String>> map2 = AllCache.getInstance().getMenu();
        if (map2 != null) {
            for (Map.Entry<String, java.util.List<String>> entry : map2.entrySet()) {
                VpicFrame.getInstance().setInfo(entry.getKey());
                break;
            }
        }
//        loadPic(null);
//        ViewContentPanel.getInstance().clear();
//        ViewContentPanel.getInstance().constructPage();
//        ViewContentPanel.getInstance().updateUI();
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
    public void loadPic(String folder,final List<String> list,int start) {
//        ViewContentPanel.getInstance().clear();
//        ViewContentPanel.getInstance().loadPic(list);
        final ViewContentPanel viewContentPanel = new ViewContentPanel(folder,list,start);
//        HtmlPanel viewContentPanel=new HtmlPanel(HtmlUtil.getHtmlStrByList(list));
        viewContentPanel.repaint();
        ViewScrollPanel.getInstance().clear();
        ViewScrollPanel.getInstance().setViewContentPanel(viewContentPanel);
        ViewScrollPanel.getInstance().setViewportView(viewContentPanel);
//        ViewScrollPanel.getInstance().getHorizontalScrollBar().updateUI();
        ViewScrollPanel.getInstance().revalidate();
        ViewScrollPanel.getInstance().repaint();
        ViewScrollPanel.getInstance().doLayout();
        ViewScrollPanel.getInstance().getVerticalScrollBar().setValue(0);//滚动条设置为0
//        ViewScrollPanel.getInstance().updateUI();
        VpicFrame.getInstance().repaint();//测试释放内存
//        System.gc();
    }

    //    public void loadPic(final List<String> list) {
//        SwingUtilities.invokeLater(new Runnable() {
//            public void run() {
//                ViewContentPanel.getInstance().removeAll();
//                ViewContentPanel.getInstance().loadPic(list);
//                ViewContentPanel.getInstance().revalidate();
//                ViewContentPanel.getInstance().repaint();
//                ViewContentPanel.getInstance().updateUI();
//                ViewScrollPanel.getInstance().getVerticalScrollBar().setValue(0);//滚动条设置为0
//                ViewScrollPanel.getInstance().updateUI();
//                VpicFrame.getInstance().repaint();//测试释放内存
//            }
//        });
//    }
    @Override
    public void saveAll() {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}

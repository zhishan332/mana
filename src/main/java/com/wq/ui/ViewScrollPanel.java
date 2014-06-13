package com.wq.ui;

import com.wq.cache.AllCache;
import com.wq.model.SysData;
import com.wq.service.SysDataHandler;

import javax.swing.*;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: wangq
 * Date: 12-8-2
 * Time: 上午11:25
 * 滚动容器
 */
public class ViewScrollPanel extends JScrollPane implements Page {
    private static ViewScrollPanel viewPanel;
    private ViewContentPanel viewContentPanel;
    private SysData data = SysDataHandler.getInstance().getData();

    public static ViewScrollPanel getInstance() {
        if (viewPanel == null) {
            viewPanel = new ViewScrollPanel();
        }
        return viewPanel;
    }

    private ViewScrollPanel() {
        constructPlate();
        constructPage();
    }

    public void constructPlate() {
        if (data.isHMode()){
            this.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);//垂直滚动条适时出现
            this.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);//水平滚动条适时出现
            this.getHorizontalScrollBar().setUnitIncrement(SysDataHandler.getInstance().getData().getSpeed());//滚动速度
        }else{
            this.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);//垂直滚动条适时出现
            this.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);//水平滚动条适时出现
            this.getVerticalScrollBar().setUnitIncrement(SysDataHandler.getInstance().getData().getSpeed());//滚动速度
            this.getHorizontalScrollBar().setUnitIncrement(SysDataHandler.getInstance().getData().getSpeed());//滚动速度
        }
    }

    /**
     * 动态加载图片
     */
    @Override
    public void constructPage() {
//        final JScrollBar jScrollBar = this.getVerticalScrollBar();
//        jScrollBar.addAdjustmentListener(new AdjustmentListener() {
//            private int height = 0;
//
//            @Override
//            public void adjustmentValueChanged(AdjustmentEvent e) {
//                int cur = jScrollBar.getValue();
//                int max = jScrollBar.getMaximum();
//                if ((cur - height > 3500) && (max - cur) < 1800) {
//                    System.out.println("开始加载新图片...........................");
//                    ViewContentPanel.getInstance().loadNextPic();
//                    ViewPanel.getInstance().updateUI();
//                    height = cur;
//                }
//            }
//        });
//        this.setViewportView(ViewContentPanel.getInstance());
        Map<String, List<String>> map = AllCache.getInstance().getMenu();
        if (map == null || map.size() == 0) {
//            ViewContentPanel.getInstance().setList(null);
            this.setViewportView(new ViewContentPanel(null));
//            this.setViewportView(ViewContentPanel.getInstance());
        } else {
            for (Map.Entry<String, java.util.List<String>> entry : map.entrySet()) {
                List<String> list = entry.getValue();
                this.viewContentPanel = new ViewContentPanel(list);
//                ViewContentPanel viewContentPanel2 = ViewContentPanel.getInstance();
//                viewContentPanel2.setList(list);
                this.setViewportView(viewContentPanel);
//                this.viewContentPanel=new HtmlPanel(HtmlUtil.getHtmlStrByList(list));
//                this.setViewportView(viewContentPanel);
                break;
            }
        }
    }

    public ViewContentPanel getViewContentPanel() {
        return viewContentPanel;
    }

    public void setViewContentPanel(ViewContentPanel viewContentPanel) {
        this.viewContentPanel = viewContentPanel;
    }

    public void clear() {
        if (viewContentPanel != null) {
            viewContentPanel.removeAll();
//            viewContentPanel.setVisible(false);
//            viewContentPanel.repaint();
//            Component[] common = viewContentPanel.getComponents();
//            for (Component com : common) {
//                if (com instanceof JLabel) {
//                    JLabel label = (JLabel) com;
//                    label.removeAll();
//                    label.revalidate();
//                    label.repaint();
//                }
//            }
        }
    }
}
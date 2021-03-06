package com.wq.ui;

import com.sun.awt.AWTUtilities;
import com.wq.cache.AllCache;
import com.wq.cache.LocalFileCache;
import com.wq.cache.SystemCache;
import com.wq.constans.Constan;
import com.wq.context.SystemContext;
import com.wq.model.DirMenu;
import com.wq.service.CacheService;
import com.wq.service.CacheServiceImpl;
import com.wq.service.ListService;
import com.wq.service.ListServiceImpl;
import com.wq.ui.module.FolderChooser;
import com.wq.ui.module.MesBox;
import com.wq.ui.module.VipcTreeCellRenderer;
import com.wq.util.FontUtil;
import com.wq.util.JTreeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * To change this template use File | Settings | File Templates.
 *
 * @author wangqing
 * @since 1.0.0
 */
public class ListPanel extends JPanel implements Page {
    private static final Logger log = LoggerFactory.getLogger(ListPanel.class);
    private static ListPanel listPanel;
    private final MemoryPanel memoryPanel = new MemoryPanel();
    private JScrollPane listPanelContainer;
    private DefaultMutableTreeNode root, child, chosen;
    private JTree tree;
    private DefaultTreeModel model;
    private ListService listService = ListServiceImpl.getInstance();
    private CacheService cacheService = CacheServiceImpl.getInstance();
    private int flag = 0;//标志位，防止树被选中后自动加载图片
    private TreePath lastTreePath;
    private String lastFilePath;

    private ListPanel() {
        constructPlate();
        constructPage();
    }

    public static ListPanel getInstance() {
        if (listPanel == null) {
            listPanel = new ListPanel();
        }
        return listPanel;
    }

    public void constructPlate() {
        this.setLayout(new BorderLayout());
        this.setPreferredSize(new Dimension(280, 1000));
        this.setSize(new Dimension(280, 1000));
        this.setMinimumSize(new Dimension(280, 1000));
        Timer timer = new Timer(2000, new ActionListener() {
            public void actionPerformed(ActionEvent actionevent) {
                memoryPanel.repaint();
            }
        });
        timer.start();
    }

    public void constructPage() {
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        toolBar.setBorder(BorderFactory.createEtchedBorder());
//        toolBar.setBorderPainted(false); //不画边界
        JButton addBtn = new JButton("", new ImageIcon(Constan.RESPAHT + "res/img/new.png"));
        addBtn.setToolTipText("添加文件夹");
        addBtn.setFont(FontUtil.getDefault());
        addBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new FolderChooser();
            }
        });

        JButton setBtn = new JButton("", new ImageIcon(Constan.RESPAHT + "res/img/set.png"));
        setBtn.setToolTipText("设置");
        setBtn.setFont(FontUtil.getDefault());
        setBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                com.wq.model.SysData data = SystemCache.getInstance().getData();
                SetPanel setPanel = SetPanel.getInstance();
                setPanel.getSpeed().setText(String.valueOf(data.getSpeed()));
                setPanel.getSavePath().setText(data.getSavePath());
                setPanel.getExp().setSelected(data.isExpland());
                setPanel.getIgnoreEmptyFolder().setSelected(data.isIgnoreEmptyFolder());
                setPanel.getAutoFit().setSelected(data.isAutoFit());
                setPanel.getJf().setText(data.getPassword());
                setPanel.getUserProxyBox().setSelected(data.isUseProxy());
                if (!setPanel.getUserProxyBox().isSelected()) {
                    setPanel.getProxyHostField().setEnabled(false);
                    setPanel.getProxyPortField().setEnabled(false);
                    setPanel.getProxyUserNameField().setEnabled(false);
                    setPanel.getProxyPasswordField().setEnabled(false);
                } else {
                    setPanel.getProxyHostField().setEnabled(true);
                    setPanel.getProxyPortField().setEnabled(true);
                    setPanel.getProxyUserNameField().setEnabled(true);
                    setPanel.getProxyPasswordField().setEnabled(true);
                }
                setPanel.getProxyHostField().setText(data.getProxyHost());
                setPanel.getProxyPortField().setText(data.getProxyPort() == 0 ? null : String.valueOf(data.getProxyPort()));
                setPanel.getProxyUserNameField().setText(data.getProxyUserName());
                setPanel.getProxyPasswordField().setText(data.getProxyUserPassword());
                setPanel.getHModeBox().setSelected(data.isHMode());
                Set<String> listType = data.getTypeList();//获取支持类型
                if (listType.size() > 3) {
                    if (listType.contains("jpg")) {
                        setPanel.getJpg().setSelected(true);
                    } else {
                        setPanel.getJpg().setSelected(false);
                    }
                    if (listType.contains("gif")) {
                        setPanel.getPng().setSelected(true);
                    } else {
                        setPanel.getPng().setSelected(false);
                    }
                    if (listType.contains("bmp")) {
                        setPanel.getWin().setSelected(true);
                    } else {
                        setPanel.getWin().setSelected(false);
                    }
                    if (listType.contains("png")) {
                        setPanel.getGif().setSelected(true);
                    } else {
                        setPanel.getGif().setSelected(false);
                    }
                }
                setPanel.setVisible(true);
            }
        });
//        JButton ctBtn = new JButton(new ImageIcon(Constan.RESPAHT + "res/img/catch.png"));
//        ctBtn.setToolTipText("网页取图");
//        ctBtn.addActionListener(new java.awt.event.ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                FetchImgPanel.getInstance().getWidthField().setText(String.valueOf(SystemCache.getInstance().getData().getFetchMinWidth()));
//                FetchImgPanel.getInstance().getHeigthField().setText(String.valueOf(SystemCache.getInstance().getData().getFetchMinHeight()));
//                FetchImgPanel.getInstance().getDepField().setText(String.valueOf(SystemCache.getInstance().getData().getFetchDeep()));
//                FetchImgPanel.getInstance().getAddBox().setSelected(SystemCache.getInstance().getData().isFetchToMana());
//                FetchImgPanel.getInstance().setVisible(true);
//            }
//        });
        JButton reBtn = new JButton("", new ImageIcon(Constan.RESPAHT + "res/img/refresh.png"));
        reBtn.setToolTipText("刷新文件夹");
        reBtn.setFont(FontUtil.getDefault());
        reBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                listService.reloadTreeData();
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) ListPanel.getInstance().getTree().getLastSelectedPathComponent(); //返回最后选中的结点
                if (node != null) {
                    lastTreePath = new TreePath(node);
                }
            }
        });
        JButton clBtn = new JButton("", new ImageIcon(Constan.RESPAHT + "res/img/broom.png"));
        clBtn.setToolTipText("清理缓存文件夹，删除过期缓存");
        clBtn.setFont(FontUtil.getDefault());
        clBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        LocalFileCache.deleteDirty();
                        MesBox.success("清理完成", VpicFrame.getInstance());
                    }
                });
            }
        });
        JButton fireBtn = new JButton("", new ImageIcon(Constan.RESPAHT + "res/img/fire.png"));
        fireBtn.setToolTipText("文件夹中显示");
        fireBtn.setFont(FontUtil.getDefault());
        fireBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (lastFilePath == null) return;
                try {
                    Desktop.getDesktop().open(new File(lastFilePath));
                } catch (IOException exp) {
                    log.error("进入文件目录失败", exp);
                }
            }
        });
        JButton toolBtn = new JButton("", new ImageIcon(Constan.RESPAHT + "res/img/tool.png"));
        toolBtn.setToolTipText("工具");
        toolBtn.setFont(FontUtil.getDefault());
        toolBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ToolPanel toolPanel = ToolPanel.getInstance();
                toolPanel.setVisible(true);
            }
        });
        JButton aboutBtn = new JButton("", new ImageIcon(Constan.RESPAHT + "res/img/help.png"));
        aboutBtn.setFont(FontUtil.getDefault());
        aboutBtn.setToolTipText("关于");
        aboutBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        AboutDialog about = AboutDialog.getInstance();
                        about.setVisible(true);
                        AWTUtilities.setWindowOpacity(about, 0.9f);
                    }
                });
            }
        });
        JButton hideBtn = new JButton(new ImageIcon(Constan.RESPAHT + "res/img/hide.png"));
        hideBtn.setToolTipText("快速隐藏");
        hideBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                hidePanel();
                ViewPanelMenu.getInstance().getHideItem().setEnabled(false);
                ViewPanelMenu.getInstance().getShowItem().setEnabled(true);
                PicMenu.getInstance().getHideItem().setEnabled(false);
                PicMenu.getInstance().getShowItem().setEnabled(true);
            }
        });
//        toolBar.putClientProperty(Options.HEADER_STYLE_KEY, HeaderStyle.BOTH);
        toolBar.add(addBtn);
        toolBar.add(setBtn);
        toolBar.add(reBtn);
        toolBar.add(toolBtn);
        toolBar.add(clBtn);
        toolBar.add(aboutBtn);
        toolBar.add(Box.createHorizontalGlue());
        toolBar.add(fireBtn);
        toolBar.add(hideBtn);
        this.add("North", toolBar);
        root = new DefaultMutableTreeNode("全部");
        //根节点进行初始化
        tree = new JTree(root);
        tree.setFont(FontUtil.getDefault());
        tree.setRootVisible(false);//根节点不可见
//        tree.setUI(new BasicTreeUI());
        //树进行初始化，其数据来源是root对象
        listPanelContainer = new JScrollPane(tree);
        //把滚动面板添加到Trees中
        loadTreeModel();//加载树形数据
        JTreeUtil.expandTree(tree, SystemCache.getInstance().getData().isExpland());  //展开所有子节点
        tree.validate();
        tree.addTreeSelectionListener(new TreeSelectionListener() {
            public void valueChanged(TreeSelectionEvent evt) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent(); //返回最后选中的结点
                final DirMenu dirMenu;
                if (node != null && node.getUserObject() instanceof DirMenu) {
                    dirMenu = (DirMenu) node.getUserObject();//获得这个结点的名称
                } else {
                    return;
                }
                if (!"所有分类".equals(dirMenu.getName()) && flag == 0) {//根节点无反应
                    Map<String, List<String>> map = AllCache.getInstance().getMenu();
                    final List<String> list = map.get(dirMenu.getFilePath());
                    //更新下方条数
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            VpicFrame.getInstance().setInfo(dirMenu.getFilePath());
                            SystemContext.getInstance().put("ImageTotal", list == null ? 0 : list.size());
                            VpicFrame.getInstance().refreshNum();  //更新下方条数
                        }
                    });
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            lastFilePath = dirMenu.getFilePath();
                            listService.loadPic(dirMenu.getFilePath(), list, 0);
                        }
                    });
                }
            }
        });
        tree.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                log.info("进入树结构鼠标事件");
                TreePath path = tree.getPathForLocation(20, e.getY());
                if (path == null) {
                    log.debug("path ==null 返回");
                    return;
                }
                log.debug("setSelectionPath:" + path);
                tree.setSelectionPath(path);
                if ((e.getModifiers() & MouseEvent.BUTTON3_MASK) != 0
                        && !e.isControlDown() && !e.isShiftDown()) {
                    DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent(); //返回最后选中的结点
                    final DirMenu dirMenu;
                    if (node != null && node.getUserObject() instanceof DirMenu) {
                        dirMenu = (DirMenu) node.getUserObject();//获得这个结点的名称
                    } else {
                        log.info("节点为空或者数据类型不符合不弹出右键菜单");
                        return;
                    }
                    if (!"所有分类".equals(dirMenu.getName()) && flag == 0) {//根节点无反应
                        lastFilePath = dirMenu.getFilePath();
                        TreeMenu treeMenu = new TreeMenu(lastFilePath);
                        treeMenu.show(tree, e.getX(), e.getY());
                    } else {
                        log.info("点击根节点不弹出右键菜单");
                    }
                } else {
                    log.debug("非右键事件");
                }
            }
        });
        flag++;
        tree.setSelectionRow(0); //选中第一行
        flag--;
        tree.setCellRenderer(new VipcTreeCellRenderer()); //设置图标样式
        this.add("Center", listPanelContainer);
    }

    public void hidePanel() {
        VpicFrame.getInstance().getjSplitPane().setDividerLocation(0.0);
//        VpicFrame.getInstance().getjSplitPane().addImpl();
//        this.setVisible(false);
    }

    public void showPanel() {
        VpicFrame.getInstance().getjSplitPane().setDividerLocation(0.206);
    }

    public JTree getTree() {
        return tree;
    }

    public DefaultTreeModel getModel() {
        return model;
    }

    public void loadTreeModel() {
        if (model == null) {    //首次页面加载时
            model = (DefaultTreeModel) tree.getModel();
            try {
                //把child添加到chosen
                List<DefaultMutableTreeNode> list = cacheService.getTreeData();
                for (int n = 0; n < list.size(); n++) {
                    //生成子节点
                    chosen = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();  //选择child的父节点
                    if (chosen == null) chosen = root;
                    model.insertNodeInto(list.get(n), chosen, n);
                }
            } catch (IOException e) {
                log.error("getTreeData异常", e);
            }
        } else {  //刷新时
            root.removeAllChildren();
            DefaultTreeModel models = new DefaultTreeModel(root);
            tree.setModel(models);    //关联TreeModel
            try {
                //把child添加到chosen
                List<DefaultMutableTreeNode> list = cacheService.getTreeData();
                for (int n = 0; n < list.size(); n++) {
                    //生成子节点
                    chosen = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();  //选择child的父节点
                    if (chosen == null) chosen = root;
                    models.insertNodeInto(list.get(n), chosen, n);
                }
            } catch (IOException e) {
                log.error("getTreeData异常", e);
            }
        }
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public TreePath getLastTreePath() {
        return lastTreePath;
    }

    //    /**
//     * 设置背景色
//     *
//     * @param g
//     */
//    public void paintComponent(Graphics g) {
//
//    }
}

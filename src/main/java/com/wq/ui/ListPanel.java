package com.wq.ui;

import com.sun.awt.AWTUtilities;
import com.wq.cache.AllCache;
import com.wq.constans.Constan;
import com.wq.model.DirMenu;
import com.wq.model.SysData;
import com.wq.service.*;
import com.wq.util.*;

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
 * Created with IntelliJ IDEA.
 * User: wangq
 * Date: 12-8-2
 * Time: 上午11:28
 * To change this template use File | Settings | File Templates.
 */
public class ListPanel extends JPanel implements Page {
    private JScrollPane listPanelContainer;
    private DefaultMutableTreeNode root, child, chosen;
    private JTree tree;
    private DefaultTreeModel model;
    private static ListPanel listPanel;
    private ListService listService = ListServiceImpl.getInstance();
    private CacheService cacheService = CacheServiceImpl.getInstance();
    //    private static LightLog logger = LightLog.getInstance(ListPanel.class);
    private int flag = 0;//标志位，防止树被选中后自动加载图片
    private final MemoryPanel memoryPanel = new MemoryPanel();
    private SysDataHandler dataHandler = SysDataHandler.getInstance();
    private TreePath lastTreePath;

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
        this.setPreferredSize(new Dimension(200, 1000));
        this.setSize(new Dimension(200, 1000));
        this.setMinimumSize(new Dimension(200, 1000));
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
        toolBar.setBorderPainted(false); //不画边界
        JButton addBtn = new JButton(new ImageIcon(Constan.RESPAHT + "img/add.png"));
        addBtn.setToolTipText("增加文件夹映射");
        addBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                chooseFile();
            }
        });
        JButton delBtn = new JButton(new ImageIcon(Constan.RESPAHT + "img/fuckdel.png"));
        delBtn.setToolTipText("删除文件夹映射");
        delBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (tree.getLastSelectedPathComponent() == null) {
                    MesBox.warn("请选择要删除的映射文件夹");
                    return;
                }
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
                if (node.getLevel() != 1) {
                    MesBox.warn("您只能删除映射文件夹");
                } else {
                    DirMenu dirMenu = (DirMenu) node.getUserObject();//获得这个结点的名称
                    String path = dirMenu.getFilePath();
                    listService.delTreeData(path);
                    listService.reloadTreeData();
                }
            }
        });
        JButton fireBtn = new JButton(new ImageIcon(Constan.RESPAHT + "img/fire.png"));
        fireBtn.setToolTipText("命中");
        fireBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
                DirMenu dirMenu = (DirMenu) node.getUserObject();//获得这个结点的名称
                String path = dirMenu.getFilePath();
                try {
                    java.awt.Desktop.getDesktop().open(new File(path));
                } catch (IOException exp) {
                    // TODO Auto-generated catch block
                    exp.printStackTrace();
                }
            }
        });
        JButton setBtn = new JButton(new ImageIcon(Constan.RESPAHT + "img/set.png"));
        setBtn.setToolTipText("设置");
        setBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SysData data = SysDataHandler.getInstance().getData();
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
        JButton ctBtn = new JButton(new ImageIcon(Constan.RESPAHT + "img/catch.png"));
        ctBtn.setToolTipText("网页取图");
        ctBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                FetchImgPanel.getInstance().getWidthField().setText(String.valueOf(SysDataHandler.getInstance().getData().getFetchMinWidth()));
                FetchImgPanel.getInstance().getHeigthField().setText(String.valueOf(SysDataHandler.getInstance().getData().getFetchMinHeight()));
                FetchImgPanel.getInstance().getDepField().setText(String.valueOf(SysDataHandler.getInstance().getData().getFetchDeep()));
                FetchImgPanel.getInstance().getAddBox().setSelected(SysDataHandler.getInstance().getData().isFetchToMana());
                FetchImgPanel.getInstance().setVisible(true);
            }
        });
        JButton reBtn = new JButton(new ImageIcon(Constan.RESPAHT + "img/refresh.gif"));
        reBtn.setToolTipText("刷新文件夹");
        reBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) ListPanel.getInstance().getTree().getLastSelectedPathComponent(); //返回最后选中的结点
                if (node != null) {
                    TreePath path = new TreePath(node);
                    lastTreePath = path;
                }
                listService.reloadTreeData();
            }
        });
        JButton aboutBtn = new JButton(new ImageIcon(Constan.RESPAHT + "img/help.png"));
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
        JButton hideBtn = new JButton(new ImageIcon(Constan.RESPAHT + "img/left.png"));
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
        toolBar.add(delBtn);
        toolBar.add(fireBtn);
        toolBar.add(ctBtn);
        toolBar.add(setBtn);
        toolBar.add(reBtn);
        toolBar.add(aboutBtn);
        toolBar.add(Box.createHorizontalGlue());
        toolBar.add(hideBtn);
        this.add("North", toolBar);
        root = new DefaultMutableTreeNode("全部");
        //根节点进行初始化
        tree = new JTree(root);
        tree.setFont(FontUtil.getArial12());
        tree.setRootVisible(false);//根节点不可见
//        tree.setUI(new BasicTreeUI());
        //树进行初始化，其数据来源是root对象
        listPanelContainer = new JScrollPane(tree);
        //把滚动面板添加到Trees中
        loadTreeModel();//加载树形数据
        JTreeUtil.expandTree(tree, SysDataHandler.getInstance().getData().isExpland());  //展开所有子节点
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
                            if (list == null) {
                                VpicFrame.getInstance().setNum(0);
                            } else VpicFrame.getInstance().setNum(list.size());  //更新下方条数
                        }
                    });
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            listService.loadPic(list);
                        }
                    });
                }
            }
        });
        tree.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                TreePath path = tree.getPathForLocation(e.getX(), e.getY());
                if (path == null) {
                    return;
                }
                tree.setSelectionPath(path);
                if ((e.getModifiers() & MouseEvent.BUTTON3_MASK) != 0
                        && !e.isControlDown() && !e.isShiftDown()) {
                    DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent(); //返回最后选中的结点
                    final DirMenu dirMenu;
                    if (node != null && node.getUserObject() instanceof DirMenu) {
                        dirMenu = (DirMenu) node.getUserObject();//获得这个结点的名称
                    } else {
                        return;
                    }
                    if (!"所有分类".equals(dirMenu.getName()) && flag == 0) {//根节点无反应
                        TreeMenu.getInstance().setFilePath(dirMenu.getFilePath());
                        TreeMenu.getInstance().setTreePath(path);
                        TreeMenu.getInstance().show(tree, e.getX(), e.getY());
                    }
                }
            }
        });
        flag++;
        tree.setSelectionRow(0); //选中第一行
        flag--;
        tree.setCellRenderer(new VipcTreeCellRenderer()); //设置图标样式
        this.add("Center", listPanelContainer);
    }

    public void chooseFile() {
        JFileChooser jChooser = new JFileChooser();
        SwingUtils.updateFont(jChooser, FontUtil.getArial12());
        File tem = new File(dataHandler.getData().getLastOpenPath());
        if (!tem.exists()) {
            tem = new File(SystemUtil.getDefaultUserFile());
        }
        jChooser.setCurrentDirectory(tem);//设置默认打开路径
        jChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);//只能选择目录
        jChooser.setDialogType(JFileChooser.OPEN_DIALOG);//设置此对话框的类型
        jChooser.setDialogTitle("选择映射文件夹");
        jChooser.setSize(new Dimension(650, 500));
        jChooser.setPreferredSize(new Dimension(650, 500));
        jChooser.setMultiSelectionEnabled(true);//可以选择多个文件
        int index = jChooser.showDialog(VpicFrame.getInstance(), "确定");
        if (index == JFileChooser.APPROVE_OPTION) {
            try {
                dataHandler.getData().setLastOpenPath(jChooser.getCurrentDirectory().getCanonicalPath());
                dataHandler.update();
                File[] files = jChooser.getSelectedFiles();
                for (File sFile : files) {
                    listService.addTreeData(sFile.getCanonicalPath());
                }
                listService.reloadTreeData();
            } catch (IOException e1) {
                e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
    }

    public void hidePanel() {
        VpicFrame.getInstance().getjSplitPane().setDividerLocation(0.0);
//        VpicFrame.getInstance().getjSplitPane().addImpl();
//        this.setVisible(false);
    }

    public void showPanel() {
        VpicFrame.getInstance().getjSplitPane().setDividerLocation(0.18);
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
                e.printStackTrace();
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
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
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

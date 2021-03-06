package com.wq.ui;

import com.sun.awt.AWTUtilities;
import com.wq.cache.AllCache;
import com.wq.constans.Constan;
import com.wq.context.SystemContext;
import com.wq.service.CacheService;
import com.wq.service.CacheServiceImpl;
import com.wq.ui.module.LinkLabel;
import com.wq.util.FileUtil;
import com.wq.util.FontUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Map;

/**
 * 最底层的面板，软件启动时初始化该JFrame
 *
 * @author wangqing
 * @since 1.0.0
 */
public class VpicFrame extends JFrame {
    private static final Logger log = LoggerFactory.getLogger(VpicFrame.class);
    private static VpicFrame vpic;
    private CacheService cacheService = CacheServiceImpl.getInstance();
    private JLabel numField;
    private JLabel infoLabel;
    private JSplitPane jSplitPane;
    private JLabel runInfo;

    private VpicFrame() {
        super("Mana看图-1.0.0");
        createGUI();
    }

    public static VpicFrame getInstance() {
        if (vpic == null) {
            vpic = new VpicFrame();
        }
        return vpic;
    }

    public void createGUI() {
        constructStyle();
        constructPlate();
        constructPage();
    }

    /**
     * 构造全局
     */
    public void constructStyle() {
        // 注册全局快捷键
//        ShortcutManager.getInstance().addShortcutListener(new ShortcutManager.ShortcutListener() {
//            public void handle() {
//                //TODO System.out.println("Meta + I");
//            }
//        }, KeyEvent.VK_CONTROL, KeyEvent.VK_S);
    }

    /**
     * 构造页面底版
     */
    public void constructPlate() {
        BorderLayout layout = new BorderLayout();
        this.setLayout(layout);
        Toolkit tk = Toolkit.getDefaultToolkit();
        Image mImage = tk.createImage(Constan.RESPAHT + "res/img/logo.png");
        setIconImage(mImage);
        this.setResizable(true); //可以调整大小
        this.setExtendedState(this.getExtendedState() | MAXIMIZED_BOTH);//全屏打开
        Dimension local_size = new Dimension(700, 600);
        Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((screensize.width - local_size.width) / 2, (screensize.height - local_size.height) / 2);//设置窗体居中显示
        this.setMinimumSize(local_size); //设置最小大小
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                FileUtil.pool.shutdownNow();
                cacheService.getPool().shutdownNow();
            }
        });
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//设置关闭操作
    }

    /**
     * 关闭主窗口处理
     */
//    @Override
//    public void windowClosing(WindowEvent e) {
//        ViewContentPanel.getInstance().getExec().shutdown(); //关闭线程池
//        this.dispose();
//        System.exit(0);
//        int option = JOptionPane.showConfirmDialog(this, "确定退出系统?", "提示",
//                JOptionPane.YES_NO_OPTION);
//        if (option == JOptionPane.YES_OPTION) {
//            if (e.getWindow() == this) {
//                ViewContentPanel.getInstance().getExec().shutdown(); //关闭线程池
//                this.dispose();
//                System.exit(0);
//            } else {
//                return;
//            }
//        } else if (option == JOptionPane.NO_OPTION) {
//            if (e.getWindow() == this) {
//                return;
//            }
//        }
//    }
    public void constructPage() {
        JToolBar jToolBar = new JToolBar();
        jToolBar.setFloatable(false);
//        jToolBar.setBorderPainted(false); //不画边界
        jToolBar.setPreferredSize(new Dimension(500, 20));
//        jToolBar.setMargin(new Insets(1, 20, 1, 10));
        jToolBar.setBorder(BorderFactory.createEmptyBorder());
//        jToolBar.setBackground(Color.BLACK);
        infoLabel = new JLabel("");
        infoLabel.setIcon(new ImageIcon(Constan.RESPAHT + "res/img/disk.png"));
        infoLabel.setPreferredSize(new Dimension(500, 20));
        infoLabel.setMaximumSize(new Dimension(500, 20));
        infoLabel.setFont(FontUtil.getDefault());
        numField = new JLabel("0 P");
        numField.setIcon(new ImageIcon(Constan.RESPAHT + "res/img/picture.png"));
        numField.setFont(FontUtil.getSong12());
        numField.setPreferredSize(new Dimension(180, 20));
        numField.setMaximumSize(new Dimension(180, 20));
        numField.setFont(FontUtil.getDefault());
        numField.setToolTipText("跳转");
        numField.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(final MouseEvent e) {
                GoPageDialog goPageDialog = GoPageDialog.getInstance();
                goPageDialog.make();
                goPageDialog.setLocation(e.getXOnScreen(), (e.getYOnScreen() - goPageDialog.getHeight() - 12));
                goPageDialog.setVisible(true);
                AWTUtilities.setWindowOpacity(goPageDialog, 0.85f);
            }
        });
        loadBotoom();
        final MemoryPanel memoryPanel = new MemoryPanel();
        memoryPanel.setPreferredSize(new Dimension(100, 20));
        memoryPanel.setMaximumSize(new Dimension(100, 20));
        memoryPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        memoryPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                System.gc();
                memoryPanel.repaint();
            }
        });
//        JButton rubBtn = ButtonUtil.createJButton(Constan.RESPAHT + "res/img/rub3.png", null, null);
//        rubBtn.setToolTipText("清理内存");
//        rubBtn.setPressedIcon(new ImageIcon(Constan.RESPAHT + "res/img/rub-b.png"));
//        rubBtn.addActionListener(new java.awt.event.ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                System.gc();
//                memoryPanel.repaint();
//            }
//        });
        JLabel git = new LinkLabel("GitHub", "https://github.com/zhishan332/mana");
        git.setIcon(new ImageIcon(Constan.RESPAHT + "res/img/github.png"));
        git.setFont(FontUtil.getDefault());
        git.setPreferredSize(new Dimension(70, 20));
        git.setMaximumSize(new Dimension(70, 20));
        JLabel weibo = new LinkLabel("微博留言", "http://weibo.com/aziqing");
        weibo.setIcon(new ImageIcon(Constan.RESPAHT + "res/img/weibo.png"));
        weibo.setFont(FontUtil.getDefault());
        weibo.setPreferredSize(new Dimension(80, 20));
        weibo.setMaximumSize(new Dimension(80, 20));
        JLabel csdn = new LinkLabel("泊川", "http://blog.csdn.net/wantken");
        csdn.setIcon(new ImageIcon(Constan.RESPAHT + "res/img/csdn.jpg"));
        csdn.setFont(FontUtil.getDefault());
        csdn.setFont(FontUtil.getDefault());
        csdn.setPreferredSize(new Dimension(70, 20));
        csdn.setMaximumSize(new Dimension(70, 20));
        runInfo = new JLabel("加载完成");
        runInfo.setIcon(new ImageIcon(Constan.RESPAHT + "res/img/computer.png"));
        runInfo.setFont(FontUtil.getDefault());
        runInfo.setPreferredSize(new Dimension(200, 20));
        runInfo.setMaximumSize(new Dimension(200, 20));
        jToolBar.add(infoLabel);
        jToolBar.addSeparator();
        jToolBar.add(numField);
        jToolBar.addSeparator();
        jToolBar.add(git);
        jToolBar.add(weibo);
        jToolBar.add(csdn);
//        jToolBar.add(Box.createHorizontalGlue());
        jToolBar.addSeparator();
        jToolBar.add(runInfo);
        jToolBar.add(Box.createHorizontalGlue());
        jToolBar.add(memoryPanel);
//        jToolBar.add(rubBtn);
//        jToolBar.addSeparator();
        jSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true);
        jSplitPane.setBorder(null);
        jSplitPane.setDividerSize(8);
        jSplitPane.setOneTouchExpandable(true);
//        jSplitPane.addImpl();
        jSplitPane.add(ListPanel.getInstance(), JSplitPane.LEFT);
        jSplitPane.add(ViewScrollPanel.getInstance(), JSplitPane.RIGHT);
        this.add(BorderLayout.CENTER, jSplitPane);
        this.add(BorderLayout.SOUTH, jToolBar);
//        this.add(BorderLayout.CENTER, ViewScrollPanel.getInstance());
//        this.add(BorderLayout.WEST, ListPanel.getInstance());
    }

    private void loadBotoom() {
        Map<String, java.util.List<String>> map = AllCache.getInstance().getMenu();
        if (map != null) {
            for (Map.Entry<String, java.util.List<String>> entry : map.entrySet()) {
                setInfo(entry.getKey());
                SystemContext.getInstance().put("ImageTotal", entry.getValue().size());
                refreshNum();
                break;
            }
        }
    }

    public void setInfo(String info) {
        infoLabel.setText(info);
        infoLabel.setToolTipText(info);
    }

    public void refreshNum() {
        int total = SystemContext.getInstance().getIntVal("ImageTotal");
        int start = SystemContext.getInstance().getIntVal("PageStart");
        start = (int) Math.ceil((double)start / Constan.PAGE_SHOW_NUM);
        numField.setText("共" + total + "P" + "   当前第 " + (start + 1) + " 页 GO!");
    }

    public JSplitPane getjSplitPane() {
        return jSplitPane;
    }

    public JLabel getRunInfo() {
        return runInfo;
    }
}

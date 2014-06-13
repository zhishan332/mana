package com.wq.ui;

import com.wq.constans.Constan;
import com.wq.model.SysData;
import com.wq.service.FetchService;
import com.wq.service.FetchServiceImpl;
import com.wq.service.ListServiceImpl;
import com.wq.service.SysDataHandler;
import com.wq.util.FontUtil;
import com.wq.util.MesBox;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Created with IntelliJ IDEA.
 * User: wangq
 * Date: 13-5-21
 * Time: 下午10:49
 * To change this template use File | Settings | File Templates.
 */
public class FetchImgPanel extends JFrame implements Page {
    private static FetchImgPanel fetchImgPanel;
    private Container container;
    private JLabel urlLable;
    private JTextField urlField;
    private JLabel imgLenLable;
    private JTextField widthField;
    private JTextField heigthField;
    private JLabel depLabel;
    private JTextField depField;
    private JLabel addLabel;
    private JCheckBox addBox;
    private JButton fetchBtn;
    private JButton cancelBtn;
    private JLabel infoLabel;
    private Fetcher fetcher;
    private SysData sysData = SysDataHandler.getInstance().getData();

    private FetchImgPanel() {
        constructPlate();
        constructPage();
    }

    public static FetchImgPanel getInstance() {
        if (fetchImgPanel == null) {
            fetchImgPanel = new FetchImgPanel();
        }
        return fetchImgPanel;
    }

    @Override
    public void constructPlate() {
        this.setTitle("网页集图");
        this.setSize(new Dimension(550, 260));
        this.setLocationRelativeTo(null);
        this.setLayout(null);
        this.setResizable(false);
        Toolkit tk = Toolkit.getDefaultToolkit();
        Image mImage = tk.createImage(Constan.RESPAHT + "res/img/logo.png");
        setIconImage(mImage);
        container = this.getContentPane();
        this.addWindowFocusListener(new WindowAdapter() {  //这个事件让窗口一直在最前面
            public void windowLostFocus(WindowEvent e) {
                e.getWindow().toFront();
            }
        });
        this.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });
        this.setVisible(true);
    }

    @Override
    public void constructPage() {
        fetcher = new Fetcher();
        urlLable = new JLabel("网页地址:", SwingConstants.RIGHT);
        urlLable.setFont(FontUtil.getSong12());
        urlLable.setBounds(4, 2, 90, 35);
        urlField = new JTextField();
        urlField.setBounds(95, 7, 380, 25);
        imgLenLable = new JLabel("图片大小限制:", SwingConstants.RIGHT);
        imgLenLable.setFont(FontUtil.getSong12());
        imgLenLable.setBounds(4, 32, 90, 35);
        widthField = new JTextField();
        widthField.setBounds(95, 37, 70, 25);
        widthField.setDocument(new NumOnlyDocument());
        widthField.setToolTipText("最小宽度(像素)");
        widthField.setFont(FontUtil.getSong12());
        heigthField = new JTextField();
        heigthField.setBounds(175, 37, 70, 25);
        heigthField.setDocument(new NumOnlyDocument());
        heigthField.setToolTipText("最小高度(像素)");
        heigthField.setFont(FontUtil.getSong12());
        depLabel = new JLabel("抓取深度:", SwingConstants.RIGHT);
        depLabel.setFont(FontUtil.getSong12());
        depLabel.setBounds(4, 62, 90, 35);
        depField = new JTextField();
        depField.setBounds(95, 67, 70, 25);
        depField.setDocument(new NumOnlyDocument());
        depField.setFont(FontUtil.getSong12());
        addLabel = new JLabel("是否加入到Mana:", SwingConstants.RIGHT);
        addLabel.setFont(FontUtil.getSong12());
        addLabel.setBounds(4, 92, 90, 35);
        addBox = new JCheckBox();
        addBox.setFont(FontUtil.getSong12());
        addBox.setBounds(95, 93, 50, 35);
        infoLabel = new JLabel("");
        infoLabel.setFont(FontUtil.getSong12());
        infoLabel.setBounds(4, 190, 540, 35);

        fetchBtn = new JButton("开 始 集 图");
        fetchBtn.setFont(FontUtil.getSong14());
        fetchBtn.setBounds(95, 130, 130, 50);
        fetchBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                fetchBtn.setEnabled(false);
                infoLabel.setText("正在分析下载图片，根据网速不同，执行速度有所不同，请耐心等待.........");
                fetcher.execute();
            }
        });
        cancelBtn = new JButton("取 消");
        cancelBtn.setFont(FontUtil.getSong14());
        cancelBtn.setBounds(235, 130, 90, 50);
        cancelBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                fetcher.cancel(true);
                infoLabel.setText("");
                fetchBtn.setEnabled(true);
                FetchImgPanel.getInstance().setVisible(false);
                if(fetcher.getNum()!=0){
                    ListServiceImpl.getInstance().reloadTreeData();
                }
            }
        });
        container.add(urlLable);
        container.add(urlField);
        container.add(imgLenLable);
        container.add(widthField);
        container.add(heigthField);
        container.add(depLabel);
        container.add(depField);
        container.add(addLabel);
        container.add(addBox);
        container.add(fetchBtn);
        container.add(cancelBtn);
        container.add(infoLabel);
    }

    private int fetch() {
        int num = 0;
        String url = urlField.getText();
        if (url != null) {
            url = url.trim();
            if ("".equals(url)) {
                MesBox.comAlert("URL地址不能为空");
            } else {
                int imgW = Integer.parseInt(widthField.getText() == null || "".equals(widthField.getText().trim()) ? "0" : widthField.getText());
                int imgH = Integer.parseInt(heigthField.getText() == null || "".equals(heigthField.getText().trim()) ? "0" : heigthField.getText());
                int deep = Integer.parseInt(depField.getText() == null || "".equals(depField.getText().trim()) ? "1" : depField.getText());
                boolean addToMana = addBox.isSelected();
                sysData.setFetchMinWidth(imgW);
                sysData.setFetchMinHeight(imgH);
                sysData.setFetchDeep(deep);
                sysData.setFetchToMana(addToMana);
                SysDataHandler.getInstance().update();
                FetchService fetchService = new FetchServiceImpl();
                num = fetchService.fetchUrl(url, sysData.getSavePath(), imgW, imgH, deep, addToMana);
            }
        } else {
            MesBox.comAlert("URL地址不能为空");
        }
        return num;
    }

    public JTextField getUrlField() {
        return urlField;
    }

    public JTextField getWidthField() {
        return widthField;
    }

    public JTextField getHeigthField() {
        return heigthField;
    }

    public JTextField getDepField() {
        return depField;
    }

    public JCheckBox getAddBox() {
        return addBox;
    }

    class Fetcher extends SwingWorker {
        private int num = 0;

        @Override
        protected Integer doInBackground() throws Exception {
            num = fetch();
            return num;
        }

        @Override
        protected void done() {
            fetchBtn.setEnabled(true);
            infoLabel.setText("下载图片数目：" + num);
            if(num!=0){
                ListServiceImpl.getInstance().reloadTreeData();
            }
        }

        public int getNum() {
            return num;
        }
    }
}

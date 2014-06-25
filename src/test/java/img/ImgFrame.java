package java.img;

import com.wq.constans.Constan;
import com.wq.util.ImageUtils;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * To change this template use File | Settings | File Templates.
 *
 * @author wangqing
 * @since 1.0.0
 */
public class ImgFrame extends JFrame {
    public ImgFrame() {
        constructPlate();
        long beg=System.currentTimeMillis();
        mm();
        long end=System.currentTimeMillis();
        System.out.println("耗时："+(end-beg));
    }

    public void constructPlate() {
        this.setLayout(null);
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
                System.out.println("已关闭！");
            }
        });
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//设置关闭操作
    }

    public void mm(){
        File ff=new File("D:\\wangqing\\105D7000");
        File[] dd = ff.listFiles();
        for(int i=0;i<10;i++){
            JLabel jLabel=new JLabel();
            jLabel.setBounds(i*50,0,50,50);
            jLabel.setPreferredSize(new Dimension(50,50));
            makePage(jLabel,dd[i]);
        }
    }
    public void makePage( JLabel jLabel,File path){
        try {
            BufferedImage img = ImageIO.read(path);
            img= ImageUtils.reduce(img, 50);
            jLabel.setIcon(new ImageIcon(img));
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.add(jLabel);
    }
}

package java.img;

import java.img.ImgFrame;

/**
 * To change this template use File | Settings | File Templates.
 *
 * @author wangqing
 * @since 1.0.0
 */
public class TestLoad {
    public static void main(String[] args) {
        long beg=System.currentTimeMillis();
        ImgFrame imgFrame=new ImgFrame();
        imgFrame.setVisible(true);
        long end=System.currentTimeMillis();
        System.out.println("耗时："+(end-beg));
    }
}

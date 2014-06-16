package com.wq.util;

import com.wq.cache.AllCache;
import com.wq.cache.SystemCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;

/**
 * Created with IntelliJ IDEA.
 * User: wangq
 * Date: 14-6-14
 * Time: 下午9:39
 * To change this template use File | Settings | File Templates.
 */
public class ImageLabelUtil {
    private static final Logger log = LoggerFactory.getLogger(ImageLabelUtil.class);
    /**
     * 处理一般图片
     *
     * @param path
     */
    public static JLabel getImageLabel(final String path) {
        BufferedImage bufferedImage = null;
        com.wq.model.SysData data = SystemCache.getInstance().getData();
        boolean isgif = false;
        Image img = null;
        if (path.endsWith(".gif") || path.endsWith(".GIF")) {
            isgif = true;
            Toolkit tk = Toolkit.getDefaultToolkit();
            img = tk.createImage(path);
            bufferedImage = ImageUtils.toBufferedImage(img);
        }else{
            try {
                bufferedImage = ImageIO.read(new FileInputStream(path));
            } catch (Throwable e) {
                log.error("读取图片异常", e);
            }
        }
        if (bufferedImage == null) return null;
        final JLabel jLabel = new JLabel();
        ImageIcon icon = null;
        if (data.isHMode()) {
            int maxHeight = AllCache.getInstance().getMaxPicPanelHeight();
            int imgHeight = bufferedImage.getHeight();
            //等比缩放
            if (data.isAutoFit() && imgHeight > maxHeight) {
                double bo = (double) maxHeight / (double) imgHeight;
                bo = Math.round(bo * 10000) / 10000.0;
                bufferedImage = ImageUtils.reduceImg(bufferedImage, bo);
                icon = new ImageIcon(bufferedImage);
            } else {
                if (!isgif) {
                    icon = new ImageIcon(bufferedImage);
                } else {
                    icon = new ImageIcon(img);
                }
            }
        } else {
            int maxWidth = AllCache.getInstance().getMaxPicPanelWidth();
            int imgWidth = bufferedImage.getWidth();
            //等比缩放
            if (data.isAutoFit() && imgWidth > maxWidth) {
                double bo = (double) maxWidth / (double) imgWidth;
                bo = Math.round(bo * 10000) / 10000.0;
                bufferedImage = ImageUtils.reduceImg(bufferedImage, bo);
                icon = new ImageIcon(bufferedImage);
            } else {
                if (!isgif) {
                    icon = new ImageIcon(bufferedImage);
                } else {
                    icon = new ImageIcon(img);
                }
            }
        }
        jLabel.setIcon(icon);
        jLabel.setName(path);
        return jLabel;
    }
}

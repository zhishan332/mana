package com.wq.util;

import com.wq.cache.AllCache;
import com.wq.cache.SystemCache;
import com.wq.exception.ManaException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.FileInputStream;

/**
 * To change this template use File | Settings | File Templates.
 *
 * @author wangqing
 * @since 1.0.0
 */
public class ImageLabelUtil {
    private static final Logger log = LoggerFactory.getLogger(ImageLabelUtil.class);

    /**
     * 处理一般图片
     *
     * @param path 图片路径
     */
    public static JLabel getImageLabel(final String path) throws ManaException {
        BufferedImage bufferedImage = null;
        com.wq.model.SysData data = SystemCache.getInstance().getData();
        boolean isgif = false;
        Image img = null;
        if (path.endsWith(".gif") || path.endsWith(".GIF")) {
            isgif = true;
            Toolkit tk = Toolkit.getDefaultToolkit();
            img = tk.createImage(path);
            bufferedImage = ImageUtils.toBufferedImage(img);
        } else {
            try {
                bufferedImage = ImageIO.read(new BufferedInputStream(new FileInputStream(path)));
            } catch (Throwable e) {
                log.error("读取图片异常", e);
                throw new ManaException("内存不足，加载图片失败");
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

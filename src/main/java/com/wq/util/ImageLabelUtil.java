package com.wq.util;

import com.wq.cache.AllCache;
import com.wq.cache.SystemCache;
import com.wq.exception.ManaException;
import com.wq.model.SysData;
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
        SysData data = SystemCache.getInstance().getData();
        ImageIcon icon;
        JLabel jLabel = null;
        try {
            jLabel = new JLabel();
        } catch (Throwable e) { //使用了substance可能导致动态创建JLabel失败
            log.warn("创建JLabel失败", e.getMessage());
        }
        if (path.endsWith(".gif") || path.endsWith(".GIF")) {
            Toolkit tk = Toolkit.getDefaultToolkit();
            Image img = tk.createImage(path);
            icon = new ImageIcon(img);
        } else {
            try {
                bufferedImage = ImageIO.read(new BufferedInputStream(new FileInputStream(path)));
            } catch (Throwable e) {
                log.error("读取图片异常", e);
                throw new ManaException("加载图片失败",e);
            }
            if (bufferedImage == null) return null;
            if (data.isHMode()) {
                int maxHeight = AllCache.getInstance().getMaxPicPanelHeight();
                int imgHeight = bufferedImage.getHeight();
                //等比缩放
                if (data.isAutoFit() && imgHeight > maxHeight) {
                    double bo = (double) imgHeight / (double) maxHeight;
//                    bo = Math.round(bo * 10000) / 10000.0;
                    bufferedImage = ImageUtils.reduce(bufferedImage, bo);
                }
            } else {
                int maxWidth = AllCache.getInstance().getMaxPicPanelWidth();
                int imgWidth = bufferedImage.getWidth();
                //等比缩放
                if (data.isAutoFit() && imgWidth > maxWidth) {
                    double bo = (double) imgWidth / (double) maxWidth;
//                    bo = Math.round(bo * 10000) / 10000.0;
                    bufferedImage = ImageUtils.reduce(bufferedImage, bo);
                }
            }
            icon = new ImageIcon(bufferedImage);
        }
        if (jLabel != null) {
            jLabel.setIcon(icon);
            jLabel.setName(path);
        }
        return jLabel;
    }
}

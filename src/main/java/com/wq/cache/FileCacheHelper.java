package com.wq.cache;

import com.wq.constans.Constan;
import com.wq.model.FileCacheModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Created with IntelliJ IDEA.
 * User: wangq
 * Date: 14-6-14
 * Time: 下午2:17
 * To change this template use File | Settings | File Templates.
 */
public class FileCacheHelper {
    private static final Logger log = LoggerFactory.getLogger(FileCacheHelper.class);
    private static String cachePath = Constan.CACHEPATH;
    private static String suffix = ".mec";

    public static void save(FileCacheModel fileCacheModel, long hashcode) {

        ObjectOutputStream out = null;
        try {
            String newName = String.valueOf(hashcode);
            File ff = new File(cachePath + newName + suffix);
            if (!ff.exists()) {
                log.info("创建缓存文件：" + ff.getName());
                boolean isSuc = ff.createNewFile();
                if (!isSuc) {
                    log.info("创建缓存文件失败..........");
                    return;
                }
            }
            GZIPOutputStream gos = new GZIPOutputStream(new FileOutputStream(ff));
            out = new ObjectOutputStream(gos);
            out.writeObject(fileCacheModel);
            out.flush();
            out.close();
            gos.close();
        } catch (IOException e) {
            log.error("缓存图片异常", e);
        }
    }

    public static FileCacheModel get(long hashcode) {
        ObjectInputStream in = null;
        FileCacheModel data = null;
        try {
            String newName = String.valueOf(hashcode);
            File ff = new File(cachePath + newName + suffix);
            if (!ff.exists()) return null;
            GZIPInputStream gis = new GZIPInputStream(new FileInputStream(ff));
            in = new ObjectInputStream(gis);
            data = (FileCacheModel) in.readObject();
            in.close();
            gis.close();
        } catch (IOException e) {
            log.error("缓存图片异常", e);
        } catch (ClassNotFoundException e) {
            log.error("缓存图片异常", e);
        }
        return data;
    }

    public static void deleteSome() {
        File ff = new File(cachePath);
        File[] lists = ff.listFiles();
        if (null == lists) return;
        for (int i = 100; i < lists.length; i++) {
            File dFile = lists[i];
            log.info("删除多余文件cache:" + dFile.getName());
            dFile.deleteOnExit();
        }
    }

    public static void main(String[] args) throws Exception {
        File ff = new File("F:\\Image\\tem");
        File[] list = ff.listFiles();
        List<ImageIcon> clist = new ArrayList<ImageIcon>();
        int i = 0;
        for (File f1 : list) {
            if (i >= 10) break;
            BufferedImage bufferedImage = ImageIO.read(new FileInputStream(f1));
            ImageIcon dd = new ImageIcon(bufferedImage);
            clist.add(dd);
            i++;
        }
        FileCacheModel mod = new FileCacheModel();
        mod.setModifyDate(1231231231);
        mod.setObject(clist);
        FileCacheHelper.save(mod, 1212);
    }
}

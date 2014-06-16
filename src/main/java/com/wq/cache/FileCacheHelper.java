package com.wq.cache;

import com.wq.constans.Constan;
import com.wq.model.FileCacheModel;
import com.wq.service.CacheService;
import com.wq.service.CacheServiceImpl;
import com.wq.util.ImageLabelUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
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
    private static String suffix = ".dat";
    private static int BUFFER = 1024 * 10;

    public static void save(FileCacheModel fileCacheModel, long hashcode) {
        ObjectOutputStream out = null;
        try {
            String newName = String.valueOf(hashcode);
            File ff = new File(cachePath + newName + suffix);
            if (!ff.exists()) {
                boolean isSuc = ff.createNewFile();
                if (!isSuc) {
                    log.info("创建缓存文件失败..........");
                    return;
                }
            }
            log.info("创建缓存文件：" + ff.getName());
            BufferedOutputStream dest = new BufferedOutputStream(new GZIPOutputStream(new FileOutputStream(ff)), BUFFER);
            out = new ObjectOutputStream(dest);
            out.writeObject(fileCacheModel);
            out.flush();
            out.close();
            dest.close();
        } catch (IOException e) {
            log.error("缓存图片异常", e);
        }
    }

    public static void save2(FileCacheModel fileCacheModel, long hashcode) {
        ObjectOutputStream out = null;
        try {
            String newName = String.valueOf(hashcode);
            File ff = new File(cachePath + newName + suffix);
            if (!ff.exists()) {
                boolean isSuc = ff.createNewFile();
                if (!isSuc) {
                    log.info("创建缓存文件失败..........");
                    return;
                }
            }
            log.info("创建缓存文件：" + ff.getName());
            GZIPOutputStream gos = new GZIPOutputStream(new FileOutputStream(ff));
//            BufferedOutputStream  dest= new BufferedOutputStream(gos, BUFFER);
            out = new ObjectOutputStream(gos);
            out.writeObject(fileCacheModel);
            out.flush();
            out.close();
            gos.close();
//            dest.close();
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
            BufferedInputStream dest = new BufferedInputStream(new GZIPInputStream(new FileInputStream(ff)), BUFFER);
            in = new ObjectInputStream(dest);
            data = (FileCacheModel) in.readObject();
            in.close();
            dest.close();
        } catch (IOException e) {
            log.error("缓存图片异常", e);
        } catch (ClassNotFoundException e) {
            log.error("缓存图片异常", e);
        }
        return data;
    }

    public static FileCacheModel get2(long hashcode) {
        ObjectInputStream in = null;
        FileCacheModel data = null;
        try {
            String newName = String.valueOf(hashcode);
            File ff = new File(cachePath + newName + suffix);
            if (!ff.exists()) return null;
            in = new ObjectInputStream(new GZIPInputStream(new FileInputStream(ff)));
            data = (FileCacheModel) in.readObject();
            in.close();
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

    public static void asynIndex() {
        ExecutorService pool = Executors.newSingleThreadExecutor();
        pool.execute(new Runnable() {
            @Override
            public void run() {
                long beg = System.currentTimeMillis();
                log.info("开始构建缓存>>>>>>>>>>>>");
                FileCacheHelper.index();//构建缓存索引
                log.info("构建缓存索引完成>,本次耗时：" + (System.currentTimeMillis() - beg));
            }
        });
    }

    public static void index() {
        CacheService cacheService = CacheServiceImpl.getInstance();
        Map<String, List<String>> map = cacheService.getAllPic();
        ExecutorService pool = Executors.newFixedThreadPool(2);
        for (final Map.Entry<String, List<String>> entry : map.entrySet()) {
            final List<String> list = entry.getValue();
            for (int i = 0; i < list.size(); i += 10) {
                final int st=i;
                pool.execute(new Runnable() {
                    @Override
                    public void run() {
                        final long beg = System.currentTimeMillis();
                        long hashcode = indexImageLabel(entry.getKey(), list, st);
                        long end = System.currentTimeMillis();
                        log.info(hashcode + suffix + "构建耗时：" + (end - beg) + "ms");
                    }
                });
            }
        }
    }

    private static long indexImageLabel(String folder, List<String> list, int start) {
        List<String> tempList = new ArrayList<String>();
        for (int i = start; i < list.size() && i < start + 10; i++) {
            tempList.add(list.get(i));
        }
        File folderFile = new File(folder);
        final long mdate = folderFile.lastModified();
        final long hashCode = tempList.hashCode();
        String newName = String.valueOf(hashCode);
        File ff = new File(cachePath + newName + suffix);
        if (ff.exists()) return hashCode;
        List<JLabel> labels = new ArrayList<JLabel>();
        for (String path : tempList) {
            JLabel imgLabel = ImageLabelUtil.getImageLabel(path);
            labels.add(imgLabel);
        }
        if (!labels.isEmpty()) {
            final FileCacheModel fileCacheModel = new FileCacheModel();
            fileCacheModel.setModifyDate(mdate);
            fileCacheModel.setObject(labels);
            FileCacheHelper.save(fileCacheModel, hashCode);
        }
        return hashCode;
    }

    //删除无用的缓存
    public static void deleteDirty() {
        List<String> validList = new ArrayList<String>();
        Map<String, List<String>> map = CacheServiceImpl.getInstance().getAllPic();
        for (final Map.Entry<String, List<String>> entry : map.entrySet()) {
            final List<String> list = entry.getValue();
            for (int i = 0; i < list.size(); i += 10) {
                List<String> tempList = new ArrayList<String>();
                for (int t = i; t < list.size() && t < i + 10; t++) {
                    tempList.add(list.get(i));
                }
                long hashcode = tempList.hashCode();
                String newName = String.valueOf(hashcode);
                validList.add(newName + suffix);
            }
        }
        File cf = new File(cachePath);
        File[] files = cf.listFiles();
        int i=0;
        if (files != null) {
            for (File file : files) {
                String name = file.getName();
                log.debug("validList:"+validList.size()+",filename:"+name);
                if (!validList.contains(name)) {
                    file.deleteOnExit();
                    log.info("删除无用缓存文件：" + file.getName());
                    i++;
                }
            }
        }
        log.info("清理缓存文件完成,删除缓存个数："+i);
    }

    public static void main(String[] args) throws Exception {
        File ff = new File("F:\\Image\\tem");
        File[] list = ff.listFiles();
        List<JLabel> clist = new ArrayList<JLabel>();
        int i = 0;
        for (File f1 : list) {
            if (i >= 10) break;
            JLabel imgLabel = ImageLabelUtil.getImageLabel(f1.getAbsolutePath());
            clist.add(imgLabel);
            i++;
        }
        FileCacheModel mod = new FileCacheModel();
        mod.setModifyDate(1231231231);
        mod.setObject(clist);
        long beg = System.currentTimeMillis();
        FileCacheHelper.save(mod, 1111111111);
        long end = System.currentTimeMillis();
        System.out.println(end - beg);
        FileCacheHelper.get(1111111111);
        long end2 = System.currentTimeMillis();
        System.out.println(end2 - end);
    }
}

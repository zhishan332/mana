package com.wq.cache;

import com.wq.constans.Constan;
import com.wq.model.SysData;
import com.wq.util.SystemUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 系统级别的缓存，用于存储用户的设置信息等
 *
 * @author wangqing
 * @since 1.0.0
 */
public class SystemCache {
    private static final Logger log = LoggerFactory.getLogger(SystemCache.class);
    private static SystemCache systemCache = null;
    private File dbpath = new File(Constan.DBPATH);
    private SysData data;
    private List<String> validFileCacheList;

    private SystemCache() {
        validFileCacheList = new ArrayList<String>();
    }

    public static SystemCache getInstance() {
        if (systemCache == null) systemCache = new SystemCache();
        return systemCache;
    }

    public com.wq.model.SysData getData() {
        if (data == null) loadData();
        return data;
    }

    public void loadData() {
        if (!dbpath.exists()) init();
        ObjectInputStream in = null;
        try {
            in = new ObjectInputStream(new FileInputStream(dbpath));
            com.wq.model.SysData data = (com.wq.model.SysData) in.readObject();
            in.close();
            this.data = data;
        } catch (IOException e) {
            log.error("读取系统缓存失败", e);
        } catch (ClassNotFoundException e) {
            log.error("读取系统缓存失败", e);
        }
    }

    public void update() {
        writeSysData(data);
    }

    /**
     * 初始化系统数据
     */
    private void init() {
        boolean isSuc = false;
        try {
            isSuc = dbpath.createNewFile();
        } catch (IOException e) {
            log.error("初始化系统缓存失败", e);
        }
        SysData data = new SysData();
        Set<String> fileList = new HashSet<String>();
        data.setFileList(fileList);
        data.setExpland(true);
        data.setFirstLogin(true);
        data.setSavePath(SystemUtil.getDefaultUserFile());
        data.setLastOpenPath(SystemUtil.getDefaultUserFile());
        data.setSpeed(35);
        Set<String> typeList = new HashSet<String>();
        typeList.add("jpg");
        typeList.add("JPG");
        typeList.add("jpeg");
        typeList.add("JPEG");
        typeList.add("gif");
        typeList.add("GIF");
        typeList.add("bmp");
        typeList.add("png");
        typeList.add("PNG");
        data.setTypeList(typeList);
        data.setIgnoreEmptyFolder(true);
        data.setAutoFit(true);
        data.setFetchDeep(1);
        data.setFetchMinWidth(200);
        data.setFetchMinHeight(200);
        data.setFetchToMana(true);
        data.setUseProxy(false);
        data.setHMode(false);
        if (isSuc)
            writeSysData(data);
    }

    private void writeSysData(com.wq.model.SysData data) {
        ObjectOutputStream out = null;
        try {
            out = new ObjectOutputStream(new FileOutputStream(dbpath));
            out.writeObject(data);
            out.close();
        } catch (IOException e) {
            log.error("写入系统缓存失败", e);
        }
    }

    public List<String> getValidFileCacheList() {
        return validFileCacheList;
    }

    public void setValidFileCacheList(List<String> validFileCacheList) {
        this.validFileCacheList = validFileCacheList;
    }
}


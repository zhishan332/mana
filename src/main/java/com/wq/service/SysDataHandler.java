package com.wq.service;

import com.wq.constans.Constan;
import com.wq.model.SysData;
import com.wq.util.SystemUtil;

import java.io.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: wangq
 * Date: 13-5-17
 * Time: 下午3:12
 * To change this template use File | Settings | File Templates.
 */
public class SysDataHandler {
    private File dbpath = new File(Constan.DBPATH);
    private static SysDataHandler sysDataHandler = null;
    private SysData data;

    private SysDataHandler() {
    }

    public static SysDataHandler getInstance() {
        if (sysDataHandler == null) sysDataHandler = new SysDataHandler();
        return sysDataHandler;
    }

    public SysData getData() {
        if (data == null) loadData();
        return data;
    }

    public void loadData() {
        if (!dbpath.exists()) init();
        ObjectInputStream in = null;
        try {
            in = new ObjectInputStream(new FileInputStream(dbpath));
            SysData data = (SysData) in.readObject();
            in.close();
            this.data = data;
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (ClassNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public void update() {
        writeSysData(data);
    }

    /**
     * 初始化系统数据
     */
    private void init() {
        try {
            dbpath.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        SysData data = new SysData();
        Set fileList = new HashSet();
        data.setFileList(fileList);
        data.setExpland(true);
        data.setFirstLogin(true);
        data.setSavePath(SystemUtil.getDefaultUserFile());
        data.setLastOpenPath(SystemUtil.getDefaultUserFile());
        data.setSpeed(35);
        Set typeList = new HashSet();
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
        writeSysData(data);
    }

    private void writeSysData(SysData data) {
        ObjectOutputStream out = null;
        try {
            out = new ObjectOutputStream(new FileOutputStream(dbpath));
            out.writeObject(data);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}


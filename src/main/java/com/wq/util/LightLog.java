package com.wq.util;

import com.wq.constans.Constan;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by IntelliJ IDEA.
 * User: wq
 * Date: 11-6-26
 * Time: 下午1:32
 * To change this template use File | Settings | File Templates.
 */
public class LightLog {
    private String cls;
    private static File f;
    private static FileOutputStream out;

//    static {
//        new Thread() {
//            public void run() {
//                try {
//                    f = new File(Constan.LOGPATH);
//                    if (f.exists()) {
//                        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>日志文件加载成功！");
//                    } else {
//                        f.createNewFile();
//                        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>日志文件创建成功！");
//                    }
//                } catch (Exception e1) {
//                    // TODO Auto-generated catch block
//                    e1.printStackTrace();
//                }
//                if (null != f && !f.exists()) {
//                    f.mkdirs();
//                }
//
//                try {
//                    out = new FileOutputStream(f, true);
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                }
//            }
//        }.start();
//    }

    private static LightLog lightLog;

    private LightLog(String cls) {
        this.cls = "[" + cls + "] ";
    }

    public static LightLog getInstance(Class cls) {
        if (lightLog == null) {
            lightLog = new LightLog(cls.getName());
        }
        return lightLog;
    }

    public void info(Exception e, String msg) {
        System.err.println(cls + getTime() + ":" + msg+e);
        outLog(cls + getTime() + ":" + msg+e);
    }

    public void info(Exception e) {
        System.err.println(cls + getTime() + ":" + e);
        outLog(cls + getTime() + ":" + e);
    }

    public void info(String msg) {
        System.err.println(cls + getTime() + ":" + msg);
        outLog(cls + getTime() + ":" + msg);
    }


    public void debug(Exception e, String msg) {
        System.err.println(cls + getTime() + ":" + msg+e);
        outLog(cls + getTime() + ":" +msg+ e);
    }

    public void debug(Exception e) {
        System.err.println(cls + getTime() + ":" + e);
        outLog(cls + getTime() + ":" + e);
    }

    public void debug(String msg) {
        System.err.println(cls + getTime() + ":" + msg);
        outLog(cls + getTime() + ":" + msg);
    }

    public void error(Exception e, String msg) {
        System.err.println(cls + getTime() + ":" + msg+e);
        outLog("系统错误：" + cls + getTime() + ":" + e);
    }

    public void error(Exception e) {
        System.err.println(cls + getTime() + ":" + e);
        outLog("系统错误：" + cls + getTime() + ":" + e);
    }

    public void error(String msg) {
        System.err.println(cls + getTime() + ":" + msg);
        outLog("系统错误：" + cls + getTime() + ":" + msg);
    }

    public static String getTime() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
    }

    private static void outLog(final Object exp) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (null != out) {
                    try {
                        out.write((getTime() + ":" + exp + "\r\n").getBytes());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
}

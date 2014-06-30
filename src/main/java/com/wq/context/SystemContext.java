package com.wq.context;

import java.util.HashMap;
import java.util.Map;

/**
 * 系统上下文
 * @author wangqing
 * @since 1.0.0
 */
public class SystemContext {
    private static volatile SystemContext instance = null; //必须设置为volatile否则double check可能无效（JDK1.5+）
    private Map<String, Object> context = new HashMap<String, Object>();

    private SystemContext() {
    }

    /**
     * 获取系统配置对象
     *
     * @return instance
     */
    public static SystemContext getInstance() {
        if (instance == null) { // double check (jdk1.5+)
            synchronized (SystemContext.class) {
                if (instance == null)
                    instance = new SystemContext();
            }
        }
        return instance;
    }

    public void put(String key, Object val) {
        context.put(key, val);
    }

    public Object get(String key) {
        return context.get(key);
    }

    public int getIntVal(String key) {
        Object obj = context.get(key);
        if (null == obj) obj = "0";
        return Integer.parseInt(String.valueOf(obj));
    }

    public String getStrVal(String key) {
        Object obj = context.get(key);
        if (null == obj) obj = "";
        return String.valueOf(obj);
    }
}

package com.wq.model;

import java.io.Serializable;

/**
 * 文件缓存对象
 * @author wangqing
 * @since 1.0.0
 */
public class FileCacheModel implements Serializable {
    private Object object;
    private long modifyDate;

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public long getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(long modifyDate) {
        this.modifyDate = modifyDate;
    }
}

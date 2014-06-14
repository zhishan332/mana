package com.wq.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: wangq
 * Date: 14-6-14
 * Time: 下午2:19
 * To change this template use File | Settings | File Templates.
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

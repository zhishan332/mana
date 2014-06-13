package com.wq.model;

/**
 * Created with IntelliJ IDEA.
 * User: wangq
 * Date: 12-8-3
 * Time: 下午3:17
 * 目录对象
 */
public class DirMenu {
    private String name;
    private String filePath;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public String toString() {
        return name;
    }
}

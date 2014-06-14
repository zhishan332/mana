package com.wq.service;

import java.io.IOException;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: wangq
 * Date: 12-8-2
 * Time: 下午5:17
 * 列表操作业务处理
 */
public interface ListService {

    /**
     * 添加映射文件夹路径
     *
     * @param path
     * @throws IOException
     */
    public void addTreeData(String path);

    /**
     * 删除映射文件夹
     *
     * @param path
     * @throws IOException
     */
    public void delTreeData(String path);

    /**
     * 刷新树以及刷新图床
     *
     * @throws IOException
     */
    public void reloadTreeData();

    /**
     * 根据图片路径列表加载图片
     *
     * @param list
     */
    public void loadPic(String folder,List<String> list,int start);

    /**
     * 保存面板中所有的图片
     */
    public void saveAll();

}

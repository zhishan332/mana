package com.wq.service;

import javax.swing.tree.DefaultMutableTreeNode;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created with IntelliJ IDEA.
 * User: wangq
 * Date: 12-8-4
 * Time: 下午4:16
 * 缓存业务处理
 */
public interface CacheService {
    /**
     * 处理路径图片加载的线程池
     */
    public ExecutorService pool = Executors.newFixedThreadPool(4);

    /**
     * 获取目录树
     *
     * @return
     * @throws IOException
     */
    public List<DefaultMutableTreeNode> getTreeData() throws IOException;

    /**
     * 获取所有图片路径
     *
     * @return 《文件夹，图片列表》
     * @throws IOException
     */
    public Map<String, List<String>> getAllPic();

    /**
     * 获取连接池
     *
     * @return
     */
    public ExecutorService getPool();


}

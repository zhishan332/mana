package com.wq.service;

import com.wq.cache.SystemCache;
import com.wq.model.DirMenu;
import com.wq.util.FileFilter;
import com.wq.util.FileUtil;
import com.wq.util.RecursiveTravelPerf;

import javax.swing.tree.DefaultMutableTreeNode;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutorService;

/**
 * Created with IntelliJ IDEA.
 * User: wangq
 * Date: 12-8-4
 * Time: 下午4:19
 * To change this template use File | Settings | File Templates.
 */
public class CacheServiceImpl implements CacheService {
    private static CacheServiceImpl ourInstance = new CacheServiceImpl();
    private List<DefaultMutableTreeNode> nodeList = new LinkedList<DefaultMutableTreeNode>();
    private SystemCache systemCache = SystemCache.getInstance();

    public static CacheServiceImpl getInstance() {
        return ourInstance;
    }

    private CacheServiceImpl() {
        makeNodeList();
    }

    private void makeNodeList() {
        nodeList.clear();
        Set<String> list = systemCache.getData().getFileList();
        for (String filePath : list) {
            File fileTemp = new File(filePath);
            if (fileTemp.isDirectory() && openEmptyFolder(fileTemp)) {
                DefaultMutableTreeNode node = getNode(fileTemp);
                if (node != null) {
                    nodeList.add(node);
                }
            }
        }
    }

    private DefaultMutableTreeNode getNode(File file) {
        DirMenu menu = new DirMenu();
        menu.setName(file.getName());
        menu.setFilePath(file.getAbsolutePath());
        final DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode(menu);
        File[] files = file.listFiles();
        if (files != null) {
            for (final File file1 : files) {
                if (file1.isDirectory() && openEmptyFolder(file1)) {
                    pool.execute(new Runnable() {
                        public void run() {
                            DefaultMutableTreeNode node = getNode(file1);
                            if (node != null) {
                                rootNode.add(node);
                            }
                        }
                    });
                }
            }
        }
        return rootNode;
    }

    @Override
    public List<DefaultMutableTreeNode> getTreeData() throws IOException {
        return nodeList;
    }

    private boolean openEmptyFolder(File file) {
        return !systemCache.getData().isIgnoreEmptyFolder() || FileUtil.getFileSize(file) > 0;
    }

    @Override
    public Map<String, List<String>> getAllPic() {
        Set<String> list = systemCache.getData().getFileList();
        Set<String> typeList = systemCache.getData().getTypeList();
        Map<String, List<String>> map = new LinkedHashMap<String, List<String>>();
        final FileFilter fileFilter = new FileFilter(typeList); //文件的后缀名
        for (String filePath : list) {
            File fileTemp = new File(filePath);
            if (!fileTemp.exists()) continue;
            Map<String, List<String>> temMap = new RecursiveTravelPerf().scan(filePath, fileFilter);
            if (temMap != null) {
                map.putAll(temMap);
            }
        }
        makeNodeList();
        return map;
    }

    public ExecutorService getPool() {
        return pool;
    }

}

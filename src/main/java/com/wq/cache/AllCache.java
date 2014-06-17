package com.wq.cache;

import com.wq.service.CacheService;
import com.wq.service.CacheServiceImpl;

import java.awt.*;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: wangq
 * Date: 12-8-2
 * Time: 下午5:39
 * 所有缓存
 */
public class AllCache {
    private static AllCache ourInstance = new AllCache();
    private static Map<String, List<String>> map = new LinkedHashMap<String, List<String>>();
    private static CacheService cacheService = CacheServiceImpl.getInstance();

    static {
        map = cacheService.getAllPic();
    }

    public static AllCache getInstance() {
        return ourInstance;
    }

    private AllCache() {
    }

    public Map<String, List<String>> getMenu() {
        return map;
    }

    /**
     * 获取自适应情况下图片最大宽度
     *
     * @return
     */
    public int getMaxPicPanelWidth() {
        Dimension scrSize = Toolkit.getDefaultToolkit().getScreenSize();
        return scrSize.width - 260;
    }

    /**
     * 获取自适应情况下图片最大高度
     *
     * @return
     */
    public int getMaxPicPanelHeight() {
        Dimension scrSize = Toolkit.getDefaultToolkit().getScreenSize();
        return scrSize.height - 60;
    }

    public void reloadListCache() throws IOException {
        map.clear();
        map = cacheService.getAllPic();
    }
}

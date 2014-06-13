package com.wq.cache;

import java.util.HashMap;
import java.util.Map;

/**
 * 默认的Cache管理者
 *
 * @author wangqing
 * @since 1.0.0
 */
public class DefultCacheManager {
    private static volatile DefultCacheManager instance = null;
    private Cache defaultCache;
    private Map<String, Cache> cacheMap;

    private DefultCacheManager() {
        cacheMap = new HashMap<String, Cache>();
        defaultCache = new SimpleMemCache(10000, 1000 * 60, ExpiryStrategy.LRU);
        cacheMap.put("defaultCache", defaultCache);
    }

    /**
     * 获取DefultCacheManager对象
     *
     * @return instance
     */
    public static DefultCacheManager getInstance() {
        if (instance == null) {
            synchronized (DefultCacheManager.class) {
                if (instance == null)
                    instance = new DefultCacheManager();
            }
        }
        return instance;
    }

    public Cache getCache(String cacheName) {
        if (!cacheMap.keySet().contains(cacheName)) {
            Cache nwCache = new SimpleMemCache(1000, 1000 * 60, ExpiryStrategy.LRU);
            cacheMap.put(cacheName, nwCache);
            return nwCache;
        } else return cacheMap.get(cacheName);
    }
    public Cache getCache(String cacheName,int max) {
        if (!cacheMap.keySet().contains(cacheName)) {
            Cache nwCache = new SimpleMemCache(max, 1000 * 60, ExpiryStrategy.LRU);
            cacheMap.put(cacheName, nwCache);
            return nwCache;
        } else return cacheMap.get(cacheName);
    }

    public Cache getCache() {
        return defaultCache;
    }
}

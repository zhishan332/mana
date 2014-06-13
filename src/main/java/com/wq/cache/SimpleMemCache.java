package com.wq.cache;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 简易的内存缓存
 *
 * @author wangqing
 * @since 1.0.0
 */
public class SimpleMemCache extends AbstractMemCache {

    private final static int defaultCacheSize = 0;
    private final static int defaultExpire = 0;
    private ExpiryStrategy strategy = ExpiryStrategy.LRU;

    public SimpleMemCache() {
        super(defaultCacheSize, defaultExpire);
        construct();
    }

    public SimpleMemCache(ExpiryStrategy strategy) {
        super(defaultCacheSize, defaultExpire);
        construct();
    }

    public SimpleMemCache(int cacheSize) {
        super(cacheSize, defaultExpire);
        construct();
    }

    public SimpleMemCache(int cacheSize, long defaultExpire) {
        super(cacheSize, defaultExpire);
        construct();
    }

    public SimpleMemCache(int cacheSize, long defaultExpire, ExpiryStrategy strategy) {
        super(cacheSize, defaultExpire);
        this.strategy = strategy;
        construct();
    }

    private void construct() {
        switch (strategy) {
            case LRU:
                this.cacheMap = new LinkedHashMap<String, CacheObject>(cacheSize + 1, 1f, true) {
                    @Override
                    protected boolean removeEldestEntry(
                            Map.Entry<String, CacheObject> eldest) {
                        return SimpleMemCache.this.removeEldestEntry(eldest);
                    }
                };
                break;
            case LFU:
                cacheMap = new HashMap<String, CacheObject>(cacheSize + 1);
                break;
            case FIFO:
                cacheMap = new LinkedHashMap<String, CacheObject>(cacheSize + 1);
                break;
            default:
                cacheMap = new HashMap<String, CacheObject>(cacheSize + 1);
                break;
        }

    }

    /**
     * 淘汰对象具体实现
     *
     * @return
     */
    @Override
    protected int eliminateCache() {
        if (!isNeedClearExpiredObject()) return 0;
        Iterator<CacheObject> iterator = cacheMap.values().iterator();
        int count = 0;
        long minAccessCount = Long.MAX_VALUE;
        String firstKey = null;
        while (iterator.hasNext()) {
            CacheObject cacheObject = iterator.next();
            if (cacheObject.isExpired()) {
                iterator.remove();
                count++;
            } else {
                minAccessCount = Math.min(cacheObject.accessCount, minAccessCount);
                if (firstKey == null)
                    firstKey = cacheObject.key;
            }
        }

        switch (strategy) {
            case LRU:
                return count;
            case LFU:
                if (count > 0) return count;
                if (minAccessCount != Long.MAX_VALUE) {
                    iterator = cacheMap.values().iterator();
                    while (iterator.hasNext()) {
                        CacheObject cacheObject = iterator.next();
                        cacheObject.accessCount -= minAccessCount;
                        if (cacheObject.accessCount <= 0) {
                            iterator.remove();
                            count++;
                        }
                    }
                }
                return count;
            case FIFO:
                if (firstKey != null && isFull()) {//删除过期对象还是满,继续删除链表第一个
                    cacheMap.remove(firstKey);
                }
                return count;
            default:
                return count;
        }
    }

    private boolean removeEldestEntry(Map.Entry<String, CacheObject> eldest) {
        return cacheSize != 0 && size() > cacheSize;

    }
}

package com.wq.cache;

import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 *  默认的Cache实现
 *
 * @author wangqing
 * @since 1.0.0
 */
public abstract class AbstractMemCache implements Cache {

    private final ReentrantReadWriteLock cacheLock = new ReentrantReadWriteLock();
    private final Lock readLock = cacheLock.readLock();
    private final Lock writeLock = cacheLock.writeLock();
    protected Map<String, CacheObject> cacheMap;
    protected int cacheSize;// 缓存大小 , 0 -> 无限制
    protected boolean existCustomExpire; //是否设置默认过期时间
    protected long defaultExpire;// 默认过期时间, 0 -> 永不过期

    public AbstractMemCache(int cacheSize, long defaultExpire) {
        this.cacheSize = cacheSize;
        this.defaultExpire = defaultExpire;
    }

    public int getCacheSize() {
        return cacheSize;
    }

    public long getDefaultExpire() {
        return defaultExpire;
    }

    protected boolean isNeedClearExpiredObject() {
        return defaultExpire > 0 || existCustomExpire;
    }

    public void put(String key, Object value) {
        put(key, value, defaultExpire);
    }

    public void put(String key, Object value, long expire) {
        writeLock.lock();
        try {
            CacheObject co = new CacheObject(key, value, expire);
            if (expire != 0) {
                existCustomExpire = true;
            }
            if (isFull()) {
                eliminate();
            }
            cacheMap.put(key, co);
        } finally {
            writeLock.unlock();
        }
    }

    /**
     * {@inheritDoc}
     */
    public Object get(String key) {
        readLock.lock();
        try {
            CacheObject co = cacheMap.get(key);
            if (co == null) {
                return null;
            }
            if (co.isExpired()) {
                cacheMap.remove(key);
                return null;
            }

            return co.getObject();
        } finally {
            readLock.unlock();
        }
    }

    public final int eliminate() {
        writeLock.lock();
        try {
            return eliminateCache();
        } finally {
            writeLock.unlock();
        }
    }

    /**
     * 淘汰对象具体实现
     *
     * @return
     */
    protected abstract int eliminateCache();

    public boolean isFull() {
        return cacheSize != 0 && cacheMap.size() >= cacheSize;
    }

    public void evict(String key) {
        writeLock.lock();
        try {
            cacheMap.remove(key);
        } finally {
            writeLock.unlock();
        }
    }

    public void clear() {
        writeLock.lock();
        try {
            cacheMap.clear();
        } finally {
            writeLock.unlock();
        }
    }

    public int size() {
        return cacheMap.size();
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    class CacheObject {
        final String key;
        final Object cachedObject;
        long lastAccess;        // 最后访问时间
        long accessCount;        // 访问次数
        long ttl;                // 对象存活时间(time-to-live)

        CacheObject(String key, Object value, long ttl) {
            this.key = key;
            this.cachedObject = value;
            this.ttl = ttl;
            this.lastAccess = System.currentTimeMillis();
        }

        boolean isExpired() {
            return ttl != 0 && lastAccess + ttl < System.currentTimeMillis();
        }

        Object getObject() {
            lastAccess = System.currentTimeMillis();
            accessCount++;
            return cachedObject;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("CacheObject{");
            sb.append("key='").append(key).append('\'');
            sb.append(", cachedObject=").append(cachedObject);
            sb.append(", lastAccess=").append(lastAccess);
            sb.append(", accessCount=").append(accessCount);
            sb.append(", ttl=").append(ttl);
            sb.append('}');
            return sb.toString();
        }
    }
}

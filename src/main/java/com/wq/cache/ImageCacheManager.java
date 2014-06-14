package com.wq.cache;

/**
 * To change this template use File | Settings | File Templates.
 *
 * @author wangqing
 * @since 1.0.0
 */
public class ImageCacheManager {
    private static volatile ImageCacheManager instance = null;
    private Cache imageCache;

    private ImageCacheManager() {
        //缓存100M数据
        imageCache = new SimpleMemCache(340, 1000 * 60, ExpiryStrategy.LRU);
    }

    /**
     * 获取DefultCacheManager对象
     *
     * @return instance
     */
    public static ImageCacheManager getInstance() {
        if (instance == null) {
            synchronized (ImageCacheManager.class) {
                if (instance == null)
                    instance = new ImageCacheManager();
            }
        }
        return instance;
    }

    public Cache getCache() {
        return imageCache;
    }
}

package com.wq.cache;

/**
 * 缓存
 *
 * @author wangqing
 * @since 1.0.0
 */
public interface Cache {
    /**
     * 返回当前缓存的大小
     *
     * @return
     */
    int size();

    /**
     * 返回默认存活时间
     *
     * @return
     */
    long getDefaultExpire();

    /**
     * 设置缓存，存在则更新，不存在则新增
     *
     * @param key
     * @param value
     */
    void put(String key, Object value);

    /**
     * 向缓存添加value对象,并指定存活时间
     *
     * @param key
     * @param value
     * @param expire 过期时间
     */
    void put(String key, Object value, long expire);

    /**
     * 获取缓存值
     *
     * @param key
     * @return
     */
    Object get(String key);

    /**
     * 根据key移除缓存
     *
     * @param key
     */
    void evict(String key);

    /**
     * 清空所有缓存
     */
    void clear();

    /**
     * 返回缓存大小
     *
     * @return
     */
    int getCacheSize();

    /**
     * 缓存是否已经满
     *
     * @return
     */
    boolean isFull();

    /**
     * 淘汰对象
     *
     * @return 被淘汰对象的大小
     */
    int eliminate();

    /**
     * 缓存中是否为空
     */
    boolean isEmpty();

}

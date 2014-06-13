package com.wq.cache;

/**
 * 缓存策略
 * 当存在热点数据时，LRU的效率很好，但偶发性的、周期性的批量操作会导致LRU命中率急剧下降，缓存污染情况比较严重。
 * LFU效率要优于LRU，且能够避免周期性或者偶发性的操作导致缓存命中率下降的问题。
 * 但LFU需要记录数据的历史访问记录，一旦数据访问模式改变，LFU需要更长时间来适用新的访问模式，
 * 即：LFU存在历史数据影响将来数据的“缓存污染”效用。FIFO虽然实现很简单，但是命中率很低，实际上也很少使用这种算法。
 *
 * @author wangqing
 * @since 1.0.0
 */
public enum ExpiryStrategy {
    LRU/*Least Recently Used ，最近最少使用*/,
    LFU/*Least Frequently Used，最不经常使用*/,
    FIFO/*First In First Out ，先进先出*/
}

package com.wq.service;

/**
 * Created with IntelliJ IDEA.
 * User: wangq
 * Date: 13-5-21
 * Time: 上午11:47
 * To change this template use File | Settings | File Templates.
 */
public interface FetchService {
    /**
     * 抓取网页图片
     *
     * @param url
     * @param dstPath
     * @param imgWidth
     * @param imgHeight
     * @param deep      默认3
     */
    public int fetchUrl(String url, String dstPath, int imgWidth, int imgHeight, int deep, boolean addToMana);
}

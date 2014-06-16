package com.wq.util;

import com.wq.cache.SystemCache;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: wangq
 * Date: 13-5-24
 * Time: 上午9:18
 * To change this template use File | Settings | File Templates.
 */
public class TestJsoup {
    public static void main(String[] args) throws IOException {
        com.wq.model.SysData data = SystemCache.getInstance().getData();
        WebUtil.initProxy("172.16.9.12", 8088, "wangqinga", "super007");
        long a = System.currentTimeMillis();
//        String html = WebUtil.getContent("http://huaban.com/pins/67208446/");
        Document doc = Jsoup.connect("http://car.autohome.com.cn/photo/series/14295/1/1936349.html").get();
//        System.out.println("t="+doc.title());

        Elements links = doc.getElementsByTag("a");
        for (Element link : links) {
            String linkHref =link.absUrl("href");
            String linkText =link.text();
            System.out.println("t=" + linkHref);
        }

        long b = System.currentTimeMillis();
        System.out.println(b - a);
    }
}

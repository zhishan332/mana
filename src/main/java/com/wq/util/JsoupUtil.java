package com.wq.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: wangq
 * Date: 13-5-24
 * Time: 上午10:17
 * To change this template use File | Settings | File Templates.
 */
public class JsoupUtil {

    public static Document getContent(String url) {
        Document doc = null;
        try {
            doc = Jsoup.connect(url).header("User-Agent",
                    "Mozilla/5.0 (Windows; U; Windows NT 5.2) Gecko/2008070208 Firefox/3.0.1").get();
            return doc;
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return doc;
    }

    public static Map<String, String> getUrlTag(Document doc) {
        Map<String, String> map = new HashMap<String, String>();
        Elements links = doc.getElementsByTag("a");
        for (Element link : links) {
            String linkHref = link.absUrl("href").trim();
            String linkText = link.text();
            if (!"".equals(linkHref)) {
                map.put(linkHref, linkText);
            }
        }
        return map;
    }

    public static Set<String> getImgTag(Document doc) {
        Set<String> set = new HashSet<String>();
        Elements links = doc.getElementsByTag("img");
        for (Element link : links) {
            String linkHref = link.absUrl("src").trim();
            if ("".equals(linkHref)) {
                linkHref = link.absUrl("file").trim();
            }
            if (!"".equals(linkHref)) {
                set.add(linkHref);
            }
        }
        return set;
    }
}

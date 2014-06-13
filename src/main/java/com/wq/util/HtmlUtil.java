package com.wq.util;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: wangq
 * Date: 12-8-20
 * Time: 上午9:25
 * To change this template use File | Settings | File Templates.
 */
public class HtmlUtil {
    public static String getHtmlStrByList(List<String> pathList) {
        String html = "<html><head><body>";
        for (String path : pathList) {
            html += "<p><img src=\"file:///" + path + "\" /></p>";
        }
        html += "</head></body></html>";
        return html;
    }

}

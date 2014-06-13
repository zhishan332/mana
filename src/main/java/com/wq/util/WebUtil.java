package com.wq.util;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class WebUtil {
    /**
     * 获取网页编码
     *
     * @param url 页面url地址,需要以 http://开始，例：http://www.pujia.com
     * @return
     * @throws IOException
     */
    public static String getCharset(HttpURLConnection urlConnection, URL url) throws IOException {
        // 网页编码
        String strencoding = null;
        /**
         * 通过解析meta得到网页编码
         */
        // 获取网页源码(英文字符和数字不会乱码，所以可以得到正确<meta/>区域)
        StringBuffer sb = new StringBuffer();
        String line;
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(url
                    .openStream()));
            while ((line = in.readLine()) != null) {
                sb.append(line);
            }
            in.close();
        } catch (Exception e) { // Report any errors that arise
            System.err.println(e);
            System.err
                    .println("Usage:   java   HttpClient   <URL>   [<filename>]");
        }
        String htmlcode = sb.toString();
        // 解析html源码，取出<meta />区域，并取出charset
        String strbegin = "<meta";
        String strend = ">";
        String strtmp;
        int begin = htmlcode.indexOf(strbegin);
        int end = -1;
        int inttmp;
        while (begin > -1) {
            end = htmlcode.substring(begin).indexOf(strend);
            if (begin > -1 && end > -1) {
                strtmp = htmlcode.substring(begin, begin + end).toLowerCase();
                inttmp = strtmp.indexOf("charset");
                if (inttmp > -1) {
                    strencoding = strtmp.substring(inttmp + 7, end).replace(
                            "=", "").replace("/", "").replace("\"", "")
                            .replace("\'", "").replace(" ", "");
                    return strencoding;
                }
            }
            htmlcode = htmlcode.substring(begin);
            begin = htmlcode.indexOf(strbegin);
        }
        return strencoding;
    }

    /**
     * 获取网页内容
     *
     * @param strUrl
     * @return 返回字符串，错误则返回"error open url"
     */

    public static String getContent(String strUrl) {
        HttpURLConnection con = null;
        try {
            URL url = new URL(strUrl);
            con = (HttpURLConnection) url.openConnection();
            con.setConnectTimeout(60000);
            con.setReadTimeout(60000);
            //设置模拟浏览器 不被服务端拒绝
            con.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.2;Trident/4.0; .NET CLR 1.1.4322; .NET CLR 2.0.50727)");
            con.setRequestProperty("Accept", "text/html");
            con.setUseCaches(false);
            con.connect();
            String encoding = con.getContentType();
            if (encoding != null && encoding.indexOf("charset") > 0) {
                encoding = encoding.substring(encoding.indexOf("charset=") + 8).trim();
            } else {
                encoding = getCharset(con, url);
                if (encoding == null) {
                    encoding = "UTF-8";
                }
            }
            StringBuilder sb = new StringBuilder();
            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), encoding));
//            BufferedInputStream in = new BufferedInputStream(url.openStream());
//            byte[] bytes = new byte[1024];
//            int i = 0;
//            int len;
//            while ((len = in.read(bytes)) != -1) {
//                sb.append(new String(bytes, 0, len, encoding));
//                System.out.println(i);
//
//            }
            String s = "";
            while ((s = br.readLine()) != null) {
                sb.append(s);
            }
            br.close();
//            in.close();
            return sb.toString();
        } catch (Exception e) {
            return null;
        } finally {
            if (con != null) {
                con.disconnect();
            }
        }
    }

    /**
     * 设置代理
     *
     * @param host
     * @param port
     * @param username
     * @param password
     */
    public static void initProxy(String host, int port, final String username,
                                 final String password) {
        Authenticator.setDefault(new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username,
                        new String(password).toCharArray());
            }
        });
        System.setProperty("http.proxyType", "4");
        System.setProperty("http.proxyPort", Integer.toString(port));
        System.setProperty("http.proxyHost", host);
        System.setProperty("http.proxySet", "true");
    }

    /**
     * 获取网页名称
     *
     * @param source
     * @return
     */
    public static String getTitle(String source) {
        String reg = "(?<=<title>).*(?=</title>)";
        Matcher m = Pattern.compile(reg, Pattern.MULTILINE | Pattern.DOTALL).matcher(source);
        if (m.find()) {
            return m.group();
        }
        return null;
    }

    /**
     * 获取网页所有A标签
     *
     * @param source
     * @return
     */
    public static Map<String, String> getUrlTag(String source) {
        String reg = "((?<=[\\s+]?href[\\s+]?=[\\s+]?('|\")?)[^\"|'>]+?(?=\"|'))(.+?)?((?<=>)(.+?)?(?=</a>))";
        Matcher m = Pattern.compile(reg, Pattern.CASE_INSENSITIVE | Pattern.DOTALL).matcher(source);
        Map<String, String> map = new HashMap<String, String>();
        while (m.find()) {
            String title = m.group(4);
            String url = m.group(1);
            map.put(url, title);
        }
        return map;
    }

    /**
     * 获取所有图片标签
     *
     * @param src
     * @return
     */
    public static Set<String> getImgTag(String src) {
        Set<String> list = new HashSet<String>();
        Matcher m = Pattern.compile("<img.*?(http:.*?)\"", Pattern.DOTALL).matcher(src);
        while (m.find()) {
            list.add(m.group(1));
        }
        return list;
    }

    public static void main(String[] args) throws IOException {
        String url = "http://car.autohome.com.cn/photo/series/14295/1/1936349.html";
        String proxy = "172.16.9.12";
        int port = 8088;
        String username = "wangqinga";
        String password = "super007";
        String curLine = "";
        String content = "";
        URL server = new URL(url);
        initProxy(proxy, port, username, password);
//        HttpURLConnection connection = (HttpURLConnection) server
//                .openConnection();
//        connection.connect();
//        InputStream is = connection.getInputStream();
//        BufferedReader reader = new BufferedReader(new
//                InputStreamReader(is));
//        while ((curLine = reader.readLine()) != null) {
//            content = content + curLine + "/r/n";
//        }
//        System.out.println("content= " + content);
//        is.close();
        System.out.println(getContent(url));
    }

}


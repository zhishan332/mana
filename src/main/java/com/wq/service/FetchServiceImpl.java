package com.wq.service;

import com.wq.cache.SystemCache;
import com.wq.model.SysData;
import com.wq.util.JsoupUtil;
import com.wq.util.WebUtil;
import org.jsoup.nodes.Document;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: wangq
 * Date: 13-5-21
 * Time: 上午11:50
 * To change this template use File | Settings | File Templates.
 */
public class FetchServiceImpl implements FetchService {
    private static int deepFlag = 1; //深度计数
    private static int fetchNum = 0;
    private SysData data = SystemCache.getInstance().getData();

    @Override
    public int fetchUrl(String url, String dstPath, int imgWidth, int imgHeight, int deep, boolean addToMana) {
        if (data.isUseProxy()) {
            WebUtil.initProxy(data.getProxyHost(), data.getProxyPort(), data.getProxyUserName(), data.getProxyUserPassword());
        }
        deepFlag = 1;
        if (!data.getFileList().contains(dstPath)) {
            ListServiceImpl.getInstance().addTreeData(dstPath);
        }
        Set<String> urls = new HashSet<String>();
        urls.add(url);
        fetch(null, dstPath, url, imgWidth, imgHeight, deep, urls);
        return fetchNum;
    }

    private void fetch(String foldername, String dstPath, String url, int imgWidth, int imgHeight, int deep, Set<String> urls) {
        Document doc = JsoupUtil.getContent(url);
        if (doc == null) return;
        System.out.println(doc.html());
        String title = foldername == null || "".equals(foldername) ? doc.title() : foldername;
        Set<String> imgList = JsoupUtil.getImgTag(doc);
        if (imgList.isEmpty()) return;
        String regEx = "[`~!@#$%^&*()+=|{}':;',//[//].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？\"]";
        dstPath += File.separator + title.trim().replaceAll(regEx, "").replaceAll(" ", "");
        File destFile = new File(dstPath);
        if (!destFile.exists()) {
            destFile.mkdir();
        } else {
            dstPath += "_" + System.currentTimeMillis();
            destFile = new File(dstPath);
            destFile.mkdir();
        }
        for (String imgUrl : imgList) {
            if (downloadImg(imgUrl, dstPath, imgWidth, imgHeight)) fetchNum++;
        }
        File[] ff = destFile.listFiles();
        if (destFile.listFiles() != null && ff.length == 0) {
            destFile.delete();
        }
        deepFlag++;
        if (deepFlag > deep) {
            deepFlag = 1;
            return;
        }
        Map<String, String> urlMap = JsoupUtil.getUrlTag(doc);
        for (Map.Entry<String, String> m : urlMap.entrySet()) {
            if (urls.contains(m.getKey())) continue;
            urls.add(m.getKey());
            fetch(m.getValue(), dstPath, m.getKey(), imgWidth, imgHeight, deepFlag, urls);
        }
    }

    /**
     * 下载图片
     *
     * @param urlString
     * @param baseDir
     */
    private static boolean downloadImg(String urlString, String baseDir, int minWidth, int minHeight) {
        HttpURLConnection httpURLConnection = null;
        InputStream inputStream = null;
        try {
            if (!urlString.startsWith("http")) return false;
            URL url = new URL(urlString);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            inputStream = httpURLConnection.getInputStream();
            BufferedImage bufferedImage = ImageIO.read(inputStream);
            if (bufferedImage == null) return false;
            if (bufferedImage.getWidth() < minWidth || bufferedImage.getHeight() < minHeight) return false;
            String pix = "jpg";
            if (urlString.endsWith("gif") || urlString.endsWith("GIF")) {
                pix = "gif";
            } else if (urlString.endsWith("png") || urlString.endsWith("PNG")) {
                pix = "png";
            } else if (urlString.endsWith("bmp") || urlString.endsWith("BMP")) {
                pix = "bmp";
            }
            File b = new File(baseDir);
            if (!b.exists()) {
                b.mkdir();
            }
            File f = new File(baseDir, System.currentTimeMillis() + "." + pix);
            ImageIO.write(bufferedImage, pix, f);
            return true;
        } catch (Exception e) {
//            e.printStackTrace();
            return false;
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
//                    e.printStackTrace();
                }
            }
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
        }
    }

    public static void main(String[] args) {
        new FetchServiceImpl().fetchUrl("http://huaban.com/pins/67208446/", "D:\\root\\web", 200, 200, 2, false);
    }
}

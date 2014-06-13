package com.wq.util;

import javax.swing.filechooser.FileSystemView;

/**
 * Created with IntelliJ IDEA.
 * User: wangq
 * Date: 13-5-20
 * Time: 下午2:54
 * To change this template use File | Settings | File Templates.
 */
public class SystemUtil {
    /**
     * 返回一个与系统无关的用户路径
     *
     * @return
     */
    public static String getDefaultUserFile() {
        FileSystemView fsv = FileSystemView.getFileSystemView();  //注意了，这里重要的一句
        return fsv.getHomeDirectory().getPath();
    }
}

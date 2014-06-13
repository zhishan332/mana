package com.wq.util;

import java.io.File;
import java.io.FilenameFilter;
import java.util.List;
import java.util.Set;

/**
 * 文件过滤器 过滤指定后缀的文件
 */
public class FileFilter implements FilenameFilter { //实现FilenameFilter接口
    private Set<String> fileExtension;

    public FileFilter(Set<String> fileExtension) {
        this.fileExtension = fileExtension;
    }

    public boolean accept(File dir, String name) {
        File file=new File(dir.getAbsolutePath()+File.separator+name);
        if (file.isDirectory()) {
            return true;
        } else {
            for (String ext : fileExtension) {
                if (name.endsWith("." + ext)) return true;
            }
        }
        return false;
    }

//    public static void main(String[] args) {
//        List<String> fileExtension = new ArrayList<String>();
//        fileExtension.add("zip");
//        File file = new File("D:\\mas\\mms\\mmsAttachmentManager\\2012080720120807");
//        File[] files = file.listFiles(new FileFilter(fileExtension));
//        for (File file1 : files) {
//            System.out.println("res=="+file1.getAbsolutePath());
//        }
//    }
}
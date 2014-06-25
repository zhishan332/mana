package com.wq.tools;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: wangq
 * Date: 14-6-21
 * Time: 下午4:42
 * To change this template use File | Settings | File Templates.
 */
public class Arranger {
    private static final Logger log = LoggerFactory.getLogger(Arranger.class);
    public static final String[] imgExtension = {"jpg", "JPG", "jpeg", "JPEG",
            "tif", "TIF", "bmp", "BMP", "png", "PNG", "gif", "GIF"};
    public static final String[] videoExtension = {"avi", "svi", "divx", "asx",
            "asf", "wmx", "wmv", "wvx", "wm", "wmp", "mpg", "mpe", "mpeg"
            , "ifo", "vob", "m1v", "m2v", "tp", "ts", "trp", "mts", "m2ts"
            , "m2t", "dmb", "mp4", "k3g", "3gp", "skm", "dmskm", "lmp4", "rm",
            "rmvb", "mkv", "ogm", "ogv", "swf", "flv", "mqv", "mov"};

    /**
     * 将多个文件夹的图片整合到一个文件夹
     *
     * @param sourceFolder 源文件夹
     * @param destFolder   目的文件夹
     * @param delete       整合后是否删除源文件夹
     * @param rename       整合时是否按纳秒时间重新命名，保持文件顺序
     * @param extension    整合文件类型
     */
    public void simpleArrange(String sourceFolder, String destFolder, boolean delete, boolean rename, String[] extension) {
        File srcFile = new File(sourceFolder);
        if (!srcFile.isDirectory())
            throw new RuntimeException("SourceFolder is not a directory!");
        File destFile = new File(destFolder);
        if (!destFile.exists()) {
            boolean flag = destFile.mkdir();
            if (!flag)
                throw new RuntimeException("Create destFolder error,please check");
        } else if (!destFile.isDirectory())
            throw new RuntimeException("DestFolder is not a directory!");
        File[] fileList = srcFile.listFiles(new FileFilter(extension));
        for (File file : fileList) {
            if (file.isFile()) {
                try {
                    String newFilename = file.getName();
                    if (rename) {
                        String newName = String.valueOf(System.nanoTime());
                        String ext = FilenameUtils.getExtension(file.getName());
                        newFilename = newName + "." + ext;
                    }
                    File newFile = new File(destFolder + File.separator + newFilename);
                    FileUtils.copyFile(file, newFile);
                    Thread.sleep(1);
                } catch (IOException e) {
                    log.error("copyFileToDirectory error", e);
                } catch (InterruptedException e) {
                    log.error("线程休眠异常", e);
                }
            } else {
                simpleArrange(file.getAbsolutePath(), destFolder, delete, rename, extension);
            }
        }
        if (delete){
            FileUtils.deleteQuietly(srcFile);
        }
        log.info("整合完成："+srcFile.getAbsolutePath());
    }

    class FileFilter implements FilenameFilter {

        private String[] extension;

        public FileFilter() {
        }

        public FileFilter(String[] extension) {
            this.extension = extension;
        }

        @Override
        public boolean accept(File dir, String name) {
            if (extension == null || extension.length == 0) return true;
            File file = new File(dir.getAbsolutePath() + File.separator + name);
            if (file.isDirectory()) {
                return true;
            } else {
                if (FilenameUtils.isExtension(name, extension)) return true;
            }
            return false;
        }

        public String[] getExtension() {
            return extension;
        }

        public void setExtension(String[] extension) {
            this.extension = extension;
        }
    }

    public static void main(String[] args) throws IOException {
        long beg = System.currentTimeMillis();
        Arranger arranger = new Arranger();
        arranger.simpleArrange("F:\\Image\\新建文件夹", "F:\\Image\\meizi\\rosi", true, true, Arranger.imgExtension);
        arranger.simpleArrange("F:\\Image\\baiduyun\\climax", "F:\\Image\\meizi\\climax", true, true, Arranger.imgExtension);
        long end = System.currentTimeMillis();
        System.out.println("耗时：" + (end - beg));
    }
}

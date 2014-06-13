package com.wq.util;

import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created with IntelliJ IDEA.
 * User: wangq
 * Date: 12-6-13
 * Time: 上午9:00
 * 文件写入,文件读出   文件编码全部以UTF-8处理
 */
public class FileUtil {
    private static final String CHARSET = "UTF-8";
    public static ExecutorService pool = Executors.newFixedThreadPool(4);

    /**
     * 写入文件（追加），每行换行
     * 文件最好以TXT形式，以.xls、.doc形式，需修改打开时的编码方式
     *
     * @param filePath 文件路径
     * @param content  写入内容
     */
    public static void appendTxtln(String filePath, String... content) throws IOException {
        PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath, true), CHARSET)));
        for (String s : content) {
            out.println(s);
        }
        out.flush();
        out.close();
    }

    /**
     * 写入文件（追加），不换行
     * 文件最好以TXT形式，以.xls、.doc形式，需修改打开时的编码方式
     *
     * @param filePath 文件路径
     * @param content  写入内容
     */
    public static void appendTxt(String filePath, String... content) throws IOException {
        PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath, true), CHARSET)));
        for (String s : content) {
            out.print(s);
        }
        out.flush();
        out.close();
    }

    /**
     * 写入文件（覆盖），换行
     * 文件最好以TXT形式，以.xls、.doc形式，需修改打开时的编码方式
     *
     * @param filePath 文件路径
     * @param content  写入内容
     */
    public static void writeTxtln(String filePath, String... content) throws IOException {
        PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath), CHARSET)));
        for (String s : content) {
            out.println(s);
        }
        out.flush();
        out.close();
    }

    /**
     * 写入List类型文本（覆盖） 换行写入
     *
     * @param filePath 文件路径
     * @param list     写入内容
     * @throws IOException
     */
    public static void writeTxtln(String filePath, List<String> list) throws IOException {
        PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath), CHARSET)));
        for (String s : list) {
            out.println(s);
        }
        out.flush();
        out.close();
    }

    /**
     * 写入文件（覆盖），不换行
     * 文件最好以TXT形式，以.xls、.doc形式，需修改打开时的编码方式
     *
     * @param filePath 文件路径
     * @param content  写入内容
     */
    public static void writeTxt(String filePath, String... content) throws IOException {
        PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath), CHARSET)));
        for (String s : content) {
            out.print(s);
        }
        out.flush();
        out.close();
    }

    /**
     * 按行读Txt文件
     *
     * @param filePath 文件路径
     * @return 用于存储结果List  该List是有序的
     * @throws java.io.IOException
     */
    public static List<String> readTxt(String filePath) throws IOException {
//        BufferedReader br = new BufferedReader(new FileReader(filePath,CHARSET));
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), CHARSET));
        List list = new LinkedList();
        String r;
        do {
            r = br.readLine();
            if (r != null && !"".equalsIgnoreCase(r)) {
                list.add(r);
            }
        } while (r != null);
        return list;
    }

    /**
     * 删除某行
     *
     * @param filePath 文件路径
     * @param text     删除的文字
     * @param isAll    是否删除所有
     * @throws IOException
     */
    public static void delTxt(String filePath, String text, boolean isAll) throws IOException {
        List<String> list = readTxt(filePath);
        List<String> newList = new LinkedList<String>();
        int flag = 0;
        for (String str : list) {
            if (str.equals(text)) {
                if (!isAll) {
                    flag++;
                }
                if (flag > 1) {
                    newList.add(str);
                }
            } else {
                newList.add(str);
            }
        }
        writeTxtln(filePath, newList);
    }

    /**
     * 按分隔符读Txt文件
     *
     * @param filePath 文件路径
     * @param fix      分割符
     * @return 用于存储结果的数组 该数组是有序的
     * @throws java.io.IOException
     */
    public static String[] readTxtWith(String filePath, String fix) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), CHARSET));
        String str = "";
        String r;
        do {
            r = br.readLine();
            if (r != null && !"".equalsIgnoreCase(r)) {
                str += r;
            }
        } while (r != null);
        String[] res = str.split(fix);
        return res;
    }

    /**
     * 获取文件夹大小
     *
     * @param f
     * @return
     * @throws Exception
     */
    public static long getFileSize(File f)//取得文件夹大小
    {
        long size = 0;
        if (f == null) return size;
        File flist[] = f.listFiles();
        for (int i = 0; i < flist.length; i++) {
            if (flist[i].isDirectory()) {
                size = size + getFileSize(flist[i]);
            } else {
                size = size + flist[i].length();
            }
        }
        return size;
    }

    // 复制文件
    public static void copyFile(File sourceFile, File targetFile) throws IOException {
        BufferedInputStream inBuff = null;
        BufferedOutputStream outBuff = null;
        try {
            // 新建文件输入流并对它进行缓冲
            inBuff = new BufferedInputStream(new FileInputStream(sourceFile));

            // 新建文件输出流并对它进行缓冲
            outBuff = new BufferedOutputStream(new FileOutputStream(targetFile));

            // 缓冲数组
            byte[] b = new byte[1024 * 5];
            int len;
            while ((len = inBuff.read(b)) != -1) {
                outBuff.write(b, 0, len);
            }
            // 刷新此缓冲的输出流
            outBuff.flush();
        } finally {
            // 关闭流
            if (inBuff != null)
                inBuff.close();
            if (outBuff != null)
                outBuff.close();
        }
    }

    // 复制文件夹
    public static void copyDirectiory(String sourceDir, String targetDir) throws IOException {
        // 新建目标目录
        (new File(targetDir)).mkdirs();
        // 获取源文件夹当前下的文件或目录
        File[] file = (new File(sourceDir)).listFiles();
        for (int i = 0; i < file.length; i++) {
            if (file[i].isFile()) {
                // 源文件
                File sourceFile = file[i];
                // 目标文件
                File targetFile = new File(new File(targetDir).getAbsolutePath() + File.separator + file[i].getName());
                copyFile(sourceFile, targetFile);
            }
            if (file[i].isDirectory()) {
                // 准备复制的源文件夹
                String dir1 = sourceDir + "/" + file[i].getName();
                // 准备复制的目标文件夹
                String dir2 = targetDir + "/" + file[i].getName();
                copyDirectiory(dir1, dir2);
            }
        }
    }

    /**
     * 复制文件
     *
     * @param srcFileName
     * @param destFileName
     * @param srcCoding
     * @param destCoding
     * @throws java.io.IOException
     */
    public static void copyFile(File srcFileName, File destFileName, String srcCoding, String destCoding) throws IOException {// 把文件转换为GBK文件
        BufferedReader br = null;
        BufferedWriter bw = null;
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(srcFileName), srcCoding));
            bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(destFileName), destCoding));
            char[] cbuf = new char[1024 * 5];
            int len = cbuf.length;
            int off = 0;
            int ret = 0;
            while ((ret = br.read(cbuf, off, len)) > 0) {
                off += ret;
                len -= ret;
            }
            bw.write(cbuf, 0, off);
            bw.flush();
        } finally {
            if (br != null)
                br.close();
            if (bw != null)
                bw.close();
        }
    }

    /**
     * 删除文件夹
     *
     * @param filepath
     * @throws java.io.IOException
     */
    public static void del(String filepath) {
        File f = new File(filepath);// 定义文件路径
        if (f.exists() && f.isDirectory()) {// 判断是文件还是目录
            if (f.listFiles().length == 0) {// 若目录下没有文件则直接删除
                f.delete();
            } else {// 若有则把文件放进数组，并判断是否有下级目录
                File delFile[] = f.listFiles();
                int i = f.listFiles().length;
                for (int j = 0; j < i; j++) {
                    if (delFile[j].isDirectory()) {
                        del(delFile[j].getAbsolutePath());// 递归调用del方法并取得子目录路径
                    }
                    delFile[j].delete();// 删除文件
                }
            }
        }
    }

    /**
     * Deletes the directory passed in.
     *
     * @param dir Directory to be deleted
     */
    public static void doDeleteEmptyDir(File dir) {
        dir.delete();
    }

    /**
     * Deletes all files and subdirectories under "dir".
     *
     * @param dir Directory to be deleted
     * @return boolean Returns "true" if all deletions were successful.
     *         If a deletion fails, the method stops attempting to
     *         delete and returns "false".
     */
    public static boolean deleteDir(String dir) {
        File ff = new File(dir);
        if (ff.isDirectory()) {
            FileUtil.del(dir);
        }
        return ff.delete();
    }

    /**
     * 压缩文件夹
     *
     * @param sourceDir 待压缩的文件夹路径
     * @param zipFile   压缩后的文件夹路径类似c:\\test.zip
     */
    public static void zip(String sourceDir, String zipFile) {
        OutputStream os;
        try {
            os = new FileOutputStream(zipFile);
            BufferedOutputStream bos = new BufferedOutputStream(os);
            ZipOutputStream zos = new ZipOutputStream(bos);
            File file = new File(sourceDir);
            String basePath = null;
            if (file.isDirectory()) {
                basePath = file.getPath();
            } else {
                basePath = file.getParent();
            }
            zipFile(file, basePath, zos);
            zos.closeEntry();
            zos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void zipFile(File source, String basePath, ZipOutputStream zos) throws IOException {
        File[] files = new File[0];
        if (source.isDirectory()) {
            files = source.listFiles();
        } else {
            files = new File[1];
            files[0] = source;
        }
        String pathName;
        byte[] buf = new byte[1024];
        int length = 0;
        for (File file : files) {
            if (file.isDirectory()) {
                pathName = file.getPath().substring(basePath.length() + 1)
                        + "/";
                zos.putNextEntry(new ZipEntry(pathName));
                zipFile(file, basePath, zos);
            } else {
                pathName = file.getPath().substring(basePath.length() + 1);
                InputStream is = new FileInputStream(file);
                BufferedInputStream bis = new BufferedInputStream(is);
                zos.putNextEntry(new ZipEntry(pathName));
                while ((length = bis.read(buf)) > 0) {
                    zos.write(buf, 0, length);
                }
                is.close();
            }
        }
    }

    /**
     * 获取文件夹下所有文件   递归
     *
     * @param file 文件路径
     */
    public static List<File> getAllFile(File file) {
        List<File> list = new LinkedList<File>();
        File[] files = file.listFiles(); // 获取文件夹下面的所有文件
        for (File f : files) {
            // 判断是否为文件夹
            if (f.isDirectory()) {
                // 如果是文件夹，重新遍历
                getAllFile(f);
            } else {
                // 如果是文件 就打印文件的路径
                list.add(new File(f.getAbsolutePath()));
            }
        }
        return list;
    }

    /**
     * 获取文件夹下所有文件路径 递归
     *
     * @param file 文件路径
     */
    public static List<String> getAllFileName(File file) {
        List<String> list = new LinkedList<String>();
        File[] files = file.listFiles(); // 获取文件夹下面的所有文件
        for (File f : files) {
            // 判断是否为文件夹
            if (f.isDirectory()) {
                // 如果是文件夹，重新遍历
                list.addAll(getAllFileName(f));
            } else {
                // 如果是文件 就打印文件的路径
                list.add(f.getAbsolutePath());
            }
        }
        return list;
    }

    /**
     * 获取文件夹下每个文件夹对应的文件名称
     *
     * @param file     文件夹
     * @param filtList 需要过滤的文件后缀
     * @return
     */
    public static Map<String, List<String>> getFileNameByDic(File file, final Set<String> filtList) {
        final Map<String, List<String>> map = new ConcurrentSkipListMap<String, List<String>>();
        final FileFilter fileFilter = new FileFilter(filtList); //文件的后缀名
        if (!file.isDirectory()) {
            return null;
        }
        File[] files = file.listFiles(fileFilter); // 获取文件夹下面的所有文件
        if (files == null) return null;
//        List<String> list = new LinkedList<String>();
        List<String> list = Collections.synchronizedList(new LinkedList<String>());
        for (final File f : files) {
            // 判断是否为文件夹
            if (f.isDirectory()) {
                // 如果是文件夹，重新遍历
                pool.execute(new Runnable() {
                    public void run() {
                        Map<String, List<String>> temMap = getFileNameByDic(f, filtList);
                        if (temMap != null) {
                            map.putAll(temMap);
                        }
                    }
                });

            } else {
                // 如果是文件 就打印文件的路径
                list.add(f.getAbsolutePath());
            }
        }
        map.put(file.getAbsolutePath(), list);
        return map;
    }

    /**
     * 获取文件后缀
     *
     * @param fileName
     * @return
     */
    public static String getFileType(String fileName) {
        if (fileName.indexOf(".") == -1) return "";
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    public static void main(String[] args) {
        System.out.println(FileUtil.getFileType("QQ截图20120814170459.png"));
    }
}

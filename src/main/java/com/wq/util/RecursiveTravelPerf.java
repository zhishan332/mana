package com.wq.util;

import java.io.File;
import java.io.FilenameFilter;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created with IntelliJ IDEA.
 * User: wangq
 * Date: 12-8-2
 * Time: 上午10:36
 * 快速的文件扫描工具
 */
public class RecursiveTravelPerf {

//    /**
//     * @param args
//     * @throws IOException
//     * @throws InterruptedException
//     */
//    public static void main(String[] args) throws IOException, InterruptedException {
//        RecursiveTravelPerf bean = new RecursiveTravelPerf();
//        bean.scan("E:\\Down", null);
//    }

    /**
     * 获取文件夹下所有的文件
     *
     * @param path   文件夹路径
     * @param filter 过滤器  如果为NULL则不过滤
     * @return
     */
    public Map<String, List<String>> scan(String path, FilenameFilter filter) {
        final File file = new File(path);
        final Processor processor = new Processor(4);
//        final AtomicLong count = new AtomicLong();

//        FileHandler handler = new FileHandler() {
//
//            @Override
//            public void handle(File file) throws IOException {
////                file.getName();
//                System.out.println(file.getName());
//                count.incrementAndGet();
//            }
//        };

//        long begin = System.nanoTime();
        Map<String, List<String>> map = new ConcurrentSkipListMap<String, List<String>>();
        processor.execute(new RecursiveTraveler(file, map, processor, filter));
        try {
            processor.awaitForEnd();
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
//        long end = System.nanoTime();
//        System.out.println("File count     : " + count.getData());
//        System.out.println("Time elapse (s): " + TimeUnit.NANOSECONDS.toSeconds(end - begin));
//        System.out.println("map==" + map);
        return map;
    }
//
//    interface FileHandler {
//        void handle(File file) throws IOException;
//    }

    static class RecursiveTraveler implements Runnable {
        public RecursiveTraveler(File file, Map<String, List<String>> map, Executor executor, FilenameFilter filter) {
            if (!file.isDirectory()) throw new IllegalArgumentException("Invalid Directory: " + file);
            this.file = file;
            this.executor = executor;
            this.map = map;
            this.filter = filter;
        }

        @Override
        public void run() {
            List list = new LinkedList();
            File[] files;
            if (filter == null) {
                files = file.listFiles();
            } else {
                files = file.listFiles(filter);
            }
            for (File sub : files) {
                if (sub.isFile()) list.add(sub.getAbsolutePath());
                else executor.execute(new RecursiveTraveler(sub, map, executor, filter));
            }
            Map<String, List<String>> temMap = new ConcurrentSkipListMap<String, List<String>>();
            temMap.put(file.getAbsolutePath(), list);
            map.putAll(temMap);
        }

        private final Executor executor;
        private final File file;
        private Map<String, List<String>> map;
        private final FilenameFilter filter;
    }

    static class Processor implements Executor {
        public Processor(int core) {
            service = Executors.newFixedThreadPool(core);
            taskStatus = new TaskStatus();
        }

        public void awaitForEnd() throws InterruptedException {
            taskStatus.awaitForAllDone();
            service.shutdownNow();
        }

        @Override
        public void execute(final Runnable command) {
            taskStatus.increment();
            service.execute(new Runnable() {

                @Override
                public void run() {
                    command.run();
                    taskStatus.decrement();
                }
            });
        }

        private final ExecutorService service;

        private final TaskStatus taskStatus;
    }

    static class TaskStatus {
        public void awaitForAllDone() throws InterruptedException {
            synchronized (status) {
                do {
                    status.wait(500);
                } while (status.get() > 0L);
            }
        }

        public void decrement() {
            status.decrementAndGet();
            synchronized (status) {
                status.notifyAll();
            }
        }

        public void increment() {
            status.incrementAndGet();
        }

        private final AtomicLong status = new AtomicLong();

    }
}

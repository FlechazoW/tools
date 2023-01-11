package com.wtz.java.doublebufferqueue;

import lombok.NonNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Stream;

/**
 * @author tiezhu@dtstack.com
 * @since 2022/2/8 星期二
 */
public class _01_Main_2 {

    private static final SimpleDateFormat SIMPLE_DATEFORMAT = new SimpleDateFormat("yyyy-MM-dd");

    public static void main(String[] args) throws InterruptedException {
        final long count = 450 * 10000L;

        //test1(count);// 975
        //test2(count);//1756
        //test3(count);
        //System.out.println(getFileLineNumberFromDir("/Users/wtz/conf_place/hadoop2/op/test"));
        Date date = new Date(System.currentTimeMillis());
        String format = SIMPLE_DATEFORMAT.format(date);
        System.out.println(date);
        System.out.println(Date.valueOf(format));
    }

    public static void test1(long count) throws InterruptedException {
        _01_DoubleBufferQueue_2<Long> queue1 = new _01_DoubleBufferQueue_2<>();

        long l = System.currentTimeMillis();

        Thread thread = new Thread(() -> {
            long i = 0;
            while (i < count) {
                queue1.add(i++);
            }
        });
        thread.start();
        Thread thread1 = new Thread(() -> {
            while (true) {
                Long poll = queue1.poll();
                if (poll != null && poll == count - 1)
                    return;
            }
        });
        thread1.start();

        thread.join();
        thread1.join();

        System.out.println(System.currentTimeMillis() - l);
    }

    public static void test3(long count) throws InterruptedException {
        _01_DoubleBufferQueue<Long> queue1 = new _01_DoubleBufferQueue<>();

        long l = System.currentTimeMillis();

        Thread thread = new Thread(() -> {
            long i = 0;
            while (i < count) {
                queue1.add(i++);
            }
        });
        thread.start();
        Thread thread1 = new Thread(() -> {
            while (true) {
                Long poll = queue1.poll();
                if (poll != null && poll == count - 1)
                    return;
            }
        });
        thread1.start();

        thread.join();
        thread1.join();

        System.out.println(System.currentTimeMillis() - l);
    }

    public static void test2(long count) throws InterruptedException {
        LinkedBlockingQueue<Long> queue1 = new LinkedBlockingQueue<>();

        long l = System.currentTimeMillis();

        Thread thread = new Thread(() -> {
            long i = 0;
            while (i < count) {
                queue1.add(i++);
            }
        });
        thread.start();
        Thread thread1 = new Thread(() -> {
            while (true) {
                Long poll = queue1.poll();
                if (poll != null && poll == count - 1)
                    return;
            }
        });
        thread1.start();

        thread.join();
        thread1.join();

        System.out.println(System.currentTimeMillis() - l);
    }

    /**
     * return the line number of all files in the dirPath
     *
     * @param dirPath dirPath
     * @return
     */
    public static Long getFileLineNumberFromDir(@NonNull String dirPath) {
        File file = new File(dirPath);
        long value;
        if (file.isDirectory()) {
            value = Arrays.stream(file.listFiles()).map(currFile -> {
                if (currFile.isDirectory()) {
                    return getFileLineNumberFromDir(currFile.getPath());
                } else {
                    return getFileLineNumber(currFile.getPath());
                }
            }).mapToLong(Long::longValue).sum();
        } else {
            value = getFileLineNumber(file.getPath());
        }

        return value;
    }

    /**
     * return the line number of file
     *
     * @param filePath The file need be read
     * @return
     */
    public static Long getFileLineNumber(@NonNull String filePath) {
        try (Stream<String> lines = Files.lines(Paths.get(filePath))) {
            return lines.count();
        } catch (IOException e) {
            throw new RuntimeException(String.format("get file[%s] line error", filePath), e);
        }
    }
}

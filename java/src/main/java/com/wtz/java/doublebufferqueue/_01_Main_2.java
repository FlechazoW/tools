package com.wtz.java.doublebufferqueue;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author tiezhu@dtstack.com
 * @since 2022/2/8 星期二
 */
public class _01_Main_2 {
    public static void main(String[] args) throws InterruptedException {
        final long count = 10000 * 100000L;

        test1(count);// 975
        test2(count);//1756
        test3(count);

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
}

package com.wtz.java.doublebufferqueue;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author tiezhu@dtstack.com
 * @since 2022/2/8 星期二
 */
public class _01_Main {
    public static void main(String[] args) throws Exception {
        long[] arrays = new long[]{10000, 10000 * 10, 10000 * 100, 10000 * 1000, 10000 * 10000};
        for (long count : arrays) {
            testQueue(new _01_DoubleBufferQueue<>(), count);
            testQueue(new ArrayBlockingQueue<>((int) count), count);
            testQueue(new LinkedBlockingQueue<>(), count);
            testQueue(new LinkedBlockingDeque<>(), count);
        }
    }

    public static void testQueue(Queue<Long> queue, long count) throws Exception {
        long start = System.currentTimeMillis();
        Thread addThread = new Thread(() -> {
            for (long i = 0; i < count; i++) {
                queue.offer(i);
            }
        });

        Thread pollThread = new Thread(() -> {
            long last = -1;
            boolean lastEle = false;
            while (!lastEle) {
                Long poll = queue.poll();
                if (poll != null) {
                    if (poll - last != 1) {
                        System.out.println("not correct:::" + poll);
                    } else {
                        if (poll == count - 1) {
                            lastEle = true;
                        }
                        last = poll;
                    }
                }
            }
        });
        addThread.start();
        pollThread.start();

        addThread.join();
        pollThread.join();
        System.out.println("count:::" + count + ":::" + queue.getClass().getSimpleName() + ":::elapsed:::" + (System.currentTimeMillis() - start));
    }
}

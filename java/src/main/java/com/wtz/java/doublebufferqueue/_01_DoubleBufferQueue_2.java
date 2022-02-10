package com.wtz.java.doublebufferqueue;

import java.util.LinkedList;
import java.util.List;

/**
 * @author tiezhu@dtstack.com
 * @since 2022/2/8 星期二
 */
public class _01_DoubleBufferQueue_2<T> {

    private volatile boolean direction = true;

    private List<T> listOne = new LinkedList<>();
    private List<T> listTwo = new LinkedList<>();

    private final Object readLock = new Object();
    private final Object writeLock = new Object();

    public void add(T v) {
        synchronized (readLock) {
            if (direction) {
                listTwo.add(v);
            } else {
                listOne.add(v);
            }
        }
    }

    public T poll() {
        synchronized (writeLock) {
            if (direction) {
                return poll(listOne, listTwo);
            } else {
                return poll(listTwo, listOne);
            }
        }
    }

    private T poll(List<T> poll, List<T> put) {
        if (poll.isEmpty()) {
            synchronized (readLock) {
                if (put.isEmpty())
                    return null;
                direction = !direction;
            }
            return put.remove(0);
        } else {
            return poll.remove(0);
        }
    }
}

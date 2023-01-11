package com.wtz.java.doublebufferqueue;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.AbstractQueue;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author tiezhu@dtstack.com
 * @since 2022/2/6 星期日
 * 双缓冲队列
 */
public class _01_DoubleBufferQueue<T> extends AbstractQueue<T> implements Queue<T> {

    private Lock readLock = new ReentrantLock();
    private Lock writeLock = new ReentrantLock();

    private LinkedList<T> readQueue = new LinkedList<>();
    private LinkedList<T> writeQueue = new LinkedList<>();

    public _01_DoubleBufferQueue() {
    }

    public _01_DoubleBufferQueue(boolean fair) {
        readLock = new ReentrantLock(fair);
        writeLock = new ReentrantLock(fair);
    }


    @Override
    public Iterator<T> iterator() {
        throw new NotImplementedException();
    }

    /**
     * 读写队列置换
     */
    private void swap() {
        writeLock.lock();
        try {
            if (!writeQueue.isEmpty()) {
                LinkedList<T> tmpList = readQueue;
                readQueue = writeQueue;
                writeQueue = tmpList;
            }
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public int size() {
        int readSize;
        int writeSize;

        readLock.lock();
        try {
            readSize = readQueue.size();
        } finally {
            readLock.unlock();
        }

        writeLock.lock();
        try {
            writeSize = writeQueue.size();
        } finally {
            writeLock.unlock();
        }
        return writeSize + readSize;
    }

    @Override
    public boolean offer(T t) {
        writeLock.lock();
        try {
            return writeQueue.offer(t);
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public boolean add(T t) {
        writeLock.lock();
        try {
            return writeQueue.add(t);
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        if (null == c) {
            throw new NullPointerException();
        }
        writeLock.lock();
        try {
            return writeQueue.addAll(c);
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public T poll() {
        readLock.lock();
        try {
            if (readQueue.isEmpty()) {
                swap();
            }
            return readQueue.poll();
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public T peek() {
        readLock.lock();
        try {
            if (readQueue.isEmpty()) {
                swap();
            }
            return readQueue.peek();
        } finally {
            readLock.unlock();
        }
    }
}

package com.wtz.java.sink;

import java.util.LinkedList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author tiezhu
 * @date 2022/2/17
 */
public class Switcher {

    private Source source;

    private Sink sink;

    private Lock readLock = new ReentrantLock();

    private Lock writeLock = new ReentrantLock();

    private LinkedList<String> readQueue = new LinkedList<>();

    private LinkedList<String> writeQueue = new LinkedList<>();

    public void register(Source source, Sink sink) {
        this.source = source;
        this.sink = sink;
    }

    public void switchTo(Sink sink) {
        this.sink.stop();
        sink.sink(writeQueue);
        this.sink = sink;
    }

    public void switchTo(Source source) {
        this.source.stop();
        source.source(readQueue);
        this.source = source;
    }

    public void start() {
        source.source(readQueue);
        sink.sink(writeQueue);
    }
}

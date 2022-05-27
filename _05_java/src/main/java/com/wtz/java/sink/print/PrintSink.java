package com.wtz.java.sink.print;

import com.wtz.java.sink.Sink;

import java.util.List;

/**
 * @author tiezhu
 * @date 2022/2/17
 */
public class PrintSink implements Sink {

    private String name;

    private Thread thread;

    public PrintSink(String name) {
        this.name = name;
    }

    @Override
    public void sink(List<String> list) {
        System.out.println("Sink_name: " + name);
        if (thread == null) {
            this.thread = new Thread(() -> {
                while (true) {
                    for (String s : list) {
                        System.out.println("Sink_" + name + " : " + s);
                    }
                    for (int j = 0; j < 1000000; j++) {
                    }
                    System.out.println("Sink_" + name);
                }
            });
            thread.start();
        } else if (thread.isAlive()) {
            thread.notify();
        } else {
            System.out.println("Sink_" + name + " is Dead.");
        }
    }

    @Override
    public void stop() {
        try {
            Thread.sleep(60 * 1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

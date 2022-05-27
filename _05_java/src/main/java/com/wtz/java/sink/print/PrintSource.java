package com.wtz.java.sink.print;

import com.wtz.java.sink.Source;

import java.util.List;

/**
 * @author tiezhu
 * @date 2022/2/17
 */
public class PrintSource implements Source {

    private final String name;

    private Thread thread;

    public PrintSource(String name) {
        this.name = name;

    }

    @Override
    public void source(List<String> list) {
        System.out.println("Source_name: " + name);
        if (thread == null) {
            this.thread = new Thread(() -> {
                int i = 0;
                while (true) {
                    list.add("Source_" + name + "_" + i++);
                    for (int j = 0; j < 1000000; j++) {
                    }
                    System.out.println("Source_" + name + "_" + i++);
                }
            });
            thread.start();
        } else if (thread.isAlive()) {
            thread.notify();
        } else {
            System.out.println("Source_" + name + " is Dead.");
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

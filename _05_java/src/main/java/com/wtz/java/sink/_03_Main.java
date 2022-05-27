package com.wtz.java.sink;

import com.wtz.java.sink.print.PrintSink;
import com.wtz.java.sink.print.PrintSource;

/**
 * @author tiezhu
 * @date 2022/2/17
 */
public class _03_Main {
    public static void main(String[] args) {
        Switcher switcher = new Switcher();
        PrintSource oneSource = new PrintSource("One");
        PrintSource twoSource = new PrintSource("Two");

        PrintSink oneSink = new PrintSink("One");
        PrintSink twoSink = new PrintSink("Two");



        switcher.register(oneSource, oneSink);
        switcher.start();

        sleep(1000);
        switcher.switchTo(twoSource);

        sleep(1000);
        switcher.switchTo(twoSink);
    }

    public static void sleep(long time) {
        for (int i = 0; i < time; i++) {
        }
    }
}

package com.wtz.guava.eventbus;

import com.google.common.eventbus.Subscribe;

public class _01_Observer_One {

    @Subscribe
    public void func(String data) {
        System.out.println("One receive data: " + data);
    }
}

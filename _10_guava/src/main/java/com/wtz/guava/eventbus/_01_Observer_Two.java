package com.wtz.guava.eventbus;

import com.google.common.eventbus.Subscribe;

public class _01_Observer_Two {

    @Subscribe
    public void func(Integer data) {
        System.out.println("Two receive data: " + data);
    }
}

package com.wtz.vertx.eventbus.event;

public interface Event<T> {

    String identity();

    T get();
}

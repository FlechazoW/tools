package com.wtz.guava.eventbus;

import com.google.common.eventbus.EventBus;

public class _01_EventCenter {

    private static final EventBus EVENT_BUS = new EventBus();

    private _01_EventCenter() throws IllegalAccessException {
        throw new IllegalAccessException(getClass() + " can not be instantiated.");
    }

    public static EventBus eventBus() {
        return EVENT_BUS;
    }

    public static void register(Object obj) {
        EVENT_BUS.register(obj);
    }

    public static void unregister(Object obj) {
        EVENT_BUS.unregister(obj);
    }

    public static void post(Object obj) {
        EVENT_BUS.post(obj);
    }
}

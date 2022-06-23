package com.wtz.vertx.eventbus.event;

import java.util.UUID;

public class SendCacheEvent implements Event<String> {
    @Override
    public String identity() {
        return "send-cache-" + UUID.randomUUID();
    }

    @Override
    public String get() {
        return "Cache from " + identity();
    }
}

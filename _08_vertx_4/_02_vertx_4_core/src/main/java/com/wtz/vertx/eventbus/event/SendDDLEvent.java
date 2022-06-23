package com.wtz.vertx.eventbus.event;

import java.util.UUID;

public class SendDDLEvent implements Event<String> {

    @Override
    public String identity() {
        return "send-ddl-" + UUID.randomUUID();
    }

    @Override
    public String get() {
        return "DDL from " + identity();
    }
}

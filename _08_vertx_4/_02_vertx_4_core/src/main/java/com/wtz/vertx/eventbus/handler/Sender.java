package com.wtz.vertx.eventbus.handler;

import com.wtz.vertx.eventbus.event.Event;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Sender extends AbstractVerticle {

    private static final Logger LOG = LoggerFactory.getLogger(Sender.class);

    @Override
    public void start() throws Exception {
        LOG.info("Sender start...");

        EventBus eventBus = vertx.eventBus();

        eventBus.consumer("test-ddl", event -> {
            Event<String> body = (Event<String>) event.body();
            LOG.info("Receive event from " + event.address());
            LOG.info("Event : " + body.get());
        });

        eventBus.consumer("test-cache", event -> {
            Event<String> body = (Event<String>) event.body();
            LOG.info("Receive event from " + event.address());
            LOG.info("Event : " + body.get());
        });
    }

    @Override
    public void stop() throws Exception {
        LOG.info("Sender stop...");
    }
}

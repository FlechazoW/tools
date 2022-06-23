package com.wtz.vertx.eventbus.handler;

import com.wtz.vertx.eventbus.event.DDLSucceedEvent;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Fetcher extends AbstractVerticle {

    private static final Logger LOG = LoggerFactory.getLogger(Fetcher.class);

    @Override
    public void start() throws Exception {
        LOG.info("Fetcher start...");

        EventBus eventBus = vertx.eventBus();

        vertx.setPeriodic(1000, v -> eventBus.publish("test-succeed", new DDLSucceedEvent("tiezhu_test")));
    }

    @Override
    public void stop() throws Exception {
        LOG.info("Fetcher stop...");
    }
}

package com.wtz.vertx.eventbus.handler;

import com.wtz.vertx.eventbus.codec.DefaultEventCodec;
import com.wtz.vertx.eventbus.codec.EventCodec;
import com.wtz.vertx.eventbus.event.BlockEvent;
import com.wtz.vertx.eventbus.event.Event;
import com.wtz.vertx.eventbus.event.SendCacheEvent;
import com.wtz.vertx.eventbus.event.SendDDLEvent;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Cache extends AbstractVerticle {

    private static final Logger LOG = LoggerFactory.getLogger(Cache.class);

    @Override
    public void start() throws Exception {

        LOG.info("Cache start...");

        // get event bus
        EventBus eventBus = vertx.eventBus();

        // register codec for custom message, @see EventBus#registerDefaultCodec or EventBus#registerCodec
        eventBus.registerCodec(new DefaultEventCodec());

        // custom message
        SendDDLEvent ddlEvent = new SendDDLEvent();
        SendCacheEvent cacheEvent = new SendCacheEvent();
        BlockEvent blockEvent = new BlockEvent("tiezhu_test");


        // send a message

        vertx.setPeriodic(1000, v -> eventBus.publish("test-ddl", ddlEvent));
        vertx.setPeriodic(1000, v -> eventBus.publish("test-cache", cacheEvent));
        vertx.setPeriodic(1000, v -> eventBus.publish("test-block", blockEvent));

        eventBus.consumer("test-succeed", event -> {
            Event<String> body = (Event<String>) event.body();
            LOG.info("Receive event from " + event.address());
            LOG.info("Event : " + body.get());
        });
    }

    @Override
    public void stop() throws Exception {
        LOG.info("Cache stop...");
    }
}

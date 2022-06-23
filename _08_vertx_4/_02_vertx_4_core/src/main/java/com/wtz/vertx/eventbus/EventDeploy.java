package com.wtz.vertx.eventbus;

import com.wtz.vertx.eventbus.codec.BlockEventCodec;
import com.wtz.vertx.eventbus.codec.DDLSucceedEventCodec;
import com.wtz.vertx.eventbus.codec.SendCacheEventCodec;
import com.wtz.vertx.eventbus.codec.SendDDLEventCodec;
import com.wtz.vertx.eventbus.event.BlockEvent;
import com.wtz.vertx.eventbus.event.DDLSucceedEvent;
import com.wtz.vertx.eventbus.event.SendCacheEvent;
import com.wtz.vertx.eventbus.event.SendDDLEvent;
import com.wtz.vertx.eventbus.handler.Cache;
import com.wtz.vertx.eventbus.handler.Fetcher;
import com.wtz.vertx.eventbus.handler.Sender;
import com.wtz.vertx.eventbus.handler.Worker;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.EventBusOptions;

public class EventDeploy {
    public static void main(String[] args) {
        VertxOptions vertxOptions = new VertxOptions();
        EventBusOptions eventBusOptions = new EventBusOptions();

        Vertx vertx = Vertx.vertx();

        EventBus eventBus = vertx.eventBus();
        eventBus.registerDefaultCodec(BlockEvent.class, new BlockEventCodec());
        eventBus.registerDefaultCodec(DDLSucceedEvent.class, new DDLSucceedEventCodec());
        eventBus.registerDefaultCodec(SendCacheEvent.class, new SendCacheEventCodec());
        eventBus.registerDefaultCodec(SendDDLEvent.class, new SendDDLEventCodec());

        vertx.deployVerticle(Fetcher.class.getName());
        vertx.deployVerticle(Worker.class.getName());
        vertx.deployVerticle(Sender.class.getName());
        vertx.deployVerticle(Cache.class.getName());

        vertx.setTimer(20 * 1000, v -> vertx.close());
    }
}

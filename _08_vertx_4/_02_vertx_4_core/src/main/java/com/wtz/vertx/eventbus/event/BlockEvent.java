package com.wtz.vertx.eventbus.event;

public class BlockEvent implements Event<String> {

    private final String tableIdentity;

    public BlockEvent(String tableIdentity) {
        this.tableIdentity = tableIdentity;
    }

    @Override
    public String identity() {
        return tableIdentity;
    }

    @Override
    public String get() {
        return "Block event of " + tableIdentity;
    }
}

package com.wtz.vertx.eventbus.event;

public class DDLSucceedEvent implements Event<String> {

    private final String tableIdentity;

    public DDLSucceedEvent(String tableIdentity) {
        this.tableIdentity = tableIdentity;
    }

    @Override
    public String identity() {
        return tableIdentity;
    }

    @Override
    public String get() {
        return "DDL succeed event of " + tableIdentity;
    }
}

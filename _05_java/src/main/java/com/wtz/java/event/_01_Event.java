package com.wtz.java.event;

public class _01_Event {

    private final _01_Person person;

    public _01_Event(_01_Person person) {
        this.person = person;
    }

    public _01_Person report() {
        return person;
    }
}

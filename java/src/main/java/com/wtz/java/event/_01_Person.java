package com.wtz.java.event;

public class _01_Person {

    private _01_PersonListener personListener;

    public void eat() {
        personListener.doEat(new _01_Event(this));
    }

    public void sleep() {
        personListener.doSleep(new _01_Event(this));
    }

    public void register(_01_PersonListener listener) {
        this.personListener = listener;
    }
}

package com.wtz.java.event;

public class _01_Main {
    public static void main(String[] args) {
        _01_Person person = new _01_Person();

        person.register(
                new _01_PersonListener() {
                    @Override
                    public void doEat(_01_Event event) {
                        _01_Person report = event.report();
                        System.out.println(report + ": eating.");
                    }

                    @Override
                    public void doSleep(_01_Event event) {
                        _01_Person report = event.report();
                        System.out.println(report + ": sleeping.");
                    }
                }
        );

        person.eat();

        person.sleep();
    }
}

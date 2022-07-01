package com.wtz.guava.eventbus;

public class _01_EventBus {
    public static void main(String[] args) {
        _01_Observer_One observer_one = new _01_Observer_One();
        _01_Observer_Two observer_two = new _01_Observer_Two();

        _01_EventCenter.register(observer_one);
        _01_EventCenter.register(observer_two);

        System.out.println("============   start  ====================");

        _01_EventCenter.post("aaa");
        _01_EventCenter.post(1);

        System.out.println("============ after unregister ============");
        // 注销observer2
        _01_EventCenter.unregister(observer_two);
        _01_EventCenter.post("post string method");
        _01_EventCenter.post(123);

        System.out.println("============    end           =============");
    }
}

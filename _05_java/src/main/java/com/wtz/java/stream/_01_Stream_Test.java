package com.wtz.java.stream;

import java.util.HashMap;
import java.util.Map;

public class _01_Stream_Test {
    public static void main(String[] args) {
        Map<String, Object> testMap = new HashMap<>();
        testMap.put("a", "b");
        testMap.put("b", "c");
        testMap.put("c", "d");
        testMap.put("d", "e");

        String testStr = "a";

        testMap.entrySet()
                .stream()
                .filter(entry -> !testStr.contains(entry.getKey()))
                .forEach(entry -> System.out.println(entry.getKey() + ": " + entry.getValue()));
    }
}

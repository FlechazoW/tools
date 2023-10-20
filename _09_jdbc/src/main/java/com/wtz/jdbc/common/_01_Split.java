package com.wtz.jdbc.common;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

public class _01_Split {

    public static void main(String[] args) {
        streamTest();
    }

    public static void splitTest() {
        String name = "dbName.tableName";
        String escapeCharacter = "`";
        StringBuilder result = new StringBuilder();
        String[] split = name.split("\\.");
        for (String param : split) {
            result.append(escapeCharacter).append(param).append(escapeCharacter).append(".");
        }

        System.out.println(result);

        String collect = Arrays.stream(split).map(item -> escapeCharacter + item + escapeCharacter).collect(Collectors.joining("."));
        System.out.println(collect);
    }

    public static void streamTest() {
        Map<String, String> parameters = new HashMap<>();
        String url = "jdbc:mysql?useSSL";

        parameters.put("serverTimezone", "Asia/Shanghai");
        parameters.put("useSSL", "false");
        parameters.put("allowPublicKeyRetrieval", "true");

        Properties properties = new Properties();
        parameters.entrySet()
                .stream()
                .filter(entry -> !url.contains(entry.getKey()))
                .forEach(entry -> properties.put(entry.getKey(), entry.getValue()));

        System.out.println(properties);
    }
}

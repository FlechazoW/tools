package com.wtz.flink;

import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.TableResult;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Objects;

public class _02_FlinkSQL {

    public static void main(String[] args) throws Exception {
        // 构建任务执行上下文环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        EnvironmentSettings settings = EnvironmentSettings.newInstance()
                .useBlinkPlanner()
                .inStreamingMode()
                .build();
        StreamTableEnvironment tEnv = StreamTableEnvironment.create(env, settings);

        String create_mysql_table = readFile("test_one_mysql.sql");

        String create_print_table = readFile("print_table.sql");

        String insertSQL = readFile("insert_print.sql");

        tEnv.executeSql(create_mysql_table);
        tEnv.executeSql(create_print_table);
        TableResult tableResult = tEnv.executeSql(insertSQL);
        tableResult.print();

        env.execute();
    }

    private static String readFile(String fileName) throws IOException {
        // Creating an InputStream object
        try (InputStream inputStream =
                     Objects.requireNonNull(
                             _02_FlinkSQL.class.getClassLoader().getResourceAsStream(fileName));
             // creating an InputStreamReader object
             InputStreamReader isReader = new InputStreamReader(inputStream);
             // Creating a BufferedReader object
             BufferedReader reader = new BufferedReader(isReader)) {
            StringBuilder sb = new StringBuilder();
            String str;
            while ((str = reader.readLine()) != null) {
                sb.append(str);
            }

            return sb.toString();
        }
    }
}

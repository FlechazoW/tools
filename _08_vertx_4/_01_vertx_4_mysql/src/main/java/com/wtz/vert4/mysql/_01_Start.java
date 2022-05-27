package com.wtz.vert4.mysql;

import io.vertx.mysqlclient.MySQLConnectOptions;
import io.vertx.mysqlclient.MySQLPool;
import io.vertx.sqlclient.PoolOptions;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;
import io.vertx.sqlclient.SqlClient;
import io.vertx.sqlclient.Tuple;

public class _01_Start {

    private static final String insert = "insert into `tiezhu`.`one` (id, name) values (?, ?);";

    public static void main(String[] args) {
        MySQLConnectOptions options = new MySQLConnectOptions()
                .setPort(3306)
                .setHost("k3")
                .setDatabase("tiezhu")
                .setUser("root")
                .setPassword("admin123")
                .setSsl(false)
                .setConnectTimeout(60 * 1000);

        PoolOptions poolOptions = new PoolOptions().setMaxSize(5);

        SqlClient client = MySQLPool.client(options, poolOptions);

        executeInsert(client);
        executeQuery(client);
    }

    private static void executeQuery(SqlClient client) {
        client.query("SELECT * FROM one LIMIT 5")
                .execute(ar -> {
                    if (ar.succeeded()) {
                        RowSet<Row> result = ar.result();
                        System.out.println("Got " + result.size() + " rows ");
                    } else {
                        System.out.println("Failure: " + ar.cause().getMessage());
                    }
                });
    }

    private static void executeInsert(SqlClient client) {
        client.preparedQuery(insert).execute(Tuple.of(1, "bb"), ar -> {
            if (ar.succeeded()) {
                RowSet<Row> rows = ar.result();
                System.out.println(rows.rowCount());
            } else {
                System.out.println("Failure: " + ar.cause().getMessage());
            }
        });
    }
}

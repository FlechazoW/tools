package com.wtz.inceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class _01_Inceptor_Main {

    private static final Logger LOG = LoggerFactory.getLogger(_01_Inceptor_Main.class);

    private static final String JDBC_DRIVER = "org.apache.hive.jdbc.HiveDriver";
    private static final String INCEPTOR_DRIVER = "io.transwarp.jdbc.InceptorDriver";
    private static final String CONNECTION_URL = "jdbc:hive2://172.16.84.196:10000/tiezhu";
    private static final String DESCRIBE_FORMATTED_TABLE = "DESCRIBE FORMATTED %s";

    public static final String INCEPTOR_TRANSACTION_TYPE = "set transaction.type=inceptor";
    public static final String INCEPTOR_TRANSACTION_BEGIN = "BEGIN TRANSACTION";
    public static final String INCEPTOR_TRANSACTION_COMMIT = "COMMIT";

    static {
        try {
            Class.forName(INCEPTOR_DRIVER);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public static void main(String[] arg) throws Exception {
        Connection con = DriverManager.getConnection(CONNECTION_URL);
        Statement statement = con.createStatement();
        statement.execute(INCEPTOR_TRANSACTION_TYPE);
        statement.execute(INCEPTOR_TRANSACTION_BEGIN);
        statement.execute("use tiezhu");
        try (ResultSet resultSet = statement.executeQuery(String.format(DESCRIBE_FORMATTED_TABLE, "test_two"))) {
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            List<String> columnNames = new ArrayList<>(columnCount);
            for (int i = 0; i < columnCount; i++) {
                String columnName = metaData.getColumnName(i + 1);
                columnNames.add(columnName);
            }
            while (resultSet.next()) {
                Map<String, String> lineData =
                        new HashMap<>(Math.max((int) (columnCount / .75f) + 1, 16));
                for (String columnName : columnNames) {
                    lineData.put(columnName, resultSet.getString(columnName));
                }
                lineData.forEach((key, value) -> {
                    if (value != null && value.contains("Orc")) {
                        System.out.println("contains orc");
                    }
                });
            }
        }

        //PreparedStatement preparedStatement = con.prepareStatement("INSERT INTO test_two PARTITION  ( pt= '20221010' ) (id, name, type) VALUES (?, ?, ?)");
        //try {
        //    for (int i = 0; i < 1000; i++) {
        //        preparedStatement.setInt(1, i);
        //        preparedStatement.setString(2, "vvv" + i);
        //        preparedStatement.setString(3, "v");
        //        //executeSingle(preparedStatement);
        //        executeBatch(preparedStatement, false);
        //    }
        //    executeBatch(preparedStatement, true);
        //} finally {
        //    statement.execute(INCEPTOR_TRANSACTION_COMMIT);
        //}
    }

    private static void executeSingle(PreparedStatement preparedStatement) throws SQLException {
        long start = System.currentTimeMillis();
        preparedStatement.execute();
        long end = System.currentTimeMillis();
        LOG.info("Inceptor execute done. Take time: " + (end - start));
    }

    private static void executeBatch(PreparedStatement preparedStatement, boolean execute) throws SQLException {
        if (execute) {
            long start = System.currentTimeMillis();
            preparedStatement.executeBatch();
            long end = System.currentTimeMillis();
            LOG.info("Inceptor execute batch done. Take time: " + (end - start));
        } else {
            preparedStatement.addBatch();
        }
    }
}
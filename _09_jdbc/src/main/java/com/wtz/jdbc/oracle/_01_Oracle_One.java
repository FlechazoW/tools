package com.wtz.jdbc.oracle;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class _01_Oracle_One {

    public static final Object LOCK_STR = new Object();

    public static void main(String[] args) throws SQLException {
        forName(
                "oracle.jdbc.OracleDriver",
                Thread.currentThread().getContextClassLoader());

        final Connection connection = getConnection();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT \"CUST_CODE_\", \"CUST_NAME_\" FROM \"TIEZHU\".\"CUST_BASIC_INFO\" WHERE  1=1");
        while  (resultSet.next()) {
            System.out.println(resultSet.getString(1));
        }
    }


    private static Connection getConnection() throws SQLException {
        Properties properties = new Properties();
        String jdbcUrl = "jdbc:oracle:thin:@172.16.100.243:1521:ORCL";
        properties.put("user", "oracle");
        properties.put("password", "oracle");
        return DriverManager.getConnection(jdbcUrl, properties);
    }

    public static void forName(String clazz, ClassLoader classLoader) {
        synchronized (LOCK_STR) {
            try {
                Class.forName(clazz, true, classLoader);
                DriverManager.setLoginTimeout(10);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}

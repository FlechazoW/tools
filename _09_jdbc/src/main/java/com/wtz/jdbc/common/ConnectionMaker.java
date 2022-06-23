package com.wtz.jdbc.common;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionMaker {

    private ConnectionMaker() throws IllegalAccessException {
        throw new IllegalAccessException(getClass() + " can not be instantiated.");
    }

    private static final Object LOCK_STR = new Object();

    public static Connection makeConnection(String url, String username, String password, String clazzName) throws SQLException {
        forName(clazzName, Thread.currentThread().getContextClassLoader());
        Properties properties = new Properties();
        properties.put("user", username);
        properties.put("password", password);
        return DriverManager.getConnection(url, properties);
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

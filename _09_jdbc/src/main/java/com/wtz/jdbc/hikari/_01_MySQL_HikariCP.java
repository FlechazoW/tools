package com.wtz.jdbc.hikari;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class _01_MySQL_HikariCP {
    public static void main(String[] args) throws SQLException {
        HikariConfig hikariConfig = new HikariConfig();

        hikariConfig.setJdbcUrl("jdbc:mysql://localhost:3306?useSSL=true");
        hikariConfig.setUsername("root");
        hikariConfig.setPassword("admin123");

        Properties properties = new Properties();

        hikariConfig.setDataSourceProperties(properties);

        HikariDataSource hikariDataSource = new HikariDataSource(hikariConfig);

        try(Connection connection = hikariDataSource.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from wentz._01_Sink")) {
            while (resultSet.next()) {
                System.out.println(resultSet.getObject(1));
                System.out.println(resultSet.getObject(2));
            }

        }
    }
}

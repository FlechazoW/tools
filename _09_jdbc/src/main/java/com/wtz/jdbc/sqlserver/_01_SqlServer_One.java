package com.wtz.jdbc.sqlserver;

import com.wtz.jdbc.common.ConnectionMaker;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class _01_SqlServer_One {
    public static void main(String[] args) throws SQLException {
        Connection connection = ConnectionMaker.makeConnection(
                "jdbc:sqlserver://;serverName=172.16.101.246;databaseName=TestDB;TrustServerCertificate=True",
                "SA",
                "d@efaX4Wp10",
                "com.microsoft.sqlserver.jdbc.SQLServerDriver");

        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("select * from tiezhu.test.one where dt > 0x00000000000007D2;");
        while (resultSet.next()) {
            System.out.println(resultSet.getInt("id"));
        }
    }
}

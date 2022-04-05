package com.example.vavagoinventory.DBconnector;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnection {
    public static Connection connection;
    public Connection getConnection() {
        String dbName = "goinventory";
        String userName = "root";
        String password = "";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/goinventory?serverTimezone=" + "Europe/Bratislava",
                    userName, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connection;

    }
}
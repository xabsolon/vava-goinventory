package com.example.vavagoinventory.DBconnector;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ResourceBundle;

import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

public class DatabaseContextSingleton {
    private static DatabaseContextSingleton single_instance = null;

    private final DSLContext dslContext;

    private DatabaseContextSingleton() throws SQLException {
        ResourceBundle rd = ResourceBundle.getBundle("system");
        String dbUrl = rd.getString("dbUrl");
        String dbUser = rd.getString("dbUser");
        String dbPassword = rd.getString("dbPassword");
        Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
        dslContext = DSL.using(connection, SQLDialect.MYSQL);
    }

    public static DSLContext getContext() {
        return DatabaseContextSingleton.getInstance().getDslContext();
    }

    public static void init() {
        if (single_instance == null) {
            try {
                single_instance = new DatabaseContextSingleton();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static DatabaseContextSingleton getInstance()
    {
        if (single_instance == null) {
            try {
                single_instance = new DatabaseContextSingleton();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return single_instance;
    }

    public DSLContext getDslContext() {
        return dslContext;
    }
}

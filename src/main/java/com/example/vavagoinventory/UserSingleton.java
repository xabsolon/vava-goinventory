package com.example.vavagoinventory;

import org.jooq.codegen.maven.goinventory.tables.records.UsersRecord;

public class UserSingleton {
    private static UserSingleton single_instance = null;

    private UsersRecord user = null;

    public static UserSingleton getInstance()
    {
        if (single_instance == null) {
            single_instance = new UserSingleton();
        }
        return single_instance;
    }

    public void setUser(UsersRecord user) {
        this.user = user;
    }

    public UsersRecord getUser() {
        return this.user;
    }
}

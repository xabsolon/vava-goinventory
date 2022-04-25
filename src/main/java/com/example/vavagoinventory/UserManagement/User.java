package com.example.vavagoinventory.UserManagement;

import com.example.vavagoinventory.Orders.Order;

import java.util.Scanner;

public class User {
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private int id;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    private String username;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    private String password;

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    private String position;

    public User(UserBuilder builder){
        this.id = builder.id;
        this.position = builder.position;
        this.password = builder.password;
        this.username = builder.username;
    }



    public static class UserBuilder {
        private int id;
        private String username;
        private String password;
        private String position;

        public UserBuilder() {
        }

        public User.UserBuilder id(int id) {
            this.id = id;
            return this;
        }

        public User.UserBuilder username(String username) {
            this.username = username;
            return this;
        }

        public User.UserBuilder password(String password) {
            this.password = password;
            return this;
        }

        public User.UserBuilder position(String position) {
            this.position = position;
            return this;
        }


        public User build() {
            return new User(this);
        }
    }
}

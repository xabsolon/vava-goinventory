package com.example.vavagoinventory;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Log { // Log class to log errors and warnings and app events

    public final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    public void login(String username) {
        LOGGER.log(Level.INFO, "User " + username + " logged in");
    }
    public void userLogout(){
        String name = UserSingleton.getInstance().getUser().getName();
        LOGGER.log(Level.INFO,"User named : " + name + " logged out");
    }
    public void ProductCreated(String nameOfProduct){
        String name = UserSingleton.getInstance().getUser().getName();
        LOGGER.log(Level.WARNING, "User " + name + " created:" + nameOfProduct);
    }
    public void ProductDeleted(String nameOfProduct){
        String name = UserSingleton.getInstance().getUser().getName();
        LOGGER.log(Level.WARNING, "User " + name + " deleted:" + nameOfProduct);
    }
    public void ProductEdited(String nameOfProduct){
        String name = UserSingleton.getInstance().getUser().getName();
        LOGGER.log(Level.WARNING, "User " + name + " edited:" + nameOfProduct);
    }
    public void ProductAdded(String nameOfProduct, int quantity){
        String name = UserSingleton.getInstance().getUser().getName();
        LOGGER.log(Level.WARNING, "User " + name + " added:" + nameOfProduct + " with quantity: " + quantity);
    }
    public void OrderCreated( String nameOfOrder , int quantity){
        String name = UserSingleton.getInstance().getUser().getName();
        LOGGER.log(Level.WARNING, "User " + name + " created: " + nameOfOrder + " with quantity: " + quantity);
    }
    public void OrderDeleted(String nameOfOrder, int quantity){
        String name = UserSingleton.getInstance().getUser().getName();
        LOGGER.log(Level.WARNING, "User " + name + " deleted:" + nameOfOrder + " with quantity: " + quantity);
    }
    public void OrderEdited( String nameOfOrder, int quantity){
        String name = UserSingleton.getInstance().getUser().getName();
        LOGGER.log(Level.WARNING, "User " + name + " edited:" + nameOfOrder + " with quantity: " + quantity);
    }
    public void Exceptions(String type, Exception e){
        LOGGER.log(Level.SEVERE, type + ", " + e);
    }
}

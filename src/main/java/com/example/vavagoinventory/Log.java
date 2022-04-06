package com.example.vavagoinventory;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Log { //nezabudnut pridavat ako public metody - {"public Xxxxx()"}
    public final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    public void login(String username) {
        LOGGER.log(Level.INFO, "User " + username + "logged in");
    }
    public void userLogout(String username){
        LOGGER.log(Level.INFO,"User named : " + username + " logged out");
    }
    public void ProductCreated(String username, String nameOfProduct){
        LOGGER.log(Level.WARNING, "User " + username + "created:" + nameOfProduct);
    }
    public void Exceptions(String type, Exception e){
        e.printStackTrace();
        LOGGER.log(Level.SEVERE, type + ", " + e);
    }
}

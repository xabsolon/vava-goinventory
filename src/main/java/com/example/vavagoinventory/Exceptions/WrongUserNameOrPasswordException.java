package com.example.vavagoinventory.Exceptions;

public class WrongUserNameOrPasswordException extends Exception{
    public WrongUserNameOrPasswordException(){

    }
    public WrongUserNameOrPasswordException(String msg){
        super(msg);
    }
    public String toString(){
        return "Wrong username or password exception";
    }
}

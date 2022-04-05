package com.example.vavagoinventory.Employee.Storage;

public class Context {
    private Strategy strategy;

    public Context(Strategy strategy){
        this.strategy = strategy;
    }
    public int execute(int x, double y){
        return strategy.checkProducts(x,y);
    }
}

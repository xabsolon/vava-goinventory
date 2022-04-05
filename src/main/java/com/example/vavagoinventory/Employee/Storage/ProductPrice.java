package com.example.vavagoinventory.Employee.Storage;

public class ProductPrice implements Strategy{
    @Override
    public int checkProducts(int quantity, double sellingPrice) {
        return Integer.parseInt(String.valueOf(sellingPrice));
    }
}

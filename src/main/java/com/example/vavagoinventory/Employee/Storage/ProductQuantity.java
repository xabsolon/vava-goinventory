package com.example.vavagoinventory.Employee.Storage;

public class ProductQuantity implements Strategy{

    @Override
    public int checkProducts(int quantity, double sellingPrice) {
        return quantity;
    }
}

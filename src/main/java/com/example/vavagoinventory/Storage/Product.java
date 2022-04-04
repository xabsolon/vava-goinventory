package com.example.vavagoinventory.Storage;

public class Product {
    private int id;
    private String name;
    private int quantity;
    private double sellingPrice;

    public Product() {
    }

    public Product(int id, String name, int quantity, double sellingPrice) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.sellingPrice = sellingPrice;
    }
    public Product(int id, String name, double sellingPrice,int quantity) {
        this.id = id;
        this.name = name;
        this.sellingPrice = sellingPrice;
        this.quantity = quantity;

    }
    public Product(ProductBuilder builder) {  //using builder instead of setters
        this.id = builder.id;
        this.name = builder.name;
        this.quantity = builder.quantity;
        this.sellingPrice = builder.sellingPrice;
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(double sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public static class ProductBuilder {
        private int id;
        private String name;
        private int quantity;
        private double sellingPrice;

        public ProductBuilder() {

        }

        public ProductBuilder id(int id) {
            this.id = id;
            return this;
        }

        public ProductBuilder name(String name) {
            this.name = name;
            return this;
        }

        public ProductBuilder quantity(int quantity) {
            this.quantity = quantity;
            return this;
        }

        public ProductBuilder sellingPrice(double sellingPrice) {
            this.sellingPrice = sellingPrice;
            return this;
        }

        public Product build() {
            return new Product(this);
        }
    }

}
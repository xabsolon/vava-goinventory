package com.example.vavagoinventory.Orders;

public class Order {
    private int o_id;
    private int p_id;
    private int quantity;
    private String productName;

    public Order() {
        super();
    }

    public Order(OrderBuilder builder) {
        o_id = builder.o_id;
        p_id = builder.p_id;
        quantity = builder.quantity;
        productName = builder.productName;
    }

    public String getProductName() {
        return productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getO_id() {
        return o_id;
    }

    public int getP_id() {
        return p_id;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setO_id(int o_id) {
        this.o_id = o_id;
    }

    public void setP_id(int p_id) {
        this.p_id = p_id;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public static class OrderBuilder {
        private int o_id;
        private int p_id;
        private int quantity;
        private String productName;

        public OrderBuilder() {
        }

        public Order.OrderBuilder o_id(int o_id) {
            this.o_id = o_id;
            return this;
        }

        public Order.OrderBuilder p_id(int p_id) {
            this.p_id = p_id;
            return this;
        }

        public Order.OrderBuilder quantity(int quantity) {
            this.quantity = quantity;
            return this;
        }

        public Order.OrderBuilder productName(String productName) {
            this.productName = productName;
            return this;
        }


        public Order build() {
            return new Order(this);
        }
    }
}

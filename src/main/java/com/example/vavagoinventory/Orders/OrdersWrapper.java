package com.example.vavagoinventory.Orders;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.util.List;

public class OrdersWrapper {
    @JacksonXmlProperty(localName = "Orders")
    private List<Order> orders;

    public OrdersWrapper() {
        super();
    }

    public OrdersWrapper(List<Order> orders) {
        this.orders = orders;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }
}

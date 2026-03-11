package com.javatechie.aws/cicd/example;

public class Order {

    private int id;
    private String name;
    private int quantity;
    private long price;

    public Order(int id, String name, int quantity, long price) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public long getPrice() {
        return price;
    }
}
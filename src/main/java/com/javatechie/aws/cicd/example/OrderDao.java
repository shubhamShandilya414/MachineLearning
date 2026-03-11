package com.javatechie.aws.cicd.example;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class OrderDao {

    public List<Order> getOrders() {
        return java.util.Arrays.asList(
                new Order(101, "Mobile", 1, 300000),
                new Order(58, "Book", 4, 2000),
                new Order(205, "Laptop", 1, 150000),
                new Order(809, "headset", 1, 1799))
                .stream().sorted((o1, o2) -> Integer.compare(o1.getPrice(), o2.getPrice())).collect(Collectors.toList());
    }

    public Order getOrderById(int id) {
        return getOrders().stream()
                .filter(order -> order.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public List<Order> getOrdersByMinPrice(double minPrice, int page, int size) {
        return getOrders().stream()
                .filter(order -> order.getPrice() >= minPrice)
                .skip(page * size)
                .limit(size)
                .collect(Collectors.toList());
    }

    public List<Order> getOrders(int page, int size) {
        return getOrders().stream()
                .skip(page * size)
                .limit(size)
                .collect(Collectors.toList());
    }
}
package com.javatechie.aws.cicd.example;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Repository
public class OrderDao {

    public List<Order> getOrders(int page, int size) {
        // NEW
        return getOrdersStream().skip(page * size).limit(size).collect(Collectors.toList());
    }

    public List<Order> getOrdersByMinPrice(Double minPrice, int page, int size) {
        // NEW
        return getOrdersStream()
                .filter(order -> order.getPrice() >= minPrice)
                .skip(page * size)
                .limit(size)
                .collect(Collectors.toList());
    }

    public Order getOrderById(int id) {
        // NEW
        return getOrdersStream()
                .filter(order -> order.getId() == id)
                .findFirst()
                .orElse(null);
    }

    private Stream<Order> getOrdersStream() {
        return Stream.of(
                new Order(101, "Mobile", 1, 300000),
                new Order(58, "Book", 4, 2000),
                new Order(205, "Laptop", 1, 150000),
                new Order(809, "headset", 1, 1799)
        ).sorted((o1, o2) -> Long.compare(o1.getPrice(), o2.getPrice()));
    }
}
package com.javatechie.aws/cicd/example;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class OrderDao {

    // existing code...

    public Order getOrderById(int id) {
        // NEW
        return orders.stream()
                .filter(order -> order.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public List<Order> getOrdersByMinPrice(double minPrice) {
        // NEW
        return orders.stream()
                .filter(order -> order.getPrice() >= minPrice)
                .sorted(Comparator.comparingDouble(Order::getPrice))
                .collect(Collectors.toList());
    }

    public List<Order> getOrdersPaginated(int page, int size) {
        // NEW
        return orders.stream()
                .sorted(Comparator.comparingDouble(Order::getPrice))
                .skip(page * size)
                .limit(size)
                .collect(Collectors.toList());
    }
}
package com.javatechie.aws.cicd.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class OrderService {

    private final OrderDao orderDao;

    @Autowired
    public OrderService(OrderDao orderDao) {
        this.orderDao = orderDao;
    }

    public List<Order> getOrders() {
        return orderDao.getOrders();
    }

    // NEW: add logic for filtering by minimum price
    public List<Order> getOrdersByMinPrice(int minPrice) {
        return orderDao.getOrders().stream()
                .filter(order -> order.getPrice() >= minPrice)
                .collect(Collectors.toList());
    }

    // NEW: add logic for pagination
    public List<Order> getOrdersPaginated(int pageSize, int pageNumber) {
        return orderDao.getOrders().stream()
                .skip((long) pageNumber * pageSize)
                .limit(pageSize)
                .collect(Collectors.toList());
    }

    // NEW: add logic for order lookup by ID
    public Order getOrderById(int id) {
        return orderDao.getOrders().stream()
                .filter(order -> order.getId() == id)
                .findFirst()
                .orElse(null);
    }
}
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
        return orderDao.getOrders().stream()
                .sorted(Comparator.comparingLong(Order::getPrice))
                .collect(Collectors.toList());
    }

    // NEW
    public Order getOrderById(int id) {
        return orderDao.getOrders().stream()
                .filter(order -> order.getId() == id)
                .findFirst()
                .orElse(null);
    }

    // NEW
    public List<Order> getOrdersWithFiltering(int minPrice, int pageNumber, int pageSize) {
        return orderDao.getOrders().stream()
                .filter(order -> order.getPrice() >= minPrice)
                .skip((long) pageNumber * pageSize)
                .limit(pageSize)
                .collect(Collectors.toList());
    }
}
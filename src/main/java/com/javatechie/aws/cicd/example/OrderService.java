package com.javatechie.aws.cicd.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private OrderDao orderDao;

    public List<Order> getOrders(int minPrice, int page, int size) {
        return orderDao.getOrders().stream()
                .filter(order -> order.getPrice() >= minPrice)
                .sorted((o1, o2) -> o1.getPrice() - o2.getPrice())
                .skip(page * size)
                .limit(size)
                .collect(Collectors.toList());
    }

    public Order getOrderById(int id) {
        return orderDao.getOrders().stream()
                .filter(order -> order.getId() == id)
                .findFirst()
                .orElse(null);
    }
}
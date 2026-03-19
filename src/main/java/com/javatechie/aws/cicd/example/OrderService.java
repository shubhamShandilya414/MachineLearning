package com.javatechie.aws.cicd.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderDao orderDao;

    @Autowired
    public OrderService(OrderDao orderDao) {
        this.orderDao = orderDao;
    }

    public List<Order> getOrders(Integer minPrice, Integer page, Integer size) {
        List<Order> orders = orderDao.getOrders();
        if (minPrice != null) {
            orders = orders.stream().filter(order -> order.getPrice() >= minPrice).collect(Collectors.toList());
        }
        if (page != null && size != null) {
            int start = page * size;
            int end = start + size;
            orders = orders.stream().skip(start).limit(size).collect(Collectors.toList());
        }
        return orders;
    }

    public Order getOrderById(int id) {
        return orderDao.getOrders().stream().filter(order -> order.getId() == id).findFirst().orElse(null);
    }
}
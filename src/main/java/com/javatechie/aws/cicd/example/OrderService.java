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

    public List<Order> getOrders() {
        return orderDao.getOrders().stream()
                .sorted(Comparator.comparingLong(Order::getPrice)) // NEW: sort by price
                .collect(Collectors.toList());
    }

    public Order getOrderById(int id) { // NEW: retrieve order by id
        return orderDao.getOrders().stream()
                .filter(order -> order.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public List<Order> getOrdersByMinPrice(long minPrice) { // NEW: filter orders by min price
        return orderDao.getOrders().stream()
                .filter(order -> order.getPrice() >= minPrice)
                .sorted(Comparator.comparingLong(Order::getPrice))
                .collect(Collectors.toList());
    }

    public List<Order> getOrdersPaginated(int pageNumber, int pageSize) { // NEW: pagination
        List<Order> orders = orderDao.getOrders().stream()
                .sorted(Comparator.comparingLong(Order::getPrice))
                .collect(Collectors.toList());
        int start = (pageNumber - 1) * pageSize;
        int end = Math.min(start + pageSize, orders.size());
        return orders.subList(start, end);
    }
}
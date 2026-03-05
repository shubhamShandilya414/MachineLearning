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

    // NEW: method to retrieve an order by ID
    public Order getOrderById(int id) {
        return orderDao.getOrders().stream()
                .filter(order -> order.getId() == id)
                .findFirst()
                .orElse(null);
    }

    // NEW: method to filter orders by minimum price
    public List<Order> getOrdersByMinPrice(long minPrice) {
        return orderDao.getOrders().stream()
                .filter(order -> order.getPrice() >= minPrice)
                .collect(Collectors.toList());
    }

    // NEW: method to paginate orders
    public List<Order> getOrdersPaginated(int pageNumber, int pageSize) {
        List<Order> orders = orderDao.getOrders();
        int start = (pageNumber - 1) * pageSize;
        int end = Math.min(start + pageSize, orders.size());
        return orders.subList(start, end);
    }
}
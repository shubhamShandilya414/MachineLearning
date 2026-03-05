package com.javatechie.aws.cicd.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    private final OrderDao orderDao;

    @Autowired
    public OrderService(OrderDao orderDao) {
        this.orderDao = orderDao;
    }

    // NEW: Added method to retrieve an order by ID
    public Optional<Order> getOrderById(int id) {
        return orderDao.getOrders().stream()
                .filter(order -> order.getId() == id)
                .findFirst();
    }

    // NEW: Added method to filter orders by minimum price
    public List<Order> getOrdersByMinPrice(int minPrice) {
        return orderDao.getOrders().stream()
                .filter(order -> order.getPrice() >= minPrice)
                .collect(Collectors.toList());
    }

    // NEW: Added method to paginate orders
    public Page<Order> getOrdersPaginated(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return orderDao.getOrders().stream()
                .skip(pageable.getOffset())
                .limit(pageable.getPageSize())
                .collect(Collectors.toList());
    }

    // NEW: Added method to filter and paginate orders
    public Page<Order> getOrdersFilteredAndPaginated(int minPrice, int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return orderDao.getOrders().stream()
                .filter(order -> order.getPrice() >= minPrice)
                .skip(pageable.getOffset())
                .limit(pageable.getPageSize())
                .collect(Collectors.toList());
    }
}
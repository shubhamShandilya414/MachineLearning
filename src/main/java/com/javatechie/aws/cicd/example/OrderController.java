package com.javatechie.aws.cicd.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@SpringBootApplication
@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderDao orderDao;

    @GetMapping
    public List<Order> getOrders(@RequestParam(required = false) Long minPrice, @RequestParam(required = false, defaultValue = "0") int page, @RequestParam(required = false, defaultValue = "10") int size) {
        List<Order> orders = orderDao.getOrders();
        // NEW: Filter orders by minimum price
        if (minPrice != null) {
            orders = orders.stream().filter(order -> order.getPrice() >= minPrice).collect(Collectors.toList());
        }
        // NEW: Paginate orders
        int start = page * size;
        int end = start + size;
        orders = orders.stream().skip(start).limit(size).collect(Collectors.toList());
        return orders;
    }

    // NEW: Endpoint for retrieving order by ID
    @GetMapping("/{id}")
    public Order getOrderById(@PathVariable int id) {
        List<Order> orders = orderDao.getOrders();
        return orders.stream().filter(order -> order.getId() == id).findFirst().orElse(null);
    }
}
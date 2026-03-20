package com.javatechie.aws.cicd.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The main application class for the Order Service.
 * Provides RESTful API endpoints for retrieving orders.
 */
@SpringBootApplication
@RestController
@RequestMapping("/orders")
public class OrderServiceApplication {

    /**
     * The Data Access Object (DAO) for Order entities.
     */
    @Autowired
    private OrderDao orderDao;

    /**
     * Retrieves a list of all orders, sorted by price.
     * 
     * @return a list of Order objects
     */
    @GetMapping
    public List<Order> fetchOrders() {
        return orderDao.getOrders().stream()
                .sorted(Comparator.comparing(Order::getPrice))
                .collect(Collectors.toList());
    }

    /**
     * Retrieves an order by its ID.
     * 
     * @param id the ID of the order to retrieve
     * @return the Order object with the specified ID, or null if not found
     */
    @GetMapping("/{id}")
    public Order fetchOrderById(@PathVariable int id) {
        return orderDao.getOrderById(id);
    }

    /**
     * The main entry point for the application.
     * 
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(OrderServiceApplication.class, args);
    }
}
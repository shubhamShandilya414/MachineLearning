package com.javatechie.aws.cicd.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
     * Supports filtering by minimum price and pagination.
     * 
     * @param minPrice the minimum price to filter by (optional)
     * @param pageNumber the page number to retrieve (optional)
     * @param pageSize the number of orders per page (optional)
     * @return a list of Order objects
     */
    @GetMapping
    public ResponseEntity<List<Order>> fetchOrders(
            @RequestParam(required = false) Long minPrice,
            @RequestParam(required = false) Integer pageNumber,
            @RequestParam(required = false) Integer pageSize) {
        List<Order> orders;
        if (minPrice != null && pageNumber != null && pageSize != null) {
            orders = orderDao.getOrders(minPrice, pageNumber, pageSize);
        } else {
            orders = orderDao.getOrders();
        }
        return new ResponseEntity<>(orders.stream()
                .sorted(Comparator.comparing(Order::getPrice))
                .collect(Collectors.toList()), HttpStatus.OK);
    }

    /**
     * Retrieves an order by its ID.
     * 
     * @param id the ID of the order to retrieve
     * @return the Order object with the specified ID, or null if not found
     */
    @GetMapping("/{id}")
    public ResponseEntity<Order> fetchOrderById(@PathVariable int id) {
        Order order = orderDao.getOrderById(id);
        if (order == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(order, HttpStatus.OK);
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
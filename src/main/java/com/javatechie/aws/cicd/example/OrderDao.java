package com.javatechie.aws.cicd.example;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Data Access Object (DAO) for Order entities.
 * Provides methods for retrieving orders with filtering and pagination.
 */
@Repository
public class OrderDao {

    /**
     * Retrieves a list of all orders.
     * 
     * @return a list of Order objects
     */
    public List<com.javatechie.aws.cicd.example.Order> getOrders() {
        return Stream.of(
                new com.javatechie.aws.cicd.example.Order(101, "Mobile", 1, 300000),
                new com.javatechie.aws.cicd.example.Order(58, "Book", 4, 2000),
                new com.javatechie.aws.cicd.example.Order(205, "Laptop", 1, 150000),
                new com.javatechie.aws.cicd.example.Order(809, "headset", 1, 1799))
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a list of orders filtered by minimum price and paginated.
     * 
     * @param minPrice the minimum price to filter by
     * @param pageNumber the page number to retrieve
     * @param pageSize the number of orders per page
     * @return a list of Order objects
     */
    public List<com.javatechie.aws.cicd.example.Order> getOrders(long minPrice, int pageNumber, int pageSize) {
        List<com.javatechie.aws.cicd.example.Order> orders = getOrders();
        return orders.stream()
                .filter(order -> order.getPrice() >= minPrice)
                .skip((long) pageNumber * pageSize)
                .limit(pageSize)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves an order by its ID.
     * 
     * @param id the ID of the order to retrieve
     * @return the Order object with the specified ID, or null if not found
     */
    public com.javatechie.aws.cicd.example.Order getOrderById(int id) {
        return getOrders().stream()
                .filter(order -> order.getId() == id)
                .findFirst()
                .orElse(null);
    }
}
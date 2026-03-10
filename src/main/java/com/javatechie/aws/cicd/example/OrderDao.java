package com.javatechie.aws.cicd.example;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Repository
@Slf4j
public class OrderDao {

    public List<Order> getOrders() {
        return Stream.of(
                new Order(101, "Mobile", 1, 300000),
                new Order(58, "Book", 4, 2000),
                new Order(205, "Laptop", 1, 150000),
                new Order(809, "headset", 1, 1799))
                .collect(Collectors.toList());
    }

    // NEW: method to retrieve an order by ID
    public Optional<Order> getOrderById(int id) {
        log.info("getOrderById called with id={}", id);
        try {
            return getOrders().stream()
                    .filter(order -> order.getId() == id)
                    .findFirst();
        } catch (Exception e) {
            log.error("Failed to retrieve order id={}", id, e);
            return Optional.empty();
        }
    }

    // NEW: method to filter orders by minimum price
    public List<Order> getOrdersByMinPrice(int minPrice) {
        log.info("getOrdersByMinPrice called with minPrice={}", minPrice);
        try {
            return getOrders().stream()
                    .filter(order -> order.getPrice() >= minPrice)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Failed to retrieve orders with minPrice={}", minPrice, e);
            return List.of();
        }
    }

    // NEW: method to paginate orders
    public List<Order> getOrdersPaginated(int pageNumber, int pageSize) {
        log.info("getOrdersPaginated called with pageNumber={} and pageSize={}", pageNumber, pageSize);
        try {
            if (pageNumber < 0 || pageSize < 1) {
                log.error("Invalid pagination parameters");
                return List.of();
            }
            return getOrders().stream()
                    .skip((long) pageNumber * pageSize)
                    .limit(pageSize)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Failed to retrieve paginated orders", e);
            return List.of();
        }
    }
}
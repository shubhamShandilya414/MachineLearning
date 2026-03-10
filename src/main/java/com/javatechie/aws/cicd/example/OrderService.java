package com.javatechie.aws.cicd.example;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class OrderService {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(OrderService.class);

    private final OrderDao orderDao;

    @Autowired
    public OrderService(OrderDao orderDao) {
        this.orderDao = orderDao;
    }

    // NEW: Added method to retrieve order by ID
    public ResponseEntity<Order> getOrderById(int id) {
        log.info("getOrderById called with id={}", id);
        try {
            return orderDao.getOrderById(id)
                    .map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());
        } catch (Exception e) {
            log.error("Failed to retrieve order id={}", id, e);
            return ResponseEntity.badRequest().build();
        }
    }

    // NEW: Added method to retrieve orders with filtering and pagination
    public ResponseEntity<List<Order>> getOrders(int pageNumber, int pageSize, long minPrice) {
        log.info("getOrders called with pageNumber={}, pageSize={}, minPrice={}", pageNumber, pageSize, minPrice);
        try {
            if (pageNumber < 0 || pageSize < 1) {
                return ResponseEntity.badRequest().build();
            }
            List<Order> orders = orderDao.getOrders()
                    .stream()
                    .filter(order -> order.getPrice() >= minPrice)
                    .skip(pageNumber * pageSize)
                    .limit(pageSize)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(orders);
        } catch (Exception e) {
            log.error("Failed to retrieve orders", e);
            return ResponseEntity.badRequest().build();
        }
    }
}
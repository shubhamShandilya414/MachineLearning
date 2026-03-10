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

    // NEW: Get order by ID
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

    // NEW: Get orders with filtering and pagination
    public ResponseEntity<List<Order>> getOrders(Integer minPrice, Integer page, Integer size) {
        log.info("getOrders called with minPrice={}, page={}, size={}", minPrice, page, size);
        try {
            List<Order> orders = orderDao.getOrders();
            if (minPrice != null) {
                orders = orders.stream()
                        .filter(order -> order.getPrice() >= minPrice)
                        .collect(Collectors.toList());
            }
            if (page != null && size != null) {
                int start = page * size;
                int end = start + size;
                orders = orders.stream()
                        .skip(start)
                        .limit(size)
                        .collect(Collectors.toList());
            }
            return ResponseEntity.ok(orders);
        } catch (Exception e) {
            log.error("Failed to retrieve orders", e);
            return ResponseEntity.badRequest().build();
        }
    }
}
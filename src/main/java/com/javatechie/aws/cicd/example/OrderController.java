package com.javatechie.aws.cicd.example;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
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

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private static final Logger log = Logger.getLogger(OrderController.class.getName());

    @Autowired
    private OrderDao orderDao;

    @GetMapping
    public ResponseEntity<List<Order>> getOrders(@RequestParam(required = false) Integer minPrice,
                                                 @RequestParam(required = false, defaultValue = "0") int page,
                                                 @RequestParam(required = false, defaultValue = "10") int size) {
        log.info("getOrders called with minPrice={}, page={}, size={}", minPrice, page, size);
        try {
            List<Order> orders = orderDao.getOrders();
            if (minPrice != null) {
                orders = orders.stream()
                        .filter(order -> order.getPrice() >= minPrice)
                        .collect(Collectors.toList());
            }
            // NEW: pagination
            int start = page * size;
            int end = start + size;
            orders = orders.subList(start, Math.min(end, orders.size()));
            return ResponseEntity.ok(orders);
        } catch (Exception e) {
            log.error("Failed to retrieve orders", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable int id) {
        log.info("getOrderById called with id={}", id);
        try {
            Order order = orderDao.getOrders().stream()
                    .filter(o -> o.getId() == id)
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Order not found"));
            return ResponseEntity.ok(order);
        } catch (Exception e) {
            log.error("Failed to retrieve order id={}", id, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
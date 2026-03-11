package com.javatechie.aws.cicd.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringBootApplication
@RestController
@RequestMapping("/orders")
public class OrderServiceApplication {

    private static final Logger log = LoggerFactory.getLogger(OrderServiceApplication.class);

    @Autowired
    private OrderDao orderDao;

    @GetMapping
    public ResponseEntity<List<Order>> fetchOrders() {
        log.info("fetchOrders called");
        try {
            List<Order> orders = orderDao.getOrders().stream().
                    sorted(Comparator.comparing(Order::getPrice)).collect(Collectors.toList());
            return ResponseEntity.ok(orders);
        } catch (Exception e) {
            log.error("Failed to retrieve orders", e);
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable int id) {
        log.info("getOrderById called with id={}", id);
        try {
            Optional<Order> order = orderDao.getOrders().stream()
                    .filter(o -> o.getId() == id)
                    .findFirst();
            return order.map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());
        } catch (Exception e) {
            log.error("Failed to retrieve order id={}", id, e);
            return ResponseEntity.badRequest().build();
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(OrderServiceApplication.class, args);
    }
}
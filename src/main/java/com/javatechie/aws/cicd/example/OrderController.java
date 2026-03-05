package com.javatechie.aws.cicd.example;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/orders")
@Slf4j
public class OrderController {

    private final OrderDao orderDao;

    @Autowired
    public OrderController(OrderDao orderDao) {
        this.orderDao = orderDao;
    }

    @GetMapping
    public ResponseEntity<List<Order>> getOrders(@RequestParam(required = false) Integer minPrice, @RequestParam(required = false) Integer pageSize, @RequestParam(required = false) Integer pageNumber) {
        log.info("getOrders called with minPrice={}, pageSize={}, pageNumber={}", minPrice, pageSize, pageNumber);
        try {
            List<Order> orders = orderDao.getOrders();
            if (minPrice != null) {
                orders = orders.stream().filter(order -> order.getPrice() >= minPrice).collect(Collectors.toList());
            }
            if (pageSize != null && pageNumber != null) {
                int start = pageNumber * pageSize;
                int end = Math.min(start + pageSize, orders.size());
                orders = orders.subList(start, end);
            }
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
            Optional<Order> order = orderDao.getOrders().stream().filter(o -> o.getId() == id).findFirst();
            return order.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
        } catch (Exception e) {
            log.error("Failed to retrieve order id={}", id, e);
            return ResponseEntity.badRequest().build();
        }
    }
}